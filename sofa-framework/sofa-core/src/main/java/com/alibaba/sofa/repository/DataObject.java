package com.alibaba.sofa.repository;

import java.io.Serializable;

/**
 * This is the parent of all Data Objects.
 * Data object only has fields and according getters and setters, you can also call it anemic objects
 * @author fulan.zjf 2017年10月27日 上午10:21:01
 */
public interface DataObject<T> extends Serializable {
}
