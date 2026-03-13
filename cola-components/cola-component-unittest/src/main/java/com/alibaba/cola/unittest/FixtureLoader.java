package com.alibaba.cola.unittest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FixtureLoader {

    public static String loadResource(String resourcePath) {
        log.info("Fixture resource location: " + resourcePath);
        // 创建一个 ClassPathResource 对象
        ClassPathResource resource = new ClassPathResource(resourcePath);

        // 使用 withResource 来自动关闭输入流
        String content = "";
        try (InputStream inputStream = resource.getInputStream()) {
            content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return content;
    }
}
