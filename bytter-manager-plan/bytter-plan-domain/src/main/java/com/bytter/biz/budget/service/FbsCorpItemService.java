package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsCorpItemDao;
import com.bytter.biz.budget.entity.*;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.vo.SimpleFbsItemVo;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsCorpItemVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.OrgantypeModuleDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: FBS_CORP_ITEM
* @author onlineGenerator
* @date 2017-07-11 10:46:42
* @version 1.0
*
*/
@Service("fbsCorpItemService")
@Transactional
public class FbsCorpItemService extends BaseService<FbsCorpItem,String> implements ICorpDeleteVerifyService {
    @Autowired
    private FbsCorpItemService fbsCorpItemService;
    @Autowired
	private FbsCorpItemDao fbsCorpItemDao;
	@Autowired
	private FbsItemService fbsItemService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	@Autowired
	private FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
	@Autowired
	private FbsBudgetResolveYearService fbsBudgetResolveYearService;
	@Autowired
	private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@Resource
    private IMultiLangService multiLangServiceI;

    @DubboReference(check = false)
    private IOrgantypeService iOrgantypeService;



    //这个不大，之间放在类级别缓存
    private static List<FbsCorpItem> IS_ITEM_LIST=null;

    /**
     * 办理批量处理线程池
     */
    private static ExecutorService EXECUTOR_SERVICE = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(8));

    @Override
	public BaseDao<FbsCorpItem, String> getEntityDao() {
		return fbsCorpItemDao;
	}
	/**
	* 保存
	* @param
	*/
	public void saveCorpItem(String corpId,List<String> itemCodes, ICurrentUser user){
		//获取单位修改之前分配的科目
		List<FbsItemVO> list= this.getAllItemByCorpIdNotSort(corpId);
		HashMap<String, String> itemMap = null;
		HashMap<String, FbsItem> newItemMap = null;
		if(!EmptyUtils.isEmptyList(list)){
			itemMap = new HashMap<String, String>();
			for(FbsItemVO item : list){
				itemMap.put(item.getItemCode(), item.getItemCode());
			}
		}
		//删除单位科目
 		fbsCorpItemDao.deleteByCorpId(corpId);
		if(StringUtils.isNotEmpty(itemCodes.get(0))){
			newItemMap = new HashMap<String, FbsItem>();
			FbsCorpItem entity = null;
			for(String itemCode : itemCodes){
				if(itemMap!=null && itemMap.size()>0 && !itemMap.containsKey(itemCode)){
					newItemMap.put(itemCode, fbsItemService.getFbsItemByCode(itemCode));
				}
				entity = new FbsCorpItem();
				entity.setCorpId(corpId);
				entity.setItemCode(itemCode);
				entity.setValidSign("1");
				entity.setCreateBy(user.getLoginName());
				entity.setCreateDate(DateUtils.getDate());
				super.saveEntity(entity);
			}
		}
		//新增科目初始化计划上报
		if(!EmptyUtils.isEmpty(newItemMap)){
			this.initBudgetDtl(corpId, newItemMap, user);
		}
 	}
    /**
     * 批量保存
     *
     * @param
     */
    public void saveCorpItem(List<String> corpIds, List<String> itemCodes, ICurrentUser user) {
        //批量查询itemCodes
        //获取单位分配的科目 按单位分组
        List<SimpleFbsItemVo> items = fbsCorpItemDao.getAllItemByCorpIdExecutir(corpIds);
        Map<String, Set<String>> existItemCodeMap = new HashMap<>();
        //按照单位分组
        for (SimpleFbsItemVo item : items) {
            Set<String> corpItemCodes;
            if (existItemCodeMap.containsKey(item.getCorpId())) {
                corpItemCodes = existItemCodeMap.get(item.getCorpId());
            } else {
                corpItemCodes = new HashSet<>();
                existItemCodeMap.put(item.getCorpId(), corpItemCodes);
            }
            corpItemCodes.add(item.getItemCode());
        }
        items.clear();
        items = null;
        //存在分配科目删除数据
        if (!existItemCodeMap.isEmpty()) {
            fbsCorpItemDao.deleteByCorpId(corpIds);
        }
        java.sql.Date now = new java.sql.Date(new Date().getTime());
        //存放单位需要初始化的新科目itemCode 按单位分组
        Map<String, List<String>> newItemMap = new HashMap<>();
        //待增加科目
        List<FbsCorpItem> newCorpItems = new ArrayList<>();

        for (String corpId : corpIds) {
            Set<String> corpExistItems = existItemCodeMap.get(corpId);
            //存放单位新科目 用于历史数据初始化
            List<String> newItemList = new ArrayList<>();
            for (String itemCode : itemCodes) {
                if (EmptyUtils.isNotEmpty(itemCode)) {
                    //当旧的科目不包含新科目某个科目 证明需要历史数据初始化
                    if (EmptyUtils.isNotEmpty(corpExistItems) && !corpExistItems.contains(itemCode)) {
                        newItemList.add(itemCode);
                    }
                    FbsCorpItem fbsCorpItem = new FbsCorpItem();

                    fbsCorpItem.setId(UUID.randomUUID().toString().replace("-", ""));
                    fbsCorpItem.setValidSign("1");
                    fbsCorpItem.setCreateDate(new Date());
                    fbsCorpItem.setCorpId(corpId);
                    fbsCorpItem.setItemCode(itemCode);

                    newCorpItems.add(fbsCorpItem);
                    if (newCorpItems.size() > 50000) {
                        saveBatch(newCorpItems);
                    }
                }
            }
            newItemMap.put(corpId, newItemList);

        }
        //循环结束保存一次数据
        if (EmptyUtils.isNotEmpty(newCorpItems)) {
            saveBatch(newCorpItems);
            // fbsCorpItemDao.batchJdbcSave(newCorpItems);
        }
        //清理缓存重新造数据
        IS_ITEM_LIST=null;

        //开始多线程执行计划上报
        final ICurrentUser iCurrentUser = user;
        //线程执行结果集合
        final Map<String, List<String>> finalItemMap = newItemMap;
        for (String str : corpIds) {
            final String corpId = str;
            //没有需要初始化的单位科目
            final List<String> newItemCodes = finalItemMap.get(corpId);
            if (EmptyUtils.isEmptyList(newItemCodes)) {
                continue;
            }
            EXECUTOR_SERVICE.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<SimpleFbsItemVo> simpleFbsItemVo = fbsItemService.getSimpleFbsItemVo(newItemCodes);
                        //新增科目初始化计划上报
                        fbsCorpItemService.initBudgetDtl(corpId, simpleFbsItemVo, iCurrentUser);
                    } catch (Exception e) {
                        logger.error("资金计划历史数据初始化异常单位{} 科目code{}", corpId, JsonUtils.toJSONString(newItemCodes));
                    }
                }
            });
        }
    }


    public void saveBatch(List<FbsCorpItem> fbsCorpItems) {
        fbsCorpItemDao.batchJdbcSave(fbsCorpItems);
        fbsCorpItems.clear();
    }


    /**
	 * 初始化计划明细
	 * @param corpId
	 * @param itemMap
	 */
	public void initBudgetDtl(String corpId, HashMap<String, FbsItem> itemMap, ICurrentUser user){
		//获取单位上报数据
		List<FbsBudget> budgetList = new ArrayList<FbsBudget>();
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
		String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
		String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
		String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		//季计划上报模式:1,季固定;2,季滚动;
		String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
		//周计划上报模式:1,固定;2,周滚动;
		String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//周计划滚动;
		int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
		if(!CommonConstant.NO.equals(ycrl)){
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_YEAR);//年计划
			List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_YEAR, fbsBudgetPeriod.getBeginDate());
			if(!EmptyUtils.isEmptyList(budgetTimeList)){
				budgetList.addAll(budgetTimeList);
			}
		}
		if(!CommonConstant.NO.equals(qcrl)){
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_QUARTER);//季计划
			List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_QUARTER, fbsBudgetPeriod.getBeginDate());
			if(!EmptyUtils.isEmptyList(budgetTimeList)){
				budgetList.addAll(budgetTimeList);
			}
			if(BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)){
				List<FbsBudget> budgetTimeMonthList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetPeriod.getBeginDate(),fbsBudgetPeriod.getEndDate());
				if(!EmptyUtils.isEmptyList(budgetTimeMonthList)){
					budgetList.addAll(budgetTimeMonthList);
				}
				if(qimodeRoll >0){
					Date begin = DateUtils.getDate();
					for (int i = 1; i < mimodeRoll; i++) {
						begin = DateUtils.addMonth(begin,3);
						FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_QUARTER);//季计划
						List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_QUARTER, nextFbsBudgetPeriod.getBeginDate());
						if(!EmptyUtils.isEmptyList(nextbudgetTimeList)){
							budgetList.addAll(nextbudgetTimeList);
						}
					}
				}
			}
		}
		if(!CommonConstant.NO.equals(mcrl)){
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_MONTH);//月计划
			List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetPeriod.getBeginDate());
			if(!EmptyUtils.isEmptyList(budgetTimeList)){
				budgetList.addAll(budgetTimeList);
			}
			if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
				List<FbsBudget> budgetTimeweekList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetPeriod.getBeginDate(),fbsBudgetPeriod.getEndDate());
				if(!EmptyUtils.isEmptyList(budgetTimeweekList)){
					budgetList.addAll(budgetTimeweekList);
				}
				if(mimodeRoll >0){
					Date begin = DateUtils.getDate();
					for (int i = 1; i < mimodeRoll; i++) {
						begin = DateUtils.addMonth(begin,1);
						FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_MONTH);//月计划
						List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, nextFbsBudgetPeriod.getBeginDate());
						if(!EmptyUtils.isEmptyList(nextbudgetTimeList)){
							budgetList.addAll(nextbudgetTimeList);
						}
					}
				}
			}
		}
		if(!CommonConstant.NO.equals(wcrl)){
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_WEEK);//周计划
			List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetPeriod.getBeginDate());
			if(!EmptyUtils.isEmptyList(budgetTimeList)){
				budgetList.addAll(budgetTimeList);
			}
			if(BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)){
				List<FbsBudget> budgetTimedayList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetPeriod.getBeginDate(),fbsBudgetPeriod.getEndDate());
				if(!EmptyUtils.isEmptyList(budgetTimedayList)){
					budgetList.addAll(budgetTimedayList);
				}
				if(wimodeRoll >0){
					Date begin = DateUtils.getDate();
					for (int i = 1; i < mimodeRoll; i++) {
						begin = DateUtils.addWeek(begin,1);
						FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_WEEK);//周计划
						List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, nextFbsBudgetPeriod.getBeginDate());
						if(!EmptyUtils.isEmptyList(nextbudgetTimeList)){
							budgetList.addAll(nextbudgetTimeList);
						}
					}
				}
			}
		}
		if(!CommonConstant.NO.equals(dcrl)){
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_DAY);//天计划
			List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetPeriod.getBeginDate());
			if(!EmptyUtils.isEmptyList(budgetTimeList)){
				budgetList.addAll(budgetTimeList);
			}
		}
		//年计划上报模式:0,年固定;1,年分解
		String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
		if(!EmptyUtils.isEmptyList(budgetList)){
			for(FbsBudget budget : budgetList){
				for(Map.Entry<String, FbsItem> entry : itemMap.entrySet()){
                    FbsBudgetDtlAbs abs = fbsBudgetDtlAbsService.getByBudgetId(budget.getId(),entry.getKey(),budget.getTimeType());
                    if(EmptyUtils.isEmpty(abs)) {
                        FbsBudgetDtlAbs dtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
                        dtl.setBudgetId(budget.getId());
                        dtl.setItemCode(entry.getKey());
                        dtl.setItemName(entry.getValue().getItemName());
                        dtl.setbMoney(BigDecimal.ZERO);
                        dtl.setBsuperMoney(BigDecimal.ZERO);
                        dtl.setBtempMoney(BigDecimal.ZERO);
                        fbsBudgetDtlAbsService.save(dtl);
                    }
				}
				//获取追加信息
				List<FbsBudgetSuperadd> superBudgetList = fbsBudgetSuperaddService.listByBudgetId(budget.getId());
				if(!EmptyUtils.isEmptyList(superBudgetList)){
					for(FbsBudgetSuperadd superBudget : superBudgetList){
						for(Map.Entry<String, FbsItem> entry : itemMap.entrySet()){
							FbsBudgetSuperaddDetail dtl = new FbsBudgetSuperaddDetail();
							dtl.setSuperaddId(superBudget.getId());
							dtl.setItemCode(entry.getKey());
							dtl.setItemName(entry.getValue().getItemName());
							dtl.setBsuperMoney(BigDecimal.ZERO);
							fbsBudgetSuperaddDetailService.save(dtl);
						}
					}
				}
				//年分解初始化年分解明细
				if(BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode) && BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType())){
					for(Map.Entry<String, FbsItem> entry : itemMap.entrySet()){
						FbsBudgetResolveYear year = new FbsBudgetResolveYear();
						year.setBudgetId(budget.getId());
						year.setItemCode(entry.getKey());
						year.setItemName(entry.getValue().getItemName());
						year.setBMoney(BigDecimal.ZERO);
						year.setBMoney1(BigDecimal.ZERO);
						year.setBMoney2(BigDecimal.ZERO);
						year.setBMoney3(BigDecimal.ZERO);
						year.setBMoney4(BigDecimal.ZERO);
						year.setBMoney5(BigDecimal.ZERO);
						year.setBMoney6(BigDecimal.ZERO);
						year.setBMoney7(BigDecimal.ZERO);
						year.setBMoney8(BigDecimal.ZERO);
						year.setBMoney9(BigDecimal.ZERO);
						year.setBMoney10(BigDecimal.ZERO);
						year.setBMoney11(BigDecimal.ZERO);
						year.setBMoney12(BigDecimal.ZERO);
						year.setCreateBy(user.getLoginName());
						year.setCreateDate(DateUtils.getDate());
						fbsBudgetResolveYearService.saveOrUpdate(year);
					}
				}
				//月分解初始化月分解明细
				if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode) && BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())){
					for(Map.Entry<String, FbsItem> entry : itemMap.entrySet()){
						FbsBudgetResolveMonth month = new FbsBudgetResolveMonth();
						month.setBudgetId(budget.getId());
						month.setItemCode(entry.getKey());
						month.setItemName(entry.getValue().getItemName());
						month.setBMoney(BigDecimal.ZERO);
						month.setBMoney1(BigDecimal.ZERO);
						month.setBMoney2(BigDecimal.ZERO);
						month.setBMoney3(BigDecimal.ZERO);
						month.setBMoney4(BigDecimal.ZERO);
						month.setBMoney5(BigDecimal.ZERO);
						month.setBMoney6(BigDecimal.ZERO);
						month.setCreateBy(user.getLoginName());
						month.setCreateDate(DateUtils.getDate());
						fbsBudgetResolveMonthService.saveOrUpdate(month);
					}
				}
			}
		}
	}
	
	/**
	 * 根据单位id 找 所属单位科目列表
	 * @param corpId
	 * @param
	 * @return
	 */
	public List<FbsItemVO> getAllItemByCorpId(String corpId){
		List<FbsItemVO> list= fbsCorpItemDao.getAllItemByCorpId(corpId);
		return fbsItemService.getListSort(list, null);
	}
	/**
	 * 根据单位id 找 所属单位科目列表
	 * @param corpId
	 * @return
	 */
	public List<FbsItemVO> getAllItemByCorpIdNotSort(String corpId){
		List<FbsItem> list= fbsCorpItemDao.getAllItemByCorpIdNotVO(corpId);
		List<FbsItemVO> fbsItemVOList = BeanUtils.copyListProperties(list, FbsItemVO.class);
		return fbsItemVOList;
	}
 	/**
	 * 重复code过滤
	 * @param list
	 * @return
	 */
	private List<String> getListNoRepeat(List<FbsCorpItemVO> list){
		List<String> codes=new ArrayList<String>();
		for(FbsCorpItemVO vo : list){
			if(!codes.contains(vo.getItemCode())) {
				codes.add(vo.getItemCode());
			}
			if(StringUtils.isNotEmpty(vo.getSuperItemCodes())) {
				String[] superCodes=vo.getSuperItemCodes().split(",");
				for (String code : superCodes) {
					if(!codes.contains(code)) {
						codes.add(code);
					}
				}
			}
		}
		return codes;
	}
	
	/**
	 *  根据层级结构排序，组装 tree 结构
	 * @param list
	 * @return
	 */
	public List<FbsCorpItemVO> getListSort(List<FbsCorpItemVO> list){
		List<FbsCorpItemVO> root=new ArrayList<FbsCorpItemVO>();
		List<FbsCorpItemVO> subList=new ArrayList<FbsCorpItemVO>();
		for(FbsCorpItemVO fbs : list){
			if(StringUtils.isEmpty(fbs.getSuperItemCode())){
				root.add(fbs);
			}else{
				subList.add(fbs);
			}
		}
		List<FbsCorpItemVO> allList=new ArrayList<FbsCorpItemVO>();
		for(FbsCorpItemVO fbs : root) {
			getListSort(fbs, subList,allList);
		}
		return allList;
	}
	
	/**
	 * 根据层级结构排序，组装 tree 结构
	 * @param fbsItemVO
	 * @param subList
	 * @param allList
	 */
	public void getListSort(FbsCorpItemVO fbsItemVO, List<FbsCorpItemVO> subList, List<FbsCorpItemVO> allList){
		allList.add(fbsItemVO);
		for(FbsCorpItemVO sub : subList){
			if(fbsItemVO.getItemCode().equals(sub.getSuperItemCode())){
				getListSort(sub,subList,allList);
			}
		}
	}
	
	/**
	 * 根据单位id查找科目实体
	 * @param corpId
	 * @return
	 */
	public List<FbsCorpItem> getItemCode(String corpId){
		return fbsCorpItemDao.getItemCode(corpId);
	}
	/**
	 * 求单位的状态  									
	 * @param
	 * @return 单位性质：
	 * -1函数运行错误
	 * 0 不汇总直接子单位的预算 且不被上级单位汇总预算，
	 * 1 要汇总直接子单位的预算 但不被上级单位汇总预算,
	 * 2 要汇总直接子单位的预算 且要被上级单位汇总预算,
	 * 3 要被上级单位汇总预算   但不汇总直接子单位的预算,
	 * @throws Exception 
	 */
	public int getCorpStatus(String corpId) throws Exception {
		int corpStatus = 0;
		if (StringUtils.isBlank(corpId)){
			return corpStatus;
		}
		boolean collectSelf = false;//要被上级单位汇总预算
		boolean collectSub = false;//要汇总直接子单位的预算
		CorpDTO parentCorp = iCorpService.getParentById(corpId);
		List<CorpDTO> corpChild = iCorpService.listUnderCorpByParentCorpId(corpId);
		int collectFlagOwn = this.fbsCorpBudgetService.collectFlagByCorpId(corpId) ? 1 : 0;//自身汇总标志
		int collectFlagParent = 0;
		if(!EmptyUtils.isEmpty(parentCorp)){
			collectFlagParent = this.fbsCorpBudgetService.collectFlagByCorpId(parentCorp.getId()) ? 1 : 0;//上级汇总标志
			if(1 == collectFlagParent){
				collectSelf = true;
			}
		}
		if(!EmptyUtils.isEmpty(corpChild) && corpChild.size() > 0){
			if(1 == collectFlagOwn){
				collectSub = true;
			}
		}
		if(collectSelf){
			if(collectSub){//有上级且有下级
				corpStatus = FbsCorpItemVO.COLLECT_BOTH;//要被上级单位汇总预算 且要汇总直接子单位的预算 2
			}else{//有上级但无下级
				corpStatus = FbsCorpItemVO.COLLECT_SELF;//要被上级单位汇总预算 但不汇总直接子单位的预算 3
			}
		}else {
			if(collectSub){//无上级但有下级
				//不被上级单位汇总预算，但要汇总直接子单位的预算 1
				corpStatus = FbsCorpItemVO.COLLECT_SUBCORP;
			}else{//无上级且无下级
				//不被上级单位汇总预算, 且不汇总直接子单位的预算0
				corpStatus = FbsCorpItemVO.COLLECT_NEITHER;
			}
		}
		return corpStatus;
	}
	/**
     * 根据单位id获取已经分配科目
     * @param corpId
     * @param corpId
     * @return
     */
  	public List<FbsCorpItemVO> getAlreadyCorpItem(String corpId){
  		return fbsCorpItemDao.getAlreadyCorpItem(corpId);
  	}
    /**
     * 根据单位id获取已经分配科目
     *
     * @param corpIds
     * @return
     */
    public List<FbsCorpItemVO> getAlreadyCorpItem(List<String> corpIds) {
        return fbsCorpItemDao.getAlreadyCorpItem(corpIds);
    }
  	/**
  	 * 查找已分配科目的单位
  	 * @return
  	 */
	public List<FbsCorpItem> isItem(){
		List<FbsCorpItem> list = fbsCorpItemDao.isItem();
		return list;
	}
	/**
	 * 根据code判断是否存在使用记录
	 * @param itemCodes
	 * @return
	 */
	public Long getCountByCodes(List<String> itemCodes){
		return fbsCorpItemDao.getCountByCodes(itemCodes);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有分配资金计划科目，不允许删除！
			int num = this.fbsCorpItemDao.getCorpItemCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_ITEM,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DEL_ITEM_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}

    public Boolean fbscorpitemSave(String[] corpIds, List<String> itemCodes, ICurrentUser user) {
        OrgantypeModuleDTO organtypeModuleDTO = iOrgantypeService.getOrgantypeModule(SysConfigConstant.Module.FBS);
        Integer isValid = organtypeModuleDTO.getIsValid();
        if (corpIds.length < 1) {
            throw new ParameterException("单位参数缺少");
        }
        if (itemCodes.isEmpty()) {
            throw new ParameterException("科目参数缺少");
        }
        //本单位分配的科目 todo数据校验方式改成批量校验
        final HashMap<String, String> itemMap = new HashMap<String, String>();
        for (String itemCode : itemCodes) {
            itemMap.put(itemCode, itemCode);
        }
        List<CorpDTO> curCorp = null;
        //获取当前分配科目单位
        if (isValid == 1) {
            curCorp = iCorpService.listCorpDTOByCorpIdsForOrgant(Arrays.asList(corpIds), organtypeModuleDTO.getOrgtId());
        } else {
            curCorp = iCorpService.listCorpDTOByCorpIds(Arrays.asList(corpIds));
        }
        //判断单位是否需要汇总  --还有改进空间 获取单位科目
        corpIsSummary(curCorp, itemCodes, itemMap);
        //判断单位是否存在下级要汇总单位  继续改进
        corpIsChildSummary(curCorp, itemMap);
        //判断单位是否存已经上报的计划
        corpIsReport(curCorp, itemCodes);
        //存储
        saveCorpItem(Arrays.asList(corpIds), itemCodes, user);
        return true;
    }

    /**
     * 判断单位是否存已经上报的计划
     *
     * @return
     */
    private void corpIsReport(List<CorpDTO> curCorps, List<String> itemCodes) {
        List<String> ids=curCorps.stream().map(CorpDTO::getCorpId).collect(Collectors.toList());
        Map<String,Boolean> flagMap=fbsBudgetService.isExistBudget(ids);
        Map<String, List<FbsCorpItem>> itemMap=null;
        for (CorpDTO curCorp : curCorps) {
            //判断单位是否存已经上报的计划
            if (flagMap.get(curCorp.getCorpId())) {
                //获取单位原科目集合
                if (itemMap==null){
                    itemMap=getItemCodeMap(ids);
                }
                List<FbsCorpItem> itemcodeList = itemMap.get(curCorp.getCorpId());
                if (itemcodeList.size() > itemCodes.size()) {
                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_ALREADYEXIST, curCorp.getCode());
                }
            }
        }
    }

    /**
     * 判断单位是否存在下级要汇总单位
     *
     * @return
     */
    private void corpIsChildSummary(List<CorpDTO> curCorps, HashMap<String, String> itemMap) {
        List<String> ids=curCorps.stream().map(item->item.getId()).collect(Collectors.toList());
        //判断单位是否存在下级要汇总单位
        if (!fbsCorpBudgetService.getChildCollectFlagMap(ids)) {
            return;
        }
        List<FbsCorpBudgetDTO> blist = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(ids);
        Map<String, List<FbsCorpBudgetDTO>> bMap=blist.stream().collect(Collectors.groupingBy(FbsCorpBudgetDTO::getCorpParentId));
        if (CollectionUtils.isEmpty(bMap)){
            return ;
        }
        //下级级单位分配的科目
        Map<String, List<FbsCorpItem>> childMap = null;
        for (CorpDTO curCorp : curCorps) {
            List<FbsCorpBudgetDTO> list = bMap.get(curCorp.getId());
            if (EmptyUtils.isEmptyList(list)) {
                return;
            }
            for (FbsCorpBudgetDTO dto : list) {
                if (dto.getReportBudget() != 0 && dto.getCollectFlag() == 1) {
                    if (childMap == null) {
                        List<String> corpIds=curCorps.stream().map(CorpDTO::getCorpId).filter(str->{
                            if (StringUtils.isNotEmpty(str)){
                                return true;
                            }
                            return false;
                        }).collect(Collectors.toList());
                        childMap = getItemCodeMap(corpIds);
                    }
                    List<FbsCorpItem> childItemList = childMap.get(dto.getCorpId());
                    HashMap<String, String> childItemMap = new HashMap<String, String>();
                    if (childItemList==null){
                        return;
                    }
                    //下级级单位分配的科目
                    for (FbsCorpItem item : childItemList) {
                        childItemMap.put(item.getItemCode(), item.getItemCode());
                    }
                    for (Map.Entry<String, String> entry : childItemMap.entrySet()) {
                        if (!itemMap.containsKey(entry.getKey())) {
                            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, dto.getCorpCode(), curCorp.getCode());
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断单位是否需要汇总
     *
     * @return
     */
    private void corpIsSummary(List<CorpDTO> curCorps, List<String> itemCodes, HashMap<String, String> itemMap) {
        List<String> corpIds=curCorps.stream().filter(item->item.getId()!=null).map(CorpDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(corpIds)){
            return ;
        }
        Map<String, Boolean> flagMap = fbsCorpBudgetService.queryCollectFlagMap(corpIds);
        if (CollectionUtils.isEmpty(flagMap)){
            return ;
        }
        //获取上级单位已分配的科目,暂时为空，只有需要的时候再去查询
        Map<String, List<FbsCorpItem>> parentMap = null;
        for (CorpDTO curCorp : curCorps) {
            //判断单位是否需要汇总
            if (flagMap.get(curCorp.getId())) {
                //获取上级单位已分配的科目，只获取一次
                if (parentMap == null) {
                    List<String> parentIdCorpIds=curCorps.stream().filter(item->item.getParentId()!=null).map(CorpDTO::getParentId).collect(Collectors.toList());
                    parentMap = getItemCodeMap(parentIdCorpIds);
                }
                List<FbsCorpItem> parentItemList = parentMap.get(curCorp.getParentId());
                if (!EmptyUtils.isEmptyList(parentItemList)) {
                    if (parentItemList.size() < itemCodes.size()) {
                        throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), iCorpService.get(curCorp.getId()).getCode());
                    } else {
                        HashMap<String, String> parentItemMap = new HashMap<String, String>();
                        //上级单位分配的科目
                        for (FbsCorpItem item : parentItemList) {
                            parentItemMap.put(item.getItemCode(), item.getItemCode());
                        }
                        for (Map.Entry<String, String> entry : itemMap.entrySet()) {
                            if (!EmptyUtils.isBlank(entry.getKey()) && !parentItemMap.containsKey(entry.getKey())) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), iCorpService.get(curCorp.getId()).getCode());
                            }
                        }
                    }
                } else {
                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), iCorpService.get(curCorp.getId()).getCode());
                }
            }
        }
    }

    /**
     * 根据单位id查找科目实体
     *
     * @param corpIds
     * @return
     */
    public Map<String, List<FbsCorpItem>> getItemCodeMap(List<String> corpIds) {
        //获取上级单位已分配的科目
        List<FbsCorpItem> parentList = fbsCorpItemDao.getItemCode(corpIds);
        Map<String, List<FbsCorpItem>> parentMap = parentList.stream().collect(Collectors.groupingBy(FbsCorpItem::getCorpId));
        return parentMap;
    }

    /**
     * 初始化计划明细
     *
     * @param corpId
     * @param simpleFbsItemVos
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initBudgetDtl(String corpId, List<SimpleFbsItemVo> simpleFbsItemVos, ICurrentUser user) {
        //获取单位上报数据
        List<FbsBudget> budgetList = new ArrayList<FbsBudget>();
        //是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
        String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
        String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
        String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
        String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
        String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
        //月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
        String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
        //季计划上报模式:1,季固定;2,季滚动;
        String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //季计划滚动;
        int qimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
        //月滚动模式
        int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
        //周计划滚动;
        int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        if (!CommonConstant.NO.equals(ycrl)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_YEAR);//年计划
            List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_YEAR, fbsBudgetPeriod.getBeginDate());
            if (!EmptyUtils.isEmptyList(budgetTimeList)) {
                budgetList.addAll(budgetTimeList);
            }
        }
        if (!CommonConstant.NO.equals(qcrl)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_QUARTER);//季计划
            List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_QUARTER, fbsBudgetPeriod.getBeginDate());
            if (!EmptyUtils.isEmptyList(budgetTimeList)) {
                budgetList.addAll(budgetTimeList);
            }
            if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
                List<FbsBudget> budgetTimeMonthList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
                if (!EmptyUtils.isEmptyList(budgetTimeMonthList)) {
                    budgetList.addAll(budgetTimeMonthList);
                }
                if (qimodeRoll > 0) {
                    Date begin = DateUtils.getDate();
                    for (int i = 1; i < mimodeRoll; i++) {
                        begin = DateUtils.addMonth(begin, 3);
                        FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_QUARTER);//季计划
                        List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_QUARTER, nextFbsBudgetPeriod.getBeginDate());
                        if (!EmptyUtils.isEmptyList(nextbudgetTimeList)) {
                            budgetList.addAll(nextbudgetTimeList);
                        }
                    }
                }
            }
        }
        if (!CommonConstant.NO.equals(mcrl)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_MONTH);//月计划
            List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetPeriod.getBeginDate());
            if (!EmptyUtils.isEmptyList(budgetTimeList)) {
                budgetList.addAll(budgetTimeList);
            }
            if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode) || BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
                List<FbsBudget> budgetTimeweekList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
                if (!EmptyUtils.isEmptyList(budgetTimeweekList)) {
                    budgetList.addAll(budgetTimeweekList);
                }
                if (mimodeRoll > 0) {
                    Date begin = DateUtils.getDate();
                    for (int i = 1; i < mimodeRoll; i++) {
                        begin = DateUtils.addMonth(begin, 1);
                        FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_MONTH);//月计划
                        List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, nextFbsBudgetPeriod.getBeginDate());
                        if (!EmptyUtils.isEmptyList(nextbudgetTimeList)) {
                            budgetList.addAll(nextbudgetTimeList);
                        }
                    }
                }
            }
        }
        if (!CommonConstant.NO.equals(wcrl)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_WEEK);//周计划
            List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetPeriod.getBeginDate());
            if (!EmptyUtils.isEmptyList(budgetTimeList)) {
                budgetList.addAll(budgetTimeList);
            }
            if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
                List<FbsBudget> budgetTimedayList = fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
                if (!EmptyUtils.isEmptyList(budgetTimedayList)) {
                    budgetList.addAll(budgetTimedayList);
                }
                if (wimodeRoll > 0) {
                    Date begin = DateUtils.getDate();
                    for (int i = 1; i < mimodeRoll; i++) {
                        begin = DateUtils.addWeek(begin, 1);
                        FbsBudgetPeriodVO nextFbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(begin, BudgetConstant.TIME_TYPE_WEEK);//周计划
                        List<FbsBudget> nextbudgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, nextFbsBudgetPeriod.getBeginDate());
                        if (!EmptyUtils.isEmptyList(nextbudgetTimeList)) {
                            budgetList.addAll(nextbudgetTimeList);
                        }
                    }
                }
            }
        }
        if (!CommonConstant.NO.equals(dcrl)) {
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_DAY);//天计划
            List<FbsBudget> budgetTimeList = fbsBudgetService.getCorpBudget(corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetPeriod.getBeginDate());
            if (!EmptyUtils.isEmptyList(budgetTimeList)) {
                budgetList.addAll(budgetTimeList);
            }
        }
        //年计划上报模式:0,年固定;1,年分解
        String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
        if (!EmptyUtils.isEmptyList(budgetList)) {
            for (FbsBudget budget : budgetList) {
                for (SimpleFbsItemVo entry : simpleFbsItemVos) {
                    FbsBudgetDtlAbs abs = fbsBudgetDtlAbsService.getByBudgetId(budget.getId(), entry.getItemCode(), budget.getTimeType());
                    if (EmptyUtils.isEmpty(abs)) {
                        FbsBudgetDtlAbs dtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
                        dtl.setBudgetId(budget.getId());
                        dtl.setItemCode(entry.getItemCode());
                        dtl.setItemName(entry.getItemName());
                        dtl.setbMoney(BigDecimal.ZERO);
                        dtl.setBsuperMoney(BigDecimal.ZERO);
                        dtl.setBtempMoney(BigDecimal.ZERO);
                        fbsBudgetDtlAbsService.save(dtl);
                    }
                }
                //获取追加信息
                List<FbsBudgetSuperadd> superBudgetList = fbsBudgetSuperaddService.listByBudgetId(budget.getId());
                if (!EmptyUtils.isEmptyList(superBudgetList)) {
                    for (FbsBudgetSuperadd superBudget : superBudgetList) {
                        for (SimpleFbsItemVo entry : simpleFbsItemVos) {
                            FbsBudgetSuperaddDetail dtl = new FbsBudgetSuperaddDetail();
                            dtl.setSuperaddId(superBudget.getId());
                            dtl.setItemCode(entry.getItemCode());
                            dtl.setItemName(entry.getItemName());
                            dtl.setBsuperMoney(BigDecimal.ZERO);
                            fbsBudgetSuperaddDetailService.save(dtl);
                        }
                    }
                }
                //年分解初始化年分解明细
                if (BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode) && BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType())) {
                    for (SimpleFbsItemVo entry : simpleFbsItemVos) {
                        FbsBudgetResolveYear year = new FbsBudgetResolveYear();
                        year.setBudgetId(budget.getId());
                        year.setItemCode(entry.getItemCode());
                        year.setItemName(entry.getItemName());
                        year.setBMoney(BigDecimal.ZERO);
                        year.setBMoney1(BigDecimal.ZERO);
                        year.setBMoney2(BigDecimal.ZERO);
                        year.setBMoney3(BigDecimal.ZERO);
                        year.setBMoney4(BigDecimal.ZERO);
                        year.setBMoney5(BigDecimal.ZERO);
                        year.setBMoney6(BigDecimal.ZERO);
                        year.setBMoney7(BigDecimal.ZERO);
                        year.setBMoney8(BigDecimal.ZERO);
                        year.setBMoney9(BigDecimal.ZERO);
                        year.setBMoney10(BigDecimal.ZERO);
                        year.setBMoney11(BigDecimal.ZERO);
                        year.setBMoney12(BigDecimal.ZERO);
                        year.setCreateBy(user.getLoginName());
                        year.setCreateDate(DateUtils.getDate());
                        fbsBudgetResolveYearService.saveOrUpdate(year);
                    }
                }
                //月分解初始化月分解明细
                if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode) && BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())) {
                    for (SimpleFbsItemVo entry : simpleFbsItemVos) {
                        FbsBudgetResolveMonth month = new FbsBudgetResolveMonth();
                        month.setBudgetId(budget.getId());
                        month.setItemCode(entry.getItemCode());
                        month.setItemName(entry.getItemName());
                        month.setBMoney(BigDecimal.ZERO);
                        month.setBMoney1(BigDecimal.ZERO);
                        month.setBMoney2(BigDecimal.ZERO);
                        month.setBMoney3(BigDecimal.ZERO);
                        month.setBMoney4(BigDecimal.ZERO);
                        month.setBMoney5(BigDecimal.ZERO);
                        month.setBMoney6(BigDecimal.ZERO);
                        month.setCreateBy(user.getLoginName());
                        month.setCreateDate(DateUtils.getDate());
                        fbsBudgetResolveMonthService.saveOrUpdate(month);
                    }
                }
            }
        }
    }

}
