package com.alibaba.cola.domain.annotationentity;

import com.alibaba.cola.domain.Entity;

/**
 * @author liuziyuan
 * @date 12/4/2020 1:45 PM
 */
@Entity
public class Address {
    private String street;
    private String city;
    private ZipCode zipCode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }
}
