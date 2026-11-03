package com.bytter.biz.compareBill.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.User;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
/**
 * 单位对账单填写VO对象
 *
 * @author zhoul 2020-11-04
 * @since 3.4
 */
@ApiModel("单位对账单VO对象")
public class CorpCompareBillInputVO implements  IBusinessKey {
    /**
     * 账单ID
     */
    @ApiModelProperty(value = "账单ID")
    private String billId;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @ApiModelProperty(value = "单位ID")
    private String corpId;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String corpName;
    /**
     * 网点名称
     */
    @ApiModelProperty(value = "网点名称")
    private String netName;
    /**
     * 币别ID
     */
    @Currency(inject = "currencyName")
    @ApiModelProperty(value = "币别ID")
    private String currencyId;
    /**
     * 币别名称
     */
    @ApiModelProperty(value = "币别名称")
    private String currencyName;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String account;
    /**
     * 账号名称
     */
    @ApiModelProperty(value = "账号名称")
    private String accountName;
    /**
     * 账号类型
     */
    @Dict(dictGroup = "InnerAccount.AccType",inject = "accountTypeName")
    @ApiModelProperty(value = "账号类型")
    private String accountType;
    @ApiModelProperty(value = "账号类型名称")
    private String accountTypeName;
    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private String balance;
    @ApiModelProperty(value = "企业账面余额")
    private String erpBalance;
    /**
     * 状态
     */
    @Dict(dictGroup = "fc.corpCompareBill",inject = "statusName")
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "状态名称")
    private String statusName;
    /**
     * 是否相符
     */
    @Dict(dictGroup = "fc.corpCompareBillSame",inject = "sameName")
    @ApiModelProperty(value = "是否相符标识")
    private String same;
    @ApiModelProperty(value = "是否相符字符标识")
    private String sameName;
    /**
     * 操作人
     */
    @User(inject = "opUserName")
    @ApiModelProperty("操作人code")
    private String opCode;
    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String opUserName;
    /**
     * 操作日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty("操作日期")
    private Date opDate;
    private Date checkDate;
    /**
     * 中心借方未达帐
     */
    @ApiModelProperty(value = "中心借方未达帐")
    private String accountDebitMoney;
    /**
     * 中心贷方未达帐
     */
    @ApiModelProperty(value = "中心贷方未达帐")
    private String accountCreditMoney;
    /**
     * 单位借方未达帐
     */
    @ApiModelProperty(value = "单位借方未达帐")
    private String erpDebitMoney;
    /**
     * 单位贷方未达帐
     */
    @ApiModelProperty(value = "单位贷方未达帐")
    private String erpCreditMoney;
    @ApiModelProperty(value = "对账月份")
    private String billMonth;
    @ApiModelProperty(value = "打印次数")
    private Integer prints;

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSameName() {
        return sameName;
    }

    public void setSameName(String sameName) {
        this.sameName = sameName;
    }

    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
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
    public String getCurrencyId() {
        return currencyId;
    }
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }
    public String getCurrencyName() {
        return currencyName;
    }
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSame() {
        return same;
    }
    public void setSame(String same) {
        this.same = same;
    }
    public String getOpCode() {
        return opCode;
    }
    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
    public String getOpUserName() {
        return opUserName;
    }
    public void setOpUserName(String opUserName) {
        this.opUserName = opUserName;
    }
    public String getAccountDebitMoney() {
        return accountDebitMoney;
    }
    public void setAccountDebitMoney(String accountDebitMoney) {
        this.accountDebitMoney = accountDebitMoney;
    }
    public String getAccountCreditMoney() {
        return accountCreditMoney;
    }
    public void setAccountCreditMoney(String accountCreditMoney) {
        this.accountCreditMoney = accountCreditMoney;
    }
    public String getErpDebitMoney() {
        return erpDebitMoney;
    }
    public void setErpDebitMoney(String erpDebitMoney) {
        this.erpDebitMoney = erpDebitMoney;
    }
    public String getErpCreditMoney() {
        return erpCreditMoney;
    }
    public void setErpCreditMoney(String erpCreditMoney) {
        this.erpCreditMoney = erpCreditMoney;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getBillMonth() {
        return billMonth;
    }
    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }
    public Integer getPrints() {
        return prints;
    }
    public void setPrints(Integer prints) {
        this.prints = prints;
    }
    @Override
    public String getBusinessKey() {
        return billId;
    }
    public String getErpBalance() {
        return erpBalance;
    }
    public void setErpBalance(String erpBalance) {
        this.erpBalance = erpBalance;
    }
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
}
