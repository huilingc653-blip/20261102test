package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SysSubject;
import com.bytter.core.constant.ConstantBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>银行活期存款计息适配器<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年10月29日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Transactional
@Service("interest_BankCurrentInterestProvider")
public class BankCurrentInterestProvider extends CurrentInterestProvider implements Serializable {
	
	/**
	 * 贷方系统科目/查找单位应收利息-银行活期存款账户
	 */
	@Override
    public ConstantBean<String> getDebitSysAcc(){
        return SysSubject.INTEREST_PAYABLE_BANK_CURRENT;
    }
	/**
	 * 借方系统科目/查找单位利息收入-银行活期存款账户
	 */
	@Override
	public ConstantBean<String> getCreditSysAcc(){
	    return SysSubject.INTEREST_EXPENSES_BANK_CURRENT;
    }
	
}
