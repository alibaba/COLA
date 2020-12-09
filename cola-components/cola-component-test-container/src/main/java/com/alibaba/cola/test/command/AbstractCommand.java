package com.alibaba.cola.test.command;

import org.apache.commons.cli.*;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractCommand
 *
 * @author Frank Zhang
 * @date 2020-11-17 4:33 PM
 */
public abstract class AbstractCommand {
    private static CommandLineParser parser = new DefaultParser();
    protected static AbstractCommand curCmd;
    protected static AbstractCommand preCmd;

    protected String cmdRaw;
    private Map<String, Object> params;
    private Options options;
    private CommandLine commandLine;

    private final String SPACE = " ";
    private final String EMPTY = "";

    public AbstractCommand(String cmdRaw){
        this.cmdRaw = cmdRaw.replaceAll(" +", SPACE);
        params = new HashMap<>();
        options = new Options();
        initParser(options);
        commandLine = parse();
    }

    public void execute(){
        System.out.println("===Run start==== "+cmdRaw);
        action();
        System.out.println("===Run end====\n");
    }

    /**
     * 清理当前命令的上下文
     */
    protected void cleanContext(){}

    protected void initParser(Options options){};

    protected abstract void action();

    public CommandLine parse(){
        try {
            return parser.parse(options, cmdRaw.split(SPACE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getParam(String key){
        return params.get(key);
    }

    public void putParam(String key, Object value){
        params.put(key, value);
    }

    public String getStringParam(String key){
        Object value = params.get(key);
        if(value == null){
            return EMPTY;
        }
        return value.toString();
    }

    public boolean isEclipseMethod(String input) {
        return input.indexOf("(") > 0 ;
    }

    public boolean isIdeaMethod(String input) {
        return input.indexOf("#") > 0 ;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public static AbstractCommand createCmd(String cmdRaw){
        if(StringUtils.isEmpty(cmdRaw)){
            return null;
        }

        AbstractCommand command = null;

         if(cmdRaw.matches(CommandEnum.TestMethodRunCmd.getDesc())){
            command = new TestMethodRunCmd(cmdRaw);
        } else if(cmdRaw.matches(CommandEnum.TestClassRunCmd.getDesc())){
            command = new TestClassRunCmd(cmdRaw);
        }else if(cmdRaw.matches(CommandEnum.GuideCmd.getDesc())){
            command = new GuideCmd(cmdRaw);
        }

        if(command != null){
            preCmd = curCmd;
            curCmd = command;
        }
        if(preCmd != null){
            preCmd.cleanContext();
        }

        return command;
    }

}