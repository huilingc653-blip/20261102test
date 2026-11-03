package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsBudgetResolveMonthDao;
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
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_RESOLVE_MONTH
 * @date 2017-08-04 10:03:40
 */
@Service("fbsBudgetResolveMonthService")
@Transactional
public class FbsBudgetResolveMonthService extends BaseService<FbsBudgetResolveMonth, String> implements IWorkFlowStartedListener {
	@Autowired
	private FbsBudgetResolveMonthDao fbsBudgetResolveMonthDao;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
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
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	@Autowired
	private FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetResolveMonth entity) {
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
	public void save(FbsBudgetResolveMonth entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetResolveMonth entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetResolveMonth entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetResolveMonth, String> getEntityDao() {
		return fbsBudgetResolveMonthDao;
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetResolveMonth> listByBudgetId(String budgetId) {
		return fbsBudgetResolveMonthDao.listByBudgetId(budgetId);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetResolveMonth> listByBudgetId(Set<String> budgetIds) {
		if(EmptyUtils.isEmpty(budgetIds)){
			return Lists.newArrayListWithCapacity(0);
		}
		return fbsBudgetResolveMonthDao.listByBudgetIds(budgetIds);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetIds
	 * @return
	 */
	public HashMap<String, List<FbsBudgetResolveMonth>> mapByBudgetIds(List<String> budgetIds) {
		HashMap<String, List<FbsBudgetResolveMonth>> map = new HashMap<>();
		List<FbsBudgetResolveMonth> list = fbsBudgetResolveMonthDao.listByBudgetIds(budgetIds);
		if(!CollectionUtils.isEmpty(list)) {
			for (FbsBudgetResolveMonth month:list) {
				List<FbsBudgetResolveMonth> tempList = null;
				if(map.containsKey(month.getBudgetId())){
					tempList = map.get(month.getBudgetId());
				} else {
					tempList = new ArrayList<>();
				}
				tempList.add(month);
				map.put(month.getBudgetId(), tempList);
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
	public List<FbsBudgetResolveMonthVO> listVOByBudgetId(String budgetId) {
		return fbsBudgetResolveMonthDao.listVOByBudgetId(budgetId);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @param itemCode
	 * @return
	 */
	public FbsBudgetResolveMonth getByBudgetIdAndItemCode(String budgetId, String itemCode) {
		return fbsBudgetResolveMonthDao.getByBudgetIdAndItemCode(budgetId, itemCode);
	}
	/**
	 * 获取月计划分解
	 *
	 * @param budget
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<FbsBudgetResolveMonthVO> initResolveMonthBudget(FbsBudgetVO budget, ICurrentUser user) {
		List<FbsBudgetResolveMonthVO> listBudget = new ArrayList<FbsBudgetResolveMonthVO>();
		if (EmptyUtils.isEmptyList(budget.getDtlList())) {
			return listBudget;
		}
		//获取月分解信息
		List<FbsBudgetResolveMonth> listResolveMonthBudget = this.listByBudgetId(budget.getId());
		//获取周期
		List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budget.getBeginDate());
		try {
			if (listResolveMonthBudget != null && listResolveMonthBudget.size() > 0) {
				for (FbsBudgetResolveMonth resolve : listResolveMonthBudget) {
					FbsBudgetResolveMonthVO entity = new FbsBudgetResolveMonthVO();
					BeanUtils.copyNotNullProperties(resolve, entity);
					for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
						if (resolve.getItemCode().equals(dtl.getItemCode())) {
							entity.setDetailId(dtl.getId());
							entity.setItem(dtl.getItem());
							entity.setCanBMoney(dtl.getCanBMoney());
							entity.setRmk(dtl.getRmk());
						}
					}
					HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> bsuperMoneyMap = new HashMap<Integer, BigDecimal>();
					HashMap<Integer, BigDecimal> btempMoneyMap = new HashMap<Integer, BigDecimal>();
					for (int i = 0; i < listPeriod.size(); i++) {
						Method method = ReflectionUtils.findMethod(entity.getClass(), "getbMoney" + (i + 1));
						bMoneyMap.put((i + 1), (BigDecimal) method.invoke(entity));
						Method bsuperMethod = entity.getClass().getMethod("getBsuperMoney" + (i + 1), new Class[0]);
						bsuperMoneyMap.put((i + 1), (BigDecimal) bsuperMethod.invoke(entity));
						Method btempMethod = entity.getClass().getMethod("getBtempMoney" + (i + 1), new Class[0]);
						btempMoneyMap.put((i + 1), (BigDecimal) btempMethod.invoke(entity));
					}
					entity.setbMoneyMap(bMoneyMap);
					entity.setBsuperMoneyMap(bsuperMoneyMap);
					entity.setBtempMoneyMap(btempMoneyMap);
					listBudget.add(entity);
				}
			} else {
//				CorpDTO parentCorp = corpService.getParentById(budget.getCorpId());//获取上级单位
				for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
					//初始化月分解表
					FbsBudgetResolveMonth month = new FbsBudgetResolveMonth();
					month.setBudgetId(dtl.getBudgetId());
					month.setItemCode(dtl.getItemCode());
					month.setItemName(dtl.getItemName());
					month.setBMoney(BigDecimal.ZERO);
					month.setBMoney1(BigDecimal.ZERO);
					month.setBMoney2(BigDecimal.ZERO);
					month.setBMoney3(BigDecimal.ZERO);
					month.setBMoney4(BigDecimal.ZERO);
					month.setBMoney5(BigDecimal.ZERO);
					month.setBMoney6(BigDecimal.ZERO);
					month.setBtempMoney(BigDecimal.ZERO);
					month.setBtempMoney1(BigDecimal.ZERO);
					month.setBtempMoney2(BigDecimal.ZERO);
					month.setBtempMoney3(BigDecimal.ZERO);
					month.setBtempMoney4(BigDecimal.ZERO);
					month.setBtempMoney5(BigDecimal.ZERO);
					month.setBtempMoney6(BigDecimal.ZERO);
					month.setBsuperMoney(BigDecimal.ZERO);
					month.setBsuperMoney1(BigDecimal.ZERO);
					month.setBsuperMoney2(BigDecimal.ZERO);
					month.setBsuperMoney3(BigDecimal.ZERO);
					month.setBsuperMoney4(BigDecimal.ZERO);
					month.setBsuperMoney5(BigDecimal.ZERO);
					month.setBsuperMoney6(BigDecimal.ZERO);
					month.setCreateBy(user.getLoginName());
					month.setCreateDate(DateUtils.getDate());
					this.saveOrUpdate(month);
					FbsBudgetResolveMonthVO entity = new FbsBudgetResolveMonthVO();
					BeanUtils.copyNotNullProperties(month, entity);
					entity.setDetailId(dtl.getId());
					entity.setItem(dtl.getItem());
					entity.setCanBMoney(dtl.getCanBMoney());
					HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
					for (int i = 0; i < listPeriod.size(); i++) {
						Method method = entity.getClass().getMethod("getbMoney" + (i + 1), new Class[0]);
						bMoneyMap.put((i + 1), (BigDecimal) method.invoke(entity));
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
	public void saveFbsBudget(FbsBudgetInputResolveMonthVO input, ICurrentUser user) {
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
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
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
			dtl.setRmk(input.getRmk()[i]);
			checkMoney = checkMoney.add(dtl.getbMoney());
			fbsBudgetDtlAbsList.add(dtl);
			//月分解保存
			FbsBudgetResolveMonth month = this.get(input.getResolveId()[i]);
			if (EmptyUtils.isEmpty(month)) {
				month = new FbsBudgetResolveMonth();
			}
			BeanUtils.copyNotNullProperties(dtl, month);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				month.setId(null);
			} else {
				month.setId(input.getResolveId()[i]);
			}
			month.setBMoney(input.getbMoney()[i]);
			month.setBMoney1(input.getbMoney1()[i]);
			month.setBMoney2(input.getbMoney2()[i]);
			month.setBMoney3(input.getbMoney3()[i]);
			if (input.getbMoney4() != null && input.getbMoney4().length > 0) {
				month.setBMoney4(input.getbMoney4()[i]);
			} else {
				month.setBMoney4(BigDecimal.ZERO);
			}
			if (input.getbMoney5() != null && input.getbMoney5().length > 0) {
				month.setBMoney5(input.getbMoney5()[i]);
			} else {
				month.setBMoney5(BigDecimal.ZERO);
			}
			if (input.getbMoney6() != null && input.getbMoney6().length > 0) {
				month.setBMoney6(input.getbMoney6()[i]);
			} else {
				month.setBMoney6(BigDecimal.ZERO);
			}
			month.setBtempMoney(BigDecimal.ZERO);
			month.setBtempMoney1(BigDecimal.ZERO);
			month.setBtempMoney2(BigDecimal.ZERO);
			month.setBtempMoney3(BigDecimal.ZERO);
			month.setBtempMoney4(BigDecimal.ZERO);
			month.setBtempMoney5(BigDecimal.ZERO);
			month.setBtempMoney6(BigDecimal.ZERO);
			month.setBsuperMoney(BigDecimal.ZERO);
			month.setBsuperMoney1(BigDecimal.ZERO);
			month.setBsuperMoney2(BigDecimal.ZERO);
			month.setBsuperMoney3(BigDecimal.ZERO);
			month.setBsuperMoney4(BigDecimal.ZERO);
			month.setBsuperMoney5(BigDecimal.ZERO);
			month.setBsuperMoney6(BigDecimal.ZERO);
			month.setCreateBy(loginName);
			month.setCreateDate(curDate);
			month.setUpdateBy(loginName);
			month.setUpdateDate(curDate);
			this.saveOrUpdate(month);
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
		notificationService.complete(NotificationConstant.BUDGET.BUDGET_INPUT_MONTH, entity.getId(), user.getId());
	}
	/**
	 * 审批时保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudgetForApprove(FbsBudgetInputResolveMonthVO input, ICurrentUser user) {
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
			checkMoney = checkMoney.add(dtl.getbMoney());
			fbsBudgetDtlAbsList.add(dtl);
			//月分解保存
			FbsBudgetResolveMonth month = this.get(input.getResolveId()[i], true);
			if (EmptyUtils.isEmpty(month)) {
				month = new FbsBudgetResolveMonth();
			}
			BeanUtils.copyNotNullProperties(dtl, month);
			if (StringUtils.isEmpty(input.getResolveId()[i])) {
				month.setId(null);
			} else {
				month.setId(input.getResolveId()[i]);
			}
			month.setBMoney1(input.getbMoney1()[i]);
			month.setBMoney2(input.getbMoney2()[i]);
			month.setBMoney3(input.getbMoney3()[i]);
			if (input.getbMoney4() != null && input.getbMoney4().length > 0) {
				month.setBMoney4(input.getbMoney4()[i]);
			} else {
				month.setBMoney4(BigDecimal.ZERO);
			}
			if (input.getbMoney5() != null && input.getbMoney5().length > 0) {
				month.setBMoney5(input.getbMoney5()[i]);
			} else {
				month.setBMoney5(BigDecimal.ZERO);
			}
			if (input.getbMoney6() != null && input.getbMoney6().length > 0) {
				month.setBMoney6(input.getbMoney6()[i]);
			} else {
				month.setBMoney6(BigDecimal.ZERO);
			}
			month.setCreateBy(user.getLoginName());
			month.setCreateDate(DateUtils.getDate());
			this.saveOrUpdate(month);
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
						this.initResolveMonthBudget(fbsBudgetVO, user);//生成日志
						fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_AUTO_INIT, FbsMessageConstant.FBS_LOG_COLLECT_APP_INIT_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
					} else {
						//未汇总单位资金计划更新为零计划
						if (budget.getStatus() <= BudgetConstant.STATUE_WAIT_COLLECT) {
							budget.setCheckMoney(BigDecimal.ZERO);
							budget.setStatus(status);
							fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
							//更新月分解
							fbsBudgetResolveMonthDao.clearInputMoney(budget.getId());
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
	 * 资金计划删除
	 *
	 * @param id
	 * @param timeType
	 */
	public void deleteFbsBudget(String id, String timeType, ICurrentUser user) {
		//删除分解表
		fbsBudgetResolveMonthDao.deleteByBudgetId(id);
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
		List<FbsBudgetResolveMonth> list = fbsBudgetResolveMonthDao.collectByBudgetIds(budgetAuthSql);
		//获取汇总单位计划分解
		List<FbsBudgetResolveMonth> listMonth = this.listByBudgetId(input.getId());
		for (FbsBudgetResolveMonth month : listMonth) {
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsBudgetResolveMonth entity : list) {
					if (month.getItemCode().equals(entity.getItemCode())) {
						month.setBMoney(month.getBMoney().add(entity.getBMoney()));
						month.setBMoney1(month.getBMoney1().add(entity.getBMoney1()));
						month.setBMoney2(month.getBMoney2().add(entity.getBMoney2()));
						month.setBMoney3(month.getBMoney3().add(entity.getBMoney3()));
						if (entity.getBMoney4() != null) {
							month.setBMoney4(month.getBMoney4().add(entity.getBMoney4()));
						} else {
							month.setBMoney4(BigDecimal.ZERO);
						}
						if (entity.getBMoney5() != null) {
							month.setBMoney5(month.getBMoney5().add(entity.getBMoney5()));
						} else {
							month.setBMoney5(BigDecimal.ZERO);
						}
						if (entity.getBMoney6() != null) {
							month.setBMoney6(month.getBMoney6().add(entity.getBMoney6()));
						} else {
							month.setBMoney6(BigDecimal.ZERO);
						}
						month.setUpdateBy(user.getLoginName());
						month.setUpdateDate(DateUtils.getDate());
					}
				}
			}
		}
	}
	/**
	 * 计划上报-审批通过
	 *
	 * @param entity
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
		String message = result ? FbsMessageConstant.FBS_LOG_APP_SUCC : FbsMessageConstant.FBS_LOG_APP_BACK;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, message, FbsMessageConstant.BUS_NAME_INPUT, user);
	}
	/**
	 * 计划追加-审批通过
	 *
	 * @param entity
	 */
	public void approvePassEndInput(FbsBudgetSuperadd entity, boolean result, ICurrentUser user) {
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
				//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
				String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
				//更新上报明细的追加金额
				if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
					fbsBudgetDtlAbsService.setSuperaddMoney(budget.getId(), budget.getTimeType());
					this.fbsBudgetResolveMonthDao.setSuperaddMoney(budget.getId());
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
		//获取周次
		int week = fbsBudgetPeriodService.getWeekNoByTime(fbsBudget.getBeginDate());
		//获取年计划分解信息
		List<FbsBudgetResolveMonth> listResolve = this.listByBudgetId(parentBudget.getId());
		HashMap<String, BigDecimal> resolveMap = new HashMap<String, BigDecimal>();
		for (FbsBudgetResolveMonth resolve : listResolve) {
			try {
				Method method = resolve.getClass().getMethod("getBMoney" + week, new Class[0]);
				BigDecimal bMoney = (BigDecimal) method.invoke(resolve);
				Method method1 = resolve.getClass().getMethod("getBsuperMoney" + week, new Class[0]);
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
			for (FbsBudgetResolveMonth resolve : listResolve) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < week; i++) {
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
			for (FbsBudgetResolveMonth resolve : listResolve) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < week; i++) {
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
//					dtl.setCanBMoney(dtl.getCanBMoney().subtract(dtl.getbMoney()).subtract(dtl.getBsuperMoney()).add(processMoney));//减去本期已上报 //modify by qijh 追加金额的可追加金额不应该减去已执行的金额。
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
		List<FbsBudgetResolveMonth> corpList = this.listByBudgetId(entity.getId());
		//获取汇总单位计划分解
		List<FbsBudgetResolveMonth> preCorpList = this.listByBudgetId(preCorpBudget.getId());
		for (FbsBudgetResolveMonth month : preCorpList) {
			for (FbsBudgetResolveMonth corpMonth : corpList) {
				if (month.getItemCode().equals(corpMonth.getItemCode())) {
					month.setBMoney(month.getBMoney().subtract(corpMonth.getBMoney()));
					month.setBMoney1(month.getBMoney1().subtract(corpMonth.getBMoney1()));
					month.setBMoney2(month.getBMoney2().subtract(corpMonth.getBMoney2()));
					month.setBMoney3(month.getBMoney3().subtract(corpMonth.getBMoney3()));
					if (corpMonth.getBMoney4() != null && month.getBMoney4() != null) {
						month.setBMoney4(month.getBMoney4().subtract(corpMonth.getBMoney4()));
					}
					if (corpMonth.getBMoney5() != null && month.getBMoney5() != null) {
						month.setBMoney5(month.getBMoney5().subtract(corpMonth.getBMoney5()));
					}
					if (corpMonth.getBMoney6() != null && month.getBMoney6() != null) {
						month.setBMoney6(month.getBMoney6().subtract(corpMonth.getBMoney6()));
					}
					month.setUpdateBy(user.getLoginName());
					month.setUpdateDate(DateUtils.getDate());
					this.getEntityDao().evict(corpMonth);
				}
			}
		}
		//计划明细扣减
		fbsBudgetDetailService.repulseDeatil(entity);
	}
	/**
	 * 获取月计划分解
	 *
	 * @param budget
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<FbsBudgetResolveMonthVO> getResolveMonthBudget(FbsBudgetVO budget, ICurrentUser user) {
		List<FbsBudgetResolveMonthVO> listBudget = new ArrayList<FbsBudgetResolveMonthVO>();
		if (EmptyUtils.isEmptyList(budget.getDtlList())) {
			return listBudget;
		}
		//获取月分解信息
		List<FbsBudgetResolveMonth> listResolveMonthBudget = this.listByBudgetId(budget.getId());
		int week = DateUtils.getWeekOfMonth(DateUtils.getDate());
		//分解计划上报余额控制方式  0-分解金额;1-分解金额+前期未上报余额;2-分解金额+前期未执行余额
		String inputBlance = this.configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_INPUTBLANCE).getValue();
		Date beginStartDate = DateUtils.getFirstMonthDate(DateUtils.getDate());
		Date beginEndDate = DateUtils.getLastWeekDate(DateUtils.addDay(DateUtils.getFirstWeekDate(DateUtils.getDate()), -1));
		beginEndDate = BudgetUtils.isFirstWeek(DateUtils.getDate(), beginEndDate);
		//获取前期未上报余额
		HashMap<String, BigDecimal> preDtlInputBalMap = new HashMap<String, BigDecimal>();
		//获取前期未执行的余额
		HashMap<String, BigDecimal> preDtlProcessBalMap = new HashMap<String, BigDecimal>();
		if ("1".equals(inputBlance)) {
			//获取前期上报的金额
			List<FbsBudget> monthList = fbsBudgetService.listBudgetByBeginDate(budget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, beginStartDate, beginEndDate);
			List<String> preBudgetIds = new ArrayList<String>();
			//获取下级单位计划ID集合
			if (!EmptyUtils.isEmptyList(monthList)) {
				for (FbsBudget monthBudget : monthList) {
					preBudgetIds.add(monthBudget.getId());
				}
			}
			String budgetAuthSql = SqlUtil.getInSql(preBudgetIds, "b.budget_id", true);
			List<FbsBudgetDtlAbsVO> preDtlist = fbsBudgetDtlAbsService.collectByBudgetIds(BudgetConstant.TIME_TYPE_WEEK, budgetAuthSql);
			HashMap<String, BigDecimal> preDtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(preDtlist)) {
				for (FbsBudgetDtlAbsVO dtl : preDtlist) {
					preDtlMap.put(dtl.getItemCode(), dtl.getbMoney().add(dtl.getBsuperMoney()));//上报总金额 = 上报金额+追加金额（已通过）
				}
			}
			for (FbsBudgetResolveMonth resolve : listResolveMonthBudget) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < week; i++) {
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
			for (FbsBudgetResolveMonth resolve : listResolveMonthBudget) {
				try {
					BigDecimal result = BigDecimal.ZERO;
					for (int i = 1; i < week; i++) {
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
		//获取周期
		List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budget.getBeginDate());
		try {
			if (listResolveMonthBudget != null && listResolveMonthBudget.size() > 0) {
				for (FbsBudgetResolveMonth resolve : listResolveMonthBudget) {
					FbsBudgetResolveMonthVO entity = new FbsBudgetResolveMonthVO();
					BeanUtils.copyNotNullProperties(resolve, entity);
					for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
						if (resolve.getItemCode().equals(dtl.getItemCode())) {
							entity.setDetailId(dtl.getId());
							entity.setItem(dtl.getItem());
							entity.setCanBMoney(dtl.getCanBMoney());
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
					for (int i = 0; i < listPeriod.size(); i++) {
						int key = (i + 1);
						Method method = entity.getClass().getMethod("getBtempMoney" + key, new Class[0]);
						btempMoneyMap.put(key, (BigDecimal) method.invoke(entity));
						Date beginDate = BudgetUtils.getFirstWeekDateOfMonth(budget.getBeginDate(), i);
						Date endDate = BudgetUtils.getLastWeekDateOfMonth(budget.getBeginDate(), i);
						FbsBudget fbsBudgetWeek = this.fbsBudgetService.getCorpBudget(budget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, beginDate, endDate);
						BigDecimal canBMoney = BigDecimal.ZERO;
						BigDecimal processMoney = BigDecimal.ZERO;
						if (!EmptyUtils.isEmpty(fbsBudgetWeek)) {
							FbsBudgetDtlAbs dtl = this.fbsBudgetDtlAbsService.getByBudgetId(fbsBudgetWeek.getId(), resolve.getItemCode(), BudgetConstant.TIME_TYPE_WEEK);
							if (!EmptyUtils.isEmpty(dtl)) {
								canBMoney = dtl.getbMoney().add(dtl.getBsuperMoney());
							}
							//获取科目执行金额
							processMoney = fbsBudgetProcessService.collectProcessMoneyByStatus(beginDate, endDate, resolve.getItemCode(), budget.getCorpId());
						}
						if (key == week) {
							if ("1".equals(inputBlance)) {
								BigDecimal inputBal = preDtlInputBalMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlInputBalMap.get(resolve.getItemCode());
								//留存金额 = 上报金额+追加金额-已执行金额
								remainBMoneyMap.put(key, canBMoney.subtract(processMoney).add(inputBal));
							} else if ("2".equals(inputBlance)) {
								BigDecimal processBal = preDtlProcessBalMap.get(resolve.getItemCode()) == null ? BigDecimal.ZERO : preDtlProcessBalMap.get(resolve.getItemCode());
								//留存金额 = 上报金额+追加金额-已执行金额
								remainBMoneyMap.put(key, canBMoney.subtract(processMoney).add(processBal));
							} else {
								//留存金额 = 上报金额+追加金额-已执行金额
								remainBMoneyMap.put(key, canBMoney.subtract(processMoney));
							}
						} else {
							//留存金额 = 上报金额+追加金额-已执行金额
							remainBMoneyMap.put(key, canBMoney.subtract(processMoney));
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
									//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
									String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
									//汇总中状态的下级单位状态更新追加金额
									if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
										fbsBudgetDtlAbsService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
										this.fbsBudgetResolveMonthDao.setSuperaddMoney(budgetTemp.getId());
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
		this.fbsBudgetResolveMonthDao.clearSuperaddTempMoney(budget.getId());
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
						//fbsBudgetResolveMonthDao.clearSuperaddTempMoney(budgetTemp.getId());
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
	 * 资金计划追加保存
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAdd(FbsBudgetInputResolveMonthVO input, ICurrentUser user) {
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
			FbsBudgetResolveMonth month = this.get(input.getResolveId()[i], true);
			month.setBtempMoney(input.getBtempMoney()[i]);
			month.setBtempMoney1(input.getBtempMoney1()[i]);
			month.setBtempMoney2(input.getBtempMoney2()[i]);
			month.setBtempMoney3(input.getBtempMoney3()[i]);
			month.setBtempMoney4(input.getBtempMoney4()[i]);
			month.setBtempMoney5(EmptyUtils.isEmpty(input.getBtempMoney5()) ? BigDecimal.ZERO : input.getBtempMoney5()[i]);
			month.setBtempMoney6(EmptyUtils.isEmpty(input.getBtempMoney6()) ? BigDecimal.ZERO : input.getBtempMoney6()[i]);
			month.setUpdateBy(user.getLoginName());
			month.setUpdateDate(curDate);
			this.saveOrUpdate(month);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = fbsBudgetSuperaddDetailService.get(input.getSuperaddDtlId()[i], true);
			if (EmptyUtils.isEmpty(superaddDtl)) {
				superaddDtl = new FbsBudgetSuperaddDetail();
			}
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(input.getBtempMoney()[i]);
			if(!EmptyUtils.isEmpty(input.getRmk()) && !EmptyUtils.isEmpty(input.getRmk()[i])) {
				superaddDtl.setRmk(input.getRmk()[i]);
			}
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
	public boolean checkBudgetWeekReportByCurDate(String corpId) {
		Date beginStartDate = DateUtils.getFirstWeekDate(DateUtils.getDate());
		Date beginEndDate = DateUtils.getLastMonthDate(DateUtils.getDate());
		List<FbsBudget> list = this.fbsBudgetService.listBudgetByBeginDate(corpId, BudgetConstant.TIME_TYPE_WEEK, beginStartDate, beginEndDate);
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 审批时保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAddForApprove(FbsBudgetInputResolveMonthVO input, ICurrentUser user) {
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
			FbsBudgetResolveMonth month = this.get(input.getResolveId()[i], true);
			month.setBtempMoney(input.getBtempMoney()[i]);
			month.setBtempMoney1(input.getBtempMoney1()[i]);
			month.setBtempMoney2(input.getBtempMoney2()[i]);
			month.setBtempMoney3(input.getBtempMoney3()[i]);
			month.setBtempMoney4(input.getBtempMoney4()[i]);
			month.setBtempMoney5(EmptyUtils.isEmpty(input.getBtempMoney5()) ? BigDecimal.ZERO : input.getBtempMoney5()[i]);
			month.setBtempMoney6(EmptyUtils.isEmpty(input.getBtempMoney6()) ? BigDecimal.ZERO : input.getBtempMoney6()[i]);
			month.setUpdateBy(user.getLoginName());
			month.setUpdateDate(curDate);
			this.saveOrUpdate(month);
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
	public void collectFbsBudgetSuperAdd(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user,HashMap<String, FbsItemVO>  fbsItemVOHashMap) {
		List<FbsBudget> childBudgetList = fbsBudgetService.collectFbsBudgetSuperAdd(input, childIds, user,fbsItemVOHashMap);
		List<String> childBudgetIds = new ArrayList<String>();
		//获取下级单位计划ID集合
		for (FbsBudget budget : childBudgetList) {
			childBudgetIds.add(budget.getId());
		}
		String budgetAuthSql = SqlUtil.getInSql(childBudgetIds, "b.budget_id", true);
		//汇总下级单位计划分解上报数据
		List<FbsBudgetResolveMonth> list = fbsBudgetResolveMonthDao.collectByBudgetIds(budgetAuthSql);
		//获取汇总单位计划分解
		List<FbsBudgetResolveMonth> listMonth = this.listByBudgetId(input.getId());
		for (FbsBudgetResolveMonth month : listMonth) {
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsBudgetResolveMonth entity : list) {
					if (month.getItemCode().equals(entity.getItemCode())) {
						month.setBtempMoney(month.getBtempMoney().add(entity.getBtempMoney()));
						month.setBtempMoney1(month.getBtempMoney1().add(entity.getBtempMoney1()));
						month.setBtempMoney2(month.getBtempMoney2().add(entity.getBtempMoney2()));
						month.setBtempMoney3(month.getBtempMoney3().add(entity.getBtempMoney3()));
						if (entity.getBtempMoney4() != null) {
							month.setBtempMoney4(month.getBtempMoney4().add(entity.getBtempMoney4()));
						} else {
							month.setBtempMoney4(BigDecimal.ZERO);
						}
						if (entity.getBtempMoney5() != null) {
							month.setBtempMoney5(month.getBtempMoney5().add(entity.getBtempMoney5()));
						} else {
							month.setBtempMoney5(BigDecimal.ZERO);
						}
						if (entity.getBtempMoney6() != null) {
							month.setBtempMoney6(month.getBtempMoney6().add(entity.getBtempMoney6()));
						} else {
							month.setBtempMoney6(BigDecimal.ZERO);
						}
						month.setUpdateBy(user.getLoginName());
						month.setUpdateDate(DateUtils.getDate());
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
		FbsBudgetResolveMonth resolve200 = this.getByBudgetIdAndItemCode(budgetId, BudgetConstant.ITEM_CODE_200);
		//获取200科目上报金额
		FbsBudgetResolveMonth resolve400 = this.getByBudgetIdAndItemCode(budgetId, BudgetConstant.ITEM_CODE_400);
		//当前月份
		int weeks = DateUtils.getWeekOfMonth(beginDate);
		BigDecimal bMoney200 = BigDecimal.ZERO;
		BigDecimal bMoney400 = BigDecimal.ZERO;
		try {
			for(int i=1; i<=weeks; i++){
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
