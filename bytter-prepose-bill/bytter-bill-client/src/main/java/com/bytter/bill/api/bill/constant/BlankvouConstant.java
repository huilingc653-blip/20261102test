package com.bytter.bill.api.bill.constant;
/**
 * 空白凭证模块常量类
 * @author luop
 *
 */
public class BlankvouConstant {
	/*** 有效标志：0否 */
	public static final String VALID_SIGN_0 = "0";
	/*** 有效标志：1是 */
	public static final String VALID_SIGN_1 = "1";
	
	/**
	 * 空白凭证入库-暂存
	 */
	public static final int BVVOUINPUT_STATUS_CREATE = 0;
	/**
	 * 空白凭证入库-已入库
	 */
	public static final int BVVOUINPUT_STATUS_STORAGE = 95;
	/**
	 * 空白凭证入库-已删除
	 */
	public static final int BVVOUINPUT_STATUS_DELETE = -2;
	/**
	 * 操作-保存
	 */
	public static final String BILL_OPERATE_SAVE = "save";
	/**
	 * 操作-修改
	 */
	public static final String BILL_OPERATE_EDIT = "edit";
	/**
	 * 操作-入库
	 */
	public static final String BILL_OPERATE_STORAGE = "stotage";
	/**
	 * 操作-审核
	 */
	public static final String BILL_OPERATE_CHECK = "check";
	/**
	 * 空白凭证明细-已入库
	 */
	public static final String BVVOUINFODTL_STATUS_STORAGE = "01";
	/**
	 * 空白凭证明细-已出售
	 */
	public static final String BVVOUINFODTL_STATUS_SELL = "02";
	/**
	 * 空白凭证明细-已使用
	 */
	public static final String BVVOUINFODTL_STATUS_USE = "03";
	/**
	 * 空白凭证明细-已退票
	 */
	public static final String BVVOUINFODTL_STATUS_RETREAT = "04";
	/**
	 * 空白凭证明细-已丢失
	 */
	public static final String BVVOUINFODTL_STATUS_LOSE  = "05";
	/**
	 * 空白凭证明细-已作废
	 */
	public static final String BVVOUINFODTL_STATUS_TOVOID = "06";
	//收款支票状态 状态：-2已删除，-1已打回，0已登记，1复核中，2已入库，3已退票，4已作废，5托收中，6已到账
	/*** 收款支票状态 -2已删除  */
	public static final Integer BV_CHEQUE_STATUS_DELETE=-2;
	/*** 收款支票状态 -1已打回  */
	public static final Integer BV_CHEQUE_STATUS_BACK=-1;
	/*** 收款支票状态 0已登记  */
	public static final Integer BV_CHEQUE_STATUS_SAVE=0;
	/*** 收款支票状态 1复核中  */
	public static final Integer BV_CHEQUE_STATUS_CHECK=1;
	/*** 收款支票状态 2已入库 */
	public static final Integer BV_CHEQUE_STATUS_INPUT=2;
	/*** 收款支票状态 3已退票  */
	public static final Integer BV_CHEQUE_STATUS_BACKBILL=3;
	/*** 收款支票状态 4已作废  */
	public static final Integer BV_CHEQUE_STATUS_CANCELLATION=4;
	/*** 收款支票状态 5托收中  */
	public static final Integer BV_CHEQUE_STATUS_COLLECTION=5;
	/*** 收款支票状态 6已到账  */
	public static final Integer BV_CHEQUE_STATUS_ARRIVALACCOUNT=6;
	//凭证管理日志信息
	//01新增02修改03删除04启用05停用06入库07复制10打印11重新打印20使用21出售22丢失23作废24退票25移库26导入27提交28复核打回29复核通过
	/**
	 * 凭证管理日志01新增
	 */
	public static final String BVLOG_LOGTYPE_ADD="01";
	/**
	 * 凭证管理日志02修改
	 */
	public static final String BVLOG_LOGTYPE_UPDATE="02";
	/**
	 * 凭证管理日志03删除
	 */
	public static final String BVLOG_LOGTYPE_DELETE="03";
	/**
	 * 凭证管理日志04启用
	 */
	public static final String BVLOG_LOGTYPE_START="04";
	/**
	 * 凭证管理日志05停用
	 */
	public static final String BVLOG_LOGTYPE_STOP="05";
	/**
	 * 凭证管理日志06入库
	 */
	public static final String BVLOG_LOGTYPE_INPUT="06";
	/**
	 * 凭证管理日志07复制
	 */
	public static final String BVLOG_LOGTYPE_COPY="07";
	/**
	 * 凭证管理日志10打印
	 */
	public static final String BVLOG_LOGTYPE_PRINT="10";
	/**
	 * 凭证管理日志11重新打印
	 */
	public static final String BVLOG_LOGTYPE_REPRINT="11";
	/**
	 * 凭证管理日志20使用
	 */
	public static final String BVLOG_LOGTYPE_USE="20";
	/**
	 * 凭证管理日志21出售
	 */
	public static final String BVLOG_LOGTYPE_SELL="21";
	/**
	 * 凭证管理日志22丢失
	 */
	public static final String BVLOG_LOGTYPE_LOSE="22";
	/**
	 * 凭证管理日志23作废
	 */
	public static final String BVLOG_LOGTYPE_CANCELLATION="23";
	/**
	 * 凭证管理日志24退票
	 */
	public static final String BVLOG_LOGTYPE_BACKBILL="24";
	/**
	 * 凭证管理日志25移库
	 */
	public static final String BVLOG_LOGTYPE_TRANSFER="25";
	/**
	 * 凭证管理日志26导入
	 */
	public static final String BVLOG_LOGTYPE_IMPORT="26";
	/**
	 * 凭证管理日志27提交
	 */
	public static final String BVLOG_LOGTYPE_SUBMIT="27";
	/**
	 * 凭证管理日志28复核打回
	 */
	public static final String BVLOG_LOGTYPE_CHECKBACK="28";
	/**
	 * 凭证管理日志29复核通过
	 */
	public static final String BVLOG_LOGTYPE_CHECKOK="29";
	/**
	 * 支票登记日志打回
	 */
	public static final String BVLOG_LOGTYPE_BACK="30";
	/**
	 * 凭证管理日志31 托收申请
	 */
	public static final String BVLOG_LOGTYPE_COLLECTION="31";
	/**
	 * 凭证管理日志32 到账
	 */
	public static final String BVLOG_LOGTYPE_ARRIVALACCOUNT="32";
	//国际化取值
	/** 国际化 凭证类型取值*/
	public static final String LANG_VOUCHER_TYPE="table.bvPrintFormat.voucherType";
	/**新增数据成功！*/
	public static final String LANG_LOG_SAVE_SUCC="bill.log.save.succ";
	/*** 修改数据成功！*/
	public static final String LANG_LOG_UPDATE_SUCC="bill.log.update.succ";
	/*** 删除数据成功！*/
	public static final String LANG_LOG_DELETE_SUCC="bill.log.delete.succ";
	/*** 复制数据成功！*/
	public static final String LANG_LOG_COPY_SUCC="bill.log.copy.succ";
	/*** 打印数据成功！*/
	public static final String LANG_LOG_PRINT_SUCC="bill.log.print.succ";
	/*** 重新打印数据成功！*/
	public static final String LANG_LOG_REPPRINT_SUCC="bill.log.repprint.succ";
	/*** 批量删除数据成功！*/
	public static final String LANG_LOG_BATCHDELETE_SUCC="bill.log.batchdelete.succ";
	/*** 批量移库数据成功！*/
	public static final String LANG_LOG_BATCHTRANSF_SUCC="bill.log.batchtransf.succ";
	/**单位代码不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_CORPCODE_ISNOTNULL="blankvou.bvvouinput.corpcode.message.isnotnull";
	/**单位代码不存在！*/
	public static final String BLANKVOU_BVVOUINPUT_CORPCODE_ISERROR="blankvou.bvvouinput.corpcode.message.iserror";
	/**所属库箱不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_SBOXNAME_ISNOTNULL="blankvou.bvvouinput.sboxname.message.isnotnull";
	/**所属库箱名称错误或无权限！*/
	public static final String BLANKVOU_BVVOUINPUT_SBOXNAME_ISERROR="blankvou.bvvouinput.sboxname.message.iserror";
	/**凭证种类不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_VOUTNAME_ISNOTNULL="blankvou.bvvouinput.voutname.message.isnotnull";
	/**凭证种类名称错误不存在！*/
	public static final String BLANKVOU_BVVOUINPUT_VOUTNAME_ISERROR="blankvou.bvvouinput.voutname.message.iserror";
	/**编码规则不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_RULENO_ISNOTNULL="blankvou.bvvouinput.ruleno.message.isnotnull";
	/**编码规则格式不正确！*/
	public static final String BLANKVOU_BVVOUINPUT_RULENO_ISERROR="blankvou.bvvouinput.ruleno.message.iserror";
	/**起始号码不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_BEGINNO_ISNOTNULL="blankvou.bvvouinput.beginno.message.isnotnull";
	/**截止号码不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_ENDNO_ISNOTNULL="blankvou.bvvouinput.endno.message.isnotnull";
	/**起始、截止号码位数错误不能超过编码规则*的长度！*/
	public static final String BLANKVOU_BVVOUINPUT_RULENO_ISLENGTHERROR="blankvou.bvvouinput.ruleno.message.isLengthError";
	/**起始、截止号码必须为数字！*/
	public static final String BLANKVOU_BVVOUINPUT_RULENO_ISSTRINGERROR="blankvou.bvvouinput.ruleno.message.isStringError";
	/**截止号码必须大于起始号码！*/
	public static final String BLANKVOU_BVVOUINPUT_RULENO_ISSIZEERROR="blankvou.bvvouinput.ruleno.message.isSizeError";
	/**购入日期不允许为空！*/
	public static final String BLANKVOU_BVVOUINPUT_INPUTDATE_ISNOTNULL="blankvou.bvvouinput.inputdate.message.isnotnull";
	/**银行账号不存在！*/
	public static final String BLANKVOU_BVVOUINPUT_BANKACC_ISNULL="blankvou.bvvouinput.bankacc.message.isnull";
	/**银行账号非此单位的账户！*/
	public static final String BLANKVOU_BVVOUINPUT_BANKACC_ISNOTCORP="blankvou.bvvouinput.bankacc.message.isnotcorp";
	/*** 凭证维护-数据来源-手动录入*/
	public static final String BVVOUEMPLOYINFO_DATASOURCE_MANUAL="01";
	/**凭证维护-数据来源-打印占用*/
	public static final String BVVOUEMPLOYINFO_DATASOURCE_PRINT="02";
//	匹配规则 MATCH_RULE
//	字段代码	字段说明
//	01	全匹配
//	02	包含匹配
//	03	左匹配右包含
//	04	右匹配左匹配
//	05	区间匹配（数字“,”隔开）
	/**
	 * 打印格式匹配规则 - 01 全匹配
	 */
	public static final String BV_PRINT_MATCH_RULE_ALL="01";
	/**
	 * 打印格式匹配规则 - 02 包含匹配
	 */
	public static final String BV_PRINT_MATCH_RULE_LIKE="02";
	/**
	 * 打印格式匹配规则 - 03 左匹配右包含
	 */
	public static final String BV_PRINT_MATCH_RULE_LLIKE="03";
	/**
	 * 打印格式匹配规则 - 04 右匹配左匹配
	 */
	public static final String BV_PRINT_MATCH_RULE_RLIKE="04";
	/**
	 * 打印格式匹配规则 - 05 区间匹配（数字“,”隔开）
	 */
	public static final String BV_PRINT_MATCH_RULE_INTERVAL="05";
//	值打印类型 VAL_PRINT_TYPE
//	字段代码	字段说明
//	01	普通字符
//	02	金额数字
//	03	金额中文
//	04	日期数字
//	05	日期中文
//	06	金额数字中文
	/**
	 * 打印格式值打印类型 - 01	普通字符
	 */
	public static final String BV_PRINT_VALTYPE_STR="01";
	/**
	 * 打印格式值打印类型 - 02	金额数字
	 */
	public static final String BV_PRINT_VALTYPE_MONEYNUM="02";
	/**
	 * 打印格式值打印类型 - 03	金额中文
	 */
	public static final String BV_PRINT_VALTYPE_MONEYCNY="03";
	/**
	 * 打印格式值打印类型 - 06	金额数字中文
	 */
	public static final String BV_PRINT_VALTYPE_MONEYNUMCNY="06";
	/**
	 * 打印格式值打印类型 - 04	日期数字
	 */
	public static final String BV_PRINT_VALTYPE_DATENUM="04";
	/**
	 * 打印格式值打印类型 - 05	日期中文
	 */
	public static final String BV_PRINT_VALTYPE_DATECNY="05";
	/**
	 * 打印格式值打印类型 - 07 电子签章
	 */
	public static final String BV_PRINT_VALTYPE_SIGNATURE="07";
//	凭证打印格式明细 BV_PRINT_FORMAT_DTL
//	值打印类型格式 VAL_PRINT_FMT
//	字段代码	字段说明
//	01	日期年YYYY
//	02	日期月MM
//	03	日期天DD
//	04	日期yyyy-MM-dd
//	05	日期yyyyMMdd
//	06	金额####.00
//	07	金额#,###.00
//	08	日期yyyy年MM月dd日
	/**
	 * 打印格式值打印类型格式 - 01	日期年YYYY
	 */
	public static final String BV_PRINT_VALTYPEFMT_YYYY="01";
	/**
	 * 打印格式值打印类型格式 - 02	日期月MM
	 */
	public static final String BV_PRINT_VALTYPEFMT_MM="02";
	/**
	 * 打印格式值打印类型格式 - 03	日期天DD
	 */
	public static final String BV_PRINT_VALTYPEFMT_DD="03";
	/**
	 * 打印格式值打印类型格式 - 04	日期yyyy-MM-dd
	 */
	public static final String BV_PRINT_VALTYPEFMT_YYYY_MM_DD="04";
	/**
	 * 打印格式值打印类型格式 - 05	日期年yyyyMMdd
	 */
	public static final String BV_PRINT_VALTYPEFMT_YYYYMMDD="05";
	/**
	 * 打印格式值打印类型格式 - 06	金额####.00
	 */
	public static final String BV_PRINT_VALTYPEFMT_MONEY1="06";
	/**
	 * 打印格式值打印类型格式 - 07	金额#,###.00
	 */
	public static final String BV_PRINT_VALTYPEFMT_MONEY2="07";
	/**
	 * 打印格式值打印类型格式 - 08	YYYY年MM月DD日
	 */
	public static final String BV_PRINT_VALTYPEFMT_YYYYMMDD_CNY="08";
	/**
	 * 凭证类型取数来源数据类型 1金额
	 */
	public static final String BV_VOUTYPE_DATATYPE_MONEY="1";
	/**
	 * 凭证类型取数来源数据类型 2银行账户
	 */
	public static final String BV_VOUTYPE_DATATYPE_BANKACC="2";
	/**
	 * 凭证类型取数来源数据类型 3银行大类
	 */
	public static final String BV_VOUTYPE_DATATYPE_BANKTYPE="3";
	/**
	 * 凭证类型取数来源数据类型 4电子签章
	 */
	public static final String BV_VOUTYPE_DATATYPE_SIGNATURE="4";
	/**
	 * 凭证类型取数来源数据类型 5套打完成回调Bean
	 */
	public static final String BV_VOUTYPE_DATATYPE_SERVICEBEAN="5";
	
