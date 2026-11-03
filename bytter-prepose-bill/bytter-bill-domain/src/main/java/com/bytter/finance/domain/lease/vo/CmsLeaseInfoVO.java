package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CmsLeaseInfoVO implements Serializable, IBusinessKey {
    private String id;
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
     * 承租人
     */
    @BtCompositeSearch(fieldName = "linfo.corp_id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
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
     * 出租人
     */
    @BtCompositeSearch(fieldName = "linfo.lessor")
    private String lessor;
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
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 展期次数
     */
    private Integer expendedCount;
    private LeaseExtendVO extend;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOriCurid() {
        return oriCurid;
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
    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }
    public Integer getExpendedCount() {
        return expendedCount;
    }
    public void setExpendedCount(Integer expendedCount) {
        this.expendedCount = expendedCount;
    }
    public LeaseExtendVO getExtend() {
        return extend;
    }
    public void setExtend(LeaseExtendVO extend) {
        this.extend = extend;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
