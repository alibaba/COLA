package com.alibaba.cola.ruleengine.fizzbuzz.v2;

import static com.alibaba.cola.ruleengine.fizzbuzz.v2.SimpleRuleEngine.*;
import static com.alibaba.cola.ruleengine.fizzbuzz.v2.TimesCondition.times;

/**
 * 用简易规则引擎重构后的FizzBuzz实现
 */
public class FizzBuzz {
    public static String count(int i){
        //Composite condition
        Rule fizzBuzzRule = atom(times(3).and(times(5)), n -> "FizzBuzz");
        Rule fizzRule = atom(times(3) , n -> "Fizz");
        Rule buzzRule = atom(times(5), n -> "Buzz");
        //Composite rule
        Rule compositeFizzBuzzRule = allOf(fizzRule, buzzRule);
        Rule defaultRule = atom(n -> true, n -> String.valueOf(n));
        Rule rule = anyOf(compositeFizzBuzzRule, fizzRule, buzzRule, defaultRule);
        return rule.apply(i);
    }
}
