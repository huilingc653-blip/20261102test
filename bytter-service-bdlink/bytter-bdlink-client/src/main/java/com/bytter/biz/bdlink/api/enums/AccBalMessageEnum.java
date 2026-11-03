package com.bytter.biz.bdlink.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum AccBalMessageEnum implements IMessageEnum {
	/**银行账户不能为空*/
    BIS_ACCBAL_MESSAGE_BANKACC_NULL("01020"),
	/**余额不能为空*/
    BIS_ACCBAL_MESSAGE_BAL_NULL("01021"),
	/**初始化余额不能为空*/
	BIS_ACCBAL_MESSAGE_INITBAL_NULL("01021"),
	/**日期不能为空*/
    BIS_ACCBAL_MESSAGE_BALDATE_NULL("01022"),
	/**初始化余额日期不能为空*/
	BIS_ACCBAL_MESSAGE_INITBALDATE_NULL("01022"),
	/**币别不能为空*/
    BIS_ACCBAL_MESSAGE_CURID_NULL("01023"),
	/**币别在系统中不存在*/
    BIS_ACCBAL_MESSAGE_CURNAME_NULL("01024"),
	/**银行账户在系统中不存在*/
	BIS_ACCBAL_MESSAGE_BANKACCINFO_NULL("01025"),
	/** 余额必须为数字 */
	BIS_ACCBAL_MESSAGE_BAL_NUMBER("01026"),
	/** 当前余额日期已录入了余额 */
	BIS_ACCBAL_MESSAGE_BALDATE_CHECK_BAL("01027"),
	/** 余额日期小于账户注册日期 */
	BIS_ACCBAL_MESSAGE_BALDATE_LT_ACCREDDATE("01028"),
	/** 手工录入的余额与明细生成的余额不一致! */
	BIS_ACCBAL_MESSAGE_BAL_NOTEQ_BISACCDTLBAL("01029"),
	/** 录入余额大于可用余额! */
	BIS_ACCBAL_MESSAGE_BAL_GT_AVAILBAL("01030"),
	/** 账户注册日期为null */
	BIS_ACCBAL_MESSAGE_BANKACC_REGDATE_NULL("01031"),
	/** 由流水明细生成余额失败 */
	BIS_ACCBAL_MESSAGE_BALBYDTL_GENERATE_FAIL("01032"),
	/** 账户XXXX在XXXX-XX-XX日录入多条数据，请检查！ */
	BIS_ACCBAL_MESSAGE_MULBALDATE_CHECK_BAL("01033");
	AccBalMessageEnum(String code) {
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
