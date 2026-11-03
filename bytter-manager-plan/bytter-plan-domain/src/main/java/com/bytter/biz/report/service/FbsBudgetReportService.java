package com.bytter.biz.report.service;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.biz.budget.dao.FbsCollectCorpDao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.entity.FbsBudgetResolveMonth;
import com.bytter.biz.budget.entity.FbsBudgetResolveYear;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.budget.vo.*;
import com.bytter.biz.report.dao.FbsBudgetReportDao;
import com.bytter.biz.report.vo.FbsBudgetDetailReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Service("fbsBudgetReportService")
@Transactional
public class FbsBudgetReportService extends BaseService<FbsBudget, String> {
	@Autowired
	private FbsBudgetReportDao fbsBudgetReportDao;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsBudgetResolveYearService fbsBudgetResolveYearService;
	@Autowired
	private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFbsItemService iFbsItemService;
	@Autowired
	private FbsItemService fbsItemService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsCollectCorpDao fbsCollectCorpDao;
	@DubboReference(check = false)
	private IDictService dictService;
	@Override
	public BaseDao<FbsBudget, String> getEntityDao() {
		return fbsBudgetReportDao;
	}
	public DataTablesOutput dataTableReport(DataTablesInput dataTablesInput,ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource = "fbs:report:fbsBudget:query";
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "fbs.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput dataTablesOutput = this.fbsBudgetReportDao.dataTableReport(dataTablesInput,sql.toString());
		return dataTablesOutput;
	}
	/**
	 * 根据查询条件查询预算信息
	 *
	 * @param timeType  类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	public List<FbsBudgetVO> listFbsBudget(String timeType, String corpId, Date beginDate, Date endDate) {
		return this.fbsBudgetReportDao.listFbsBudget(timeType, corpId, beginDate, endDate);
	}
	/**
	 * 根据查询条件查询预算信息
	 *
	 * @param timeType  类型
	 * @param corpIds   单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	public List<FbsBudgetVO> listFbsBudget(String timeType, String[] corpIds, Date beginDate, Date endDate) {
		String corpId = SqlUtil.getInSql(corpIds, "b.corpId", true);
		return this.fbsBudgetReportDao.listFbsBudget(timeType, corpId, beginDate, endDate);
	}
	/**
	 * 初始化固定预算报表数据
	 *
	 * @param timeType       类型
	 * @param corpId         单位ID
	 * @param seachBeginDate 开始日期
	 * @param seachEndDate   结束日期
	 * @return
	 */
	public Map<String, Object> getFbsBudget(String timeType, String corpId, Date seachBeginDate, Date seachEndDate) {
		StopWatch stopWatch = new StopWatch("getFbsBudget");
		stopWatch.start("listFbsBudget");
		List<FbsBudgetVO> list = this.listFbsBudget(timeType, corpId, seachBeginDate, seachEndDate);
		stopWatch.stop();
		Map<String, Object> map = new HashMap<>();
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		try {
			// 日期
			Map<Object, Object> timeMap = new TreeMap<Object, Object>(
					new Comparator<Object>() {
						@Override
						public int compare(Object obj1, Object obj2) {
							// 降序排序
							return obj1.toString().compareTo(obj2.toString());
						}
					});
			// 科目
			List<FbsBudgetDtlAbsVO> itemList = new ArrayList<>();
			stopWatch.start("fbsItemService.getAllItemMap");
			HashMap<String, FbsItemVO> itemMap = fbsItemService.getAllItemMapV2();
			stopWatch.stop();
			// 明细金额
			Map<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<>();
			stopWatch.start("fbsBudgetDtlAbsService.listByBudgetId for loop");
			Map<String,List<String>> timeType2Ids= Maps.newHashMap();
			for (FbsBudgetVO entity : list) {
				List<String> ids=timeType2Ids.get(entity.getTimeType());
				if(EmptyUtils.isEmpty(ids)){
					ids=Lists.newArrayList();
					timeType2Ids.put(entity.getTimeType(),ids);
				}
				ids.add(entity.getId());
			}
			Map<String,List<FbsBudgetDtlAbsVO>> idTimeType2Dtls = Maps.newHashMap();
			for(String timeTypeKey:timeType2Ids.keySet()){
				Map<String,List<FbsBudgetDtlAbsVO>> id2DtlsForOneTimeType=fbsBudgetDtlAbsService.listByBudgetIds(timeType2Ids.get(timeTypeKey),timeTypeKey);
				for(String id:id2DtlsForOneTimeType.keySet()){
					idTimeType2Dtls.put(id+CommonConstant.COLON_SPLIT+timeTypeKey,id2DtlsForOneTimeType.get(id));
				}
			}
			for (FbsBudgetVO entity : list) {
				//取计划的汇率
				BigDecimal rate = entity.getExchangeRate();
//				List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
				List<FbsBudgetDtlAbsVO> dtlList = idTimeType2Dtls.get(entity.getId()+CommonConstant.COLON_SPLIT+entity.getTimeType());
				if (!EmptyUtils.isEmptyList(dtlList)) {
					// 初始化科目
					if (EmptyUtils.isEmptyList(itemList)) {
						for (FbsBudgetDtlAbsVO dtlAbsVO : dtlList) {
							FbsItemVO vo = itemMap.get(dtlAbsVO.getItemCode());
							dtlAbsVO.setItem(vo);
							itemList.add(dtlAbsVO);
						}
					}
					// 初始化日期和明细金额
					for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList) {
						//根据汇率计算折合本位币金额
						fbsBudgetDtlAbsVO.setbMoney(fbsBudgetDtlAbsVO.getbMoney().multiply(rate));
						fbsBudgetDtlAbsVO.setBtempMoney(fbsBudgetDtlAbsVO.getBtempMoney().multiply(rate));
						fbsBudgetDtlAbsVO.setBsuperMoney(fbsBudgetDtlAbsVO.getBsuperMoney().multiply(rate));
						if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), fbsBudgetDtlAbsVO);
							timeMap.put(DateUtils.getYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getQuarterOfYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), fbsBudgetDtlAbsVO);
							timeMap.put(DateUtils.getQuarterOfYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getMonth(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), fbsBudgetDtlAbsVO);
							timeMap.put(DateUtils.getMonth(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getWeekOfYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), fbsBudgetDtlAbsVO);
							timeMap.put(DateUtils.getWeekOfYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else {
							dtlMap.put(DateUtils.formatDate(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), fbsBudgetDtlAbsVO);
							timeMap.put(DateUtils.formatDate(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()));
						}
					}
				}
			}
			stopWatch.stop();
			stopWatch.start("handle FbsBudgetDtlAbsVO list");
			for (FbsBudgetDtlAbsVO itemVO : itemList) {
				// 不存在的预算数据，金额设置为0
				for (Map.Entry<Object, Object> time : timeMap.entrySet()) {
					String key = time.getKey() + BudgetConstant.BUDGET_SPLIT + itemVO.getItemCode();
					if (!dtlMap.containsKey(key)) {
						FbsBudgetDtlAbsVO vo = new FbsBudgetDtlAbsVO();
						vo.setbMoney(BigDecimal.ZERO);
						vo.setBsuperMoney(BigDecimal.ZERO);
						vo.setBtempMoney(BigDecimal.ZERO);
						dtlMap.put(key, vo);
					}
				}
			}
			stopWatch.stop();
			map.put("timeMap", timeMap);
			map.put("itemList", itemList);
			map.put("dtlMap", dtlMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("--------------getFbsBudget all spends :"+stopWatch.prettyPrint());
		return map;
	}
	
	public com.bytter.biz.budget.spi.vo.FbsItemVO transfer(FbsItemVO fbsItemVO){
		com.bytter.biz.budget.spi.vo.FbsItemVO vo = new com.bytter.biz.budget.spi.vo.FbsItemVO();
		BeanUtils.copyProperties(fbsItemVO, vo);
		return vo;
	}
	/**
	 * 初始化固定预算报表数据
	 *
	 * @param timeType       类型
	 * @param corpId         单位ID
	 * @param seachBeginDate 开始日期
	 * @param seachEndDate   结束日期
	 * @return
	 */
	public Map<String, Object> getFbsBudgetCollect(String timeType, String corpId, Date seachBeginDate, Date seachEndDate) {
		List<FbsBudgetVO> list = this.listFbsBudget(timeType, corpId, seachBeginDate, seachEndDate);
		FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(corpId);
		List<FbsBudgetVO> list1 = this.listFbsBudget(timeType, fbsCollectCorpVO.getId(), seachBeginDate, seachEndDate);
		Map<String, Object> map = new HashMap<>();
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		try {
			// 日期
			Map<Object, Object> timeMap = new TreeMap<Object, Object>(
					new Comparator<Object>() {
						@Override
						public int compare(Object obj1, Object obj2) {
							// 降序排序
							return obj1.toString().compareTo(obj2.toString());
						}
					});
			// 科目
			List<FbsBudgetDtlAbsVO> itemList = new ArrayList<>();
			HashMap<String, FbsItemVO> itemMap = fbsItemService.getAllItemMapV2();
			// 明细金额
			Map<String, Map<String,BigDecimal>> dtlMap = new HashMap<>();
			Map<String,List<String>> timeType2Ids= Maps.newHashMap();
			Map<String,List<String>> timeType3Ids= Maps.newHashMap();
			for (FbsBudgetVO entity : list) {
				List<String> ids=timeType2Ids.get(entity.getTimeType());
				if(EmptyUtils.isEmpty(ids)){
					ids=Lists.newArrayList();
					timeType2Ids.put(entity.getTimeType(),ids);
				}
				ids.add(entity.getId());
			}
			for (FbsBudgetVO entity : list1) {
				List<String> ids=timeType3Ids.get(entity.getTimeType());
				if(EmptyUtils.isEmpty(ids)){
					ids=Lists.newArrayList();
					timeType3Ids.put(entity.getTimeType(),ids);
				}
				ids.add(entity.getId());
			}
			Map<String,List<FbsBudgetDtlAbsVO>> idTimeType2Dtls = Maps.newHashMap();
			for(String timeTypeKey:timeType2Ids.keySet()){
				Map<String,List<FbsBudgetDtlAbsVO>> id2DtlsForOneTimeType=fbsBudgetDtlAbsService.listByBudgetIds(timeType2Ids.get(timeTypeKey),timeTypeKey);
				for(String id:id2DtlsForOneTimeType.keySet()){
					idTimeType2Dtls.put(id+CommonConstant.COLON_SPLIT+timeTypeKey,id2DtlsForOneTimeType.get(id));
				}
			}
			Map<String,List<FbsBudgetDtlAbsVO>> idTimeType3Dtls = Maps.newHashMap();
			for(String timeTypeKey:timeType3Ids.keySet()){
				Map<String,List<FbsBudgetDtlAbsVO>> id3DtlsForOneTimeType=fbsBudgetDtlAbsService.listByBudgetIds(timeType3Ids.get(timeTypeKey),timeTypeKey);
				for(String id:id3DtlsForOneTimeType.keySet()){
					idTimeType3Dtls.put(id+CommonConstant.COLON_SPLIT+timeTypeKey,id3DtlsForOneTimeType.get(id));
				}
			}
			for (int i=0;i<list.size();i++) {
				FbsBudgetVO entity = list.get(i);
				FbsBudgetVO entity1 = list1.get(i);
				//取计划的汇率
				BigDecimal rate = entity.getExchangeRate();
				List<FbsBudgetDtlAbsVO> dtlList = idTimeType2Dtls.get(entity.getId()+CommonConstant.COLON_SPLIT+entity.getTimeType());
				List<FbsBudgetDtlAbsVO> dtlList1 = idTimeType3Dtls.get(entity1.getId()+CommonConstant.COLON_SPLIT+entity.getTimeType());
				if (!EmptyUtils.isEmptyList(dtlList)) {
					// 初始化科目
					if (EmptyUtils.isEmptyList(itemList)) {
						for (FbsBudgetDtlAbsVO dtlAbsVO : dtlList) {
							FbsItemVO vo = itemMap.get(dtlAbsVO.getItemCode());
							dtlAbsVO.setItem(vo);
							itemList.add(dtlAbsVO);
						}
					}
					// 初始化日期和明细金额
					for (int j=0;j<dtlList.size();j++) {
						FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = dtlList.get(j);
						FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO1 = dtlList1.get(j);
						Map<String,BigDecimal> saveMap = new HashMap<>();
						//根据汇率计算折合本位币金额
						saveMap.put("bMoney",fbsBudgetDtlAbsVO.getbMoney().multiply(rate));
						saveMap.put("bTempMoney",fbsBudgetDtlAbsVO.getBtempMoney().multiply(rate));
						saveMap.put("bSuperMoney",fbsBudgetDtlAbsVO.getBsuperMoney().multiply(rate));
						saveMap.put("bMoney1",fbsBudgetDtlAbsVO1.getbMoney().multiply(rate));
						saveMap.put("bTempMoney1",fbsBudgetDtlAbsVO1.getBtempMoney().multiply(rate));
						saveMap.put("bSuperMoney1",fbsBudgetDtlAbsVO1.getBsuperMoney().multiply(rate));
						if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), saveMap);
							timeMap.put(DateUtils.getYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getQuarterOfYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), saveMap);
							timeMap.put(DateUtils.getQuarterOfYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getMonth(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), saveMap);
							timeMap.put(DateUtils.getMonth(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(entity.getTimeType())) {
							dtlMap.put(DateUtils.getWeekOfYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), saveMap);
							timeMap.put(DateUtils.getWeekOfYear(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()));
						} else {
							dtlMap.put(DateUtils.formatDate(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + fbsBudgetDtlAbsVO.getItemCode(), saveMap);
							timeMap.put(DateUtils.formatDate(entity.getBeginDate()), DateUtils.formatDate(entity.getBeginDate()));
						}
					}
				}
			}
			for (FbsBudgetDtlAbsVO itemVO : itemList) {
				// 不存在的预算数据，金额设置为0
				for (Map.Entry<Object, Object> time : timeMap.entrySet()) {
					String key = time.getKey() + BudgetConstant.BUDGET_SPLIT + itemVO.getItemCode();
					if (!dtlMap.containsKey(key)) {
						Map<String,BigDecimal> saveMap = new HashMap<>();
						saveMap.put("bMoney",BigDecimal.ZERO);
						saveMap.put("bTempMoney",BigDecimal.ZERO);
						saveMap.put("bSuperMoney",BigDecimal.ZERO);
						saveMap.put("bMoney1",BigDecimal.ZERO);
						saveMap.put("bTempMoney1",BigDecimal.ZERO);
						saveMap.put("bSuperMoney1",BigDecimal.ZERO);
						dtlMap.put(key, saveMap);
					}
				}
			}
			map.put("timeMap", timeMap);
			map.put("itemList", itemList);
			map.put("dtlMap", dtlMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 初始化年分解月，月分解周预算报表数据
	 *
	 * @param timeType       类型
	 * @param corpId         单位ID
	 * @param seachBeginDate 开始日期
	 * @param seachEndDate   结束日期
	 * @return
	 */
	public Map<String, Object> getFbsBudgetResolve(String timeType, String corpId, Date seachBeginDate, Date seachEndDate) {
		List<FbsBudgetVO> list = this.listFbsBudget(timeType, corpId, seachBeginDate, seachEndDate);
		Map<String, Object> map = new HashMap<>();
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		try {
			// 日期
			Map<Object, Object> timeMap = this.getResolveTimeMap(list);
			// 科目
			List itemList = new ArrayList();
			HashMap<String, FbsItemVO> itemMap = fbsItemService.getAllItemMap();
			// 明细金额
			Map<String, Object> dtlMap = new HashMap<>();
			//分解信息
			HashMap<String, List<FbsBudgetResolveYear>> resolveYearMap = new HashMap<>();
			HashMap<String, List<FbsBudgetResolveMonth>> resolveMonthMap = new HashMap<>();
			List<FbsBudgetPeriodVO> listPeriod = null;
			List<String> budgetIds = new ArrayList<>();
			for (FbsBudgetVO entity : list) {
				budgetIds.add(entity.getId());
			}
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
				resolveYearMap = this.fbsBudgetResolveYearService.mapByBudgetIds(budgetIds);
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
				listPeriod = fbsBudgetPeriodService.getWeekByMonth(seachBeginDate);
				resolveMonthMap = this.fbsBudgetResolveMonthService.mapByBudgetIds(budgetIds);
			}
			for (FbsBudgetVO entity : list) {
				//取计划的汇率
				BigDecimal rate = entity.getExchangeRate();
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
					List<FbsBudgetResolveYear> listResolveYearBudget = resolveYearMap.get(entity.getId());
					if (EmptyUtils.isEmptyList(listResolveYearBudget)) {
						timeMap.remove(DateUtils.getYear(entity.getBeginDate()));
					} else {
						if (EmptyUtils.isEmptyList(itemList)) {
							for (FbsBudgetResolveYear resolveYear : listResolveYearBudget) {
								FbsBudgetResolveYearVO resolveYearVO = new FbsBudgetResolveYearVO();
								BeanUtils.copyNotNullProperties(resolveYear, resolveYearVO);
								resolveYearVO.setItem(itemMap.get(resolveYear.getItemCode()));
								itemList.add(resolveYearVO);
							}
						}
						for (FbsBudgetResolveYear resolveYear : listResolveYearBudget) {
							int year = 12;
							for (int i = 1; i <= year; i++) {
								HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
								Method method = resolveYear.getClass().getMethod("getBMoney" + i, new Class[0]);
								bMoneyMap.put(1, EmptyUtils.isEmpty(method.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method.invoke(resolveYear)).multiply(rate));
								Method method1 = resolveYear.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
								bMoneyMap.put(2, EmptyUtils.isEmpty(method1.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method1.invoke(resolveYear)).multiply(rate));
								Method method2 = resolveYear.getClass().getMethod("getBtempMoney" + i, new Class[0]);
								bMoneyMap.put(3, EmptyUtils.isEmpty(method2.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method2.invoke(resolveYear)).multiply(rate));
								dtlMap.put(DateUtils.getYear(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + resolveYear.getItemCode() + BudgetConstant.BUDGET_SPLIT + i, bMoneyMap);
							}
						}
					}
				} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
					List<FbsBudgetResolveMonth> listMonth = resolveMonthMap.get(entity.getId());
					if (EmptyUtils.isEmptyList(listMonth)) {
						timeMap.remove(DateUtils.getMonth(entity.getBeginDate()));
					} else {
						if (EmptyUtils.isEmptyList(itemList)) {
							for (FbsBudgetResolveMonth resolveMonth : listMonth) {
								FbsBudgetResolveMonthVO resolveMonthVO = new FbsBudgetResolveMonthVO();
								BeanUtils.copyNotNullProperties(resolveMonth, resolveMonthVO);
								resolveMonthVO.setItem(itemMap.get(resolveMonth.getItemCode()));
								itemList.add(resolveMonthVO);
							}
						}
						//List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(entity.getBeginDate());
						for (FbsBudgetResolveMonth resolveMonth : listMonth) {
							for (int i = 0; i < listPeriod.size(); i++) {
								HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
								Method method = resolveMonth.getClass().getMethod("getBMoney" + (i + 1), new Class[0]);
								bMoneyMap.put(1, EmptyUtils.isEmpty(method.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method.invoke(resolveMonth)).multiply(rate));
								Method method1 = resolveMonth.getClass().getMethod("getBsuperMoney" + (i + 1), new Class[0]);
								bMoneyMap.put(2, EmptyUtils.isEmpty(method1.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method1.invoke(resolveMonth)).multiply(rate));
								Method method2 = resolveMonth.getClass().getMethod("getBtempMoney" + (i + 1), new Class[0]);
								bMoneyMap.put(3, EmptyUtils.isEmpty(method2.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method2.invoke(resolveMonth)).multiply(rate));
								dtlMap.put(DateUtils.getMonth(entity.getBeginDate()) + BudgetConstant.BUDGET_SPLIT + resolveMonth.getItemCode() + BudgetConstant.BUDGET_SPLIT + (i + 1), bMoneyMap);
							}
						}
					}
				}
			}
			map.put("timeMap", timeMap);
			map.put("colSize", timeMap.get("colSize"));
			timeMap.remove("colSize");
			map.put("itemList", itemList);
			map.put("dtlMap", dtlMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 获取年分解月日期集合
	 *
	 * @param list 预算集合
	 * @return
	 */
	public Map<Object, Object> getResolveTimeMap(List<FbsBudgetVO> list) {
		Map<Object, Object> timeMap = new TreeMap<Object, Object>(
				new Comparator<Object>() {
					@Override
					public int compare(Object obj1, Object obj2) {
						// 降序排序
						return obj1.toString().compareTo(obj2.toString());
					}
				});
		List<Object> monthList = new ArrayList();
		int year = 12;
		for (int i = 1; i <= year; i++) {
			monthList.add(i);
		}
		int colSize = 0;
		for (FbsBudgetVO entity : list) {
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
				Object[] objs = new Object[]{DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()), monthList};
				timeMap.put(DateUtils.getYear(entity.getBeginDate()), objs);
				colSize += monthList.size();
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(entity.getBeginDate());
				List<Object> weekList = new ArrayList();
				for (int i = 0; i < listPeriod.size(); i++) {
					weekList.add(i + 1);
				}
				Object[] objs = new Object[]{DateUtils.formatDate(entity.getBeginDate()) + "~" + DateUtils.formatDate(entity.getEndDate()), weekList};
				timeMap.put(DateUtils.getMonth(entity.getBeginDate()), objs);
				colSize += weekList.size();
			}
		}
		timeMap.put("colSize", colSize * 3);
		return timeMap;
	}
	/**
	 * 获取有权限的单位
	 *
	 * @return
	 */
	public List<CorpDTO> listEffectiveCorp(ICurrentUser user) {
		String resource = ResourceConstant.FBS_REPORT_FBSBUDGET_MERGEQUERY;
		List<String> corpIds = corpService.getCorpIdsByUserResource(resource, user.getId());
		if(EmptyUtils.isEmptyList(corpIds)){
			return null;
		}
		List<String> ctlCorpIds = this.fbsCorpBudgetService.listCorpIdsByReportBudget(corpIds);
		if(EmptyUtils.isEmptyList(ctlCorpIds)){
			return null;
		}
		List<CorpDTO> listCorp = this.corpService.listCorpDTOByCorpIds2(ctlCorpIds);
		if(EmptyUtils.isEmptyList(listCorp)){
			return null;
		}
		return corpService.listCorpTree(listCorp);
	}
	/**
	 * 初始化固定合并上报预算报表数据
	 *
	 * @param timeType       预算类型
	 * @param corpIds        单位集合
	 * @param seachBeginDate 查询日期
	 * @param seachEndDate   查询日期
	 * @param showItem       科目显示方式
	 * @return
	 */
	public Map<String, Object> getMergFbsBudget(String timeType, String corpIds, Date seachBeginDate, Date seachEndDate, String showItem, Map<String, String> moneyMap) {
		String[] corpIdArr = corpIds.split(CommonConstant.COMMA_SPLIT);
		List<FbsBudgetVO> list = this.listFbsBudget(timeType, corpIdArr, seachBeginDate, seachEndDate);
		Map<String, Object> map = new HashMap<>();
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		try {
			// 日期
			Map<Object, Object> timeMap = new TreeMap<Object, Object>(
					new Comparator<Object>() {
						@Override
						public int compare(Object obj1, Object obj2) {
							// 降序排序
							return obj1.toString().compareTo(obj2.toString());
						}
					});
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
				timeMap.put(DateUtils.getYear(seachBeginDate), DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate));
			} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
				timeMap.put(DateUtils.getQuarterOfYear(seachBeginDate), DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate));
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
				timeMap.put(DateUtils.getMonth(seachBeginDate), DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate));
			} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
				timeMap.put(DateUtils.getWeekOfYear(seachBeginDate), DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate));
			} else {
				timeMap.put(DateUtils.formatDate(seachBeginDate), DateUtils.formatDate(seachBeginDate));
			}
			// 科目
			List<FbsBudgetDtlAbsVO> itemList = new ArrayList();
			Set<String> itemSet = new HashSet<>();
			// 单位
			LinkedHashMap<String, String> corpMap = new LinkedHashMap<>();
			// 明细金额
			Map<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<>();
			List<String> budgetIds = new ArrayList<>();
			Map<String, FbsBudgetVO> budgetId2FbsBudgetVO = new HashMap<>();
			Map<String, Set<String>> timeType2BudgetIds = new HashMap<>();
			for (FbsBudgetVO entity : list) {
				budgetId2FbsBudgetVO.put(entity.getId(),entity);
				Set<String> budgetIdsForTimeType=timeType2BudgetIds.get(entity.getTimeType());
				if(budgetIdsForTimeType == null){
					budgetIdsForTimeType = new HashSet<>();
					timeType2BudgetIds.put(entity.getTimeType(),budgetIdsForTimeType);
				}
				budgetIdsForTimeType.add(entity.getId());
			}
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetIdConvertCur(timeType2BudgetIds);
			Set<String> budgetIdsForConvertCurs = new HashSet<>();
			Map<String,FbsItemVO> itemCode2ItemVO=new HashMap<>();
			Set<String> itemCodes=new HashSet<>();
			if (!EmptyUtils.isEmptyList(dtlList)) {
				// 初始化日期和明细金额
				for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList) {
					if (!itemSet.contains(fbsBudgetDtlAbsVO.getItemCode())) {
						itemCodes.add(fbsBudgetDtlAbsVO.getItemCode());
					}
				}
			}
			List<com.bytter.biz.budget.spi.vo.FbsItemVO> itemVOList=this.fbsItemService.getFbsItemVOS(itemCodes);
			List<FbsItemVO> fbsItemVOList = BeanUtils.copyListProperties(itemVOList, FbsItemVO.class);
			if(EmptyUtils.isNotEmpty(fbsItemVOList)){
				for(FbsItemVO itemVO:fbsItemVOList){
					itemCode2ItemVO.put(itemVO.getItemCode(),itemVO);
				}
			}
			if (!EmptyUtils.isEmptyList(dtlList)) {
				// 初始化日期和明细金额
				for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList) {
					if (!itemSet.contains(fbsBudgetDtlAbsVO.getItemCode())) {
						fbsBudgetDtlAbsVO.setItem(itemCode2ItemVO.get(fbsBudgetDtlAbsVO.getItemCode()));
						if(StringUtils.isNotBlank(showItem)){
							Integer showItemLevel = Integer.parseInt(showItem);
							if(showItemLevel > 0 && showItemLevel.equals(fbsBudgetDtlAbsVO.getItem().getItemLevel())) {
								itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
								itemList.add(fbsBudgetDtlAbsVO);
							}else if(showItemLevel == -1){
								//显示末级
								if ("1".equalsIgnoreCase(fbsBudgetDtlAbsVO.getItem().getIsWrite())) {
									itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
									itemList.add(fbsBudgetDtlAbsVO);
								}
							}else if(showItemLevel == 0){
								itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
								itemList.add(fbsBudgetDtlAbsVO);
							}
						}else{
							itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
							itemList.add(fbsBudgetDtlAbsVO);
						}
					}
					budgetIdsForConvertCurs.add(fbsBudgetDtlAbsVO.getBudgetId());
					FbsBudgetVO fbsBudgetVO =budgetId2FbsBudgetVO.get(fbsBudgetDtlAbsVO.getBudgetId());
					if(EmptyUtils.isEmpty(fbsBudgetVO.getCorpName())){
						FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpById(fbsBudgetVO.getCorpId());
						corpMap.put(fbsBudgetVO.getCorpId(), fbsCollectCorpVO.getCorpName());
					}else{
						FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(fbsBudgetVO.getCorpId());
						if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
							corpMap.put(fbsBudgetVO.getCorpId(), fbsBudgetVO.getCorpName()+"（汇总）");
						}else{
							corpMap.put(fbsBudgetVO.getCorpId(), fbsBudgetVO.getCorpName());
						}
					}
					String key = fbsBudgetDtlAbsVO.getItemCode() + BudgetConstant.BUDGET_SPLIT + fbsBudgetVO.getCorpId();
					dtlMap.put(key, fbsBudgetDtlAbsVO);
				}
			}
			budgetIds.addAll(budgetIdsForConvertCurs);
			/*for (FbsBudgetVO entity : list) {
				List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetIdConvertCur(entity.getId(), entity.getTimeType());
				if (!EmptyUtils.isEmptyList(dtlList)) {
					budgetIds.add(entity.getId());
					// 初始化日期和明细金额
					for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList) {
						if (!itemSet.contains(fbsBudgetDtlAbsVO.getItemCode())) {
							fbsBudgetDtlAbsVO.setItem(this.fbsItemService.getFbsItemVO(null, fbsBudgetDtlAbsVO.getItemCode()));
							if(StringUtils.isNotBlank(showItem)){
								Integer showItemLevel = Integer.parseInt(showItem);
								if(showItemLevel > 0 && showItemLevel.equals(fbsBudgetDtlAbsVO.getItem().getItemLevel())) {
									itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
									itemList.add(fbsBudgetDtlAbsVO);
								}else if(showItemLevel == -1){
									//显示末级
									if ("1".equalsIgnoreCase(fbsBudgetDtlAbsVO.getItem().getIsWrite())) {
										itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
										itemList.add(fbsBudgetDtlAbsVO);
									}
								}else if(showItemLevel == 0){
									itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
									itemList.add(fbsBudgetDtlAbsVO);
								}
							}else{
								itemSet.add(fbsBudgetDtlAbsVO.getItemCode());
								itemList.add(fbsBudgetDtlAbsVO);
							}
						}
						corpMap.put(entity.getCorpId(), entity.getCorpName());
						String key = fbsBudgetDtlAbsVO.getItemCode() + BudgetConstant.BUDGET_SPLIT + entity.getCorpId();
						dtlMap.put(key, fbsBudgetDtlAbsVO);
					}
				}
			}*/
			List<FbsBudgetDtlAbsVO> collectList = this.fbsBudgetReportDao.getMergeCollect(timeType, SqlUtil.getInSql(budgetIds, "t.budget_id", true), moneyMap);
			Map<String, Object> collectMap = new HashMap<>();
			if (!EmptyUtils.isEmptyList(collectList)) {
				for (FbsBudgetDtlAbsVO collect : collectList) {
					collectMap.put(collect.getItemCode(), collect.getbMoney());
				}
			}
			for (String itemCode : itemSet) {
				// 不存在的预算数据，汇总金额设置为0
				if (!collectMap.containsKey(itemCode)) {
					collectMap.put(itemCode, BigDecimal.ZERO);
				}
				// 不存在的预算数据，金额设置为0
				for (Map.Entry<String, String> corp : corpMap.entrySet()) {
					if (!dtlMap.containsKey(itemCode + BudgetConstant.BUDGET_SPLIT + corp.getKey())) {
						String key = itemCode + BudgetConstant.BUDGET_SPLIT + corp.getKey();
						FbsBudgetDtlAbsVO vo = new FbsBudgetDtlAbsVO();
						vo.setbMoney(BigDecimal.ZERO);
						vo.setBsuperMoney(BigDecimal.ZERO);
						vo.setBtempMoney(BigDecimal.ZERO);
						dtlMap.put(key, vo);
					}
				}
			}
			this.orderList(itemList);
			map.put("corpMap", corpMap);
			map.put("timeMap", timeMap);
			map.put("itemList", itemList);
			map.put("dtlMap", dtlMap);
			map.put("collectMap", collectMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 集合排序
	 * @param list
	 * @return
	 */
	private List<FbsBudgetDtlAbsVO> orderList(List<FbsBudgetDtlAbsVO> list){
		Collections.sort(list, new Comparator<FbsBudgetDtlAbsVO>() {
			@Override
			public int compare(FbsBudgetDtlAbsVO o1, FbsBudgetDtlAbsVO o2) {
				int ret = o1.getItemCode().compareTo(o2.getItemCode());
				if (ret < 0) {
					return -1;
				}
				if (ret == 0) {
					return 0;
				}
				return 1;
			}
		});
		return list;
	}
	/**
	 * 初始化年分解月，月分解周预算报表数据
	 *
	 * @param timeType       类型
	 * @param corpId         单位ID
	 * @param seachBeginDate 开始日期
	 * @param seachEndDate   结束日期
	 * @return
	 */
	public Map<String, Object> getMergeFbsBudgetResolve(String timeType, String corpId, Date seachBeginDate, Date seachEndDate,String showItem, Map<String, String> moneyMap) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("before search year and month:");
		List<FbsBudgetVO> list = this.listFbsBudget(timeType, corpId.split(CommonConstant.COMMA_SPLIT), seachBeginDate, seachEndDate);
		Map<String, Object> map = new HashMap<>();
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		try {
			List<String> budgetIds = new ArrayList<>();
			// 科目
			Map<String, com.bytter.biz.budget.spi.vo.FbsItemVO> itemMap = new TreeMap<String, com.bytter.biz.budget.spi.vo.FbsItemVO>(
					new Comparator<String>() {
						@Override
						public int compare(String obj1, String obj2) {
							// 降序排序
							return obj1.compareTo(obj2);
						}
					});
			// 单位
			Map<String, String> corpMap = new TreeMap<String, String>(
					new Comparator<String>() {
						@Override
						public int compare(String obj1, String obj2) {
							// 降序排序
							return obj1.compareTo(obj2);
						}
					});
			// 明细金额
			Map<String, Object> dtlMap = new HashMap<>();
			// 日期最大数
			int maxDateCount = 0;
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
				maxDateCount = 12;
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(seachBeginDate);
				maxDateCount = listPeriod.size();
			}
			/*for (FbsBudgetVO entity : list) {
				budgetIds.add(entity.getId());
				corpMap.put(entity.getCorpId(), entity.getCorpName());
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
					List<FbsBudgetResolveYear> listResolveYearBudget = this.fbsBudgetResolveYearService.listByBudgetId(entity.getId());
					if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
						for (FbsBudgetResolveYear resolveYear : listResolveYearBudget) {
							if (!itemMap.containsKey(resolveYear.getItemCode())) {
								FbsItemVO fbsItemVO = this.fbsItemService.getFbsItemVO(null, resolveYear.getItemCode());
								if(StringUtils.isNotBlank(showItem)){
									Integer showItemLevel = Integer.parseInt(showItem);
									if(showItemLevel > 0 && showItemLevel.equals(fbsItemVO.getItemLevel())) {
										itemMap.put(resolveYear.getItemCode(),fbsItemVO);
									}else if(showItemLevel == -1){
										//显示末级
										if ("1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
											itemMap.put(resolveYear.getItemCode(),fbsItemVO);
										}
									}else if(showItemLevel == 0){
										itemMap.put(resolveYear.getItemCode(),fbsItemVO);
									}
								}else {
									itemMap.put(resolveYear.getItemCode(),fbsItemVO);
								}
							}
						}
					}
				} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
					List<FbsBudgetResolveMonth> listMonth = this.fbsBudgetResolveMonthService.listByBudgetId(entity.getId());
					if (!EmptyUtils.isEmptyList(listMonth)) {
						for (FbsBudgetResolveMonth resolveMonth : listMonth) {
							if (!itemMap.containsKey(resolveMonth.getItemCode())) {
								FbsItemVO fbsItemVO = this.fbsItemService.getFbsItemVO(null, resolveMonth.getItemCode());
								if(StringUtils.isNotBlank(showItem)){
									Integer showItemLevel = Integer.parseInt(showItem);
									if(showItemLevel > 0 && showItemLevel.equals(fbsItemVO.getItemLevel())) {
										itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
									}else if(showItemLevel == -1){
										//显示末级
										if ("1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
											itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
										}
									}else if(showItemLevel == 0){
										itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
									}
								}else {
									itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
								}
							}
						}
					}
				}
			}*/
			stopWatch.stop();
			stopWatch.start("year listByBudgetIds before:");
			// 下面是上面注释的部分，仅提出查询 start
			Set<String> yearBudgetIds= Sets.newHashSet();
			Set<String> monthBudgetIds= Sets.newHashSet();
			for (FbsBudgetVO entity : list) {
				budgetIds.add(entity.getId());
				corpMap.put(entity.getCorpId(), entity.getCorpName());
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
					yearBudgetIds.add(entity.getId());
				} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
					monthBudgetIds.add(entity.getId());
				}
			}
			List<FbsBudgetResolveYear> listResolveYearBudget = null;
			Map<String,List<FbsBudgetResolveYear>> budgetId2FbsBudgetResolveYear = new HashMap<>();
			stopWatch.stop();
			stopWatch.start("year listByBudgetIds:");
			if(EmptyUtils.isNotEmpty(yearBudgetIds)) {
				//listResolveYearBudget = this.fbsBudgetResolveYearService.listByBudgetIds(yearBudgetIds);
				listResolveYearBudget = this.fbsBudgetResolveYearService.listBudgetIdItemCodeByBudgetIds(yearBudgetIds);
			}
			stopWatch.stop();
			if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
				stopWatch.start("year listByBudgetIds after getFbsItemVOS before:");
				Set<String> itemCodes=Sets.newHashSet();
				for (FbsBudgetResolveYear resolveYear : listResolveYearBudget) {
					if (!itemMap.containsKey(resolveYear.getItemCode())) {
						itemCodes.add(resolveYear.getItemCode());
					}
					// 下面新增加是为了在i <= maxDateCount 的for循环中使用   start
					List<FbsBudgetResolveYear> fbsBudgetResolveYearsForOneBudgetId= budgetId2FbsBudgetResolveYear.get(resolveYear.getBudgetId());
					if(fbsBudgetResolveYearsForOneBudgetId == null) {
						fbsBudgetResolveYearsForOneBudgetId = Lists.newArrayList();
						fbsBudgetResolveYearsForOneBudgetId.add(resolveYear);
						budgetId2FbsBudgetResolveYear.put(resolveYear.getBudgetId(),fbsBudgetResolveYearsForOneBudgetId);
					}else {
						fbsBudgetResolveYearsForOneBudgetId.add(resolveYear);
						budgetId2FbsBudgetResolveYear.put(resolveYear.getBudgetId(),fbsBudgetResolveYearsForOneBudgetId);
					}
					
					// 下面新增加是为了在i <= maxDateCount 的for循环中使用   end
				}
				Map<String,com.bytter.biz.budget.spi.vo.FbsItemVO> itemCode2ItemVO=new HashMap<>();
				stopWatch.stop();
				stopWatch.start("year listByBudgetIds after getFbsItemVOS :");
				List<com.bytter.biz.budget.spi.vo.FbsItemVO> fbsItemVOList = this.fbsItemService.getFbsItemVOS(itemCodes);
				stopWatch.stop();
				stopWatch.start("year listByBudgetIds after getFbsItemVOS after:");
				if(EmptyUtils.isNotEmpty(fbsItemVOList)) {
					for (com.bytter.biz.budget.spi.vo.FbsItemVO fbsItemVO : fbsItemVOList) {
						itemCode2ItemVO.put(fbsItemVO.getItemCode(), fbsItemVO);
					}
				}
				for (FbsBudgetResolveYear resolveYear : listResolveYearBudget) {
					if (!itemMap.containsKey(resolveYear.getItemCode())) {
						com.bytter.biz.budget.spi.vo.FbsItemVO fbsItemVO = itemCode2ItemVO.get(resolveYear.getItemCode());
						if(StringUtils.isNotBlank(showItem)){
							Integer showItemLevel = Integer.parseInt(showItem);
							if(showItemLevel > 0 && showItemLevel.equals(fbsItemVO.getItemLevel())) {
								itemMap.put(resolveYear.getItemCode(),fbsItemVO);
							}else if(showItemLevel == -1){
								//显示末级
								if ("1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
									itemMap.put(resolveYear.getItemCode(),fbsItemVO);
								}
							}else if(showItemLevel == 0){
								itemMap.put(resolveYear.getItemCode(),fbsItemVO);
							}
						}else {
							itemMap.put(resolveYear.getItemCode(),fbsItemVO);
						}
					}
				}
				itemCode2ItemVO = null;
				fbsItemVOList = null;
				stopWatch.stop();
			}
			listResolveYearBudget = null;
			stopWatch.start("month :");
			List<FbsBudgetResolveMonth> listMonth = null;
			Map<String,List<FbsBudgetResolveMonth>> budgetId2FbsBudgetResolveMonth = new HashMap<>();
			if(EmptyUtils.isNotEmpty(monthBudgetIds)) {
				listMonth=this.fbsBudgetResolveMonthService.listByBudgetId(monthBudgetIds);
			}
			if (!EmptyUtils.isEmptyList(listMonth)) {
				Set<String> monthItemCodes=new HashSet<>();
				for (FbsBudgetResolveMonth resolveMonth : listMonth) {
					if (!itemMap.containsKey(resolveMonth.getItemCode())) {
						monthItemCodes.add(resolveMonth.getItemCode());
					}
					// 下面新增加是为了在i <= maxDateCount 的for循环中使用   start
					List<FbsBudgetResolveMonth> fbsBudgetResolveMonthForOneBudgetId = budgetId2FbsBudgetResolveMonth.get(resolveMonth.getBudgetId());
					if(fbsBudgetResolveMonthForOneBudgetId == null){
						fbsBudgetResolveMonthForOneBudgetId = Lists.newArrayList();
						budgetId2FbsBudgetResolveMonth.put(resolveMonth.getBudgetId(),fbsBudgetResolveMonthForOneBudgetId);
					}
					fbsBudgetResolveMonthForOneBudgetId.add(resolveMonth);
					// 下面新增加是为了在i <= maxDateCount 的for循环中使用   end
				}
				List<com.bytter.biz.budget.spi.vo.FbsItemVO> fbsItemVOList = this.fbsItemService.getFbsItemVOS(monthItemCodes);
				Map<String,com.bytter.biz.budget.spi.vo.FbsItemVO> monthItemCode2ItemVO=new HashMap<>();
				if(EmptyUtils.isNotEmpty(fbsItemVOList)){
					for(com.bytter.biz.budget.spi.vo.FbsItemVO fbsItemVO:fbsItemVOList){
						monthItemCode2ItemVO.put(fbsItemVO.getItemCode(),fbsItemVO);
					}
				}
				for (FbsBudgetResolveMonth resolveMonth : listMonth) {
					if (!itemMap.containsKey(resolveMonth.getItemCode())) {
						com.bytter.biz.budget.spi.vo.FbsItemVO fbsItemVO = monthItemCode2ItemVO.get(resolveMonth.getItemCode());
						if(StringUtils.isNotBlank(showItem)){
							Integer showItemLevel = Integer.parseInt(showItem);
							if(showItemLevel > 0 && showItemLevel.equals(fbsItemVO.getItemLevel())) {
								itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
							}else if(showItemLevel == -1){
								//显示末级
								if ("1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
									itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
								}
							}else if(showItemLevel == 0){
								itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
							}
						}else {
							itemMap.put(resolveMonth.getItemCode(),fbsItemVO);
						}
					}
				}
				fbsItemVOList = null;
				monthItemCode2ItemVO = null;
			}
			listMonth = null;
			stopWatch.stop();
			stopWatch.start("rate :");
			for (Map.Entry<String, com.bytter.biz.budget.spi.vo.FbsItemVO> item : itemMap.entrySet()) {
				Map<Integer, BigDecimal> bMoneyMap = new TreeMap<Integer, BigDecimal>(
						new Comparator<Integer>() {
							@Override
							public int compare(Integer obj1, Integer obj2) {
								// 降序排序
								return obj1.compareTo(obj2);
							}
						});
				int count = 0;
				for (int i = 1; i <= maxDateCount; i++) {
					for (Map.Entry<String, String> money : moneyMap.entrySet()) {
						for (FbsBudgetVO entity : list) {
							//取计划的汇率
							BigDecimal rate = entity.getExchangeRate();
							if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(entity.getTimeType())) {
								List<FbsBudgetResolveYear> listResolveYearBudgets = budgetId2FbsBudgetResolveYear.get(entity.getId());
								if (!EmptyUtils.isEmptyList(listResolveYearBudgets)) {
									for (FbsBudgetResolveYear resolveYear : listResolveYearBudgets) {
										if (item.getKey().equalsIgnoreCase(resolveYear.getItemCode())) {
											if ("1".equalsIgnoreCase(money.getKey())) {
												Method method = resolveYear.getClass().getMethod("getBMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method.invoke(resolveYear)).multiply(rate));
											}
											if ("2".equalsIgnoreCase(money.getKey())) {
												Method method1 = resolveYear.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method1.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method1.invoke(resolveYear)).multiply(rate));
											}
											if ("3".equalsIgnoreCase(money.getKey())) {
												Method method2 = resolveYear.getClass().getMethod("getBtempMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method2.invoke(resolveYear)) ? BigDecimal.ZERO : ((BigDecimal) method2.invoke(resolveYear)).multiply(rate));
											}
										}
									}
								}
							} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(entity.getTimeType())) {
								List<FbsBudgetResolveMonth> listMonths = budgetId2FbsBudgetResolveMonth.get(entity.getId());
								if (!EmptyUtils.isEmptyList(listMonths)) {
									for (FbsBudgetResolveMonth resolveMonth : listMonths) {
										if (item.getKey().equalsIgnoreCase(resolveMonth.getItemCode())) {
											if ("1".equalsIgnoreCase(money.getKey())) {
												Method method = resolveMonth.getClass().getMethod("getBMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method.invoke(resolveMonth)).multiply(rate));
											}
											if ("2".equalsIgnoreCase(money.getKey())) {
												Method method1 = resolveMonth.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method1.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method1.invoke(resolveMonth)).multiply(rate));
											}
											if ("3".equalsIgnoreCase(money.getKey())) {
												Method method2 = resolveMonth.getClass().getMethod("getBtempMoney" + i, new Class[0]);
												bMoneyMap.put(++count, EmptyUtils.isEmpty(method2.invoke(resolveMonth)) ? BigDecimal.ZERO : ((BigDecimal) method2.invoke(resolveMonth)).multiply(rate));
											}
										}
									}
								}
							}
						}
					}
				}
				dtlMap.put(item.getKey(), bMoneyMap);
			}
			budgetId2FbsBudgetResolveYear = null;
			budgetId2FbsBudgetResolveMonth = null;
			stopWatch.stop();
			stopWatch.start("getMergeResolveCollect :");
			int colSum = maxDateCount * moneyMap.size() * corpMap.size();
			List<FbsBudgetDtlAbsVO> collectList = this.fbsBudgetReportDao.getMergeResolveCollect(timeType, SqlUtil.getInSql(budgetIds, "t.budget_id", true), moneyMap);
			Map<String, Object> collectMap = new HashMap<>();
			if (!EmptyUtils.isEmptyList(collectList)) {
				for (FbsBudgetDtlAbsVO collect : collectList) {
					collectMap.put(collect.getItemCode(), collect.getbMoney());
				}
			}
			for (Map.Entry<String, com.bytter.biz.budget.spi.vo.FbsItemVO> item : itemMap.entrySet()) {
				// 不存在的预算数据，汇总金额设置为0
				if (!collectMap.containsKey(item.getKey())) {
					collectMap.put(item.getKey(), BigDecimal.ZERO);
				}
				// 不存在的预算科目数据，金额设置为0
				Map<Integer, BigDecimal> bMoneyMap = (Map<Integer, BigDecimal>) dtlMap.get(item.getKey());
				for (int i = bMoneyMap.size() + 1; i <= colSum; i++) {
					bMoneyMap.put(i, BigDecimal.ZERO);
				}
			}
			map.put("maxDateCount", maxDateCount);
			map.put("corpMap", corpMap);
			map.put("itemMap", itemMap);
			map.put("dtlMap", dtlMap);
			map.put("collectMap", collectMap);
			stopWatch.stop();
			logger.info("getMergeFbsBudgetResolve:{}",stopWatch.prettyPrint());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 计划明细列表数据
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableDetailReport(DataTablesInput dataTablesInput, ICurrentUser user) {
		//获取计划执行周期类型
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		StringBuilder sql = new StringBuilder();
		String resource = ResourceConstant.FBS_REPORT_FBSBUDGETDETAIL_QUERY;
		List<String> corpList = corpService.getCorpIdsByUserResource(resource, user.getId());
		List<String> ids = this.fbsCollectCorpDao.getIdByCorpId(corpList);
		corpList.addAll(ids);
		String corpAuthSql = SqlUtil.getInSql(corpList, "b.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		String ins_corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
		if(EmptyUtils.isNotEmpty(ins_corpId)){
			String id = this.fbsCollectCorpDao.getIdByCorpId(ins_corpId.split(","));
			if(EmptyUtils.isNotEmpty(id)){
				ins_corpId = ins_corpId+","+id;
				dataTablesInput.getFormdata().put("ins_corpId",ins_corpId);
			}
		}
		//itemName 关联查询时已有;
		DataTablesOutput out = this.fbsBudgetReportDao.dataTableDetailReport(dataTablesInput, tableName, sql.toString());
		List<FbsBudgetDetailReportVO> list = out.getData();
		Map<String, DictDTO> settlTypeMap = dictService.listMap(BudgetConstant.BUDGET_SETTLTYPE_DICT);
		for(FbsBudgetDetailReportVO vo :list){
			if(!MapUtils.isEmpty(settlTypeMap)) {
				if(!ObjectUtils.isEmpty(settlTypeMap.get(vo.getSettlType())) && !StringUtils.isEmpty(vo.getSettlType())) {
					vo.setSettlTypeName(settlTypeMap.get(vo.getSettlType()).getName());
				}
			}
			if(EmptyUtils.isEmpty(vo.getCorpName())){
				FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpById(vo.getCorpId());
				if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
					vo.setCorpName(fbsCollectCorpVO.getCorpName());
				}
			}else{
				FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(vo.getCorpId());
				if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
					vo.setCorpName(vo.getCorpName()+"(汇总)");
				}
			}
		}
		return out;
	}
}
