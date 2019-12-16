package com.alibaba.cola.boot;

import com.alibaba.cola.exception.framework.ColaException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author shawnzhan.zxy
 * @date 2018/02/13
 */
public abstract class AbstractRegister implements RegisterI, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    protected<T> T getBean(Class targetClz){
        T beanInstance = null;
        //优先按type查
        try {
            beanInstance = (T) applicationContext.getBean(targetClz);
        }catch (Exception e){
        }
        //按name查
        if(beanInstance == null){
            String simpleName = targetClz.getSimpleName();
            //首字母小写
            simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            beanInstance = (T) applicationContext.getBean(simpleName);
        }
        if(beanInstance == null){
            new ColaException("Component " + targetClz + " can not be found in Spring Container");
        }
        return beanInstance;
    }

    /**
     * 根据Order注解排序
     * @param interceptorIList
     */
    protected <T> void order(List<T> interceptorIList){
        if(interceptorIList == null || interceptorIList.size() <= 1){
            return;
        }
        T newInterceptor = interceptorIList.get(interceptorIList.size() - 1);
        Order order = newInterceptor.getClass().getDeclaredAnnotation(Order.class);
        if(order == null){
            return;
        }
        int index = interceptorIList.size() - 1;
        for(int i = interceptorIList.size() - 2; i >= 0; i--){
            int itemOrderInt = Ordered.LOWEST_PRECEDENCE;
            Order itemOrder = interceptorIList.get(i).getClass().getDeclaredAnnotation(Order.class);
            if(itemOrder != null){
                itemOrderInt = itemOrder.value();
            }
            if(itemOrderInt > order.value()){
                interceptorIList.set(index, interceptorIList.get(i));
                index = i;
            }else {
                break;
            }
        }
        if(index < interceptorIList.size() - 1){
            interceptorIList.set(index, newInterceptor);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
