package com.alibaba.cola.mock.agent;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.utils.CommonUtils;
import com.alibaba.fastjson.JSON;

import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @since 2018/11/12
 */
public class ColaTestAttach {
    private static String agentJarPath;
    private static VirtualMachine virtualMachine;
    public static ClassLoader curClassLoader;
    public static VirtualMachine attach(AgentArgs config)
        throws Exception {
        curClassLoader = Thread.currentThread().getContextClassLoader();
        VirtualMachine virtualMachine = VirtualMachine.attach(getPid2());
        virtualMachine.loadAgent(getAgentJar(), JSON.toJSONString(config));
        ColaTestAttach.virtualMachine = virtualMachine;
        return virtualMachine;
    }

    public static void detach(){
        if(ColaTestAttach.virtualMachine != null){
            try {
                ColaTestAttach.virtualMachine.detach();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void detach(VirtualMachine machine){
        if(machine == null){
            return;
        }
        try {
            machine.detach();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPid2(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        // get pid
        String pid = name.split("@")[0];
        return pid;
    }

    private static String getAgentJar() throws ClassNotFoundException {
        if(StringUtils.isNotBlank(agentJarPath)){
            return agentJarPath;
        }
        String file = Class.forName("com.alibaba.cola.mock.agent.MainAgent").getResource("").getFile();
        String jarPath = file.split("!")[0];
        if(CommonUtils.isWindows()) {
            jarPath = jarPath.replace("file:/", "");
        }else{
            jarPath = jarPath.replace("file:", "");
        }
        agentJarPath = jarPath;
        return jarPath;
    }
}
