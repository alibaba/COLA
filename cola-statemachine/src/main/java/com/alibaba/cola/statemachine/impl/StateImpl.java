package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

/**
 * StateImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S,E,C> implements State<S,E,C> {
    protected final S stateId;
    private HashMap<E, Transition<S, E,C>> transitions = new HashMap<>();

    StateImpl(S stateId){
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S,E,C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);

        Debugger.debug("Begin to add new transition: "+ newTransition);
        verify(event, newTransition);
        transitions.put(event, newTransition);
        return newTransition;
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     * @param event
     * @param newTransition
     */
    private void verify(E event, Transition<S,E,C> newTransition) {
        Transition existingTransition = transitions.get(event);
        if(existingTransition != null){
            if(existingTransition.equals(newTransition)){
                throw new StateMachineException(existingTransition+" already Exist, you can not add another one");
            }
        }
    }

    @Override
    public Optional<Transition<S, E, C>> getTransition(E event) {
        return Optional.ofNullable(transitions.get(event));
    }

    @Override
    public Collection<Transition<S, E, C>> getTransitions() {
        return transitions.values();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof State){
            State other = (State)anObject;
            if(this.stateId.equals(other.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return stateId.toString();
    }
}
