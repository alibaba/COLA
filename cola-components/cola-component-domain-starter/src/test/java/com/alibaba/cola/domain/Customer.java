package com.alibaba.cola.domain;

import com.alibaba.cola.domain.Entity;

import javax.annotation.Resource;

/**
 * Customer
 *
 * @author Frank Zhang
 * @date 2020-11-14 2:43 PM
 */
@Entity
public class Customer {
    private String name;

    private Integer age;

    @Resource
    private PurchasePowerGateway purchasePowerGateway;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPurchasePowerScore(){
        return purchasePowerGateway.getScore();
    }
}
