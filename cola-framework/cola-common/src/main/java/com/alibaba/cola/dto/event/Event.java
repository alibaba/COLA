package com.alibaba.cola.dto.event;

import com.alibaba.cola.dto.DTO;

/**
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public class Event extends DTO{
    private static final long serialVersionUID = 5740150436439366761L;

    /**
     * Optional fields, mainly used for Messaging Middleware
     */
    private String eventId;
    private String eventType;
    private String eventTopic;

    public String getEventType(){
        return eventType;
    }

    public String getEventId(){
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTopic() {
        return eventTopic;
    }

    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }
}
