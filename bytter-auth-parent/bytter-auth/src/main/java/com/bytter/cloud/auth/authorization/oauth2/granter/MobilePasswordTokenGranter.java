package com.bytter.cloud.auth.authorization.oauth2.granter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.event.BytterLoginException;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.jdbc.TenantProperties;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.starter.crypto.AESUtil;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 密码登陆管理类
 *
 * @author zhoul 2021-1-20
 * @since 1.0.0
 */
public class MobilePasswordTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "phone";

    private final AuthenticationManager authenticationManager;

    @Setter
    @Getter
    private RedisUtils redisUtils;

    @Setter
    @Getter
    private RedisTemplate redisTemplate;

    @Setter
    @Getter
    private IUserService systemClient;// 是否set 注入

    @Setter
    @Getter
    private IUserService userService;

    @Setter
    @Getter
    private SM2ManagerService sm2ManagerService;

    @Resource
    private TenantProperties tenantProperties;

    public MobilePasswordTokenGranter(AuthenticationManager authenticationManager,
                                      AuthorizationServerTokenServices tokenServices,
                                      ClientDetailsService clientDetailsService,
                                      OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected MobilePasswordTokenGranter(AuthenticationManager authenticationManager,
                                         AuthorizationServerTokenServices tokenServices,
                                         ClientDetailsService clientDetailsService,
                                         OAuth2RequestFactory requestFactory,
                                         String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    /**
     * final String pubKey = parameters.get("pubKey");
     * final String priKey = loginTokenCache.get(pubKey);
     * try {password = decodePassword(password, priKey);} catch (InvalidCipherTextException e) {logger.error("decode password error!", e);}
     *
     * @param client       客户端
     * @param tokenRequest 请求
     * @return 授权信息
     */
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new HashMap<>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        AssertHelper.isTrueLoginEx(StringUtils.isNoneBlank(username), "用户名不能为空！！");
        String publickey = parameters.get("key");
        String password = parameters.get("password");

        String tenantId = parameters.get("tenantId");
        //开启了单租户模式
        if(StrUtil.isBlank(tenantId) && !tenantProperties.getEnabled()){
            tenantId = tenantProperties.getDefaultId();
        }
        AssertHelper.isTrueLoginEx(StringUtils.isNoneBlank(tenantId), "租户ID不能为空！！");
        // 将租户信息编码到用户名中
        String tenantAwareUsername = encodeUsernameWithTenant(username, tenantId);
        // 更新参数中的用户名
        parameters.put("username", tenantAwareUsername);

        try {
            // 人脸识别登陆
            if (ObjectUtil.equal(password, "faceLogin")) {
                logger.info("人脸识别登陆。。。。。。");
                UserDTO userDTO = userService.getByLoginName(username);
                if (userDTO != null) {
                    password = userDTO.getPassword();
                }else {
                    logger.error("password is null !");
                    throw new BytterLoginException(ErrorConstant.AUTH_ERROR_PWD_INVALID);
                }
            }
            if (ObjectUtils.isNotEmpty(publickey)) {
                // 获取加密密文
                String entryKey = RedisCacheKey.makeRedisKey(RedisCacheKey.SM2_KEY, publickey);
                Map<String, String> entryMap = Convert.toMap(String.class, String.class, redisUtils.get(entryKey));
                String key = entryMap.get("key");
                String iv = entryMap.get("iv");
                password = AESUtil.aesCbcDecrypt(password, key, iv, "UTF-8");
            } else {
                password = parameters.get("password");
            }
        } catch (Exception e) {
            logger.error("decode password error!", e);
            throw new BytterLoginException(ErrorConstant.AUTH_ERROR_PWD_INVALID);
        }
        // 移除敏感信息
        parameters.remove("password");
        Authentication userAuth = new UsernamePasswordAuthenticationToken(tenantAwareUsername, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            // 获取用户登入方式 0用户密码，1银行密码+证书，2证书 todo 证书登陆暂无
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            throw new BytterLoginException("密码错误！！");
        }
        AssertHelper.isTrueLoginEx(userAuth != null && userAuth.isAuthenticated(), "认证失败");
        OAuth2Request oauthRequest = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(oauthRequest, userAuth);
    }

    /**
     * 将租户信息编码到用户名中（username@BT@tenantId）。
     *
     * @param username 用户名。
     * @param tenantId 租户ID。
     * @return 编码后的用户名。
     */
    private String encodeUsernameWithTenant(String username, String tenantId) {
        // 使用分隔符
        return String.format("%s@BT@%s", username, tenantId);
    }

}
