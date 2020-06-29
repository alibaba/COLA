package com.alibaba.cola.mock.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @since 2018/11/12
 */
public class MainAgent {
    private static Map<String, Boolean> lock = new ConcurrentHashMap<>();
    public static void agentmain(String agentArgs, Instrumentation inst)
        throws ClassNotFoundException, UnmodifiableClassException,
        InterruptedException {
        final ColaTransformer transformer = new ColaTransformer(convert(agentArgs));
        if(!isLock(transformer)){
            return;
        }
        try {
            inst.addTransformer(transformer, true);
            Set<Class<?>> oriClassSet = searchClass(inst, transformer.getArgs().getClassName(), 1000);
            final Class<?>[] classArray = new Class<?>[oriClassSet.size()];
            System.arraycopy(oriClassSet.toArray(), 0, classArray, 0, oriClassSet.size());
            inst.retransformClasses(classArray);
        }finally {
            inst.removeTransformer(transformer);
        }
        System.out.println("agentmain DONE");
    }

    private static AgentArgs convert(String agentArgs){
        JSONObject jsonObject = JSON.parseObject(agentArgs);
        AgentArgs model = new AgentArgs();
        model.setClassName(jsonObject.getString("className"));
        model.setMethodName(jsonObject.getString("methodName"));
        model.setTranslateType(TranslateType.valueOf(jsonObject.getString("translateType")));
        return model;
    }

    public static Set<Class<?>> searchClass(Instrumentation inst, String searchName, int limit) {
        if (StringUtils.isBlank(searchName)) {
            return Collections.emptySet();
        }
        final Set<Class<?>> matches = new HashSet<>();
        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            if (searchName.equals(clazz.getName())) {
                matches.add(clazz);
            }
            if (matches.size() >= limit) {
                break;
            }
        }
        return matches;
    }

    private static boolean isLock(ColaTransformer transformer){
        if(null != lock.putIfAbsent(transformer.getArgs().getKey(), true)){
            return false;
        }
        return true;
    }
}
