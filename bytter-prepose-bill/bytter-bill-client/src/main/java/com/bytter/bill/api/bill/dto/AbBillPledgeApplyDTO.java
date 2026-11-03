package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 质押申请-接口参数对象
 * @date 2019-06-20
 */
public class AbBillPledgeApplyDTO implements Serializable {
    /**
     * 流水表id
     */
    private String accId;
    /**
     * 票据主表id
     */
    private String vid;
    /**
     * 票据从表id
     */
    private String vvid;
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
    private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
    private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
    public String getSbbllRngStrtSn() {
        return sbbllRngStrtSn;
    }
    public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
        this.sbbllRngStrtSn = sbbllRngStrtSn;
    }
    public String getSbbllRngEndSn() {
        return sbbllRngEndSn;
    }
    public void setSbbllRngEndSn(String sbbllRngEndSn) {
        this.sbbllRngEndSn = sbbllRngEndSn;
    }
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getVvid() {
        return vvid;
    }
    public void setVvid(String vvid) {
        this.vvid = vvid;
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
        return "AbBillPledgeApplyDTO{" +
                "accId='" + accId + '\'' +
                ", vid='" + vid + '\'' +
                ", vvid='" + vvid + '\'' +
                ", corpId='" + corpId + '\'' +
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
