package com.alibaba.cola.unittest.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Person> find(String name) {
        Query query = entityManager.createNamedQuery("Person.find");
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    public void remove(int personId) {
        Person person = entityManager.find(Person.class, personId);
        entityManager.remove(person);
    }

}
