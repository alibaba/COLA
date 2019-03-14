package com.alibaba.craftsman.app;

import com.alibaba.cola.mock.annotation.ColaMockConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/31
 */
@ContextConfiguration("classpath:spring-mock-test.xml")
@ColaMockConfig(mocks={}, annotationMocks = {Mapper.class})
public class MockTestBase {
}
