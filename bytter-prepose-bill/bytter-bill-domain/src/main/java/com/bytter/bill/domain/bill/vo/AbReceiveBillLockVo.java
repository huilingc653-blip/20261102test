package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Vo
 * @Description: 票据锁定/解锁
 * @date 2017-06-21 17:35:25
 */
@SuppressWarnings("serial")
@Data
public class AbReceiveBillLockVo implements Serializable {
    private String id;
    /**
     * 票据主表id
     */
    private String vid;
    /**
     * 票据从表id
     */
    private String vvid;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName = "z.orig_No")
    private String origNo;
    /**
     * 票据类型：10 银票、11 商票
     */
    @BtCompositeSearch(fieldName = "z.bill_Type")
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    /**
     * 票据类型名称：10 银票、11 商票
     */
    private String billTypeName;
    /**
     * 持票单位
     */
    @BtCompositeSearch(fieldName = "c.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "c.corp_name")
    private String corpIdName;
    private String corpCode;
    private String billStatus;
    /**
     * 流通介质：10 纸票、11 电票
     */
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    @BtCompositeSearch(fieldName = "z.bill_Flag")
    private Integer billFlag;
    /**
     * 流通介质名称：10 纸票、11 电票
     */
    private String billFlagName;
    /**
     * 票据金额
     */
    @BtCompositeSearch(fieldName = "z.bill_Money")
    private java.math.BigDecimal billMoney;
    /**
     * 出票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.start_Date")
    private java.util.Date startDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.end_Date")
    private java.util.Date endDate;
    /**
     * 出票人
     */
    @BtCompositeSearch(fieldName = "z.payer")
    private String payer;
    /**
     * 收票人全称
     */
    @BtCompositeSearch(fieldName = "z.payee")
    private String payee;
    /**
     * 承兑人
     */
    @BtCompositeSearch(fieldName = "z.acceptor")
    private String acceptor;
    /**
     * 前手
     */
    private String cnAccrecords;
    /**
     * 开出方式：0  对外开出；1 对内自开
     */
    // @Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
    @BtCompositeSearch(fieldName = "c.inside_Sing")
    private Integer insideSing;
    private String insideSingName;
    /**
     * 业务处理状态
     */
    // @Dict(dictGroup = "AB_BILL_AbReceiveBillLock_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "l.status")
    private Integer status;
    private String statusName;
    /**
     * 业务状态
     */
    // @Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
    @BtCompositeSearch(fieldName = "l.brief_Id")
    private String briefId;
    private String briefIdName;
    private java.util.Date date;
    private String ids;
    private String vids;
    private String vvids;
    private Integer checkboxHide;
    // @Dict(dictGroup = "abReceiveBillLock.isLock", inject = "lockSignName")
    @BtCompositeSearch(fieldName = "c.valid_sign")
    private String lockSign;
    @BtCompositeSearch(fieldName = "c.valid_sign")
    private String lockSignName;
    /**
     * 所属库存
     */
    @BtCompositeSearch(fieldName = "c.enter_stock")
    // @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
    private String enterStock;
    private String enterStockName;
    /**
     * 是否发送指令标志：0否； 1是 ；
     */
    @BtCompositeSearch(fieldName = "l.send_Sign")
    // @Dict(dictGroup = "AB_BILL_SEND_SIGN", inject = "sendSignName")
    private String sendSign;
    @BtCompositeSearch(fieldName = "l.send_Sign")
    private String sendSignName;
    /** 申请单位 */
    @BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
    // @Corp(inject="applyCorpName")
    private String applyCorpId;
    /**申请单位名称*/
    @Excel(name = "申请单位",isImportField="true")
    @BtCompositeSearch(fieldName="c.APPLY_CORP_NAME")
    private String applyCorpName;
    /**申请单位代码*/
    @Excel(name = "申请单位代码",isImportField="true")
    private String applyCorpCode;
    /** 业务单位 */
    // @Corp(inject="busCorpName")
    @BtCompositeSearch(fieldName="c.BUS_CORP_ID")
    private String busCorpId;
    /** 业务单位名称 */
    @Excel(name = "业务单位",isImportField="true")
    private String busCorpName;
    /** 业务人员 */
    @BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
    @Excel(name = "业务人员",isImportField="true")
    private String busPersonnel;
    /** 出票标志 new_stm_issuofppr_ind*/
    @BtCompositeSearch(fieldName = "z.new_stm_issuofppr_ind")
   // @Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /** 是否允许分包*/
    @BtCompositeSearch(fieldName = "z.blgrdbagcrcl_prmt_ind")
    //@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;
    /** 子票区间开始序号 */
    private String sbbllRngStrtSn;
    /** 子票区间结束序号 */
    private String sbbllRngEndSn;
    /** 标准金额 */
    private BigDecimal blStdAmt;
    /**
     * 业务批次号
     */
    @BtCompositeSearch(fieldName = "c.BATCH_NO")
    private String batchNo;
    private String reason;
    @BtCompositeSearch(fieldName="c.BANK_TYPE_ID")
    private String bankTypeId;
}
