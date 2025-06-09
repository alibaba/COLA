package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateChain;
import com.alibaba.cola.statemachine.StateChainVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StateChainImpl
 *
 * @author benym
 * @date 2024/6/12 0:04
 */
public class StateChainImpl<S, E, C> implements StateChain<S, E> {

    private final State<S, E, C> sourceState;

    private final E event;

    private final List<State<S, E, C>> targetStates;

    public StateChainImpl(State<S, E, C> sourceState, E event, List<State<S, E, C>> targetStates) {
        this.sourceState = sourceState;
        this.event = event;
        this.targetStates = targetStates;
    }

    @Override
    public S getSource() {
        return this.sourceState.getId();
    }

    @Override
    public List<S> getTargets() {
        return this.targetStates.stream().map(State::getId)
                .collect(Collectors.toList());
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void showStateChain() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUml() {
        PlantUMLVisitor plantUmlVisitor = new PlantUMLVisitor();
        return accept(plantUmlVisitor);
    }

    @Override
    public String accept(StateChainVisitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        List<State<S, E, C>> chainStateList = new ArrayList<>();
        chainStateList.add(this.sourceState);
        chainStateList.addAll(this.targetStates);
        String row = visitor.visitOnStateChain(chainStateList, event);
        sb.append(row);
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }
}
