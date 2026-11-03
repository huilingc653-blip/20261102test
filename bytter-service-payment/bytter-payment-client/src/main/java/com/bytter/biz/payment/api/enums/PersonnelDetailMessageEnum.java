package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum PersonnelDetailMessageEnum implements IMessageEnum {
	/** 职工编号必须唯一 */
    NIS_PERSONNELDETAIL_MESSAGE_WORKNO_UNIQUE("01001"),
	/** 职工信息已被使用 */
	NIS_PERSONNELDETAIL_MESSAGE_WORKID_USE("01002"),
    /** 职工编号不能为空 */
    NIS_PERSONNELDETAIL_MESSAGE_WORKNO_NULL("01020"),
	/**银行账户不能为空*/
    NIS_PERSONNELDETAIL_MESSAGE_BANKACC_NULL("01021"),
	/**银行账户在系统中不存在*/
    NIS_PERSONNELDETAIL_MESSAGE_BANKACCINFO_NULL("01022"),
	/**姓名不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_USERNAME_NULL("01023"),
	/**性别不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_SEX_NULL("01024"),
	/**请输入正确的身份证号码*/
	NIS_PERSONNELDETAIL_MESSAGE_CORRECT_IDCARD("01025"),
	/**单位不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_CORPNAME_NULL("01026"),
	/**部门名称不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_DEPTNAME_NULL("01027"),
	/**开户行行号不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_BANKCODE_NULL("01028"),
	/**户名不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_ACCNAME_NULL("01029"),
	/**银行接口在系统中不存在*/
	NIS_PERSONNELDETAIL_MESSAGE_BISBIFINIT_NULL("01030"),
	/**省不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_PROVNAME_NULL("01031"),
	/**市不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_CITYNAME_NULL("01032"),
	/**银行类型不能为空*/
	NIS_PERSONNELDETAIL_MESSAGE_CARDTYPENAME_NULL("01033"),
	/**单位名称在系统中不存在*/
	NIS_PERSONNELDETAIL_MESSAGE_CORPINFO_NULL("01034"),
	/**开户行行号在系统中不存在*/
	NIS_PERSONNELDETAIL_MESSAGE_BTBANKINFO_NULL("01035"),
	/**所在省在系统中不存在*/
	NIS_PERSONNELDETAIL_MESSAGE_PROVINFO_NULL("01036"),
	/**所在市在系统中不存在*/
	NIS_PERSONNELDETAIL_MESSAGE_CITYINFO_NULL("01037");
	
	PersonnelDetailMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
	@Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
