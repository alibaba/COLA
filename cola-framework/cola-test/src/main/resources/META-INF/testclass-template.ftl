package ${namespace};

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.cola.mock.ColaMockito;
<#list imports as item>
import ${item};
</#list>

/**
* @author xxxxx
* @date ${date}
*/
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={})
public class ${unitTestClass} <#if superClass !=''> extends ${superClass} </#if>{

    @Autowired
    ${testClass} ${testClassName};

    @Test
    public void ${testMethod}(){
        <#list varDefinitions as item>
        ${item};
        </#list>
        <#if return !=''>${return} = </#if>${testClassName}.${testMethod}(<#list params as item><#if item_index gt 0>, </#if>${item}</#list>);
        Assert.assertTrue(true);
    }
}
