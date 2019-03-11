package com.alibaba.cola.mock.scan;

import java.io.Serializable;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/24
 */
public interface TypeFilter extends Serializable{
    public boolean match(Class clazz);
    public String getUid();

}
