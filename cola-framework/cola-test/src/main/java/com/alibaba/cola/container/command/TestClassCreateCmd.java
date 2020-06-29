package com.alibaba.cola.container.command;

import java.util.Arrays;

import com.alibaba.cola.mock.ColaTestRecordController;
import com.alibaba.cola.mock.autotest.ColaTestGenerator;

import org.apache.commons.lang3.StringUtils;

/**
 * 格式=》new com.alibaba.crm.sales.domain.customer.entity.CustomerE#addContact 1 2 3
 * @author shawnzhan.zxy
 * @since 2019/03/19
 */
public class TestClassCreateCmd extends AbstractCommand {
    private static final String INPUT_PARAMS = "inputParams";
    private String method;

    public TestClassCreateCmd(String cmdRaw) {
        super(cmdRaw);

        cmdRaw = cmdRaw.replaceAll(" +", StringUtils.SPACE);
        String[] cmdParams = cmdRaw.split(StringUtils.SPACE);
        if(cmdParams.length < 2){
            throw new RuntimeException("Your input is not a valid");
        }
        this.method = cmdParams[1];
        this.putParam(INPUT_PARAMS, Arrays.copyOfRange(cmdParams, 2, cmdParams.length));
    }

    @Override
    protected void action() {
        ColaTestGenerator generator = new ColaTestGenerator(this.getMethod(), ColaTestRecordController.getTemplateSuperClassName());
        generator.generate(this.getParams());
    }

    public String[] getParams(){
        return (String[])this.getParam(INPUT_PARAMS);
    }

    public String getMethod() {
        return method;
    }

}
