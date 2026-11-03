package com.bytter.bill.domain.billbank.pool.service.impl;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolEmitFinaApplyImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolContract;
import com.bytter.bill.domain.billbank.pool.service.BillPoolBankExcImpService;
import com.bytter.bill.domain.billbank.pool.service.BillPoolDtlImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolEmitFinaApplyService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>出票融资申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-24 <br>
 */
@Service("billPoolEmitFinaApplyImpService")
@Transactional
public class BillPoolEmitFinaApplyImpService implements IBillPoolEmitFinaApplyService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolBankExcImpService billPoolBankExcImpService;
	
	@Autowired
	private BillPoolDtlImpService billPoolDtlImpService;
	@Autowired
	private BillPoolEmitFinaApplyImpDao billPoolEmitFinaApplyImpDao;
	
	@Override
	public List<PoolEmitFinaApplyVO> queryEmitFinaByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaApplyInfo(BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	@Override
	public List<PoolEmitFinaApplyVO> queryEmitFinaResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaApplyInfo(BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	@Override
	public void updatePoolEmitFinaApply(PoolEmitFinaApplyVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(StrUtil.isNotEmpty(paramsVo.getBusId())){
			PoolEmitFinaApplyVO info = this.queryEmitFinaApplyById(paramsVo.getBusId());
			if (info != null && (StrUtil.isNotEmpty(paramsVo.getOptStatus()) || StrUtil.isNotEmpty(paramsVo.getReCode()))
					&& (!paramsVo.getOptStatus().equals(info.getOptStatus()) || !paramsVo.getReCode().equals(info.getReCode()))) {
				billPoolDtlImpService.updateAccrecordsByBusId(paramsVo.getBusId(), paramsVo.getOptStatus(), paramsVo.getReCode(), paramsVo.getReMsg(), paramsVo.getBillId());
				//失败-1或者发送成功18的时候设置票据池明细指令状态
				if (StrUtil.isNotEmpty(info.getPdId())
						&& (BillPoolConstant.optStatus_1.equals(info.getOptStatus())
						|| BillPoolConstant.optStatus_18.equals(info.getOptStatus()))) {
					billPoolDtlImpService.updatePoolDtlOptStatusByBusId(info.getPdId(), info.getOptStatus());
					//发送银行成功以后设置银行发送流水和申请流水
					if (BillPoolConstant.optStatus_18.equals(info.getOptStatus())) {
						paramsVo.setBusId(info.getPdId());
						billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
					}
				}
			}
		}
	}
	@Override
	public void savePoolEmitFinaCon(PoolEmitFinaConVO conVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("conVo="+conVo.toString());
		}
		AbPoolContract poolCon=new AbPoolContract();
		if(StrUtil.isNotEmpty(conVo.getConId())){
			AbPoolContract poolConOld=(AbPoolContract) billPoolEmitFinaApplyImpDao.load(AbPoolContract.class, conVo.getConId());
			if(poolConOld!=null){
				conVo.setDtlId(poolConOld.getDtlId());
			}
		}
		//根据获取票据池明细表ID
		PoolEmitFinaApplyVO info=null;
		if(StrUtil.isNotEmpty(conVo.getDtlId())){
			info=this.queryEmitFinaApplyByPdId(conVo.getDtlId());
		}else if(StrUtil.isNotEmpty(conVo.getBusId())){
			info=this.queryEmitFinaApplyById(conVo.getBusId());
		}else if(StrUtil.isNotEmpty(conVo.getBusAppNo())){
			info=this.queryEmitFinaApplyByAppNo(conVo.getBusAppNo());
		}
		if(info!=null
				&&StrUtil.isNotEmpty(info.getPdId())){
			//判断是否有待处理数据有则不新增
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("dtlId", info.getPdId());
			params.put("status", BillPoolConstant.CON_STATUS_0);
			if(!billPoolEmitFinaApplyImpDao.isAbPoolContract(params)){
				BeanUtils.copyNotNullProperties(conVo, poolCon);
				poolCon.setDtlId(info.getPdId());
				poolCon.setStatus(BillPoolConstant.CON_STATUS_0);
				poolCon.setConName(conVo.getConName());
				poolCon.setConFtype(conVo.getConFtype());
				poolCon.setConInfo(conVo.getConInf());
				poolCon.setConInfoMd5(conVo.getConInfMd5());
				if(StrUtil.isNotEmpty(poolCon.getConName())){
					int index=poolCon.getConName().lastIndexOf(".");
					if(index>1){
						if(StrUtil.isEmpty(poolCon.getConFtype())){
							poolCon.setConFtype(poolCon.getConName().substring(index+1));
						}
						poolCon.setConName(poolCon.getConName().substring(0, index));
					}
				}
				poolCon.setCreateTime(new Date());
				billPoolEmitFinaApplyImpDao.save(poolCon);
			}
		}
	}
	
	@Override
	public List<PoolEmitFinaConVO> queryResetPoolEmitFinaConByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaConInfo(BillPoolConstant.CON_STATUS_2, BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	@Override
	public List<PoolEmitFinaConVO> queryResetPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaConInfo(BillPoolConstant.CON_STATUS_2, BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	@Override
	public void updateResetPoolEmitFinaCon(PoolEmitFinaConVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(StrUtil.isNotEmpty(paramsVo.getBusId())){
			billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
			PoolEmitFinaConVO info=queryEmitFinaConById(paramsVo.getBusId());
			if(StrUtil.isNotEmpty(paramsVo.getOptStatus())
					&&!paramsVo.getOptStatus().equals(info.getOptStatus())){
				billPoolEmitFinaApplyImpDao.updatePoolConByBusId(paramsVo.getBusId(), paramsVo.getOptStatus());
			}
		}
	}
	@Override
	public List<PoolEmitFinaConVO> querySignOkPoolEmitFinaConByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaConInfo(BillPoolConstant.CON_STATUS_1, BillPoolConstant.optStatus_16, bifCode, reCode);
	}
	@Override
	public List<PoolEmitFinaConVO> querySignOkPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolEmitFinaApplyImpDao.queryEmitFinaConInfo(BillPoolConstant.CON_STATUS_1, BillPoolConstant.optStatus_18, bifCode, reCode);
	}
	@Override
	public void updateSignOkPoolEmitFinaCon(PoolEmitFinaConVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(StrUtil.isNotEmpty(paramsVo.getBusId())){
			billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
			PoolEmitFinaConVO info=queryEmitFinaConById(paramsVo.getBusId());
			if(StrUtil.isNotEmpty(paramsVo.getOptStatus())
					&&!paramsVo.getOptStatus().equals(info.getOptStatus())){
				billPoolEmitFinaApplyImpDao.updatePoolConByBusId(paramsVo.getBusId(), paramsVo.getOptStatus());
				if(BillPoolConstant.optStatus_19.equals(paramsVo.getOptStatus())){
					billPoolDtlImpService.updatePoolDtlOptStatusByBusId(info.getDtlId(), BillPoolConstant.optStatus_19);
				}
			}
		}
	}
	
	/**
	 * <b>功能：</b>根据业务ID获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param pdId 票据池明细表ID
	 * @return
	 */
	public PoolEmitFinaApplyVO queryEmitFinaApplyByPdId(String pdId){
		if (logger.isDebugEnabled()) {
			logger.debug("pdId="+pdId);
		}
		if(StrUtil.isNotEmpty(pdId)){
			List<PoolEmitFinaApplyVO> list=billPoolEmitFinaApplyImpDao.queryEmitFinaApplyInfoByPdId(pdId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据业务ID获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public PoolEmitFinaApplyVO queryEmitFinaApplyById(String busId){
		if (logger.isDebugEnabled()) {
			logger.debug("busId="+busId);
		}
		if(StrUtil.isNotEmpty(busId)){
			List<PoolEmitFinaApplyVO> list=billPoolEmitFinaApplyImpDao.queryEmitFinaApplyInfo(busId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据业务ID获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public PoolEmitFinaConVO queryEmitFinaConById(String busId){
		if (logger.isDebugEnabled()) {
			logger.debug("busId="+busId);
		}
		if(StrUtil.isNotEmpty(busId)){
			List<PoolEmitFinaConVO> list=billPoolEmitFinaApplyImpDao.queryEmitFinaConInfo(busId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据申请单号appNo获取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param appNo 申请单号
	 * @return
	 */
	public PoolEmitFinaApplyVO queryEmitFinaApplyByAppNo(String appNo){
		if (logger.isDebugEnabled()) {
			logger.debug("appNo="+appNo);
		}
		if(StrUtil.isNotEmpty(appNo)){
			List<PoolEmitFinaApplyVO> list=billPoolEmitFinaApplyImpDao.queryEmitFinaApplyInfoByAppNo(appNo);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
}
