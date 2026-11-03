package com.bytter.cloud.auth.config;

import com.bytter.cloud.auth.authorization.oauth2.provider.token.TenantAuthenticationKeyGenerator;
import com.bytter.cloud.auth.authorization.oauth2.provider.token.TenantRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
public class TokenConfig {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * jwt 对称加密密钥
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * oauth2 token 缓存是否使用redis
     */
    @Value("${spring.security.oauth2.token.redis-enable:false}")
    private boolean redisEnable;

    /**
     * oauth2存储值统一前缀
     */
    private static final String PRE_FIX_NAME = "auth:";

    /**
     * token的持久化
     *
     * @return tokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        if (redisEnable) {
            // 多租户RedisTokenStore
            TenantRedisTokenStore redisTokenStore = new TenantRedisTokenStore(redisConnectionFactory);
            redisTokenStore.setPrefix(PRE_FIX_NAME);
            redisTokenStore.setAuthenticationKeyGenerator(new TenantAuthenticationKeyGenerator());
            return redisTokenStore;
        } else {
            JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
            tokenStore.setAuthenticationKeyGenerator(new TenantAuthenticationKeyGenerator());
            return tokenStore;
        }
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }
}
