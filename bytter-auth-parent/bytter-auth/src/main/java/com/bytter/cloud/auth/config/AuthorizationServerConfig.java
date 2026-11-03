package com.bytter.cloud.auth.config;

import com.bytter.cloud.auth.authorization.exception.CustomWebResponseExceptionTranslator;
import com.bytter.cloud.auth.authorization.oauth2.enhancer.CustomTokenEnhancer;
import com.bytter.cloud.auth.authorization.oauth2.enhancer.RemoveAdditionalInformationEnhancer;
import com.bytter.cloud.auth.authorization.oauth2.granter.*;
import com.bytter.cloud.auth.authorization.oauth2.sso.SsoAuthAdapter;
import com.bytter.core.utils.RedisUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.config.SM2ManagerService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 授权信息配置
 *
 * @author zhoul 2021-1-19
 * @since 1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
    
    @Autowired
    @Qualifier("certificateUserDetailsService")
    private UserDetailsService certificateUserDetailsService;

    @Autowired
    @Qualifier("ssoUserDetailsService")
    private UserDetailsService ssoUserDetailsService;
    
    @Value("${bytter.oauth2.isPerfToken:false}")
    private boolean isPerfToken;
    
    @Value("${bytter.oauth2.perfTokenTime:86400}")
    private int perfTokenTime;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SsoAuthAdapter ssoService;

    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @DubboReference
    private IUserService userService;
    
    @DubboReference
    private IConfigService configService;

    @Autowired
    private SM2ManagerService sm2ManagerService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        // 支持将client参数放在header或body中
        oauthServer.allowFormAuthenticationForClients();
        oauthServer.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()");
    }
    
   

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息，从数据库中读取，对应oauth_client_details表
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 配置token的数据源、自定义的tokenServices等信息,配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
        endpoints.tokenStore(tokenStore)
                .authorizationCodeServices(authorizationCodeServices())
                .approvalStore(approvalStore())
                .exceptionTranslator(customExceptionTranslator())
                .tokenEnhancer(tokenEnhancerChain())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(createDefaultTokenServices())
                //update by joe_chen add  granter
                .tokenGranter(tokenGranter(endpoints));
    }

    private DefaultTokenServices createDefaultTokenServices() {
        BaseTokenServices tokenServices = new BaseTokenServices();
        if (this.isPerfToken) {
            tokenServices.setAccessTokenValiditySeconds(this.perfTokenTime);
        }
        tokenServices.setRedisTemplate(redisTemplate);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        addUserDetailsService(tokenServices, this.userDetailsService);
        return tokenServices;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider>asList(provider)));
        }
    }

    /**
     * 自定义OAuth2异常处理
     *
     * @return CustomWebResponseExceptionTranslator
     */
    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> customExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    /**
     * 授权信息持久化实现
     *
     * @return JdbcApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式持久化授权码code
     *
     * @return JdbcAuthorizationCodeServices
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        // 授权码存储等处理方式类，使用jdbc，操作oauth_code表
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    /**
     * 自定义token
     *
     * @return tokenEnhancerChain
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        //token增强顺序为，1、自定义token值；2、生成jwt；3、清除token中的额外信息，避免信息泄露
        tokenEnhancerChain.setTokenEnhancers(
                Lists.newArrayList(new CustomTokenEnhancer(),
                        jwtAccessTokenConverter,
                        new RemoveAdditionalInformationEnhancer())
        );
        return tokenEnhancerChain;
    }

    /**
     * 配置自定义granter
     *
     * @param endpoints 授权端点配置
     * @return token鉴权器
     */
    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();

        List<TokenGranter> tokenGranters = new ArrayList<>();
        //重写框架原有的鉴权器，更改grantType=password的鉴权器
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices,
                authorizationCodeServices, clientDetails, requestFactory));

        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);

        // 证书登录
        CertificateTokenGranter certificateTokenGranter = new CertificateTokenGranter(authenticationManager,
                tokenServices, certificateUserDetailsService, clientDetails, requestFactory);
        certificateTokenGranter.setRedisTemplate(redisTemplate);
        certificateTokenGranter.setConfigService(configService);
        certificateTokenGranter.setUserService(userService);
        certificateTokenGranter.setSm2ManagerService(sm2ManagerService);
        // 支持单一证书登录
        if (authenticationManager != null) {
            tokenGranters.add(certificateTokenGranter);
        }
        // 账号密码登陆鉴权器
        PasswordTokenGranter passwordTokenGranter =
                new PasswordTokenGranter(authenticationManager,
                        tokenServices, clientDetails, requestFactory);
        passwordTokenGranter.setRedisTemplate(redisTemplate);
        passwordTokenGranter.setRedisUtils(redisUtils);
        passwordTokenGranter.setUserService(userService);
        passwordTokenGranter.setConfigService(configService);
        passwordTokenGranter.setSm2ManagerService(sm2ManagerService);
        
        if (authenticationManager != null) {
            //该鉴权器进行重写，适配多租户
            tokenGranters.add(passwordTokenGranter);
        }
        // 短信验证码登陆鉴权器
        MobileTokenGranter mobileTokenGranter = new MobileTokenGranter(authenticationManager,
                tokenServices,
                userDetailsService,
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());
        mobileTokenGranter.setRedisTemplate(redisTemplate);
        //自定义鉴权器扩展
        tokenGranters.add(mobileTokenGranter);

        // APP账号密码登陆鉴权器
        MobilePasswordTokenGranter mobilePasswordTokenGranter =
                new MobilePasswordTokenGranter(authenticationManager,
                        tokenServices, clientDetails, requestFactory);
        mobilePasswordTokenGranter.setRedisTemplate(redisTemplate);
        mobilePasswordTokenGranter.setRedisUtils(redisUtils);
        mobilePasswordTokenGranter.setUserService(userService);
        mobilePasswordTokenGranter.setSm2ManagerService(sm2ManagerService);
        tokenGranters.add(mobilePasswordTokenGranter);

        // 添加SSO单点登陆鉴权器
        SsoAuthorizationTokenGranter ssoAuthorizationTokenGranter = new SsoAuthorizationTokenGranter(tokenServices,
                ssoUserDetailsService, clientDetails, requestFactory, ssoService);
        tokenGranters.add(ssoAuthorizationTokenGranter);

        return new CompositeTokenGranter(tokenGranters);
    }
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
