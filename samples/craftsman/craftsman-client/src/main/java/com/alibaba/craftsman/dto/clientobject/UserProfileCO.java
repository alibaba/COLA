package com.alibaba.craftsman.dto.clientobject;

import com.alibaba.cola.dto.ClientObject;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserProfileCO extends ClientObject {

    public final static String IS_MANAGER = "y";
    public final static String IS_NOT_MANAGER = "n";

    public final static String DEV_ROLE = "DEV";
    public final static String QA_ROLE = "QA";
    public final static String OTHER_ROLE = "OTHER";

    @NotEmpty
    private String userId;
    private String userName;
    private String dep;
    private String role;
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
