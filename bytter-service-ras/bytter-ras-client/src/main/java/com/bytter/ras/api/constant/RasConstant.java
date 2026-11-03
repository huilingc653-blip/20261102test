package com.bytter.ras.api.constant;

/**
 * 营收稽核常量类
 * 
 * @author zhengg
 * 20190428
 **/
public class RasConstant {

	public final static String RAS_TASKCOMMAND = "com.bytter.ras.service.RasTaskService";

	// 营收稽核常量类
	/*** 营收稽核系统参数配置：RAS 系统类型 票据 **/
	public static final String RAS_CONFIG_TYPE = "ras";
	/*** 营收稽核系统参数配置：historyDay 判断历史天数 **/
	public static final String RAS_CONFIG_HISTORY_MOVE_DAY = "historyMoveDay";
	/*** 营收稽核系统参数配置：revenueRollDay 营收滚动天数 **/
	public static final String RAS_CONFIG_REVENUE_ROLL_DAY = "revenueRollDay";
	/*** 营收稽核系统参数配置：receivedRollDay 实收滚动天数 **/
	public static final String RAS_CONFIG_RECEIVED_ROLL_DAY = "receivedRollDay";
	/*** 营收稽核系统参数配置：defaultAuditUser 自动稽核操作人 **/
	public static final String RAS_CONFIG_DEFAULT_AUDIT_USER = "defaultAuditUser";
	
	
	// 营收稽核操作资源常量类
	/***	营收稽核参数	操作资源		ras.param.op  **/
	public static final String RAS_RESOURCE_PARAM_OP = "ras:param:op";
	/***	门店管理	操作资源		ras:shopop  **/
	public static final String RAS_RESOURCE_SHOP_OP = "ras:shop:op";
	/***	银行卡绑定	操作资源		ras:bankCardBinding:op  **/
	public static final String RAS_RESOURCE_BANKCARDBINDING_OP = "ras:bankCardBinding:op";
	/***	第三方账号绑定	操作资源		ras:otherCardBinding:op  **/
	public static final String RAS_RESOURCE_OTHERCARDBINDING_OP = "ras:otherCardBinding:op";
	/***	POS机绑定	操作资源		ras:posBinding:op  **/
	public static final String RAS_RESOURCE_POSBINDING_OP = "ras:posBinding:op";
	/***	门店授权  ras:shopAuth:op **/
	public static final String RAS_RESOURCE_SHOPAUTH_OP = "ras:shopAuth:op";
	/***	POS机借入	操作资源		ras:posBorrow:op  **/
	public static final String RAS_RESOURCE_POSBORROW_OP = "ras:posBorrow:op";
	/***	稽核策略维护	操作资源		ras:auditStrategy:op  **/
	public static final String RAS_RESOURCE_AUDITSTRATEGY_OP = "ras:auditStrategy:op";
	/***	稽核任务配置	操作资源		ras:auditTask:op  **/
	public static final String RAS_RESOURCE_AUDITTASK_OP = "ras:auditTask:op";
	/***	营收数据导入配置	操作资源		ras:revenueImportConf:op  **/
	public static final String RAS_RESOURCE_REVENUEIMPORTCONF_OP = "ras:revenueImportConf:op";
	/***	营收数据导入	操作资源		ras:revenueImport:op  **/
	public static final String RAS_RESOURCE_REVENUEIMPORT_OP = "ras:revenueImport:op";
	/***	营收数据查询	操作资源		ras:revenueQuery:op  **/
	public static final String RAS_RESOURCE_REVENUEQUERY_OP = "ras:revenueQuery:op";
	/***	实收数据导入配置	操作资源		ras:receivedImportConf:op  **/
	public static final String RAS_RESOURCE_RECEIVEDIMPORTCONF_OP = "ras:receivedImportConf:op";
	/***	实收数据导入	操作资源		ras:receivedImport:op  **/
	public static final String RAS_RESOURCE_RECEIVEDIMPORT_OP = "ras:receivedImport:op";
	/***	实收数据认领	操作资源		ras:receivedClaim:op  **/
	public static final String RAS_RESOURCE_RECEIVEDCLAIM_OP = "ras:receivedClaim:op";
	/***	实收数据查询	操作资源		ras:receivedQuery:op  **/
	public static final String RAS_RESOURCE_RECEIVEDQUERY_OP = "ras:receivedQuery:op";
	/***	营收手工稽核	操作资源		ras:manualAudit:op  **/
	public static final String RAS_RESOURCE_MANUALAUDIT_OP = "ras:manualAudit:op";
//	/***	滚动外稽核	操作资源		ras:rollOutAudit:op  **/
//	public static final String RAS_RESOURCE_ROLLOUTAUDIT_OP = "ras:rollOutAudit:op";
	/***	反稽核	操作资源		ras:backAudit:op  **/
	public static final String RAS_RESOURCE_BACKAUDIT_OP = "ras:backAudit:op";
	/***	退款申请处理	操作资源		ras:backPayOp:op  **/
	public static final String RAS_RESOURCE_BACKPAYOP_OP = "ras:backPayOp:op";
	/***	退款申请查询	操作资源		ras:backPayQuery:op  **/
	public static final String RAS_RESOURCE_BACKPAYQUERY_OP = "ras:backPayQuery:op";
	/**异明细	操作资源		ras:differentDtl:op  **/
	public static final String RAS_RESOURCE_DIFFERENTDTL_OP = "ras:differentDtl:op";
	/***	门店待稽核统计	操作资源		ras:shopNotAudit:op  **/
	public static final String RAS_RESOURCE_SHOPNOTAUDIT_OP = "ras:shopNotAudit:op";
	/***	门店已稽核统计	操作资源		ras:shopYesAudit:op  **/
	public static final String RAS_RESOURCE_SHOPYESAUDIT_OP = "ras:shopYesAudit:op";

	
	// RAS_PAY_TYPE 营收稽核缴款方式
	/** 缴付方式字典**/
	public static final String RAS_PAYTYPE_DICT= "RAS_PAY_TYPE";
	/*** 营收稽核缴款方式 001 现金 **/
	public static final String RAS_PAYTYPE_001 = "001";
	/*** 营收稽核缴款方式 002 支付宝 **/
	public static final String RAS_PAYTYPE_002 = "002";
	/*** 营收稽核缴款方式 003 微信 **/
	public static final String RAS_PAYTYPE_003 = "003";
	/*** 营收稽核缴款方式 004 银联POS机 **/
	public static final String RAS_PAYTYPE_004 = "004";
	/*** 营收稽核缴款方式 005  收钱吧 **/
	public static final String RAS_PAYTYPE_005 = "005";
	/*** 营收稽核缴款方式 006 其他 **/
	public static final String RAS_PAYTYPE_006 = "006";
	

