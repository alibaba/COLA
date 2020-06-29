package com.alibaba.cola.mock.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.codehaus.groovy.runtime.StringBufferWriter;

/**
 * @author shawnzhan.zxy
 * @since 2019/05/13
 */
public class TemplateBuilder {
    private Map<String, Object> var = new HashMap<>();
    private String templateContent;

    public TemplateBuilder(String templateContent){
        this.templateContent = templateContent;
    }

    public TemplateBuilder addVar(String key, Object value){
        var.put(key, value);
        return this;
    }

    public void build(Writer writer) {
        try {
            // 创建一个模板对象
            Template t = new Template(null, templateContent, null);
            // 执行插值，并输出到指定的输出流中
            t.process(var, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String build() {
        StringWriter writer = new StringWriter();
        build(writer);
        return writer.toString();
    }

    public void setVar(Map<String, Object> var) {
        this.var = var;
    }

    public static void main(String[] args) {
        TemplateBuilder builder = new TemplateBuilder(Constants.AGENT_NEW_METHOD_TEMPALTE);
        builder.addVar("beforeCode", "aa")
            .addVar("afterCode", "bb")
            .addVar("renamedMethodName", "ccc")
            .addVar("isReturn", true);
        System.out.println(builder.build());
    }
}
