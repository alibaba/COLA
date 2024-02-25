package com.alibaba.cola.ruleengine.api;

import java.util.*;

public class Facts implements Iterable<Fact<?>> {

    private final Set<Fact<?>> facts = new HashSet<>();

    /**
     * Add a fact, replacing any fact with the same name.
     *
     * @param name of the fact to add, must not be null
     * @param value of the fact to add, must not be null
     */
    public <T> void put(String name, T value) {
        Objects.requireNonNull(name, "fact name must not be null");
        Objects.requireNonNull(value, "fact value must not be null");
        Fact<?> retrievedFact = getFact(name);
        if (retrievedFact != null) {
            remove(retrievedFact);
        }
        add(new Fact<>(name, value));
    }

    /**
     * Add a fact, replacing any fact with the same name.
     *
     * @param fact to add, must not be null
     */
    public <T> void add(Fact<T> fact) {
        Objects.requireNonNull(fact, "fact must not be null");
        Fact<?> retrievedFact = getFact(fact.getName());
        if (retrievedFact != null) {
            remove(retrievedFact);
        }
        facts.add(fact);
    }

    /**
     * Remove a fact by name.
     *
     * @param factName name of the fact to remove, must not be null
     */
    public void remove(String factName) {
        Objects.requireNonNull(factName, "fact name must not be null");
        Fact<?> fact = getFact(factName);
        if (fact != null) {
            remove(fact);
        }
    }

    /**
     * Remove a fact.
     *
     * @param fact to remove, must not be null
     */
    public <T> void remove(Fact<T> fact) {
        Objects.requireNonNull(fact, "fact must not be null");
        facts.remove(fact);
    }

    /**
     * Get the value of a fact by its name. This is a convenience method provided
     * as a short version of {@code getFact(factName).getValue()}.
     *
     * @param factName name of the fact, must not be null
     * @param <T> type of the fact's value
     * @return the value of the fact having the given name, or null if there is
     * no fact with the given name
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String factName) {
        Objects.requireNonNull(factName, "fact name must not be null");
        Fact<?> fact = getFact(factName);
        if (fact != null) {
            return (T) fact.getValue();
        }
        return null;
    }

    /**
     * Get a fact by name.
     *
     * @param factName name of the fact, must not be null
     * @return the fact having the given name, or null if there is no fact with the given name
     */
    public Fact<?> getFact(String factName) {
        Objects.requireNonNull(factName, "fact name must not be null");
        return facts.stream()
                .filter(fact -> fact.getName().equals(factName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Return a copy of the facts as a map. It is not intended to manipulate
     * facts outside of the rules engine (aka other than manipulating them through rules).
     *
     * @return a copy of the current facts as a {@link HashMap}
     */
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        for (Fact<?> fact : facts) {
            map.put(fact.getName(), fact.getValue());
        }
        return map;
    }

    /**
     * Return an iterator on the set of facts. It is not intended to remove
     * facts using this iterator outside of the rules engine (aka other than doing it through rules)
     *
     * @return an iterator on the set of facts
     */
    @Override
    public Iterator<Fact<?>> iterator() {
        return facts.iterator();
    }

    /**
     * Clear facts.
     */
    public void clear() {
        facts.clear();
    }

    @Override
    public String toString() {
        Iterator<Fact<?>> iterator = facts.iterator();
        StringBuilder stringBuilder = new StringBuilder("[");
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next().toString());
            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
