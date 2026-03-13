package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.core.AllRules;
import com.alibaba.cola.ruleengine.core.AnyRules;
import com.alibaba.cola.ruleengine.core.DefaultRuleEngine;
import com.alibaba.cola.ruleengine.core.RuleBuilder;

public class FizzBuzz {
    public static void main(String[] args) {
        Rule fizzBuzzRule = assembleFizzBuzzRule();
        RuleEngine ruleEngine = new DefaultRuleEngine();
        for (int i = 0; i <= 15; i++) {
            Facts facts = new Facts();
            facts.put("number", i);
            ruleEngine.fire(fizzBuzzRule, facts);
            System.out.println();
        }
    }

    public static Rule assembleFizzBuzzRule() {
        Rule fizzRule = new RuleBuilder()
                .name("fizzRule")
                .description("fizz rule when input times 3, output is Fizz")
                .priority(10)
                .when(facts -> (int) facts.get("number") % 3 == 0)
                .then(facts -> System.out.print("Fizz"))
                .build();

        Rule buzzRule = new RuleBuilder()
                .name("buzzRule")
                .description("buzz rule when input times 5, output is buzz")
                .priority(20)
                .when(facts -> (int) facts.get("number") % 5 == 0)
                .then(facts -> System.out.print("Buzz"))
                .build();

        Rule fizzBuzzRule = AllRules.allOf(fizzRule, buzzRule)
                .name("fizzBuzzRule")
                .priority(1);

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
