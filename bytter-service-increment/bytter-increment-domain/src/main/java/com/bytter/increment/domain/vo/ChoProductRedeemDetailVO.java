package com.bytter.increment.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public class ChoProductRedeemDetailVO extends IdEntity implements java.io.Serializable, IBusinessKey {
    /**
     * 业务单号
     */
    private String billNo;

    /**
     * 产品登记单号
     */
    private String reqId;

    /**
     * 单位Id
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "pr.corp_Id")
    private String corpId;

    private String corpName;

    /**
     * 银行类别
     */
    @Dict(tableName = "bt_bank_type", valueFieldName = "id", showFiledName = "type_name", inject = "bankTypeName")
    @BtCompositeSearch(fieldName = "pr.bank_Type_Id")
    private String bankTypeId;

    private String bankTypeName;

    /**
     * 购买银行ID
     */
    @BtCompositeSearch(fieldName = "pr.bank_Info_Id")
    private String bankInfoId;

    private String bankName;

    /**
     * 产品类型
     */
    @BtCompositeSearch(fieldName = "pr.product_Type")
    private String productType;

    /**
     * 产品代码
     */
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;

    /**
     * 产品名称
     */
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;

    /**
     * 理财账号
     */
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    private String bankAcc;

    /**
     * 金额
     */
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;

    /**
     * 币别
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;

    private String curName;

    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;

    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;

    /**
     * 赎回金额
     */
    @BtCompositeSearch(fieldName = "prd.redeem_money")
    private BigDecimal redeemMoney;

    /**
     * 赎回日期
     */
    @BtCompositeSearch(fieldName = "prd.redeem_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date redeemDate;

    /**
     * 到账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transferDate;

    /**
     * 收益金额
     */
    @BtCompositeSearch(fieldName = "prd.income_money")
    private BigDecimal incomeMoney;

    /**
     * 实际收益率
     */
    @BtCompositeSearch(fieldName = "prd.income_rate")
    private BigDecimal incomeRate;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 申购费
     */
    @BtCompositeSearch(fieldName = "prd.sub_fee_money")
    private BigDecimal subFeeMoney;

    /**
     * 赎回费
     */
    @BtCompositeSearch(fieldName = "prd.redeem_charge_money")
    private BigDecimal redeemChargeMoney;

    /**
     * 其他手续费
     */
    @BtCompositeSearch(fieldName = "prd.charge_money")
    private BigDecimal chargeMoney;

    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "prd.status")
    private Integer status;

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
     * 更新时间
     */
    private Date updateDate;

    /**
     * 购买日期
     */
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    /**
     * 期限:期限=到期日期-起息日期,单位:天
     */
    private Integer dayLimit;

    /**
     * 收益条款:360-360天;365-365天
     */
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;

    /**
     * 兑付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cashDate;

    /**
     * 参考收益率
     */
    private BigDecimal referIncomeRate;

    /**
     * 结束参考收益率
     */
    private BigDecimal endReferIncomeRate;

    /**
     * 参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）
     */
    private BigDecimal referIncomeMoney;

    /**
     * 结束参考收益金额
     */
    private BigDecimal endReferIncomeMoney;

    /**
     * 银行流水认领组件挂接确认
     */
    private String collectionConfirm;

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

    private String payWayName;

    /**
     * 合同号
     */
    @BtCompositeSearch(fieldName = "pr.CONTRACT_NO")
    private String contractNo;

    /**
     * 收益类型
     */
    @BtCompositeSearch(fieldName = "pr.INCOME_TYPE")
    private String incomeType;

    /**
     * 到期是否续存
     */
    @BtCompositeSearch(fieldName = "pr.IS_STORE")
    private String isStore;

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
     * 是否可提前赎回 0-否 1-是
     */
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    private String isMoveUp;

    /**
     * 资金来源:1 自由资金 2 募集资金 3 银行贷款
     */
    private Integer capitalSource;

    /**
     * 申购费
     */
    @BtCompositeSearch(fieldName = "pr.sub_fee_money")
    private BigDecimal reqSubFeeMoney;

    /**
     * 其他手续费
     */
    @BtCompositeSearch(fieldName = "pr.charge_money")
    private BigDecimal reqChargeMoney;

    /**
     * 理财续期id
     */
    private String renewalId;

    /**
     * 是否隐藏 1：隐藏
     */
    private String checkboxHide;

    /**
     * 续期状态
     */
    @BtCompositeSearch(fieldName = "pw.status")
    private Integer renewalStatus;

    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;

    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;

    /**
     * 日净值
     */
    private BigDecimal dailyNetVal;

    /**
     * 份额
     */
    private BigDecimal portion;

    /**
     * 是否维护日净值
     */
    private String isMaintainVal;

    /**
     * 上次赎回时间
     */
    private Date lastRedeemDate;
    /**
     * 可赎回金额
     */
    private BigDecimal leftMoney;

    /**
     * 可赎回本金
     */
    private BigDecimal redeemableMoney;
    /**
     * 最大赎回日期
     */
    private Date maxRedeemDate;
    /**
     * 最大赎回份额
     */
    private BigDecimal maxPortion;
    /**
     * 最小赎回日期
     */
    private Date minDate;

    public String getIsMaintainVal() {
        return isMaintainVal;
    }

    public void setIsMaintainVal(String isMaintainVal) {
        this.isMaintainVal = isMaintainVal;
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

    public Integer getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(Integer renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getCheckboxHide() {
        if (StringUtils.isEmpty(checkboxHide)
                && AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())) {
            return "1";
        } else {
            return "0";
        }
    }

    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }

    public String getRenewalId() {
        return renewalId;
    }

    public void setRenewalId(String renewalId) {
        this.renewalId = renewalId;
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

    public String getIsMoveUp() {
        return isMoveUp;
    }

    public void setIsMoveUp(String isMoveUp) {
        this.isMoveUp = isMoveUp;
    }

    public Integer getCapitalSource() {
        return capitalSource;
    }

    public void setCapitalSource(Integer capitalSource) {
        this.capitalSource = capitalSource;
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

    public String getCollectionConfirm() {
        return collectionConfirm;
    }

    public void setCollectionConfirm(String collectionConfirm) {
        this.collectionConfirm = collectionConfirm;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public BigDecimal getRedeemMoney() {
        return redeemMoney;
    }

    public void setRedeemMoney(BigDecimal redeemMoney) {
        this.redeemMoney = redeemMoney;
    }

    public Date getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public BigDecimal getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(BigDecimal incomeRate) {
        this.incomeRate = incomeRate;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }

    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }

    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }

    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
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

    public Integer getIncomeClause() {
        return incomeClause;
    }

    public void setIncomeClause(Integer incomeClause) {
        this.incomeClause = incomeClause;
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

    public BigDecimal getReferIncomeMoney() {
        return referIncomeMoney;
    }

    public void setReferIncomeMoney(BigDecimal referIncomeMoney) {
        this.referIncomeMoney = referIncomeMoney;
    }

    public Date getLastRedeemDate() {
        return lastRedeemDate;
    }

    public void setLastRedeemDate(Date lastRedeemDate) {
        this.lastRedeemDate = lastRedeemDate;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    public BigDecimal getRedeemableMoney() {
        return redeemableMoney;
    }

    public void setRedeemableMoney(BigDecimal redeemableMoney) {
        this.redeemableMoney = redeemableMoney;
    }

    public Date getMaxRedeemDate() {
        return maxRedeemDate;
    }

    public void setMaxRedeemDate(Date maxRedeemDate) {
        this.maxRedeemDate = maxRedeemDate;
    }

    public BigDecimal getMaxPortion() {
        return maxPortion;
    }

    public void setMaxPortion(BigDecimal maxPortion) {
        this.maxPortion = maxPortion;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
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

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
