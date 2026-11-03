package com.bytter.accountReceivable.writeOff.vo;

import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 核销信息
 */
@Data
public class DocWriteOffRelationVo implements java.io.Serializable, IBusinessKey {

    /** 核销批次号 */
    @BtCompositeSearch(fieldName = "rel.batch_no")
    private String batchNo;
    /** 取消核销批次号 */
    private String cancelBatchNo;
    @BtCompositeSearch(fieldName = "dm.corp_id")
    @Corp(inject = "corpName")
    private String corpId;

    private String corpName;
    /**
     * 单据来源
     */
    @BtCompositeSearch(fieldName = "dm.source")
    @Dict(dictGroup = "writeOff.docSource", inject = "sourceName")
    private String source;
    private String sourceName;
    /**
     * 单据编号
     */
    @BtCompositeSearch(fieldName = "dm.code")
    private String code;

    @BtCompositeSearch(fieldName = "dm.batch_bus_type")
    private String batchBusType;

    private String batchBusTypeName;

    /**
     * 应收金额
     */
    private BigDecimal receivableMoney;

    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    /**
     *  核销金额
     */

    @BtCompositeSearch(fieldName = "rel.execute_money")
    private BigDecimal executeMoney;

    private Map<String,Object> executeMoneyMap;

    /**
     * 客商名称
     */
    @BtCompositeSearch(fieldName = "dm.external_name")
    private String externalName;

    @BtCompositeSearch(fieldName = "dm.cur_id")
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 应收单据ID */
    @BtCompositeSearch(fieldName = "rel.doc_id")
    private String docId;
    /** 临时存储-应收单核销总额 */
    private BigDecimal docMoney;
    /** 临时存储-应收单币别 */
    private String docCurName;
    /** 临时存储-应收单核销总条数 */
    private int docNum;
    /** 关联数据ID */
    private String relationId;
    /** 结算方式 */
    @BtCompositeSearch(fieldName = "rel.payment_means")
    private String paymentMeans;
    /** 匹配规则描述 */
    private String bankAccTypeStr;
    private String rulesName;
    /** 本次核销汇率 */
    private BigDecimal exchangeRate;
    /** 可核销原币金额 */
    private BigDecimal relationOriginMoney;
    /** 可核销应收币种金额 */
    private BigDecimal relationConvertMoney;
    /** 临时存储-收入数据核销总条数 */
    private int relationNum;
    /** 临时存储-应收单币别 */
    private String relationCurName;
    private String rateOpenStatus;
    /** 核销类型 */
    @BtCompositeSearch(fieldName = "rel.write_off_type")
    @Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
    private String writeOffType;
    private String writeOffTypeName;
    /** 状态 */
    private Integer status;
    private String forceReasonType;
    private String forcePaymentMeans;
    private String forceReason;

    /** 应收单-核销后应收余额 */
    private BigDecimal afterDocWriteOffBal;
    /** 应收单-核销后状态 */
    @Dict(dictGroup = "writeOff.status", inject = "afterDocWriteOffStatusName")
    private String afterDocWriteOffStatus;
    private String afterDocWriteOffStatusName;

    /** 收入数据-核销后应收余额 */
    private BigDecimal afterRelationWriteOffBal;
    /** 收入数据-核销后状态 */
    @Dict(dictGroup = "writeOff.status", inject = "afterRelationWriteOffStatusName")
    private String afterRelationWriteOffStatus;
    private String afterRelationWriteOffStatusName;

    private String checkMsg;
    /** 收入数据-交易日期 */
    private Date transDate;
    private Date createDate;
    /** 核销笔数 11一对一，12一对多，21多对一，10强制核销 */
    private String writeOffNum;
    /** 收款单关联id */
    private String nisReceivableBillId;
    private String businessType;
    private String showCurMoney;
    private String writeOffStatus;
    private String type;
    private String isApprove;
    private String businessId;
    private BigDecimal rate;
    private BigDecimal money0;

    /** 应收单信息 */
    private DocManageVo docManageVo;

    /** 流水信息 */
    private BisAccDtlVO bisAccDtlVO;
    /**核销前状态*/
    private String beforeWriteOffStatus;
    private BigDecimal beforeWriteOffBal;
    @Override
    public String getBusinessKey() {
        return batchNo;
    }
}
