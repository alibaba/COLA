package com.alibaba.cola.ruleengine.fizzbuzz.v2;

/**
 * 计算倍数关系的谓词逻辑
 */
public class TimesCondition {
    public static Condition times(int i){
        return n -> n % i == 0;
    }
}

