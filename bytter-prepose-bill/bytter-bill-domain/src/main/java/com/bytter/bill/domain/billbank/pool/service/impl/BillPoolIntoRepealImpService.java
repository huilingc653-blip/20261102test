package com.bytter.bill.domain.billbank.pool.service.impl;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolIntoRepealImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolIntoRepeal;
import com.bytter.bill.domain.billbank.pool.service.BillPoolImpService;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.pool.service.IBillPoolIntoRepealService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * <br>
 * <b>功能：</b>票据入池撤销申请服务接口<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2021-11-29 <br>
 */
@Service("billPoolIntoRepealImpService")
@Transactional
public class BillPoolIntoRepealImpService implements IBillPoolIntoRepealService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	@Autowired
	private BillPoolImpService billPoolImpService;
	@Autowired
	private BillPoolIntoRepealImpDao billPoolIntoRepealImpDao;
	/**
	 * <b>功能：对获取的可入池撤消数据进行入库（先根据票据号、交易日期判断是否存在数据再新增）</b><br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 数据的信息
	 */
	@Override
	public synchronized void savePoolIntoRepealInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String, PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		List<AbPoolIntoRepeal> addList=new ArrayList<AbPoolIntoRepeal>();
		for(PoolBillVoucherVO info:paramsVo){
			System.out.println("入池撤销数据：PoolNo="+info.getPoolNo()+" BifCode:"+info.getBifCode());
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
			if(poolInfo==null){
				continue;
			}
			//System.out.println("入池撤销数据：poolInfo="+poolInfo);
			Map<String,Object> params = new HashMap<String, Object>();
			//根据票据号、交易日期判断是否存在数据
			//System.out.println("入池撤销数据：origNo="+info.getOrigNo());
			if(StringUtils.isNotEmpty(info.getOrigNo())){
				params.put("origNo",info.getOrigNo());
			}
			//System.out.println("入池撤销数据：TradeDate="+info.getTradeDate());
			if(info.getTradeDate()!=null){
				params.put("transDate", DateUtils.getDateYMD(info.getTradeDate()));
			}
			params.put("validSign",BillPoolConstant.VALID_SIGN_1);
			if(billPoolIntoRepealImpDao.isAbPoolIntoRepeal(params)){
				continue;
			}
			AbPoolIntoRepeal repeal=new AbPoolIntoRepeal();
			BeanUtils.copyNotNullProperties(info, repeal);
			repeal.setBpId(poolInfo.getBusId());
			repeal.setOrigNo(info.getOrigNo());
			repeal.setBillIdCode(info.getBillIdCode());
			repeal.setValidSign(BillPoolConstant.VALID_SIGN_1);
			repeal.setTransDate(DateUtils.getDateYMD(info.getTradeDate()));
			repeal.setCreateTime(new Date());
			repeal.setBriefId(BillPoolConstant.BRIEF_TYPE_IN_REPEAL);
			if(info.getBillType()!=null){
				repeal.setBillType(String.valueOf(info.getBillType()));
			}
			System.out.println("保存入池撤销数据：repeal="+repeal);
			billPoolIntoRepealImpDao.saveOrUpdate(repeal);
			//addList.add(repeal);
		}
		if(addList.size()>0){
			//billPoolIntoRepealImpDao.batchJdbcSave(addList);
		}
	}
	/**
	 * <b>功能：</b>获取待发送入池撤销申请数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 获取待发送入池撤销申请数据
	 */
	@Override
	public List<PoolBillVoucherVO> queryIntoRepealByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryPoolBillVoucherInfo(BillPoolConstant.BRIEF_TYPE_IN_REPEAL, BillPoolConstant.optStatus_16, bifCode, reCode, null);
	}
	/**
	 * <b>功能：</b>获取等待结果票据入池撤销数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 等待结果票据入池撤销数据
	 */
	@Override
	public List<PoolBillVoucherVO> queryIntoRepealResultByBifCode(String bifCode, String reCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+" ,reCode="+reCode);
		}
		return billPoolManageImpService.queryPoolBillVoucherInfo(BillPoolConstant.BRIEF_TYPE_IN_REPEAL, BillPoolConstant.optStatus_18, bifCode, reCode, null);
	}
	/**
	 * <b>功能：修改票据入池撤销的指令状态</b><br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@Override
	public void updatePoolIntoRepeal(PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo);
		}
		billPoolManageImpService.updatePoolApplyRepeal(BillPoolConstant.BRIEF_TYPE_IN_REPEAL, paramsVo);
	}
	/**
	 * 根据撤销id修改入池撤销是否有效
	 * @param rid 撤销id
	 * @param validSign 是否有效
	 * @return 结果数量
	 */
	public int updatePoolIntoRepealByRid(String rid,String validSign){
		if(StrUtil.isEmpty(rid) || StrUtil.isEmpty(validSign)) {
			return 0;
		}
		return billPoolIntoRepealImpDao.updatePoolIntoRepealByRid(rid,validSign);
	}
}
