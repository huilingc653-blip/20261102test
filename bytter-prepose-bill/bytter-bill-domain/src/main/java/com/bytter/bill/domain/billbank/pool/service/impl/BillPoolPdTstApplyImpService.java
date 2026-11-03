package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolPdTstApplyService;
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
 * <b>功能：</b>票据质押托管转换申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-27 <br>
 */
@Service("billPoolPdTstApplyImpService")
@Transactional
public class BillPoolPdTstApplyImpService implements IBillPoolPdTstApplyService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	@Override
	public List<PoolBillVoucherVO> queryPdTstApplyByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		//System.out.println("==============queryPdTstApplyByBifCode"+billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING, bifCode, reCode));
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryPdTstApplyResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING, bifCode, reCode);
	}
	@Override
	public void updatePoolPdTstApply(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING, paramsVo);
	}
	@Override
	public List<PoolBillVoucherVO> queryPdTstPledgeByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryPdTstPledgeResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE, bifCode, reCode);
	}
	@Override
	public void updatePoolPdTstPledge(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE, paramsVo);
	}
	@Override
	public List<PoolBillVoucherVO> queryRemovePledgeByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, bifCode, reCode);
	}
	@Override
	public List<PoolBillVoucherVO> queryRemovePledgeResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, bifCode, reCode);
	}
	@Override
	public void updatePoolRemovePledge(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		billPoolManageImpService.updatePoolPdTstPledgeConfirm(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, paramsVo);
		//billPoolManageImpService.updatePoolApply(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, paramsVo);
	}
	
	@Override
	public List<PoolBillVoucherVO> queryPdTstPledgeConfirmFailByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		//return billPoolManageImpService.queryApplyByBifCode(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, bifCode, reCode);
		return billPoolManageImpService.queryPoolBillVoucherInfo(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, BillPoolConstant.optStatus_18,
				bifCode, null, null,BillPoolConstant.POOL_PLEDGE_STATUS_0);
	}
	@Override
	public List<PoolBillVoucherVO> queryPdTstPledgeConfirmResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		//return billPoolManageImpService.queryApplyResultByBifCode(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, bifCode, reCode);
		return billPoolManageImpService.queryPoolBillVoucherInfo(BillPoolConstant.BRIEF_TYPE_TO_HOSTING, BillPoolConstant.optStatus_18,
				bifCode, null, null,BillPoolConstant.POOL_PLEDGE_STATUS_1);
	}
}
