package com.alibaba.cola.context;

import java.util.Set;

/**
 * 能够自动context穿透的Runnable
 * 线程池中，加入SofaContextRunnable对象，在构造函数中，将context传递到子线程
 * 若不需考虑context穿透，可直接用普通Runnable
 *
 * 使用示例参见测试用例：TestSofaContext.testSofaContextRunnable
 * @author niexiaolong
 * @date 2018/6/26
 */
public abstract class ColaContextRunnable implements Runnable {

	private Set<ColaContextSupport> sofaContextSet;

	private ColaContextRunnable() {}

	public ColaContextRunnable(Set<ColaContextSupport> sofaContextSet) {
		this.sofaContextSet = sofaContextSet;
	}

	@Override
	public void run() {
		sofaContextSet.forEach(ColaContext::setContext);
		execute();
		ColaContext.clearAllContext();
	}

	public abstract void execute();
}
