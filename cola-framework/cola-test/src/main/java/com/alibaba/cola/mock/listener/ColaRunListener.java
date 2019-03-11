package com.alibaba.cola.mock.listener;

import com.alibaba.cola.mock.ColaMockito;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/09
 */
public class ColaRunListener {

    public void testRunStarted(Object testInstance){
        ColaMockito.g().getContext().setTestInstance(testInstance);
    }
}
