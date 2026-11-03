package com.bytter.poi.exception.excel.enums;
/**
 * excel导入枚举类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public enum ExcelImportEnum {
    /**
     * 不是合法的Excel模板
     */
    IS_NOT_A_VALID_TEMPLATE("不是合法的Excel模板"),
    /**
     * 值获取失败
     */
    GET_VALUE_ERROR("值获取失败"),
    /**
     * 值格式有误
     */
    FORMAT_VALUE_ERROR("值格式有误"),
    /**
     * 值校验失败
     */
    VERIFY_ERROR("值校验失败");
    private String msg;
    private ExcelImportEnum(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
