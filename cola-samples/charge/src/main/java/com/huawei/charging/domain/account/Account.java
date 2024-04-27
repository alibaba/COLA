package com.huawei.charging.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huawei.charging.domain.BizException;
import com.huawei.charging.domain.DomainFactory;
import com.huawei.charging.domain.Entity;
import com.huawei.charging.domain.charge.*;
import com.huawei.charging.domain.charge.chargeplan.BasicChargePlan;
import com.huawei.charging.domain.charge.chargeplan.ChargePlan;
import com.huawei.charging.domain.charge.chargerule.ChargeRuleFactory;
import com.huawei.charging.domain.charge.chargerule.CompositeChargeRule;
import com.huawei.charging.domain.gateway.AccountGateway;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Slf4j
public class Account {
    /**
     * 用户号码
     */
    private long phoneNo;

    /**
     * 账户余额
     */
    private Money remaining;

    /**
     * 账户所拥有的套餐
     */
    @JsonIgnore
    private List<ChargePlan> chargePlanList = new ArrayList<>();;

    @Resource
    private AccountGateway accountGateway;

    private String name;

    public Account(){

    }

    public Account(long phoneNo, Money amount, List<ChargePlan> chargePlanList){
        this.phoneNo = phoneNo;
        this.remaining = amount;
        this.chargePlanList = chargePlanList;
    }

    public static Account valueOf(long phoneNo, Money amount) {
        Account account = DomainFactory.get(Account.class);
        account.setPhoneNo(phoneNo);
        account.setRemaining(amount);
        account.chargePlanList.add(new BasicChargePlan());
        return account;
    }

    /**
     * 检查账户余额是否足够
     */
    public void checkRemaining() {
        if (remaining.isLessThan(Money.of(0))) {
            throw BizException.of(this.phoneNo + " has insufficient amount");
        }
    }

    public List<ChargeRecord> charge(ChargeContext ctx) {
        CompositeChargeRule compositeChargeRule = ChargeRuleFactory.get(chargePlanList);
        List<ChargeRecord> chargeRecords = compositeChargeRule.doCharge(ctx);
        log.debug("Charges: "+ chargeRecords);

        //跟新账户系统
        accountGateway.sync(phoneNo, chargeRecords);
        return chargeRecords;
    }

    @Override
    public String toString() {
        return "Account{" +
                "phoneNo=" + phoneNo +
                ", remaining=" + remaining +
                ", chargePlanList=" + chargePlanList +
                ", name=" + name +
                '}';
    }
}
