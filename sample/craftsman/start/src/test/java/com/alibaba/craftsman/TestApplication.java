package com.alibaba.craftsman;

import com.alibaba.cola.container.TestsContainer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TestApplication
 *
 * @author Frank Zhang
 * @date 2019-02-27 4:14 PM
 */
@SpringBootApplication(scanBasePackages = {"com.alibaba.craftsman","com.alibaba.cola"})
public class TestApplication {

        public static void main(String[] args) {
            SpringApplication.run(TestApplication.class, args);
            TestsContainer.start();
        }

}
