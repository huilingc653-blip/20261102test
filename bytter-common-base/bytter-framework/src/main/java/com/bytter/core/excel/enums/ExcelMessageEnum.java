package com.bytter.core.excel.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * excel导入导出异常提醒信息 Created by zhouj on 2019/4/18.
 */
public enum ExcelMessageEnum implements IMessageEnum {
    /** 模板解析异常 **/
    EXCEL_MESSAGE_ERROR_TEMPLATE_PARSING("991001"),
    /** 模板实例化异常 **/
    EXCEL_MESSAGE_ERROR_TEMPLATE_INSTANCE("991002"),
    /** 模板上传异常 **/
    EXCEL_MESSAGE_ERROR_TEMPLATE_UPLOAD("991003"),
    /** 复杂模板解析异常 **/
    EXCEL_MESSAGE_ERROR_COMPLEX_TEMPLATE_PARSING("991004"),
    /** 模板解析异常，请确认导入模板是否正确！ **/
    EXCEL_MESSAGE_ERROR_CUSTOM_TEMPLATE_PARSING("991005"),
    /** 导入数量大于2000 **/
    EXCEL_MESSAGE_ERROR_ROWS_GT_20000("991006"),
    ;
    private String code;
    ExcelMessageEnum(String code) {
        this.code = code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
