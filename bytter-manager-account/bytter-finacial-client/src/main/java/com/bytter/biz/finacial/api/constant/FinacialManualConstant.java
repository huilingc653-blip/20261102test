package com.bytter.biz.finacial.api.constant;
import com.bytter.core.constant.ConstantBean;
import java.util.HashMap;
import java.util.Map;
/**
 * 记账常量类
 * @author lix
 * @since 2011-8-21
 */
public class FinacialManualConstant {
	
	/**手工帐流程 - 1 记账*/
	public final static String FLOW_TYPE_1 = "1";
	/**手工帐流程 - 2 改账*/
	public final static String FLOW_TYPE_2 = "2";
	/**手工帐流程 - 3 复核*/
	public final static String FLOW_TYPE_3 = "3";
	
	/**记账业务状态 记账*/
	public final static String MANUAL_STATUS_INIT = "Init";
	/**记账业务状态 改账*/
	public final static String MANUAL_STATUS_EDIT = "Edit";
	/**记账业务状态 复核*/
	public final static String MANUAL_STATUS_CHECK = "Check";
	
	/**字段值类型 - 常量类型*/
	public static final String COLUMN_VALUE_TYPE_0 = "0";
	/**字段值类型 - 从对象获取*/
	public static final String COLUMN_VALUE_TYPE_1 = "1";
	/**字段值类型 - 固定字段*/
	public static final String COLUMN_VALUE_TYPE_2 = "2";
	
	/*** 手工账类型*/
	/*** 上划*/
	public final static String MANUAL_TYPE_UP = "10";
	/*** 下拨*/
	public final static String MANUAL_TYPE_DOWN = "11";
	/*** 请款*/
	public final static String MANUAL_TYPE_ASK = "12";
	/*** 头寸调拨*/
	public final static String MANUAL_TYPE_POS = "13";
	/*** 上缴*/
	public final static String MANUAL_TYPE_TURN = "14";
	/*** 委托贷款发放*/
	public final static String MANUAL_TYPE_PRECATIVE_SEND = "41";
	/*** 委托贷款归还*/
	public final static String MANUAL_TYPE_PRECATIVE_REPAY = "42";
	/**借款发放**/
	public final static String MANUAL_TYPE_PROVIDE_SEND = "21";
	/**借款线上还款**/
	public final static String MANUAL_TYPE_PROVIDE_TOPREPAY = "22";
	/**借款线下还款**/
	public final static String MANUAL_TYPE_PROVIDE_DOWNREPAY = "23";
	/**借款还款确认**/
	public final static String MANUAL_TYPE_PROVIDE_CONFIRM = "24";
	/**入库中心**/
	public final static String MANUAL_TYPE_PUT_IN_STORAGE = "61";
	/**移存中心**/
	public final static String MANUAL_TYPE_MOVE_TO_SAVE = "62";
	/**中心移存银行**/
	public final static String MANUAL_TYPE_CENTER_TO_BANK = "63";
	/**中心取回单位**/
	public final static String MANUAL_TYPE_CENTER_TO_CORP = "64";
	/**银行移存中心**/
	public final static String MANUAL_TYPE_BANK_TO_CENTER = "65";
	/**退票记账**/
	public final static String MANUAL_TYPE_REFUND = "71";
	/**托收申请**/
	public final static String MANUAL_TYPE_COLLECTION_APPLY = "81";
	/**托收到账**/
	public final static String MANUAL_TYPE_COLLECTION_TO_THE_ACCOUNT = "82";
	/**对内背书**/
	public final static String MANUAL_TYPE_INTERNAL_ENDORSEMENT = "91";
	/**对外背书**/
	public final static String MANUAL_TYPE_FOREIGN_ENDORSEMENT = "92";
	/**中心库存贴现买断式**/
	public final static String MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_BUYOUT = "101";
	/**中心库存贴现赎回式**/
	public final static String MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_REDEMPTION = "102";
	/**单位库存贴中心买断式**/
	public final static String MANUAL_TYPE_UNIT_STOCK_PASTE_CENTER_BUYING_PATTERN = "103";
	/**单位库存贴中心赎回式**/
	public final static String MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_REDEMPTION = "104";
	/**中心库存贴中心收回*/
	public final static String MANUAL_TYPE_CENTER_INVENTORY_POST_CENTER_RECOVERY = "105";
	/**单位库存贴中心收回*/
	public final static String MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_RECOVERY = "106";
	/**中心库存质押中心*/
	public final static String MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER = "151";
	/**中心库存质押中心收回*/
	public final static String MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER_IS_RECOVERED = "152";
	/**单位库存质押中心*/
	public final static String MANUAL_TYPE_UNIT_STOCK_PLEDGE_CENTER = "153";
	/**单位库存质押中心收回*/
	public final static String MANUAL_TYPE_THE_UNIT_INVENTORY_PLEDGE_CENTER_IS_RECOVERED = "154";
	/**托收申请来源库存*/
	public final static String MANUAL_TYPE_COLLECTION_APPLICATION_SOURCE_INVENTORY = "161";
	/**托收申请来源贴现*/
	public final static String MANUAL_TYPE_APPLY_FOR_SOURCE_DISCOUNT_FOR_COLLECTION = "162";
	/**托收到账*/
	public final static String MANUAL_TYPE_THE_COLLECTION_TO_THE_ACCOUNT = "163";
	/**贴现申请来源库存*/
	public final static String MANUAL_TYPE_DISCOUNT_SOURCE_INVENTORY = "171";
	/**贴现申请来源贴现*/
	public final static String MANUAL_TYPE_DISCOUNT_APPLICATION_SOURCE_DISCOUNT = "172";
	/**贴现到账*/
	public final static String MANUAL_TYPE_DISCOUNT_TO_ACCOUNT = "173";
	/**贴现收回来源库存*/
	public final static String MANUAL_TYPE_DISCOUNT_TO_RECOVER_SOURCE_INVENTORIES = "174";
	/**贴现收回来源贴现*/
	public final static String MANUAL_TYPE_DISCOUNT_CALL_SOURCE_DISCOUNT = "175";
	/**上划*/
	public final static String MANUAL_TYPE_ON_THE_CROSS = "181";
	/**下拨*/
	public final static String MANUAL_TYPE_STIR = "182";
	
