package com.alibaba.cola.job.model;


import com.alibaba.cola.job.ExecutionContext;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Job实例：包含了Job的定义，以及运行job的runtime信息，也就是ExecutionContext
 * 一个BatchJob会包含多个JobInstance，是为了BatchJob创建的模型。
 */
@Data
@AllArgsConstructor
public class JobInstance {
    private Job job;
    private ExecutionContext executionContext;
}

