package com.bytter.biz.payment.api.constant;
/**
 * 外汇管理常量类
 * @author huangx
 * @since 2019-07-15 15:20:33
 */
public class ForeignConstant {
    /*** 外币汇款参数配置：fex-外币汇款 */
    public static final String FEX_CONFIG_TYPE = "fex";
    /*** 外币汇款参数配置：budgetSel-是否挂接资金计划  */
    public static final String FEX_CONFIG_BUDGETSEL = "budgetSel";
    /*** 外币汇款参数配置：payItem-默认走的计划科目  */
    public static final String FEX_CONFIG_PAYITEM = "payItem";
    /*** 外币汇款参数配置：bisExcAutoAccount-指令发送模式 */
    public static final String FEX_CONFIG_BISEXC_AUTOACCOUNT = "bisExcAutoAccount";
    /*** 外币汇款参数配置：bisExcAutoAccount-指令发送模式 */
    public static final String FEX_CONFIG_ISSKIPSETTLE = "isSkipSettle";
    /** 支付结算参数配置：autoPayerAccNum-付款支付超时预警天数  */
    public static final String FEX_CONFIG_PAYOVERTIME = "payOverTime";

    /** 外币汇款参数配置：tranRateTypeDefault-交易的默认外汇牌价类型  */
    public static final String FEX_CONFIG_TRANRATETYPEDEFAULT = "tranRateTypeDefault";
    /** 外币汇款参数配置：defTranRateType-交易的默认外汇牌价类型  */
    public static final String FEX_CONFIG_DEFTRANRATETYPE = "valuationRateTypeDefault";
    /** 外币汇款参数配置：valuationExchangeRate-估值汇率  */
    public static final String FEX_CONFIG_VALUATIONEXCHANGERATE = "valuationExchangeRate";
    /** 外币汇款参数配置：defProfitAndLossCur-默认损益币种  */
    public static final String FEX_CONFIG_DEFPROFITANDLOSSCUR= "profitLossCur";
    /** 外币汇款参数配置：defProfitAndLossCur-外汇即期是否计算损益  */
    public static final String FEX_CONFIG_ISPROFITANDLOSS= "calculatedProfitLoss";

    /**默认损益币种 1-标准币种 2-报价币种*/
    public static final String FEX_CONFIG_PROFITLOSSCURTYPE_BASE = "1";
    public static final String FEX_CONFIG_PROFITLOSSCURTYPE_REFER = "2";

    /*** 外币汇款业务处理状态：0-暂存  */
    public final static Integer STATUE_TEMP = 0;
    /*** 外币汇款业务处理状态：1-打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 外币汇款业务处理状态：-2-删除  */
    public final static Integer STATUE_DEL = -2;
    /*** 外币汇款业务处理状态：11-待审批  */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 外币汇款业务处理状态：95-审批通过  */
    public final static Integer STATUE_CHECK = 95;
    /*** 外币汇款业务处理状态：96-已失效  */
    public final static Integer STATUE_INVALID = 96;
    /*** 外币汇款业务处理状态：97-已确认均价  */
    public final static Integer STATUE_ENTERAVG = 97;
    /*** 外币汇款业务处理状态：98-已执行  */
    public final static Integer STATUE_EXERCISE = 98;
    /*** 外币汇款业务处理状态：99-已敲出  */
    public final static Integer STATUE_KNOCKOUT = 99;
    /*** 外币汇款业务处理状态：100-已敲入  */
    public final static Integer STATUE_KNOCKIN = 100;
    /*** 外币汇款业务处理状态：101-已违约  */
    public final static Integer STATUE_DEFAULT = 101;

    /*** 外币汇款业务处理状态：110-已平仓  */
    public final static Integer STATUE_CLOSE_POSITION = 110;
    /*** 外币汇款业务处理状态：111-已放弃行权  */
    public final static Integer STATUE_WAIVER = 111;
    /*** 外币汇款业务处理状态：112-提前交割  */
    public final static Integer STATUE_EARLY_DELIVERY = 112;
    /*** 外币汇款业务处理状态：113-提前  */
    public final static Integer STATUE_EARLY = 113;
    /*** 外币汇款业务处理状态：114-展期  */
    public final static Integer STATUE_DEFERRED = 114;
    /*** 外币汇款业务处理状态：115-到期交割  */
    public final static Integer STATUE_DELIVERY = 115;
    /*** 外币汇款业务处理状态：116-汇率确认  */
    public final static Integer STATUE_RATE_CONFIRM = 116;
    /*** 外币汇款业务处理状态：117-待汇率确认  */
    public final static Integer STATUE_TOBE_RATE_CONFIRM = 117;
    /*** 外币汇款业务处理状态：120-结束  */
    public final static Integer STATUE_END = 120;
    /**
     * 续存
     */
    public final static Integer ONGOING = 130;

