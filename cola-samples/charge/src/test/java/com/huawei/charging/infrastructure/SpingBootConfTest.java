package com.huawei.charging.infrastructure;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
//use test profile, this will merge application.yml and application-test.yaml.
//but if you put an application.yml under test/resources, it will replace the project application.yml.
//the advantage of profile, is that it can inherit and override.
@ActiveProfiles("test")
public class SpingBootConfTest {

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${my-name}")
    private String myName;

    @Value("${my-age}")
    private String myAge;

    @Value("${my-age-test}")
    private String myAgeTest;


    @Test
    public void test() {
        System.out.println("spring.jpa.show-sql : " + showSql);
        System.out.println("spring.jpa.hibernate.ddl-auto : " + ddlAuto);
        System.out.println("myName : " + myName);
        System.out.println("myAge : " + myAge);
        System.out.println("myAgeTest : " + myAgeTest);

        Assertions.assertEquals("30", myAge);
        Assertions.assertEquals("40", myAgeTest);
    }
}
