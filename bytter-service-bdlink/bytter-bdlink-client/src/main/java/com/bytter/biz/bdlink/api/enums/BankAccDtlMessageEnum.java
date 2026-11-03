package com.bytter.biz.bdlink.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum BankAccDtlMessageEnum implements IMessageEnum {
	/** 交易日期小于账户注册日期 */
	BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_ACCREDDATE("01020"),
	/**交易日期小于账户注册日期.录入失败！账户{0}的录入余额日期{1}小于账户的注册日期！*/
	BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_ACCREDDATE_DTL("01021"),
	/**	交易时间不能大于系统当前时间*/
	BT_BANKACCDTL_MESSAGE_TRANSTIME_LT_SYSDATE("01021"),
	/** 当前余额日期已录入了余额 */
	BT_BANKACCDTL_MESSAGE_BALDATE_CHECK_BAL("01021"),
	/** 余额日期小于账户注册日期 */
	BT_BANKACCDTL_MESSAGE_BALDATE_LT_ACCREDDATE("01022"),
	/** 余额不能为空 */
	BT_BANKACCDTL_MESSAGE_AMT_NULL("01023"),
	/** 余额要大于0 */
	BT_BANKACCDTL_MESSAGE_AMT_NOTZERO("01024"),
	/** 余额不能为字符 */
	BT_BANKACCDTL_MESSAGE_AMT_NUMBER("01025"),
	/** 排序号不能为空 */
	BT_BANKACCDTL_MESSAGE_ORDERNO_NULL("01026"),
	/** 排序号不能为字符 */
	BT_BANKACCDTL_MESSAGE_ORDERNO_NUMBER("01027"),
	/** 账户注册日期为null */
	BT_BANKACCDTL_MESSAGE_BANKACC_REGDATE_NULL("01028"),
	/** 交易日期不能小于截至时间 */
	BT_BANKACCDTL_MESSAGE_BALDATE_LT_COMPLETEDATE("01029"),
	/** 只能删除状态为暂存或打回的流水信息 */
	BT_BANKACCDTL_MESSAGE_DELETE_TEMP_OR_BACK("01029"),
	/** 只能作废状态为审批通过的流水信息 */
	BT_BANKACCDTL_MESSAGE_DELETE_CHECK("01029"),
	/** 只能提交状态为暂存或打回的流水信息 */
	BT_BANKACCDTL_MESSAGE_SUBMIT_TEMP_OR_BACK("01030"),
	/** 该银行类型已维护相同文件类型的配置，请确认！ */
	BISACCDTL_IMPORTCONFIG_MESSAGE_REPEAT_FILETYPE("01031"),
	/** 该银行类别对应的文件类型模板未配置 */
	BISACCDTL_IMPORT_FILETYPE_NOT_CONFIGURED_EXIST("01032");
	
	BankAccDtlMessageEnum(String code) {
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
