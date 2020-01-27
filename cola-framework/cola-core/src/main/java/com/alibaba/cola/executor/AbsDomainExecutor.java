package com.alibaba.cola.executor;

import com.alibaba.cola.domain.DomainFactoryHelper;
import com.alibaba.cola.domain.DomainObject;
import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lorne
 * @date 2020/1/26
 * @description
 */
public abstract class AbsDomainExecutor<R extends Response, C extends Executor> implements ExecutorI<R,C> {

    @Autowired
    protected DomainFactoryHelper domainFactoryHelper;

    public <T extends DomainObject>T createDomain(Class<T> clazz, Object ... initargs){
        return (T)domainFactoryHelper.create(clazz,initargs);
    }


    public <T extends DomainObject>T createDomainAndExecute(Class<T> clazz, Object ... initargs){
        DomainObject domain = createDomain(clazz,initargs);
        domain.execute();
        return (T)domain;
    }

}
