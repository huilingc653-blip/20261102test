package com.bytter.poi.handler.inter;
/**
 * Excel标记类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public interface IExcelModel {
    /**
     * 获取错误数据
     *
     * @return 错误信息
     */
    String getErrorMsg();
    /**
     * 设置错误信息
     *
     * @param errorMsg
     *            错误信息
     */
    void setErrorMsg(String errorMsg);
}