    /*** 汇款类型：0-境内汇款  */
    public final static String REMIT_TYPE_0 = "0";
    /*** 汇款类型：1-境外汇款  */
    public final static String REMIT_TYPE_1 = "1";
    /*** 汇款类型：2-境外账户汇款  */
    public final static String REMIT_TYPE_2 = "2";
    /*** 发电等级：0-普通 Normal */
    public final static String QUICK_SIGN_0 = "0";
    /*** 发电等级：1-加急 Urgent  */
    public final static String QUICK_SIGN_1 = "1";
    /*** 付汇性质：1-保税区  */
    public final static String REMIT_NATURE_1 = "1";
    /*** 付汇性质：2-出口加工区  */
    public final static String REMIT_NATURE_2 = "2";
    /*** 付汇性质：3-钻石交易所  */
    public final static String REMIT_NATURE_3 = "3";
    /*** 付汇性质：4-其他特殊经济区域  */
    public final static String REMIT_NATURE_4 = "4";
    /*** 付汇性质：5-深加工结转  */
    public final static String REMIT_NATURE_5 = "5";
    /*** 付汇性质：6-离岸账户  */
    public final static String REMIT_NATURE_6 = "6";
    /*** 付汇性质：7-其他  */
    public final static String REMIT_NATURE_7 = "7";
    /*** 汇款方式：0-电汇 */
    public final static String REMIT_METHOD_0 = "0";
    /*** 汇款方式：1-票汇  */
    public final static String REMIT_METHOD_1 = "1";
    /*** 国内外费用承担标志：1-汇款人 OUR  */
    public final static String COST_SIGN_1 = "1";
    /*** 国内外费用承担标志：2-收款人 BEN  */
    public final static String COST_SIGN_2 = "2";
    /*** 国内外费用承担标志：3-共同 SHA  */
    public final static String COST_SIGN_3 = "3";
    /*** 付款类型：1-预付货款 */
    public final static String PAY_TYPE_1 = "1";
    /*** 付款类型：2-货到付款  */
    public final static String PAY_TYPE_2 = "2";
    /*** 付款类型：3-退款  */
    public final static String PAY_TYPE_3 = "3";
    /*** 付款类型：4-其他  */
    public final static String PAY_TYPE_4 = "4";
    /*** 费用交付方式：1-现金  */
    public final static String COST_MODE_1 = "1";
    /*** 费用交付方式：2-支票  */
    public final static String COST_MODE_2 = "2";
    /*** 费用交付方式：3-账户  */
    public final static String COST_MODE_3 = "3";
    /*** 钞汇代码：1-钞  */
    public final static String BANK_CUR_CODE_1 = "1";
    /*** 钞汇代码：2-汇  */
    public final static String BANK_CUR_CODE_2 = "2";
    /**
     * 交易对手组
     */
    public final static String FEX_COTP_LEVEL_0 = "0";
    /**
     * 交易对手
     */
    public final static String FEX_COTP_LEVEL_1 = "1";
    /** 产品层级：0-产品组  */
    public final static String PRO_LEVEL_0 = "0";
    /** 产品层级：1-产品  */
    public final static String PRO_LEVEL_1 = "1";
    /** 交易类型：1-外汇交易  */
    public final static String TRAN_TYPE_1 = "1";
    /** 交易类型：2-外汇期权交易  */
    public final static String TRAN_TYPE_2 = "2";
    /** 交易类型：3-调拨交易  */
    public final static String TRAN_TYPE_3 = "3";
    /** 交易类型：4-资金收付交易  */
    public final static String TRAN_TYPE_4 = "4";
    /** 资金收付交易单据最大合并笔数 */
    public final static int FEX_TRAN_REQ_MERGE_MAX_COUNT = 100;
    /** 交易类型：5-货币市场交易  */
    public final static String TRAN_TYPE_5 = "5";
    /** 交易类型：6-债券交易  */
    public final static String TRAN_TYPE_6 = "6";
    /** 交易类型：7-外汇即期交易  */
    public final static String TRAN_TYPE_7 = "7";
    /** 交易类型：8-外汇远期交易  */
    public final static String TRAN_TYPE_8 = "8";
    /** 交易类型：9-外汇掉期交易  */
    public final static String TRAN_TYPE_9 = "9";
    /** 交易对手类别：01-内部银行  */
    public final static String FEX_COTP_TYPE_1 = "01";
    /** 交易对手类别：02-外部交易方  */
    public final static String FEX_COTP_TYPE_2 = "02";
    /** 交易对手类别：03-托管人  */
    public final static String FEX_COTP_TYPE_3 = "03";
    /** 交易对手类别：04-结算银行  */
    public final static String FEX_COTP_TYPE_4 = "04";
    /** 交易对手类别：05-外部银行  */
    public final static String FEX_COTP_TYPE_5 = "05";
    /** 交易对手类别：06-交易所  */
    public final static String FEX_COTP_TYPE_6 = "06";
    /** 交易对手类别：07-经纪人  */
    public final static String FEX_COTP_TYPE_7 = "07";
    /** 交易对手类别：08-基金管理人  */
    public final static String FEX_COTP_TYPE_8 = "08";
    /** 交易对手类别：09-发行或支行代理人  */
    public final static String FEX_COTP_TYPE_9 = "09";
    /** 交易对手类别：10-借记防/贷记方  */
    public final static String FEX_COTP_TYPE_10 = "10";
    /** 交易对手类别：11-清算经济人  */
    public final static String FEX_COTP_TYPE_11 = "11";
    /** 交易对手类别：12-清算所  */
    public final static String FEX_COTP_TYPE_12 = "12";
    /** 交易对手类别：13-内部交易方  */
    public final static String FEX_COTP_TYPE_13 = "13";
    /** 交易对手类别：14-发行人/承兑人  */
    public final static String FEX_COTP_TYPE_14 = "14";
    /** 交易对手类别：15-税务机关  */
    public final static String FEX_COTP_TYPE_15 = "15";
    /*** 是否主账户 0-否  */
    public final static String FEX_PARENT_SIGN_0 = "0";
    /*** 是否主账户 1-是  */
    public final static String FEX_PARENT_SIGN_1 = "1";
    /*** 限额设置 1-外债余额  */
    public final static String FEX_LIMIT_TYPE_1 = "1";
    /*** 限额设置 2-对外放款  */
    public final static String FEX_LIMIT_TYPE_2 = "2";
    /*** 限额设置 3-净流入额  */
    public final static String FEX_LIMIT_TYPE_3 = "3";
    /*** 资金池标志 : 0-资金池主账户 */
    public final static String FEX_POOL_SIGN_0 = "0";
    /*** 资金池标志 : 1-资金池主体  */
    public final static String FEX_POOL_SIGN_1 = "1";
    /**外汇默认银行账户业务类型：主体单位*/
    public final static String FEX_BUS_TYPE_CORP = "0";
    /**外汇默认银行账户业务类型：交易对手*/
    public final static String FEX_BUS_TYPE_COTP = "1";
    /*** 交易标志 : 0-借入外债交易 */
    public final static String FEX_TRAN_SIGN_0 = "0";
    /*** 交易标志 : 1-对外放款交易  */
    public final static String FEX_TRAN_SIGN_1 = "1";


