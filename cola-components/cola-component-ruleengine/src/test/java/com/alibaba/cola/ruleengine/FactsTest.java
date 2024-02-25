package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Fact;
import com.alibaba.cola.ruleengine.api.Facts;

import java.util.Map;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FactsTest {

    private final Facts facts = new Facts();

    @Test
    public void factsMustHaveUniqueName() {
        facts.add(new Fact<>("foo", 1));
        facts.add(new Fact<>("foo", 2));

        assertThat(facts).hasSize(1);
        Fact<?> fact = facts.getFact("foo");
        assertThat(fact.getValue()).isEqualTo(2);
    }

    @Test
    public void testAdd() {
        Fact<Integer> fact1 = new Fact<>("foo", 1);
        Fact<Integer> fact2 = new Fact<>("bar", 2);
        facts.add(fact1);
        facts.add(fact2);

        assertThat(facts).contains(fact1);
        assertThat(facts).contains(fact2);
    }

    @Test
    public void testPut() {
        facts.put("foo", 1);
        facts.put("bar", 2);

        assertThat(facts).contains(new Fact<>("foo", 1));
        assertThat(facts).contains(new Fact<>("bar", 2));
    }

    @Test
    public void testRemove() {
        Fact<Integer> foo = new Fact<>("foo", 1);
        facts.add(foo);
        facts.remove(foo);

        assertThat(facts).isEmpty();
    }

    @Test
    public void testRemoveByName() {
        Fact<Integer> foo = new Fact<>("foo", 1);
        facts.add(foo);
        facts.remove("foo");

        assertThat(facts).isEmpty();
    }

    @Test
    public void testGet() {
        Fact<Integer> fact = new Fact<>("foo", 1);
        facts.add(fact);
        Integer value = facts.get("foo");
        assertThat(value).isEqualTo(1);
    }

    @Test
    public void testGetFact() {
        Fact<Integer> fact = new Fact<>("foo", 1);
        facts.add(fact);
        Fact<?> retrievedFact = facts.getFact("foo");
        assertThat(retrievedFact).isEqualTo(fact);
    }

    @Test
    public void testAsMap() {
        Fact<Integer> fact1 = new Fact<>("foo", 1);
        Fact<Integer> fact2 = new Fact<>("bar", 2);
        facts.add(fact1);
        facts.add(fact2);
        Map<String, Object> map = facts.asMap();
        assertThat(map).containsKeys("foo", "bar");
        assertThat(map).containsValues(1, 2);
    }

    @Test
    public void testClear() {
        Facts facts = new Facts();
        facts.add(new Fact<>("foo", 1));
        facts.clear();
        assertThat(facts).isEmpty();
    }

}
