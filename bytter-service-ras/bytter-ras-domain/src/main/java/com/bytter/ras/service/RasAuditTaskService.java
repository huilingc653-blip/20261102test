package com.bytter.ras.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasAuditInterDao;
import com.bytter.ras.dao.RasAuditTaskDao;
import com.bytter.ras.entity.RasAuditInfo;
import com.bytter.ras.entity.RasAuditInter;
import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.entity.RasAuditTask;
import com.bytter.ras.entity.RasReceivedDtl;
import com.bytter.ras.entity.RasRevenueDtl;
import com.bytter.ras.entity.RasShop;
import com.bytter.ras.utils.RasThreadUtil;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.RasAuditInfoVO;
import com.bytter.ras.vo.RasAuditTaskVO;
import com.bytter.ras.vo.RasPayTypeVO;
import com.bytter.ras.vo.RasReceivedDtlVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.IUserService;

/**
 * 
* @Title:  营收稽核任务维护业务操作类
* @author: zhengg
* @date:   2019-5-15 下午5:55:04   
 */
@Service("rasAuditTaskService")
@Transactional
public class RasAuditTaskService extends BaseService<RasAuditTask, String> {
	
	@Autowired
	private RasAuditTaskDao rasAuditTaskDao;

	@Autowired
	private RasAuditInterDao rasAuditInterDao;
	
	@Autowired
	private RasTaskService rasTaskService;

	@Autowired
	private RasConfigService rasConfigService;
	
	@Autowired(required=false)
	private IUserService userService;
	
	@Autowired
	private RasLogService rasLogService;
	@Autowired
	private RasShopService rasShopService;
	@Autowired
	private RasAuditStrategyService rasAuditStrategyService;
	@Autowired
	private RasAuditService rasAuditService;
	@Autowired
	private RasRevenueService rasRevenueService;
	@Autowired
	private RasReceivedService rasReceivedService;
	
	@Override
	public BaseDao<RasAuditTask, String> getEntityDao() {
		return rasAuditTaskDao;
	}
	
	/**
	 * 根据ID获取任务VO信息
	 * @param id
	 * @return
	 */
	public RasAuditTaskVO getRasAuditTaskVO(String id){
		RasAuditTask info=this.get(id, true);
		RasAuditTaskVO infoVo=new RasAuditTaskVO();
		BeanUtils.copyProperties(info, infoVo);
		return infoVo;
	}
	
