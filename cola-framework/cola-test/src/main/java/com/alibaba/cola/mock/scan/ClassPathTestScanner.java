package com.alibaba.cola.mock.scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.cola.mock.annotation.ColaMock;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.cola.mock.runner.ColaTestUnitRunner;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
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
 * @since 2018/09/24
 */
public class ClassPathTestScanner {
    private Environment environment = new StandardEnvironment();
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public ColaTestModel scanColaTest(Class testClzz){
        return resolveTestClass(testClzz, false);
    }

    public List<ColaTestModel> scanColaTests(String... basePackages) throws Exception {
        List<ColaTestModel> colaTestModelList = new ArrayList<>();
        Set<Class<?>> candidates = findTestComponents(basePackages);
        for (Class<?> testClzz : candidates){
            ColaTestModel colaTestModel = resolveTestClass(testClzz, false);
            if(colaTestModel != null){
                colaTestModelList.add(colaTestModel);
            }
        }

        return colaTestModelList;
    }

    public ColaTestModel resolveTestClass(Class<?> testClzz, boolean checkConfig){
        if(testClzz == null){
            return null;
        }
        ColaMockConfig colaMockConfig = testClzz.getAnnotation(ColaMockConfig.class);
        if(checkConfig && colaMockConfig == null){
            return null;
        }

        ColaTestModel colaTestModel = new ColaTestModel();
        colaTestModel.setTestClazz(testClzz);
        colaTestModel.setColaMockConfig(colaMockConfig);
        resolveColaMockConfig(colaTestModel);

        if(colaTestModel.getTypeFilters().size() == 0){
            fillTypeFiltersFromSuperClass(colaTestModel);
        }
        return colaTestModel;
    }

    private void fillTypeFiltersFromSuperClass(ColaTestModel colaTestModel){
        ColaTestModel superTestModel = resolveTestClass(colaTestModel.getTestClazz().getSuperclass(), true);
        if(superTestModel == null){
            return;
        }
        for(TypeFilter filter : superTestModel.getTypeFilters()){
            colaTestModel.addMockFilter(filter);
        }
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
                Class testClass = Thread.currentThread().getContextClassLoader().loadClass(getClassName(res));
                if(!isColaTestClass(testClass)){
                    continue;
                }
                testClzzList.add(testClass);
            }
        }

        return testClzzList;
    }

    private void resolveColaMockConfig(ColaTestModel colaTestModel){
        if(colaTestModel.getColaMockConfig() == null){
            return;
        }

        Class[] mocks = colaTestModel.getColaMockConfig().mocks();
        String[] regexMocks = colaTestModel.getColaMockConfig().regexMocks();
        Class[] annotationMocks = colaTestModel.getColaMockConfig().annotationMocks();
        Class[] dataManufactures = colaTestModel.getColaMockConfig().dataManufactures();

        if(regexMocks != null && regexMocks.length > 0){
            Arrays.stream(regexMocks).forEach(p->{
                colaTestModel.addMockFilter(new RegexPatternTypeFilter(p));
            });
        }
        if(mocks != null && mocks.length > 0){
            Arrays.stream(mocks).forEach(p->{
                colaTestModel.addMockFilter(new AssignableTypeFilter(p));
            });
        }
        if(annotationMocks != null && annotationMocks.length > 0){
            Arrays.stream(annotationMocks).forEach(p->{
                colaTestModel.addMockFilter(new AnnotationTypeFilter(p));
            });
        }
        if(dataManufactures != null && dataManufactures.length > 0){
            Arrays.stream(dataManufactures).forEach(p->{
                colaTestModel.addDataManufactureFilter(new AssignableTypeFilter(p));
            });
        }
    }

    private String getClassName(Resource resource) throws IOException {
        ClassReader classReader = new ClassReader(resource.getInputStream());
        String className = classReader.getClassName();
        className = className.replaceAll("/", ".");
        return className;
    }

    private boolean isColaTestClass(Class<?> testClzz){
        RunWith runWith = testClzz.getAnnotation(RunWith.class);
        if(runWith == null){
            return false;
        }
        if(runWith.value().equals(ColaTestRunner.class)
            || runWith.value().equals(ColaTestUnitRunner.class)){
            return true;
        }
        return false;
    }
}
