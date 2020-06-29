package com.alibaba.cola.mock.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.alibaba.cola.mock.agent.ColaTestAttach;
import com.alibaba.cola.mock.agent.convert.AgentArgsConvertor;
import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;
import com.alibaba.cola.mock.utils.reflection.ParameterizedTypeImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/12
 */
public class JsonUtils {

    public static String checkAndToJson(Object data){
        String json = JSONArray.toJSONString(data, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat);
        checkJson(json);
        return json;
    }

    public static<T> T parseJson(String json){
        try {
            return parseJsonWithException(json, StringUtils.EMPTY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static<T> T parseJsonWithException(String json, String exception) throws Exception {
        T data;
        try{
            data = (T)JSON.parse(json);
        }catch (JSONException e){
            //if(!e.getMessage().equals(exception) && injectConstructor(e)) {
            //    data = (T)parseJsonWithException(json, e.getMessage());
            //}
            AgentArgs agentArgs = AgentArgsConvertor.convert(e.getMessage());
            if(agentArgs != null) {
                return parseJsonByGson(json, Class.forName(agentArgs.getClassName()));
            }else{
                throw new RuntimeException("无法反序列化,请确保序列化对象满足POJO,同时 存在无参构造!json=>" + json, e);
            }
        }
        return data;
    }

    private static void checkJson(String json){
        parseJson(json);
    }

    private static <T> T parseJsonByGson(String json, Class<?> clazz){
        Gson gson = new Gson();
        Type type = new ParameterizedTypeImpl(List.class, new Type[]{clazz}, List.class);
        T value = (T)gson.fromJson(json, type);
        return value;
    }

    private  static boolean injectConstructor(JSONException jsonE){
        AgentArgs agentArgs = AgentArgsConvertor.convert(jsonE.getMessage());
        if(agentArgs == null){
            return false;
        }
        try {
            ColaTestAttach.attach(agentArgs);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
