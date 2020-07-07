package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

import java.util.Map;
import java.util.Optional;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 *
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S,E,C> implements StateMachine<S, E, C> {

    private String machineId;

    private final Map<S, State<S,E,C>> stateMap;

    private boolean ready;

    public StateMachineImpl(Map<S, State< S, E, C>> stateMap){
        this.stateMap = stateMap;
    }

    public S fireEvent(S sourceStateId, E event, C ctx){
        isReady();
        State sourceState = getState(sourceStateId);
        return doTransition(sourceState, event, ctx).getId();
    }

    private State<S, E, C> doTransition(State sourceState, E event, C ctx) {
        Optional<Transition<S,E,C>> transition = sourceState.getTransition(event);
        if(transition.isPresent()){
            return transition.get().transit(ctx);
        }
        Debugger.debug("There is no Transition for " + event);
        return sourceState;
    }

    private State getState(S currentStateId) {
        State state = StateHelper.getState(stateMap, currentStateId);
        if(state == null){
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    private void isReady() {
        if(!ready){
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for(State state: stateMap.values()){
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUML(){
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        return accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
