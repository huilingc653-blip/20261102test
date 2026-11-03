package com.bytter.credit.domain.vm;
import java.io.Serializable;
import java.util.List;
/**
 * 授信公共页面详情
 * 
 * @author yangl
 *
 */
public class RatBkrationCommonDTO implements Serializable {
    /** 授信录入主键ID **/
    private String id;
    /** 授信分配主键ID **/
    private String disCode;
    /** 授信编号 */
    private String ratCode;
    /** 单位代码 */
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 授信种类 */
    private String ratType;
    /** 银行类型 */
    private String bankType;
    /** 银行类型名称 */
    private String typeName;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 敞口授信 */
    private String openSign;
    /** 授信模式 */
    private String ratMode;
    /** 授信币种 */
    private String curCode;
    /** 授信币种 */
    private String curName;
    /** 授信开始时间 */
    private java.util.Date beginDate;
    /** 授信结束时间 */
    private java.util.Date endDate;
    /** 授信金额 */
    private java.math.BigDecimal money;
    /** 敞口额度 */
    private java.math.BigDecimal openMoney;
    /** 剩余授信额度 */
    private java.math.BigDecimal unUserMoney;
    /** 剩余授信额度 */
    private java.math.BigDecimal disMoney;
    /** 授信合同号 */
    private String contractNo;
    /**
     * 授信录入综合品种明细数据
     */
    private List<RatBkRationDtlVm> compRationDtls;
    /**
     * 授信录入单品种明细数据
     */
    private List<RatBkRationDtlVm> singRationDtls;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
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
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
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
    public String getOpenSign() {
        return openSign;
    }
    public void setOpenSign(String openSign) {
        this.openSign = openSign;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
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
    public java.util.Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public java.math.BigDecimal getUnUserMoney() {
        return unUserMoney;
    }
    public void setUnUserMoney(java.math.BigDecimal unUserMoney) {
        this.unUserMoney = unUserMoney;
    }
    public java.math.BigDecimal getDisMoney() {
        return disMoney;
    }
    public void setDisMoney(java.math.BigDecimal disMoney) {
        this.disMoney = disMoney;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public List<RatBkRationDtlVm> getCompRationDtls() {
        return compRationDtls;
    }
    public void setCompRationDtls(List<RatBkRationDtlVm> compRationDtls) {
        this.compRationDtls = compRationDtls;
    }
    public List<RatBkRationDtlVm> getSingRationDtls() {
        return singRationDtls;
    }
    public void setSingRationDtls(List<RatBkRationDtlVm> singRationDtls) {
        this.singRationDtls = singRationDtls;
    }
    public Integer getCompItemSize() {
        return compItemSize;
    }
    public void setCompItemSize(Integer compItemSize) {
        this.compItemSize = compItemSize;
    }
}
