package com.alibaba.cola.statemachine.ai.model;

import java.time.Duration;

/**
 * 工具调用结果
 *
 * @author xiaowu
 * @date 2026-02-27 11:23 PM
 */
public sealed interface AiToolResult
    permits AiToolResult.Success, AiToolResult.Failure, AiToolResult.Timeout {

  String toolName();

  record Success(String toolName, String output) implements AiToolResult {
  }

  record Failure(String toolName, String error, Throwable cause) implements AiToolResult {
  }

  record Timeout(String toolName, Duration elapsed) implements AiToolResult {
  }

  default String summary() {
    return switch (this) {
      case Success(var name, var output) -> "[OK] " + name + " → " + output;
      case Failure(var name, var err, _) -> "[FAIL] " + name + " → " + err;
      case Timeout(var name, var elapsed) -> "[TIMEOUT] " + name + " after " + elapsed.toMillis() + "ms";
    };
  }

  default boolean isSuccess() {
    return this instanceof Success;
  }

  default boolean isFailure() {
    return !(this instanceof Success);
  }

  static Success success(String toolName, String output) {
    return new Success(toolName, output);
  }

  static Failure failure(String toolName, String error) {
    return new Failure(toolName, error, null);
  }

  static Failure failure(String toolName, String error, Throwable cause) {
    return new Failure(toolName, error, cause);
  }

  static Timeout timeout(String toolName, Duration elapsed) {
    return new Timeout(toolName, elapsed);
  }
}
