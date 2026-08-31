package com.alibaba.cola.statemachine.ai.model;

/**
 * 工具调用请求
 *
 * <p>
 * LLM 在推理阶段决定调用工具时，通过 {@link LlmThinkResult.NeedsTools} 返回一组
 * {@code ToolRequest}。
 * Runner 负责并发分发给 {@link com.alibaba.cola.statemachine.ai.spi.ToolExecutor} 执行。
 *
 * @param toolName 工具名称（由 ToolExecutor 路由）
 * @param params   工具参数（JSON 字符串或任意格式，由 ToolExecutor 解析）
 * @author xiaowu
 */
public record ToolRequest(String toolName, String params) {
}
