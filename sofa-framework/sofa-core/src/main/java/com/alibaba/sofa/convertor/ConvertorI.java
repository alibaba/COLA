package com.alibaba.sofa.convertor;

/**
 * 适用于两个对象的转换
 * @author fulan.zjf on 2017/12/16.
 */
public interface ConvertorI<F, T> {

    default public T convert(F fromObject){return null;}

    default public void convert(F fromObject, T toObject){}
}

