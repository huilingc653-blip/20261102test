package com.bytter.biz.finacial.api.enums.paramconfig;
import com.bytter.core.enums.IMessageEnum;
/**
 * 参数配置枚举
 * @author caozx
 * @since 2017-7-5
 */
public enum ParamConfigMessageEnum implements IMessageEnum {
	
	/**
	 * 01 核算基础数据维护 
	 * xx1 会计科目
	 * xx2 凭证种类管理
	 * xx3 摘要词组维护
	 * xx4 业务种类管理
	 * xx5 科目利率管理
	 * xx6 月结区间管理
	 * xx7 报表公式设计
	 * xx8 记账规则管理
	 * xx9 手续费
	 * xx0 核算系统参数
	 */
	/**科目代码不能为空*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NULL("01101"),
	/**科目代码已存在*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_REPEAT("01102"),
	/**科目名称不能为空*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTNAME_NULL("01103"),
	/**余额方向不能为空*/
	FIN_PARAMCONFIG_FCSUBJECT_BALANCEWAY_NULL("01104"),
	/**计息标志不能为空*/
	FIN_PARAMCONFIG_FCSUBJECT_INTERESTSIGN_NULL("01105"),
	/**开户种类不能为空*/
	FIN_PARAMCONFIG_FCSUBJECT_ACCTYPE_NULL("01106"),
	/**科目代码被账号信息引用*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_USE("01107"),
	/**科目代码下存在子科目*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NODE("01108"),
	/**未找到此科目的父科目，请先保存父科目*/
	FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NOT_FATHER("01109"),
	/** 摘要内容已存在 */
	FIN_PARAMCONFIG_SUMMARY_EXISTS("01301"),
	/** 业务种类必须唯一 */
	FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE("01401"),
	/**该业务种类已经被引用，不能删除*/
	FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISUSED("01402"),
	/** 业务种类必须唯一*/
	FIN_PARAMCONFIG_MESSAGE_BUSRACE_UNIQUE_POOL("01403"),
	/**该业务种类已经被引用，不能编辑*/
	FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISNOTEDIT("01404"),
	
	/** 该科目利率在此执行日期后已存在生效利率，不允许再次调整*/
	FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISEXIST("01501"),
	/** 该科目利率存在未生效利率，不允许再次调整*/
	FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISNOTUSED("01502"),
	/** 该科目利率利率已执行，不允许再次修改*/
	FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISUSED("01503"),
	/** 该科目利率利率存在已复核的内部账号调整记录，不能删除*/
	FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISCHECK("01504"),
	/** 该科目已被使用，不能停用*/
	FIN_PARAMCONFIG_MESSAGE_INSTRATE_ISEMPLOY("01505"),
	/**记账规则分录不能为空*/
	FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL("01801"),
	/**记账规则分录借贷必须同时存在*/
	FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE("01802"),
	/**记账规则分录借贷必须同时存在*/
	FIN_PARAMCONFIG_BUSINESS_SAME("01803"),
	/**该记账规则已经被引用，不能删除*/
	FIN_PARAMCONFIG_MESSAGE_RULE_ISUSED("01804"),
    /**记账规则分录科目不能为空*/
    FIN_PARAMCONFIG_BUSINESS_SUBJECT_NULL("01805"),
	/**费用明细不能为空*/
	FIN_PARAMCONFIG_CHARGE_DTLS_NULL("01901"),
	/**费用类型已被引用*/
	FIN_PARAMCONFIG_CHARGE_TYPE_USE("01902"),
	/**费用代码已存在*/
	FIN_PARAMCONFIG_CHARGE_TYPE_CODE_UNIQUE("01903"),
	/**业务规则未定义*/
	FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE("01904"),
	/**行号不能为空*/
	FIN_PARAMCONFIG_MESSAGE_REPORT_LINENO_NOTNULL("01905"),
	/**项目名不能为空*/
	FIN_PARAMCONFIG_MESSAGE_ITEM_NOTNULL("01906"),
	/**左右不能为空*/
	FIN_PARAMCONFIG_MESSAGE_DIRECTION_NOTNULL("01915"),
	/**该网点下的报表格式为空*/
	FIN_PARAMCONFIG_MESSAGE_REPORT_NOTNULL("01907"),
	/**网点不能为空**/
	FIN_PARAMCONFIG_MESSAGE_NETID_NOTNULL("01908"),
	/**科目不能为空*/
	FIN_PARAMCONFIG_MESSAGE_SUBJECTID_NOTNULL("01909"),
	/**币别不能为空*/
	FIN_PARAMCONFIG_MESSAGE_CURID_NOTNULL("01910"),
	/**业务种类代码长度必须小于10个字符*/
	TABLE_FCBUSRACE_RACECODELENGTHBEYOND("01911"),
	/**业务种类名称长度必须小于60个字符*/
	TABLE_FCBUSRACE_RACENAMELENGTHBEYOND("01912"),
	/**不能同网点复制*/
	FIN_PARAMCONFIG_MESSAGE_NETID_NOTCOPY("01913"),
	/**资金池不能为空*/
	FIN_PARAMCONFIG_MESSAGE_POOLID_NOTNULL("01914"),
	/**行次不能为空*/
	FIN_PARAMCONFIG_MESSAGE_REPORT_ORDERNO_NOTNULL("01916"),
    /** 该项目已存在，请检查*/
    FIN_PARAMCONFIG_MESSAGE_ITEM_EXISTS("11001"),
    /** 该项目已被使用，不能停用*/
    FIN_PARAMCONFIG_MESSAGE_ITEM_ISEMPLOY("11002"),
	/** 本位币不能为空*/
	FIN_PARAMCONFIG_MESSAGE_CURNAME_NOTNULL("12001"),
    /** 该现金流量表项目科目关系已存在，请检查*/
    FIN_PARAMCONFIG_MESSAGE_VOUCHERITEM_EXISTS("11003"),
	;

	ParamConfigMessageEnum(String code) {
		
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
