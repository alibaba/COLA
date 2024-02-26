package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.core.AbstractRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityTest {

    @Test
    public void testNoPriority() {
        DummyRule r1 = new DummyRule();
        DummyRule r2 = new DummyRule();
        DummyRule r3 = new DummyRule();

//        assertThat(rules).startsWith(r1).endsWith(r3);
    }

    @Test
    public void testPriority(){
        DummyRule r1 = new DummyRule(10);
        DummyRule r2 = new DummyRule(3);
        DummyRule r3 = new DummyRule(1);


//        assertThat(rules).startsWith(r3).endsWith(r1);
    }




    static class DummyRule extends AbstractRule {

        public DummyRule(){

        }

        public DummyRule(int priority){
            super(priority);
        }

        @Override
        public boolean evaluate(Facts facts) {
            return false;
        }

        @Override
        public void execute(Facts facts) {

        }

        @Override
        public boolean apply(Facts facts) {
            return false;
        }
    }
}

