package com.alibaba.cola.mock.spring;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author shawnzhan.zxy
 * @since 2019/04/28
 */
public class ColaBeanDefinitionReader extends XmlBeanDefinitionReader{
    /**
     * Create new XmlBeanDefinitionReader for the given bean factory.
     *
     * @param registry the BeanFactory to load bean definitions into,
     *                 in the form of a BeanDefinitionRegistry
     */
    public ColaBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * Register the bean definitions contained in the given DOM document.
     * Called by {@code loadBeanDefinitions}.
     * <p>Creates a new instance of the parser class and invokes
     * {@code registerBeanDefinitions} on it.
     * @param doc the DOM document
     * @param resource the resource descriptor (for context information)
     * @return the number of bean definitions found
     * @throws BeanDefinitionStoreException in case of parsing errors
     * @see #loadBeanDefinitions
     * @see #setDocumentReaderClass
     * @see BeanDefinitionDocumentReader#registerBeanDefinitions
     */
    @Override
    public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
        assembleExclude4SpringBootApplication(doc);

        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }

    private void assembleExclude4SpringBootApplication(Document doc){
        Node compScan = findCompScanNode(doc.getDocumentElement());
        if(compScan == null){
            throw new RuntimeException("cannot find component-scan ");
        }
        Node excludeNode = createExcludeFilterNode(doc, compScan);
        compScan.appendChild(excludeNode);
    }

    private Node findCompScanNode(Element root){
        NodeList nodeList = root.getChildNodes();
        for(int i = 0; i <nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node instanceof Element && node.getLocalName().toLowerCase().equals("component-scan")){
                return node;
            }
        }
        return null;
    }

    private Node createExcludeFilterNode(Document doc, Node compScan){
        try {
            //创建元素使用
            String prefix = compScan.getPrefix();
            Element excludeNode = doc.createElementNS(compScan.getNamespaceURI(), prefix+":exclude-filter");;
            excludeNode.setAttribute("type", "annotation");
            excludeNode.setAttribute("expression", "org.springframework.boot.autoconfigure.SpringBootApplication");
            return excludeNode;
        }catch (Exception e){

        }
        return null;
    }
}
