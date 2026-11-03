package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-22 15:15
 * @Version 1.0
 */
public class GuaContractBillVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 开票申请单号
     */
    private String appNo;
    /**
     * 票据类型
     */
    @Dict(dictGroup = "AB_BAIL_TYPE", inject = "billTypeName")
    @BtCompositeSearch(fieldName = "v.bill_type")
    private Integer billType;

    @BtCompositeSearch(fieldName = "v.bill_type")
    private String billTypeName;
    /**
     * 流通方式
     */
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    private String billFlagName;
    /**
     * 票据号码
     */
    private String origNo;
    /**
     * 票据金额
     */
    private BigDecimal billMoney;
    /**
     * 出票人
     */
    private String payer;
    /**
     * 收票人
     */
    private String payee;
    /**
     * 承兑人
     */
    private String acceptor;
    /**
     * 出票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "gco.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 系统代码
     */
    @BtCompositeSearch(fieldName = "gco.syscode")
    private String sysCode;
    /**
     * 担保合同ID
     */
    @BtCompositeSearch(fieldName = "gco.contract_id")
    private String contractId;
    /**
     * 单位id
     */
    private String corpId;
    private String corpName;
    /**
     * 币种
     */
    private String curId;
    /**
     * 业务流水号
     */
    private String billCode;
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public String getBillFlagName() {
        return billFlagName;
    }
    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }
}
