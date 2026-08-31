package com.alibaba.cola.statemachine.ai.spi;

import com.alibaba.cola.statemachine.ai.model.AiToolResult;
import com.alibaba.cola.statemachine.ai.model.ToolRequest;

/**
 * 工具执行接口 — 可插拔设计
 *
 * <p>
 * 每次工具调用运行在独立的虚拟线程中（由 {@code AiAgentRunner} 通过 Structured Concurrency 调度），
 * 实现时可以放心做 I/O 阻塞操作（HTTP 请求、数据库查询等）。
 *
 * <p>
 * 示例(Mock 实现)：
 *
 * <pre>{@code
 * ToolExecutor mockExecutor = request -> switch (request.toolName()) {
 *   case "search" -> AiToolResult.success("search", "Result for: " + request.params());
 *   case "calculate" -> AiToolResult.success("calculate", "42");
 *   default -> AiToolResult.failure(request.toolName(), "Unknown tool");
 * };
 * }</pre>
 *
 * @author xiaowu
 */
@FunctionalInterface
public interface ToolExecutor {

  /**
   * 执行一个工具调用。
   *
   * @param request 工具请求（含工具名和参数）
   * @return 工具调用结果
   * @throws Exception 工具执行失败时抛出，Runner 会自动包装为 {@link AiToolResult.Failure}
   */
  AiToolResult execute(ToolRequest request) throws Exception;
}
