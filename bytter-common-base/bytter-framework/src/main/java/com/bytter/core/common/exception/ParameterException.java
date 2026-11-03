package com.bytter.core.common.exception;
import com.bytter.core.enums.IMessageEnum;
import java.io.Serializable;
/**
 * 参数异常类
 * 
 * @author 周亮
 */
public class ParameterException extends AbstractBytterException implements Serializable {
    /**
     * 向外层抛出枚举业务异常错误
     * 
     * @param IMessageEnum
     */
    public ParameterException(IMessageEnum IMessageEnum) {
        super(IMessageEnum);
    }
    /**
     * 向外层抛出枚举业务异常错误
     * 
     * @param IMessageEnum
     *            枚举错误描述
     * @param params
     *            替换参数
     */
    public ParameterException(IMessageEnum IMessageEnum, String... params) {
        super(IMessageEnum, params);
    }
    /**
     * 抛出错误信息，不建议使用
     * 
     * @param message
     */
    @Deprecated
    public ParameterException(String message) {
        super(message);
    }
    @Deprecated
    public ParameterException(Throwable cause) {
        super(cause);
    }
    @Deprecated
    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
