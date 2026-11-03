package com.bytter.finance.domain.provide.vo;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.provide.entity.ProvideSyndicated;
import com.bytter.core.annotation.*;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class LoanInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /*
     * 结算中心代码
     */
    private String netId;
    /*
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "info.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    @Excel(name = "*申请单位")
    private String corpName;
    /*
     * 借款流水号
     */
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String billCode;
    /*
     * 借款合同号
     */
    @Excel(name = "借款合同号")
    private String contractNo;
    /*
     * 合同号期限
     */
    @Dict(dictGroup = "Provide.ContractTerm", inject = "contractTermName")
    private String contractTerm;
    private String contractTermName;
    /*
     * 借款金额
     */
    @BtCompositeSearch(fieldName = "info.PL_MONEY")
    @Excel(name = "*借款金额")
    private BigDecimal plMoney;
    /*
     * 借款期限
     */
    @BtCompositeSearch(fieldName = "info.pl_term")
    private Integer plTerm;
    /*
     * 期限单位
     */
//    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;
    private String ternUnitName;
    /*
     * 借款币种
     */
    @Currency(inject = "curName")
    private String curId;
    @Excel(name = "*借款币别")
    private String curName;
    /*
     * 借款银行类别
     */
    @BankType(inject = "bankTypeName")
    private String bankType;
    /*
     * 联行号
     */
    private String bankCode;
    /*
     * 银行类别名称
     */
    private String bankTypeName;
    /*
     * 借款银行
     */
    private String bankId;
    @Excel(name = "*借款银行")
    private String bankName;
    /*
     * 借款银行账号
     */
    private String plAccount;
    /*
     * 借款日期
     */
    @BtCompositeSearch(fieldName = "info.PL_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*借款日期")
    private Date plDate;
    /*
     * 还款日期
     */
    @BtCompositeSearch(fieldName = "info.REPAY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*借款到期日期")
    private Date repayDate;
    /*
     * 计息财务季度
     */
    private String fiscalQuarter;
    /*
     * 计息日
     */
    private Integer expiryDate;
    /*
     * 预提日
     */
    private Integer accruedDailyDate;
    /*
     * 还款方式
     */
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    @Excel(name = "*还款方式")
    private String repayTypeName;
    // 新需求 还款类型维护，repayType与CMS_REPAY_TYPE关联
    private String repayTypeName1;
    /*
     * 还款方式说明
     */
    private String repayTypeDesc;
    /*
     * 借款种类
     */
    @Dict(tableName = "CMS_LOAN_TYPE", valueFieldName = "id", showFiledName = "NAME", inject = "plClassName")
    private String plClassId;
    private String plClass;
    @Excel(name = "*借款种类")
    private String plClassName;
    /*
     * 贷款类型
     */
    private String plGenre;
    /*
     * 担保主业务类型
     */
    private String mainPlType;
    /*
     * 担保方式
     */
    @Dict(dictGroup = "Cms.PlType.K", inject = "plTypeName")
    private String plType;
    private String plTypeName;
    /*
     * 担保说明
     */
    private String suretyExplain;
    /*
     * 借款利率
     */
    @Excel(name = "*借款利率")
    private BigDecimal plRate;
    /*
     * 展期利率
     */
    @Excel(name = "展期利率")
    private BigDecimal deferredRate;
    /*
     * 逾期利率
     */
    @Excel(name = "逾期利率")
    private BigDecimal overdueRate;
    /*
     * 补录员
     */
    private String repairOp;
    /*
     * 状态
     */
    @Dict(dictGroup = "cms.status", inject = "statusName")
    private Integer status;
    private String statusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /*
     * 借款用途
     */
    @Excel(name = "*借款用途")
    private String plPurpose;
    /*
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /*
     * 打回原因
     */
    private String refuseWhy;
    /*
     * 是否挂授信
     */
    private String isHangCredit;
    /*
     * 授信模式
     */
    private String ratmodel;
    /*
     * 额度编号
     */
    private String rationCode;
    /*
     * 占用授信代码
     */
    private String ratItemCode;
    /*
     * 保存当前币种转换汇率
     */
    private BigDecimal exchangerate;
    /*
     * 是否折合成本位币
     */
    private String isRmbRat;
    /*
     * 使用人
     */
    private String users;
    /*
     * 信贷合同号
     */
    private String creditContractNo;
    /*
     * 内部合同号
     */
    private String insideContractNo;
    /*
     * 项目名称
     */
    private String projectName;
    /*
     * 付息方式
     */
    @Dict(dictGroup = "provide.payInterest", inject = "cplInterestPaymentName")
    private String cplInterestPayment;
    @Excel(name = "付息方式")
    private String cplInterestPaymentName;
    /*
     * 中间业务费
     */
    private BigDecimal interbusexpenses;
    /*
     * 发放方式
     */
    @Dict(dictGroup = "cms.loan.loanSendOutType", inject = "loanSendOutTypeName")
    private String loanSendOutType;
    @Excel(name = "发放方式")
    private String loanSendOutTypeName;
    /*
     * 跳过借款发放功能
     */
    @Dict(dictGroup = "sf_yn", inject = "acrossSendName")
    private String acrossSend;
    @Excel(name = "跳过借款发放功能")
    private String acrossSendName;
    /*
     * 跳过借款补录功能
     */
    @Dict(dictGroup = "sf_yn", inject = "isOverRepairName")
    private String isOverRepair;
    @Excel(name = "*跳过借款补录功能")
    private String isOverRepairName;
    /*
     * 浮动方式
     */
