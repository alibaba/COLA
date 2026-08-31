package com.alibaba.cola.statemachine.ai.model;

/**
 * AI Agent ReAct 状态机事件
 *
 * @author xiaowu
 */
public enum AiAgentEvent {

  /** 接收用户提问，驱动状态从 IDLE → THINKING */
  RECEIVE_PROMPT,

  /** 推理完成（工具结果已观察），进入下一轮 THINKING */
  THINK_COMPLETE,

  /** 推理决定需要调用工具，THINKING → TOOL_CALLING */
  NEED_TOOL,

  /** 工具执行完毕，TOOL_CALLING → OBSERVING */
  TOOL_RESULT,

  /** 推理决定可以直接回答，THINKING → RESPONDING */
  DIRECT_ANSWER,

  /** 回答生成完毕，RESPONDING → COMPLETED */
  GENERATE_COMPLETE,

  /** 超时，任意状态 → FAILED */
  TIMEOUT,

  /** 运行时异常，任意状态 → FAILED */
  ERROR,

  /** 失败后重试，FAILED → THINKING */
  RETRY,

  /** 完全重置，FAILED → IDLE */
  RESET
}
