package com.bytter.bill.domain.billbank.pool.service.impl;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolBailImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolBail;
import com.bytter.bill.domain.billbank.pool.service.BillPoolBankExcImpService;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolBailService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>保证金服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-24 <br>
 */
@Service("billPoolBailImpService")
@Transactional
public class BillPoolBailImpService implements IBillPoolBailService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolBankExcImpService billPoolBankExcImpService;
	
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	
	@Autowired
	private BillPoolBailImpDao billPoolBailImpDao;
	/**
	 * 根据保证金账户获取保证金账户信息表
	 * @param bankAcc 保证金账户
	 * @return
	 */
	public AbPoolBail getBillPoolBailByBankAcc(String bankAcc){
		return billPoolBailImpDao.getBillPoolBailByBankAcc(bankAcc);
	}
	
	@Override
	public List<PoolBailVO> queryBailInByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolBailImpDao.queryBailInfo(BillPoolConstant.BAIL_TYPE_IN, BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	@Override
	public List<PoolBailVO> queryBailInResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolBailImpDao.queryBailInfo(BillPoolConstant.BAIL_TYPE_IN, BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	@Override
	public void updatePoolBailInApply(PoolBailVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		updatePoolBail(BillPoolConstant.BAIL_TYPE_IN, paramsVo);
	}
	@Override
	public List<PoolBailVO> queryBailOutByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolBailImpDao.queryBailInfo(BillPoolConstant.BAIL_TYPE_OUT, BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	@Override
	public List<PoolBailVO> queryBailOutResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolBailImpDao.queryBailInfo(BillPoolConstant.BAIL_TYPE_OUT, BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	@Override
	public void updatePoolBailOut(PoolBailVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		updatePoolBail(BillPoolConstant.BAIL_TYPE_OUT, paramsVo);
	}
	/**
	 * <b>功能：修改保证金的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	public void updatePoolBail(String type,PoolBailVO paramsVo){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+",paramsVo="+paramsVo.toString());
		}
		if(StrUtil.isNotEmpty(paramsVo.getBusId())){
			billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
			PoolBailVO info=queryBailById(type, paramsVo.getBusId());
			if(StrUtil.isNotEmpty(paramsVo.getOptStatus())
					&&!paramsVo.getOptStatus().equals(info.getOptStatus())){
				billPoolBailImpDao.updateBailOptStatusByBusId(type, paramsVo.getBusId(), paramsVo.getOptStatus(),paramsVo.getReMsg(),paramsVo.getBillId());
				if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(paramsVo.getOptStatus())){
					billPoolManageImpService.saveBillPoolQueryExc(info.getBpId(), info.getCorpId(), BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT);
					billPoolManageImpService.saveBillPoolQueryExc(info.getBpId(), info.getCorpId(), BillPoolConstant.POOL_EXC_QUERY_TYPE_BAIL);
				}
			}
		}
	}
	/**
	 * <b>功能：</b>根据业务ID获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param busId 业务ID
	 * @return
	 */
	public PoolBailVO queryBailById(String type,String busId){
		if (logger.isDebugEnabled()) {
			logger.debug("type="+type+" ,busId="+busId);
		}
		if(StrUtil.isNotEmpty(busId)){
			List<PoolBailVO> list=billPoolBailImpDao.queryBailInfo(type, busId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
}
