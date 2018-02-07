package com.alibaba.sofa.context;

import com.alibaba.sofa.exception.Preconditions;

/**
 * Pvg Context
 * <p/>
 * Created by Danny.Lee on 2017/11/9.
 */
public class PvgContext {

    private static ThreadLocal<Pvg> pvgContext = new ThreadLocal<>();

    private static class Pvg {
        private String loginId;
        private String userId;
        private String roleName;
        private String orgId;
        private String corpId;

        public Pvg(String loginId, String roleName, String orgId, String corpId) {
            this.loginId = loginId;
            this.roleName = roleName;
            this.orgId = orgId;
            this.corpId = corpId;
        }
    }

    public static void set(String loginId, String roleName, String orgId, String corpId) {
        Pvg pvg = new Pvg(loginId, roleName, orgId, corpId);
        pvgContext.set(pvg);
    }

    public static void remove() {
        pvgContext.remove();
    }

    public static String getLoginId() {
        Preconditions.checkArgument(null != pvgContext.get() && null != pvgContext.get().loginId, "No loginId in Context");
        return pvgContext.get().loginId;
    }

    public static String getRoleName() {
        Preconditions.checkArgument(null != pvgContext.get() && null != pvgContext.get().roleName, "No roleName in Context");
        return pvgContext.get().roleName;
    }

    public static String getOrgId() {
        Preconditions.checkArgument(null != pvgContext.get() && null != pvgContext.get().orgId, "No orgId in Context");
        return pvgContext.get().orgId;
    }

    public static String getCorpId() {
        Preconditions.checkArgument(null != pvgContext.get() && null != pvgContext.get().corpId, "No corpId in Context");
        return pvgContext.get().corpId;
    }

    public static String getUserId() {
    	Preconditions.checkArgument(null != pvgContext.get() && null != pvgContext.get().userId, "No userId in Context");
    	return pvgContext.get().userId;
    }

}
