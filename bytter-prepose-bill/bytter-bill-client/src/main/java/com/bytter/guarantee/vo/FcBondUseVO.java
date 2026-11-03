package com.bytter.guarantee.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author luxy
 * @Title: FcBondUseVO
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/7/17 17:42
 */
public class FcBondUseVO implements Serializable {
    /**
     * 单位id
     */
    @Corp(inject = "corpName")
    private String corpId;
    /**单位代码*/
    private String corpCode;
    /**
     * 单位名称
     */
    private String corpName;
    /** 银行Code*/
    private String bankCode;
    /** 存入银行 */
    @Excel(name = "*存入银行")
    @BtCompositeSearch(fieldName = "n.bank_name")
    private String bankName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "n.cur_Id")
    private String curName;
    /** 存出总额 */
    private BigDecimal bondSumMoney;
    /** 占用总额 */
    private BigDecimal applySumMoney;
    /** 可用总额 */
    private BigDecimal canUseSumMoney;
    /** 占用类型 */
    private String itemType;
    /** 占用金额 */
    private BigDecimal applyMoney;
    /**
     * 占用明细
     */
    private List<FcBondUseDtlVO> applyDtls;
    private Integer dtlSize;
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
    public BigDecimal getApplySumMoney() {
        return applySumMoney;
    }
    public void setApplySumMoney(BigDecimal applySumMoney) {
        this.applySumMoney = applySumMoney;
    }
    public BigDecimal getCanUseSumMoney() {
        return canUseSumMoney;
    }
    public void setCanUseSumMoney(BigDecimal canUseSumMoney) {
        this.canUseSumMoney = canUseSumMoney;
    }
    public List<FcBondUseDtlVO> getApplyDtls() {
        return applyDtls;
    }
    public void setApplyDtls(List<FcBondUseDtlVO> applyDtls) {
        this.applyDtls = applyDtls;
    }
    public BigDecimal getBondSumMoney() {
        return bondSumMoney;
    }
    public void setBondSumMoney(BigDecimal bondSumMoney) {
        this.bondSumMoney = bondSumMoney;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public Integer getDtlSize() {
        return dtlSize;
    }
    public void setDtlSize(Integer dtlSize) {
        this.dtlSize = dtlSize;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
}
