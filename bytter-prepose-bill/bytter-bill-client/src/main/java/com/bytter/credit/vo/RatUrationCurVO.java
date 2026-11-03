package com.bytter.credit.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 授信占用情况DTO
 *
 * @author liyh
 */
public class RatUrationCurVO implements Serializable {
    /**
     * 占用编号
     */
    private String id;
    /**
     * 占用编号
     */
    private String urationCode;
    /**
     * 业务名称
     */
    private String type;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 占用业务类别
     */
    private String sysCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 单位代码
     */
    private String billId;
    /**
     * 占用授信类型
     */
    private String ratType;
    /**
     * 授信额度编号
     */
    private String ratCode;
    /**
     * 授信额度编号
     */
    private String disCode;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 品种标识
     */
    private String itemCode;
    /**
     * 授信币别
     */
    private String curCode;
    /**
     * 业务币别
     */
    private String initCurCode;
    /**
     * 是否启用敞口
     */
    private String isOpenRat;
    /**
     * 业务金额
     */
    private BigDecimal billMoney;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 占用状态
     */
    private Integer status;
    /**
     * 占用金额
     */
    private BigDecimal money;
    /**
     * 折算后金额
     */
    private BigDecimal convertPreMoney;
    // 保证金币别
    private String bondCurId;
    // 保证金金额
    private BigDecimal bondMoney;
    // 是否更新所有的综合授信额度
    private boolean updateAll = true;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrationCode() {
        return urationCode;
    }
    public void setUrationCode(String urationCode) {
        this.urationCode = urationCode;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
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
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getInitCurCode() {
        return initCurCode;
    }
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getConvertPreMoney() {
        return convertPreMoney;
    }
    public void setConvertPreMoney(BigDecimal convertPreMoney) {
        this.convertPreMoney = convertPreMoney;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public boolean isUpdateAll() {
        return updateAll;
    }
    public void setUpdateAll(boolean updateAll) {
        this.updateAll = updateAll;
    }
}
