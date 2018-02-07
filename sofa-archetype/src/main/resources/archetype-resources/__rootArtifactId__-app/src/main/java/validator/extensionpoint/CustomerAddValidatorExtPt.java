#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shdemo not disclose such Confidential Information and shdemo
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package ${package}.validator.extensionpoint;

import com.alibaba.sofa.extension.ExtensionPointI;
import com.alibaba.sofa.validator.ValidatorI;

/**
 * CustomerAdd Validation Point
 * @author fulan.zjf 2017-11-05
 */
public interface CustomerAddValidatorExtPt extends ValidatorI, ExtensionPointI{

}
