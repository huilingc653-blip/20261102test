package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsLeaseContinueInfoVO implements Serializable, IBusinessKey {
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
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "linfo.lessor")
    private String lessor;
    /**
     * 原币币种
     */
    @BtCompositeSearch(fieldName = "linfo.ori_curid")
    private String oriCurid;
    /**
     * 原币币别
     */
    private String oriCurName;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 退租状态
     */
    private Integer refundStatus;
    /**
     * 回购状态
     */
    private Integer repoStatus;
    /**
     * 续租次数
     */
    private Integer continueCount;
    /**
     * 币种
     */
    private String curName;
    /**
     * 租金金额
     */
    @BtCompositeSearch(fieldName = "linfo.ORI_MONEY")
    private BigDecimal oriMoney;
    /**
     * 租金人民币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 起租日
     */
    @BtCompositeSearch(fieldName = "linfo.BEGIN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 到期日
     */
    @BtCompositeSearch(fieldName = "linfo.END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private LeaseContinueVO continueVO;
    public Integer getRefundStatus() {
        return refundStatus;
    }
    public Integer getRepoStatus() {
        return repoStatus;
    }
    public void setRepoStatus(Integer repoStatus) {
        this.repoStatus = repoStatus;
    }
    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }
    public Integer getContinueCount() {
        return continueCount;
    }
    public void setContinueCount(Integer continueCount) {
        this.continueCount = continueCount;
    }
    public String getOriCurid() {
        return oriCurid;
    }
    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }
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
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public LeaseContinueVO getContinueVO() {
        return continueVO;
    }
    public void setContinueVO(LeaseContinueVO continueVO) {
        this.continueVO = continueVO;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getOriMoney() {
        return oriMoney;
    }
    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
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
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getOriCurName() {
        return oriCurName;
    }
    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }
}
