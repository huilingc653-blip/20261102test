package com.bytter.cloud.auth.authorization.oauth2.provider.token;

import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import lombok.Setter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.*;

/**
 * 多租户RedisTokenStore - 组合模式实现
 * 只重写需要多租户支持的方法，其他方法委托给内部的RedisTokenStore
 *
 * @author zhaohc
 * @since 2026/1/14 17:12
 */
public class TenantRedisTokenStore implements TokenStore {

    // 委托的RedisTokenStore实例
    private final RedisTokenStore delegate;
    private final RedisConnectionFactory connectionFactory;

    // 租户相关的key前缀
    private static final String UNAME_TO_ACCESS = "auth:uname_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "auth:client_id_to_access:";
    private static final String TOKEN_TO_TENANT = "auth:token_to_tenant:";

    // 租户认证Key生成器
    @Setter
    private AuthenticationKeyGenerator authenticationKeyGenerator;

    public TenantRedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.delegate = new RedisTokenStore(connectionFactory);
        this.authenticationKeyGenerator = new TenantAuthenticationKeyGenerator();
        delegate.setAuthenticationKeyGenerator(authenticationKeyGenerator);
    }

    public void setPrefix(String prefix) {
        // 同时设置delegate的prefix和本地的prefix处理
        delegate.setPrefix(prefix);
    }

    // ============= 需要多租户支持的方法 =============

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        // 1. 先委托给delegate存储基本的token信息
        delegate.storeAccessToken(token, authentication);

        // 2. 额外存储租户信息
        String tenantId = extractTenantId(authentication);
        if (tenantId != null && !tenantId.isEmpty()) {
            storeTenantInfo(token, authentication, tenantId);
        }
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        // 1. 先删除租户相关信息
        removeTenantInfo(accessToken);

        // 2. 再委托给delegate删除基本token信息
        delegate.removeAccessToken(accessToken);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        // 这个方法在多租户环境下意义不明确，建议使用带租户版本的方法
        // 返回空集合，避免误导调用者
        return delegate.findTokensByClientIdAndUserName(clientId, userName);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        // 这个方法在多租户环境下意义不明确，建议使用带租户版本的方法
        // 返回空集合，避免误导调用者
        return delegate.findTokensByClientId(clientId);
    }

    // ============= 新增的租户相关方法 =============

    /**
     * 根据客户端ID和租户ID查找令牌
     */
    public Collection<OAuth2AccessToken> findTokensByClientIdAndTenant(String clientId, String tenantId) {
        String key = CLIENT_ID_TO_ACCESS + clientId + ":" + tenantId;
        return getTokensFromKey(key);
    }

    /**
     * 根据客户端ID、用户名和租户ID查找令牌
     */
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserNameAndTenant(String clientId, String userName, String tenantId) {
        String key = UNAME_TO_ACCESS + clientId + ":" + userName + ":" + tenantId;
        return getTokensFromKey(key);
    }

    /**
     * 根据租户ID清除所有令牌
     */
    public void removeTokensByTenant(String tenantId) {
        try (RedisConnection conn = connectionFactory.getConnection()) {
            conn.openPipeline();

            // 1. 查找所有属于该租户的客户端key
            String clientPattern = CLIENT_ID_TO_ACCESS + "*:" + tenantId;
            Set<byte[]> clientKeys = conn.keys(clientPattern.getBytes());

            if (clientKeys != null) {
                for (byte[] clientKeyBytes : clientKeys) {
                    // 2. 获取该客户端下的所有令牌
                    Set<byte[]> tokenValues = conn.sMembers(clientKeyBytes);

                    if (tokenValues != null) {
                        for (byte[] tokenValueBytes : tokenValues) {
                            String tokenValue = new String(tokenValueBytes);
                            OAuth2AccessToken token = delegate.readAccessToken(tokenValue);

                            // 3. 删除令牌
                            if (token != null) {
                                removeAccessToken(token);
                            }
                        }
                    }

                    // 4. 删除客户端key
                    conn.del(clientKeyBytes);
                }
            }

            // 5. 删除该租户的所有用户名key
            String unamePattern = UNAME_TO_ACCESS + "*:" + tenantId;
            Set<byte[]> unameKeys = conn.keys(unamePattern.getBytes());
            if (unameKeys != null) {
                for (byte[] unameKeyBytes : unameKeys) {
                    conn.del(unameKeyBytes);
                }
            }

            // 6. 查找并删除所有该租户的token_to_tenant映射
            String tokenToTenantPattern = TOKEN_TO_TENANT + "*";
            Set<byte[]> tokenKeys = conn.keys(tokenToTenantPattern.getBytes());
            if (tokenKeys != null) {
                for (byte[] tokenKeyBytes : tokenKeys) {
                    byte[] tenantBytes = conn.get(tokenKeyBytes);
                    if (tenantBytes != null && tenantId.equals(new String(tenantBytes))) {
                        conn.del(tokenKeyBytes);
                    }
                }
            }

            conn.closePipeline();
        }
    }

    // ============= 委托给delegate的方法 =============

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        // // 如果token是DefaultOAuth2AccessToken，可以附加租户信息
        // if (token instanceof DefaultOAuth2AccessToken) {
        //     String tenantId = getTenantIdByToken(tokenValue);
        //     if (tenantId != null) {
        //         Map<String, Object> additionalInfo = token.getAdditionalInformation();
        //         if (additionalInfo == null) {
        //             additionalInfo = new HashMap<>();
        //         }
        //         additionalInfo.put("tenantId", tenantId);
        //         ((DefaultOAuth2AccessToken) token).setAdditionalInformation(additionalInfo);
        //     }
        // }
        return delegate.readAccessToken(tokenValue);
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return delegate.readAuthentication(token);
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        return delegate.readAuthentication(token);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return delegate.readAuthenticationForRefreshToken(token);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        delegate.storeRefreshToken(refreshToken, authentication);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return delegate.readRefreshToken(tokenValue);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        delegate.removeRefreshToken(refreshToken);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        delegate.removeAccessTokenUsingRefreshToken(refreshToken);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        // 使用支持租户的认证key生成器
        String key = authenticationKeyGenerator.extractKey(authentication);
        try (RedisConnection conn = connectionFactory.getConnection()) {
            byte[] serializedKey = ("auth:auth_to_access:" + key).getBytes();
            byte[] bytes = conn.get(serializedKey);
            if (bytes != null) {
                String tokenValue = new String(bytes);
                return readAccessToken(tokenValue);
            }
        }
        return null;
    }

    // ============= 私有方法 =============

    /**
     * 存储租户相关信息
     */
    private void storeTenantInfo(OAuth2AccessToken token, OAuth2Authentication authentication, String tenantId) {
        try (RedisConnection conn = connectionFactory.getConnection()) {
            conn.openPipeline();

            String clientId = authentication.getOAuth2Request().getClientId();
            String username = authentication.getName();
            String tokenValue = token.getValue();

            // 存储客户端ID+租户ID -> 令牌集合的映射
            String clientIdToAccessKey = CLIENT_ID_TO_ACCESS + clientId + ":" + tenantId;
            conn.sAdd(clientIdToAccessKey.getBytes(), tokenValue.getBytes());

            // 存储用户名+客户端ID+租户ID -> 令牌集合的映射
            String unameToAccessKey = UNAME_TO_ACCESS + clientId + ":" + username + ":" + tenantId;
            conn.sAdd(unameToAccessKey.getBytes(), tokenValue.getBytes());

            // 存储令牌 -> 租户ID的映射
            String tokenToTenantKey = TOKEN_TO_TENANT + tokenValue;
            conn.set(tokenToTenantKey.getBytes(), tenantId.getBytes());

            conn.closePipeline();
        }
    }

    /**
     * 删除租户相关信息
     */
    private void removeTenantInfo(OAuth2AccessToken accessToken) {
        String tokenValue = accessToken.getValue();
        String tenantId = getTenantIdByToken(tokenValue);

        if (tenantId == null || tenantId.isEmpty()) {
            return;
        }

        try (RedisConnection conn = connectionFactory.getConnection()) {
            conn.openPipeline();

            // 从token_to_tenant映射中获取租户ID
            String tokenToTenantKey = TOKEN_TO_TENANT + tokenValue;
            conn.del(tokenToTenantKey.getBytes());

            // 获取认证信息以确定clientId和username
            OAuth2Authentication authentication = delegate.readAuthentication(tokenValue);
            if (authentication != null) {
                String clientId = authentication.getOAuth2Request().getClientId();

                // 从客户端集合中移除
                String clientIdToAccessKey = CLIENT_ID_TO_ACCESS + clientId + ":" + tenantId;
                conn.sRem(clientIdToAccessKey.getBytes(), tokenValue.getBytes());

                // 从用户名集合中移除
                if (!authentication.isClientOnly()) {
                    String username = authentication.getName();
                    String unameToAccessKey = UNAME_TO_ACCESS + clientId + ":" + username + ":" + tenantId;
                    conn.sRem(unameToAccessKey.getBytes(), tokenValue.getBytes());
                }
            }

            conn.closePipeline();
        }
    }

    /**
     * 从认证信息中提取租户ID
     */
    private String extractTenantId(OAuth2Authentication authentication) {
        if (authentication.getUserAuthentication() != null) {
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof AuthUser) {
                return ((AuthUser) principal).getTenantId();
            }
        }
        return null;
    }

    /**
     * 根据token获取租户ID
     */
    private String getTenantIdByToken(String tokenValue) {
        try (RedisConnection conn = connectionFactory.getConnection()) {
            String key = TOKEN_TO_TENANT + tokenValue;
            byte[] bytes = conn.get(key.getBytes());
            if (bytes != null) {
                return new String(bytes);
            }
        }
        return null;
    }

    /**
     * 从Redis key中获取令牌集合
     */
    private Collection<OAuth2AccessToken> getTokensFromKey(String key) {
        try (RedisConnection conn = connectionFactory.getConnection()) {
            Set<byte[]> tokenValues = conn.sMembers(key.getBytes());
            if (tokenValues == null || tokenValues.isEmpty()) {
                return Collections.emptyList();
            }

            List<OAuth2AccessToken> accessTokens = new ArrayList<>();
            for (byte[] tokenValueBytes : tokenValues) {
                String tokenValue = new String(tokenValueBytes);
                OAuth2AccessToken token = delegate.readAccessToken(tokenValue);
                if (token != null) {
                    // // 附加租户信息
                    // if (token instanceof DefaultOAuth2AccessToken) {
                    //     String tenantId = getTenantIdByToken(tokenValue);
                    //     if (tenantId != null) {
                    //         Map<String, Object> additionalInfo = token.getAdditionalInformation();
                    //         if (additionalInfo == null) {
                    //             additionalInfo = new HashMap<>();
                    //         }
                    //         additionalInfo.put("tenantId", tenantId);
                    //         ((DefaultOAuth2AccessToken) token).setAdditionalInformation(additionalInfo);
                    //     }
                    // }
                    accessTokens.add(token);
                }
            }
            return accessTokens;
        }
    }

    /**
     * 获取所有租户的令牌统计信息
     */
    public Map<String, Integer> getTokenStatsByTenant() {
        Map<String, Integer> stats = new HashMap<>();
        try (RedisConnection conn = connectionFactory.getConnection()) {
            // 查找所有租户的客户端key
            String pattern = CLIENT_ID_TO_ACCESS + "*:*";
            Set<byte[]> keys = conn.keys(pattern.getBytes());

            if (keys != null) {
                for (byte[] keyBytes : keys) {
                    String key = new String(keyBytes);
                    // 解析租户ID：auth:client_id_to_access:clientId:tenantId
                    String[] parts = key.split(":");
                    if (parts.length >= 4) {
                        String tenantId = parts[3];
                        Long count = conn.sCard(keyBytes);
                        if (count != null) {
                            stats.put(tenantId, stats.getOrDefault(tenantId, 0) + count.intValue());
                        }
                    }
                }
            }
        }
        return stats;
    }


}