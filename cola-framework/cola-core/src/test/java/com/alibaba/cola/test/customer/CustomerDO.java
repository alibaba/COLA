package com.alibaba.cola.test.customer;

import com.alibaba.cola.tunnel.DataObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CustomerDO
 *
 * @author Frank Zhang
 * @date 2018-01-08 1:45 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerDO extends DataObject {
    private String customerId;
    private String memberId;
    private String globalId;
    private String companyName;
    private String source;
    private String companyType;
}