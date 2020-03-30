package com.alibaba.craftsman.domain.metrics;

/**
 * SubMetricType
 *
 * @author Frank Zhang
 * @date 2018-08-27 4:54 PM
 */
public enum SubMetricType {

    LongMethod(MainMetricType.APP_QUALITY, "LongMethod","超长方法"),
    Cyclomatic(MainMetricType.APP_QUALITY, "Cyclomatic","圈复杂度"),
    Duplication(MainMetricType.APP_QUALITY, "Duplication","代码重复度"),

    App(MainMetricType.APP_QUALITY, "App","App应用"),

    ATA(MainMetricType.TECH_INFLUENCE, "ATA", "ATA文章"),
    Sharing(MainMetricType.TECH_INFLUENCE, "Sharing", "技术分享"),
    Patent(MainMetricType.TECH_INFLUENCE, "Patent", "专利"),
    Paper(MainMetricType.TECH_INFLUENCE, "Paper", "论文"),

    CodeReview(MainMetricType.TECH_CONTRIBUTION, "CodeReview", "Code Review"),
    Refactoring(MainMetricType.TECH_CONTRIBUTION, "Refactoring", "重构"),
    Misc(MainMetricType.TECH_CONTRIBUTION, "Misc", "其他贡献"),

    Bug(MainMetricType.DEV_QUALITY, "Bug", "提测Bug"),
    Fault(MainMetricType.DEV_QUALITY, "Fault", "故障"),
    ;

    //度量类型
    private MainMetricType parentType;

    //度量项Code
    private String metricSubTypeCode;

    //度量项中文名称
    private String metricSubTypeName;

    private SubMetricType(MainMetricType parentType, String metricSubTypeCode, String metricSubTypeName){
        this.parentType = parentType;
        this.metricSubTypeCode = metricSubTypeCode;
        this.metricSubTypeName = metricSubTypeName;
    }

    public MainMetricType getParentType() {
        return parentType;
    }

    public String getMetricSubTypeCode() {
        return metricSubTypeCode;
    }

    public String getMetricSubTypeName() {
        return metricSubTypeName;
    }


}
