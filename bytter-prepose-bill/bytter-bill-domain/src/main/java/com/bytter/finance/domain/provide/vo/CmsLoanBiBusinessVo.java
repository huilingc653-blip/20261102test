package com.bytter.finance.domain.provide.vo;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author caojy
 *
 */
public class CmsLoanBiBusinessVo {
    private String corpIds;
    private String curId;
    private String[] corpNames;
    private String sendCode;
    private String sendBeginDate;
    private String sendEndDate;
    private String overBeginDate;
    private String overEndDate;
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public String getCorpIds() {
        return corpIds;
    }
    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }
    public String[] getCorpNames() {
        return corpNames;
    }
    public void setCorpNames(String[] corpNames) {
        this.corpNames = corpNames;
    }
    public String getSendBeginDate() {
        return sendBeginDate;
    }
    public void setSendBeginDate(String sendBeginDate) {
        this.sendBeginDate = sendBeginDate;
    }
    public String getSendEndDate() {
        return sendEndDate;
    }
    public void setSendEndDate(String sendEndDate) {
        this.sendEndDate = sendEndDate;
    }
    public String getOverBeginDate() {
        return overBeginDate;
    }
    public void setOverBeginDate(String overBeginDate) {
        this.overBeginDate = overBeginDate;
    }
    public String getOverEndDate() {
        return overEndDate;
    }
    public void setOverEndDate(String overEndDate) {
        this.overEndDate = overEndDate;
    }
}