	//营收稽核门店类型
	/*** 营收稽核门店类型 1 直营店 **/
	public static final String RAS_SHOPTYPE_1 = "1";
	/*** 营收稽核门店类型 2 联营店 **/
	public static final String RAS_SHOPTYPE_2 = "2";
	/*** 营收稽核门店类型 3专柜 **/
	public static final String RAS_SHOPTYPE_3 = "3";
	/*** 营收稽核门店类型 4 加盟店 **/
	public static final String RAS_SHOPTYPE_4 = "4";
	/*** 营收稽核门店类型 5 电商 **/
	public static final String RAS_SHOPTYPE_5 = "5";
	
	
	
//	营收稽核日志类型 RAS_LOG_TYPE	
	/*** 01	新增 **/
	public static final String RAS_LOGTYPE_ADD = "01";
	/*** 02	修改 **/
	public static final String RAS_LOGTYPE_UPDATE = "02";
	/*** 03	删除 **/
	public static final String RAS_LOGTYPE_DELETE = "03";
	/*** 04	启用 **/
	public static final String RAS_LOGTYPE_START = "04";
	/*** 05	停用 **/
	public static final String RAS_LOGTYPE_STOP = "05";
	/*** 06	认领 **/
	public static final String RAS_LOGTYPE_CLAIM = "06";
	/*** 07	导入 **/
	public static final String RAS_LOGTYPE_IMP = "07";
	/*** 10	自动稽核 **/
	public static final String RAS_LOGTYPE_AUDIT = "10";
	/*** 11	手动稽核 **/
	public static final String RAS_LOGTYPE_MANUAL_AUDIT = "11";
	/*** 12	差异稽核 **/
	public static final String RAS_LOGTYPE_DIFFERENT_AUDIT = "12";
	/*** 13	反稽核 **/
	public static final String RAS_LOGTYPE_BACK_AUDIT = "13";
	/*** 20	借出 **/
	public static final String RAS_LOGTYPE_LOAN = "20";
	/*** 21	收回 **/
	public static final String RAS_LOGTYPE_BACK = "21";
	/*** 22	确认 **/
	public static final String RAS_LOGTYPE_OK = "22";
	/*** 23	借入 **/
	public static final String RAS_LOGTYPE_BORROW = "23";
	/*** 23	借入 **/
	public static final String RAS_LOGTYPE_TASK_AUTO = "30";
	/*** 23	借入 **/
	public static final String RAS_LOGTYPE_TASK_HANDWORK = "31";
	/*** 23	借入 **/
	public static final String RAS_LOGTYPE_CLAIM_HANDWORK = "32";
	/*** 33	取消认领 **/
	public static final String RAS_LOGTYPE_CLAIM_CANCELCLAIM = "33";
	/*** -1	打回 **/
	public static final String RAS_LOGTYPE_RETURN = "-1";
	
	
	//营收导入文件类型
	public static final String RAS_IMPORT_FILE_DICT = "RAS_IMPORT_FILE_DICT";
	public static final String RAS_IMPORT_FILE_TXT = "txt";
	public static final String RAS_IMPORT_FILE_XLS = "xls";
	public static final String RAS_IMPORT_FILE_CSV = "csv";
	public static final String RAS_IMPORT_FILE_XLSX = "xlsx";
	

//	营收稽核 门店授权类型 GRANT_TYPE
	/*** 营收稽核 门店授权类型 query 查询权限**/
	public static final String GRANT_TYPE_QUERY = "query";
	/*** 营收稽核 门店授权类型 audit 稽核权限**/
	public static final String GRANT_TYPE_AUDIT = "audit";

