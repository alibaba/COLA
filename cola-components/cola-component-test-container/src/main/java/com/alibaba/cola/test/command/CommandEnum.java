package com.alibaba.cola.test.command;

/**
 * CommandEnum
 *
 * @author Frank Zhang
 * @date 2020-11-17 4:39 PM
 */
public enum CommandEnum {
    TestMethodRunCmd("", "[^\\s]*[\\(#].*"),
    TestClassRunCmd("", "(\\w+\\.\\w+){1,}"),
    GuideCmd("", "^[rhq]$"),
    ;

    private String cmd;
    private String desc;

    CommandEnum(String cmd, String desc){
        this.cmd = cmd;
        this.desc = desc;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

