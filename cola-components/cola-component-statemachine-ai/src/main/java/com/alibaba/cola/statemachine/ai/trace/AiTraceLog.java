package com.alibaba.cola.statemachine.ai.trace;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.alibaba.cola.statemachine.ai.model.AiAgentEvent;
import com.alibaba.cola.statemachine.ai.model.AiAgentState;
import com.alibaba.cola.statemachine.ai.model.AiToolResult;

/**
 * AI Agent 全链路追踪日志
 *
 * @author xiaowu
 * @date 2026-02-27 15:30 PM
 */
public class AiTraceLog {

  /**
   * 追踪事件：4 种子类型，编译期穷举
   */
  public sealed interface TraceEvent
      permits TraceEvent.StateChanged, TraceEvent.ToolInvoked,
      TraceEvent.RetryAttempted, TraceEvent.ErrorOccurred {

    Instant timestamp();

    /** 状态变化事件 */
    record StateChanged(
        Instant timestamp,
        AiAgentState from,
        AiAgentState to,
        AiAgentEvent event) implements TraceEvent {
    }

    /** 工具调用事件 */
    record ToolInvoked(
        Instant timestamp,
        String toolName,
        Duration duration,
        AiToolResult result) implements TraceEvent {
    }

    /** 重试事件 */
    record RetryAttempted(
        Instant timestamp,
        AiAgentState state,
        int attempt,
        String reason) implements TraceEvent {
    }

    /** 错误事件 */
    record ErrorOccurred(
        Instant timestamp,
        AiAgentState state,
        Throwable cause) implements TraceEvent {
    }
  }

  private final List<TraceEvent> events = new CopyOnWriteArrayList<>();

  public void record(TraceEvent event) {
    events.add(event);
  }

  public List<TraceEvent> getEvents() {
    return List.copyOf(events);
  }

  public Map<String, Duration> getToolDurationStats() {
    return events.stream()
        .filter(e -> e instanceof TraceEvent.ToolInvoked)
        .map(e -> (TraceEvent.ToolInvoked) e)
        .collect(Collectors.groupingBy(
            TraceEvent.ToolInvoked::toolName,
            Collectors.collectingAndThen(
                Collectors.summingLong(e -> e.duration().toMillis()),
                Duration::ofMillis)));
  }

  public Map<String, Long> getStateTransitionCount() {
    return events.stream()
        .filter(e -> e instanceof TraceEvent.StateChanged)
        .map(e -> (TraceEvent.StateChanged) e)
        .collect(Collectors.groupingBy(
            e -> e.from() + " → " + e.to(),
            Collectors.counting()));
  }

  public double getToolSuccessRate() {
    long total = events.stream().filter(e -> e instanceof TraceEvent.ToolInvoked).count();
    if (total == 0)
      return 1.0;
    long success = events.stream()
        .filter(e -> e instanceof TraceEvent.ToolInvoked ti && ti.result().isSuccess())
        .count();
    return (double) success / total;
  }

  /**
   * 将完整追踪渲染为可读字符串，每行一个事件
   */
  public String formatTrace() {
    return events.stream()
        .map(this::formatEvent)
        .collect(Collectors.joining("\n"));
  }

  private String formatEvent(TraceEvent e) {
    return switch (e) {
      case TraceEvent.StateChanged(var ts, var from, var to, var evt) ->
        String.format("[%s] STATE  %s -[%s]→ %s", ts, from, evt, to);

      case TraceEvent.ToolInvoked(var ts, var name, var dur, var result) ->
        String.format("[%s] TOOL   %s (%dms): %s",
            ts, name, dur.toMillis(), result.summary());

      case TraceEvent.RetryAttempted(var ts, var state, var attempt, var reason) ->
        String.format("[%s] RETRY  #%d at %s — %s", ts, attempt, state, reason);

      case TraceEvent.ErrorOccurred(var ts, var state, var cause) ->
        String.format("[%s] ERROR  at %s — %s", ts, state,
            cause != null ? cause.getMessage() : "unknown");
    };
  }
}
