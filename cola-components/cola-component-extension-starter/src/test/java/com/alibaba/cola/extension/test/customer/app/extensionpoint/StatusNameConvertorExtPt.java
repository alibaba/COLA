package com.alibaba.cola.extension.test.customer.app.extensionpoint;

import com.alibaba.cola.extension.ExtensionPointI;

/**
 * This extension point supports state transition operations of multiple manufacturers and different business lines
 *
 * @author wangguoqiang wrote on 2022/10/10 14:37
 * @version 1.0
 */
public interface StatusNameConvertorExtPt extends ExtensionPointI {
    String statusNameConvertor(Integer statusCode);
}
