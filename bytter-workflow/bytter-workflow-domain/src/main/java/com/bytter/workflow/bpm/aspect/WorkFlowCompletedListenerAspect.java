package com.bytter.workflow.bpm.aspect;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 工作流完成监听器异常处理切面
 * 统一拦截 flowCompleted 方法的异常并记录日志
 * 
 * @author luoyj
 * @since 2026-03-06
 */
@Aspect
@Slf4j
@Component
public class WorkFlowCompletedListenerAspect {

    @Resource
    private IWorkFlowBusErrorLog iWorkFlowBusErrorLog;

    /**
     * 切入点：所有实现了 IWorkFlowCompletedListener 接口的类的 flowCompleted 方法
     */
    @Pointcut("execution(* com.bytter.workflow.bpm.IWorkFlowCompletedListener.flowCompleted(..))")
    public void flowCompletedPointcut() {
    }

    /**
     * 环绕通知：拦截所有 IWorkFlowCompletedListener 的 flowCompleted 方法
     * 
     * @param joinPoint 连接点
     * @return 方法返回值
     * @throws Throwable 抛出的异常
     */
    @Around("flowCompletedPointcut()")
    public Object aroundFlowCompleted(ProceedingJoinPoint joinPoint) throws Throwable {
        ICurrentUser user = extractUser(joinPoint.getArgs());
        String businessKey = extractBusinessKey(joinPoint.getArgs());
        // 获取流程分类 Key（从@WorkFlow 注解）
        String workflowCategory = extractWorkflowCategory(joinPoint);
        
        try {
            log.debug("流程完成回调开始，workflowCategory: {}, businessKey: {}", workflowCategory, businessKey);
            // 执行原方法
            Object result = joinPoint.proceed();
            log.debug("流程完成回调成功，workflowCategory: {}, businessKey: {}", workflowCategory, businessKey);
            return result;
            
        } catch (Exception e) {
            // 记录异常日志
            IWorkFlowResult workFlowResult = extractWorkFlowResult(joinPoint.getArgs());
            Boolean pass = workFlowResult.isPass();
            log.error("流程完成回调失败，workflowCategory: {}, businessKey: {}", workflowCategory, businessKey, e);
            iWorkFlowBusErrorLog.save(user, workflowCategory, businessKey, pass, getFullStackTrace(e));
            throw e;
        }
    }

    /**
     * 获取完整堆栈字符串
     */
    private String getFullStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
        }
        return sw.toString();
    }

    /**
     * 从参数中提取 IWorkFlowResult
     *
     * @param args 方法参数
     * @return IWorkFlowResult 对象
     */
    private IWorkFlowResult extractWorkFlowResult(Object[] args) {
        if (args != null && args.length > 0 && args[0] instanceof IWorkFlowResult) {
            return (IWorkFlowResult) args[0];
        }
        return null;
    }

    /**
     * 从参数中提取业务键
     *
     * @param args 方法参数
     * @return 业务键
     */
    private String extractBusinessKey(Object[] args) {
        IWorkFlowResult workFlowResult = extractWorkFlowResult(args);
        if (workFlowResult != null && workFlowResult.getBusinessKey() != null) {
            return workFlowResult.getBusinessKey();
        }
        return "unknown";
    }

    /**
     * 从参数中提取 user
     * @param args 方法参数
     * @return 业务键
     */
    private ICurrentUser extractUser(Object[] args) {
        IWorkFlowResult workFlowResult = extractWorkFlowResult(args);
        if (workFlowResult != null && workFlowResult.getUser() != null) {
            return workFlowResult.getUser();
        }
        return null;
    }

    /**
     * 从目标类的@WorkFlow 注解中提取流程分类 Key
     * @param joinPoint 连接点
     * @return 流程分类 Key，如果没有注解则返回"unknown"
     */
    private String extractWorkflowCategory(ProceedingJoinPoint joinPoint) {
        try {
            // 获取目标类
            Class<?> targetClass = joinPoint.getTarget().getClass();
            
            // 获取@WorkFlow 注解
            WorkFlow workFlowAnnotation = targetClass.getAnnotation(WorkFlow.class);
            
            if (workFlowAnnotation != null) {
                String[] values = workFlowAnnotation.value();
                if (values != null && values.length > 0) {
                    // 返回第一个值（通常只有一个）
                    return values[0];
                }
            }
            
            // 如果没有注解，返回 unknown
            return "unknown";
            
        } catch (Exception e) {
            // 防止反射调用失败影响主流程
            log.warn("获取@WorkFlow 注解失败", e);
            return "unknown";
        }
    }

    /**
     * around he afterThrowing 二选一
     * 异常通知：只在抛出异常时记录日志
     */
/*    @AfterThrowing(pointcut = "flowCompletedPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        IWorkFlowResult workFlowResult = extractWorkFlowResult(joinPoint.getArgs());
        String businessKey = workFlowResult != null ? workFlowResult.getBusinessKey() : "unknown";
        String workflowCategory = extractWorkflowCategory(joinPoint);
        ICurrentUser user = workFlowResult != null ? workFlowResult.getUser() : null;

        log.error("流程完成回调失败，workflowCategory: {}, businessKey: {}, userId: {}",
                workflowCategory, businessKey, user != null ? user.getId() : "unknown", e);
        //TODO 记录异常日志
    }*/
    

    private String extractWorkflowCategory(JoinPoint joinPoint) {
        try {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            WorkFlow workFlowAnnotation = targetClass.getAnnotation(WorkFlow.class);

            if (workFlowAnnotation != null) {
                String[] values = workFlowAnnotation.value();
                if (values != null && values.length > 0) {
                    return values[0];
                }
            }
            return "unknown";
        } catch (Exception e) {
            log.warn("获取@WorkFlow 注解失败", e);
            return "unknown";
        }
    }

}
