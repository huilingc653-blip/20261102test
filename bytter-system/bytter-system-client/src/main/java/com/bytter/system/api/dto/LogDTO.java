package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhouj on 2017/5/17.
 */
public class LogDTO implements Serializable {
    private Short loglevel;
    private Date operatetime;
    private Short operatetype;
    private String logcontent;
    private String broswer;
    private String note;
    public Short getLoglevel() {
        return loglevel;
    }
    public void setLoglevel(Short loglevel) {
        this.loglevel = loglevel;
    }
    public Date getOperatetime() {
        return operatetime;
    }
    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }
    public Short getOperatetype() {
        return operatetype;
    }
    public void setOperatetype(Short operatetype) {
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
}
