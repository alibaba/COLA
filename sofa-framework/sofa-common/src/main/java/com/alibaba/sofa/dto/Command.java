package com.alibaba.sofa.dto;

/**
 * Command stands for a request from Client.
 * According CommandExecutor will help to handle the business logic. This is a classic Command Pattern
 *
 * @author fulan.zjf 2017年10月27日 下午12:28:24
 */
public abstract class Command extends DTO{

    private static final long serialVersionUID = 1L;

    /**
     * command的操作人
     */
	private String operater;

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}



}
