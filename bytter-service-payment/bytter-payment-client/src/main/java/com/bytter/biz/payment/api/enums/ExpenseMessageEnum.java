package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ExpenseMessageEnum implements IMessageEnum {
	/** 请选择单位 */
	NIS_EXPENSE_MESSAGE_CORP_NULL("01001"),
	/** 请选择银行卡类型 */
	NIS_EXPENSE_MESSAGE_CARDTYPE_NULL("01002"),
	/**银行账户不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_BANKACC_NULL("01021"),
	/**银行账户在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_BANKACCINFO_NULL("01022"),
	/**姓名不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_USERNAME_NULL("01023"),
	/**报账日期不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_EXPENSEDATE_NULL("01024"),
	/**操作类型不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_OPTYPENAME_NULL("01025"),
	/**单位不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_CORPNAME_NULL("01026"),
	/**部门名称不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_DEPTNAME_NULL("01027"),
	/**开户行行号不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_BANKCODE_NULL("01028"),
	/**户名不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_ACCNAME_NULL("01029"),
	/**银行接口在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_BISBIFINIT_NULL("01030"),
	/**省不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_PROVNAME_NULL("01031"),
	/**市不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_CITYNAME_NULL("01032"),
	/**银行类型不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_CARDTYPENAME_NULL("01033"),
	/**单位名称在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_CORPINFO_NULL("01034"),
	/**开户行行号在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_BTBANKINFO_NULL("01035"),
	/**业务类型不能为空*/
	NIS_EXPENSEDETAIL_MESSAGE_BUSTYPENAME_NULL("01036"),
	/**业务类型在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_BUSTYPEINFO_NULL("01037"),
	/**操作类型在系统中不存在*/
	NIS_EXPENSEDETAIL_MESSAGE_OPTYPINFO_NULL("01038");
	
	ExpenseMessageEnum(String code) {
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
