package com.bytter.cloud.gateway.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bytter.auth.api.ApiIAuthenticationService;
import com.bytter.cloud.gateway.auth.service.IAuthService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * 授权服务实现类
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Slf4j
@Service
@RefreshScope
public class AuthServiceImpl implements IAuthService {
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Authorization ";
    
    @DubboReference
    private ApiIAuthenticationService authClient;
    

    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * 不需要网关签权的url配置(/oauth,/open)
     * 默认/oauth开头是不需要的
     */
    @Value("${gate.ignore.authentication.startwith}")
    private String ignoreUrls;

    @Override
    public boolean ignoreAuthentication(String url) {
        return Stream.of(this.ignoreUrls.split(",")).anyMatch(ignoreUrl -> url.startsWith(ignoreUrl.trim()));
    }

    @Override
    public boolean hasPermission(String uid, String url, String method) {
        // 从认证服务获取是否有权限,远程调用 todo 增加缓存
        return authClient.hasPermission(uid, url, method);
    }

    @Override
    public boolean hasPermissionV1(String uid, String url, String method,String tenantId) {
        // 从认证服务获取是否有权限,远程调用 todo 增加缓存
        return authClient.hasPermissionV1(uid, url, method,tenantId);
    }

    @Override
    public boolean checkToken(String tokenValue) {
        return authClient.checkToken(tokenValue);
    }

    @Override
    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        if (jwtToken.startsWith("Authorization%20")) {
            jwtToken = StringUtils.substring(jwtToken, "Authorization%20".length());
        }
        if (StrUtil.startWithIgnoreCase(jwtToken,"bearer ")) {
            jwtToken = StringUtils.substring(jwtToken, "bearer ".length());
        }

        // 得到DefaultJwtParser
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwtToken);
    }


    public Jws<Claims> getJwt2(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwtToken);
    }

    @Override
    public Jws<Claims> invalidJwtAccessToken(String authentication) {
        try {
            return getJwt(authentication);
        } catch (ExpiredJwtException ex) {
            log.error("用户token已过期，user token Expired :{}", ex.getMessage());
            throw ex;
        } catch (SignatureException ex) {
            log.error("user token SignatureException :{}", ex.getMessage(), ex);
            throw ex;
        } catch (MalformedJwtException ex) {
            log.error("user token MalformedJwtException :{}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public String ipWhiteList() {
        return authClient.ipWhiteList();
    }

    @Override
    public Boolean checkIp(String remoteIp) {
        return authClient.checkIp(remoteIp);
    }

    @Override
    public Boolean tokenExpiredNotify(String userId) {
        return authClient.tokenExpiredNotify(userId);
    }
}
