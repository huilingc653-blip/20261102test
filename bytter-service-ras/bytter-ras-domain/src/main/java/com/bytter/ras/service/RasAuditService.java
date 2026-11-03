package com.bytter.ras.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.ras.dao.RasAuditDao;
import com.bytter.ras.dao.RasAuditInterDao;
import com.bytter.ras.entity.RasAuditInfo;
import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.entity.RasShop;
import com.bytter.ras.utils.RasThreadUtil;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.RasAuditBackVO;
import com.bytter.ras.vo.RasAuditInfoVO;
import com.bytter.ras.vo.RasPayTypeVO;
import com.bytter.ras.vo.RasReceivedDtlVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.INotificationService;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: -稽核业务处理类
 * @date 2019-05-16
 */
@Service("rasAuditService")
@Transactional
public class RasAuditService extends BaseService<RasAuditInfo, String> {

    @Autowired
    private RasConfigService rasConfigService;

    @Autowired
    private RasShopGrantService rasShopGrantService;

    @Autowired
    private RasAuditDao rasAuditDao;

    @Autowired(required=false)
    private ICorpService corpService;

    @Autowired
    private RasLogService rasLogService;

    @Autowired
    private RasRevenueService rasRevenueService;

    @Autowired
    private RasReceivedService rasReceivedService;

    @Autowired
    private RasShopService rasShopService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired
    private RasAuditTaskService rasAuditTaskService;

    @Autowired
    private RasAuditStrategyService rasAuditStrategyService;

    @Autowired(required=false)
    private IDictService dictService;

	@Autowired
	private RasAuditInterDao rasAuditInterDao;

    @Override
    public BaseDao<RasAuditInfo, String> getEntityDao() {
        return rasAuditDao;
    }


