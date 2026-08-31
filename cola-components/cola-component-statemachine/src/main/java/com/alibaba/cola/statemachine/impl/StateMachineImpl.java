package com.alibaba.cola.statemachine.impl;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateChain;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;
import com.alibaba.cola.statemachine.builder.FailCallback;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

    private String machineId;

    private final Map<S, State<S, E, C>> stateMap;

    private final Map<String, List<StateChain<S, E>>> stateChainMap;

    private boolean ready;

    private FailCallback<S, E, C> failCallback;

    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
        this.stateChainMap = new ConcurrentHashMap<>(32);
    }

    @Override
    public boolean verify(S sourceStateId, E event) {
        isReady();

        State sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        return transitions != null && transitions.size() != 0;
    }

    @Override
    public List<S> getTargetStates(S sourceStateId, E event) {
        isReady();
        State<S, E, C> sourceState = getState(sourceStateId);
        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);
        if (transitions == null || transitions.isEmpty()) {
            Debugger.debug("There is no Transition for " + event);
            return new ArrayList<>();
        }
        Set<S> targetStateSet = new LinkedHashSet<>();
        for (Transition<S, E, C> transition : transitions) {
            State<S, E, C> targetState = transition.getTarget();
            S targetStateId = targetState.getId();
            targetStateSet.add(targetStateId);
        }
        return new ArrayList<>(targetStateSet);
    }

    @Override
    public List<StateChain<S, E>> getTargetStateChain(S sourceStateId, E event) {
        isReady();
        String key = sourceStateId.toString() + ":" + event.toString();
        return stateChainMap.computeIfAbsent(key, k -> {
            State sourceState = getState(sourceStateId);
            List<Transition<S, E, C>> internalTransitions = sourceState.getEventTransitions(event,
                    TransitionType.INTERNAL);
            List<Transition<S, E, C>> externalTransitions = sourceState.getEventTransitions(event,
                    TransitionType.EXTERNAL);
            List<StateChain<S, E>> internalStateChains = buildInternalStateChains(sourceState, event,
                    internalTransitions);
            List<StateChain<S, E>> externalStateChains = buildExternalStateChains(sourceState, event,
                    externalTransitions);
            List<StateChain<S, E>> result = new ArrayList<>(internalStateChains);
            result.addAll(externalStateChains);
            return result;
        });
    }

    /**
     * Build state chains for internal transitions.
     * <p>
     * Internal transition means the source state and target state are the same.
     * Since all internal transitions on the same event have the same source state,
     * we only need to take the first one to get the target state.
     *
     * @param sourceState         the source state
     * @param event               the event that triggers the transition
     * @param internalTransitions list of internal transitions
     * @return list of state chains for internal transitions
     */
    private List<StateChain<S, E>> buildInternalStateChains(State<S, E, C> sourceState, E event,
            List<Transition<S, E, C>> internalTransitions) {
        List<StateChain<S, E>> internalStateChains = new ArrayList<>();
        if (!internalTransitions.isEmpty()) {
            // For internal transitions, source == target (same state), so we only need the
            // first one.
            // All internal transitions on the same event from the same source state have
            // identical result.
            State<S, E, C> targetState = internalTransitions.get(0).getSource();
            List<State<S, E, C>> targetStateIds = new ArrayList<>();
            targetStateIds.add(targetState);
            internalStateChains.add(new StateChainImpl<>(sourceState, event, targetStateIds));
        }
        return internalStateChains;
    }

    /**
     * Build state chains for external transitions using BFS algorithm.
     * <p>
     * This method finds all possible state chains from the source state through
     * external transitions triggered by the given event. It handles:
     * <ul>
     * <li>Linear paths: chains that end at a state with no further transitions</li>
     * <li>Cyclic paths: chains that loop back to a previously visited state</li>
     * </ul>
     * <p>
     * The algorithm uses a queue to explore all paths in breadth-first order,
     * storing complete paths to track visited states and detect cycles.
     *
     * @param sourceState         the source state
     * @param event               the event that triggers the transitions
     * @param externalTransitions list of external transitions from the source state
     * @return list of all possible state chains
     */
    private List<StateChain<S, E>> buildExternalStateChains(State<S, E, C> sourceState, E event,
            List<Transition<S, E, C>> externalTransitions) {
        List<StateChain<S, E>> externalStateChains = new ArrayList<>();
        Queue<List<State<S, E, C>>> pathQueue = new ArrayDeque<>();
        for (Transition<S, E, C> transition : externalTransitions) {
            State<S, E, C> targetState = transition.getTarget();
            List<State<S, E, C>> initialPath = new ArrayList<>();
            initialPath.add(sourceState);
            initialPath.add(targetState);
            pathQueue.add(initialPath);
        }
        while (!pathQueue.isEmpty()) {
            List<State<S, E, C>> currentPath = pathQueue.poll();
            State<S, E, C> lastState = currentPath.get(currentPath.size() - 1);
            State<S, E, C> state = getState(lastState.getId());
            List<Transition<S, E, C>> transitions = state.getEventTransitions(event, TransitionType.EXTERNAL);
            if (transitions.isEmpty()) {
                externalStateChains
                        .add(new StateChainImpl<>(sourceState, event, currentPath.subList(1, currentPath.size())));
            }
            for (Transition<S, E, C> transition : transitions) {
                State<S, E, C> targetState = transition.getTarget();
                if (currentPath.contains(targetState)) {
                    List<State<S, E, C>> cyclePath = new ArrayList<>(currentPath);
                    cyclePath.add(targetState);
                    externalStateChains
                            .add(new StateChainImpl<>(sourceState, event, cyclePath.subList(1, cyclePath.size())));
                } else {
                    List<State<S, E, C>> newPath = new ArrayList<>(currentPath);
                    newPath.add(targetState);
                    pathQueue.add(newPath);
                }
            }
        }
        return externalStateChains;
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        isReady();
        Transition<S, E, C> transition = routeTransition(sourceStateId, event, ctx);

        if (transition == null) {
            Debugger.debug("There is no Transition for " + event);
            failCallback.onFail(sourceStateId, event, ctx);
            return sourceStateId;
        }

        return transition.transit(ctx, false).getId();
    }

    @Override
    public List<S> fireParallelEvent(S sourceState, E event, C context) {
        isReady();
        List<Transition<S, E, C>> transitions = routeTransitions(sourceState, event, context);
        List<S> result = new ArrayList<>();
        if (transitions == null || transitions.isEmpty()) {
            Debugger.debug("There is no Transition for " + event);
            failCallback.onFail(sourceState, event, context);
            result.add(sourceState);
            return result;
        }
        for (Transition<S, E, C> transition : transitions) {
            S id = transition.transit(context, false).getId();
            result.add(id);
        }
        return result;
    }

    private Transition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        State sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.size() == 0) {
            return null;
        }

        Transition<S, E, C> transit = null;
        for (Transition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private List<Transition<S, E, C>> routeTransitions(S sourceStateId, E event, C context) {
        State sourceState = getState(sourceStateId);
        List<Transition<S, E, C>> result = new ArrayList<>();
        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);
        if (transitions == null || transitions.size() == 0) {
            return null;
        }

        for (Transition<S, E, C> transition : transitions) {
            Transition<S, E, C> transit = null;
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(context)) {
                transit = transition;
            }
            result.add(transit);
        }
        return result;
    }

    private State getState(S currentStateId) {
        State state = StateHelper.getState(stateMap, currentStateId);
        if (state == null) {
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (State state : stateMap.values()) {
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
    public String generatePlantUML() {
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

    public void setFailCallback(FailCallback<S, E, C> failCallback) {
        this.failCallback = failCallback;
    }
}
