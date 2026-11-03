package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 理财登记导入vo
 *
 * @author ouyj
 */
@ExcelTarget("ChoProductReqImportVo")
public class ChoProductReqImportVo extends ExcelModel implements Serializable,  IBusinessKey {
    private String id;
    /**
     * 业务单号
     */
    private String billNo;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "pr.corp_Id")
    private String corpId;
    @Excel(name = "*单位")
    private String corpName;
    /**
     * 银行类别
     */
    @Dict(tableName = "bt_bank_type", valueFieldName = "id", showFiledName = "type_name", inject = "bankTypeName")
    @BtCompositeSearch(fieldName = "pr.bank_Type_Id")
    private String bankTypeId;
    private String bankTypeName;
    /**
     * 产品类型
     */
    @BtCompositeSearch(fieldName = "pr.product_Type")
    private String productType;
    /**
     * 产品代码
     */
    @Excel(name = "*产品代码")
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;
    /**
     * 产品名称
     */
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**
     * 购买银行ID
     */
    @BtCompositeSearch(fieldName = "pr.bank_Info_Id")
    private String bankInfoId;
    /**
     * 购买银行
     */
    private String bankName;
    /**
     * 理财账号ID
     */
    @BtCompositeSearch(fieldName = "pr.bank_Acc_Id")
    private String bankAccId;
    /**
     * 理财账号
     */
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    @Excel(name = "*理财账号")
    private String bankAcc;
    /**
     * 理财账号（针对保本收益型）
     */
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    @Excel(name = "理财账号")
    private String bankAccBreak;
    /**
     * 币别
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;
    @Excel(name = "*币别")
    private String curName;
    /**
     * 金额
     */
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    @Excel(name = "*购买金额", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal buyMoney;
    /**
     * 收益条款:360-360天;365-365天
     */
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;
    @Excel(name = "*收益条款")
    private String incomeClauseName;
    /**
     * 购买日期
     */
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*购买日期", format = "yyyy-MM-dd", align = HorizontalAlignment.CENTER)
    private Date buyDate;
    /**
     * 期限:期限=到期日期-起息日期,单位:天
     */
    @BtCompositeSearch(fieldName = "pr.day_limit")
    private Integer dayLimit;
    /**
     * 起息日期
     */
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*起息日期", format = "yyyy-MM-dd", align = HorizontalAlignment.CENTER)
    private Date interestStartDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*到期日期", format = "yyyy-MM-dd", align = HorizontalAlignment.CENTER)
    private Date deadlineDate;
    /**
     * 兑付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "兑付日期", format = "yyyy-MM-dd", align = HorizontalAlignment.CENTER)
    private Date cashDate;
    /**
     * 参考收益率
     */
    @BtCompositeSearch(fieldName = "pr.refer_income_rate")
    private BigDecimal referIncomeRate;

    /**
     * 结束参考收益率
     */
    private BigDecimal endReferIncomeRate;

    /**
     * 参考收益率 区间
     */
    @Excel(name = "*参考收益率")
    private String referIncomeRateRange;

    /**
     * 申购费
     */
    @Excel(name = "申购费", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal subFeeMoney;
    /**
     * 参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）
     */
    @Excel(name = "参考收益", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal referIncomeMoney;
    /**
     * 结束参考收益金额
     */
    private BigDecimal endReferIncomeMoney;

    /**
     * 其他手续费
     */
    @Excel(name = "其他手续费", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal chargeMoney;
    /**
     * 是否可提前赎回 0-否 1-是
     */
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    @Excel(name = "是否提前赎回")
    private String isMoveUp;
    /**
     * 提前回购条约
     */
    @Excel(name = "提前回购条约")
    private String moveUpRmk;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "pr.status")
    @Dict(dictGroup = "choBankFinance.status", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 创建人登录姓名
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
    /**
     * 金额单位
     */
    private Integer moneyUnit;
    /**
     * 消息提醒标志
     */
    private String noticeSign;
    /**
     * 资金来源:1 自由资金 2 募集资金 3 银行贷款
     */
    private Integer capitalSource;
    @Excel(name = "资金来源")
    private String capitalSourceName;
    /**
     * 是否保本 0-否 1-是
     */
    private String isGuaranteed;
    /**
     * 已赎回金额
     */
    private BigDecimal redeemedMoney;
    /**
     * 待赎回金额
     */
    private BigDecimal waitRedeemMoney;
    /**
     * 理财类型
     */
    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    @Dict(dictGroup = "financial.type", inject = "financialTypeName")
    private String financialType;
    private String financialTypeName;
    /**
     * 付息方式
     */
    @BtCompositeSearch(fieldName = "pr.PAY_WAY")
    @Dict(dictGroup = "financial.payWay", inject = "payWayName")
    private String payWay;
    @Excel(name = "*付息方式")
    private String payWayName;
    /**
     * 合同号
     */
    @BtCompositeSearch(fieldName = "pr.CONTRACT_NO")
    @Excel(name = "合同号")
    private String contractNo;
    /**
     * 收益类型
     */
    @BtCompositeSearch(fieldName = "pr.INCOME_TYPE")
    private String incomeType;
    @Excel(name = "*收益类型")
    private String incomeTypeName;
    /**
     * 到期是否续存
     */
    @BtCompositeSearch(fieldName = "pr.IS_STORE")
    @Excel(name = "到期是否续存")
    private String isStore;
    @Excel(name = "*合同利率")
    private String contractRate;
    /**
     * 起始合同利率
     */
    @BtCompositeSearch(fieldName = "pr.START_CONTRACT_RATE")
    private BigDecimal startContractRate;
    /**
     * 结束合同利率
     */
    @BtCompositeSearch(fieldName = "pr.END_CONTRACT_RATE")
    private BigDecimal endContractRate;
    /**
     * 理财登记申购费
     */
    private BigDecimal reqSubFeeMoney;
    /**
     * 理财登记其他手续费
     */
    private BigDecimal reqChargeMoney;
    /**
     * 赎回费
     */
    private BigDecimal redeemChargeMoney;
    /**
     * 理财赎回表id
     */
    private String redeemId;
    /**
     * 日净值
     */
    @Excel(name = "*购买日净值", numFormat = "##########0.000000", align = HorizontalAlignment.RIGHT)
    private BigDecimal dailyNetVal;
    /**
     * 份额
     */
    @Excel(name = "份额", numFormat = "##########0.000000", align = HorizontalAlignment.RIGHT)
    private BigDecimal portion;
    /**
     * 合同利率(非必填的合同利率)
     */
    @Excel(name = "合同利率")
    private String nonContractRate;
    /**
     * 到期日期(非必填的到期日期)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期日期", format = "yyyy-MM-dd", align = HorizontalAlignment.CENTER)
    private Date nonDeadlineDate;
    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;
    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;
    /**
     * 业务状态
     */
    @BtCompositeSearch(fieldName = "pr.biz_status")
    private Integer bizStatus;
    /**
     * 最新净值
     */
    private BigDecimal latestNetVal;
    /**
     * 最新日期
     */
    private Date latestDate;
    /**
     * 是否维护日净值 0-否 1-是
     */
    @Excel(name = "是否维护日净值")
    private String isMaintainVal;
    @Override
    public String getBeanName() {
        return "choProductReqService";
    }

    public String getReferIncomeRateRange() {
        return referIncomeRateRange;
    }

    public void setReferIncomeRateRange(String referIncomeRateRange) {
        this.referIncomeRateRange = referIncomeRateRange;
    }

    public String getIncomeTypeName() {
        return incomeTypeName;
    }
    public void setIncomeTypeName(String incomeTypeName) {
        this.incomeTypeName = incomeTypeName;
    }
    public String getIsMaintainVal() {
        return isMaintainVal;
    }
    public void setIsMaintainVal(String isMaintainVal) {
        this.isMaintainVal = isMaintainVal;
    }
    public Date getNonDeadlineDate() {
        return nonDeadlineDate;
    }
    public void setNonDeadlineDate(Date nonDeadlineDate) {
        this.nonDeadlineDate = nonDeadlineDate;
    }
    public String getNonContractRate() {
        return nonContractRate;
    }
    public void setNonContractRate(String nonContractRate) {
        this.nonContractRate = nonContractRate;
    }
    public String getContractRate() {
        return contractRate;
    }
    public void setContractRate(String contractRate) {
        this.contractRate = contractRate;
    }
    public String getCapitalSourceName() {
        return capitalSourceName;
    }
    public void setCapitalSourceName(String capitalSourceName) {
        this.capitalSourceName = capitalSourceName;
    }
    public String getIncomeClauseName() {
        return incomeClauseName;
    }
    public void setIncomeClauseName(String incomeClauseName) {
        this.incomeClauseName = incomeClauseName;
    }
    public BigDecimal getLatestNetVal() {
        return latestNetVal;
    }
    public void setLatestNetVal(BigDecimal latestNetVal) {
        this.latestNetVal = latestNetVal;
    }
    public Date getLatestDate() {
        return latestDate;
    }
    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    public BigDecimal getRedeemDailyNetVal() {
        return redeemDailyNetVal;
    }
    public void setRedeemDailyNetVal(BigDecimal redeemDailyNetVal) {
        this.redeemDailyNetVal = redeemDailyNetVal;
    }
    public BigDecimal getRedeemPortion() {
        return redeemPortion;
    }
    public void setRedeemPortion(BigDecimal redeemPortion) {
        this.redeemPortion = redeemPortion;
    }
    public BigDecimal getDailyNetVal() {
        return dailyNetVal;
    }
    public void setDailyNetVal(BigDecimal dailyNetVal) {
        this.dailyNetVal = dailyNetVal;
    }
    public BigDecimal getPortion() {
        return portion;
    }
    public void setPortion(BigDecimal portion) {
        this.portion = portion;
    }
    public String getRedeemId() {
        return redeemId;
    }
    public void setRedeemId(String redeemId) {
        this.redeemId = redeemId;
    }
    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }
    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }
    public BigDecimal getReqSubFeeMoney() {
        return reqSubFeeMoney;
    }
    public void setReqSubFeeMoney(BigDecimal reqSubFeeMoney) {
        this.reqSubFeeMoney = reqSubFeeMoney;
    }
    public BigDecimal getReqChargeMoney() {
        return reqChargeMoney;
    }
    public void setReqChargeMoney(BigDecimal reqChargeMoney) {
        this.reqChargeMoney = reqChargeMoney;
    }
    public String getFinancialType() {
        return financialType;
    }
    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }
    public String getFinancialTypeName() {
        return financialTypeName;
    }
    public void setFinancialTypeName(String financialTypeName) {
        this.financialTypeName = financialTypeName;
    }
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public String getPayWayName() {
        return payWayName;
    }
    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getIncomeType() {
        return incomeType;
    }
    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }
    public String getIsStore() {
        return isStore;
    }
    public void setIsStore(String isStore) {
        this.isStore = isStore;
    }
    public BigDecimal getStartContractRate() {
        return startContractRate;
    }
    public void setStartContractRate(BigDecimal startContractRate) {
        this.startContractRate = startContractRate;
    }
    public BigDecimal getEndContractRate() {
        return endContractRate;
    }
    public void setEndContractRate(BigDecimal endContractRate) {
        this.endContractRate = endContractRate;
    }
    public String getNoticeSign() {
        return noticeSign;
    }
    public void setNoticeSign(String noticeSign) {
        this.noticeSign = noticeSign;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getBankInfoId() {
        return bankInfoId;
    }
    public void setBankInfoId(String bankInfoId) {
        this.bankInfoId = bankInfoId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankAccBreak() {
        return bankAccBreak;
    }
    public void setBankAccBreak(String bankAccBreak) {
        this.bankAccBreak = bankAccBreak;
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
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }
    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }
    public Integer getIncomeClause() {
        return incomeClause;
    }
    public void setIncomeClause(Integer incomeClause) {
        this.incomeClause = incomeClause;
    }
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public Date getCashDate() {
        return cashDate;
    }
    public void setCashDate(Date cashDate) {
        this.cashDate = cashDate;
    }
    public BigDecimal getReferIncomeRate() {
        return referIncomeRate;
    }
    public void setReferIncomeRate(BigDecimal referIncomeRate) {
        this.referIncomeRate = referIncomeRate;
    }
    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }
    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }
    public BigDecimal getReferIncomeMoney() {
        return referIncomeMoney;
    }
    public void setReferIncomeMoney(BigDecimal referIncomeMoney) {
        this.referIncomeMoney = referIncomeMoney;
    }
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }
    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
    public String getIsMoveUp() {
        return isMoveUp;
    }
    public void setIsMoveUp(String isMoveUp) {
        this.isMoveUp = isMoveUp;
    }
    public String getMoveUpRmk() {
        return moveUpRmk;
    }
    public void setMoveUpRmk(String moveUpRmk) {
        this.moveUpRmk = moveUpRmk;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public Integer getMoneyUnit() {
        return moneyUnit;
    }
    public void setMoneyUnit(Integer moneyUnit) {
        this.moneyUnit = moneyUnit;
    }
    public Integer getCapitalSource() {
        return capitalSource;
    }
    public void setCapitalSource(Integer capitalSource) {
        this.capitalSource = capitalSource;
    }
    public String getIsGuaranteed() {
        return isGuaranteed;
    }
    public void setIsGuaranteed(String isGuaranteed) {
        this.isGuaranteed = isGuaranteed;
    }
    public BigDecimal getRedeemedMoney() {
        return redeemedMoney;
    }
    public void setRedeemedMoney(BigDecimal redeemedMoney) {
        this.redeemedMoney = redeemedMoney;
    }
    public BigDecimal getWaitRedeemMoney() {
        return waitRedeemMoney;
    }
    public void setWaitRedeemMoney(BigDecimal waitRedeemMoney) {
        this.waitRedeemMoney = waitRedeemMoney;
    }

    public BigDecimal getEndReferIncomeRate() {
        return endReferIncomeRate;
    }

    public void setEndReferIncomeRate(BigDecimal endReferIncomeRate) {
        this.endReferIncomeRate = endReferIncomeRate;
    }

    public BigDecimal getEndReferIncomeMoney() {
        return endReferIncomeMoney;
    }

    public void setEndReferIncomeMoney(BigDecimal endReferIncomeMoney) {
        this.endReferIncomeMoney = endReferIncomeMoney;
    }
}
