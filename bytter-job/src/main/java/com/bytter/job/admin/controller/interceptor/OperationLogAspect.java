package com.bytter.job.admin.controller.interceptor;

import com.bytter.job.admin.controller.annotation.OperationLog;
import com.bytter.job.admin.core.model.XxlJobInfo;
import com.bytter.job.admin.core.model.XxlJobOperateLog;
import com.bytter.job.admin.core.model.XxlJobUser;
import com.bytter.job.admin.core.util.I18nUtil;
import com.bytter.job.admin.dao.XxlJobOperateLogDao;
import com.bytter.job.admin.service.LoginService;
import com.xxl.job.core.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * TODO:
 *
 * @author zhaohc
 * @date 2025/6/24 15:49
 */
@Aspect
@Component
public class OperationLogAspect {

    @Resource
    private XxlJobOperateLogDao xxlJobOperateLogDao;

    @Pointcut("@annotation(com.bytter.job.admin.controller.annotation.OperationLog)")
    public void operationPointcut() {}

    @AfterReturning(pointcut = "operationPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        OperationLog operationLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(OperationLog.class);

        saveOperateLog(joinPoint, operationLog, true);
    }

    @AfterThrowing(pointcut = "operationPointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        OperationLog operationLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(OperationLog.class);

        saveOperateLog(joinPoint, operationLog, false);
    }

    private void saveOperateLog(JoinPoint joinPoint, OperationLog operationLog, boolean success) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        XxlJobOperateLog log = new XxlJobOperateLog();
        log.setOperateTime(new Date());
        log.setOperateType(I18nUtil.getString(operationLog.value()));
        log.setIp(IpUtil.getIp());

        // 设置用户信息
        XxlJobUser user = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (user != null) {
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
        }

        // 设置任务信息
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            if (args[0] instanceof Integer) {
                log.setJobId((Integer) args[0]);
            } else if (args[0] instanceof XxlJobInfo) {
                log.setJobId(((XxlJobInfo) args[0]).getId());
            }
        }

        log.setDescription(success ? "操作成功" : "操作失败");
        log.setCreateBy("system");
        log.setCreateTime(new Date());
        xxlJobOperateLogDao.save(log);
    }
}
