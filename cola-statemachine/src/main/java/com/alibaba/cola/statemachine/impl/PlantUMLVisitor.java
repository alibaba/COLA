package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

/**
 * PlantUMLVisitor
 *
 * @author Frank Zhang
 * @date 2020-02-09 7:47 PM
 */
public class PlantUMLVisitor implements Visitor {

    /**
     * Since the state machine is stateless, there is no initial state.
     *
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     */
    @Override
    public void visitOnEntry(StateMachine<?, ?, ?> visitable) {
        System.out.println("@startuml");
    }

    @Override
    public void visitOnExit(StateMachine<?, ?, ?> visitable) {
        System.out.println("@enduml");
    }

    @Override
    public void visitOnEntry(State<?, ?, ?> state) {
        for(Transition transition: state.getTransitions()){
            System.out.println(transition.getSource().getId()+" --> "+transition.getTarget().getId()+" : "+transition.getEvent());
        }
    }

    @Override
    public void visitOnExit(State<?, ?, ?> state) {
    }
}
