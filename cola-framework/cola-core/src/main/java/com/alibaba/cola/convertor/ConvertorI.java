package com.alibaba.cola.convertor;

/**
 * Convertor  are used to convert Objects among Client Object, Domain Object and Data Object.
 *
 * @author fulan.zjf on 2017/12/16.
 */
public interface ConvertorI<C, E, D> {

    //Convert entity object to client object
    default public C entityToClient(E entityObject){return null;}

    //Convert entity object to data object
    default public D entityToData(E entityObject){return null;}

    //Convert data object to client object
    default public C dataToClient(D dataObject){return null;}

    //Convert data object to entity object
    default public E dataToEntity(D dataObject){return null;}

    //Convert client object to entity object
    default public E clientToEntity(C clientObject){return null;}
}

