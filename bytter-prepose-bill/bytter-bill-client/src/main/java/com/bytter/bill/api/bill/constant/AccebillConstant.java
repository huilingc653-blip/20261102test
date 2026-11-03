package com.bytter.bill.api.bill.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 承兑汇票模块常量类
 *
 * @author luop
 */
public class AccebillConstant {
    // 票据系统参数配置信息
    /*** 票据系统参数配置：accebill 系统类型 票据 */
    public static final String AB_CONFIG_TYPE = "accebill";
    /*** 票据系统参数配置：isEmitMake 开票是否需要补录 */
    public static final String AB_CONFIG_ISEMITMAKE = "isEmitMake";
    /*** 票据系统参数配置：isBudget 是否挂预算 */
    public static final String AB_CONFIG_ISBUDGET = "isBudget";
    /*** 票据系统参数配置：emitItem 开票挂预算科目 */
    public static final String AB_CONFIG_EMITITEM = "emitItem";
    /*** 票据系统参数配置：recItem 收票挂预算科目 */
    public static final String AB_CONFIG_RECITEM = "recItem";
    /*** 票据系统参数配置：endorseItem 背书挂预算科目 */
    public static final String AB_CONFIG_ENDORSEITEM = "endorseItem";
    /*** 票据系统参数配置：discountItem 贴现挂预算科目 */
    public static final String AB_CONFIG_DISCOUNTITEM = "discountItem";
    /*** 票据系统参数配置：collectionItem 托收挂预算科目 */
    public static final String AB_CONFIG_COLLECTIONITEM = "collectionItem";
    /*** 票据系统参数配置：isAccbook是否挂记账 */
    public static final String AB_CONFIG_ISACCBOOK = "isAccbook";
    /*** 票据系统参数配置：isAutoAccbook是否自动记账 */
    public static final String AB_CONFIG_ISAUTOACCBOOK = "isAutoAccbook";
    /*** 票据系统参数配置：isCredit是否挂授信 */
    public static final String AB_CONFIG_ISCREDIT = "isCredit";
    /*** 票据系统参数配置：isOpenRat是否启用授信敞口(前置：挂接时候可操作)*/
    public static final String AB_CONFIG_ISOPENRAT = "isOpenRat";
    /*** 票据系统参数配置: isOpenCpRat是否启用内部授信*/
    public static final String AB_CONFIG_ISOPENCPRAT = "isOpenCpRat";
    /*** 票据系统参数配置：isBear是否启用担保*/
    public static final String AB_CONFIG_ISBEAR = "isBear";
    /*** 票据系统参数配置：tradeBillCredit 商票挂授信模式(0企业授信1银行授信) */
    public static final String AB_CONFIG_TRADEBILLCREDIT = "tradeBillCredit";
    /*** 票据系统参数配置：tradeBillCredit 商票贴现是否挂银行授信模式(0否1是) */
    public static final String AB_CONFIG_ISDISCOUNTCREDIT = "isDisCountCredit";
    /*** 票据系统参数配置：isEAutoAccept电票自动提示承兑 */
    public static final String AB_CONFIG_ISEAUTOACCEPT = "isEAutoAccept";
    /*** 票据系统参数配置：isEAutoInvoice电票自动提示收票 */
    public static final String AB_CONFIG_ISEAUTOINVOICE = "isEAutoInvoice";
    /*** 票据系统参数配置：isEAutoPay电票自动提示付款 */
    public static final String AB_CONFIG_ISEAUTOPAY = "isEAutoPay";
    /*** 票据系统参数配置：isPoolEndorse票据池背书 */
    public static final String AB_CONFIG_ISPOOLENDORSE = "isPoolEndorse";
    /*** 票据系统参数配置：isPoolEmitBill 票据池开票*/
    public static final String AB_CONFIG_ISPOOLEMITBILL = "isPoolEmitBill";
    /*** 票据系统参数配置：isEAutoPayDayNum电票自动提示付款提前天数 默认10天 */
    public static final String AB_CONFIG_ISEAUTOPAYDAYNUM = "isEAutoPayDayNum";
    /*** 票据系统参数配置：isEOnlinePay电票是否启用线上清算 */
    public static final String AB_CONFIG_ISEONLINEPAY = "isEOnlinePay";
    /*** 票据系统参数配置：isEAutoInstruct 电票自动发送指令 */
    public static final String AB_CONFIG_ISEAUTOINSTRUCT = "isEAutoInstruct";
    /*** 票据系统参数配置：eBankOverDayNum电票银票逾期天数 默认10天 */
    public static final String AB_CONFIG_EBANKOVERDAYNUM = "eBankOverDayNum";
    /*** 票据系统参数配置：eTradeOverDayNum电票商票逾期天数 默认12天 */
    public static final String AB_CONFIG_ETRADEOVERDAYNUM = "eTradeOverDayNum";
    /*** 票据系统参数配置：emitWarnDayNum自开票据到期提醒天数 默认10天 */
    public static final String AB_CONFIG_EMITWARNDAYNUM = "emitWarnDayNum";
    /*** 票据系统参数配置：reserveWarnDayNum库存票据到期提醒天数 默认10天 */
    public static final String AB_CONFIG_RESERVEWARNDAYNUM = "reserveWarnDayNum";
    /*** 票据系统参数配置：isBail 是否挂保证金 */
    public static final String AB_CONFIG_ISBAIL = "isBail";
    /*** 票据系统参数配置：bailBankControlType 保证金银行控制类型 */
    public static final String AB_CONFIG_BAILBANKCONTROLTYPE = "bailBankControlType";
    /*** 票据系统参数配置：maxBillMoney 纸票票据金额最大额度 */
    public static final String AB_CONFIG_MAXBILLMONEY = "maxBillMoney";
    /*** 票据系统参数配置：billBackDesc 票据背面信息是否启用降序 0为顺序1为降序 */
    public static final String AB_CONFIG_BILLBACKDESC = "billBackDesc";
    /*** 票据系统参数配置：isDiscountConfirm 是否贴现到账确认 0否1是 默认是 */
    public static final String AB_CONFIG_ISDISCOUNTCONFIRM = "isDiscountConfirm";
    /*** 票据系统参数配置：isCollectionApplyConfirm 是否贴现到账确认 0否1是 默认是 */
    public static final String AB_CONFIG_ISCOLLECTIONAPPLYCONFIRM = "isCollectionApplyConfirm";
    /*** 票据系统参数配置：isEAutoPaySttlmmk 电票自动提示付款清算方式 (银票)*/
    public static final String AB_CONFIG_ISEAUTOPAYSTTLMMK = "isEAutoPaySttlmmk";
    /*** 票据系统参数配置：isEAutoPaySttlmmk 电票自动提示付款清算方式 (商票)*/
    public static final String AB_CONFIG_ISEAUTOPAYSTTLMMK_MERCH = "isEAutoPaySttlmmkMerch";
    /*** 票据系统参数配置：tradeBillCredit 第三方票据贴现是否挂银行授信模式(0否1是) */
    public static final String AB_CONFIG_ISDISTHIRDBILLCREDIT = "isDisThirdBillCredit";
    /*** 票据系统参数配置：isRepeatEndorse 是否允许重复背书*/
    public static final String AB_CONFIG_ISREPEATENDORSE = "isRepeatEndorse";
    /*** 票据系统参数配置：isCounterpartEndorse 是否支持同名跨行背书*/
    public static final String AB_CONFIG_ISCOUNTERPARTENDORSE = "isCounterpartEndorse";
    /*** 票据系统参数配置：isNotDiscountApply 不启用贴现申请功能直接贴现办理 */
    public static final String AB_CONFIG_ISNOTDISCOUNTAPPLY = "isNotDiscountApply";
    /*** 票据系统参数配置：isElectricBill 是否按票据账户控制 */
    public static final String AB_CONFIG_ISELECTRICBILL = "isElectricBill";
    /*** 票据系统参数配置：isCashingOnlinePayee 兑付申请线上兑付收款人 */
    public static final String AB_CONFIG_ISCASHINGONLINEPAYEE = "isCashingOnlinePayee";
    /**
     * 单位统计表业务类型
     **/
    public static final String AB_BILL_CORPTJ_BUSTYPE = "AB_BILL_TJ_BUSTYPE";
    /**
     * 持票账号为空时默认值
     **/
    public static final String AB_BILL_ACCNO_DEFAULT = null;
    /**
     * 持票账号为空时显示默认值
     **/
    public static final String AB_BILL_ACCNO_SHOW_DEFAULT = " ";
    /**
     * 定时器生成当日余额获取单位和账号数据往前推移多少天 默认半年180天
     **/
    public static final Integer AB_BILLBAL_LASTDAY = 180;
    /**
     * 票据锁定原因: 应收付款
     */
    public static final String AB_ACCEBILL_RECEIVABLE = "应收付款";
    public static final String AB_ACCEBILL_LOCK = "付款锁定";
    /**
     * 托收说明: 自动托收申请
     */
    public static final String AB_ACCEBILL_STTLMMK = "自动托收申请";
    /**
     * 应付开票数据来源:10付款
     */
    public static final String AB_ACCEBILL_SOURCE10 = "10";
    /**
     * 应付开票数据来源:11录入
     */
    public static final String AB_ACCEBILL_SOURCE11 = "11";
//	/** 开票数据来源:12质押 */
//	public static final String AB_ACCEBILL_SOURCE12 = "12";
//	/** 开票数据来源:14贴现 */
//	public static final String AB_ACCEBILL_SOURCE14 = "14";
    /**
     * 应付开票数据来源:15 ERP
     */
    public static final String AB_ACCEBILL_SOURCE15 = "15";
    /*** 应付开票电票票据最大金额 */
    public static final Double AB_ACCBILL_MONEY_BILLFLAG11 = 1000000000.00;
    /*** 应付开票纸票票据最大金额 */
    public static final Double AB_ACCBILL_MONEY_BILLFLAG10 = 1000000.00;
    /*** 票据金额最小值为0.01 */
    public static final Double AB_ACCBILL_MINMONEY = 0.01;
    /*** 应收票据来源：10 收票 */
    public static final String AB_BILL_SOURCE_REC = "10";
    /*** 应收票据来源：11 背书 */
    public static final String AB_BILL_SOURCE_ENDORSE = "11";
    /*** 应收票据来源：12 质押 */
    public static final String AB_BILL_SOURCE_PLEDGE = "12";
    /*** 应收票据来源：13 签收 */
    public static final String AB_BILL_SOURCE_SIGN = "13";
    /*** 应收票据来源：14 贴现 */
    public static final String AB_BILL_SOURCE_DISCOUNT = "14";
    /**
     * 应收开票数据来源:15归集
     */
    public static final String AB_BILL_SOURCE15 = "15";
    /**
     * 应收开票数据来源:16上划
     */
    public static final String AB_BILL_SOURCE16 = "16";
    /**
     * 应收开票数据来源:17下拨
     */
    public static final String AB_BILL_SOURCE17 = "17";
    /**
     * 兑付方式: 0线下兑付
     */
    public static final String AB_BILL_PAYEMITSTTLMMK0 = "0";
    /**
     * 兑付方式: 1线上兑付
     */
    public static final String AB_BILL_PAYEMITSTTLMMK1 = "1";
    // 票据操作类型 0应付票据，1应收票据
    /*** 票据操作类型 0应付票据 */
    public static final String AB_VOUCHERINFO_OP_TYPE0 = "0";
    /*** 票据操作类型 1应收票据 */
    public static final String AB_VOUCHERINFO_OP_TYPE1 = "1";
    /*** 票据背面信息 */
    public static final String AB_BILLINFO_BACK = "back";
    /*** 票据正面信息 */
    public static final String AB_BILLINFO_FRONT = "front";
    /*** 票据业务处理状态：0暂存 */
    public static final Integer AB_BILL_BUS_STATUS0 = 0;
    /*** 票据业务处理状态：11 审批中 */
    public static final Integer AB_BILL_BUS_STATUS11 = 11;
    /*** 票据业务处理状态：94 待记账 */
    public static final Integer AB_BILL_BUS_STATUS94 = 94;
    /*** 票据业务处理状态：95 业务完成 */
    public static final Integer AB_BILL_BUS_STATUS95 = 95;
    /*** 票据业务处理状态：-1打回 */
    public static final Integer AB_BILL_BUS_STATUS_1 = -1;
    /*** 票据业务处理状态：-2删除 */
    public static final Integer AB_BILL_BUS_STATUS_2 = -2;
    /*** 票据业务处理状态：-3撤回 -- 兑付跟踪专用 */
    public static final Integer AB_BILL_BUS_STATUS_3 = -3;
    /*** 票据业务处理状态：100 确认已付款 -- 兑付跟踪专用 */
    public static final Integer AB_BILL_BUS_STATUS100 = 100;
    /*** 余额明细借贷标志 0借 */
    public static final String AB_BILL_CD_SIGN_0 = "0";
    /*** 余额明细借贷标志 1贷 */
    public static final String AB_BILL_CD_SIGN_1 = "1";
    /*** 余额明细借贷标志 2冻结 */
    public static final String AB_BILL_CD_SIGN_2 = "2";
    /*** 有效标志：0否/者处理中 */
    public static final String AB_BILL_VALID_SIGN_0 = "0";
    /*** 有效标志：1是 */
    public static final String AB_BILL_VALID_SIGN_1 = "1";
    /*** 有效标志：2已删除/业务已结束 */
    public static final String AB_BILL_VALID_SIGN_2 = "2";
    /**
     * 开票方式类型数据字典代码
     **/
    public static final String AB_EMIT_TYPE_DICT = "ACCEBILL_EMIT_TYPE";
    /*** 开票方式类型 票据类型：10 质押开票 */
    public static final String AB_EMIT_TYPE_10 = "10";
    /*** 开票方式类型 票据类型：20 保证金开票 */
    public static final String AB_EMIT_TYPE_20 = "20";
    /*** 开票方式类型 票据类型：30 信用开票 */
    public static final String AB_EMIT_TYPE_30 = "30";
    /**
     * 票据类型数据字典代码
     **/
    public static final String AB_BILL_TYPE_DICT = "AB_BILL_TYPE";
    /*** 票据类型 票据类型：10 银票 */
    public static final String AB_BILL_TYPE_BANK = "10";
    /*** 票据类型 票据类型：11 商票 */
    public static final String AB_BILL_TYPE_MERCH = "11";
    /*** 票据转让标记 EM00可再转让 */
    public static final String AB_BANEM_Y = "EM00";
    /*** 票据转让标记 EM01不得转让 */
    public static final String AB_BANEM_N = "EM01";
    /*** 处理标记 10正在处理 */
    public static final Integer AB_BOX_STATUS_N = 10;
    /*** 处理标记 11处理完成 */
    public static final Integer AB_BOX_STATUS_Y = 11;
    /*** 流通介质：10 纸票 */
    public static final String AB_BILL_FLAG_PAPER = "10";
    /*** 流通介质：11 电票 */
    public static final String AB_BILL_FLAG_ELECT = "11";
    /*** 内部业务标志：1 是 */
    public static final String AB_INSIDE_SING_Y = "1";
    /*** 内部业务标志：0 否 */
    public static final String AB_INSIDE_SING_N = "0";
    /*** 内部背书线上背给线下自动登记标志：1-已登记 */
    public static final String INSIDE_ENDORSE_AUTO_REG_SIGN_Y = "1";
    /*** 内部背书线上背给线下自动登记标志：0-未登记 */
    public static final String INSIDE_ENDORSE_AUTO_REG_SIGN_N = "0";
    /*** 挂授信标志：0 否 */
    public static final String AB_RATMODEL_SING_N = "0";
    /*** 商票贴现挂银行授信类型：1 承兑人*/
    public static final String AB_RATMODEL_TYPE_ACCEPTOR = "1";
    /*** 所属库存:10单位库存 */
    public static final String AB_ENTER_STOCK_CORP = "10";
    /*** 所属库存:11中心代管 */
    public static final String AB_ENTER_STOCK_NET = "11";
    /*** 所属库存:12银行托管 */
    public static final String AB_ENTER_STOCK_BANK = "12";
    /*** 贴现种类：RM00 买断式 */
    public static final String AB_DISCOUNT_RPDMK_BUYOUT = "RM00";
    /*** 贴现种类：RM01 回购式 */
    public static final String AB_DISCOUNT_RPDMK_BUYBACK = "RM01";
    /*** 线上清算标记：SM00 开启线上清算(线上清算) */
    public static final String AB_STTLMMK_Y = "SM00";
    /*** 线上清算标记：SM01 关闭线上清算(线下清算) */
    public static final String AB_STTLMMK_N = "SM01";
    /*** 划款天数：0 本地0天*/
    public static final String AB_PAYDATE_HERE = "0";
    /*** 划款天数：3 异地3天 */
    public static final String AB_PAYDATE_THERE = "3";
    /*** 是否买方付息：0 否*/
    public static final String AB_BUYERINTERESTSING_N = "0";
    /*** 是否买方付息：1 是 */
    public static final String AB_BUYERINTERESTSING_Y = "1";
    /*** 是否隐藏: 0 否*/
    public static final String AB_IS_HIDE_N = "0";
    /*** 是否隐藏: 1 是*/
    public static final String AB_IS_HIDE_Y = "1";
    /*** 是否已付款: 1 是*/
    public static final String AB_IS_PAY_Y = "1";
    /*** 是否已付款: 0 否 */
    public static final String AB_IS_PAY_N = "0";
    /*** 创建人为空 1 */
    public static final String AB_BILL_ISCREATEBY_NULL = "1";
    /*** 追索种类：RT00 拒付追索；RT01 非拒付追索*/
    public static final String AB_RCRS_TP_DICT = "AB_RCRS_TP";
    /*** 追索种类：RT00 拒付追索 */
    public static final String AB_RCRS_TP_PROTESTY = "RT00";
    /*** 追索种类：RT01 非拒付追索 */
    public static final String AB_RCRS_TP_PROTESTN = "RT01";
    /*** 追索理由代码  */
    public static final String AB_RCRSRSNCD_DICT = "AB_RCRSRSNCD";
    /*** 追索理由代码：RC00 承兑人被依法宣告破产； */
    public static final String AB_RCRSRSNCD_ONE = "RT00";
    /*** 追索理由代码：RC01 承兑人因违法被责令终止活动（非拒付追索时填写） */
    public static final String AB_RCRSRSNCD_TWO = "RT01";
    /*** 统计方式：0 按月统计*/
    public static final String AB_STATISTICAL_TYPE0 = "0";
    /*** 统计方式：1 按日统计*/
    public static final String AB_STATISTICAL_TYPE1 = "1";
    /*** 统计维度：0 按贴入人统计*/
    public static final String AB_STATISTICAL_DIMENSION_0 = "0";
    /*** 统计维度：1 按持票单位统计*/
    public static final String AB_STATISTICAL_DIMENSION_1 = "1";
    /*** 中间表类型 1 票据签收消息提醒*/
    public static final String BILL_MIDDLE_TYPE_1 = "1";
    /*** 中间表类型 2 票据到期托收提醒*/
    public static final String BILL_MIDDLE_TYPE_2 = "2";
    /*** 中间表类型 3 到期拒付消息提醒*/
    public static final String BILL_MIDDLE_TYPE_3 = "3";
    /*** 中间表类型 4 收到待清偿数据提醒*/
    public static final String BILL_MIDDLE_TYPE_4 = "4";
    /*** 中间表类型 5 票据到期未兑付提醒*/
    public static final String BILL_MIDDLE_TYPE_5 = "5";
    /*** 中间表类型 6 线上电票提示收票完成补录提醒*/
    public static final String BILL_MIDDLE_TYPE_6 = "6";
    /*** 中间表类型 7 电票指令失败消息提醒*/
    public static final String BILL_MIDDLE_TYPE_7 = "7";
    /*** 中间表类型 002 自动提示承兑中间表标记*/
    public static final String BILL_MIDDLE_TYPE_002 = "002";
    /*** 中间表类型 003 自动提示收票中间表标记*/
    public static final String BILL_MIDDLE_TYPE_003 = "003";
    /*** 中间表类型 020 自动提示付款中间表标记*/
    public static final String BILL_MIDDLE_TYPE_020 = "020";
    /*** 中间表类型 010 背书失败推送付款写入中间表标记*/
    public static final String BILL_MIDDLE_TYPE_010PAYERROR = "010PE";
    /*** 中间表类型 004PBS 未用退票指令成功推送付款写入中间表标记*/
    public static final String BILL_MIDDLE_TYPE_004PAY_BACKSUCCESS = "004PBS";
    // 业务类型：002承兑003收票010背书011贴现012贴现赎回013转贴现014转贴现赎回015再贴现016再贴现赎回017保证018质押019解除质押020提示付款021逾期提示付款022追索025央行卖票
    /*** 业务类型：002承兑 */
    public static final String AB_BUSTYPE_ACCEPT = "002";
    /*** 业务类型：003收票 */
    public static final String AB_BUSTYPE_RECBILL = "003";
    /*** 业务类型：010背书 */
    public static final String AB_BUSTYPE_RECITE = "010";
    /*** 业务类型：011贴现 */
    public static final String AB_BUSTYPE_DISCOUNT = "011";
    /*** 业务类型：012贴现赎回 */
    public static final String AB_BUSTYPE_DISCOUNT_REDEM = "012";
    /*** 业务类型：013转贴现 */
    public static final String AB_BUSTYPE_DISCOUNT_TRANF = "013";
    /*** 业务类型：014转贴现赎回 */
    public static final String AB_BUSTYPE_DISCOUNT_TRANF_REDEM = "014";
    /*** 业务类型：015再贴现 */
    public static final String AB_BUSTYPE_DISCOUNT_AGAIN = "015";
    /*** 业务类型：016再贴现赎回 */
    public static final String AB_BUSTYPE_DISCOUNT_AGAIN_REDEM = "016";
    /*** 业务类型：017保证 */
    public static final String AB_BUSTYPE_GUARGN = "017";
    /*** 业务类型：018质押 */
    public static final String AB_BUSTYPE_PLEDGE = "018";
    /*** 业务类型：019解除质押 */
    public static final String AB_BUSTYPE_PLEDGE_RELIEVE = "019";
    /*** 业务类型：020提示付款 */
    public static final String AB_BUSTYPE_HINT_PAYER = "020";
    /*** 业务类型：021逾期提示付款 */
    public static final String AB_BUSTYPE_HINT_PAYER_OVERDUE = "021";
    /*** 业务类型：025央行卖票 */
    public static final String AB_BUSTYPE_CB_SELLBILL = "025";
    // 报文消息类型：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿
    /*** 报文消息类型：002承兑 */
    public static final String AB_MSGTYPE_ACCEPT = "002";
    /*** 报文消息类型：003收票 */
    public static final String AB_MSGTYPE_RECBILL = "003";
    /*** 报文消息类型：010背书 */
    public static final String AB_MSGTYPE_RECITE = "010";
    /*** 报文消息类型：011贴现 */
    public static final String AB_MSGTYPE_DISCOUNT = "011";
    /*** 报文消息类型：012贴现赎回 */
    public static final String AB_MSGTYPE_DISCOUNT_REDEM = "012";
    /*** 报文消息类型：017提示保证 */
    public static final String AB_MSGTYPE_GUARGN = "017";
    /*** 报文消息类型：018质押 */
    public static final String AB_MSGTYPE_PLEDGE = "018";
    /*** 报文消息类型：019解除质押 */
    public static final String AB_MSGTYPE_PLEDGE_RELIEVE = "019";
    /*** 报文消息类型：020提示付款 */
    public static final String AB_MSGTYPE_HINT_PAYER = "020";
    /*** 报文消息类型：021逾期提示付款 */
    public static final String AB_MSGTYPE_HINT_PAYER_OVERDUE = "021";
    /*** 报文消息类型：022追索通知 */
    public static final String AB_MSGTYPE_SEARCHFOR = "022";
    /*** 报文消息类型：023追索同意清偿 */
    public static final String AB_MSGTYPE_SEARCHFOR_PAYOFF = "023";
    /*** 回复标记：SU00:同意签收 */
    public static final String AB_SGNUPMK_Y = "SU00";
    /*** 回复标记：SU01：拒收 */
    public static final String AB_SGNUPMK_N = "SU01";
    /*** 到期无条件支付委托:提示承兑专用 CC00含委托/承诺兑付 */
    public static final String UCONDLCONSGNMTMRK = "CC00";
    // 参与者类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
    /*** 参与者类别 RC00接入行 */
    public static final String AB_BILL_ROLE_BANK = "RC00";
    /*** 参与者类别 RC01企业 */
    public static final String AB_BILL_ROLE_CORP = "RC01";
    /*** 参与者类别 RC02人民银行 */
    public static final String AB_BILL_ROLE_CB = "RC02";
    /*** 参与者类别 RC03被代理行 */
    public static final String AB_BILL_ROLE_AGENCY_BANK = "RC03";
    /*** 参与者类别 RC04被代理财务公司 */
    public static final String AB_BILL_ROLE_AGENCY_FC = "RC04";
    /*** 参与者类别 RC05接入财务公司 */
    public static final String AB_BILL_ROLE_FC = "RC05";
    // 签收表处理状态：18待签收19签成功-2打回-3撤回
    /*** 签收表处理状态：18待签收 */
    public static final int AB_BILL_RECEIVEBIZ_STATUS_PENDING = 18;
    /*** 签收表处理状态：19签成功 */
    public static final int AB_BILL_RECEIVEBIZ_STATUS_OK = 19;
    /*** 签收表处理状态： -2打回 */
    public static final int AB_BILL_RECEIVEBIZ_STATUS_REPULSE = -2;
    /*** 签收表处理状态：-3撤回 */
    public static final int AB_BILL_RECEIVEBIZ_STATUS_RECALL = -3;
    // 是否发送指令标志：0否； 1是 ；
    /*** 是否发送指令标志：1是 */
    public static final String AB_BILL_SEND_SIGN_Y = "1";
    /*** 是否发送指令标志：0否； */
    public static final String AB_BILL_SEND_SIGN_N = "0";
//
    // 电票发送回应标志：E  发送MQ队列失败，F 指令已提取 S 发送MQ队列成功 W MQ队列已消费 1 等待查询结果 2 交易失败 5 批次提交成功 3 准备发送 ；
    /*** E  发送MQ队列失败 */
    public static final String AB_BILL_RETURN_CODE_E = "E";
    /*** F 指令已提取  */
    public static final String AB_BILL_RETURN_CODE_F = "F";
    /*** S 发送MQ队列成功 */
    public static final String AB_BILL_RETURN_CODE_S = "S";
    /*** W MQ队列已消费 */
    public static final String AB_BILL_RETURN_CODE_W = "W";
    /*** 1 等待查询结果 */
    public static final String AB_BILL_RETURN_CODE_1 = "1";
    /*** 2 交易失败 */
    public static final String AB_BILL_RETURN_CODE_2 = "2";
    /*** 5 批次提交成功 */
    public static final String AB_BILL_RETURN_CODE_5 = "5";
    /*** 3 准备发送 */
    public static final String AB_BILL_RETURN_CODE_3 = "3";
    // 业务单据挂授信标志：0否； 1是 ；
    /*** 业务单据据挂授信标志：1是 */
    public static final String AB_BILL_RATMODEL_Y = "1";
    /*** 业务单据据挂授信标志：0否； */
    public static final String AB_BILL_RATMODEL_N = "0";
    // 保证类型:01开票保证，02承兑保证；03背书保证；
    /*** 保证类型:01开票保证 */
    public static final String AB_BAIL_TYPE_ADD = "01";
    /*** 保证类型:02承兑保证； */
    public static final String AB_BAIL_TYPE_ACCEPT = "02";
    /*** 保证类型:03背书保证； */
    public static final String AB_BAIL_TYPE_RECITE = "03";
    // 流水表操作状态：15待发送；16发送中；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
    /*** 流水表操作状态：15待发送 */
    public static final String AB_BILL_OPT_STATUS15 = "15";
    /*** 流水表操作状态：16发送中 */
    public static final String AB_BILL_OPT_STATUS16 = "16";
    /*** 流水表操作状态：17已发送待回应 */
    public static final String AB_BILL_OPT_STATUS17 = "17";
    /*** 流水表操作状态：18已回应待签收 */
    public static final String AB_BILL_OPT_STATUS18 = "18";
    /*** 流水表操作状态： 19已签收成功业务完成 */
    public static final String AB_BILL_OPT_STATUS19 = "19";
    /*** 流水表操作状态：-1发送失败 */
    public static final String AB_BILL_OPT_STATUS_1 = "-1";
    /*** 流水表操作状态：-2打回 */
    public static final String AB_BILL_OPT_STATUS_2 = "-2";
    /*** 流水表操作状态： -3撤回 */
    public static final String AB_BILL_OPT_STATUS_3 = "-3";
    /*** 流水表操作状态： -4拒签 */
    public static final String AB_BILL_OPT_STATUS_4 = "-4";
    // 人行定义的票据状态
    /**
     * 010004 : 出票已登记
     */
    public static final String AB_BILL_STATUS_010004 = "010004";
    /**
     * 020001 : 提示承兑待签收
     */
    public static final String AB_BILL_STATUS_020001 = "020001";
    /**
     * 020006 : 提示承兑已签收
     */
    public static final String AB_BILL_STATUS_020006 = "020006";
    /**
     * 030001 : 提示收票待签收
     */
    public static final String AB_BILL_STATUS_030001 = "030001";
    /**
     * 030006 : 提示收票已签收
     */
    public static final String AB_BILL_STATUS_030006 = "030006";
    /**
     * 030007 : 提示收票拒绝签收
     */
    public static final String AB_BILL_STATUS_030007 = "030007";
    /**
     * 000002 : 结束已作废
     */
    public static final String AB_BILL_STATUS_000002 = "000002";
    /**
     * 100001 : 背书待签收
     */
    public static final String AB_BILL_STATUS_100001 = "100001";
    /**
     * 100006 : 背书已签收
     */
    public static final String AB_BILL_STATUS_100006 = "100006";
    /**
     * 110101 : 买断式贴现待签收
     */
    public static final String AB_BILL_STATUS_110101 = "110101";
    /**
     * 110103 : 买断式贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_110103 = "110103";
    /**
     * 110105 : 买断式贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_110105 = "110105";
    /**
     * 110106 : 买断式贴现已签收
     */
    public static final String AB_BILL_STATUS_110106 = "110106";
    /**
     * 110201 : 回购式贴现待签收
     */
    public static final String AB_BILL_STATUS_110201 = "110201";
    /**
     * 110203 : 回购式贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_110203 = "110203";
    /**
     * 110205 : 回购式贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_110205 = "110205";
    /**
     * 110206 : 回购式贴现已签收
     */
    public static final String AB_BILL_STATUS_110206 = "110206";
    /**
     * 110216 : 回购式贴现已至赎回开放日
     */
    public static final String AB_BILL_STATUS_110216 = "110216";
    /**
     * 110218 : 回购式贴现已逾赎回截止日
     */
    public static final String AB_BILL_STATUS_110218 = "110218";
    /**
     * 120001 : 回购式贴现赎回待签收
     */
    public static final String AB_BILL_STATUS_120001 = "120001";
    /**
     * 120003 : 回购式贴现赎回已签收待清算
     */
    public static final String AB_BILL_STATUS_120003 = "120003";
    /**
     * 120005 : 回购式贴现赎回已签收已排队
     */
    public static final String AB_BILL_STATUS_120005 = "120005";
    /**
     * 120006 : 回购式贴现赎回已签收
     */
    public static final String AB_BILL_STATUS_120006 = "120006";
    /**
     * 130101 : 买断式转贴现待签收
     */
    public static final String AB_BILL_STATUS_130101 = "130101";
    /**
     * 130103 : 买断式转贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_130103 = "130103";
    /**
     * 130105 : 买断式转贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_130105 = "130105";
    /**
     * 130106 : 买断式转贴现已签收
     */
    public static final String AB_BILL_STATUS_130106 = "130106";
    /**
     * 130201 : 回购式转贴现待签收
     */
    public static final String AB_BILL_STATUS_130201 = "130201";
    /**
     * 130203 : 回购式转贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_130203 = "130203";
    /**
     * 130205 : 回购式转贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_130205 = "130205";
    /**
     * 130206 : 回购式转贴现已签收
     */
    public static final String AB_BILL_STATUS_130206 = "130206";
    /**
     * 130216 : 回购式转贴现已至赎回开放日
     */
    public static final String AB_BILL_STATUS_130216 = "130216";
    /**
     * 130218 : 回购式转贴现已逾赎回截止日
     */
    public static final String AB_BILL_STATUS_130218 = "130218";
    /**
     * 140001 : 回购式转贴现赎回待签收
     */
    public static final String AB_BILL_STATUS_140001 = "140001";
    /**
     * 140003 : 回购式转贴现赎回已签收待清算
     */
    public static final String AB_BILL_STATUS_140003 = "140003";
    /**
     * 140005 : 回购式转贴现赎回已签收已排队
     */
    public static final String AB_BILL_STATUS_140005 = "140005";
    /**
     * 140006 : 回购式转贴现赎回已签收
     */
    public static final String AB_BILL_STATUS_140006 = "140006";
    /**
     * 150101 : 买断式再贴现待签收
     */
    public static final String AB_BILL_STATUS_150101 = "150101";
    /**
     * 150103 : 买断式再贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_150103 = "150103";
    /**
     * 150105 : 买断式再贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_150105 = "150105";
    /**
     * 150106 : 买断式再贴现已签收
     */
    public static final String AB_BILL_STATUS_150106 = "150106";
    /**
     * 150201 : 回购式再贴现待签收
     */
    public static final String AB_BILL_STATUS_150201 = "150201";
    /**
     * 150203 : 回购式再贴现已签收待清算
     */
    public static final String AB_BILL_STATUS_150203 = "150203";
    /**
     * 150205 : 回购式再贴现已签收已排队
     */
    public static final String AB_BILL_STATUS_150205 = "150205";
    /**
     * 150206 : 回购式再贴现已签收
     */
    public static final String AB_BILL_STATUS_150206 = "150206";
    /**
     * 150216 : 回购式再贴现已至赎回开放日
     */
    public static final String AB_BILL_STATUS_150216 = "150216";
    /**
     * 150218 : 回购式再贴现已逾赎回截止日
     */
    public static final String AB_BILL_STATUS_150218 = "150218";
    /**
     * 160001 : 回购式再贴现赎回待签收
     */
    public static final String AB_BILL_STATUS_160001 = "160001";
    /**
     * 160003 : 回购式再贴现赎回已签收待清算
     */
    public static final String AB_BILL_STATUS_160003 = "160003";
    /**
     * 160005 : 回购式再贴现赎回已签收已排队
     */
    public static final String AB_BILL_STATUS_160005 = "160005";
    /**
     * 160006 : 回购式再贴现赎回已签收
     */
    public static final String AB_BILL_STATUS_160006 = "160006";
    /**
     * 180001 : 质押待签收
     */
    public static final String AB_BILL_STATUS_180001 = "180001";
    /**
     * 180006 : 质押已签收
     */
    public static final String AB_BILL_STATUS_180006 = "180006";
    /**
     * 180020 : 质押已至票据到期日
     */
    public static final String AB_BILL_STATUS_180020 = "180020";
    /**
     * 190001 : 质押解除待签收
     */
    public static final String AB_BILL_STATUS_190001 = "190001";
    /**
     * 190006 : 质押解除已签收
     */
    public static final String AB_BILL_STATUS_190006 = "190006";
    /**
     * 170001 : 保证待签收
     */
    public static final String AB_BILL_STATUS_170001 = "170001";
    /**
     * 170002 : 保证已签收
     */
    public static final String AB_BILL_STATUS_170002 = "170002";
    /**
     * 200001 : 提示付款待签收
     */
    public static final String AB_BILL_STATUS_200001 = "200001";
    /**
     * 200003 : 提示付款已签收待清算
     */
    public static final String AB_BILL_STATUS_200003 = "200003";
    /**
     * 200005 : 提示付款已签收已排队
     */
    public static final String AB_BILL_STATUS_200005 = "200005";
    /**
     * 000000 : 结束已结清
     */
    public static final String AB_BILL_STATUS_000000 = "000000";
    /**
     * 200312 : 提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）
     */
    public static final String AB_BILL_STATUS_200312 = "200312";
    /**
     * 200412 : 提示付款已拒付（可拒付追索，可以追所有人）
     */
    public static final String AB_BILL_STATUS_200412 = "200412";
    /**
     * 200512 : 提示付款已拒付（不可进行拒付追索）
     */
    public static final String AB_BILL_STATUS_200512 = "200512";
    /**
     * 210001 : 逾期提示付款待签收
     */
    public static final String AB_BILL_STATUS_210001 = "210001";
    /**
     * 210003 : 逾期提示付款已签收待清算
     */
    public static final String AB_BILL_STATUS_210003 = "210003";
    /**
     * 210005 : 逾期提示付款已签收已排队
     */
    public static final String AB_BILL_STATUS_210005 = "210005";
    /**
     * 210312 : 逾期提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）
     */
    public static final String AB_BILL_STATUS_210312 = "210312";
    /**
     * 210412 : 逾期提示付款已拒付（可拒付追索，可以追所有人）
     */
    public static final String AB_BILL_STATUS_210412 = "210412";
    /**
     * 220607 : 拒付追索待清偿
     */
    public static final String AB_BILL_STATUS_220607 = "220607";
    /**
     * 230601 : 拒付追索同意清偿待签收
     */
    public static final String AB_BILL_STATUS_230601 = "230601";
    /**
     * 230606 : 拒付追索同意清偿已签收
     */
    public static final String AB_BILL_STATUS_230606 = "230606";
    /**
     * 220707 : 非拒付追索待清偿
     */
    public static final String AB_BILL_STATUS_220707 = "220707";
    /**
     * 220710 : 非拒付追索已撤销
     */
    public static final String AB_BILL_STATUS_220710 = "220710";
    /**
     * 230701 : 非拒付追索同意清偿待签收
     */
    public static final String AB_BILL_STATUS_230701 = "230701";
    /**
     * 230706 : 非拒付追索同意清偿已签收
     */
    public static final String AB_BILL_STATUS_230706 = "230706";
    /**
     * 250001 : 央行卖票待签收
     */
    public static final String AB_BILL_STATUS_250001 = "250001";
    /**
     * 250003 : 央行卖票已签收待清算
     */
    public static final String AB_BILL_STATUS_250003 = "250003";
    /**
     * 250005 : 央行卖票已签收已排队
     */
    public static final String AB_BILL_STATUS_250005 = "250005";
    /**
     * 250006 : 央行卖票已签收
     */
    public static final String AB_BILL_STATUS_250006 = "250006";
    /**
     * 000026 : 已逾票据权利失效日
     */
    public static final String AB_BILL_STATUS_000026 = "000026";
    // BRIEF_STATUS 流水业务状态信息
    /*** 流水业务状态 001 出票登记 */
    public static final String BRIEF_STATUS_001 = "001";
    /*** 流水业务状态 017 提示保证 */
    public static final String BRIEF_STATUS_017 = "017";
    /*** 流水业务状态 9017 提示保证签收 */
    public static final String BRIEF_STATUS_9017 = "9017";
    /*** 流水业务状态 002 提示承兑； */
    public static final String BRIEF_STATUS_002 = "002";
    /*** 流水业务状态 9002 提示承兑签收； */
    public static final String BRIEF_STATUS_9002 = "9002";
    /*** 流水业务状态 003 提示收票； */
    public static final String BRIEF_STATUS_003 = "003";
    /*** 流水业务状态 9003 提示收票签收； */
    public static final String BRIEF_STATUS_9003 = "9003";
    /*** 流水业务状态 6101 应付补录 */
    public static final String BRIEF_STATUS_6101 = "6101";
    /*** 流水业务状态 6120 应付兑付确认 */
    public static final String BRIEF_STATUS_6120 = "6120";
    /**
     * 票据到期提醒类型
     */
    public static final String BRIEF_STATUS_6120_1 = "6120_1";
    /**
     * 承兑账户资金余额不足承兑扣款消息提醒
     */
    public static final String BRIEF_STATUS_6120_2 = "6120_2";
    /*** 流水业务状态 6001 应收票据登记 */
    public static final String BRIEF_STATUS_6001 = "6001";
    /*** 流水业务状态 6005 应收移存 */
    public static final String BRIEF_STATUS_6005 = "6005";
    /*** 流水业务状态 6006 应收取票 */
    public static final String BRIEF_STATUS_6006 = "6006";
    /*** 流水业务状态 6201 票据上划 */
    public static final String BRIEF_STATUS_6201 = "6201";
    /*** 流水业务状态 6202 票据归集 */
    public static final String BRIEF_STATUS_6202 = "6202";
    /*** 流水业务状态 6203 票据申请下拨 */
    public static final String BRIEF_STATUS_6203 = "6203";
    /*** 流水业务状态 6204 票据下划 */
    public static final String BRIEF_STATUS_6204 = "6204";
    /*** 流水业务状态 010 背书申请 */
    public static final String BRIEF_STATUS_010 = "010";
    /*** 流水业务状态 9010 背书申请签收； */
    public static final String BRIEF_STATUS_9010 = "9010";
    /*** 流水业务状态 6011 应收贴现申请 */
    public static final String BRIEF_STATUS_6011 = "6011";
    /*** 流水业务状态 011 应收贴现办理 */
    public static final String BRIEF_STATUS_011 = "011";
    /*** 流水业务状态 6012 应收贴现到账 */
    public static final String BRIEF_STATUS_6012 = "6012";
    /*** 流水业务状态 9012 贴现赎回签收 */
    public static final String BRIEF_STATUS_9012 = "9012";
    /*** 流水业务状态 012 贴现赎回申请 */
    public static final String BRIEF_STATUS_012 = "012";
    /*** 流水业务状态 6212 贴现赎回收回 */
    public static final String BRIEF_STATUS_6212 = "6212";
    /*** 流水业务状态 018 应收质押申请 */
    public static final String BRIEF_STATUS_018 = "018";
    /**  ** 流水业务状态 8018	入池申请 */
    public static final String BRIEF_STATUS_8018 = "8018";
    /*** 流水业务状态 9018 质押申请签收； */
    public static final String BRIEF_STATUS_9018 = "9018";
    /*** 流水业务状态 019 应收质押解除 */
    public static final String BRIEF_STATUS_019 = "019";
    /*** 流水业务状态 9019 质押解除签收； */
    public static final String BRIEF_STATUS_9019 = "9019";
    /*** 流水业务状态 6019 应收质押收回 */
    public static final String BRIEF_STATUS_6019 = "6019";
    /*** 流水业务状态 020 应收托收申请 */
    public static final String BRIEF_STATUS_020 = "020";
    /*** 流水业务状态 9020 提示付款签收； */
    public static final String BRIEF_STATUS_9020 = "9020";
    /*** 流水业务状态 021 应收逾期托收申请 */
    public static final String BRIEF_STATUS_021 = "021";
    /*** 流水业务状态 9021 逾期付款签收； */
    public static final String BRIEF_STATUS_9021 = "9021";
    /*** 流水业务状态 6020 应收托收到账 */
    public static final String BRIEF_STATUS_6020 = "6020";
    /*** 流水业务状态 022 追索申请； */
    public static final String BRIEF_STATUS_022 = "022";
    /*** 流水业务状态 022 追索申请待签收； */
    public static final String BRIEF_STATUS_9022 = "9022";
    /*** 流水业务状态 023 追索同意清偿 */
    public static final String BRIEF_STATUS_023 = "023";
    /*** 流水业务状态 9023 追索同意清偿签收； */
    public static final String BRIEF_STATUS_9023 = "9023";
    /*** 流水业务状态 004 应付退票 */
    public static final String BRIEF_STATUS_004 = "004";
    /*** 流水业务状态 032 通用撤回 */
    public static final String BRIEF_STATUS_032 = "032";
    /*** 流水业务状态 6004 应收退票 */
    public static final String BRIEF_STATUS_6004 = "6004";
    /*** 流水业务状态 6007 应收票据锁定 */
    public static final String BRIEF_STATUS_6007 = "6007";
    /*** 流水业务状态 6008 应收票据解锁 */
    public static final String BRIEF_STATUS_6008 = "6008";
    /*** 流水业务状态 9004 票据删除 */
    public static final String BRIEF_STATUS_9004 = "9004";
    /**
     * 业务类型 4000 指令失败处理
     */
    public static final String BRIEF_STATUS_4000 = "4000";
    /**
     * 流水业务状态 013 背书请求
     */
    public static final String BRIEF_STATUS_013 = "013";
    /**
     * 流水业务状态 013 背书请求应答
     */
    public static final String BRIEF_STATUS_014 = "014";
    /**
     * 流水业务状态 9101 签收补录
     */
    public static final String BRIEF_STATUS_9101 = "9101";
    /**
     * 8001出票融资申请
     */
    public static final String AB_POOL_BRIEF_8001 = "8001";
    /**
     * 入池申请
     */
    public static final String AB_POOL_BRIEF_8018 = "8018";
    /**
     * 票据入池
     */
    public static final String AB_POOL_BRIEF_8818 = "8818";
    /**
     * 出池申请
     */
    public static final String AB_POOL_BRIEF_8019 = "8019";
    /**
     * 质押转托管
     */
    public static final String AB_POOL_BRIEF_8118 = "8118";
    /**
     * 托管转质押
     */
    public static final String AB_POOL_BRIEF_8218 = "8218";
    /**
     * 入池撤销
     */
    public static final String AB_POOL_BRIEF_8032 = "8032";
    /**
     * 操作-新增
     */
    public static final String BILL_OPERATE_ADD = "add";
    /**
     * 操作-保存
     */
    public static final String BILL_OPERATE_SAVE = "save";
    /**
     * 操作-提交
     */
    public static final String BILL_OPERATE_SUBMIT = "submit";
    /**
     * 操作-批量提交
     */
    public static final String BILL_OPERATE_BATCHSUBMIT = "batchSubmit";
    /**
     * 操作-编辑
     */
    public static final String BILL_OPERATE_EDIT = "edit";
    /**
     * 操作-修改
     */
    public static final String BILL_OPERATE_UPDATE = "update";
    /**
     * 操作-审批打回
     */
    public static final String BILL_OPERATE_BACK = "back";
    /**
     * 操作-业务结束
     */
    public static final String BILL_OPERATE_REPULSE = "repulse";
    /**
     * 操作-审批通过
     */
    public static final String BILL_OPERATE_APPOK = "appok";
    /**
     * 操作-删除
     */
    public static final String BILL_OPERATE_DELETE = "delete";
    /**
     * 操作-入库
     */
    public static final String BILL_OPERATE_STORAGE = "stotage";
    /**
     * 操作-补录
     */
    public static final String BILL_OPERATE_SUPPLY = "supply";
    /**
     * 操作-未用退票
     */
    public static final String BILL_OPERATE_OPTBACK = "optBack";
    /**
     * 操作-票据审批通过后指令失败打回操作
     */
    public static final String BILL_INSTRUCTION_OPTBACK = "instructionOptBack";
    /**
     * 操作-批量补录
     */
    public static final String BILL_OPERATE_BATCHSUPPLY = "batchSupply";
    /**
     * 操作-导入
     */
    public static final String BILL_OPERATE_IMPORTS = "imports";
    // 承兑汇票日志信息
    // 日志类型：-2删除-1打回01新增02修改04启用05停用06入库0210打印11重新打印20使用21出售22丢失23作废24退票25移库26签收
    /**
     * 承兑汇票日志-4 线上兑付撤回
     */
    public static final String ABLOG_LOGTYPE_WITHDRAW = "-4";
    /**
     * 承兑汇票日志-3批量删除
     */
    public static final String ABLOG_LOGTYPE_BATCHDELETE = "-3";
    /**
     * 承兑汇票日志-2删除
     */
    public static final String ABLOG_LOGTYPE_DELETE = "-2";
    /**
     * 承兑汇票日志-1审批打回
     */
    public static final String ABLOG_LOGTYPE_REPULSE = "-1";
    /**
     * 承兑汇票日志01新增
     */
    public static final String ABLOG_LOGTYPE_ADD = "01";
    /**
     * 承兑汇票日志02修改
     */
    public static final String ABLOG_LOGTYPE_UPDATE = "02";
    /**
     * 承兑汇票日志04启用
     */
    public static final String ABLOG_LOGTYPE_START = "04";
    /**
     * 承兑汇票日志05停用
     */
    public static final String ABLOG_LOGTYPE_STOP = "05";
    /**
     * 承兑汇票日志06入库
     */
    public static final String ABLOG_LOGTYPE_INPUT = "06";
    /**
     * 承兑汇票日志07复制
     */
    public static final String ABLOG_LOGTYPE_COPY = "07";
    /**
     * 承兑汇票日志10打印
     */
    public static final String ABLOG_LOGTYPE_PRINT = "10";
    /**
     * 承兑汇票日志11重新打印
     */
    public static final String ABLOG_LOGTYPE_REPRINT = "11";
    /**
     * 承兑汇票日志20使用
     */
    public static final String ABLOG_LOGTYPE_USE = "20";
    /**
     * 承兑汇票日志21出售
     */
    public static final String ABLOG_LOGTYPE_SELL = "21";
    /**
     * 承兑汇票日志22丢失
     */
    public static final String ABLOG_LOGTYPE_LOSE = "22";
    /**
     * 承兑汇票日志23作废
     */
    public static final String ABLOG_LOGTYPE_CANCELLATION = "23";
    /**
     * 承兑汇票日志24退票
     */
    public static final String ABLOG_LOGTYPE_BACKBILL = "24";
    /**
     * 承兑汇票日志25移库
     */
    public static final String ABLOG_LOGTYPE_TRANSFER = "25";
    /**
     * 承兑汇票日志26签收
     */
    public static final String ABLOG_LOGTYPE_SIGN = "26";
    /**
     * 承兑汇票日志27指令发送
     */
    public static final String ABLOG_LOGTYPE_INSSEND = "27";
    /**
     * 承兑汇票日志28指令打回
     */
    public static final String ABLOG_LOGTYPE_INSREPULSE = "28";
    /**
     * 承兑汇票日志29取票
     */
    public static final String ABLOG_LOGTYPE_FETCH = "29";
    /**
     * 承兑汇票日志30提交
     */
    public static final String ABLOG_LOGTYPE_SUBMIT = "30";
    /**
     * 承兑汇票日志31批量提交
     */
    public static final String ABLOG_LOGTYPE_BATCHSUBMIT = "31";
    /**
     * 承兑汇票日志32批量成功
     */
    public static final String ABLOG_LOGTYPE_BATCHSUCCE = "32";
	/** * 承兑汇票日志33质押 */
	public static final String ABLOG_LOGTYPE_PLEDGE = "33";
    /**
     * 承兑汇票日志34批量已回应待签收
     */
    public static final String ABLOG_LOGTYPE_BATCHSUCCESIGN = "34";
    /**
     * 承兑汇票日志35批量已发送待回应
     */
    public static final String ABLOG_LOGTYPE_BATCHSENDSUCCE = "35";
    /**
     * 承兑汇票日志36批量已失败
     */
    public static final String ABLOG_LOGTYPE_BATCHFAILURE = "36";
	/** * 承兑汇票日志37 解除质押 */
	public static final String ABLOG_LOGTYPE_PLEDGERELIEVE = "37";
	/** * 承兑汇票日志38 质押收回 */
	public static final String ABLOG_LOGTYPE_PLEDGECOLLECT = "38";
    /**
     * 承兑汇票日志39导入
     */
    public static final String ABLOG_LOGTYPE_IMPORT = "39";
    /**
     * 承兑汇票日志40 定时器
     */
    public static final String ABLOG_LOGTYPE_TASK = "40";
    /**
     * 承兑汇票日志41 自动生成
     */
    public static final String ABLOG_LOGTYPE_AUTO = "41";
    /**
     * 承兑汇票日志42 线上兑付确认
     */
    public static final String ABLOG_CONFRIME_PAY = "42";
    /**
     * 承兑汇票日志43 批量线上兑付确认
     */
    public static final String ABLOG_CONFRIM_BATCHPAY = "43";
    /**
     * 承兑汇票日志95审批通过
     */
    public static final String ABLOG_LOGTYPE_APP = "95";
    /**
     * 承兑汇票日志100记账完成
     */
    public static final String ABLOG_LOGTYPE_ACCBOOK_APP = "100";
    /**
     * 承兑汇票日志-5记账打回
     */
    public static final String ABLOG_LOGTYPE_ACCBOOK_REPULSE = "-5";
    /**
     * 商票是挂贴现挂银行授信日志
     */
    public static final String ABLOG_LOGTYPE_ISDISCOUNTCREDIT = "61";
    /**
     * 96已拆包（新增状态）
     */
    public static final Integer ACCE_BILL_STATUS_96 = 96;
	/*** 签收查询资源 */
	public static final String AB_BILL_RESOURCE_RECEIVEBIZ_QUERY = "bill:accebill:abBillReceivebiz:query";
	/*** 签收操作资源 */
	public static final String AB_BILL_RESOURCE_RECEIVEBIZ_SIGN = "bill:accebill:abBillReceivebiz:sign";
    /*** 商票签收查询资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_MERCHQUERY = "bill:accebill:abBillReceivebiz:merch:query";
    /*** 商票签收操作资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_MERCHSIGN = "bill:accebill:abBillReceivebiz:merch:sign";
    /*** 银票签收查询资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_BANKQUERY = "bill:accebill:abBillReceivebiz:bank:query";
    /*** 银票签收操作资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_BANKSIGN = "bill:accebill:abBillReceivebiz:bank:sign";
    /*** 第三方票据签收查询资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_OTHERQUERY = "bill:accebill:abBillReceivebiz:other:query";
    /*** 第三方票据签收操作资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_OTHERSIGN = "bill:accebill:abBillReceivebiz:other:sign";
    /**
     * 签收补录查询资源
     */
    public static final String AB_BILL_RESOURCE_SIGNSUPPLY_QUERY = "bill:accebill:abBillSignSupply:query";
    /**
     * 签收补录操作资源
     */
    public static final String AB_BILL_RESOURCE_SIGNSUPPLY_SUMIT = "bill:accebill:abBillSignSupply:submit";
    /*** 已撤回资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBIZ_RECALL = "bill:accebill:abBillReceivebiz:recall";
    /*** 提示保证查询资源 */
    public static final String AB_BILL_RESOURCE_GUARANTEEAPPLY_QUERY = "bill:accebill:abBillGuaranteeApply:query";
    /*** 提示保证申请资源 */
    public static final String AB_BILL_RESOURCE_GUARANTEEAPPLY_APPLY = "bill:accebill:abBillGuaranteeApply:apply";
    /*** 提示承兑查询资源 */
    public static final String AB_BILL_RESOURCE_ACCEPTAPPLY_QUERY = "bill:accebill:abBillAcceptApply:query";
    /*** 提示承兑申请资源 */
    public static final String AB_BILL_RESOURCE_ACCEPTAPPLY_APPLY = "bill:accebill:abBillAcceptApply:apply";
    /*** 提示收票查询资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEAPPLY_QUERY = "bill:accebill:abBillReceiveApply:query";
    /*** 提示收票申请资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEAPPLY_APPLY = "bill:accebill:abBillReceiveApply:apply";
    /*** 通用撤回查询资源 */
    public static final String AB_BILL_RESOURCE_RECALL_QUERY = "bill:accebill:abBillRecall:query";
    /*** 通用撤回申请资源 */
    public static final String AB_BILL_RESOURCE_RECALL_APPLY = "bill:accebill:abBillRecall:apply";
    /*** 追索通知查询资源 */
    public static final String AB_BILL_RESOURCE_SEARCHFORAPPLY_QUERY = "bill:accebill:abBillSearchforApply:query";
    /*** 追索通知申请资源 */
    public static final String AB_BILL_RESOURCE_SEARCHFORAPPLY_APPLY = "bill:accebill:abBillSearchforApply:apply";
    /*** 追索清偿查询资源 */
    public static final String AB_BILL_RESOURCE_SEARCHFORMANAGE_QUERY = "bill:accebill:abBillSearchforManage:query";
    /*** 追索清偿申请资源 */
    public static final String AB_BILL_RESOURCE_SEARCHFORMANAGE_APPLY = "bill:accebill:abBillSearchforManage:apply";
    /*** 指令查询资源 */
    public static final String AB_BILL_RESOURCE_INSTRUCTION_QUERY = "bill:accebill:abBillInstruction:query";
    /*** 指令发送资源 */
    public static final String AB_BILL_RESOURCE_INSTRUCTION_SEND = "bill:accebill:abBillInstruction:send";
    /*** 指令测试查询资源 */
    public static final String AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST = "bill:accebill:abBillInstruction:queryTest";
    /*** 指令测试操作资源 */
    public static final String AB_BILL_RESOURCE_INSTRUCTION_OPTEST = "bill:accebill:abBillInstruction:opTest";
    /*** 指令打回资源 */
    public static final String AB_BILL_RESOURCE_INSTRUCTION_REPULSE = "bill:accebill:abBillInstruction:repulse";
    /*** 应收票据移存查询资源 */
    public static final String AB_BILL_RESOURCE_TRANSFER_QUERY = "bill:accebill:abbillinventransfer:query";
    /*** 应收票据移存操作资源 */
    public static final String AB_BILL_RESOURCE_TRANSFER_TRANSFER = "bill:accebill:abbillinventransfer:transfer";
    /*** 应收票据取票查询资源 */
    public static final String AB_BILL_RESOURCE_FETCH_QUERY = "bill:accebill:abbillinvenfetch:query";
    /*** 应收票据取票操作资源 */
    public static final String AB_BILL_RESOURCE_FETCH_FETCH = "bill:accebill:abbillinvenfetch:fetch";
    /*** 未用退票查询条件资源 */
    public static final String AB_BILL_RESOURCE_RETURNTICKET_QUERY = "bill:accebill:abemitbillback:query";
    /*** 未用退票确认退票操作资源 */
    public static final String AB_BILL_RESOURCE_RETURNTICKET_CONFIRM = "bill:accebill:abemitbillback:confirm";
    /*** 兑付确认查询条件资源 */
    public static final String AB_BILL_RESOURCE_PAY_QUERY = "bill:accebill:abemitbillpay:query";
    /*** 兑付确认操作资源 */
    public static final String AB_BILL_RESOURCE_PAY_CONFIRM = "bill:accebill:abemitbillpay:confirm";
    /*** 应收票据质押申请查询资源 */
    public static final String AB_BILL_RESOURCE_PLEDGEAPPLY_QUERY = "bill:accebill:abbillpledgeapply:query";
    /*** 应收票据质押申请操作资源 */
    public static final String AB_BILL_RESOURCE_PLEDGEAPPLY_APPLY = "bill:accebill:abbillpledgeapply:apply";
    /*** 应收票据收票登记查询资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBILL_QUERY = "bill:accebill:abreceivebill:query";
    /*** 应收票据收票登记新增资源 */
    public static final String AB_BILL_RESOURCE_RECEIVEBILL_REGISTER = "bill:accebill:abreceivebill:register";
    /*** 应收票据贴现申请查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_QUERY = "bill:accebill:abBillDiscountApply:query";
    /*** 应收票据贴现申请提交资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_APPLY = "bill:accebill:abBillDiscountApply:apply";
    /*** 应收票据贴现办理查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_QUERY = "bill:accebill:abBillDiscountManager:query";
    /*** 应收票据贴现办理提交资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_APPLY = "bill:accebill:abBillDiscountManager:apply";
    /*** 应收票据贴现到账查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTACCOUNT_QUERY = "bill:accebill:abBillDiscountAccount:query";
    /*** 应收票据贴现到账提交资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTACCOUNT_APPLY = "bill:accebill:abBillDiscountAccount:apply";
    /*** 应收票据贴现收回查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTCOLLECT_QUERY = "bill:accebill:abbilldiscountcollect:query";
    /*** 应收票据贴现收回操作资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTCOLLECT_OPREATE = "bill:accebill:abbilldiscountcollect:opreate";
    /*** 应收票据贴现额度释放查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTRELEASE_QUERY = "bill:accebill:abBillDiscountRelease:query";
    /*** 应收票据贴现额度释放操作资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTRELEASE_OPREATE = "bill:accebill:abBillDiscountRelease:apply";
    /*** 应收票据贴现赎回申请查询资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTRANSOM_QUERY = "bill:accebill:abbilldiscountransom:query";
    /*** 应收票据贴现赎回申请操作资源 */
    public static final String AB_BILL_ACCEBILL_ABBILLDISCOUNTRANSOM_OPREATE = "bill:accebill:abbilldiscountransom:opreate";
    /*** 应收票据-票据划拨-票据上划查询资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERUPAPPLY_QUERY = "bill:accebill:abbilltransferupapply:query";
    /*** 应收票据-票据划拨-票据上划操作资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERUPAPPLY_TRANSFERUP = "bill:accebill:abbilltransferupapply:transferup";
    /*** 应收票据-票据划拨-票据归集查询资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERCOLLECTION_QUERY = "bill:accebill:abbilltransfercollection:query";
    /*** 应收票据-票据划拨-票据归集操作资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERCOLLECTION_TRANSFERCOLLECTION = "bill:accebill:abbilltransfercollection:transfercollection";
    /*** 应收票据-票据划拨-票据下拨申请查询资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERDOWNAPPLY_QUERY = "ab:bill:resource:transferdownapply:query";
    /*** 应收票据-票据划拨-票据下拨申请操作资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERDOWNAPPLY_OPREATE = "ab_bill:resource:transferdownapply:opreate";
    /*** 应收票据-票据划拨-票据下拨查询资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERDOWNMANAGE_QUERY = "ab:bill:resource:transferdownmanage:query";
    /*** 应收票据-票据划拨-票据下拨操作资源 */
    public static final String AB_BILL_RESOURCE_TRANSFERDOWNMANAGE_OPREATE = "ab:bill:resource:transferdownmanage:opreate";
    /*** 应收票据解除质押查询资源 */
    public static final String AB_BILL_RESOURCE_PLEDGERELIEVE_QUERY = "bill:accebill:abbillpledgerelieve:query";
    /*** 应收票据解除质押操作资源 */
    public static final String AB_BILL_RESOURCE_PLEDGERELIEVE_RELIEVE = "bill:accebill:abbillpledgerelieve:relieve";
    /*** 应收票据质押收回查询资源 */
    public static final String AB_BILL_RESOURCE_PLEDGECOLLECT_QUERY = "bill:accebill:abbillpledgecollect:query";
    /*** 应收票据质押收回操作资源 */
    public static final String AB_BILL_RESOURCE_PLEDGECOLLECT_COLLECT = "bill:accebill:abbillpledgecollect:collect";
    /*** 应付票据-开票登记-查询资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILL_QUERY = "bill:accebill:abemitbill:query";
    /*** 应付票据-开票登记-保存资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILL_SAVE = "bill:accebill:abemitbill:save";
    /*** 应付票据-开票登记-提交资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILL_SUBMIT = "bill:accebill:abemitbill:submit";
    /*** 应付票据-开票登记-删除资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILL_DELETE = "bill:accebill:abemitbill:delete";
    /*** 应付票据-开票登记-打回资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILL_REPULSE = "bill:accebill:abemitbill:repulse";
    /*** 应付票据-开票补录-查询资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILLSUPPLY_QUERY = "bill:accebill:abemitbillsupply:query";
    /*** 应付票据-开票补录-补录资源 */
    public static final String AB_BILL_RESOURCE_ABEMITBILLSUPPLY_MAKEUP = "bill:accebill:abemitbillsupply:makeup";
    /*** 应付票据-票据托收-托收申请查询资源 */
    public static final String AB_BILL_RESOURCE_COLLECTIONAPPLY_QUERY = "bill:accebill:collectionapply:query";
    /*** 应付票据-票据托收-托收申请提交资源 */
    public static final String AB_BILL_RESOURCE_COLLECTIONAPPLY_SUBMIT = "bill:accebill:collectionapply:submit";
    /*** 应付票据-票据托收-托收到账查询资源 */
    public static final String AB_BILL_RESOURCE_COLLECTIONACCOUNT_QUERY = "bill:accebill:collectionaccount:query";
    /*** 应付票据-票据托收-托收到账确认资源 */
    public static final String AB_BILL_RESOURCE_COLLECTIONACCOUNT_SUBMIT = "bill:accebill:collectionaccount:confirm";
    /*** 票据认领-规则配置-票号规则查询资源 */
    public static final String AB_BILL_RESOURCE_RULEBILLNO_QUERY = "ab:bill:resource:rulebill:query";
    /*** 票据认领-规则配置-票据流水备注规则查询资源 */
    public static final String AB_BILL_RESOURCE_RULEBILLDTLRMK_QUERY = "ab:bill:resource:rulebilltlrmk:query";
    /*** 票据保证金利息维护查询资源 */
    public static final String AB_BILL_RESOURCE_BAILINTERESTINFO_QUERY = "bill:accebill:abemitbill:bailInterestInfo:query";
    /*** 银承开票占比目标维护查询资源 */
    public static final String AB_BILL_RESOURCE_BILLTARGETPROPORTIONINFO_QUERY = "bill:accebill:abemitbill:billTargetProportion:query";
    /*** 银承开票占比目标维护操作资源 */
    public static final String AB_BILL_RESOURCE_BILLTARGETPROPORTIONINFO_OPERATE = "bill:accebill:abemitbill:billTargetProportion:operate";
    /*** 背书请求查询资源 */
    public static final String AB_BILL_RESOURCE_ENDORSEREQUEST_QUERY = "bill:accebill:endorseRequest:query";
    /*** 背书请求操作资源 */
    public static final String AB_BILL_RESOURCE_ENDORSEREQUEST_APPLY = "bill:accebill:endorseRequest:apply";
    /*** 背书请求应答查询资源 */
    public static final String AB_BILL_RESOURCE_ENDORSERESPONSE_QUERY = "bill:accebill:endorseResponse:query";
    /*** 背书请求应答操作资源 */
    public static final String AB_BILL_RESOURCE_ENDORSERESPONSE_APPLY = "bill:accebill:endorseResponse:apply";
    /*** 票据背书操作资源 */
    public static final String AB_BILL_RESOURCE_ENDORSE_CONFIRM = "bill:accebill:abbillendorse:confirm";
    /*** 票据背书查询条件资源 */
    public static final String AB_BILL_RESOURCE_ENDORSE_QUERY = "bill:accebill:abbillendorse:query";
    /*** 退票申请操作资源 */
    public static final String AB_BILL_RESOURCE_REBILLBACK_CONFIRM = "bill:accebill:abreceivebillback:confirm";
    /*** 退票申请查询条件资源 */
    public static final String AB_BILL_RESOURCE_REBILLBACK_QUERY = "bill:accebill:abreceivebillback:query";
    /*** 票据锁定、解锁-锁定操作资源 */
    public static final String AB_BILL_RESOURCE_BILLLOCK_LOCK = "bill:accebill:abReceiveBillLock:lock";
    /*** 票据锁定、解锁-解锁操作资源 */
    public static final String AB_BILL_RESOURCE_BILLLOCK_UNLOCK = "bill:accebill:abReceiveBillLock:unLock";
    /*** 票据锁定、解锁查询条件资源 */
    public static final String AB_BILL_RESOURCE_BILLLOCK_QUERY = "bill:accebill:abReceiveBillLock:query";
    /*** 票据明细查询单位操作权限 */
    public static final String AB_BILL_BILLDTL_QUERY_CORPOP = "bill:accebill:billdtl:query:corpop";
    /*** 票据月度还银承表操作权限 */
    public static final String AB_BILL_BILLBAB_BYMONTH_QUERY_CORPOP = "bill:accebill:billbab:query:corpop";
    /***票据解付与解付账户前一天余额对比表操作权限 */
    public static final String AB_BILL_BANKACC_BAL_COMPARE_QUERY_CORPOP = "bill:accebill:bankaccBalCompare:query:corpop";
    /***银承开票金额统计表操作权限 */
    public static final String AB_BILL_BANK_OPEN_BILL_AMT_STATISTICS_QUERY_CORPOP = "bill:accebill:BankOpenBillAmtStatistics:query:corpop";
    /***银承占比及额度使用情况统计表数据查询权限 */
    public static final String AB_BILL_TARGETPROPORTION_QUOTAUSAGE_STATISTICS_QUERY_CORPOP = "bill:accebill:targetProportionAndQuotaUsage:query:corpop";
    /*** 票据到期兑付确认查询单位操作权限 */
    public static final String AB_BILL_BILLPAYREPORT_QUERY_CORPOP = "bill:accebill:payreport:query:corpop";
    /*** 票据到期托收到账统计查询单位操作权限 */
    public static final String AB_BILL_RESOURCE_COLLECTIONACCOUNTREPORT_CORPOP = "bill:accebill:collectionaccountreport:corpop";
    /*** 票据业务查询单位操作权限 */
    public static final String AB_BILL_BILLBUS_QUERY_CORPOP = "bill:accebill:billbus:query:corpop";
    /*** 票据下拨明细查询单位操作权限 */
    public static final String AB_BILL_TRANSFERDOWN_QUERY_CORPOP = "bill:accebill:transferdown:query:corpop";
    /*** 票据账户余额查询单位操作权限 */
    public static final String AB_BILL_ACCBAL_QUERY_CORPOP = "bill:accebill:accbal:query:corpop";
    /*** 票据单位余额查询单位操作权限 */
    public static final String AB_BILL_CORPBAL_QUERY_CORPOP = "bill:accebill:corpbal:query:corpop";
    /*** 票据贴现到账统计查询单位操作权限 */
    public static final String AB_BILL_DISCOUNTACCOUNTCOUNT_QUERY_CORPOP = "ab:bill:discountaccountcount:query:corpop";
    /*** 票据调拨明细表查询单位操作权限*/
    public static final String AB_BILL_ABBILLTRANSFERDOWN_CORPDTL_QUERY_CORPOP = "bill:accebill:abbillTransferDown:corpDtl:query:corpop";
    /*** 网点票据划拨汇总表查询单位操作权限 */
    public static final String AB_BILL_ABBILLTRANSFERDOWN_NETCOLLECT_QUERY_CORPOP = "bill:accebill:abbillTransferDown:netCollect:query:corpop";
    /*** 票据贴现信息查询单位操作权限 */
    public static final String AB_BILL_DISCOUNTQUERY_QUERY_CORPOP = "bill:accebill:discountquery:query:corpop";
    /*** 应付票据台账查询单位操作权限 */
    public static final String AB_BILL_BILLBUS_QUERY_PAYCORP = "bill:accebill:billbus:query:paycorp";
    /*** 应收票据台账查询单位操作权限 */
    public static final String AB_BILL_BILLBUS_QUERY_RECECORP = "bill:accebill:billbus:query:rececorp";
    /*** 统计报表资源*/
    /*** 即将到期票据分析 查询资源*/
    public static final String AB_BILL_SOON_EXPIRE_QUERY = "bill:accebill:soonexpire:query";
    /*** 票据余额对比分析查询权限 */
    public static final String AB_BILL_BALCOMPARE_QUERY = "bill:accebill:balcompare:query";
    /*** 票据余额重置单位操作权限 */
    public static final String AB_BILL_ACCBAL_RESET_CORPOP = "bill:accebill:accbal:reset:corpop";
    /*** 票据删除查询资源 */
    public static final String AB_BILL_RESOURCE_DELETE_QUERY = "bill:accebill:delete:query";
    /*** 票据删除操作资源 */
    public static final String AB_BILL_RESOURCE_DELETE_CONFIRM = "bill:accebill:delete:confirm";
    /*** 单位统计表资源权限 */
    public static final String AB_BILL_ACCBAL_BILLUNITLSTATISTICS_CORPOP = "bill:accebill:billUnitLstatistics:query:corpop";
    /*** 票据认领处理查询资源 */
    public static final String AB_BILL_RESOURCE_CLAIM_QUERY = "bill:accebill:claim:query";
    /*** 票据认领处理操作资源 */
    public static final String AB_BILL_RESOURCE_CLAIM_APPLY = "bill:accebill:claim:apply";
    /*** 票据认领查询查询资源 */
    public static final String AB_BILL_RESOURCE_CLAIMQUERY_QUERY = "bill:accebill:claimQuery:query";
    /*** 银行开票授信资料维护查询资源 */
    public static final String AB_BILL_RESOURCE_CREDIT_MAINTAIN_QUERY = "bill:accebill:credit:maintain:query";
    /*** 银行开票授信资料维护应用资源 */
    public static final String AB_BILL_RESOURCE_CREDIT_MAINTAIN_APPLY = "bill:accebill:credit:maintain:apply";
    /*** 银行计划开票金额维护查询资源 */
    public static final String AB_BILL_RESOURCE_PLANAMT_MAINTAIN_QUERY = "bill:accebill:planamt:maintain:query";
    /*** 银行计划开票金额维护应用资源 */
    public static final String AB_BILL_RESOURCE_PLANAMT_MAINTAIN_APPLY = "bill:accebill:planamt:maintain:apply";
    /**
     * 新增数据成功！
     */
    public static final String LANG_LOG_SAVE_SUCC = "bill.log.save.succ";
    /**
     * 修改数据成功！
     */
    public static final String LANG_LOG_UPDATE_SUCC = "bill.log.update.succ";
    /**
     * 删除数据成功！
     */
    public static final String LANG_LOG_DELETE_SUCC = "bill.log.delete.succ";
    /**
     * 撤回数据成功！
     */
    public static final String LANG_LOG_WITHDRAW_SUCC = "bill.log.withdraw.succ";
    /**
     * 兑付确认成功！
     */
    public static final String LANG_LOG_PAY_SUCC = "bill.log.pay.succ";
    /**
     * 批量兑付确认成功！
     */
    public static final String LANG_LOG_BATCHPAY_SUCC = "bill.log.batchpay.succ";
    /**
     * 复制数据成功！
     */
    public static final String LANG_LOG_COPY_SUCC = "bill.log.copy.succ";
    /**
     * 打印数据成功！
     */
    public static final String LANG_LOG_PRINT_SUCC = "bill.log.print.succ";
    /**
     * 重新打印数据成功！
     */
    public static final String LANG_LOG_REPPRINT_SUCC = "bill.log.repprint.succ";
    /**
     * 批量删除数据成功！
     */
    public static final String LANG_LOG_BATCHDELETE_SUCC = "bill.log.batchdelete.succ";
    /**
     * 批量移库数据成功！
     */
    public static final String LANG_LOG_BATCHTRANSF_SUCC = "bill.log.batchtransf.succ";
    /**
     * 批量取票数据成功！
     */
    public static final String LANG_LOG_BATCHFETCH_SUCC = "bill.log.batchfetch.succ";
    /**
     * 批量确认,发送电票指令！
     */
    public static final String LANG_LOG_BATCHINSSEND_SUCC = "bill.log.batchInsSend.succ";
    /**
     * 批量成功,银行电票指令处理成功！
     */
    public static final String LANG_LOG_BATCHINSBANK_SUCC = "bill.log.batchInsBank.succ";
    /**
     * 批量确认,打回电票指令！
     */
    public static final String LANG_LOG_BATCHINSREPULSE_SUCC = "bill.log.batchInsRepulse.succ";
    /**
     * 提交数据成功！
     */
    public static final String LANG_LOG_SUBMIT_SUCC = "bill.log.submit.succ";
    /**
     * 批量提交数据成功！
     */
    public static final String LANG_LOG_BATCHSUBMIT_SUCC = "bill.log.batchsubmit.succ";
    /**
     * 定时器操作数据成功！
     */
    public static final String LANG_LOG_TASK_SUCC = "bill.log.task.succ";
    /**
     * 贴现申请打回！
     */
    public static final String LANG_LOG_DISCOUNT_BACK = "lang.log.discount.back";
    /**
     * 审批通过！
     */
    public static final String LANG_LOG_APP_SUCC = "bill.log.app.succ";
    /**
     * 自动记账完成！
     */
    public static final String LANG_LOG_AUTOACCBOOK_SUCC = "bill.log.autoaccbook.succ";
    /**
     * 手动记账完成！
     */
    public static final String LANG_LOG_ACCBOOK_SUCC = "bill.log.accbook.succ";
    /**
     * 审批打回！
     */
    public static final String LANG_LOG_APP_BACK = "bill.log.app.back";
    /**
     * 手动记账打回！
     */
    public static final String LANG_LOG_ACCBOOK_BACK = "bill.log.accbook.back";
    /**
     * 导入数据成功！
     */
    public static final String LANG_LOG_IMPORT_SUCC = "bill.log.import.succ";
    /**
     * 背书流水占用成功！
     */
    public static final String LANG_LOG_ENDORSE_HOLD = "bill.log.endorse.hold";
    /**
     * 背书流水解除占用成功！
     */
    public static final String LANG_LOG_ENDORSE_UNHOLD = "bill.log.endorse.unHold";
    /**
     * 票据号不存在或没有此功能操作权限！
     */
    public static final String LANG_ERROR_ORIGNO_NOOP = "bill.error.origno.noop";
    /**
     * 票据号处于审批不能做业务！
     */
    public static final String LANG_ERROR_ORIGNO_APPROVEOP = "bill.error.origno.approveop";
    /**
     * 保证人所属银行行号和银行名称不能同时为空！
     */
    public static final String LANG_ERROR_GUARANTEEBANK_CODEANDNAME_NONULL = "bill.error.guaranteebank.codeandname.notnull";
    /**
     * 保证人所属银行行号或银行行名不正确！
     */
    public static final String LANG_ERROR_GUARANTEEBANK_CODEORNAME = "bill.error.guaranteebank.codeorname";
    /**
     * 数据中票据号不能重复！
     */
    public static final String LANG_ERROR_ORIGNO_NOTREPETITION = "bill.error.origno.notrepetition";
    /**
     * 单位代码不存在，检查是否正确！
     */
    public static final String LANG_ERROR_CORP_NONENTITY = "bill.error.corp.nonentity";
    /**
     * 票据号码不能为空！
     */
    public static final String LANG_ERROR_ORIGNO_ISNOTNULL = "accebill.message.origno.isnotnull";
    /**
     * 单位代码不能为空！
     */
    public static final String LANG_ERROR_CORPCODE_ISNOTNULL = "accebill.error.corpcode.isnotnull";
    /**
     * 保证人名称不能为空！
     */
    public static final String LANG_ERROR_GUARANTEENM_ISNOTNULL = "accebill.error.guaranteenm.isnotnull";
    /**
     * 保证人账号不能为空！
     */
    public static final String LANG_ERROR_GUARANTEEACC_ISNOTNULL = "accebill.error.guaranteeacc.isnotnull";
    /**
     * 被追索人名称不能为空！
     */
    public static final String LANG_ERROR_BSEARCHFORPERSONNM_ISNOTNULL = "accebill.error.bsearchforpersonm.isnotnull";
    /**
     * 被追索人账号不能为空！
     */
    public static final String LANG_ERROR_BSEARCHFORPERSONACC_ISNOTNULL = "accebill.error.bsearchforpersonacc.isnotnull";
    /**
     * 被追索人机构号不能为空！
     */
    public static final String LANG_ERROR_BSEARCHFORPERSONCMONID_ISNOTNULL = "accebill.error.bsearchforpersoncmonid.isnotnull";
    /**
     * 被追索人机构号格式不正确！
     */
    public static final String LANG_BSEARCHFORPERSONCMONID_FORMAT_ERROR = "accebill.bsearchforpersoncmonid.format.error";
    /**
     * 被追索人所属银行行号和银行名称不能同时为空！
     */
    public static final String LANG_ERROR_BSEARCHFORPERSONBANK_CODEANDNAME_NONULL = "bill.error.bsearchforpersonbank.codeandname.notnull";
    /**
     * 被追索人所属银行行号或银行行名不正确！
     */
    public static final String LANG_ERROR_BSEARCHFORPERSONBANK_CODEORNAME = "bill.error.bsearchforpersonbank.codeorname";
    /**
     * 非拒付追索，追索原因不能为空！
     */
    public static final String LANG_ERROR_SEARCHFORPERSONBRCRSRSNCD_ISNOTNULL = "bill.error.searchforpersonbrcrsrsncd.isnotnull";
    /*** 票据类型不能为空 */
    public static final String ACCEBILL_MESSAGE_BILLTYPE_ISNOTNULL = "accebill.message.billtype.isnotnull";
//	/*** 票据类型只能为银票或商票 */
//	public static final String ACCEBILL_MESSAGE_BILLTYPE_ERROR = "accebill.message.billtype.error";
    /*** 票据类型不正确，必须为字典维护的票据类型代码或者名称！ */
    public static final String ACCEBILL_MESSAGE_BILLTYPE_ERROR = "accebill.message.billtype.error";
    /*** 开票方式不正确，必须为字典维护的票据类型代码或者名称！ */
    public static final String ACCEBILL_MESSAGE_EMITTYPE_ERROR = "accebill.message.emittype.error";
    /*** 流通介质不能为空 */
    public static final String ACCEBILL_MESSAGE_BILLFLAG_ISNOTNULL = "accebill.message.billflag.isnotnull";
    /*** 流通介质只能为电票或纸票 */
    public static final String ACCEBILL_MESSAGE_BILLFLAG_ERROR = "accebill.message.billflag.error";
    /*** 所属库存不能为空 */
    public static final String ACCEBILL_MESSAGE_ENTERSTOCK_ISNOTNULL = "accebill.message.enterstock.isnotnull";
    /*** 所属库存只能为单位库存，中心代管或银行托管 */
    public static final String ACCEBILL_MESSAGE_ENTERSTOCK_ERROR = "accebill.message.enterstock.error";
    /*** 电票只能为单位库存 */
    public static final String ACCEBILL_MESSAGE_BILLFLAG11_CORPENTERSTOCK = "accebill.message.billFlag11.corpEnterStock";
    /*** 票据来源不能为空 */
    public static final String ACCEBILL_MESSAGE_BILLSOURCE_ISNOTNULL = "accebill.message.billsource.isnotnull";
    /*** 票据来源只能为收票，背书或质押 */
    public static final String ACCEBILL_MESSAGE_BILLSOURCE_ERROR = "accebill.message.billsource.error";
    /*** 票据来源为质押时只能为单位库存 */
    public static final String ACCEBILL_MESSAGE_BILLSOURCE_PLEDGE_ERROR = "accebill.message.billsource.pledge.error";
    /*** 收票方式不能为空 */
    public static final String ACCEBILL_MESSAGE_INSIDESING_ISNOTNULL = "accebill.message.insidesing.isnotnull";
    /*** 收票方式只能为对内,对外*/
    public static final String ACCEBILL_MESSAGE_INSIDESING_ERROR = "accebill.message.insidesing.error";
    /*** 不得转让标记不能为空 */
    public static final String ACCEBILL_MESSAGE_BANENDRSMTMK_ISNOTNULL = "accebill.message.banendrsmtmk.isnotnull";
    /*** 不得转让标记只能为可再转让或不得转让 */
    public static final String ACCEBILL_MESSAGE_BANENDRSMTMK_ERROR = "accebill.message.banendrsmtmk.error";
    /*** 所属单位不能为空 */
    public static final String ACCEBILL_MESSAGE_CORPID_ISNOTNULL = "accebill.message.corpid.isnotnull";
    /*** 票据单位不存在 */
    public static final String ACCEBILL_MESSAGE_CORPID_ISNOTEXIST = "accebill.message.corpid.isnotexist";
    /*** 申请单位不存在 */
    public static final String ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST = "accebill.message.applycorpid.isnotexist";
    /*** 业务单位不存在 */
    public static final String ACCEBILL_MESSAGE_BUSCORPID_ISNOTEXIST = "accebill.message.buscorpid.isnotexist";
    /*** 票据单位账号不存在 */
    public static final String ACCEBILL_MESSAGE_BANKACC_ISNOTEXIST = "accebill.message.bankacc.isnotexist";
    /*** 票据单位账号不能为空 */
    public static final String ACCEBILL_MESSAGE_BANKACC_ISNOTNULL = "accebill.message.bankacc.isnotnull";
    /*** 票据单位账号与票据单位不匹配! */
    public static final String ACCEBILL_MESSAGE_BANKACCANDCORP_NOTMATCH = "accebill.message.bankaccandcorp.notmatch";
    /*** 票据号不能重复 */
    public static final String ACCEBILL_MESSAGE_ORIGNO_REPEAT = "accebill.message.origno.repeat";
    /*** 第【】条数据票据号【】不能重复！ */
    public static final String ACCEBILL_MESSAGE_SUBMIT_ORIGNO_REPEAT = "accebill.message.submit.origno.repeat";
    /*** 票据号在系统中已经存在,不能重复 */
    public static final String ACCEBILL_MESSAGE_ORIGNOINSYSTEM_REPEAT = "accebill.message.orignoInSystem.repeat";
    /*** 出票日期不能为空 */
    public static final String ACCEBILL_MESSAGE_STARTDATE_ISNOTNULL = "accebill.message.startdate.isnotnull";
    /*** 到期日期不能为空 */
    public static final String ACCEBILL_MESSAGE_ENDDATE_ISNOTNULL = "accebill.message.enddate.isnotnull";
    /*** 业务办理日期不能为空*/
    public static final String ACCEBILL_MESSAGE_BUSINESSDATE_ISNOTNULL = "accebill.message.businessdate.isnotnull";
    /*** 业务办理日期必须大于等于出票日期,小于到期日期! */
    public static final String BILL_ACCEBILL_ABRECEIVEBILL_CHECKBUSINESSDATE = "bill.accebill.abReceiveBill.checkBusinessDate";
    /*** 到期日期必须大于出票日期 */
    public static final String ACCEBILL_MESSAGE_COMSTARTTOEND = "accebill.message.comstarttoend";
    /*** 电票到期日不能超过开票日一年 */
    public static final String BILL_ACCEBILL_BILLFLAG_11 = "bill.accebill.billFlag_11";
    /*** 纸票到期日不能超过开票日半年 */
    public static final String BILL_ACCEBILL_BILLFLAG_10 = "bill.accebill.billFlag_10";
    /*** 票据金额不能为空 */
    public static final String ACCEBILL_MESSAGE_BILLMONEY_ISNOTNULL = "accebill.message.billmoney.isnotnull";
    /*** 票据金额最小值为0.01 */
    public static final String ACCEBILL_MESSAGE_BILLMONEYMIX = "accebill.message.billMoneyMix";
    /*** 电票金额不能大于1000000000 */
    public static final String TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11 = "table.abvoucherinfo.billmoney.billflag11";
    /*** 纸票金额不能大于1000000*/
    public static final String TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG10 = "table.abvoucherinfo.billmoney.billflag10";
    /*** 纸票金额不能超过设置的额度*/
    public static final String TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY = "table.abvoucherinfo.billflag10.maxbillmoney";
    /*** 出票人不能为空 */
    public static final String ACCEBILL_MESSAGE_PAYER_ISNOTNULL = "accebill.message.payer.isnotnull";
    /*** 电票出票人账号不能为空 */
    public static final String ACCEBILL_MESSAGE_PAYERACC_BILLFLAG11_ISNOTNULL = "accebill.message.payeracc.billFlag11.isnotnull";
    /*** 电票出票人开户银行名称不能为空 */
    public static final String ACCEBILL_MESSAGE_PAYERBANKNAME_BILLFLAG11_ISNOTNULL = "accebill.message.payerbankname.billFlag11.isnotnull";
    /*** 收票人不能为空 */
    public static final String ACCEBILL_MESSAGE_PAYEE_ISNOTNULL = "accebill.message.payee.isnotnull";
    /*** 电票收票人账号不能为空 */
    public static final String ACCEBILL_MESSAGE_PAYEEACC_BILLFLAG11_ISNOTNULL = "accebill.message.payeeacc.billFlag11.isnotnull";
    /*** 电票收票人开户银行名称不能为空 */
    public static final String MESSAGE_PAYEEBANKNAME_BILLFLAG11_ISNOTNULL = "accebill.message.payeebankname.billFlag11.isnotnull";
    /*** 承兑人不能为空 */
    public static final String ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL = "accebill.message.acceptor.isnotnull";
    /*** 承兑行名称不存在 */
    public static final String ACCEBILL_MESSAGE_ACCEPTOR_ISNULL = "accebill.message.acceptor.isnull";
    /*** 电票承兑人账号不能为空*/
    public static final String ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL = "accebill.message.acceptoracc.billFlag11.isnotnull";
    /*** 电票承兑人开户行名称不能为空 */
    public static final String ACCEBILL_MESSAGE_ACCEPTORBANKNAME_BILLFLAG11_ISNOTNULL = "accebill.message.acceptorbankname.billFlag11.isnotnull";
    /*** 电票承兑人开户行行号不能为空*/
    public static final String ACCEBILL_MESSAGE_ACCEPTORBANKNO_BILLFLAG11_ISNOTNULL = "accebill.message.acceptorbankno.billFlag11.isnotnull";
    /*** 是否内部贴现不能为空*/
    public static final String ACCEBILL_MESSAGE_DISCOUNTAPPLY_INSIDESING_ISNOTNULL = "accebill.message.discountApply.insideSing.isnotnull";
    /*** 是否内部贴现只能为是或否*/
    public static final String ACCEBILL_MESSAGE_DISCOUNTAPPLY_INSIDESING_ERROR = "accebill.message.discountApply.insideSing.error";
    /*** 内部贴现时,贴入人账号和贴入人名称不能同时为空!*/
    public static final String ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTNULL = "accebill.message.discountRecAcctId.isnotnull";
    /*** 贴入人账号不存在*/
    public static final String ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTEXIST = "accebill.message.discountRecAcctId.isnotexist";
    /**
     * 外部贴现时,贴入人开户行号和名称不能同时为空！
     */
    public static final String LANG_ERROR_DISCOUNTREC_CODEANDNAME_NONULL = "bill.error.discountrec.codeandname.notnull";
    /**
     * 贴入人开户行号或名称不正确！
     */
    public static final String LANG_ERROR_DISCOUNTREC_CODEORNAME = "bill.error.discountrec.codeorname";
    /**
     * 中心代管的纸票,只能进行内部贴现！
     */
    public static final String ACCEBILL_MESSAGE_DISCOUNTAPPLY_BILLFLAG10ANDNET_TOINNER = "accebill.message.discountapply.billflag10andnet.toinner";
    /**
     * 银行托管的纸票,只能贴到银行！
     */
    public static final String ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG10ANDBANK_TOOUTER = "accebill.message.discountrec.billflag10andbank.toouter";
    /**
     * 电票只能贴到银行！
     */
    public static final String ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG11_TOOUTER = "accebill.message.discountrec.billflag11.toouter";
    /**
     * 票据单位代码和票据单位不能同时为空！
     */
    public static final String ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL = "accebill.message.corp.codeandname.notnull";
    /**
     * 申请单位代码和申请单位不能同时为空！
     */
    public static final String ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL = "accebill.message.corp.applycodeandname.notnull";
    /**
     * 贴现种类不能为空！
     */
    public static final String ACCEBILL_MESSAGE_RPDMK_ISNOTNULL = "accebill.message.rpdmk.isnotnull";
    /**
     * 贴现种类只能为买断式或回购式！
     */
    public static final String ACCEBILL_MESSAGE_RPDMK_ERROR = "accebill.message.rpdmk.error";
    /**
     * 清算方式不能为空！
     */
    public static final String ACCEBILL_MESSAGE_STTLMMK_ISNOTNULL = "accebill.message.sttlmmk.isnotnull";
    /**
     * 清算方式只能为线上清算或线下清算！
     */
    public static final String ACCEBILL_MESSAGE_STTLMMK_ERROR = "accebill.message.sttlmmk.error";
    /**
     * 贴现日期不能为空！
     */
    public static final String ACCEBILL_MESSAGE_DISCOUNTDATE_ISNOTNULL = "accebill.message.discountdate.isnotnull";
    /**
     * 贴现申请日期必须大于等于出票日期,小于到期日期!！
     */
    public static final String ACCEBILL_MESSAGE_DISCOUNTDATE_COMPARESTARTANDEND = "accebill.message.discountdate.comparestartandend";
    /**
     * 贴现种类为回购式时,赎回开放日不能为空！
     */
    public static final String ACCEBILL_MESSAGE_RM01_RPDOPENDT_ISNOTNULL = "accebill.message.rm01.rpdopendt.isnotnull";
    /**
     * 贴现种类为回购式时,赎回截止日不能为空！
     */
    public static final String ACCEBILL_MESSAGE_RM01_RPDDUEDT_ISNOTNULL = "accebill.message.rm01.rpdduedt.isnotnull";
    /**
     * 赎回截止日必须大于赎回开放日!
     */
    public static final String BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMDUETOOPENMSG = "bill.accebill.abBillDiscountManager.comDueToOpenMsg";
    /**
     * 贴现申请日期不能大于赎回开放日！!
     */
    public static final String BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDOPENDTMSG = "bill.accebill.abBillDiscountManager.comRpdopendtMsg";
    /**
     * 赎回截止日必须大于贴现申请日期！!
     */
    public static final String BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDDUEDTMSG = "bill.accebill.abBillDiscountManager.comRpdduedtMsg";
    /**
     * 赎回开放日必须大于等于出票日期小于到期日期!
     */
    public static final String BILL_ACCEBILL_RPDOPENDT_COMPARESTARTANDEND = "bill.accebill.rpdopendt.comparestartandend";
    /**
     * 赎回截止日必须大于出票日期小于到期日期!
     */
    public static final String BILL_ACCEBILL_RPDDUEDT_COMPARESTARTANDEND = "bill.accebill.rpdduedt.comparestartandend";
    /**
     * 划款天数不能为空!
     */
    public static final String ACCEBILL_MESSAGE_PAYDATE_ISNOTNULL = "accebill.message.paydate.isnotnull";
    /**
     * 划款天数只能为本地0天,异地3天!
     */
    public static final String ACCEBILL_MESSAGE_PAYDATE_ERROR = "accebill.message.paydate.error";
    /**
     * 假期顺延天数不能为空!
     */
    public static final String ACCEBILL_MESSAGE_DEFERDATE_ISNOTNULL = "accebill.message.deferdate.isnotnull";
    /**
     * 贴现天数不能为空!
     */
    public static final String ACCEBILL_MESSAGE_DAYNUM_ISNOTNULL = "accebill.message.daynum.isnotnull";
    /**
     * 是否买方付息不能为空!
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTSING_ISNOTNULL = "accebill.message.buyerinterestsing.isnotnull";
    /**
     * 买方付息比率不能为空!
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTRATIO_ISNOTNULL = "accebill.message.buyerinterestratio.isnotnull";
    /**
     * 买方付息比率必须大于等于0%,小于等于100%!
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTRATIO_ERROR = "accebill.message.buyerinterestratio.error";
    /**
     * 是否买方付息为是时,买方付息比率必须为100% !
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTRATIO_100 = "accebill.message.buyerinterestratio.100";
    /**
     * 贴现利率不能为空!
     */
    public static final String ACCEBILL_MESSAGE_INTERESTRATES_ISNOTNULL = "accebill.message.interestrates.isnotnull";
    /**
     * 贴现利率必须大于等于0%,小于等于100%!
     */
    public static final String ACCEBILL_MESSAGE_INTERESTRATES_ERROR = "accebill.message.interestrates.error";
    /**
     * 贴现利息不能为空!
     */
    public static final String ACCEBILL_MESSAGE_INTEREST_ISNOTNULL = "accebill.message.interest.isnotnull";
    /**
     * 贴现利息不能大于票据金额!
     */
    public static final String ACCEBILL_MESSAGE_INTEREST_ERROR = "accebill.message.interest.error";
    /**
     * 买方付息金额不能为空!
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTMONEY_ISNOTNULL = "accebill.message.buyerinterestmoney.isnotnull";
    /**
     * 买方付息金额不能大于贴现利息!
     */
    public static final String ACCEBILL_MESSAGE_BUYERINTERESTMONEY_ERROR = "accebill.message.buyerinterestmoney.error";
    /**
     * 实付金额不能为空!
     */
    public static final String ACCEBILL_MESSAGE_REALMONEY_ISNOTNULL = "accebill.message.realmoney.isnotnull";
    /**
     * 实付金额不能大于票据金额!
     */
    public static final String ACCEBILL_MESSAGE_REALMONEY_ERROR = "accebill.message.realmoney.error";
    /**
     * 流通介质为电票时,入账行号不能为空!
     */
    public static final String ACCEBILL_MESSAGE_AOACCNINFAC_BILLFLAG11_ISNOTNULL = "accebill.message.aoaccninfac.billFlag11.isnotnull";
    /**
     * 流通介质为电票时,入账账户不能为空!
     */
    public static final String ACCEBILL_MESSAGE_AOACCNINFID_BILLFLAG11_ISNOTNULL = "accebill.message.aoaccninfid.billFlag11.isnotnull";
    /**
     * 开票登记单号不能为空!
     */
    public static final String ACCEBILL_MESSAGE_APPNO_ISNOTNULL = "accebill.message.appno.isnotnull";
    /**
     * 票据单位账号不是票据专户!
     */
    public static final String ACCEBILL_MESSAGE_BANKACC_ISNOTELECTRICBILL = "accebill.message.bankAcc.isnotElectricBill";
    /**
     * 票据池不能为空
     */
    public static final String ACCEBILL_BILLPOOL_ERRORMSG_PBCODEANDNAME_ISNOTEXIST = "accebill.billpool.errormsg.pbcodeandname.isnotexist";
    /**
     * 申请人账号不能为空
     */
    public static final String ACCEBILL_BILLPOOL_ERRORMSG_PAYERACC_ISNOTEXIST = "accebill.billpool.errormsg.payeracc.isnotexist";
    /**
     * 融资用途不能为空
     */
    public static final String ACCEBILL_BILLPOOL_ERRORMSG_ABS_ISNOTEXIST = "accebill.billpool.errormsg.abs.isnotexist";
    /**
     * 收票人不能为空
     */
    public static final String ACCEBILL_BILLPOOL_ERRORMSG_BANENDRSMTMKNAME_ISNOTEXIST = "accebill.billpool.errormsg.banendrsmtmkname.isnotexist";
    /**
     * 票据池不存在
     */
    public static final String AB_BILLPOOL_EXCEL_ERRORMSG_PBCODEANDNAME_ISNOTEXIST = "ab.billpool.excel.errormsg.pbcodeandname.isnotexist";
    /**
     * 申请人银行账号和票据池只能是兴业银行
     */
    public static final String AB_BILLPOOL_EXCEL_ERRORMSG_EMITFINA_ERROR = "ab.billpool.excel.errormsg.emitfina.error";
    //导入错误提示

