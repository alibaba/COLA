package com.alibaba.sofa.domain;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * 聚合内的实体
 * This is the parent object of all domain objects
 * @author fulan.zjf 2017年10月27日 上午10:16:10
 */
public abstract class Entity  {

    /*
     * Entity 包含所有表都有的6个基础字段.
     */
	@Getter @Setter
    protected String id;
	@Getter @Setter
	protected Date gmtCreate;
	@Getter @Setter
	protected Date gmtModified;
	@Getter @Setter
	protected String creator;
	@Getter @Setter
	protected String modifier;
	@Getter @Setter
	protected String isDeleted;
	@Getter @Setter
	protected String tenantId;//租户ID
	@Getter @Setter
	protected String bizCode;//业务代码

	/*
	 * 扩展字段
	 */
	@Getter
	@Setter
	protected Map<String, Object> extValues = new ConcurrentHashMap<String, Object>();

    public<T> T getExtField(String key){
        if(extValues != null){
            return (T)extValues.get(key);
        }
        return null;
    }

    public void putExtField(String fieldName, Object value){
        this.extValues.put(fieldName, value);
    }

}