    /**
     * 加载营收反稽核数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listRasAuditBack(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_BACKAUDIT_OP, dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasAuditDao.getDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }


    /**
     * 反稽核确认
     *
     * @param aids   稽核ids
     * @param userVo 操作用户
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public boolean backAudit(String aids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(aids)) {
            String[] aidArray = aids.split(",");
            //更新稽核表状态为-1稽核打回
            int borrowNum = batchUpdate(aidArray, RasConstant.RAS_AUDIT_STATUS_RETURN_1, null, userVo);
            if (borrowNum != aidArray.length) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            } else {
                //更新营收数据明细表状态为1待稽核
                rasRevenueService.batchUpdate(aidArray, RasConstant.RAS_AUDIT_STATUS_1, null, userVo);
                //更新实收数据明细表状态为1待稽核
                rasReceivedService.batchUpdateByAid(aidArray, RasConstant.RAS_AUDIT_STATUS_1, null, userVo);
                //添加日志
                rasLogService.batchSaveRasLog(aidArray, RasConstant.RAS_LOGTYPE_BACK_AUDIT, "营收稽核反稽核确认", userVo);
            }
        } else {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        return true;
    }

    /**
     * 根据查询条件自动反稽核确认
     *
     * @param rasAuditBackVO   反稽核查询参数
     * @param userVo 操作用户
     */
    public boolean autoBackAudit(RasAuditBackVO rasAuditBackVO, ICurrentUser userVo) {
        boolean resule = false;
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_BACKAUDIT_OP, userVo.getId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, userVo);
        List<RasAuditBackVO> auditBackList = null;
        do{
	        auditBackList = rasAuditDao.getBackAuditList(rasAuditBackVO,corpAuthList,shopAuthList);
	        if (auditBackList != null && !auditBackList.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (RasAuditBackVO info : auditBackList) {
					if(sb.length()>0){
						sb.append(",");
					}
					sb.append(info.getId());
				}
				((RasAuditService)this.getBean(this.getClass())).backAudit(sb.toString(),userVo);
	        }
        }while(auditBackList!=null&&auditBackList.size()==RasConstant.RAS_AUTOT_MAXRESULT);
        resule = true;
        return resule;
    }


    /**
     * 根据ids批量修改稽核表数据状态
     *
     * @param ids       稽核表ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废）
     * @param statusOld 根据对应旧状态处理状态值修改
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, Integer statusNew, Integer statusOld, ICurrentUser userVo) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if (statusOld != null) {
            params.put("statusOld", statusOld);
        }
        int num = rasAuditDao.batchUpdate(params);
        return num;
    }

    /**
     * 根据ids批量修改稽核表数据状态
     *
     * @param ids       稽核表ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废）
     * @return 修改成功的笔数
     */
    public int batchUpdateBackAudit(String[] ids, Integer statusNew, ICurrentUser userVo) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        int num = rasAuditDao.batchUpdate(params);
        return num;
    }
    
    /**
     * 根据参数获取稽核第一条数据
     *
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    public RasAuditInfo getRasAuditInfoOneByParams(Map<String, Object> params) {
    	return rasAuditDao.getRasAuditInfoOneByParams(params);
    }



    /**
     * 根据参数获取稽核数据
     *
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    public List<RasAuditInfo> getRasAuditInfoByParams(Map<String, Object> params) {
        return rasAuditDao.getRasAuditInfoByParams(params);
    }


    /**
     * 根据规则进行营收和实收数据匹配
     *
     * @param params    传递查询参数
     * @param rulesWsql 稽核关联条件
     * @return
     */
    @Transactional(readOnly = true)
    public List<RasAuditInfo> getRasAuditInfo(Map<String, Object> params, String rulesWsql) {
        return rasAuditDao.getRasAuditInfo(params, rulesWsql);
    }


    /**
     * 进行稽核操作
     *
     * @param auditInfoVO
     * @param user
     * @return
     */
    public boolean auditOp(RasAuditInfoVO auditInfoVO) {
        if (RasConstant.RAS_AUDIT_OPTYPE_AUTO.equals(auditInfoVO.getOpType())) {
//        	return auditAuto(auditInfoVO);
            return auditAutoDataCache(auditInfoVO);
        } else if (RasConstant.RAS_AUDIT_OPTYPE_MANUAL.equals(auditInfoVO.getOpType())
                || RasConstant.RAS_AUDIT_OPTYPE_LESS.equals(auditInfoVO.getOpType())) {
            return auditManual(auditInfoVO);
        }
        return false;
    }
    
    /**
     * 进行手工稽核中进行自动稽核操作
     *
     * @param auditInfoVO
     * @param user
     * @return
     */
    public boolean auditAuto(RasAuditInfoVO auditInfoVO) {
    	initAuditInfoVO(auditInfoVO);
    	this.logger.info("begin 手工稽核执行开始 ============================"+auditInfoVO.getUnId());
    	try {
    		//先把待稽核数据变成稽核中
    		setRevenueReceivedDataLock(auditInfoVO);
    		if (auditInfoVO.getRasPayTypeVO().size() < 1) {
    			RasUtils.setPayTypeList(auditInfoVO.getPayType(), auditInfoVO.getRasPayTypeVO());
//    			//this.inject(auditInfoVO.getRasPayTypeVO());
    		}
    		for (RasPayTypeVO payType : auditInfoVO.getRasPayTypeVO()) {
    			auditInfoVO.setPayType(payType.getPayType());
    			auditInfoVO.setPayTypeName(payType.getPayTypeName());
    			executeStrategyByPayType(auditInfoVO);
    		}
    	} catch (Exception e) {
    		this.logger.error("触发手工稽核执行报错", e);
    		throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR);
    	} finally {
    		setRevenueReceivedDataUnLock(auditInfoVO);
    		this.logger.info("end 手工稽核执行结束==============================="+auditInfoVO.getUnId());
    	}
    	return true;
    }

    /**
     * 进行手工稽核中进行自动稽核操作
     *
     * @param auditInfoVO
     * @param user
     * @return
     */
    public boolean auditAutoDataCache(RasAuditInfoVO auditInfoVO) {
//		long beforeMemory = Runtime.getRuntime().totalMemory();
//		long start1 = System.currentTimeMillis();
        initAuditInfoVO(auditInfoVO);
        this.logger.info("begin 手工稽核执行开始 ============================"+auditInfoVO.getUnId());
        try {
            //先把待稽核数据变成稽核中
            setRevenueReceivedDataLock(auditInfoVO);
            Map<String, Object> params = new HashMap<>();
            params.put("wUnid", auditInfoVO.getUnId());
            
//			List<RasRevenueDtlVO> revenueList=rasRevenueService.getAllRasRevenueDtlVOInfo(params);
//			this.logger.info("营收数据量============================"+revenueList.size());
//			List<RasReceivedDtlVO> receivedList=rasReceivedService.getAllRasReceivedDtlVOInfo(params);
//			this.logger.info("实收数据量============================"+receivedList.size());
//			auditDataCacheOp(revenueList, receivedList, auditInfoVO);
//			revenueList=null;
//			receivedList=null;
			
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
			auditDataCacheOp(revenueZList, receivedZList, auditInfoVO);
			revenueZList=null;
	        receivedZList=null;
        } catch (Exception e) {
            this.logger.error("触发手工稽核执行报错", e);
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR);
        } finally {
            setRevenueReceivedDataUnLock(auditInfoVO);
            rasAuditInterDao.deleteByUnid(auditInfoVO.getUnId());
            this.logger.info("end 手工稽核执行结束==============================="+auditInfoVO.getUnId());
        }

