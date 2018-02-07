package com.alibaba.sofa.dto.event;

import com.alibaba.sofa.dto.DTO;

/**
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public class Event extends DTO{
    private static final long serialVersionUID = 5740150436439366761L;
    protected String eventId;
    protected String eventType;

    /**
     * event type,for example "CREATE"/"UPDATE"...
     * @return
     */
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
}
