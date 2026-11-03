package com.bytter.cloud.auth.authorization.oauth2.enhancer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
/**
 * 移除token中的敏感信息
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public class RemoveAdditionalInformationEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        accessToken.getAdditionalInformation().clear();
        return accessToken;
    }
}
