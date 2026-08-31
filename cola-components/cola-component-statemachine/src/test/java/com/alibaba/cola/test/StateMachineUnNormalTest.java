package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.alibaba.cola.statemachine.impl.StateMachineException;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:52 PM
 */
public class StateMachineUnNormalTest {

    @Test
    public void testConditionNotMeet(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        String uniqueId = "NotMeetConditionMachine" + UUID.randomUUID().toString();
        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build(uniqueId);
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assertions.assertEquals(StateMachineTest.States.STATE1,target);
    }


    @Test
    public void testDuplicatedTransition(){
        Assertions.assertThrows(StateMachineException.class, ()->{
            StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());
        });
    }

    @Test
    public void testDuplicateMachine(){
        Assertions.assertThrows(StateMachineException.class, ()-> {
            StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
            builder.externalTransition()
                    .from(StateMachineTest.States.STATE1)
                    .to(StateMachineTest.States.STATE2)
                    .on(StateMachineTest.Events.EVENT1)
                    .when(checkCondition())
                    .perform(doAction());

            builder.build("DuplicatedMachine");
            builder.build("DuplicatedMachine");
        });
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> {return true;};
    }

    private Condition<StateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> {return false;};
    }

    private Action<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx)->{
            System.out.println(ctx.operator+" is operating "+ctx.entityId+"from:"+from+" to:"+to+" on:"+event);
        };
    }
}
