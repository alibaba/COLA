package com.huawei.charging.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.charging.domain.account.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONTest {

    @Test
    public void testJsonBind() {
        // this will throw exception since account not recognized
        String badJson = "{\"account\":{\"name\":\"frank\",\"phoneNo\":\"15921582125\",\"remaining\":\"400\",\"chargePlanList\":[{\"priority\":\"2\",\"type\":\"fixedTime\"},{\"priority\":\"1\",\"type\":\"familyMember\"}]}}";
        // this is good
        String goodJson = "{\"name\":\"frank\",\"phoneNo\":\"15921582125\",\"remaining\":\"400\",\"chargePlanList\":[{\"priority\":\"2\",\"type\":\"fixedTime\"},{\"priority\":\"1\",\"type\":\"familyMember\"}]}";

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
