package com.alibaba.cola.test.extension;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;

/**
 * DogExt
 *
 * @author Frank Zhang
 * @date 2019-01-02 9:42 PM
 */
@Extension(bizCode = "ali.animal.dog")
public class DogExt implements AnimalExtPt{

    @Override
    public String eat() {
        System.out.println("Dog Eat, Wow");
        return "Dog";
    }
}
