package com.alibaba.cola.domain.conventionsentity;

import com.alibaba.cola.domain.annotationentity.Address;

import java.util.List;
import java.util.Set;

/**
 * @author liuziyuan
 * @date 12/4/2020 4:13 PM
 */
public class UserE {
    private String name;

    private Integer age;

    private AddressV address;

    private List<String> stringList;

    private Set<String> stringSet;

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

    public AddressV getAddress() {
        return address;
    }

    public void setAddress(AddressV address) {
        this.address = address;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }
}