	/**
	 * 
	* @Title: 查询营收稽核策略数据
	* @param input
	* @return 列表数据  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		DataTablesOutput data=rasAuditTaskDao.getDataTablesUnion(input);
		@SuppressWarnings("unchecked")
		List<RasAuditTaskVO> infoList=data.getData();
		for(RasAuditTaskVO info:infoList){
			RasUtils.setPayTypeList(info.getPayType(), info.getRasPayTypeVO());
			//this.inject(info.getRasPayTypeVO());
		}
		return data;
	}

	/**
	 * 
	* @Title: 营收稽核策略数据新增或者修改操作
	* @param info 核策略数据信息
	* @param user 用户
	* @return 处理结果
	* @throws
	 */
	public AjaxJson saveOrUpdate(RasAuditTaskVO infoVO,ICurrentUser user){
		if(infoVO!=null){
			if(infoVO.getPayTypeArray()!=null&&infoVO.getPayTypeArray().length>0){
				infoVO.setPayType(StringUtils.join(infoVO.getPayTypeArray(),RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS));
			}
		}
		AjaxJson reJson = new AjaxJson();
		if(infoVO==null||StringUtils.isEmpty(infoVO.getPayType())||StringUtils.isEmpty(infoVO.getPayType())){
			throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITTASK_NULL);
		}else{
			RasAuditTask info=new RasAuditTask();
			BeanUtils.copyNotNullProperties(infoVO, info);
			if(!checkPayType(info.getPayType(), info.getId())){
				throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITTASK_CHECK);
			}
			if(!codeCheck(info.getCode(), info.getId())){
				throw new BusinessException(RasMessageEnum.RAS_TASK_CODECHECK_UNIQ);
			}
			if(StringUtils.isEmpty(info.getId())){
				info.setValidSign(RasConstant.RAS_VALID_SIGN_0);
				info.setCreateBy(user.getLoginName());
				info.setCreateName(user.getUsername());
				info.setUpdateBy(user.getLoginName());
				info.setUpdateName(user.getUsername());
				info.setCreateDate(new Date());
				//对复制的功能把任务处理策略信息给予赋值
				if(RasConstant.RAS_TASK_OPTYPE_CLONEOP.equals(infoVO.getOpType())&&StringUtils.isNotEmpty(infoVO.getLastId())){
					RasAuditTask lastInfo=rasAuditTaskDao.get(infoVO.getLastId(), true);
					if(lastInfo!=null&&StringUtils.isNotEmpty(lastInfo.getTaskId())){
						info.setStartDate(lastInfo.getStartDate());
						info.setTaskCron(lastInfo.getTaskCron());
						info.setValidSign(lastInfo.getValidSign());
						rasTaskService.saveOrUpdateWarnTaskInfo(info, user);
						if(RasConstant.RAS_VALID_SIGN_1.equals(info.getValidSign())){
							rasTaskService.opWarnTaskInfo(info.getId(),info.getValidSign());
						}
					}
				}
				rasLogService.saveRasLog(info.getId(), RasConstant.RAS_LOGTYPE_ADD, "进行新增操作！", user);
				rasAuditTaskDao.save(info);
			}else{
				RasAuditTask oldInfo=rasAuditTaskDao.get(info.getId(), true);
				BeanUtils.copyNotNullProperties(info, oldInfo);
				oldInfo.setUpdateBy(user.getLoginName());
				oldInfo.setUpdateName(user.getUsername());
				oldInfo.setUpdateDate(new Date());

				String msg="进行修改操作！";
				if(RasConstant.RAS_TASK_OPTYPE_UPDATEOP.equals(infoVO.getOpType())
						&&StringUtils.isNotEmpty(oldInfo.getTaskId())){
					rasTaskService.saveOrUpdateWarnTaskInfo(oldInfo, user);
				}
				if(RasConstant.RAS_TASK_OPTYPE_TASKOP.equals(infoVO.getOpType())){
					rasTaskService.saveOrUpdateWarnTaskInfo(oldInfo, user);
					rasTaskService.opWarnTaskInfo(oldInfo.getTaskId(),oldInfo.getValidSign());
					msg="进行扫描参数维护!";
				}
				rasLogService.saveRasLog(info.getId(), RasConstant.RAS_LOGTYPE_UPDATE, msg, user);
				rasAuditTaskDao.update(oldInfo);
			}
		}
		reJson.setSuccess(true);
		return reJson;
	}
	
	/**
	 * 检查是否有重复的缴款方式和策略 
	 * @param payType 缴款方式
	 * @param id 需要排除的ID（判断修改的时候需要排除自身）
	 * @return true不存在重复的信息 false存在重复的信息
	 */
	@Transactional(readOnly = true)
	public boolean checkPayType(String payType,String id){
		List<RasAuditTask> list=rasAuditTaskDao.getRasAuditTaskListByPayType(payType, id);
		if(list==null||list.size()<1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 检查是否有重复的code
	 * @param code 任务code
	 * @param id 需要排除的ID（判断修改的时候需要排除自身）
	 * @return true不存在重复的信息 false存在重复的信息
	 */
	@Transactional(readOnly = true)
	public boolean codeCheck(String code,String id){
		List<RasAuditTask> list=rasAuditTaskDao.codeCheck(code, id);
		if(list==null||list.size()<1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 触发定时任务执行 
	 * @param info 策略信息
	 */
	public void executeStrategy(RasAuditTaskVO info){
//		executeStrategy(info, null);
		executeStrategyDataCache(info, null);
	}
	
	/**
	 * 
	* @Title: 稽核任务手工执行  
	* @param info 核策略数据信息
	* @param user 用户
	* @return 处理结果
	* @throws
	 */
	public boolean executeScan(RasAuditTaskVO infoVO,ICurrentUser user){
		boolean reBoo=false;
		if(infoVO!=null
				&&StringUtils.isNotEmpty(infoVO.getId())){
			RasAuditTaskVO runVO=this.getRasAuditTaskVO(infoVO.getId());
			runVO.setStartDate(infoVO.getStartDate());
			Date todayDay=DateUtils.getDateYMD();
			if(todayDay.getTime()>=runVO.getStartDate().getTime()){
//				executeStrategy(runVO,user);
				executeStrategyDataCache(runVO,user);
			}else{
				throw new BusinessException(RasMessageEnum.RAS_TASK_CODECHECK_RUN_STATUSDATE);
			}
			reBoo=true;
		}
		return reBoo;
	}

	/**
	 * 修改任务执行状态
	 * @return
	 */
	public boolean setTaskRunSatuas(String id,String runStatus,String runMsg,ICurrentUser user){
		Map<String,Object> params=new HashMap<>();
		params.put("wId", id);
		params.put("runStatus", runStatus);
		params.put("runMsg", runMsg);
		Date todayDay=new Date();
		params.put("lastDate", todayDay);
		if(user!=null){
			params.put("updateBy", user.getLoginName());
			params.put("updateName", user.getUsername());
			params.put("updateDate", todayDay);
		}
		int num=((RasAuditTaskService)this.getBean(this.getClass())).updateRasAuditTaskByParams(params);
		if(num<1){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据票据修改定时任务信息
	 * @param params
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public int updateRasAuditTaskByParams(Map<String,Object> params){
		return rasAuditTaskDao.updateRasAuditTaskByParams(params);
	}
	
	/**
	 * 修改营收实收数据进行释放锁定状态
	 * @return
	 */
	public boolean setRevenueReceivedDataUnLock(RasAuditTaskVO info){
		this.logger.info("====解锁数据，稽核中数据变成待稽核===============================："+info.toString());
		return setRevenueReceivedDataLock(info, false);
	}
	

	/**
	 * 修改营收实收数据进行锁定状态
	 * @return
	 */
	public boolean setRevenueReceivedDataLock(RasAuditTaskVO info){
		this.logger.info("====锁定数据，待稽核数据变成稽核中===============================："+info.toString());
		return setRevenueReceivedDataLock(info, true);
	}
	
	/**
	 * 修改任务执行状态
	 * @return
	 */
	public boolean setRevenueReceivedDataLock(RasAuditTaskVO info,boolean lockType){
		Map<String,Object> params=new HashMap<>();
		if(lockType){
			params.put("unId", info.getUnId());
			params.put("startRevenueDate", info.getStartRevenueDate());
			params.put("startReceivedDate", info.getStartReceivedDate());
			params.put("payTypeArray", info.getPayTypeArray());
		}else{
			params.put("wUnid", info.getUnId());
		}
		Date todayDay=new Date();
		if(info.getUser()!=null){
			params.put("updateBy", info.getUser().getLoginName());
			params.put("updateName", info.getUser().getUsername());
			params.put("updateDate", todayDay);
		}
		return ((RasAuditTaskService)this.getBean(this.getClass())).setRevenueReceivedDataLock(params, lockType);
	}

	/**
	 * 修改任务执行状态
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public boolean setRevenueReceivedDataLock(Map<String,Object> params,boolean lockType){
		if(lockType){
			params.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_2);
			params.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_1);
		}else{
			params.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_1);
			params.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		}
		rasRevenueService.autoOpAuditStatusControl(params);
		rasReceivedService.autoOpAuditStatusControl(params);
		return true;
	}
	
	/**
	 * 任务执行初始化数据
	 * @param info 执行VO对象
	 * @param user 操作用户
	 */
	public void initRasAuditTaskVO(RasAuditTaskVO info,ICurrentUser user){
		//设置稽核用户
		if(info.getUser()==null){
			info.setUser(user);
			//用户为空时设置系统参数配置默认用户
			if(info.getUser()==null){
				String userCode=rasConfigService.getDefaultAuditUser();
				if(StringUtils.isNotEmpty(userCode)){
					UserDTO userDTO=userService.getByLoginName(userCode);
					if(userDTO==null){
						userDTO=new UserDTO();
						userDTO.setLoginName(userCode);
						userDTO.setUsername(userCode);
					}else{
						info.setUser(userDTO);
					}
				}
			}
		}
		Date todayDay=DateUtils.getDateYMD();
		if(info.getLastDate()==null){
			info.setLastDate(todayDay);
		}else{
			info.setLastDate(DateUtils.getDateYMD(info.getLastDate()));
		}
		//设置营收数据起始日期
		if(info.getStartRevenueDate()==null){
			int revenueRollDay=rasConfigService.getRevenueRollDay();
			if(revenueRollDay>0){
				info.setStartRevenueDate(DateUtils.addDay(info.getLastDate(), revenueRollDay*-1));
			}
		}
		//设置实收数据起始日期
		if(info.getStartReceivedDate()==null){
			int receivedRollDay=rasConfigService.getReceivedRollDay();
			if(receivedRollDay>0){
				info.setStartReceivedDate(DateUtils.addDay(info.getLastDate(), receivedRollDay*-1));
			}
		}
		if(info.getPayTypeArray()==null&&info.getPayType()!=null){
			info.setPayTypeArray(info.getPayType().split(RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS));
		}
		if(StringUtils.isEmpty(info.getUnId())){
			info.setUnId(UUID.randomUUID().toString().replace("-", ""));
		}
		
//
		Map<String,Object> params=new HashMap<>();
		//初始化门店信息
		List<RasShop> shopList=rasShopService.getRasShopValidByParams(params);
		if(shopList!=null){
			for(RasShop shopInfo:shopList){
				info.getShopMap().put(shopInfo.getId(), shopInfo);
			}
		}

		//初始化缴款方式策略信息
		if(StringUtils.isNotEmpty(info.getPayType())){
			List<String> payTypeList=StringUtils.splitToList(",", info.getPayType());
			params.put("wPayTypes", payTypeList);
		}
		List<RasAuditStrategy> auditStrategyList=rasAuditStrategyService.getRasAuditStrategyValidList(params);
		List<RasAuditStrategy> strategyList=null;
		for(RasAuditStrategy strategyInfo:auditStrategyList){
			if(info.getPayTypeMap().containsKey(strategyInfo.getPayType())){
				strategyList=info.getPayTypeMap().get(strategyInfo.getPayType());
			}else{
				strategyList=new ArrayList<>();
				info.getPayTypeMap().put(strategyInfo.getPayType(), strategyList);
			}
			strategyList.add(strategyInfo);
		}
	}
	
	/**
	 * 触发定时任务执行 
	 * @param info 策略信息
	 */
	public void executeStrategyDataCache(RasAuditTaskVO info,ICurrentUser user){
		initRasAuditTaskVO(info,user);
		this.logger.info("begin 定时任务执行开始 ============================"+info.getUnId());
		String logType=RasConstant.RAS_LOGTYPE_TASK_AUTO;
		String msg="任务自动执行操作！";
		rasLogService.saveRasLog(info.getId(), logType, msg, user);
		if(user!=null){
			logType=RasConstant.RAS_LOGTYPE_TASK_HANDWORK;
			msg="任务手工执行操作！";
		}
		setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_R, null, user);
		try{
			setRevenueReceivedDataLock(info);
			
			Map<String, Object> params = new HashMap<>();
			params.put("wUnid", info.getUnId());
			
//			List<RasRevenueDtlVO> revenueList=rasRevenueService.getAllRasRevenueDtlVOInfo(params);
//			this.logger.info("营收数据量============================"+revenueList.size());
//			List<RasReceivedDtlVO> receivedList=rasReceivedService.getAllRasReceivedDtlVOInfo(params);
//			this.logger.info("实收数据量============================"+receivedList.size());
//			auditDataCacheOp(revenueList, receivedList, info);
			
			//分页抓取数据处理
			List<RasRevenueDtlVO> revenueZList=new ArrayList<>();
			List<RasReceivedDtlVO> receivedZList=new ArrayList<>();
			List<RasRevenueDtlVO> revenueList=null;
			int maxResult=RasConstant.RAS_AUTOT_MAXCACHE_RESULT;
			int first=0;
			params.put("maxResult", maxResult);
			do{
				if(revenueList!=null){
					first++;
				}
				params.put("first", first * maxResult);
				revenueList=rasRevenueService.getAllRasRevenueDtlVOInfo(params);
				this.logger.debug("营收数据量第【"+(first+1)+"】次============================"+revenueList.size());
				revenueZList.addAll(revenueList);
			}while(revenueList!=null&&revenueList.size()==maxResult);
			revenueList=null;
			
            first=0;
            List<RasReceivedDtlVO> receivedList=null;
            do{
            	if(receivedList!=null){
            		first++;
            	}
				params.put("first", first * maxResult);
            	receivedList=rasReceivedService.getAllRasReceivedDtlVOInfo(params);
                this.logger.debug("实收数据量第【"+(first+1)+"】次============================"+receivedList.size());
                receivedZList.addAll(receivedList);
            }while(receivedList!=null&&receivedList.size()==maxResult);
            receivedList=null;
			
			this.logger.info("营收数据量============================"+revenueZList.size());
			this.logger.info("实收数据量============================"+receivedZList.size());

			auditDataCacheOp(revenueZList, receivedZList, info);
			revenueZList=null;
	        receivedZList=null;
			
			setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_S, null, user);
		}catch(Exception e){
			this.logger.error("触发定时任务执行报错", e);
			setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_F, "执行报错:"+e.getMessage(), user);
			msg+=",出现错误：错误信息"+e.getMessage();
			rasLogService.saveRasLog(info.getId(), logType, msg, user);
		}finally {
			setRevenueReceivedDataUnLock(info);
            rasAuditInterDao.deleteByUnid(info.getUnId());
			this.logger.info("end 定时任务执行结束==============================="+info.getUnId());
		}
	}


    /**
     * 对稽核查询条件进行初始化
     * @param params
     * @param info
     * @param shop
     */
    public void rasAuditInfoVOParamsInit(Map<String,Object> params, RasAuditTaskVO info, RasShop shop){
    	params.put("startRevenueDate", info.getStartRevenueDate());
		params.put("startReceivedDate", info.getStartReceivedDate());
		params.put("payTypeName", info.getPayTypeName());
		params.put("wValidSign", RasConstant.RAS_VALID_SIGN_1);
        params.put("wUnid", info.getUnId());
		params.put("wShopId", shop.getId());
		params.put("shopCode", shop.getCode());
		params.put("shopName", shop.getName());
		params.put("shopAsDate", shop.getAsDate());
		if(shop.getAsDate().getTime()>info.getStartRevenueDate().getTime()){
			params.put("startRevenueDate", shop.getAsDate());
		}
		if(shop.getAsDate().getTime()>info.getStartReceivedDate().getTime()){
			params.put("startReceivedDate", shop.getAsDate());
		}
    }
    
    /**
     * 对同一门店和缴款方式的数据进行稽核操作
     * @param shopId 门店Id
     * @param payType 缴款方式
     * @param revenueShopPayList 相同门店和缴款方式的营收数据
     * @param receivedShopPayList 相同门店和缴款方式的实收数据
     * @param auditInfoVO 初始的操作参数
     */
    public void auditShopPayDate(String shopId,String payType,Map<String,RasRevenueDtlVO> revenueShopPayList,Map<String,RasReceivedDtlVO> receivedShopPayList, RasAuditTaskVO info){
    	info.setPayType(payType);
    	RasShop shop=info.getShopMap().get(shopId);
    	List<RasAuditStrategy> auditStrategy=info.getPayTypeMap().get(payType);
    	if(shop!=null&&auditStrategy!=null&&auditStrategy.size()>0){
    		Map<String,Object> params=new HashMap<>();
    		rasAuditInfoVOParamsInit(params, info, shop);
            this.logger.info("begin 对门店("+shop.getCode()+")"+shop.getName()+",缴款方式：(" + info.getPayType() + ") 处理开始============================线程ID：  "+Thread.currentThread().getId());
            for(RasAuditStrategy strategy:auditStrategy){
    			params.put("strategyName", strategy.getName());
    			params.put("rules", strategy.getRules());
    	        params.put("wPayType", strategy.getPayType());
    	        ((RasAuditTaskService)this.getBean(this.getClass())).executeStrategyDtlOp(params, revenueShopPayList, receivedShopPayList, info.getUser());
            }
            this.logger.info("end 对门店("+shop.getCode()+")"+shop.getName()+",缴款方式：(" + info.getPayType() + ") 处理结束============================线程ID：  "+Thread.currentThread().getId());
    	}
    }
	
    /**
     * 对获取到的营收实收数据进行操作
     * @param revenueList 营收数据
     * @param receivedList 实收数据
     * @param auditInfoVO 操作参数信息
     */
    public void auditDataCacheOp(List<RasRevenueDtlVO> revenueList,List<RasReceivedDtlVO> receivedList,RasAuditTaskVO auditInfoVO){
    	Map<String,Map<String,RasRevenueDtlVO>> revenueMap=new HashMap<>();
		Map<String,Map<String,RasReceivedDtlVO>> receivedMap=new HashMap<>();
		rasAuditService.auditDataCacheInit(revenueList, receivedList, revenueMap, receivedMap);
		//对相同的门店和缴款方式进行稽核匹配处理
		String shopId=null;
		String payType=null;
		String key=null;
		String[] keys=null;
		Map<String,RasRevenueDtlVO> revenueShopPayList=null;
		Map<String,RasReceivedDtlVO> receivedShopPayList=null;
		for (Entry<String, Map<String,RasRevenueDtlVO>> entry : revenueMap.entrySet()) {
			key=entry.getKey();
			revenueShopPayList=entry.getValue();
			receivedShopPayList=receivedMap.get(key);
			if(revenueShopPayList!=null&&revenueShopPayList.size()>0
					&&receivedShopPayList!=null&&receivedShopPayList.size()>0){
				keys=key.split(RasConstant.RAS_MAP_KEYSEPARATORCHARS);
				shopId=keys[0];
				payType=keys[1];
				if(!RasConstant.RAS_PAYTYPE_001.equals(payType)
						&&!RasConstant.RAS_PAYTYPE_006.equals(payType)){
					auditShopPayDate(shopId, payType, revenueShopPayList, receivedShopPayList, auditInfoVO);
				}
			}
		}
		
		//每个门店针对其他和现金的缴款方式进行单独处理
		for (Entry<String, RasShop> rasShop : auditInfoVO.getShopMap().entrySet()) {
			RasShop shop=rasShop.getValue();
    		Map<String,Object> params=new HashMap<>();
    		rasAuditInfoVOParamsInit(params, auditInfoVO, shop);
			for (Entry<String, List<RasAuditStrategy>> paytype : auditInfoVO.getPayTypeMap().entrySet()) {
				List<RasAuditStrategy> auditStrategyList=paytype.getValue();
				for(RasAuditStrategy strategy:auditStrategyList){
					if(RasConstant.RAS_PAYTYPE_001.equals(strategy.getPayType())
							||RasConstant.RAS_PAYTYPE_006.equals(strategy.getPayType())){
		    			params.put("strategyName", strategy.getName());
		    			params.put("rules", strategy.getRules());
		    	        params.put("wPayType", strategy.getPayType());
						key=shop.getId()+RasConstant.RAS_MAP_KEYSEPARATORCHARS+strategy.getPayType();
						revenueShopPayList=revenueMap.get(key);
						receivedShopPayList=receivedMap.get(key);
						if(revenueShopPayList==null){
							revenueShopPayList=new LinkedHashMap<>();
						}
						if(receivedShopPayList==null){
							receivedShopPayList=new LinkedHashMap<>();
						}
						((RasAuditTaskService)this.getBean(this.getClass())).executeStrategyCountNumOp(params, revenueShopPayList, receivedShopPayList, auditInfoVO.getUser());
					}
				}
			}
		}
		
		Map<String,Object> params=new HashMap<>();
        params.put("wUnid", auditInfoVO.getUnId());
        ((RasAuditTaskService)this.getBean(this.getClass())).auditEndRasAuditInter(params, auditInfoVO.getUser());
    }
    
	
	/**
	 * 触发定时任务执行 
	 * @param info 策略信息
	 */
	public void executeStrategy(RasAuditTaskVO info,ICurrentUser user){
		initRasAuditTaskVO(info,user);
		this.logger.info("begin 定时任务执行开始 ============================"+info.getUnId());
		String logType=RasConstant.RAS_LOGTYPE_TASK_AUTO;
		String msg="任务自动执行操作！";
		rasLogService.saveRasLog(info.getId(), logType, msg, user);
		if(user!=null){
			logType=RasConstant.RAS_LOGTYPE_TASK_HANDWORK;
			msg="任务手工执行操作！";
		}
		setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_R, null, user);
		try{
			setRevenueReceivedDataLock(info);
			RasUtils.setPayTypeList(info.getPayType(), info.getRasPayTypeVO());
			//this.inject(info.getRasPayTypeVO());
			for(RasPayTypeVO payType:info.getRasPayTypeVO()){
				info.setPayType(payType.getPayType());
				info.setPayTypeName(payType.getPayTypeName());
				executeStrategyByPayType(info);
			}
			
			setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_S, null, user);
		}catch(Exception e){
			this.logger.error("触发定时任务执行报错", e);
			setTaskRunSatuas(info.getId(), RasConstant.RAS_RUN_STATUS_F, "执行报错:"+e.getMessage(), user);
			msg+=",出现错误：错误信息"+e.getMessage();
			rasLogService.saveRasLog(info.getId(), logType, msg, user);
		}finally {
			setRevenueReceivedDataUnLock(info);
			this.logger.info("end 定时任务执行结束==============================="+info.getUnId());
		}
	}
	
	/**
	 * 针对对应缴款方式和分门店进行稽核处理
	 * @param info 
	 */
	public void executeStrategyByPayType(RasAuditTaskVO info){
		this.logger.info("begin 对("+info.getPayType()+")"+info.getPayTypeName()+"缴款方式 进行处理============================");
		Map<String,Object> params=new HashMap<>();
		params.put("startRevenueDate", info.getStartRevenueDate());
		params.put("startReceivedDate", info.getStartReceivedDate());
		params.put("wPayType", info.getPayType());
		params.put("payTypeName", info.getPayTypeName());
		params.put("wValidSign", RasConstant.RAS_VALID_SIGN_1);
		List<RasAuditStrategy> auditStrategyList=rasAuditStrategyService.getRasAuditStrategyValidList(params);
//		List<RasAuditStrategy> auditStrategyList=info.getAuditStrategyList();
//		if(auditStrategyList==null){
//			auditStrategyList=rasAuditStrategyService.getRasAuditStrategyValidList(params);
//			info.setAuditStrategyList(auditStrategyList);
//		}
		if(auditStrategyList==null||auditStrategyList.size()<1){
			this.logger.info(" 此缴款方式无有效的策略信息！ ");
		}else{
//			List<RasShop> shopList=rasShopService.getRasShopByParams(params);
			List<RasShop> shopList=info.getShopList();
			if(shopList==null){
				shopList=rasShopService.getRasShopByParams(params);
				info.setShopList(shopList);
			}
            //进行多线程处理
            ExecutorService esThread = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(RasConstant.RAS_AUTOT_THREAD_SIZE));
			for(RasShop shop:shopList){
                params.put("wUnid", info.getUnId());
				params.put("wShopId", shop.getId());
				params.put("shopCode", shop.getCode());
				params.put("shopName", shop.getName());
				params.put("shopAsDate", shop.getAsDate());
				if(shop.getAsDate().getTime()>info.getStartRevenueDate().getTime()){
					params.put("startRevenueDate", shop.getAsDate());
				}
				if(shop.getAsDate().getTime()>info.getStartReceivedDate().getTime()){
					params.put("startReceivedDate", shop.getAsDate());
				}
//				for(RasAuditStrategy strategy:auditStrategyList){
//					params.put("strategyName", strategy.getName());
//					params.put("rules", strategy.getRules());
//					executeStrategyByPayTypeAndStrategy(params,info.getUser());
//					esThread.execute(new RasThreadUtil(((RasAuditTaskService)this.getBean(this.getClass())), params, info.getUser()));//执行
//				}
                Map<String, Object> paramsNew = new HashMap<>();
                paramsNew.putAll(params);
                esThread.execute(new RasThreadUtil(((RasAuditTaskService)this.getBean(this.getClass())), auditStrategyList, paramsNew, info.getUser()));//执行
			}
			try {
		        esThread.shutdown();
				esThread.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
	            this.logger.error("多线程同步异常报错", e);
	            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR);
			}
		}
		this.logger.info("end 对("+info.getPayType()+")"+info.getPayTypeName()+"缴款方式 处理结束============================");
	}
	
	/**
	 * 针对对应缴款方式和配置的策略进行稽核处理
	 * @param info 
	 */
	public void executeStrategyByPayTypeAndStrategy(List<RasAuditStrategy> auditStrategyList,Map<String,Object> params,ICurrentUser user){
        for (RasAuditStrategy strategy : auditStrategyList) {
            params.put("strategyName", strategy.getName());
            params.put("rules", strategy.getRules());
            executeStrategyByPayTypeAndStrategy(params, user);
        }
	}
	
	/**
	 * 针对对应缴款方式和配置的策略进行稽核处理
	 * @param info 
	 */
	public void executeStrategyByPayTypeAndStrategy(Map<String,Object> params,ICurrentUser user){
		this.logger.info("begin 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+",门店：("+params.get("shopCode")+")"+params.get("shopName")+",进行处理============================线程ID：  "+Thread.currentThread().getId());
		if(RasConstant.RAS_PAYTYPE_001.equals(params.get("wPayType"))
				||RasConstant.RAS_PAYTYPE_006.equals(params.get("wPayType"))){
			((RasAuditTaskService)this.getBean(this.getClass())).executeStrategyCountNumOp(params,user);
		}else{
			executeStrategyDtlOp(params,user);
		}
		this.logger.info("end 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+",门店：("+params.get("shopCode")+")"+params.get("shopName")+",处理结束============================线程ID：  "+Thread.currentThread().getId());
	}
	
	
	/**
	 * 根据稽核规则进行营收和实收明细自动稽核
	 * @param params
	 */
	public void executeStrategyDtlOp(Map<String,Object> params,ICurrentUser user){
		params.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		String rules=String.valueOf(params.get("rules"));
		String payType=String.valueOf(params.get("wPayType"));
		String shopId=String.valueOf(params.get("wShopId"));
		String rulesWsql=RasUtils.getRulesWSql(rules);
		if(StringUtils.isEmpty(rulesWsql)){
			return;
		}
		Map<String,Object> paramsAuditInfo=new HashMap<>();
		paramsAuditInfo.put("wUnid", params.get("wUnid"));
		paramsAuditInfo.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_3);
		paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		List<RasAuditInfo> rasAuditInfoList=null;
		do{
			rasAuditInfoList=this.rasAuditService.getRasAuditInfo(params, rulesWsql);
			//修改分页获取稽核成功的数据
			if(rasAuditInfoList!=null&&rasAuditInfoList.size()>0){
				((RasAuditTaskService)this.getBean(this.getClass())).executeStrategyAuditBatchSave(rasAuditInfoList, paramsAuditInfo, rules, payType, shopId, user);
			}
	//		Set<String> keyMap=new HashSet<>();
	//		for(RasAuditInfo info:rasAuditInfoList){
	//			if(StringUtils.isNotEmpty(info.getRevenueId())&&StringUtils.isNotEmpty(info.getReceivedId())){
	//				((RasAuditTaskService)this.getBean(this.getClass())).executeStrategyAuditSave(paramsAuditInfo, keyMap, info, rules, rulesWsql, user);
	//			}
	//		}
		}while(rasAuditInfoList!=null&&rasAuditInfoList.size()==RasConstant.RAS_AUTOT_MAXRESULT);
	}
	
	/**
	 * 对单笔进行自动稽核处理
	 * @param paramsAuditInfo
	 * @param keyMap
	 * @param info
	 * @param rules
	 * @param rulesWsql
	 * @param user
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void executeStrategyAuditSave(Map<String,Object> paramsAuditInfo,Set<String> keyMap,RasAuditInfo info,String rules,String rulesWsql,ICurrentUser user){
		String revenueKey=RasConstant.RAS_AUTOT_REVENUE_ALIAS+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS+info.getRevenueId();
		String receivedKey=RasConstant.RAS_AUTOT_RECEIVED_ALIAS+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS+info.getReceivedId();
		if(!keyMap.contains(revenueKey)&&!keyMap.contains(receivedKey)){
			info.setAuditNo(SequenceSingleton.getSequence(SequenceEnum.RAS_AUDIT_NOZD));
			info.setReceivedMoney(info.getRevenueMoney());
			info.setRevenueSize(1);
			info.setReceivedSize(1);
			info.setType(RasConstant.RAS_AUDIT_TYPE_0);
			info.setLessMoney(new BigDecimal(0));
			info.setStatus(RasConstant.RAS_AUDIT_STATUS_3);
			info.setRules(rules);
			if(user!=null){
				Date todayDay=new Date();
				info.setCreateDate(todayDay);
				info.setCreateBy(user.getLoginName());
				info.setCreateName(user.getUsername());
				paramsAuditInfo.put("updateBy", user.getLoginName());
				paramsAuditInfo.put("updateName", user.getUsername());
				paramsAuditInfo.put("updateDate", todayDay);
			}
			keyMap.add(revenueKey);
			keyMap.add(receivedKey);
			rasAuditService.saveEntity(info);
			
			paramsAuditInfo.put("auditId", info.getId());
			paramsAuditInfo.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_3);
			paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
			
			paramsAuditInfo.put("wId", info.getRevenueId());
			rasRevenueService.autoOpAuditStatusControl(paramsAuditInfo);
			
			paramsAuditInfo.put("wId", info.getReceivedId());
			rasReceivedService.autoOpAuditStatusControl(paramsAuditInfo);
		}
	}

	/**
	 * 批量对匹配上的数据进行自动稽核处理
	 * @param rasAuditInfoList
	 * @param paramsAuditInfo
	 * @param rules
	 * @param payType
	 * @param shopId
	 * @param user
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void executeStrategyAuditBatchSave(List<RasAuditInfo> rasAuditInfoList, Map<String,Object> paramsAuditInfo,String rules, String payType, String shopId, ICurrentUser user){
		Set<String> keyMap=new HashSet<>();
		List<RasAuditInfo> auditInfoNewList=new ArrayList<>();
		List<RasAuditInter> rasAuditInterList=new ArrayList<>();
		List<String> revenueIdList=new ArrayList<>();
		List<String> receivedIdList=new ArrayList<>();
		String unId=String.valueOf(paramsAuditInfo.get("wUnid"));
		Date todayDay=new Date();
		for(RasAuditInfo info:rasAuditInfoList){
			if(StringUtils.isNotEmpty(info.getRevenueId())&&StringUtils.isNotEmpty(info.getReceivedId())){
				if(StringUtils.isEmpty(info.getPayType())){
					info.setPayType(payType);
				}
				if(StringUtils.isEmpty(info.getShopId())){
					info.setShopId(shopId);
				}
				setSaveAuditInfo(auditInfoNewList, rasAuditInterList, revenueIdList, receivedIdList, keyMap, info, rules, user, todayDay, unId);
			}
		}
		if(auditInfoNewList!=null&&auditInfoNewList.size()>0){
			rasAuditService.batchSave(auditInfoNewList);
			rasAuditInterDao.batchSave(rasAuditInterList);
			if(user!=null){
				paramsAuditInfo.put("updateBy", user.getLoginName());
				paramsAuditInfo.put("updateName", user.getUsername());
				paramsAuditInfo.put("updateDate", todayDay);
			}
			//批量修改营收数据为已稽核
			paramsAuditInfo.put("isAuditIdInter", "1");
			paramsAuditInfo.put("wIdList", revenueIdList);
			rasRevenueService.autoOpAuditStatusControl(paramsAuditInfo);

			//批量修改实收数据为已稽核
			paramsAuditInfo.put("wIdList", receivedIdList);
			rasReceivedService.autoOpAuditStatusControl(paramsAuditInfo);
		}
	}
	
	/**
	 * 补齐稽核上的数据信息
	 * @param keyMap
	 * @param info
	 * @param rules
	 * @param user
	 */
	public void setSaveAuditInfo(List<RasAuditInfo> auditInfoNewList,
			List<RasAuditInter> RasAuditInterList,
			List<String> revenueIdList,
			List<String> receivedIdList,
			Set<String> keyMap,
			RasAuditInfo info,
			String rules,
			ICurrentUser user,
			Date todayDay,
			String unId){
		String revenueKey=RasConstant.RAS_AUTOT_REVENUE_ALIAS+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS+info.getRevenueId();
		String receivedKey=RasConstant.RAS_AUTOT_RECEIVED_ALIAS+RasConstant.RAS_AUDIT_RULES_SEPARATORCHARS+info.getReceivedId();
		if(!keyMap.contains(revenueKey)&&!keyMap.contains(receivedKey)){
			info.setAuditNo(SequenceSingleton.getSequence(SequenceEnum.RAS_AUDIT_NOZD));
			info.setReceivedMoney(info.getRevenueMoney());
			info.setRevenueSize(1);
			info.setReceivedSize(1);
			info.setType(RasConstant.RAS_AUDIT_TYPE_0);
			info.setLessMoney(new BigDecimal(0));
			info.setStatus(RasConstant.RAS_AUDIT_STATUS_3);
			info.setRules(rules);
			info.setCreateDate(todayDay);
			if(user!=null){
				info.setCreateBy(user.getLoginName());
				info.setCreateName(user.getUsername());
			}
			keyMap.add(revenueKey);
			keyMap.add(receivedKey);
			info.setId(UUID.randomUUID().toString().replace("-", ""));
			auditInfoNewList.add(info);
			
			RasAuditInter auditInter=new RasAuditInter();
			auditInter.setId(info.getId());
			auditInter.setRevenueId(info.getRevenueId());
			auditInter.setReceivedId(info.getReceivedId());
			auditInter.setUnId(unId);
			RasAuditInterList.add(auditInter);
			revenueIdList.add(info.getRevenueId());
			receivedIdList.add(info.getReceivedId());
		}
	}
	
	/**
	 * 对汇总模式数据进行操作
	 * @param params
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public synchronized void executeStrategyCountNumOp(Map<String,Object> params,ICurrentUser user){
		//针对数据进行按照门店+交易日期+缴款方式（现金|其他）进行汇总 且只汇总上一稽核日期后一天的数据
//		1、先获取最后一次稽核日期的数据
		Map<String,Object> paramsAuditInfo=new HashMap<>();
		paramsAuditInfo.put(RasConstant.RAS_AUTOTASK_OPKEY, "1");
		paramsAuditInfo.put("wUnid", params.get("wUnid"));
		paramsAuditInfo.put("wShopId", params.get("wShopId"));
		paramsAuditInfo.put("wPayType", params.get("wPayType"));
		Date startDate=(Date)params.get("startRevenueDate");
		Date shopAsDate=(Date)params.get("shopAsDate");
		paramsAuditInfo.put("startDate", startDate);
		paramsAuditInfo.put("statusArray", new Integer[]{RasConstant.RAS_AUDIT_STATUS_5,RasConstant.RAS_AUDIT_STATUS_4,RasConstant.RAS_AUDIT_STATUS_3});
		RasAuditInfo auditInfo = rasAuditService.getRasAuditInfoOneByParams(paramsAuditInfo);
		BigDecimal lessMoney=new BigDecimal(0);
		if(auditInfo!=null){
			startDate=DateUtils.addDay(DateUtils.getDateYMD(auditInfo.getCreateDate()), 1);
			paramsAuditInfo.put("startDate", startDate);
			lessMoney=auditInfo.getLessMoney();
		}else{
			//如何开始时间大约开始稽核日期则不做汇总
			if(startDate.getTime()>shopAsDate.getTime()){
				return;
			}
		}
		Date endDate=DateUtils.addDay(startDate, 1);
		paramsAuditInfo.put("endDate", endDate);
		paramsAuditInfo.put("wClaimType", RasConstant.RAS_CLAIM_TYPE_1);
		paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		paramsAuditInfo.put("isSum", Integer.parseInt(RasConstant.RAS_STATUS_YES));
		boolean opRevenueAudit=false;
		boolean opReceivedAudit=false;
		
		Date todayDay=new Date();
//		2、获取营收汇总数据 先判断是否已存在汇总数据 存在则对未做汇总的进行叠加金额，不存在进行汇总操作
		//根据条件查看是否有汇总数据 
		List<RasRevenueDtl> sumRevenueList=rasRevenueService.getRasRevenueDtlByParams(paramsAuditInfo);
		RasRevenueDtl revenueSumOld=null;
		if(sumRevenueList!=null && sumRevenueList.size()>0){
			revenueSumOld=sumRevenueList.get(0);
		}
		//获取最新的待汇总信息进行汇总操作
		paramsAuditInfo.put("notIsSum", Integer.parseInt(RasConstant.RAS_STATUS_YES));
		sumRevenueList=rasRevenueService.getRasRevenueDtlSumByParams(paramsAuditInfo);
		RasRevenueDtl revenueSum=null;
		if(sumRevenueList!=null&&sumRevenueList.size()==1){
			revenueSum=sumRevenueList.get(0);
			opRevenueAudit=true;
		}else if(revenueSumOld==null){
			revenueSum=new RasRevenueDtl();
			revenueSum.setPayType(String.valueOf(params.get("wPayType")));
			revenueSum.setShopId(String.valueOf(params.get("wShopId")));
			revenueSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			revenueSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
			revenueSum.setTransDate(startDate);
			revenueSum.setMoney(new BigDecimal(0));
		}
		
		if(revenueSum!=null){
			if(revenueSumOld==null){
				revenueSum.setTransDate(DateUtils.getDateYMD(revenueSum.getTransDate()));
				if(user!=null){
					revenueSum.setCreateDate(todayDay);
					revenueSum.setCreateBy(user.getLoginName());
					revenueSum.setCreateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				if(RasConstant.RAS_PAYTYPE_001.equals(params.get("wPayType"))){
					revenueSum.setMoney(revenueSum.getMoney().add(lessMoney));
				}
				//对汇总数据进行保存
				rasRevenueService.saveEntity(revenueSum);
			}else{
				revenueSumOld.setMoney(revenueSumOld.getMoney().add(revenueSum.getMoney()));
				if(user!=null){
					revenueSumOld.setUpdateDate(todayDay);
					revenueSumOld.setUpdateBy(user.getLoginName());
					revenueSumOld.setUpdateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				rasRevenueService.updateEntity(revenueSumOld);
			}
			this.logger.info("对营收数据进行保存操作！");
		}
		
//		3、获取实收汇总数据 先判断是否已存在汇总数据 存在则对未做汇总的进行叠加金额，不存在进行汇总操作
		//根据条件查看是否有汇总数据
		List<RasReceivedDtl> sumReceivedList=rasReceivedService.getRasReceivedDtlByParams(paramsAuditInfo);
		RasReceivedDtl receivedSumOld=null;
		if(sumReceivedList!=null && sumReceivedList.size()>0){
			receivedSumOld=sumReceivedList.get(0);
		}
		//获取最新的待汇总信息进行汇总操作
		sumReceivedList=rasReceivedService.getRasReceivedDtlSumByParams(paramsAuditInfo);
		RasReceivedDtl receivedSum=null;
		if(sumReceivedList!=null&&sumReceivedList.size()==1){
			receivedSum=sumReceivedList.get(0);
			opReceivedAudit=true;
		}else if(revenueSumOld==null){
			receivedSum=new RasReceivedDtl();
			receivedSum.setPayType(String.valueOf(params.get("wPayType")));
			receivedSum.setShopId(String.valueOf(params.get("wShopId")));
			receivedSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			receivedSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
			receivedSum.setClaimType(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			receivedSum.setTransDate(startDate);
			receivedSum.setMoney(new BigDecimal(0));
			receivedSum.setTransMoney(new BigDecimal(0));
		}
		if(receivedSum!=null){
			if(receivedSumOld==null){
				receivedSum.setTransDate(DateUtils.getDateYMD(receivedSum.getTransDate()));
				if(user!=null){
					receivedSum.setCreateDate(todayDay);
					receivedSum.setCreateBy(user.getLoginName());
					receivedSum.setCreateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				//对汇总数据进行保存
				rasReceivedService.saveEntity(receivedSum);
			}else{
				receivedSumOld.setMoney(receivedSumOld.getMoney().add(receivedSum.getMoney()));
				receivedSumOld.setTransMoney(receivedSumOld.getTransMoney().add(receivedSum.getTransMoney()));
				if(user!=null){
					receivedSumOld.setUpdateDate(todayDay);
					receivedSumOld.setUpdateBy(user.getLoginName());
					receivedSumOld.setUpdateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				rasReceivedService.updateEntity(receivedSumOld);
			}
			this.logger.info("对实收数据进行保存操作！");
		}
		
		paramsAuditInfo.remove("status");
		paramsAuditInfo.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_6);
		paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		if(opRevenueAudit){
			//修改已汇总数据状态为已汇总
			rasRevenueService.autoOpAuditStatusControl(paramsAuditInfo);
			this.logger.info("对营收数据稽核中状态变更已汇总操作！");
		}
		if(opReceivedAudit){
			//修改已汇总数据状态为已汇总
			rasReceivedService.autoOpAuditStatusControl(paramsAuditInfo);
			this.logger.info("对实收数据稽核中状态变更已汇总操作！");
		}
	}
	
	
	/**
	 * 根据稽核规则进行营收和实收明细自动稽核
	 * @param params
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void executeStrategyDtlOp(Map<String,Object> params, Map<String,RasRevenueDtlVO> revenueShopPayList,Map<String,RasReceivedDtlVO> receivedShopPayList, ICurrentUser user){
		this.logger.info("begin 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+"("+params.get("rules")+"),门店：("+params.get("shopCode")+")"+params.get("shopName")+",营收数据【"+revenueShopPayList.size()+"】实收数据【"+receivedShopPayList.size()+"】,进行处理============================线程ID：  "+Thread.currentThread().getId());
		String rules=String.valueOf(params.get("rules"));
		String unId=String.valueOf(params.get("wUnid"));
		String payType=String.valueOf(params.get("wPayType"));
		String shopId=String.valueOf(params.get("wShopId"));
		Date todayDay=new Date();
		List<RasAuditInter> rasAuditInterList=new ArrayList<>();
		Map<String, List<RasReceivedDtlVO>> receivedDtlMap=new HashMap<>();
		String key=null;
		List<RasReceivedDtlVO> receivedDtlMapList=null;
		int num=0;
		for (Entry<String, RasReceivedDtlVO> entry : receivedShopPayList.entrySet()) {
			RasReceivedDtlVO info=entry.getValue();
			key=RasUtils.getRulesMapKey(rules, null, info);
			if(receivedDtlMap.containsKey(key)){
				receivedDtlMapList=receivedDtlMap.get(key);
			}else{
				receivedDtlMapList=new ArrayList<>();
				receivedDtlMap.put(key, receivedDtlMapList);
			}
			receivedDtlMapList.add(info);
			num++;
			if(num%10000==0){
				this.logger.debug("10000完成,实收过程完成数量"+num+",线程ID：  "+Thread.currentThread().getId());
			}
		}
		
		num=0;
		Iterator<Entry<String,RasRevenueDtlVO>> iterator = revenueShopPayList.entrySet().iterator();
	    while (iterator.hasNext()) {
	    	Entry<String,RasRevenueDtlVO> entry=iterator.next();
	    	RasRevenueDtlVO revenueDtl=entry.getValue();
	    	key=RasUtils.getRulesMapKey(rules, revenueDtl, null);
	    	receivedDtlMapList=receivedDtlMap.get(key);
			if(receivedDtlMapList!=null&&receivedDtlMapList.size()>0){
				RasReceivedDtlVO receivedDtl=receivedDtlMapList.get(0);
				RasAuditInter auditInter=new RasAuditInter();
				auditInter.setId(UUID.randomUUID().toString().replace("-", ""));
				auditInter.setRevenueId(revenueDtl.getId());
				auditInter.setReceivedId(receivedDtl.getId());
				auditInter.setUnId(unId);
				auditInter.setRules(rules);
				auditInter.setMoney(revenueDtl.getMoney());
				auditInter.setPayType(payType);
				auditInter.setShopId(shopId);
				auditInter.setCreateDate(todayDay);
				rasAuditInterList.add(auditInter);
				
				iterator.remove();
				receivedShopPayList.remove(receivedDtl.getId());
				
				receivedDtlMapList.remove(0);
				if(receivedDtlMapList.size()==0){
					receivedDtlMap.remove(key);
				}
			}
			num++;
			if(num%10000==0){
				this.logger.debug("稽核匹配完成数量"+num+",线程ID：  "+Thread.currentThread().getId());
			}
		}
		
		if(rasAuditInterList.size()>0){
			//批量获取稽核码
			List<String> auditNoList=SequenceSingleton.listSequence(SequenceEnum.RAS_AUDIT_NOZD,rasAuditInterList.size());
			for(int i=0;i<rasAuditInterList.size();i++){
				RasAuditInter info=rasAuditInterList.get(i);
				info.setAuditNo(auditNoList.get(i));
			}
//			rasAuditInterDao.batchSave(rasAuditInterList);
			rasAuditInterDao.batchJdbcSave(rasAuditInterList);
		}
		this.logger.info("end 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+"("+params.get("rules")+"),门店：("+params.get("shopCode")+")"+params.get("shopName")+",营收数据【"+revenueShopPayList.size()+"】实收数据【"+receivedShopPayList.size()+"】,处理结束============================线程ID：  "+Thread.currentThread().getId());
	}
	
	/**
	 * 对汇总模式数据进行操作
	 * @param params
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public synchronized void executeStrategyCountNumOp(Map<String,Object> params,Map<String,RasRevenueDtlVO> revenueShopPayList,Map<String,RasReceivedDtlVO> receivedShopPayList,ICurrentUser user){
		this.logger.info("begin 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+"("+params.get("rules")+"),门店：("+params.get("shopCode")+")"+params.get("shopName")+",营收数据【"+revenueShopPayList.size()+"】实收数据【"+receivedShopPayList.size()+"】,进行处理============================线程ID：  "+Thread.currentThread().getId());
		//针对数据进行按照门店+交易日期+缴款方式（现金|其他）进行汇总 且只汇总上一稽核日期后一天的数据
//		1、先获取最后一次稽核日期的数据
		Map<String,Object> paramsAuditInfo=new HashMap<>();
		paramsAuditInfo.put(RasConstant.RAS_AUTOTASK_OPKEY, "1");
		paramsAuditInfo.put("wUnid", params.get("wUnid"));
		paramsAuditInfo.put("wShopId", params.get("wShopId"));
		paramsAuditInfo.put("wPayType", params.get("wPayType"));
		Date startDate=(Date)params.get("startRevenueDate");
		Date shopAsDate=(Date)params.get("shopAsDate");
		startDate=DateUtils.getDateYMD(startDate);
		shopAsDate=DateUtils.getDateYMD(shopAsDate);
		paramsAuditInfo.put("startDate", startDate);
		paramsAuditInfo.put("statusArray", new Integer[]{RasConstant.RAS_AUDIT_STATUS_5,RasConstant.RAS_AUDIT_STATUS_4,RasConstant.RAS_AUDIT_STATUS_3});
		RasAuditInfo auditInfo = rasAuditService.getRasAuditInfoOneByParams(paramsAuditInfo);
		BigDecimal lessMoney=new BigDecimal(0);
		if(auditInfo!=null){
			startDate=DateUtils.addDay(DateUtils.getDateYMD(auditInfo.getCreateDate()), 1);
			paramsAuditInfo.put("startDate", startDate);
			lessMoney=auditInfo.getLessMoney();
		}else{
			//如何开始时间大约开始稽核日期则不做汇总
			if(startDate.getTime()>shopAsDate.getTime()){
				return;
			}
		}
		Date endDate=DateUtils.addDay(startDate, 1);
		paramsAuditInfo.put("endDate", endDate);
		paramsAuditInfo.put("wClaimType", RasConstant.RAS_CLAIM_TYPE_1);
		paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		paramsAuditInfo.put("isSum", Integer.parseInt(RasConstant.RAS_STATUS_YES));
		boolean opRevenueAudit=false;
		boolean opReceivedAudit=false;
		
		Date todayDay=new Date();
//		2、获取营收汇总数据 先判断是否已存在汇总数据 存在则对未做汇总的进行叠加金额，不存在进行汇总操作
		//根据条件查看是否有汇总数据 
//		List<RasRevenueDtl> sumRevenueList=rasRevenueService.getRasRevenueDtlByParams(paramsAuditInfo);
		RasRevenueDtl revenueSumOld=getRasRevenueDtl(startDate,endDate,revenueShopPayList);
		//获取最新的待汇总信息进行汇总操作
//		paramsAuditInfo.put("notIsSum", Integer.parseInt(RasConstant.RAS_STATUS_YES));
		RasRevenueDtl revenueSum=getRasRevenueDtlSum(startDate,endDate,revenueShopPayList);
		if(revenueSum!=null){
			opRevenueAudit=true;
		}else if(revenueSumOld==null){
			revenueSum=new RasRevenueDtl();
			revenueSum.setPayType(String.valueOf(params.get("wPayType")));
			revenueSum.setShopId(String.valueOf(params.get("wShopId")));
			revenueSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			revenueSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
			revenueSum.setTransDate(startDate);
			revenueSum.setMoney(new BigDecimal(0));
		}
		
		if(revenueSum!=null){
			if(revenueSumOld==null){
				revenueSum.setTransDate(DateUtils.getDateYMD(revenueSum.getTransDate()));
				if(user!=null){
					revenueSum.setCreateDate(todayDay);
					revenueSum.setCreateBy(user.getLoginName());
					revenueSum.setCreateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				if(RasConstant.RAS_PAYTYPE_001.equals(params.get("wPayType"))){
					revenueSum.setMoney(revenueSum.getMoney().add(lessMoney));
				}
				//对汇总数据进行保存
				rasRevenueService.saveEntity(revenueSum);
			}else{
				revenueSumOld.setMoney(revenueSumOld.getMoney().add(revenueSum.getMoney()));
				if(user!=null){
					revenueSumOld.setUpdateDate(todayDay);
					revenueSumOld.setUpdateBy(user.getLoginName());
					revenueSumOld.setUpdateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				rasRevenueService.updateEntity(revenueSumOld);
			}
			this.logger.info("对营收数据进行保存操作！");
		}
		
//		3、获取实收汇总数据 先判断是否已存在汇总数据 存在则对未做汇总的进行叠加金额，不存在进行汇总操作
		//根据条件查看是否有汇总数据
//		List<RasReceivedDtl> sumReceivedList=rasReceivedService.getRasReceivedDtlByParams(paramsAuditInfo);
		RasReceivedDtl receivedSumOld=getRasReceivedDtl(startDate,endDate,receivedShopPayList);
		//获取最新的待汇总信息进行汇总操作
//		sumReceivedList=rasReceivedService.getRasReceivedDtlSumByParams(paramsAuditInfo);
		RasReceivedDtl receivedSum=getRasReceivedDtlSum(startDate,endDate,receivedShopPayList);
		if(receivedSum!=null){
			opReceivedAudit=true;
		}else if(revenueSumOld==null){
			receivedSum=new RasReceivedDtl();
			receivedSum.setPayType(String.valueOf(params.get("wPayType")));
			receivedSum.setShopId(String.valueOf(params.get("wShopId")));
			receivedSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			receivedSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
			receivedSum.setClaimType(Integer.parseInt(RasConstant.RAS_STATUS_YES));
			receivedSum.setTransDate(startDate);
			receivedSum.setMoney(new BigDecimal(0));
			receivedSum.setTransMoney(new BigDecimal(0));
		}
		if(receivedSum!=null){
			if(receivedSumOld==null){
				receivedSum.setTransDate(DateUtils.getDateYMD(receivedSum.getTransDate()));
				if(user!=null){
					receivedSum.setCreateDate(todayDay);
					receivedSum.setCreateBy(user.getLoginName());
					receivedSum.setCreateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				//对汇总数据进行保存
				rasReceivedService.saveEntity(receivedSum);
			}else{
				receivedSumOld.setMoney(receivedSumOld.getMoney().add(receivedSum.getMoney()));
				receivedSumOld.setTransMoney(receivedSumOld.getTransMoney().add(receivedSum.getTransMoney()));
				if(user!=null){
					receivedSumOld.setUpdateDate(todayDay);
					receivedSumOld.setUpdateBy(user.getLoginName());
					receivedSumOld.setUpdateName(user.getUsername());
					paramsAuditInfo.put("updateBy", user.getLoginName());
					paramsAuditInfo.put("updateName", user.getUsername());
					paramsAuditInfo.put("updateDate", todayDay);
				}
				rasReceivedService.updateEntity(receivedSumOld);
			}
			this.logger.info("对实收数据进行保存操作！");
		}
		
		paramsAuditInfo.remove("status");
		paramsAuditInfo.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_6);
		paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
		if(opRevenueAudit){
			//修改稽核中数据状态为已汇总
			rasRevenueService.autoOpAuditStatusControl(paramsAuditInfo);
			this.logger.info("对营收数据稽核中状态变更已汇总操作！");
		}
		if(opReceivedAudit){
			//修改稽核中数据状态为已汇总
			rasReceivedService.autoOpAuditStatusControl(paramsAuditInfo);
			this.logger.info("对实收数据稽核中状态变更已汇总操作！");
		}
		this.logger.info("end 缴款方式:("+params.get("wPayType")+")"+params.get("payTypeName")+",策略信息："+params.get("strategyName")+"("+params.get("rules")+"),门店：("+params.get("shopCode")+")"+params.get("shopName")+",营收数据【"+revenueShopPayList.size()+"】实收数据【"+receivedShopPayList.size()+"】,处理结束============================线程ID：  "+Thread.currentThread().getId());
	}
	
	/**
	 * 获取营收数据现金汇总的合计值
	 * @param startDate
	 * @param endDate
	 * @param revenueShopPayList
	 * @return
	 */
	public RasRevenueDtl getRasRevenueDtl(Date startDate,Date endDate,Map<String,RasRevenueDtlVO> revenueShopPayList){
		RasRevenueDtl revenueSum=null;
		for (Entry<String, RasRevenueDtlVO> entry : revenueShopPayList.entrySet()) {
			RasRevenueDtlVO info=entry.getValue();
//		for(RasRevenueDtlVO info:revenueShopPayList){
			if(info.getTransDate().getTime()<startDate.getTime()){
				continue;
			}
			if(info.getTransDate().getTime()>=startDate.getTime()
					&&info.getTransDate().getTime()<endDate.getTime()
					&&RasConstant.RAS_STATUS_YES.equals(String.valueOf(info.getIsSum()))){
				revenueSum=new RasRevenueDtl();
				revenueSum.setId(info.getId());
				revenueSum.setPayType(info.getPayType());
				revenueSum.setShopId(info.getShopId());
				revenueSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
				revenueSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
				revenueSum.setTransDate(startDate);
				revenueSum.setMoney(info.getMoney());
				break;
			}else if(info.getTransDate().getTime()>=endDate.getTime()){
				break;
			}
		}
		return revenueSum;
	}
	
	
	/**
	 * 获取实数据现金汇总的合计值
	 * @param startDate
	 * @param endDate
	 * @param revenueShopPayList
	 * @return
	 */
	public RasReceivedDtl getRasReceivedDtl(Date startDate,Date endDate,Map<String,RasReceivedDtlVO> receivedShopPayList){
		RasReceivedDtl receivedSum=null;
		for (Entry<String, RasReceivedDtlVO> entry : receivedShopPayList.entrySet()) {
			RasReceivedDtlVO info=entry.getValue();
//		for(RasReceivedDtlVO info:receivedShopPayList){
			if(info.getTransDate().getTime()<startDate.getTime()){
				continue;
			}
			if(info.getTransDate().getTime()>=startDate.getTime()
					&&info.getTransDate().getTime()<endDate.getTime()
					&&RasConstant.RAS_STATUS_YES.equals(String.valueOf(info.getIsSum()))){
				receivedSum=new RasReceivedDtl();
				receivedSum.setId(info.getId());
				receivedSum.setPayType(info.getPayType());
				receivedSum.setShopId(info.getShopId());
				receivedSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
				receivedSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
				receivedSum.setClaimType(Integer.parseInt(RasConstant.RAS_STATUS_YES));
				receivedSum.setTransDate(startDate);
				receivedSum.setMoney(info.getMoney());
				receivedSum.setTransMoney(info.getTransMoney());
				break;
			}else if(info.getTransDate().getTime()>=endDate.getTime()){
				break;
			}
		}
		return receivedSum;
	}
	
	/**
	 * 获取营收数据现金汇总的合计值
	 * @param startDate
	 * @param endDate
	 * @param revenueShopPayList
	 * @return
	 */
	public RasRevenueDtl getRasRevenueDtlSum(Date startDate,Date endDate,Map<String,RasRevenueDtlVO> revenueShopPayList){
		RasRevenueDtl revenueSum=null;
		for (Entry<String, RasRevenueDtlVO> entry : revenueShopPayList.entrySet()) {
			RasRevenueDtlVO info=entry.getValue();
//		for(RasRevenueDtlVO info:revenueShopPayList){
			if(info.getTransDate().getTime()<startDate.getTime()){
				continue;
			}
			if(info.getTransDate().getTime()>=startDate.getTime()
					&&info.getTransDate().getTime()<endDate.getTime()
					&&!RasConstant.RAS_STATUS_YES.equals(String.valueOf(info.getIsSum()))){
				if(revenueSum==null){
					revenueSum=new RasRevenueDtl();
					revenueSum.setPayType(info.getPayType());
					revenueSum.setShopId(info.getShopId());
					revenueSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
					revenueSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
					revenueSum.setTransDate(startDate);
					revenueSum.setMoney(new BigDecimal(0));
				}
				revenueSum.setMoney(revenueSum.getMoney().add(info.getMoney()));
			}else if(info.getTransDate().getTime()>=endDate.getTime()){
				break;
			}
		}
		return revenueSum;
	}
	

	/**
	 * 获取实数据现金汇总的合计值
	 * @param startDate
	 * @param endDate
	 * @param revenueShopPayList
	 * @return
	 */
	public RasReceivedDtl getRasReceivedDtlSum(Date startDate,Date endDate,Map<String,RasReceivedDtlVO> receivedShopPayList){
		RasReceivedDtl receivedSum=null;
		for (Entry<String, RasReceivedDtlVO> entry : receivedShopPayList.entrySet()) {
			RasReceivedDtlVO info=entry.getValue();
			if(info.getTransDate().getTime()<startDate.getTime()){
				continue;
			}
			if(info.getTransDate().getTime()>=startDate.getTime()
					&&info.getTransDate().getTime()<endDate.getTime()
					&&!RasConstant.RAS_STATUS_YES.equals(String.valueOf(info.getIsSum()))){
				if(receivedSum==null){
					receivedSum=new RasReceivedDtl();
					receivedSum.setPayType(info.getPayType());
					receivedSum.setShopId(info.getShopId());
					receivedSum.setIsSum(Integer.parseInt(RasConstant.RAS_STATUS_YES));
					receivedSum.setStatus(RasConstant.RAS_AUDIT_STATUS_1);
					receivedSum.setClaimType(Integer.parseInt(RasConstant.RAS_STATUS_YES));
					receivedSum.setTransDate(startDate);
					receivedSum.setMoney(new BigDecimal(0));
					receivedSum.setTransMoney(new BigDecimal(0));
				}
				receivedSum.setMoney(receivedSum.getMoney().add(info.getMoney()));
				receivedSum.setTransMoney(receivedSum.getTransMoney().add(info.getTransMoney()));
			}else if(info.getTransDate().getTime()>=endDate.getTime()){
					break;
			}
		}
		return receivedSum;
	}
	
	/**
	 * 稽核完成对稽核中间表进行处理
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void auditEndRasAuditInter(Map<String,Object> params, ICurrentUser user){
		String unId=String.valueOf(params.get("wUnid"));
		int num=rasAuditInterDao.getCountNumByUnid(unId);
		this.logger.debug(" 稽核完成笔数:" + num + " ============================线程ID：  "+Thread.currentThread().getId());
		if(num>0){
			params.put("auditStatus", RasConstant.RAS_AUDIT_STATUS_3);
			params.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_2);
			if(user!=null){
				params.put("updateBy", user.getLoginName());
				params.put("updateName", user.getUsername());
				params.put("updateDate", new Date());
			}
			//批量修改营收数据为已稽核
			params.put("isAuditIdInter", "1");
			int numUpdate=rasRevenueService.autoOpAuditStatusControl(params);
			this.logger.debug(" 批量修改营收数据为已稽核:" + numUpdate + " ============================线程ID：  "+Thread.currentThread().getId());
			//批量修改实收数据为已稽核
			numUpdate=rasReceivedService.autoOpAuditStatusControl(params);
			this.logger.debug(" 批量修改实收数据为已稽核:" + numUpdate + " ============================线程ID：  "+Thread.currentThread().getId());
			if(user!=null){
				params.put("createBy", user.getLoginName());
				params.put("createName", user.getUsername());
			}else{
				params.put("createBy", null);
				params.put("createName", null);
			}
			//批量通过中间表写入稽核表
			rasAuditService.saveAuditInfoByAuditInter(params);
			this.logger.debug(" 批量通过中间表写入稽核表:" + num + " ============================线程ID：  "+Thread.currentThread().getId());
		}
		this.logger.debug(" 稽核成功笔数:" + num + " ============================线程ID：  "+Thread.currentThread().getId());
	}
}
