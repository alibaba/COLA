package com.huawei.charging.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class FixtureLoader {

    public static String loadResource(String resourcePath) {
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
