package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Vo
 * @Description: 兑付确认
 * @date 2017-06-21 17:35:25
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbEmitBillPayVo extends AbAccrecordsVO implements Serializable,  IBusinessKey {
    private String dateStr;
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
    @BtCompositeSearch(fieldName = "z.SBBLL_RNG_STRT_SN")
    private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
    @BtCompositeSearch(fieldName = "z.SBBLL_RNG_END_SN")
    private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
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
    @BtCompositeSearch(fieldName = "c.corp_Id")
    private String corpId;
    @BtCompositeSearch(fieldName = "c.corp_code")
    private String corpIdName;
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
    private BigDecimal billMoney;
    /**
     * 出票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.start_Date")
    private Date startDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.end_Date")
    private Date endDate;
    /**
     * 出票人
     */
    @BtCompositeSearch(fieldName = "z.payer")
    private String payer;
    /**出票人账号*/
    @BtCompositeSearch(fieldName = "z.payeracc")
    private String payeracc;
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
     * 承兑人账号
     */
    private String acceptoracc;
    /**
     * 承兑人开户行行号
     */
    private String acceptorbankno;
    /**
     * 承兑人开户行名称
     */
    private String acceptorBankName;
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
    // @Dict(dictGroup = "AB_BILL_AbEmitBillPay_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "l.status")
    private Integer status;
    private String statusName;
    private Integer checkboxHide;
    /**
     * 业务状态
     */
    // @Dict(dictGroup = "AB_BILL_AbEmitBillPay_BRIEF_ID", inject = "briefIdName")
    @BtCompositeSearch(fieldName = "l.brief_Id")
    private String briefId;
    private String briefIdName;
    private String ids;
    private String vids;
    private String vvids;
    private Date date;
    //添加线上兑付确认改造 2017/11/8 add by luop
    /**
     * 兑付单位
     */
    private String payCorpId;
    /**
     * 兑付金额
     */
    private BigDecimal payMoney;
    /**
     * 收款账号
     */
    private String payPayeeacc;
    /**
     * 收款账号名称
     */
    private String payPayeeName;
    /**
     * 收款开户行行号
     */
    private String payPayeeBankNo;
    /**
     * 收款开户行名称
     */
    private String payPayeeBankName;
    /**
     * 兑付用途
     */
    private String payUse;
    /**
     * 兑付备注
     */
    private String payAbs;
    /**
     * 兑付方式:0或空线下兑付,1线上兑付'
     */
    // @Dict(dictGroup = "AB_BILL_ABACCRECORDS_EMITSTTLMMK", inject = "emitSttlmmkName")
    private String emitSttlmmk;
    private String emitSttlmmkName;
    /**
     * 付款单号
     */
    private String billNo;
    /**
     * 付款状态
     */
    // @Dict(dictGroup = "nisBillhead.status", inject = "payStatusName")
    @BtCompositeSearch(fieldName = "n.status")
    private Integer payStatus;
    private String payStatusName;
    /**
     * 交易状态
     */
    // @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    @BtCompositeSearch(fieldName = "e.VOUCHER_STAT")
    private String voucherStatName;
    /**
     * 所在省
     */
    private String prov;
    private String provName;
    /**
     * 所在市
     */
    private String city;
    private String cityName;
    /** 申请单位 */
    @BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
    private String applyCorpId;
    /**申请单位名称*/
    @BtCompositeSearch(fieldName="c.APPLY_CORP_NAME")
    private String applyCorpName;
    /**申请单位代码*/
    private String applyCorpCode;
    /** 业务单位 */
    // @Corp(inject="busCorpName")
    @BtCompositeSearch(fieldName="c.BUS_CORP_ID")
    private String busCorpId;
    /** 业务单位名称 */
    private String busCorpName;
    /** 业务人员 */
    @BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
    private String busPersonnel;
    private List<AbEmitBillPayOnlineVo> abEmitBillPayOnlineVos;
    /**
     * 业务办理日期
     */
    @BtCompositeSearch(fieldName = "l.BUSINESS_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;
    /**
     * 出票标志
     */
    @BtCompositeSearch(fieldName="z.NEW_STM_ISSUOFPPR_IND")
    @Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /** 是否允许分包 */
    @BtCompositeSearch(fieldName="z.BLGRDBAGCRCL_PRMT_IND")
    @Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;
    /** 标准金额 */
    private BigDecimal blStdAmt;
    /**出票人统一信用代码 */
    private String blRmtrUnnIdNo;
    /**业务批次号 */
    @BtCompositeSearch(fieldName="c.BATCH_NO")
    private String batchNo;
    //打回业务类型
    private String backBriefId;
    //流水状态
    private String lStatus;
    private String ebillNo;
    @BtCompositeSearch(fieldName="c.BANK_TYPE_ID")
    private String bankTypeId;
    @BtCompositeSearch(fieldName="bbt.type_name")
    private String bankTypeName;
    /**保证金*/
    @BtCompositeSearch(fieldName="c.BAIL_MONEY")
    private BigDecimal bailmoney;
    /**保证金比例*/
    @BtCompositeSearch(fieldName="c.BAIL_PERCENT")
    private BigDecimal bailpercent;
    /**到期解付金额*/
    //@BtCompositeSearch(fieldName="(Z.BILL_MONEY-IFNULL(C.BAIL_MONEY,0))")
    private BigDecimal endPayMoney;
    @Override
    public String getBusinessKey() {
        return id;
    }
}
