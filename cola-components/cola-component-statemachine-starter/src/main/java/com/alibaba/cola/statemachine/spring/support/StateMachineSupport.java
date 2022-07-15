package com.alibaba.cola.statemachine.spring.support;



import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Order
@Slf4j
public class StateMachineSupport {

    /**
     * 状态机构造器
     */
    public static Map<String, StateMachineBuilder> stateMachineBuilderMap = new ConcurrentHashMap<>();

    public static void startStateMachine(){
        stateMachineBuilderMap.forEach((k, v) -> {
            StateMachine build =  v.build(k);
            log.info("{},状态机初始化！ ===> {}", k, build.generatePlantUML());
        });
        stateMachineBuilderMap.clear();
    }
}
