package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.*;

import java.util.List;

/**
 * SysOutVisitor
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:48 PM
 */
public class SysOutVisitor implements Visitor, StateChainVisitor {

    @Override
    public String visitOnEntry(StateMachine<?, ?, ?> stateMachine) {
        String entry = "-----StateMachine:"+stateMachine.getMachineId()+"-------";
        System.out.println(entry);
        return entry;
    }

    @Override
    public String visitOnExit(StateMachine<?, ?, ?> stateMachine) {
        String exit = "------------------------";
        System.out.println(exit);
        return exit;
    }

    @Override
    public String visitOnEntry(StateChain<?, ?> stateChain) {
        StringBuilder sb = new StringBuilder();
        String entry = "-----StateChain-Event:+" + stateChain.getEvent() + "------";
        sb.append(entry).append(LF);
        System.out.println(entry);
        return sb.toString();
    }

    @Override
    public String visitOnExit(StateChain<?, ?> stateChain) {
        String exit = "-----------------------------------";
        System.out.println(exit);
        return exit;
    }

    @Override
    public <S, C, E> String visitOnStateChain(List<State<S, E, C>> chainStateList, E event) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chainStateList.size() - 1; i++) {
            State<S, E, C> sourceState = chainStateList.get(i);
            sb.append(sourceState.getId()).append("->");
        }
        if (!chainStateList.isEmpty()) {
            sb.append(chainStateList.get(chainStateList.size() - 1).getId());
        }
        System.out.println(sb);
        return sb.toString();
    }

    @Override
    public String visitOnEntry(State<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        String stateStr = "State:"+state.getId();
        sb.append(stateStr).append(LF);
        System.out.println(stateStr);
        for(Transition transition: state.getAllTransitions()){
            String transitionStr = "    Transition:"+transition;
            sb.append(transitionStr).append(LF);
            System.out.println(transitionStr);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?, ?> visitable) {
        return "";
    }
}
