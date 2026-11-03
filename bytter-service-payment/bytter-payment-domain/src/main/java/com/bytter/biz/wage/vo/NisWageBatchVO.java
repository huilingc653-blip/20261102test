package com.bytter.biz.wage.vo;
import com.bytter.core.annotation.*;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
/**
 * @Title: Entity
 * @Description: 代发工资批量导入VO
 * @author onlineGenerator
 * @date 2017-10-31 15:20:33
 * @version 1.0
 *
 */
public class NisWageBatchVO extends ExcelModel implements Serializable {
    /**工资册标题*/
    @Excel(name = "*工资册标题")
    @NotNull(message = "nis.netbank.message.wagetitle.null")
    @BtCompositeSearch(fieldName = "n.wage_Title")
    private String wageTitle;
    /**单位*/
    @BtCompositeSearch(fieldName = "n.corp_Id")
    @Corp(inject="corpName")
    private String corpId;
    /**单位名称*/
    @Excel(name = "*单位名称")
    @NotNull(message = "nis.netbank.message.corpname.null")
    private String corpName;
    @Excel(name = "*付款账号")
    @BtCompositeSearch(fieldName = "n.payer_Acc")
    @NotNull(message = "nis.netbank.message.payeracc.null")
    private String payerAcc;
    /**付款账号名称*/
    private String payerAccName;
    /**付款开户行*/
    private String payerBank;
    /**付款开户行行号*/
    private String payerBankCode;
    /**账号所在省*/
    private String payerProv;
    /**账号所在市*/
    private String payerCity;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    @Excel(name = "*币别")
    @NotNull(message = "nis.netbank.message.curname.null")
    private String curName;
    /**接口代码*/
    private String bifCode;
    /**金额*/
    //@Excel(name = "*工资总额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @BtCompositeSearch(fieldName = "n.wages")
    private BigDecimal wages;
    /**工资发放日期*/
    @Excel(name = "*工资发放日期",format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "n.wage_Day")
    private Date wageDay;
    /**期望支付日期*/
    @Excel(name = "*期望支付日期",format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "n.wish_Payday")
    private Date wishPayday;
    /**附言*/
    @Excel(name = "附言")
    private String addWord;
    /**状态*/
    @BtCompositeSearch(fieldName = "n.status")
    private Integer status;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "n.voucher_Stat")
    private String voucherStat;
    /**业务类别id*/
    @Dict(tableName="NIS_BUS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
    private String busTypeId;
    /**业务类别*/
    private String busType;
    /**业务类别编码*/
    @Excel(name = "业务类别")
    private String busCode;
    /**业务类别名称*/
    private String busTypeName;
    /**代发工资ID*/
    @BtCompositeSearch(fieldName = "n.wage_Id")
    private String wageId;
    /**工号*/
    @BtCompositeSearch(fieldName = "n.work_No")
    @Excel(name = "工号")
    /*@NotNull(message = "nis.netbank.message.workno.null")*/
    private String workNo;
    /**姓名*/
	/*@Excel(name = "*姓名")
	@NotNull(message = "nis.netbank.message.username.null")*/
    private String userName;
    /**银行账号*/
    @BtCompositeSearch(fieldName = "n.bank_Acc")
    @Excel(name = "*银行账号")
    @NotNull(message = "nis.netbank.message.bankacc.null")
    private String bankAcc;
    /**账户名*/
    @BtCompositeSearch(fieldName = "n.acc_Name")
    @Excel(name = "*账户名")
    @NotNull(message = "nis.netbank.message.accname.null")
    private String accName;
    /**实发工资*/
    private String wagesVerify;
    /**实发工资*/
    @BtCompositeSearch(fieldName = "n.wages_Verify")
    @Excel(name = "*实发工资", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    private BigDecimal dtlWages;
    /**用途*/
    @BtCompositeSearch(fieldName = "n.purpose")
    @Excel(name = "*工资用途")
    @NotNull(message = "nis.wage.message.purpose.null")
    private String purpose;
    /**所在省*/
    @Region(inject="provName")
    private String prov;
    /*@Excel(name = "账号所在省")
    @NotNull(message = "nis.netbank.message.provname.null")*/
    private String provName;
    /**所在市*/
    @Region(inject="cityName")
    private String city;
    /*@Excel(name = "账号所在市")
    @NotNull(message = "nis.netbank.message.cityname.null")*/
    private String cityName;
    /**开户行行号*/
    @BtCompositeSearch(fieldName = "n.bank_Code")
    @Excel(name = "开户行行号")
    private String bankCode;
    /**开户银行名称*/
    @BtCompositeSearch(fieldName = "n.bank_Name")
    @Excel(name = "开户行名称")
    private String bankName;
    /**跨行标识 replace={"同行_0","他行_1"}*/
    @BtCompositeSearch(fieldName = "n.dif_Bank")
    @Excel(name = "跨行标志")
    private String difBank;
    /**地域标识, replace={"同城_0","异地_1"}*/
    @BtCompositeSearch(fieldName = "n.area_Sign")
    @Excel(name = "地域标志")
    private String areaSign;
    @Excel(name = "证件类型")
    private String idType;
    /**身份证号（部分银行需要身份证信息，已知北京银行、沧州银行）*/
    @Excel(name = "证件号")
    private String idNo;
    public String getWageTitle() {
        return wageTitle;
    }
    public void setWageTitle(String wageTitle) {
        this.wageTitle = wageTitle;
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
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getPayerAccName() {
        return payerAccName;
    }
    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }
    public String getPayerBank() {
        return payerBank;
    }
    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }
    public String getPayerBankCode() {
        return payerBankCode;
    }
    public void setPayerBankCode(String payerBankCode) {
        this.payerBankCode = payerBankCode;
    }
    public String getPayerProv() {
        return payerProv;
    }
    public void setPayerProv(String payerProv) {
        this.payerProv = payerProv;
    }
    public String getPayerCity() {
        return payerCity;
    }
    public void setPayerCity(String payerCity) {
        this.payerCity = payerCity;
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
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public BigDecimal getWages() {
        return wages;
    }
    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }
    public Date getWageDay() {
        return wageDay;
    }
    public void setWageDay(Date wageDay) {
        this.wageDay = wageDay;
    }
    public Date getWishPayday() {
        return wishPayday;
    }
    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }
    public String getAddWord() {
        return addWord;
    }
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public String getBusTypeId() {
        return busTypeId;
    }
    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusCode() {
        return busCode;
    }
    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
    public String getWageId() {
        return wageId;
    }
    public void setWageId(String wageId) {
        this.wageId = wageId;
    }
    public String getWorkNo() {
        return workNo;
    }
    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getWagesVerify() {
        return wagesVerify;
    }
    public void setWagesVerify(String wagesVerify) {
        this.wagesVerify = wagesVerify;
    }
    public BigDecimal getDtlWages() {
        return dtlWages;
    }
    public void setDtlWages(BigDecimal dtlWages) {
        this.dtlWages = dtlWages;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
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
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    public String getAreaSign() {
        return areaSign;
    }
    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public String getIdNo() {
        return idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    @Override
    public String getBeanName() {
        return "nisWageBatchService";
    }
    @Override
    public Map<String, String> getLangMap() {
        super.addLangMap("difBank", "nisBillhead.difBank");
        super.addLangMap("areaSign", "nisBillhead.areaSign");
        return super.getLangMap();
    }
}
