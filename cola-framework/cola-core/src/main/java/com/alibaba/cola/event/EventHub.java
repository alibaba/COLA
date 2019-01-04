package com.alibaba.cola.event;

import com.alibaba.cola.dto.event.Event;
import com.alibaba.cola.exception.ColaException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件控制中枢
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@SuppressWarnings("rawtypes")
@Component
@Data
public class EventHub {

    /**
     * one event could have multiple event handlers
     */
    private ListMultimap<Class, EventHandlerI> eventRepository = ArrayListMultimap.create();

    public List<EventHandlerI> getEventHandler(Class eventClass) {
        List<EventHandlerI> eventHandlerIList = findHandler(eventClass);
        if (eventHandlerIList == null || eventHandlerIList.size() == 0) {
            throw new ColaException(eventClass + "is not registered in eventHub, please register first");
        }
        return eventHandlerIList;
    }

    /**
     * 注册事件
     * @param eventClz
     * @param executor
     */
    public void register(Class<? extends Event> eventClz, EventHandlerI executor){
        eventRepository.put(eventClz, executor);
    }

    private List<EventHandlerI> findHandler(Class<? extends Event> eventClass){
        return eventRepository.get(eventClass);
    }

}