    /**
     * 票据信息不正确
     */
    public static final String ACCEBILL_MESSAGE_ERROR_BILLISNONULL = "accebill.message.error.billisnonull";

    /**
     * 票据信息不存在，请检查
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_DATA_NULL = "ab.bill.message.data.null";
    /**
     * 票据号码不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ORIGNO_NULL = "ab.bill.message.origNo.null";
    /**
     * 票据单位代码不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_CORPCODE_NULL = "ab.bill.message.corpCode.null";
    /**
     * 票据单位代码错误
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_CORPCODE_ERROR = "ab.bill.message.corpCode.error";
    /**
     * 申请单位代码错误
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_APPLYCORPCODE_ERROR = "ab.bill.message.applycorpCode.error";
    /**
     * 业务办理日期必须大于等于出票日期,小于到期日期!
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_CHECKBUSINESSDATE = "bill.accebill.abReceiveBill.checkBusinessDate";
    /**
     * 业务办理日期不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BUSINESSDATE_NULL = "ab.bill.message.businessDate.null";
    /**
     * 不得转让值错误
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BANKENDRSMTMK_ERROR = "ab.bill.message.bankendrSmtmk.error";
    /**
     * 是否内部质押不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTERIORAPPLY_NULL = "ab.bill.pledge.message.interiorApply.null";
    /**
     * 内部质押时质权人单位代码不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPCODE_NULL = "ab.bill.pledge.message.pledgeCorpCode.null";
    /**
     * 质权人账号不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_PLEDGEEACC = "ab.bill.pledge.message.pledgeeAcc.null";
    /**
     * 中心代管票据只能做内部质押业务
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_ENTERSTOCK_NET_FALSE_INTERIORAPPLY = "ab.bill.pledge.message.enterStock.netFalseInteriorApply";
    /**
     * 银行托管票据只能做外部质押业务
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_ENTERSTOCK_BANK_TRUE_INTERIORAPPLY = "ab.bill.pledge.message.enterStock.bankTrueInteriorApply";
    /**
     * 质权人账号与质权人单位不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPNAME_BANKACC_ISNOTEXIST = "ab.bill.pledge.message.pledgeCorpName.bankaccisnotexist";
    /**
     * 质权人账号不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKACC_ISNOTEXIST = "ab.bill.pledge.message.bankacc.isnotexist";
    /**
     * 质权人单位不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_CORPNAME_ISNOTEXIST = "ab.bill.pledge.message.pledgeCorpName.isnotexist";
    /**
     * 质权人开户行行号不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKCODE_NULL = "ab.bill.pledge.message.bankCode.null";
    /**
     * 质权人开户行行号不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_BANKACC_NULL = "ab.bill.pledge.message.pledgeeBankAcc.null";
    /**
     * 质权人名称不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_NAME_NULL = "ab.bill.pledge.message.pledgeeName.null";
    /**
     * 质押单位代码与质押单位名称不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_CORPNAME_ISNOT = "ab.bill.pledge.message.interiorApply.corpName.isnot";
    /**
     * 票据单位与质权人单位不能相同
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_CORP_NOTEQUAL = "ab.bill.pledge.message.interiorApply.corp.notEqual";
    /**
     * 是否内部质押值错误
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PLEDGE_INTAPPLY_ERROR = "ab.bill.pledge.message.interiorApply.error";
    /**
     * 是否内部背书不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTERIORAPPLY_NULL = "ab.bill.endorse.message.interiorApply.null";
    /**
     * 是否可转让不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKENDRSMTMK_NULL = "ab.bill.endorse.message.bankendrSmtmk.null";
    /**
     * 内部单位不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_CORPNAME_ISNOTEXIST = "ab.bill.endorse.message.endorseCorpName.isnotexist";
    /**
     * 被背书人账号不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_ENDORSEACC_NULL = "ab.bill.endorse.message.endorseAcc.null";
    /**
     * 被背书人账号与内部单位不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_CORPNAME_BANKACC_ISNOTEXIST = "ab.bill.endorse.message.endorseCorpName.bankaccisnotexist";
    /**
     * 被背书人账号不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKACC_ISNOTEXIST = "ab.bill.endorse.message.bankacc.isnotexist";
    /**
     * 内部背书时内部单位代码不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_INTAPPLY_ENDORSE_CORPCODE_NULL = "ab.bill.endorse.message.endorseCorpName.null";
    /**
     * 被背书人开户行行号不正确
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKCODE_NULL = "ab.bill.endorse.message.bankCode.null";
    /**
     * 被背书人开户行行号不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKACC_NULL = "ab.bill.endorse.message.pledgeeBankAcc.null";
    /**
     * 被背书人名称不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_NAME_NULL = "ab.bill.endorse.message.endorseName.null";
    /**
     * 票据单位代码与票据单位名称不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_CORPNAME_ISNOT = "ab.bill.message.corpName.isnot";
    /**
     * 申请单位代码与申请单位名称不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_APPLYCORPNAME_ISNOT = "ab.bill.message.applycorpName.isnot";
    /**
     * 内部单位代码与内部单位名称不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_CORPNAME_ISNOT = "ab.bill.endorse.message.interiorApply.corpName.isnot";
    /**
     * 票据单位与被背书人单位不能相同
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_CORP_NOTEQUAL = "ab.bill.endorse.message.interiorApply.corp.notEqual";
    /**
     * 是否内部背书值错误
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTAPPLY_ERROR = "ab.bill.endorse.message.interiorApply.error";
    /**
     * 开出方式不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_INSIDESING_NULL = "ab.bill.endorse.message.insideSing.null";
    /**
     * 收票人名称或收票人账号不存在
     */
    public static final String ACCEBILL_MESSAGE_PAYEEACC_ISNOTEXIST = "ab.bill.endorse.message.payeeacc.null";
    /**
     * 流通介质不正确，应为:10纸票或11电票
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BILLFLAG_ISNOTEXIST = "ab.bill.excel.errormsg.billflag.isnotexist";
    /**
     * 流通类型不正确，应为:10银票或11商票
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BILLTYPE_ISNOTEXIST = "ab.bill.excel.errormsg.billtype.isnotexist";
    /**
     * 开出方式不正确，应为:0对外或1对内
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_INSIDESING_ISNOTEXIST = "ab.bill.excel.errormsg.insidesing.isnotexist";
    /**
     * 出票人账号与票据单位不匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PAYERCORP_MATCHERROR = "ab.bill.excel.errormsg.payercorp.matcherror";
    /**
     * 出票人账号不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PAYERACC_ISNOTEXIST = "ab.bill.excel.errormsg.payeracc.isnotexist";
    /**
     * 收票人账号不存在
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PAYEEACC_ISNOTEXIST = "ab.bill.excel.errormsg.payeeacc.isnotexist";
    /**
     * 收票人开户银行名称不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PAYEEBANKNAME_ISNOTEXIST = "ab.bill.excel.errormsg.payeebankname.isnotexist";
    /**
     * 收票人开户银行行号不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_PAYEEBANKCODE_ISNOTEXIST = "ab.bill.excel.errormsg.payeebankcode.isnotexist";
    /**
     * 不得转让标记不正确，应为:EM00可再转让或EM01不可转让
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BANENDRSMTMK_ISNOTEXIST = "ab.bill.excel.errormsg.banendrsmtmk.isnotexist";
    /**
     * 开票登记单号找不到票据
     */
    public static final String BILL_ACCEBILL_APPNO_ISNOTEXIST = "bill.accebill.appno.isnotexist";
    /**
     * 资金计划科目代码不能为空
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST = "ab.bill.excel.errormsg.itemcode.isnotexist";
    /**
     * 资金计划科目在系统中不存在!
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNULL = "ab.bill.excel.errormsg.itemcode.isnull";
    /**
     * 请确认科目代码资金流向与业务是否匹配
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISMATCH_BUSINESS = "ab.bill.excel.errormsg.itemcode.ismatch_business";
    /**
     * 单位未上报计划
     */
    public static final String AB_BILL_EXCEL_ERRORMSG_BUDGETCORP_ISNOTEXIST = "ab.bill.excel.errormsg.budgetcorp.isnotexist";
    /** 不可拆分票据不允许做子票区间拆分！ */
    public static final String AB_BILL_EXCEL_ERRORMSG_NOT_SPLIT = "ab.bill.excel.errormsg.not.split";
    /** 开始纸票区间必须小于结束子票区间！ */
    public static final String AB_BILL_EXCEL_ERRORMSG_SPLIT_START_BE_END = "ab.bill.excel.errormsg.split.start.be.end";
    /** 持有票据子票必须包含拆分子票区间！ */
    public static final String AB_BILL_EXCEL_ERRORMSG_OLD_NOT_INCLUDE_SPLIT = "ab.bill.excel.errormsg.old.not.include.split";
    /** 拆分金额和子票区间不匹配！ */
    public static final String AB_BILL_EXCEL_ERRORMSG_TRANSMONEY_NOT_SBBLLRNG = "ab.bill.excel.errormsg.transmoney.not.sbbllrng";
    /** 拆分子票区间存在交叉！ */
    public static final String AB_BILL_EXCEL_ERRORMSG_SPLIT_SBBLLRNG_REP = "ab.bill.excel.errormsg.split.sbbllrng.rep";

