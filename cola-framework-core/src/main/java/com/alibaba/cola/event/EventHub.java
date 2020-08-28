package com.alibaba.cola.event;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.framework.ColaException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public class EventHub {

    private HashMap<Class, List<EventHandlerI>> eventRepository = new HashMap<>();
    
    private Map<Class, Class> responseRepository = new HashMap<>();

    public HashMap<Class, List<EventHandlerI>> getEventRepository() {
        return eventRepository;
    }

    public void setEventRepository(HashMap<Class, List<EventHandlerI>> eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<Class, Class> getResponseRepository() {
        return responseRepository;
    }

    @SuppressWarnings("unchecked")
    public List<EventHandlerI> getEventHandler(Class eventClass) {
        List<EventHandlerI> eventHandlerList = findHandler(eventClass);
        if (eventHandlerList == null || eventHandlerList.size() == 0) {
            throw new ColaException(eventClass + "is not registered in eventHub, please register first");
        }
        return eventHandlerList;
    }

    /**
     * 注册事件
     *
     * @param eventClz 领域事件实体clazz
     * @param executor 领域事件处理器
     */
    public void register(Class<? extends EventI> eventClz, EventHandlerI executor){
        List<EventHandlerI> eventHandlers = eventRepository.get(eventClz);
        if(eventHandlers == null){
            eventHandlers = new ArrayList<>();
        }
        eventHandlers.add(executor);
        eventRepository.put(eventClz, eventHandlers);
    }

    /**
     * 注册事件处理器的返回类
     *
     * @param eventHandlerClz 事件处理器clazz
     * @param responseClz 事件处理器返回实体clazz
     */
    public void register(Class<? extends EventHandlerI> eventHandlerClz, Class<? extends Response> responseClz) {
        responseRepository.put(eventHandlerClz, responseClz);
    }

    private List<EventHandlerI> findHandler(Class<? extends EventI> eventClass){
        List<EventHandlerI> eventHandlerList;
        eventHandlerList = eventRepository.get(eventClass);
        return eventHandlerList;
    }

}
