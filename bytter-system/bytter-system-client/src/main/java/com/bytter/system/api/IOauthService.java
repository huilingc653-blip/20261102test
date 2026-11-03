package com.bytter.system.api;
/**
 * @author chenlw 2019/08/15
 */
public interface IOauthService {
    /**
     * 获取资金管理系统的AccessToken
     *
     * @param clientId     clientId
     * @param clientSecret clientSecret
     * @param scope        scope
     * @param username     username
     * @return accessToken
     * @throws Exception 不可预知异常
     */
    String getAccessTokenId(String clientId, String clientSecret, String scope, String username) throws Exception;
}
