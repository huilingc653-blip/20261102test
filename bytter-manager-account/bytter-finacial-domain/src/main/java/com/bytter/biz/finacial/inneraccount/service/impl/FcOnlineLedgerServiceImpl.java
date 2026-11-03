package com.bytter.biz.finacial.inneraccount.service.impl;

import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcAccVO;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.finacial.inneraccount.dao.FcOnlineLedgerDao;
import com.bytter.biz.finacial.inneraccount.entity.FcOnlineLedger;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 账户联机余额对外接口实现类
 * @author liyh
 *
 */
@Service("fcOnlineLedgerServiceImpl")
@Transactional
@DubboService
public class FcOnlineLedgerServiceImpl implements IFcOnlineLedgerService {
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	@Autowired
	private FcOnlineLedgerDao fcOnlineLedgerDao;
	@Override
	public List<FcOnlineLedgerDTO> getLedgerDTOsByAccSet(List<String> accList) {
		return fcOnlineLedgerService.getLedgerDTOsByAccSet(accList);
	}
	@Override
	public List<FcOnlineLedgerDTO> getPoolLedgerDTOsByAccSet(List<String> accList) {
		return fcOnlineLedgerService.getPoolLedgerDTOsByAccSet(accList);
	}
	@Override
	public List<FcOnlineLedgerDTO> getFcOnlineLedgerByAccList(List<String> accList) {
		return fcOnlineLedgerService.getFcOnlineLedgerByAccList(accList);
	}
	@Override
	public void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs) {
		fcOnlineLedgerService.updateOnlineBalanceList(ledgerDTOs);
	}
	@Override
	public void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs, List<FcOnlineLedgerDTO> origLedgerDTOs) {
		fcOnlineLedgerService.updateOnlineBalanceList(ledgerDTOs, origLedgerDTOs);
	}
	@Override
	public void updateCheckBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs) {
		fcOnlineLedgerService.updateCheckBalanceList(ledgerDTOs);
	}
	@Override
	public FcOnlineLedgerDTO getByBlnctrAcc(String blnctrAcc) {
		return this.fcOnlineLedgerService.getByBlnctrAcc(blnctrAcc);
	}

	@Override
	public List<FcOnlineLedgerDTO> getByBlnctrAccByList(List<String> blnctrAcc) {
		return this.fcOnlineLedgerService.getByBlnctrAccByList(blnctrAcc);
	}
	/**
	 * 效验金额
	 * @return true 不通过，false 通过
	 */
	@Override
	public boolean validAmt(String blnctrAcc){
		boolean flag = false;
		FcOnlineLedger onlineLedger=fcOnlineLedgerService.getEntityByAccount(blnctrAcc);
		if (onlineLedger != null) {
			BigDecimal bal = onlineLedger.getCheckBalance();//复核余额
			BigDecimal onlBal = onlineLedger.getOnlineBalance();//联机余额
			if (bal.compareTo(BigDecimal.ZERO)!=0 || onlBal.compareTo(BigDecimal.ZERO)!=0) {
				flag = true;
			}
		}
		return flag;
	}
	@Override
	public FcOnlineLedgerVO getEntityByAccount(String account) {
		FcOnlineLedger fol =  fcOnlineLedgerService.getEntityByAccount(account);
		FcOnlineLedgerVO ledger = null;
		if(null != fol) {
			ledger = new FcOnlineLedgerVO();
			BeanUtils.copyProperties(fol, ledger);
		}
		return ledger;
	}
	@Override
	public void insertFclOnlineLedger(FcAccVO acc, String loginName) {
		fcOnlineLedgerService.insertFclOnlineLedger(acc, loginName);
	}
	@Override
	public void updateFreezeMoney(FcOnlineLedgerVO onlineLedger, BigDecimal money,
			boolean isFreeze, String loginName) {
		BigDecimal freezeMoney = onlineLedger.getFreezeMoney();
		if (isFreeze) {
			freezeMoney = freezeMoney.add(money);
		} else {
			freezeMoney = freezeMoney.subtract(money);
		}
		fcOnlineLedgerDao.updateFreezeMoney(freezeMoney, DateUtils.getDate(), loginName, onlineLedger.getAccount());
	}
	/**
	 * 账户初始化更新账户余额
	 *
	 * @param account
	 * @param balance
	 */
	public void updateOnlineBalanceByInitAcc(String account, BigDecimal balance) {
		fcOnlineLedgerService.updateOnlineBalanceByInitAcc( account,  balance);
	}
	@Override
	public void updateFreezeMoney(String account, BigDecimal money, boolean isFreeze) {
		FcOnlineLedger onlineLedger = fcOnlineLedgerDao.getUniqueBy("account", account);
		BigDecimal freezeMoney = onlineLedger.getFreezeMoney();
		if (isFreeze) {
			freezeMoney = freezeMoney.add(money);
		} else {
			freezeMoney = freezeMoney.subtract(money);
		}
		fcOnlineLedgerDao.updateFreezeMoney(account, freezeMoney, onlineLedger.getFreezeMoney());
	}
	@Override
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FcOnlineLedgerVO getFcOnlineLedgerById(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void save(FcOnlineLedgerVO fcAccuBalance) {
		// TODO Auto-generated method stub
	}
	@Override
	public void saveOrUpdate(FcOnlineLedgerVO fcAccuBalance) {
		// TODO Auto-generated method stub
	}
	@Override
	public void deleteEntity(FcOnlineLedgerVO fcAccuBalance) {
		// TODO Auto-generated method stub
	}
    @Override
    public List<FcOnlineLedgerDTO> getBalanceByAcc(List<String> accList) {
        return fcOnlineLedgerService.getBalanceByAcc(accList);
    }

	@Override
	public Map<String, FcOnlineLedgerDTO> getBalanceByAccMap(List<String> accList) {
		return fcOnlineLedgerService.getBalanceByAccMap(accList);
	}

	@Override
	public FcOnlineLedgerDTO getByBlnctrAcc(String payeeCounterAcc, String poolSign) {
		return this.fcOnlineLedgerService.getByBlnctrAcc(payeeCounterAcc,poolSign);
	}

	@Override
	public void updateBalance(List<FcOnlineLedgerDTO> balanceList) {
		fcOnlineLedgerService.updateBalance(balanceList);
	}
}
