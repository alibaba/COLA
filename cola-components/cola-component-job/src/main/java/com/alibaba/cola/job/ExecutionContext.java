package com.alibaba.cola.job;

import com.alibaba.cola.job.repository.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ToString
public class ExecutionContext<T> {
    /**
     * 这是任务执行的主参数，通常是client自定义的类
     */
    private T param;

    @JsonIgnore
    private Object rawParam;

    /**
     * 为了灵活性，主参数之外的数据，放在extensions
     */
    @JsonProperty("extensions")
    private final Map<String, Object> extensions;


    /**
     * 这里是暂存数据到context的，不会进行持久化
     */
    @JsonIgnore
    private final Map<String, Object> temp;

    /**
     * 当执行批量任务的时候，需要传入batchJobId
     */
    @JsonIgnore
    private String batchJobId;

    /**
     * 当重复执行一个Job的时候，需要传入上一次执行的jobExecutionId
     */
    @JsonIgnore
    private String jobId;

    public T getParam() {
        return (T) param;
    }

    @SuppressWarnings("unchecked")
    public T getParam(Class<T> tClass) {
        if (rawParam != null) {
            return (T) rawParam;
        }
        if (param.getClass().equals(tClass)) {
            rawParam = param;
        }
        rawParam = JsonUtil.decode(JsonUtil.encode(param), tClass);
        return (T) rawParam;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getBatchJobId() {
        return batchJobId;
    }

    public void setBatchJobId(String batchJobId) {
        this.batchJobId = batchJobId;
    }

    public ExecutionContext() {
        this.extensions = new ConcurrentHashMap();
        this.temp = new ConcurrentHashMap<>();
    }

    public ExecutionContext(String jobId) {
        this();
        this.jobId = jobId;
    }

    public ExecutionContext(Map<String, Object> extensions) {
        this.extensions = new ConcurrentHashMap(extensions);
        this.temp = new ConcurrentHashMap<>();
    }

    public void putString(String key, @Nullable String value) {
        this.put(key, value);
    }

    public void putTemp(String key, Object value){
        this.temp.put(key, value);
    }

    public Object getTemp(String key){
        return this.temp.get(key);
    }

    public String getString(String key) {
        return (String) this.get(key, String.class);
    }

    public String getString(String key, String defaultString) {
        return !this.containsKey(key) ? defaultString : this.getString(key);
    }

    public void put(String key, @Nullable Object value) {
        if (value != null) {
            this.extensions.put(key, value);
        } else {
            this.extensions.remove(key);
        }
    }

    @Nullable
    public Object get(String key) {
        return this.extensions.get(key);
    }

    @Nullable
    public <V> V get(String key, Class<V> type) {
        Object value = this.extensions.get(key);
        return value == null ? null : this.get(key, type, null);
    }

    @Nullable
    public <V> V get(String key, Class<V> type, @Nullable V defaultValue) {
        Object value = this.extensions.get(key);
        if (value == null) {
            return defaultValue;
        } else if (!type.isInstance(value)) {
            throw new ClassCastException(
                    "Value for key=[" + key + "] is not of type: [" + type + "], it is [(" + value.getClass() + ")" + value
                            + "]");
        } else {
            return type.cast(value);
        }
    }

    public boolean containsKey(String key) {
        return this.extensions.containsKey(key);
    }

    @Nullable
    public Object remove(String key) {
        return this.extensions.remove(key);
    }

    public boolean containsValue(Object value) {
        return this.extensions.containsValue(value);
    }

    public ExecutionContext<?> fromJsonString(String jsonString) {
        return JsonUtil.decode(jsonString, ExecutionContext.class);
    }

    @Override
    public String toString() {
        return "ExecutionContext{" + "param=" + param + ", extensions=" + extensions + '}';
    }
}

