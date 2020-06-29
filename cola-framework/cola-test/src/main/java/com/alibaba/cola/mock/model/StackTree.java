package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.utils.StackSearcher;

/**
 * 调用栈树
 * @author shawnzhan.zxy
 * @since 2019/05/22
 */
public class StackTree {

    List<StackNode> nodeList = new ArrayList<>();

    public StackNode recordCurrentStackPoint(ColaTestModel testModel){
        StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
        stackTraceElements = StackSearcher.getBusinessStack(testModel, stackTraceElements);
        StackNode curNode = null;
        for(int i = stackTraceElements.length - 1; i > -1; i--){
            StackTraceElement element = stackTraceElements[i];
            if(i == stackTraceElements.length - 1){
                curNode = buildRoot(element);
            }else{
                curNode = append(element, curNode);
            }
        }
        return curNode;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("\n============framework trace============\n");
        for(StackNode node : nodeList){
            sb.append(node.toString()).append("\n");
        }
        sb.append("============================\n");
        return sb.toString();
    }

    private StackNode append(StackTraceElement element, StackNode parentNode){
        StackNode curNode = new StackNode(element.getClassName(), element.getMethodName(), element.getLineNumber());
        StackNode findNode = parentNode.findChildNode(curNode);
        if(findNode != null){
            return findNode;
        }
        parentNode.addChild(curNode);
        return curNode;
    }

    private StackNode buildRoot(StackTraceElement element){
        StackNode curNode = new StackNode(element.getClassName(), element.getMethodName(), element.getLineNumber());
        for(StackNode root : nodeList){
            if(root.equals(curNode)){
                return root;
            }
        }
        nodeList.add(curNode);
        return curNode;
    }
}
