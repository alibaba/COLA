package com.alibaba.cola.test.context;

import com.alibaba.cola.context.ColaContextSupprot;

/**
 * 用户上下文 -- demo
 * 每个应用在自己的工程代码中定义自己需要的Context结构，只需要实现SofaContextSupport接口即可
 *
 * @author niexiaolong
 * @date 2018/6/25
 */
public class PvgContext implements ColaContextSupprot {

	private String crmUserId;
	private String roleCode;
	private String orgId;
	private String companyId;

	public PvgContext(String crmUserId, String roleCode, String orgId, String companyId) {
		this.crmUserId = crmUserId;
		this.roleCode = roleCode;
		this.orgId = orgId;
		this.companyId = companyId;
	}

	public String getCrmUserId() {
		return crmUserId;
	}

	public void setCrmUserId(String crmUserId) {
		this.crmUserId = crmUserId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "PvgContext{" +
				"crmUserId='" + crmUserId + '\'' +
				", roleCode='" + roleCode + '\'' +
				", orgId='" + orgId + '\'' +
				", companyId='" + companyId + '\'' +
				'}';
	}
}
