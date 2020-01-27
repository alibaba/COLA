package com.alibaba.cola.repository;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * @author lorne
 * @date 2020/1/27
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repository
public @interface RepositoryHandler {

}
