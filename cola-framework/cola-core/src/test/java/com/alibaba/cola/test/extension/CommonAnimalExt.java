package com.alibaba.cola.test.extension;

import com.alibaba.cola.extension.Extension;

/**
 * CommonAnimalExt
 *
 * @author Frank Zhang
 * @date 2019-01-02 10:05 PM
 */
@Extension(bizCode = "ali.animal")
public class CommonAnimalExt  implements AnimalExtPt{
    @Override
    public String eat() {
        System.out.println("Common Animal Eat, Terrific");
        return "CommonAnimal";
    }
}
