package com.bytter.supply.domain.payable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: ScfPayableInstructionDTO
 * @Description: 应付指令创建数据传输对象——从应付凭证实体+源表名称查询构建
 */
@ApiModel("应付指令创建DTO")
public class ScfPayableInstructionDTO implements Serializable {

    @ApiModelProperty("债权凭证编码")
    private String voucherCode;

    @ApiModelProperty("关联编号（应付债权凭证凭证号，用于精确关联对应业务表）")
    private String relateNo;

    @ApiModelProperty("平台ID")
    private String platformId;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("产品ID")
    private String productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("开立单位ID")
    private String corpId;

    @ApiModelProperty("开立单位名称")
    private String corpName;

    @ApiModelProperty("币种ID")
    private String curId;

    @ApiModelProperty("币种名称")
    private String curName;

    @ApiModelProperty("凭证金额")
    private BigDecimal voucherMoney;

    @ApiModelProperty("到期日期")
    private Date expireDate;

    @ApiModelProperty("创建人")
    private String createBy;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getRelateNo() {
        return relateNo;
    }

    public void setRelateNo(String relateNo) {
        this.relateNo = relateNo;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public BigDecimal getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(BigDecimal voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
