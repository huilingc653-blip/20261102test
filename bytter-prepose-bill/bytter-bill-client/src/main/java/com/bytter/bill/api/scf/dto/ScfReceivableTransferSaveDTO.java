package com.bytter.bill.api.scf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 应收债权凭证转让暂存入参（跨模块调用债权凭证转让接口发起暂存）
 *
 * @author ztd
 * @since 2026/8/21
 */
@Data
public class ScfReceivableTransferSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private String id;

    /** 转让记录编号 */
    @ApiModelProperty("转让记录编号")
    private String transferNo;

    @ApiModelProperty("转让批次号")
    private String transferBatchNo;

    /** 债权凭证编号 */
    @ApiModelProperty("债权凭证编号")
    private String voucherCode;

    /** 转让状态 */
    @ApiModelProperty("转让状态")
    private String transferStatus;

    /** 平台 */
    @ApiModelProperty("平台")
    private String platformId;

    /** 平台名称*/
    @ApiModelProperty("平台名称")
    private String platformName;

    /** 融资产品 */
    @ApiModelProperty("融资产品")
    private String productId;

    /** 融资产品名称 */
    @ApiModelProperty("融资产品名称")
    private String productName;

    /** 开立/转让单位 */
    @ApiModelProperty("开立/转让单位")
    private String issueTransferCompany;

    /** 开立/转让企业id */
    @ApiModelProperty("开立/转让企业id")
    private String issueTransferCompanyId;

    /** 币别 */
    @ApiModelProperty("币别")
    private String curId;

    /** 币别名称 */
    @ApiModelProperty("币别名称")
    private String curName;

    /** 接收单位 */
    @ApiModelProperty("接收单位")
    private String receiveCompany;

    /** 接收单位id */
    @ApiModelProperty("接收单位id")
    private String receiveCompanyId;

    /** 接收方统一社会信用代码 */
    @ApiModelProperty("接收方统一社会信用代码")
    private String receiverCreditCode;

    /** 转让日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("转让日期")
    private Date transferDate;

    /** 转让金额 */
    @DecimalMin(value = "0.00", inclusive = false, message = "转让金额必须大于0")
    @ApiModelProperty("转让金额")
    private BigDecimal transferAmt;

    /** 用途 */
    @ApiModelProperty("用途")
    private String purpose;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    /** 转让标识: 1.一对多，凭证拆分转让多家企业;2.多对一,多张凭证转让一家企业 */
    @ApiModelProperty("转让标识: 1.一对多，凭证拆分转让多家企业;2.多对一,多张凭证转让一家企业")
    private String transferFlag;

    /** 提交状态：提交:submit ; 暂存:save */
    @ApiModelProperty("提交状态：提交:submit ; 暂存:save")
    private String sendStatus;

    /** 应收凭证id集合 */
    @ApiModelProperty("应收凭证id集合")
    private String voucherIds;

    /** 批量转让接收方信息 */
    @ApiModelProperty("批量转让接收方信息")
    List<ScfReceivableTransferReceiveDTO> TransferReceive;
}
