package com.alibaba.sofa.repository;

import java.util.List;

/**
 * Data Tunnel is the real Data CRUD Operator may interact with DB, service, cache etc...
 * 
 * @author fulan.zjf 2017年10月27日 上午10:34:17
 */
public interface DataTunnel<T extends DataObject> {

    public T create(T dataObject);

    default public void delete(T dataObject) {};

    default public void update(T dataObject){};

    public T get(String id);

    public List<T> getByEntity(T entity);
}
