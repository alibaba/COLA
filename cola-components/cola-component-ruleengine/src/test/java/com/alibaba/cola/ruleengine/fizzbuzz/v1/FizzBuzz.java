package com.alibaba.cola.ruleengine.fizzbuzz.v1;

public class FizzBuzz {
    public static String count(int n){
        if (((n % 3) == 0) && ((n % 5) == 0))
            return "FizzBuzz";
        if ((n % 3) == 0)
            return "Fizz";
        if ((n % 5) == 0)
            return "Buzz";
        return String.valueOf(n);
    }
}
