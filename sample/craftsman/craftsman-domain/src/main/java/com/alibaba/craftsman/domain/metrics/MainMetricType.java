package com.alibaba.craftsman.domain.metrics;

public enum MainMetricType {
    APP_QUALITY("app-quality","应用质量"),
    TECH_INFLUENCE("tech-influence","技术影响力"),
    TECH_CONTRIBUTION("tech-contribution","技术贡献"),
    DEV_QUALITY("dev-quality","开发质量");

    private String metricCode;
    private String metricName;

    private MainMetricType(String metricCode, String metricName){
        this.metricCode = metricCode;
        this.metricName = metricName;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public String getMetricName() {
        return metricName;
    }

    public static MainMetricType of(String metricCode){
        if(metricCode == null){
            return null;
        }
        for (MainMetricType metricMainType : MainMetricType.values()) {
            if(metricCode.equals(metricMainType.metricCode)){
                return metricMainType;
            }
        }
        return null;
    }
}
