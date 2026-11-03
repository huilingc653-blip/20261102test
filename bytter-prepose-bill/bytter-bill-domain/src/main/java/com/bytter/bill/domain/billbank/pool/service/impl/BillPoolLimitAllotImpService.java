package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.domain.billbank.pool.service.BillPoolImpService;
import com.bytter.bill.domain.billbank.pool.service.BillPoolLimitImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolLimitAllotService;
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
 * <b>功能：</b>额度分配服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-24 <br>
 */
@Service("billPoolLimitAllotImpService")
@Transactional
public class BillPoolLimitAllotImpService implements IBillPoolLimitAllotService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolImpService billPoolImpService;
	@Autowired
	private BillPoolLimitImpService billPoolLimitImpService;
	@Override
	public List<PoolLimitVO> queryLimitAllotByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitByBifCode(BillPoolConstant.LIMIT_TYPE_ALLOT, bifCode, reCode);
	}
	@Override
	public List<PoolLimitVO> queryLimitAllotResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitResultByBifCode(BillPoolConstant.LIMIT_TYPE_ALLOT, bifCode, reCode);
	}
	@Override
	public void updatePoolLimitAllot(PoolLimitVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.updatePoolLimit(BillPoolConstant.LIMIT_TYPE_ALLOT, paramsVo);
	}
	@Override
	public List<PoolBaseVO> queryCheckPoolByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		return billPoolImpService.queryPoolByBifCode(bifCode);
	}
	@Override
	public synchronized void savePoolLimitAllotCheck(List<PoolLimitVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.savePoolLimitCheck(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK, paramsVo);
	}
	@Override
	public List<PoolLimitVO> queryLimitAllotCheckByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitByBifCode(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK, bifCode, reCode);
	}
	@Override
	public List<PoolLimitVO> queryLimitAllotCheckResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolLimitImpService.queryLimitResultByBifCode(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK, bifCode, reCode);
	}
	@Override
	public void updatePoolLimitAllotCheck(PoolLimitVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolLimitImpService.updatePoolLimit(BillPoolConstant.LIMIT_TYPE_ALLOTCHECK, paramsVo);
	}
	
}
