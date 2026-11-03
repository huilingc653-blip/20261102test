package com.bytter.core.autoconfig;

import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.entity.BtLogDTO;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.BrowserUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.IpUtil;
import com.bytter.core.utils.JsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author wade
 * @description
 * @createDate 2025/6/3
 */
@Component
@Aspect
public class LogAopAspect {

    @Autowired
    private MessageProducer messageProducer;

    @Pointcut("@annotation(logMessage)")
    public void logMessageMethod(LogMessage logMessage) {
    }

    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";

    @Around("logMessageMethod(logMessage)")
    public Object handler(ProceedingJoinPoint proceedingJoinPoint, LogMessage logMessage) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        request.getCookies();
        String tokenUser = request.getHeader(X_CLIENT_TOKEN_USER);
        String decodeUser = URLUtil.decode(tokenUser, StandardCharsets.UTF_8);
        ShiroUser shiroUser = JsonUtils.parseObject(decodeUser, ShiroUser.class);
        try {
            BtLogDTO log = new BtLogDTO();
            log.setLogcontent(logMessage.content());
            log.setLoglevel(logMessage.level());
            log.setOperatetype(logMessage.type());
            log.setBroswer(!StringUtils.isBlank(BrowserUtils.checkBrowse(request)) ? BrowserUtils.checkBrowse(request) : "");
            log.setOperatetime(DateUtils.getDate());
//            log.setNote(!StringUtils.isBlank(request.getRemoteHost()) ? request.getRemoteHost() : "");
            log.setNote(IpUtil.getClientIpAddress(request));
            log.setUserId(!ObjectUtils.isEmpty(shiroUser) ? shiroUser.getId() : "");
            log.setTenantId(!ObjectUtils.isEmpty(shiroUser) ? shiroUser.getTenantId() : "");
            String message = JsonUtils.toJSONString(log);
            messageProducer.sendMessage(message);
        } catch (Exception e) {
            return proceedingJoinPoint.proceed();
        }
        return proceedingJoinPoint.proceed();
    }
}
