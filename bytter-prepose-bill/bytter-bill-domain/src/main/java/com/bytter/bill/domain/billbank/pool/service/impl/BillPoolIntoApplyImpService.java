package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolIntoApplyService;
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
 * <b>功能：</b>票据入池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-27 <br>
 */
@Service("billPoolIntoApplyImpService")
@Transactional
public class BillPoolIntoApplyImpService implements IBillPoolIntoApplyService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	
	@Override
	public List<PoolBillVoucherVO> queryIntoApplyByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_IN, bifCode, reCode);
	}
	
	@Override
	public List<PoolBillVoucherVO> queryIntoApplyResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_IN, bifCode, reCode);
	}
	@Override
	public void updatePoolIntoApply(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_IN, paramsVo);
	}
//
//	@Override
//	public List<PoolBillVoucherVO> queryAgainIntoApplyByBifCode(String bifCode, String reCode) {
//		return null;
//	}
//
//	@Override
//	public List<PoolBillVoucherVO> queryAgainIntoApplyResultByBifCode(String bifCode, String reCode) {
//		return null;
//	}
//
//	@Override
//	public void updatePoolAgainIntoApply(PoolBillVoucherVO paramsVo) {
//		
//	}
	@Override
	public List<PoolBillVoucherVO> queryIntoManagedByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_INOP_HOSTING, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryIntoManagedResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_INOP_HOSTING, bifCode, reCode);
	}
	@Override
	public void updatePoolIntoManaged(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_INOP_HOSTING, paramsVo);
	}
	@Override
	public List<PoolBillVoucherVO> queryIntoPledgeByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_INOP_PLEDGE, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryIntoPledgeResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_INOP_PLEDGE, bifCode, reCode);
	}
	@Override
	public void updatePoolIntoPledge(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_INOP_PLEDGE, paramsVo);
	}
	@Override
	public List<PoolBillVoucherVO> queryDeleteBillByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_IN_DEL, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryDeleteBillResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_IN_DEL, bifCode, reCode);
	}
	@Override
	public void updateDeleteBill(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_IN_DEL, paramsVo);
	}
}
