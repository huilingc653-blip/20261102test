package com.bytter.workflow.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 兼容app旧版本
 */
public class AppTaskVo extends TaskVo implements Serializable {

    private String username;
    private Date entTime;

    public Date getEntTime() {
        return entTime;
    }

    public void setEntTime(Date entTime) {
        this.entTime = entTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
