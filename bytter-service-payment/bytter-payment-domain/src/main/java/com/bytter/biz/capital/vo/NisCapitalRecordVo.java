package com.bytter.biz.capital.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@ExcelTarget("NisCapitalRecordVo")
public class NisCapitalRecordVo extends ExcelModel implements Serializable {
    /**记录ID*/
    private String id;
    /**付款单号 年年月月日日+6位序列号(从000000开始)*/
    private java.lang.String billNo;
    /**单位ID*/
    @Dict(dictGroup = "corpId",inject = "corpName")//用于排序
    @BtCompositeSearch(fieldName = "c.corp_id")
    private java.lang.String corpId;
    @Excel(name = "*成员单位")
    @NotNull( message = "nis.capitalRecord.message.resetAcceptor.isnull")
    private java.lang.String corpName;
    /**收款账号*/
    @BtCompositeSearch(fieldName = "c.payee_acc")
    private java.lang.String payeeAcc;
    /**收款账号名称*/
    @BtCompositeSearch(fieldName = "c.payee_acc_name")
    @Excel(name = "*收款单位")
    @NotNull( message = "nis.capitalRecord.message.payeeAcc.isnull")
    private java.lang.String payeeAccName;
    /**收款方开户行行号*/
    @BtCompositeSearch(fieldName = "c.payee_bank_code")
    private java.lang.String payeeBankCode;
    /**收款开户行名称*/
    private java.lang.String payeeBank;
    /**金额*/
    @BtCompositeSearch(fieldName = "c.pay_money")
    @Excel(name = "*金额",align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NotNull( message = "nis.capitalRecord.message.payMoney.isnull")
    private java.math.BigDecimal payMoney;
    /**期望支付日期*/
    @BtCompositeSearch(fieldName = "c.wish_payDay")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name = "*期望支付日期",align = HorizontalAlignment.CENTER,databaseFormat = "yyyy-MM-dd")
    @NotNull( message = "nis.capitalRecord.message.wishPayDay.isnull")
    private java.util.Date wishPayDay;
    /**结算中心Id*/
    @Dict(dictGroup = "netId",inject = "netName") //用于排序
    @BtCompositeSearch(fieldName = "c.net_id")
    private java.lang.String netId;
    @Excel(name = "*结算中心")
    @NotNull( message = "nis.capitalRecord.message.netName.isnull")
    private java.lang.String netName;
    /**付款方式*/
    @Dict(dictGroup = "capitalRecord.voucerType", inject = "voucherTypeStr")
    @BtCompositeSearch(fieldName = "c.voucher_type")
    private java.lang.String voucherType;
    @Excel(name = "付款方式")
    private java.lang.String voucherTypeStr;
    /**资金备案类别*/
    @Dict(dictGroup = "capitalRecord.busType", inject = "busTypeStr")
    @BtCompositeSearch(fieldName = "c.bus_type")
    private java.lang.String busType;
    @Excel(name = "*资金备案类别")
    @NotNull( message = "nis.capitalRecord.message.busType.isnull")
    private java.lang.String busTypeStr;
    /**付款事由*/
    @BtCompositeSearch(fieldName = "c.purpose")
    @Excel(name = "付款事由")
    private java.lang.String purpose;
    /**项目名称*/

    @BtCompositeSearch(fieldName = "c.project_name")
    @Excel(name = "项目名称")
    private java.lang.String projectName;
    /**合同编号*/
    @BtCompositeSearch(fieldName = "c.contract_code")
    @Excel(name = "合同编号")
    private java.lang.String contractCode;
    /**合同名称*/
    @BtCompositeSearch(fieldName = "c.contract_name")
    @Excel(name = "合同名称")
    private java.lang.String contractName;
    /**合同类型*/

    @BtCompositeSearch(fieldName = "c.contract_type")
    @Excel(name = "合同类型")
    private java.lang.String contractType;
    /**状态*/
    @BtCompositeSearch(fieldName = "c.status")
    @Dict(dictGroup = "capitalRecord.status", inject = "statusStr")
    private java.lang.Integer status;
    @BtCompositeSearch(fieldName = "c.status")
    private java.lang.String statusStr;
    /**附件Id*/
    private java.lang.String attachmentId;
    /**创建人登录姓名*/
    private java.lang.String createBy;
    /**创建日期*/
    @BtCompositeSearch(fieldName = "c.create_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.util.Date createDate;
    /**更新人登录名称*/
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    public String getPayeeBankCode() {
        return payeeBankCode;
    }
    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }
    public String getPayeeBank() {
        return payeeBank;
    }
    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public Date getWishPayDay() {
        return wishPayDay;
    }
    public void setWishPayDay(Date wishPayDay) {
        this.wishPayDay = wishPayDay;
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
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public String getVoucherTypeStr() {
        return voucherTypeStr;
    }
    public void setVoucherTypeStr(String voucherTypeStr) {
        this.voucherTypeStr = voucherTypeStr;
    }
    public String getBusTypeStr() {
        return busTypeStr;
    }
    public void setBusTypeStr(String busTypeStr) {
        this.busTypeStr = busTypeStr;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getContractName() {
        return contractName;
    }
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    public String getContractType() {
        return contractType;
    }
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusStr() {
        return statusStr;
    }
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public String getBeanName() {
        return "nisCapitalRecordService";
    }
}
