package com.bytter.accountReceivable.docManage.vo;

import com.bytter.accountReceivable.constant.AccountReceivableConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Data
public class DocManageWriteOffVo extends ExcelModel implements Serializable, IBusinessKey {

    @BtCompositeSearch(fieldName = "dm.id")
    private String id;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "dm.corp_id")
    @Corp(inject = "corpName")
    private String corpId;

    @Excel(name = "*单位", isImportField = "true")
    private String corpName;
    /**
     * 单据编号
     */
    @BtCompositeSearch(fieldName = "dm.code")
    private String code;
    /**
     * 业务类型
     */
    @BtCompositeSearch(fieldName = "dm.batch_bus_type")
    private String batchBusType;
    @Excel(name = "*业务类型", isImportField = "true")
    private String batchBusTypeName;
    /**
     * 单据来源
     */
    @BtCompositeSearch(fieldName = "dm.source")
    @Dict(dictGroup = "writeOff.docSource", inject = "sourceName")
    private String source;

    @BtCompositeSearch(fieldName = "dm.source")
    private String sourceName;
    /**
     * 结算方式
     */
    @BtCompositeSearch(fieldName = "dm.voucher_type")
    @Dict(dictGroup = "account.receivable.doc.voucherType", inject = "voucherTypeName")
    private String voucherType;
    @Excel(name = "*结算方式", isImportField = "true")
    private String voucherTypeName;
    /**
     * 核销处理-结算方式
     */
    @Dict(dictGroup = "basic.paymentMeans", inject = "paymentMeansName")
    private String paymentMeans;
    private String paymentMeansName;
    /**
     * 单据日期
     */
    @Excel(name = "*单据日期", isImportField = "true")
    @BtCompositeSearch(fieldName = "dm.receipt_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;
    /**
     * 到期日期
     */
    @Excel(name = "*到期日期", isImportField = "true")
    @BtCompositeSearch(fieldName = "dm.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 账龄起算日
     */
    @Excel(name = "*账龄起算日", isImportField = "true")
    @BtCompositeSearch(fieldName = "dm.aging_date")
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
    @Excel(name = "*应收金额", isImportField = "true")
    private BigDecimal receivableMoney;
    private BigDecimal executeMoney;
    /**
     * 应收币种
     */
    @BtCompositeSearch(fieldName = "dm.cur_id")
    @Currency(inject = "curName")
    private String curId;
    @Excel(name = "*应收币种", isImportField = "true")
    private String curName;
    private String enCode;

    private BigDecimal exchangeRate;
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
    private String newWriteOffStatus;
    /**
     * 客商名称
     */
    @BtCompositeSearch(fieldName = "dm.external_name")
    private String externalName;
    /**
     * 客商id
     */
    private String externalId;
    /**
     * 客商编号
     */
    @Excel(name = "*客商编号", isImportField = "true")
    private String externalCode;
    /**
     * 客商类型
     */
    @BtCompositeSearch(fieldName = "dm.external_type")
    @Dict(dictGroup = "external.type", inject = "externalTypeName")
    private String externalType;
    private String externalTypeName;
    /**
     * 合同编号
     */
    @BtCompositeSearch(fieldName = "dm.contract_code")
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
    @BtCompositeSearch(fieldName = "dm.write_off_status")
    @Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
    private String writeOffStatus;
    private String writeOffStatusName;
    /**
     * 核销类型
     */
    @BtCompositeSearch(fieldName = "dm.write_off_type")
    @Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
    private String writeOffType;
    private String writeOffTypeName;

    @BtCompositeSearch(fieldName = "dm.status")
    private String status;
    /**
     * 资金预算
     */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;

    /**
     * 核销相关联的id
     */
    private String writeOffId;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    private BigDecimal newWriteOffBal;

    /**发票附件*/
    private String attachmentId;

    /**合同附件*/
    private String contractAttachmentId;
    /**变更作废原因标题*/
    @Dict(dictGroup = "account.receivable.doc.reasonTitle", inject = "reasonTitleName")
    private String reasonTitle;
    private String reasonTitleName;
    /**变更作废原因内容*/
    private String reasonContent;
    /**变更作废状态，为空则为工作流的审批标准状态*/
    private String reasonStatus;


    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    public String getCheckboxHide() {
        if(this.getStatus()!=null && AccountReceivableConstant.STATUS_WAIT_CHECK.equals(this.getStatus()) ){
            return "1";
        }
        return checkboxHide;
    }


    @Override
    public String getBusinessKey() {
        return getId();
    }


    @Override
    public String getBeanName() {
        return "DocManageService";
    }
}