    /**
     * 交易汇率类型 :挂牌汇率
     */
    public final static String FEX_TRAN_RATE_TYPE_LISTED = "1";

    /**
     * 交易汇率类型 :合同汇率
     */
    public final static String FEX_TRAN_RATE_TYPE_CONTRACT = "2";

    /** 操作类型：1-提前交割或展期  */
    public final static String OP_TYPE_1 = "1";
    /** 操作类型：2-违约  */
    public final static String OP_TYPE_2 = "2";
    /** 操作类型：3-NDF交割  */
    public final static String OP_TYPE_3 = "3";
    /** 操作类型：4-汇率确认  */
    public final static String OP_TYPE_4 = "4";
    /** 操作类型：1-失效  */
    public final static String OPTION_OP_TYPE_1 = "1";
    /** 操作类型：2-已确认均价  */
    public final static String OPTION_OP_TYPE_2 = "2";
    /** 操作类型：3-行权  */
    public final static String OPTION_OP_TYPE_3 = "3";
    /** 操作类型：4-敲出  */
    public final static String OPTION_OP_TYPE_4 = "4";
    /** 操作类型：5-敲入  */
    public final static String OPTION_OP_TYPE_5 = "5";
    /** 掉期交易：1-近端  */
    public final static String SWAP_TYPE_1 = "1";
    /** 掉期交易：2-远端  */
    public final static String SWAP_TYPE_2 = "2";
    /** 外汇业务类型：1-结汇  */
    public final static String FEX_BUS_TYPE_1 = "1";
    /** 外汇业务类型：2-售汇  */
    public final static String FEX_BUS_TYPE_2 = "2";
    /** 外汇业务类型：3-外汇买卖  */
    public final static String FEX_BUS_TYPE_3 = "3";
    /** 外汇交易类型：0-即期  */
    public final static String FEX_TRAN_TYPE_0 = "0";
    /** 外汇交易类型：1-远期  */
    public final static String FEX_TRAN_TYPE_1 = "1";

