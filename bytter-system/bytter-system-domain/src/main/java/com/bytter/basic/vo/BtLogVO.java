package com.bytter.basic.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by  zhouj on 2017/11/10.
 */
public class BtLogVO implements Serializable{
    private BigDecimal loglevel;
    @BtCompositeSearch(fieldName = "operatetime")
    private String operatetime;
    private BigDecimal operatetype;
    @BtCompositeSearch(fieldName = "logcontent")
    private String logcontent;
    private String broswer;
    @BtCompositeSearch(fieldName = "note")
    private String note;
    @BtCompositeSearch(fieldName = "u.username")
    private String username;

    @BtCompositeSearch(fieldName = "operatetime")
    private Date startTime;
    @BtCompositeSearch(fieldName = "operatetime")
    private Date endTime;

    public BigDecimal getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(BigDecimal loglevel) {
        this.loglevel = loglevel;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public BigDecimal getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(BigDecimal operatetype) {
        this.operatetype = operatetype;
    }

    public String getLogcontent() {
        return logcontent;
    }

    public void setLogcontent(String logcontent) {
        this.logcontent = logcontent;
    }

    public String getBroswer() {
        return broswer;
    }

    public void setBroswer(String broswer) {
        this.broswer = broswer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
