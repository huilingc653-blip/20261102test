package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 票据签收信息
 * @date 2017-06-21 15:36:42
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillReceivebizVO extends AbAccrecordsVO implements java.io.Serializable,  IBusinessKey {
    /**
     * 签收表id
     **/
    private String id;
    /**
     * 票据从表id
     **/
    private String vvid;
    private String businessKey;
    /**
     * 追索同意清偿金额
     */
    private java.math.BigDecimal rcrsokmoney;
    /**
     * 保证人地址
     */
    private String adr;
    /**
     * 业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    // @Dict(dictGroup = "AB_BILL_ROLE", inject = "reqRoleName")
    private String reqRole;
    private String reqRoleName;
    /**
     * 业务申请人名称
     */
    @BtCompositeSearch(fieldName = "P.req_nm")
    private String reqNm;
    /**
     * 业务申请人组织机构代码
     */
    private String reqCmonid;
    /**
     * 业务申请人账号
     */
    private String reqAcctId;
    /**
     * 业务申请人开户行号
     */
    private String reqAcctsvcr;
    /**
     * 业务申请人承接行号
     */
    private String reqAcctsvcrAg;
    /**
     * 业务申请人电子签名
     */
    private String elctrncsgntr;
    /**
     * 批次号
     */
    private String btchnb;
    /**
     * 业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    // @Dict(dictGroup = "AB_BILL_ROLE", inject = "recRoleName")
    private String recRole;
    private String recRoleName;
    /**
     * 业务接收人名称
     */
    @BtCompositeSearch(fieldName = "P.rec_nm")
    private String recNm;
    /**
     * 业务接收人组织机构代码
     */
    private String recCmonid;
    /**
     * 业务接收人账号
     */
    @BtCompositeSearch(fieldName = "P.rec_acct_id")
    private String recAcctId;
    /**
     * 业务接收人开户行号
     */
    private String recAcctsvcr;
    /**
     * 业务接收人承接行号
     */
    private String recAcctsvcrAg;
    /**
     * 业务接收人单位ID
     */
    @BtCompositeSearch(fieldName = "P.REC_CORP_ID")
    private String recCorpId;
    @BtCompositeSearch(fieldName = "P.REC_CORP_NAME")
    private String corpName;
    @BtCompositeSearch(fieldName = "P.REC_CORP_CODE")
    private String corpCode;
    /**
     * 回复类别
     */
    private String replyType;
    /**
     * 回复日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replyDt;
    /**
     * 回复标志SU00同意签收SU01拒绝签收
     */
    // @Dict(dictGroup = "AB_BILL_SGNUPMK", inject = "sgnupmkName")
    private String sgnupmk;
    @BtCompositeSearch(fieldName = "P.sgnupmk")
    private String sgnupmkName;
    /**
     * 签收标识：SU00 通用回复同意；SU01 通用回复拒绝
     */
    private String signUpFlag;
    /**
     * 拒付理由代码
     */
    // @Dict(dictGroup = "AB_BILL_DSHNRCD", inject = "dshnrcdName")
    private String dshnrcd;
    private String dshnrcdName;
    /**
     * 拒付理由备注
     */
    private String dshnrrsn;
    /**
     * 处理状态：18待签收19签成功-2打回-3撤回
     */
    // @Dict(dictGroup = "AB_BILL_RECEIVEBIZ_STATUS", inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName = "P.STATUS")
    private String statusName;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 报文id
     */
    private String msgid;
    /**
     * 报文时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date msdt;
    /**
     * 原申请报文id
     */
    private String orgnlmsgid;
    /**
     * 原申请报文时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orgnlmsdt;
    /**
     * 报文编号：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿
     */
    // @Dict(dictGroup = "AB_BILL_SIGNERTYPE", inject = "signerType")
    private String msgtype;
    /**
     * 签收业务类型信息
     */
    @BtCompositeSearch(fieldName = "P.MSGTYPE")
    private String signerType;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName = "P.ORIG_NO")
    private String origNo;
    @BtCompositeSearch(fieldName = "P.SBBLL_RNG_STRT_SN")
    private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
    @BtCompositeSearch(fieldName = "P.SBBLL_RNG_END_SN")
    private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
    /**
     * 票据种类：10 银票；11 商票
     */
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    /**
     * 票据种类：10 银票；11 商票
     */
    @BtCompositeSearch(fieldName = "P.BILL_TYPE")
    private String billTypeName;
    /**
     * 起始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "P.start_date")
    private Date startDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "P.end_date")
    private Date endDate;
    /**
     * 票据金额
     */
    @BtCompositeSearch(fieldName = "P.bill_money")
    private java.math.BigDecimal billMoney;
    /**
     * 票据状态：参照人行标准格式的81个业务状态编号
     */
    private String billStatus;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 收款人账号
     */
    private String payeeacc;
    /**
     * 收款人开户行号
     */
    private String payeebankno;
    /**
     * 出票人
     */
    @BtCompositeSearch(fieldName = "P.payer")
    private String payer;
    /**
     * 出票人账号
     */
    private String payeracc;
    /**
     * 出票人开户行行号
     */
    private String payerbankno;
    /**
     * 出票人开户行
     */
    @BtCompositeSearch(fieldName = "p.PAYER_BANK_NAME")
    private String payerbankName;
    /**
     * 承兑人
     */
    @BtCompositeSearch(fieldName = "P.acceptor")
    private String acceptor;
    /**
     * 承兑人账号
     */
    private String acceptoracc;
    /**
     * 承兑人行户行行号
     */
    private String acceptorbankno;
    /**
     * 承兑人行户行
     */
    @BtCompositeSearch(fieldName = "p.ACCEPTOR_BANK_NAME")
    private String acceptorbankName;
    /**
     * 前手
     */
    private String cnAccrecords;
    /**
     * 信用等级
     */
    private String cdtratgs;
    /**
     * 评级机构
     */
    private String cdtratgagcy;
    /**
     * 评级到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cdtratgduedt;
    /**
     * 业务申请日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "P.bussdt")
    private Date bussdt;
    /**
     * 承兑协议编号
     */
    private String accptncagrmtnb;
    /**
     * 交易合同编号
     */
    private String txlctrctnb;
    /**
     * 发票号码
     */
    private String invcnb;
    /**
     * 承兑日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date acceptordate;
    /**
     * 到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付
     */
    // @Dict(dictGroup = "AB_BILL_UCONDLPRMSMRK", inject = "ucondlprmsmrkName")
    private String ucondlprmsmrk;
    private String ucondlprmsmrkName;
    /**
     * 线上清算标记： SM00 开启线上清算；SM01 关闭线上清算
     */
    // @Dict(dictGroup = "AB_STTLMMK", inject = "sttlmmkName")
    @BtCompositeSearch(fieldName = "P.sttlmmk")
    private String sttlmmk;
    private String sttlmmkName;
    /**
     * 不得转让标记：EM00 可再转让；EM01 不得转让
     */
    // @Dict(dictGroup = "AB_BANEM", inject = "bankendrsmtmkName")
    @BtCompositeSearch(fieldName = "P.bankendrsmtmk")
    private String bankendrsmtmk;
    private String bankendrsmtmkName;
    /**
     * 贴现种类：RM00 买断式；RM01 回购式
     */
    private String rpdmk;
    /**
     * 贴现利率
     */
    private java.math.BigDecimal intrstrate;
    /**
     * 贴现金额
     */
    private java.math.BigDecimal amt;
    /**
     * 入账账号
     */
    private String aoaccninfId;
    /**
     * 入账账号行号
     */
    private String aoaccninfAc;
    /**
     * 赎回开放日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rpdopendt;
    /**
     * 赎回截止日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rpdduedt;
    /**
     * 申请赎回利率
     */
    private java.math.BigDecimal rpdintrstrate;
    /**
     * 赎回金额
     */
    private java.math.BigDecimal rpdamt;
    /**
     * 买方付息标志
     */
    private Integer buyerInterestSign;
    /**
     * 买方付息单位
     */
    private String buyerInterestUnit;
    /**
     * 买方付息账号
     */
    private String buyerInterestAccount;
    /**
     * 贴出人备注
     */
    private String rmrkbypropsr;
    /**
     * 追索通知日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appldt;
    /**
     * 原因说明
     */
    private String rsn;
    /**
     * 追索种类：RT00 拒付追索；RT01 非拒付追索
     */
    // @Dict(dictGroup = "AB_RCRS_TP", inject = "rcrsTpName")
    private String rcrsTp;
    private String rcrsTpName;
    /**
     * 追索金额
     */
    private java.math.BigDecimal reqamt;
    /**
     * 追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
     */
    // @Dict(dictGroup = "AB_RCRSRSNCD", inject = "rcrsrsncdName")
    private String rcrsrsncd;
    private String rcrsrsncdName;
    /**
     * 资金计划信息
     */
    private List<FbsBudgetProcessDTO> budgetList;
    /**
     * 资金计划科目代码信息
     */
    private String itemCodeStr;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    /**
     * 内部业务标志：0  否；1 是
     */
    // @Dict(dictGroup = "ACCEBILL_ABRECEIVE_INSIDESING", inject = "insideSingName")
    private Integer insideSing;
    @BtCompositeSearch(fieldName = "insideSing")
    private String insideSingName;
    @BtCompositeSearch(fieldName = "P.REC_ACCT_TYPE_NAME")
    private String recBankTypeId;
    private String recBankTypeName;
    /**
     * 票据池交易明细id
     */
    private String pdId;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 类型 0应付票据，1应收票据
     */
    private String type;
    /**
     * 申请人名称
     */
    @BtCompositeSearch(fieldName = "applyName")
    private String applyName;
    /**
     * 背书转让次数
     */
    @BtCompositeSearch(fieldName = "endorsementCount")
    private String endorsementCount;
    /** 申请单位 */
