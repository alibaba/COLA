package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.core.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompositeRuleTest {

    RuleEngine fizzBuzzEngine;

    @BeforeEach
    public void setUp(){
        fizzBuzzEngine = new DefaultRuleEngine();
    }



    @Test
    public void test_fizz_first(){
        Facts facts = new Facts();
        facts.put("number", 15);

        Rule rule = assembleRules(1,2,3);

        fizzBuzzEngine.fire(rule, facts);
        assertEquals(facts.getFact("fizz").getValue(), "Fizz");
    }

    @Test
    public void test_buzz_first(){
        Facts facts = new Facts();
        facts.put("number", 15);

        Rule rule = assembleRules(2,1,3);

        fizzBuzzEngine.fire(rule, facts);
        assertEquals(facts.getFact("buzz").getValue(), "Buzz");
    }

    @Test
    public void test_fizzBuzz_first(){
        Facts facts = new Facts();
        facts.put("number", 15);

        Rule rule = assembleRules(2,3,1);

        fizzBuzzEngine.fire(rule, facts);
        assertEquals(facts.getFact("fizz").getValue(), "Fizz");
        assertEquals(facts.getFact("buzz").getValue(), "Buzz");
    }


    private Rule assembleRules(int fizzPriority, int BuzzPriority, int FizzBuzzPriority){
        // create rules
        Rule fizzRule = new RuleBuilder()
                .name("fizzRule")
                .description("fizz rule when input times 3, output is Fizz")
                .priority(fizzPriority)
                .when(facts -> (int) facts.get("number") % 3 == 0)
                .then(facts -> facts.put("fizz","Fizz"))
                .build();

        Rule buzzRule = new RuleBuilder()
                .name("buzzRule")
                .description("buzz rule when input times 5, output is buzz")
                .priority(BuzzPriority)
                .when(facts -> (int) facts.get("number") % 5 == 0)
                .then(facts -> facts.put("buzz","Buzz"))
                .build();


        Rule fizzBuzzRule = AllRules.allOf(fizzRule, buzzRule)
                .name("fizzBuzzRule")
                .priority(FizzBuzzPriority);

        Rule defaultRule = new RuleBuilder()
                .name("defaultRule")
                .description("default rule, output number")
                .priority(40)
                .when(facts -> true)
                .then(facts -> System.out.print((int) facts.get("number")))
                .build();

        Rule rule = AnyRules.anyOf(fizzBuzzRule, fizzRule, buzzRule, defaultRule)
                .name("anyRule");

        return rule;
    }
}
