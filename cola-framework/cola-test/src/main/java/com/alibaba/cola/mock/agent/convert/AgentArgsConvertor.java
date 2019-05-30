package com.alibaba.cola.mock.agent.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;
import com.alibaba.fastjson.JSONException;

/**
 * @author shawnzhan.zxy
 * @date 2019/05/06
 */
public class AgentArgsConvertor {
    private static Pattern DEFAULT_CONSTRUCTOR_JSONEXECPTION = Pattern.compile("autoType is not support. (.*)");

    public static AgentArgs convert(String jsonException){
        //autoType is not support. com.alibaba.crm.auth.common.utils.BooleanDO
        Matcher matcher = DEFAULT_CONSTRUCTOR_JSONEXECPTION.matcher(jsonException);
        if(!matcher.find()){
            return null;
        }

        AgentArgs agentArgs = new AgentArgs();
        agentArgs.setTranslateType(TranslateType.DEFAULT_CONSTRUCTOR);
        agentArgs.setClassName(matcher.group(1));
        return agentArgs;
    }

    public static AgentArgs convertForOnlineRecord(String clazz, String methodName){
        AgentArgs args = new AgentArgs();
        args.setClassName(clazz);
        args.setMethodName(methodName);
        args.setTranslateType(TranslateType.ONLINE_RECORD);
        return args;
    }

    public static void main(String[] args) {
        AgentArgsConvertor.convert("autoType is not support. com.alibaba.crm.auth.common.utils.BooleanDO");

    }
}