	//POS机绑定状态
	/**01正常**/
	public static final String RAS_POS_BD_STATUS_01 = "01";
	/**02待确认**/
	public static final String RAS_POS_BD_STATUS_02 = "02";
	/**03已借出**/
	public static final String RAS_POS_BD_STATUS_03 = "03";

	//POS机借入状态
	/**01待确认**/
	public static final String RAS_POS_BR_STATUS_01 = "01";
	/**02借用**/
	public static final String RAS_POS_BR_STATUS_02 = "02";
	/**03已归还**/
	public static final String RAS_POS_BR_STATUS_03 = "03";
	/**-1打回**/
	public static final String RAS_POS_BR_STATUS_RETURN = "-1";

	/**RAS_VALID_SIGN数据状态**/
	public static final String RAS_VALID_SIGN_DICT = "RAS_VALID_SIGN";
	/**0禁用**/
	public static final String RAS_VALID_SIGN_0 = "0";
	/**1启用**/
	public static final String RAS_VALID_SIGN_1 = "1";

	//退款管理状态
	/**00待处理**/
	public static final String RAS_BACK_PAY_STATUS_00 = "00";
	/**-1已打回**/
	public static final String RAS_BACK_PAY_STATUS_RETURN_1 = "-1";
	/**10待打回**/
	public static final String RAS_BACK_PAY_STATUS_10 = "10";
	/**11处理中**/
	public static final String RAS_BACK_PAY_STATUS_11 = "11";
	/**95业务结束**/
	public static final String RAS_BACK_PAY_STATUS_95 = "95";

	//退款管理指令发送状态
	/**00处理失败**/
	public static final String RAS_BACK_PAY_SEND_SIGN_00 = "00";
	/**01处理成功**/
	public static final String RAS_BACK_PAY_SEND_SIGN_01 = "01";
	/**11待发送**/
	public static final String RAS_BACK_PAY_SEND_SIGN_11 = "11";
	/**16处理中**/
	public static final String RAS_BACK_PAY_SEND_SIGN_16 = "16";


