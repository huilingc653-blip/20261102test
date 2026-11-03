package com.bytter.system.api.constant;
import java.util.HashMap;
import java.util.Map;
/**
 * 基础平台常量信息
 * Created by zhengg on 2017/11/16
 */
public class SysConfigConstant {
    /**
     * 系统模块代码
     *
     * @author zhengg
     */
    public class Module {
        /**
         * 基础平台
         */
        public static final String SYS = "sys";
        /**
         * 内部结算模块
         */
        public static final String NIS = "nis";
        /**
         * 承兑汇票模块
         */
        public static final String ACCEBILL = "accebill";
        /**
         * 票据票据池
         */
        public static final String BILLPOOL = "billPool";
        /**
         * 空白凭证模块
         */
        public static final String CASHVOUCHER = "cashVoucher";
        /**
         * 风险预警模块
         */
        public static final String FUNDWARN = "fundWarn";
        /**
         * 第三方支付模块
         */
        public static final String TTPAY = "ttpay";
        /**
         * 银行账户管理模块
         */
        public static final String BANKACC = "bankacc";
        /**
         * 内部账户管理模块
         */
        public static final String FCS = "fcs";
        /**
         * 核算管理模块
         */
        public static final String FCL = "fcl";
        /**
         * 信贷融资模块
         */
        public static final String CMS = "cms";
        /**
         * 授信管理模块
         */
        public static final String RAT = "rat";
        /**
         * 担保管理模块
         */
        public static final String GUA = "gua";
        /**
         * 资金计划模块
         */
        public static final String FBS = "fbs";
        /**
         * 国际结算模块
         */
        public static final String INTERNATIONAL = "international";
        /**
         * 理财管理模块
         */
        public static final String CHOWMATISTIC = "chowmatistic";
        /**
         * 统计报表
         */
        public static final String BI = "bi";
        /**
         * 定价管理模块
         */
        public static final String PRICING = "pricing";
        /**
         * 外汇管理模块
         */
        public static final String FEX = "fex";
        /**
         * 企E金服模块
         */
        public static final String FUNDJR = "fundjr";
        /**
         * 工作流程模块
         */
        public static final String BPM = "bpm";
        /**
         * 营收稽核模块
         */
        public static final String RAS = "ras";
        /**
         * 数据交换
         */
        public static final String ERP = "erp";
        /**
         * 合同管理
         */
        public static final String CON = "con";
    }
    /**
     * 业务类型代码
     *
     * @author zhengg
     */
    public class BusType {
        /*** 基础平台-用户管理*/
        public static final String SYS_USER = "user";
        /*** 承兑汇票-应付票据*/
        public static final String ACCEBILL_EMIT = "billEmit";
        /*** 承兑汇票-应收票据*/
        public static final String ACCEBILL_RECEIVE = "billReceive";
        /*** 承兑汇票-电票票据*/
        public static final String ACCEBILL_ELECTRIC = "billElectric";
        /*** 空白凭证*/
        public static final String CASH_VOU_CHER = "cashVoucher";
        /*** 支票管理*/
        public static final String BV_PAYEE_CHEQUE = "bvPayeeCheque";
        /** 集团票据池**/
        /***集团票据池-额度管理*/
        public static final String POOL_LIMIT = "poolLimit";
        /***集团票据池-额度管理*/
        public static final String POOL_LIMIT_ALLO = "poolLimitAllo";
        /***集团票据池-保证金*/
        public static final String POOL_BAIL = "poolBail";
        /***集团票据池-池管理*/
        public static final String POOL_MANAGE = "poolManage";
        /***集团票据池-票据管理*/
        public static final String POOL_ACCEBILL = "poolAccebill";
        /***集团票据池-融资申请*/
        public static final String POOL_EMIT_FINA = "poolEmitFina";
        /**
         * 资金预警
         **/
        public static final String FUND_WARN = "fundWarn";
        /**
         * 营收稽核
         */
        /**
         * 营收管理
         **/
        public static final String RAS_REVENUE = "rasRevenue";
        /**
         * 实收管理
         **/
        public static final String RAS_ECEIVED = "rasReceived";
        /**
         * 合同管理
         **/
        public static final String CON_TRACT = "conTract";
        /**
         * 第三方支付
         **/
        public static final String TTPAY_REFUND = "ttpayRefund";
        /**
         * 直融业务
         **/
        public static final String CMS_DF = "cmsDf";
        /**
         * 内部贷款
         **/
        public static final String CMS_LOAN_INFO = "cmsLoanInfo";
        /**
         * 委托贷款
         **/
        public static final String CMS_PRECATIVE_LOAN_INFO = "cmsPrecativeLoanInfo";
        /**
         * 国际结算业务-开证
         **/
        public static final String INTERNATIONAL_OPEN = "international_open";
        /**
         * 国际结算业务-收
         **/
        public static final String INTERNATIONAL_RECEIVE = "international_receive";
        /** 结算管理 **/
        /**
         * 内部账户管理
         **/
        public static final String NIS_INNERACC = "nisInnerAcc";
        /**
         * 银行账户管理
         **/
        public static final String NIS_BANKACC = "nisBankAcc";
        /**
         * 银行账户流水管理
         **/
        public static final String NIS_BANKACC_DTL = "nisBankAccDtl";
        /**
         * 银行账户余额管理
         **/
        public static final String NIS_BANKACC_BAL = "nisBankAccBal";
        /**
         * 付款方式分配
         **/
        public static final String NIS_PAYWAY = "nisPayWay";
        /**
         * 单位业务种类设置
         **/
        public static final String NIS_BUSTYPE = "nisBusType";
        /**
         * 职工信息
         **/
        public static final String NIS_PERSONNEL = "nisPersonnel";
        /**
         * 单位资金池维护
         **/
        public static final String NIS_POOL = "nisPool";
        /**
         * 费用报销
         **/
        public static final String NIS_EXPENSE = "nisExpense";
        /**
         * 付款办理
         **/
        public static final String NIS_BILLHEAD = "nisBillhead";
        /**
         * 收款管理
         **/
        public static final String NIS_RECEIVABLE = "nisReceivable";
        /**
         * 上划
         **/
        public static final String NIS_ALLOCATION_UP = "nisAllocationUp";
        /**
         * 下拨
         **/
        public static final String NIS_ALLOCATION_DOWN = "nisAllocationDown";
        /**
         * 请款
         **/
        public static final String NIS_ALLOCATION_ASK = "nisAllocationAsk";
        /**
         * 上缴
         **/
        public static final String NIS_ALLOCATION_TURN = "nisAllocationTurn";
        /**
         * 头寸调拨
         **/
        public static final String NIS_ALLOCATION_POS = "nisAllocationPos";
        /**
         * 代发工资
         **/
        public static final String NIS_WAGE = "nisWage";
        /**
         * 自营付款
         **/
        public static final String NIS_INPUT_EXC = "nisInputExc";
        /**
         * 付款指令
         **/
        public static final String NIS_EXC = "nisExc";
        /**
         * 收款指令
         **/
        public static final String NIS_GATHER_CMD = "nisGatherCmd";
        /**
         * 理财指令
         **/
        public static final String NIS_FINANCE_EXC = "nisFinanceExc";
        /**
         * 查询指令
         **/
        public static final String NIS_QUERY_CMD = "nisQueryCmd";
        /** 资金计划 **/
        /**
         * 单位资金计划汇总设置
         **/
        public static final String FBS_COLLECTFLAG = "fbsCollectFlag";
        /**
         * 单位资金计划控制设置
         **/
        public static final String FBS_CONTROLBUDGET = "fbsControlBudget";
        /**
         * 单位资金计划科目设置
         **/
        public static final String FBS_ITEM = "fbsItem";
        /**
         * 资金计划上报
         **/
        public static final String FBS_INPUT = "fbsInput";
        /**
         * 资金计划追加
         **/
        public static final String FBS_SUPERADD = "fbsSuperadd";
        /**
         * 资金计划执行
         **/
        public static final String FBS_PROCESS = "fbsProcess";
        /** 理财管理 **/
        /**
         * 理财产品登记
         **/
        public static final String CHO_PRODUCTREQ = "choProductReq";
        /**
         * 理财产品赎回
         **/
        public static final String CHO_PRODUCTREDEEM = "choProductRedeem";
        /**
         * 银行理财-活期转定期
         **/
        public static final String CHO_BANKFINANCE_C2T = "choBankfinanceC2T";
        /**
         * 银行理财-定期转活期
         **/
        public static final String CHO_BANKFINANCE_T2C = "choBankfinanceT2C";
        /**
         * 银行理财-活期转通知
         **/
        public static final String CHO_BANKFINANCE_C2N = "choBankfinanceC2N";
        /**
         * 银行理财-通知转活期
         **/
        public static final String CHO_BANKFINANCE_N2C = "choBankfinanceN2C";
        /**
         * 内部理财-活期转定期
         **/
        public static final String CHO_INNERFINANCE_C2T = "choBankfinanceC2T";
        /**
         * 内部理财-定期转活期
         **/
        public static final String CHO_INNERFINANCE_T2C = "choBankfinanceT2C";
        /**
         * 内部理财-活期转通知
         **/
        public static final String CHO_INNERFINANCE_C2N = "choBankfinanceC2N";
        /**
         * 内部理财-通知预约
         **/
        public static final String CHO_INNERFINANCE_N2B = "choBankfinanceN2B";
        /**
         * 内部理财-取消通知预约
         **/
        public static final String CHO_INNERFINANCE_B2N = "choBankfinanceB2N";
        /**
         * 内部理财-通知转活期
         **/
        public static final String CHO_INNERFINANCE_N2C = "choBankfinanceN2C";
        /**
         * 协定存款
         **/
        public static final String CHO_AGREED_DEPOSIT = "choAgreedDeposit";
        /** 外汇管理 **/
        /**
         * 交易需求管理
         **/
        public static final String FEX_TRANAPP = "fexTranApp";
        /**
         * 外汇买卖交易
         **/
        public static final String FEX_TRANFOREIGN = "fexTranForeign";
        /**
         * 外汇期权交易
         **/
        public static final String FEX_TRANOPTION = "fexTranOption";
        /**
         * 资金调拨交易
         **/
        public static final String FEX_TRANALLOCATION = "fexTranAllocation";
        /**
         * 资金收付交易
         **/
        public static final String FEX_TRANPAY = "fexTranPay";
        /**
         * 结算执行
         **/
        public static final String FEX_CASHFLOW = "fexCashFlow";
        /**核算*/
        /**
         * 银企接口收款
         **/
        public static final String FCL_NET_CHALK_TALLY = "fclNetChalkTally";
        /**
         * 网点间来账
         **/
        public static final String FCL_UNION_TALLY = "fclUnionTally";
        /**
         * 中心申请付款
         **/
        public static final String FCL_CENTER_TALLY = "fclCenterTally";
        /**
         * 委托贷款
         **/
        public static final String FCL_PRECATIVE_TALLY = "fclPrecativeTally";
        /**
         * 银行借款
         **/
        public static final String FCL_PROVIDE_TALLY = "fclProvideTally";
        /**
         * 内部贷款
         **/
        public static final String FCL_LOAN_TALLY = "fclLoanTally";
        /**
         * 资金调拨
         **/
        public static final String FCL_ALLOCATION_TALLY = "fclAllocationTally";
        /**
         * 单位申请付款
         **/
        public static final String FCL_BILLHEAD_TALLY = "fclBillheadTally";
        /**
         * 中心代保管票据
         **/
        public static final String FCL_AB_SAFEKEEPING_TALLY = "fclAbSafekeepingTally";
        /**
         * 中心自身库存票据
         */
        public static final String FCL_AB_ONESELF_TALLY = "fclAbOneselfTally";
        /**
         * 划拨
         */
        public static final String FCL_AB_TRANSFER_TALLY = "fclAbTransferTally";
        /**
         * 单位资金池
         */
        public static final String FCL_POOL_TALLY = "fclPoolTally";
        /**
         * 内部存款
         */
        public static final String FCL_INNER_TALLY = "fclInnerTally";
        /**
         * 网络记账
         */
        public static final String FCL_TALLY = "fclTally";
        /**
         * 手工记账
         */
        public static final String FCL_HAND_TALLY = "fclHandTally";
        /**
         * 批量计息
         */
        public static final String FCL_INTEREST = "fclInterest";
        /**
         * 保证金管理
         **/
        public static final String BOND_MANAGE = "bondManage";
        /**
         * 担保管理
         **/
        public static final String GUA_MANAGE = "guaManage";
        /**
         * 授信管理
         **/
        public static final String RAT_MANAGE = "ratManage";
        /**
         * 银行借款
         **/
        public static final String CMS_PROVIDELOAN = "cmsProLoan";
    }
    /**
     * 模块对应系统菜单ID
     */
    public static final Map<String, String> MODULETOMENUID = new HashMap<>();
    /**
     * 业务模块对应系统菜单ID
     */
    public static final Map<String, String> BUSTYPETOMENUID = new HashMap<>();
    static {
        //系统模块对应菜单id
        MODULETOMENUID.put(Module.SYS, "402880385c15400e015c154cd3450006");
        MODULETOMENUID.put(Module.NIS, "4028802f5bf6b978015bf6bb00190000");
        MODULETOMENUID.put(Module.ACCEBILL, "402880455c616213015c6162e77d0000");
        MODULETOMENUID.put(Module.CASHVOUCHER, "d0ed01326ac94d689a5bc5c082375d8f");
        MODULETOMENUID.put(Module.FUNDWARN, "5a1b112888cd4e60b639197c09a08d53");
        MODULETOMENUID.put(Module.TTPAY, "d03c955371304c9b8f1b7ff034f8b52d");
        MODULETOMENUID.put(Module.BANKACC, "d5b09cb914304f8987cb8e6e0ecb3ff3");
        MODULETOMENUID.put(Module.FCS, "bb39f697db29466aa2c5b8795372af60");
        MODULETOMENUID.put(Module.FCL, "4028801f5bf6ae1c015bf6af6cca0001");
        MODULETOMENUID.put(Module.CMS, "28183155434f425e8ce436c2b19eb2fd");
        MODULETOMENUID.put(Module.RAT, "57abaa37db0144d790991c421c9720e5");
        MODULETOMENUID.put(Module.GUA, "f6651ae1b18b4da18ccf5490fbd655d0");
        MODULETOMENUID.put(Module.FBS, "402880275c8b05d6015c8b60ad1a0002");
        MODULETOMENUID.put(Module.INTERNATIONAL, "e837e42f607d4d94b1e65ca4d49e6113");
        MODULETOMENUID.put(Module.CHOWMATISTIC, "90f6219604ea4303a6b2c3d99d177d7d");
        MODULETOMENUID.put(Module.BI, "b007c9318c744e0193982c3a2bea432c");
        MODULETOMENUID.put(Module.PRICING, "e396949dac634de39f1b6bdf2243b6f8");
        MODULETOMENUID.put(Module.FEX, "88cb524b44dc4d8d8ec7e56000e072cd");
        MODULETOMENUID.put(Module.FUNDJR, "3859d6993e4f41e69f8bf065a131ca8c");
        MODULETOMENUID.put(Module.BPM, "7822128f5de14e3a8473ac42880de120");
        MODULETOMENUID.put(Module.RAS, "402880455c616213015c6162e7000000");
        MODULETOMENUID.put(Module.ERP, "c1fb5fbb835e4303852804517e8ea6c9");
        MODULETOMENUID.put(Module.BILLPOOL, "d73c35f292494dd899c139eac39d229a");
        MODULETOMENUID.put(Module.CON, "2b3ace2e58aa4dc294f198b14d98a4db");
        //业务模块对应菜单ID
        BUSTYPETOMENUID.put(BusType.SYS_USER, "402880425b951777015b95203d670005");
        BUSTYPETOMENUID.put(BusType.ACCEBILL_EMIT, "38d6293b5fbf4597aa52222b429724ae");
        BUSTYPETOMENUID.put(BusType.ACCEBILL_RECEIVE, "a6b30d0b46174b088f279c3eb8263686");
        BUSTYPETOMENUID.put(BusType.ACCEBILL_ELECTRIC, "918afac2c28e4ccc824b69c971b1119b");
        BUSTYPETOMENUID.put(BusType.TTPAY_REFUND, "cdf6ab77f28644aab36da1ac5d3c5012");
//		国际结算
        BUSTYPETOMENUID.put(BusType.INTERNATIONAL_OPEN, "feaf8940db824f5f88a70f0523639378");
        BUSTYPETOMENUID.put(BusType.INTERNATIONAL_RECEIVE, "2f5615d048d54846b5a89e4f5099730c");
        //票据池和合同管理、资金预警、空白凭证、营收稽核
        BUSTYPETOMENUID.put(BusType.POOL_LIMIT, "a758531404bf4343b0ab09c8a50363ab");
        BUSTYPETOMENUID.put(BusType.POOL_LIMIT_ALLO, "2e243b334e4d4890a3db42ac0ce8db88");
        BUSTYPETOMENUID.put(BusType.POOL_BAIL, "4e55b9e1dc9b48df87d42cec7fac0e14");
        BUSTYPETOMENUID.put(BusType.POOL_MANAGE, "59d7aca2c4f847b0a89d56055f206d50");
        BUSTYPETOMENUID.put(BusType.POOL_ACCEBILL, "15a8872848304beeb12a27099f0da113");
        BUSTYPETOMENUID.put(BusType.POOL_EMIT_FINA, "25c821b9de5a41a3bcde88e4e93bf1af");
        BUSTYPETOMENUID.put(BusType.FUND_WARN, "5a1b112888cd4e60b639197c09a08d53");
        BUSTYPETOMENUID.put(BusType.RAS_REVENUE, "402880455c616213015c6162e7000012");
        BUSTYPETOMENUID.put(BusType.RAS_ECEIVED, "402880455c616213015c6162e7000016");
        BUSTYPETOMENUID.put(BusType.CON_TRACT, "2b3ace2e58aa4dc294f198b14d98a4db");
        BUSTYPETOMENUID.put(BusType.CASH_VOU_CHER, "402880455c39a4f3015c3a4894c60002");
        BUSTYPETOMENUID.put(BusType.BV_PAYEE_CHEQUE, "d1f4278b8dcd4d89bbdef67d615e91da");
        //结算管理
        BUSTYPETOMENUID.put(BusType.NIS_INNERACC, "bb39f697db29466aa2c5b8795372af60");
        BUSTYPETOMENUID.put(BusType.NIS_BANKACC, "d5b09cb914304f8987cb8e6e0ecb3ff3");
        BUSTYPETOMENUID.put(BusType.NIS_BANKACC_DTL, "f3325d207cf84902b4c3b4ec89256186");
        BUSTYPETOMENUID.put(BusType.NIS_BANKACC_BAL, "16df50dcd9544ddfb808f9213994e151");
        BUSTYPETOMENUID.put(BusType.NIS_PAYWAY, "4028802f5c0ad6dc015c0ade35960007");
        BUSTYPETOMENUID.put(BusType.NIS_BUSTYPE, "402880275c153352015c1535770e0000");
        BUSTYPETOMENUID.put(BusType.NIS_PERSONNEL, "4028802e5c424bf8015c42644eb80026");
        BUSTYPETOMENUID.put(BusType.NIS_POOL, "cd00831951b846ddb235cfa99fdfdb6c");
        BUSTYPETOMENUID.put(BusType.NIS_EXPENSE, "2e1bd78f822f41b7a0988457eb0f32b3");
        BUSTYPETOMENUID.put(BusType.NIS_BILLHEAD, "e5966e6088954cf4996811b12916b91f");
        BUSTYPETOMENUID.put(BusType.NIS_RECEIVABLE, "8949f7e1894343db91c0ec2c79bf037c");
        BUSTYPETOMENUID.put(BusType.NIS_ALLOCATION_UP, "4028802e5c5c2e2d015c5c5732ba002f");
        BUSTYPETOMENUID.put(BusType.NIS_ALLOCATION_DOWN, "4028802e5c5d50ad015c5d8df5cb001a");
        BUSTYPETOMENUID.put(BusType.NIS_ALLOCATION_ASK, "4028802e5c675881015c6763cce30011");
        BUSTYPETOMENUID.put(BusType.NIS_ALLOCATION_TURN, "4028802e5c5d50ad015c5d8c6d970019");
        BUSTYPETOMENUID.put(BusType.NIS_ALLOCATION_POS, "4028802e5c675881015c6767b8270014");
        BUSTYPETOMENUID.put(BusType.NIS_WAGE, "17e04d2adf0745218d4b7ff38458964c");
        BUSTYPETOMENUID.put(BusType.NIS_INPUT_EXC, "d466013f3dde4a2ab5f814039f3eb3f5");
        BUSTYPETOMENUID.put(BusType.NIS_EXC, "347c60d302c443db87213714548bf56f");
        BUSTYPETOMENUID.put(BusType.NIS_GATHER_CMD, "c5fdee4be4e549e8a639962d381f0e09");
        BUSTYPETOMENUID.put(BusType.NIS_FINANCE_EXC, "1d4254a42af64e698271383532639da0");
        BUSTYPETOMENUID.put(BusType.NIS_QUERY_CMD, "53c974cae24343c2a65ccf0135afc12f");
        //资金计划
        BUSTYPETOMENUID.put(BusType.FBS_COLLECTFLAG, "402880275ca43ae9015ca43c13360000");
        BUSTYPETOMENUID.put(BusType.FBS_CONTROLBUDGET, "402880275c8ba0db015c8bbf08e5004c");
        BUSTYPETOMENUID.put(BusType.FBS_ITEM, "7758a0e4c51a45a49b6b16918ea92f8f");
        BUSTYPETOMENUID.put(BusType.FBS_INPUT, "ad726048e4c14b7a95538e556c3ee2df");
        BUSTYPETOMENUID.put(BusType.FBS_SUPERADD, "e2e893a49d994ddcb03f00fa0886a110");
        BUSTYPETOMENUID.put(BusType.FBS_PROCESS, "8f60ee65e7b543e39a5145d3d5522ff1");
        //理财管理
        BUSTYPETOMENUID.put(BusType.CHO_PRODUCTREQ, "a6ede075de4246b69cb223664f1e80c8");
        BUSTYPETOMENUID.put(BusType.CHO_PRODUCTREDEEM, "c244aac85ae54411bd285b4ae028ebb1");
        BUSTYPETOMENUID.put(BusType.CHO_BANKFINANCE_C2T, "b9ea94320ff440bea030b1df211904df");
        BUSTYPETOMENUID.put(BusType.CHO_BANKFINANCE_T2C, "0e66d17b03f14dde8b69c3acec40786e");
        BUSTYPETOMENUID.put(BusType.CHO_BANKFINANCE_C2N, "c3496bf3db2348e09e0b88e1ef734b09");
        BUSTYPETOMENUID.put(BusType.CHO_BANKFINANCE_N2C, "333b762a50ca4d56b3b7f922f1a4cd6a");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_C2T, "d0f3bd9668cd4e27b0d78112a88a58e4");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_T2C, "1ad54f9db7d940e59c717c30ea9683f6");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_C2N, "e4793aa2507b414b9833aee5098a889d");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_N2B, "fb373029c883421b863c11eb7a4845e9");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_B2N, "da0d8cc56d984763bfcf56bf925cc80b");
        BUSTYPETOMENUID.put(BusType.CHO_INNERFINANCE_N2C, "39248b1bd7a947db8bd3720838be2f4a");
        BUSTYPETOMENUID.put(BusType.CHO_AGREED_DEPOSIT, "c1b5d841488e4801b8b43771d6f7a961");
        //外汇管理
        BUSTYPETOMENUID.put(BusType.FEX_TRANAPP, "01ec37de1c1546fcaf416e2979a4841d");
        BUSTYPETOMENUID.put(BusType.FEX_TRANFOREIGN, "ab979840628242d796c2631562bbf48c");
        BUSTYPETOMENUID.put(BusType.FEX_TRANOPTION, "4dfa9ebe56d341bfba3d030c51ffad1a");
        BUSTYPETOMENUID.put(BusType.FEX_TRANALLOCATION, "ab2d6312220a4d3e824527fd972f9fe8");
        BUSTYPETOMENUID.put(BusType.FEX_TRANPAY, "40bfa22fd72f4121be973ed2c806782c");
        //核算-网络记账
        BUSTYPETOMENUID.put(BusType.FCL_NET_CHALK_TALLY, "b82ad535291943798b13d36f90dfd55c");
        BUSTYPETOMENUID.put(BusType.FCL_UNION_TALLY, "a8c590d34986482eb48a4b3766ce888c");
        BUSTYPETOMENUID.put(BusType.FCL_CENTER_TALLY, "59159283e92e43b094d6b133c8641eb8");
        BUSTYPETOMENUID.put(BusType.FCL_PRECATIVE_TALLY, "1ae3a05037544ac78ce2cb4f02012406");
        BUSTYPETOMENUID.put(BusType.FCL_PROVIDE_TALLY, "017c16cf045645ce9658ac3b94e13082");
        BUSTYPETOMENUID.put(BusType.FCL_LOAN_TALLY, "2a42ecbefdaf47a0a570fd5ec5c0cccc");
        BUSTYPETOMENUID.put(BusType.FCL_ALLOCATION_TALLY, "ebc1c7213ad04a52b7fcd31c836dbcf7");
        BUSTYPETOMENUID.put(BusType.FCL_BILLHEAD_TALLY, "b8ee1a8115214ae4bd3356fa88ec6830");
        BUSTYPETOMENUID.put(BusType.FCL_AB_SAFEKEEPING_TALLY, "3abf2417a18446f0a021238f3df01b98");
        BUSTYPETOMENUID.put(BusType.FCL_AB_ONESELF_TALLY, "1dd8b35fd8254c3ebbc614293ded5914");
        BUSTYPETOMENUID.put(BusType.FCL_AB_TRANSFER_TALLY, "2a445139b9084b70bbb441ddc8eab887");
        BUSTYPETOMENUID.put(BusType.FCL_POOL_TALLY, "50fd229d2f114bf19015f9d7aa33ab9e");
        BUSTYPETOMENUID.put(BusType.FCL_INNER_TALLY, "79af5376d95042e5a8bda6b653982582");
        BUSTYPETOMENUID.put(BusType.FCL_TALLY, "60f41f95688f4456864b33e5842182b4");
        BUSTYPETOMENUID.put(BusType.FCL_HAND_TALLY, "ca66805dc6634fb9b0d2f950fe137981");
        BUSTYPETOMENUID.put(BusType.FCL_INTEREST, "a696e41e3b9c4675838d99e0952e8bcc");
        BUSTYPETOMENUID.put(BusType.FEX_CASHFLOW, "76e7a9b63de04e67a224aa36b8a2d370");
