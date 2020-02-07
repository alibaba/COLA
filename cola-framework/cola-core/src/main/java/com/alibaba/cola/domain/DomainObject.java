package com.alibaba.cola.domain;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.event.EventBus;
import com.alibaba.cola.repository.RepositoryBus;

/**
 * @author lorne
 * @date 2020/1/23
 * @description
 */
public abstract class DomainObject extends EntityObject {

  protected static EventBus eventBus;

  protected static RepositoryBus repositoryBus;

  static {
    eventBus = ApplicationContextHelper.getBean(EventBus.class);
    repositoryBus = ApplicationContextHelper.getBean(RepositoryBus.class);
  }

  public void execute(){}

}
