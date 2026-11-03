package com.bytter.dual.component;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.Reflections;
import com.bytter.dual.interfaces.DualManagerCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.io.IOException;
/**
 * 双管理员模版基类
 * Created by zhoul on 2017/8/31.
 */
public abstract class AbstractDualManagerCommand<T> implements DualManagerCommand<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ObjectMapper mapper;
    public String serializable(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        }
        return "";
    }
    public T serializableReversal(String str) {
        Class<T> clazz = Reflections.getClassGenricType(getClass());
        try {
            return mapper.readValue(str, clazz);
        } catch (IOException e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        }
        return null;
    }
    @Override
    public void execute(T variables) {
        executeData(variables);
    }
    abstract void executeData(T variables);
}