//		信贷
        BUSTYPETOMENUID.put(BusType.CMS_DF, "3cb4abbc8a434e70bbab987cd7cca2b9");
        BUSTYPETOMENUID.put(BusType.CMS_LOAN_INFO, "4b1bcffdaa464e92a44f31bee53d6451");
        BUSTYPETOMENUID.put(BusType.CMS_PRECATIVE_LOAN_INFO, "4007b8a99f454b2abf2557eac3bec997");
        //保证金管理
        BUSTYPETOMENUID.put(BusType.BOND_MANAGE, "6b540ef2aa494a76932d1586bb575603");    /** 保证金管理**/
        //担保管理
        BUSTYPETOMENUID.put(BusType.GUA_MANAGE, "f6651ae1b18b4da18ccf5490fbd655d0");    /** 担保管理**/
        //授信管理
        BUSTYPETOMENUID.put(BusType.RAT_MANAGE, "57abaa37db0144d790991c421c9720e5");/** 授信管理**/
        //银行借款
        BUSTYPETOMENUID.put(BusType.CMS_PROVIDELOAN, "bf54345e8eda4aea9f517cca80a7adba");/** 银行借款 **/
    }
    /**
     * 踢出标识
     */
    public static final String KICK_OUT = "kickOut";
    /**
     * 开启IP登录控制
     */
    public static final String IP_BAN = "loginOpenIpLimit";
    public static final String IP_BAN_WHITE_LIST = "ipWhiteList";
    /**
     * 证书类型选择
     * 1天威证书，2CFCA证书，0不使用证书
     */
    public static final String ENCRYPT = "encrypt";
    /**
     * 登录方式
     * 0用户密码，1银行密码+证书，2证书
     */
    public static final String LOGINTYPE = "loginType";
    /**
     * 设置系统客商是否可以从其他业务层手动录入
     * 0-否,1-是
     */
    public static final String EXTERNALCORP = "externalCorp";
    /**
     * 权限控制类型
     * 0-账户级,1-单位级
     */
    public static final String CONTROLTYPE = "controlType";
    /**
     * 是否启用限额管理
     * 0-否,1-是
     */
    public static final String ISOPENLIMIT = "isOpenLimit";
    /**
     * 票据起始认领时间
     */
    public static final String CLAIM_START_DATE = "claimStartDate";
    /**
     * 第三方票据自动托收申请
     */
    public static final String IS_THIRD_BILL_AUTO_COLLECT = "isThirdBillAutoCollect";
    /**
     * 历史消息保留天数
     */
    public static final String NOTIFICAT_SAVE_DAYS = "notificatSaveDays";

    /**
     * 单租户模式租户id 注释 取配置
     */
    //public static final String aloneTenantModeID = "1000";
}
