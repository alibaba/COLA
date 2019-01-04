package com.alibaba.cola.test.customer.entity.extension;

import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.CustomerType;
import com.alibaba.cola.test.customer.ErrorCode;
import com.alibaba.cola.test.customer.entity.CustomerE;
import com.alibaba.cola.test.customer.entity.extensionpoint.CustomerCheckExtPt;

/**
 * CustomerBizTwoCheckExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:10 PM
 */
@Extension(bizCode = Constants.BIZ_TWO)
public class CustomerBizTwoCheckExt implements CustomerCheckExtPt {

    @Override
    public void addCustomerCheck(CustomerE customerEntity) {
        //Any Customer can be added
        if(CustomerType.VIP == customerEntity.getCustomerType()){
            throw new BizException(ErrorCode.B_CUSTOMER_vipNeedApproval, "Sorry, you don't have privilege to add VIP customer");
        }
    }
}