	//稽核数据状态 RAS_AUDIT_STATUS
	/** 1	待稽核 */
	public static final Integer RAS_AUDIT_STATUS_1 = 1;
	/** 2	稽核中 */
	public static final Integer RAS_AUDIT_STATUS_2 = 2;
	/** 3	已稽核 */
	public static final Integer RAS_AUDIT_STATUS_3 = 3;
	/** 4	差异稽核 */
	public static final Integer RAS_AUDIT_STATUS_4 = 4;
	/** 5	稽核完成 */
	public static final Integer RAS_AUDIT_STATUS_5 = 5;
	/** 6	已汇总 */
	public static final Integer RAS_AUDIT_STATUS_6 = 6;
	/** 0	待确认 */
	public static final Integer RAS_AUDIT_STATUS_0 = 0;
	/** -1	稽核打回 */
	public static final Integer RAS_AUDIT_STATUS_RETURN_1 = -1;
	/** -2	已作废 */
	public static final Integer RAS_AUDIT_STATUS_RETURN_2 = -2;

	//认领状态
	/** 0	待认领 */
	public static final Integer RAS_CLAIM_TYPE_0 = 0;
	/** 1	已认领 */
	public static final Integer RAS_CLAIM_TYPE_1 = 1;



	//导入校验错误提示
	/** * 单位代码和单位名称不能同时为空！**/
	public static final String RAS_EXCEL_MESSAGE_CORP_CODEANDNAME_NOTNULL= "ras.message.corp.codeandname.notnull";
	/** 单位代码与单位名称不匹配 **/
	public static final String RAS_EXCEL_MESSAGE_CORPNAME_ISNOT = "ras.message.corpName.isnot";
	/** 单位代码错误 **/
	public static final String RAS_EXCEL_MESSAGE__CORPCODE_ERROR = "ras.message.corpCode.error";
	/*** 单位名称不存在 **/
	public static final String RAS_EXCEL_MESSAGE_CORPID_ISNOTEXIST = "ras.message.corpid.isnotexist";
	/** * 门店编码和门店名称不能同时为空！**/
	public static final String RAS_EXCEL_MESSAGE_SHOP_CODEANDNAME_NOTNULL = "ras.message.shop.codeandname.notnull";
	/** 门店编码与门店名称不匹配 **/
	public static final String RAS_EXCEL_MESSAGE_SHOP_CODNAME_ISNOT = "ras.message.shop.codename.isnot";
	/** 门店编码错误 **/
	public static final String RAS_EXCEL_MESSAGE_SHOPCODE_ERROR = "ras.message.shopcode.error";
	/*** 门店已禁用! **/
	public static final String RAS_EXCEL_MESSAGE_SHOP_VALIDSIGN_ISNOT= "ras.message.shop.validsign.isnot";
	/*** 门店名称不存在 **/
	public static final String RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTEXIST = "ras.message.shop.isnotexist";
	/** 终端号已经存在！**/
	public static final String RAS_EXCEL_MESSAGE_POSNO_REPEAT = "ras.message.posno.repeat";
	/** 终端号不能为空！**/
	public static final String RAS_EXCEL_MESSAGE_POSNO_NOTNULL = "ras.message.posno.notnull";
	/** * 数据中终端号不能重复！ **/
	public static final String RAS_EXCEL_MESSAGE_POSNO_NOTREPETITION= "ras.message.posno.notrepetition";


	//导入校验错误提示--门店管理
	/** 该门店编码已存在 **/
	public static final String RAS_EXCEL_MESSAGE_SHOPCODE_ISEXIST = "ras.message.shopcode.isexist";
	/*** 门店编码不能为空 **/
	public static final String RAS_EXCEL_MESSAGE_SHOPCODE_ISNOTNULL = "ras.message.shopcode.isnotnull";
	/** 门店名称不能为空**/
	public static final String RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTNULL = "ras.message.shopname.isnotnull";
	/** 门店类别不存在**/
	public static final String RAS_EXCEL_MESSAGE_SHOPTYPE_ISNOTEXIST = "ras.message.shoptype.isnotexist";
	/** 是否单位级别门店不能为空 **/ 
	public static final String RAS_EXCEL_MESSAGE_ISCORP_ISNOTNULL= "ras.message.iscorp.isnotnull";
	/** 开始稽核日期不能为空 **/
	public static final String RAS_EXCEL_MESSAGE_AUDITSTARTDATE_ISNOTNULL= "ras.message.auditstartdate.isnotnull";
	/** 你未拥有该单位的权限 **/
	public static final String RAS_EXCEL_MESSAGE_CORP_ISNOTPOWER= "ras.message.corp.isnotpower";
	/** 该单位下已存在单位级别门店，请修改后再提交   **/
	public static final String RAS_EXCEL_MESSAGE_ISCORP_REPEAT= "ras.shop.iscorp.repeat";
	
