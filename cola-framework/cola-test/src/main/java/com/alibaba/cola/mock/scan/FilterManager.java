package com.alibaba.cola.mock.scan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shawnzhan.zxy
 * @since 2018/10/07
 */
public class FilterManager {
    private Set<TypeFilter> filterList = new HashSet<>();

    public void addFilter(TypeFilter typeFilter){
        filterList.add(typeFilter);
    }

    public void addAll(Collection<TypeFilter> typeFilterLst){
        filterList.addAll(typeFilterLst);
    }

    public Collection<TypeFilter> getFilterList() {
        return filterList;
    }

    public boolean match(Class clazz){
        try {
            for (TypeFilter filter : filterList) {
                if (filter.match(clazz)) {
                    return true;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
