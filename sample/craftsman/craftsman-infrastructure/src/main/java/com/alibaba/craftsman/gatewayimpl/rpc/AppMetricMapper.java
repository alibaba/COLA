package com.alibaba.craftsman.gatewayimpl.rpc;

import com.alibaba.craftsman.gatewayimpl.rpc.dataobject.AppMetricDO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppMetricMapper {

    /**
     *  Dummy RPC Call
     */
    public List<AppMetricDO> listByUserId(String userId){
        List<AppMetricDO> appMetricDOList = new ArrayList<>();
        AppMetricDO appMetricDO1 = new AppMetricDO();
        appMetricDO1.setAppName("app1");
        appMetricDO1.setCyclomaticComplexityCount(200);
        appMetricDO1.setDuplicatedMethodCount(80);
        appMetricDO1.setLongMethodCount(70);
        appMetricDO1.setBlockedCodeConductCount(20);
        appMetricDOList.add(appMetricDO1);

        AppMetricDO appMetricDO2 = new AppMetricDO();
        appMetricDO2.setAppName("app2");
        appMetricDO2.setCyclomaticComplexityCount(20);
        appMetricDO2.setDuplicatedMethodCount(30);
        appMetricDO2.setLongMethodCount(7);
        appMetricDO2.setBlockedCodeConductCount(5);
        appMetricDOList.add(appMetricDO2);
        return appMetricDOList;
    }
}
