package com.alibaba.cola.test.command;

import com.alibaba.cola.test.TestsContainer;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * TestMethodRunCmd
 *
 * @author Frank Zhang
 * @date 2020-11-17 4:43 PM
 */
public class TestMethodRunCmd extends AbstractCommand {

    private static final String RE_RECORD = "rr";
    public final static String DOT = ".";
    public final static String NOTE_SYMBOL = "#";
    private String methodName;
    private String className;
    /** 是否片段录制*/
    private boolean segmentRecord = false;
    List<RegexPatternTypeFilter> recordFilters = new ArrayList<>();

    public TestMethodRunCmd(String cmdRaw) {
        super(cmdRaw);
        parseCommand();
    }

    @Override
    protected void action() {
        try {
            TestsContainer.getTestExecutor().execute(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initParser(Options options) {
        Option point = Option.builder(RE_RECORD)
                .hasArgs()
                .argName("p1,p2...")
                .valueSeparator(',')
                .desc("A directories list with ',' separate to handle its child files")
                .build();
        options.addOption(point);
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public boolean isSegmentRecord() {
        return segmentRecord;
    }

    private void parseCommand(){
        String cmd = getCommandLine().getArgs()[0];
        if (isEclipseMethod(cmd)) {
            methodName = cmd.substring(cmd.lastIndexOf(DOT)+1, cmd.indexOf("("));
            className = cmd.substring(0, cmd.lastIndexOf(DOT));
        }
        if (isIdeaMethod(cmd)) {
            methodName = cmd.substring(cmd.lastIndexOf(NOTE_SYMBOL)+1, cmd.length());
            className = cmd.substring(0, cmd.lastIndexOf(NOTE_SYMBOL));
        }
    }

}