package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class LeaseListUpdateVO implements Serializable {
    private String id;
    /**
     * 变更表ID
     */
    private String updateId;
    /**
     * 变更状态
     */
    private Integer modifystatus;
    /**
     * 补充合同编号
     */
    private String addLeaseCode;
    /**
     * 变更时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date modifyDate;
    /**
     * 融资租赁单号
     */
    @BtCompositeSearch(fieldName = "linfo.lease_no")
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    @BtCompositeSearch(fieldName = "linfo.lease_type")
    private String leaseType;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "linfo.corp_id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "linfo.lessor")
    private String lessor;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 融资租赁合同编号
     */
    @BtCompositeSearch(fieldName = "linfo.lease_code")
    private String leaseCode;
    /**
     * 买卖合同编号
     */
    @BtCompositeSearch(fieldName = "linfo.pay_code")
    private String payCode;
    /**
     * 原币币种
     */
    @Currency(inject = "oriCurName")
    @BtCompositeSearch(fieldName = "linfo.ori_curid")
    private String oriCurid;
    private String oriCurName;
    /**
     * 原币金额
     */
    @BtCompositeSearch(fieldName = "linfo.ori_money")
    private BigDecimal oriMoney;
    /**
     * 人名币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 起租日
     */
    @BtCompositeSearch(fieldName = "linfo.begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 到期日
     */
    @BtCompositeSearch(fieldName = "linfo.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String status;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Integer getModifystatus() {
        return modifystatus;
    }
    public void setModifystatus(Integer modifystatus) {
        this.modifystatus = modifystatus;
    }
    public String getAddLeaseCode() {
        return addLeaseCode;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    public void setAddLeaseCode(String addLeaseCode) {
        this.addLeaseCode = addLeaseCode;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
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
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    public String getOriCurid() {
        return oriCurid;
    }
    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }
    public String getOriCurName() {
        return oriCurName;
    }
    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }
    public BigDecimal getOriMoney() {
        return oriMoney;
    }
    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
    }
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
