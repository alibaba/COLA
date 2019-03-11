package com.alibaba.cola.mock.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class CompareUtils {


    public static String compareFields(Object target, Object origin, String[] ignoreArr) {

        if(target == null && origin == null){
            return null;
        }
        if(target == null && origin != null ||
        origin == null && target != null){
            return "target is:" + JSON.toJSONString(target) + " .but origin is:" + JSON.toJSONString(origin);
        }

        try{
            List<String> ignoreList = null;
            if(ignoreArr != null && ignoreArr.length > 0){
                // array转化为list
                ignoreList = Arrays.asList(ignoreArr);
            }

            if (target.getClass() == origin.getClass()) {// 只有两个对象都是同一类型的才有可比性

                boolean isTheSame = true;
                if(isBaseType(target)){
                    isTheSame = compareIfBaseField(target, origin);
                    if(!isTheSame){
                        return "compareFields failed, when expect is :" + origin.toString()+ " , and real value is :" + target.toString();
                    }
                }else{
                    Class clazz = target.getClass();
                    //获取object的属性描述
                    PropertyDescriptor[] pds=Introspector.getBeanInfo(clazz,Object.class).getPropertyDescriptors();
                    for(PropertyDescriptor pd:pds){//这里就是所有的属性了
                        String name = pd.getName();//属性名
                        if(ignoreList!= null && ignoreList.contains(name)){
                            continue;
                        }
                        Object o1 = null;
                        Object o2 = null;
                        Method readMethod=pd.getReadMethod();//get方法
                        if(readMethod == null){
                            Field f = clazz.getDeclaredField(name);
                            f.setAccessible(true);
                            o1 = f.get(target);
                            o2 = f.get(origin);
                        }else {
                            //在obj1上调用get方法等同于获得obj1的属性值
                            o1 = readMethod.invoke(target);
                            //在obj2上调用get方法等同于获得obj2的属性值
                            o2 = readMethod.invoke(origin);
                        }

                        if(o1 == null && o2 == null){
                            continue;
                        }
                        if(o1 == null && o2 != null){
                            return name + " compareFields failed, when expect is not null , and real value is null";
                        }
                        if(o1 != null && o2 == null){
                            return name + " compareFields failed, when expect is  null , and real value is not null";
                        }

                        /**
                         * 需要考虑数组，链表等数据结构的对比，看看是否能够复用json解析的功能
                         */
                        /*String subCompareResult =  compareFields(o1, o2, ignoreArr);

                        if(!StringUtils.isEmpty(subCompareResult)){
                            return "compare sub complex Fields failed, subFiled is: " + name + " .inner compare result is:" + subCompareResult;
                        }*/
                        String json1 = JSON.toJSONString(o1);
                        String json2 = JSON.toJSONString(o2);
                        isTheSame = json1.equals(json2);
                        if(!isTheSame){//比较这两个值是否相等,不等就可以放入map了
                            return name + " compareFields failed, when expect is :" + json2+ " , and real value is :" + json1;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return null;
    }



    public static boolean compareIfBaseField(Object target, Object origin) {
        Class className = target.getClass();
        if (className.equals(Integer.class)){
            return Integer.compare((Integer) target, (Integer) origin) == 0;

        }else if(className.equals(java.lang.Byte.class)){
            return Byte.compare((Byte) target, (Byte) origin) == 0;

        }else if(className.equals(Long.class)){
            return Long.compare((Long) target, (Long) origin) == 0;

        }else if(className.equals(java.lang.Double.class)){
            return Double.compare((Double) target, (Double) origin) == 0;

        }else if(className.equals(java.lang.Float.class) ){
            return Float.compare((Float) target, (Float) origin) == 0;


        }else if(className.equals(Character.class)){

            return Character.compare((Character) target, (Character) origin) == 0;
        }else if(className.equals(Short.class)){
            return Short.compare((Short) target, (Short) origin) == 0;

        }else if(className.equals(java.lang.Boolean.class)){

            return Boolean.compare((Boolean)target,(Boolean) origin) == 0;
        }else if(className.equals(java.lang.String.class)){
            return ((String)target).equals((String) origin);
        }else if(className.equals(com.alibaba.fastjson.JSONArray.class)){

            /**
             * 先判断数组大小，然后判断数据的json值
             */
            if(((JSONArray)target).size() != ((JSONArray)origin).size()){
                return false;
            }else{
                String targetStr = JSON.toJSONString(target);
                String originStr = JSON.toJSONString(origin);
                return targetStr.equals(originStr);
            }
        }


        return false;
    }


    public static boolean isBaseType(Object object) {
        Class className = object.getClass();
        if (className.equals(java.lang.Integer.class) ||
            className.equals(java.lang.Byte.class) ||
            className.equals(java.lang.Long.class) ||
            className.equals(java.lang.Double.class) ||
            className.equals(java.lang.Float.class) ||
            className.equals(java.lang.Character.class) ||
            className.equals(java.lang.Short.class) ||
            className.equals(java.lang.Boolean.class) ||
        className.equals(java.lang.String.class) ||
        className.equals(com.alibaba.fastjson.JSONArray.class)) {
            return true;
        }
        return false;
    }

      /*private static final ObjectMapper mapper=new ObjectMapper();

             *//**
             * Compare two object and return modified fields
             * @param source source object
             * @param target target object
             * @return the modified fields and value after modify
             *//*
    public static Map<String,Object> getModifyContent(Object source, Object target) {
        Map<String,Object> modifies=new HashMap<>();
         *//*
          process null problem, if all null means equal
          if only source is null means all modified
          if only target is null means nothing changed
         *//*
        if(null == source || null == target) {
            if(null==source&&null==target) {
                return modifies;
            }
            else if(null == target) {
                return modifies;
            }
            else {
                return mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
            }
        }
        // source and target must be same class type
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            throw new ClassCastException("source and target are not same class type");
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
        sourceMap.forEach((k,v)->{
            Object targetValue=targetMap.get(k);
            if (!Objects.equals(v,targetValue)){modifies.put(k,targetValue);}
        });
        return modifies;
    }

    *//**
             * Compare two object and return modified fields which contain in comparedProperties
             * @param source ource object
             * @param target target object
             * @param comparedProperties the fields need to be compare
             * @return the modified fields and value after modify
             *//*
    public static Map<String,Object> getModifyContent(Object source, Object target,Map<String,String> comparedProperties) {
        Map<String,Object> modifies=new HashMap<>();
        if(null == source || null == target) {
            if(null==source&&null==target) return modifies;
            else if(null == target) return modifies;
            else {return mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});}
        }
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            throw new ClassCastException("source and target are not same class type");
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
        sourceMap.forEach((k,v)->{
            if(comparedProperties!=null&&!comparedProperties.containsKey(k)){
                return;
            }
            Object targetValue=targetMap.get(k);
            if (!Objects.equals(v,targetValue)){modifies.put(k,targetValue);}
        });
        return modifies;
    }

    *//**
             * Compare two object and return if equal
             * @param source source object
             * @param target target object
             * @return true-equal
             *//*
    public static boolean isEuqal(Object source, Object target) {
        if(null == source || null == target) {
            return false;
        }
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            return false;
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
        return Objects.equals(sourceMap,targetMap);
    }

    *//**
             * Compare two object and return if equal
             * @param source source object
             * @param target target object
             * @param comparedProperties only compare fields in this map
             * @return  rue-equal
             *//*
    public static boolean isEuqal(Object source, Object target,Map<String,String> comparedProperties) {
        if(null == source || null == target) {
            return null == source && null == target;
        }
        if(!Objects.equals(source.getClass().getName(), target.getClass().getName())){
            return false;
        }
        Map<String, Object> sourceMap= mapper.convertValue(source, new TypeReference<Map<String,Object>>(){});
        Map<String, Object> targetMap = mapper.convertValue(target, new TypeReference<Map<String,Object>>(){});
        for(String k:sourceMap.keySet()){
            if(comparedProperties!=null&&!comparedProperties.containsKey(k)){
                continue;
            }
            Object v=sourceMap.get(k);
            Object targetValue=targetMap.get(k);
            if(!Objects.equals(v,targetValue)){return false;}
        }
        return true;
    }*/


}