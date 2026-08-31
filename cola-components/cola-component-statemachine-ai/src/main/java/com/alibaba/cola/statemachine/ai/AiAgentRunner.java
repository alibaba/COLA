package com.alibaba.cola.statemachine.ai;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.ai.config.AiAgentConfig;
import com.alibaba.cola.statemachine.ai.context.AiAgentContext;
import com.alibaba.cola.statemachine.ai.model.AiAgentEvent;
import com.alibaba.cola.statemachine.ai.model.AiAgentState;
import com.alibaba.cola.statemachine.ai.model.AiToolResult;
import com.alibaba.cola.statemachine.ai.model.LlmThinkResult;
import com.alibaba.cola.statemachine.ai.model.ToolRequest;
import com.alibaba.cola.statemachine.ai.spi.LlmClient;
import com.alibaba.cola.statemachine.ai.spi.ToolExecutor;
import com.alibaba.cola.statemachine.ai.trace.AiTraceLog;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;

/**
 * AI Agent ReAct 状态机编排器 — COLA 原生事件驱动写法
 *
 * 架构关键：事件队列驱动
 *
 * @author xiaowu
 */
public class AiAgentRunner {

    private static final String MACHINE_ID_PREFIX = "ai-react-machine-";
    private final StateMachine<AiAgentState, AiAgentEvent, AiAgentContext> engine;
    private final AiAgentConfig config;
    private final LlmClient llmClient;
    private final ToolExecutor toolExecutor;

