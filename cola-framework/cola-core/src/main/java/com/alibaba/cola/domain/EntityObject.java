package com.alibaba.cola.domain;

import com.alibaba.cola.context.Context;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Entity Object
 *
 * This is the parent object of all domain objects
 * @author fulan.zjf 2017年10月27日 上午10:16:10
 */
public abstract class EntityObject {

	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

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
