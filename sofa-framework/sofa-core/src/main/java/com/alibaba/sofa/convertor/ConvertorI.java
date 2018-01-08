package com.alibaba.sofa.convertor;

/**
 * Convertor  are used to convert Objects among Client Object, Domain Object and Data Object.
 *
 * @author fulan.zjf on 2017/12/16.
 */
public interface ConvertorI<C, E, D> {

    default public C entityToClient(E entityObject){return null;}

    default public C dataToClient(D dataObject){return null;}

    default public E clientToEntity(C clientObject){return null;}

    default public E dataToEntity(D dataObject){return null;}

    default public D entityToData(E entityObject){return null;}

}

