package com.alibaba.cola.unittest.db;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 关于TestExecutionListener： https://www.baeldung.com/spring-testexecutionlistener
@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class DBSetupTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DatabaseSetup("/fixture/db/sample-data.xml")
    public void testFind() throws Exception {
        List<Person> personList = personRepository.find("hil");
        System.out.println(personList);
        assertEquals(1, personList.size());
        assertEquals("Phillip", personList.get(0).getFirstName());
    }
}
