package com.bytter.cloud.auth.authorization.oauth2.granter;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.cloud.auth.cache.LoginLockCache;
import com.bytter.cloud.auth.cache.LoginTokenCache;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.event.BytterLoginException;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.CAUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.starter.crypto.cfca.util.CfcaUtil;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 证书登录令牌授予者
 *
 * @author huangw 2022-1-12
 * @since 1.0.0
 */
public class CertificateTokenGranter extends ClientCredentialsTokenGranter {
    private static final String X_CLIENT_BYTTER_TENANT = "x-client-bytter-tenant";
    private final UserDetailsService userDetailsService;
    private static final String GRANT_TYPE = "certificate";
    private final AuthenticationManager authenticationManager;
    @Setter
	@Getter
    private RedisTemplate redisTemplate;
    
    @Setter
    @Getter
    private SM2ManagerService sm2ManagerService;
    
    @Setter
    @Getter
    private IConfigService configService;
    
    @Setter
    @Getter
    private IUserService userService;//是否set 注入
    
    public CertificateTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                   UserDetailsService userDetailsService, ClientDetailsService clientDetailsService,
                                   OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
    	String password = "";
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        final String pubKey = parameters.get("publickey");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tenant = "000";
        String username = parameters.get("username");
        String userCode = "";
        AuthUser userDetails = null;
        boolean compareFromP7SignData = true;
        try {
        	if(parameters.get("password")!=null && parameters.get("loginType")!=null && parameters.get("loginType").equals("1")) {//证书密码，把密码查出来
        		password=sm2ManagerService.decodeSm2ByKey(parameters.get("password"), parameters.get("publickey"));
        	}
        } catch (Exception e) {
            logger.error("decode password error!", e);
            throw new BytterLoginException(ErrorConstant.AUTH_ERROR_PWD_INVALID);
        }
        
        try {
            final String priKey = RedisCacheKey.makeRedisKey(RedisCacheKey.SM2_KEY, pubKey);
            final String userCadn = parameters.get("userCadn");
            final String signature = parameters.get("signature");
            if (StringUtils.isEmpty(userCadn) || StringUtils.isBlank(signature)) {
                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_TOKEN_INVALID);
            }
            if(!StringUtils.isEmpty(userCadn)) {
            	userCode = userService.getUserCodeByCadn(userCadn);
            }
            if(StringUtils.isEmpty(userCode)) {
            	compareFromP7SignData = false;
            }
            compareFromP7SignData = verify(userCadn,userCode,userCadn,signature);//true;//CfcaUtil.compareFromP7SignData(signature, userCadn); 
            // 验签失败
            if (!compareFromP7SignData) {
                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_TOKEN_INVALID);
            }
            userDetails = (AuthUser) userDetailsService.loadUserByUsername(username);
            // 登录成功，移除失败次数缓存
            redisTemplate.delete("fail_count" + userDetails.getCurrentUserId());
        } catch (AccountStatusException | BadCredentialsException ase) {
            logger.error("oauth password error", ase);
            if (ase instanceof AccountStatusException) {
                throw new InvalidGrantException(ase.getMessage());
            }
            UserDTO user = userService.getUserByCode(parameters.get("username"), tenant).getData();
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
                throw new InvalidGrantException(ase.getMessage());
            }
        }
        
        Authentication userAuth = null;
        if(parameters.get("password")!=null && parameters.get("loginType")!=null && parameters.get("loginType").equals("1")) 
        {//证书密码   登录中的密码
            userAuth = new UsernamePasswordAuthenticationToken(username, password);
            ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
            try {
                // 获取用户登入方式 0用户密码，1银行密码+证书，2证书 todo 证书登陆暂无
                userAuth = authenticationManager.authenticate(userAuth);
            } catch (AccountStatusException | BadCredentialsException ase) {
                throw new BytterLoginException("密码错误！！");
            }
            AssertHelper.isTrueLoginEx(userAuth != null && userAuth.isAuthenticated(), "认证失败");
            OAuth2Request oauthRequest = getRequestFactory().createOAuth2Request(client, tokenRequest);
            parameters.remove("password");
            return new OAuth2Authentication(oauthRequest, userAuth);
        }else {//证书登录
            userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
          if (userAuth == null || !userAuth.isAuthenticated()) {
              throw new InvalidGrantException("Could not authenticate user: " + username);
          }
          OAuth2Request oauthRequest = getRequestFactory().createOAuth2Request(client, tokenRequest);
          return new OAuth2Authentication(oauthRequest, userAuth);
        }
        
       

    }
    
    
    public boolean verify(String userCadn,String userCode,String sourceData, String signData) {
        //加密方式
    	ConfigDTO encrypt = configService.getGlobalConfig("sys", "encrypt");
        //是否启用信安证书后端验证
    	ConfigDTO xasjCert = configService.getGlobalConfig("sys", "xasjCert");
        if(null == encrypt){
            encrypt = new ConfigDTO("0");
        }
        boolean reslut = false;
        CAUtil ca = new CAUtil();
        try {
            if(!encrypt.getValue().equals("4")) {
                if(ObjectUtils.isEmpty(xasjCert) || StringUtils.isEmpty(xasjCert.getValue())) {
                    reslut = ca.verify(encrypt.getValue(), sourceData, signData);
                }else{
                	//与客户对接实现
                    reslut = true;//xasjCertService.check(sourceData,signData);
                }
            }else{//山地证书验签
                reslut = true;//sdCertService.check(userCadn,sourceData,signData,userCode);
            	//客户自己实现
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BytterLoginException(ErrorConstant.AUTH_ERROR_CERT_INVALID);
        }
        return  reslut;
    }
    
    

    
//    public static void main(String[] args) {
//    	String data = CfcaUtil.getContentFromP7SignData("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");
//    	System.out.println("data="+data);
//    	boolean result = CfcaUtil.compareFromP7SignData("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");
//    	System.out.println("result="+result);
//	}
}
