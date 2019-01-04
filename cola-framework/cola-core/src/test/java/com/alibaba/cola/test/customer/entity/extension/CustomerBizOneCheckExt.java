package com.alibaba.cola.test.customer.entity.extension;

import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.ErrorCode;
import com.alibaba.cola.test.customer.entity.CustomerE;
import com.alibaba.cola.test.customer.entity.SourceType;
import com.alibaba.cola.test.customer.entity.extensionpoint.CustomerCheckExtPt;

/**
 * CustomerBizOneCheckExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:10 PM
 */
@Extension(bizCode = Constants.BIZ_ONE)
public class CustomerBizOneCheckExt implements CustomerCheckExtPt {

    @Override
    public void addCustomerCheck(CustomerE customerEntity) {
        if(SourceType.AD == customerEntity.getSourceType()){
            throw new BizException(ErrorCode.B_CUSTOMER_advNotAllowed, "Sorry, Customer from advertisement can not be added in this period");
        }
    }
}
