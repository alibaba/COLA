package com.alibaba.cola.mock.agent;

import com.sun.tools.attach.VirtualMachine;
/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class ColaTestAttach {

    public void attach(String config) throws Exception {
        VirtualMachine virtualMachine = null;
        try {
            virtualMachine = VirtualMachine.attach("1111");
            virtualMachine.loadAgent("", config);
        } finally {
            if (null != virtualMachine) {
                virtualMachine.detach();
            }
        }
    }
}