    /**
     * 买卖方向 1-买入
     */
    public final static String FEX_TRAN_BUSDIRECTION_BUY = "1";
    /**
     * 买卖方向 2-卖出
     */
    public final static String FEX_TRAN_BUSDIRECTION_SELL = "2";

    /**
     * 涨跌方向 0-看涨
     */
    public final static String FEX_TRAN_DIRECTION_UP = "0";
    /**
     * 涨跌方向 1-看跌
     */
    public final static String FEX_TRAN_DIRECTION_DOWN = "1";
    /** 结算类型：1-全额  */
    public final static String SETTLE_TYPE_1 = "1";
    /** 结算类型：2-净额  */
    public final static String SETTLE_TYPE_2 = "2";
    /** 现金流类型：0-本金  */
    public final static String CASH_FLOW_TYPE_0 = "0";
    /** 现金流类型：1-利息  */
    public final static String CASH_FLOW_TYPE_1 = "1";
    /** 现金流类型：2-违约金  */
    public final static String CASH_FLOW_TYPE_2 = "2";
    /** 现金流类型：3-期权费  */
    public final static String CASH_FLOW_TYPE_3 = "3";
    /** 现金流类型：4-退款  */
    public final static String CASH_FLOW_TYPE_4 = "4";
    /** 支付状态：0-待付款  */
    public final static String PAY_STATUS_0 = "0";
    /** 支付状态：1-已付款  */
    public final static String PAY_STATUS_1 = "1";

    /** 支付状态：2-已退款  */
    public final static String PAY_STATUS_REFUNDED = "-1";
    /**业务类型：0主体单位*/
    public final static String BUS_TYPE_0 = "0";
    /**业务类型：1交易对手*/
    public final static String BUS_TYPE_1 = "1";
    public final static String FROM_FOREIGN = "1";//外币汇款
    public final static String FROM_TRAN = "2";//外汇交易
    /**交割方向：1-买入*/
    public final static String DELIVERY_SIGN_1 = "1";
    /**交割方向：2-卖出*/
    public final static String DELIVERY_SIGN_2 = "2";
    /**结算方式：0-现金*/
    public final static String TRAN_SETTLE_METHOD_0 = "0";
    /**结算方式：0-即期交易*/
    public final static String TRAN_SETTLE_METHOD_1 = "1";
    /*** 固定金额：1-买入金额  */
    public final static String FIXED_MONEY_1 = "1";
    /*** 固定金额：2-卖出金额  */
    public final static String FIXED_MONEY_2 = "2";
    /** 银行流水认领功能挂接标记，未认领=0*/
    public final static String BANK_FLOW_SIGN_0 = "0";
    /** 银行流水认领功能挂接标记，认领=1*/
    public final static String BANK_FLOW_SIGN_1 = "1";
    /*** 外汇牌价接口类别：1-银企直联  */
    public final static String RATE_INTERFACE_TYPE_1 = "1";
    /*** 外汇牌价接口类别：2-网络机器人  */
    public final static String RATE_INTERFACE_TYPE_2 = "2";
    /**
     * 根据指令来源获取业务service
     * @param voucherFrom
     * @return  业务service
     */
    public static String getServiceNameByVoucherFrom(String voucherFrom){
        if(FROM_FOREIGN.equals(voucherFrom)){
            return "fexForeignReqServiceImpl";
        }else if(FROM_TRAN.equals(voucherFrom)){
            return "fexCashFlowServiceImpl";
        }
        return "";
    }
    /*** 收支方向：0-收汇  */
    public final static String CD_SIGN_0 = "收汇";
    /*** 收支方向：1-付汇  */
    public final static String CD_SIGN_1 = "付汇";


    /**
     * 外汇指令来源：REQ-外汇交易，CASH-现金
     */
    public final static String EXC_SOURCE_REQ = "REQ";//外汇交易
    public final static String EXC_SOURCE_CASH_FLOW = "CASH_FLOW";//现金

    /*** 单据来源：0-外币汇款申请  */
    public final static String BILLFROM_0 = "0";
    /*** 单据来源：1-ERP接口  */
    public final static String BILLFROM_1 = "1";

