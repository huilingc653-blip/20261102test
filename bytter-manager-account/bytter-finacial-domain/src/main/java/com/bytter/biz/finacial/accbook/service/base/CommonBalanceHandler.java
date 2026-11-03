package com.bytter.biz.finacial.accbook.service.base;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
/**
 * 通用账务余额处理
 * @author liyh
 *
 */
@Component
@Service("commonBalanceHandler")
@Transactional
public abstract class CommonBalanceHandler extends BaseService implements Serializable {
    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	public FcOnlineLedgerService getLedgerService() {
		if (fcOnlineLedgerService == null) {
			fcOnlineLedgerService = ApplicationContextUtil.getContext().getBean(FcOnlineLedgerService.class);
		}
		return fcOnlineLedgerService;
	}
	
	/**
	 * 功能介绍 : 检查账户余额透支情况。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh <b>
	 */
	public Map<String, FcOnlineLedgerDTO> processAccountBalance(VoucherWrapper voucher) {
		// 获取账户联机余额
		Map<String, FcOnlineLedgerDTO> ledgerMap = initAccoutOnlineInfo(voucher);
		
		// 还原原凭证余额和笔数
		recoverOnlineInfo(voucher, ledgerMap);
		// 凭证计算联机余额信息并检查透支
		calcBalanceOver(voucher, ledgerMap);
		return ledgerMap;
	}
	
	/**
	 * 功能介绍 : 检查账户余额透支情况。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh <b>
	 */
	public Map<String, FcOnlineLedgerDTO> checkProcessAccountBalance(VoucherWrapper voucher) {
		// 获取账户联机余额
		Map<String, FcOnlineLedgerDTO> ledgerMap = initAccoutOnlineInfo(voucher);//无需校验
		
		// 还原原凭证余额和笔数
		checkRecoverOnlineInfo(voucher, ledgerMap);//已校验
		// 凭证计算联机余额信息并检查透支
		checkCalcBalanceOver(voucher, ledgerMap);
		return ledgerMap;
	}
	
