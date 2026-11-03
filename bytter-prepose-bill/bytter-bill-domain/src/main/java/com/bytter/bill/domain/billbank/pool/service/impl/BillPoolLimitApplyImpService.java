package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.domain.billbank.pool.service.BillPoolImpService;
import com.bytter.bill.domain.billbank.pool.service.BillPoolLimitImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolLimitApplyService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>额度申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-24 <br>
 */
@Service("billPoolLimitApplyImpService")
@Transactional
public class BillPoolLimitApplyImpService implements IBillPoolLimitApplyService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolImpService billPoolImpService;
	@Autowired
	private BillPoolLimitImpService billPoolLimitImpService;
	@Override
	public List<PoolLimitVO> queryLimitApplyByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitByBifCode(BillPoolConstant.LIMIT_TYPE_APPLY, bifCode, reCode);
	}
	@Override
	public List<PoolLimitVO> queryLimitApplyResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitResultByBifCode(BillPoolConstant.LIMIT_TYPE_APPLY, bifCode, reCode);
	}
	@Override
	public void updatePoolLimitApply(PoolLimitVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.updatePoolLimit(BillPoolConstant.LIMIT_TYPE_APPLY, paramsVo);
	}
	@Override
	public List<PoolBaseVO> queryCheckPoolByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		return billPoolImpService.queryPoolByBifCode(bifCode);
	}
	@Override
	public synchronized void savePoolLimitApplyCheck(List<PoolLimitVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.savePoolLimitCheck(BillPoolConstant.LIMIT_TYPE_APPLYCHECK, paramsVo);
	}
	@Override
	public List<PoolLimitVO> queryLimitApplyCheckByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitByBifCode(BillPoolConstant.LIMIT_TYPE_APPLYCHECK, bifCode, reCode);
	}
	@Override
	public List<PoolLimitVO> queryLimitApplyCheckResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitResultByBifCode(BillPoolConstant.LIMIT_TYPE_APPLYCHECK, bifCode, reCode);
	}
	@Override
	public void updatePoolLimitApplyCheck(PoolLimitVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.updatePoolLimit(BillPoolConstant.LIMIT_TYPE_APPLYCHECK, paramsVo);
	}
	
}