    private AiAgentRunner(AiAgentConfig config, LlmClient llmClient, ToolExecutor toolExecutor) {
        this.config = config;
        this.llmClient = llmClient;
        this.toolExecutor = toolExecutor;
        this.engine = buildStateMachine();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构建 COLA 状态机。
     *
     * 所有业务逻辑都在 Action 中，Action 执行完后通过 {@code ctx.enqueueEvent()} 驱动下一步。
     * Runner 的事件循环 {@link #run} 只做"从队列取事件 → fireEvent → 循环"，不含任何业务判断。
     */
    private StateMachine<AiAgentState, AiAgentEvent, AiAgentContext> buildStateMachine() {
        StateMachineBuilder<AiAgentState, AiAgentEvent, AiAgentContext> builder = StateMachineBuilderFactory.create();

        // 1. IDLE -> THINKING
        builder.externalTransition()
                .from(AiAgentState.IDLE)
                .to(AiAgentState.THINKING)
                .on(AiAgentEvent.RECEIVE_PROMPT)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    ctx.incrementRound();
                    // Action 做完推理后，投递下一个事件(不直接调用 fireEvent)
                    // 因为 fireEvent 是同步调用，直接调用会导致当前事件未处理完就进入下一个事件，造成状态混乱和难以追踪的 bug。
                    invokeThinkingAndEnqueueNext(ctx);
                });

        // 2. THINKING -> TOOL_CALLING
        builder.externalTransition()
                .from(AiAgentState.THINKING)
                .to(AiAgentState.TOOL_CALLING)
                .on(AiAgentEvent.NEED_TOOL)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    List<AiToolResult> results = executeToolsConcurrently(
                            ctx.getPendingToolRequests(), ctx.getConfig().toolTimeout());
                    results.forEach(result -> {
                        ctx.addToolResult(result);
                        ctx.getTraceLog().record(new AiTraceLog.TraceEvent.ToolInvoked(
                                Instant.now(), result.toolName(), config.toolTimeout(), result));
                    });
                    ctx.enqueueEvent(AiAgentEvent.TOOL_RESULT);
                });

        // 3. TOOL_CALLING -> OBSERVING
        builder.externalTransition()
                .from(AiAgentState.TOOL_CALLING)
                .to(AiAgentState.OBSERVING)
                .on(AiAgentEvent.TOOL_RESULT)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    ctx.enqueueEvent(AiAgentEvent.THINK_COMPLETE);
                });

        // 4. OBSERVING -> THINKING
        builder.externalTransition()
                .from(AiAgentState.OBSERVING)
                .to(AiAgentState.THINKING)
                .on(AiAgentEvent.THINK_COMPLETE)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    if (ctx.hasExceededMaxRounds()) {
                        if (ctx.getFinalAnswer() == null) {
                            ctx.setFinalAnswer("[Degraded] Exceeded max rounds. " +
                                    "Tool results: " + ctx.getToolResults().stream()
                                            .map(AiToolResult::summary).toList());
                        }
                        ctx.enqueueEvent(AiAgentEvent.DIRECT_ANSWER);
                    } else {
                        ctx.incrementRound();
                        invokeThinkingAndEnqueueNext(ctx);
                    }
                });

        // 5. THINKING -> RESPONDING
        builder.externalTransition()
                .from(AiAgentState.THINKING)
                .to(AiAgentState.RESPONDING)
                .on(AiAgentEvent.DIRECT_ANSWER)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    ctx.enqueueEvent(AiAgentEvent.GENERATE_COMPLETE);
                });

        // 6. RESPONDING -> COMPLETED
        builder.externalTransition()
                .from(AiAgentState.RESPONDING)
                .to(AiAgentState.COMPLETED)
                .on(AiAgentEvent.GENERATE_COMPLETE)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                });

        // 7. 任意活跃状态 -> FAILED：执行器异常
        builder.externalTransitions()
                .fromAmong(AiAgentState.IDLE, AiAgentState.THINKING,
                        AiAgentState.TOOL_CALLING, AiAgentState.OBSERVING,
                        AiAgentState.RESPONDING)
                .to(AiAgentState.FAILED)
                .on(AiAgentEvent.ERROR)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    // FAILED 后不自动投递事件，等待外部 RETRY 或 RESET
                });
        // 8. 任意活跃状态 -> FAILED：超时处理
        builder.externalTransitions()
                .fromAmong(AiAgentState.IDLE, AiAgentState.THINKING,
                        AiAgentState.TOOL_CALLING, AiAgentState.OBSERVING,
                        AiAgentState.RESPONDING)
                .to(AiAgentState.FAILED)
                .on(AiAgentEvent.TIMEOUT)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                    ctx.getTraceLog().record(new AiTraceLog.TraceEvent.ErrorOccurred(
                            Instant.now(), from, new RuntimeException("Timeout")));
                });

        // 9. FAILED -> THINKING：重试（回到决策节点）
        builder.externalTransition()
                .from(AiAgentState.FAILED)
                .to(AiAgentState.THINKING)
                .on(AiAgentEvent.RETRY)
                .perform((from, to, event, ctx) -> {
                    ctx.incrementRetry();
                    ctx.recordStateTransition(from, to, event);
                    ctx.getTraceLog().record(new AiTraceLog.TraceEvent.RetryAttempted(
                            Instant.now(), from, ctx.getRetryCount(), "Manual retry"));
                    invokeThinkingAndEnqueueNext(ctx);
                });

        // 10. FAILED -> IDLE：完全重置
        builder.externalTransition()
                .from(AiAgentState.FAILED)
                .to(AiAgentState.IDLE)
                .on(AiAgentEvent.RESET)
                .perform((from, to, event, ctx) -> {
                    ctx.recordStateTransition(from, to, event);
                });

        return builder.build(MACHINE_ID_PREFIX + UUID.randomUUID());
    }

    /**
     * 执行完整的 ReAct 推理循环并返回最终答案。
     *
     * <p>
     * 事件循环的精髓：
     * <ol>
     * <li>将首个事件 {@code RECEIVE_PROMPT} 入队</li>
     * <li>从队列取事件，调用 {@code engine.fireEvent()} — COLA 验证转换合法性并执行对应 Action</li>
     * <li>Action 内部完成业务后，再通过 {@code ctx.enqueueEvent()} 投递下一事件</li>
     * <li>直到队列为空或到达终止态（COMPLETED / FAILED）</li>
     * </ol>
     *
     * <p>
     * 使用 {@link ScopedValue#runWhere} 将 ctx 绑定到调用栈，
     * 并发工具调用所在的虚拟线程自动继承此绑定，无需手动传递。
     *
     */
    public String run(String prompt) {
        AiAgentContext ctx = new AiAgentContext(prompt, config);
        ScopedValue.where(AiAgentContext.CURRENT, ctx).run(() -> {
            AiAgentState currentState = AiAgentState.IDLE;
            ctx.enqueueEvent(AiAgentEvent.RECEIVE_PROMPT);
            while (ctx.hasPendingEvent()) {
                AiAgentEvent nextEvent = ctx.pollEvent();
                try {
                    currentState = engine.fireEvent(currentState, nextEvent, ctx);
                } catch (Exception e) {
                    // 未预期异常：投递 ERROR 事件触发状态机的容错分支
                    ctx.getTraceLog().record(new AiTraceLog.TraceEvent.ErrorOccurred(
                            Instant.now(), currentState, e));
                    ctx.enqueueEvent(AiAgentEvent.ERROR);
                }
                if (currentState == AiAgentState.COMPLETED ||
                        (currentState == AiAgentState.FAILED && ctx.getRetryCount() >= config.maxRetries())) {
                    break;
                }
            }
        });

        return ctx.getFinalAnswer() != null
                ? ctx.getFinalAnswer()
                : "[FAILED] No answer generated. Check traceLog for details.";
    }

    /**
     * 执行并返回带有完整追踪信息的结果。
     */
    public RunResult runWithTrace(String prompt) {
        AiAgentContext ctx = new AiAgentContext(prompt, config);

        ScopedValue.where(AiAgentContext.CURRENT, ctx).run(() -> {
            AiAgentState currentState = AiAgentState.IDLE;
            ctx.enqueueEvent(AiAgentEvent.RECEIVE_PROMPT);

            while (ctx.hasPendingEvent()) {
                AiAgentEvent nextEvent = ctx.pollEvent();
                try {
                    currentState = engine.fireEvent(currentState, nextEvent, ctx);
                } catch (Exception e) {
                    ctx.getTraceLog().record(new AiTraceLog.TraceEvent.ErrorOccurred(
                            Instant.now(), currentState, e));
                    ctx.enqueueEvent(AiAgentEvent.ERROR);
                }
                if (currentState == AiAgentState.COMPLETED ||
                        (currentState == AiAgentState.FAILED && ctx.hasExceededMaxRetries())) {
                    break;
                }
            }
        });

        return new RunResult(
                ctx.getFinalAnswer(),
                ctx.getTraceLog(),
                ctx.getCurrentRound(),
                ctx.getToolResults());
    }

    /**
     * 运行结果 Record，含追踪信息
     */
    public record RunResult(
            String finalAnswer,
            AiTraceLog traceLog,
            int totalRounds,
            List<AiToolResult> toolResults) {
    }

    /**
     * 调用 LLM 推理，并根据结果向队列投递下一事件。
     *
     * 注意：此方法在 COLA Action 内部执行，不可直接调用 {@code engine.fireEvent()}，
     * 只能通过 {@code ctx.enqueueEvent()} 投递。
     */
    private void invokeThinkingAndEnqueueNext(AiAgentContext ctx) {
        switch (llmClient.think(ctx)) {
            case LlmThinkResult.NeedsTools(var requests) -> {
                ctx.setPendingToolRequests(requests);
                ctx.enqueueEvent(AiAgentEvent.NEED_TOOL);
            }
            case LlmThinkResult.DirectAnswer(var answer) -> {
                ctx.setFinalAnswer(answer);
                ctx.enqueueEvent(AiAgentEvent.DIRECT_ANSWER);
            }
        }
    }

    private List<AiToolResult> executeToolsConcurrently(
            List<ToolRequest> requests, Duration timeout) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<AiToolResult>> tasks = requests.stream()
                    .<Callable<AiToolResult>>map(req -> () -> safeInvokeTool(req))
                    .toList();

            // invokeAll：并发提交，统一等待，超时取消
            List<Future<AiToolResult>> futures = executor.invokeAll(tasks, timeout.toMillis(), TimeUnit.MILLISECONDS);

            List<AiToolResult> results = new ArrayList<>(requests.size());
            for (int i = 0; i < futures.size(); i++) {
                Future<AiToolResult> f = futures.get(i);
                if (f.isCancelled()) {
                    // 超时取消 → Timeout 结果
                    results.add(AiToolResult.timeout(requests.get(i).toolName(), timeout));
                } else {
                    try {
                        results.add(f.get());
                    } catch (ExecutionException e) {
                        // safeInvokeTool 已捕获异常，这里理论上不会触发
                        // results.add(AiToolResult.failure(
                        // requests.get(i).toolName(), e.getCause().getMessage(), e.getCause()));
                    }
                }
            }
            return results;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 显式类型参数：sealed record 子类型需要明确 List<AiToolResult> 而非 List<Timeout>
            return requests.stream()
                    .<AiToolResult>map(r -> AiToolResult.timeout(r.toolName(), timeout))
                    .toList();
        }
    }

    /**
     * 单个工具调用的防御性包装（异常 → {@link AiToolResult.Failure}）
     */
    private AiToolResult safeInvokeTool(ToolRequest req) {
        try {
            return toolExecutor.execute(req);
        } catch (Exception e) {
            return AiToolResult.failure(req.toolName(), e.getMessage(), e);
        }
    }

    /** 输出PlantUML */
    public String generateDiagram() {
        return engine.generatePlantUML();
    }

    public StateMachine<AiAgentState, AiAgentEvent, AiAgentContext> getEngine() {
        return engine;
    }

    public static class Builder {
        private AiAgentConfig config = AiAgentConfig.defaults();
        private LlmClient llmClient;
        private ToolExecutor toolExecutor;

        public Builder config(AiAgentConfig config) {
            this.config = config;
            return this;
        }

        public Builder llmClient(LlmClient llmClient) {
            this.llmClient = llmClient;
            return this;
        }

        public Builder toolExecutor(ToolExecutor toolExecutor) {
            this.toolExecutor = toolExecutor;
            return this;
        }

        public AiAgentRunner build() {
            if (llmClient == null)
                throw new IllegalStateException("llmClient is required");
            if (toolExecutor == null) {
                toolExecutor = req -> AiToolResult.failure(req.toolName(), "No ToolExecutor configured");
            }
            return new AiAgentRunner(config, llmClient, toolExecutor);
        }
    }
}
