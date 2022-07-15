package com.alibaba.cola.statemachine.spring.plugin;

import com.alibaba.cola.statemachine.spring.config.AbstractConditionAdapter;
import com.alibaba.cola.statemachine.spring.model.DemoSpringModel;
import org.springframework.stereotype.Component;

@Component
public class DemoCondition extends AbstractConditionAdapter<DemoSpringModel> {
    /**
     * 自己的根据枚举简单的实现Condition判断
     *
     * @param from
     * @param to
     * @param demoSpringModel
     * @return
     */
    @Override
    public boolean commonCheck(Enum from, Enum to, DemoSpringModel demoSpringModel) {
        return demoSpringModel.getSourceEnum().equals(from)
                && demoSpringModel.getTargetEnum().equals(to);
    }
}
