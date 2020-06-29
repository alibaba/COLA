package com.alibaba.cola.container.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.cola.container.TestsContainer;
import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.scan.RegexPatternTypeFilter;
import com.alibaba.cola.mock.scan.TypeFilter;
import com.alibaba.cola.mock.utils.Constants;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author shawnzhan.zxy
 * @since 2019/03/19
 */
public class TestMethodRunCmd extends AbstractCommand {
    /** 重新录制*/
    private static final String RE_RECORD = "rr";
    private String methodName;
    private String className;
    /** 是否片段录制*/
    private boolean segmentRecord = false;
    List<RegexPatternTypeFilter> recordFilters = new ArrayList<>();

    public TestMethodRunCmd(String cmdRaw) {
        super(cmdRaw);
        parseCommand();
        parseParamters(getCommandLine().getOptionValues(RE_RECORD));
    }

    @Override
    protected void action() {
        try {
            ColaMockito.g().getContext().setRecording(true);
            TestsContainer.getTestExecutor().execute(this);
            ColaMockito.g().getContext().setRecording(false);
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

    public boolean matchRecordFilter(String methodFullName){
        for(RegexPatternTypeFilter filter : recordFilters){
            if(filter.match(methodFullName)){
                return true;
            }
        }
        return false;
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
            methodName = cmd.substring(cmd.lastIndexOf(Constants.DOT)+1, cmd.indexOf("("));
            className = cmd.substring(0, cmd.lastIndexOf(Constants.DOT));
        }
        if (isIdeaMethod(cmd)) {
            methodName = cmd.substring(cmd.lastIndexOf(Constants.NOTE_SYMBOL)+1, cmd.length());
            className = cmd.substring(0, cmd.lastIndexOf(Constants.NOTE_SYMBOL));
        }
    }

    private void parseParamters(String[] parentNS){
        if(parentNS == null){
            recordFilters.add(new RegexPatternTypeFilter(".*"));
        }else {
            segmentRecord = true;
            Arrays.stream(parentNS).forEach(p -> {
                p = completingRegex(p);
                recordFilters.add(new RegexPatternTypeFilter(p));
            });
        }
    }

    /**
     * 补全前后正则
     * @param p
     * @return
     */
    private String completingRegex(String p){
        if(!p.endsWith("$")){
            p = p + ".*";
        }
        if(!p.startsWith("^")){
            p = ".*" + p;
        }
        p = p.replaceAll("(\\.\\*)+", ".*");
        return p;
    }

}
