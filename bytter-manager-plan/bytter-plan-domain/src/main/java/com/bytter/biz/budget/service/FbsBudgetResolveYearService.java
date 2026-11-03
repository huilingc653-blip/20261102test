package com.bytter.biz.budget.service;

import com.bytter.biz.budget.dao.FbsBudgetResolveYearDao;
import com.bytter.biz.budget.entity.*;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.utils.BudgetUtils;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowStartedListener;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_RESOLVE_YEAR
 * @date 2017-07-26 11:24:33
 */
@Service("fbsBudgetResolveYearService")
@Transactional
public class FbsBudgetResolveYearService extends BaseService<FbsBudgetResolveYear, String> implements IWorkFlowStartedListener {
	@Autowired
	private FbsBudgetResolveYearDao fbsBudgetResolveYearDao;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private FbsBudgetDetailService fbsBudgetDetailService;
	@Autowired
	private FbsMessageService fbsMessageService;
	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	@Autowired
	private FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@Autowired
	private FbsItemService fbsItemService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetResolveYear entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(FbsBudgetResolveYear entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetResolveYear entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetResolveYear entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetResolveYear, String> getEntityDao() {
		return fbsBudgetResolveYearDao;
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetResolveYear> listByBudgetId(String budgetId) {
		return fbsBudgetResolveYearDao.listByBudgetId(budgetId);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetResolveYear> listBudgetIdItemCodeByBudgetIds(Set<String> budgetIds) {
		if(EmptyUtils.isEmpty(budgetIds)){
			return Lists.newArrayListWithCapacity(0);
		}
		List<String> list = budgetIds.stream().collect(Collectors.toList());
		return fbsBudgetResolveYearDao.listByBudgetIds(list);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetIds
	 * @return
	 */
	public HashMap<String, List<FbsBudgetResolveYear>> mapByBudgetIds(List<String> budgetIds) {
		HashMap<String, List<FbsBudgetResolveYear>> map = new HashMap<>();
		List<FbsBudgetResolveYear> list = fbsBudgetResolveYearDao.listByBudgetIds(budgetIds);
		if(!CollectionUtils.isEmpty(list)) {
			for (FbsBudgetResolveYear year:list) {
				List<FbsBudgetResolveYear> tempList = null;
				if(map.containsKey(year.getBudgetId())){
					tempList = map.get(year.getBudgetId());
				} else {
					tempList = new ArrayList<>();
				}
				tempList.add(year);
				map.put(year.getBudgetId(), tempList);
			}
		}
		return map;
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetResolveYearVO> listVOByBudgetId(String budgetId) {
		return fbsBudgetResolveYearDao.listVOByBudgetId(budgetId);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @param itemCode
	 * @return
	 */
	public FbsBudgetResolveYear getByBudgetIdAndItemCode(String budgetId, String itemCode) {
		return fbsBudgetResolveYearDao.getByBudgetIdAndItemCode(budgetId, itemCode);
	}
	/**
	 * 获取年计划分解
	 *
	 * @param budget
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<FbsBudgetResolveYearVO> initResolveYearBudget(FbsBudgetVO budget, ICurrentUser user) {
		List<FbsBudgetResolveYearVO> listBudget = new ArrayList<FbsBudgetResolveYearVO>();
		if (EmptyUtils.isEmptyList(budget.getDtlList())) {
			return listBudget;
		}
		//获取年分解信息
		List<FbsBudgetResolveYear> listResolveYearBudget = this.listByBudgetId(budget.getId());
		try {
			if (listResolveYearBudget != null && listResolveYearBudget.size() > 0) {
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailResolve(budget.getId(), budget.getTimeType(), BudgetConstant.BUDGET_TARGET_INPUT);
				for (FbsBudgetResolveYear resolve : listResolveYearBudget) {
					FbsBudgetResolveYearVO entity = new FbsBudgetResolveYearVO();
					BeanUtils.copyNotNullProperties(resolve, entity);
					for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
						if (resolve.getItemCode().equals(dtl.getItemCode())) {
							entity.setDetailId(dtl.getId());
							entity.setItem(dtl.getItem());
							entity.setRmk(dtl.getRmk());
						}
					}
					HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> bsuperMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> btempMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> bdetailMoneyMap = new HashMap<Integer, BigDecimal>();
					for (int i = 1; i <= 12; i++) {
						Method method = entity.getClass().getMethod("getbMoney" + i, new Class[0]);
						bMoneyMap.put(i, (BigDecimal) method.invoke(entity));
						Method bsuperMethod = entity.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
						bsuperMoneyMap.put(i, (BigDecimal) bsuperMethod.invoke(entity));
						Method btempMethod = entity.getClass().getMethod("getBtempMoney" + i, new Class[0]);
						btempMoneyMap.put(i, (BigDecimal) btempMethod.invoke(entity));
						bdetailMoneyMap.put(i, map.get(resolve.getItemCode() + "_" + i));
					}
					entity.setbMoneyMap(bMoneyMap);
					entity.setBsuperMoneyMap(bsuperMoneyMap);
					entity.setBtempMoneyMap(btempMoneyMap);
					entity.setBdetailMoneyMap(bdetailMoneyMap);
					listBudget.add(entity);
				}
			} else {
				for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
					//初始化年分解表
					FbsBudgetResolveYear year = new FbsBudgetResolveYear();
					year.setBudgetId(dtl.getBudgetId());
					year.setItemCode(dtl.getItemCode());
					year.setItemName(dtl.getItemName());
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
					year.setBsuperMoney(BigDecimal.ZERO);
					year.setBsuperMoney1(BigDecimal.ZERO);
					year.setBsuperMoney2(BigDecimal.ZERO);
					year.setBsuperMoney3(BigDecimal.ZERO);
					year.setBsuperMoney4(BigDecimal.ZERO);
					year.setBsuperMoney5(BigDecimal.ZERO);
					year.setBsuperMoney6(BigDecimal.ZERO);
					year.setBsuperMoney7(BigDecimal.ZERO);
					year.setBsuperMoney8(BigDecimal.ZERO);
					year.setBsuperMoney9(BigDecimal.ZERO);
					year.setBsuperMoney10(BigDecimal.ZERO);
					year.setBsuperMoney11(BigDecimal.ZERO);
					year.setBsuperMoney12(BigDecimal.ZERO);
					year.setBtempMoney(BigDecimal.ZERO);
					year.setBtempMoney1(BigDecimal.ZERO);
					year.setBtempMoney2(BigDecimal.ZERO);
					year.setBtempMoney3(BigDecimal.ZERO);
					year.setBtempMoney4(BigDecimal.ZERO);
					year.setBtempMoney5(BigDecimal.ZERO);
					year.setBtempMoney6(BigDecimal.ZERO);
					year.setBtempMoney7(BigDecimal.ZERO);
					year.setBtempMoney8(BigDecimal.ZERO);
					year.setBtempMoney9(BigDecimal.ZERO);
					year.setBtempMoney10(BigDecimal.ZERO);
					year.setBtempMoney11(BigDecimal.ZERO);
					year.setBtempMoney12(BigDecimal.ZERO);
					year.setCreateBy(user.getLoginName());
					year.setCreateDate(DateUtils.getDate());
					this.saveOrUpdate(year);
					FbsBudgetResolveYearVO entity = new FbsBudgetResolveYearVO();
					BeanUtils.copyNotNullProperties(year, entity);
					entity.setDetailId(dtl.getId());
					entity.setItem(dtl.getItem());
					HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
					for (int i = 1; i <= 12; i++) {
						Method method = entity.getClass().getMethod("getbMoney" + i, new Class[0]);
						bMoneyMap.put(i, (BigDecimal) method.invoke(entity));
					}
					entity.setbMoneyMap(bMoneyMap);
					listBudget.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBudget;
	}
	/**
	 * 保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudget(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		FbsBudget entity = fbsBudgetService.get(input.getId(), true);
		//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额，同时状态更新为待汇总状态
		FbsBudget preCorpBudget = fbsBudgetService.subtractFbsBudget(entity, false);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
			//扣减汇总单位分解金额
			this.subtractFbsBudgetResolve(entity, preCorpBudget, user);
		} else {
			entity.setStatus(input.getStatus());
		}
		boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		BigDecimal checkMoney = BigDecimal.ZERO;
		String loginName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], entity.getTimeType());
			dtl.setbMoney(input.getbMoney()[i]);
			String rmkVal = null;
			if (input.getRmk() != null && i < input.getRmk().length) {
				String temp = input.getRmk()[i];
				rmkVal = (temp == null || StringUtils.isBlank(temp)) ? null : temp;
			}
			dtl.setRmk(rmkVal);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i]);
			if (EmptyUtils.isEmpty(year)) {
				year = new FbsBudgetResolveYear();
			}
			BeanUtils.copyNotNullProperties(dtl, year);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				year.setId(null);
			} else {
				year.setId(input.getResolveId()[i]);
			}
			year.setBMoney(input.getbMoney()[i]);
			year.setBMoney1(input.getbMoney1()[i]);
			year.setBMoney2(input.getbMoney2()[i]);
			year.setBMoney3(input.getbMoney3()[i]);
			year.setBMoney4(input.getbMoney4()[i]);
			year.setBMoney5(input.getbMoney5()[i]);
			year.setBMoney6(input.getbMoney6()[i]);
			year.setBMoney7(input.getbMoney7()[i]);
			year.setBMoney8(input.getbMoney8()[i]);
			year.setBMoney9(input.getbMoney9()[i]);
			year.setBMoney10(input.getbMoney10()[i]);
			year.setBMoney11(input.getbMoney11()[i]);
			year.setBMoney12(input.getbMoney12()[i]);
			year.setBsuperMoney(BigDecimal.ZERO);
			year.setBsuperMoney1(BigDecimal.ZERO);
			year.setBsuperMoney2(BigDecimal.ZERO);
			year.setBsuperMoney3(BigDecimal.ZERO);
			year.setBsuperMoney4(BigDecimal.ZERO);
			year.setBsuperMoney5(BigDecimal.ZERO);
			year.setBsuperMoney6(BigDecimal.ZERO);
			year.setBsuperMoney7(BigDecimal.ZERO);
			year.setBsuperMoney8(BigDecimal.ZERO);
			year.setBsuperMoney9(BigDecimal.ZERO);
			year.setBsuperMoney10(BigDecimal.ZERO);
			year.setBsuperMoney11(BigDecimal.ZERO);
			year.setBsuperMoney12(BigDecimal.ZERO);
			year.setBtempMoney(BigDecimal.ZERO);
			year.setBtempMoney1(BigDecimal.ZERO);
			year.setBtempMoney2(BigDecimal.ZERO);
			year.setBtempMoney3(BigDecimal.ZERO);
			year.setBtempMoney4(BigDecimal.ZERO);
			year.setBtempMoney5(BigDecimal.ZERO);
			year.setBtempMoney6(BigDecimal.ZERO);
			year.setBtempMoney7(BigDecimal.ZERO);
			year.setBtempMoney8(BigDecimal.ZERO);
			year.setBtempMoney9(BigDecimal.ZERO);
			year.setBtempMoney10(BigDecimal.ZERO);
			year.setBtempMoney11(BigDecimal.ZERO);
			year.setBtempMoney12(BigDecimal.ZERO);
			year.setCreateBy(loginName);
			year.setCreateDate(curDate);
			year.setUpdateBy(loginName);
			year.setUpdateDate(curDate);
			this.saveOrUpdate(year);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_TEMP;
		String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : isEdit ? FbsMessageConstant.FBS_LOG_ADD_TEMPSAVE_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_TEMPSAVE_SUCC;
		if (CommonConstant.YES.equalsIgnoreCase(input.getIsCollect())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_COLLECT_EDIT;
			msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_COLLECT_EDIT;
		}
		//提交时调用审批方法
		if (BudgetConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_SUBMIT;
			msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : isEdit ? FbsMessageConstant.FBS_LOG_ADD_SUBMIT_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_SUBMIT_SUCC;
			//调用审批后方法
			 getStartHandler().handler(FbsWorkFlowConstant.FBS_BUDGET_INPUT, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, msg, FbsMessageConstant.BUS_NAME_INPUT, user);
		//完成消息通知
		notificationService.complete(NotificationConstant.BUDGET.BUDGET_INPUT_YEAR, entity.getId(), user.getId());
	}
	/**
	 * 导入时后台计算
	 * @param input
	 */
	public void saveFbsBudgetForImport(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		FbsBudget entity = fbsBudgetService.get(input.getId(), true);
		entity.setStatus(input.getStatus());
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		BigDecimal checkMoney = BigDecimal.ZERO;
		String loginName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		List<Map<String,Object>> list1 = new ArrayList<>();
		List<Map<String,Object>> list2 = new ArrayList<>();
		List<Map<String,Object>> list3 = new ArrayList<>();
		List<Map<String,Object>> list4 = new ArrayList<>();
		List<Map<String,Object>> list5 = new ArrayList<>();
		List<Map<String,Object>> list6 = new ArrayList<>();
		List<Map<String,Object>> list7 = new ArrayList<>();
		List<Map<String,Object>> list8 = new ArrayList<>();
		List<Map<String,Object>> list9 = new ArrayList<>();
		List<Map<String,Object>> list10 = new ArrayList<>();
		List<Map<String,Object>> list11 = new ArrayList<>();
		List<Map<String,Object>> list12 = new ArrayList<>();
		Map<String, FbsItemVO> fbsItemMap = new HashMap<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], entity.getTimeType());
			Map<String,Object> map1 = new HashMap<>();
			Map<String,Object> map2 = new HashMap<>();
			Map<String,Object> map3 = new HashMap<>();
			Map<String,Object> map4 = new HashMap<>();
			Map<String,Object> map5 = new HashMap<>();
			Map<String,Object> map6 = new HashMap<>();
			Map<String,Object> map7 = new HashMap<>();
			Map<String,Object> map8 = new HashMap<>();
			Map<String,Object> map9 = new HashMap<>();
			Map<String,Object> map10 = new HashMap<>();
			Map<String,Object> map11 = new HashMap<>();
			Map<String,Object> map12 = new HashMap<>();
			map1.put("itemCode",dtl.getItemCode());
			map2.put("itemCode",dtl.getItemCode());
			map3.put("itemCode",dtl.getItemCode());
			map4.put("itemCode",dtl.getItemCode());
			map5.put("itemCode",dtl.getItemCode());
			map6.put("itemCode",dtl.getItemCode());
			map7.put("itemCode",dtl.getItemCode());
			map8.put("itemCode",dtl.getItemCode());
			map9.put("itemCode",dtl.getItemCode());
			map10.put("itemCode",dtl.getItemCode());
			map11.put("itemCode",dtl.getItemCode());
			map12.put("itemCode",dtl.getItemCode());
			map1.put("bMoney",input.getbMoney1()[i]);
			map2.put("bMoney",input.getbMoney2()[i]);
			map3.put("bMoney",input.getbMoney3()[i]);
			map4.put("bMoney",input.getbMoney4()[i]);
			map5.put("bMoney",input.getbMoney5()[i]);
			map6.put("bMoney",input.getbMoney6()[i]);
			map7.put("bMoney",input.getbMoney7()[i]);
			map8.put("bMoney",input.getbMoney8()[i]);
			map9.put("bMoney",input.getbMoney9()[i]);
			map10.put("bMoney",input.getbMoney10()[i]);
			map11.put("bMoney",input.getbMoney11()[i]);
			map12.put("bMoney",input.getbMoney12()[i]);
			list1.add(map1);
			list2.add(map2);
			list3.add(map3);
			list4.add(map4);
			list5.add(map5);
			list6.add(map6);
			list7.add(map7);
			list8.add(map8);
			list9.add(map9);
			list10.add(map10);
			list11.add(map11);
			list12.add(map12);
			fbsItemMap.put(dtl.getItemCode(),fbsItemService.getFbsItemVO(null,dtl.getItemCode()));
		}
		//计算上报金额
		calItemMoney(list1,fbsItemMap,"bMoney");
		calItemMoney(list2,fbsItemMap,"bMoney");
		calItemMoney(list3,fbsItemMap,"bMoney");
		calItemMoney(list4,fbsItemMap,"bMoney");
		calItemMoney(list5,fbsItemMap,"bMoney");
		calItemMoney(list6,fbsItemMap,"bMoney");
		calItemMoney(list7,fbsItemMap,"bMoney");
		calItemMoney(list8,fbsItemMap,"bMoney");
		calItemMoney(list9,fbsItemMap,"bMoney");
		calItemMoney(list10,fbsItemMap,"bMoney");
		calItemMoney(list11,fbsItemMap,"bMoney");
		calItemMoney(list12,fbsItemMap,"bMoney");
		//计算期初期末
		calPeriodItemMoney(list1,list2,fbsItemMap,"bMoney");
		calPeriodItemMoney(list2,list3,fbsItemMap,"bMoney");
		calPeriodItemMoney(list3,list4,fbsItemMap,"bMoney");
		calPeriodItemMoney(list4,list5,fbsItemMap,"bMoney");
		calPeriodItemMoney(list5,list6,fbsItemMap,"bMoney");
		calPeriodItemMoney(list6,list7,fbsItemMap,"bMoney");
		calPeriodItemMoney(list7,list8,fbsItemMap,"bMoney");
		calPeriodItemMoney(list8,list9,fbsItemMap,"bMoney");
		calPeriodItemMoney(list9,list10,fbsItemMap,"bMoney");
		calPeriodItemMoney(list10,list11,fbsItemMap,"bMoney");
		calPeriodItemMoney(list11,list12,fbsItemMap,"bMoney");
		//计算年计划
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], entity.getTimeType());
			FbsItemVO fbsItemVO = fbsItemMap.get(dtl.getItemCode());
			BigDecimal bmoney = new BigDecimal(0);
			//不为期初期末则计算和
			if(fbsItemVO.getIsBeginPeriod().equals("0") && fbsItemVO.getIsEndPeriod().equals("0")) {
				bmoney = bmoney.add((BigDecimal) list1.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list2.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list3.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list4.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list5.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list6.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list7.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list8.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list9.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list10.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list11.get(i).get("bMoney"));
				bmoney = bmoney.add((BigDecimal) list12.get(i).get("bMoney"));
			}
			//期初则1月份期初
			if(fbsItemVO.getIsBeginPeriod().equals("1")){
				bmoney = (BigDecimal) list1.get(i).get("bMoney");
			}
			//期末则为12月份期末
			if(fbsItemVO.getIsEndPeriod().equals("1")){
				bmoney = (BigDecimal) list12.get(i).get("bMoney");
			}
			dtl.setbMoney(bmoney);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i]);
			if (EmptyUtils.isEmpty(year)) {
				year = new FbsBudgetResolveYear();
			}
			BeanUtils.copyNotNullProperties(dtl, year);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				year.setId(null);
			} else {
				year.setId(input.getResolveId()[i]);
			}
			year.setBMoney(bmoney);
			year.setBMoney1((BigDecimal) list1.get(i).get("bMoney"));
			year.setBMoney2((BigDecimal) list2.get(i).get("bMoney"));
			year.setBMoney3((BigDecimal) list3.get(i).get("bMoney"));
			year.setBMoney4((BigDecimal) list4.get(i).get("bMoney"));
			year.setBMoney5((BigDecimal) list5.get(i).get("bMoney"));
			year.setBMoney6((BigDecimal) list6.get(i).get("bMoney"));
			year.setBMoney7((BigDecimal) list7.get(i).get("bMoney"));
			year.setBMoney8((BigDecimal) list8.get(i).get("bMoney"));
			year.setBMoney9((BigDecimal) list9.get(i).get("bMoney"));
			year.setBMoney10((BigDecimal) list10.get(i).get("bMoney"));
			year.setBMoney11((BigDecimal) list11.get(i).get("bMoney"));
			year.setBMoney12((BigDecimal) list12.get(i).get("bMoney"));
			year.setUpdateBy(loginName);
			year.setUpdateDate(curDate);
			this.saveOrUpdate(year);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
	}
	public void calItemMoney(List<Map<String,Object>> list, Map<String, FbsItemVO> fbsItemMap, String moneyName){
		//上级科目汇总计算
		calSuperItemMoney(list,fbsItemMap,moneyName);
		HashMap<String, Map<String,Object>> dtlMap = new HashMap<>();
		for(Map<String,Object> map : list){
			dtlMap.put(map.get("itemCode").toString(),map);
		}
		//计算公式
		for(Map<String,Object> map : list){
			FbsItemVO fbsItemVO = fbsItemMap.get(map.get("itemCode"));
			caluteRelation(fbsItemVO.getItemRelation(),dtlMap,moneyName,fbsItemMap,map);
		}
	}
	/**
	 * 计算期初期末
	 * @param list1
	 * @param list2
	 */
	public void calPeriodItemMoney(List<Map<String,Object>> list1, List<Map<String,Object>> list2, Map<String, FbsItemVO> fbsItemMap, String moneyName){
		BigDecimal endPeriodMoney = new BigDecimal(0);
		for(Map<String,Object> map1 : list1){
			FbsItemVO fbsItemVO1 = fbsItemMap.get(map1.get("itemCode"));
			if(fbsItemVO1.getIsEndPeriod().equals("1") && EmptyUtils.isEmpty(fbsItemVO1.getSuperItemCode())){
				endPeriodMoney = (BigDecimal) map1.get(moneyName);
			}
		}
		for(Map<String,Object> map2 : list2){
			FbsItemVO fbsItemVO2 = fbsItemMap.get(map2.get("itemCode"));
			if(fbsItemVO2.getIsBeginPeriod().equals("1") && EmptyUtils.isEmpty(fbsItemVO2.getSuperItemCode())){
				map2.put(moneyName,endPeriodMoney);
			}
		}
		//计算公式
		HashMap<String, Map<String,Object>> dtlMap = new HashMap<>();
		for(Map<String,Object> map : list2){
			dtlMap.put(map.get("itemCode").toString(),map);
		}
		for(Map<String,Object> map : list2){
			FbsItemVO fbsItemVO = fbsItemMap.get(map.get("itemCode"));
			caluteRelation(fbsItemVO.getItemRelation(),dtlMap,moneyName,fbsItemMap,map);
		}
	}
	/**
	 * 上级科目汇总计算
	 * @param list
	 * @param fbsItemMap
	 * @param moneyName
	 */
	public void calSuperItemMoney(List<Map<String,Object>> list, Map<String, FbsItemVO> fbsItemMap, String moneyName){
		//
		for(int i=0;i<list.size();i++){
			FbsItemVO fbsItemVO = fbsItemMap.get(list.get(i).get("itemCode"));
			BigDecimal totalMoney = new BigDecimal(0);
			int signSuper = fbsItemVO.getSign();
			//期末科目不做上级科目汇总
			if(fbsItemVO.getIsEndPeriod().equals("0")){
				for(int j=0;j<list.size();j++){
					FbsItemVO fbsItemVO1 = fbsItemMap.get(list.get(j).get("itemCode"));
					//如果是下级科目则计算
					if(fbsItemVO1.getItemCode().indexOf(fbsItemVO.getItemCode()) == 0){
						int sign = fbsItemVO1.getSign();
						if(sign == signSuper){
							totalMoney = totalMoney.add((BigDecimal) list.get(j).get(moneyName));
						}else{
							totalMoney = totalMoney.subtract((BigDecimal) list.get(j).get(moneyName));
						}
					}
				}
				list.get(i).put(moneyName,totalMoney);
			}
		}
	}
	/**
	 * 根据计算公式计算
	 * @param itemRelation
	 * @param dtlMap
	 * @param moneyName
	 * @param fbsItemMap
	 * @param map
	 * @return
	 */
	public void caluteRelation(String itemRelation, HashMap<String, Map<String,Object>> dtlMap, String moneyName, Map<String, FbsItemVO> fbsItemMap, Map<String,Object> map) {
		//获取科目计算公式
		if (StringUtils.isNotEmpty(itemRelation)) { //当存在自定义科目关系时，计算科目科目金额
			String itemRelation1 = itemRelation.replace("+", ";");
			itemRelation1 = itemRelation1.replace("-", ";");
			itemRelation1 = itemRelation1.replace("*", ";");
			itemRelation1 = itemRelation1.replace("/", ";");
			String[] itemCodes = itemRelation1.split(";");
			String itemRelation2 = itemRelation;
			for (int i = 0; i < itemCodes.length; i++) {
				itemRelation2 = itemRelation2.replace(itemCodes[i], ";");
			}
			String[] operators = itemRelation2.split(";");
			//金额计算
			BigDecimal bMoneyTotal = BigDecimal.ZERO;
			for (int i = 0; i < itemCodes.length - 1; i++) {
				String itemCode = itemCodes[i];//获取科目
				String itemCodeNext = itemCodes[i + 1];
				String operator = operators[i + 1];//获取运算符
				BigDecimal bMoneySuper = BigDecimal.ZERO;
				BigDecimal bMoneySuperNext = BigDecimal.ZERO;
				if (i != 0) {
					bMoneySuper = bMoneyTotal;
				} else {
					if (itemCode.indexOf("k") != -1) {
						itemCode = itemCode.replace("k", "");
						if (!dtlMap.containsKey(itemCode)) {
							bMoneySuper = BigDecimal.ZERO;
						} else {
							bMoneySuper = ((BigDecimal) dtlMap.get(itemCode).get(moneyName)).multiply(new BigDecimal(fbsItemMap.get(itemCode).getSign()));
						}
					} else {
						bMoneySuper = new BigDecimal(itemCode);
					}
				}
				if (itemCodeNext.indexOf("k") != -1) {
					itemCodeNext = itemCodeNext.replace("k", "");
					if (!dtlMap.containsKey(itemCodeNext)) {
						bMoneySuperNext = BigDecimal.ZERO;
					} else {
							bMoneySuperNext = ((BigDecimal) dtlMap.get(itemCodeNext).get(moneyName)).multiply(new BigDecimal(fbsItemMap.get(itemCodeNext).getSign()));
					}
				} else {
					bMoneySuperNext = new BigDecimal(itemCodeNext);
				}
				BigDecimal resultTemp = BigDecimal.ZERO;
				//根据运算符计算
				if ("+".equals(operator)) {
					resultTemp = bMoneySuper.add(bMoneySuperNext);
				} else if ("-".equals(operator)) {
					resultTemp = bMoneySuper.subtract(bMoneySuperNext);
				}
				bMoneyTotal = resultTemp;
			}
			map.put(moneyName,bMoneyTotal);
		}
	}
	/**
	 * 审批时保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudgetForApprove(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		FbsBudget entity = fbsBudgetService.get(input.getId(), true);
		entity.setStatus(input.getStatus());
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], entity.getTimeType());
			dtl.setbMoney(input.getbMoney()[i]);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i]);
			if (EmptyUtils.isEmpty(year)) {
				year = new FbsBudgetResolveYear();
			}
			BeanUtils.copyNotNullProperties(dtl, year);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				year.setId(null);
			} else {
				year.setId(input.getResolveId()[i]);
			}
			year.setBMoney1(input.getbMoney1()[i]);
			year.setBMoney2(input.getbMoney2()[i]);
			year.setBMoney3(input.getbMoney3()[i]);
			year.setBMoney4(input.getbMoney4()[i]);
			year.setBMoney5(input.getbMoney5()[i]);
			year.setBMoney6(input.getbMoney6()[i]);
			year.setBMoney7(input.getbMoney7()[i]);
			year.setBMoney8(input.getbMoney8()[i]);
			year.setBMoney9(input.getbMoney9()[i]);
			year.setBMoney10(input.getbMoney10()[i]);
			year.setBMoney11(input.getbMoney11()[i]);
			year.setBMoney12(input.getbMoney12()[i]);
			year.setCreateBy(user.getLoginName());
			year.setCreateDate(DateUtils.getDate());
			this.saveOrUpdate(year);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_APP_SAVE_SUCC;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_EDIT, msg, FbsMessageConstant.BUS_NAME_INPUT, user);
	}
	/**
	 * 资金计划汇总提交，下级单位计划处理
	 *
	 * @param entity
	 */
	public void saveFbsBudgetCollectUnderCorp(FbsBudget entity, ICurrentUser user) {
		//判断是否是汇总单位提交资金计划
		if (fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
			Integer status = entity.getStatus();
			if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
				//上级单位计划编制完成，下级单位计划状态也编制完成
				status = BudgetConstant.STATUE_CHECK;
			} else if (BudgetConstant.STATUE_WAIT_COLLECT.equals(entity.getStatus())) {
				//上级单位计划待汇总，下级单位计划状态为汇总中
				status = BudgetConstant.STATUE_WAIT_COLLECTED;
			} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
				//上级单位计划待审批，下级单位计划状态也汇总待审批
				status = BudgetConstant.STATUE_WAIT_CHECK_COLLECT;
			} else if (BudgetConstant.STATUE_BACK.equals(entity.getStatus())) {
				//上级单位计划审批打回，下级单位计划状态汇总审批打回 
				status = BudgetConstant.STATUE_COLLECT_BACK;
			} else {
				return;
			}
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(entity.getCorpId());
			//是否存在虚拟单位
			FbsCollectCorpVO vo = fbsBudgetService.listCorpByCorpId(entity.getCorpId());
			if(!EmptyUtils.isEmpty(vo)){
				FbsCorpBudgetDTO fbsCorpBudgetDTO = fbsCorpBudgetService.getByCorpId(vo.getCorpId());
				if(!EmptyUtils.isEmpty(fbsCorpBudgetDTO)) {
					fbsCorpBudgetDTO.setCorpId(vo.getId());
					fbsCorpBudgetDTO.setCollectFlag(1);
					list.add(fbsCorpBudgetDTO);
				}
			}
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO dto : list) {
					//过滤计划不控制单位
					if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
						continue;
					}
					//获取下级单位已上报计划，未上报计划，则初始化计划上报数据
					FbsBudget budget = fbsBudgetService.getCorpBudget(dto.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
					if (budget == null) {
						//初始化未编制资金计划的下级单位
						FbsBudgetVO fbsBudgetVO = fbsBudgetService.initBudget(dto.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate(), status, user);
						budget = new FbsBudget();
						BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
						//初始化追加信息
						this.initResolveYearBudget(fbsBudgetVO, user);
						//生成日志
						fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_AUTO_INIT, FbsMessageConstant.FBS_LOG_COLLECT_APP_INIT_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
					} else {
						//未汇总单位资金计划更新为零计划
						if (budget.getStatus() <= BudgetConstant.STATUE_WAIT_COLLECT) {
							budget.setCheckMoney(BigDecimal.ZERO);
							fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
							//更新年分解
							fbsBudgetResolveYearDao.clearInputMoney(budget.getId());
							//待审批状态的计划删除工作流信息
							if(BudgetConstant.STATUE_WAIT_CHECK.equals(budget.getStatus())){
								workFlowService.deleteRuntimeTaskByCategoryAndBusinessId(FbsWorkFlowConstant.FBS_BUDGET_INPUT, budget.getId());
							}
						}
						budget.setStatus(status);
						//上级单位编制完成，则更新下级单位的追加金额
						if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
							//生成日志
							fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_COLLECT_APP_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
						}
						fbsBudgetService.saveOrUpdate(budget);
					}
					//判断单位是否汇总下级单位
					if (fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId())) {
						//判断单位是否存在下级单位，如果存在下级单位，需要对下级单位做相同处理
						List<CorpDTO> listCorpTemp = corpService.listUnderCorpByParentCorpId(dto.getCorpId());
						if (listCorpTemp != null && listCorpTemp.size() > 0) {
							this.saveFbsBudgetCollectUnderCorp(budget, user);
						}
					}
				}
			}
		}
	}
	/**
	 * 生成12个月的月计划
	 *
	 * @param entity
	 */
	public void createBudgetAllMonths(FbsBudget entity, ICurrentUser user) {
		//获取年分解信息
		List<FbsBudgetResolveYear> listResolveYearBudget = this.listByBudgetId(entity.getId());
		try {
			//获取周期
			List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getMonthByYear(entity.getBeginDate());
			//循环生成月计划
			for (int i = 0; i < listPeriod.size(); i++) {
				FbsBudgetPeriodVO period = listPeriod.get(i);
				//计划上报生成
				FbsBudget budget = new FbsBudget();
				budget.setCorpId(entity.getCorpId());
				budget.setEnrolDate(DateUtils.getDate());
				budget.setBeginDate(period.getBeginDate());
				budget.setEndDate(period.getEndDate());
				budget.setTimeType(BudgetConstant.TIME_TYPE_MONTH);
				budget.setCheckMoney(BigDecimal.ZERO);
				budget.setStatus(BudgetConstant.STATUE_CHECK);
				budget.setCreateBy(user.getLoginName());
				budget.setCreateDate(DateUtils.getDate());
				//写入币别和汇率
				budget.setCurId(entity.getCurId());
				budget.setExchangeRate(entity.getExchangeRate());
				fbsBudgetService.save(budget);
				//生成日志
				fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_AUTO_INIT, "", FbsMessageConstant.BUS_NAME_INPUT, user);
				for (FbsBudgetResolveYear year : listResolveYearBudget) {
					//计划明细生成
					FbsBudgetDtlAbs dtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
					dtl.setBudgetId(budget.getId());
					dtl.setItemCode(year.getItemCode());
					dtl.setItemName(year.getItemName());
					Method method = year.getClass().getMethod("getBMoney" + (i + 1), new Class[0]);
					dtl.setbMoney((BigDecimal) method.invoke(year));
					dtl.setBsuperMoney(BigDecimal.ZERO);
					dtl.setBtempMoney(BigDecimal.ZERO);
					fbsBudgetDtlAbsService.save(dtl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//判断是否是汇总单位提交资金计划
		if (fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(entity.getCorpId());
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO dto : list) {
					//过滤计划不控制单位
					if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
						continue;
					}
					FbsBudget budget = fbsBudgetService.getCorpBudget(dto.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
					this.createBudgetAllMonths(budget, user);
				}
			}
		}
	}
	/**
	 * 资金计划删除
	 *
	 * @param id
	 * @param timeType
	 */
	public void deleteFbsBudget(String id, String timeType, ICurrentUser user) {
		//删除分解表
		fbsBudgetResolveYearDao.deleteByBudgetId(id);
		fbsBudgetService.deleteFbsBudget(id, timeType, user);
	}
	/**
	 * 汇总
	 *
	 * @param input
	 * @param childIds
	 */
	public void collectFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
		List<FbsBudget> childBudgetList = fbsBudgetService.collectFbsBudget(input, childIds, user);
		List<String> childBudgetIds = new ArrayList<String>();
		//获取下级单位计划ID集合
		for (FbsBudget budget : childBudgetList) {
			childBudgetIds.add(budget.getId());
		}
		String budgetAuthSql = SqlUtil.getInSql(childBudgetIds, "b.budget_id", true);
		//汇总下级单位计划分解上报数据
		List<FbsBudgetResolveYear> list = fbsBudgetResolveYearDao.collectByBudgetIds(budgetAuthSql);
		//获取汇总单位计划分解
		List<FbsBudgetResolveYear> listYear = this.listByBudgetId(input.getId());
		for (FbsBudgetResolveYear year : listYear) {
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsBudgetResolveYear entity : list) {
					if (year.getItemCode().equals(entity.getItemCode())) {
						year.setBMoney(year.getBMoney().add(entity.getBMoney()));
						year.setBMoney1(year.getBMoney1().add(entity.getBMoney1()));
						year.setBMoney2(year.getBMoney2().add(entity.getBMoney2()));
						year.setBMoney3(year.getBMoney3().add(entity.getBMoney3()));
						year.setBMoney4(year.getBMoney4().add(entity.getBMoney4()));
						year.setBMoney5(year.getBMoney5().add(entity.getBMoney5()));
						year.setBMoney6(year.getBMoney6().add(entity.getBMoney6()));
						year.setBMoney7(year.getBMoney7().add(entity.getBMoney7()));
						year.setBMoney8(year.getBMoney8().add(entity.getBMoney8()));
						year.setBMoney9(year.getBMoney9().add(entity.getBMoney9()));
						year.setBMoney10(year.getBMoney10().add(entity.getBMoney10()));
						year.setBMoney11(year.getBMoney11().add(entity.getBMoney11()));
						year.setBMoney12(year.getBMoney12().add(entity.getBMoney12()));
						year.setUpdateBy(user.getLoginName());
						year.setUpdateDate(DateUtils.getDate());
					}
				}
			}
		}
	}
	/**
	 * 计划上报-审批通过
	 *
	 * @param entity
	 * @param result
	 */
	public void approvePassEndInput(FbsBudget entity, boolean result, ICurrentUser user) {
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS;
		if (result) {
			//判断单位是否需要汇总
			if (fbsCorpBudgetService.getCollectFlag(entity.getCorpId())) {
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
			} else {
				entity.setStatus(BudgetConstant.STATUE_CHECK);//计划编制完成
			}
			//如果是汇总单位虚拟单位需要汇总
			FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpById(entity.getCorpId());
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
			}
		} else {
			entity.setStatus(BudgetConstant.STATUE_BACK);//打回
			messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		}
		fbsBudgetService.saveOrUpdate(entity);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(entity, user);
		if (result) {
			//判断参数年分解月（月是否上报计划）：0-否 1-是
			String yimodeM = configService.getConfig("fbs", "yimodeM").getValue();
			if ("0".equals(yimodeM) && BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
				//生成12个月的月计划
				this.createBudgetAllMonths(entity, user);
			}
		}
		String message = result ? FbsMessageConstant.FBS_LOG_APP_SUCC : FbsMessageConstant.FBS_LOG_APP_BACK;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, message, FbsMessageConstant.BUS_NAME_INPUT, user);
	}
	/**
	 * 可上报/可追加金额计算
	 *
	 * @param fbsBudget
	 * @param target    input-上报  super-追加
	 */
	public void calculateCanBMoney(FbsBudgetVO fbsBudget, String target) {
		//不存在父计划，不计算可上报/可追加金额计算
		if (StringUtils.isEmpty(fbsBudget.getParentTimeType())) {
			return;
		}
		//获取父计划周期开始
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudget.getBeginDate(), fbsBudget.getParentTimeType());
		//获取父计划
		FbsBudget parentBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getParentTimeType(), fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		if (parentBudget == null || parentBudget.getStatus() < BudgetConstant.STATUE_CHECK) {
			return;
		}
		//当前上报月份
		int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		//获取年计划分解信息
		List<FbsBudgetResolveYear> listResolve = this.listByBudgetId(parentBudget.getId());
		HashMap<String, BigDecimal> resolveMap = new HashMap<String, BigDecimal>();
		for (FbsBudgetResolveYear resolve : listResolve) {
			try {
				Method method = resolve.getClass().getMethod("getBMoney" + month, new Class[0]);
				BigDecimal bMoney = (BigDecimal) method.invoke(resolve);
				Method method1 = resolve.getClass().getMethod("getBsuperMoney" + month, new Class[0]);
				BigDecimal bsuperMoney = (BigDecimal) method1.invoke(resolve);
				resolveMap.put(resolve.getItemCode(), bMoney.add(bsuperMoney));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//分解计划上报余额控制方式  0-分解金额;1-分解金额+前期未上报余额;2-分解金额+前期未执行余额
		String inputBlance = this.configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_INPUTBLANCE).getValue();
		if ("0".equals(inputBlance)) {
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				BigDecimal resolveMoney = resolveMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : resolveMap.get(dtl.getItemCode());
				dtl.setCanBMoney(resolveMoney);//0-分解金额
				//追加时，获取留存金额
				if ("super".equals(target)) {
					dtl.setCanBMoney(dtl.getCanBMoney().subtract(dtl.getbMoney()).subtract(dtl.getBsuperMoney()));//减去本期已上报
					//获取科目执行金额
					BigDecimal processMoney = fbsBudgetProcessService.collectProcessMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
					dtl.setRemainBMoney(dtl.getbMoney().add(dtl.getBsuperMoney()).subtract(processMoney));//留存金额 = 上报金额+追加金额-已执行金额
				}
			}
		} else if ("1".equals(inputBlance)) {
			//获取前期上报的金额
			List<FbsBudget> preBudgetList = fbsBudgetService.getPreCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), parentBudget.getBeginDate(), fbsBudget.getBeginDate());
			List<String> preBudgetIds = new ArrayList<String>();
			//获取下级单位计划ID集合
			if (!EmptyUtils.isEmptyList(preBudgetList)) {
				for (FbsBudget budget : preBudgetList) {
					preBudgetIds.add(budget.getId());
				}
			}
			String budgetAuthSql = SqlUtil.getInSql(preBudgetIds, "b.budget_id", true);
			List<FbsBudgetDtlAbsVO> preDtlist = fbsBudgetDtlAbsService.collectByBudgetIdsNormal(fbsBudget.getTimeType(), budgetAuthSql);
			HashMap<String, BigDecimal> preDtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(preDtlist)) {
				for (FbsBudgetDtlAbsVO dtl : preDtlist) {
					preDtlMap.put(dtl.getItemCode(), dtl.getbMoney().add(dtl.getBsuperMoney()));//上报总金额 = 上报金额+追加金额（已通过） 
				}
			}
			//获取前期未上报余额
			HashMap<String, BigDecimal> preDtlInputBalMap = new HashMap<String, BigDecimal>();
			for (FbsBudgetResolveYear resolve : listResolve) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < month; i++) {
						Method method = resolve.getClass().getMethod("getBMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method.invoke(resolve));
						Method method1 = resolve.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method1.invoke(resolve));
					}
					BigDecimal preInputBal = preDtlMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlMap.get(resolve.getItemCode());
					preDtlInputBalMap.put(resolve.getItemCode(), result.subtract(preInputBal));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				BigDecimal resolveMoney = resolveMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : resolveMap.get(dtl.getItemCode());
				BigDecimal inputBal = preDtlInputBalMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : preDtlInputBalMap.get(dtl.getItemCode());
				dtl.setCanBMoney(resolveMoney.add(inputBal));//1-分解金额+前期未上报余额
				//追加时，获取留存金额
				if ("super".equals(target)) {
					dtl.setCanBMoney(dtl.getCanBMoney().subtract(dtl.getbMoney()).subtract(dtl.getBsuperMoney()));//减去本期已上报
					//获取科目执行金额
					BigDecimal processMoney = fbsBudgetProcessService.collectProcessMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
					dtl.setRemainBMoney(dtl.getbMoney().add(dtl.getBsuperMoney()).subtract(processMoney));//留存金额 = 上报金额+追加金额-已执行金额
				}
			}
		} else if ("2".equals(inputBlance)) {
			//获取前期未执行的余额
			HashMap<String, BigDecimal> preDtlProcessBalMap = new HashMap<String, BigDecimal>();
			for (FbsBudgetResolveYear resolve : listResolve) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < month; i++) {
						Method method = resolve.getClass().getMethod("getBMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method.invoke(resolve));
						Method method1 = resolve.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method1.invoke(resolve));
					}
					//获取科目执行金额
					BigDecimal preProcessMoney = fbsBudgetProcessService.collectProcessMoney(parentBudget.getBeginDate(), DateUtils.addDay(fbsBudget.getBeginDate(), -1), resolve.getItemCode(), fbsBudget.getCorpId());
					preDtlProcessBalMap.put(resolve.getItemCode(), result.subtract(preProcessMoney));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				BigDecimal resolveMoney = resolveMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : resolveMap.get(dtl.getItemCode());
				BigDecimal processBal = preDtlProcessBalMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : preDtlProcessBalMap.get(dtl.getItemCode());
				dtl.setCanBMoney(resolveMoney.add(processBal));//2-分解金额+前期未执行余额
				//追加时，获取留存金额
				if ("super".equals(target)) {
					dtl.setCanBMoney(dtl.getCanBMoney().subtract(dtl.getbMoney()).subtract(dtl.getBsuperMoney()));//减去本期已上报
					//获取科目执行金额
					BigDecimal processMoney = fbsBudgetProcessService.collectProcessMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
					dtl.setRemainBMoney(dtl.getbMoney().add(dtl.getBsuperMoney()).subtract(processMoney));//留存金额 = 上报金额+追加金额-已执行金额
				}
			}
		}
	}
	/**
	 * 资金计划打回-上报
	 *
	 * @param entity
	 * @param backMsg
	 */
	public void backFbsBudget(FbsBudget entity, String backMsg, ICurrentUser user) {
		FbsBudget preCorpBudget = fbsBudgetService.backFbsBudget(entity, backMsg, user);
		//扣减汇总单位分解金额
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			this.subtractFbsBudgetResolve(entity, preCorpBudget, user);
		}
	}
	/**
	 * 汇总打回/编辑扣减汇总单位计划上报数据
	 *
	 * @param entity        子单位计划
	 * @param preCorpBudget 汇总单位计划
	 */
	public void subtractFbsBudgetResolve(FbsBudget entity, FbsBudget preCorpBudget, ICurrentUser user) {
		//汇总下级单位计划分解上报数据
		List<FbsBudgetResolveYear> corpList = this.listByBudgetId(entity.getId());
		//获取汇总单位计划分解
		List<FbsBudgetResolveYear> preCorpList = this.listByBudgetId(preCorpBudget.getId());
		for (FbsBudgetResolveYear year : preCorpList) {
			for (FbsBudgetResolveYear corpYear : corpList) {
				if (year.getItemCode().equals(corpYear.getItemCode())) {
					year.setBMoney(year.getBMoney().subtract(corpYear.getBMoney()));
					year.setBMoney1(year.getBMoney1().subtract(corpYear.getBMoney1()));
					year.setBMoney2(year.getBMoney2().subtract(corpYear.getBMoney2()));
					year.setBMoney3(year.getBMoney3().subtract(corpYear.getBMoney3()));
					year.setBMoney4(year.getBMoney4().subtract(corpYear.getBMoney4()));
					year.setBMoney5(year.getBMoney5().subtract(corpYear.getBMoney5()));
					year.setBMoney6(year.getBMoney6().subtract(corpYear.getBMoney6()));
					year.setBMoney7(year.getBMoney7().subtract(corpYear.getBMoney7()));
					year.setBMoney8(year.getBMoney8().subtract(corpYear.getBMoney8()));
					year.setBMoney9(year.getBMoney9().subtract(corpYear.getBMoney9()));
					year.setBMoney10(year.getBMoney10().subtract(corpYear.getBMoney10()));
					year.setBMoney11(year.getBMoney11().subtract(corpYear.getBMoney11()));
					year.setBMoney12(year.getBMoney12().subtract(corpYear.getBMoney12()));
					year.setUpdateBy(user.getLoginName());
					year.setUpdateDate(DateUtils.getDate());
					this.getEntityDao().evict(corpYear);
				}
			}
		}
		//计划明细扣减
		fbsBudgetDetailService.repulseDeatil(entity);
	}
	/**
	 * 获取年计划分解
	 *
	 * @param budget
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<FbsBudgetResolveYearVO> getResolveYearBudgetBySuperadd(FbsBudgetVO budget, ICurrentUser user) {
		List<FbsBudgetResolveYearVO> listBudget = new ArrayList<FbsBudgetResolveYearVO>();
		if (EmptyUtils.isEmptyList(budget.getDtlList())) {
			return listBudget;
		}
		//获取年分解信息
		List<FbsBudgetResolveYear> listResolveYearBudget = this.listByBudgetId(budget.getId());
		int month = DateUtils.getMonth(DateUtils.getDate());
		//分解计划上报余额控制方式  0-分解金额;1-分解金额+前期未上报余额;2-分解金额+前期未执行余额
		String inputBlance = this.configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_INPUTBLANCE).getValue();
		Date beginStartDate = DateUtils.getFirstYearDate(DateUtils.getDate());
		Date beginEndDate = DateUtils.getLastMonthDate(DateUtils.addMonth(DateUtils.getDate(), -1));
		//获取前期未上报余额
		HashMap<String, BigDecimal> preDtlInputBalMap = new HashMap<String, BigDecimal>();
		//获取前期未执行的余额
		HashMap<String, BigDecimal> preDtlProcessBalMap = new HashMap<String, BigDecimal>();
		if ("1".equals(inputBlance)) {
			//获取前期上报的金额
			List<FbsBudget> monthList = fbsBudgetService.listBudgetByBeginDate(budget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, beginStartDate, beginEndDate);
			List<String> preBudgetIds = new ArrayList<String>();
			//获取下级单位计划ID集合
			if (!EmptyUtils.isEmptyList(monthList)) {
				for (FbsBudget monthBudget : monthList) {
					preBudgetIds.add(monthBudget.getId());
				}
			}
			String budgetAuthSql = SqlUtil.getInSql(preBudgetIds, "b.budget_id", true);
			List<FbsBudgetDtlAbsVO> preDtlist = fbsBudgetDtlAbsService.collectByBudgetIds(BudgetConstant.TIME_TYPE_MONTH, budgetAuthSql);
			HashMap<String, BigDecimal> preDtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(preDtlist)) {
				for (FbsBudgetDtlAbsVO dtl : preDtlist) {
					preDtlMap.put(dtl.getItemCode(), dtl.getbMoney().add(dtl.getBsuperMoney()));//上报总金额 = 上报金额+追加金额（已通过）
				}
			}
			for (FbsBudgetResolveYear resolve : listResolveYearBudget) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < month; i++) {
						Method method = resolve.getClass().getMethod("getBMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method.invoke(resolve));
						Method method1 = resolve.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method1.invoke(resolve));
					}
					BigDecimal preInputBal = preDtlMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlMap.get(resolve.getItemCode());
					preDtlInputBalMap.put(resolve.getItemCode(), result.subtract(preInputBal));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if ("2".equals(inputBlance)) {
			for (FbsBudgetResolveYear resolve : listResolveYearBudget) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < month; i++) {
						Method method = resolve.getClass().getMethod("getBMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method.invoke(resolve));
						Method method1 = resolve.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
						result = result.add((BigDecimal) method1.invoke(resolve));
					}
					//获取科目执行金额
					BigDecimal preProcessMoney = fbsBudgetProcessService.collectProcessMoneyByStatus(beginStartDate, beginEndDate, resolve.getItemCode(), budget.getCorpId());
					preDtlProcessBalMap.put(resolve.getItemCode(), result.subtract(preProcessMoney));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
				for (FbsBudgetResolveYear resolve : listResolveYearBudget) {
					FbsBudgetResolveYearVO entity = new FbsBudgetResolveYearVO();
					BeanUtils.copyNotNullProperties(resolve, entity);
					for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
						if (resolve.getItemCode().equals(dtl.getItemCode())) {
							entity.setDetailId(dtl.getId());
							entity.setItem(dtl.getItem());
						}
					}
					//新增或更新追加表
					FbsBudgetSuperadd superadd = fbsBudgetSuperaddService.get(budget.getSuperaddId());
					if(!EmptyUtils.isEmpty(superadd)){
						List<FbsBudgetSuperaddDetail> fbsBudgetSuperaddDetails = fbsBudgetSuperaddDetailService.listBySuperaddId(superadd.getId());
						for(FbsBudgetSuperaddDetail fbsBudgetSuperaddDetail : fbsBudgetSuperaddDetails){
							if(resolve.getItemCode().equals(fbsBudgetSuperaddDetail.getItemCode())){
								entity.setSuperaddDtlId(fbsBudgetSuperaddDetail.getId());
								entity.setRmk(fbsBudgetSuperaddDetail.getRmk());
							}
						}
					}
					HashMap<Integer, BigDecimal> btempMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> remainBMoneyMap = new HashMap<Integer, BigDecimal>();
					for (int i = 1; i <= 12; i++) {
						Method method = entity.getClass().getMethod("getBtempMoney" + i, new Class[0]);
						BigDecimal btempMoney = EmptyUtils.isEmpty(method.invoke(entity)) ? BigDecimal.ZERO : (BigDecimal) method.invoke(entity);
						btempMoneyMap.put(i, btempMoney);
						Date beginDate = BudgetUtils.getFirstMonthDateOfYear(budget.getBeginDate(), i);
						Date endDate = BudgetUtils.getLastMonthDateOfYear(budget.getBeginDate(), i);
						FbsBudget fbsBudget = this.fbsBudgetService.getCorpBudget(budget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, beginDate, endDate);
						BigDecimal canBMoney = BigDecimal.ZERO;
						BigDecimal processMoney = BigDecimal.ZERO;
						if (!EmptyUtils.isEmpty(fbsBudget)) {
							FbsBudgetDtlAbs dtl = this.fbsBudgetDtlAbsService.getByBudgetId(fbsBudget.getId(), resolve.getItemCode(), BudgetConstant.TIME_TYPE_MONTH);
							if (!EmptyUtils.isEmpty(dtl)) {
								canBMoney = dtl.getbMoney().add(dtl.getBsuperMoney());
							}
							//获取科目执行金额
							processMoney = fbsBudgetProcessService.collectProcessMoneyByStatus(beginDate, endDate, resolve.getItemCode(), budget.getCorpId());
						}
						if (i == month) {
							if ("1".equals(inputBlance)) {
								BigDecimal inputBal = preDtlInputBalMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlInputBalMap.get(resolve.getItemCode());
								//留存金额 = 上报金额+追加金额-已执行金额+前期未上报余额
								remainBMoneyMap.put(i, canBMoney.subtract(processMoney).add(inputBal));
							} else if ("2".equals(inputBlance)) {
								BigDecimal processBal = preDtlProcessBalMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlProcessBalMap.get(resolve.getItemCode());
								//留存金额 = 上报金额+追加金额-已执行金额+前期未执行的余额
								remainBMoneyMap.put(i, canBMoney.subtract(processMoney).add(processBal));
							} else {
								//留存金额 = 上报金额+追加金额-已执行金额
								remainBMoneyMap.put(i, canBMoney.subtract(processMoney));
							}
						} else {
							//留存金额 = 上报金额+追加金额-已执行金额
							remainBMoneyMap.put(i, canBMoney.subtract(processMoney));
						}
					}
					entity.setBtempMoneyMap(btempMoneyMap);
					entity.setRemainBMoneyMap(remainBMoneyMap);
					listBudget.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBudget;
	}
	/**
	 * 资金计划打回-追加
	 *
	 * @param entity
	 * @param superAdd
	 * @param backMsg
	 */
	public void backFbsBudgetSuperAdd(FbsBudget entity, FbsBudgetSuperadd superAdd, String backMsg, ICurrentUser user) {
		//汇总单位计划扣减
		this.subtractFbsBudgetSuperAdd(entity, superAdd);
		//更新计划上报
		entity.setStatus(BudgetConstant.STATUE_SUPERADD_BACK);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.fbsBudgetService.saveOrUpdate(entity);
		//更新计划追加
		superAdd.setStatus(BudgetConstant.STATUE_BACK);
		superAdd.setUpdateBy(user.getLoginName());
		superAdd.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(superAdd);
		//生成日志
		fbsMessageService.saveMessage(superAdd.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT_BACK, backMsg, FbsMessageConstant.BUS_NAME_SUPERADD, user);
		//汇总打回消息写入
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			notificationService.send(entity.getId(), entity.getCorpId(), tag, entity);
		}
	}
	/**
	 * 汇总打回/编辑扣减汇总单位计划上报数据
	 *
	 * @param entity
	 */
	public FbsBudget subtractFbsBudgetSuperAdd(FbsBudget entity, FbsBudgetSuperadd superAdd) {
		FbsBudget preCorpBudget = null;
		//汇总后打回，需扣减汇总单位上报金额
		if (BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT < entity.getStatus()) {
			//获取计划明细
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
			HashMap<String, BigDecimal> dtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(dtlList)) {
				for (FbsBudgetDtlAbsVO dtl : dtlList) {
					dtlMap.put(dtl.getItemCode(), dtl.getBtempMoney());
				}
			}
			//获取上级单位
			CorpDTO preCorp = corpService.getParentById(entity.getCorpId());
			//获取上级单位计划上报
			preCorpBudget = this.fbsBudgetService.getCorpBudget(preCorp.getId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
			List<FbsBudgetDtlAbsVO> preCorpDtlList = fbsBudgetDtlAbsService.listByBudgetId(preCorpBudget.getId(), preCorpBudget.getTimeType());
			if (!EmptyUtils.isEmptyList(preCorpDtlList)) {
				for (FbsBudgetDtlAbsVO preDtl : preCorpDtlList) {
					if (dtlMap.containsKey(preDtl.getItemCode())) {
						preDtl.setBtempMoney(preDtl.getBtempMoney().subtract(dtlMap.get(preDtl.getItemCode())));
						FbsBudgetDtlAbs budgetDtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(preCorpBudget.getTimeType());
						BeanUtils.copyNotNullProperties(preDtl, budgetDtl);
						fbsBudgetDtlAbsService.saveOrUpdate(budgetDtl);
					}
				}
			}
			//计划明细扣减
			fbsBudgetDetailService.repulseDeatil(entity);
			//获取追加明细
			List<FbsBudgetSuperaddDetailVO> dtlSuperList = fbsBudgetSuperaddDetailService.listVOBySuperaddId(superAdd.getId());
			HashMap<String, BigDecimal> dtlSuperMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(dtlSuperList)) {
				for (FbsBudgetSuperaddDetailVO dtl : dtlSuperList) {
					dtlSuperMap.put(dtl.getItemCode(), dtl.getBsuperMoney());
				}
			}
			//获取上级单位计划追加明细
			FbsBudgetSuperadd preCorpSuperBudget = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(preCorpBudget.getId());
			List<FbsBudgetSuperaddDetailVO> preCorpSuperDtlList = fbsBudgetSuperaddDetailService.listVOBySuperaddId(preCorpSuperBudget.getId());
			if (!EmptyUtils.isEmptyList(preCorpSuperDtlList)) {
				for (FbsBudgetSuperaddDetailVO preDtl : preCorpSuperDtlList) {
					if (dtlSuperMap.containsKey(preDtl.getItemCode())) {
						preDtl.setBsuperMoney(preDtl.getBsuperMoney().subtract(dtlSuperMap.get(preDtl.getItemCode())));
						fbsBudgetSuperaddDetailService.updateSuperMoneyBySuperaddId(preDtl);
					}
				}
			}
		}
		return preCorpBudget;
	}
	/**
	 * 资金计划追加保存
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAdd(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.fbsBudgetService.get(input.getId(), true);
		//新增或更新追加表
		FbsBudgetSuperadd entity = fbsBudgetSuperaddService.get(input.getSuperaddId());
		//如果是汇总单位编辑计划，需要扣减汇总单位的追加金额，同时状态更新为计划追加待汇总状态
		FbsBudget preCorpBudget = this.subtractFbsBudgetSuperAdd(budget, entity);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT);
		} else {
			if (BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
				budget.setStatus(BudgetConstant.STATUE_SUPERADD_TEMP);//计划追加录入
			} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(input.getStatus())) {
				budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_CHECK);//计划追加审批中
			}
		}
		boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.fbsBudgetService.saveOrUpdate(budget);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
		} else {
			entity.setStatus(input.getStatus());
		}
		entity.setAttachmentId(input.getAttachmentId());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		BigDecimal checkMoney = BigDecimal.ZERO;
		Date curDate = DateUtils.getDate();
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//更新计划上报明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			dtl.setBtempMoney(input.getBtempMoney()[i]);//更新追加金额
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getBtempMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i], true);
			year.setBtempMoney(input.getBtempMoney()[i]);
			year.setBtempMoney1(input.getBtempMoney1()[i]);
			year.setBtempMoney2(input.getBtempMoney2()[i]);
			year.setBtempMoney3(input.getBtempMoney3()[i]);
			year.setBtempMoney4(input.getBtempMoney4()[i]);
			year.setBtempMoney5(input.getBtempMoney5()[i]);
			year.setBtempMoney6(input.getBtempMoney6()[i]);
			year.setBtempMoney7(input.getBtempMoney7()[i]);
			year.setBtempMoney8(input.getBtempMoney8()[i]);
			year.setBtempMoney9(input.getBtempMoney9()[i]);
			year.setBtempMoney10(input.getBtempMoney10()[i]);
			year.setBtempMoney11(input.getBtempMoney11()[i]);
			year.setBtempMoney12(input.getBtempMoney12()[i]);
			year.setUpdateBy(user.getLoginName());
			year.setUpdateDate(curDate);
			this.saveOrUpdate(year);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = fbsBudgetSuperaddDetailService.get(input.getSuperaddDtlId()[i], true);
			if (EmptyUtils.isEmpty(superaddDtl)) {
				superaddDtl = new FbsBudgetSuperaddDetail();
			}
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(input.getBtempMoney()[i]);
			superaddDtl.setRmk(input.getRmk()[i]);
			fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(budget, entity, user);
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_TEMP;
		String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : isEdit ? FbsMessageConstant.FBS_LOG_ADD_TEMPSAVE_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_TEMPSAVE_SUCC;
		if (CommonConstant.YES.equalsIgnoreCase(input.getIsCollect())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_COLLECT_EDIT;
			msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_COLLECT_EDIT;
		}
		//提交时调用审批方法
		if (BudgetConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_SUBMIT;
			msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : isEdit ? FbsMessageConstant.FBS_LOG_ADD_SUBMIT_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_SUBMIT_SUCC;
			FbsBudgetSuperaddVO superaddVO = new FbsBudgetSuperaddVO();
			BeanUtils.copyNotNullProperties(entity, superaddVO);
			superaddVO.setTimeType(budget.getTimeType());
			superaddVO.setCorpName(budget.getCorpName());
			//调用审批后方法
			 getStartHandler().handler(FbsWorkFlowConstant.FBS_BUDGET_SUPERADD, entity.getId(), superaddVO, entity.getCorpId(), user.getId());
		}
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, msg, FbsMessageConstant.BUS_NAME_SUPERADD, user);
		//完成消息通知
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(budget.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(budget.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(budget.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}
	/**
	 * 导入时后台计算
	 * @param input
	 */
	public void saveFbsBudgetSuperAddForImport(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.fbsBudgetService.get(input.getId(), true);
		//新增或更新追加表
		FbsBudgetSuperadd entity = fbsBudgetSuperaddService.get(input.getSuperaddId());
		if (BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_TEMP);//计划追加录入
		} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_CHECK);//计划追加审批中
		}
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.fbsBudgetService.saveOrUpdate(budget);
		entity.setStatus(input.getStatus());
		entity.setAttachmentId(input.getAttachmentId());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		BigDecimal checkMoney = BigDecimal.ZERO;
		String loginName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		List<Map<String,Object>> list1 = new ArrayList<>();
		List<Map<String,Object>> list2 = new ArrayList<>();
		List<Map<String,Object>> list3 = new ArrayList<>();
		List<Map<String,Object>> list4 = new ArrayList<>();
		List<Map<String,Object>> list5 = new ArrayList<>();
		List<Map<String,Object>> list6 = new ArrayList<>();
		List<Map<String,Object>> list7 = new ArrayList<>();
		List<Map<String,Object>> list8 = new ArrayList<>();
		List<Map<String,Object>> list9 = new ArrayList<>();
		List<Map<String,Object>> list10 = new ArrayList<>();
		List<Map<String,Object>> list11 = new ArrayList<>();
		List<Map<String,Object>> list12 = new ArrayList<>();
		Map<String, FbsItemVO> fbsItemMap = new HashMap<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			Map<String,Object> map1 = new HashMap<>();
			Map<String,Object> map2 = new HashMap<>();
			Map<String,Object> map3 = new HashMap<>();
			Map<String,Object> map4 = new HashMap<>();
			Map<String,Object> map5 = new HashMap<>();
			Map<String,Object> map6 = new HashMap<>();
			Map<String,Object> map7 = new HashMap<>();
			Map<String,Object> map8 = new HashMap<>();
			Map<String,Object> map9 = new HashMap<>();
			Map<String,Object> map10 = new HashMap<>();
			Map<String,Object> map11 = new HashMap<>();
			Map<String,Object> map12 = new HashMap<>();
			map1.put("itemCode",dtl.getItemCode());
			map2.put("itemCode",dtl.getItemCode());
			map3.put("itemCode",dtl.getItemCode());
			map4.put("itemCode",dtl.getItemCode());
			map5.put("itemCode",dtl.getItemCode());
			map6.put("itemCode",dtl.getItemCode());
			map7.put("itemCode",dtl.getItemCode());
			map8.put("itemCode",dtl.getItemCode());
			map9.put("itemCode",dtl.getItemCode());
			map10.put("itemCode",dtl.getItemCode());
			map11.put("itemCode",dtl.getItemCode());
			map12.put("itemCode",dtl.getItemCode());
			map1.put("bTempMoney",input.getBtempMoney1()[i]);
			map2.put("bTempMoney",input.getBtempMoney2()[i]);
			map3.put("bTempMoney",input.getBtempMoney3()[i]);
			map4.put("bTempMoney",input.getBtempMoney4()[i]);
			map5.put("bTempMoney",input.getBtempMoney5()[i]);
			map6.put("bTempMoney",input.getBtempMoney6()[i]);
			map7.put("bTempMoney",input.getBtempMoney7()[i]);
			map8.put("bTempMoney",input.getBtempMoney8()[i]);
			map9.put("bTempMoney",input.getBtempMoney9()[i]);
			map10.put("bTempMoney",input.getBtempMoney10()[i]);
			map11.put("bTempMoney",input.getBtempMoney11()[i]);
			map12.put("bTempMoney",input.getBtempMoney12()[i]);
			list1.add(map1);
			list2.add(map2);
			list3.add(map3);
			list4.add(map4);
			list5.add(map5);
			list6.add(map6);
			list7.add(map7);
			list8.add(map8);
			list9.add(map9);
			list10.add(map10);
			list11.add(map11);
			list12.add(map12);
			fbsItemMap.put(dtl.getItemCode(),fbsItemService.getFbsItemVO(null,dtl.getItemCode()));
		}
		//计算上报金额
		calItemMoney(list1,fbsItemMap,"bTempMoney");
		calItemMoney(list2,fbsItemMap,"bTempMoney");
		calItemMoney(list3,fbsItemMap,"bTempMoney");
		calItemMoney(list4,fbsItemMap,"bTempMoney");
		calItemMoney(list5,fbsItemMap,"bTempMoney");
		calItemMoney(list6,fbsItemMap,"bTempMoney");
		calItemMoney(list7,fbsItemMap,"bTempMoney");
		calItemMoney(list8,fbsItemMap,"bTempMoney");
		calItemMoney(list9,fbsItemMap,"bTempMoney");
		calItemMoney(list10,fbsItemMap,"bTempMoney");
		calItemMoney(list11,fbsItemMap,"bTempMoney");
		calItemMoney(list12,fbsItemMap,"bTempMoney");
		//计算期初期末
		calPeriodItemMoney(list1,list2,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list2,list3,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list3,list4,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list4,list5,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list5,list6,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list6,list7,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list7,list8,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list8,list9,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list9,list10,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list10,list11,fbsItemMap,"bTempMoney");
		calPeriodItemMoney(list11,list12,fbsItemMap,"bTempMoney");
		//计算年计划
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			FbsItemVO fbsItemVO = fbsItemMap.get(dtl.getItemCode());
			BigDecimal bTempMoney = new BigDecimal(0);
			//不为期初期末则计算和
			if(fbsItemVO.getIsBeginPeriod().equals("0") && fbsItemVO.getIsEndPeriod().equals("0")) {
				bTempMoney = bTempMoney.add((BigDecimal) list1.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list2.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list3.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list4.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list5.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list6.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list7.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list8.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list9.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list10.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list11.get(i).get("bTempMoney"));
				bTempMoney = bTempMoney.add((BigDecimal) list12.get(i).get("bTempMoney"));
			}
			//期初则1月份期初
			if(fbsItemVO.getIsBeginPeriod().equals("1")){
				bTempMoney = (BigDecimal) list1.get(i).get("bTempMoney");
			}
			//期末则为12月份期末
			if(fbsItemVO.getIsEndPeriod().equals("1")){
				bTempMoney = (BigDecimal) list12.get(i).get("bTempMoney");
			}
			dtl.setBtempMoney(bTempMoney);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getBtempMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i]);
			if (EmptyUtils.isEmpty(year)) {
				year = new FbsBudgetResolveYear();
			}
			BeanUtils.copyNotNullProperties(dtl, year);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				year.setId(null);
			} else {
				year.setId(input.getResolveId()[i]);
			}
			year.setBtempMoney(bTempMoney);
			year.setBtempMoney1((BigDecimal) list1.get(i).get("bTempMoney"));
			year.setBtempMoney2((BigDecimal) list2.get(i).get("bTempMoney"));
			year.setBtempMoney3((BigDecimal) list3.get(i).get("bTempMoney"));
			year.setBtempMoney4((BigDecimal) list4.get(i).get("bTempMoney"));
			year.setBtempMoney5((BigDecimal) list5.get(i).get("bTempMoney"));
			year.setBtempMoney6((BigDecimal) list6.get(i).get("bTempMoney"));
			year.setBtempMoney7((BigDecimal) list7.get(i).get("bTempMoney"));
			year.setBtempMoney8((BigDecimal) list8.get(i).get("bTempMoney"));
			year.setBtempMoney9((BigDecimal) list9.get(i).get("bTempMoney"));
			year.setBtempMoney10((BigDecimal) list10.get(i).get("bTempMoney"));
			year.setBtempMoney11((BigDecimal) list11.get(i).get("bTempMoney"));
			year.setBtempMoney12((BigDecimal) list12.get(i).get("bMoney"));
			year.setUpdateBy(loginName);
			year.setUpdateDate(curDate);
			this.saveOrUpdate(year);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = fbsBudgetSuperaddDetailService.get(input.getSuperaddDtlId()[i], true);
			if (EmptyUtils.isEmpty(superaddDtl)) {
				superaddDtl = new FbsBudgetSuperaddDetail();
			}
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(bTempMoney);
			fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
	}
	/**
	 * 审批时资金计划追加保存
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAddForApprove(FbsBudgetInputResolveYearVO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.fbsBudgetService.get(input.getId(), true);
		if (BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_TEMP);//计划追加录入
		} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_CHECK);//计划追加审批中
		}
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.fbsBudgetService.saveOrUpdate(budget);
		//新增或更新追加表
		FbsBudgetSuperadd entity = fbsBudgetSuperaddService.get(input.getSuperaddId());
		entity.setStatus(input.getStatus());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		List<FbsBudgetSuperaddDetail> dtlList = new ArrayList<FbsBudgetSuperaddDetail>();
		BigDecimal checkMoney = BigDecimal.ZERO;
		Date curDate = DateUtils.getDate();
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//更新计划上报明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			dtl.setBtempMoney(input.getBtempMoney()[i]);//更新追加金额
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//年分解保存
			FbsBudgetResolveYear year = this.get(input.getResolveId()[i], true);
			year.setBtempMoney(input.getBtempMoney()[i]);
			year.setBtempMoney1(input.getBtempMoney1()[i]);
			year.setBtempMoney2(input.getBtempMoney2()[i]);
			year.setBtempMoney3(input.getBtempMoney3()[i]);
			year.setBtempMoney4(input.getBtempMoney4()[i]);
			year.setBtempMoney5(input.getBtempMoney5()[i]);
			year.setBtempMoney6(input.getBtempMoney6()[i]);
			year.setBtempMoney7(input.getBtempMoney7()[i]);
			year.setBtempMoney8(input.getBtempMoney8()[i]);
			year.setBtempMoney9(input.getBtempMoney9()[i]);
			year.setBtempMoney10(input.getBtempMoney10()[i]);
			year.setBtempMoney11(input.getBtempMoney11()[i]);
			year.setBtempMoney12(input.getBtempMoney12()[i]);
			year.setUpdateBy(user.getLoginName());
			year.setUpdateDate(curDate);
			this.saveOrUpdate(year);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = new FbsBudgetSuperaddDetail();
			superaddDtl.setId(input.getSuperaddDtlId()[i]);
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(input.getBtempMoney()[i]);
			fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
			dtlList.add(superaddDtl);
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(budget, entity, user);
		String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_APP_SAVE_SUCC;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_EDIT, msg, FbsMessageConstant.BUS_NAME_SUPERADD, user);
	}
	/**
	 * 下级单位追加更新
	 *
	 * @param budget
	 * @param entity
	 */
	private void saveFbsBudgetCollectUnderCorp(FbsBudget budget, FbsBudgetSuperadd entity, ICurrentUser user) {
		CorpDTO corp = corpService.get(entity.getCorpId());
		//判断是否是汇总单位提交资金计划
		if (this.fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
			Integer status = entity.getStatus();
			Integer superStatus = entity.getStatus();
			if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
				//上级单位计划追加状态为审批通过，单位计划上报状态为计划追加完成
				status = BudgetConstant.STATUE_CHECK;
				//上级单位计划追加状态为审批通过，下级单位计划状态也审批通过
				superStatus = BudgetConstant.STATUE_CHECK;
			} else if (BudgetConstant.STATUE_WAIT_COLLECT.equals(entity.getStatus())) {
				//上级单位计划追加状态为待汇总，单位计划上报状态为计划追加汇总中
				status = BudgetConstant.STATUE_SUPERADD_WAIT_COLLECTED;
				//上级单位计划追加状态为待汇总，下级单位计划状态为汇总中
				superStatus = BudgetConstant.STATUE_WAIT_COLLECTED;
			} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
				//上级单位计划追加状态为待审批，单位计划上报状态为计划追加审批中
				status = BudgetConstant.STATUE_SUPERADD_WAIT_CHECK;
				//上级单位计划追加状态为待审批，下级单位计划状态也汇总待审批
				superStatus = BudgetConstant.STATUE_WAIT_CHECK_COLLECT;
			} else if (BudgetConstant.STATUE_BACK.equals(entity.getStatus())) {
				//上级单位计划追加状态审批打回，下级单位计划状态计划追加审批打回
				status = BudgetConstant.STATUE_SUPERADD_COLLECT_BAC;
				//上级单位计划追加状态审批打回，下级单位计划状态汇总审批打回
				superStatus = BudgetConstant.STATUE_COLLECT_BACK;
				;
			} else {
				return;
			}
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(entity.getCorpId());
			//是否存在虚拟单位
			FbsCollectCorpVO vo = fbsBudgetService.listCorpByCorpId(entity.getCorpId());
			if(!EmptyUtils.isEmpty(vo)){
				FbsCorpBudgetDTO fbsCorpBudgetDTO = fbsCorpBudgetService.getByCorpId(vo.getCorpId());
				if(!EmptyUtils.isEmpty(fbsCorpBudgetDTO)) {
					fbsCorpBudgetDTO.setCorpId(vo.getId());
					fbsCorpBudgetDTO.setCollectFlag(1);
					list.add(fbsCorpBudgetDTO);
				}
			}
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO dto : list) {
					//过滤计划不控制单位
					if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
						continue;
					}
					//获取计划上报信息
					FbsBudget budgetTemp = this.fbsBudgetService.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
					if (budgetTemp != null) {
						//获取对应的追加信息
						FbsBudgetSuperadd budgetSuperAdd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budgetTemp.getId());
						if (budgetSuperAdd != null) {
							//追加状态为汇总中的单位更新追加状态
							if (budgetSuperAdd.getStatus() >= BudgetConstant.STATUE_WAIT_COLLECTED) {
								//计划追加状态为审批通过
								budgetSuperAdd.setStatus(superStatus);
								fbsBudgetSuperaddService.saveOrUpdate(budgetSuperAdd);
								//上级单位编制完成，则更新下级单位的追加金额
								if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
									//年计划上报模式:0,年固定;1,年分解
									String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
									//汇总中状态的下级单位状态更新追加金额
									if (BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
										fbsBudgetDtlAbsService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
										this.fbsBudgetResolveYearDao.setSuperaddMoney(budgetTemp.getId());
									} else {
										fbsBudgetDtlAbsService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
									}
									// 更新明细录入的追加金额 zuoy 2018-11-20
									fbsBudgetDetailService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
									//生成日志
									fbsMessageService.saveMessage(budgetTemp.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_COLLECT_APP_SUCC, FbsMessageConstant.BUS_NAME_SUPERADD, user);
								}
								//更新计划上报状态
								budgetTemp.setStatus(status);
								this.fbsBudgetService.saveOrUpdate(budgetTemp);
								//判断单位是否汇总下级单位
								if (this.fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId())) {
									//判断单位是否存在下级单位，如果存在下级单位，需要对下级单位做相同处理
									List<CorpDTO> listCorpTemp = corpService.listUnderCorpByParentCorpId(dto.getCorpId());
									if (!EmptyUtils.isEmptyList(listCorpTemp)) {
										this.saveFbsBudgetCollectUnderCorp(budgetTemp, budgetSuperAdd, user);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 资金计划追加删除
	 *
	 * @param budget
	 * @param superaddId
	 */
	public void deleteFbsBudgetSuperAdd(FbsBudget budget, String superaddId, ICurrentUser user) {
		//获取资金计划上报信息
		budget.setStatus(BudgetConstant.STATUE_CHECK);
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.fbsBudgetService.saveOrUpdate(budget);
		this.fbsBudgetResolveYearDao.clearSuperaddTempMoney(budget.getId());
		//获取资金计划追加信息
		FbsBudgetSuperadd superAdd = fbsBudgetSuperaddService.get(superaddId, true);
		//判断是否是汇总单位删除资金计划追加，汇总单位需要更新汇总中的下级单位资金计划状态为待汇总
		if (this.fbsCorpBudgetService.getChildCollectFlag(budget.getCorpId())) {
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(budget.getCorpId());
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO dto : list) {
					//过滤计划不控制单位
					if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
						continue;
					}
					//获取追加计划汇总中状态的单位计划上报信息
					FbsBudget budgetTemp = this.fbsBudgetService.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
					if (budgetTemp != null && budgetTemp.getStatus() >= BudgetConstant.STATUE_SUPERADD_WAIT_COLLECTED) {
						budgetTemp.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT);
						this.fbsBudgetService.saveOrUpdate(budgetTemp);
						//fbsBudgetResolveYearDao.clearSuperaddTempMoney(budgetTemp.getId());
						//根据计划ID获取计划追加信息,更新为待汇总
						FbsBudgetSuperadd superAddTemp = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budgetTemp.getId());
						superAddTemp.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
						superAddTemp.setUpdateBy(user.getLoginName());
						superAddTemp.setUpdateDate(DateUtils.getDate());
						fbsBudgetSuperaddService.saveOrUpdate(superAddTemp);
					}
				}
			}
		}
		//删除录入明细详情
		fbsBudgetDetailService.delColloctDetailSuper(budget.getId(), budget.getTimeType());
		//删除计划追加明细
		fbsBudgetSuperaddDetailService.deleteBySuperaddId(superaddId);
		//删除计划追加主表信息
		fbsBudgetSuperaddService.delete(superAdd.getId());
		//生成日志
		fbsMessageService.saveMessage(superAdd.getId(), FbsMessageConstant.MESSAGE_TYPE_DELETE, FbsMessageConstant.FBS_LOG_DELETE_SUCC, FbsMessageConstant.BUS_NAME_SUPERADD, user);
	}
	/**
	 * 计划上报-审批通过
	 *
	 * @param entity
	 */
	public void approvePassEndSuper(FbsBudgetSuperadd entity, boolean result, ICurrentUser user) {
		//获取对应的资金计划上报信息
		FbsBudget budget = this.fbsBudgetService.get(entity.getBudgetId(), true);
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS;
		if (result) {
			//如果是汇总单位虚拟单位需要汇总
			FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpById(entity.getCorpId());
			//判断单位是否需要汇总
			if (this.fbsCorpBudgetService.getCollectFlag(entity.getCorpId()) || !EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
				budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT);
			} else {
				entity.setStatus(BudgetConstant.STATUE_CHECK);//计划编制完成
				budget.setStatus(BudgetConstant.STATUE_CHECK);
				//年计划上报模式:0,年固定;1,年分解
				String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
				//更新上报明细的追加金额
				if (BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)) {
					fbsBudgetDtlAbsService.setSuperaddMoney(budget.getId(), budget.getTimeType());
					this.fbsBudgetResolveYearDao.setSuperaddMoney(budget.getId());
				} else {
					fbsBudgetDtlAbsService.setSuperaddMoney(budget.getId(), budget.getTimeType());
				}
				// 更新明细录入的追加金额 zuoy 2018-11-20
				fbsBudgetDetailService.setSuperaddMoney(budget.getId(), budget.getTimeType());
			}
		} else {
			entity.setStatus(BudgetConstant.STATUE_BACK);//打回
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_BACK);//计划追加打回
			messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		}
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		//更新资金计划状态
		this.fbsBudgetService.saveOrUpdate(budget);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(budget, entity, user);
		String message = result ? FbsMessageConstant.FBS_LOG_APP_SUCC : FbsMessageConstant.FBS_LOG_APP_BACK;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, message, FbsMessageConstant.BUS_NAME_SUPERADD, user);
	}
	/**
	 * 当前时间之后是否存在已上报审批完成的月计划
	 *
	 * @return
	 */
	public boolean checkBudgetMonthReportByCurDate(String corpId) {
		Date beginStartDate = DateUtils.getFirstMonthDate(DateUtils.getDate());
		Date beginEndDate = DateUtils.getLastYearDate(DateUtils.getDate());
		List<FbsBudget> list = this.fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_MONTH, beginStartDate, beginEndDate);
		return EmptyUtils.isEmptyList(list);
	}
	public void collectFbsBudgetSuperAdd(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user,HashMap<String, FbsItemVO>  fbsItemVOHashMap) {
		List<FbsBudget> childBudgetList = fbsBudgetService.collectFbsBudgetSuperAdd(input, childIds, user,fbsItemVOHashMap);
		List<String> childBudgetIds = new ArrayList<String>();
		//获取下级单位计划ID集合
		for (FbsBudget budget : childBudgetList) {
			childBudgetIds.add(budget.getId());
		}
		String budgetAuthSql = SqlUtil.getInSql(childBudgetIds, "b.budget_id", true);
		//汇总下级单位计划分解上报数据
		List<FbsBudgetResolveYear> list = fbsBudgetResolveYearDao.collectByBudgetIds(budgetAuthSql);
		//获取汇总单位计划分解
		List<FbsBudgetResolveYear> listYear = this.listByBudgetId(input.getId());
		for (FbsBudgetResolveYear year : listYear) {
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsBudgetResolveYear entity : list) {
					if (year.getItemCode().equals(entity.getItemCode())) {
						year.setBtempMoney(year.getBtempMoney().add(entity.getBtempMoney()));
						year.setBtempMoney1(year.getBtempMoney1().add(entity.getBtempMoney1()));
						year.setBtempMoney2(year.getBtempMoney2().add(entity.getBtempMoney2()));
						year.setBtempMoney3(year.getBtempMoney3().add(entity.getBtempMoney3()));
						year.setBtempMoney4(year.getBtempMoney4().add(entity.getBtempMoney4()));
						year.setBtempMoney5(year.getBtempMoney5().add(entity.getBtempMoney5()));
						year.setBtempMoney6(year.getBtempMoney6().add(entity.getBtempMoney6()));
						year.setBtempMoney7(year.getBtempMoney7().add(entity.getBtempMoney7()));
						year.setBtempMoney8(year.getBtempMoney8().add(entity.getBtempMoney8()));
						year.setBtempMoney9(year.getBtempMoney9().add(entity.getBtempMoney9()));
						year.setBtempMoney10(year.getBtempMoney10().add(entity.getBtempMoney10()));
						year.setBtempMoney11(year.getBtempMoney11().add(entity.getBtempMoney11()));
						year.setBtempMoney12(year.getBtempMoney12().add(entity.getBtempMoney12()));
						year.setUpdateBy(user.getLoginName());
						year.setUpdateDate(DateUtils.getDate());
					}
				}
			}
		}
	}
	/**
	 * 查询上报累计净额
	 *
	 * @param budgetId
	 * @return
	 */
	public BigDecimal getNetAmount(String budgetId, Date beginDate) {
		BigDecimal netAmount = BigDecimal.ZERO;
		//获取200科目上报金额
		FbsBudgetResolveYear resolve200 = this.getByBudgetIdAndItemCode(budgetId, BudgetConstant.ITEM_CODE_200);
		//获取200科目上报金额
		FbsBudgetResolveYear resolve400 = this.getByBudgetIdAndItemCode(budgetId, BudgetConstant.ITEM_CODE_400);
		//当前月份
		int month = DateUtils.getMonth(beginDate);
		BigDecimal bMoney200 = BigDecimal.ZERO;
		BigDecimal bMoney400 = BigDecimal.ZERO;
		try {
			for(int i=1; i<=month; i++){
				if(resolve200!=null) {
					Method method200 = resolve200.getClass().getMethod("getBMoney" + i, new Class[0]);
					bMoney200 = bMoney200.add((BigDecimal) method200.invoke(resolve200));
				}
				if(resolve400!=null) {
					Method method400 = resolve400.getClass().getMethod("getBMoney" + i, new Class[0]);
					bMoney400 = bMoney400.add((BigDecimal) method400.invoke(resolve400));
				}
			}
			netAmount = bMoney200.subtract(bMoney400);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netAmount;
	}
}
