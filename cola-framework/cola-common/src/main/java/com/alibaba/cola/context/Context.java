package com.alibaba.cola.context;

/**
 * Context is initialized before request processing, usually it should be in Interceptor,
 * and will be used through in all layers in one request, that is why it's composited in Command and Entity
 *
 * Note that the Context is stateless and thread safe
 *
 * @author Frank Zhang
 * @date 2019-01-02 10:14 AM
 */
public class Context<T> {

    /**
     * bizCode is used for Extension, the naming should follow Java namespace.
     *
     * For example: "ali.tmall.supermarket" means it is "天猫超市的业务编码"
     */
    private String bizCode;

    /**
     * This is the content for application customization, different application would have different Context content.
     *
     * For example: in a typical application, the content might contain before info
     *
     * String userId;
     * String roleName;
     * String orgId;
     * String operatorId;
     */
    private T content;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
