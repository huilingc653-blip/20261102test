package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author wanggl
 * @Date 2022-4-8
 * @Version 1.0
 */
public class LeaseRefundUpdateVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 租赁表ID
     */
    private String leaseId;
    /**
     * 退租日期
     */
    private Date refundDate;
    /**
     * 是否提前退租
     */
    private String isAdvanceBack;
    /**
     * 备注
     */
    private String refundRmk;
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
     * 境内/境外
     */
    @Dict(dictGroup = "btBankAcc.isDomesticBank", inject = "isDomesticName")
    private String isDomestic;
    private String isDomesticName;
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
     * 出租人
     */
    @BtCompositeSearch(fieldName = "linfo.lessor")
    private String lessor;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 租赁物
     */
    private String leaseItem;
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
     * 内含报酬率
     */
    private BigDecimal incRate;
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
    /**
     * 租赁期限
     */
    private Integer dayLimit;
    /**
     * 期限单位
     */
    private String ternUnit;
    /**
     * 到期处理
     */
    private String dueProcess;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 展期到期日期
     */
    private Date expendedDate;
    /**
     * 状态
     */
    private Integer status;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public Date getRefundDate() {
        return refundDate;
    }
    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }
    public String getIsAdvanceBack() {
        return isAdvanceBack;
    }
    public void setIsAdvanceBack(String isAdvanceBack) {
        this.isAdvanceBack = isAdvanceBack;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRefundRmk() {
        return refundRmk;
    }
    public void setRefundRmk(String refundRmk) {
        this.refundRmk = refundRmk;
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
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }
    public String getIsDomesticName() {
        return isDomesticName;
    }
    public void setIsDomesticName(String isDomesticName) {
        this.isDomesticName = isDomesticName;
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
    public String getLeaseItem() {
        return leaseItem;
    }
    public void setLeaseItem(String leaseItem) {
        this.leaseItem = leaseItem;
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
    public BigDecimal getIncRate() {
        return incRate;
    }
    public void setIncRate(BigDecimal incRate) {
        this.incRate = incRate;
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
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getDueProcess() {
        return dueProcess;
    }
    public void setDueProcess(String dueProcess) {
        this.dueProcess = dueProcess;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Date getExpendedDate() {
        return expendedDate;
    }
    public void setExpendedDate(Date expendedDate) {
        this.expendedDate = expendedDate;
    }
}
