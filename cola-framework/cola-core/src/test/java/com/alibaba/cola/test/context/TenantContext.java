package com.alibaba.cola.test.context;

import com.alibaba.cola.context.ColaContextSupport;

/**
 * 租户上下文 -- demo
 * 每个应用在自己的工程代码中定义自己需要的Context结构，只需要实现SofaContextSupport接口即可
 *
 * @author niexiaolong
 * @date 2018/6/25
 */
public class TenantContext implements ColaContextSupport {

	private String tenantId;
	private String bizCode;

	public TenantContext(String tenantId, String bizCode) {
		this.tenantId = tenantId;
		this.bizCode = bizCode;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	@Override
	public String toString() {
		return "TenantContext{" +
				"tenantId='" + tenantId + '\'' +
				", bizScenarioUniqueIdentity='" + bizCode + '\'' +
				'}';
	}
}
