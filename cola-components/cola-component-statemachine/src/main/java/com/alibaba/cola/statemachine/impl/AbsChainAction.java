package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Action;

import java.util.Objects;

/**
 * @author wuxin
 * @date 2024/10/14 23:56:10
 */
public abstract class AbsChainAction<S,E,C> implements Action<S,E,C> {

    private Action next;

    @Override
    public void execute(S from, S to, E event, C context) {
        doExecute(from, to, event, context);
        if(Objects.nonNull(next)){
            next.execute(from, to, event, context);
        }
    }

    protected abstract void doExecute(S from, S to, E event, C context);

    public AbsChainAction next(Action action){
        this.next = action;
        return this;
    }
}