//    @Dict(dictGroup = "Cms.FloatWay", inject = "floatWayName")
    private String floatWay;
    @Excel(name = "*浮动方式")
    private String floatWayName;
    /*
     * 二级浮动方式
     */
    private String floatWayNext;
    @Excel(name = "浮动方式调整日期")
    private String floatWayNextName;
    /*
     * 点数或浮动比例
     */
    @Excel(name = "*BP点数或比例")
    private BigDecimal pointsProportionText;
    /*
     * 点数或者比例
     */
//    @Dict(dictGroup = "Cms.PointsProportion", inject = "pointsProportionSelectName")
    private String pointsProportionSelect;
    private String pointsProportionSelectName;
    /*
     * 利率调整生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rateEntriesEffectDate;
    @Dict(dictGroup = "Admin.PredrawSign", inject = "interestWayName")
    private String interestWay;
    private String interestWayName;
    /*
     * 基准利率类型
     */
    private String referenceRateType;
    @Excel(name = "*利率类型")
    private String referenceRateTypeName;
    /*
     * 是否央行基准利率
     */
    private String isBankBaseRate;
    /*
     * 贷款周期
     */
    private String repayCycle;
    @Excel(name = "*利率期限")
    private String repayCycleName;
    /*
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /*
     * 利率复核生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ratetimeoftakeeffect;
    /*
     * 预计利息
     */
    private BigDecimal anticInterest;
    /*
     * 顾问费利率
     */
    private BigDecimal consultantFeeRate;
    /*
     * 顾问费
     */
    private BigDecimal consultantFee;
    /*
     * 第三方收益利率
     */
    private BigDecimal thirdPartyProfitRate;
    /*
     * 第三方收益
     */
    private BigDecimal thirdPartyProfit;
    /*
     * 其他利率
     */
    private BigDecimal otherFeeRate;
    /*
     * 其他费用
     */
    private BigDecimal otherFee;
    /*
     * 作废单号
     */
    private String bolishCode;
    /*
     * 利息测算基数
     */
    @Excel(name = "*利息测算基数")
    private String rateBase;
    /*
     * 是否勾选顾问费复选框
     */
    private String consultantCb;
    /*
     * 是否勾选第三方收益复选框
     */
    private String thirdPartyCb;
    /*
     * 是否勾其他利率复选框
     */
    private String otherFeeCb;
    /*
     * 综合成本年化利率
     */
    @Excel(name = "综合成本年化利率")
    private BigDecimal compositeFeeRate;
    /*
     * 综合成本
     */
    @Excel(name = "综合成本")
    private BigDecimal compositeFee;
    /*
     * 创建人名称
     */
    @BtCompositeSearch(fieldName = "info.create_name")
    private String createName;
    /*
     * 创建人登录名称
     */
    private String createBy;
    /*
     * 创建日期
     */
    @BtCompositeSearch(fieldName = "info.CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /*
     * 更新人名称
     */
    private String updateName;
    /*
     * 更新人登录名称
     */
    private String updateBy;
    /*
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    private String uploadId;
    /*
     * 资金预算
     */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    /*
     * 还款计划Id
     */
    private String planId;
    private String planStatus;
    /*
     * 录入/修改标志
     */
    private String myflag;
    // 以下是挂接保证金的信息
    /**
     * 保证金Id
     */
    private String bondId;
    /*
     * 保证金比例
     */
    private BigDecimal bailmoneyRate;
    /*
     * 保证金金额
     */
    private BigDecimal bailmoney;
    /*
     * 保证金合同号
     */
    private String bailmoneyContract;
    /*
     * 是否挂接保证金
     */
    private String includeBailmoney;
    /*
     * 修改前挂接保证金Id
     */
    private String bondIdPre;
    /*
     * 是否挂接保证金
     */
    private String isHookBond;
    /*
     * 保证金币别
     */
    private String bondCurId;
    private String bondCurName;
    /*
     * 保证金币别
     */
    private String bondBankAcc;
    /*
     * 是否挂接保证金
     */
    private String isOpenRat;
    /*
     * 授信业务品种id
     */
    private String billName;
    /*
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /*
     * 担保备注
     */
    private String guaranteeRmk;
    /*
     * 担保方式
     */
    private String guaWay;
    /*
     * 担保合同Id
     */
    private String guaContract;
    /*
     * 保证金利率
     */
    private BigDecimal bondRate;
    /*
     * 汇率
     */
    private BigDecimal[] exchangeRate;
    /*
     * 执行金额（折合本身币别）
     */
    private BigDecimal[] processCurMoney;
    /*
     * 是否对接合同管理
     */
    private String relateCon;
    /*
     * 对接合同管理管理模式
     */
    private String relateConType;
    /*
     * 调用合同id
     */
    private String conId;
    /*
     * 生成合同类型
     */
    private String conTypeId;
    private List<DfIntermediateCost> costTypeDtl;
    /*
     * 银团明细信息
     */
    private List<ProvideSyndicated> syndicatedList;
    /*
     * 利率报价日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "利率报价日期")
    private Date rateQuotedPriceDate;
    /*
     * 展期上浮点数
     */
    private BigDecimal deferredPoints;
    /*
     * 逾期上浮比例
     */
    private BigDecimal overdueProportion;
    /*
     * 是否挂抵质押物
     */
    private String isRelatePledge;
    /*
     * 抵质押物id
     */
    private String[] pledgeId;
    /*
     * 占用抵质押物额度
     */
    private BigDecimal[] pledgeMoney;
    /*
     * 计提方式
     */
//    @Dict(dictGroup = "nisBillhead.isPayed", inject = "isAutoPayName")
    private String isAutoPay;
    private String isAutoPayName;
    /*
     * 计提方式
     */
    private String accrualType;
    /*
     * 计提方式名称
     */
    @Excel(name = "*计提方式")
    private String accrualTypeName;

    /**
     * 还款账号
     */
    private String onlinePaymentAcc;

    /**
     * 还款账号名称
     */
    private String onlineAccName;

    /**
     * 还款开户行名称
     */
    private String onlineBankName;

    /**
     * 还款账号id
     */
    private String onlinePaymentAccId;

    @Override
    public String getBeanName() {
        return "loanInfoApplyService";
    }
    public String getMyflag() {
        return myflag;
    }
    public void setMyflag(String myflag) {
        this.myflag = myflag;
    }
    public String getFloatWayNextName() {
        return floatWayNextName;
    }
    public void setFloatWayNextName(String floatWayNextName) {
        this.floatWayNextName = floatWayNextName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getContractTerm() {
        return contractTerm;
    }
    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }
    public BigDecimal getPlMoney() {
        return plMoney;
    }
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }
    public Integer getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(Integer plTerm) {
        this.plTerm = plTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getTernUnitName() {
        return ternUnitName;
    }
    public void setTernUnitName(String ternUnitName) {
        this.ternUnitName = ternUnitName;
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
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getIsOverRepair() {
        return isOverRepair;
    }
    public void setIsOverRepair(String isOverRepair) {
        this.isOverRepair = isOverRepair;
    }
    public String getIsOverRepairName() {
        return isOverRepairName;
    }
    public void setIsOverRepairName(String isOverRepairName) {
        this.isOverRepairName = isOverRepairName;
    }
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getPlAccount() {
        return plAccount;
    }
    public void setPlAccount(String plAccount) {
        this.plAccount = plAccount;
    }
    public Date getPlDate() {
        return plDate;
    }
    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeDesc() {
        return repayTypeDesc;
    }
    public void setRepayTypeDesc(String repayTypeDesc) {
        this.repayTypeDesc = repayTypeDesc;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    public String getPlClassName() {
        return plClassName;
    }
    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }
    public String getPlGenre() {
        return plGenre;
    }
    public void setPlGenre(String plGenre) {
        this.plGenre = plGenre;
    }
    public String getMainPlType() {
        return mainPlType;
    }
    public void setMainPlType(String mainPlType) {
        this.mainPlType = mainPlType;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getPlTypeName() {
        return plTypeName;
    }
    public void setPlTypeName(String plTypeName) {
        this.plTypeName = plTypeName;
    }
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    public BigDecimal getBailmoney() {
        return bailmoney;
    }
    public void setBailmoney(BigDecimal bailmoney) {
        this.bailmoney = bailmoney;
    }
    public String getSuretyExplain() {
        return suretyExplain;
    }
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
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
    public String getPlPurpose() {
        return plPurpose;
    }
    public void setPlPurpose(String plPurpose) {
        this.plPurpose = plPurpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    public String getRatmodel() {
        return ratmodel;
    }
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    public BigDecimal getExchangerate() {
        return exchangerate;
    }
    public void setExchangerate(BigDecimal exchangerate) {
        this.exchangerate = exchangerate;
    }
    public String getIsRmbRat() {
        return isRmbRat;
    }
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
    public String getUsers() {
        return users;
    }
    public void setUsers(String users) {
        this.users = users;
    }
    public String getCreditContractNo() {
        return creditContractNo;
    }
    public void setCreditContractNo(String creditContractNo) {
        this.creditContractNo = creditContractNo;
    }
    public String getInsideContractNo() {
        return insideContractNo;
    }
    public void setInsideContractNo(String insideContractNo) {
        this.insideContractNo = insideContractNo;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getCplInterestPayment() {
        return cplInterestPayment;
    }
    public void setCplInterestPayment(String cplInterestPayment) {
        this.cplInterestPayment = cplInterestPayment;
    }
    public BigDecimal getInterbusexpenses() {
        return interbusexpenses;
    }
    public void setInterbusexpenses(BigDecimal interbusexpenses) {
        this.interbusexpenses = interbusexpenses;
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
    public String getFloatWayName() {
        return floatWayName;
    }
    public void setFloatWayName(String floatWayName) {
        this.floatWayName = floatWayName;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
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
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public Date getRatetimeoftakeeffect() {
        return ratetimeoftakeeffect;
    }
    public void setRatetimeoftakeeffect(Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    public BigDecimal getAnticInterest() {
        return anticInterest;
    }
    public void setAnticInterest(BigDecimal anticInterest) {
        this.anticInterest = anticInterest;
    }
    public BigDecimal getConsultantFeeRate() {
        return consultantFeeRate;
    }
    public void setConsultantFeeRate(BigDecimal consultantFeeRate) {
        this.consultantFeeRate = consultantFeeRate;
    }
    public BigDecimal getConsultantFee() {
        return consultantFee;
    }
    public void setConsultantFee(BigDecimal consultantFee) {
        this.consultantFee = consultantFee;
    }
    public BigDecimal getThirdPartyProfitRate() {
        return thirdPartyProfitRate;
    }
    public void setThirdPartyProfitRate(BigDecimal thirdPartyProfitRate) {
        this.thirdPartyProfitRate = thirdPartyProfitRate;
    }
    public BigDecimal getThirdPartyProfit() {
        return thirdPartyProfit;
    }
    public void setThirdPartyProfit(BigDecimal thirdPartyProfit) {
        this.thirdPartyProfit = thirdPartyProfit;
    }
    public BigDecimal getOtherFeeRate() {
        return otherFeeRate;
    }
    public void setOtherFeeRate(BigDecimal otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }
    public BigDecimal getOtherFee() {
        return otherFee;
    }
    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }
    public String getBolishCode() {
        return bolishCode;
    }
    public void setBolishCode(String bolishCode) {
        this.bolishCode = bolishCode;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public String getConsultantCb() {
        return consultantCb;
    }
    public void setConsultantCb(String consultantCb) {
        this.consultantCb = consultantCb;
    }
    public String getThirdPartyCb() {
        return thirdPartyCb;
    }
    public void setThirdPartyCb(String thirdPartyCb) {
        this.thirdPartyCb = thirdPartyCb;
    }
    public String getOtherFeeCb() {
        return otherFeeCb;
    }
    public void setOtherFeeCb(String otherFeeCb) {
        this.otherFeeCb = otherFeeCb;
    }
    public BigDecimal getCompositeFeeRate() {
        return compositeFeeRate;
    }
    public void setCompositeFeeRate(BigDecimal compositeFeeRate) {
        this.compositeFeeRate = compositeFeeRate;
    }
    public BigDecimal getCompositeFee() {
        return compositeFee;
    }
    public void setCompositeFee(BigDecimal compositeFee) {
        this.compositeFee = compositeFee;
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
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
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
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getContractTermName() {
        return contractTermName;
    }
    public void setContractTermName(String contractTermName) {
        this.contractTermName = contractTermName;
    }
    public String getRepayTypeName() {
        return StringUtils.isNotEmpty(repayTypeName) ? repayTypeName : repayTypeName1;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    public String getAcrossSend() {
        return acrossSend;
    }
    public void setAcrossSend(String acrossSend) {
        this.acrossSend = acrossSend;
    }
    public String getLoanSendOutTypeName() {
        return loanSendOutTypeName;
    }
    public void setLoanSendOutTypeName(String loanSendOutTypeName) {
        this.loanSendOutTypeName = loanSendOutTypeName;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getBizStatusName() {
        return bizStatusName;
    }
    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }
    public String getCplInterestPaymentName() {
        return cplInterestPaymentName;
    }
    public void setCplInterestPaymentName(String cplInterestPaymentName) {
        this.cplInterestPaymentName = cplInterestPaymentName;
    }
    public String getInterestWayName() {
        return interestWayName;
    }
    public void setInterestWayName(String interestWayName) {
        this.interestWayName = interestWayName;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getPlanStatus() {
        return planStatus;
    }
    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }
    public String getRatItemCode() {
        return ratItemCode;
    }
    public void setRatItemCode(String ratItemCode) {
        this.ratItemCode = ratItemCode;
    }
    public String[] getItemCode() {
        return itemCode;
    }
    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }
    public String[] getItemName() {
        return itemName;
    }
    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }
    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }
    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }
    public String getFiscalQuarter() {
        return fiscalQuarter;
    }
    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    public String getPointsProportionSelectName() {
        return pointsProportionSelectName;
    }
    public void setPointsProportionSelectName(String pointsProportionSelectName) {
        this.pointsProportionSelectName = pointsProportionSelectName;
    }
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    public String getAcrossSendName() {
        return acrossSendName;
    }
    public void setAcrossSendName(String acrossSendName) {
        this.acrossSendName = acrossSendName;
    }
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public String getIncludeBailmoney() {
        return includeBailmoney;
    }
    public void setIncludeBailmoney(String includeBailmoney) {
        this.includeBailmoney = includeBailmoney;
    }
    public String getBondIdPre() {
        return bondIdPre;
    }
    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public String getBondBankAcc() {
        return bondBankAcc;
    }
    public void setBondBankAcc(String bondBankAcc) {
        this.bondBankAcc = bondBankAcc;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    public BigDecimal[] getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal[] exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public BigDecimal[] getProcessCurMoney() {
        return processCurMoney;
    }
    public void setProcessCurMoney(BigDecimal[] processCurMoney) {
        this.processCurMoney = processCurMoney;
    }
    public String getRelateCon() {
        return relateCon;
    }
    public void setRelateCon(String relateCon) {
        this.relateCon = relateCon;
    }
    public String getRelateConType() {
        return relateConType;
    }
    public void setRelateConType(String relateConType) {
        this.relateConType = relateConType;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getPlClassId() {
        return plClassId;
    }
    public void setPlClassId(String plClassId) {
        this.plClassId = plClassId;
    }
    public List<DfIntermediateCost> getCostTypeDtl() {
        return costTypeDtl;
    }
    public void setCostTypeDtl(List<DfIntermediateCost> costTypeDtl) {
        this.costTypeDtl = costTypeDtl;
    }
    public List<ProvideSyndicated> getSyndicatedList() {
        return syndicatedList;
    }
    public void setSyndicatedList(List<ProvideSyndicated> syndicatedList) {
        this.syndicatedList = syndicatedList;
    }
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    public String[] getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String[] pledgeId) {
        this.pledgeId = pledgeId;
    }
    public BigDecimal[] getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal[] pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    public String getIsBankBaseRate() {
        return isBankBaseRate;
    }
    public void setIsBankBaseRate(String isBankBaseRate) {
        this.isBankBaseRate = isBankBaseRate;
    }
    public String getIsAutoPay() {
        return isAutoPay;
    }
    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }
    public String getIsAutoPayName() {
        return isAutoPayName;
    }
    public void setIsAutoPayName(String isAutoPayName) {
        this.isAutoPayName = isAutoPayName;
    }
    public Date getRateQuotedPriceDate() {
        return rateQuotedPriceDate;
    }
    public void setRateQuotedPriceDate(Date rateQuotedPriceDate) {
        this.rateQuotedPriceDate = rateQuotedPriceDate;
    }
    public BigDecimal getDeferredPoints() {
        return deferredPoints;
    }
    public void setDeferredPoints(BigDecimal deferredPoints) {
        this.deferredPoints = deferredPoints;
    }
    public BigDecimal getOverdueProportion() {
        return overdueProportion;
    }
    public void setOverdueProportion(BigDecimal overdueProportion) {
        this.overdueProportion = overdueProportion;
    }
    public String getAccrualType() {
        return accrualType;
    }
    public void setAccrualType(String accrualType) {
        this.accrualType = accrualType;
    }
    public String getRepayTypeName1() {
        return repayTypeName1;
    }
    public void setRepayTypeName1(String repayTypeName1) {
        this.repayTypeName1 = repayTypeName1;
    }
    public String getAccrualTypeName() {
        return accrualTypeName;
    }
    public void setAccrualTypeName(String accrualTypeName) {
        this.accrualTypeName = accrualTypeName;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getOnlineBankName() {
        return onlineBankName;
    }

    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }

    public String getOnlinePaymentAccId() {
        return onlinePaymentAccId;
    }

    public void setOnlinePaymentAccId(String onlinePaymentAccId) {
        this.onlinePaymentAccId = onlinePaymentAccId;
    }
}
