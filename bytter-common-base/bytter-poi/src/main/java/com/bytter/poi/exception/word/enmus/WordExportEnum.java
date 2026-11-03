package com.bytter.poi.exception.word.enmus;
/**
 * word导出枚举类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public enum WordExportEnum {
    /**
     * Excel 导出 参数错误
     */
    EXCEL_PARAMS_ERROR("Excel 导出 参数错误"),
    /**
     * Excel 表头 有的字段为空
     */
    EXCEL_HEAD_HAVA_NULL("Excel 表头 有的字段为空"),
    /**
     * Excel 没有表头
     */
    EXCEL_NO_HEAD("Excel 没有表头");
    private String msg;
    private WordExportEnum(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
