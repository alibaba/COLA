package com.alibaba.cola.statemachine.ai.spi;

import com.alibaba.cola.statemachine.ai.context.AiAgentContext;
import com.alibaba.cola.statemachine.ai.model.LlmThinkResult;

/**
 * LLM 客户端接口 — 可插拔设计
 *
 * <p>
 * 用户只需实现此接口，即可接入任意 LLM（OpenAI、Claude、通义千问等）。
 *
 * @author xiaowu
 */
@FunctionalInterface
public interface LlmClient {

  /**
   * 调用 LLM 进行推理。
   *
   * @param ctx 当前 Agent 上下文（含 prompt、历史工具结果、当前轮次等）
   * @return 思考结果：{@link LlmThinkResult.DirectAnswer} 或
   *         {@link LlmThinkResult.NeedsTools}
   */
  LlmThinkResult think(AiAgentContext ctx);
}