	/**活期转定期*/
	public final static String MANUAL_TYPE_CURRENT_TO_REGULAR = "191";
	/**定期转活期*/
	public final static String MANUAL_TYPE_REGULAR_TO_CURRENT = "192";
	/**活期转通知*/
	public final static String MANUAL_TYPE_CURRENT_TO_INFORM = "193";
	/**通知转活期*/
	public final static String MANUAL_TYPE_INFORM_TO_CURRENT = "194";
	
	
	/*** 单据来源 1-资金调拨*/
	public final static String MANUAL_FROM_1 = "1";
	/*** 单据来源 2-信贷管理-银行借款*/
	public final static String MANUAL_FROM_2= "2";
	/*** 单据来源 3-信贷管理-内部贷款*/
	public final static String MANUAL_FROM_3= "3";
	/*** 单据来源 4-信贷管理-委托贷款*/
	public final static String MANUAL_FROM_4= "4";
	/*** 单据来源 5-单位付款*/
	public final static String MANUAL_FROM_5= "5";
	/***单据来源 6-出入库记账***/
	public final static String MANUAL_FROM_6= "6";
	/***单据来源 7-退票记账***/
	public final static String MANUAL_FROM_7= "7";
	/***单据来源 8-托收记账***/
	public final static String MANUAL_FROM_8= "8";
	/***单据来源 9-背书记账***/
	public final static String MANUAL_FROM_9= "9";
	/***单据来源 10-贴现记账-代保 ***/
	public final static String MANUAL_FROM_10= "10";
	/***单据来源 11-质押记账***/
	public final static String MANUAL_FROM_15= "15";
	/***单据来源 12-托收记账***/
	public final static String MANUAL_FROM_16= "16";
	/***单据来源 13-贴现记账-自身 ***/
	public final static String MANUAL_FROM_17= "17";
	/***单据来源 14-票据调拨***/
	public final static String MANUAL_FROM_18= "18";
	/**单据来源 15-内部存款管理*/
	public final static String MANUAL_FROM_19= "19";
	
