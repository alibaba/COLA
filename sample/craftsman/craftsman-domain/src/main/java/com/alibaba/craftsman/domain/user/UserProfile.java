package com.alibaba.craftsman.domain.user;

import com.alibaba.cola.domain.EntityObject;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.domain.metrics.appquality.AppQualityMetric;
import com.alibaba.craftsman.domain.metrics.devquality.DevQualityMetric;
import com.alibaba.craftsman.domain.metrics.weight.Weight;
import com.alibaba.craftsman.domain.metrics.techcontribution.ContributionMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.InfluenceMetric;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工档案
 * @author frankzhang
 */
@Data
@NoArgsConstructor
public class UserProfile extends EntityObject{
    private static Logger logger = LoggerFactory.getLogger(UserProfile.class);

    private String id;
    private String userId;
    private String userName;
    private String dep;
    private Role role;
    private String isManager;
    private Weight weight;
    private double totalScore;
    private double appQualityScore;
    private double techInfluenceScore;
    private double techContributionScore;
    private double devQualityScore;
    private double checkinCodeQuantity;

    private AppQualityMetric appQualityMetric;
    private InfluenceMetric influenceMetric;
    private ContributionMetric contributionMetric;
    private DevQualityMetric devQualityMetric;

    private static double MAXIMUM_SCORE = 100;
    private static double MINUMUM_SCORE = 0;

    public void calculateScore(){
        calculateTechInfluenceScore();
        calculateTechContributionScore();
        calculateDevQualityMetric();
        calculateAppQualityMetric();
        calculateTotalScore();
    }

    private void calculateAppQualityMetric() {
        Assert.notNull(appQualityMetric, "appQualityMetric is null, initialize it before calculating");
        appQualityScore = appQualityMetric.calculateScore();
    }

    private void calculateDevQualityMetric(){
        Assert.notNull(devQualityMetric, "devQualityMetric is null, initialize it before calculating");
        devQualityScore = devQualityMetric.calculateScore();
    }

    private void calculateTechInfluenceScore(){
        Assert.notNull(devQualityMetric, "influenceMetric is null, initialize it before calculating");
        techInfluenceScore = influenceMetric.calculateScore();
    }

    private void calculateTechContributionScore(){
        Assert.notNull(devQualityMetric, "contributionMetric is null, initialize it before calculating");
        techContributionScore = contributionMetric.calculateScore();
    }

    private void calculateTotalScore(){
        totalScore = round(this.techInfluenceScore) * influenceMetric.getWeight()
                + round(this.techContributionScore) * contributionMetric.getWeight()
                + round(this.devQualityScore) * devQualityMetric.getWeight()
                + round(this.appQualityScore) * appQualityMetric.getWeight();
    }

    private double round(double score){
        if(score > MAXIMUM_SCORE){
            score = MAXIMUM_SCORE;
        }else if(score < MINUMUM_SCORE){
            score = MINUMUM_SCORE;
        }
        return score;
    }

    public UserProfile(String userId){
        this.userId = userId;
    }
}
