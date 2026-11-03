package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核门店维护VO
 * @date 2019/5/6 9:13
 */
public class RasShopVO extends ExcelModel implements Serializable{
    private String id;
    /**门店编码*/
    @Excel(name = "*门店编码")
    @BtCompositeSearch(fieldName = "code")
    private String code;
    /**门店名称*/
    @Excel(name = "*门店名称")
    @BtCompositeSearch(fieldName = "name")
    private String name;
    /**单位id*/
    @BtCompositeSearch(fieldName = "corp_Id")
    private String corpId;
    /**单位id*/
    private String corpCode;
    /**单位名称*/
    @Excel(name = "*单位名称")
    @BtCompositeSearch(fieldName = "sc.code")
    private String corpName;
    /**门店类别*/
    @Dict(dictGroup = "RAS_SHOP_TYPE",inject = "shopTypeName")
    private String shopType;
    @Excel(name = "*门店类别")
    @BtCompositeSearch(fieldName = "shop_type")
    private String shopTypeName;

    /**开始稽核日期*/
    @Excel(name = "*开始稽核日期",format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "audit_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date asDate;
    /**尾款阈值*/
    @Excel(name = "尾款阈值", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @BtCompositeSearch(fieldName = "end_check_money")
    private BigDecimal ecMoney;
    /**商户号*/
    @Excel(name = "商户号")
    @BtCompositeSearch(fieldName = "shop_no")
    private String shopNo;
    /**商户名称*/
    @Excel(name = "商户名称")
    @BtCompositeSearch(fieldName = "shop_name")
    private String shopName;
    /**备注*/
    @Excel(name = "备注")
    @BtCompositeSearch(fieldName = "rmk")
    private String rmk;
    /**状态*/
    @BtCompositeSearch(fieldName = "valid_sign")
    @Dict(dictGroup = "RAS_VALID_SIGN",inject = "vSignName")
    private String vSign;
    @BtCompositeSearch(fieldName = "valid_sign")
    private String vSignName;

    /**是否单位级别门店 1是，0否*/
    @Dict(dictGroup = "sf_yn",inject = "isCorpName")
    private String isCorp;
    @Excel(name = "*是否单位级别门店")
    @BtCompositeSearch(fieldName = "IS_CORP")
    private String isCorpName;

    /**对应其他系统门店编码*/
    @Excel(name = "对应其他系统门店编码")
    private String extemalNo;

    /** 是否有权限**/
    private String isAudit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        if(StringUtils.isNotEmpty(corpCode)
                &&StringUtils.isNotEmpty(corpName)){
            return "("+corpCode+")"+corpName;
        }
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public Date getAsDate() {
        return asDate;
    }

    public void setAsDate(Date asDate) {
        this.asDate = asDate;
    }

    public BigDecimal getEcMoney() {
        return ecMoney;
    }

    public void setEcMoney(BigDecimal ecMoney) {
        this.ecMoney = ecMoney;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getvSign() {
        return vSign;
    }

    public void setvSign(String vSign) {
        this.vSign = vSign;
    }

    public String getIsCorp() {
        return isCorp;
    }

    public void setIsCorp(String isCorp) {
        this.isCorp = isCorp;
    }

    public String getExtemalNo() {
        return extemalNo;
    }

    public void setExtemalNo(String extemalNo) {
        this.extemalNo = extemalNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getvSignName() {
        return vSignName;
    }

    public void setvSignName(String vSignName) {
        this.vSignName = vSignName;
    }

    public String getIsCorpName() {
        return isCorpName;
    }

    public void setIsCorpName(String isCorpName) {
        this.isCorpName = isCorpName;
    }
    
    public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

    @Override
    public String getBeanName() {
        return "rasShopService";
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

	
}