	/**处理账务*/
    public static final String FC_PROCESS_ACCBOOK = "processAccbook";
    /**获取数据*/
    public static final String FC_QUERY_DATE = "getDateTable";
    /**获取账号信息*/
    public static final String FC_GET_FCACC = "getFcAcc";
    /**设置复核凭证信息*/
    public static final String SET_CHECK_VOUCHER = "setCheckVoucherInfo";
    /**有效信息*/
    public static final String VALID_BEFORE = "validBefore";
	/**获取账号信息（转付、代付、下拨、请款借方账户获取单位对应的内部账户）*/
	public static final String FC_GET_FCACC_DEBIT = "getFcAccDebit";
    /**待记账*/
    public static final String TALLY_SIGN_WAIT_TALLY = "0";
	/**记账通过*/
	public static final String TALLY_SIGN_TALLY_OVER = "1";
	/**记账待复核*/
	public static final String TALLY_SIGN_TALLY_CHECK = "3";
	/**记账打回*/
	public static final String TALLY_SIGN_TALLY_BACK = "4";
	/**记账暂存*/
	public static final String TALLY_SIGN_TALLY_TEMP = "5";
	public static final Map<String, String> AccTypeMap = new HashMap<String, String>();
	public static final Map<String, String> ClassMap = new HashMap<String, String>();
	
