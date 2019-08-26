package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.domain.metrics.SubMetric;
import com.alibaba.craftsman.domain.metrics.appquality.AppMetric;
import com.alibaba.craftsman.domain.metrics.appquality.AppQualityMetric;
import com.alibaba.craftsman.domain.metrics.devquality.BugMetric;
import com.alibaba.craftsman.domain.metrics.devquality.DevQualityMetric;
import com.alibaba.craftsman.domain.metrics.techcontribution.ContributionMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.InfluenceMetric;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.RefreshScoreCmd;
import com.alibaba.craftsman.event.handler.MetricItemCreatedHandler;
import com.alibaba.craftsman.repository.MetricRepository;
import com.alibaba.craftsman.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Command
public class RefreshScoreCmdExe implements CommandExecutorI<Response, RefreshScoreCmd> {
    private Logger logger = LoggerFactory.getLogger(MetricItemCreatedHandler.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Response execute(RefreshScoreCmd cmd) {
        UserProfile userProfile = getUserProfile(cmd);
        calculateScore(userProfile);
        update(userProfile);
        return Response.buildSuccess();
    }

    private UserProfile getUserProfile(RefreshScoreCmd cmd) {
        UserProfile userProfile = userProfileRepository.getByUserId(cmd.getUserId());
        Assert.notNull(userProfile, "There is no User Profile for "+cmd.getUserId()+" to update");
        return userProfile;
    }

    private void calculateScore(UserProfile userProfile) {
        loadInfluenceMetric(userProfile);
        loadContributionMetrics(userProfile);
        loadDevQualityMetrics(userProfile);
        loadAppQualityMetrics(userProfile);
        userProfile.calculateScore();
    }

    private void loadAppQualityMetrics(UserProfile userProfile) {
        AppQualityMetric appQualityMetric = new AppQualityMetric(userProfile);
        AppMetric appMetric = metricRepository.getAppMetric(userProfile.getUserId());
        appMetric.setParent(appQualityMetric);
    }

    private void loadDevQualityMetrics(UserProfile userProfile) {
        DevQualityMetric devQualityMetric = new DevQualityMetric(userProfile);
        BugMetric bugMetric = metricRepository.getBugMetric(userProfile.getUserId());
        bugMetric.setParent(devQualityMetric);
    }

    private void loadContributionMetrics(UserProfile userProfile) {
        ContributionMetric contributionMetric = new ContributionMetric(userProfile);
        List<SubMetric> subMetricList = metricRepository.listByTechContribution(userProfile.getUserId());
        subMetricList.forEach(subMetric -> subMetric.setParent(contributionMetric));
    }

    private void loadInfluenceMetric(UserProfile userProfile) {
        InfluenceMetric influenceMetric = new InfluenceMetric(userProfile);
        List<SubMetric> subMetricList = metricRepository.listByTechInfluence(userProfile.getUserId());
        subMetricList.forEach(subMetric -> subMetric.setParent(influenceMetric));
    }

    private void update(UserProfile userProfile) {
        userProfileRepository.update(userProfile);
    }
}
