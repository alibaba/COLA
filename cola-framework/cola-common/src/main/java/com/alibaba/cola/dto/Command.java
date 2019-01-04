package com.alibaba.cola.dto;

import com.alibaba.cola.context.Context;

/**
 * Command stands for a request from Client.
 * According CommandExecutor will help to handle the business logic. This is a classic Command Pattern
 *
 * @author fulan.zjf 2017年10月27日 下午12:28:24
 */
public abstract class Command extends DTO{

    private static final long serialVersionUID = 1L;

	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
