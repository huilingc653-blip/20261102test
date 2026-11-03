package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolOutApplyService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据出池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-27 <br>
 */
@Service("billPoolOutApplyImpService")
@Transactional
public class BillPoolOutApplyImpService implements IBillPoolOutApplyService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	@Override
	public List<PoolBillVoucherVO> queryOutApplyByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_OUT, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryOutApplyResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_OUT, bifCode, reCode);
	}
	@Override
	public void updatePoolOutApply(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_OUT, paramsVo);
	}
	@Override
	public List<PoolBillVoucherVO> queryRemovePledgeByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_OUT_REPLEDGE, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryRemovePledgeResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_OUT_REPLEDGE, bifCode, reCode);
	}
	@Override
	public void updatePoolRemovePledge(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_OUT_REPLEDGE, paramsVo);
	}
}