	/****** 银行绑卡信息常量类****************/
	/**日志常量 ***/
	/** 新增数据**/
	public static final String RAS_BANK_SAVE_SUCC = "bill.log.save.succ";
	/** 导入数据**/
	public static final String RAS_BANK_SAVE_IMPORT = "ras.log.import.succ";
	/** 修改数据**/
	public static final String RAS_BANK_UPDATE_SUCC = "common.update.data";
	/** 启用数据**/
	public static final String RAS_BANK_UPDATE_START = "ras.shopButton.start";
	/** 禁用数据**/
	public static final String RAS_BANK_UPDATE_ENABLE = "common.button.taboo";
	/** 操作成功**/
	public static final String COMMON_MESSAGE_SUCCESS = "common.message.success";
	/** 操作失败**/
	public static final String COMMON_MESSAGE_OPERATE_FAIL = "common.message.operate.fail";
	/** 作废数据**/
	public static final String RAS_CANCELLATION_MESSAGE_SUCESS = "ras.cancellation.message.sucess";
	
	/** 请选择是否虚拟账户**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ISVIRTUALACC_EMPTY = "ras.message.rasbankcardbinding.isvirtualacc.empty";
	/** 请选择银行账号**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_BANKNO_EMPTY = "ras.message.rasbankcardbinding.bankno.empty";
	/** 请选择银行账户名称**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ACCNAME_EMPTY = "ras.message.rasbankcardbinding.accname.empty";
	/** 请选择银行开户行名称**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ACCBANKNAME_EMPTY = "ras.message.rasbankcardbinding.accbankname.empty";
	/** 请选择关联实体账户**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ENTITYACCNO_EMPTY = "ras.message.rasbankcardbinding.entityaccno.empty";
	/** 该银行账号已被绑定**/
	public static final String RAS_RASBANKCARDBINDING_MESSAGE_REPEAT = "ras.rasbankcardbinding.message.repeat";
	/** 银行账号不存在**/
	public static final String RAS_RASBANKCARDBINDING_BANKNO_NEGATION = "ras.message.rasbankcardbinding.bankno.negation";
	/** 银行账户名称和系统不匹配**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ACCNAME_NOTLIKE = "ras.message.rasbankcardbinding.accname.notlike";
	/** 开户行户名称和系统不匹配**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ACCBANKNAME_NOTLIKE = "ras.message.rasbankcardbinding.accbankname.notlike";
	/** 实体账号与系统不匹配**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ENTITYACCNO_NOTLIKE = "ras.message.rasbankcardbinding.entityaccno.notlike";
	
	/*普通常量 **/
	/** 新增**/
	public static final String RAS_BANK_OPERATE_STATUS_SAVE = "save";
	/** 修改**/
	public static final String RAS_BANK_OPERATE_STATUS_UPDATE = "update";
	/** 导入**/
	public static final String RAS_BANK_OPERATE_STATUS_IMPORT = "import";
	/** 开启**/
	public static final String RAS_BANK_OPERATE_STATUS_OPEN = "open";
	/** 关闭**/
	public static final String RAS_BANK_OPERATE_STATUS_CLOSE = "close";
	/** 营收**/
	public static final String RAS_BANK_OPERATE_STATUS_REVENUE = "revenue";
	/** 实收**/
	public static final String RAS_BANK_OPERATE_STATUS_RECEIVED = "received";
	
	/** 缴付方式字典**/
	public static final String RAS_OTHER_CARD_DICT= "RAS_PAY_TYPE";
	
	/** 是否虚拟账户 ***/
	public static final String RAS_BANK_BANKCARD_ISVIRTUALACC_STATUS_OK = "是";
	public static final String RAS_BANK_BANKCARD_ISVIRTUALACC_STATUS_NO = "否";
	
