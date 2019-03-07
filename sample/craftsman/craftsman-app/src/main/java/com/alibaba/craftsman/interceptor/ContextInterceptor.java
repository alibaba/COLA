package com.alibaba.craftsman.interceptor;

import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.context.Context;
import com.alibaba.cola.exception.Assert;
import com.alibaba.craftsman.context.LoginUser;
import com.alibaba.craftsman.dto.CommonCommand;

@PreInterceptor
public class ContextInterceptor implements CommandInterceptorI<CommonCommand>{

    public final static String SYS_USER = "System";

    @Override
    public void preIntercept(CommonCommand command) {
        Context<LoginUser> context = new Context();
        LoginUser content = new LoginUser();
        //Normally, this will go to Auth System to do Authorization and Authentication.
        if (command.isNeedsOperator()){
            Assert.notNull(command.getOperater());
            content.setLoginUserId(command.getOperater());
            content.setLoginUserName("HumanBeing-Lucy");
        }
        //System operation, No Human Operator needed
        else{
            content.setLoginUserId(ContextInterceptor.SYS_USER);
        }
        context.setContent(content);

        if(command.getContext() != null){
            context.setBizCode(command.getContext().getBizCode());
        }

        command.setContext(context);

    }

}
