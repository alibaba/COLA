package com.alibaba.cola.statemachine.ai.config;

import java.time.Duration;

/**
 * AI Agent 运行时配置
 *
 * @author xiaowu
 */
public record AiAgentConfig(
        int maxRounds,
        Duration toolTimeout,
        int maxRetries) {
    public AiAgentConfig {
        if (maxRounds <= 0)
            throw new IllegalArgumentException("maxRounds must be > 0");
        if (toolTimeout == null || toolTimeout.isNegative())
            throw new IllegalArgumentException("toolTimeout must be positive");
        if (maxRetries < 0)
            throw new IllegalArgumentException("maxRetries must be >= 0");
    }

    public static AiAgentConfig defaults() {
        return new AiAgentConfig(10, Duration.ofSeconds(30), 3);
    }

    public static AiAgentConfig forTest() {
        return new AiAgentConfig(3, Duration.ofSeconds(5), 1);
    }
}
