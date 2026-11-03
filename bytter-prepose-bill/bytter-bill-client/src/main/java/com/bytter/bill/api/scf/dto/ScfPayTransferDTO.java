package com.bytter.bill.api.scf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 应收债权凭证转让暂存入参（付款方式52：前端提交付款单前调用 saveTransfer 保存转让数据）
 *
 * @author zhukr
 * @since 2026/8/25
 */
@Data
public class ScfPayTransferDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转让标识：1 单笔（一张凭证转让一家），2 多对一（多张凭证转让一家） */
    @ApiModelProperty("转让标识：1 单笔，2 多对一")
    private String transferFlag;

    /** 应收凭证ID集合（单笔取第一个，多对一全部），多个用逗号分隔 */
    @ApiModelProperty("应收凭证ID集合，多个用逗号分隔")
    private String voucherIds;

    /** 本次变更前已保存的转让草稿ID集合（再次进入变更时释放原转让暂存，再保存当前数据），多个用逗号分隔 */
    @ApiModelProperty("本次变更前已保存的转让草稿ID集合，多个用逗号分隔")
    private String transferIds;

    @ApiModelProperty("转让单位id")
    private String issueTransferCompanyId;

    @ApiModelProperty("转让单位名称")
    private String issueTransferCompany;

    /** 接收方企业ID */
    @ApiModelProperty("接收方企业ID")
    private String receiverCorpId;

    /** 接收方名称 */
    @ApiModelProperty("接收方名称")
    private String receiverName;

    /** 接收方统一社会信用代码 */
    @ApiModelProperty("接收方统一社会信用代码")
    private String receiverCreditCode;

    /** 接收方收款账号 */
    @ApiModelProperty("接收方收款账号")
    private String receiveAccount;

    /** 转让金额（单笔转让可部分金额；多对一为全部转让，取凭证金额） */
    @ApiModelProperty("转让金额")
    private BigDecimal transferMoney;

    /** 转让日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("转让日期")
    private Date transferDate;

    /** 用途 */
    @ApiModelProperty("用途")
    private String purpose;

    /** 线上线下标志（0=线下，1=线上） */
    @ApiModelProperty("线上线下标志")
    private String onlineSign;

    /** 供应链金融平台ID */
    @ApiModelProperty("供应链金融平台ID")
    private String platformId;

    /** 融资产品ID */
    @ApiModelProperty("融资产品ID")
    private String productId;

    /** 付款单ID */
    @ApiModelProperty("付款单ID")
    private String payId;

    /** 付款单号 */
    @ApiModelProperty("付款单号")
    private String payNo;

    /** 创建人登录名 */
    @ApiModelProperty("创建人登录名")
    private String createBy;
}
