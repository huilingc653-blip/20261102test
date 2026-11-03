package com.bytter.poi.exception.excel.enums;
/**
 * excel导出枚举类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public enum ExcelExportEnum {
    /**
     * Excel 导出 参数错误
     */
    PARAMETER_ERROR("Excel 导出   参数错误"),
    /**
     * Excel导出错误
     */
    EXPORT_ERROR("Excel导出错误"),
    /**
     * Excel导出Html流错误
     */
    HTML_ERROR("Excel导出Html流错误"),
    /**
     * Excel 模板错误
     */
    TEMPLATE_ERROR("Excel 模板错误");
    private String msg;
    private ExcelExportEnum(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
