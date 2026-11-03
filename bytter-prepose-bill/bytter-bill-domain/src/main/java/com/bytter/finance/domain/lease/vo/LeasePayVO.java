package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class LeasePayVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 租赁表id
     */
    private String leaseId;
    /**
     * 租金计划主表id
     */
    private String leasePlanId;
    /**
     * 租金计划子表id
     */
    private String leasePlanDelId;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "l.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
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
     * 还款开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayBeginDate;
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
     * 收款账号
     */
    private String onlinePaymentAcc;
    /**
     * 收款账户名称
     */
    private String onlineAccName;
    /**
     * 收款账户开户行行号
     */
    private String onlineBankCode;
    /**
     * 收款账户开户行名称
     */
    private String onlineBankName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 支付方式
     */
    private String onlineSign;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 租金支付日期
     */
    private Date payDate;
    /**
     * 本金
     */
    private BigDecimal money;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 租金
     */
    private BigDecimal leaseMoney;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    private BigDecimal rmbMoney;

    private  String saveType;
    private String tempOrSubmitSign;
    private String sign;


    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }

    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }

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
    public Date getRepayBeginDate() {
        return repayBeginDate;
    }
    public void setRepayBeginDate(Date repayBeginDate) {
        this.repayBeginDate = repayBeginDate;
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
    public String getLeasePlanId() {
        return leasePlanId;
    }
    public void setLeasePlanId(String leasePlanId) {
        this.leasePlanId = leasePlanId;
    }
    public String getLeasePlanDelId() {
        return leasePlanDelId;
    }
    public void setLeasePlanDelId(String leasePlanDelId) {
        this.leasePlanDelId = leasePlanDelId;
    }
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    public String getOnlineBankCode() {
        return onlineBankCode;
    }
    public void setOnlineBankCode(String onlineBankCode) {
        this.onlineBankCode = onlineBankCode;
    }
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public BigDecimal getLeaseMoney() {
        return leaseMoney;
    }
    public void setLeaseMoney(BigDecimal leaseMoney) {
        this.leaseMoney = leaseMoney;
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
    @Override
    public String getBusinessKey() {
        return id;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getTempOrSubmitSign() {
        return tempOrSubmitSign;
    }

    public void setTempOrSubmitSign(String tempOrSubmitSign) {
        this.tempOrSubmitSign = tempOrSubmitSign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
