package com.alibaba.craftsman.interceptor;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.exception.Assert;
import com.alibaba.craftsman.context.UserContext;
import com.alibaba.craftsman.dto.CommonCommand;

@PreInterceptor
public class ContextInterceptor implements CommandInterceptorI{

    public final static String SYS_USER = "System";

    public void preIntercept(Command command) {
        UserContext content = new UserContext();
        content.setOperator(ContextInterceptor.SYS_USER);
    }

}