//    @BtCompositeSearch(fieldName="vv.APPLY_CORP_ID")
//    @Corp(inject="applyCorpName")
    private String applyCorpId;
    /**申请单位名称*/
    @BtCompositeSearch(fieldName="vv.APPLY_CORP_NAME")
    private String applyCorpName;
    /**申请单位代码*/
//    @BtCompositeSearch(fieldName="vv.applyCorpCode")
    private String applyCorpCode;
    /** 业务单位 */
    //@Corp(inject="busCorpName")
    //@BtCompositeSearch(fieldName="vv.BUS_CORP_ID")
    private String busCorpId;
    /** 业务单位名称 */
    private String busCorpName;
    /** 业务人员 */
    //@BtCompositeSearch(fieldName="vv.BUS_PERSONNEL")
    private String busPersonnel;
    /**
     * 开票信用等级
     */
    private String payerCdtRatgs;
    /**
     * 开票评级机构
     */
    private String payerCdtRatgGgcy;
    /**
     * 开票评级到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payerCdtRatgDuedt;
    /**
     * 统一社会信用代码
     */
    private String socCode;
    private String acceptorbankno1;
    private String acceptorbankno2;
    private Integer billType1;
    private Integer billType2;
    /**
     * 出票标志
     */
    @BtCompositeSearch(fieldName = "p.NEW_STM_ISSUOFPPR_IND")
    //@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /**
     * 是否允许分包
     */
    @BtCompositeSearch(fieldName = "p.BLGRDBAGCRCL_PRMT_IND")
    //@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;
    /**
     * 标准金额
     */
    private BigDecimal blStdAmt = new BigDecimal("0.01");
    /**
     * 出票人统一信用代码
     */
    private String blRmtrUnnIdNo;
    /**
     * 新票拒付理由代码
     */
    private String newDshnrcd;
    public String getCheckboxHide() {
        if (this.getStatus() != null && this.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)) {
            return "1";
        }
        return checkboxHide;
    }
}