    /**
     * 查询账号为空
     */
    public static final String PAYERACC_IS_NUL = "payerAcc.is.null";
    /**
     * 票据类型为空
     */
    public static final String BILLTYPE_IS_NUL = "billType.is.null";
    /**
     *签收类型为空
     */
    public static final String SIGNTYPE_IS_NUL = "signType.is.null";




    /**
     * 新增银行授信贴现
     **/
    public static final String AB_ADD_BANK_ACCEBILL_ISDISCOUNTCREDIT = "ab.add.bank.accebill.isDisCountCredit";
    /**
     * 银行授信数据释放
     **/
    public static final String AB_ADD_BANK_DATA_RELEASE = "ab.add.bank.data.Release";
    /**
     * 票据余额定时器类型参数：设置当天票据账户余额处理
     **/
    public static final String ACCEBILL_TASK_AUTOTODAYBILLACCBAL = "autoTodayBillAccBal";
    /**
     * 票据余额定时器类型参数：设置电票已完成的业务进行票据账户余额处理
     **/
    public static final String ACCEBILL_TASK_ELECTBUSBILLACCBAL = "electBusBillAccBal";
    /**
     * 票据余额定时器类型参数：设置质押未解除已到期截止票据账户余额处理
     **/
    public static final String ACCEBILL_TASK_PLEDGEBUSBILLACCBAL = "pledgeBusBillAccBal";
    /**
     * 生成余额票据业务类型：一般票据业务
     */
    public static final String BILLBAL_OP_BUSTYPE_DEFAULT = "defaultBill";
    /**
     * 生成余额票据业务类型：票据划拨下拨业务审批中
     */
    public static final String BILLBAL_OP_BUSTYPE_TRANSFERDOWN11 = "billTransferDown11";
    /**
     * 生成余额票据业务类型：票据划拨下拨业务打回
     */
    public static final String BILLBAL_OP_BUSTYPE_TRANSFERDOWN_1 = "billTransferDown-1";
    /**
     * 手动记账处理操作类型 -1记账打回
     */
    public static final String BILLACCBOOK_OPTYPE_REPULSE = "-1";
    /**
     * 手动记账处理操作类型 0复核中
     */
    public static final String BILLACCBOOK_OPTYPE_CHECK = "0";
    /**
     * 手动记账处理操作类型 1记账成功
     */
    public static final String BILLACCBOOK_OPTYPE_OK = "1";
    /**
     * 手动记账处理操作类型 2改账打回
     */
    public static final String BILLACCBOOK_OPTYPE_BACK = "2";
    /**
     * 手动记账处理操作状态 0新增记账
     */
    public static final String BILLACCBOOK_STATUS_ADD = "0";
    /**
     * 手动记账处理操作状态 1改账
     */
    public static final String BILLACCBOOK_STATUS_UPDATE = "1";
    /**
     * 手动记账处理操作状态 2复核
     */
    public static final String BILLACCBOOK_STATUS_CHECK = "2";
    /**
     * 之前已经有查询请求发送中，请等待结果。
     */
    public static final String AB_DRAFT_EXC_SEND_sWAIT_MSG = "ab.draft.exc.wait.msg";
    /**
     * 查询请求发送成功，请等待结果。
     */
    public static final String AB_DRAFT_EXC_SEND_SUCESS_MSG = "ab.draft.exc.sucess.msg";
    /**
     * 查询信息类型为空！
     */
    public static final String AB_DRAFT_EXC_MSGTYPE_MSG = "ab.draft.exc.msgtype.msg";
    /**
     * 查询信息类型为空！
     */
    public static final String AB_DRAFT_EXC_ERROR_MSG = "ab.draft.exc.error.msg";
    /**
     * 拒签业务自动化处理状态方式
     **/
    public static final String AB_REFUSE_FBS = "fbs";
    public static final String AB_REFUSE_RAT = "rat";
    /**
     * 电票自动签收定时任务类型：autoSignStrategy自动策略签收
     **/
    public static final String AB_TIMEDTASK_AUTO_SIGN_STRATEGY = "autoSignStrategy";
    /**
     * 电票自动签收定时任务类型：autoSignAccept 自动商票提示承兑出票人和承兑人一致自动签收
     **/
    public static final String AB_TIMEDTASK_AUTO_SIGN_ACCEPT = "autoSignAccept";
    /**支付信用状态*/
    /**
     * 指令表消息类型--支付信用
     */
    public final static String MSG_TYPE_PAYMENTCREDIT = "01";
    /**
     * 指令表消息类型--背面信息
     */
    public final static String MSG_TYPE_BACKINFO = "02";
    /**
     * 指令表消息类型--正面信息
     */
    public final static String MSG_TYPE_BILLINFO = "03";
    /**
     * 是否启用微信敞口
     */
    public final static String TABLE_BILL_EXPOSURE_ISENABLE = "table.bill.exposure.isenable";
    /**
     * 对比周期：1-较上月末、2-较上季度、3-较三个月前、4-较上年同期、5-较年初
     */
    public final static String COMPARED_CYCLE_1 = "1";
    public final static String COMPARED_CYCLE_2 = "2";
    public final static String COMPARED_CYCLE_3 = "3";
    public final static String COMPARED_CYCLE_4 = "4";
    public final static String COMPARED_CYCLE_5 = "5";
    ///** 票据池状态空或0池外1池内2入池在途3出池在途 */
    /**
     * 票据池状态空或0池外
     **/
    public static final String POOL_STATUS_0 = "0";
    /**
     * 票据池状态1池内
     **/
    public static final String POOL_STATUS_1 = "1";
    /**
     * 票据池状态2入池在途
     **/
    public static final String POOL_STATUS_2 = "2";
    /**
     * 票据池状态3出池在途
     **/
    public static final String POOL_STATUS_3 = "3";
    /*** 配置字段是否可以修改标记：1是 */
    public static final String AB_BILL_MDFSIGN_1 = "1";
    /*** 配置字段是否可以修改标记：0否 */
    public static final String AB_BILL_MDFSIGN_0 = "0";
    /*** 票据类型字段 billType */
    public static final String BILL_TYPE = "billType";
    /*** 流通介质字段 billFlag */
    public static final String BILL_FLAG = "billFlag";
    /**
     * 票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息
     **/
    public static final String ACCEBILL_TASK_SIGNBILLINFO = "autoQueryBillInfoBySign";
    /**
     * 票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理
     **/
    public static final String ACCEBILL_TASK_HOLDBILLINFO = "autoQueryBillInfoByHold";

