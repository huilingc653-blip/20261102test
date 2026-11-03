package com.bytter.biz.finacial.accbook.service.base;
import java.math.BigDecimal;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 复核余额处理
 * @author liyh
 *
 */
@Component
@Service("checkBalanceHandler")
@Transactional
public class CheckBalanceHandler extends CommonBalanceHandler {
	@Override
	protected BigDecimal getBalance(FcOnlineLedgerDTO onlineLedgerDTO) {
		return onlineLedgerDTO.getCheckBalance();
	}
	@Override
	protected Integer getCnt(FcOnlineLedgerDTO onlineLedgerDTO) {
		return onlineLedgerDTO.getCheckCnt();
	}
	@Override
	protected void recoverBalance(ProcessType processType, OperateType opType, BigDecimal oriAccountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO) {
		//记账业务未完成不需要还原复核余额和笔数
	}
	/**
	 * 复核通过情况下金额和笔数
	 */
	@Override
	protected void calcBalance(ProcessType processType, OperateType opType, BigDecimal accountMoney,
			FcOnlineLedgerDTO onlineLedgerDTO, String voucherStatus) {
		BigDecimal balance = getBalance(onlineLedgerDTO);
		BigDecimal accBal = balance;
		Integer CheckCnt = getCnt(onlineLedgerDTO);
		if(EmptyUtils.isEmpty(CheckCnt)){
			CheckCnt = 0;
		}
		Integer cnt = CheckCnt;
		if (ProcessType.PROCESS_COMMON == processType) {
			//金额和笔数正常增加
			accBal = balance.add(accountMoney);
			cnt = CheckCnt + 1;
        } else if (ProcessType.PROCESS_RUSH == processType) {
        	accBal = balance.add(accountMoney);
			cnt = CheckCnt + 1;
        } else if (ProcessType.PROCESS_INVALID == processType) {
        	//金额和笔数减少
        	accBal = balance.subtract(accountMoney);
			cnt = CheckCnt - 1;
        } else {
        }
		onlineLedgerDTO.setCheckBalance(accBal);
        onlineLedgerDTO.setCheckCnt(cnt);
        onlineLedgerDTO.setBookMoney(onlineLedgerDTO.getBookMoney().add(accBal.subtract(balance)));
        onlineLedgerDTO.setBookCnt(onlineLedgerDTO.getBookCnt() + cnt - CheckCnt);
	}
	
	
	@Override
	protected void setCurId(FcAccbookDtl bookDtl, FcOnlineLedgerDTO onlineLedgerDTO) {
	}
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
