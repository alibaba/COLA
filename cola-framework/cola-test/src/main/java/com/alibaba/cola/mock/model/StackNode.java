package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.utils.CommonUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @date 2019/05/22
 */
public class StackNode {
    String className;
    String method;
    int line;
    int level;
    StackNode parent;
    List<StackNode> childList = new ArrayList<>();

    public StackNode(String className, String method, int line){
        this.className = className;
        this.method = method;
        this.line = line;
        this.level = 1;
    }

    public void addChild(StackNode node){
        node.setLevel(level + 1);
        node.setParent(this);
        childList.add(node);
    }

    public StackNode findChildNode(StackNode node){
        for(StackNode child : childList){
            if(child.equals(node)){
                return child;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj){
        StackNode node = (StackNode)obj;
        if(node.className.equals(this.className) && node.method.equals(this.method)
            && node.getLine() == node.getLine()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return pretty2DependencyTree("");
    }

    protected String pretty2DependencyTree(String prefix){
        StringBuilder sb = new StringBuilder();

        boolean isLast = true;
        if(parent != null && parent.childList.size() > 0){
            isLast = parent.childList.get(parent.childList.size() - 1).equals(this);
        }
        String selfPrefix = prefix + (isLast?"\\- ":"+- ");
        String lineStr = line > 0 ? "("+CommonUtils.getSimpleClassName(this.className)+".java:" + line + ")" : ":mock service";
        sb.append(selfPrefix).append(className + ".").append(method).append(lineStr).append("\n");

        String childPrefix = prefix + (isLast?"   ":"|  ");
        for(int i = 0; i < this.childList.size(); i++){
            StackNode node = this.childList.get(i);
            sb.append(node.pretty2DependencyTree(childPrefix));
        }

        return sb.toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<StackNode> getChildList() {
        return childList;
    }

    public void setChildList(List<StackNode> stackNodeList) {
        this.childList = stackNodeList;
    }

    public StackNode getParent() {
        return parent;
    }

    public void setParent(StackNode parent) {
        this.parent = parent;
    }
}
