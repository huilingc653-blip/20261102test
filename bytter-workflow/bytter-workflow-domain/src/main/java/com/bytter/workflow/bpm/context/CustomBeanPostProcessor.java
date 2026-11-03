package com.bytter.workflow.bpm.context;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import java.util.Objects;

/**
 * @deprecated 请使用 {@link WorkFlowListenerRegistrar} 替代
 * 
 * 原因：在 postProcessAfterInitialization 阶段注册时，Spring AOP 可能还未创建代理
 */
@Deprecated
// @Configuratio
public class CustomBeanPostProcessor implements BeanPostProcessor, MergedBeanDefinitionPostProcessor, Ordered {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IWorkFlowListener) {
            WorkFlow workFlow = AnnotationUtils.findAnnotation(bean.getClass(), WorkFlow.class);
            if (Objects.nonNull(workFlow)) {
                String[] value = workFlow.value();
                WorkFlowHelper.helper().setFlowListenerMap(bean, value);
            }
        }
        return bean;
    }
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String s) {
        //do nothing
    }
    @Override
    public int getOrder() {
        return -1;
    }
}
