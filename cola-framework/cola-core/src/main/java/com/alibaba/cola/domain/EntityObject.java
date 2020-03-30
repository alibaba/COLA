package com.alibaba.cola.domain;


import com.alibaba.cola.extension.BizScenario;
import lombok.Data;
/**
 * Entity Object
 *
 * This is the parent object of all domain objects
 * @author fulan.zjf 2017年10月27日 上午10:16:10
 */

@Data
public abstract class EntityObject {
	private BizScenario bizScenario;
}
