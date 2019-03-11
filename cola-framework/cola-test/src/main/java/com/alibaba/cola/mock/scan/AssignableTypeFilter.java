package com.alibaba.cola.mock.scan;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/24
 */
public class AssignableTypeFilter implements TypeFilter{
    private final String uid;
    private final Class<?> targetType;

    public AssignableTypeFilter(Class<?> targetType){
        this.uid = targetType.getName();
        this.targetType = targetType;
    }

    @Override
    public boolean match(Class clzz) {
        return this.targetType.isAssignableFrom(clzz);
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        TypeFilter compareObj = (TypeFilter)o;
        if(getUid().equals(compareObj.getUid())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return getUid().hashCode();
    }
}
