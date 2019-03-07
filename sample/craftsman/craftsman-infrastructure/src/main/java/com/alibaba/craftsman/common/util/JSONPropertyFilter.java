package com.alibaba.craftsman.common.util;

import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * JSONPropertyFilter
 *
 * @author Frank Zhang
 * @date 2019-03-02 10:53 PM
 */
public class JSONPropertyFilter implements PropertyFilter{

    public static JSONPropertyFilter singleton = new JSONPropertyFilter();

    @Override
    public boolean apply(Object object, String name, Object value) {
        if(name.equalsIgnoreCase("context")){
            return false;
        }
        if(name.equalsIgnoreCase("extValues")){
            return false;
        }
        return true;
    }
}
