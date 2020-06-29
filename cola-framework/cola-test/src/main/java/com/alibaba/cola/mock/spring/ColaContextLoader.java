package com.alibaba.cola.mock.spring;

import com.alibaba.cola.mock.utils.reflection.BeanPropertySetter;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * @author shawnzhan.zxy
 * @since 2019/04/25
 */
public class ColaContextLoader extends GenericXmlContextLoader {

    /**
     * Prepare the {@link GenericApplicationContext} created by this {@code ContextLoader}.
     * Called <i>before</i> bean definitions are read.
     *
     * <p>The default implementation is empty. Can be overridden in subclasses to
     * customize {@code GenericApplicationContext}'s standard settings.
     *
     * @param context the context that should be prepared
     * @see #loadContext(MergedContextConfiguration)
     * @see #loadContext(String...)
     * @see GenericApplicationContext#setAllowBeanDefinitionOverriding
     * @see GenericApplicationContext#setResourceLoader
     * @see GenericApplicationContext#setId
     * @see #prepareContext(ConfigurableApplicationContext, MergedContextConfiguration)
     * @since 2.5
     */
    @Override
    protected void prepareContext(GenericApplicationContext context) {
        BeanPropertySetter setter = new BeanPropertySetter(context, "beanFactory");
        setter.setValue(new ColaBeanFactory());
    }

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        beanFactory.setInstantiationStrategy(new ColaBeanInstantiationStrategy());

        //GenericBeanDefinition definition = new GenericBeanDefinition();
        //definition.getConstructorArgumentValues().addGenericArgumentValue("com.alibaba.crm.marketing");
        //definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
        //definition.setBeanClass(ColaMockController.class);
        //definition.setScope("");
        //definition.setLazyInit(false);
        //definition.setAutowireCandidate(true);
        //beanFactory.registerBeanDefinition("colaMockController", definition);
    }

    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        return new ColaBeanDefinitionReader(context);
    }
}
