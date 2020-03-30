package com.alibaba.craftsman.dto.clientobject;

import com.alibaba.cola.dto.ClientObject;
import lombok.Data;

/**
 * RefactoringMetricCO
 *
 * @author Frank Zhang
 * @date 2019-03-04 10:58 AM
 */
@Data
public class RefactoringMetricCO  extends AbstractMetricCO {

    public static final String METHOD_LEVEL = "METHOD";
    public static final String MODULE_LEVEL = "MODULE";
    public static final String PROJECT_LEVEL = "PROJECT";

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 文档链接
     */
    private String docUrl;

    /**
     * 代码链接
     */
    private String codeUrl;
    /**
     * 重构的范围
     */
    private String refactoringLevel;
}
