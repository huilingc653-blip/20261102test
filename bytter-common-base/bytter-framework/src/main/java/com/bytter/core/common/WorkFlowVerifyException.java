package com.bytter.core.common;
import java.io.Serializable;

import com.bytter.core.common.exception.AbstractBytterException;
import com.bytter.core.enums.IMessageEnum;

/**
 * 工作流校验异常
 *
 * @author chenlw
 * @since 2020-05-20
 */
public class WorkFlowVerifyException extends AbstractBytterException implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 工作流校验状态,用于强控还是提示.
     *  WorkFlowConstant
     */
    private String verifyStatus;
    /**
     * 向外层抛出枚举业务异常错误
     *
     * @param IMessageEnum
     * @param verifyStatus @see WorkFlowConstant，
     */
    public WorkFlowVerifyException(IMessageEnum IMessageEnum,String verifyStatus, String... params) {
    	super(IMessageEnum, params);
        this.verifyStatus = verifyStatus;
    }
    /**
     * 向外层抛出枚举业务异常错误
     *
     * @param IMessageEnum
     * @param verifyStatus @see WorkFlowConstant，
     */
    public WorkFlowVerifyException(IMessageEnum IMessageEnum, String verifyStatus) {
        super(IMessageEnum);
        this.verifyStatus = verifyStatus;
    }
    /**
     * 向外层抛出枚举业务异常错误
     *
     * @param IMessageEnum
     */
    public WorkFlowVerifyException(IMessageEnum IMessageEnum) {
        super(IMessageEnum);
    }
    /**
     * 向外层抛出枚举业务异常错误
     *
     * @param IMessageEnum 枚举错误描述
     * @param params       替换参数
     */
    public WorkFlowVerifyException(IMessageEnum IMessageEnum, String... params) {
        super(IMessageEnum, params);
    }
    /**
     * 抛出错误信息，不建议使用
     *
     * @param message
     */
    public WorkFlowVerifyException(String message) {
        super(message);
    }
    public WorkFlowVerifyException(Throwable cause) {
        super(cause);
    }
    public WorkFlowVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
    public String getVerifyStatus() {
        return verifyStatus;
    }
    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}
