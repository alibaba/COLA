#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import ${package}.domain.account.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONTest {

    @Test
    public void testJsonBind() {
        // this will throw exception since account not recognized
        String badJson = "{${symbol_escape}"account${symbol_escape}":{${symbol_escape}"name${symbol_escape}":${symbol_escape}"frank${symbol_escape}",${symbol_escape}"phoneNo${symbol_escape}":${symbol_escape}"15921582125${symbol_escape}",${symbol_escape}"remaining${symbol_escape}":${symbol_escape}"400${symbol_escape}",${symbol_escape}"chargePlanList${symbol_escape}":[{${symbol_escape}"priority${symbol_escape}":${symbol_escape}"2${symbol_escape}",${symbol_escape}"type${symbol_escape}":${symbol_escape}"fixedTime${symbol_escape}"},{${symbol_escape}"priority${symbol_escape}":${symbol_escape}"1${symbol_escape}",${symbol_escape}"type${symbol_escape}":${symbol_escape}"familyMember${symbol_escape}"}]}}";
        // this is good
        String goodJson = "{${symbol_escape}"name${symbol_escape}":${symbol_escape}"frank${symbol_escape}",${symbol_escape}"phoneNo${symbol_escape}":${symbol_escape}"15921582125${symbol_escape}",${symbol_escape}"remaining${symbol_escape}":${symbol_escape}"400${symbol_escape}",${symbol_escape}"chargePlanList${symbol_escape}":[{${symbol_escape}"priority${symbol_escape}":${symbol_escape}"2${symbol_escape}",${symbol_escape}"type${symbol_escape}":${symbol_escape}"fixedTime${symbol_escape}"},{${symbol_escape}"priority${symbol_escape}":${symbol_escape}"1${symbol_escape}",${symbol_escape}"type${symbol_escape}":${symbol_escape}"familyMember${symbol_escape}"}]}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(goodJson, Account.class);
            Assertions.assertEquals(account.getPhoneNo(), 15921582125L);
            System.out.println(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
