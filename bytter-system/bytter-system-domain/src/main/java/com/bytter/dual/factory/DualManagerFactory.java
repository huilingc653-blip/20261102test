package com.bytter.dual.factory;
import com.bytter.core.annotation.DualManager;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.dual.interfaces.DualManagerCommand;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhoul on 2017/8/31.
 */
public class DualManagerFactory implements ApplicationListener<ContextRefreshedEvent> {
    private static final Map<String, Class<?>> COMMAND_MAP = new HashMap<>();
    public static <T> DualManagerCommand getCommand(Class<? extends DualManagerCommand<T>> aClass) {
        ApplicationContext applicationContext = ApplicationContextUtil.getContext();
        return applicationContext.getBean(aClass);
    }
    public static <T> DualManagerCommand<T> getCommand(String commandName) {
        ApplicationContext applicationContext = ApplicationContextUtil.getContext();
        Class<?> aClass = COMMAND_MAP.get(commandName);
        if (aClass != null) {
            return (DualManagerCommand<T>) applicationContext.getBean(aClass);
        }
        return null;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(DualManager.class);
            for (String keyObj : beans.keySet()) {
                Object bean = beans.get(keyObj);
                if (bean instanceof DualManagerCommand) {
                    Class<?> targetClass = AopUtils.getTargetClass(bean);
                    DualManager workFlowCallBack = targetClass.getAnnotation(DualManager.class);
                    if (workFlowCallBack != null) {
                        COMMAND_MAP.put(targetClass.getName(), targetClass);
                    }
                }
            }
        }
    }
}
