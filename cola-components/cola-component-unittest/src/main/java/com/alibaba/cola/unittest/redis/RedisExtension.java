package com.alibaba.cola.unittest.redis;

import com.alibaba.cola.unittest.FixtureLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.time.Duration;
import java.util.*;

@Slf4j
public class RedisExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
    ObjectMapper objectMapper = new ObjectMapper();
    //default port is 6397
    private static RedisServer redisServer;
    public static Jedis jedis;
    private static boolean isStarted;

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        ExpectRedis expectRedis = context.getElement().get().getAnnotation(ExpectRedis.class);
        if (Objects.nonNull(expectRedis)) {
            log.info("after each, check redis result");
            String location = expectRedis.value();
            long interval = expectRedis.interval();
            long timeout = expectRedis.timeout();
            RedisData redisData = objectMapper.readValue(FixtureLoader.loadResource(location), RedisData.class);
            redisData.getRecords().forEach((key, content) -> {
                await(interval, timeout, key);
                String expect = content.textValue();
                String actual = jedis.get(key);
                log.debug("expect: " + expect);
                log.debug("actual: " + actual);
                Assertions.assertEquals(expect, actual);
            });
        }
    }

    private void await(long interval, long timeout, String key) {
        Awaitility.await().pollInterval(Duration.ofMillis(interval)).atMost(Duration.ofMillis(timeout))
                .until(() -> jedis.exists(key));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SetupRedis setupRedis = context.getElement().get().getAnnotation(SetupRedis.class);
        if (Objects.nonNull(setupRedis)) {
            log.info("before each, setup redis");
            String location = setupRedis.value();
            RedisData redisData = objectMapper.readValue(FixtureLoader.loadResource(location), RedisData.class);
            log.debug("redisData: " + redisData);
            redisData.getRecords().forEach((key, jsonNode) -> {
                processJsonNode(key, jsonNode);
            });
        }
    }

    private void processJsonNode(String key, JsonNode jsonNode) {
        JsonNodeType nodeType = jsonNode.getNodeType();
        log.debug("set redis record: TYPE--> {} , KEY--> {}, VALUE--> {}", nodeType, key, jsonNode);
        if (nodeType == JsonNodeType.STRING) {
            jedis.set(key, jsonNode.textValue());
            return;
        }
        if (nodeType == JsonNodeType.ARRAY) {
            List<String> elements = new ArrayList<>();
            jsonNode.forEach(item -> {
                String itemStr = item.isValueNode() ? item.asText() : item.toString();
                elements.add(itemStr);
            });
            jedis.sadd(key, elements.toArray(new String[0]));
            return;
        }
        if (nodeType == JsonNodeType.OBJECT) {
            Map<String, String> map = new HashMap<>();
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.fields()
                    .forEachRemaining(
                            field -> {
                                String value = field.getValue().isValueNode() ? field.getValue().asText() : field.getValue().toString();
                                map.put(field.getKey(), value);
                            });
            jedis.hmset(key, map);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        try {
            if (redisServer == null && !isStarted) {
                redisServer = new RedisServer(); //default port is 6379
                redisServer.start();
                log.debug("Redis server started");
            }
        } catch (Exception e) {
            isStarted = true;
            log.warn("Redis Server may already started, just ignore this exception:" + e.getMessage());
        }
        if (jedis == null) {
            jedis = new Jedis("localhost", 6379);
        }
    }
}
