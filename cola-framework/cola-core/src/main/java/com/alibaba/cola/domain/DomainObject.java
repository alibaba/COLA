package com.alibaba.cola.domain;

import com.alibaba.cola.event.EventBus;
import com.alibaba.cola.repository.RepositoryBus;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne
 * @date 2020/1/23
 * @description
 */
public abstract class DomainObject extends EntityObject {

  protected EventBus eventBus;

  protected RepositoryBus repositoryBus;

  public final void initEventBus(EventBus eventBus){
    this.eventBus = eventBus;
  }

  public final void initPresentationBus(RepositoryBus repositoryBus){
    this.repositoryBus = repositoryBus;
  }

  public void execute(){}

  public final  <T extends DomainObject> T createDomain(Class<T> clazz, Object ... initargs) {
    List<Class<?>> list =null;
    if(initargs!=null){
      list = new ArrayList<>();
      for (Object obj:initargs){
        list.add(obj.getClass());
      }
    }
    Class<?>[] parameterTypes = list!=null?list.toArray(new Class[]{}):null;
    try {
      DomainObject domain = clazz.getDeclaredConstructor(parameterTypes).newInstance(initargs);
      domain.initEventBus(eventBus);
      domain.initPresentationBus(repositoryBus);
      return (T)domain;
    } catch (InstantiationException | IllegalAccessException |
            InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

}
