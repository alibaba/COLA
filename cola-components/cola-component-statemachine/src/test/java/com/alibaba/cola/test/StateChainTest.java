package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateChain;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

/**
 * StateChainTest
 *
 * @author benym
 * @date 2024/6/12 18:57
 */
public class StateChainTest {

    private static final Boolean DEBUG = false;

    @Test
    public void testGetTargetStates() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransitions()
                .fromAmong(StateMachineTest.States.STATE1, StateMachineTest.States.STATE2)
                .to(StateMachineTest.States.STATE5)
                .on(StateMachineTest.Events.EVENT3)
                .when(checkCondition())
                .perform(doAction());
        builder.externalParallelTransition()
                .from(StateMachineTest.States.STATE3)
                .toAmong(StateMachineTest.States.STATE4, StateMachineTest.States.STATE5)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("TestGetTargetStatesMachine");
        System.out.println(stateMachine.generatePlantUML());
        List<StateMachineTest.States> targetStateList = stateMachine.getTargetStates(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, new HashSet<>(targetStateList).size(),
                        "The list should contain only two unique elements"),
                () -> Assertions.assertTrue(targetStateList.stream().allMatch(s -> s.equals(StateMachineTest.States.STATE1) || s.equals(StateMachineTest.States.STATE2)),
                        "The list should only contain 'state1' and 'state2'"),
                () -> Assertions.assertFalse(targetStateList.contains(null),
                        "The list should not contain null.")
        );
        List<StateMachineTest.States> targetStateList2 = stateMachine.getTargetStates(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new HashSet<>(targetStateList2).size(),
                        "The list should contain only one unique elements"),
                () -> Assertions.assertTrue(targetStateList2.stream().allMatch(s -> s.equals(StateMachineTest.States.STATE5)),
                        "The list should only contain 'state5'"),
                () -> Assertions.assertFalse(targetStateList2.contains(null),
                        "The list should not contain null.")
        );
        List<StateMachineTest.States> targetStateList3 = stateMachine.getTargetStates(StateMachineTest.States.STATE2, StateMachineTest.Events.EVENT3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new HashSet<>(targetStateList3).size(),
                        "The list should contain only one unique elements"),
                () -> Assertions.assertTrue(targetStateList3.stream().allMatch(s -> s.equals(StateMachineTest.States.STATE5)),
                        "The list should only contain 'state5'"),
                () -> Assertions.assertFalse(targetStateList3.contains(null),
                        "The list should not contain null.")
        );
        List<StateMachineTest.States> targetStateList4 = stateMachine.getTargetStates(StateMachineTest.States.STATE3, StateMachineTest.Events.EVENT1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, new HashSet<>(targetStateList4).size(),
                        "The list should contain only two unique elements"),
                () -> Assertions.assertTrue(targetStateList4.stream().allMatch(s -> s.equals(StateMachineTest.States.STATE4) || s.equals(StateMachineTest.States.STATE5)),
                        "The list should only contain 'state4' and 'state5'"),
                () -> Assertions.assertFalse(targetStateList4.contains(null),
                        "The list should not contain null.")
        );
    }

    @Test
    public void testGetTargetStateChain() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        // event1
        builder.internalTransition()
                .within(StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransition()
                .from(StateMachineTest.States.STATE2)
                .to(StateMachineTest.States.STATE3)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalTransitions()
                .fromAmong(StateMachineTest.States.STATE1, StateMachineTest.States.STATE2)
                .to(StateMachineTest.States.STATE5)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        builder.externalParallelTransition()
                .from(StateMachineTest.States.STATE3)
                .toAmong(StateMachineTest.States.STATE4, StateMachineTest.States.STATE5, StateMachineTest.States.STATE1)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("TestGetTargetStateChainMachine");
        System.out.println(stateMachine.generatePlantUML());
        System.out.println();
        List<StateChain<StateMachineTest.States, StateMachineTest.Events>> targetStateChainList = stateMachine.getTargetStateChain(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1);
        targetStateChainList.forEach(
                targetStateChain -> {
                    targetStateChain.showStateChain();
                    System.out.println();
                    String plantUml = targetStateChain.generatePlantUml();
                    System.out.println(plantUml);
                    System.out.println();
                    checkStateChain(targetStateChain);
                }
        );
    }

    private void checkStateChain(StateChain<StateMachineTest.States, StateMachineTest.Events> stateChain) {
        boolean group1Passed;
        boolean group2Passed;
        boolean group3Passed;
        boolean group4Passed;
        boolean group5Passed;
        boolean group6Passed;
        StateMachineTest.States source = stateChain.getSource();
        Assertions.assertEquals(source, StateMachineTest.States.STATE1);
        group1Passed = checkGroup(stateChain.getTargets(), 1, List.of(StateMachineTest.States.STATE1));
        group2Passed = checkGroup(stateChain.getTargets(), 3, List.of(StateMachineTest.States.STATE2, StateMachineTest.States.STATE3, StateMachineTest.States.STATE4));
        group3Passed = checkGroup(stateChain.getTargets(), 3, List.of(StateMachineTest.States.STATE2, StateMachineTest.States.STATE3, StateMachineTest.States.STATE5));
        group4Passed = checkGroup(stateChain.getTargets(), 3, List.of(StateMachineTest.States.STATE2, StateMachineTest.States.STATE3, StateMachineTest.States.STATE1));
        group5Passed = checkGroup(stateChain.getTargets(), 1, List.of(StateMachineTest.States.STATE5));
        group6Passed = checkGroup(stateChain.getTargets(), 2, List.of(StateMachineTest.States.STATE2, StateMachineTest.States.STATE5));
        Assertions.assertTrue((group1Passed || group2Passed || group3Passed || group4Passed || group5Passed || group6Passed),
                "At least one group of assertions should pass");
    }

    private boolean checkGroup(List<StateMachineTest.States> targets, Integer setNum, List<StateMachineTest.States> equalsList) {
        boolean passed = true;
        HashSet<StateMachineTest.States> uniqueStates = new HashSet<>(targets);
        if (uniqueStates.size() != setNum) {
            if (DEBUG) {
                System.out.println("The list should contain only " + setNum + " unique elements");
            }
            passed = false;
        }
        for (StateMachineTest.States target : targets) {
            if (target == null) {
                if (DEBUG) {
                    System.out.println("The list should not contain null.");
                }
                passed = false;
                break;
            }
            boolean foundMatch = false;
            for (StateMachineTest.States state : equalsList) {
                if (target.equals(state)) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                if (DEBUG) {
                    System.out.println("The list should only contain elements from the provided equalsList.");
                }
                passed = false;
                break;
            }
        }
        return passed;
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return new Condition<StateMachineTest.Context>() {
            @Override
            public boolean isSatisfied(StateMachineTest.Context context) {
                System.out.println("Check condition : " + context);
                return true;
            }
        };
    }

    private Action<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(
                    ctx.operator + " is operating " + ctx.entityId + " from:" + from + " to:" + to + " on:" + event);
        };
    }
}
