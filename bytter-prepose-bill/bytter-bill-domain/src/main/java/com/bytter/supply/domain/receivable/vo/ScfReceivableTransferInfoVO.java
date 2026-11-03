package com.bytter.supply.domain.receivable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoVO
 * @Description: 债权凭证转让信息VO
 * @since 2026/7/8
 */
@ApiModel(value = "ScfReceivableTransferInfoVO", description = "债权凭证转让信息VO")
@Data
public class ScfReceivableTransferInfoVO implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("转让记录编号")
    @BtCompositeSearch(fieldName = "t.transfer_no")
    private String transferNo;

    @ApiModelProperty("转让批次号")
    @BtCompositeSearch(fieldName = "t.transfer_batch_no")
    private String transferBatchNo;

    /** 债权凭证编号 */
    @ApiModelProperty("债权凭证编号")
    @BtCompositeSearch(fieldName = "t.voucher_code")
    private String voucherCode;

    /** 转让审批状态 */
    @ApiModelProperty("转让审批状态")
    @BtCompositeSearch(fieldName = "t.transfer_status")
    private String transferStatus;
    @ApiModelProperty("转让审批状态名称")
    private String transferStatusName;

    @JsonIgnore
    private String businessKey;

    /** 平台 */
    @ApiModelProperty("平台")
    @BtCompositeSearch(fieldName = "t.platform_id")
    private String platformId;

    /** 平台 */
    @ApiModelProperty("平台名称")
    @BtCompositeSearch(fieldName = "t.platform_name")
    private String platformName;

    /** 融资产品 */
    @ApiModelProperty("融资产品")
    @BtCompositeSearch(fieldName = "t.product_id")
    private String productId;

    /** 融资产品名称 */
    @ApiModelProperty("融资产品名称")
    private String productName;

    /** 持有单位 */
    @ApiModelProperty("持有单位")
    private String holdCompany;

    /** 签收日期 */
    @ApiModelProperty("签收日期")
    private Date signDate;

    /** 签收日期-起 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签收日期-起")
    private Date signDateStart;

    /** 签收日期-止 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签收日期-止")
    private Date signDateEnd;

    /** 到期日 */
    @ApiModelProperty("到期日")
    private Date expireDate;

    /** 开立/转让单位 */
    @ApiModelProperty("开立/转让单位")
    @BtCompositeSearch(fieldName = "t.issue_transfer_company")
    private String issueTransferCompany;

    /** 开立/转让企业id */
    @ApiModelProperty("开立/转让企业id")
    @BtCompositeSearch(fieldName = "t.issue_transfer_company_id")
    private String issueTransferCompanyId;

    /** 币别 */
    @ApiModelProperty("币别")
    private String curId;

    /** 币别名称 */
    @ApiModelProperty("币别名称")
    private String curName;

    /** 凭证金额 */
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherAmt;

    /** 核心企业 */
    @ApiModelProperty("核心企业")
    private String coreCompanyId;

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
    @BtCompositeSearch(fieldName = "t.transfer_date")
    private Date transferDate;

    /** 转让日期-起 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("转让日期-起")
    private Date transferDateStart;

    /** 转让日期-止 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("转让日期-止")
    private Date transferDateEnd;

    /** 转让金额 */
    @ApiModelProperty("转让金额")
    @BtCompositeSearch(fieldName = "t.transfer_amt")
    private BigDecimal transferAmt;

    /** 用途 */
    @ApiModelProperty("用途")
    private String purpose;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;

    /** 转让标识: 1.一对多，凭证拆分转让多家企业; 2.多对一，多张凭证转让一家企业 */
    @ApiModelProperty("转让标识: 1.一对多，凭证拆分转让多家企业; 2.多对一，多张凭证转让一家企业")
    private String transferFlag;

    /** 凭证信息 */
    @ApiModelProperty("凭证信息")
    private ScfReceivableVoucherInfoVO voucherInfo;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty("修改人")
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty("修改时间")
    private Date updateDate;

    @Override
    public String getBusinessKey() {
        return businessKey != null ? businessKey : getId();
    }
}
