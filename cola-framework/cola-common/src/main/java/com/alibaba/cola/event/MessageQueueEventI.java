package com.alibaba.cola.event;

/**
 * @author: caiqiang.w
 * @date: 2019/2/14
 * @description:
 */
public interface MessageQueueEventI extends EventI {
    public String getEventType();
    public String getEventTopic();
}