    /*** 初始化产品ID：香港FPS  */
    public final static String FEX_PRODUCT_FPS= "53f8408e666b42b9abbe2dcf8372bc76";
    /*** 初始化产品ID：PAYPAL  */
    public final static String FEX_PRODUCT_PAYPAL= "4c182f17455f4614a79d66bdfd755c8d";

    /*** 勾兑规则：1-金额  */
    public final static String REFUND_CHECK_RULE_1 = "1";
    /*** 勾兑规则：2-本方账号  */
    public final static String REFUND_CHECK_RULE_2 = "2";
    /*** 勾兑规则：3-对方账号  */
    public final static String REFUND_CHECK_RULE_3 = "3";
    /*** 勾兑规则：4-交易日期  */
    public final static String REFUND_CHECK_RULE_4 = "4";

    /*** 勾兑处理：1-不处理  */
    public final static String REFUND_CHECK_HANDLE_1 = "1";
    /*** 勾兑处理：2-生成新结算执行单  */
    public final static String REFUND_CHECK_HANDLE_2 = "2";
    /*** 勾兑处理：3-生成新交易单  */
    public final static String REFUND_CHECK_HANDLE_3 = "3";

    /*** 单据来源：0-手工新增  */
    public final static String BILL_FROM_0 = "0";
    /*** 单据来源：1-交易需求  */
    public final static String BILL_FROM_1 = "1";
    /*** 单据来源：2-掉期生成  */
    public final static String BILL_FROM_2 = "2";
    /*** 单据来源：3-交易生成  */
    public final static String BILL_FROM_3 = "3";
    /*** 单据来源：4-退款勾对生成  */
    public final static String BILL_FROM_4 = "4";

    /*** 单据来源：10-单据拆分  */
    public final static String BILL_FROM_10 = "10";

    /*** 单据来源：15-单据合并  */
    public final static String BILL_FROM_15= "15";



    /** 生命周期 操作类型：1-失效  */
    public final static String CYCLE_OP_TYPE_1 = "1";
    /** 生命周期 操作类型：2-录入均价  */
    public final static String CYCLE_OP_TYPE_2 = "2";
    /** 生命周期 操作类型：3-行权  */
    public final static String CYCLE_OP_TYPE_3 = "3";
    /** 生命周期 操作类型：4-敲出  */
    public final static String CYCLE_OP_TYPE_4 = "4";
    /** 生命周期 操作类型：5-敲入  */
    public final static String CYCLE_OP_TYPE_5 = "5";
    /** 生命周期 操作类型：6-平仓  */
    public final static String CYCLE_OP_TYPE_6 = "6";
    /** 生命周期 操作类型：7-放弃行权  */
    public final static String CYCLE_OP_TYPE_7 = "7";
    /** 生命周期 操作类型：8-提前交割  */
    public final static String CYCLE_OP_TYPE_8 = "8";
    /** 生命周期 操作类型：9-提前  */
    public final static String CYCLE_OP_TYPE_9 = "9";
    /** 生命周期 操作类型：10-展期  */
    public final static String CYCLE_OP_TYPE_10 = "10";
    /** 生命周期 操作类型：11-到期交割  */
    public final static String CYCLE_OP_TYPE_11 = "11";
    /** 生命周期 操作类型：12-汇率确认  */
    public final static String CYCLE_OP_TYPE_12 = "12";

    /**
     * 显示折本位币信息
     */
    public final static String CYCLE_SHOW_CONVERT = "Y";
    /**
     * 影藏折本位币信息
     */
    public final static String CYCLE_HIDE_CONVERT = "N";

    /**
     * 外汇期权品种2 1-亚式期权、2-单障碍期权、3-双障碍期权
     */
    public final static String FEX_OPTION2_1 = "1";

    public final static String FEX_OPTION2_2 = "2";

    public final static String FEX_OPTION2_3 = "3";

    /**外汇期权品种1 0-普通期权、1-奇异期权*/
    public final static String FEX_OPTION1_0 = "0";

    public final static String FEX_OPTION1_1 = "1";

    /**外汇期权品种3 1-上涨生效单障碍期权(Up & In)、2-上涨失效单障碍期权(Up & Out)、3-下跌失效单障碍期权(Down & Out)、4-下跌生效单障碍期权(Down & In)、5-单触碰生效双障碍期权(Double In)、6-单触碰失效双障碍期权(Double Out)*/
    public final static String FEX_OPTION3_1 = "1";
    public final static String FEX_OPTION3_2 = "2";
    public final static String FEX_OPTION3_3 = "3";
    public final static String FEX_OPTION3_4 = "4";
    public final static String FEX_OPTION3_5 = "5";
    public final static String FEX_OPTION3_6 = "6";



}
