package com.alibaba.craftsman;

import com.alibaba.cola.mock.ColaTestRecordController;
import com.alibaba.cola.mock.container.ColaMockContainer;
import com.alibaba.craftsman.app.MockTestBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {Application.class})})
@SpringBootApplication(scanBasePackages = {"com.alibaba.craftsman"})
public class TestApplication {

    public static void main(String[] args) {
        //这里填的是TestApplication
        ApplicationContext context = SpringApplication.run(Application.class, args);
        //配置测试容器
        ColaMockContainer.start(context);
    }

    //配置录制控制器
    @Bean
    public ColaTestRecordController sofaRecordController(){
        ColaTestRecordController recordController = new ColaTestRecordController("com.alibaba.craftsman");
        //自动生成代码的Test基类
        recordController.setTemplateSuperClass(MockTestBase.class);
        return recordController;
    }

}
