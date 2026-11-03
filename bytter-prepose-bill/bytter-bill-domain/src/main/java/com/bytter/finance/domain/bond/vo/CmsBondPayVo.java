package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 债券还款表
 * @date 2022-06-07 10:07:46
 */
@SuppressWarnings("serial")
public class CmsBondPayVo implements Serializable, IBusinessKey {
    private String id;
    /**
     * 所属单位
     */
    @BtCompositeSearch(fieldName = "r.corp_id")
    private String corpId;
    private String corpName;
    /**
     * 债劵注册id
     */
    private String registerId;
    /**
     * 债劵发行id
     */
    private String issueId;
    /**
     * 债券还款计划主表id
     */
    private String planId;
    /**
     * 债券还款计划子表id
     */
    private String planDtlId;
    /**
     * 债券类型
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String typeId;
    /**
     * 债券类型名称
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String bondTypeName;
    /**
     * 债券代码
     */
    @BtCompositeSearch(fieldName = "r.bond_code")
    private String bondCode;
    /**
     * 债券全称
     */
    @BtCompositeSearch(fieldName = "r.bond_full_name")
    private String bondFullName;
    /**
     * 债劵简称
     */
    @BtCompositeSearch(fieldName = "r.bond_short_name")
    private String bondShortName;
    /**
     * 主承销商全称
     */
    private String leadUnderwriter;
    /**
     * 债券发行编号
     */
    private String bondIssueNum;
    /**
     * 发行金额
     */
    private BigDecimal issueMoney;
    /**
     * 收款账号
     */
    private String onlinePaymentAcc;
    /**
     * 收款账户名称
     */
    private String onlineAccName;
    /**
     * 收款人开户行行号
     */
    private String onlineBankCode;
    /**
     * 收款账户开户行名称
     */
    private String onlineBankName;
    /**
     * 状态
     */
//    @Dict(dictGroup = "lease.status", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 支付订单号
     */
    private String payNo;
    /**
     * 支付方式
     */
    private String payType;
    private String payTypeName;
    /**
     * 线下线上还款标识
     */
    @Dict(dictGroup = "cms.repay.online.sign", inject = "onlineSignName")
    private String onlineSign;
    private String onlineSignName;
    /**
     * 债券原币币种
     */
//    @Currency(inject = "bondCurName")
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCur;
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCurName;
    /**
     * 计划发行原币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_cur_total")
    private BigDecimal issueCurTotal;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 支付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 还款本金
     */
    private BigDecimal repayMoney;
    /**
     * 还款总金额
     */
    private BigDecimal repayTotalMoney;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 是否付息
     */
    @Dict(dictGroup = "sf_yn", inject = "isPayInterestName")
    private String isPayInterest;
    private String isPayInterestName;
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
    private  String saveType;
    private  String tempOrSubmitSign;
    private String sign;

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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getPlanDtlId() {
        return planDtlId;
    }
    public void setPlanDtlId(String planDtlId) {
        this.planDtlId = planDtlId;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    public String getBondFullName() {
        return bondFullName;
    }
    public void setBondFullName(String bondFullName) {
        this.bondFullName = bondFullName;
    }
    public String getBondShortName() {
        return bondShortName;
    }
    public void setBondShortName(String bondShortName) {
        this.bondShortName = bondShortName;
    }
    public String getLeadUnderwriter() {
        return leadUnderwriter;
    }
    public void setLeadUnderwriter(String leadUnderwriter) {
        this.leadUnderwriter = leadUnderwriter;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
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
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    public String getOnlineSignName() {
        return onlineSignName;
    }
    public void setOnlineSignName(String onlineSignName) {
        this.onlineSignName = onlineSignName;
    }
    public String getBondCur() {
        return bondCur;
    }
    public void setBondCur(String bondCur) {
        this.bondCur = bondCur;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public BigDecimal getIssueCurTotal() {
        return issueCurTotal;
    }
    public void setIssueCurTotal(BigDecimal issueCurTotal) {
        this.issueCurTotal = issueCurTotal;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRepayTotalMoney() {
        return repayTotalMoney;
    }
    public void setRepayTotalMoney(BigDecimal repayTotalMoney) {
        this.repayTotalMoney = repayTotalMoney;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public String getIsPayInterest() {
        return isPayInterest;
    }
    public void setIsPayInterest(String isPayInterest) {
        this.isPayInterest = isPayInterest;
    }
    public String getIsPayInterestName() {
        return isPayInterestName;
    }
    public void setIsPayInterestName(String isPayInterestName) {
        this.isPayInterestName = isPayInterestName;
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
        return getId();
    }
}
