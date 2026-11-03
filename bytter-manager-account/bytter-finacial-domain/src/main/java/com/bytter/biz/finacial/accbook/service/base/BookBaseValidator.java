package com.bytter.biz.finacial.accbook.service.base;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
/**
 * 凭证基础数据校验
 * 包括记账日期、起息日期、凭证借贷平衡、同网点同币别、余额透支等检查
 * @author liyh
 *
 */
@Component
@Service("bookBaseValidator")
public class BookBaseValidator {
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	public BookBaseValidator() {
	}
	
	public BookBaseValidator(FcAccbookVoucherVO voucher) {
		super();
	}
	
	/**
	 * 功能介绍 : 记账基础数据校验。<br>
	 * <b>日期：</b>2017年6月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void baseValidate(FcAccbookVoucherVO voucher){
		requriedValidate(voucher);
        isInterestStartDateEffect(voucher.getAccbook());
        moneyValidate(voucher);
        isTaillyDateLtDailyDate(voucher, voucher.getAccbook());
        setInnerSign(voucher);
	}
	
	/**
	 * 功能介绍 : 记账基础数据校验。<br>
	 * <b>日期：</b>2017年6月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void checkBaseValidate(FcAccbookVoucherVO voucher){
		checkRequriedValidate(voucher);
		checkIsInterestStartDateEffect(voucher.getAccbook());
		checkMoneyValidate(voucher);
		checkIsTaillyDateLtDailyDate(voucher, voucher.getAccbook());
        setInnerSign(voucher);
	}
	
	/**
	 * 功能介绍 : 记账凭证是否挂内部往来资金计划。<br>
	 * <b>日期：</b>2018年3月27日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void setInnerSign(FcAccbookVoucherVO voucher) {
		FcAccbook accbook = voucher.getAccbook();
		String generateMode = accbook.getGenerateMode();//非手工账的业务标识是否内转
		if (!FinaBookConstant.GenerateMode.TAILLY.get().equals(generateMode)) {
			return;
		}
		
		if (EmptyUtils.isEmpty(accbook.getInnerSign())) {
			accbook.setInnerSign(BudgetConstant.INNER_SIGN_0);//默认非内转
		}
		List<FcAccbookDtl> accbookDtls = voucher.getBookDtls();
		if (null != accbookDtls && accbookDtls.size() != 2) {
			return;
		}
		//借贷方账号均为企业活期
		for (FcAccbookDtl accbookDtl : accbookDtls) {
			FcAccDTO accDTO = accbookDtl.getAccDTO();
			if (null != accDTO) {
				String sysSubjectCode = accDTO.getSysSubjectCode();
				//企业活期科目
				if (!SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get().equals(sysSubjectCode)) {
					return;
				}
			}
		}
		accbook.setInnerSign(BudgetConstant.INNER_SIGN_1);//成员单位往来资金计划内转标识
	}
	private void checkNull(Object value, AccbookMessageEnum msg){
		if (String.class instanceof Object) {
			if (null == value|| StringUtils.isEmpty(value.toString())) {
				throw new ParameterException(msg);
			}
		} else {
			if (StringUtils.isNotEmpty(value)) {
				throw new ParameterException(msg);
			}
		}
	}
	
	/**
	 * 功能介绍 : 必填项基础检查<br>
	 * <b>日期：</b>2017年6月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
    private void requriedValidate(FcAccbookVoucherVO voucher) {
    	checkNull(voucher.getAccbook().getBusRaceId(), AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
    	checkNull(voucher.getAccbook().getInterestStartDate(), AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
    	checkNull(voucher.getAccbook().getTallyDate(), AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
    	checkNull(voucher.getAccbook().getEmergencySign(), AccbookMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO);
    	checkNull(voucher.getAccbook().getVoucherType(), AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        
        List<String> accList = new ArrayList<String>();
        for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
        	checkNull(accbookDtl.getSummaryContent(), AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
        	checkNull(accbookDtl.getAccount(), AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
            if (null == accbookDtl.getAccDTO()) {
            	accList.add(accbookDtl.getAccount());
			}
        }
        if (null == fcAccService) {
			fcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
		}
        //查询所有账号基本信息
        List<FcAccDTO> accDTOs = fcAccService.getAccSet(accList);
        Map<String, FcAccDTO> accMap = new HashMap<String, FcAccDTO>();
        for (FcAccDTO accDTO : accDTOs) {
        	accMap.put(accDTO.getBlnctrAcc(), accDTO);
		}
        Set<FcAccDTO> payAccs = new HashSet<FcAccDTO>();
		for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
			if (null == accbookDtl.getAccDTO()) {
				accbookDtl.setAccDTO(accMap.get(accbookDtl.getAccount()));
			}
			Double payMoney = getPayMoney(accbookDtl);
			if (payMoney < 0) {
				payAccs.add(accbookDtl.getAccDTO());
			}
		}
		validateAccpays(payAccs);//检查账户是否冻结只收不支
    }
    
    
    /**
	 * 功能介绍 : 必填项基础检查<br>
	 * <b>日期：</b>2017年6月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
    private void checkRequriedValidate(FcAccbookVoucherVO voucher) {
    	if(StringUtils.isEmpty(voucher.getAccbook().getBusRaceId())) {
    		throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
    	}
		if(ObjectUtils.isEmpty(voucher.getAccbook().getInterestStartDate())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		    	}
		if(ObjectUtils.isEmpty(voucher.getAccbook().getTallyDate())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		if(StringUtils.isEmpty(voucher.getAccbook().getEmergencySign())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		if(StringUtils.isEmpty(voucher.getAccbook().getVoucherType())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
        List<String> accList = new ArrayList<String>();
        for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
        	if(StringUtils.isEmpty(accbookDtl.getSummaryContent())) {
        		throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
    		}
        	if(StringUtils.isEmpty(accbookDtl.getAccount())) {
        		throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
    		}
            if (null == accbookDtl.getAccDTO()) {
            	accList.add(accbookDtl.getAccount());
			}
        }
        if (null == fcAccService) {
			fcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
		}
        //查询所有账号基本信息
        List<FcAccDTO> accDTOs = fcAccService.getAccSet(accList);
        Map<String, FcAccDTO> accMap = new HashMap<String, FcAccDTO>();
        for (FcAccDTO accDTO : accDTOs) {
        	accMap.put(accDTO.getBlnctrAcc(), accDTO);
		}
        Set<FcAccDTO> payAccs = new HashSet<FcAccDTO>();
		for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
			if (null == accbookDtl.getAccDTO()) {
				accbookDtl.setAccDTO(accMap.get(accbookDtl.getAccount()));
			}
			Double payMoney = getPayMoney(accbookDtl);
			if (payMoney < 0) {
				payAccs.add(accbookDtl.getAccDTO());
			}
		}
		checkValidateAccpays(payAccs);//检查账户是否冻结只收不支
    }
    /**
     * 功能介绍 : 获取付款金额。<br>
     * <b>日期：</b>May 26, 2018<br>
     * @version 1.0
     * @author liyh 
     */
    private Double getPayMoney(FcAccbookDtl accbookDtl) {
    	if(accbookDtl.getAccDTO() == null){
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_ERRO);
		}
		BigDecimal debitMoney = accbookDtl.getDebitMoney() == null ? BigDecimal.ZERO : accbookDtl.getDebitMoney();
		BigDecimal creditMoney = accbookDtl.getCreditMoney() == null ? BigDecimal.ZERO : accbookDtl.getCreditMoney();
        return debitMoney.subtract(creditMoney).doubleValue() * accbookDtl.getAccDTO().getBalanceWay();
    }
    
    /**
     * 功能介绍 : 账号属性检查能否做付款业务。<br>
     * <b>日期：</b>2018-05-26<br>
     * @version 1.0
     * @author liyh 
     */
    private void validateAccpays(Set<FcAccDTO> payAccs) {
        for (FcAccDTO fcAcc : payAccs) {
            if (InnerAccountConstant.ACC_STATE_S.equals(fcAcc.getAccState())) {
                //账户为冻结只收不支账户
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_FREEZE, fcAcc.getBlnctrAcc());
            }
        }
    }
    
    /**
     * 功能介绍 : 账号属性检查能否做付款业务。<br>
     * <b>日期：</b>2018-05-26<br>
     * @version 1.0
     * @author liyh 
     */
    private void checkValidateAccpays(Set<FcAccDTO> payAccs) {
        for (FcAccDTO fcAcc : payAccs) {
            if (InnerAccountConstant.ACC_STATE_S.equals(fcAcc.getAccState())) {
                //账户为冻结只收不支账户
            	String[] params = {StringUtils.isEmpty(fcAcc.getAccName())?"":fcAcc.getAccName()};
                throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACCOUNT_FREEZE, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
            }
        }
    }
    
    /**
     * 功能介绍 : 账号借贷方重复检查。<br>
     * <b>日期：</b>2017年10月31日<br>
     * @version 1.0
     * @author liyh 
     */
    private void accountRepeatValidate(FcAccbookVoucherVO voucher) {
    	List<String> debitAcc = new ArrayList<String>();
        List<String> creditAcc = new ArrayList<String>();
        for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
        	String account = accbookDtl.getAccount();
        	if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
        		if(debitAcc.contains(account)){//借方重复
        			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_DREPEAT, account);
        		}
        		debitAcc.add(account);
			} else {
				if(creditAcc.contains(account)){//贷方重复
        			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_CREPEAT, account);
        		}
				creditAcc.add(account);
			}
        }
    }
    
    /**
     * 功能介绍 : 分录金额检查。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void moneyValidate(FcAccbookVoucherVO voucher) {
    	Map<String, String> accountsInfo = new HashMap<String, String>();
        BigDecimal debitMoneyTot = BigDecimal.ZERO;
        BigDecimal creditMoneyTot = BigDecimal.ZERO;
        String curCode = null;
        String netCode = null; 
        
        for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
            blankMoneyValidate(accbookDtl);
            BigDecimal debitMoney = accbookDtl.getDebitMoney();
            BigDecimal creditMoney = accbookDtl.getCreditMoney();
            if (debitMoney != null) {
                debitMoneyTot = debitMoneyTot.add(debitMoney);
            }
            if (creditMoney != null) {
                creditMoneyTot = creditMoneyTot.add(creditMoney);
            }
            
            String account = accbookDtl.getAccount();
            String balanceWay = "debit";
            if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                balanceWay = "credit";
            }
            String balanceWayFirst = accountsInfo.get(account);
            if (balanceWayFirst != null && !balanceWayFirst.equals(balanceWay)) {
            	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_BOTHSIDE, account);
            } else {
                accountsInfo.put(account, balanceWay);
            }
            
            if (curCode == null) {
                curCode = accbookDtl.getAccDTO().getCurId();
                netCode = accbookDtl.getAccDTO().getNetId();
                voucher.setNetCode(netCode);//设置网点
            } else {
            	if (!curCode.equals(accbookDtl.getAccDTO().getCurId())) {
            		throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_SAMECUR);
            	}
				
				if (!netCode.equals(accbookDtl.getAccDTO().getNetId())) {
					if(FinaBookConstant.GenerateMode.BANK_POOL.getValue().equals(voucher.getAccbook().getGenerateMode())){
						throw new ParameterException(AccbookMessageEnum.FIN_BOOK_POOL_TALLY_ACCOUNT_SAMENET);
					}else{
						throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_SAMENET);
					}
				}
				
            }
            accountValidate(voucher, accbookDtl);
        }
        if (debitMoneyTot.compareTo(creditMoneyTot) != 0) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_MONEY_BALANCE);
        }
        //accountRepeatValidate(voucher);
    }
    
    /**
     * 功能介绍 : 分录金额检查。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void checkMoneyValidate(FcAccbookVoucherVO voucher) {
    	Map<String, String> accountsInfo = new HashMap<String, String>();
        BigDecimal debitMoneyTot = BigDecimal.ZERO;
        BigDecimal creditMoneyTot = BigDecimal.ZERO;
        String curCode = null;
        String netCode = null; 
        
        for (FcAccbookDtl accbookDtl : voucher.getBookDtls()) {
            blankMoneyValidate(accbookDtl);
            BigDecimal debitMoney = accbookDtl.getDebitMoney();
            BigDecimal creditMoney = accbookDtl.getCreditMoney();
            if (debitMoney != null) {
                debitMoneyTot = debitMoneyTot.add(debitMoney);
            }
            if (creditMoney != null) {
                creditMoneyTot = creditMoneyTot.add(creditMoney);
            }
            
            String account = accbookDtl.getAccount();
            String balanceWay = "debit";
            if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                balanceWay = "credit";
            }
            String balanceWayFirst = accountsInfo.get(account);
            if (balanceWayFirst != null && !balanceWayFirst.equals(balanceWay)) {
            	String[] params = {StringUtils.isEmpty(account)?"":account};
            	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACCOUNT_BOTHSIDE, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
            } else {
                accountsInfo.put(account, balanceWay);
            }
            
            if (curCode == null) {
                curCode = accbookDtl.getAccDTO().getCurId();
                netCode = accbookDtl.getAccDTO().getNetId();
                voucher.setNetCode(netCode);//设置网点
            } else {
            	if (!curCode.equals(accbookDtl.getAccDTO().getCurId())) {
            		throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACCOUNT_SAMECUR, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
            	}
				
				if (!netCode.equals(accbookDtl.getAccDTO().getNetId())) {
					if(FinaBookConstant.GenerateMode.BANK_POOL.getValue().equals(voucher.getAccbook().getGenerateMode())){
						throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_POOL_TALLY_ACCOUNT_SAMENET, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
					}else{
						throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACCOUNT_SAMENET, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
					}
				}
				
            }
            checkAccountValidate(voucher, accbookDtl);
        }
        if (debitMoneyTot.compareTo(creditMoneyTot) != 0) {
        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_MONEY_BALANCE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
    }
    /**
     * 功能介绍 : 分录借贷金额不能同时为空或同时为零检查。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void blankMoneyValidate(FcAccbookDtl accbookDtl) {
        BigDecimal debitMoney = accbookDtl.getDebitMoney();
        BigDecimal creditMoney = accbookDtl.getCreditMoney();
        if (debitMoney == null && creditMoney == null) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_MONEY_BOTH_NO);
        }
        if (debitMoney == null) {
			accbookDtl.setDebitMoney(BigDecimal.ZERO);
		}
        if (creditMoney == null) {
        	accbookDtl.setCreditMoney(BigDecimal.ZERO);
        }
        if (debitMoney != null && creditMoney != null) {
            if (BigDecimal.ZERO.compareTo(debitMoney) == 0 && BigDecimal.ZERO.compareTo(creditMoney) == 0) {
            	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_MONEY_BOTH_ZERO);
            }
        }
    }
    /**
     * 功能介绍 : 起息日期校验。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void isInterestStartDateEffect(FcAccbook accbook) {
        if (accbook.getInterestStartDate().compareTo(accbook.getTallyDate()) > 0) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTART_AFTER);
        }
    }
    
    /**
     * 功能介绍 : 起息日期校验。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void checkIsInterestStartDateEffect(FcAccbook accbook) {
        if (accbook.getInterestStartDate().compareTo(accbook.getTallyDate()) > 0) {
        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_INTERESTSTART_AFTER, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
    }
    protected void checkAccountValidate(FcAccbookVoucherVO voucher, FcAccbookDtl accbookDtl) {
    	checkIsInterestStartDateLtAccountEffectDate(voucher.getAccbook(), accbookDtl);
    	checkIsTaillyDateLtAccountEffectDate(voucher.getAccbook(), accbookDtl);
    }
    
    
    /**
     * 功能介绍 : 账户检查。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     * <b>更新者：</b>李四<br>
     * <b>日期：</b> 2017-5-1 <br>
     * <b>更新内容：</b> 修改XXXX <br>
     */
    protected void accountValidate(FcAccbookVoucherVO voucher, FcAccbookDtl accbookDtl) {
        isInterestStartDateLtAccountEffectDate(voucher.getAccbook(), accbookDtl);
        isTaillyDateLtAccountEffectDate(voucher.getAccbook(), accbookDtl);
    }
    /**
     * 功能介绍 : 起息日期是否大于账户启用日期。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void isInterestStartDateLtAccountEffectDate(FcAccbook accbook, FcAccbookDtl accbookDtl) {
        if (accbook.getInterestStartDate().before(accbookDtl.getAccDTO().getAccStartDate())) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_START_AFTER_ACC, accbookDtl.getAccount());
        }
    }
    private void checkIsInterestStartDateLtAccountEffectDate(FcAccbook accbook, FcAccbookDtl accbookDtl) {
        if (accbook.getInterestStartDate().before(accbookDtl.getAccDTO().getAccStartDate())) {
        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_START_AFTER_ACC, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
    }
    /**
     * 功能介绍 : 记账日期大于账户启用日期检查。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void isTaillyDateLtAccountEffectDate(FcAccbook accbook, FcAccbookDtl accbookDtl) {
        if (accbook.getTallyDate().before(accbookDtl.getAccDTO().getAccStartDate())) {
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYD_AFTER_ACC, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
    }
    private void checkIsTaillyDateLtAccountEffectDate(FcAccbook accbook, FcAccbookDtl accbookDtl) {
        if (accbook.getTallyDate().before(accbookDtl.getAccDTO().getAccStartDate())) {
        	throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_TALLYD_AFTER_ACC, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
        }
    }
    
    /**
     * 功能介绍 : 判断记账日期是否比已日结日期早。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void isTaillyDateLtDailyDate(FcAccbookVoucherVO voucher, FcAccbook accbook) {
    	if (null == dayCloseService) {
			dayCloseService = (IFcDayCloseService) ApplicationContextUtil.getContext().getBean("fcDayCloseServiceImpl");
		}
        Date dayOverDate = null;
		String generateMode = accbook.getGenerateMode();//产生方式
        if (FinaBookConstant.GenerateMode.BANK_POOL.get().equals(generateMode)) {
    	    dayOverDate = dayCloseService.getPoolDayCloseDate(voucher.getNetCode());//根据资金池获取已日结日期
            if (null == dayOverDate) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_NOPOOL_CLOSEDATE);
            }
        } else {
    	    dayOverDate = dayCloseService.getDayCloseFinalDate(voucher.getNetCode());//根据网点获取已日结日期
            if (null == dayOverDate) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_NONET_CLOSEDATE);
            }
        }
        //判断记账日期和日结日期比较
        if(FinaBookConstant.BusOriType.RUSH.getValue().equals(voucher.getOpType())){
        	 if (accbook.getTallyDate().compareTo(dayOverDate) <= 0) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED,
						DateUtils.formatDate(accbook.getTallyDate()));
             }
        }else{
	        if (accbook.getTallyDate().compareTo(dayOverDate) <= 0) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYD_AFTER_CLOSED,
						DateUtils.formatDate(accbook.getTallyDate()));
	        }
        }
    }
    
    /**
     * 功能介绍 : 判断记账日期是否比已日结日期早。<br>
     * <b>日期：</b>2017年6月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void checkIsTaillyDateLtDailyDate(FcAccbookVoucherVO voucher, FcAccbook accbook) {
    	if (null == dayCloseService) {
			dayCloseService = (IFcDayCloseService) ApplicationContextUtil.getContext().getBean("fcDayCloseServiceImpl");
		}
        Date dayOverDate = null;
		String generateMode = accbook.getGenerateMode();//产生方式
        if (FinaBookConstant.GenerateMode.BANK_POOL.get().equals(generateMode)) {
    	    dayOverDate = dayCloseService.getPoolDayCloseDate(voucher.getNetCode());//根据资金池获取已日结日期
            if (null == dayOverDate) {
                throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_NOPOOL_CLOSEDATE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
            }
        } else {
    	    dayOverDate = dayCloseService.getDayCloseFinalDate(voucher.getNetCode());//根据网点获取已日结日期
            if (null == dayOverDate) {
                throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_NONET_CLOSEDATE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
            }
        }
        //判断记账日期和日结日期比较
        if(FinaBookConstant.BusOriType.RUSH.getValue().equals(voucher.getOpType())){
        	 if (accbook.getTallyDate().compareTo(dayOverDate) <= 0) {
        		String[] params = {(ObjectUtils.isEmpty(accbook) || ObjectUtils.isEmpty(accbook.getTallyDate())) ? "" : accbook.getTallyDate().toString()}; 
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
             }
        }else{
	        if (accbook.getTallyDate().compareTo(dayOverDate) <= 0) {
	        	String[] params = {(ObjectUtils.isEmpty(accbook) || ObjectUtils.isEmpty(accbook.getTallyDate())) ? "" : accbook.getTallyDate().toString()}; 
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_TALLYD_AFTER_CLOSED, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
	        }
        }
    }
    
	
}