	/** 此账号已绑定门店，请修改后再提交**/
	public static final String RAS_RASBANKCARDBINDING_MESSAGE_EXIST = "ras.rasothercardbinding.message.exist";
	public static final String RAS_RASOTHERCARDBINDING_MESSAGE_REPEAT = "ras.rasothercardbinding.message.repeat";
	/** 缴款方式不能为空**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EMPTY = "ras.message.rasbankcardbinding.paytype.empty";
	/** 缴款方式不存在系统**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EXIST = "ras.message.rasbankcardbinding.paytype.exist";
	/** 账号不能为空**/
	public static final String RAS_MESSAGE_RASBANKCARDBINDING_ACCNO_EMPTY = "ras.message.rasbankcardbinding.accno.empty";
	/** 账号名称不能为空**/
	public static final String RAS_MESSAGE_RASOTHERCARDBINDING_ACCNAME_EMPTY = "ras.message.rasothercardbinding.accname.empty";
	/** 导入营收数据不允许重复**/
	public static final String RAS_IMPORT_REVENUE_REPEAT_EXIST = "ras.import.revenue.repeat.exist";
	/** 导入营收数据门店不存在**/
	public static final String RAS_IMPORT_REVENUE_NOT_EXIST = "ras.import.revenue.not.exist";
	/** 交易时间不能为空**/
	public static final String RAS_IMPORT_TRANSDATE_NOTNULL = "ras.import.revenue.transdate.notnull";
	/** 交易时间格式问题**/
	public static final String RAS_IMPORT_TRANSDATE_FORMAT_ERROR = "ras.import.revenue.transdate.format.error";
	/** 营收金额不能为空**/
	public static final String RAS_IMPORT_REVENUE_MONEY_NOTNULL = "ras.import.revenue.money.notnull";
	/** 营/实收导入没有相关账户权限 **/
	public static final String RAS_IMPORT_SHOP_NOT_POWER = "ras.import.shop.not.power";
	/** 实收金额不能为空**/
	public static final String RAS_RASRECEIVEDDTL_IS_NOTNULL = "ras.rasreceiveddtl.is.notnull";
	
	/** 实收是否有分隔符**/
	public static final String RAS_RECEIVED_IMPORT_ISSEPARATOR = "ras.received.import.isseparator";
	/** 分隔符国际化名称**/
	public static final String TABLE_RAS_RECEIVED_IMPORT_SEPARATOR = "table.ras.received.import.separator";
	/** 实收分隔符**/
	public static final String RAS_RECEIVED_SEPARATOR = "RAS_RECEIVED_SEPARATOR";
	/** 实收分隔符 状态，1是，0否**/
	public static final String RAS_RECEIVED_SEPARATOR_YES = "1";
	public static final String RAS_RECEIVED_SEPARATOR_NO = "0";
	
	
	
	/**稽核规则 拼接字符串 ,**/
	public static final String RAS_AUDIT_RULES_SEPARATORCHARS = ",";
	/**稽核规则 RAS_AUDIT_RULES**/
	public static final String RAS_AUDIT_RULES_DICT = "RAS_AUDIT_RULES";
	/***00	金额 **/
	public static final String RAS_AUDIT_RULES_MONEY = "00";
	/** 稽核规则	01	订单号**/
	public static final String RAS_AUDIT_RULES_ORDERNO = "01";
	/** 稽核规则	02	交易日期**/
	public static final String RAS_AUDIT_RULES_TRANSDATE = "02";
	/** 稽核规则	03	交易流水**/
	public static final String RAS_AUDIT_RULES_SERIALNO = "03";
	/** 稽核规则	04	现金汇总稽核**/
	public static final String RAS_AUDIT_RULES_MONEY_COUNT = "04";
	/** 稽核规则	05	其他方式汇总稽核**/
	public static final String RAS_AUDIT_RULES_OTHER_COUNT = "05";

	/**稽核执行缴款方式 拼接字符串 ,**/
	public static final String RAS_AUDIT_TASK_SEPARATORCHARS = ",";
	/***稽核任务运行状态 RAS_RUN_STATUS	**/
	public static final String RAS_RUN_STATUS_DICT = "RAS_RUN_STATUS";
	/***定时任务状态 S	成功**/
	public static final String RAS_RUN_STATUS_S = "S";
	/***定时任务状态 F	失败**/
	public static final String RAS_RUN_STATUS_F = "F";
	/***定时任务状态 R	处理中**/
	public static final String RAS_RUN_STATUS_R = "R";
	
	/***状态是否标记 1是**/
	public static final String RAS_STATUS_YES = "1";
	/***状态是否标记 0否**/
	public static final String RAS_STATUS_NO = "0";

