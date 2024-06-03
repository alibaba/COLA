package com.alibaba.cola.unittest.redis;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 使用jackson：https://zhuanlan.zhihu.com/p/646744855
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisData {
    // Map<redis key, json content>
    private Map<String, JsonNode> records;
}

