package com.bytter.system.service.impl;
import com.bytter.system.api.IOauthService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
@Service("oauthServiceI")
@DubboService
public class OauthServiceImpl implements IOauthService {
    @Override
    public String getAccessTokenId(String clientId, String clientSecret, String scope, String username) throws Exception {
        return null;
    }
}
