package com.bytter.third.service;

import com.bytter.third.enums.ThirdType;

import java.util.HashMap;
import java.util.Map;

public abstract class ThirdAuthClient {

    protected static final Integer TOKEN_OFFSET = 50;



    public abstract ThirdProperties getThirdProperties();

    public abstract ThirdType getType();

    /**
     * 获取应用token
     * @return token
     */
    public abstract String getAppAccessToken();

    // 根据授权码获取用户信息
    public abstract String getUserInfo(String code);

    String formatURI(String uri) {
        ThirdType type = getType();
        String host = "";
        switch (type) {
            case DD:
                break;
            case FS:
                break;
            case CP_WX:
//                host=getThirdProperties().getCorpWx().getHost();
                host="https://qyapi.weixin.qq.com";
        }
        return String.format("%s%s", host, uri);
    }

    Map<String, Object> defaultAuthorization(String token) {
        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", String.format("Bearer %s", token));
        return header;
    }

}
