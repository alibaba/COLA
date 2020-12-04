package com.alibaba.cola.domain;

/**
 * DomainFactory
 *
 * @author Frank Zhang
 * @date 2019-01-03 2:41 PM
 */
public class DomainFactory {

    public static <T> T create(Class<T> entityClz){
        return ApplicationContextHelper.getBean(entityClz);
    }

    /**
     * 为聚合根创建简单的对象。此方法会自动为聚合内声明的关联，组合关系的对象进行实例化。<br/>
     * 该方法调用默认构造器创建对象，如默认构造器没有为成员进行实例化。会自动new新的对象，以保证在使用聚合对象的时候，不会出现聚合内对象的空指针异常<br/>
     * 支持的类型有:<br/>
     * 使用@Entity注解的实体类<br/>
     * 使用@ValueObject注解的值对象，暂时未支持<br/>
     * 命名规范符合COLA规范的Entity，ValueObject分别以大写E和V结尾的对象
     * 集合List<T> 会默认new ArrayList<T>()<br/>
     * 集合Set<T> 会默认new HashSet<T>()<br/>
     * @param entityClz 聚合根的对象类型
     * @param <T> 类型
     * @return 聚合根对象
     */
    public static <T> T aggregateCreate(Class<T> entityClz){
        return ApplicationContextHelper.getAggregateBean(entityClz);
    }

}
