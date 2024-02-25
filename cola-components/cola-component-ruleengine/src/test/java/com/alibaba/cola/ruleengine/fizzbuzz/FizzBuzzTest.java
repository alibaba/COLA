package com.alibaba.cola.ruleengine.fizzbuzz;

//import com.alibaba.cola.ruleengine.fizzbuzz.v1.FizzBuzz;
import com.alibaba.cola.ruleengine.fizzbuzz.v2.FizzBuzz;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FizzBuzzTest {

    @Test
    public void num_given_1() {
        //given
        int input = 1;
        //when
        String result = FizzBuzz.count(input);
        //then
        Assertions.assertThat(result).isEqualTo("1");
    }

    @Test
    public void fizz_given_3() {
        //given
        int input = 3;
        //when
        String result = FizzBuzz.count(input);
        //then
        Assertions.assertThat(result).isEqualTo("Fizz");

    }

    @Test
    public void buzz_given_5() {
        //given
        int input = 5;
        //when
        String result = FizzBuzz.count(input);
        //then
        Assertions.assertThat(result).isEqualTo("Buzz");
    }

    @Test
    public void fizz_buzz_given_15() {
        //given
        int input = 15;
        //when
        String result = FizzBuzz.count(input);
        //then
        Assertions.assertThat(result).isEqualTo("FizzBuzz");
    }
}
