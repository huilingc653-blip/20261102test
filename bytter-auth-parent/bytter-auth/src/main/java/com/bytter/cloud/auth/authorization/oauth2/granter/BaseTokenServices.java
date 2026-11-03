package com.bytter.cloud.auth.authorization.oauth2.granter;
import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bytter.cloud.auth.constants.TenantConstant;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 重写oauth2实现类
 *
 * @author liuwf 2022-01-06
 * @since 1.0.0
 */
@Slf4j
public class BaseTokenServices extends DefaultTokenServices {
//    private static final String X_CLIENT_BYTTER_TENANT = "x-client-bytter-tenant";
    private TokenStore tokenStore;
    private TokenEnhancer accessTokenEnhancer;
    @Getter
    @Setter
    private RedisTemplate redisTemplate;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        StopWatch stopWatch = new StopWatch("createAccessToken耗时");
        stopWatch.start("all");
        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication); // 这里面需要将租户ID存到 store 中
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 集团号TODO
//        String tenantNumber = request.getHeader(X_CLIENT_BYTTER_TENANT); //TODO here
        // 一个账户一个用户登陆标志
        boolean isOne = false;
        /*try {
            String isOneString = (String) redisTemplate.opsForValue().get("manager:configs:" + tenantNumber);
            if (!StringUtils.isEmpty(isOneString) && TenantConstant.KICK_OUT.equals(isOneString)) {
                isOne = true;
            }
        } catch (Exception e) {
            log.warn("租户号：" + tenantNumber + "获取配置缓存失败！");
        }*/
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            } else {
                if (isOne) {
                    tokenStore.removeAccessToken(existingAccessToken);
                } else {
                    // Re-store the access token in case the authentication has changed
                    tokenStore.storeAccessToken(existingAccessToken, authentication);
                    return existingAccessToken;
                }
            }
        }
        // Only create a new refresh token if there wasn't an existing one
        // associated with an expired access token.
        // Clients might be holding existing refresh tokens, so we re-use it in
        // the case that the old access token
        // expired.
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        } else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }
        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        // In case it was modified
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        stopWatch.stop();
        // 清除登陆锁屏状态
        stopWatch.start("清除登陆锁屏状态");
        assert refreshToken != null;
        JWT jwt = JWTUtil.parseToken(refreshToken.getValue());
        JSONObject payloads = jwt.getPayloads();
        String uid = payloads.getStr("uid");
        String hashKey = RedisCacheKey.makeRedisKey("", RedisCacheKey.LOCK_SCREEN);
        boolean isFlag = redisTemplate.opsForHash().delete(hashKey, uid) > 0;
        log.error("清除用户【{}】的锁屏信息【{}】", uid, isFlag);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return accessToken;
    }
    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());
        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }
    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }
    /**
     * The persistence strategy for token storage.
     *
     * @param tokenStore the store for access and refresh tokens.
     */
    public void setTokenStore(TokenStore tokenStore) {
        super.setTokenStore(tokenStore);
        this.tokenStore = tokenStore;
    }
    /**
     * An access token enhancer that will be applied to a new token before it is saved in the token store.
     *
     * @param accessTokenEnhancer the access token enhancer to set
     */
    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
    }
}
