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

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;

import java.util.Objects;

/**
 * Basic rule implementation class that provides common methods.
 * <p>
 * You can extend this class and override {@link AbstractRule#evaluate(Facts)} and {@link AbstractRule#execute(Facts)} to provide rule
 * conditions and actions logic.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class AbstractRule implements Rule {

    /**
     * Rule name.
     */
    protected String name;

    /**
     * Rule description.
     */
    protected String description;

    /**
     * Rule priority.
     */
    protected int priority;

    /**
     * Create a new {@link AbstractRule}.
     */
    public AbstractRule() {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    /**
     * Create a new {@link AbstractRule}.
     *
     * @param name rule name
     */
    public AbstractRule(final String name) {
        this(name, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public AbstractRule(final int priority) {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, priority);
    }

    /**
     * Create a new {@link AbstractRule}.
     *
     * @param name        rule name
     * @param description rule description
     */
    public AbstractRule(final String name, final String description) {
        this(name, description, Rule.DEFAULT_PRIORITY);
    }

    /**
     * Create a new {@link AbstractRule}.
     *
     * @param name        rule name
     * @param description rule description
     * @param priority    rule priority
     */
    public AbstractRule(final String name, final String description, final int priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public abstract boolean evaluate(Facts facts);

    public abstract void execute(Facts facts);

    public abstract boolean apply(Facts facts);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /*
     * Rules are unique according to their names within a rules engine registry.
     */

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AbstractRule basicRule = (AbstractRule) o;

        if (priority != basicRule.priority)
            return false;
        if (!name.equals(basicRule.name))
            return false;
        return Objects.equals(description, basicRule.description);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }

    @Override
    public String toString() {
        return name + ":" + priority;
    }

    @Override
    public int compareTo(final Rule rule) {
        if (getPriority() < rule.getPriority()) {
            return -1;
        } else if (getPriority() > rule.getPriority()) {
            return 1;
        }
        return 0;
    }

}
