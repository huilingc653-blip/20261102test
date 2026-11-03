package com.bytter.biz.finacial.interest.vo;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 企业通知存款VO对象
 *
 * @author zuoy 2020-12-22
 */
public class NoticeAccountVO implements java.io.Serializable {
    private String id;
    /**
     * 账号
     */
    private String blnctrAcc;
    /**
     * 账号名称
     */
    private String accName;
    /**
     * 网点
     */
    private String netId;
    private String netName;
    /**
     * 单位
     */
    private String corpId;
    private String corpName;
    /**
     * 币种
     */
    private String curId;
    private String curName;
    /**
     * 内部账户余额
     */
    private BigDecimal fcBalance;
    /**
     * 起息日期
     */
    private Date interestStartDate;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 通知类型
     */
    private Integer notifyType;
    /**
     * 关联活期账号
     */
    private String relationAccId;
    /**
     * 关联活期账号
     */
    private String relationAcc;
    /**
     * 关联活期账号名称
     */
    private String relationAccName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getFcBalance() {
        return fcBalance;
    }
    public void setFcBalance(BigDecimal fcBalance) {
        this.fcBalance = fcBalance;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Integer getNotifyType() {
        return notifyType;
    }
    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }
    public String getRelationAccId() {
        return relationAccId;
    }
    public void setRelationAccId(String relationAccId) {
        this.relationAccId = relationAccId;
    }
    public String getRelationAcc() {
        return relationAcc;
    }
    public void setRelationAcc(String relationAcc) {
        this.relationAcc = relationAcc;
    }
    public String getRelationAccName() {
        return relationAccName;
    }
    public void setRelationAccName(String relationAccName) {
        this.relationAccName = relationAccName;
    }
}
