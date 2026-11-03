package com.bytter.cloud.auth.authorization.oauth2.provider.token;

import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 租客唯一条件生成器
 *
 * @author zhoul 2021-3-3
 * @since 1.0.0
 */
public class TenantAuthenticationKeyGenerator implements AuthenticationKeyGenerator {
    private static final String CLIENT_ID = "client_id";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    private static final String TENANT_ID = "tenantId";

    public static void main(String[] args) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(CLIENT_ID,"mobile");
        values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<>(Collections.singleton("read"))));
        String generateKey = generateKey(values);
        System.out.println("generateKey = " + generateKey);
    }

    /**
     * 通过{@link OAuth2Authentication}对象解析生成key
     *
     * @param authentication OAuth2Authentication
     * @return key
     */
    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<>();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        Object principal = authentication.getUserAuthentication().getPrincipal();
        // 获取 principal 对象里的 tenantId
        if (principal instanceof AuthUser) {
            AuthUser authUser = (AuthUser) principal;
            values.put(TENANT_ID, authUser.getTenantId());
        }
        values.put(CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<>(authorizationRequest.getScope())));
        }
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication.getPrincipal() != null) {
            //values.put("sub", JsonUtils.toJSONString(userAuthentication.getPrincipal()));
        }
        return generateKey(values);
    }

    public static String generateKey(Map<String, String> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        }
    }
}
