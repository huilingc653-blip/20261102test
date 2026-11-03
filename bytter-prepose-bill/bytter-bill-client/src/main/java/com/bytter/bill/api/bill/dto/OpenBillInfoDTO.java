package com.bytter.bill.api.bill.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author luop
 * @version 1.0
 * @Title: Entity
 * @Description: 开票登记VO
 * @date 2017-06-21 18:00:00
 *
 */

@SuppressWarnings("serial")
@Data
public class OpenBillInfoDTO {

    /**
     * 开票数据来源
     */
    private String source;

    /**
     * 保证金额度单Id
     */
    private String bondId;

    /**
     * 授信占用金额 -启用授信敞口，
     * 授信占用金额=票面金额-保证金金额
     * 不启用授信产口，授信占用金额=票面金额
     **/
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal ratBillMoney;
    /**
     * 客商所在省(承兑人)
     */
    private String acceptProvince;
    /**
     * 客商所在市(承兑人)
     */
    private String acceptCity;
    /**
     * 客商银行类别id(承兑人)
     */
    private String acceptBankTypeId;
    /**
     * 收票人单位id--用于编辑对内的开票申请时，内部成员单位回显
     */
    private String payeeCorpId;
    /**
     * 是否批次开票数据1为是
     */
    private String isBatchBill;
    /**
     * 外部系统id编码
     */

    private String voucherId;
    /**
     * 是否是付款办理弹窗开票
     */
    private String isPayFlg;
    /**
     * 开票登记单号
     */

    private String appNo;
    /**
     * 出票标志
     */
    //@Excel(name = "*出票标志")
    //@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")

    private String newStmIssuofpprInd;
    /**
     * 是否允许分包
     */
    //@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    @BtCompositeSearch(fieldName = "a.blgrdbagcrcl_Prmt_Ind")
    private String blgrdbagcrclPrmtInd;
    /**
     * 收票人账号 --用于对内开票时提交数据
     */
    private String payeeacc1;

    /**
     * 收票人全称--用于对内开票时提交数据
     */
    private String payee1;
    /**
     * 票据金额
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal billMoney;
    private String isEnsure;
    private String payeracc;
    /**出票人开户银行名称*/
    /**
     * 出票人
     */

//	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYER_ISNOTNULL)
    private String payer;
    private String payerBankName;
    /**出票人开户银行行号*/
    /**
     * 银行授信单位Id
     */
    private String ratBankCorpId;
    /**
     * 授信银行类型：0承兑人开户银行 1出票人开户银行
     */
    private String ratBankType;
    /**
     * 开票信用等级
     */

    private String payerCdtRatgs;
    /**
     * 开票评级机构
     */

    private String payerCdtRatgGgcy;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 附件数量
     */
    private Integer attachmentNum;

    /**
     * 流通介质：10 纸票、11 电票
     */
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    /**
     * 票据号码
     */

    private String origNo;
    /**
     * 申请单位
     */
    private String applyCorpId;
    /**
     * 业务单位
     */
    // @Corp(inject="busCorpName")
    private String busCorpId;
    /**
     * 持票单位
     */

    private String corpId;

    /**
     * 业务人员
     */
    private String busPersonnel;
    /**
     * 不得转让标记EM00可再转让EM01不得转让
     */
    // @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
    private String banendrsmtmk;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;
    /**
     * 票据类型：10 银票、11 商票
     */
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    /**
     * 开出方式（10票据质押、20保证金、30信用）
     */
    // @Dict(dictGroup="ACCEBILL_EMIT_TYPE",inject = "emitTypeName")
    private String emitType;
    /**
     * 出票日期
     */

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String payerbankno;
    /**
     * 出票人统一信用代码
     */
    private String blRmtrUnnIdNo;
    /**
     * 计划科目
     */
    private String itemCode;
    /**
     * 计划科目名称
     */
    private String itemName;
    /**
     * 执行金额
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal processMoney;
    /**
     * 汇率
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal exchangeRate;
    /**
     * 执行金额（折合本身币别）
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal processCurMoney;


    private String cmbBnkbilnegId;//招行合作框架协议表id
    private String accptncagrmtBranchNo;
    private String accptncagrmtBranch;

    private String cvmLimitNo;
    private String riskContractNumber;
    /**
     * 合作银行号
     */

    private String cooperativeBankNo;
    /**
     * 承兑协议编号
     */

    private String accptncagrmtnb;
    /**
     * 是否授信提用：1是 0否
     */
    private String isCreditDraw;
    /**
     * 备注
     */

    private String rmk;

    /**
     * 摘要
     */

    private String abs;

    /**
     * 手续费比例
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal feemoneyScale;
    /**
     * 手续费
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal feemoney;
    /**
     * 发票号
     */

    private String ainvcnb;

    /**
     * 保证金合同
     */
    private String bailContract;
    /**
     * 保证金利率
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal bailrate;

    /**
     * 招行列表保证金选择标识 0-否 1-是
     */
    private String cmbBailFlag;
    /**
     * 缴存保证金账户
     */

    private String bailAcc;
    /**
     * 保证金金额
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal bailmoney;
    /**
     * 保证金类型
     */
    // @Dict(dictGroup = "AB_BILL_BAIL_TYPE", inject = "bailTypeName")
    private String bailType;

    /**
     * 开票评级到期日
     */

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payerCdtRatgDuedt;
    private String cmbCrlConId;//招行风险合同信息表id

    private String payeeacc;
    /**
     * 收票人全称--用于对外开票时提交数据和数据库查询
     */

    private String payee;
    /**
     * 收票人开户银行名称
     */


    private String payeeBankName;
    /**
     * 收票人开户银行行号
     */

    private String payeebankno;
    /**
     * 出票人统一信用代码
     */
    private String blRcvpymtpsUnnIdNo;
    /**
     * 承兑人
     */

    private String acceptor;
    /**
     * 是否共享
     */
    private String isCommon;
    /**
     * 承兑人开户行行号
     */

    private String acceptorbankno;
    /**
     * 承兑人开户行名称
     */

    private String acceptorBankName;
    /**
     * 承兑人开户行地址
     */

    private String acceptorbankaddress;
    /**
     * 是否保存客商资料
     */
    private String isExternalCorp;
    /**
     * 承兑人账号
     */

//	@NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL)
    private String acceptoracc;
    /**
     * 承兑合同号/交易合同号
     */

    private String acontractNo;
    /**
     * 出票人统一信用代码
     */
    private String blAcptrUnnIdNo;
    /**
     * 保证金比例
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal bailpercent;
    /**
     * 银行授信单位类型 开票人=0、承兑人=1
     */
    private String ratBankCorpType;
    /**
     * 是否挂授信
     */
    private String ratmodel;
}
