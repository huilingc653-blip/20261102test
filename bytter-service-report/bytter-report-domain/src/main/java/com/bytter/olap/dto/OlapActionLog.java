package com.bytter.olap.dto;

import com.bytter.core.utils.JsonUtils;

import java.util.Date;

/**
 * 自定义报表的日志类
 * @history
 *  1. 2010-01-09  chenlw  修改类名为OlapActionLog；去除不必要的属性。
 */
public class OlapActionLog {

    private String requestUrl;
    private Date actionTime = new Date();

    public OlapActionLog() {
    }

    public OlapActionLog(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    public String toString() {
        return JsonUtils.toJSONString(this);
    }
}
