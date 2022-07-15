package com.alibaba.cola.statemachine.spring.config;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.spring.annotation.StateMachine;
import com.alibaba.cola.statemachine.spring.annotation.StateMachines;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

@Slf4j
public class StateMachineAnnotationBeanPostProcessor implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor, EnvironmentAware, ResourceLoaderAware, BeanClassLoaderAware {


    public static final String COMMON_SPRING_UTILS_STATE_MACHINE_SCAN_PACKAGE = "common.spring.utils.stateMachineScanPackage";

    public StateMachineAnnotationBeanPostProcessor(Collection<String> packagesToScan) {
        this.packagesToScan = new LinkedHashSet<>(packagesToScan);
    }

    private final Set<String> packagesToScan;

    private ResourceLoader resourceLoader;

    private Environment environment;

    private ClassLoader classLoader;

    private ApplicationContext applicationContext;

    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     *
     * @param registry the bean definition registry used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        Set<String> resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);

        if (!CollectionUtils.isEmpty(resolvedPackagesToScan)) {
            registerStateMachine(resolvedPackagesToScan, registry);
        } else {
            log.warn("packagesToScan is empty , stateMachine registry will be ignored!");
        }
    }

    private void registerStateMachine(Set<String> resolvedPackagesToScan, BeanDefinitionRegistry registry) {
        StateMachineBeanDefinitionScanner scanner = new StateMachineBeanDefinitionScanner(registry, environment, resourceLoader);

        scanner.addIncludeFilter(new AnnotationTypeFilter(StateMachine.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(StateMachines.class));

        List<Class<?>> classList = new ArrayList<>();
        for (String packageToScan : resolvedPackagesToScan) {
            // find @StateMachine
            scanner.scan(packageToScan);

            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);


            if (!CollectionUtils.isEmpty(beanDefinitions)) {
                beanDefinitions.forEach(beanDefinition -> {
                    if (StringUtils.hasText(beanDefinition.getBeanClassName())) {
                        // 注册状态机
                        // get beanClass
                        Class<?> targetClass = ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), classLoader);
                        if (!Action.class.isAssignableFrom(targetClass)) {
                            log.warn("target bean name {} not set interface Action.class ", targetClass);
                            return;
                        }
                        classList.add(targetClass);
                    }
                });
            } else {
                log.warn("No Spring Bean annotating @StateMachine was found under package[" + packageToScan + "]");
            }
        }

        AbstractBeanDefinition serviceBeanDefinition =
                buildStateMachineBeanDefinition(classList);
        registry.registerBeanDefinition(StateMachineServiceBean.class.getName(),serviceBeanDefinition);
    }

    private AbstractBeanDefinition buildStateMachineBeanDefinition(List<Class<?>> classList) {
        BeanDefinitionBuilder builder = rootBeanDefinition(StateMachineServiceBean.class);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue("classList",classList);
        propertyValues.addPropertyValue("applicationContext",applicationContext);

        return beanDefinition;
    }

    /**
     * get need scan package
     *
     * @param packagesToScan
     * @return
     */
    private Set<String> resolvePackagesToScan(Set<String> packagesToScan) {

        Set<String> resolvedPackagesToScan = environment.getProperty(COMMON_SPRING_UTILS_STATE_MACHINE_SCAN_PACKAGE, Set.class);

        if (CollectionUtils.isEmpty(resolvedPackagesToScan)) {
            resolvedPackagesToScan = new LinkedHashSet<>(packagesToScan);
        }

        for (String packageToScan : packagesToScan) {
            if (StringUtils.hasText(packageToScan)) {
                String resolvedPackageToScan = environment.resolvePlaceholders(packageToScan.trim());
                resolvedPackagesToScan.add(resolvedPackageToScan);
            }
        }
        return resolvedPackagesToScan;
    }

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Set the ResourceLoader that this object runs in.
     * <p>This might be a ResourcePatternResolver, which can be checked
     * through {@code instanceof ResourcePatternResolver}. See also the
     * {@code ResourcePatternUtils.getResourcePatternResolver} method.
     * <p>Invoked after population of normal bean properties but before an init callback
     * like InitializingBean's {@code afterPropertiesSet} or a custom init-method.
     * Invoked before ApplicationContextAware's {@code setApplicationContext}.
     *
     * @param resourceLoader the ResourceLoader object to be used by this object
     * @see ResourcePatternResolver
     * @see ResourcePatternUtils#getResourcePatternResolver
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Callback that supplies the bean {@link ClassLoader class loader} to
     * a bean instance.
     * <p>Invoked <i>after</i> the population of normal bean properties but
     * <i>before</i> an initialization callback such as
     * {@link InitializingBean InitializingBean's}
     * {@link InitializingBean#afterPropertiesSet()}
     * method or a custom init-method.
     *
     * @param classLoader the owning class loader
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
