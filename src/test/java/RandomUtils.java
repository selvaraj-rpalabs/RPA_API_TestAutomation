import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;

public class RandomUtils {

    private static HashMap hashMap=new HashMap();
    public static String getString(){
        return RandomStringUtils.randomAlphanumeric(8,15);
    }

    public static String getPhone(){
        return RandomStringUtils.randomNumeric(10).toString();
    }

    public static String getEmail(){
        return RandomStringUtils.randomAlphanumeric(8).toLowerCase() + "@test.com";
    }
    public static HashMap getBody(){
        hashMap.put("address",getString());
        hashMap.put("api_key",getString());
        hashMap.put("company_name",getString());
        hashMap.put("description",getString());
        hashMap.put("email",getEmail());
        hashMap.put("first_name",getString());
        hashMap.put("last_name",getString());
        hashMap.put("phone",getPhone());
        hashMap.put("referal_user",getString());
        hashMap.put("reference_number",getString());
        hashMap.put("user_api_key",getString());
        return hashMap;
    }
}