	/**
	 * 页面菜单手工账类型和业务来源类型对应
	 */
	public static final Map<String, ConstantBean<Integer>> MANUALTYPE_BUSORITYPE_MAP = new HashMap<>();
    static {
        AccTypeMap.put(MANUAL_FROM_4, "precativetally/PrecativeManualTab");
        AccTypeMap.put(MANUAL_TYPE_PRECATIVE_SEND, "precativetally/PrecativeManual");
        AccTypeMap.put(MANUAL_TYPE_PRECATIVE_REPAY, "precativetally/PrecativeRapayManual");
        ClassMap.put(MANUAL_FROM_4, "com.bytter.biz.finacial.bankCollect.service.PrecativeTallyService");
		
        AccTypeMap.put(MANUAL_FROM_1, "nisallocationreqmanual/nisAllocationReqManualTab");
        AccTypeMap.put(MANUAL_TYPE_DOWN, "nisallocationreqmanual/nisAllocationReqManual");
        AccTypeMap.put(MANUAL_TYPE_ASK, "nisallocationreqmanual/nisAllocationReqManual");
        AccTypeMap.put(MANUAL_TYPE_POS, "nisallocationreqmanual/nisAllocationReqManual");
        ClassMap.put(MANUAL_FROM_1, "com.bytter.biz.finacial.bankCollect.service.NisAllocationReqManualService");
        
        AccTypeMap.put(MANUAL_FROM_5, "nisbillheadmanual/nisBillheadManual");
        ClassMap.put(MANUAL_FROM_5, "com.bytter.biz.finacial.bankCollect.service.NisBillheadManualService");
        
		/**银行借款*/
        AccTypeMap.put(MANUAL_FROM_2, "providetally/provideManualTab");
        AccTypeMap.put(MANUAL_TYPE_PROVIDE_SEND, "providetally/provideManual");
        AccTypeMap.put(MANUAL_TYPE_PROVIDE_TOPREPAY, "providetally/provideManualRepay");
        AccTypeMap.put(MANUAL_TYPE_PROVIDE_DOWNREPAY, "providetally/provideManualRepay");
        AccTypeMap.put(MANUAL_TYPE_PROVIDE_CONFIRM, "providetally/provideManualRepay");
		/**承兑汇票*/
		AccTypeMap.put(MANUAL_FROM_6, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_PUT_IN_STORAGE , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_MOVE_TO_SAVE , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_CENTER_TO_BANK , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_CENTER_TO_CORP , "abbilltally/abBillManual");
		/**出入库*/
		AccTypeMap.put(MANUAL_TYPE_BANK_TO_CENTER , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_FROM_7, "abbilltally/abBillManualTab");
		/**退票*/
		AccTypeMap.put(MANUAL_TYPE_REFUND , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_FROM_8, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_COLLECTION_APPLY , "abbilltally/abBillManual");
		/**托收*/
		AccTypeMap.put(MANUAL_TYPE_COLLECTION_TO_THE_ACCOUNT, "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_FROM_9, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_INTERNAL_ENDORSEMENT , "abbilltally/abBillManualEndOrsement");
		/**背书*/
		AccTypeMap.put(MANUAL_TYPE_FOREIGN_ENDORSEMENT , "abbilltally/abBillManualEndOrsement");
		AccTypeMap.put(MANUAL_FROM_10, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_BUYOUT , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_REDEMPTION  , "abbilltally/abBillManualRedeem");
		AccTypeMap.put(MANUAL_TYPE_UNIT_STOCK_PASTE_CENTER_BUYING_PATTERN , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_REDEMPTION , "abbilltally/abBillManualRedeem");
		AccTypeMap.put(MANUAL_TYPE_CENTER_INVENTORY_POST_CENTER_RECOVERY , "abbilltally/abBillManualDiscount");
		/**贴现*/
		AccTypeMap.put(MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_RECOVERY , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_FROM_15, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER_IS_RECOVERED , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_UNIT_STOCK_PLEDGE_CENTER , "abbilltally/abBillManual");
		/**质押*/
		AccTypeMap.put(MANUAL_TYPE_THE_UNIT_INVENTORY_PLEDGE_CENTER_IS_RECOVERED , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_FROM_16, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_COLLECTION_APPLICATION_SOURCE_INVENTORY , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_TYPE_APPLY_FOR_SOURCE_DISCOUNT_FOR_COLLECTION , "abbilltally/abBillManual");
		/**托收*/
		AccTypeMap.put(MANUAL_TYPE_THE_COLLECTION_TO_THE_ACCOUNT , "abbilltally/abBillManual");
		AccTypeMap.put(MANUAL_FROM_17, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_DISCOUNT_SOURCE_INVENTORY , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_TYPE_DISCOUNT_APPLICATION_SOURCE_DISCOUNT , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_TYPE_DISCOUNT_TO_ACCOUNT , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_TYPE_DISCOUNT_TO_RECOVER_SOURCE_INVENTORIES , "abbilltally/abBillManualDiscount");
		/**贴现记账-自身*/
		AccTypeMap.put(MANUAL_TYPE_DISCOUNT_CALL_SOURCE_DISCOUNT , "abbilltally/abBillManualDiscount");
		AccTypeMap.put(MANUAL_FROM_18, "abbilltally/abBillManualTab");
		AccTypeMap.put(MANUAL_TYPE_ON_THE_CROSS , "abbilltally/abBillManual");
		/**票据调拨*/
		AccTypeMap.put(MANUAL_TYPE_STIR , "abbilltally/abBillManualAllocated");
		
		/**内部存款*/
		AccTypeMap.put(MANUAL_FROM_19, "innerdeposit/innerDepositManualTab");
		AccTypeMap.put(MANUAL_TYPE_CURRENT_TO_REGULAR , "innerdeposit/currentToAll");//活转定
		AccTypeMap.put(MANUAL_TYPE_REGULAR_TO_CURRENT , "innerdeposit/regularToCurrent");//定转活
		AccTypeMap.put(MANUAL_TYPE_CURRENT_TO_INFORM , "innerdeposit/currentToAll");//活转通
		AccTypeMap.put(MANUAL_TYPE_INFORM_TO_CURRENT , "innerdeposit/informToCurrent");//通转活
		
		
        /**入库中心**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_PUT_IN_STORAGE,FinaBookConstant.BusOriType.NB_BILL_CORP_IN_NET);
    	/**移存中心**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_MOVE_TO_SAVE,FinaBookConstant.BusOriType.NB_BILL_CORP_MOVE_NET);
    	/**中心移存银行**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTER_TO_BANK,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_MOVE_BANK);
    	/**中心取回单位**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTER_TO_CORP,FinaBookConstant.BusOriType.NB_BILL_CORP_GET);
    	/**银行移存中心**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_BANK_TO_CENTER,FinaBookConstant.BusOriType.NB_BILL_BANK_TO_NET_GET);
    	/**退票记账**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_REFUND,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_BACEBILL);
    	/**托收申请**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_COLLECTION_APPLY,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_COLLECTION);
    	/**托收到账**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_COLLECTION_TO_THE_ACCOUNT,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_COLLECTION_DAOZHANG);
    	/**对内背书**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_INTERNAL_ENDORSEMENT,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_IN_ENDORSEMENT);
    	/**对外背书**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_FOREIGN_ENDORSEMENT,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_OUT_ENDORSEMENT);
    	/**中心库存贴现买断式**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_BUYOUT,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD);
    	/**中心库存贴现赎回式**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTRAL_INVENTORY_DISCOUNT_REDEMPTION,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH);
    	/**单位库存贴中心买断式**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_UNIT_STOCK_PASTE_CENTER_BUYING_PATTERN,FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_MD);
    	/**单位库存贴中心赎回式**/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_REDEMPTION,FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_SH);
    	/**中心库存贴中心收回*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTER_INVENTORY_POST_CENTER_RECOVERY,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET);
    	/**单位库存贴中心收回*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_UNIT_INVENTORY_POST_CENTER_RECOVERY,FinaBookConstant.BusOriType.NB_BILL_CORP_DISCOUNT_NET_GET);
    	/**中心库存质押中心*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NET);
    	/**中心库存质押中心收回*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CENTER_INVENTORY_PLEDGE_CENTER_IS_RECOVERED,FinaBookConstant.BusOriType.NB_BILL_CORP_CENTER_PLEDGE_NETGET);
    	/**单位库存质押中心*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_UNIT_STOCK_PLEDGE_CENTER,FinaBookConstant.BusOriType.NB_BILL_CORP_PLEDGE_NET);
    	/**单位库存质押中心收回*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_THE_UNIT_INVENTORY_PLEDGE_CENTER_IS_RECOVERED,FinaBookConstant.BusOriType.NB_BILL_CORP_PLEDGE_NETGET);
    	/**托收申请来源库存*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_COLLECTION_APPLICATION_SOURCE_INVENTORY,FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION);
    	/**托收申请来源贴现*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_APPLY_FOR_SOURCE_DISCOUNT_FOR_COLLECTION,FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION_TX);
    	/**托收到账*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_THE_COLLECTION_TO_THE_ACCOUNT,FinaBookConstant.BusOriType.NB_BILL_NET_COLLECTION_DAOZHANG);
    	/**贴现申请来源库存*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_DISCOUNT_SOURCE_INVENTORY,FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK);
    	/**贴现申请来源贴现*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_DISCOUNT_APPLICATION_SOURCE_DISCOUNT,FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_TX);
    	/**贴现到账*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_DISCOUNT_TO_ACCOUNT,FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG);
    	/**贴现收回来源库存*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_DISCOUNT_TO_RECOVER_SOURCE_INVENTORIES,FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET);
    	/**贴现收回来源贴现*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_DISCOUNT_CALL_SOURCE_DISCOUNT,FinaBookConstant.BusOriType.NB_BILL_NET_DISCOUNT_BANK_GET_TX);
    	/**上划*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_ON_THE_CROSS,FinaBookConstant.BusOriType.NB_BILL_DELIVER);
    	/**下拨*/
    	MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_STIR,FinaBookConstant.BusOriType.NB_BILL_ALLOCATE);
		/**活期转定期*/
		MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CURRENT_TO_REGULAR,FinaBookConstant.BusOriType.CH_DEPOSIT_CURRENT_TO_REG_PAYMENT);
		/**定期转活期*/
		MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_REGULAR_TO_CURRENT,FinaBookConstant.BusOriType.CH_DEPOSIT_REG_TO_CURRENT_PAYMENT);
		/**活期转通知*/
		MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_CURRENT_TO_INFORM,FinaBookConstant.BusOriType.CH_DEPOSIT_CURRENT_TO_INFORM_PAYMENT);
		/**通知转活期*/
		MANUALTYPE_BUSORITYPE_MAP.put(MANUAL_TYPE_INFORM_TO_CURRENT,FinaBookConstant.BusOriType.CH_DEPOSIT_INFORM_TO_CURRENT_PAYMENT);
    }
}