	/**
	 * 功能介绍 : 初始化账户联机余额信息。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 * @param voucher
	 */
	private Map<String, FcOnlineLedgerDTO> initAccoutOnlineInfo(VoucherWrapper voucher) {
//		获取初始化联机余额
		List<FcOnlineLedgerDTO> onlineLedgerDTOs = ListAccoutOnlineInfo(voucher);
//		按账号分类联机余额
		Map<String, FcOnlineLedgerDTO> ledgerMap = new HashMap<String, FcOnlineLedgerDTO>();
		for (FcOnlineLedgerDTO fcOnlineLedgerDTO : onlineLedgerDTOs) {
			ledgerMap.put(fcOnlineLedgerDTO.getAccount(), fcOnlineLedgerDTO);
            //logger.error("账户余额：" + fcOnlineLedgerDTO.getAccount() + "/" + fcOnlineLedgerDTO.getOnlineBalance());
		}
		return ledgerMap;
	}
	/**
	 * 获取联机余额
	 * @param voucher
	 * @return
	 */
	public List<FcOnlineLedgerDTO> ListAccoutOnlineInfo(VoucherWrapper voucher){
		List<String> accList = new ArrayList<String>();// 账户集合
		String generateMode = null;
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			generateMode = voucherVO.getAccbook().getGenerateMode();
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl fcAccbookDtl : bookDtls) {
				accList.add(fcAccbookDtl.getAccount());
			}
		}
		// 如果存在改账的凭证
		for (FcAccbookVoucherVO voucherVO : voucher.getOriTarget()) {
			if(EmptyUtils.isEmpty(generateMode)){
				generateMode = voucherVO.getAccbook().getGenerateMode();
			}
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl fcAccbookDtl : bookDtls) {
				accList.add(fcAccbookDtl.getAccount());
			}
		}
		// FIXME 调用联机余额接口，返回账户的余额信息
		List<FcOnlineLedgerDTO> onlineLedgerDTOs = new ArrayList<>();
		if(FinaBookConstant.GenerateMode.BANK_POOL.get().equals(generateMode)){
			onlineLedgerDTOs = getLedgerService().getPoolLedgerDTOsByAccSet(accList);
		} else {
			onlineLedgerDTOs = getLedgerService().getLedgerDTOsByAccSet(accList);
		}
		return onlineLedgerDTOs;
	}
	/**
	 * 功能介绍 : 恢复原凭证的账户余额。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	private void recoverOnlineInfo(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
		ProcessType processType = voucher.getProcessType();
		for (FcAccbookVoucherVO voucherVO : voucher.getOriTarget()) {
			OperateType opType = voucherVO.getOpType();
			List<FcAccbookDtl> oriBookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl bookDtl : oriBookDtls) {
				String account = bookDtl.getAccount();
				FcOnlineLedgerDTO onlineLedgerDTO = ledgerMap.get(account);
				if (null == onlineLedgerDTO) {
					throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_LEDGER_NO, account);
				}
				BigDecimal calcMoney = calcMoney(bookDtl.getDebitMoney(), bookDtl.getCreditMoney(),
						onlineLedgerDTO.getBalanceWay());// 发生额
				recoverBalance(processType, opType, calcMoney, onlineLedgerDTO);
			}
		}
	}
	
	/**
	 * 功能介绍 : 恢复原凭证的账户余额。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	private void checkRecoverOnlineInfo(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
		ProcessType processType = voucher.getProcessType();
		for (FcAccbookVoucherVO voucherVO : voucher.getOriTarget()) {
			OperateType opType = voucherVO.getOpType();
			List<FcAccbookDtl> oriBookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl bookDtl : oriBookDtls) {
				String account = bookDtl.getAccount();
				FcOnlineLedgerDTO onlineLedgerDTO = ledgerMap.get(account);
				if (null == onlineLedgerDTO) {
					String[] params = {account};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_LEDGER_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
				}
				BigDecimal calcMoney = calcMoney(bookDtl.getDebitMoney(), bookDtl.getCreditMoney(),
						onlineLedgerDTO.getBalanceWay());// 发生额
				recoverBalance(processType, opType, calcMoney, onlineLedgerDTO);
			}
		}
	}
	protected abstract BigDecimal getBalance(FcOnlineLedgerDTO onlineLedgerDTO);
	
	protected abstract Integer getCnt(FcOnlineLedgerDTO onlineLedgerDTO);
	
	protected abstract void setCurId(FcAccbookDtl bookDtl, FcOnlineLedgerDTO onlineLedgerDTO);
	/**
	 * 功能介绍 : 检查账户余额透支。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	private void calcBalanceOver(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
		ProcessType processType = voucher.getProcessType();
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			OperateType opType = voucherVO.getOpType();
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl bookDtl : bookDtls) {
				String account = bookDtl.getAccount();
				FcOnlineLedgerDTO onlineLedgerDTO = ledgerMap.get(account);
				if (null == onlineLedgerDTO) {
					throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_LEDGER_NO, account);
				}
				BigDecimal calcMoney = calcMoney(bookDtl.getDebitMoney(), bookDtl.getCreditMoney(), onlineLedgerDTO.getBalanceWay());// 发生额
				calcBalance(processType, opType, calcMoney, onlineLedgerDTO, voucherVO.getAccbook().getVoucherState());
				//检查账户余额透支情况
				processOverDraft(onlineLedgerDTO);
				bookDtl.setBalance(getBalance(onlineLedgerDTO));//记账完后账务的余额
				setCurId(bookDtl, onlineLedgerDTO);//设置币别
			}
		}
	}
	
	/**
	 * 功能介绍 : 检查账户余额透支。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	private void checkCalcBalanceOver(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
		ProcessType processType = voucher.getProcessType();
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			OperateType opType = voucherVO.getOpType();
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			for (FcAccbookDtl bookDtl : bookDtls) {
				String account = bookDtl.getAccount();
				FcOnlineLedgerDTO onlineLedgerDTO = ledgerMap.get(account);
				if (null == onlineLedgerDTO) {
					String[] params = {account};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_LEDGER_NO, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
				}
				BigDecimal calcMoney = calcMoney(bookDtl.getDebitMoney(), bookDtl.getCreditMoney(), onlineLedgerDTO.getBalanceWay());// 发生额
				calcBalance(processType, opType, calcMoney, onlineLedgerDTO, voucherVO.getAccbook().getVoucherState());
				//检查账户余额透支情况
				checkProcessOverDraft(onlineLedgerDTO);
				bookDtl.setBalance(getBalance(onlineLedgerDTO));//记账完后账务的余额
				setCurId(bookDtl, onlineLedgerDTO);//设置币别
			}
		}
	}
	
	/**
     * <br>
     * <b>功能：</b>可用余额计算<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2017年6月7日 <br>
     * @param balance 当前余额
     * @param freezeMoney 冻结金额
     * @return
     */
    public BigDecimal calc(BigDecimal balance, BigDecimal freezeMoney) {
        return balance.subtract(freezeMoney);
    }
	
    /**
     * 功能介绍 : 检查账户是否透支。<br>
     * <b>日期：</b>2017年6月7日<br>
     * @version 1.0
     * @author liyh 
     */
	private void processOverDraft(FcOnlineLedgerDTO onlineLedgerDTO) {
		//检查账户余额透支情况
		BigDecimal accBalance = getBalance(onlineLedgerDTO);//记账完后账务的余额
		BigDecimal freezeMoney = onlineLedgerDTO.getFreezeMoney();
		BigDecimal availableBalance = calc(accBalance, freezeMoney);//账户余额-冻结金额
		if (availableBalance.compareTo(BigDecimal.ZERO) < 0) {//账户余额不够
			String overdraftSign = onlineLedgerDTO.getOverdraftSign();//透支标识
			if (EmptyUtils.isEmpty(overdraftSign) || InnerAccountConstant.OVERDRAFT_SIGN_N.equals(overdraftSign)) {//不允许透支
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACC_OVERDRAFT, onlineLedgerDTO.getAccount());
			}
			if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(overdraftSign)) {//受限透支
				BigDecimal overdraftMoney = onlineLedgerDTO.getOverdraftMoney();
				if (null == overdraftMoney) {
					overdraftMoney = BigDecimal.ZERO;
				}
				if (availableBalance.abs().compareTo(onlineLedgerDTO.getOverdraftMoney()) > 0) {//账户余额不够
					throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACC_OVERDRAFT_MONEY, onlineLedgerDTO.getAccount());
				}
			}
		}
    }
	
	 /**
     * 功能介绍 : 检查账户是否透支。<br>
     * <b>日期：</b>2017年6月7日<br>
     * @version 1.0
     * @author liyh 
     */
	private void checkProcessOverDraft(FcOnlineLedgerDTO onlineLedgerDTO) {
		//检查账户余额透支情况
		BigDecimal accBalance = getBalance(onlineLedgerDTO);//记账完后账务的余额
		BigDecimal freezeMoney = onlineLedgerDTO.getFreezeMoney();
		BigDecimal availableBalance = calc(accBalance, freezeMoney);//账户余额-冻结金额
		if (availableBalance.compareTo(BigDecimal.ZERO) < 0) {//账户余额不够
			String overdraftSign = onlineLedgerDTO.getOverdraftSign();//透支标识
			if (EmptyUtils.isEmpty(overdraftSign) || InnerAccountConstant.OVERDRAFT_SIGN_N.equals(overdraftSign)) {//不允许透支
				String[] params = {StringUtils.isEmpty(onlineLedgerDTO.getAccount()) ? "":onlineLedgerDTO.getAccount()};
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACC_OVERDRAFT, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
			}
			if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(overdraftSign)) {//受限透支
				BigDecimal overdraftMoney = onlineLedgerDTO.getOverdraftMoney();
				if (null == overdraftMoney) {
					overdraftMoney = BigDecimal.ZERO;
				}
				if (availableBalance.abs().compareTo(onlineLedgerDTO.getOverdraftMoney()) > 0) {//账户余额不够
					String[] params = {StringUtils.isEmpty(onlineLedgerDTO.getAccount()) ? "":onlineLedgerDTO.getAccount()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.FIN_BOOK_TALLY_ACC_OVERDRAFT_MONEY, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
				}
			}
		}
    }
	/**
	 * 功能介绍 : 计算发生额。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	private BigDecimal calcMoney(BigDecimal debitMoney, BigDecimal creditMoney, Integer balanceWay) {
		return debitMoney.subtract(creditMoney).multiply(BigDecimal.valueOf(balanceWay));
	}
	/**
	 * 功能介绍 : 恢复原记账凭证的金额和笔数。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 * @param oriAccountMoney 发生额
	 */
	protected abstract void recoverBalance(ProcessType processType, OperateType opType, BigDecimal oriAccountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO);
	
	/**
	 *  功能介绍 : 计算新记账凭证的金额和发生笔数，
	 *  如果账户出现透支或者大于允许透支的金额，则在实现子类中抛出相应异常。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * @version 1.0
	 * @author liyh 
	 * @param oriAccountMoney 发生额
	 */
	protected abstract void calcBalance(ProcessType processType, OperateType opType, BigDecimal oriAccountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO, String voucherStatus);
}
