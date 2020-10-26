package com.alibaba.craftsman.gatewayimpl.rpc;

import com.alibaba.craftsman.gatewayimpl.rpc.dataobject.BugMetricDO;
import org.springframework.stereotype.Component;

/**
 * 模拟一个RPC的Tunnel，也是日常业务中非常常见的场景。
 *
 * 假设Bug数和代码checkin数再另外一个系统中，没有存在本地，需要通过RPC调用才能获取。
 *
 */
@Component
public class BugMetricMapper {

    /**
     *  Dummy RPC Call
     */
    public BugMetricDO getByUserId(String userId){
        BugMetricDO bugMetricDO = new BugMetricDO();
        bugMetricDO.setBugCount(3);
        bugMetricDO.setCheckInCodeCount(1500);
        return bugMetricDO;
    }
}
