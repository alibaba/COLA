package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;

import java.util.HashMap;
import java.util.Map;

/**
 * StateHelper
 *
 * @author Frank Zhang
 * @date 2020-02-08 4:23 PM
 */
public class StateHelper {

    private StateHelper() {
    }

    public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId) {
        return stateMap.computeIfAbsent(stateId, StateImpl::new);
    }

    static <S, E, C> Map<S, State<S, E, C>> cloneStateMap(Map<S, State<S, E, C>> stateMap) {
        Map<S, State<S, E, C>> cloneStateMap = new HashMap<>();
        stateMap.forEach((stateId, state) -> {
            State<S, E, C> cloneState = getState(cloneStateMap, stateId);
            state.getAllTransitions().forEach(transition -> {
                State<S, E, C> target = transition.getTarget();
                Transition<S, E, C> cloneTransition = cloneState.addTransition(transition.getEvent(),
                    getState(cloneStateMap, target.getId()), transition.getType());
                cloneTransition.setCondition(transition.getCondition());
                cloneTransition.setAction(transition.getAction());
            });
        });
        return cloneStateMap;
    }
}
