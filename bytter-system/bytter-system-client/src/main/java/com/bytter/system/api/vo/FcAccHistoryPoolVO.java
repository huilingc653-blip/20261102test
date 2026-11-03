package com.bytter.system.api.vo;
import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
public class FcAccHistoryPoolVO implements Serializable {
    private String id;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 结算中心账号
     */
    @BtCompositeSearch(fieldName = "blnctrAcc")
    private String blnctrAcc;
    /**
     * 账户名称
     */
    @BtCompositeSearch(fieldName = "accName")
    private String accName;
    /**
     * 网点
     */
    @BtCompositeSearch(fieldName = "netId")
    @CorpPool(inject = "netName")
    private String netId;
    @BtCompositeSearch(fieldName = "netId")
    private String netName;
    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "corpId")
    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "corpId")
    private String corpName;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "curId")
    @Currency(inject = "curName")
    private String curId;
    @BtCompositeSearch(fieldName = "curId")
    private String curName;

    /**
     * 科目名称
     */
//    @Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
    @BtCompositeSearch(fieldName = "subjectId")
    @Dict(tableName="FC_SUBJECT_POOL",valueFieldName="ID",showFiledName="SUBJECT_NAME", inject = "subjectName")
    private String subjectId;
    @BtCompositeSearch(fieldName = "subjectId")
    private String subjectName;
    /**
     * 账户类型
     */
    @BtCompositeSearch(fieldName = "accType")
    @Dict(dictGroup = "InnerAccount.AccType", inject = "accTypeName")
    private String accType;
    @BtCompositeSearch(fieldName = "accType")
    private String accTypeName;
    /**
     * 余额方向
     */
    @BtCompositeSearch(fieldName = "balanceWay")
    @Dict(dictGroup = "Admin.BalanceWay", inject = "balanceWayName")
    private Integer balanceWay;
    @BtCompositeSearch(fieldName = "balanceWay")
    private String balanceWayName;
    /**
     * 账户状态
     */
    @BtCompositeSearch(fieldName = "accState")
    @Dict(dictGroup = "inneraccount.fcStatus", inject = "accStateName")
    private String accState;
    @BtCompositeSearch(fieldName = "accState")
    private String accStateName;
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
    @Dict(tableName = "BT_INPUT_BANK_INFO", valueFieldName = "SYS_BANK_CODE", showFiledName = "BANK_NAME", inject = "bankName")
    private String bankCode;
    private String bankName;
    /**
     * 计息标志
     */
    private String interestSign;
    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 透支标志
     */
    @BtCompositeSearch(fieldName = "overdraftSign")
    private String overdraftSign;
    /**
     * 透支利率
     */
    private BigDecimal overdraftRate;
    /**
     * 透支金额
     */
    @BtCompositeSearch(fieldName = "overdraftMoney")
    private BigDecimal overdraftMoney;
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
    private BigDecimal overdueRate;
    /**
     * 扣息活期存款账户
     */
    private String accForDeduct;
    /**
     * 扣息方式
     */
    private String deductMode;
    /**
     * 通知类型
     */
    private Integer notifyType;
    /**
     * 协定存款标志
     */
    private String agreedDepositSign;
    /**
     * 预提标志
     */
    private String predrawSign;
    /**
     * 是否产生对账数据---对账标志
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
    /**
     * 开户日期
     */
    @BtCompositeSearch(fieldName = "accOpenTime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accOpenTime;
    /**
     * 复核员代码
     */
    private String checkerCode;
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
    private String shortcut;
    /**
     * 联行网点
     */
    @Corp(inject = "unionNetName")
    private String unionNet;
    private String unionNetName;
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
    @Corp(inject = "precationCorpName")
    private String precationCorp;
    private String precationCorpName;
    /**
     * 委托存款的手续费率
     */
    private BigDecimal poundageRate;
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
     * 延期合同编号
     */
    private String delayContractCode;
    /**
     * 是否收取手续费
     */
    private String handleCharge;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新人登录名称
     */

    private String updateBy;

    private Integer accAccNoRule;

    private String attributeName;

    private String natureName;
    /**
     * 贷款账号
     */

    private String accCussent;

    private String fcaccId;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    private String poolSign;
    public String getUnionNetName() {
        return unionNetName;
    }
    public void setUnionNetName(String unionNetName) {
        this.unionNetName = unionNetName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getBalanceWayName() {
        return balanceWayName;
    }
    public void setBalanceWayName(String balanceWayName) {
        this.balanceWayName = balanceWayName;
    }
    public String getAccState() {
        return accState;
    }
    public void setAccState(String accState) {
        this.accState = accState;
    }
    public String getAccStateName() {
        return accStateName;
    }
    public void setAccStateName(String accStateName) {
        this.accStateName = accStateName;
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
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getInterestSign() {
        return interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public String getOverdraftSign() {
        return overdraftSign;
    }
    public void setOverdraftSign(String overdraftSign) {
        this.overdraftSign = overdraftSign;
    }
    public BigDecimal getOverdraftRate() {
        return overdraftRate;
    }
    public void setOverdraftRate(BigDecimal overdraftRate) {
        this.overdraftRate = overdraftRate;
    }
    public BigDecimal getOverdraftMoney() {
        return overdraftMoney;
    }
    public void setOverdraftMoney(BigDecimal overdraftMoney) {
        this.overdraftMoney = overdraftMoney;
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
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
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
    public String getShortcut() {
        return shortcut;
    }
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
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
    public String getPrecationCorpName() {
        return precationCorpName;
    }
    public void setPrecationCorpName(String precationCorpName) {
        this.precationCorpName = precationCorpName;
    }
    public BigDecimal getPoundageRate() {
        return poundageRate;
    }
    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
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
    public String getDelayContractCode() {
        return delayContractCode;
    }
    public void setDelayContractCode(String delayContractCode) {
        this.delayContractCode = delayContractCode;
    }
    public String getHandleCharge() {
        return handleCharge;
    }
    public void setHandleCharge(String handleCharge) {
        this.handleCharge = handleCharge;
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
    public Integer getAccAccNoRule() {
        return accAccNoRule;
    }
    public void setAccAccNoRule(Integer accAccNoRule) {
        this.accAccNoRule = accAccNoRule;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    public String getNatureName() {
        return natureName;
    }
    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }
    public String getAccCussent() {
        return accCussent;
    }
    public void setAccCussent(String accCussent) {
        this.accCussent = accCussent;
    }
    public String getFcaccId() {
        return fcaccId;
    }
    public void setFcaccId(String fcaccId) {
        this.fcaccId = fcaccId;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
}