	/** 营收稽核稽核任务操作类型 addOp,updateOp,taskOp **/
	public static final String RAS_TASK_OPTYPE_ADDOP = "addOp";
	public static final String RAS_TASK_OPTYPE_UPDATEOP = "updateOp";
	public static final String RAS_TASK_OPTYPE_TASKOP = "taskOp";
	public static final String RAS_TASK_OPTYPE_CLONEOP = "cloneOp";
	
	/** 自动任务操作KEY ***/
	public static final String RAS_AUTOTASK_OPKEY = "autoTaskOp";


	public static final String RAS_AUTOT_REVENUE_ALIAS = "ry";
	/** 营收数据 匹配字段 money金额 ***/
	public static final String RAS_AUTOT_REVENUE_FIELD_MONEY = "money";
	/** 营收数据 匹配字段 order_no 订单号 ***/
	public static final String RAS_AUTOT_REVENUE_FIELD_ORDERNO = "order_no";
	/** 营收数据 匹配字段trans_date交易日期 ***/
	public static final String RAS_AUTOT_REVENUE_FIELD_TRANSDATE = "trans_date";
	/** 营收数据 匹配字段str_trans_date交易日期 ***/
	public static final String RAS_AUTOT_REVENUE_FIELD_STRTRANSDATE = "str_trans_date";
	/** 营收数据 匹配字段 serial_no 流水号 ***/
	public static final String RAS_AUTOT_REVENUE_FIELD_SERIALNO = "serial_no";
	

	/**进行缓存取数分页设置每页大小**/
	public static final int RAS_AUTOT_MAXCACHE_RESULT = 100001;
	/**进行取数连接每次获取数量**/
	public static final int RAS_AUTOT_MAXCACHE_FETCHSIZE = 20000;
	/**自动稽核进行多线程处理，线程池大小**/
	public static final int RAS_AUTOT_THREAD_SIZE = 5;
	/**进行公共分页设置每页大小**/
	public static final int RAS_AUTOT_MAXRESULT = 10000;
	public static final String RAS_AUTOT_RECEIVED_ALIAS = "rs";
	/** 实收数据 匹配字段 trans_money金额 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_MONEY = "money";
	public static final String RAS_AUTOT_RECEIVED_FIELD_TRANSMONEY = "trans_money";
//	public static final String RAS_AUTOT_RECEIVED_FIELD_MONEY = "money";
	/** 实收数据 匹配字段 transMoney金额别名 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_MONEY_ALIAS = "money";
	public static final String RAS_AUTOT_RECEIVED_FIELD_TRANSMONEY_ALIAS = "transMoney";
	/** 实收数据 匹配字段 order_no 订单号 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_ORDERNO = "order_no";
	/** 实收数据 匹配字段trans_date交易日期 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_TRANSDATE = "trans_date";
	/** 实收数据 匹配字段Str_trans_date交易日期 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_STRTRANSDATE = "str_trans_date";
	/** 实收数据 匹配字段 serial_no 流水号 ***/
	public static final String RAS_AUTOT_RECEIVED_FIELD_SERIALNO = "serial_no";
	

	//稽核类型:0自动稽核1手工稽核
	public static final String RAS_AUDIT_TYPE_DICT = "RAS_AUDIT_TYPE";
	/** 1	手动稽核 */
	public static final String RAS_AUDIT_TYPE_1 = "1";
	/** 0	自动稽核 */
	public static final String RAS_AUDIT_TYPE_0 = "0";
	

	/** 营收稽核 手工稽核操作类型 manual手工稽核,less差异稽核,auto 自动稽核 **/
	public static final String RAS_AUDIT_OPTYPE_MANUAL = "manual";
	public static final String RAS_AUDIT_OPTYPE_LESS = "less";
	public static final String RAS_AUDIT_OPTYPE_AUTO = "auto";
	
	public static final String RAS_AUDIT_SENDMSG_ECINFO_MORE= "多缴款";
	public static final String RAS_AUDIT_SENDMSG_ECINFO_FEW= "少缴款";
	
	/**营收、实收*/
	public static final String RAS_STRATEGY_YS= "ras.strategy.ys";
	/**一致*/
	public static final String RAS_STRATEGY_AGREE= "ras.strategy.agree";
	/**分别汇总*/
	public static final String RAS_STRATEGY_SEP= "ras.strategy.sep";
	
	public static String RAS_MAP_KEYSEPARATORCHARS=";,;";
	
}
