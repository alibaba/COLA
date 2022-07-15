package com.alibaba.cola.statemachine.spring.model;

import com.alibaba.cola.statemachine.spring.constant.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DemoSpringModel {

    private StatusEnum sourceEnum;
    private StatusEnum targetEnum;

    public DemoSpringModel(StatusEnum sourceEnum,StatusEnum targetEnum) {
        this.sourceEnum = sourceEnum;
        this.targetEnum = targetEnum;
    }
}
