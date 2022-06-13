package com.huawei.charging.domain.gateway;

import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.ChargeRecord;

import java.util.List;

/**
 * 跟账户系统交互的网关（Gateway）
 *
 * @version 1.0
 */
public interface AccountGateway {

    /**
     * 根据用户号码获取账户信息（含计费项余额等信息）
     *
     * @param phoneNo 电话号码
     * @return 账户信息
     */
    Account getAccount(long phoneNo);

    /**
     * 将扣费记录同步到账户中
     *
     * @param phoneNo 电话号码
     * @param records 扣费记录
     */
    void sync(long phoneNo, List<ChargeRecord> records);
}
