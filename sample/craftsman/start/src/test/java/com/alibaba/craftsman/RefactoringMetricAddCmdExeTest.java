package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.RefactoringMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.RefactoringMetricCO;
import com.alibaba.craftsman.mock.MockTestBase;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RefactoringMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-04 1:59 PM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class RefactoringMetricAddCmdExeTest extends MockTestBase {

    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testSuccess(){
        RefactoringMetricAddCmd refactoringMetricAddCmd = new RefactoringMetricAddCmd();
        RefactoringMetricCO refactoringMetricCO = new RefactoringMetricCO();
        refactoringMetricCO.setOwnerId("RefactoringMetricAddCmdExeTest_09098");
        refactoringMetricCO.setName("Refactor Leads code");
        refactoringMetricCO.setRefactoringLevel(RefactoringMetricCO.MODULE_LEVEL);
        refactoringMetricCO.setContent("Refactor Content");
        refactoringMetricCO.setDocUrl("docUrl");
        refactoringMetricCO.setCodeUrl("codeUrl");
        refactoringMetricAddCmd.setRefactoringMetricCO(refactoringMetricCO);
        refactoringMetricAddCmd.setOperater("Lucy");

        Response response = metricsService.addRefactoringMetric(refactoringMetricAddCmd);

        Assert.assertTrue(response.isSuccess());
    }
}

