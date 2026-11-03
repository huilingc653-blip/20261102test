package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolBailBookVO
 * @Description: 保证金登记簿VO
 * @author wangzy
 * @date 2020/3/17 17:08
 *
 */
@SuppressWarnings("serial")
public class AbPoolBailBookVO implements Serializable {
    /**保证金登记簿Id*/
    private String id;
    /**存取类型*/
    @Dict(dictGroup = "AB_POOL_BAIL_TRANS_TYPE", inject = "typeName")
    private String type;
    /**存取类型名称*/
    @BtCompositeSearch(fieldName="bail.type")
    private String typeName;
    /**存取金额*/
    private BigDecimal money;
    /**交易日期*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="bail.trans_Date")
    private Date transDate;
    /**存取账号*/
    private String oppAcc;
    /**存取账户名称*/
    private String oppAccName;
    /**存取账户行号*/
    private String oppBankNo;
    /**存取账户行名*/
    private String oppBankName;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName="dtl.status")
    private Integer status;
    private String statusName;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName="dtl.opt_status")
    private String optStatus;
    private String optStatusName;
    /**保证金账户*/
    private String bailAcc;
    /**单位*/
    @BtCompositeSearch(fieldName="bail.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName="s.code")
    private String corpCodeAndName;
    /**票据池*/
    @BtCompositeSearch(fieldName="bail.bp_id")
    private String pbCodeAndName;
    /**更新时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public Date getTransDate() {
        return transDate;
    }
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getOppAcc() {
        return oppAcc;
    }
    public void setOppAcc(String oppAcc) {
        this.oppAcc = oppAcc;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppBankNo() {
        return oppBankNo;
    }
    public void setOppBankNo(String oppBankNo) {
        this.oppBankNo = oppBankNo;
    }
    public String getOppBankName() {
        return oppBankName;
    }
    public void setOppBankName(String oppBankName) {
        this.oppBankName = oppBankName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getOptStatusName() {
        return optStatusName;
    }
    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }
    public String getBailAcc() {
        return bailAcc;
    }
    public void setBailAcc(String bailAcc) {
        this.bailAcc = bailAcc;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpCodeAndName() {
        return corpCodeAndName;
    }
    public void setCorpCodeAndName(String corpCodeAndName) {
        this.corpCodeAndName = corpCodeAndName;
    }
    public String getPbCodeAndName() {
        return pbCodeAndName;
    }
    public void setPbCodeAndName(String pbCodeAndName) {
        this.pbCodeAndName = pbCodeAndName;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
