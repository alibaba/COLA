package com.alibaba.cola.statemachine.spring.transition;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.spring.annotation.StateMachine;
import com.alibaba.cola.statemachine.spring.constant.ConstantString;
import com.alibaba.cola.statemachine.spring.constant.EventEnum;
import com.alibaba.cola.statemachine.spring.constant.StatusEnum;
import com.alibaba.cola.statemachine.spring.model.DemoSpringModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@StateMachine(from = "INITIALIZED", to = "STARTED", on = "TEST_EVENT",machineId = ConstantString.COLA_STATE_MACHINE_DEMO_ID)
@Component
public class Init2StartTransition implements Action<StatusEnum, EventEnum, DemoSpringModel> {

    @Override
    public void execute(StatusEnum from, StatusEnum to, EventEnum event, DemoSpringModel context) {
        log.info("Init2StartTransition execute, from: {}, to: {}, event: {}, context: {}", from, to, event, context);
    }
}
