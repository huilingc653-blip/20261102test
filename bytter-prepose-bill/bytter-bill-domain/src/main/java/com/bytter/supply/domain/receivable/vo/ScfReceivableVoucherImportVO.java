package com.bytter.supply.domain.receivable.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherImportVO
 * @Description: 应收债权凭证Excel导入VO
 * @since 2026/7/22
 */
@ApiModel(value = "ScfReceivableVoucherImportVO", description = "应收债权凭证Excel导入VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScfReceivableVoucherImportVO extends ExcelModel implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    @Excel(name = "*凭证编号", isImportField = "true")
    @ApiModelProperty("凭证编号")
    private String voucherCode;

    @Excel(name = "*凭证金额", isImportField = "true")
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherAmt;

    @Excel(name = "*平台名称", isImportField = "true")
    @ApiModelProperty("平台名称")
    private String platformName;

    @Excel(name = "*产品名称", isImportField = "true")
    @ApiModelProperty("产品名称")
    private String productName;

    @Excel(name = "*持有单位", isImportField = "true")
    @ApiModelProperty("持有单位")
    private String corpName;

    private String corpId;
    private String platformId;
    private String productId;
    private String curId;
    private String coreCompanyId;
    private String issueTransferCompanyId;

    @Excel(name = "*签收日期", format = "yyyy-MM-dd", isImportField = "true")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签收日期")
    private Date signDate;

    @Excel(name = "*到期日期", format = "yyyy-MM-dd", isImportField = "true")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日期")
    private Date expireDate;

    @Excel(name = "*凭证类型", isImportField = "true")
    @ApiModelProperty("凭证类型")
    private String voucherType;

    @Excel(name = "核心企业", isImportField = "true")
    @ApiModelProperty("核心企业")
    private String coreCompanyName;

    @Excel(name = "*币别名称", isImportField = "true")
    @ApiModelProperty("币别名称")
    private String curName;

    @Excel(name = "*开立单位", isImportField = "true")
    @ApiModelProperty("开立单位")
    private String issueTransferCompany;


    @Excel(name = "流转单位", isImportField = "true")
    @ApiModelProperty("流转单位")
    private String transferCompany;

    @ApiModelProperty("流转单位ID")
    private String transferCompanyId;

    @ApiModelProperty("线上线下标志")
    @Excel(name = "*线上线下标志", isImportField = "true")
    private String onlineSign;

    @ApiModelProperty("是否允许拆分(0:否, 1:是)")
    @Excel(name = "*是否允许拆分", isImportField = "true")
    private String allowSplit;

    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    @Excel(name = "*融资付息方式", isImportField = "true")
    private String interestMode;

    @Excel(name = "原始凭证编号", isImportField = "true")
    @ApiModelProperty("原始凭证编号")
    private String sourceVoucherCode;

    @Excel(name = "前置凭证编号", isImportField = "true")
    @ApiModelProperty("前置凭证编号")
    private String forehandVoucherCode;

    @Excel(name = "合同编号", isImportField = "true")
    @ApiModelProperty("合同编号")
    private String contractCode;

    @Excel(name = "合同名称", isImportField = "true")
    @ApiModelProperty("合同名称")
    private String contractName;

    @Excel(name = "合同金额", isImportField = "true")
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmt;

    @Excel(name = "合同日期", format = "yyyy-MM-dd", isImportField = "true")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("合同日期")
    private Date contractDate;

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public String getBeanName() {
        return "scfReceivableVoucherExcelService";
    }
}
