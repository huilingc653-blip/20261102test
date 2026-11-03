package com.bytter.biz.finacial.interest.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.finance.dto.CmsLoanRepayMoneyDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class ListFcAccInterestVO implements Serializable {
    /**
     * 户名
     **/
    private String accName;
    /**
     * 币别代码
     */
    private String curCode;
    private String curName;
    /**
     * 结息日期
     **/
    @BtCompositeSearch(fieldName = "d.bln_inst_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date blnInstDate;
    /**
     * 计息日期
     **/
    @BtCompositeSearch(fieldName = "d.interest_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestTime;
    /**
     * 起息日期
     **/
    @BtCompositeSearch(fieldName = "d.interest_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**
     * 天数
     */
    private Integer days;
    /**
     * 积数
     */
    private BigDecimal cumulation;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 网点
     **/
    @BtCompositeSearch(fieldName = "d.net_id")
    private String netId;
    /**
     * 单位
     **/
    @BtCompositeSearch(fieldName = "d.corp_id")
    private String corpId;
    /**
     * 开户种类
     **/
    @BtCompositeSearch(fieldName = "d.acc_type")
    private String accType;
    private String id;
    /**
     * 结算中心账号
     */
//    @BtCompositeSearch(fieldName = "a.BLNCTR_ACC")
    private String blnctrAcc;
    /**
     * 币别代码
     */
    private String curId;
    /**
     * 科目代码
     */
    private String subjectId;
    private String subjectName;
    /**
     * 计息类型
     */
    private String interestType;
    /**
     * 计息方式
     */
    private String generateMode;
    /**
     * 账户属性
     */
    private String attributeId;
    /**
     * 账户性质
     */
    private String natureCodeId;
    /**
     * 银行代码
     */
    private String bankCode;
    /**
     * 余额方向
     */
    private Integer balanceWay;
    /**
     * 透支标志
     */
    private String overDraftSign;
    /**
     * 透支利率
     */
    private BigDecimal overDraftRate;
    /**
     * 透支金额
     */
    private BigDecimal overDraftMoney;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**
     * 展期到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date delayDeadlineDate;
    /**
     * 逾期利率
     */
    private Integer overdueDate;
    /**
     * 扣息活期存款账户
     */
    private String accForDeduct;
    /**
     * 扣息方式
     */
    private String deductMode;
    /**
     * 通知类型(期限)
     */
    private Integer notifyType;
    /**
     * 账户状态
     */
    private String accState;
    /**
     * 协定存款标志
     */
    private String agreedDepositSign;
    /**
     * 预提标志
     */
    private String predrawSign;
    /**
     * 是否产生对账数据
     */
    private String ifCompare;
    /**
     * 往来账号
     */
    private String accId;
    /**
     * 账号启用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accStartDate;
    /**
     * 经办人代码
     */
    private String operatorCode;
    private String operatorName;
    /**
     * 开户日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accOpenTime;
    /**
     * 复核员代码
     */
    private String checkerCode;
    private String checkerName;
    /**
     * 复核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 行业代码
     */
    private String vocationCode;
    /**
     * 助记码
     */
    private String shortcui;
    /**
     * 联行网点
     */
    private String unionNet;
    /**
     * 展期利率
     */
    private BigDecimal delayRate;
    /**
     * 联行账号
     */
    private String unionAcc;
    /**
     * 委托贷款帐号
     */
    private String precationAcc;
    /**
     * 委托贷款方
     */
    private String precationCorp;
    /**
     * 委托存款的手续费率
     */
    private BigDecimal poundAgeRate;
    /**
     * 手续费收取账户
     */
    private String chargeAcc;
    /**
     * 办理方式
     */
    private Integer transactWay;
    /**
     * 销户日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cancelDate;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 打回原因
     */
    private String checkBackReason;
    /**
     * 到期单位
     */
    private String endUnit;
    /**
     * 到期续存方式
     */
    private Integer certificateWay;
    /**
     * 是否收取手续费
     */
    private String handleCharge;
    /**
     * 贷款账号
     */
    private String accCussent;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 最后更新时间
     */
    private Date updateDate;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 借
     */
    private String borrow;
    /**
     * 贷
     */
    private String loan;
    private String interestSign;
    /**
     * 流水号
     */
    private String orderNo;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 放款户
     */
    private CmsLoanRepayMoneyDTO repayMoneyDTO;
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getBorrow() {
        return borrow;
    }
    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }
    public String getLoan() {
        return loan;
    }
    public void setLoan(String loan) {
        this.loan = loan;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
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
    public Date getBlnInstDate() {
        return blnInstDate;
    }
    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }
    public BigDecimal getCumulation() {
        return cumulation;
    }
    public void setCumulation(BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    public String getNatureCodeId() {
        return natureCodeId;
    }
    public void setNatureCodeId(String natureCodeId) {
        this.natureCodeId = natureCodeId;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getOverDraftSign() {
        return overDraftSign;
    }
    public void setOverDraftSign(String overDraftSign) {
        this.overDraftSign = overDraftSign;
    }
    public BigDecimal getOverDraftRate() {
        return overDraftRate;
    }
    public void setOverDraftRate(BigDecimal overDraftRate) {
        this.overDraftRate = overDraftRate;
    }
    public BigDecimal getOverDraftMoney() {
        return overDraftMoney;
    }
    public void setOverDraftMoney(BigDecimal overDraftMoney) {
        this.overDraftMoney = overDraftMoney;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public Date getDelayDeadlineDate() {
        return delayDeadlineDate;
    }
    public void setDelayDeadlineDate(Date delayDeadlineDate) {
        this.delayDeadlineDate = delayDeadlineDate;
    }
    public Integer getOverdueDate() {
        return overdueDate;
    }
    public void setOverdueDate(Integer overdueDate) {
        this.overdueDate = overdueDate;
    }
    public String getAccForDeduct() {
        return accForDeduct;
    }
    public void setAccForDeduct(String accForDeduct) {
        this.accForDeduct = accForDeduct;
    }
    public String getDeductMode() {
        return deductMode;
    }
    public void setDeductMode(String deductMode) {
        this.deductMode = deductMode;
    }
    public Integer getNotifyType() {
        return notifyType;
    }
    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }
    public String getAccState() {
        return accState;
    }
    public void setAccState(String accState) {
        this.accState = accState;
    }
    public String getAgreedDepositSign() {
        return agreedDepositSign;
    }
    public void setAgreedDepositSign(String agreedDepositSign) {
        this.agreedDepositSign = agreedDepositSign;
    }
    public String getPredrawSign() {
        return predrawSign;
    }
    public void setPredrawSign(String predrawSign) {
        this.predrawSign = predrawSign;
    }
    public String getIfCompare() {
        return ifCompare;
    }
    public void setIfCompare(String ifCompare) {
        this.ifCompare = ifCompare;
    }
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
    }
    public Date getAccStartDate() {
        return accStartDate;
    }
    public void setAccStartDate(Date accStartDate) {
        this.accStartDate = accStartDate;
    }
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public Date getAccOpenTime() {
        return accOpenTime;
    }
    public void setAccOpenTime(Date accOpenTime) {
        this.accOpenTime = accOpenTime;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public Date getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getVocationCode() {
        return vocationCode;
    }
    public void setVocationCode(String vocationCode) {
        this.vocationCode = vocationCode;
    }
    public String getShortcui() {
        return shortcui;
    }
    public void setShortcui(String shortcui) {
        this.shortcui = shortcui;
    }
    public String getUnionNet() {
        return unionNet;
    }
    public void setUnionNet(String unionNet) {
        this.unionNet = unionNet;
    }
    public BigDecimal getDelayRate() {
        return delayRate;
    }
    public void setDelayRate(BigDecimal delayRate) {
        this.delayRate = delayRate;
    }
    public String getUnionAcc() {
        return unionAcc;
    }
    public void setUnionAcc(String unionAcc) {
        this.unionAcc = unionAcc;
    }
    public String getPrecationAcc() {
        return precationAcc;
    }
    public void setPrecationAcc(String precationAcc) {
        this.precationAcc = precationAcc;
    }
    public String getPrecationCorp() {
        return precationCorp;
    }
    public void setPrecationCorp(String precationCorp) {
        this.precationCorp = precationCorp;
    }
    public BigDecimal getPoundAgeRate() {
        return poundAgeRate;
    }
    public void setPoundAgeRate(BigDecimal poundAgeRate) {
        this.poundAgeRate = poundAgeRate;
    }
    public String getChargeAcc() {
        return chargeAcc;
    }
    public void setChargeAcc(String chargeAcc) {
        this.chargeAcc = chargeAcc;
    }
    public Integer getTransactWay() {
        return transactWay;
    }
    public void setTransactWay(Integer transactWay) {
        this.transactWay = transactWay;
    }
    public Date getCancelDate() {
        return cancelDate;
    }
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getCheckBackReason() {
        return checkBackReason;
    }
    public void setCheckBackReason(String checkBackReason) {
        this.checkBackReason = checkBackReason;
    }
    public String getEndUnit() {
        return endUnit;
    }
    public void setEndUnit(String endUnit) {
        this.endUnit = endUnit;
    }
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
    public String getHandleCharge() {
        return handleCharge;
    }
    public void setHandleCharge(String handleCharge) {
        this.handleCharge = handleCharge;
    }
    public String getAccCussent() {
        return accCussent;
    }
    public void setAccCussent(String accCussent) {
        this.accCussent = accCussent;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getInterestTime() {
        return interestTime;
    }
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public String getCheckerName() {
        return checkerName;
    }
    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }
    public String getInterestSign() {
        return interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getInterestType() {
        return interestType;
    }
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
    public String getGenerateMode() {
        return generateMode;
    }
    public void setGenerateMode(String generateMode) {
        this.generateMode = generateMode;
    }
    public CmsLoanRepayMoneyDTO getRepayMoneyDTO() {
        return repayMoneyDTO;
    }
    public void setRepayMoneyDTO(CmsLoanRepayMoneyDTO repayMoneyDTO) {
        this.repayMoneyDTO = repayMoneyDTO;
    }
}
