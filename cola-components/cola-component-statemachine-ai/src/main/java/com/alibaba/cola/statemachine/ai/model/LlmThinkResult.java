package com.alibaba.cola.statemachine.ai.model;

import java.util.List;

/**
 * LLM 思考结果 — 驱动下一步状态流转的决策
 *
 * <p>
 * Sealed 设计保证 {@link com.alibaba.cola.statemachine.ai.AiAgentRunner} 的 Action
 * 在编译期穷举所有决策分支。
 *
 * @author xiaowu
 */
public sealed interface LlmThinkResult
    permits LlmThinkResult.DirectAnswer, LlmThinkResult.NeedsTools {

  record DirectAnswer(String answer) implements LlmThinkResult {
  }

  record NeedsTools(List<ToolRequest> toolRequests) implements LlmThinkResult {
    public NeedsTools {
      toolRequests = List.copyOf(toolRequests);
    }
  }

  static DirectAnswer directAnswer(String answer) {
    return new DirectAnswer(answer);
  }

  static NeedsTools needsTools(List<ToolRequest> requests) {
    return new NeedsTools(requests);
  }
}