    /** 已到期持有票据正面信息同步已结清自动托收申请**/
    public static final String ACCEBILL_TASK_HOLDBILLINFOTOPAY="autoQueryBillInfoToPayByHold";
    /**
     * 票据正面新背面信息指令定时任务：把所有待签收业务数据变为撤回状态重新同步待签收票据
     **/
    public static final String ACCEBILL_TASK_PENDINGTORECALL = "autoPendingToRecall";
    /**
     * 是否显示审批中数据 0表示不显示
     */
    public static final String ACCEBILL_IS_APPLY = "0";
    /*** 自动签收标记 1自动  */
    public static final String AB_BILL_ISAUTOSIGN_Y = "1";
    /*** 自动签收标记 0或空手动默认为0  */
    public static final String AB_BILL_ISAUTOSIGN_N = "0";
    /** 自动签收自动化配置**/
    /**
     * 组织标识字典（BILL_SIGN_STRATEGY_ORG_TAG）：01单位，02客户/供应商
     **/
    public static final String BILL_SIGN_STRATEGY_ORG_TAG_01 = "01";
    /**
     * 02客户/供应商
     **/
    public static final String BILL_SIGN_STRATEGY_ORG_TAG_02 = "02";
    /**
     * 数据类型字典 数据类型字典（BILL_SIGN_STRATEGY_DATA_TYPE）：01出票人，02承兑人，03申请人
     **/
    public static final String BILL_SIGN_STRATEGY_DATA_TYPE_01 = "01";
    public static final String BILL_SIGN_STRATEGY_DATA_TYPE_02 = "02";
    public static final String BILL_SIGN_STRATEGY_DATA_TYPE_03 = "03";
    //		包含票据池流水标识0或空非票据池流水1票据池流水2不控制
    /*** 包含票据池流水标识0或空非票据池流水*/
    public static final String poolSign_0 = "0";
    /*** 包含票据池流水标识1票据池流水*/
    public static final String poolSign_1 = "1";
    /*** 包含票据池流水标识2票据池流水和票据流水都包含*/
    public static final String poolSign_2 = "2";
//		银行借款return_code代码 3待发送、2失败、1队列处理中、0成功
    /*** 银行借款return_code代码 0成功*/
    public static final String BANK_RE_CODE0 = "0";
    /*** 银行借款return_code代码 1队列处理中*/
    public static final String BANK_RE_CODE1 = "1";
    /*** 银行借款return_code代码2失败*/
    public static final String BANK_RE_CODE2 = "2";
    /*** 银行借款return_code代码3待发送*/
    public static final String BANK_RE_CODE3 = "3";
    /*** 是否经办 1是*/
    public static final String IS_HANDLING1 = "1";
//
//
//	数值比较运算符：FUNDWARN_OPERATOR
//	序号	代码	名称
//	1	10	＞
//	2	11	＞＝
//	3	20	＜
//	4	21	＜＝
    /*** 预警数值比较运算符-＞*/
    public final static String BILL_SIGN_STRATEGY_OPERATOR_GT = "10";
    /*** 预警数值比较运算符-＞＝ */
    public final static String BILL_SIGN_STRATEGY_OPERATOR_GTEQ = "11";
    /*** 预警数值比较运算符-＜ */
    public final static String BILL_SIGN_STRATEGY_OPERATOR_LT = "20";
    /*** 预警数值比较运算符-＜＝ */
    public final static String BILL_SIGN_STRATEGY_OPERATOR_LTEQ = "21";
//
//	值关系运算符：FUNDWARN_NUM_OPERATOR
//	序号	代码	名称
//	1	01	或者
//	2	02	并且
    /*** 预警值关系运算符-或者 */
    public final static String BILL_SIGN_STRATEGY_OPERATOR_OR = "01";
    /*** 预警值关系运算符-并且 */
    public final static String BILL_SIGN_STRATEGY_OPERATOR_AND = "02";
    /*** 电票自动签收配置 */
    public final static String BILL_ACCEBILL_SIGN_QUERY = "bill:accebill:sign:query";
    /**
     * 是否付款新增数据1为是isNisPayAdd
     */
    public final static String BILL_ISNISPAYADD = "1";
    /**
     * 是否批次开票 1为是isBatchBill
     */
    public final static String BILL_ISBATCHBILL = "1";
    /**
     * 票据异常情况  1为未逾期，2为已逾期
     */
    public final static String BILL_ANOMALIES_NO = "1";
    public final static String BILL_ANOMALIES_YES = "2";
    /**
     * 承兑汇票参数配置查询资源
     */
    public final static String BILL_CONFIG_ACCEBILLCONFIG = "bill:config:accebillConfig";
    /**
     * 银行自动业务配置应用资源
     */
    public final static String BILL_CONFIG_ABBILLBANKAUTODTL = "bill:config:abbillbankautodtl";
    /**
     * 票据认领状态 待认领11
     */
    public final static String BILL_CLAIM_STATUS11 = "11";
    /**
     * 票据认领状态 打回-1
     */
    public final static String BILL_CLAIM_STATUS_1 = "-1";
    /**
     * 票据认领状态 已确认95
     */
    public final static String BILL_CLAIM_STATUS95 = "95";
    /**
     * 票据认领状态 待确认94
     */
    public final static String BILL_CLAIM_STATUS94 = "94";
    /**
     * 承兑汇票系统账号规则配置应用资源
     */
    public final static String BILL_CONFIG_ACCOUNTCONFIG = "bill:accebill:claim:accountConfig";
    /**
     * 认领参数 1自动认领
     */
    public static final String BILL_CONFIG_CLAIM1 = "1";
    /**
     * 认领参数 是否勾选 1勾选
     */
    public static final String BILL_CONFIG_CHECK1 = "1";
    /**
     * 票据兑付定时器类型参数：电票商票提示付款签收完成自动线下兑付确认
     **/
    public static final String ACCEBILL_TASK_AUTOPAY = "all";
    /**
     * 票据兑付定时器类型参数：电票到期线上兑付自动确认已付款
     **/
    public static final String ACCEBILL_TASK_AUTO_CONFRIMEPAY = "confrimePay";
    /**
     * 票据兑付定时器类型参数：兑付状态自动认领成功的票据，自动进行线下到期解付
     **/
    public static final String ACCEBILL_TASK_AUTO_CLAIMPAY = "claimPay";
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式4系统常量
     */
    public final static String SOURCE_TYPE1 = "1";
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式4系统常量
     */
    public final static String SOURCE_TYPE2 = "2";
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式4系统常量
     */
    public final static String SOURCE_TYPE3 = "3";
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式4系统常量
     */
    public final static String SOURCE_TYPE4 = "4";
    /**
     * 数据类型:money金额 date日期 text文本 num数值
     */
    public final static String DATA_TYPE_MONEY = "money";
    /**
     * 数据类型:money金额 date日期 text文本 num数值
     */
    public final static String DATA_TYPE_DATE = "date";
    /**
     * 数据类型: text文本
     */
    public final static String DATA_TYPE_TEXT = "text";
    /**
     * 数据类型:money金额 date日期 text文本 num数值
     */
    public final static String DATA_TYPE_NUM = "num";
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    public final static String COMPUTE_MODE_SUM = "sum";
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    public final static String COMPUTE_MODE_COUNT = "count";
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    public final static String COMPUTE_MODE_ADD = "add";
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    public final static String COMPUTE_MODE_SUB = "sub";
    /**
     * 筛选类型：sort排序 scope范围 val值
     */
    public final static String SCREEN_TYPE_SORT = "sort";
    /**
     * 筛选类型：sort排序 scope范围 val值
     */
    public final static String SCREEN_TYPE_SCOPE = "scope";
    /**
     * 筛选类型：sort排序 scope范围 val值
     */
    public final static String SCREEN_TYPE_VAL = "val";
    /**
     * 范围方式：series系列 valSco介于值之间
     */
    public final static String SCOPE_MODE_SERIES = "series";
    /**
     * 范围方式：series系列 valSco介于值之间
     */
    public final static String SCOPE_MODE_VALSCO = "valSco";
    /**
     * valid_sign is '0否；1是；2已删除；3已拆票'
     */
    public final static String VALID_SIGN_FALE = "0";
    public final static String VALID_SIGN_TRUE = "1";
    public final static String VALID_SIGN_DEL = "2";
    public final static String VALID_SIGN_SPLIT_TICKET = "3";
    /**
     * 新、旧、原数据标签
     */
    public final static String DATE_FLAG_NEW = "new";
    public final static String DATE_FLAG_OLD = "old";
    public final static String DATE_FLAG_RES = "res";
    /**
     * new_stm_issuofppr_ind is '出票标志。0：老系统出票,   1：新系统出票。默认值为0，不能为空
     */
    public final static String NEW_STM_ISSUOFPPR_IND_ZERO = "0";
    public final static String NEW_STM_ISSUOFPPR_IND_ONE = "1";
    /**
     * blgrdbagcrcl_prmt_ind is '是否允许分包。1 是  0 否，默认为否  新系统出票标志为"0"(老系统出 票)时，只能填写"0"(不可分包流 转)'
     */
    public final static String BLGRDBAGCRCL_PRMT_IND_ZERO = "0";
    public final static String BLGRDBAGCRCL_PRMT_IND_ONE = "1";
    /**
     * 子票区间开始初始序号
     */
    public final static String DRAFT_RANG_START = "000000000001";
    /**
     * 已出票
     */
    public static final String NEWBILL_STATUS_CS01 = "CS01";
    /**
     * 已承兑
     */
    public static final String NEWBILL_STATUS_CS02 = "CS02";
    /**
     * 已收票
     */
    public static final String NEWBILL_STATUS_CS03 = "CS03";
    /**
     * 已到期
     */
    public static final String NEWBILL_STATUS_CS04 = "CS04";
    /**
     * 已终止
     */
    public static final String NEWBILL_STATUS_CS05 = "CS05";
    /**
     * 已结清
     */
    public static final String NEWBILL_STATUS_CS06 = "CS06";
    /**
     * 待收票
     */
    public static final String CIRCULATE_TF0101 = "TF0101";
    /**
     * 可流通
     */
    public static final String CIRCULATE_TF0301 = "TF0301";
    /**
     * 已锁定
     */
    public static final String CIRCULATE_TF0302 = "TF0302";
    /**
     * 不可转让
     */
    public static final String CIRCULATE_TF0303 = "TF0303";
    /**
     * 已质押
     */
    public static final String CIRCULATE_TF0304 = "TF0304";
    /**
     * 待赎回
     */
    public static final String CIRCULATE_TF0305 = "TF0305";
    /**
     * 托收在途
     */
    public static final String CIRCULATE_TF0401 = "TF0401";
    /**
     * 追索中
     */
    public static final String CIRCULATE_TF0402 = "TF0402";
    /**
     * 已结束
     */
    public static final String CIRCULATE_TF0501 = "TF0501";
    /**
     * 出票信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC01 = "BC01";
    /**
     * 提示承兑申请
     */
    public static final String NEW_DRAFT_TYPE_BC02 = "BC02";
    /**
     * 提示收票申请
     */
    public static final String NEW_DRAFT_TYPE_BC03 = "BC03";
    /**
     * 保证申请
     */
    public static final String NEW_DRAFT_TYPE_BC04 = "BC04";
    /**
     * 保贴增信
     */
    public static final String NEW_DRAFT_TYPE_BC05 = "BC05";
    /**
     * 保兑增信
     */
    public static final String NEW_DRAFT_TYPE_BC06 = "BC06";
    /**
     * 转让背书申请
     */
    public static final String NEW_DRAFT_TYPE_BC07 = "BC07";
    /**
     * 买断式贴现
     */
    public static final String NEW_DRAFT_TYPE_BC08 = "BC08";
    /**
     * 回购式贴现
     */
    public static final String NEW_DRAFT_TYPE_BC09 = "BC09";
    /**
     * 贴现回购赎回申请
     */
    public static final String NEW_DRAFT_TYPE_BC10 = "BC10";
    /**
     * 质押申请
     */
    public static final String NEW_DRAFT_TYPE_BC11 = "BC11";
    /**
     * 质押解除申请
     */
    public static final String NEW_DRAFT_TYPE_BC12 = "BC12";
    /**
     * 提示付款
     */
    public static final String NEW_DRAFT_TYPE_BC13 = "BC13";
    /**
     * 拒付追索
     */
    public static final String NEW_DRAFT_TYPE_BC14 = "BC14";
    /**
     * 非拒付追索
     */
    public static final String NEW_DRAFT_TYPE_BC15 = "BC15";
    /**
     * 追索同意清偿申请
     */
    public static final String NEW_DRAFT_TYPE_BC16 = "BC16";
    /**
     * 冻结（解除）登记
     */
    public static final String NEW_DRAFT_TYPE_BC17 = "BC17";
    /**
     * 不得转让撤销
     */
    public static final String NEW_DRAFT_TYPE_BC18 = "BC18";
    /**
     * 撤票申请
     */
    public static final String NEW_DRAFT_TYPE_BC19 = "BC19";
    /**
     * 票据查验
     */
    public static final String NEW_DRAFT_TYPE_BC20 = "BC20";
    /**
     * 存托
     */
    public static final String NEW_DRAFT_TYPE_BC21 = "BC21";
    /**
     * 保证增信申请
     */
    public static final String NEW_DRAFT_TYPE_BC22 = "BC22";
    /**
     * ECDS电票迁移
     */
    public static final String NEW_DRAFT_TYPE_BC23 = "BC23";
    /**
     * 转贴现
     */
    public static final String NEW_DRAFT_TYPE_BC24 = "BC24";
    /**
     * 质押式回购首期
     */
    public static final String NEW_DRAFT_TYPE_BC25 = "BC25";
    /**
     * 质押式回购提前赎回
     */
    public static final String NEW_DRAFT_TYPE_BC26 = "BC26";
    /**
     * 质押式回购到期赎回
     */
    public static final String NEW_DRAFT_TYPE_BC27 = "BC27";
    /**
     * 质押式回购逾期赎回
     */
    public static final String NEW_DRAFT_TYPE_BC28 = "BC28";
    /**
     * 买断式回购首期
     */
    public static final String NEW_DRAFT_TYPE_BC29 = "BC29";
    /**
     * 买断式回购赎回
     */
    public static final String NEW_DRAFT_TYPE_BC30 = "BC30";
    /**
     * 再贴现质押式回购首期
     */
    public static final String NEW_DRAFT_TYPE_BC31 = "BC31";
    /**
     * 再贴现质押式回购提前赎回
     */
    public static final String NEW_DRAFT_TYPE_BC32 = "BC32";
    /**
     * 再贴现质押式回购到期赎回
     */
    public static final String NEW_DRAFT_TYPE_BC33 = "BC33";
    /**
     * 再贴现质押式回购逾期赎回
     */
    public static final String NEW_DRAFT_TYPE_BC34 = "BC34";
    /**
     * 非交易过户
     */
    public static final String NEW_DRAFT_TYPE_BC35 = "BC35";
    /**
     * 承兑信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC36 = "BC36";
    /**
     * 贴现信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC37 = "BC37";
    /**
     * 保证信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC38 = "BC38";
    /**
     * 质押信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC39 = "BC39";
    /**
     * 质押解除信息登记
     */
    public static final String NEW_DRAFT_TYPE_BC40 = "BC40";
    /**
     * 止付（解除）登记
     */
    public static final String NEW_DRAFT_TYPE_BC41 = "BC41";
    /**
     * 转托管（票据账户）
     */
    public static final String NEW_DRAFT_TYPE_BC42 = "BC42";
    /**
     * 再贴现买断
     */
    public static final String NEW_DRAFT_TYPE_BC43 = "BC43";
    /** 提示承兑 */
    public static final String NEW_DRAFT_BUS_TYPE_ET01 = "ET01";
    /** 保证 */
    public static final String NEW_DRAFT_BUS_TYPE_ET02 = "ET02";
    /** 质押 */
    public static final String NEW_DRAFT_BUS_TYPE_ET03 = "ET03";
    /** 解质押 */
    public static final String NEW_DRAFT_BUS_TYPE_ET04 = "ET04";
    /** 背书  */
    public static final String NEW_DRAFT_BUS_TYPE_ET05 = "ET05";
    /** 止付/冻结 */
    public static final String NEW_DRAFT_BUS_TYPE_ET06 = "ET06";
    /** 解除 止付/冻结 */
    public static final String NEW_DRAFT_BUS_TYPE_ET07 = "ET07";
    /** 保证增信 */
    public static final String NEW_DRAFT_BUS_TYPE_ET08 = "ET08";
    /** 提示付款 */
    public static final String NEW_DRAFT_BUS_TYPE_ET09 = "ET09";
    /** 追索 */
    public static final String NEW_DRAFT_BUS_TYPE_ET10 = "ET10";
    /** 电票转入 */
    public static final String NEW_DRAFT_BUS_TYPE_ET11 = "ET11";
    /** 转托管 */
    public static final String NEW_DRAFT_BUS_TYPE_ET12 = "ET12";
    /** 买断式回购 */
    public static final String NEW_DRAFT_BUS_TYPE_ET13 = "ET13";
    /** 回购式贴现 */
    public static final String NEW_DRAFT_BUS_TYPE_ET14 = "ET14";
    /** 回购式贴现赎回 */
    public static final String NEW_DRAFT_BUS_TYPE_ET15 = "ET15";
    /** */
    //public static final String NEW_DRAFT_BUS_TYPE_ET16 = "ET16";
    /**买断式贴现赎回 */
    public static final String NEW_DRAFT_BUS_TYPE_ET17 = "ET17";
    /**权属初始登记 */
    public static final String NEW_DRAFT_BUS_TYPE_ET18 = "ET18";
    /** 质押式回购 */
    public static final String NEW_DRAFT_BUS_TYPE_ET19 = "ET19";
    /**质押式回购赎回 */
    public static final String NEW_DRAFT_BUS_TYPE_ET20 = "ET20";
    /**再贴现质押式回购 */
    public static final String NEW_DRAFT_BUS_TYPE_ET21 = "ET21";
    /**再贴现质押式回购赎回 */
    public static final String NEW_DRAFT_BUS_TYPE_ET22 = "ET22";
    /**
     * circulation_mode is '流通方式 0-线下 1-线上'
     */
    public static final String CIRCULATION_MODE_0 = "0";
    public static final String CIRCULATION_MODE_1 = "1";
    /**
     * 贴现办理业务是否完成 0-否 1-是
     */
    public static final String BUSINESS_IS_OVER_NO = "0";
    public static final String BUSINESS_IS_OVER_YES = "1";
    /**
     * 签收表状态 - 已拆分
     */
    public static final Integer RECIVE_STATUE_100F = -100;
    /**
     * 不可转让撤销打回
     */
    public static final String RECALL_TYPE_024 = "024";
    /**
     * 签收类型
     * 002_提示承兑，003_提示收票，017_提示保证，010_背书申请，012_贴现赎回签收，018_质押申请，019_质押解除，020_提示付款，021_逾期付款，023_追索同意清偿，024_不可转让撤销
     */
    public static final String BILL_SIGNERTYPE_002 = "002";
    public static final String BILL_SIGNERTYPE_003 = "003";
    public static final String BILL_SIGNERTYPE_017 = "017";
    public static final String BILL_SIGNERTYPE_010 = "010";
    public static final String BILL_SIGNERTYPE_012 = "012";
    public static final String BILL_SIGNERTYPE_018 = "018";
    public static final String BILL_SIGNERTYPE_019 = "019";
    public static final String BILL_SIGNERTYPE_020 = "020";
    public static final String BILL_SIGNERTYPE_021 = "021";
    public static final String BILL_SIGNERTYPE_023 = "023";
    public static final String BILL_SIGNERTYPE_024 = "024";
    /**
     * 不可转让撤销申请
     */
    public final static String BRIEF_STATUS_024 = "024";
    /**
     * 不可转让撤销签收
     */
    public final static String BRIEF_STATUS_9024 = "9024";

