package com.alibaba.cola.extension;


import org.apache.commons.lang3.StringUtils;

/**
 * BizScenario（业务场景）= bizId + useCase + scenario, which can uniquely identify a user scenario.
 *
 * @author Frank Zhang
 * @date 2019-08-20 12:07
 */
public class BizScenario {
    public final static String DEFAULT_BIZ_ID = "defaultBizId";
    public final static String DEFAULT_USE_CASE = "";
    public final static String DEFAULT_SCENARIO = "";
    private final static String DOT_SEPARATOR = ".";

    /**
     * bizId is used to identify a business, such as "tmall", it's nullable if there is only one biz
     */
    private String bizId = DEFAULT_BIZ_ID;

    /**
     * useCase is used to identify a use case, such as "placeOrder", can not be null
     */
    private String useCase = DEFAULT_USE_CASE;

    /**
     * scenario is used to identify a use case, such as "88vip","normal", can not be null
     */
    private String scenario = DEFAULT_SCENARIO;

    /**
     * For above case, the BizScenario will be "tmall.placeOrder.88vip",
     * with this code, we can provide extension processing other than "tmall.placeOrder.normal" scenario.
     *
     * @return
     */
    public String getUniqueIdentity(){
        String uniqueIdentity = bizId;
        if (StringUtils.isNotBlank(useCase)) {
            uniqueIdentity = uniqueIdentity + DOT_SEPARATOR + useCase;
        }
        if (StringUtils.isNotBlank(scenario)) {
            uniqueIdentity = uniqueIdentity + DOT_SEPARATOR + scenario;
        }
        return uniqueIdentity;
    }

    public static BizScenario valueOf(String bizId, String useCase, String scenario){
        BizScenario bizScenario = new BizScenario();
        bizScenario.bizId = bizId;
        bizScenario.useCase = useCase;
        bizScenario.scenario = scenario;
        return bizScenario;
    }

    public static BizScenario valueOf(String bizId){
        return BizScenario.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    public static BizScenario valueOf(String useCase, String scenario){
        return BizScenario.valueOf(DEFAULT_BIZ_ID, useCase, scenario);
    }

    public static BizScenario newDefault(){
        return BizScenario.valueOf(DEFAULT_BIZ_ID, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }
}
