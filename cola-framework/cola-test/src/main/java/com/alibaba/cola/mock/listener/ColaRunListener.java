package com.alibaba.cola.mock.listener;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.ColaTestDescription;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/09
 */
public class ColaRunListener {

    public void testRunStarted(ColaTestDescription description){
        ColaMockito.g().getContext().setColaTestMeta(description);
    }
}
