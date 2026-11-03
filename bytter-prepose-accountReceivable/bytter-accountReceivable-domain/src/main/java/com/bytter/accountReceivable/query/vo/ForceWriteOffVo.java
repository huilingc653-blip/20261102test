package com.bytter.accountReceivable.query.vo;

import com.bytter.accountReceivable.docManage.vo.BudgetDocVO;
import com.bytter.accountReceivable.docManage.vo.InvoiceDocVo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ForceWriteOffVo {
    @Corp(inject = "corpName")
    private String corpId;

    private String corpName;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 业务类型
     */
    @BtCompositeSearch(fieldName = "dm.batch_bus_type")
    private String batchBusType;
    private String batchBusTypeName;

    /**
     * 单据来源
     */
    @Dict(dictGroup = "writeOff.docSource", inject = "sourceName")
    private String source;
    private String sourceName;
    /**
     * 结算方式
     */
    @Dict(dictGroup = "account.receivable.doc.voucherType", inject = "sourceName")
    private String voucherType;
    private String voucherTypeName;
    /**
     * 核销处理-结算方式
     */
    @Dict(dictGroup = "writeOff.force.paymentMeans", inject = "paymentMeansName")
    private String paymentMeans;
    private String paymentMeansName;
    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 账龄起算日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agingDate;
    /**
     * 收款账号
     */
    private String payeeAcc;
    /**
     * 收款账户名
     */
    private String payeeAccName;
    /**
     * 收款账户开户行名称
     */
    private String payeeBankName;
    /**
     * 收款账户开户行号
     */
    private String payeeBankCode;
    /**
     * 应收金额
     */
    private BigDecimal receivableMoney;
    /**
     * 应收币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 汇率类型
     */
    private String exRateType;
    /**
     * 折算金额
     */
    private String convertPreMoney;
    /**
     * 付款账号
     */
    private String payerAccNo;
    /**
     * 付款账号名称
     */
    private String payerAccName;
    /**
     * 付款开户行名称
     */
    private String payerBankName;
    /**
     * 付款开户行行号
     */
    private String payerBankNo;
    /**
     * 客商名称
     */
    private String externalName;
    /**
     * 客商id
     */
    private String externalId;
    /**
     * 客商编号
     */
    private String externalCode;
    /**
     * 客商类型
     */

    @Dict(dictGroup = "external.type", inject = "externalTypeName")
    private String externalType;
    private String externalTypeName;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同金额
     */
    private String contractMoney;
    /**
     * 合同日期
     */
    private Date contractDate;
    /**
     * 资金计划数据
     */
    private List<BudgetDocVO> budgetVOS;
    /**
     * 发票数据
     */
    private List<InvoiceDocVo> invoiceVos;
    /**
     * 核销状态
     */
    @Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
    private String writeOffStatus;
    private String writeOffStatusName;
    /**
     * 核销类型
     */
    @Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
    private String writeOffType;
    private String writeOffTypeName;

    private String status;

    /**
     * 核销相关联的id
     */
    private String writeOffId;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    /**强制核销类型*/
    @Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
    private String forceReasonType;
    private String forceReasonTypeName;
    /**本次核销前应收余额*/
    private String afterDocWriteOffBal;
    /**可核销应收币种金额*/
    private String relationConvertMoney;
    /**收入结算方式*/
    private String forcePaymentMeans;
    /**核销原因*/
    private String forceReason;
    /**核销前状态*/
    private String beforeWriteOffStatus;
    /**核销前金额*/
    private String beforeWriteOffBal;
}
