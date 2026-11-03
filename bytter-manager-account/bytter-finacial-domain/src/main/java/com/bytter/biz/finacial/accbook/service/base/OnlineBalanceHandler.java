package com.bytter.biz.finacial.accbook.service.base;

import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
/**
 * 记账余额处理
 * @author liyh
 *
 */
@Component
@Service("onlineBalanceHandler")
@Transactional
public class OnlineBalanceHandler extends CommonBalanceHandler {
	@Override
	protected BigDecimal getBalance(FcOnlineLedgerDTO onlineLedgerDTO) {
		return onlineLedgerDTO.getOnlineBalance();
	}
	
	@Override
	protected Integer getCnt(FcOnlineLedgerDTO onlineLedgerDTO) {
		return onlineLedgerDTO.getTallyCnt();
	}
	@Override
	protected void recoverBalance(ProcessType processType, OperateType opType, BigDecimal oriAccountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO) {
		BigDecimal balance = getBalance(onlineLedgerDTO);
		BigDecimal accBal = balance;
		Integer tallyCnt = getCnt(onlineLedgerDTO);
		Integer cnt = tallyCnt;
		if (ProcessType.PROCESS_COMMON == processType) {
            accBal = recoverCommonBalance(opType, balance, oriAccountMoney);
            cnt = recoverCommonCnt(opType, tallyCnt);//账务笔数-1
        } else if (ProcessType.PROCESS_RUSH == processType) {
        	accBal = recoverCommonBalance(opType, balance, oriAccountMoney);
        	cnt = recoverCommonCnt(opType, tallyCnt);
        } else if (ProcessType.PROCESS_INVALID == processType) {
        	accBal = BigDecimal.ZERO;
            if ((opType == OperateType.ADD) || opType == OperateType.SUBMIT) {
            	accBal = balance.subtract(oriAccountMoney);
            } else {
            	accBal = balance;
			}
        } else {
        	accBal = balance;
        }
		onlineLedgerDTO.setOnlineBalance(accBal);
        onlineLedgerDTO.setTallyCnt(cnt);
        onlineLedgerDTO.setBookMoney(onlineLedgerDTO.getBookMoney().add(accBal.subtract(balance)));
        onlineLedgerDTO.setBookCnt(onlineLedgerDTO.getBookCnt() + cnt - tallyCnt);
	}
	
	private BigDecimal recoverCommonBalance(OperateType opType, BigDecimal balance, BigDecimal oriAccountMoney) {
        if ( (opType == OperateType.ADD || opType == OperateType.MODIFY)
                || opType == OperateType.SUBMIT) {
            return balance.subtract(oriAccountMoney);
        }
        return balance;
    }
	
	private Integer recoverCommonCnt(OperateType opType, Integer cntTot) {
		if ( (opType == OperateType.ADD || opType == OperateType.MODIFY)
				|| opType == OperateType.SUBMIT) {
			return cntTot - 1;
		}
		return cntTot;
	}
	@Override
	protected void calcBalance(ProcessType processType, OperateType opType, BigDecimal accountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO, String voucherStatus) {
		BigDecimal balance = getBalance(onlineLedgerDTO);
		BigDecimal accBal = balance;
		Integer tallyCnt = getCnt(onlineLedgerDTO);
		Integer cnt = tallyCnt;
		if (ProcessType.PROCESS_COMMON == processType) {
            accBal = calcCommonBalance(opType, balance, accountMoney);
            cnt = calcCommonCnt( opType, tallyCnt);
        } else if (ProcessType.PROCESS_RUSH == processType) {
        	accBal = calcCommonBalance(opType, balance, accountMoney);
        	cnt = calcCommonCnt(opType, tallyCnt);
        } else if (ProcessType.PROCESS_INVALID == processType) {
        	accBal = calcInvalidBalance(opType, balance, accountMoney, voucherStatus);
        	cnt = calcInvalidCnt(opType, tallyCnt, voucherStatus);
        } else {
        	accBal = balance;
        }
		onlineLedgerDTO.setOnlineBalance(accBal);
        onlineLedgerDTO.setTallyCnt(cnt);
        onlineLedgerDTO.setBookMoney(onlineLedgerDTO.getBookMoney().add(accBal.subtract(balance)));
		onlineLedgerDTO.setBookCnt(onlineLedgerDTO.getBookCnt() + cnt - tallyCnt);
	}
	
	private BigDecimal calcCommonBalance(OperateType opType, BigDecimal balance, BigDecimal accountMoney) {
        if ((opType == OperateType.ADD || opType == OperateType.MODIFY)
                || opType == OperateType.SUBMIT) {
            return balance.add(accountMoney);
        } else if (opType == OperateType.DELETE) {
            return balance.subtract(accountMoney);
        } else if (opType == OperateType.BACK) {//退回不处理余额
            return balance;
        }
        return balance;
    }
	
	private BigDecimal calcInvalidBalance(OperateType opType, BigDecimal balance, BigDecimal accountMoney,
			String voucherStatus) {
		if ((opType == OperateType.ADD) || opType == OperateType.SUBMIT) {
			// 如果是作废暂存或者打回，则不用更新账户余额，其他情况要更新余额
			if (opType == OperateType.SUBMIT
					&& (FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get().equals(voucherStatus)
							|| FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get().equals(voucherStatus)
							|| FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get().equals(voucherStatus))) {
				return balance;
			}
			return balance.subtract(accountMoney);
		} else if (opType == OperateType.MODIFY) {
			return balance;
		} else if (opType == OperateType.DELETE) {
			return balance.add(accountMoney);
		} else if (opType == OperateType.BACK) {
			return balance;
		}
		return balance;
	}
	
	
    
    private Integer calcCommonCnt(OperateType opType, Integer cntTot) {
		if(null ==cntTot||cntTot==0){
			return 0;
		}
        if (( (opType == OperateType.ADD || opType == OperateType.MODIFY))
                || opType == OperateType.SUBMIT) {
            return cntTot + 1;
        } else if (opType == OperateType.DELETE) {
            return cntTot - 1;
        } else if (opType == OperateType.BACK) {//退回不处理笔数
            return cntTot;
        }
        return cntTot;
    }
    
	private Integer calcInvalidCnt(OperateType operateType, Integer cntTot, String voucherStatus) {
		if ((operateType == OperateType.ADD) || operateType == OperateType.SUBMIT) {
			// 如果是作废暂存或者打回，则不用更新账户余额，其他情况要更新记账笔数
			if (operateType == OperateType.SUBMIT
					&& (FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get().equals(voucherStatus)
							|| FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get().equals(voucherStatus)
							|| FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get().equals(voucherStatus))) {
				return cntTot;
			}
			return cntTot - 1;
		} else if (operateType == OperateType.MODIFY) {
			return cntTot;
		} else if (operateType == OperateType.DELETE) {
			return cntTot + 1;
		} else if (operateType == OperateType.BACK) {
			return cntTot;
		}
		return cntTot;
	}
	@Override
	protected void setCurId(FcAccbookDtl bookDtl, FcOnlineLedgerDTO onlineLedgerDTO) {
		bookDtl.setCurCode(onlineLedgerDTO.getCurId());//设置币别
	}
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
