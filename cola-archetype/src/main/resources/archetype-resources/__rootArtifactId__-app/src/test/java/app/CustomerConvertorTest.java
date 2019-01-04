#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.app;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.extension.ExtensionExecutor;
import ${package}.common.BizCode;
import ${package}.convertor.extensionpoint.CustomerConvertorExtPt;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.domain.customer.valueobject.SourceType;
import ${package}.dto.clientobject.CustomerCO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CustomerConvertorTest {

    @Autowired
    private ExtensionExecutor extensionExecutor;

    private CustomerCO customerCO;

    @Before
    public void setup(){
        customerCO = new CustomerCO();
        customerCO.setCompanyName("TestCompany");
    }

    @Test
    public void testBizOneConvert(){
        //1. prepare
        Context context = new Context();
        context.setBizCode(BizCode.BIZ_ONE);

        //2. execute
        CustomerE customerE = extensionExecutor.execute(CustomerConvertorExtPt.class, context, extension -> extension.clientToEntity(customerCO, context));

        //3. assert
        Assert.assertEquals(SourceType.BIZ_ONE, customerE.getSourceType());
    }

    @Test
    public void testBizTwoConvert(){
        //1. prepare
        Context context = new Context();
        context.setBizCode(BizCode.BIZ_TWO);

        //2. execute
        CustomerE customerE = extensionExecutor.execute(CustomerConvertorExtPt.class, context, extension -> extension.clientToEntity(customerCO, context));

        //3. assert
        Assert.assertEquals(SourceType.BIZ_TWO, customerE.getSourceType());
    }
}