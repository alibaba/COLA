package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.*;

import java.util.List;

/**
 * PlantUMLVisitor
 *
 * @author Frank Zhang
 * @date 2020-02-09 7:47 PM
 */
public class PlantUMLVisitor implements Visitor, StateChainVisitor {

    /**
     * Since the state machine is stateless, there is no initial state.
     *
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     * @return
     */
    @Override
    public String visitOnEntry(StateMachine<?, ?, ?> visitable) {
        return "@startuml" + LF;
    }

    @Override
    public String visitOnExit(StateMachine<?, ?, ?> visitable) {
        return "@enduml";
    }

    @Override
    public String visitOnEntry(StateChain<?, ?> visitable) {
        return "@startuml" + LF;
    }

    @Override
    public String visitOnExit(StateChain<?, ?> visitable) {
        return "@enduml";
    }

    @Override
    public <S, C, E> String visitOnStateChain(List<State<S, E, C>> chainStateList, E event) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chainStateList.size() - 1; i++) {
            sb.append(chainStateList.get(i).getId())
                    .append("->")
                    .append(chainStateList.get(i + 1).getId())
                    .append(": ")
                    .append(event)
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnEntry(State<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        for(Transition transition: state.getAllTransitions()){
            sb.append(transition.getSource().getId())
                    .append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?, ?> state) {
        return "";
    }
}
