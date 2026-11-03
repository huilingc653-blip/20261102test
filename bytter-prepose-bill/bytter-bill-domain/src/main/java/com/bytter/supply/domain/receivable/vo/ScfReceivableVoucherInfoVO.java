package com.bytter.supply.domain.receivable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ztd
 * @description 应收债权凭证信息VO
 * @since 2026/7/3
 */
@ApiModel(value = "ScfReceivableVoucherInfoVO", description = "应收债权凭证信息VO")
@Data
public class ScfReceivableVoucherInfoVO implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("凭证编号")
    @BtCompositeSearch(fieldName = "t.voucher_no")
    private String voucherNo;

    @ApiModelProperty("凭证来源")
    private String voucherSource;

    @ApiModelProperty("凭证编码")
    @BtCompositeSearch(fieldName = "t.voucher_code")
    private String voucherCode;

    @ApiModelProperty("凭证金额")
    @BtCompositeSearch(fieldName = "t.voucher_amt")
    private BigDecimal voucherAmt;

    @ApiModelProperty("平台ID")
    @BtCompositeSearch(fieldName = "t.platform_id")
    private String platformId;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("融资产品ID")
    @BtCompositeSearch(fieldName = "t.product_id")
    private String productId;

    @ApiModelProperty("融资产品名称")
    private String productName;

    @ApiModelProperty("持有单位")
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;

    @ApiModelProperty("单位名称")
    private String corpName;

    @ApiModelProperty("签收日期")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t.sign_date")
    private Date signDate;

    @ApiModelProperty("签收操作")
    private String signOperation;

    @ApiModelProperty("凭证类型")
    private String voucherType;

    @ApiModelProperty("凭证类型名称")
    private String voucherTypeName;

    @ApiModelProperty("到期日期")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t.expire_date")
    private Date expireDate;

    @ApiModelProperty("融资付息方式(1:卖方付息,2:买方付息)")
    @BtCompositeSearch(fieldName = "t.interest_mode")
    private String interestMode;

    @ApiModelProperty("承诺付款日")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t.promise_pay_date")
    private Date promisePayDate;

    @ApiModelProperty("流转单位")
    @BtCompositeSearch(fieldName = "t.transfer_company")
    private String transferCompany;

    @ApiModelProperty("流转单位ID")
    @BtCompositeSearch(fieldName = "t.transfer_company_id")
    private String transferCompanyId;

    @ApiModelProperty("业务状态")
    @BtCompositeSearch(fieldName = "t.voucher_status")
    private String voucherStatus;

    @ApiModelProperty("业务名称")
    private String voucherStatusName;

    @ApiModelProperty("审批状态")
    @BtCompositeSearch(fieldName = "t.approval_status")
    private String approvalStatus;

    @ApiModelProperty("审批状态名称")
    private String approvalStatusName;

    @ApiModelProperty("作废时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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

    @ApiModelProperty("原始凭证编号")
    private String sourceVoucherCode;

    @ApiModelProperty("前置凭证编号")
    private String forehandVoucherCode;

    @ApiModelProperty("线上签署标识")
    @BtCompositeSearch(fieldName = "t.online_sign")
    private String onlineSign;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmt;

    @ApiModelProperty("合同日期")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contractDate;

    @ApiModelProperty("合同附件ID")
    private String contractFileId;

    @ApiModelProperty("发票附件ID")
    private String invoiceFileId;

    @ApiModelProperty("是否允许拆分(0:否, 1:是)")
    private String allowSplit;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createDate;

    @ApiModelProperty("发票信息")
    private List<ScfInvoiceInfoDTO> invoiceInfo;

    @ApiModelProperty("转让记录")
    List<ScfReceivableTransferInfoDTO> transferInfo;

    @ApiModelProperty("融资记录")
    List<ScfLoanInfoDTO> loanInfo;

    @ApiModelProperty("到期收款记录")
    List<ScfReceivablePaymentInfoDTO> paymentInfo;

    @ApiModelProperty("链路签收展示节点及前手凭证")
    private List<ScfReceivableVoucherInfoVO> children;

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