    /**
     * 票据是否允许分包流转
     */
    public final static String AB_BILL_BLGRDBAGCRCL_PRMT_IND = "AB_BILL_BLGRDBAGCRCL_PRMT_IND";

    /**
     * 回复标志 SU00同意签收SU01拒绝签收
     */
    public final static String AB_BILL_SGNUPMK = "AB_BILL_SGNUPMK";

    /**
     * 拒付理由代码
     */
    public final static String AB_BILL_DSHNRCD = "AB_BILL_DSHNRCD";

    /**
     * 处理状态：18待签收19签成功-2打回-3撤回
     */
    public final static String AB_BILL_RECEIVEBIZ_STATUS = "AB_BILL_RECEIVEBIZ_STATUS";

    /**
     * 报文编号：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款022追索通知023追索同意清偿
     */
    public final static String AB_BILL_SIGNERTYPE = "AB_BILL_SIGNERTYPE";

    /**
     * 到期无条件支付承诺 承兑回复必填 CC00含委托/承诺兑付
     */
    public final static String AB_BILL_UCONDLPRMSMRK = "AB_BILL_UCONDLPRMSMRK";

    /**
     *  线上清算标记： SM00 开启线上清算；SM01 关闭线上清算
     */
    public static final String AB_STTLMMK_DICT = "AB_STTLMMK";
    /**
     * 不得转让标记：EM00 可再转让；EM01 不得转让
     */
    public static final String AB_BANEM_DICT = "AB_BANEM";

