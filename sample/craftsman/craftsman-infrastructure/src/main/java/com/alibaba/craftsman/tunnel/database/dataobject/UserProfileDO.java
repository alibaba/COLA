package com.alibaba.craftsman.tunnel.database.dataobject;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * UserProfileDO
 *
 * @author Frank Zhang
 * @date 2019-02-27 5:00 PM
 */
@Data
@ToString
public class UserProfileDO {

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

}
