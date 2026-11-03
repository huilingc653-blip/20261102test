package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class LeaseCapitalPlanVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 租赁表id
     */
    private String leaseId;
    /**
     * 融资租赁合同编号
     */
    @BtCompositeSearch(fieldName = "l.lease_code")
    private String leaseCode;
    /**
     * 融资租赁单号
     */
    @BtCompositeSearch(fieldName = "l.lease_no")
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    @BtCompositeSearch(fieldName = "l.lease_type")
    @Dict(dictGroup = "cms.financing.lease.info.leaseType", inject = "leaseTypeName")
    private String leaseType;
    private String leaseTypeName;
    /**
     * 买卖合同编号
     */
    @BtCompositeSearch(fieldName = "l.pay_code")
    private String payCode;
    /**
     * 境内/境外
     */
    private String isDomestic;
    /**
     * 租赁物
     */
    private String leaseItem;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "l.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "l.lessor")
    private String lessor;
    /**
     * 供货商
     */
    @BtCompositeSearch(fieldName = "l.supplier")
    private String supplier;
    /**
     * 原币币种
     */
    @BtCompositeSearch(fieldName = " l.ori_curid")
//    @Currency(inject = "oriCurName")
    private String oriCurid;
    private String oriCurName;
    /**
     * 原币金额
     */
    @BtCompositeSearch(fieldName = "l.ori_money")
    private BigDecimal oriMoney;
    /**
     * 人民币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 还款开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayBeginDate;
    /**
     * 还款周期
     */
    private String repayCycel;
    /**
     * 付款方式
     */
    private String repayType;
    private String repayTypeName;
    /**
     * 起租日
     */
    @BtCompositeSearch(fieldName = "l.begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 到期日
     */
    @BtCompositeSearch(fieldName = " l.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 利率期限
     */
    private String repayCycle;
    /**
     * 支付方式
     */
    private String payWay;
    /**
     * 支付金额
     */
    private BigDecimal payMoney;
    /**
     * 状态
     */
    private Integer status;
    private String statusName;
    /**
     * 租赁状态
     */
    private Integer leaseStatus;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 是否保存操作
     */
    private String isSaveSign;
    /**
     * 操作类型(add/update)
     */
    private String operator;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 租金计划明细
     */
    private List<LeaseCapitalPlanDelVO> leaseCapitalPlanDels;
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
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
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
    public String getLeaseTypeName() {
        return leaseTypeName;
    }
    public void setLeaseTypeName(String leaseTypeName) {
        this.leaseTypeName = leaseTypeName;
    }
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }
    public String getLeaseItem() {
        return leaseItem;
    }
    public void setLeaseItem(String leaseItem) {
        this.leaseItem = leaseItem;
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
    public Date getRepayBeginDate() {
        return repayBeginDate;
    }
    public void setRepayBeginDate(Date repayBeginDate) {
        this.repayBeginDate = repayBeginDate;
    }
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
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
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
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
    public Integer getLeaseStatus() {
        return leaseStatus;
    }
    public void setLeaseStatus(Integer leaseStatus) {
        this.leaseStatus = leaseStatus;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getIsSaveSign() {
        return isSaveSign;
    }
    public void setIsSaveSign(String isSaveSign) {
        this.isSaveSign = isSaveSign;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public List<LeaseCapitalPlanDelVO> getLeaseCapitalPlanDels() {
        return leaseCapitalPlanDels;
    }
    public void setLeaseCapitalPlanDels(List<LeaseCapitalPlanDelVO> leaseCapitalPlanDels) {
        this.leaseCapitalPlanDels = leaseCapitalPlanDels;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