    /**
     * 内部业务标志：0  否；1 是
     */
    public static final String ACCEBILL_ABRECEIVE_INSIDESING_DICT = "ACCEBILL_ABRECEIVE_INSIDESING";

    /**
     * 出票标志：0  否；1 是
     */
    public static final String AB_BILL_NEW_STM_ISSUOFPPR_IND_DICT = "AB_BILL_NEW_STM_ISSUOFPPR_IND";

    /**
     * 是否允许分包
     */
    public static final String AB_BILL_BLGRDBAGCRCL_PRMT_IND_DICT = "AB_BILL_BLGRDBAGCRCL_PRMT_IND";

    /**
     * 不得转让标记
     */
    public final static String BILL_BANKENDRSMTMK_DICT = "AB_BANEM";


    public static Map<String,String> BUS_TYPE_OLD_TO_NEW=new HashMap<String,String>();
    public static Map<String,String> BUS_TYPE_NEW_TO_OLD=new HashMap<String,String>();

    static {
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_002,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_003,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET11);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_010,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET05);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_011,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET13);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_012,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET15);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_017,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET02);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_018,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET03);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_019,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET04);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_020,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09);
        BUS_TYPE_OLD_TO_NEW.put(AccebillConstant.BRIEF_STATUS_021,AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09);
        for(String key:BUS_TYPE_OLD_TO_NEW.keySet()){
            BUS_TYPE_NEW_TO_OLD.put(BUS_TYPE_OLD_TO_NEW.get(key),key);
            BUS_TYPE_NEW_TO_OLD.put(AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09,AccebillConstant.BRIEF_STATUS_020);
        }
    }

    /**
     * 付款办理开票标志（新增）
     */
    public static final String PAY_MONEY_OPEN_BILL_FLG = "2";

    /**
     * 特定银行保证金选择标识 0-否 1-招行 2-民生
     */
    public static final String CMB_BAIL_FLAG = "1";

    public static final String CMBC_BAIL_FLAG = "2";

    /**
     * 票据保证金利息维护记录来源 0-自动生成 1-手动增加
     */
    public static final String AUTO_GENERATION = "0";
    public static final String MANUAL_INCREMENT = "1";

    /**
     * 票据保证金利息状态 单据状态 0-暂存,1-已确认
     */
    public static final String BAIL_INTEREST_INFO_STATUS_0 = "0";
    public static final String BAIL_INTEREST_INFO_STATUS_1 = "1";

    /**
     * 票据保证金利息推送状态 推送状态 0-未推送,1-待推送,2-已推送
     */
    public static final String BAIL_INTEREST_INFO_PUSH_STATUS_0 = "0";
    public static final String BAIL_INTEREST_INFO_PUSH_STATUS_1 = "1";
    public static final String BAIL_INTEREST_INFO_PUSH_STATUS_2 = "2";

    /**
     * 是否汇总认领流水 0-否 1-是
     */
    public static final String IS_SUMMARY_CLAIM_BILL = "1";

    /**
     * 票据银行余额不足时间配置状态 状态 0-禁用,1-正常
     */
    public static final String INSUFFICIENT_FUNDS_TIME_CONFIG_STATUS_0 = "0";
    public static final String INSUFFICIENT_FUNDS_TIME_CONFIG_STATUS_1 = "1";

    /**
     * 是否删除 1-是
     */
    public static final String IS_DELETE = "1";

    /**
     * 承兑到期和借款汇总余额不足提醒tag
     */
    public static final String INSUFFICIENT_FUNDS_REMIND_TAG = "bill:accebill:accInsufficientFundsRemind";

    /**
     * 银行类型 Ms-民生银行 Zh-招行银行
     */
    public static final String MS_BANK = "Ms";
    public static final String ZH_BANK = "Zh";

    /**
     * 保证金种类
     */
    public static final String TIMTYPE_HQ = "活期";
    public static final String TIMTYPE_DQ = "定期";

    public static final String TIMTYPE_HQ_0 = "0";
    public static final String TIMTYPE_DQ_1 = "1";

}
