package com.alibaba.cola.domain;


import com.alibaba.cola.extension.BizScenario;
/**
 * Entity Object
 *
 * This is the parent object of all domain objects
 * @author fulan.zjf 2017年10月27日 上午10:16:10
 */

public abstract class EntityObject {
    public BizScenario getBizScenario() {
        return bizScenario;
    }

    public void setBizScenario(BizScenario bizScenario) {
        this.bizScenario = bizScenario;
    }

    private BizScenario bizScenario;

}
