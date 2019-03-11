package com.alibaba.cola.mock.scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.model.ColaTestModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.asm.ClassReader;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

/**
 * mapper scanner
 * @author shawnzhan.zxy
 * @date 2018/09/24
 */
public class ClassPathTestScanner {
    private Environment environment = new StandardEnvironment();
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public ColaTestModel scanColaTest(Class testClzz){
        return resolveTestClass(testClzz);
    }

    public List<ColaTestModel> scanColaTests(String... basePackages) throws Exception {
        List<ColaTestModel> colaTestModelList = new ArrayList<>();
        Set<Class<?>> candidates = findTestComponents(basePackages);
        for (Class<?> testClzz : candidates){
            ColaTestModel colaTestModel = resolveTestClass(testClzz);
            if(colaTestModel != null){
                colaTestModelList.add(colaTestModel);
            }
        }

        return colaTestModelList;
    }

    private ColaTestModel resolveTestClass(Class<?> testClzz){
        ColaMockConfig colaMockConfig = testClzz.getAnnotation(ColaMockConfig.class);
        if(colaMockConfig == null){
            return null;
        }
        ColaTestModel colaTestModel = new ColaTestModel();
        colaTestModel.setTestClazz(testClzz);
        colaTestModel.setColaMockConfig(colaMockConfig);
        resolveColaMockConfig(colaTestModel);
        return colaTestModel;
    }

    private Set<Class<?>> findTestComponents(String... basePackages) throws Exception {
        Set<Class<?>> testClzzList = new HashSet<>();
        for(String basePackage : basePackages){
            if(StringUtils.isBlank(basePackage)){
                continue;
            }
            String resourcePath = ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));

            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resourcePath + '/' + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for(Resource res : resources){
                testClzzList.add(Thread.currentThread().getContextClassLoader().loadClass(getClassName(res)));
            }
        }

        return testClzzList;
    }

    private void resolveColaMockConfig(ColaTestModel colaTestModel){
        Class[] mocks = colaTestModel.getColaMockConfig().mocks();
        String[] regexMocks = colaTestModel.getColaMockConfig().regexMocks();
        Class[] annotationMocks = colaTestModel.getColaMockConfig().annotationMocks();

        if(regexMocks != null && regexMocks.length > 0){
            Arrays.stream(regexMocks).forEach(p->{
                colaTestModel.addFilter(new RegexPatternTypeFilter(p));
            });
        }
        if(mocks != null && mocks.length > 0){
            Arrays.stream(mocks).forEach(p->{
                colaTestModel.addFilter(new AssignableTypeFilter(p));
            });
        }
        if(annotationMocks != null && annotationMocks.length > 0){
            Arrays.stream(annotationMocks).forEach(p->{
                colaTestModel.addFilter(new AnnotationTypeFilter(p));
            });
        }
    }


    private String getClassName(Resource resource) throws IOException {
        ClassReader classReader = new ClassReader(resource.getInputStream());
        String className = classReader.getClassName();
        className = className.replaceAll("/", ".");
        return className;
    }
}
