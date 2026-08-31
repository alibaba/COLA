package com.alibaba.cola.statemachine.ai.model;

/**
 * AI Agent ReAct 状态机 — 7 个预定义状态
 *
 * <pre>
 * IDLE → THINKING → TOOL_CALLING → OBSERVING → THINKING (多轮)
 *                ↘ RESPONDING → COMPLETED
 * any → FAILED → RETRY → previous / RESET → IDLE
 * </pre>
 *
 * @author xiaowu
 */
public enum AiAgentState {

  /** 空闲，等待用户输入 */
  IDLE,

  /** Agent 推理中：分析 prompt，决定直接回答还是调用工具 */
  THINKING,

  /** 工具调用中（支持并行，基于 Structured Concurrency） */
  TOOL_CALLING,

  /** 观察工具返回结果，决定是否继续推理 */
  OBSERVING,

  /** 生成最终回答 */
  RESPONDING,

  /** 推理结束，已生成最终答案 */
  COMPLETED,

  /** 发生不可恢复错误或超时 */
  FAILED
}
