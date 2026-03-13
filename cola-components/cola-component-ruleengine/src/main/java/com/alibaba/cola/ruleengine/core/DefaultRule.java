/*
 * The MIT License
 *
 *  Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package com.alibaba.cola.ruleengine.core;

import com.alibaba.cola.ruleengine.api.Action;
import com.alibaba.cola.ruleengine.api.Condition;
import com.alibaba.cola.ruleengine.api.Facts;

import java.util.ArrayList;
import java.util.List;

public class DefaultRule extends AbstractRule {

    private final Condition condition;
    private final List<Action> actions;

   public DefaultRule(Condition condition, Action action){
        this.condition = condition;
        this.actions = new ArrayList<>();
        this.actions.add(action);
    }

    public DefaultRule(Condition condition, List<Action> actions){
        this.condition = condition;
        this.actions = actions;
    }

    public DefaultRule(String name, String description, int priority, Condition condition, List<Action> actions) {
        super(name, description, priority);
        this.condition = condition;
        this.actions = actions;
    }

    @Override
    public boolean evaluate(Facts facts) {
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) {
        for (Action action : actions) {
            action.execute(facts);
        }
    }

    @Override
    public boolean apply(Facts facts){
       if (condition.evaluate(facts)){
           for (Action action : actions) {
               action.execute(facts);
           }
           return true;
       }
       return false;
    }

}
