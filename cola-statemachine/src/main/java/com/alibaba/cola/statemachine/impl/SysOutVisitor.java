package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

/**
 * SysOutVisitor
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:48 PM
 */
public class SysOutVisitor implements Visitor {
    @Override
    public void visitOnEntry(StateMachine<?, ?, ?> stateMachine) {
        System.out.println("-----StateMachine:"+stateMachine.getMachineId()+"-------");
    }

    @Override
    public void visitOnExit(StateMachine<?, ?, ?> stateMachine) {
        System.out.println("------------------------");
    }

    @Override
    public void visitOnEntry(State<?, ?, ?> state) {
        System.out.println("State:"+state.getId());
        for(Transition transition: state.getTransitions()){
            System.out.println("    Transition:"+transition);
        }

    }

    @Override
    public void visitOnExit(State<?, ?, ?> visitable) {

    }
}
