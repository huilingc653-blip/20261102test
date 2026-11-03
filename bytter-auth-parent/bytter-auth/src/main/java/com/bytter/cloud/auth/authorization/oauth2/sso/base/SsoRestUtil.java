package com.bytter.cloud.auth.authorization.oauth2.sso.base;

import com.bytter.cloud.common.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * 通过RestTemplate获取SSO鉴权信息。
 *
 * @author zhaohc
 * @date 2024/5/17 14:21
 */
@Slf4j
@Component
public class SsoRestUtil {

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    /**
     * 组装JSON请求体。
     *
     * @param json json请求体。
     * @return 请求体。
     */
    public HttpEntity getJsonEntity(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(json, headers);
    }

    /**
     * SSO远程调用方法。
     *
     * @param ssoUrl     token
     * @param httpEntity cookies
     * @param <T>        response
     * @return 响应
     */
    public <T extends SsoResponse> T httpRequest(String ssoUrl, HttpEntity<?> httpEntity, Class<T> responseType) {
        try {
            T ssoResponse = restTemplate.postForObject(ssoUrl, httpEntity, responseType);
            log.debug("请求url:{},响应:{}", ssoUrl, JsonUtil.toJson(ssoResponse));
            if (ssoResponse == null) {
                throw new InvalidGrantException("单点登录服务鉴权失败：无响应信息");
            }
            return ssoResponse;
        } catch (ResourceAccessException resourceAccessException) {
            log.error("ResourceAccessException:", resourceAccessException);
            throw new InvalidGrantException("单点登录服务网络异常");
        } catch (Exception e) {
            log.error("SSO server:", e);
            throw new InvalidGrantException("单点登录服务异常");
        }
    }


}
