package com.bytter.supply.domain.receivable.dto;

import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfoDTO
 * @Description: 应收债权凭证信息DTO
 * @since 2026/6/30
 */
@Data
@ApiModel(value = "ScfReceivableVoucherInfoDTO", description = "应收债权凭证信息DTO")
public class ScfReceivableVoucherInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("单据编号(系统自动创建AR+年月日+3位流水号)")
    private String voucherNo;

    @ApiModelProperty("凭证来源")
    private String voucherSource;

    @ApiModelProperty("凭证编码")
    private String voucherCode;

    @DecimalMin(value = "0.00", inclusive = false, message = "凭证金额必须大于0")
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherAmt;

    @ApiModelProperty("平台ID")
    private String platformId;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("产品ID")
    private String productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("单位代码")
    private String corpId;

    @ApiModelProperty("单位名称")
    private String corpName;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签约日期")
    private Date signDate;

    @ApiModelProperty("凭证类型")
    private String voucherType;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日期")
    private Date expireDate;

    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    private String interestMode;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("承诺付款日")
    private Date promisePayDate;

    @ApiModelProperty("流转单位")
    private String transferCompany;

    @ApiModelProperty("流转单位ID")
    private String transferCompanyId;

    @ApiModelProperty("业务状态")
    private String voucherStatus;

    @ApiModelProperty("单据状态")
    private String approvalStatus;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("作废时间")
    private Date cancelDate;

    @ApiModelProperty("作废原因")
    private String cancelReason;

    @ApiModelProperty("核心企业ID")
    private String coreCompanyId;

    @ApiModelProperty("核心企业")
    private String coreCompanyName;

    @ApiModelProperty("币别ID")
    private String curId;

    @ApiModelProperty("币别名称")
    private String curName;

    @ApiModelProperty("出让人公司")
    private String issueTransferCompany;

    @ApiModelProperty("出让人公司ID")
    private String issueTransferCompanyId;

    @ApiModelProperty("根凭证编号")
    private String sourceVoucherCode;

    @ApiModelProperty("上级凭证编号")
    private String forehandVoucherCode;

    @ApiModelProperty("线上签署标识（0=线下，1=线上）")
    private String onlineSign;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmt;

    @ApiModelProperty("合同日期")
    private Date contractDate;

    @ApiModelProperty("合同附件ID")
    private String contractFileId;

    @ApiModelProperty("发票附件ID")
    private String invoiceFileId;

    @ApiModelProperty("是否允许拆分(0:否, 1:是)")
    private String allowSplit;

    @ApiModelProperty("提交状态：提交:submit ; save : 暂存")
    private String sendStatus;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty("修改人")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("签收操作")
    private String signOperation;

    @ApiModelProperty("发票信息")
    private List<ScfInvoiceInfoDTO> invoiceInfo;

}
