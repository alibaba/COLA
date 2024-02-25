package com.alibaba.cola.ruleengine.core;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeRule extends AbstractRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeRule.class);

    private List<Rule> rules = new ArrayList<>();

    //对于CompositeRule组合规则，我们要定义rule和rule之间的关系，当前支持OR和AND两种关系。
    //or关系是只要有一条rule满足条件就执行，其余的忽略
    private boolean isOrRelation;
    //and关系是要所有的rules都满足条件才全部执行
    private boolean isAndRelation;

    private boolean isSorted=false;

    public static CompositeRule anyOf(Rule... rules) {
        CompositeRule instance = new CompositeRule();
        instance.isOrRelation = true;
        Collections.addAll(instance.rules, rules);
        return instance;
    }

    public static CompositeRule allOf(Rule... rules) {
        CompositeRule instance = new CompositeRule();
        instance.isAndRelation = true;
        Collections.addAll(instance.rules, rules);
        return instance;
    }

    public CompositeRule priority(int priority) {
        this.priority = priority;
        return this;
    }

    public CompositeRule name(String name){
        this.name = name;
        return this;
    }

    private CompositeRule() {

    }

    @Override
    public boolean evaluate(Facts facts) {
        if (isOrRelation) {
            if (rules.stream().anyMatch(rule -> rule.evaluate(facts)))
                return true;
        }
        if (isAndRelation) {
            if (rules.stream().allMatch(rule -> rule.evaluate(facts)))
                return true;
        }
        return false;
    }

    /**
     * CompositeRule的execute
     *
     * @param facts
     */
    @Override
    public void execute(Facts facts) {
        //不支持OR relation
        if (isOrRelation) {
            throw new RuntimeException("execute not supported for OR relation composite");
        }
        //对于AND 就是执行all
        if (isAndRelation) {
            for (Rule rule : rules) {
                rule.execute(facts);
            }
        }
    }

    @Override
    public boolean apply(Facts facts) {
        sort();
        if (isOrRelation) {
            LOGGER.debug("start OR composite rule apply");
            for (Rule rule : rules) {
                //短路操作，只要第一个rule成功执行，其它就不执行了
                if (rule.apply(facts)) {
                    return true;
                }
            }
        }
        if (isAndRelation) {
            LOGGER.debug("start AND composite rule apply");
            if (evaluate(facts)) {
                for (Rule rule : rules) {
                    //所有的rules都执行
                    rule.execute(facts);
                }
                return true;
            }
        }
        return false;
    }

    private void sort(){
        if(!isSorted){
            LOGGER.debug(this.name+" before sort:" + rules);
            Collections.sort(rules);
            LOGGER.debug(this.name+" after sort:" + rules);
            isSorted = true;
        }
    }
}
