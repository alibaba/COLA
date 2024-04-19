package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineBuilderTest
 *
 * @author luozhenyu
 * @date 2023-02-20 01:37 AM
 */
public class StateMachineBuilderTest {

    enum States {
        STATE1,
        STATE2,
        STATE3
    }

    enum Events {
        EVENT1,
        EVENT2
    }

    static class Context {
    }

    @Test
    public void testReuseBuilder() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE2)
            .on(Events.EVENT1)
            .perform(doAction());
        StateMachine<States, Events, Context> stateMachine1 = builder.build("TestReuseBuilder1");
        {
            States target = stateMachine1.fireEvent(States.STATE1, Events.EVENT1, new Context());
            Assert.assertEquals(States.STATE2, target);
            target = stateMachine1.fireEvent(States.STATE1, Events.EVENT2, new Context());
            Assert.assertEquals(States.STATE1, target);
        }

        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE3)
            .on(Events.EVENT2)
            .perform(doAction());
        StateMachine<States, Events, Context> stateMachine2 = builder.build("TestReuseBuilder2");
        {
            States target = stateMachine1.fireEvent(States.STATE1, Events.EVENT1, new Context());
            Assert.assertEquals(States.STATE2, target);
            target = stateMachine1.fireEvent(States.STATE1, Events.EVENT2, new Context());
            Assert.assertEquals(States.STATE1, target);

            target = stateMachine2.fireEvent(States.STATE1, Events.EVENT1, new Context());
            Assert.assertEquals(States.STATE2, target);
            target = stateMachine2.fireEvent(States.STATE1, Events.EVENT2, new Context());
            Assert.assertEquals(States.STATE3, target);
        }
    }

    private Action<States, Events, Context> doAction() {
        return (from, to, event, context) -> {
            System.out.println("from:" + from + " to:" + to + " on:" + event);
        };
    }
}
