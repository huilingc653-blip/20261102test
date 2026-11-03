package com.bytter.core.enums;


import lombok.Getter;

/**
 * 通用错误异常定义
 *
 * @author 刘卫锋 2022-03-01
 * @since 1.0.0
 */
@Getter
public enum XssCodeEnum{
   
	 XSS_SQL_CHECK_EXCEPTION("CM5010", "xss 或 sql注入非法请求");


    /**
     * 错误类型码
     */
    private final String code;

    /**
     * 错误类型描述信息
     */
    private final String msg;

    /**
     * 构造器
     *
     * @param code 错误代码
     * @param msg  错误信息
     */
    XssCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
