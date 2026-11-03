package com.bytter.biz.erp.base;

/**
 * <br>
 * <b>功能：</b>新日志信息接口<br>
 * <b>更新者：</b><br>
 * <b>日期：</b> <br>
 * <b>更新内容：</b><br>
 */
public interface IBaseNewLog {

    public void debug(String msg);
    
    public void info(String msg);

    public void error(String msg);

    public void printStackTrace(Exception e);
    
}
