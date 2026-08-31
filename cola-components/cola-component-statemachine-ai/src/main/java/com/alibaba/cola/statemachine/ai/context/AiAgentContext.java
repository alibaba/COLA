package com.alibaba.cola.statemachine.ai.context;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.alibaba.cola.statemachine.ai.config.AiAgentConfig;
import com.alibaba.cola.statemachine.ai.model.AiAgentEvent;
import com.alibaba.cola.statemachine.ai.model.AiAgentState;
import com.alibaba.cola.statemachine.ai.model.AiToolResult;
import com.alibaba.cola.statemachine.ai.model.ToolRequest;
import com.alibaba.cola.statemachine.ai.trace.AiTraceLog;

/**
 * AI Agent ReAct 上下文
 *
 * 其实当初在设计这部分的时候,我也考虑过完全无状态化的设计（即所有状态都通过事件参数传递），但后来觉得这样反而更麻烦，毕竟 Agent
 * 的状态比较多，且工具调用结果等数据结构也比较复杂，如果都放在事件参数里传来传去，反而不如集中在一个上下文对象里管理清晰。
 * 所以就有了这个AiAgentContext，作为整个 ReAct
 * 循环的共享上下文，包含了当前状态、工具调用结果、事件队列等信息。虽然它是可变的，但由于事件循环是单线程串行执行的，所以不需要担心并发问题。
 * 这里的设计原理其实也就是只让Action负责写入，而把状态管理和事件流转交给Runner事件内进行循环消费
 * 这样就避免了Action之间的耦合和重入调用的问题。
 *
 * @author xiaowu
 */
public class AiAgentContext {

  public static final ScopedValue<AiAgentContext> CURRENT = ScopedValue.newInstance();
  private final String sessionId;
  private final String prompt;
  private final AiAgentConfig config;
  private final AiTraceLog traceLog = new AiTraceLog();
  private final Instant startTime = Instant.now();

  private int currentRound = 0;
  private int retryCount = 0;
  private String finalAnswer;
  private AiAgentState lastNonFailedState = AiAgentState.IDLE;

  private final List<AiToolResult> toolResults = new ArrayList<>();

  private List<ToolRequest> pendingToolRequests = List.of();

  private final Deque<AiAgentEvent> pendingEvents = new ArrayDeque<>();

  public AiAgentContext(String prompt, AiAgentConfig config) {
    this.sessionId = "ai-session-" + System.nanoTime();
    this.prompt = prompt;
    this.config = config;
  }

  /** Action 完成业务逻辑后，通过此方法投递下一步事件 */
  public void enqueueEvent(AiAgentEvent event) {
    pendingEvents.offer(event);
  }

  /** Runner 事件循环从队列头取下一个事件 */
  public AiAgentEvent pollEvent() {
    return pendingEvents.poll();
  }

  /** 是否还有待处理的事件 */
  public boolean hasPendingEvent() {
    return !pendingEvents.isEmpty();
  }

  public void recordStateTransition(AiAgentState from, AiAgentState to, AiAgentEvent event) {
    traceLog.record(new AiTraceLog.TraceEvent.StateChanged(Instant.now(), from, to, event));
    if (to != AiAgentState.FAILED) {
      this.lastNonFailedState = to;
    }
  }

  public void addToolResult(AiToolResult result) {
    toolResults.add(result);
  }

  public void setPendingToolRequests(List<ToolRequest> requests) {
    this.pendingToolRequests = List.copyOf(requests);
  }

  public void setFinalAnswer(String answer) {
    this.finalAnswer = answer;
  }

  public void incrementRound() {
    this.currentRound++;
  }

  public void incrementRetry() {
    this.retryCount++;
  }

  public boolean hasExceededMaxRounds() {
    return currentRound >= config.maxRounds();
  }

  public boolean hasExceededMaxRetries() {
    return retryCount >= config.maxRetries();
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getPrompt() {
    return prompt;
  }

  public AiAgentConfig getConfig() {
    return config;
  }

  public AiTraceLog getTraceLog() {
    return traceLog;
  }

  public String getFinalAnswer() {
    return finalAnswer;
  }

  public int getCurrentRound() {
    return currentRound;
  }

  public int getRetryCount() {
    return retryCount;
  }

  public List<AiToolResult> getToolResults() {
    return List.copyOf(toolResults);
  }

  public List<ToolRequest> getPendingToolRequests() {
    return pendingToolRequests;
  }

  public AiAgentState getLastNonFailedState() {
    return lastNonFailedState;
  }

  public Instant getStartTime() {
    return startTime;
  }
}
