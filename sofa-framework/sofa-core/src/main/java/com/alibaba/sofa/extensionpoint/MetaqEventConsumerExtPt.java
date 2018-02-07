package com.alibaba.sofa.extensionpoint;

import com.alibaba.sofa.dto.Response;

/**
 * Metaq消息消费扩展点
 * @author xueliang.sxl
 *
 */
public interface MetaqEventConsumerExtPt {

	Response receive(String msg);
}
