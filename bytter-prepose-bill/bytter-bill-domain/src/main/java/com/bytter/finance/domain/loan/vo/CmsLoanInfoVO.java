package com.bytter.finance.domain.loan.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsLoanInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /**
     * 结算中心代码
     */
    @BtCompositeSearch(fieldName = "i.net_code")
//    @Corp(inject = "netName")
    private String netCode;
    @Excel(name = "*所属资金中心")
    private String netName;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "i.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    @Excel(name = "*申请单位")
    private String corpName;
    /**
     * 贷款流水号
     */
    @BtCompositeSearch(fieldName = "i.BILL_CODE")
    private String billCode;
    /**
     * 借款合同号
     */
    private String contractCode;
    /**
     * 贷款金额
     */
    @BtCompositeSearch(fieldName = "i.loan_money")
    @Excel(name = "*贷款金额")
    private BigDecimal loanMoney;
    /**
     * 贷款期限
     */
    @BtCompositeSearch(fieldName = "i.loan_term")
    private Integer loanTerm;
    /**
     * 期限单位
     */
    private String ternUnit;
    /**
     * 放款币种
     */
    @BtCompositeSearch(fieldName = "i.cur_code")
//    @Currency(inject = "curName")
    private String curCode;
    @Excel(name = "*贷款币别")
    private String curName;
    /**
     * 贷款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.LOAN_DATE")
    @Excel(name = "*贷款日期")
    private java.util.Date loanDate;
    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.REPAY_DATE")
    @Excel(name = "*贷款到期日")
    private java.util.Date repayDate;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "i.repay_type")
    private String repayType;
    @Excel(name = "*还款方式")
    private String repayTypeName;
    /**
     * 贷款种类
     */
    @Dict(dictGroup = "cms.LoanClass", inject = "loanClassName")
    private String loanClass;
    /**
     * 贷款种类名称
     */
    private String loanClassName;
    /**
     * 贷款类型
     */
    private String loanGenreId;
    private String loanGenre;
    /**
     * 贷款类型名称
     */
    @Excel(name = "*贷款类型")
    private String loanGenreName;
    /**
     * 担保方式
     */
    private String loanType;
    /**
     * 担保说明
     */
    private String suretyExplain;
    /**
     * 担保品价值
     */
    private BigDecimal suretyMoney;
    /**
     * 放款账号
     */
    private String provideLoanAcc;
    /**
     * 放款利率
     */
    @Excel(name = "贷款利率")
    private BigDecimal loanRate;
    /**
     * 逾期利率
     */
    @Excel(name = "逾期利率")
    private BigDecimal overdueRate;
    /**
     * 展期利率
     */
    @Excel(name = "展期利率")
    private BigDecimal deferredRate;
    /**
     * 补录员
     */
    private String repairOp;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 借款级次
     */
    private String loanGrade;
    /**
     * 用途
     */
    @Excel(name = "*用途")
    private String purpose;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 打回原因
     */
    private String refuseWhy;
    /**
     * 是否挂授信
     */
    private String isHangCredit;
    /**
     * 额度编号
     */
    private String rationCode;
    /**
     * 授信额度品种代码
     */
    private String itemCode;
    /**
     * 贷款发放方式
     */
    @BtCompositeSearch(fieldName = "i.loan_send_out_type")
    private String loanSendOutType;
    /**
     * 浮动方式
     */
    private String floatWay;
    /**
     * 浮动方式名称
     */
    @Excel(name = "浮动方式")
    private String floatWayName;
    /**
     * 点数或浮动比例
     */
    private String pointsProportionText;
    @Excel(name = "BP点数或比例")
    private BigDecimal pointsProportion;
    /**
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /**
     * 利率调整生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    // @Excel(name = "利率报价日期")
    private java.util.Date rateEntriesEffectDate;
    /**
     * 计息方式
     */
    private String interestWay;
    /**
     * 基准利率类型
     */
    private String referenceRateType;
    @Excel(name = "利率类型")
    private String referenceRateTypeName;
    /**
     * 贷款周期
     */
    private String repayCycle;
    @Excel(name = "利率期限")
    private String repayCycleName;
    /**
     * 利率复核生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date ratetimeoftakeeffect;
    /**
     * 发放次数
     */
    private Integer sendTime;
    /**
     * 已发放金额
     */
    @BtCompositeSearch(fieldName = "b.send_out_money")
    private BigDecimal sendMoney;
    /**
     * 协定费率
     */
    private BigDecimal clbAgreementRate;
    /**
     * 借款单位
     */
    private String borrowCorpId;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 固定费率
     */
    private BigDecimal clbFixedRate;
    /**
     * 借贷关联的借款单号
     */
    private String ploadcode;
    /**
     * 是否自动还款
     */
    @Excel(name = "是否自动还款")
    private String isAutoRepay;
    /**
     * 是否跳过发放
     */
    @Excel(name = "是否跳过发放功能")
    private String isOverDistribute;
    /**
     * 是否跳过补录
     */
    @Excel(name = "*是否跳过补录")
    private String isOverRepair;
    /**
     * 行业代码
     */
    private String industryCode;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.CREATE_DATE")
    private java.util.Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 浮动二级方式
     **/
    private String floatWayNext;
    /**
     * 浮动二级方式
     **/
    @Excel(name = "浮动调整日期")
    private String floatWayNextName;
    /**
     * 发放表billCode
     **/
    private String sendBillCode;
    /**
     * 是否挂接担保
     **/
    public String isHangGuarantee;
    /**
     * 担保合同
     **/
    public String guaContract;
    /**
     * 担保方式
     **/
    public String guaWay;
    /**
     * 还款计划Id
     */
    private String planId;
    /**
     * 还款计划status
     */
    private String planStatus;
    /**
     * 担保合同id
     */
    private List<String> contractIds;
    /**
     * 担保备注
     **/
    public String guaranteeRmk;
    /**
     * 项目名称
     */
    private String proName;
    /**
     * 对接的合同类型id
     */
    private String conTypeId;
    /**
     * 对接的合同id
     */
    private String conId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIsAutoRepay() {
        return isAutoRepay;
    }
    public void setIsAutoRepay(String isAutoRepay) {
        this.isAutoRepay = isAutoRepay;
    }
    public String getPlanStatus() {
        return planStatus;
    }
    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }
    public BigDecimal getPointsProportion() {
        return pointsProportion;
    }
    public void setPointsProportion(BigDecimal pointsProportion) {
        this.pointsProportion = pointsProportion;
    }
    public String getIsOverDistribute() {
        return isOverDistribute;
    }
    public void setIsOverDistribute(String isOverDistribute) {
        this.isOverDistribute = isOverDistribute;
    }
    public String getIsOverRepair() {
        return isOverRepair;
    }
    public void setIsOverRepair(String isOverRepair) {
        this.isOverRepair = isOverRepair;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    @Override
    public String getBeanName() {
        return "cmsLoanInfoApplyService";
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBillCode() {
        return billCode;
    }
    public String getFloatWayNextName() {
        return floatWayNextName;
    }
    public void setFloatWayNextName(String floatWayNextName) {
        this.floatWayNextName = floatWayNextName;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public Integer getLoanTerm() {
        return loanTerm;
    }
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public java.util.Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(java.util.Date loanDate) {
        this.loanDate = loanDate;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public String getLoanClassName() {
        return loanClassName;
    }
    public void setLoanClassName(String loanClassName) {
        this.loanClassName = loanClassName;
    }
    public String getLoanGenreId() {
        return loanGenreId;
    }
    public void setLoanGenreId(String loanGenreId) {
        this.loanGenreId = loanGenreId;
    }
    public String getLoanGenre() {
        return loanGenre;
    }
    public void setLoanGenre(String loanGenre) {
        this.loanGenre = loanGenre;
    }
    public String getLoanGenreName() {
        return loanGenreName;
    }
    public void setLoanGenreName(String loanGenreName) {
        this.loanGenreName = loanGenreName;
    }
    public String getLoanType() {
        return loanType;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    public String getSuretyExplain() {
        return suretyExplain;
    }
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    public BigDecimal getSuretyMoney() {
        return suretyMoney;
    }
    public void setSuretyMoney(BigDecimal suretyMoney) {
        this.suretyMoney = suretyMoney;
    }
    public String getProvideLoanAcc() {
        return provideLoanAcc;
    }
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
    }
    public BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public String getRepairOp() {
        return repairOp;
    }
    public void setRepairOp(String repairOp) {
        this.repairOp = repairOp;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getLoanGrade() {
        return loanGrade;
    }
    public void setLoanGrade(String loanGrade) {
        this.loanGrade = loanGrade;
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
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(String pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public java.util.Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(java.util.Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getReferenceRateTypeName() {
        return referenceRateTypeName;
    }
    public void setReferenceRateTypeName(String referenceRateTypeName) {
        this.referenceRateTypeName = referenceRateTypeName;
    }
    public String getRepayCycleName() {
        return repayCycleName;
    }
    public void setRepayCycleName(String repayCycleName) {
        this.repayCycleName = repayCycleName;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public java.util.Date getRatetimeoftakeeffect() {
        return ratetimeoftakeeffect;
    }
    public void setRatetimeoftakeeffect(java.util.Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getClbAgreementRate() {
        return clbAgreementRate;
    }
    public void setClbAgreementRate(BigDecimal clbAgreementRate) {
        this.clbAgreementRate = clbAgreementRate;
    }
    public String getBorrowCorpId() {
        return borrowCorpId;
    }
    public void setBorrowCorpId(String borrowCorpId) {
        this.borrowCorpId = borrowCorpId;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public BigDecimal getClbFixedRate() {
        return clbFixedRate;
    }
    public void setClbFixedRate(BigDecimal clbFixedRate) {
        this.clbFixedRate = clbFixedRate;
    }
    public String getPloadcode() {
        return ploadcode;
    }
    public void setPloadcode(String ploadcode) {
        this.ploadcode = ploadcode;
    }
    public String getIndustryCode() {
        return industryCode;
    }
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getBusinessKey() {
        return getId();
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public List<String> getContractIds() {
        return contractIds;
    }
    public void setContractIds(List<String> contractIds) {
        this.contractIds = contractIds;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getFloatWayName() {
        return floatWayName;
    }
    public void setFloatWayName(String floatWayName) {
        this.floatWayName = floatWayName;
    }
    public String getProName() {
        return proName;
    }
    public void setProName(String proName) {
        this.proName = proName;
    }
}
