package com.bytter.erp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhenghj
 * @version 1.0
 * @Description: ERP质押申请-接口参数对象
 * @date 2019-06-21
 */

public class ErpAbBillPledgeApplyDTO implements Serializable {


    /**
     * 持票单位
     */
    private String corpId;

    /**
     * 票据号码
     */
    private String origNo;

    /**
     * 是否内部质押
     */
    private String interiorApply;

    /**
     * 质权人单位ID
     */
    private String pledgeeCorpid;

    /**
     * 质权人账号
     */
    private String pledgeeAcc;

    /**
     * 质权人名称
     */
    private String pledgeeName;

    /**
     * 质权人开户行行号
     */
    private String pledgeeBankAcc;

    /**
     * 质权人开户行行名
     */
    private String pledgeeBankName;

    /**
     * 业务办理日期
     */
    private Date businessDate;

    /**
     * 备注
     */
    private String rmk;

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

    public String getInteriorApply() {
        return interiorApply;
    }

    public void setInteriorApply(String interiorApply) {
        this.interiorApply = interiorApply;
    }

    public String getPledgeeCorpid() {
        return pledgeeCorpid;
    }

    public void setPledgeeCorpid(String pledgeeCorpid) {
        this.pledgeeCorpid = pledgeeCorpid;
    }

    public String getPledgeeAcc() {
        return pledgeeAcc;
    }

    public void setPledgeeAcc(String pledgeeAcc) {
        this.pledgeeAcc = pledgeeAcc;
    }

    public String getPledgeeName() {
        return pledgeeName;
    }

    public void setPledgeeName(String pledgeeName) {
        this.pledgeeName = pledgeeName;
    }

    public String getPledgeeBankAcc() {
        return pledgeeBankAcc;
    }

    public void setPledgeeBankAcc(String pledgeeBankAcc) {
        this.pledgeeBankAcc = pledgeeBankAcc;
    }

    public String getPledgeeBankName() {
        return pledgeeBankName;
    }

    public void setPledgeeBankName(String pledgeeBankName) {
        this.pledgeeBankName = pledgeeBankName;
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

    @Override
    public String toString() {
        return "ErpAbBillPledgeApplyDTO{" +
                "corpId='" + corpId + '\'' +
                ", origNo='" + origNo + '\'' +
                ", interiorApply='" + interiorApply + '\'' +
                ", pledgeeCorpid='" + pledgeeCorpid + '\'' +
                ", pledgeeAcc='" + pledgeeAcc + '\'' +
                ", pledgeeName='" + pledgeeName + '\'' +
                ", pledgeeBankAcc='" + pledgeeBankAcc + '\'' +
                ", pledgeeBankName='" + pledgeeBankName + '\'' +
                ", businessDate=" + businessDate +
                ", rmk='" + rmk + '\'' +
                '}';
    }
}
