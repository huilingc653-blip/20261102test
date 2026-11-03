package com.bytter.guarantee.bond.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.guarantee.bond.dao.FcBondBalDao;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.vo.FcBondBalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondBalService
 * @Description:
 * @date 2018年9月12日 下午12:36:08
 */
@Service("FcBondBalService")
@Transactional
public class FcBondBalService extends BaseService<FcBondBal, String> implements IWorkFlowListener {
	@Autowired
	private FcBondBalDao fcBondBalDao;
	/**
	 * 根据bondId获取保证金余额数据
	 */
	@Transactional(readOnly=true)
	public FcBondBalVO getBondBalByBondId(String bondId) {
		return fcBondBalDao.getBalByBondId(bondId);
	}
	@Transactional(readOnly=true)
	public FcBondBal getBondBalById(String bondId) {
		return fcBondBalDao.getBalById(bondId);
	}
	/**
	 * 更新保证金余额表中相应的信息
	 * @param fcBondBal
	 */
	public void updateFcBondBal(FcBondBal fcBondBal){
		super.saveOrUpdate(fcBondBal);
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public BaseDao<FcBondBal, String> getEntityDao() {
		// TODO Auto-generated method stub
		return fcBondBalDao;
	}
	public void deleteByBondId(String bondId){
		fcBondBalDao.deleteByBondId(bondId);
	}
	public void backBondAddMoney(String bondId, BigDecimal money){
		FcBondBal fcbondbal = fcBondBalDao.getBalById(bondId);
		BigDecimal addMoney = fcbondbal.getAddMoney();
		BigDecimal backMoney = addMoney.subtract(money);
		fcbondbal.setAddMoney(backMoney);
		this.updateEntity(fcbondbal);
	}
}
