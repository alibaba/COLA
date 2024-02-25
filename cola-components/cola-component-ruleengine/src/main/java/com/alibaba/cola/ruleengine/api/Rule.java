package com.alibaba.cola.ruleengine.api;

public interface Rule extends Comparable<Rule> {

    /**
     * Default rule name.
     */
    String DEFAULT_NAME = "rule";

    /**
     * Default rule description.
     */
    String DEFAULT_DESCRIPTION = "description";

    /**
     * Default rule priority.
     */
    int DEFAULT_PRIORITY = Integer.MAX_VALUE - 1;

    /**
     * Getter for rule name.
     * @return the rule name
     */
    default String getName() {
        return DEFAULT_NAME;
    }

    /**
     * Getter for rule description.
     * @return rule description
     */
    default String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    /**
     * Getter for rule priority.
     * @return rule priority
     */
    default int getPriority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * This method implements the rule's condition(s).
     * <strong>Implementations should handle any runtime exception and return true/false accordingly</strong>
     *
     * @return true if the rule should be applied given the provided facts, false otherwise
     */
    boolean evaluate(Facts facts);

    /**
     * This method implements the rule's action(s).
     * @throws Exception thrown if an exception occurs when performing action(s)
     */
    void execute(Facts facts);

    /**
     * This method apply facts to the rule, which is the combination of evaluation and execution
     * @param facts
     * @return true if this rule is applied successfully, false otherwise
     */
    boolean apply(Facts facts);

}
