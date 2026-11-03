package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: ERP托收申请-接口参数对象
 * @date 2019-06-24
 */
public class AbBillCollectionApplyDTO implements Serializable {
    /** 流水表ids*/
    private String aids;
    /** 持票单位 */
    private String corpId;
    /**  票据号码 */
    private String origNo;
    /** 业务办理日期 */
    private Date businessDate;
    /** 托收说明  */
    private String rmk;
    /**逾期托收说明/备注*/
    private String isOverdueRmk;
    /**清算模式:SM00：线上清算，SM01：线下清算*/
    private String sttlmmk;
    public String getAids() {
        return aids;
    }
    public void setAids(String aids) {
        this.aids = aids;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsOverdueRmk() {
        return isOverdueRmk;
    }
    public void setIsOverdueRmk(String isOverdueRmk) {
        this.isOverdueRmk = isOverdueRmk;
    }
    public String getSttlmmk() {
        return sttlmmk;
    }
    public void setSttlmmk(String sttlmmk) {
        this.sttlmmk = sttlmmk;
    }
}
