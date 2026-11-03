package com.bytter.core.base;
import com.bytter.core.utils.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 作为Service公共抽象基类
 *
 * @author zhoul
 * @since 2017/5/14.
 */
public abstract class AbstractService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public Object getBean(Class classz) {
        return ApplicationContextUtil.getContext().getBean(classz);
    }
    public Object getBean(String classz) {
        return ApplicationContextUtil.getContext().getBean(classz);
    }
}
