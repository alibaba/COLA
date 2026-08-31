/**
 * COLA 状态机 AI Agent 扩展包
 *
 * <h2>定位</h2>
 * <p>
 * 本包是基于 {@code cola-component-statemachine} 实践扩展，
 * <h2>包结构</h2>
 * <ul>
 * <li>{@code ai} —
 * 顶层：{@link com.alibaba.cola.statemachine.ai.AiAgentRunner}（主入口）</li>
 * <li>{@code ai.model} — 领域模型：State/Event/Result，纯数据类型，无外部依赖</li>
 * <li>{@code ai.context}—
 * 运行时上下文：{@link com.alibaba.cola.statemachine.ai.context.AiAgentContext}</li>
 * <li>{@code ai.trace} —
 * 可观测性：{@link com.alibaba.cola.statemachine.ai.trace.AiTraceLog}</li>
 * <li>{@code ai.spi} —
 * 扩展点：{@link com.alibaba.cola.statemachine.ai.spi.LlmClient} /
 * {@link com.alibaba.cola.statemachine.ai.spi.ToolExecutor}</li>
 * <li>{@code ai.config} —
 * 配置：{@link com.alibaba.cola.statemachine.ai.config.AiAgentConfig}</li>
 * </ul>
 *
 * <h2>快速入门</h2>
 *
 * <pre>{@code
 * // 1. 实现 LLM 接口（接入你的 LLM SDK）
 * LlmClient myLlm = ctx -> {
 *   if (ctx.getToolResults().isEmpty()) {
 *     return LlmThinkResult.needsTools(List.of(
 *         new ToolRequest("search", ctx.getPrompt())));
 *   }
 *   return LlmThinkResult.directAnswer("Final answer based on: " +
 *       ctx.getToolResults().get(0).summary());
 * };
 *
 * // 2. 实现工具执行接口
 * ToolExecutor myTool = req -> AiToolResult.success(req.toolName(), "search result");
 *
 * // 3. 构建 Runner 并运行
 * AiAgentRunner runner = AiAgentRunner.builder()
 *     .config(AiAgentConfig.defaults())
 *     .llmClient(myLlm)
 *     .toolExecutor(myTool)
 *     .build();
 *
 * String answer = runner.run("What is the capital of France?");
 * }</pre>
 *
 * @author xiaowu
 * @version 2.0
 */
package com.alibaba.cola.statemachine.ai;
