package com.alibaba.cola.test.extension;

import com.alibaba.cola.extension.Extension;

/**
 * DefaultAnimalExt
 *
 * @author Frank Zhang
 * @date 2019-01-02 9:59 PM
 */
@Extension
public class DefaultAnimalExt implements AnimalExtPt{
    @Override
    public String eat() {
        System.out.println("Default Animal Eat, Cool");
        return "DefaultAnimal";
    }
}
