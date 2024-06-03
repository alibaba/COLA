package com.alibaba.cola.unittest.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Set;

@ExtendWith(RedisExtension.class)
public class RedisExtensionTest {

    /**
     * json String 的内容，注意如果是String的话，需要用转义符号\，转义双引号
     * {
     * "records": {
     * "topo:child_job:222": "{\"id\":\"12345678-1234-1234-1234-childJob0000\",\"parent_job_id\":\"12345678-1234-1234-1234-noParentJob0\",\"resource_id\":\"1.1.1.4\",\"status\":\"success\"}",
     * "topo:child_job:333": "{\"id\":\"12345678-1234-1234-1234-childJob0000\"}"
     * }
     * }
     */
    @Test
    @SetupRedis("/fixture/redis/string-setup.json")
    public void testString() {
        System.out.println("test String SetupRedis");
    }

    /**
     * json array 的内容：
     * {
     * "records": {
     * "xlink:10.0.0.11:port": [
     * "30000000-0000-0000-0000-000000000001",
     * "30000000-0000-0000-0000-000000000002"
     * ]
     * }
     * }
     */
    @Test
    @SetupRedis("/fixture/redis/array-setup.json")
    public void testArray() {
        System.out.println("test array SetupRedis");
        Set<String> result = RedisExtension.jedis.smembers("test:array");
        System.out.println("test result : " + result);
        Assertions.assertEquals(2, result.size());
    }

    /**
     * json 的object在redis里面是用hash存储，内容如下：
     * {
     * "records": {
     * "xlink:hyper_cluster_port:30000000-0000-0000-0000-000000000001": {
     * "version": 1,
     * "json": {
     * "id": "30000000-0000-0000-0000-000000000001",
     * "name": "port-01",
     * "project_id": "7a9941d34fc1497d8d0797429ecfd354",
     * "provisioning_status": "active",
     * "created_at": "2024-01-01T12:00:00Z",
     * "updated_at": "2024-01-01T12:00:00Z"
     * }
     * }
     * }
     * }
     */
    @Test
    @SetupRedis("/fixture/redis/hash-setup.json")
    public void testHash() {
        System.out.println("test hash SetupRedis");
        Map<String, String> result = RedisExtension.jedis.hgetAll("test:hash");
        System.out.println("test result : " + result);
        Assertions.assertEquals("1", result.get("version"));
    }

    @Test
    @SetupRedis("/fixture/redis/string-setup.json")
    @ExpectRedis("/fixture/redis/string-expect.json")
    public void testStringExpect() {
        System.out.println("test ExpectRedis");
    }

    @Test
    public void testVoid() {
        System.out.println("test without SetupRedis");
    }
}

