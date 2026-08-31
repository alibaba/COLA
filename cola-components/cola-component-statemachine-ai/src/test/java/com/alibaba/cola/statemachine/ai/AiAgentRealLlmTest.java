package com.alibaba.cola.statemachine.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.alibaba.cola.statemachine.ai.config.AiAgentConfig;
import com.alibaba.cola.statemachine.ai.model.AiToolResult;
import com.alibaba.cola.statemachine.ai.model.LlmThinkResult;
import com.alibaba.cola.statemachine.ai.model.ToolRequest;
import com.alibaba.cola.statemachine.ai.spi.LlmClient;
import com.alibaba.cola.statemachine.ai.spi.ToolExecutor;
import com.alibaba.cola.statemachine.ai.trace.AiTraceLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AI Agent ReAct 状态机 — 真实 LLM 集成测试
 *
 * <p>
 * 使用 OpenAI 基站
 * <p>
 * 运行方式：
 *
 * <pre>
 * # 使用 Java 25（必须）
 * $env:JAVA_HOME="C:\Users\admin\scoop\apps\openjdk25\25.0.2-10"
 * $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
 *
 * # 单独运行集成测试
 * mvn test -pl cola-components/cola-component-statemachine-ai `
 *   -Dtest=AiAgentRealLlmTest -am -ntp
 * </pre>
 *
 * @author xiaowu
 */
@Tag("llm-integration")
@DisplayName("AI Agent — 真实 LLM 集成测试")
class AiAgentRealLlmTest {

  // ─────────────────────────────────────────────────────────
  // 配置
  // ─────────────────────────────────────────────────────────

  private static final String BASE_URL = "xxx"; // 替换为你的 OpenAI baseURLs
  private static final String API_KEY = "xxx"; // 替换为你的 OpenAI API Key
  private static final String MODEL = "xxx"; // 替换为你要测试的模型，如 "gpt-5.2"

  private static RealOpenAiClient openAiClient;

  @BeforeAll
  static void setup() {
    openAiClient = new RealOpenAiClient(BASE_URL, API_KEY, MODEL);
    System.out.println("使用模型: " + MODEL + "  基站: " + BASE_URL);
  }

  /**
   * 场景 1 — 直接回答
   * 流程：IDLE → THINKING → RESPONDING → COMPLETED
   */
  @Test
  @DisplayName("场景1:真实LLM直接回答 — 问一个无需搜索的常识问题")
  void test_realLlm_directAnswer() {
    // LlmClient：永远选择直接回答（系统 prompt 告知 LLM 直接回复即可）
    LlmClient directLlm = ctx -> {
      String question = ctx.getPrompt();
      String answer = openAiClient.chat(
          "你是一个简洁的助手，请用一句话直接回答问题，不要废话。",
          question);
      System.out.println(" test1 - [LLM回答] " + answer);
      return LlmThinkResult.directAnswer(answer);
    };

    AiAgentRunner runner = AiAgentRunner.builder()
        .config(new AiAgentConfig(3, Duration.ofSeconds(30), 1))
        .llmClient(directLlm)
        .build();

    AiAgentRunner.RunResult result = runner.runWithTrace("法国的首都是哪里？");

    System.out.println("\n>>> 最终回答: " + result.finalAnswer());
    System.out.println(">>> 总轮次: " + result.totalRounds());
    System.out.println(">>> 追踪:\n" + result.traceLog().formatTrace());

    assertThat(result.finalAnswer())
        .isNotBlank()
        .containsIgnoringCase("巴黎");
  }

  /**
   * 场景 2 — 真实 LLM + Mock 工具
   * 流程：IDLE → THINKING → TOOL_CALLING → OBSERVING → THINKING → RESPONDING →
   * COMPLETED
   * 验证：完整走过工具调用链路，LLM 根据工具结果生成回答
   */
  @Test
  @DisplayName("场景2：真实LLM + Mock工具 — 完整 ReAct 链路验证")
  void test_realLlm_withMockTool_reactLoop() {
    AtomicInteger round = new AtomicInteger(0);

    LlmClient reactLlm = ctx -> {
      int currentRound = round.getAndIncrement();
      System.out.println("\n  [LLM第" + (currentRound + 1) + "轮推理] 工具结果数=" + ctx.getToolResults().size());

      if (currentRound == 0) {
        // 第一轮：主动决定需要搜索（模拟 ReAct 的 "Act" 步骤）
        System.out.println("  [LLM决策] 需要调用搜索工具");
        return LlmThinkResult.needsTools(List.of(
            new ToolRequest("web_search",
                "{\"query\": \"" + ctx.getPrompt() + "\"}")));
      }

      // 第二轮：获得工具结果后，让 LLM 汇总作答
      String toolOutput = ctx.getToolResults().get(0).summary();
      String finalAnswer = openAiClient.chat(
          "你是一个严谨的助手。基于以下搜索结果，用一到两句话回答用户问题。" +
              "\n搜索结果：" + toolOutput,
          ctx.getPrompt());
      System.out.println("  [LLM汇总回答] " + finalAnswer);
      return LlmThinkResult.directAnswer(finalAnswer);
    };

    // 工具：Mock 搜索引擎，返回固定结果
    ToolExecutor mockSearch = req -> {
      System.out.println("  [Mock工具执行] web_search: " + req.params());
      // 模拟搜索结果
      return AiToolResult.success("web_search",
          "Java 25 于 2025年9月发布，带来 ScopedValue(JEP 506)和 Structured Concurrency(JEP 505)等重要特性。");
    };

    AiAgentRunner runner = AiAgentRunner.builder()
        .config(new AiAgentConfig(5, Duration.ofSeconds(30), 1))
        .llmClient(reactLlm)
        .toolExecutor(mockSearch)
        .build();

    AiAgentRunner.RunResult result = runner.runWithTrace("Java 25 有哪些重要的新特性？");

    System.out.println("\n>>> 最终回答: " + result.finalAnswer());
    System.out.println(">>> 总轮次: " + result.totalRounds());
    System.out.println(">>> 工具调用次数: " + result.toolResults().size());
    System.out.println(">>> 追踪:\n" + result.traceLog().formatTrace());

    // 验证：完整走过了工具调用链路
    assertThat(result.toolResults()).hasSize(1);
    assertThat(result.toolResults().get(0)).isInstanceOf(AiToolResult.Success.class);
    assertThat(result.finalAnswer()).isNotBlank();
    assertThat(result.totalRounds()).isEqualTo(2);

    // 验证 TraceLog 中存在 ToolInvoked 事件
    boolean hasToolEvent = result.traceLog().getEvents().stream()
        .anyMatch(e -> e instanceof AiTraceLog.TraceEvent.ToolInvoked);
    assertThat(hasToolEvent).isTrue();

    // 验证状态转换统计
    var transitions = result.traceLog().getStateTransitionCount();
    System.out.println(">>> 状态转换统计: " + transitions);
    assertThat(transitions).containsKey("IDLE → THINKING");
    assertThat(transitions).containsKey("RESPONDING → COMPLETED");
  }

  /**
   * 测试真实 LLM + 模拟工具
   * 场景 3 — 真实 LLM + 并行 Mock 工具
   * 流程：IDLE → THINKING → TOOL_CALLING（并行）→ OBSERVING → THINKING → RESPONDING →
   * COMPLETED
   * 验证：LLM 能同时调用多个工具，并在下一轮推理时汇总所有工具结果生成回答
   */
  @Test
  @DisplayName("场景3：真实LLM + 并行Mock工具 — LLM汇总多个工具结果")
  void test_realLlm_parallelTools_aggregation() {
    AtomicInteger round = new AtomicInteger(0);

    LlmClient reactLlm = ctx -> {
      int r = round.getAndIncrement();
      if (r == 0) {
        // 第一轮：决定并行调用两个工具
        System.out.println("  [LLM决策] 需要并行调用2个工具");
        return LlmThinkResult.needsTools(List.of(
            new ToolRequest("get_java_features", "Java 25"),
            new ToolRequest("get_release_date", "Java 25")));
      }

      // 第二轮：汇总所有工具结果
      StringBuilder toolContext = new StringBuilder();
      for (AiToolResult tr : ctx.getToolResults()) {
        toolContext.append(tr.summary()).append("\n");
      }
      String answer = openAiClient.chat(
          "请根据以下两条信息，用两句话综合回答用户问题。\n信息：\n" + toolContext,
          ctx.getPrompt());
      System.out.println("  [LLM汇总] " + answer);
      return LlmThinkResult.directAnswer(answer);
    };

    // 两个 Mock 工具
    ToolExecutor mockTools = req -> switch (req.toolName()) {
      case "get_java_features" -> {
        Thread.sleep(80); // 模拟 I/O
        yield AiToolResult.success(req.toolName(), "Java 25 包含：ScopedValue, Structured Concurrency, Valhalla 等");
      }
      case "get_release_date" -> {
        Thread.sleep(60);
        yield AiToolResult.success(req.toolName(), "Java 25 预计 2025 年 9 月正式发布（LTS）");
      }
      default -> AiToolResult.failure(req.toolName(), "Unknown tool");
    };

    long start = System.currentTimeMillis();
    AiAgentRunner runner = AiAgentRunner.builder()
        .config(new AiAgentConfig(5, Duration.ofSeconds(30), 1))
        .llmClient(reactLlm)
        .toolExecutor(mockTools)
        .build();

    AiAgentRunner.RunResult result = runner.runWithTrace("Java 25 是什么时候发布的？有哪些新特性？");
    long elapsed = System.currentTimeMillis() - start;

    System.out.println("\n>>> 最终回答: " + result.finalAnswer());
    System.out.println(">>> 耗时: " + elapsed + "ms（两个工具并行执行，理论 ~80ms I/O）");
    System.out.println(">>> 工具调用: " + result.toolResults().size() + " 个");
    System.out.println(">>> 追踪:\n" + result.traceLog().formatTrace());

    assertThat(result.toolResults()).hasSize(2);
    assertThat(result.toolResults())
        .allMatch(AiToolResult::isSuccess);
    assertThat(result.finalAnswer()).isNotBlank();
    // 两个工具并行，总耗时应远小于串行 140ms（+ LLM 调用时间）
    System.out.println(">>> 工具并发耗时合理: " + elapsed + "ms");
  }

  // ─────────────────────────────────────────────────────────
  // 内部辅助：轻量 OpenAI 兼容 HTTP 客户端
  // 使用 Java 11+ HttpClient + Jackson，不引入任何 OpenAI SDK
  // ─────────────────────────────────────────────────────────

  static class RealOpenAiClient {
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final HttpClient http;
    private final ObjectMapper mapper;

    RealOpenAiClient(String baseUrl, String apiKey, String model) {
      this.baseUrl = baseUrl.replaceAll("/$", "");
      this.apiKey = apiKey;
      this.model = model;
      this.http = HttpClient.newBuilder()
          .connectTimeout(Duration.ofSeconds(10))
          .build();
      this.mapper = new ObjectMapper();
    }

    /**
     * 发送单轮 Chat Completions 请求，返回 assistant 回复文本。
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @return assistant 回复内容
     */
    String chat(String systemPrompt, String userMessage) {
      try {
        // 构建请求体
        ObjectNode body = mapper.createObjectNode();
        body.put("model", model);
        body.put("max_tokens", 512);
        body.put("temperature", 0.3);

        ArrayNode messages = body.putArray("messages");
        messages.addObject().put("role", "system").put("content", systemPrompt);
        messages.addObject().put("role", "user").put("content", userMessage);

        String requestBody = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .timeout(Duration.ofSeconds(60))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
          throw new RuntimeException("API Error " + response.statusCode() + ": " + response.body());
        }

        // 解析响应：choices[0].message.content
        JsonNode root = mapper.readTree(response.body());
        String content = root
            .path("choices")
            .path(0)
            .path("message")
            .path("content")
            .asText();

        if (content.isBlank()) {
          throw new RuntimeException("Empty response from API. Body: " + response.body());
        }
        return content.strip();

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("API call interrupted", e);
      } catch (Exception e) {
        throw new RuntimeException("API call failed: " + e.getMessage(), e);
      }
    }
  }
}
