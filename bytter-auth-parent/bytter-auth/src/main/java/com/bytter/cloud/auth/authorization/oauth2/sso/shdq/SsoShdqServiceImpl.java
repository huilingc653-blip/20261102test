package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.cloud.auth.authorization.oauth2.sso.*;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.*;
import com.bytter.cloud.common.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * 单点登录鉴权服务--上海电气实现
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@Service
public class SsoShdqServiceImpl implements SsoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SsoShdqServiceImpl.class);

    @Resource
    private SsoRestUtil ssoRestUtil;
    private SsoShdqConfig config;
    @Resource
    private SsoTenantProperties ssoTenantProperties;
    /**
     * 表示默认的租户编号
     */
    private static final String TENANT_CODE = "default";

    @Override
    public String ssoKey() {
        return SsoTenant.SHDQ.getTenant();
    }

    public void getConfigInfo(String tenantCode) {
        if (ssoTenantProperties == null || CollectionUtils.isEmpty(ssoTenantProperties.getTenants())) {
            throw new InvalidGrantException("单点登录信息未配置，请检查配置");
        }
        if (StringUtils.isEmpty(tenantCode) || TENANT_CODE.equalsIgnoreCase(tenantCode)) {
            // 租户为空，取配置项第一个
            this.config = ssoTenantProperties.getTenants().get(0);
        } else {
            for (SsoShdqConfig info : ssoTenantProperties.getTenants()) {
                if (info.getTenantCode().equals(tenantCode)) {
                    this.config = info;
                    break;
                }
            }
        }
    }

    /**
     * 从cookie中解析token
     *
     * @param token   token
     * @param cookies cookie
     * @return token
     */
    protected String parseToken(String token, Cookie[] cookies) {
        // 获取浏览器访问访问服务器时传递过来的cookie数组
        String strToken = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                LOGGER.debug("cookie is {}", cookie.getName());
                if (cookie.getName().equalsIgnoreCase(config.getCookieName())) {
                    strToken = cookie.getValue();
                    if (StringUtils.hasText(cookie.getDomain()) && cookie.getDomain().contains(config.getDomain())) {
                        return strToken;
                    }
                }
            }
        }
        if (StringUtils.isEmpty(strToken)) {
            throw new InvalidGrantException("未包含或未解析出指定token");
        }
        return strToken;
    }

    /**
     * 组装请求体 (包含请求头)
     *
     * @param requestToken token
     * @return 请求体
     */
    protected <T extends SsoReqeust> HttpEntity<T> getRequestEntity(String requestToken) {
        // 在请求头信息中携带Basic认证信息(这里才是实际Basic认证传递用户名密码的方式)
        String account = config.getAppCode() + ":" + config.getApiAccount();
        String pwd = config.getApiPassword();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((account + ":" + pwd).getBytes(Charset.defaultCharset())));
        HttpEntity<SsoShdqRequest> httpEntity = new HttpEntity<>(new SsoShdqRequest(requestToken), headers);
        return (HttpEntity<T>) httpEntity;
    }

    /**
     * 验证和解析返回的单点登录信息
     *
     * @param token   token
     * @param cookies cookies
     * @return 返回单点登录对象
     */
    @Override
    public SsoUser parseSsoAccount(String token, Cookie[] cookies) {
        // 根据分配的租户编号初始化Config
        getConfigInfo(token);
        LOGGER.info("config==>" + config);
        // 组装请求体
        String requestToken = parseToken(token, cookies);
        HttpEntity<?> httpEntity = getRequestEntity(requestToken);
        // 远程调用鉴权方法
        SsoShdqResponse ssoResponse = ssoRestUtil.httpRequest(config.getApiUrl(), httpEntity, SsoShdqResponse.class);
        // 根据实际返回对ssorequest做校验
        if (ssoResponse.isError()) {
            throw new InvalidGrantException("单点登录服务鉴权失败，返回信息：" + ssoResponse.getMessage());
        }
        if (ssoResponse.getAccount() == null || StringUtils.isEmpty(ssoResponse.getAccount().getCode())) {
            throw new InvalidGrantException("单点登录获取的账户信息未空");
        }
        SsoUser user = new SsoUser();
        user.setTenantCode(config.getTenantCode());
        user.setUserName(ssoResponse.getAccount().getCode());
        return user;
    }
}
