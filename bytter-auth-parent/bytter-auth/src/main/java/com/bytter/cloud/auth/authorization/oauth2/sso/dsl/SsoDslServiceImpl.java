package com.bytter.cloud.auth.authorization.oauth2.sso.dsl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.SsoService;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoResponse;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoRestUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoTenant;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoUser;
import com.bytter.cloud.common.core.util.JsonUtil;
import com.bytter.system.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

/**
 * 单点登录鉴权服务--大参林实现
 *
 * @author zhaohc
 * @date 2024/5/16 18:01
 */
@Slf4j
@Service
public class SsoDslServiceImpl implements SsoService {

    @Resource
    private SsoDslConfig config;
    @Resource
    private SsoRestUtil ssoRestUtil;

    @Override
    public String ssoKey() {
        return SsoTenant.DSL.getTenant();
    }

    /**
     * 组装鉴权请求体 (包含请求头)
     *
     * @param ticket 鉴权Ticket。
     * @return 请求体。
     */
    protected HttpEntity getValidEntity(String ticket) {
        SsoDslRequest ssoDslRequest = new SsoDslRequest();
        ssoDslRequest.setSt(ticket);
        ssoDslRequest.setService(config.getLoginUrl());
        return ssoRestUtil.getJsonEntity(JSONUtil.toJsonStr(ssoDslRequest));
    }

    @Override
    public SsoUser parseSsoAccount(String token, Cookie[] cookies) {
        log.info("config==>{}", config);
        // 组装请求体
        HttpEntity validEntity = getValidEntity(token);
        // 远程调用鉴权方法
        SsoDslResponse ssoDslResponse = ssoRestUtil.httpRequest(config.getValidateUrl(), validEntity, SsoDslResponse.class);
        if (!ssoDslResponse.isSuccess()){
            log.error("单点登录服务鉴权失败，返回信息：" + ssoDslResponse.getMsg());
            throw new InvalidGrantException("单点登录服务鉴权失败！");
        }
        if (ssoDslResponse.getData() == null) {
            throw new InvalidGrantException("单点登录获取的账户信息不存在！");
        }
        JSONObject jsonData = JSONUtil.parseObj(ssoDslResponse.getData());
        log.info("jsonData==>{}",jsonData);
        SsoUser user = new SsoUser();
        user.setUserName(jsonData.getStr("empId"));
        user.setEmployeeId(jsonData.getStr("empId"));
        return user;
    }
}