	/**
	 * 打印场景 系统默认 多语言UUID
	 */
	public static final String BV_OTHERSCENE_UUID="9c771b14234c4c19b0cbf733545other";
	/**
	 * 打印场景 系统默认 国际化key
	 */
	public static final String BV_OTHERSCENE_KEY="blankvou.key.otherScene";
	/**
	 * 数据唯一id 国际化key
	 */
	public static final String BV_OPID_LONG_KEY="bv.key.dataId";
	/**
	 * 对应单位id 国际化key
	 */
	public static final String BV_OPCORPID_LONG_KEY="bv.key.corp_id";
	/**
	 * 操作人代码  国际化key
	 */
	public static final String BV_OPCODE_LONG_KEY="bv.key.op_code";
	/**
	 * 操作人名称 国际化key
	 */
	public static final String BV_OPNAME_LONG_KEY="bv.key.op_name";
	/**
	 * 操作日期  国际化key
	 */
	public static final String BV_OPDATE_LONG_KEY="bv.key.op_date";
	/**
	 * 套打数据单位id corp_id
	 */
	public static final String BV_VOUTYPE_CODE_OPCORPID="corp_id";
	/**
	 * 套打操作人代码 popopcode
	 */
	public static final String BV_VOUTYPE_CODE_OPCODE="popopcode";
	/**
	 * 套打操作人名称 popopname
	 */
	public static final String BV_VOUTYPE_CODE_OPNAME="popopname";
	/**
	 * 套打操作日期 popopdate
	 */
	public static final String BV_VOUTYPE_CODE_OPDATE="popopdate";
	/**
	 * 套打操作金额 voumoneypop
	 */
	public static final String BV_VOUTYPE_CODE_OPMONEY="voumoneypop";
	/**
	 * 套打操作银行账户 voubankaccpop
	 */
	public static final String BV_VOUTYPE_CODE_OPBANKACC="voubankaccpop";
	/**
	 * 套打操作银行大类 voubanktypepop
	 */
	public static final String BV_VOUTYPE_CODE_OPBANKTYPE="voubanktypepop";
	/**
	 * 套打操作回调处理实现IVoucherPrintService的bean servicebeanpop
	 */
	public static final String BV_VOUTYPE_CODE_SERVICEBEAN="servicebeanpop";
	/*** 凭证类型维护新增资源*/
	public static final String AB_BILL_RESOURCE_BVVOUCHERTYPE_SAVE = "bill:blankvou:bvVoucherType:save";
	/*** 凭证类型维护删除资源*/
	public static final String AB_BILL_RESOURCE_BVVOUCHERTYPE_DELETE = "bill:blankvou:bvVoucherType:delete";
	/*** 凭证类型维护修改资源*/
	public static final String AB_BILL_RESOURCE_BVVOUCHERTYPE_UPDATE = "bill:blankvou:bvVoucherType:update";
	/*** 凭证类型维护查询资源*/
	public static final String AB_BILL_RESOURCE_BVVOUCHERTYPE_QUERY = "bill:blankvou:bvVoucherType:query";
	/*** 空白凭证库箱维护新增资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_SAVE = "bill:blankvou:bvStorageBox:save";
	/*** 空白凭证库箱维护删除资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_DELETE = "bill:blankvou:bvStorageBox:delete";
	/*** 空白凭证库箱维护修改资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_UPDATE = "bill:blankvou:bvStorageBox:update";
	/*** 空白凭证库箱维护启用资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_START = "bill:blankvou:bvStorageBox:start";
	/*** 空白凭证库箱维护停止资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_STOP = "bill:blankvou:bvStorageBox:stop";
	/*** 空白凭证库箱维护查询资源*/
	public static final String AB_BILL_RESOURCE_BVSTORAGEBOX_QUERY = "bill:blankvou:bvStorageBox:query";
	/*** 套打格式维护新增资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTFORMAT_SAVE = "bill:blankvou:bvPrintFormat:save";
	/*** 套打格式维护删除资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTFORMAT_DELETE = "bill:blankvou:bvPrintFormat:delete";
	/*** 套打格式维护修改资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTFORMAT_UPDATE = "bill:blankvou:bvPrintFormat:update";
	/*** 套打格式维护复制资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTFORMAT_COPY = "bill:blankvou:bvPrintFormat:copy";
	/*** 套打格式维护查询资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTFORMAT_QUERY = "bill:blankvou:bvPrintFormat:query";
	/*** 凭证入库新增资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINPUT_SAVE = "bill:blankvou:bvVouInput:save";
	/*** 凭证入库删除资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINPUT_DELETE = "bill:blankvou:bvVouInput:delete";
	/*** 凭证入库修改资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINPUT_UPDATE = "bill:blankvou:bvVouInput:update";
	/*** 凭证入库入库资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINPUT_STORAGE = "bill:blankvou:bvVouInput:storage";
	/*** 凭证入库查询资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINPUT_QUERY = "bill:blankvou:bvVouInput:query";
	/*** 凭证维护出售资源*/
	public static final String AB_BILL_RESOURCE_BBVVOUINFODTL_SELL = "bill:blankvou:bvVouInfoDtl:sell";
	/*** 凭证维护丢失资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTL_LOSE = "bill:blankvou:bvVouInfoDtl:lose";
	/*** 凭证维护作废资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTL_CANCEL = "bill:blankvou:bvVouInfoDtl:cancel";
	/*** 凭证维护退票资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTL_RETURNTICKET = "bill:blankvou:bvVouInfoDtl:returnTicket";
	/*** 凭证维护移库资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTL_TRANSFER = "bill:blankvou:bvVouInfoDtl:transfer";
	/*** 凭证维护查询资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTL_QUERY = "bill:blankvou:bvVouInfoDtl:query";
	/*** 凭证查询查询资源*/
	public static final String AB_BILL_RESOURCE_BVVOUINFODTLQUERY_QUERY = "bill:blankvou:bvVouInfoDtlQuery:query";
	/*** 凭证打印打印资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTVOUOPINFO_PRINT = "bill:blankvou:bvPrintVouOpinfo:print";
	/*** 凭证打印查询资源*/
	public static final String AB_BILL_RESOURCE_BVPRINTVOUOPINFO_QUERY = "bill:blankvou:bvPrintVouOpinfo:query";
	/*** 支票台账退票资源*/
	public static final String AB_BILL_RESOURCE_BVPAYEECHEQUEODTL_RETURNTICKET = "blankvou.bvPayeeChequeDtl.returnTicket";
	/*** 支票台账作废资源*/
	public static final String AB_BILL_RESOURCE_BVPAYEECHEQUEDTL_CANCEL = "blankvou.bvPayeeChequeDtl.cancel";
	/*** 支票台账托收申请成功*/
	public static final String BLANKVOU_BYPAYEECHEQUE_MSG_COLLECTION_SUCC = "blankvou.bvPayeeCheque.collection.succ";
	/*** 支票台账到账成功*/
	public static final String BLANKVOU_BYPAYEECHEQUE_MSG_ARRIVALACCOUNT_SUCC = "blankvou.bvPayeeCheque.arrivalAccount.succ";

	/***
	 * 校验数据源错误信息
	 */
	public static final String BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE = "blankvou.message.error.checkdatasource";
}
