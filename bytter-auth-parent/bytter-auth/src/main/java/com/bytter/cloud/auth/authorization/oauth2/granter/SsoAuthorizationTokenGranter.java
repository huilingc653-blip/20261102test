package com.bytter.cloud.auth.authorization.oauth2.granter;
import com.bytter.cloud.auth.authorization.oauth2.sso.SsoAuthAdapter;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 单点登录授权方式登录
 *
 * @author liuwf 2021-08-11
 * @since 1.0.0
 */
public class SsoAuthorizationTokenGranter extends AbstractTokenGranter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SsoAuthorizationTokenGranter.class);
    private static final String X_CLIENT_BYTTER_TENANT = "x-client-bytter-tenant";
    private static final String GRANT_TYPE = "sso_token";
    private final UserDetailsService userDetailsService;
    private final SsoAuthAdapter ssoService;
    public SsoAuthorizationTokenGranter(AuthorizationServerTokenServices tokenServices, UserDetailsService userDetailsService,
                                        ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
                                        SsoAuthAdapter ssoService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.ssoService = ssoService;
    }
    /**
     * sso 鉴权
     * @param client 客户端
     * @param tokenRequest 请求
     * @return 授权信息
     */
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String token = parameters.get("token");
        if (token == null) {
            throw new InvalidRequestException("An authorization token must be supplied.");
        }
        String tenant = parameters.get("tenant");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (StringUtils.isEmpty(tenant)) {
            tenant = request.getHeader(X_CLIENT_BYTTER_TENANT);
        }
        //获取浏览器访问访问服务器时传递过来的cookie数组
        Cookie[] cookies = request.getCookies();
        UserDetails user = null;
        try {
            SsoUser ssoUser = ssoService.adapterSsoService(tenant).parseSsoAccount(token, cookies);
            String username = tenant + "@BT@" + ssoUser.getUserName() + "@BT@";
            user = userDetailsService.loadUserByUsername(username);
        } catch (InvalidGrantException ex) {
            throw ex;
        } catch (Exception e) {
            LOGGER.error("sso Authorization", e);
            throw new InvalidGrantException("用户不存在");
        }
        Authentication userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        OAuth2Request storedOuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOuth2Request, userAuth);
    }
}
