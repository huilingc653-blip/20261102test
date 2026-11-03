package com.bytter.biz.fundwarn.service;

import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.spi.service.IFundWarnService;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 预警事中技术处理操作服务类
 * @author zhengg
 *
 */
@DubboService
@Service("fundWarnTimelyService")
//@Transactional
public class FundWarnTimelyService extends AbstractService implements IFundWarnService {
	@Autowired
	private FundWarnStrategyAnalysisService fundWarnStrategyAnalysisService;

	/**
	 * 事中预警异步线程池的具名线程工厂，线程名可识别，便于排查与优雅关闭。
	 */
	private static final AtomicInteger FUNDWARN_THREAD_SEQ = new AtomicInteger(1);
	private static final ThreadFactory FUNDWARN_THREAD_FACTORY = r -> {
		Thread t = new Thread(r, "fundwarn-timely-" + FUNDWARN_THREAD_SEQ.getAndIncrement());
		t.setDaemon(false);
		return t;
	};

	/**
	 * 事中预警异步线程池（TTL 包装，上下文感知）。
	 */
	private static final ExecutorService FUNDWARN_EXECUTOR = SmartThreadPoolManager.wrap(
			Executors.newFixedThreadPool(8, FUNDWARN_THREAD_FACTORY));

	/**
	 * 应用关闭时优雅关闭异步线程池，避免非守护线程泄漏导致 JVM 无法退出。
	 */
	@PreDestroy
	public void shutdown() {
		if (FUNDWARN_EXECUTOR == null || FUNDWARN_EXECUTOR.isShutdown()) {
			return;
		}
		FUNDWARN_EXECUTOR.shutdown();
		try {
			if (!FUNDWARN_EXECUTOR.awaitTermination(30, TimeUnit.SECONDS)) {
				FUNDWARN_EXECUTOR.shutdownNow();
			}
		} catch (InterruptedException e) {
			FUNDWARN_EXECUTOR.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public void warnExecuteByEqCorpPay(Object info,ICurrentUser user) {
		warnExecute(info, FundWarnConstant.TYPECODE_EQCORPPAY, user);
	}
	@Override
	public void warnExecuteBankAccPay(Object info,ICurrentUser user) {
		warnExecute(info, FundWarnConstant.TYPECODE_BANKACCPAY, user);
	}
	@Override
	public void warnExecuteCashPay(Object info,ICurrentUser user) {
		warnExecute(info, FundWarnConstant.TYPECODE_CASHPAY, user);
	}
	@Override
	public void warnExecuteBudgetProgress(Object info, ICurrentUser user) {
		warnExecute(info, FundWarnConstant.TYPECODE_BUDGETPROGRESS, user);
	}
	@Override
	public void warnExecuteDaySumPay(Object info, ICurrentUser user) {
		warnExecute(info, FundWarnConstant.TYPECODE_DAYSUMPAY, user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void warnExecute(Object info, String typeCode,ICurrentUser user) {
		final Map<String,Object> threadInfo=BeanUtils.beanToMap(info);
		final String threadTypeCode=typeCode;
		final ICurrentUser currentUser=user;
		//为不影响原业务性能做成多线程处理
		FUNDWARN_EXECUTOR.execute(() -> {
			FundWarnTimelyService service=(FundWarnTimelyService)ApplicationContextUtil.getContext().getBean("fundWarnTimelyService");
			service.executeTimelyStrategy(threadInfo, threadTypeCode ,currentUser);
		});
	}
	/**
	 * 事中 业务调用预警功能处理
	 * @param dataInfo 业务数据信息
	 * @param typeCode 预警类型
	 */
	public void executeTimelyStrategy(Map<String, Object> dataInfo, String typeCode,ICurrentUser user){
		Map<String, Object> params=new HashMap<String, Object>();
		setTimelyStrategyParamsValue(dataInfo, typeCode, params);
		List<BtWarnStrategyVO> strategyVOList=fundWarnStrategyAnalysisService.getTimelyWarnStrategyByParams(params);
		if(!ListUtils.isNullOrEmpty(strategyVOList)){
			for(BtWarnStrategyVO entity:strategyVOList){
				entity.setCurrentUser(user);
				fundWarnStrategyAnalysisService.executeStrategy(entity,dataInfo);
			}
		}
		
	}
	
	
	/**
	 * 设置查询匹配策略 查询条件
	 * @param dataInfo
	 * @param typeCode
	 * @param params
	 */
	public void setTimelyStrategyParamsValue(Map<String, Object> dataInfo, String typeCode ,Map<String, Object> params){
		params.put("status", FundWarnConstant.STATUS_START);
		params.put("typeCode", typeCode);
//		dimensionDataType预警维度类型,dimensionDataTypeValue预警维度类型值
		if(FundWarnConstant.TYPECODE_EQCORPPAY.equals(typeCode)){
			params.put("corpId", dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CORPID));
			params.put("curId", dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CURID));
			params.put("bankAcc", dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_BANKACC));
			params.put("conditionMoney", dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYMONEY));
			params.put("dimensionDataType", FundWarnConstant.CONDITION_TYPE_CORP);
			params.put("dimensionDataTypeValue", dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CORPID));
		}else if(FundWarnConstant.TYPECODE_DAYSUMPAY.equals(typeCode)){
			params.put("corpId", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID));
			params.put("curId", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CURID));
			params.put("bankAcc", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_BANKACC));
			params.put("conditionMoney", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_PAYMONEY));
			params.put("dimensionDataType", FundWarnConstant.CONDITION_TYPE_CORP);
			params.put("dimensionDataTypeValue", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID));
		}else if(FundWarnConstant.TYPECODE_BUDGETPROGRESS.equals(typeCode)){
			params.put("corpId", dataInfo.get(FundWarnConstant.BUDGETPROGRESS.FIELD_CORPID));
			params.put("curId", dataInfo.get(FundWarnConstant.BUDGETPROGRESS.FIELD_CURID));
			params.put("dimensionDataType", FundWarnConstant.CONDITION_TYPE_CORP);
			params.put("dimensionDataTypeValue", dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID));
		}else if(FundWarnConstant.TYPECODE_BANKACCPAY.equals(typeCode)){
			params.put("curId", dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_CURID));
			params.put("bankAcc", dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_BANKACC));
			params.put("conditionMoney", dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_PAYMONEY));
			params.put("dimensionDataType", FundWarnConstant.CONDITION_TYPE_BANKACC);
			params.put("dimensionDataTypeValue", dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_BANKACC));
		}else if(FundWarnConstant.TYPECODE_CASHPAY.equals(typeCode)){
			params.put("fcAcc", dataInfo.get(FundWarnConstant.CASHPAY.FIELD_FCACC));
			params.put("curId", dataInfo.get(FundWarnConstant.CASHPAY.FIELD_CURID));
			params.put("conditionMoney", dataInfo.get(FundWarnConstant.CASHPAY.FIELD_PAYMONEY));
			params.put("dimensionDataType", FundWarnConstant.CONDITION_TYPE_FCACC);
			params.put("dimensionDataTypeValue", dataInfo.get(FundWarnConstant.CASHPAY.FIELD_FCACC));
		}
	}
}
