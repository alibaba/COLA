package com.alibaba.sofa.domain;

/**
 * 领域工厂
 * @author xueliang.sxl
 *
 */
public interface DomainFactoryI<T extends Entity> {

	T create();

}
