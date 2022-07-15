package com.alibaba.cola.statemachine.spring.config;


import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.alibaba.cola.statemachine.impl.StateMachineException;
import com.alibaba.cola.statemachine.spring.annotation.StateMachine;
import com.alibaba.cola.statemachine.spring.annotation.StateMachines;
import com.alibaba.cola.statemachine.spring.support.StateMachineSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alibaba.cola.statemachine.spring.support.StateMachineSupport.stateMachineBuilderMap;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;


@Slf4j
public class StateMachineServiceBean implements InitializingBean {

    @Setter
    @Getter
    private List<Class<?>> classList;

    @Setter
    @Getter
    private ApplicationContext applicationContext;


    /**
     * start to register stateMachine we needed;
     *
     * @param targetClass
     */
    private void registerSingleStateMachine(Class<?> targetClass) {

        // get bean
        Object bean = applicationContext.getBean(targetClass);
        // 获取实际的泛型
        Type[] actualTypeArguments = getActualArgumentType(targetClass);
        StateMachines annotation = findAnnotation(targetClass, StateMachines.class);
        StateMachine[] annotationsByType;
        boolean overrideFlag;
        if (Objects.nonNull(annotation)) {
            annotationsByType = annotation.value();
            overrideFlag = annotation.overrideDefaultCondition();
        } else {
            annotationsByType = targetClass.getAnnotationsByType(StateMachine.class);
            overrideFlag = false;
        }

        Arrays.stream(annotationsByType).forEach(entity -> {
            Enum from = extracted((Class) actualTypeArguments[0], entity.from());
            Enum to = extracted((Class) actualTypeArguments[0], entity.to());
            Enum on = extracted((Class) actualTypeArguments[1], entity.on());
            Class contextClass = ((Class) actualTypeArguments[2]);

            // 判断插件的Condition
            Condition c = getCondition(bean, overrideFlag, Condition.class.isAssignableFrom(targetClass), from, to, contextClass);
            // 判断插件的Action
            Action<?, ?, ?> a = getAction(bean, contextClass);

            stateMachineBuilderMap.compute(entity.machineId(), (k, v) -> {
                if (v == null) {
                    StateMachineBuilder<Object, Object, Object> builder = StateMachineBuilderFactory.create();
                    builder.externalTransition().from(from).to(to).on(on).when(c).perform(a);
                    return builder;
                } else {
                    v.externalTransition().from(from).to(to).on(on).when(c).perform(a);
                    return v;
                }
            });
        });


    }

    private Action getAction(Object bean, Class clazz) {

        Action action;

        Map<String, AbstractAfterActionAdapter> beanAdapter = applicationContext.getBeansOfType(AbstractAfterActionAdapter.class);
        List<AbstractAfterActionAdapter> abstractAfterActionAdapters =
                beanAdapter.values().stream().filter(ba ->
                                ba != null && ba.getTargetName().isAssignableFrom(clazz))
                        .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(abstractAfterActionAdapters)) {
            action = (from, to, on, m) -> {
                ((Action) bean).execute(from, to, on, m);
                abstractAfterActionAdapters.forEach(actionAdapter -> {
                    if (actionAdapter.ifUsedInCurrentAction(from, to, on, m)) {
                        actionAdapter.execute(from, to, on, m);
                    }
                });
            };
        } else {
            action = (Action) bean;
        }
        return action;
    }

    /**
     * 用公共condition 代替 每个状态自己实现一遍
     *
     * @param bean
     * @param overrideFlag
     * @param conditionFlag
     * @param from
     * @param to
     * @param clazz
     * @return
     */
    private Condition getCondition(Object bean, boolean overrideFlag, boolean conditionFlag, Enum from, Enum to, Class clazz) {

        Condition c;
        Map<String, AbstractConditionAdapter> beanAdapter = applicationContext.getBeansOfType(AbstractConditionAdapter.class);
        List<AbstractConditionAdapter> abstractConditionAdapters = beanAdapter.values().stream()
                .filter(abstractConditionAdapter ->
                        abstractConditionAdapter != null && abstractConditionAdapter.getTargetName().isAssignableFrom(clazz))
                .collect(Collectors.toList());
        // 包含了接口
        if (conditionFlag) {
            // 如果有覆盖,默认走bean的覆盖
            if (overrideFlag) {
                c = (Condition) bean;
            } else {
                // 没有覆盖 就一起判断
                if (!CollectionUtils.isEmpty(abstractConditionAdapters)) {
                    c = m -> ((Condition) bean).isSatisfied(m) && abstractConditionAdapters.stream().allMatch(e -> e.commonCheck(from, to, m));
                } else {
                    c = (Condition) bean;
                }
            }
        } else {
            //  不包含接口
            if (!CollectionUtils.isEmpty(abstractConditionAdapters)) {
                c = m -> abstractConditionAdapters.stream().allMatch(e -> e.commonCheck(from, to, m));
            } else {
                c = m -> true;
            }
        }
        return c;
    }

    private Enum extracted(Class target, String name) {
        try {
            Enum[] enumConstants = (Enum[]) target.getEnumConstants();
            Optional<Enum> any = Arrays.stream(enumConstants).filter(enums -> enums.name().equals(name)).findAny();
            return any.orElseThrow(() -> new StateMachineException("enum value setting error"));
        } catch (StateMachineException e) {
            log.error("get enum value error,req={},name={}", target.getName(), name);
            throw new StateMachineException("enum value error,name =" + name);
        }
    }


    private Type[] getActualArgumentType(Class<?> targetClass) {
        Type[] genericInterfaces = targetClass.getGenericInterfaces();
        Type[] actualTypeArguments = null;
        for (Type genericInterface : genericInterfaces) {
            ParameterizedType pType = (ParameterizedType) genericInterface;
            Type rawType = pType.getRawType();
            Class typeClass = (Class) rawType;
            // 获取action接口下的参数化类型
            if (Action.class.isAssignableFrom(typeClass)) {
                actualTypeArguments = pType.getActualTypeArguments();
            }
        }
        if (actualTypeArguments == null || actualTypeArguments.length != 3) {
            log.error("Action接口设置异常,name={}", targetClass);
            throw new StateMachineException("Action接口设置异常");
        }
        return actualTypeArguments;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (applicationContext == null){
            log.error("初始化状态机注册bean 异常,application 为空");
            throw new StateMachineException("初始化状态机注册bean 异常");
        }
        if (CollectionUtils.isEmpty(classList)){
            log.error("初始化状态机注册bean 异常,classList 为空");
            throw new StateMachineException("初始化状态机注册bean 异常,classList 为空");
        }
        classList.forEach(this::registerSingleStateMachine);
        StateMachineSupport.startStateMachine();
        classList.clear();
    }
}
