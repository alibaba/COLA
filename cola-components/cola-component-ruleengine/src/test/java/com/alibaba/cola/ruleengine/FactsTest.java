package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Fact;
import com.alibaba.cola.ruleengine.api.Facts;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class FactsTest {

    private final Facts facts = new Facts();

    @Test
    public void factsMustHaveUniqueName() {
        facts.add(new Fact<>("foo", 1));
        facts.add(new Fact<>("foo", 2));


        Fact<?> fact = facts.getFact("foo");
        assertEquals(fact.getValue(),2);
    }

    @Test
    public void testAdd() {
        Fact<Integer> fact1 = new Fact<>("foo", 1);
        Fact<Integer> fact2 = new Fact<>("bar", 2);
        facts.add(fact1);
        facts.add(fact2);

        assertTrue(facts.contains(fact1));
        assertTrue(facts.contains(fact2));
    }

    @Test
    public void testPut() {
        facts.put("foo", 1);
        facts.put("bar", 2);

        assertTrue(facts.contains(new Fact<>("foo", 1)));
        assertTrue(facts.contains(new Fact<>("bar", 2)));
    }

    @Test
    public void testRemove() {
        Fact<Integer> foo = new Fact<>("foo", 1);
        facts.add(foo);
        facts.remove(foo);

        assertTrue(facts.size() == 0);
    }

    @Test
    public void testRemoveByName() {
        Fact<Integer> foo = new Fact<>("foo", 1);
        facts.add(foo);
        facts.remove("foo");

        assertTrue(facts.size() == 0);
    }

    @Test
    public void testGet() {
        Fact<Integer> fact = new Fact<>("foo", 1);
        facts.add(fact);
        Integer value = facts.get("foo");
        assertEquals(value, 1);
    }

    @Test
    public void testGetFact() {
        Fact<Integer> fact = new Fact<>("foo", 1);
        facts.add(fact);
        Fact<?> retrievedFact = facts.getFact("foo");
        assertEquals(retrievedFact, fact);
    }

    @Test
    public void testAsMap() {
        Fact<Integer> fact1 = new Fact<>("foo", 1);
        Fact<Integer> fact2 = new Fact<>("bar", 2);
        facts.add(fact1);
        facts.add(fact2);
        Map<String, Object> map = facts.asMap();
        assertTrue(map.containsKey("foo"));
        assertTrue(map.containsKey("bar"));
    }

    @Test
    public void testClear() {
        Facts facts = new Facts();
        facts.add(new Fact<>("foo", 1));
        facts.clear();

        assertTrue(facts.size() == 0);
    }

}
