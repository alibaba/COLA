package com.alibaba.cola.container.command;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.ColaTestRecordController;
import com.alibaba.cola.mock.agent.ColaTestAttach;
import com.alibaba.cola.mock.agent.convert.AgentArgsConvertor;
import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.scan.ClassPathTestScanner;
import com.alibaba.cola.mock.scan.RegexPatternTypeFilter;
import com.alibaba.cola.mock.scan.TypeFilter;
import com.alibaba.cola.mock.utils.Constants;

import com.sun.tools.attach.VirtualMachine;
import org.junit.runner.Description;

/**
 * @author shawnzhan.zxy
 * @date 2019/05/11
 */
public class OnlineRecordCommand extends AbstractCommand{
    private String methodName;
    private String className;
    private ClassPathTestScanner scanner;
    private VirtualMachine machine;

    public OnlineRecordCommand(String cmdRaw) {
        super(cmdRaw);
        scanner = new ClassPathTestScanner();
        parseCommand();
    }

    @Override
    protected void action() {
        try {
            startRecord();
            AgentArgs agentArgs = AgentArgsConvertor.convertForOnlineRecord(className, methodName);
            machine = ColaTestAttach.attach(agentArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void cleanContext(){
        ColaTestAttach.detach(machine);
        ColaMockito.g().getContext().setColaTestMeta(null);
    }

    private void parseCommand(){
        String cmd = getCommandLine().getArgs()[1];
        if (isEclipseMethod(cmd)) {
            methodName = cmd.substring(cmd.lastIndexOf(Constants.DOT)+1, cmd.indexOf("("));
            className = cmd.substring(0, cmd.lastIndexOf(Constants.DOT));
        }
        if (isIdeaMethod(cmd)) {
            methodName = cmd.substring(cmd.lastIndexOf(Constants.NOTE_SYMBOL)+1, cmd.length());
            className = cmd.substring(0, cmd.lastIndexOf(Constants.NOTE_SYMBOL));
        }
    }

    private void startRecord() throws ClassNotFoundException {
        ColaTestModel colaTestModel = createCurColaTestModel();

        List<ColaTestModel> colaTestModelList = new ArrayList<>();
        colaTestModelList.add(colaTestModel);
        ColaMockito.g().getContext().setColaTestModelList(colaTestModelList);

        Description description = Description.createTestDescription(colaTestModel.getTestClazz(), methodName);
        ColaMockito.g().getContext().setColaTestMeta(new ColaTestDescription(description));
    }

    private ColaTestModel createCurColaTestModel() throws ClassNotFoundException {
        ColaTestModel colaTestModel = new ColaTestModel();
        colaTestModel.setTestClazz(Class.forName(className));
        ColaTestModel templateColaTestModel = scanner.scanColaTest(ColaTestRecordController.getTemplateSuperClass());
        if(templateColaTestModel == null){
            throw new RuntimeException("templateSuperClass not exists!");
        }
        for(TypeFilter filter : templateColaTestModel.getTypeFilters()){
            colaTestModel.addMockFilter(filter);
        }
        return colaTestModel;
    }
}
