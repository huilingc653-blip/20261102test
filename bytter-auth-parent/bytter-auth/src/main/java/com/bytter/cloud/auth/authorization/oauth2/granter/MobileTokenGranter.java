package com.bytter.cloud.auth.authorization.oauth2.granter;
import com.bytter.cloud.auth.cache.AuthCacheKey;
import com.bytter.cloud.auth.cache.LoginTokenCache;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.event.BytterLoginException;
import com.bytter.core.utils.StringUtil;
import com.bytter.cloud.common.core.crypto.SM2Util;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 短信验证码登陆与用户名密码登陆相似,密码为动态
 * 故继承ResourceOwnerPasswordTokenGranter
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public class MobileTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "mobile";
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    @Setter
    @Getter
    private LoginTokenCache loginTokenCache;
    @Getter
    @Setter
    private RedisTemplate redisTemplate;
    public MobileTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices, UserDetailsService userDetailsService,
                              ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }
    /**
     * sso 鉴权
     *
     * @param client       客户端
     * @param tokenRequest 请求
     * @return 授权信息
     */
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        final String pubKey = parameters.get("pubKey");
        final String priKey = loginTokenCache.get(pubKey);
        String password = parameters.get("password");
        if (StringUtil.isBlank(priKey)) {
            throw new BytterLoginException(ErrorConstant.AUTH_ERROR_TOKEN_INVALID);
        }
        try {
            password = decodePassword(password, priKey);
        } catch (InvalidCipherTextException e) {
            logger.error("decode password error!", e);
        }
        if (StringUtil.isBlank(mobile)) {
            throw new BytterLoginException("An authorization mobile must be supplied.");
        }
        String code = parameters.get("code");
        if (StringUtil.isBlank(code)) {
            throw new BytterLoginException("请输入短信验证码");
        }
        //入参code 和redis中的code对比
        String tmpCode = (String) redisTemplate.opsForValue().get(AuthCacheKey.SMS_CODE_CACHE_PREFIX + mobile);
        if (StringUtil.isBlank(tmpCode)) {
            throw new BytterLoginException("手机验证码已失效，请重新获取");
        }
        if (!code.equalsIgnoreCase(tmpCode)) {
            throw new BytterLoginException("手机验证码错误");
        }
        String tenant = "";
        String username = tenant + "@BT@" + mobile + "@BT@3";
        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = authenticationManager.authenticate(userAuth);
        OAuth2Request storedOuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        ///redisTemplate.delete("system:captcha:code:" + mobile);
        return new OAuth2Authentication(storedOuth2Request, userAuth);
    }
    /**
     * 使用国密解析密码
     *
     * @param encodePassword 前端使用公钥加密的密码
     * @param privateKey     从redis中通过公钥获取到的私钥
     * @return 原密码
     * @throws InvalidCipherTextException InvalidCipherTextException
     */
    public String decodePassword(String encodePassword, String privateKey) throws InvalidCipherTextException {
        ECPrivateKeyParameters priKeyParam = new ECPrivateKeyParameters(
                new BigInteger(ByteUtils.fromHexString(privateKey)), SM2Util.DOMAIN_PARAMS);
        byte[] decrypt = SM2Util.decrypt(priKeyParam, ByteUtils.fromHexString(encodePassword));
        return new String(decrypt, StandardCharsets.UTF_8);
    }
}
