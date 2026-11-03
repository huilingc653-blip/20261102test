package com.bytter.biz.fundtransfer.bean;


import com.bytter.core.annotation.BtCompositeSearch;

import java.util.Date;

/**
 * @author fangcf
 */
public class BisAccGatherPolicySearchBean {
    //归集类型
    @BtCompositeSearch(fieldName = "a.gather_type", operCode = "IN")
    private String[] gatherType;
    //归集模式
    @BtCompositeSearch(fieldName = "a.gather_mode", operCode = "IN")
    private String[] gatherMode;
    //银行编码
    @BtCompositeSearch(fieldName = "a.bif_code", operCode = "=")
    private String bifCode;
    //开始执行时间
    @BtCompositeSearch(fieldName = "a.first_time", operCode = "<")
    private Date firstTime;

    //nexttime 需要加上空判断，在此不使用btCompositeSearch
    private Date nextTime;

    public String[] getGatherType() {
        return gatherType;
    }

    public void setGatherType(String[] gatherType) {
        this.gatherType = gatherType;
    }

    public String[] getGatherMode() {
        return gatherMode;
    }

    public void setGatherMode(String[] gatherMode) {
        this.gatherMode = gatherMode;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

}
