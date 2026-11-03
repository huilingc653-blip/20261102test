package com.bytter.cloud.auth.event;

import cn.hutool.extra.servlet.ServletUtil;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.cloud.auth.persistence.LogLogin;
import com.bytter.cloud.common.core.util.JsonUtil;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.SocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;





/**
 * 登陆事件监听
 *
 * @author mfs 2022-08-10
 * @since 1.0.0
 */
@Slf4j
@Component
public class LoginEventListener {

    //是否set 注入

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    private static final String BYTTER_PRE = "bytter_";

    /**
     * 用户登陆地址
     */
    private static final String USER_LOGIN_ADDRESS = "system:login:address:";

    /**
     * 登陆成功事件通知
     *
     * @param successEvent 登陆成功事件
     */
    @EventListener
    public void loginSuccessEvent(AuthenticationSuccessEvent successEvent) {
        //用户登陆记录登陆日志
        if (successEvent.getSource() instanceof UsernamePasswordAuthenticationToken
                && ((UsernamePasswordAuthenticationToken) successEvent.getSource()).getPrincipal() instanceof AuthUser) {
            LogLogin logLogin = initLogLogin();
            logLogin.setStatus(1);
            logLogin.setRemark("登陆成功");
            try {
                AuthUser user = (AuthUser) ((UsernamePasswordAuthenticationToken) successEvent.getSource()).getPrincipal();
                redisTemplate.opsForValue().set(USER_LOGIN_ADDRESS + user.getCurrentUserId(), logLogin.getIpAddress());
                //配置开启了踢人
                ConfigDTO config = configService.getGlobalConfig(ConfigConstant.SYS, SysConfigConstant.KICK_OUT);
                if (Objects.nonNull(config) && CommonConstant.YES.equals(config.getValue())) {
                    String socketMsg = String.format("您已经被其他用户踢出，请查看帐号是否泄漏，登陆用户ip地址:%s", logLogin.getIpAddress());
                    notificationService.sendSocketMsg(SocketMsg.build(SocketMsg.Type.KICK_OUT, socketMsg), user.getCurrentUserId());
                    log.info("用户 {} 被踢出：{}", user.getCurrentUserId(), socketMsg);
                }
            } catch (Exception exception) {
                log.error("用户登陆地址存放失败", exception);
            }
        }
    }

    /**
     * 框架登陆失败事件通知
     *
     * @param failureEvent 失败通知事件
     */
    @EventListener
    public void loginFailEvent(AbstractAuthenticationFailureEvent failureEvent) {
        LogLogin logLogin = initLogLogin();
        logLogin.setStatus(0);
        logLogin.setRemark(failureEvent.getException().getMessage());
//        CompletableFuture.runAsync(() -> systemClient.saveLoginLog(logLogin));
    }

    /**
     * 自定义登陆失败事件通知
     *
     * @param failureEvent 失败通知事件
     */
    @EventListener
    public void loginFailEvent(BytterLoginFailureEvent failureEvent) {
        LogLogin logLogin = initLogLogin();
        logLogin.setStatus(0);
        logLogin.setRemark(failureEvent.getException().getMessage());
//        CompletableFuture.runAsync(() -> systemClient.saveLoginLog(logLogin));
    }

    private LogLogin initLogLogin() {
        LogLogin.LogLoginBuilder builder = LogLogin.builder();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            String clientIp = ServletUtil.getClientIP(request);
            String agent = ServletUtil.getHeader(request, "User-Agent", StandardCharsets.UTF_8);
            String address = ServletUtil.getHeader(request, "address", StandardCharsets.UTF_8);
            try {
                if (StringUtils.isNoneBlank(address)) {
                    address = URLDecoder.decode(address, "utf-8");
                }
            } catch (Exception e) {
                log.error("地址解析错误", e);
            }
            Map<String, String> paramMap = ServletUtil.getParamMap(request);
            builder.ipAddress(clientIp).device(agent).requestParam(JsonUtil.toJson(paramMap)).address(address)
                    .name(paramMap.get("username")).tenantId(BYTTER_PRE + paramMap.get("tenantId"));
        }
        LogLogin logLogin = builder.build();
        logLogin.setCreatedTime(new Date());
        return logLogin;
    }
}
