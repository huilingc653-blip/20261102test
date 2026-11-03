package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.TranCode;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: VO
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
public class FexTranReqVO implements Serializable, IBaseDataInject, IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**流水号*/
    @BtCompositeSearch(fieldName = "t.bill_No")
    private String billNo;
    /**交易需求申请从表ID*/
    private String tranAppDtlId;
    private String tranAppDtlNo;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    @BtCompositeSearch(fieldName = "t.tran_Type")
    private String tranType;
    /**是否掉期交易 1-是、0-否*/
    @BtCompositeSearch(fieldName = "t.is_Swap_Tran")
    private String isSwapTran;
    /**产品ID*/
    @BtCompositeSearch(fieldName = "t.pro_Id")
    private String proId;
    @BtCompositeSearch(fieldName = "p.pro_Name")
    private String proName;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    @BtCompositeSearch(fieldName = "t.fex_Bus_Type")
    private String fexBusType;
    /**外汇交易类型 0-即期、1-远期*/
    @BtCompositeSearch(fieldName = "t.fex_Tran_Type")
    private String fexTranType;
    /**交割类型 0-本金交割、1-无本金交割*/
    @BtCompositeSearch(fieldName = "t.delivery_Type")
    private String deliveryType;
    /**交割币种*/

    @Currencys(
            {
                    @Currency(inject="deliveryCurName"),
                    @Currency(inject="deliveryEnglishCode", field = "englishCode")
            }
    )
    @BtCompositeSearch(fieldName = "t.delivery_Cur_Id")
    private String deliveryCurId;
    private String deliveryCurName;
    private String deliveryEnglishCode;
    /**买入币种*/
    @BtCompositeSearch(fieldName = "t.buy_Cur_Id")
    @Currencys(
            {
                    @Currency(inject="buyCurName"),
                    @Currency(inject="buyEnglishCode", field = "englishCode")
            }
    )
    private String buyCurId;
    private String buyCurName;
    @BtCompositeSearch(fieldName = "t.buy_Cur_Id")
    private String buyEnglishCode;
    /**卖出币种*/
    @BtCompositeSearch(fieldName = "t.sell_Cur_Id")
    /**卖出币种*/
    @Currencys(
            {
                    @Currency(inject="sellCurName"),
                    @Currency(inject="sellEnglishCode", field = "englishCode")
            }
    )
    private String sellCurId;
    private String sellCurName;
    @BtCompositeSearch(fieldName = "t.sell_Cur_Id")
    private String sellEnglishCode;
    /**买入金额*/
    @BtCompositeSearch(fieldName = "t.buy_Money")
    private BigDecimal buyMoney;
    /**卖出金额*/
    @BtCompositeSearch(fieldName = "t.sell_Money")
    private BigDecimal sellMoney;
    /**固定金额 1-买入金额 2-卖出金额*/
    private String fixedMoney;
    /**合同汇率*/
    @BtCompositeSearch(fieldName = "t.contract_Rate")
    private BigDecimal contractRate;
    /**外汇合同号*/
    private String foreignContractNo;
    /**即期汇率*/
    private BigDecimal spotRate;
    /**钞汇标识 1-现钞 2-现汇 */
    private String bankNote;
    /**交易日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t.tran_Date")
    private Date tranDate;
    /**结算日期*/
    @BtCompositeSearch(fieldName = "t.settle_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date settleDate;
    /**交割期限*/
    private String deliveryDeadline;
    /**保证方式,1-定期保证金、2-活期保证金、3-其它保证方式、4-授信额度*/
    private String boodType;
    /**保证金账号*/
    private String boodAccId;
    private String boodAcc;
    /**保证金比例*/
    private BigDecimal boodRatio;
    /**保证金钞汇标识,1-现钞，2-现汇*/
    private String boodBankNote;
    /**保证金币种*/
    @Currencys(
            {
                    @Currency(inject="bondCurName"),
                    @Currency(inject="bondEnglishCode", field = "englishCode")
            }
    )
    private String bondCurId;
    private String bondCurName;
    private String bondEnglishCode;
    /**合同号*/
    private String contractNo;
    /**远期点数*/
    private BigDecimal forwardPoints;
    /**外汇局批件号/备案表号/业务编号*/
    private String busNo;
    /**结汇资金用途*/
    private String purpose;
    /**结汇资金详细用途*/
    private String purposeDtl;
    /**资金来源,1-货物贸易、2-服务贸易收益与经常转移、3-资本与金融项目*/
    private String fundsSource;
    /**发票号*/
    private String invoiceNo;
    /**账户资金性质 1-国内主账户、2-国外主账户、3-经常项目、4-资本项目、5-NRA账户*/
    @BtCompositeSearch(fieldName = "t.acc_Nature")
    private String accNature;
    /**结售汇属性*/
    private String fexProp;
    /**其它保证方式说明*/
    private String boodRmk;
    /**备注(英文)*/
    private String rmk;
    /**备注(本地语言)*/
    private String rmkLocal;
    /**附件*/
    private String attachmentId;
    /**源交易对手数字ID*/
    private String oppId;
    /**交易对手*/
    @BtCompositeSearch(fieldName = "t.cotp_Id")
    private String cotpId;
    private String cotpName;
    /**交易对手(英文)*/
    private String cotpEnName;

    /**交易对手Swift Code(交易对手-BIC Code-fexCotp.bicCode)*/
    private String cotpBicCode;
    /**交易对手银行*/
    private String cotpBank;
    @BtCompositeSearch(fieldName = "t.cotp_Bank")
    private String cotpBankName;
    /**交易对手银行账号*/
    private String cotpAccId;
    @BtCompositeSearch(fieldName = "t.cotp_Bank_Acc")
    private String cotpBankAcc;
    private String cotpAccName;
    @Currencys(
            {
                    @Currency(inject="cotpAccCurName"),
                    @Currency(inject="cotpAccEnglishCode", field = "englishCode")
            }
    )
    private String cotpAccCurId;
    private String cotpAccCurName;
    private String cotpAccEnglishCode;
    /**交易对手国家地区*/
    private String cotpCountry;
    private String cotpCountryName;
    /**交易对手地点*/
    private String cotpAddress;
    private String cotpAddressName;
    private String cotpAddressNameEn;
    /**交易对手省（国家地区为中国时需要）*/
    private String cotpProv;
    private String cotpProvName;
    /**交易对手市（国家地区为中国时需要）*/
    private String cotpCity;
    private String cotpCityName;
    /**交易对手地址(交易对手-邮政地址-fexCotp.postAddr)*/
    private String cotpAdrr;
    /**交易对手银行地址(交易对手银行账号-地址-fexCotpBankAcc.bankAddr)*/
    private String cotpBankAdrr;
    /**交易对手银行Swift Code(交易对手银行账户-BIC Code-fexCotpBankAcc.bicCode)*/
    private String cotpSwiftCode;
    /**交易对手代理行地址(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAddr)*/
    private String cotpAgentBankAddr;
    /**交易对手代理行(交易对手银行账户-中转行-fexCotpBankAcc.tranBank)*/
    private String cotpAgentBank;
    /**交易对手在代理行的账户(交易对手银行账户-中转行-fexCotpBankAcc.tranBankAcc)*/
    private String cotpAgentBankAcc;
    /**交易对手在代理行的Swift Code(交易对手银行账户-中转行-fexCotpBankAcc.tranBicCode)*/
    private String cotpAgentSwiftCode;


    /**币种*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String englishCode;
    /**收付方向 0-收款 1-付款*/
    @Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignName")
    @BtCompositeSearch(fieldName = "t.cd_Sign")
    private String cdSign;
    private String cdSignName;
    /**被代付/代付单位*/
    @Corp(inject = "payCorpName")
    private String payCorpId;
    private String payCorpName;
    /**金额*/
    @BtCompositeSearch(fieldName = "t.pay_Money")
    private BigDecimal payMoney;
    /**汇率折算金额*/
    private BigDecimal payMoneyConvert;
    /**加急标志 0-否 1-是*/
    private String isQuick;
    /**主体单位银行账户ID*/
    private String corpAccId;
    /**主体单位银行*/
    @BankType(inject = "corpBankName")
    @BtCompositeSearch(fieldName = "t.corp_Bank")
    private String corpBank;
    @BtCompositeSearch(fieldName = "t.corp_Bank")
    private String corpBankName;
    /**主体单位银行账户*/
    @BtCompositeSearch(fieldName = "t.corp_Bank_Acc")
    private String corpBankAcc;
    @BtCompositeSearch(fieldName = "t.corp_Acc_Name")
    private String corpAccName;
    @Currencys(
            {
                    @Currency(inject="corpAccCurName"),
                    @Currency(inject="corpAccEnglishCode", field = "englishCode")
            }
    )
    private String corpAccCurId;
    private String corpAccCurName;
    private String corpAccEnglishCode;
    /**主体单位银行账户SWIFT CODE*/
    private String corpBankAccSwiftCode;
    /**汇款类型 0-境内付款 1-境外付款*/
    @BtCompositeSearch(fieldName = "t.remit_Type")
    private String remitType;
    /**汇款方式 0-电汇 1-票汇*/
    private String remitMethod;
    /**主体单位名称（英文）*/
    private String corpNameEn;
    /**主体单位地址（英文）*/
    private String corpAddrEn;
    /**主体单位地址（中文）*/
    private String corpAddr;
    /**主体单位联系人*/
    private String corpContact;
    /**主体单位联系电话*/
    private String corpContactTel;
    /**国内外费用承担,1-汇款人、2-收款人、3-共同*/
    private String costSign;
    /**费用支出账户*/
    private String costAccId;
    private String costAcc;
    /**费用支出账户2*/
    private String costAccId2;
    private String costAcc2;
    /**付款类型,1-预付货款，2-货到付款，3-退款，4-其他*/
    private String payType;
    /**最迟装运日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date shipDate;
    /**是否为保税货物项付款 0-否 1-是*/
    private String isBonded;
    /**交易编码1*/
    @TranCode(inject="tranName1")
    private String tranCode1;
    private String tranName1;
    /**相应金额1*/
    private BigDecimal tranAmt1;
    /**相应币种1*/
    @Currencys(
            {
                    @Currency(inject="tranCurName1"),
                    @Currency(inject="tranEnglishCode1", field = "englishCode")
            }
    )
    private String tranCurId1;
    private String tranCurName1;
    private String tranEnglishCode1;
    /**交易附言1*/
    private String tranAddWord1;
    /**交易编码2*/
    @TranCode(inject="tranName2")
    private String tranCode2;
    private String tranName2;
    /**相应金额2*/
    private BigDecimal tranAmt2;
    /**相应币种2*/
    @Currencys(
            {
                    @Currency(inject="tranCurName2"),
                    @Currency(inject="tranEnglishCode2", field = "englishCode")
            }
    )
    private String tranCurId2;
    private String tranCurName2;
    private String tranEnglishCode2;
    /**交易附言2*/
    private String tranAddWord2;

    /************ 扩展信息开始 *************/
    /**
     * 买入方向
     */
    private String busDirection1;
    private String busDirection2;



    /**
     * 货币对
     */
    private String tranCurPairId;
    private String tranCurPairId1;
    private String tranCurPairId2;

    private String tranCurPairIdName;

    /**
     * 交易货币对标准币种
     */
    private String tranCurPairBaseId;
    private String tranCurPairBaseCode;
    private String tranCurPairBaseId1;
    private String tranCurPairBaseId2;
    /**
     * 交易货币对报价币种
     */
    private String tranCurPairReferId;
    private String tranCurPairReferCode;
    private String tranCurPairReferId1;
    private String tranCurPairReferId2;

    /**
     * 金额是否自动计算
     */
    private String isPaymentAmount;
    private String isPaymentAmount1;
    private String isPaymentAmount2;
    /**
     * 交易汇率类型
     */
    private String tranRateType;
    private String tranRateType1;
    private String tranRateType2;
    /**
     * 报价方式
     */
    private String quotationMethod;
    private String quotationMethod1;
    private String quotationMethod2;

    private String quotationCurId;
    private String quotationCurId1;
    private String quotationCurId2;

    private String quotationReferCurId;
    private String quotationReferCurId1;
    private String quotationReferCurId2;
    /**
     * 持仓金额
     */
    @BtCompositeSearch(fieldName = "fx.position_money")
    private BigDecimal positionMoney;
    private BigDecimal positionMoney1;
    private BigDecimal positionMoney2;
    /**
     * 损益币种
     */
    private String profitLossCur;
    private String profitLossCur1;
    private String profitLossCur2;
    private String profitLossCurName;
    private String profitLossCurEnglishCode;
    /**
     * 损益
     */
    private BigDecimal profitLossMoney;
    private BigDecimal profitLossMoney1;
    private BigDecimal profitLossMoney2;

    /**
     * 折本位币报价方式
     */
    private String convertCurType;
    private String convertCurType1;
    private String convertCurType2;
    /**
     * 折本位币标准币种
     */
    private String convertCurId;
    /**
     * 折本位币报价币种
     */
    private String convertCurReferId;
    /**
     * 折本位币汇率
     */
    private BigDecimal convertRate;
    private BigDecimal convertRate1;
    private BigDecimal convertRate2;
    /**
     * 折本位币损益、期权费金额
     */
    private BigDecimal convertMoney;
    private BigDecimal convertMoney1;
    private BigDecimal convertMoney2;
    /************ 扩展信息结束 *************/


    /**
     * 流水勾对状态
     */
    private String blendSign;



    /**付汇性质 1-保税区、2-出口加工区、3-钻石交易所、4-其他特殊经济区域、5-深加工结转、6-离岸账户、7-其他*/
    private String remitNature;
    /**状态*/
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    /**单位/源单位*/
    @BtCompositeSearch(fieldName = "t.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpName;
    /**资金用途*/
    private String capitalUse;
    /**源银行*/
    @BankType(inject = "srcBankName")
    private String srcBank;
    private String srcBankName;
    /**源银行账户*/
    private String srcAccId;
    private String srcBankAcc;
    private String srcAccName;
    /**源银行账户SWIFT CODE*/
    private String srcBankAccSwiftCode;
    /**目标银行*/
    @BankType(inject = "targetBankName")
    private String targetBank;
    private String targetBankName;
    /**目标银行账户*/
    private String targetAccId;
    private String targetBankAcc;
    private String targetAccName;
    /**目标银行账户币种*/
    @Currencys(
            {
                    @Currency(inject="targetAccCurName"),
                    @Currency(inject="targetAccEnglishCode", field = "englishCode")
            }
    )
    @BtCompositeSearch(fieldName = "t.target_Acc_Cur_Id")
    private String targetAccCurId;
    private String targetAccCurName;
    @BtCompositeSearch(fieldName = "t.target_Acc_Cur_Id")
    private String targetAccEnglishCode;
    private String isDomesticBank;
    private String targetBankCode;
    /**收款人地址*/
    private String targetAddr;
    /**目标单位*/
    @BtCompositeSearch(fieldName = "t.target_Corp")
    @Corp(inject = "targetCorpName")
    private String targetCorp;
    @BtCompositeSearch(fieldName = "t.target_Corp")
    private String targetCorpName;
    /**代理行*/
    private String agentBank;
    /**开户行在代理行账户*/
    private String agentBankAcc;
    /**收款人常驻国家或者地区*/
    private String countryId;
    private String nameZh;
    /**交易对手业务编号*/
    private String cotpNo;
    /**交易地点*/
    private String tranAddr;
    /**所属交易组*/
    private String tranGroup;
    private String tranGroupName;
    /**近/远端  1-近端，2-远端*/
    private String swapType;
    /**近/远端ID*/
    private String partnerId;
    /**交易属性,1-近端交易，2-远端交易*/
    private String tranAttr;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @BtCompositeSearch(fieldName = "t.create_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**结汇业务类型*/
    private String settleBusType;
    /**自动计算买入金额*/
    private String autoCal;

    /**波动率*/
    private BigDecimal volatility;
    /**买卖方向*/
    @BtCompositeSearch(fieldName = "t.bus_Direction")
    private String busDirection;
    /**涨跌方向*/
    @BtCompositeSearch(fieldName = "t.updown_Direction")
    private String updownDirection;
    /**到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t.expires_Date")
    private Date expiresDate;
    /**平均期开始日期*/
    private Date avgBeginDate;
    /**平均期结束日期*/
    private Date avgEndDate;
    /**平均期天数*/
    private BigDecimal avgDays;
    /**障碍验证标准*/
    private String validStandard;
    /**障碍值*/
    private BigDecimal exceptVal;
    /**低障碍值*/
    private BigDecimal exceptLowVal;
    /**高障碍值*/
    private BigDecimal exceptHignVal;
    /**结算方式*/
    private String tranSettleMethod;
    /**交易地点ID*/
    private String tranAddrId;
    /**期权费日期*/
    private Date premiumDate;
    @Currencys(
            {
                    @Currency(inject="premiumCurName"),
                    @Currency(inject="premiumEnglishCode", field = "englishCode")
            }
    )
    /**期权费币种*/
    private String premiumCur;
    private String premiumCurName;
    private String premiumEnglishCode;
    /**期权费点数*/
    private BigDecimal premiumPoint;
    /**期权费百分比*/
    private BigDecimal premiumRatio;
    /**期权费折算汇率*/
    private BigDecimal premiumRate;
    /**期权费折算汇率*/
    private String premiumRateStr;
    /**期权费金额（结算币种）*/
    private BigDecimal premiumMoney;
    /**期权费金额（期权费币种）*/
    private BigDecimal premiumMoneyOfPremiumCur;
    /**是否涉及退款*/
    private String isRefund;
    /**退款点数*/
    private BigDecimal refundPoint;
    /**退款百分比*/
    private BigDecimal refundRatio;
    /**退款金额*/
    private BigDecimal refundMoney;
    /**退款支付时间*/
    private String refundTime;
    /**经纪人*/
    private String cotpAgent;
    private String cotpAgentName;
    /**清算所*/
    private String cotpClearHouse;
    private String cotpClearHouseName;
    /**外汇期权品种1 0-普通期权、1-奇异期权*/
    @BtCompositeSearch(fieldName = "p.fex_Option1")
    private String fexOption1;
    /**外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权*/
    @BtCompositeSearch(fieldName = "p.fex_Option2")
    private String fexOption2;
    /**外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)*/
    @BtCompositeSearch(fieldName = "p.fex_Option3")
    private String fexOption3;
    @BtCompositeSearch(fieldName = "p.fex_Option_Type")
    /**外汇期权类型 0-欧式、1-美式*/
    private String fexOptionType;
    /**在交易时展示经纪人 0-否、1-是*/
    private String isShowAgent;
    /**在交易时展示清算所 0-否、1-是*/
    private String isShowClearHouse;
    /**跨行标识*/
    @BtCompositeSearch(fieldName = "t.dif_Bank")
    private String difBank;
    /**是否线下付款*/
    @BtCompositeSearch(fieldName = "t.is_Payed")
    private String isPayed;
    /**会计凭证号*/
    @BtCompositeSearch(fieldName = "t.bursar_Bill_No")
    private String bursarBillNo;

    /**计划科目*/
    private String[] itemCode;
    /**计划科目名称*/
    private String[] itemName;
    /**执行金额*/
    private BigDecimal[] processMoney;
    /**汇率*/
    private BigDecimal[] exchangeRate;
    /**执行金额（折合本身币别）*/
    private BigDecimal[] processCurMoney;
    /**计划上报单位*/
    private String[] budgetCorpId;
    /**执行明细信息*/
    private String[] processDetailInfo;
    /**资金计划相关信息*/
    private String budgetInfo;

    private List<FexCustInfoTenantVO> fexTranCustInfos;

    /**支付方式*/
    private String voucherTypeId;
    private String voucherTypeName;
    /**账户类别：汇款账号对应的“账户类别”*/
    private String accType;
    /**收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民*/
    private String payeeType;
    /**汇款去向：1 境外电汇、2 港澳台电汇、4 境内系统外同城、5 境内系统内异地、6 境内系统外异地*/
    private String remitRoute;
    /**汇款用途*/
    private String remitUse;

    /**关联交易号码（外汇买卖交易-远期-已违约）*/
    @BtCompositeSearch(fieldName = "t.rel_bill_no")
    private String relBillNo;
    /**办理机构*/
    private String branchCode;
    /**办理机构名称*/
    private String branchName;
    /**进口日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date arrivalDate;
    /**报关单号*/
    private String declareNo;
    /**汇款附言*/
    private String addWord;
    /**费用币种*/
    private String costCurId;
    /**费用总额*/
    private BigDecimal costMoney;

    /**创建人名称*/
    private String createName;
    /**审批人*/
    private String approveUser;
    /**审批时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    /**交易对手银行-本地清算号*/
    private String localClearNo;
    /**付款清算号*/
    private String payerLocalClearNo;
    /**支付类型（渣打银行）*/
    private String bankPayType;
    private String bankPayTypeName;
    private String bankPayTypeId;
    /**起运国（兴业银行）*/
    private String originCountry;
    private String originCountryName;
    /**现金流项目*/
    private String cashItem;
    private String cashItemName;
    /**发票日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**发票总额*/
    private BigDecimal invoiceTotal;
    /**抵扣金额*/
    private BigDecimal deductionAmount;
    /**发票净额*/
    private BigDecimal invoiceAmount;
    /**交易对手银行账号保存字段*/
    private String saveFields;

    /**
     * FPS ID
     */
    private String fpsId;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 国家代码
     */
    private String isdCode;

    /**
     * 电话号码
     */
    private String phoneCode;
    /**
     * 信用卡号
     */
    private String aiIn;

    /**
     * 香港身份证号
     */
    private String hkId;

    /**
     * 受益人关联关系
     */
    private String bnfRelate;

    /**
     * 监管类型
     */
    private String regulatoryType;

    /**
     * 单据来源
     */
    @Dict(dictGroup = "foreign.billFrom", inject = "billFromName")
    @BtCompositeSearch(fieldName = "t.bill_From")
    private String billFrom;
    @BtCompositeSearch(fieldName = "t.bill_From")
    private String billFromName;

    /**时间戳*/
    private String timeStamp;

    /**
     * 魔板ID
     */
    @BtCompositeSearch(fieldName = "t.template_id")
    private String templateId;
    private String templateName;
    private String versionNo;

    /**
     * 是否显示生命周期
     */
    private String isShowLifeCycle;


    /**货物贸易金额*/
    private BigDecimal goodsAmount;
    /**预付货款项下金额*/
    private BigDecimal advanceChargeAmount;
    /**预付货款项下所占合同金额比例*/
    private BigDecimal advanceContractRate;
    /**预计X天后报关*/
    private Integer rptGateDate;
    /**报关经营单位名称*/
    @Corp(inject="rptGateEntName")
    private String rptGateEntId;
    /**报关经营单位名称*/
    private String rptGateEntName;
    /**报关方式 R-人民币；O-外币*/
    private String rptGateType;
    /**一般贸易金额*/
    private BigDecimal generalTradeAmount;
    /**进料加工金额*/
    private BigDecimal feedAmount;
    /**其他贸易金额*/
    private BigDecimal otherTradeAmount;
    /**边境贸易金额*/
    private BigDecimal borderTradeAmount;
    /**海关特殊监管区域及保税监管场所进出境物流货物金额*/
    private BigDecimal customAmount;
    /**离岸转手买卖金额*/
    private BigDecimal offshoreAmount;
    /**其他金额*/
    private BigDecimal otherAmount;
    /**投资收益金额*/
    private BigDecimal investAmt;
    /**投资收益-国际收支编码*/
    private String investNo;
    /**投资收益-批准证书号*/
    private String investApplyNbr;
    /**经常转移金额*/
    private BigDecimal oftenAmt;
    /**经常转移-国际收支编码*/
    private String oftenNo;
    /**直接投资-批准证书号*/
    private String directInvestmentCertNo;
    /**预收款比例*/
    private BigDecimal advanceRate;
    /**结账期*/
    private Integer checkDay;


    /**
     * 自定义字段信息
     */
    private Map<String, String> fexCustInfos;

    /**损益信息集合*/
    private List<FexProfitLossManageVO> fexProfitLossManageList;

    public List<FexProfitLossManageVO> getFexProfitLossManageList() {
        return fexProfitLossManageList;
    }

    public void setFexProfitLossManageList(List<FexProfitLossManageVO> fexProfitLossManageList) {
        this.fexProfitLossManageList = fexProfitLossManageList;
    }

    public BigDecimal getExceptVal() {
        return exceptVal;
    }

    public void setExceptVal(BigDecimal exceptVal) {
        this.exceptVal = exceptVal;
    }

    public String getCotpAgentSwiftCode() {
        return cotpAgentSwiftCode;
    }

    public void setCotpAgentSwiftCode(String cotpAgentSwiftCode) {
        this.cotpAgentSwiftCode = cotpAgentSwiftCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTranAppDtlId() {
        return tranAppDtlId;
    }

    public void setTranAppDtlId(String tranAppDtlId) {
        this.tranAppDtlId = tranAppDtlId;
    }

    public String getTranAppDtlNo() {
        return tranAppDtlNo;
    }

    public void setTranAppDtlNo(String tranAppDtlNo) {
        this.tranAppDtlNo = tranAppDtlNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getIsSwapTran() {
        return isSwapTran;
    }

    public void setIsSwapTran(String isSwapTran) {
        this.isSwapTran = isSwapTran;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
    }

    public String getDeliveryCurName() {
        return deliveryCurName;
    }

    public void setDeliveryCurName(String deliveryCurName) {
        this.deliveryCurName = deliveryCurName;
    }

    public String getDeliveryEnglishCode() {
        return deliveryEnglishCode;
    }

    public void setDeliveryEnglishCode(String deliveryEnglishCode) {
        this.deliveryEnglishCode = deliveryEnglishCode;
    }

    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    public String getBuyCurName() {
        return buyCurName;
    }

    public void setBuyCurName(String buyCurName) {
        this.buyCurName = buyCurName;
    }

    public String getBuyEnglishCode() {
        return buyEnglishCode;
    }

    public void setBuyEnglishCode(String buyEnglishCode) {
        this.buyEnglishCode = buyEnglishCode;
    }

    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    public String getSellCurName() {
        return sellCurName;
    }

    public void setSellCurName(String sellCurName) {
        this.sellCurName = sellCurName;
    }

    public String getSellEnglishCode() {
        return sellEnglishCode;
    }

    public void setSellEnglishCode(String sellEnglishCode) {
        this.sellEnglishCode = sellEnglishCode;
    }

    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    public String getFixedMoney() {
        return fixedMoney;
    }

    public void setFixedMoney(String fixedMoney) {
        this.fixedMoney = fixedMoney;
    }

    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    public String getForeignContractNo() {
        return foreignContractNo;
    }

    public void setForeignContractNo(String foreignContractNo) {
        this.foreignContractNo = foreignContractNo;
    }

    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    public String getBankNote() {
        return bankNote;
    }

    public void setBankNote(String bankNote) {
        this.bankNote = bankNote;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
    }

    public String getBoodType() {
        return boodType;
    }

    public void setBoodType(String boodType) {
        this.boodType = boodType;
    }

    public String getBoodAccId() {
        return boodAccId;
    }

    public void setBoodAccId(String boodAccId) {
        this.boodAccId = boodAccId;
    }

    public String getBoodAcc() {
        return boodAcc;
    }

    public void setBoodAcc(String boodAcc) {
        this.boodAcc = boodAcc;
    }

    public BigDecimal getBoodRatio() {
        return boodRatio;
    }

    public void setBoodRatio(BigDecimal boodRatio) {
        this.boodRatio = boodRatio;
    }

    public String getBoodBankNote() {
        return boodBankNote;
    }

    public void setBoodBankNote(String boodBankNote) {
        this.boodBankNote = boodBankNote;
    }

    public String getBondCurId() {
        return bondCurId;
    }

    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }

    public String getBondCurName() {
        return bondCurName;
    }

    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }

    public String getBondEnglishCode() {
        return bondEnglishCode;
    }

    public void setBondEnglishCode(String bondEnglishCode) {
        this.bondEnglishCode = bondEnglishCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getForwardPoints() {
        return forwardPoints;
    }

    public void setForwardPoints(BigDecimal forwardPoints) {
        this.forwardPoints = forwardPoints;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurposeDtl() {
        return purposeDtl;
    }

    public void setPurposeDtl(String purposeDtl) {
        this.purposeDtl = purposeDtl;
    }

    public String getFundsSource() {
        return fundsSource;
    }

    public void setFundsSource(String fundsSource) {
        this.fundsSource = fundsSource;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public String getFexProp() {
        return fexProp;
    }

    public void setFexProp(String fexProp) {
        this.fexProp = fexProp;
    }

    public String getBoodRmk() {
        return boodRmk;
    }

    public void setBoodRmk(String boodRmk) {
        this.boodRmk = boodRmk;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getRmkLocal() {
        return rmkLocal;
    }

    public void setRmkLocal(String rmkLocal) {
        this.rmkLocal = rmkLocal;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getCdSignName() {
        return cdSignName;
    }

    public void setCdSignName(String cdSignName) {
        this.cdSignName = cdSignName;
    }

    public String getPayCorpId() {
        return payCorpId;
    }

    public void setPayCorpId(String payCorpId) {
        this.payCorpId = payCorpId;
    }

    public String getPayCorpName() {
        return payCorpName;
    }

    public void setPayCorpName(String payCorpName) {
        this.payCorpName = payCorpName;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getPayMoneyConvert() {
        return payMoneyConvert;
    }

    public void setPayMoneyConvert(BigDecimal payMoneyConvert) {
        this.payMoneyConvert = payMoneyConvert;
    }

    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    public String getCorpAccName() {
        return corpAccName;
    }

    public void setCorpAccName(String corpAccName) {
        this.corpAccName = corpAccName;
    }

    public String getCorpAccCurId() {
        return corpAccCurId;
    }

    public void setCorpAccCurId(String corpAccCurId) {
        this.corpAccCurId = corpAccCurId;
    }

    public String getCorpAccCurName() {
        return corpAccCurName;
    }

    public void setCorpAccCurName(String corpAccCurName) {
        this.corpAccCurName = corpAccCurName;
    }

    public String getCorpAccEnglishCode() {
        return corpAccEnglishCode;
    }

    public void setCorpAccEnglishCode(String corpAccEnglishCode) {
        this.corpAccEnglishCode = corpAccEnglishCode;
    }

    public String getCorpBankAccSwiftCode() {
        return corpBankAccSwiftCode;
    }

    public void setCorpBankAccSwiftCode(String corpBankAccSwiftCode) {
        this.corpBankAccSwiftCode = corpBankAccSwiftCode;
    }

    public String getCorpBank() {
        return corpBank;
    }

    public void setCorpBank(String corpBank) {
        this.corpBank = corpBank;
    }

    public String getCorpBankName() {
        return corpBankName;
    }

    public void setCorpBankName(String corpBankName) {
        this.corpBankName = corpBankName;
    }

    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getRemitMethod() {
        return remitMethod;
    }

    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    public String getCorpNameEn() {
        return corpNameEn;
    }

    public void setCorpNameEn(String corpNameEn) {
        this.corpNameEn = corpNameEn;
    }

    public String getCorpAddrEn() {
        return corpAddrEn;
    }

    public void setCorpAddrEn(String corpAddrEn) {
        this.corpAddrEn = corpAddrEn;
    }

    public String getCorpAddr() {
        return corpAddr;
    }

    public void setCorpAddr(String corpAddr) {
        this.corpAddr = corpAddr;
    }

    public String getCorpContact() {
        return corpContact;
    }

    public void setCorpContact(String corpContact) {
        this.corpContact = corpContact;
    }

    public String getCorpContactTel() {
        return corpContactTel;
    }

    public void setCorpContactTel(String corpContactTel) {
        this.corpContactTel = corpContactTel;
    }

    public String getCostSign() {
        return costSign;
    }

    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }

    public String getCostAcc() {
        return costAcc;
    }

    public void setCostAcc(String costAcc) {
        this.costAcc = costAcc;
    }

    public String getCostAcc2() {
        return costAcc2;
    }

    public void setCostAcc2(String costAcc2) {
        this.costAcc2 = costAcc2;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCostAccId() {
        return costAccId;
    }

    public void setCostAccId(String costAccId) {
        this.costAccId = costAccId;
    }

    public String getCostAccId2() {
        return costAccId2;
    }

    public void setCostAccId2(String costAccId2) {
        this.costAccId2 = costAccId2;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getIsBonded() {
        return isBonded;
    }

    public void setIsBonded(String isBonded) {
        this.isBonded = isBonded;
    }

    public String getTranCode1() {
        return tranCode1;
    }

    public void setTranCode1(String tranCode1) {
        this.tranCode1 = tranCode1;
    }

    public String getTranName1() {
        return tranName1;
    }

    public void setTranName1(String tranName1) {
        this.tranName1 = tranName1;
    }

    public BigDecimal getTranAmt1() {
        return tranAmt1;
    }

    public void setTranAmt1(BigDecimal tranAmt1) {
        this.tranAmt1 = tranAmt1;
    }

    public String getTranCurId1() {
        return tranCurId1;
    }

    public void setTranCurId1(String tranCurId1) {
        this.tranCurId1 = tranCurId1;
    }

    public String getTranCurName1() {
        return tranCurName1;
    }

    public void setTranCurName1(String tranCurName1) {
        this.tranCurName1 = tranCurName1;
    }

    public String getTranEnglishCode1() {
        return tranEnglishCode1;
    }

    public void setTranEnglishCode1(String tranEnglishCode1) {
        this.tranEnglishCode1 = tranEnglishCode1;
    }

    public String getTranAddWord1() {
        return tranAddWord1;
    }

    public void setTranAddWord1(String tranAddWord1) {
        this.tranAddWord1 = tranAddWord1;
    }

    public String getTranCode2() {
        return tranCode2;
    }

    public void setTranCode2(String tranCode2) {
        this.tranCode2 = tranCode2;
    }

    public String getTranName2() {
        return tranName2;
    }

    public void setTranName2(String tranName2) {
        this.tranName2 = tranName2;
    }

    public BigDecimal getTranAmt2() {
        return tranAmt2;
    }

    public void setTranAmt2(BigDecimal tranAmt2) {
        this.tranAmt2 = tranAmt2;
    }

    public String getTranCurId2() {
        return tranCurId2;
    }

    public void setTranCurId2(String tranCurId2) {
        this.tranCurId2 = tranCurId2;
    }

    public String getTranCurName2() {
        return tranCurName2;
    }

    public void setTranCurName2(String tranCurName2) {
        this.tranCurName2 = tranCurName2;
    }

    public String getTranEnglishCode2() {
        return tranEnglishCode2;
    }

    public void setTranEnglishCode2(String tranEnglishCode2) {
        this.tranEnglishCode2 = tranEnglishCode2;
    }

    public String getTranAddWord2() {
        return tranAddWord2;
    }

    public void setTranAddWord2(String tranAddWord2) {
        this.tranAddWord2 = tranAddWord2;
    }

    public String getRemitNature() {
        return remitNature;
    }

    public void setRemitNature(String remitNature) {
        this.remitNature = remitNature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getCotpEnName() {
        return cotpEnName;
    }

    public void setCotpEnName(String cotpEnName) {
        this.cotpEnName = cotpEnName;
    }

    public String getCotpBicCode() {
        return cotpBicCode;
    }

    public void setCotpBicCode(String cotpBicCode) {
        this.cotpBicCode = cotpBicCode;
    }

    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    public String getCotpBankName() {
        return cotpBankName;
    }

    public void setCotpBankName(String cotpBankName) {
        this.cotpBankName = cotpBankName;
    }

    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    public String getCotpAccName() {
        return cotpAccName;
    }

    public void setCotpAccName(String cotpAccName) {
        this.cotpAccName = cotpAccName;
    }

    public String getCotpAccCurId() {
        return cotpAccCurId;
    }

    public void setCotpAccCurId(String cotpAccCurId) {
        this.cotpAccCurId = cotpAccCurId;
    }

    public String getCotpAccCurName() {
        return cotpAccCurName;
    }

    public void setCotpAccCurName(String cotpAccCurName) {
        this.cotpAccCurName = cotpAccCurName;
    }

    public String getCotpAccEnglishCode() {
        return cotpAccEnglishCode;
    }

    public void setCotpAccEnglishCode(String cotpAccEnglishCode) {
        this.cotpAccEnglishCode = cotpAccEnglishCode;
    }

    public String getCotpCountry() {
        return cotpCountry;
    }

    public void setCotpCountry(String cotpCountry) {
        this.cotpCountry = cotpCountry;
    }

    public String getCotpCountryName() {
        return cotpCountryName;
    }

    public void setCotpCountryName(String cotpCountryName) {
        this.cotpCountryName = cotpCountryName;
    }

    public String getCotpAddress() {
        return cotpAddress;
    }

    public void setCotpAddress(String cotpAddress) {
        this.cotpAddress = cotpAddress;
    }

    public String getCotpAddressName() {
        return cotpAddressName;
    }

    public void setCotpAddressName(String cotpAddressName) {
        this.cotpAddressName = cotpAddressName;
    }

    public String getCotpAddressNameEn() {
        return cotpAddressNameEn;
    }

    public void setCotpAddressNameEn(String cotpAddressNameEn) {
        this.cotpAddressNameEn = cotpAddressNameEn;
    }

    public String getCotpProv() {
        return cotpProv;
    }

    public void setCotpProv(String cotpProv) {
        this.cotpProv = cotpProv;
    }

    public String getCotpProvName() {
        return cotpProvName;
    }

    public void setCotpProvName(String cotpProvName) {
        this.cotpProvName = cotpProvName;
    }

    public String getCotpCity() {
        return cotpCity;
    }

    public void setCotpCity(String cotpCity) {
        this.cotpCity = cotpCity;
    }

    public String getCotpCityName() {
        return cotpCityName;
    }

    public void setCotpCityName(String cotpCityName) {
        this.cotpCityName = cotpCityName;
    }

    public String getCotpAdrr() {
        return cotpAdrr;
    }

    public void setCotpAdrr(String cotpAdrr) {
        this.cotpAdrr = cotpAdrr;
    }

    public String getCotpBankAdrr() {
        return cotpBankAdrr;
    }

    public void setCotpBankAdrr(String cotpBankAdrr) {
        this.cotpBankAdrr = cotpBankAdrr;
    }

    public String getCotpSwiftCode() {
        return cotpSwiftCode;
    }

    public void setCotpSwiftCode(String cotpSwiftCode) {
        this.cotpSwiftCode = cotpSwiftCode;
    }

    public String getCotpAgentBankAddr() {
        return cotpAgentBankAddr;
    }

    public void setCotpAgentBankAddr(String cotpAgentBankAddr) {
        this.cotpAgentBankAddr = cotpAgentBankAddr;
    }

    public String getCotpAgentBank() {
        return cotpAgentBank;
    }

    public void setCotpAgentBank(String cotpAgentBank) {
        this.cotpAgentBank = cotpAgentBank;
    }

    public String getCotpAgentBankAcc() {
        return cotpAgentBankAcc;
    }

    public void setCotpAgentBankAcc(String cotpAgentBankAcc) {
        this.cotpAgentBankAcc = cotpAgentBankAcc;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCapitalUse() {
        return capitalUse;
    }

    public void setCapitalUse(String capitalUse) {
        this.capitalUse = capitalUse;
    }

    public String getSrcBank() {
        return srcBank;
    }

    public void setSrcBank(String srcBank) {
        this.srcBank = srcBank;
    }

    public String getSrcBankName() {
        return srcBankName;
    }

    public void setSrcBankName(String srcBankName) {
        this.srcBankName = srcBankName;
    }

    public String getSrcAccId() {
        return srcAccId;
    }

    public void setSrcAccId(String srcAccId) {
        this.srcAccId = srcAccId;
    }

    public String getSrcBankAcc() {
        return srcBankAcc;
    }

    public void setSrcBankAcc(String srcBankAcc) {
        this.srcBankAcc = srcBankAcc;
    }

    public String getSrcAccName() {
        return srcAccName;
    }

    public void setSrcAccName(String srcAccName) {
        this.srcAccName = srcAccName;
    }

    public String getSrcBankAccSwiftCode() {
        return srcBankAccSwiftCode;
    }

    public void setSrcBankAccSwiftCode(String srcBankAccSwiftCode) {
        this.srcBankAccSwiftCode = srcBankAccSwiftCode;
    }

    public String getTargetBank() {
        return targetBank;
    }

    public void setTargetBank(String targetBank) {
        this.targetBank = targetBank;
    }

    public String getTargetBankName() {
        return targetBankName;
    }

    public void setTargetBankName(String targetBankName) {
        this.targetBankName = targetBankName;
    }

    public String getTargetAccId() {
        return targetAccId;
    }

    public void setTargetAccId(String targetAccId) {
        this.targetAccId = targetAccId;
    }

    public String getTargetBankAcc() {
        return targetBankAcc;
    }

    public void setTargetBankAcc(String targetBankAcc) {
        this.targetBankAcc = targetBankAcc;
    }

    public String getTargetAccName() {
        return targetAccName;
    }

    public void setTargetAccName(String targetAccName) {
        this.targetAccName = targetAccName;
    }

    public String getTargetAccCurId() {
        return targetAccCurId;
    }

    public void setTargetAccCurId(String targetAccCurId) {
        this.targetAccCurId = targetAccCurId;
    }

    public String getTargetAccCurName() {
        return targetAccCurName;
    }

    public void setTargetAccCurName(String targetAccCurName) {
        this.targetAccCurName = targetAccCurName;
    }

    public String getTargetAccEnglishCode() {
        return targetAccEnglishCode;
    }

    public void setTargetAccEnglishCode(String targetAccEnglishCode) {
        this.targetAccEnglishCode = targetAccEnglishCode;
    }

    public String getTargetCorp() {
        return targetCorp;
    }

    public void setTargetCorp(String targetCorp) {
        this.targetCorp = targetCorp;
    }

    public String getTargetCorpName() {
        return targetCorpName;
    }

    public void setTargetCorpName(String targetCorpName) {
        this.targetCorpName = targetCorpName;
    }

    public String getIsDomesticBank() {
        return isDomesticBank;
    }

    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }

    public String getTargetBankCode() {
        return targetBankCode;
    }

    public void setTargetBankCode(String targetBankCode) {
        this.targetBankCode = targetBankCode;
    }

    public String getTargetAddr() {
        return targetAddr;
    }

    public void setTargetAddr(String targetAddr) {
        this.targetAddr = targetAddr;
    }

    public String getAgentBank() {
        return agentBank;
    }

    public void setAgentBank(String agentBank) {
        this.agentBank = agentBank;
    }

    public String getAgentBankAcc() {
        return agentBankAcc;
    }

    public void setAgentBankAcc(String agentBankAcc) {
        this.agentBankAcc = agentBankAcc;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getCotpNo() {
        return cotpNo;
    }

    public void setCotpNo(String cotpNo) {
        this.cotpNo = cotpNo;
    }

    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    public String getTranGroup() {
        return tranGroup;
    }

    public void setTranGroup(String tranGroup) {
        this.tranGroup = tranGroup;
    }

    public String getTranGroupName() {
        return tranGroupName;
    }

    public void setTranGroupName(String tranGroupName) {
        this.tranGroupName = tranGroupName;
    }

    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTranAttr() {
        return tranAttr;
    }

    public void setTranAttr(String tranAttr) {
        this.tranAttr = tranAttr;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSettleBusType() {
        return settleBusType;
    }

    public void setSettleBusType(String settleBusType) {
        this.settleBusType = settleBusType;
    }

    public String getAutoCal() {
        return autoCal;
    }

    public void setAutoCal(String autoCal) {
        this.autoCal = autoCal;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public String getUpdownDirection() {
        return updownDirection;
    }

    public void setUpdownDirection(String updownDirection) {
        this.updownDirection = updownDirection;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Date getAvgBeginDate() {
        return avgBeginDate;
    }

    public void setAvgBeginDate(Date avgBeginDate) {
        this.avgBeginDate = avgBeginDate;
    }

    public Date getAvgEndDate() {
        return avgEndDate;
    }

    public void setAvgEndDate(Date avgEndDate) {
        this.avgEndDate = avgEndDate;
    }

    public BigDecimal getAvgDays() {
        return avgDays;
    }

    public void setAvgDays(BigDecimal avgDays) {
        this.avgDays = avgDays;
    }

    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
    }

    public BigDecimal getExceptLowVal() {
        return exceptLowVal;
    }

    public void setExceptLowVal(BigDecimal exceptLowVal) {
        this.exceptLowVal = exceptLowVal;
    }

    public BigDecimal getExceptHignVal() {
        return exceptHignVal;
    }

    public void setExceptHignVal(BigDecimal exceptHignVal) {
        this.exceptHignVal = exceptHignVal;
    }

    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    public Date getPremiumDate() {
        return premiumDate;
    }

    public void setPremiumDate(Date premiumDate) {
        this.premiumDate = premiumDate;
    }

    public String getPremiumCur() {
        return premiumCur;
    }

    public void setPremiumCur(String premiumCur) {
        this.premiumCur = premiumCur;
    }

    public String getPremiumCurName() {
        return premiumCurName;
    }

    public void setPremiumCurName(String premiumCurName) {
        this.premiumCurName = premiumCurName;
    }

    public String getPremiumEnglishCode() {
        return premiumEnglishCode;
    }

    public void setPremiumEnglishCode(String premiumEnglishCode) {
        this.premiumEnglishCode = premiumEnglishCode;
    }

    public BigDecimal getPremiumPoint() {
        return premiumPoint;
    }

    public void setPremiumPoint(BigDecimal premiumPoint) {
        this.premiumPoint = premiumPoint;
    }

    public BigDecimal getPremiumRatio() {
        return premiumRatio;
    }

    public void setPremiumRatio(BigDecimal premiumRatio) {
        this.premiumRatio = premiumRatio;
    }

    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getPremiumRateStr() {
        return premiumRateStr;
    }

    public void setPremiumRateStr(String premiumRateStr) {
        this.premiumRateStr = premiumRateStr;
    }

    public BigDecimal getPremiumMoney() {
        return premiumMoney;
    }

    public void setPremiumMoney(BigDecimal premiumMoney) {
        this.premiumMoney = premiumMoney;
    }

    public BigDecimal getPremiumMoneyOfPremiumCur() {
        return premiumMoneyOfPremiumCur;
    }

    public void setPremiumMoneyOfPremiumCur(BigDecimal premiumMoneyOfPremiumCur) {
        this.premiumMoneyOfPremiumCur = premiumMoneyOfPremiumCur;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public BigDecimal getRefundPoint() {
        return refundPoint;
    }

    public void setRefundPoint(BigDecimal refundPoint) {
        this.refundPoint = refundPoint;
    }

    public BigDecimal getRefundRatio() {
        return refundRatio;
    }

    public void setRefundRatio(BigDecimal refundRatio) {
        this.refundRatio = refundRatio;
    }

    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getCotpAgent() {
        return cotpAgent;
    }

    public void setCotpAgent(String cotpAgent) {
        this.cotpAgent = cotpAgent;
    }

    public String getCotpAgentName() {
        return cotpAgentName;
    }

    public void setCotpAgentName(String cotpAgentName) {
        this.cotpAgentName = cotpAgentName;
    }

    public String getCotpClearHouse() {
        return cotpClearHouse;
    }

    public void setCotpClearHouse(String cotpClearHouse) {
        this.cotpClearHouse = cotpClearHouse;
    }

    public String getCotpClearHouseName() {
        return cotpClearHouseName;
    }

    public void setCotpClearHouseName(String cotpClearHouseName) {
        this.cotpClearHouseName = cotpClearHouseName;
    }

    public String getFexOption1() {
        return fexOption1;
    }

    public void setFexOption1(String fexOption1) {
        this.fexOption1 = fexOption1;
    }

    public String getFexOption2() {
        return fexOption2;
    }

    public void setFexOption2(String fexOption2) {
        this.fexOption2 = fexOption2;
    }

    public String getFexOption3() {
        return fexOption3;
    }

    public void setFexOption3(String fexOption3) {
        this.fexOption3 = fexOption3;
    }

    public String getFexOptionType() {
        return fexOptionType;
    }

    public void setFexOptionType(String fexOptionType) {
        this.fexOptionType = fexOptionType;
    }

    public String getIsShowAgent() {
        return isShowAgent;
    }

    public void setIsShowAgent(String isShowAgent) {
        this.isShowAgent = isShowAgent;
    }

    public String getIsShowClearHouse() {
        return isShowClearHouse;
    }

    public void setIsShowClearHouse(String isShowClearHouse) {
        this.isShowClearHouse = isShowClearHouse;
    }

    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getBursarBillNo() {
        return bursarBillNo;
    }

    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
    }

    public String[] getItemCode() {
        return itemCode;
    }

    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }

    public String[] getItemName() {
        return itemName;
    }

    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }

    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }

    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }

    public BigDecimal[] getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal[] exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal[] getProcessCurMoney() {
        return processCurMoney;
    }

    public void setProcessCurMoney(BigDecimal[] processCurMoney) {
        this.processCurMoney = processCurMoney;
    }

    public String[] getBudgetCorpId() {
        return budgetCorpId;
    }

    public void setBudgetCorpId(String[] budgetCorpId) {
        this.budgetCorpId = budgetCorpId;
    }

    public String[] getProcessDetailInfo() {
        return processDetailInfo;
    }

    public void setProcessDetailInfo(String[] processDetailInfo) {
        this.processDetailInfo = processDetailInfo;
    }

    public String getBudgetInfo() {
        return budgetInfo;
    }

    public void setBudgetInfo(String budgetInfo) {
        this.budgetInfo = budgetInfo;
    }

    public List<FexCustInfoTenantVO> getFexTranCustInfos() {
        return fexTranCustInfos;
    }

    public void setFexTranCustInfos(List<FexCustInfoTenantVO> fexTranCustInfos) {
        this.fexTranCustInfos = fexTranCustInfos;
    }

    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getRemitRoute() {
        return remitRoute;
    }

    public void setRemitRoute(String remitRoute) {
        this.remitRoute = remitRoute;
    }

    public String getRemitUse() {
        return remitUse;
    }

    public void setRemitUse(String remitUse) {
        this.remitUse = remitUse;
    }

    public String getRelBillNo() {
        return relBillNo;
    }

    public void setRelBillNo(String relBillNo) {
        this.relBillNo = relBillNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDeclareNo() {
        return declareNo;
    }

    public void setDeclareNo(String declareNo) {
        this.declareNo = declareNo;
    }

    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    public String getCostCurId() {
        return costCurId;
    }

    public void setCostCurId(String costCurId) {
        this.costCurId = costCurId;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    public String getPayerLocalClearNo() {
        return payerLocalClearNo;
    }

    public void setPayerLocalClearNo(String payerLocalClearNo) {
        this.payerLocalClearNo = payerLocalClearNo;
    }

    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginCountryName() {
        return originCountryName;
    }

    public void setOriginCountryName(String originCountryName) {
        this.originCountryName = originCountryName;
    }

    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    public String getCashItemName() {
        return cashItemName;
    }

    public void setCashItemName(String cashItemName) {
        this.cashItemName = cashItemName;
    }

    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType1;
    private String remitType2;
    /**产品*/
    private String proId1;
    private String proId2;

    private String oppId1;
    private String oppId2;

    private String cotpBicCode1;
    private String cotpBicCode2;

    /**交易对手*/
    private String cotpId1;
    private String cotpId2;

    private String cotpName1;
    private String cotpName2;

    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType1;
    private String fexBusType2;
    /**外汇交易类型 0-即期、1-远期*/
    private String fexTranType1;
    private String fexTranType2;
    /**交割类型 0-本金交割、1-无本金交割*/
    private String deliveryType1;
    private String deliveryType2;
    /**交割币种*/
    private String deliveryCurId1;
    private String deliveryCurId2;
    /**买入币种*/
    private String buyCurId1;
    private String buyCurId2;
    /**卖出币种*/
    private String sellCurId1;
    private String sellCurId2;
    /**买入金额*/
    private BigDecimal buyMoney1;
    private BigDecimal buyMoney2;
    /**卖出金额*/
    private BigDecimal sellMoney1;
    private BigDecimal sellMoney2;
    /**合同汇率*/
    private BigDecimal contractRate1;
    private BigDecimal contractRate2;
    /**外汇合同号*/
    private String foreignContractNo1;
    private String foreignContractNo2;
    /**即期汇率*/
    private BigDecimal spotRate1;
    private BigDecimal spotRate2;
    /**交易编码1*/
    private String tranCode11;
    private String tranCode12;
    /**钞汇标识 1-现钞 2-现汇 */
    private String bankNote1;
    private String bankNote2;
    /**交易日期*/
    private Date tranDate1;
    private Date tranDate2;
    /**结算日期*/
    private Date settleDate1;
    private Date settleDate2;
    /**交割期限*/
    private String deliveryDeadline1;
    private String deliveryDeadline2;
    /**保证方式,1-定期保证金、2-活期保证金、3-其它保证方式、4-授信额度*/
    private String boodType1;
    private String boodType2;
    /**保证金账号*/
    private String boodAccId1;
    private String boodAccId2;
    private String boodAcc1;
    private String boodAcc2;
    /**保证金比例*/
    private BigDecimal boodRatio1;
    private BigDecimal boodRatio2;
    /**保证金钞汇标识,1-现钞，2-现汇*/
    private String boodBankNote1;
    private String boodBankNote2;
    /**保证金币种*/
    private String bondCurId1;
    private String bondCurId2;
    /**合同号*/
    private String contractNo1;
    private String contractNo2;
    /**金额*/
    private BigDecimal payMoney1;
    private BigDecimal payMoney2;
    /**远期点数*/
    private BigDecimal forwardPoints1;
    private BigDecimal forwardPoints2;
    /**外汇局批件号/备案表号/业务编号*/
    private String busNo1;
    private String busNo2;
    /**结汇资金用途*/
    private String purpose1;
    private String purpose2;
    /**结汇资金详细用途*/
    private String purposeDtl1;
    private String purposeDtl2;
    /**资金来源,1-货物贸易、2-服务贸易收益与经常转移、3-资本与金融项目*/
    private String fundsSource1;
    private String fundsSource2;
    /**发票号*/
    private String invoiceNo1;
    private String invoiceNo2;
    /**账户资金性质 1-国内主账户、2-国外主账户、3-经常项目、4-资本项目、5-NRA账户*/
    private String accNature1;
    private String accNature2;
    /**结售汇属性*/
    private String fexProp1;
    private String fexProp2;
    /**主体单位联系人*/
    private String corpContact1;
    private String corpContact2;
    /**主体单位联系电话*/
    private String corpContactTel1;
    private String corpContactTel2;
    /**其它保证方式说明*/
    private String boodRmk1;
    private String boodRmk2;
    /**备注*/
    private String rmk1;
    private String rmk2;
    /**备注(本地语言)*/
    private String rmkLocal1;
    private String rmkLocal2;
    /**交易对手业务编号*/
    private String cotpNo1;
    private String cotpNo2;
    /**交易地点*/
    private String tranAddr1;
    private String tranAddr2;
    /**交易地点ID*/
    private String tranAddrId1;
    private String tranAddrId2;
    /**所属交易组*/
    private String tranGroup1;
    private String tranGroup2;
    /**资金计划相关信息*/
    private String budget1;
    private String budget2;

    /**主体单位银行账户ID*/
    private String corpAccId1;
    private String corpAccId2;
    /**主体单位银行*/
    private String corpBank1;
    private String corpBank2;
    /**主体单位银行账户*/
    private String corpBankAcc1;
    private String corpBankAcc2;
    private String corpAccName1;
    private String corpAccName2;
    /**交易对手银行账号*/
    private String cotpAccId1;
    private String cotpAccId2;
    private String cotpBankAcc1;
    private String cotpBankAcc2;
    private String cotpAccName1;
    private String cotpAccName2;
    /**交易对手银行*/
    private String cotpBank1;
    private String cotpBank2;

    /**源银行*/
    private String srcBank1;
    private String srcBank2;
    /**源银行账户*/
    private String srcAccId1;
    private String srcAccId2;
    private String srcBankAcc1;
    private String srcBankAcc2;
    private String srcAccName1;
    private String srcAccName2;
    /**目标银行*/
    private String targetBank1;
    private String targetBank2;
    /**目标银行账户*/
    private String targetAccId1;
    private String targetAccId2;
    private String targetBankAcc1;
    private String targetBankAcc2;
    private String targetAccName1;
    private String targetAccName2;

    /**结汇业务类型*/
    private String settleBusType1;
    private String settleBusType2;

    /**自动计算买入金额*/
    private String autoCal1;
    private String autoCal2;

    /**固定金额 1-买入金额 2-卖出金额*/
    private String fixedMoney1;
    private String fixedMoney2;

    /**跨行标识*/
    private String difBank1;
    private String difBank2;

    /**是否线下付款*/
    private String isPayed1;
    private String isPayed2;

    /**复制标志*/
    private String cloneFlag;

    /**汇款附言*/
    private String addWord1;
    /**汇款附言*/
    private String addWord2;

    /**支付类型（渣打银行）*/
    private String bankPayType1;
    private String bankPayType2;
    /**受益人关联关系*/
    private String bnfRelate1;
    private String bnfRelate2;


    /**拆分前单据号*/
    private String splitId;
    /**合并后单据号*/
    private String mergeId;
    /**原单据号*/
    @BtCompositeSearch(fieldName = "t.orig_bill_no")
    private String origBillNo;
    /**流水勾兑状态**/
    private String flowClaimStatus;

    public String getFlowClaimStatus() {
        return flowClaimStatus;
    }

    public void setFlowClaimStatus(String flowClaimStatus) {
        this.flowClaimStatus = flowClaimStatus;
    }

    public String getOrigBillNo() {
        return origBillNo;
    }

    public void setOrigBillNo(String origBillNo) {
        this.origBillNo = origBillNo;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    public String getRemitType1() {
        return remitType1;
    }

    public void setRemitType1(String remitType1) {
        this.remitType1 = remitType1;
    }

    public String getRemitType2() {
        return remitType2;
    }

    public void setRemitType2(String remitType2) {
        this.remitType2 = remitType2;
    }

    public String getProId1() {

        return proId1;
    }

    public void setProId1(String proId1) {
        this.proId1 = proId1;
    }

    public String getProId2() {
        return proId2;
    }

    public void setProId2(String proId2) {
        this.proId2 = proId2;
    }

    public String getOppId1() {
        return oppId1;
    }

    public void setOppId1(String oppId1) {
        this.oppId1 = oppId1;
    }

    public String getOppId2() {
        return oppId2;
    }

    public void setOppId2(String oppId2) {
        this.oppId2 = oppId2;
    }

    public String getCotpBicCode1() {
        return cotpBicCode1;
    }

    public void setCotpBicCode1(String cotpBicCode1) {
        this.cotpBicCode1 = cotpBicCode1;
    }

    public String getCotpBicCode2() {
        return cotpBicCode2;
    }

    public void setCotpBicCode2(String cotpBicCode2) {
        this.cotpBicCode2 = cotpBicCode2;
    }

    public String getCotpId1() {
        return cotpId1;
    }

    public void setCotpId1(String cotpId1) {
        this.cotpId1 = cotpId1;
    }

    public String getCotpId2() {
        return cotpId2;
    }

    public void setCotpId2(String cotpId2) {
        this.cotpId2 = cotpId2;
    }

    public String getCotpName1() {
        return cotpName1;
    }

    public void setCotpName1(String cotpName1) {
        this.cotpName1 = cotpName1;
    }

    public String getCotpName2() {
        return cotpName2;
    }

    public void setCotpName2(String cotpName2) {
        this.cotpName2 = cotpName2;
    }

    public String getFexBusType1() {
        return fexBusType1;
    }

    public void setFexBusType1(String fexBusType1) {
        this.fexBusType1 = fexBusType1;
    }

    public String getFexBusType2() {
        return fexBusType2;
    }

    public void setFexBusType2(String fexBusType2) {
        this.fexBusType2 = fexBusType2;
    }

    public String getFexTranType1() {
        return fexTranType1;
    }

    public void setFexTranType1(String fexTranType1) {
        this.fexTranType1 = fexTranType1;
    }

    public String getFexTranType2() {
        return fexTranType2;
    }

    public void setFexTranType2(String fexTranType2) {
        this.fexTranType2 = fexTranType2;
    }

    public String getDeliveryType1() {
        return deliveryType1;
    }

    public void setDeliveryType1(String deliveryType1) {
        this.deliveryType1 = deliveryType1;
    }

    public String getDeliveryType2() {
        return deliveryType2;
    }

    public void setDeliveryType2(String deliveryType2) {
        this.deliveryType2 = deliveryType2;
    }

    public String getDeliveryCurId1() {
        return deliveryCurId1;
    }

    public void setDeliveryCurId1(String deliveryCurId1) {
        this.deliveryCurId1 = deliveryCurId1;
    }

    public String getDeliveryCurId2() {
        return deliveryCurId2;
    }

    public void setDeliveryCurId2(String deliveryCurId2) {
        this.deliveryCurId2 = deliveryCurId2;
    }

    public String getBuyCurId1() {
        return buyCurId1;
    }

    public void setBuyCurId1(String buyCurId1) {
        this.buyCurId1 = buyCurId1;
    }

    public String getBuyCurId2() {
        return buyCurId2;
    }

    public void setBuyCurId2(String buyCurId2) {
        this.buyCurId2 = buyCurId2;
    }

    public String getSellCurId1() {
        return sellCurId1;
    }

    public void setSellCurId1(String sellCurId1) {
        this.sellCurId1 = sellCurId1;
    }

    public String getSellCurId2() {
        return sellCurId2;
    }

    public void setSellCurId2(String sellCurId2) {
        this.sellCurId2 = sellCurId2;
    }

    public BigDecimal getBuyMoney1() {
        return buyMoney1;
    }

    public void setBuyMoney1(BigDecimal buyMoney1) {
        this.buyMoney1 = buyMoney1;
    }

    public BigDecimal getBuyMoney2() {
        return buyMoney2;
    }

    public void setBuyMoney2(BigDecimal buyMoney2) {
        this.buyMoney2 = buyMoney2;
    }

    public BigDecimal getSellMoney1() {
        return sellMoney1;
    }

    public void setSellMoney1(BigDecimal sellMoney1) {
        this.sellMoney1 = sellMoney1;
    }

    public BigDecimal getSellMoney2() {
        return sellMoney2;
    }

    public void setSellMoney2(BigDecimal sellMoney2) {
        this.sellMoney2 = sellMoney2;
    }

    public BigDecimal getContractRate1() {
        return contractRate1;
    }

    public void setContractRate1(BigDecimal contractRate1) {
        this.contractRate1 = contractRate1;
    }

    public BigDecimal getContractRate2() {
        return contractRate2;
    }

    public void setContractRate2(BigDecimal contractRate2) {
        this.contractRate2 = contractRate2;
    }

    public String getForeignContractNo1() {
        return foreignContractNo1;
    }

    public void setForeignContractNo1(String foreignContractNo1) {
        this.foreignContractNo1 = foreignContractNo1;
    }

    public String getForeignContractNo2() {
        return foreignContractNo2;
    }

    public void setForeignContractNo2(String foreignContractNo2) {
        this.foreignContractNo2 = foreignContractNo2;
    }

    public BigDecimal getSpotRate1() {
        return spotRate1;
    }

    public void setSpotRate1(BigDecimal spotRate1) {
        this.spotRate1 = spotRate1;
    }

    public BigDecimal getSpotRate2() {
        return spotRate2;
    }

    public void setSpotRate2(BigDecimal spotRate2) {
        this.spotRate2 = spotRate2;
    }

    public String getTranCode11() {
        return tranCode11;
    }

    public void setTranCode11(String tranCode11) {
        this.tranCode11 = tranCode11;
    }

    public String getTranCode12() {
        return tranCode12;
    }

    public void setTranCode12(String tranCode12) {
        this.tranCode12 = tranCode12;
    }

    public String getBankNote1() {
        return bankNote1;
    }

    public void setBankNote1(String bankNote1) {
        this.bankNote1 = bankNote1;
    }

    public String getBankNote2() {
        return bankNote2;
    }

    public void setBankNote2(String bankNote2) {
        this.bankNote2 = bankNote2;
    }

    public Date getTranDate1() {
        return tranDate1;
    }

    public void setTranDate1(Date tranDate1) {
        this.tranDate1 = tranDate1;
    }

    public Date getTranDate2() {
        return tranDate2;
    }

    public void setTranDate2(Date tranDate2) {
        this.tranDate2 = tranDate2;
    }

    public Date getSettleDate1() {
        return settleDate1;
    }

    public void setSettleDate1(Date settleDate1) {
        this.settleDate1 = settleDate1;
    }

    public Date getSettleDate2() {
        return settleDate2;
    }

    public void setSettleDate2(Date settleDate2) {
        this.settleDate2 = settleDate2;
    }

    public String getDeliveryDeadline1() {
        return deliveryDeadline1;
    }

    public void setDeliveryDeadline1(String deliveryDeadline1) {
        this.deliveryDeadline1 = deliveryDeadline1;
    }

    public String getDeliveryDeadline2() {
        return deliveryDeadline2;
    }

    public void setDeliveryDeadline2(String deliveryDeadline2) {
        this.deliveryDeadline2 = deliveryDeadline2;
    }

    public String getBoodType1() {
        return boodType1;
    }

    public void setBoodType1(String boodType1) {
        this.boodType1 = boodType1;
    }

    public String getBoodType2() {
        return boodType2;
    }

    public void setBoodType2(String boodType2) {
        this.boodType2 = boodType2;
    }

    public String getBoodAccId1() {
        return boodAccId1;
    }

    public void setBoodAccId1(String boodAccId1) {
        this.boodAccId1 = boodAccId1;
    }

    public String getBoodAccId2() {
        return boodAccId2;
    }

    public void setBoodAccId2(String boodAccId2) {
        this.boodAccId2 = boodAccId2;
    }

    public String getBoodAcc1() {
        return boodAcc1;
    }

    public void setBoodAcc1(String boodAcc1) {
        this.boodAcc1 = boodAcc1;
    }

    public String getBoodAcc2() {
        return boodAcc2;
    }

    public void setBoodAcc2(String boodAcc2) {
        this.boodAcc2 = boodAcc2;
    }

    public BigDecimal getBoodRatio1() {
        return boodRatio1;
    }

    public void setBoodRatio1(BigDecimal boodRatio1) {
        this.boodRatio1 = boodRatio1;
    }

    public BigDecimal getBoodRatio2() {
        return boodRatio2;
    }

    public void setBoodRatio2(BigDecimal boodRatio2) {
        this.boodRatio2 = boodRatio2;
    }

    public String getBoodBankNote1() {
        return boodBankNote1;
    }

    public void setBoodBankNote1(String boodBankNote1) {
        this.boodBankNote1 = boodBankNote1;
    }

    public String getBoodBankNote2() {
        return boodBankNote2;
    }

    public void setBoodBankNote2(String boodBankNote2) {
        this.boodBankNote2 = boodBankNote2;
    }

    public String getBondCurId1() {
        return bondCurId1;
    }

    public void setBondCurId1(String bondCurId1) {
        this.bondCurId1 = bondCurId1;
    }

    public String getBondCurId2() {
        return bondCurId2;
    }

    public void setBondCurId2(String bondCurId2) {
        this.bondCurId2 = bondCurId2;
    }

    public String getContractNo1() {
        return contractNo1;
    }

    public void setContractNo1(String contractNo1) {
        this.contractNo1 = contractNo1;
    }

    public String getContractNo2() {
        return contractNo2;
    }

    public void setContractNo2(String contractNo2) {
        this.contractNo2 = contractNo2;
    }

    public BigDecimal getPayMoney1() {
        return payMoney1;
    }

    public void setPayMoney1(BigDecimal payMoney1) {
        this.payMoney1 = payMoney1;
    }

    public BigDecimal getPayMoney2() {
        return payMoney2;
    }

    public void setPayMoney2(BigDecimal payMoney2) {
        this.payMoney2 = payMoney2;
    }

    public BigDecimal getForwardPoints1() {
        return forwardPoints1;
    }

    public void setForwardPoints1(BigDecimal forwardPoints1) {
        this.forwardPoints1 = forwardPoints1;
    }

    public BigDecimal getForwardPoints2() {
        return forwardPoints2;
    }

    public void setForwardPoints2(BigDecimal forwardPoints2) {
        this.forwardPoints2 = forwardPoints2;
    }

    public String getBusNo1() {
        return busNo1;
    }

    public void setBusNo1(String busNo1) {
        this.busNo1 = busNo1;
    }

    public String getBusNo2() {
        return busNo2;
    }

    public void setBusNo2(String busNo2) {
        this.busNo2 = busNo2;
    }

    public String getPurpose1() {
        return purpose1;
    }

    public void setPurpose1(String purpose1) {
        this.purpose1 = purpose1;
    }

    public String getPurpose2() {
        return purpose2;
    }

    public void setPurpose2(String purpose2) {
        this.purpose2 = purpose2;
    }

    public String getPurposeDtl1() {
        return purposeDtl1;
    }

    public void setPurposeDtl1(String purposeDtl1) {
        this.purposeDtl1 = purposeDtl1;
    }

    public String getPurposeDtl2() {
        return purposeDtl2;
    }

    public void setPurposeDtl2(String purposeDtl2) {
        this.purposeDtl2 = purposeDtl2;
    }

    public String getFundsSource1() {
        return fundsSource1;
    }

    public void setFundsSource1(String fundsSource1) {
        this.fundsSource1 = fundsSource1;
    }

    public String getFundsSource2() {
        return fundsSource2;
    }

    public void setFundsSource2(String fundsSource2) {
        this.fundsSource2 = fundsSource2;
    }

    public String getInvoiceNo1() {
        return invoiceNo1;
    }

    public void setInvoiceNo1(String invoiceNo1) {
        this.invoiceNo1 = invoiceNo1;
    }

    public String getInvoiceNo2() {
        return invoiceNo2;
    }

    public void setInvoiceNo2(String invoiceNo2) {
        this.invoiceNo2 = invoiceNo2;
    }

    public String getAccNature1() {
        return accNature1;
    }

    public void setAccNature1(String accNature1) {
        this.accNature1 = accNature1;
    }

    public String getAccNature2() {
        return accNature2;
    }

    public void setAccNature2(String accNature2) {
        this.accNature2 = accNature2;
    }

    public String getFexProp1() {
        return fexProp1;
    }

    public void setFexProp1(String fexProp1) {
        this.fexProp1 = fexProp1;
    }

    public String getFexProp2() {
        return fexProp2;
    }

    public void setFexProp2(String fexProp2) {
        this.fexProp2 = fexProp2;
    }

    public String getCorpContact1() {
        return corpContact1;
    }

    public void setCorpContact1(String corpContact1) {
        this.corpContact1 = corpContact1;
    }

    public String getCorpContact2() {
        return corpContact2;
    }

    public void setCorpContact2(String corpContact2) {
        this.corpContact2 = corpContact2;
    }

    public String getCorpContactTel1() {
        return corpContactTel1;
    }

    public void setCorpContactTel1(String corpContactTel1) {
        this.corpContactTel1 = corpContactTel1;
    }

    public String getCorpContactTel2() {
        return corpContactTel2;
    }

    public void setCorpContactTel2(String corpContactTel2) {
        this.corpContactTel2 = corpContactTel2;
    }

    public String getBoodRmk1() {
        return boodRmk1;
    }

    public void setBoodRmk1(String boodRmk1) {
        this.boodRmk1 = boodRmk1;
    }

    public String getBoodRmk2() {
        return boodRmk2;
    }

    public void setBoodRmk2(String boodRmk2) {
        this.boodRmk2 = boodRmk2;
    }

    public String getRmk1() {
        return rmk1;
    }

    public void setRmk1(String rmk1) {
        this.rmk1 = rmk1;
    }

    public String getRmk2() {
        return rmk2;
    }

    public void setRmk2(String rmk2) {
        this.rmk2 = rmk2;
    }

    public String getRmkLocal1() {
        return rmkLocal1;
    }

    public void setRmkLocal1(String rmkLocal1) {
        this.rmkLocal1 = rmkLocal1;
    }

    public String getRmkLocal2() {
        return rmkLocal2;
    }

    public void setRmkLocal2(String rmkLocal2) {
        this.rmkLocal2 = rmkLocal2;
    }

    public String getCotpNo1() {
        return cotpNo1;
    }

    public void setCotpNo1(String cotpNo1) {
        this.cotpNo1 = cotpNo1;
    }

    public String getCotpNo2() {
        return cotpNo2;
    }

    public void setCotpNo2(String cotpNo2) {
        this.cotpNo2 = cotpNo2;
    }

    public String getTranAddr1() {
        return tranAddr1;
    }

    public void setTranAddr1(String tranAddr1) {
        this.tranAddr1 = tranAddr1;
    }

    public String getTranAddr2() {
        return tranAddr2;
    }

    public void setTranAddr2(String tranAddr2) {
        this.tranAddr2 = tranAddr2;
    }

    public String getTranAddrId1() {
        return tranAddrId1;
    }

    public void setTranAddrId1(String tranAddrId1) {
        this.tranAddrId1 = tranAddrId1;
    }

    public String getTranAddrId2() {
        return tranAddrId2;
    }

    public void setTranAddrId2(String tranAddrId2) {
        this.tranAddrId2 = tranAddrId2;
    }

    public String getTranGroup1() {
        return tranGroup1;
    }

    public void setTranGroup1(String tranGroup1) {
        this.tranGroup1 = tranGroup1;
    }

    public String getTranGroup2() {
        return tranGroup2;
    }

    public void setTranGroup2(String tranGroup2) {
        this.tranGroup2 = tranGroup2;
    }

    public String getBudget1() {
        return budget1;
    }

    public void setBudget1(String budget1) {
        this.budget1 = budget1;
    }

    public String getBudget2() {
        return budget2;
    }

    public void setBudget2(String budget2) {
        this.budget2 = budget2;
    }

    public String getCorpAccId1() {
        return corpAccId1;
    }

    public void setCorpAccId1(String corpAccId1) {
        this.corpAccId1 = corpAccId1;
    }

    public String getCorpAccId2() {
        return corpAccId2;
    }

    public void setCorpAccId2(String corpAccId2) {
        this.corpAccId2 = corpAccId2;
    }

    public String getCorpBank1() {
        return corpBank1;
    }

    public void setCorpBank1(String corpBank1) {
        this.corpBank1 = corpBank1;
    }

    public String getCorpBank2() {
        return corpBank2;
    }

    public void setCorpBank2(String corpBank2) {
        this.corpBank2 = corpBank2;
    }

    public String getCorpBankAcc1() {
        return corpBankAcc1;
    }

    public void setCorpBankAcc1(String corpBankAcc1) {
        this.corpBankAcc1 = corpBankAcc1;
    }

    public String getCorpBankAcc2() {
        return corpBankAcc2;
    }

    public void setCorpBankAcc2(String corpBankAcc2) {
        this.corpBankAcc2 = corpBankAcc2;
    }

    public String getCorpAccName1() {
        return corpAccName1;
    }

    public void setCorpAccName1(String corpAccName1) {
        this.corpAccName1 = corpAccName1;
    }

    public String getCorpAccName2() {
        return corpAccName2;
    }

    public void setCorpAccName2(String corpAccName2) {
        this.corpAccName2 = corpAccName2;
    }

    public String getCotpAccId1() {
        return cotpAccId1;
    }

    public void setCotpAccId1(String cotpAccId1) {
        this.cotpAccId1 = cotpAccId1;
    }

    public String getCotpAccId2() {
        return cotpAccId2;
    }

    public void setCotpAccId2(String cotpAccId2) {
        this.cotpAccId2 = cotpAccId2;
    }

    public String getCotpBankAcc1() {
        return cotpBankAcc1;
    }

    public void setCotpBankAcc1(String cotpBankAcc1) {
        this.cotpBankAcc1 = cotpBankAcc1;
    }

    public String getCotpBankAcc2() {
        return cotpBankAcc2;
    }

    public void setCotpBankAcc2(String cotpBankAcc2) {
        this.cotpBankAcc2 = cotpBankAcc2;
    }

    public String getCotpAccName1() {
        return cotpAccName1;
    }

    public void setCotpAccName1(String cotpAccName1) {
        this.cotpAccName1 = cotpAccName1;
    }

    public String getCotpAccName2() {
        return cotpAccName2;
    }

    public void setCotpAccName2(String cotpAccName2) {
        this.cotpAccName2 = cotpAccName2;
    }

    public String getCotpBank1() {
        return cotpBank1;
    }

    public void setCotpBank1(String cotpBank1) {
        this.cotpBank1 = cotpBank1;
    }

    public String getCotpBank2() {
        return cotpBank2;
    }

    public void setCotpBank2(String cotpBank2) {
        this.cotpBank2 = cotpBank2;
    }

    public String getSrcBank1() {
        return srcBank1;
    }

    public void setSrcBank1(String srcBank1) {
        this.srcBank1 = srcBank1;
    }

    public String getSrcBank2() {
        return srcBank2;
    }

    public void setSrcBank2(String srcBank2) {
        this.srcBank2 = srcBank2;
    }

    public String getSrcAccId1() {
        return srcAccId1;
    }

    public void setSrcAccId1(String srcAccId1) {
        this.srcAccId1 = srcAccId1;
    }

    public String getSrcAccId2() {
        return srcAccId2;
    }

    public void setSrcAccId2(String srcAccId2) {
        this.srcAccId2 = srcAccId2;
    }

    public String getSrcBankAcc1() {
        return srcBankAcc1;
    }

    public void setSrcBankAcc1(String srcBankAcc1) {
        this.srcBankAcc1 = srcBankAcc1;
    }

    public String getSrcBankAcc2() {
        return srcBankAcc2;
    }

    public void setSrcBankAcc2(String srcBankAcc2) {
        this.srcBankAcc2 = srcBankAcc2;
    }

    public String getSrcAccName1() {
        return srcAccName1;
    }

    public void setSrcAccName1(String srcAccName1) {
        this.srcAccName1 = srcAccName1;
    }

    public String getSrcAccName2() {
        return srcAccName2;
    }

    public void setSrcAccName2(String srcAccName2) {
        this.srcAccName2 = srcAccName2;
    }

    public String getTargetBank1() {
        return targetBank1;
    }

    public void setTargetBank1(String targetBank1) {
        this.targetBank1 = targetBank1;
    }

    public String getTargetBank2() {
        return targetBank2;
    }

    public void setTargetBank2(String targetBank2) {
        this.targetBank2 = targetBank2;
    }

    public String getTargetAccId1() {
        return targetAccId1;
    }

    public void setTargetAccId1(String targetAccId1) {
        this.targetAccId1 = targetAccId1;
    }

    public String getTargetAccId2() {
        return targetAccId2;
    }

    public void setTargetAccId2(String targetAccId2) {
        this.targetAccId2 = targetAccId2;
    }

    public String getTargetBankAcc1() {
        return targetBankAcc1;
    }

    public void setTargetBankAcc1(String targetBankAcc1) {
        this.targetBankAcc1 = targetBankAcc1;
    }

    public String getTargetBankAcc2() {
        return targetBankAcc2;
    }

    public void setTargetBankAcc2(String targetBankAcc2) {
        this.targetBankAcc2 = targetBankAcc2;
    }

    public String getTargetAccName1() {
        return targetAccName1;
    }

    public void setTargetAccName1(String targetAccName1) {
        this.targetAccName1 = targetAccName1;
    }

    public String getTargetAccName2() {
        return targetAccName2;
    }

    public void setTargetAccName2(String targetAccName2) {
        this.targetAccName2 = targetAccName2;
    }

    public String getSettleBusType1() {
        return settleBusType1;
    }

    public void setSettleBusType1(String settleBusType1) {
        this.settleBusType1 = settleBusType1;
    }

    public String getSettleBusType2() {
        return settleBusType2;
    }

    public void setSettleBusType2(String settleBusType2) {
        this.settleBusType2 = settleBusType2;
    }

    public String getAutoCal1() {
        return autoCal1;
    }

    public void setAutoCal1(String autoCal1) {
        this.autoCal1 = autoCal1;
    }

    public String getAutoCal2() {
        return autoCal2;
    }

    public void setAutoCal2(String autoCal2) {
        this.autoCal2 = autoCal2;
    }

    public String getFixedMoney1() {
        return fixedMoney1;
    }

    public void setFixedMoney1(String fixedMoney1) {
        this.fixedMoney1 = fixedMoney1;
    }

    public String getFixedMoney2() {
        return fixedMoney2;
    }

    public void setFixedMoney2(String fixedMoney2) {
        this.fixedMoney2 = fixedMoney2;
    }

    public String getDifBank1() {
        return difBank1;
    }

    public void setDifBank1(String difBank1) {
        this.difBank1 = difBank1;
    }

    public String getDifBank2() {
        return difBank2;
    }

    public void setDifBank2(String difBank2) {
        this.difBank2 = difBank2;
    }

    public String getIsPayed1() {
        return isPayed1;
    }

    public void setIsPayed1(String isPayed1) {
        this.isPayed1 = isPayed1;
    }

    public String getIsPayed2() {
        return isPayed2;
    }

    public void setIsPayed2(String isPayed2) {
        this.isPayed2 = isPayed2;
    }

    public String getCloneFlag() {
        return cloneFlag;
    }

    public void setCloneFlag(String cloneFlag) {
        this.cloneFlag = cloneFlag;
    }

    public String getAddWord1() {
        return addWord1;
    }

    public void setAddWord1(String addWord1) {
        this.addWord1 = addWord1;
    }

    public String getAddWord2() {
        return addWord2;
    }

    public void setAddWord2(String addWord2) {
        this.addWord2 = addWord2;
    }

    public String getBankPayType1() {
        return bankPayType1;
    }

    public void setBankPayType1(String bankPayType1) {
        this.bankPayType1 = bankPayType1;
    }

    public String getBankPayType2() {
        return bankPayType2;
    }

    public void setBankPayType2(String bankPayType2) {
        this.bankPayType2 = bankPayType2;
    }

    public String getBnfRelate1() {
        return bnfRelate1;
    }

    public void setBnfRelate1(String bnfRelate1) {
        this.bnfRelate1 = bnfRelate1;
    }

    public String getBnfRelate2() {
        return bnfRelate2;
    }

    public void setBnfRelate2(String bnfRelate2) {
        this.bnfRelate2 = bnfRelate2;
    }

    /**记账汇率*/
    private BigDecimal recordRate;
    /**记账损益*/
    private BigDecimal recordProfit;
    /**实际损益*/
    private BigDecimal realProfit;
    /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
    private String opType;
    /**是否显示汇率确认*/
    private String isShowRateConfirm;


    public BigDecimal getRealProfit() {
        return realProfit;
    }

    public void setRealProfit(BigDecimal realProfit) {
        this.realProfit = realProfit;
    }

    public BigDecimal getRecordRate() {
        return recordRate;
    }

    public void setRecordRate(BigDecimal recordRate) {
        this.recordRate = recordRate;
    }

    public BigDecimal getRecordProfit() {
        return recordProfit;
    }

    public void setRecordProfit(BigDecimal recordProfit) {
        this.recordProfit = recordProfit;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getIsShowRateConfirm() {
        return isShowRateConfirm;
    }

    public void setIsShowRateConfirm(String isShowRateConfirm) {
        this.isShowRateConfirm = isShowRateConfirm;
    }

    /**支付状态 0-待付款 1-已付款*/
    @BtCompositeSearch(fieldName = "f.pay_Status")
    private String payStatus;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "exc.voucher_Stat")
    private String voucherStat;
    /**银行落地限额处理结果（取自关联现金流）：0 等待银行限额审批 / A 同意 / R 拒绝*/
    @BtCompositeSearch(fieldName = "f.LANDING_LIMIT_RESULT")
    private String landingLimitResult;
    /**银行落地限额处理结果名称（service 按当前语言查字典回填，供前后端分离接口直接展示）*/
    @BtCompositeSearch(fieldName = "t.LANDING_LIMIT_RESULT")
    private String landingLimitResultName;

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getLandingLimitResult() {
        return landingLimitResult;
    }

    public void setLandingLimitResult(String landingLimitResult) {
        this.landingLimitResult = landingLimitResult;
    }

    public String getLandingLimitResultName() {
        return landingLimitResultName;
    }

    public void setLandingLimitResultName(String landingLimitResultName) {
        this.landingLimitResultName = landingLimitResultName;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(BigDecimal invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getSaveFields() {
        return saveFields;
    }

    public void setSaveFields(String saveFields) {
        this.saveFields = saveFields;
    }

    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }

    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    public String getBnfRelate() {
        return bnfRelate;
    }

    public void setBnfRelate(String bnfRelate) {
        this.bnfRelate = bnfRelate;
    }

    public String getRegulatoryType() {
        return regulatoryType;
    }

    public void setRegulatoryType(String regulatoryType) {
        this.regulatoryType = regulatoryType;
    }

    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    public String getBillFromName() {
        return billFromName;
    }

    public void setBillFromName(String billFromName) {
        this.billFromName = billFromName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }


    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    public String getIsPaymentAmount1() {
        return isPaymentAmount1;
    }

    public void setIsPaymentAmount1(String isPaymentAmount1) {
        this.isPaymentAmount1 = isPaymentAmount1;
    }

    public String getIsPaymentAmount2() {
        return isPaymentAmount2;
    }

    public void setIsPaymentAmount2(String isPaymentAmount2) {
        this.isPaymentAmount2 = isPaymentAmount2;
    }

    public String getTranRateType() {
        return tranRateType;
    }

    public void setTranRateType(String tranRateType) {
        this.tranRateType = tranRateType;
    }

    public String getTranRateType1() {
        return tranRateType1;
    }

    public void setTranRateType1(String tranRateType1) {
        this.tranRateType1 = tranRateType1;
    }

    public String getTranRateType2() {
        return tranRateType2;
    }

    public void setTranRateType2(String tranRateType2) {
        this.tranRateType2 = tranRateType2;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    public String getQuotationMethod1() {
        return quotationMethod1;
    }

    public void setQuotationMethod1(String quotationMethod1) {
        this.quotationMethod1 = quotationMethod1;
    }

    public String getQuotationMethod2() {
        return quotationMethod2;
    }

    public void setQuotationMethod2(String quotationMethod2) {
        this.quotationMethod2 = quotationMethod2;
    }

    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }

    public BigDecimal getPositionMoney1() {
        return positionMoney1;
    }

    public void setPositionMoney1(BigDecimal positionMoney1) {
        this.positionMoney1 = positionMoney1;
    }

    public BigDecimal getPositionMoney2() {
        return positionMoney2;
    }

    public void setPositionMoney2(BigDecimal positionMoney2) {
        this.positionMoney2 = positionMoney2;
    }

    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    public String getProfitLossCur1() {
        return profitLossCur1;
    }

    public void setProfitLossCur1(String profitLossCur1) {
        this.profitLossCur1 = profitLossCur1;
    }

    public String getProfitLossCur2() {
        return profitLossCur2;
    }

    public void setProfitLossCur2(String profitLossCur2) {
        this.profitLossCur2 = profitLossCur2;
    }

    public BigDecimal getProfitLossMoney() {
        return profitLossMoney;
    }

    public void setProfitLossMoney(BigDecimal profitLossMoney) {
        this.profitLossMoney = profitLossMoney;
    }

    public BigDecimal getProfitLossMoney1() {
        return profitLossMoney1;
    }

    public void setProfitLossMoney1(BigDecimal profitLossMoney1) {
        this.profitLossMoney1 = profitLossMoney1;
    }

    public BigDecimal getProfitLossMoney2() {
        return profitLossMoney2;
    }

    public void setProfitLossMoney2(BigDecimal profitLossMoney2) {
        this.profitLossMoney2 = profitLossMoney2;
    }

    public String getConvertCurType() {
        return convertCurType;
    }

    public void setConvertCurType(String convertCurType) {
        this.convertCurType = convertCurType;
    }

    public String getConvertCurType1() {
        return convertCurType1;
    }

    public void setConvertCurType1(String convertCurType1) {
        this.convertCurType1 = convertCurType1;
    }

    public String getConvertCurType2() {
        return convertCurType2;
    }

    public void setConvertCurType2(String convertCurType2) {
        this.convertCurType2 = convertCurType2;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public BigDecimal getConvertRate1() {
        return convertRate1;
    }

    public void setConvertRate1(BigDecimal convertRate1) {
        this.convertRate1 = convertRate1;
    }

    public BigDecimal getConvertRate2() {
        return convertRate2;
    }

    public void setConvertRate2(BigDecimal convertRate2) {
        this.convertRate2 = convertRate2;
    }

    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    public BigDecimal getConvertMoney1() {
        return convertMoney1;
    }

    public void setConvertMoney1(BigDecimal convertMoney1) {
        this.convertMoney1 = convertMoney1;
    }

    public BigDecimal getConvertMoney2() {
        return convertMoney2;
    }

    public void setConvertMoney2(BigDecimal convertMoney2) {
        this.convertMoney2 = convertMoney2;
    }

    public String getQuotationCurId() {
        return quotationCurId;
    }

    public void setQuotationCurId(String quotationCurId) {
        this.quotationCurId = quotationCurId;
    }

    public String getQuotationCurId1() {
        return quotationCurId1;
    }

    public void setQuotationCurId1(String quotationCurId1) {
        this.quotationCurId1 = quotationCurId1;
    }

    public String getQuotationCurId2() {
        return quotationCurId2;
    }

    public void setQuotationCurId2(String quotationCurId2) {
        this.quotationCurId2 = quotationCurId2;
    }

    public String getQuotationReferCurId() {
        return quotationReferCurId;
    }

    public void setQuotationReferCurId(String quotationReferCurId) {
        this.quotationReferCurId = quotationReferCurId;
    }

    public String getQuotationReferCurId1() {
        return quotationReferCurId1;
    }

    public void setQuotationReferCurId1(String quotationReferCurId1) {
        this.quotationReferCurId1 = quotationReferCurId1;
    }

    public String getQuotationReferCurId2() {
        return quotationReferCurId2;
    }

    public void setQuotationReferCurId2(String quotationReferCurId2) {
        this.quotationReferCurId2 = quotationReferCurId2;
    }

    public String getTranCurPairId() {
        return tranCurPairId;
    }

    public void setTranCurPairId(String tranCurPairId) {
        this.tranCurPairId = tranCurPairId;
    }

    public String getTranCurPairId1() {
        return tranCurPairId1;
    }

    public void setTranCurPairId1(String tranCurPairId1) {
        this.tranCurPairId1 = tranCurPairId1;
    }

    public String getTranCurPairId2() {
        return tranCurPairId2;
    }

    public void setTranCurPairId2(String tranCurPairId2) {
        this.tranCurPairId2 = tranCurPairId2;
    }

    public String getTranCurPairBaseId() {
        return tranCurPairBaseId;
    }

    public void setTranCurPairBaseId(String tranCurPairBaseId) {
        this.tranCurPairBaseId = tranCurPairBaseId;
    }

    public String getTranCurPairBaseId1() {
        return tranCurPairBaseId1;
    }

    public void setTranCurPairBaseId1(String tranCurPairBaseId1) {
        this.tranCurPairBaseId1 = tranCurPairBaseId1;
    }

    public String getTranCurPairBaseId2() {
        return tranCurPairBaseId2;
    }

    public void setTranCurPairBaseId2(String tranCurPairBaseId2) {
        this.tranCurPairBaseId2 = tranCurPairBaseId2;
    }

    public String getTranCurPairReferId() {
        return tranCurPairReferId;
    }

    public void setTranCurPairReferId(String tranCurPairReferId) {
        this.tranCurPairReferId = tranCurPairReferId;
    }

    public String getTranCurPairBaseCode() {
        return tranCurPairBaseCode;
    }

    public void setTranCurPairBaseCode(String tranCurPairBaseCode) {
        this.tranCurPairBaseCode = tranCurPairBaseCode;
    }

    public String getTranCurPairReferCode() {
        return tranCurPairReferCode;
    }

    public void setTranCurPairReferCode(String tranCurPairReferCode) {
        this.tranCurPairReferCode = tranCurPairReferCode;
    }

    public String getTranCurPairReferId1() {
        return tranCurPairReferId1;
    }

    public void setTranCurPairReferId1(String tranCurPairReferId1) {
        this.tranCurPairReferId1 = tranCurPairReferId1;
    }

    public String getTranCurPairReferId2() {
        return tranCurPairReferId2;
    }

    public void setTranCurPairReferId2(String tranCurPairReferId2) {
        this.tranCurPairReferId2 = tranCurPairReferId2;
    }

    public String getBusDirection1() {
        return busDirection1;
    }

    public void setBusDirection1(String busDirection1) {
        this.busDirection1 = busDirection1;
    }

    public String getBusDirection2() {
        return busDirection2;
    }

    public void setBusDirection2(String busDirection2) {
        this.busDirection2 = busDirection2;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    public String getTranCurPairIdName() {
        return tranCurPairIdName;
    }

    public void setTranCurPairIdName(String tranCurPairIdName) {
        this.tranCurPairIdName = tranCurPairIdName;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getConvertCurReferId() {
        return convertCurReferId;
    }

    public void setConvertCurReferId(String convertCurReferId) {
        this.convertCurReferId = convertCurReferId;
    }

    public String getProfitLossCurName() {
        return profitLossCurName;
    }

    public void setProfitLossCurName(String profitLossCurName) {
        this.profitLossCurName = profitLossCurName;
    }

    public String getProfitLossCurEnglishCode() {
        return profitLossCurEnglishCode;
    }

    public void setProfitLossCurEnglishCode(String profitLossCurEnglishCode) {
        this.profitLossCurEnglishCode = profitLossCurEnglishCode;
    }

    public String getIsShowLifeCycle() {
        return isShowLifeCycle;
    }

    public void setIsShowLifeCycle(String isShowLifeCycle) {
        this.isShowLifeCycle = isShowLifeCycle;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public Map<String, String> getFexCustInfos() {
        return fexCustInfos;
    }

    public void setFexCustInfos(Map<String, String> fexCustInfos) {
        this.fexCustInfos = fexCustInfos;
    }

    public String getBankPayTypeName() {
        return bankPayTypeName;
    }

    public void setBankPayTypeName(String bankPayTypeName) {
        this.bankPayTypeName = bankPayTypeName;
    }

    public String getBankPayTypeId() {
        return bankPayTypeId;
    }

    public void setBankPayTypeId(String bankPayTypeId) {
        this.bankPayTypeId = bankPayTypeId;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getAdvanceChargeAmount() {
        return advanceChargeAmount;
    }

    public void setAdvanceChargeAmount(BigDecimal advanceChargeAmount) {
        this.advanceChargeAmount = advanceChargeAmount;
    }

    public BigDecimal getAdvanceContractRate() {
        return advanceContractRate;
    }

    public void setAdvanceContractRate(BigDecimal advanceContractRate) {
        this.advanceContractRate = advanceContractRate;
    }

    public Integer getRptGateDate() {
        return rptGateDate;
    }

    public void setRptGateDate(Integer rptGateDate) {
        this.rptGateDate = rptGateDate;
    }

    public String getRptGateEntId() {
        return rptGateEntId;
    }

    public void setRptGateEntId(String rptGateEntId) {
        this.rptGateEntId = rptGateEntId;
    }

    public String getRptGateEntName() {
        return rptGateEntName;
    }

    public void setRptGateEntName(String rptGateEntName) {
        this.rptGateEntName = rptGateEntName;
    }

    public String getRptGateType() {
        return rptGateType;
    }

    public void setRptGateType(String rptGateType) {
        this.rptGateType = rptGateType;
    }

    public BigDecimal getGeneralTradeAmount() {
        return generalTradeAmount;
    }

    public void setGeneralTradeAmount(BigDecimal generalTradeAmount) {
        this.generalTradeAmount = generalTradeAmount;
    }

    public BigDecimal getFeedAmount() {
        return feedAmount;
    }

    public void setFeedAmount(BigDecimal feedAmount) {
        this.feedAmount = feedAmount;
    }

    public BigDecimal getOtherTradeAmount() {
        return otherTradeAmount;
    }

    public void setOtherTradeAmount(BigDecimal otherTradeAmount) {
        this.otherTradeAmount = otherTradeAmount;
    }

    public BigDecimal getBorderTradeAmount() {
        return borderTradeAmount;
    }

    public void setBorderTradeAmount(BigDecimal borderTradeAmount) {
        this.borderTradeAmount = borderTradeAmount;
    }

    public BigDecimal getCustomAmount() {
        return customAmount;
    }

    public void setCustomAmount(BigDecimal customAmount) {
        this.customAmount = customAmount;
    }

    public BigDecimal getOffshoreAmount() {
        return offshoreAmount;
    }

    public void setOffshoreAmount(BigDecimal offshoreAmount) {
        this.offshoreAmount = offshoreAmount;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getInvestAmt() {
        return investAmt;
    }

    public void setInvestAmt(BigDecimal investAmt) {
        this.investAmt = investAmt;
    }

    public String getInvestNo() {
        return investNo;
    }

    public void setInvestNo(String investNo) {
        this.investNo = investNo;
    }

    public String getInvestApplyNbr() {
        return investApplyNbr;
    }

    public void setInvestApplyNbr(String investApplyNbr) {
        this.investApplyNbr = investApplyNbr;
    }

    public BigDecimal getOftenAmt() {
        return oftenAmt;
    }

    public void setOftenAmt(BigDecimal oftenAmt) {
        this.oftenAmt = oftenAmt;
    }

    public String getOftenNo() {
        return oftenNo;
    }

    public void setOftenNo(String oftenNo) {
        this.oftenNo = oftenNo;
    }

    public String getDirectInvestmentCertNo() {
        return directInvestmentCertNo;
    }

    public void setDirectInvestmentCertNo(String directInvestmentCertNo) {
        this.directInvestmentCertNo = directInvestmentCertNo;
    }

    public BigDecimal getAdvanceRate() {
        return advanceRate;
    }

    public void setAdvanceRate(BigDecimal advanceRate) {
        this.advanceRate = advanceRate;
    }

    public Integer getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(Integer checkDay) {
        this.checkDay = checkDay;
    }
}
