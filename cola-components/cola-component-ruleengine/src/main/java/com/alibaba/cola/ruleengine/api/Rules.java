package com.alibaba.cola.ruleengine.api;

import java.util.*;

public class Rules implements Iterable<Rule> {

    private List<Rule> rules = new ArrayList<>();

    /**
     * Create a new {@link Rules} object.
     *
     * @param rules to register
     */
    public Rules(Rule... rules) {
        Collections.addAll(this.rules, rules);
    }

    /**
     * Register one or more new rules.
     *
     * @param rules to register, must not be null
     */
    public void register(Rule... rules) {
        Objects.requireNonNull(rules);
        for (Rule rule : rules) {
            Objects.requireNonNull(rule);
            this.rules.add(rule);
        }
    }

    /**
     * Unregister one or more rules.
     *
     * @param rules to unregister, must not be null
     */
    public void unregister(Rule... rules) {
        Objects.requireNonNull(rules);
        for (Rule rule : rules) {
            Objects.requireNonNull(rule);
            this.rules.remove(rule);
        }
    }

    public void sortRulesByPriority(){
        Collections.sort(rules);
    }

    /**
     * Check if the rule set is empty.
     *
     * @return true if the rule set is empty, false otherwise
     */
    public boolean isEmpty() {
        return rules.isEmpty();
    }

    /**
     * Clear rules.
     */
    public void clear() {
        rules.clear();
    }

    /**
     * Return how many rules are currently registered.
     *
     * @return the number of rules currently registered
     */
    public int size() {
        return rules.size();
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }
}
