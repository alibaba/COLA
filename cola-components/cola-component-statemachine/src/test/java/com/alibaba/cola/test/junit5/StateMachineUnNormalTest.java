package com.alibaba.cola.test.junit5;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.alibaba.cola.statemachine.impl.StateMachineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author cookie lin
 * @date 2023-03-08 5:52 PM
 */
public class StateMachineUnNormalTest {

    @Test
    @DisplayName("the state is unchanged when the condition is no met")
    public void testConditionNotMeet(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("NotMeetConditionMachine");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assertions.assertEquals(StateMachineTest.States.STATE1,target);
    }


    @Test
    @DisplayName("StateMachineException is thrown when duplicated transition is defined")
    public void testDuplicatedTransition(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        Assertions.assertThrows(StateMachineException.class, () -> {
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
    @DisplayName("StateMachineException is thrown when duplicated statemachine is defined")
    public void testDuplicateMachine(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        Assertions.assertThrows(StateMachineException.class, () -> {
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
        return (ctx) -> true;
    }

    private Condition<StateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> false;
    }

    private Action<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx)-> System.out.println(ctx.operator+" is operating "+ctx.entityId+"from:"+from+" to:"+to+" on:"+event);
    }
}
