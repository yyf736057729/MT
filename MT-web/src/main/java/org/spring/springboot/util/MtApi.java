package org.spring.springboot.util;

import net.sf.json.JSONObject;
import org.spring.springboot.domain.Mt;

public class MtApi {
    public static Mt mtNumber(String number) {
        String token = "masgyz3";
        AES aes = new AES();
        String sign = token + "&" + number;
        sign = aes.encrypt(sign, "4523567890194556");
        number = aes.encrypt(number, "4523567890194556");
        token = aes.encrypt(token, "4523567890194556");
        String param = "number=" + number + "&token=" + token + "&sign=" + sign;
        String s = HttpRequest.sendGet("http://47.93.191.113/meituanApi/request", param);
        System.out.println(s);
        JSONObject jsonObject = JSONObject.fromObject(s);
        Mt mt = (Mt) JSONObject.toBean(jsonObject, Mt.class);
        return mt;
    }
}
