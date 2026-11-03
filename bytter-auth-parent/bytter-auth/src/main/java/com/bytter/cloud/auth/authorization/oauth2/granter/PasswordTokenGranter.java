package com.bytter.cloud.auth.authorization.oauth2.granter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.event.BytterLoginException;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.dto.TenantPropDTO;
import com.bytter.system.api.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 密码登陆管理类
 *
 * @author zhoul 2021-1-20
 * @since 1.0.0
 */
public class PasswordTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "password";

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
    private IConfigService configService;

    @Setter
    @Getter
    private SM2ManagerService sm2ManagerService;

    public PasswordTokenGranter(AuthenticationManager authenticationManager,
                                AuthorizationServerTokenServices tokenServices,
                                ClientDetailsService clientDetailsService,
                                OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected PasswordTokenGranter(AuthenticationManager authenticationManager,
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
        String tenantId = parameters.get("tenantId");
        //开启了单租户模式
        TenantPropDTO tenantProperties = userService.getTenantProperties();
        if(!tenantProperties.getEnabled()){
            tenantId = tenantProperties.getDefaultId();
        }
        AssertHelper.isTrueLoginEx(StringUtils.isNoneBlank(username), "用户名不能为空！！");
        AssertHelper.isTrueLoginEx(StringUtils.isNoneBlank(tenantId), "租户ID不能为空！！");
        // 将租户信息编码到用户名中
        String tenantAwareUsername = encodeUsernameWithTenant(username, tenantId);
        // 更新参数中的用户名
        parameters.put("username", tenantAwareUsername);
        String password;
        try {
            if (ObjectUtils.isNotEmpty(parameters.get("bankInteface")) && parameters.get("bankInteface").equals("1")) {// bankInteface表示银企接口登录, 秘密不通过页面加密，直接明文
                password = parameters.get("password");
            } else {
                password = sm2ManagerService.decodeSm2ByKey(parameters.get("password"), parameters.get("publickey"));
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
            //判断是否锁足
            UserDTO user = userService.getUserByCode(username, tenantId).getData();
            if (user != null) {
                String limit = configService.getGlobalConfig("sys", "passwordLimit").getValue();
                int limitCount = Integer.parseInt(limit);
                String key = "fail_count:" + user.getId();
                Integer failCount = (Integer)redisTemplate.opsForValue().get(key);
                if (failCount != null && failCount >= limitCount) {
                    throw new BytterLoginException("密码错误次数过多，账号已锁定10分钟！");
                }

            }


            // 获取用户登入方式 0用户密码，1银行密码+证书，2证书 todo 证书登陆暂无
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            UserDTO user = userService.getUserByCode(username, tenantId).getData();
            if (user != null) {
                String limit = configService.getGlobalConfig("sys", "passwordLimit").getValue();
                int limitCount = Integer.parseInt(limit);
                String key = "fail_count:" + user.getId();
                Integer failCount = (Integer)redisTemplate.opsForValue().get(key);
                if (failCount == null) {
                    // 第一次失败：设置次数=1，有效期 10 分钟
                    redisTemplate.opsForValue().set(key, 1, 10, TimeUnit.MINUTES);
                    failCount = 1;
                } else {
                    // 自增 + 重新刷新过期时间
                    redisTemplate.opsForValue().increment(key);
                    redisTemplate.expire(key, 10, TimeUnit.MINUTES);
                    failCount++; // 自增后更新计数
                }
                if (failCount >= limitCount) {
                    redisTemplate.opsForValue().set(key, failCount, 10, TimeUnit.MINUTES);
                    throw new BytterLoginException("密码错误次数过多，账号已锁定10分钟！");
                }
            }
            throw new BytterLoginException("用户名或密码错误!");
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
