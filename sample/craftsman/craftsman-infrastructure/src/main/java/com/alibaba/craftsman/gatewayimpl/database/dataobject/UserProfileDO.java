package com.alibaba.craftsman.gatewayimpl.database.dataobject;


/**
 * UserProfileDO
 *
 * @author Frank Zhang
 * @date 2019-02-27 5:00 PM
 */

public class UserProfileDO extends BaseDO{

    /**
     * 域账号
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 部门
     */
    private String dep;

    /**
     * 角色
     */
    private String role;

    /**
     * 是否主管
     */
    private String isManager;

    /**
     * 综合得分
     */
    private double totalScore;

    /**
     * 代码质量分
     */
    private double appQualityScore;

    /**
     * 技术影响力分
     */
    private double techInfluenceScore;

    /**
     * 技术贡献分
     */
    private double techContributionScore;

    /**
     * 开发质量分
     */
    private double devQualityScore;

    /**
     * checkin代码量
     */
    private double checkinCodeQuantity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getAppQualityScore() {
        return appQualityScore;
    }

    public void setAppQualityScore(double appQualityScore) {
        this.appQualityScore = appQualityScore;
    }

    public double getTechInfluenceScore() {
        return techInfluenceScore;
    }

    public void setTechInfluenceScore(double techInfluenceScore) {
        this.techInfluenceScore = techInfluenceScore;
    }

    public double getTechContributionScore() {
        return techContributionScore;
    }

    public void setTechContributionScore(double techContributionScore) {
        this.techContributionScore = techContributionScore;
    }

    public double getDevQualityScore() {
        return devQualityScore;
    }

    public void setDevQualityScore(double devQualityScore) {
        this.devQualityScore = devQualityScore;
    }

    public double getCheckinCodeQuantity() {
        return checkinCodeQuantity;
    }

    public void setCheckinCodeQuantity(double checkinCodeQuantity) {
        this.checkinCodeQuantity = checkinCodeQuantity;
    }
}
