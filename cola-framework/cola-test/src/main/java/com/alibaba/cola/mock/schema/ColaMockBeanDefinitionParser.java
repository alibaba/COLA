package com.alibaba.cola.mock.schema;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.ColaMockController;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author shawnzhan.zxy
 * @date 2018/10/11
 */
public class ColaMockBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class getBeanClass(Element element) {
        return ColaMockController.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String basePackages = element.getAttribute("base-package");
        bean.addConstructorArgValue(basePackages);

        NodeList childNodeList = element.getChildNodes();

        List<String> regexList = new ArrayList<>();
        List<String> annotationList = new ArrayList<>();
        List<String> assignableList = new ArrayList<>();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node node = childNodeList.item(i);
            if(!(node instanceof Element)){
                continue;
            }
            Element cldElement = (Element)node;
            String type = cldElement.getAttribute("type");
            String expression = cldElement.getAttribute("expression");

            if (type.equals("regex")) {
                regexList.add(expression);
            } else if (type.equals("annotation")) {
                annotationList.add(expression);
            } else if (type.equals("assignable")) {
                assignableList.add(expression);
            }
        }

        bean.addPropertyValue("mockRegex", regexList);
        bean.addPropertyValue("mockAnnotation", annotationList);
        bean.addPropertyValue("mockAssignable", assignableList);
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
