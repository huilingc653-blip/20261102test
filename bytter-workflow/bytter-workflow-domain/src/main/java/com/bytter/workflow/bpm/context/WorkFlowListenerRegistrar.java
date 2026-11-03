package com.bytter.workflow.bpm.context;

import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 工作流监听器自动注册器
 * 在所有单例 Bean 初始化完成后，扫描并注册所有带有@WorkFlow 注解的 IWorkFlowListener
 * 
 * @author luoyj
 * @since 2026-03-06
 */
@Slf4j
@Configuration
public class WorkFlowListenerRegistrar implements SmartInitializingSingleton, ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /**
     * 在所有单例 Bean 初始化完成后执行
     * 此时 Spring AOP 已经完成，获取到的都是代理对象
     */
    @Override
    public void afterSingletonsInstantiated() {
        log.info("开始注册工作流监听器...");
        
        // 扫描所有 IWorkFlowListener 类型的 Bean
        Map<String, IWorkFlowListener> beans = applicationContext.getBeansOfType(IWorkFlowListener.class);
        
        if (beans.isEmpty()) {
            log.warn("未找到任何工作流监听器 Bean，请检查是否有类实现了 IWorkFlowListener 接口并标注了@WorkFlow 注解");
            return;
        }
        
        log.info("共发现{}个 IWorkFlowListener 类型的 Bean", beans.size());
        
        int registeredCount = 0;
        int skippedCount = 0;
        
        for (Map.Entry<String, IWorkFlowListener> entry : beans.entrySet()) {
            String beanName = entry.getKey();
            IWorkFlowListener listener = entry.getValue();
            
            try {
                // 获取实际的类（可能是代理类，需要获取目标类）
                Class<?> listenerClass = getTargetClass(listener);
                
                // 查找@WorkFlow 注解
                WorkFlow workFlow = AnnotationUtils.findAnnotation(listenerClass, WorkFlow.class);
                
                if (Objects.nonNull(workFlow)) {
                    String[] value = workFlow.value();
                    
                    // 注册到 WorkFlowHelper（此时 listener 已经是 AOP 代理对象）
                    WorkFlowHelper.helper().setFlowListenerMap(listener, value);
                    
                    log.info("✅ 已注册工作流监听器：beanName={}, class={}, cateKeys={}", 
                        beanName, listenerClass.getSimpleName(), String.join(",", value));
                    
                    registeredCount++;
                } else {
                    log.debug("⚠️ 跳过无@WorkFlow 注解的 Bean: beanName={}, class={}", 
                        beanName, listenerClass.getSimpleName());
                    skippedCount++;
                }
            } catch (Exception e) {
                log.error("❌ 注册工作流监听器失败：beanName={}, class={}", 
                    beanName, listener != null ? listener.getClass().getName() : "null", e);
            }
        }
        
        log.info("工作流监听器注册完成，共注册{}个，跳过{}个", registeredCount, skippedCount);
        
        // 打印所有已注册的 cateKey，方便调试
        log.info("已注册的流程分类 Keys: {}", getAllRegisteredCateKeys());
    }
    
    /**
     * 获取所有已注册的流程分类 Key（用于调试）
     */
    private String getAllRegisteredCateKeys() {
        try {
            java.lang.reflect.Field field = WorkFlowHelper.class.getDeclaredField("flowListenerMap");
            field.setAccessible(true);
            Object map = field.get(WorkFlowHelper.helper());
            if (map instanceof Map) {
                return ((Map<?, ?>) map).keySet().toString();
            }
        } catch (Exception e) {
            // ignore
        }
        return "unknown";
    }
    
    /**
     * 获取目标的 Class
     * 如果是 JDK 动态代理，获取接口；如果是 CGLIB 代理，获取目标类
     */
    private Class<?> getTargetClass(IWorkFlowListener listener) {
        Class<?> clazz = listener.getClass();
        
        // 如果是 CGLIB 代理（类名包含$$EnhancerBySpringCGLIB$$）
        if (clazz.getName().contains("$$EnhancerBySpringCGLIB$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return superClass;
            }
        }
        
        // 默认返回当前类
        return clazz;
    }
}
