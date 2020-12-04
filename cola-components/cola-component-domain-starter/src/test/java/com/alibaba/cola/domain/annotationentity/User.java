package com.alibaba.cola.domain.annotationentity;

import com.alibaba.cola.domain.Entity;

/**
 * @author liuziyuan
 * @date 12/4/2020 2:53 PM
 */
@Entity
public class User {
    private String name;

    private Integer age;

    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
