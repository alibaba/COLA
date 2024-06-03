package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.core.DefaultRule;
import com.alibaba.cola.ruleengine.core.DefaultRuleEngine;
import com.alibaba.cola.ruleengine.core.NaturalRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PriorityTest {

    RuleEngine ruleEngine;

    @BeforeEach
    public void setUp() {
        ruleEngine = new DefaultRuleEngine();
    }


    @Test
    public void testNoPriority() {
        DummyRule r1 = new DummyRule(1);
        DummyRule r2 = new DummyRule(2);
        DummyRule r3 = new DummyRule(3);

//        assertThat(rules).startsWith(r1).endsWith(r3);
    }

    @Test
    public void testPriority() {
        DummyRule r1 = new DummyRule(10);
        DummyRule r2 = new DummyRule(3);
        DummyRule r3 = new DummyRule(1);
//        assertThat(rules).startsWith(r3).endsWith(r1);
    }

    @Test
    public void test_natural_rule() {
        DummyRule r1 = new DummyRule(10);
        DummyRule r2 = new DummyRule(3);
        DummyRule r3 = new DummyRule(1);
        Facts facts = new Facts();
        facts.put("number", 15);

        Rule naturalRules = NaturalRules.of(r1, r2, r3);
        ruleEngine.fire(naturalRules, facts);
    }


    static class DummyRule extends DefaultRule {


        public DummyRule(int priority) {
            super("rule" + priority, null, priority, facts -> true, new ArrayList<>());
        }

        @Override
        public boolean evaluate(Facts facts) {
            return true;
        }

        @Override
        public void execute(Facts facts) {
            System.out.println(facts.getFact("number").getValue());
        }

        @Override
        public boolean apply(Facts facts) {
            System.out.println(name + ": " + facts.getFact("number").getValue());
            return true;
        }
    }
}

