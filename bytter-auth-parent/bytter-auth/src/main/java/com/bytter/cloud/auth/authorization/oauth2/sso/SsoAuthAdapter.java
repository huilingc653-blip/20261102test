package com.bytter.cloud.auth.authorization.oauth2.sso;
import com.bytter.cloud.auth.authorization.oauth2.sso.shdq.SsoTenantProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.List;
/**
 * 单点登录--鉴权适配器
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@Service
@Lazy
public class SsoAuthAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SsoAuthAdapter.class);

    @Autowired
    private List<SsoService> serviceList;
    /**
     * 适配并发远程起验证
     *
     * @param ssoKey 第三方标识键。
     * @return Sso Service
     */
    public SsoService adapterSsoService(String ssoKey) {
        //获取服务类
        SsoService ssoService = null;
        for (SsoService tmpService : serviceList) {
            if (ssoKey.equalsIgnoreCase(tmpService.ssoKey())) {
                ssoService = tmpService;
                break;
            }
        }
        if (ssoService == null || StringUtils.isEmpty(ssoKey) ) {
            LOGGER.error("未找到指定单点登录服务 ssoKey={}", ssoKey);
            throw new InvalidGrantException("未找到指定单点登录服务");
        }
        return ssoService;
    }
}
