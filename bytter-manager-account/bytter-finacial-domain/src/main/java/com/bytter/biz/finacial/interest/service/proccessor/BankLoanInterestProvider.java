package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SysSubject;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;
@Transactional
@Service("interest_BankLoanInterestProvider")
public class BankLoanInterestProvider extends LoanInterestProvider implements Serializable {
	/**
	 * <b>功能介绍：</b>获取账户上次预提时间<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netCode
	 * @param accTypeCode
	 * @return
	 */
	public Map<String, Date> getAccLastPredrawDate(String netCode, String accTypeCode) {
        return new HashMap<String, Date>();
    }
	
	/**
	 * <b>功能介绍：</b>利息贷方账号<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param corpCode
	 * @param curCode
	 * @return
	 */
	protected FcAccDTO findInterestCreditSysAcc(String corpCode, String curCode) {
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(corpCode);
        FcAccDTO creditSysAcc = findAccBySysSubjectCode(SysSubject.INTEREST_PAYABLE_BANK_LOAN.get(), null, corpCodes,
                curCode, SysSubject.INTEREST_PAYABLE_BANK_LOAN.getName(),null,corpCode,null).get(0);
        if (creditSysAcc == null) {//单位{0},币别{1},没有开设{2}账户。
        	String corpName = iCorpService.get(corpCode).getName();//获取单位名称
            String currentName = "";
            if (StringUtils.isNotEmpty(curCode)) {
                currentName = iBtCurrencyService.getObjectById(curCode).getCurName();
            }
        	logger.debug("单位["+corpName+"],币别["+currentName+"],没有开设["+SysSubject.INTEREST_PAYABLE_BANK_LOAN.getName()+"]账户。");
			throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpName, currentName,
					SysSubject.INTEREST_PAYABLE_BANK_LOAN.getName());
        }
        return creditSysAcc;
    }
	
	/**
	 * <b>功能介绍：</b>利息借方账号<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param corpCode
	 * @param curCode
	 * @return
	 */
	protected FcAccDTO findInterestDebitSysAcc(String corpCode, String curCode) {
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(corpCode);
        FcAccDTO creditSysAcc = findAccBySysSubjectCode(SysSubject.INTEREST_EXPENSES_BANK_LOAN.get(), null, corpCodes,
                curCode, SysSubject.INTEREST_EXPENSES_BANK_LOAN.getName(),null,corpCode,null).get(0);
        if (creditSysAcc == null) {
        	String corpName = iCorpService.get(corpCode).getName();//获取单位名称
            String currentName = "";
            if (StringUtils.isNotEmpty(curCode)) {
                currentName = iBtCurrencyService.getObjectById(curCode).getCurName();
            }
        	logger.debug("单位["+corpName+"],币别["+currentName+"],没有开设["+SysSubject.INTEREST_EXPENSES_BANK_LOAN.getName()+"]账户");
        	throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpName, currentName,
					SysSubject.INTEREST_EXPENSES_BANK_LOAN.getName());
        }
        return creditSysAcc;
    }
	
	/**
	 * <b>功能介绍：</b>贷方系统科目/查找单位应付利息－银行借款账户<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * <b>作者：</b>caozx<br>
	 */
	@Override
	public ConstantBean<String> getCreditSysAcc(){
        return SysSubject.INTEREST_PAYABLE_BANK_LOAN;
    }
	
	/**
	 * <b>功能介绍：</b>借方系统科目/查找单位利息支出－银行借款账户<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * <b>作者：</b>caozx<br>
	 */
	@Override
    public ConstantBean<String> getDebitSysAcc(){
        return SysSubject.INTEREST_EXPENSES_BANK_LOAN;
    }
}
