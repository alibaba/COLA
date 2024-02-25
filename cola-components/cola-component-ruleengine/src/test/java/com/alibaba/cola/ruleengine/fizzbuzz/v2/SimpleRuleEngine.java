package com.alibaba.cola.ruleengine.fizzbuzz.v2;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通过原子atom rule，以及atom rule之间的组合解决FizzBuzz问题
 * 这里为了简单使用Rule的组合模式代替了RuleEngine实体
 * 注意：这个SimpleRuleEngine只能解决输入为n，输出为String的FizzBuzz问题
 * 完全不具备通用性
 */
public class SimpleRuleEngine {
    public static Rule atom(Condition condition, Action action){
        return n -> condition.evaluate(n) ? action.execute(n) : "";
    }

    public static Rule anyOf(Rule... rules){
        return n -> stringStream(n, rules).filter(s -> !s.isEmpty()).findFirst().get();
    }

    public static Rule allOf(Rule... rules){
        return n -> stringStream(n, rules).collect(Collectors.joining());
    }

    public static Stream<String> stringStream(int n, Rule[] rules){
        return Arrays.stream(rules).map(r -> r.apply(n));
    }
}
