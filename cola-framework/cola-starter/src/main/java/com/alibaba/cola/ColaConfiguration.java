package com.alibaba.cola;

import com.alibaba.cola.boot.Bootstrap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
@ComponentScan
@Configuration
public class ColaConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public Bootstrap bootstrap(PackagesToScan packagesToScan) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setPackages(packagesToScan.getList());
        return bootstrap;
    }

    @Bean
    @ConfigurationProperties(prefix = "alibaba.cola.packages")
    public PackagesToScan packagesToScan(){
        return new PackagesToScan();
    }

    public class PackagesToScan{

        @Getter
        @Setter
        List<String> list;

    }

}
