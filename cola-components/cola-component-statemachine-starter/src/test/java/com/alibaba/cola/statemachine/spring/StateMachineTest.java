package com.alibaba.cola.statemachine.spring;

import com.alibaba.cola.statemachine.StateMachineFactory;
import com.alibaba.cola.statemachine.spring.constant.ConstantString;
import com.alibaba.cola.statemachine.spring.constant.EventEnum;
import com.alibaba.cola.statemachine.spring.constant.StatusEnum;
import com.alibaba.cola.statemachine.spring.model.DemoSpringModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootStart.class)
public class StateMachineTest {

    @Test
    public void check_state_machine_run_start() {
        DemoSpringModel model = new DemoSpringModel(StatusEnum.INITIALIZED,StatusEnum.STARTED);
        StatusEnum start =  (StatusEnum) StateMachineFactory.get(ConstantString.COLA_STATE_MACHINE_DEMO_ID)
                .fireEvent(StatusEnum.INITIALIZED, EventEnum.TEST_EVENT,model);
        Assert.isTrue(start.equals(StatusEnum.STARTED),"最终执行的结果为开始状态");
    }

    @Test
    public void check_state_machine_run_stopped() {
        DemoSpringModel model = new DemoSpringModel(StatusEnum.INITIALIZED,StatusEnum.STOPPED);
        StatusEnum start =  (StatusEnum) StateMachineFactory.get(ConstantString.COLA_STATE_MACHINE_DEMO_ID)
                .fireEvent(StatusEnum.INITIALIZED, EventEnum.TEST_EVENT,model);
        Assert.isTrue(start.equals(StatusEnum.STOPPED),"最终执行的结果为停止状态");
    }
}
