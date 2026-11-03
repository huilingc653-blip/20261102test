package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.dao.BtBankAccFreezeDao;
import com.bytter.system.entity.BtBankAccFreeze;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @Title: Service
* @Description: BT_BANK_ACC_FREEZE
* @author onlineGenerator
* @date 2017-06-14 14:26:46
* @version 1.0
*
*/
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_FREEZE)
@Service("btBankAccFreezeService")
@Transactional
public class BtBankAccFreezeService extends BaseService<BtBankAccFreeze,String> implements IWorkFlowListener, ICorpDisableVerifyService {
	@Autowired
	private BtBankAccFreezeDao btBankAccFreezeDao;

	/**
	 * 获取冻结/解冻金额
	 * @param bankAcc
	 * @return
	 */
	public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, Date balDate){
		//冻结金额
		BigDecimal freezeMoney = btBankAccFreezeDao.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_0.toString(), balDate);
		//解冻金额
		BigDecimal unFreezeMoney = btBankAccFreezeDao.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_1.toString(), balDate);
		return freezeMoney.subtract(unFreezeMoney);
	}


	@Override
	public BaseDao<BtBankAccFreeze, String> getEntityDao() {
		return null;
	}

	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {

	}

	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {

	}

	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		return null;
	}
}