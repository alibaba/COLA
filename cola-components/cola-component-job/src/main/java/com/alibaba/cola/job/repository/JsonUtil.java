package com.alibaba.cola.job.repository;

import com.alibaba.cola.job.JobException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.Callable;

public final class JsonUtil {
    private static ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.getObjectMapper();

    public static String encode(Object object) {
        return invoke(() -> OBJECT_MAPPER.writeValueAsString(object));
    }

    public static <T> T decode(String source, Class<T> valueType) {
        return invoke(() -> OBJECT_MAPPER.readValue(source, valueType));
    }

    public static <T> T decode(String src, TypeReference<T> valueTypeRef) {
        return invoke(() -> OBJECT_MAPPER.readValue(src, valueTypeRef));
    }

    private static <T> T invoke(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new JobException(e);
        }
    }

    public  class ObjectMapperFactory {

        public static ObjectMapper OBJECT_MAPPER;

        static {
            OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();
            OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            OBJECT_MAPPER.findAndRegisterModules();
        }

        public static ObjectMapper getObjectMapper() {
            return OBJECT_MAPPER;
        }
    }
}



