package com.alibaba.cola.statemachine.spring.plugin;

import com.alibaba.cola.statemachine.spring.config.AbstractAfterActionAdapter;
import com.alibaba.cola.statemachine.spring.config.AbstractConditionAdapter;
import com.alibaba.cola.statemachine.spring.constant.EventEnum;
import com.alibaba.cola.statemachine.spring.constant.StatusEnum;
import com.alibaba.cola.statemachine.spring.model.DemoSpringModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoAfterAction extends AbstractAfterActionAdapter<StatusEnum, EventEnum, DemoSpringModel> {

    /**
     * 判断要在哪个Action中实现
     *
     * @param from
     * @param to
     * @param on   condition
     * @param m
     * @return
     */
    @Override
    public boolean ifUsedInCurrentAction(StatusEnum from, StatusEnum to, EventEnum on, DemoSpringModel m) {
        return true;
    }

    @Override
    public void execute(StatusEnum from, StatusEnum to, EventEnum event, DemoSpringModel context) {
        log.info("DemoAfterAction execute, from: {}, to: {}, event: {}, context: {}", from, to, event, context);
    }

}