//        long afterMemory = Runtime.getRuntime().totalMemory();
//        long end1=System.currentTimeMillis();
//        System.out.println("总共内存使用:" + (afterMemory - beforeMemory) / 1024 / 1024 + "MB");
//        System.out.println("存入内存耗时:"+(end1-start1)+"毫秒");
//        System.gc();
//        try { Thread.sleep (10*1000);}catch(Exception e){}
//        long afterMemory1 = Runtime.getRuntime().totalMemory();
//        long end11=System.currentTimeMillis();
//        System.out.println("总共内存使用:" + (afterMemory1 - afterMemory1) / 1024 / 1024 + "MB");
//        System.out.println("存入内存耗时:"+(end1-end11)+"毫秒");
//        System.out.println("总共内存使用:" + (afterMemory1 - beforeMemory) / 1024 / 1024 + "MB");
//        System.out.println("存入内存耗时:"+(end11-start1)+"毫秒");
        return true;
    }
    
    /**
     * 对数据进行初始化操作
     * @param revenueList
     * @param receivedList
     * @param revenueMap
     * @param receivedMap
     * @param shopMap
     * @param payTypeMap
     */
    public void auditDataCacheInit(List<RasRevenueDtlVO> revenueList,
    		List<RasReceivedDtlVO> receivedList,
    		Map<String,Map<String,RasRevenueDtlVO>> revenueMap,
    		Map<String,Map<String,RasReceivedDtlVO>> receivedMap){
		//通过MAP把营收实收数据进行按门店和缴款方式区分匹配
		Map<String,RasRevenueDtlVO> shopPayRevenueList=null;
		for(RasRevenueDtlVO info:revenueList){
			String key=info.getShopId()+RasConstant.RAS_MAP_KEYSEPARATORCHARS+info.getPayType();
			if(revenueMap.containsKey(key)){
				shopPayRevenueList=revenueMap.get(key);
			}else{
				shopPayRevenueList=new LinkedHashMap<>();
				revenueMap.put(key, shopPayRevenueList);
			}
			shopPayRevenueList.put(info.getId(), info);
		}

		Map<String,RasReceivedDtlVO> shopPayReceivedList=null;
		for(RasReceivedDtlVO info:receivedList){
			String key=info.getShopId()+RasConstant.RAS_MAP_KEYSEPARATORCHARS+info.getPayType();
			if(receivedMap.containsKey(key)){
				shopPayReceivedList=receivedMap.get(key);
			}else{
				shopPayReceivedList=new LinkedHashMap<>();
				receivedMap.put(key, shopPayReceivedList);
			}
			shopPayReceivedList.put(info.getId(), info);
		}
    }
    
    /**
     * 对获取到的营收实收数据进行操作
     * @param revenueList 营收数据
     * @param receivedList 实收数据
     * @param auditInfoVO 操作参数信息
     */
    public void auditDataCacheOp(List<RasRevenueDtlVO> revenueList,List<RasReceivedDtlVO> receivedList,RasAuditInfoVO auditInfoVO){
    	Map<String,Map<String,RasRevenueDtlVO>> revenueMap=new HashMap<>();
		Map<String,Map<String,RasReceivedDtlVO>> receivedMap=new HashMap<>();
//		auditInfoVO.getShopMap()
//		auditInfoVO.getPayTypeMap()
		auditDataCacheInit(revenueList, receivedList, revenueMap, receivedMap);
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
						rasAuditTaskService.executeStrategyCountNumOp(params, revenueShopPayList, receivedShopPayList, auditInfoVO.getUser());
					}
				}
			}
		}
		
		Map<String,Object> params=new HashMap<>();
        params.put("wUnid", auditInfoVO.getUnId());
		rasAuditTaskService.auditEndRasAuditInter(params, auditInfoVO.getUser());
    }
    
    /**
     * 对稽核查询条件进行初始化
     * @param params
     * @param info
     * @param shop
     */
    public void rasAuditInfoVOParamsInit(Map<String,Object> params, RasAuditInfoVO info, RasShop shop){
		params.put("startRevenueDate", info.getRevenueTransDateStart());
        params.put("startReceivedDate", info.getReceivedTransDateStart());
        params.put("endRevenueDate", info.getRevenueTransDateEnd());
        params.put("endReceivedDate", info.getReceivedTransDateEnd());
        params.put("wValidSign", RasConstant.RAS_VALID_SIGN_1);
        
        params.put("wUnid", info.getUnId());
        params.put("wShopId", shop.getId());
        params.put("shopCode", shop.getCode());
        params.put("shopName", shop.getName());
        params.put("shopAsDate", shop.getAsDate());
        if (shop.getAsDate().getTime() > info.getRevenueTransDateStart().getTime()) {
            params.put("startRevenueDate", shop.getAsDate());
        }
        if (shop.getAsDate().getTime() > info.getReceivedTransDateStart().getTime()) {
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
    public void auditShopPayDate(String shopId,String payType,Map<String,RasRevenueDtlVO> revenueShopPayList,Map<String,RasReceivedDtlVO> receivedShopPayList, RasAuditInfoVO info){
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
        		rasAuditTaskService.executeStrategyDtlOp(params, revenueShopPayList, receivedShopPayList, info.getUser());
            }
            this.logger.info("end 对门店("+shop.getCode()+")"+shop.getName()+",缴款方式：(" + info.getPayType() + ") 处理结束============================线程ID：  "+Thread.currentThread().getId());
    	}
    }

    /**
     * 进行自动稽核数据初始化
     *
     * @param auditInfoVO
     */
    @Transactional(readOnly = true)
    public void initAuditInfoVO(RasAuditInfoVO auditInfoVO) {
        ICurrentUser user = auditInfoVO.getUser();
        if (StringUtils.isNotEmpty(auditInfoVO.getCorpId())) {
            auditInfoVO.setCorpIdList(StringUtils.splitToList(",", auditInfoVO.getCorpId()));
        } else {
            //获取该用户有权限的单位id集
            List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_MANUALAUDIT_OP, user.getId());
            auditInfoVO.setCorpIdList(corpAuthList);
        }
        if (StringUtils.isNotEmpty(auditInfoVO.getShopId())) {
            auditInfoVO.setShopIdList(StringUtils.splitToList(",", auditInfoVO.getShopId()));
        } else {
            //获取该用户有（查询）权限的门店id集
            List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, auditInfoVO.getCorpIdList(), user);
            auditInfoVO.setShopIdList(shopAuthList);
        }
        if (StringUtils.isNotEmpty(auditInfoVO.getPayType())) {
            auditInfoVO.setPayTypeList(StringUtils.splitToList(",", auditInfoVO.getPayType()));
        } else {
            List<DictDTO> dictDTOList = dictService.list(RasConstant.RAS_PAYTYPE_DICT);
            if (dictDTOList != null && dictDTOList.size() > 0) {
                List<String> codeList = new ArrayList<>();
                List<RasPayTypeVO> payTypeVOList = new ArrayList<>();
                RasPayTypeVO paytype = null;
                for (DictDTO dict : dictDTOList) {
                    codeList.add(dict.getCode());
                    paytype = new RasPayTypeVO();
                    paytype.setPayType(dict.getCode());
                    paytype.setPayTypeName(dict.getName());
                    payTypeVOList.add(paytype);
                }
                auditInfoVO.setPayTypeList(codeList);
                auditInfoVO.setRasPayTypeVO(payTypeVOList);
            }
        }
        if (StringUtils.isNotEmpty(auditInfoVO.getDataSource())) {
            auditInfoVO.setDataSourceList(StringUtils.splitToList(",", auditInfoVO.getDataSource()));
        }

        //设置营收起始时间
        if (auditInfoVO.getRevenueTransDateStart() == null) {
            int revenueRollDay = rasConfigService.getRevenueRollDay();
            if (revenueRollDay > 0) {
                auditInfoVO.setRevenueTransDateStart(DateUtils.addDay(DateUtils.getDateYMD(), revenueRollDay * -1));
            } else {
                auditInfoVO.setRevenueTransDateStart(DateUtils.getDateYMD());
            }
        }
        //设置实收起始时间
        if (auditInfoVO.getReceivedTransDateStart() == null) {
            int receivedRollDay = rasConfigService.getReceivedRollDay();
            if (receivedRollDay > 0) {
                auditInfoVO.setReceivedTransDateStart(DateUtils.addDay(DateUtils.getDateYMD(), receivedRollDay * -1));
            } else {
                auditInfoVO.setReceivedTransDateStart(DateUtils.getDateYMD());
            }
        }
        //设置营收结束时间
        if (auditInfoVO.getRevenueTransDateEnd() == null) {
            auditInfoVO.setRevenueTransDateEnd(DateUtils.addDay(DateUtils.getDateYMD(), 1));
        } else {
            auditInfoVO.setRevenueTransDateEnd(DateUtils.addDay(auditInfoVO.getRevenueTransDateEnd(), 1));
        }
        //设置实收结束时间
        if (auditInfoVO.getReceivedTransDateEnd() == null) {
            auditInfoVO.setReceivedTransDateEnd(DateUtils.addDay(DateUtils.getDateYMD(), 1));
        } else {
            auditInfoVO.setReceivedTransDateEnd(DateUtils.addDay(auditInfoVO.getReceivedTransDateEnd(), 1));
        }

		if(StringUtils.isEmpty(auditInfoVO.getUnId())){
			auditInfoVO.setUnId(UUID.randomUUID().toString().replace("-", ""));
		}
		

		Map<String,Object> params=new HashMap<>();
		if(auditInfoVO.getShopIdList().size()>0){
			//初始化门店信息
			params.put("ids", auditInfoVO.getShopIdList());
			List<RasShop> shopList=rasShopService.getRasShopValidByParams(params);
			if(shopList!=null){
				for(RasShop info:shopList){
					auditInfoVO.getShopMap().put(info.getId(), info);
				}
			}
			params.remove("ids");
		}
		if(auditInfoVO.getPayTypeList().size()>0){
			//初始化缴款方式策略信息
			params.put("wPayTypes", auditInfoVO.getPayTypeList());
			List<RasAuditStrategy> auditStrategyList=rasAuditStrategyService.getRasAuditStrategyValidList(params);
			for(RasAuditStrategy info:auditStrategyList){
				List<RasAuditStrategy> infoList=null;
				if(auditInfoVO.getPayTypeMap().containsKey(info.getPayType())){
					infoList=auditInfoVO.getPayTypeMap().get(info.getPayType());
				}else{
					infoList=new ArrayList<>();
					auditInfoVO.getPayTypeMap().put(info.getPayType(), infoList);
				}
				infoList.add(info);
			}
			params.remove("wPayTypes");
		}
    }


    /**
     * 针对对应缴款方式和分门店进行稽核处理
     *
     * @param info
     */
    public void executeStrategyByPayType(RasAuditInfoVO info) {
        this.logger.info("begin 对(" + info.getPayType() + ")" + info.getPayTypeName() + "缴款方式 进行处理============================线程ID：  "+Thread.currentThread().getId());
        Map<String, Object> params = new HashMap<>();
        params.put("startRevenueDate", info.getRevenueTransDateStart());
        params.put("startReceivedDate", info.getReceivedTransDateStart());
        params.put("endRevenueDate", info.getRevenueTransDateEnd());
        params.put("endReceivedDate", info.getReceivedTransDateEnd());
        params.put("wPayType", info.getPayType());
        params.put("payTypeName", info.getPayTypeName());
        params.put("wValidSign", RasConstant.RAS_VALID_SIGN_1);
        List<RasAuditStrategy> auditStrategyList = rasAuditStrategyService.getRasAuditStrategyValidList(params);
//        List<RasAuditStrategy> auditStrategyList=info.getAuditStrategyList();
//		if(auditStrategyList==null){
//			auditStrategyList=rasAuditStrategyService.getRasAuditStrategyValidList(params);
//			info.setAuditStrategyList(auditStrategyList);
//		}
        if (auditStrategyList == null || auditStrategyList.size() < 1) {
            this.logger.info(" 此缴款方式无有效的策略信息！ ");
        } else {
            List<String> shopIdList = info.getShopIdList();
            //进行多线程处理
            ExecutorService esThread = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(RasConstant.RAS_AUTOT_THREAD_SIZE));

            for (String shopId : shopIdList) {
            	RasShop shop=info.getShopMap().get(shopId);
            	if(shop==null){
            		shop = rasShopService.get(shopId, true);
            		info.getShopMap().put(shopId, shop);
            	}
                if (shop != null) {
                    params.put("wUnid", info.getUnId());
                    params.put("wShopId", shop.getId());
                    params.put("shopCode", shop.getCode());
                    params.put("shopName", shop.getName());
                    params.put("shopAsDate", shop.getAsDate());
                    if (shop.getAsDate().getTime() > info.getRevenueTransDateStart().getTime()) {
                        params.put("startRevenueDate", shop.getAsDate());
                    }
                    if (shop.getAsDate().getTime() > info.getReceivedTransDateStart().getTime()) {
                        params.put("startReceivedDate", shop.getAsDate());
                    }
//                    for (RasAuditStrategy strategy : auditStrategyList) {
//                        params.put("strategyName", strategy.getName());
//                        params.put("rules", strategy.getRules());
//                        rasAuditTaskService.executeStrategyByPayTypeAndStrategy(params, info.getUser());
//                        esThread.execute(new RasThreadUtil(rasAuditTaskService, params, info.getUser()));//执行
//                    }
                    Map<String, Object> paramsNew = new HashMap<>();
                    paramsNew.putAll(params);
                    esThread.execute(new RasThreadUtil(rasAuditTaskService, auditStrategyList, paramsNew, info.getUser()));//执行
                } else {
                    this.logger.info("门店信息不存在，shopId=" + shopId);
                }
            }
    		try {
                esThread.shutdown();
    			esThread.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    		} catch (InterruptedException e) {
                this.logger.error("触发手工稽核执行,多线程同步异常报错", e);
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR);
    		}
        }

        this.logger.info("end 对(" + info.getPayType() + ")" + info.getPayTypeName() + "缴款方式 处理结束============================线程ID：  "+Thread.currentThread().getId());
    }

    /**
     * 修改营收实收数据进行释放锁定状态
     *
     * @return
     */
    public boolean setRevenueReceivedDataUnLock(RasAuditInfoVO info) {
        this.logger.info("====解锁数据，稽核中数据变成待稽核===============================：" + info.toString());
        return setRevenueReceivedDataLock(info, false);
    }


    /**
     * 修改营收实收数据进行锁定状态
     *
     * @return
     */
    public boolean setRevenueReceivedDataLock(RasAuditInfoVO info) {
        this.logger.info("====锁定数据，待稽核数据变成稽核中===============================：" + info.toString());
        return setRevenueReceivedDataLock(info, true);
    }


    /**
     * 修改任务执行状态
     *
     * @return
     */
    public boolean setRevenueReceivedDataLock(RasAuditInfoVO info, boolean lockType) {
        Map<String, Object> params = new HashMap<>();
        if(lockType){
			params.put("unId", info.getUnId());
	        params.put("shopIdArray", info.getShopIdList());
	        params.put("revenueTransDateStart", info.getRevenueTransDateStart());
	        params.put("revenueTransDateEnd", info.getRevenueTransDateEnd());
	        params.put("receivedTransDateStart", info.getReceivedTransDateStart());
	        params.put("receivedTransDateEnd", info.getReceivedTransDateEnd());
	        if (info.getMoneyStart() != null) {
	            params.put("moneyStart", info.getMoneyStart());
	        }
	        if (info.getMoneyStart() != null) {
	            params.put("moneyEnd", info.getMoneyEnd());
	        }
	        if (info.getDataSourceList() != null && info.getDataSourceList().size() > 0) {
	            params.put("dataSourceArray", info.getDataSourceList());
	        }
	        params.put("payTypeArray", info.getPayTypeList());
		}else{
			params.put("wUnid", info.getUnId());
		}
        Date todayDay = new Date();
        if (info.getUser() != null) {
            params.put("updateBy", info.getUser().getLoginName());
            params.put("updateName", info.getUser().getUsername());
            params.put("updateDate", todayDay);
        }
        return rasAuditTaskService.setRevenueReceivedDataLock(params, lockType);
    }

    /**
     * 进行手工稽核中进行手动稽核
     *
     * @param auditInfoVO
     * @param user
     * @return
     */
    public boolean auditManual(RasAuditInfoVO auditInfoVO) {
        RasAuditInfo rasAuditInfo = new RasAuditInfo();
        BeanUtils.copyNotNullProperties(auditInfoVO, rasAuditInfo);
        rasAuditInfo.setType(RasConstant.RAS_AUDIT_TYPE_1);
        String logType = RasConstant.RAS_LOGTYPE_MANUAL_AUDIT;
        String logMsg = "手工稽核完成！";
        if (RasConstant.RAS_AUDIT_OPTYPE_MANUAL.equals(auditInfoVO.getOpType())) {
            rasAuditInfo.setAuditNo(SequenceSingleton.getSequence(SequenceEnum.RAS_AUDIT_NOJG));
            rasAuditInfo.setStatus(RasConstant.RAS_AUDIT_STATUS_3);
        } else if (RasConstant.RAS_AUDIT_OPTYPE_LESS.equals(auditInfoVO.getOpType())) {
            rasAuditInfo.setAuditNo(SequenceSingleton.getSequence(SequenceEnum.RAS_AUDIT_NOCG));
            rasAuditInfo.setStatus(RasConstant.RAS_AUDIT_STATUS_4);
            //如果为现金差异稽核需要进行消息提醒判断操作
            if (RasConstant.RAS_PAYTYPE_001.equals(rasAuditInfo.getPayType())) {
                sendMsgOp(rasAuditInfo);
            }
            logType = RasConstant.RAS_LOGTYPE_DIFFERENT_AUDIT;
            logMsg = "手工差异稽核完成！";
        } else {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        Date todayDay = new Date();
        if (RasConstant.RAS_PAYTYPE_001.equals(rasAuditInfo.getPayType())
                || RasConstant.RAS_PAYTYPE_006.equals(rasAuditInfo.getPayType())) {
            rasAuditInfo.setCreateDate(auditInfoVO.getTransDate());
        } else {
            rasAuditInfo.setCreateDate(todayDay);
        }
        Map<String, Object> paramsAuditInfo = new HashMap<>();
        if (auditInfoVO.getUser() != null) {
            rasAuditInfo.setCreateBy(auditInfoVO.getUser().getLoginName());
            rasAuditInfo.setCreateName(auditInfoVO.getUser().getUsername());
            paramsAuditInfo.put("updateBy", auditInfoVO.getUser().getLoginName());
            paramsAuditInfo.put("updateName", auditInfoVO.getUser().getUsername());
            paramsAuditInfo.put("updateDate", todayDay);
        }
        this.saveEntity(rasAuditInfo);

        paramsAuditInfo.put("auditId", rasAuditInfo.getId());
        paramsAuditInfo.put("auditStatus", rasAuditInfo.getStatus());
        paramsAuditInfo.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_1);

        //修改营收数据未已稽核
        if (StringUtils.isNotEmpty(rasAuditInfo.getRevenueId())) {
            String[] ids = StringUtils.split(rasAuditInfo.getRevenueId(), ",");
            paramsAuditInfo.put("ids", ids);
            int size = rasRevenueService.autoOpAuditStatusControl(paramsAuditInfo);
            if (size != ids.length) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }

        //修改实收数据未已稽核
        if (StringUtils.isNotEmpty(rasAuditInfo.getReceivedId())) {
            String[] ids = StringUtils.split(rasAuditInfo.getReceivedId(), ",");
            paramsAuditInfo.put("ids", ids);
            int size = rasReceivedService.autoOpAuditStatusControl(paramsAuditInfo);
            if (size != ids.length) {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
        //进行消息提醒操作
        if (RasConstant.RAS_STATUS_YES.equals(String.valueOf(rasAuditInfo.getIsMsg()))) {
            notificationService.send(rasAuditInfo.getId(), rasAuditInfo.getCorpId(), NotificationConstant.RAS.RAS_END_EXCEED_MONEY, rasAuditInfo);
        }
        //添加日志
        rasLogService.saveRasLog(rasAuditInfo.getId(), logType, logMsg, auditInfoVO.getUser());
        return true;
    }

    /**
     * 现金差异稽核需要进行消息提醒判断操作
     *
     * @param rasAuditInfo
     */
    public void sendMsgOp(RasAuditInfo rasAuditInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", rasAuditInfo.getShopId());
        List<RasShop> shopList = rasShopService.getRasShopByParams(params);
        if (shopList != null && shopList.size() > 0) {
            RasShop shop = shopList.get(0);
            rasAuditInfo.setEcMoney(shop.getEcMoney());
            if (rasAuditInfo.getEcMoney() != null
                    && rasAuditInfo.getLessMoney() != null &&
                    rasAuditInfo.getLessMoney().doubleValue() != rasAuditInfo.getEcMoney().doubleValue()) {
                rasAuditInfo.setShopName(shop.getName());
                rasAuditInfo.setCorpId(shop.getCorpId());
                if (rasAuditInfo.getLessMoney().doubleValue() > rasAuditInfo.getEcMoney().doubleValue()) {
                    rasAuditInfo.setEcInfo(RasConstant.RAS_AUDIT_SENDMSG_ECINFO_FEW);
                    rasAuditInfo.setLeeEcMoney(rasAuditInfo.getLessMoney().subtract(rasAuditInfo.getEcMoney()));
                } else {
                    rasAuditInfo.setEcInfo(RasConstant.RAS_AUDIT_SENDMSG_ECINFO_MORE);
                    rasAuditInfo.setLeeEcMoney(rasAuditInfo.getLessMoney().subtract(rasAuditInfo.getEcMoney()).multiply(new BigDecimal(-1)));
                }
                rasAuditInfo.setIsMsg(Integer.parseInt(RasConstant.RAS_STATUS_YES));
            } else {
                rasAuditInfo.setIsMsg(Integer.parseInt(RasConstant.RAS_STATUS_NO));
            }
        }
    }

    /**
     * 通过稽核中间表写入稽核表
     */
    public void saveAuditInfoByAuditInter(Map<String, Object> params) {
    	params.put("type", RasConstant.RAS_AUDIT_TYPE_0);
    	params.put("size", 1);
    	params.put("lessMoney", new BigDecimal(0));
    	params.put("status", RasConstant.RAS_AUDIT_STATUS_3);
    	rasAuditDao.saveAuditInfoByAuditInter(params);
    }
}