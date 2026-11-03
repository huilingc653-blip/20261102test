package com.bytter.supply.domain.common.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @Title: ScfInstructionVO
 * @Description: 指令管理VO
 */
@ApiModel(value = "ScfInstructionVO", description = "指令管理VO")
public class ScfInstructionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    /** 指令编码 */
    @BtCompositeSearch(fieldName = "t.instr_code")
    @ApiModelProperty("指令编码")
    private String instrCode;

    /** 指令类型 */
    @ApiModelProperty("指令类型")
    @BtCompositeSearch(fieldName = "t.instr_type")
    private String instrType;

    @ApiModelProperty("指令类型名称")
    @BtCompositeSearch(fieldName = "t.instr_type")
    private String instrTypeName;

    /** 供应链平台ID */
    @BtCompositeSearch(fieldName = "t.platform_id")
    @ApiModelProperty("供应链平台ID")
    private String platformId;

    @BtCompositeSearch(fieldName = "t.platform_name")
    @ApiModelProperty("供应链平台名称")
    private String platformName;

    /** 融资产品 */
    @ApiModelProperty("融资产品")
    @BtCompositeSearch(fieldName = "t.product_id")
    private String productId;

    @BtCompositeSearch(fieldName = "t.product_name")
    @ApiModelProperty("融资产品名称")
    private String productName;

    /** 直联接口ID */
    @BtCompositeSearch(fieldName = "t.direct_if_id")
    @ApiModelProperty("直联接口ID")
    private String directIfId;

    /** 债权凭证编码 */
    @BtCompositeSearch(fieldName = "t.voucher_code")
    @ApiModelProperty("债权凭证编码")
    private String voucherCode;

    /** 关联编号（应付：债权凭证凭证号；应收：转让记录编号/融资单号） */
    @BtCompositeSearch(fieldName = "t.relate_no")
    @ApiModelProperty("关联编号（应付：债权凭证凭证号；应收：转让记录编号/融资单号）")
    private String relateNo;

    /** 开立单位ID */
    @BtCompositeSearch(fieldName = "t.corp_id")
    @ApiModelProperty("开立单位ID")
    private String corpId;

    @BtCompositeSearch(fieldName = "t.corp_name")
    @ApiModelProperty("开立单位名称")
    private String corpName;

    /** 币别ID */
    @ApiModelProperty("币别ID")
    @BtCompositeSearch(fieldName = "t.cur_id")
    private String curId;

    @ApiModelProperty("币别名称")
    @BtCompositeSearch(fieldName = "t.cur_name")
    private String curName;

    /** 指令金额 */
    @BtCompositeSearch(fieldName = "t.instr_amt")
    @ApiModelProperty("指令金额")
    private BigDecimal instrAmt;

    /** 指令日期 */
    @BtCompositeSearch(fieldName = "t.instr_date")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("指令日期")
    private Date instrDate;

    /** 指令日期-起 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("指令日期-起")
    private Date instrDateStart;

    /** 指令日期-止 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("指令日期-止")
    private Date instrDateEnd;

    /** 到期日 */
    @BtCompositeSearch(fieldName = "t.expire_date")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日")
    private Date expireDate;

    /** 交易状态 */
    @BtCompositeSearch(fieldName = "t.trans_status")
    @ApiModelProperty("交易状态")
    private String transStatus;

    /** 交易原因 */
    @ApiModelProperty("交易原因")
    @BtCompositeSearch(fieldName = "t.trans_reason")
    private String transReason;

    /** 应付应收标识(0为应付，1为应收) */
    @ApiModelProperty("应付应收标识（0=应付，1=应收）")
    private String sign;

    /** 接收单位 */
    @ApiModelProperty("接收单位")
    private String receiveCompany;

    /** 资金方 */
    @ApiModelProperty("资金方")
    private String funderName;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty("修改人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInstrCode() { return instrCode; }
    public void setInstrCode(String instrCode) { this.instrCode = instrCode; }

    public String getInstrType() { return instrType; }
    public void setInstrType(String instrType) { this.instrType = instrType; }

    public String getInstrTypeName() { return instrTypeName; }
    public void setInstrTypeName(String instrTypeName) { this.instrTypeName = instrTypeName; }

    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDirectIfId() { return directIfId; }
    public void setDirectIfId(String directIfId) { this.directIfId = directIfId; }

    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public String getRelateNo() { return relateNo; }
    public void setRelateNo(String relateNo) { this.relateNo = relateNo; }

    public String getCorpId() { return corpId; }
    public void setCorpId(String corpId) { this.corpId = corpId; }

    public String getCorpName() { return corpName; }
    public void setCorpName(String corpName) { this.corpName = corpName; }

    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    public String getCurName() { return curName; }
    public void setCurName(String curName) { this.curName = curName; }

    public BigDecimal getInstrAmt() { return instrAmt; }
    public void setInstrAmt(BigDecimal instrAmt) { this.instrAmt = instrAmt; }

    public Date getInstrDate() { return instrDate; }
    public void setInstrDate(Date instrDate) { this.instrDate = instrDate; }

    public Date getInstrDateStart() { return instrDateStart; }
    public void setInstrDateStart(Date instrDateStart) { this.instrDateStart = instrDateStart; }

    public Date getInstrDateEnd() { return instrDateEnd; }
    public void setInstrDateEnd(Date instrDateEnd) { this.instrDateEnd = instrDateEnd; }

    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }

    public String getTransStatus() { return transStatus; }
    public void setTransStatus(String transStatus) { this.transStatus = transStatus; }

    public String getTransReason() { return transReason; }
    public void setTransReason(String transReason) { this.transReason = transReason; }

    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }

    public String getReceiveCompany() { return receiveCompany; }
    public void setReceiveCompany(String receiveCompany) { this.receiveCompany = receiveCompany; }

    public String getFunderName() { return funderName; }
    public void setFunderName(String funderName) { this.funderName = funderName; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
