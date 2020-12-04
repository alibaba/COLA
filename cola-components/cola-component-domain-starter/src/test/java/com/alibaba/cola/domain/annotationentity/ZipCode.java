package com.alibaba.cola.domain.annotationentity;

import com.alibaba.cola.domain.Entity;

/**
 * @author liuziyuan
 * @date 12/4/2020 2:00 PM
 */
@Entity
public class ZipCode {
    private String zipcode;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
