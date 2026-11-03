package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsBudgetRollMonthDao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.entity.FbsBudgetResolveYear;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowStartedListener;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_RESOLVE_YEAR
 * @date 2017-07-26 11:24:33
 */
@Service("fbsBudgetRollMonthService")
@Transactional
public class FbsBudgetRollMonthService extends BaseService<FbsBudgetResolveYear, String> implements IWorkFlowStartedListener {
	@Autowired
	private FbsBudgetRollMonthDao fbsBudgetRollMonthDao;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsItemService fbsItemService;
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private FbsCorpItemService fbsCorpItemService;
	@Autowired
	private FbsBudgetDetailService fbsBudgetDetailService;
	@Autowired
	private FbsMessageService fbsMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IConfigService configService;
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
		return fbsBudgetRollMonthDao;
	}
	public void initRollMonthBudget(FbsBudgetVO fbsBudget, Model model, ICurrentUser user) {
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否可以跨年
		boolean isSpanYear = false;
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpId(fbsBudget.getCorpId());
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存月份
			List<String> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(year+"年"+month + "");
						monthsHid.add(month+"");
						month++;
					}
				}else{
					if(month>12) {
						month = month - 12;
						year++;
					}
					months.add(year+"年"+month + "");
					monthsHid.add(month+"");
					month++;
				}
			}
			model.addAttribute("months", months);
			model.addAttribute("monthsHid", monthsHid);
			List<FbsBudgetVO>  fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			if (!EmptyUtils.isEmptyList(listFbsItem)) {
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollMonth(fbsBudget.getId(), fbsBudget.getTimeType(), BudgetConstant.BUDGET_TARGET_INPUT,months);
				for(int i = 1; i < months.size(); i++){
					FbsBudgetVO fbsBudgetVO = new FbsBudgetVO();
					FbsBudget fbsBudgetTemp = new FbsBudget();
					FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
					fbsBudgetTemp = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
					if (null == fbsBudgetTemp) {
						fbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
						fbsBudgetTemp = new FbsBudget();
						BeanUtils.copyNotNullProperties(fbsBudgetVO, fbsBudgetTemp);
					} else {
						fbsBudgetVO = fbsBudgetService.getBudget(fbsBudgetTemp.getId());
					}
					beginDate = DateUtils.addMonth(beginDate,1);
					fbsBudgetVOList.add(fbsBudgetVO);
					fbsBudgetList.add(fbsBudgetTemp);
				}
				List<FbsBudgetRollMonthVO> list = new ArrayList<>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					FbsBudgetRollMonthVO vo = new FbsBudgetRollMonthVO();
					BeanUtils.copyNotNullProperties(fbsBudgetDtlAbsVO, vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(fbsBudget.getBeginDate()));
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					for(int j = 0; j < fbsBudgetVOList.size(); j++){
						FbsBudgetVO fbsBudgetVO = fbsBudgetVOList.get(j);
						if(!EmptyUtils.isEmpty(fbsBudgetVO.getDtlList())){
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = fbsBudgetVO.getDtlList().get(i);
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" +  DateUtils.getMonth(fbsBudgetVO.getBeginDate())));
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
						}
					}
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					list.add(vo);
				}
				model.addAttribute("rollMonthList", list);
				model.addAttribute("fbsBudgetList",fbsBudgetList);
			} else {
				model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthVO>());
				model.addAttribute("fbsBudgetList",new ArrayList<FbsBudget>());
			}
		} else {
			model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthVO>());
			model.addAttribute("fbsBudgetList",new ArrayList<FbsBudget>());
		}
	}
	/**
	 * 保存
	 *
	 * @param input
	 */
	public void saveFbsBudget(FbsBudgetInputRollMonthVO input, HttpServletRequest request, ICurrentUser user, boolean isApprove) {
		FbsBudget entity = fbsBudgetService.get(input.getId(), true);
		//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额，同时状态更新为待汇总状态
		FbsBudget preCorpBudget = fbsBudgetService.subtractFbsBudget(entity, false);
		String[] nextIds = request.getParameterValues("nextId");
		boolean isRoll = false;
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
			isRoll = true;
		} else {
			entity.setStatus(input.getStatus());
		}
		boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		List<FbsBudget> fbsBudgetList = new ArrayList<>();
		if(!EmptyUtils.isEmpty(nextIds)) {
			for (int i = 0; i < nextIds.length; i++) {
				FbsBudget nextEntity = fbsBudgetService.get(nextIds[i], true);
				if (null != nextEntity && !BudgetConstant.STATUE_CHECK.equals(nextEntity.getStatus())) {
					//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额
					if (this.fbsCorpBudgetService.getCollectFlag(nextEntity.getCorpId())) {
						fbsBudgetService.subtractFbsBudget(nextEntity, isRoll);
					}
					nextEntity.setStatus(BudgetConstant.STATUE_TEMP);
					nextEntity.setCurId(input.getCurId());
					nextEntity.setExchangeRate(input.getExchangeRate());
					nextEntity.setUpdateBy(user.getLoginName());
					nextEntity.setUpdateDate(DateUtils.getDate());
					fbsBudgetList.add(nextEntity);
				}
			}
		}
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<BigDecimal> nextCheckMoneyList = new ArrayList<>();
		String[] detailIds = request.getParameterValues("detailId");
		for (int i = 0; i < detailIds.length; i++) {
			String[] bMoney1 = request.getParameterValues("bMoney1");
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(detailIds[i], entity.getTimeType());
			if (StringUtils.isNotEmpty(bMoney1[i])) {
				dtl.setbMoney(new BigDecimal(Double.valueOf(bMoney1[i])));
			} else {
				dtl.setbMoney(BigDecimal.ZERO);
			}
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsService.saveOrUpdate(dtl);
			for (int j = 0;j < fbsBudgetList.size();j++){
				FbsBudget nextEntity = fbsBudgetList.get(j);
				BigDecimal nextCheckMoney = BigDecimal.ZERO;
				String[] nextBMoney = request.getParameterValues("bMoney"+(j+2));
				String[] nextDetailId = request.getParameterValues("detailId"+(j+2));
				//保存下月明细
				if (null != nextEntity && !BudgetConstant.STATUE_CHECK.equals(nextEntity.getStatus())) {
					FbsBudgetDtlAbs nextdtl = fbsBudgetDtlAbsService.getById(nextDetailId[i], entity.getTimeType());
					if (null != nextdtl) {
						if (StringUtils.isNotEmpty(nextBMoney[i])) {
							nextdtl.setbMoney(new BigDecimal(Double.valueOf(nextBMoney[i])));
						} else {
							nextdtl.setbMoney(BigDecimal.ZERO);
						}
						if (BudgetConstant.ITEM_CODE_400.equals(nextdtl.getItemCode())) {
							nextCheckMoney = nextdtl.getbMoney();
						}
						fbsBudgetDtlAbsService.saveOrUpdate(nextdtl);
					}
				}
				nextCheckMoneyList.add(nextCheckMoney);
			}
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		for (int i = 0;i < fbsBudgetList.size();i++){
			FbsBudget nextEntity = fbsBudgetList.get(i);
			if (null != nextEntity) {
				nextEntity.setCheckMoney(nextCheckMoneyList.get(i));
				fbsBudgetService.saveOrUpdate(nextEntity);
				//资金计划汇总提交，下级单位计划处理
				saveFbsBudgetCollectUnderCorp(nextEntity, user);
			}
		}
		if(isApprove){
			String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_APP_SAVE_SUCC;
			//生成日志
			fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_EDIT, msg, FbsMessageConstant.BUS_NAME_INPUT, user);
		}else {
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
						fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_AUTO_INIT, FbsMessageConstant.FBS_LOG_COLLECT_APP_INIT_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
					} else {
						//未汇总单位资金计划更新为零计划
						if (budget.getStatus() <= BudgetConstant.STATUE_WAIT_COLLECT) {
							budget.setCheckMoney(BigDecimal.ZERO);
							fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
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
					if (fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId())) {
						List<CorpDTO> listCorpTemp = corpService.listUnderCorpByParentCorpId(dto.getCorpId());
						//判断单位是否存在下级单位，如果存在下级单位，需要对未汇总单位的下级单位也更新为零计划
						if (listCorpTemp != null && listCorpTemp.size() > 0) {
							this.saveFbsBudgetCollectUnderCorp(budget, user);
						}
					}
				}
			}
		}
	}
	/**
	 * 汇总
	 *
	 * @param input
	 * @param childIds
	 */
	public void collectFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
		fbsBudgetService.collectFbsBudget(input, childIds, user);
		FbsBudget fbsBudget = fbsBudgetService.get(input.getId());
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否可以跨年
		boolean isSpanYear = false;
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
		//保存月份
		List<String> months = new ArrayList<String>();
		for (int i = 1; i <= mimodeRoll; i++) {
			if(!isSpanYear) {
				if (month <= 12) {
					months.add(year+"年"+month + "");
					month++;
				}
			}else{
				if(month>12) {
					month = month - 12;
					year++;
				}
				months.add(year+"年"+month + "");
				month++;
			}
		}
		Date beginDate = fbsBudget.getBeginDate();
		for (int k= 1; k < months.size();k++) {
			//取下月预算信息
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			List<FbsBudgetInputPreVO> nextChildFbsBudget = fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			List<String> nextChildIds = new ArrayList<>();
			for (String id : childIds) {
				FbsBudget t = fbsBudgetService.get(id);
				for (FbsBudgetInputPreVO vo : nextChildFbsBudget) {
					if (vo.getCorpId() == t.getCorpId()) {
						nextChildFbsBudget.remove(vo);
					}
				}
			}
			for (int i = 0; i < nextChildFbsBudget.size(); i++) {
				if (StringUtils.isEmpty(nextChildFbsBudget.get(i).getId())) {
					continue;
				}
				FbsBudgetVO vo = fbsBudgetService.getBudget(nextChildFbsBudget.get(i).getId());
				if (null == vo || null == vo.getDtlList()) {
					continue;
				}
				nextChildIds.add(nextChildFbsBudget.get(i).getId());
			}
			if (null == nextFbsBudget || (null != nextFbsBudget && nextFbsBudget.getStatus().equals(BudgetConstant.STATUE_TEMP))) {
				FbsBudgetInputVO nextInput = new FbsBudgetInputVO();
				nextInput.setId(nextFbsBudget.getId());
				this.collectNextFbsBudget(nextInput, nextChildIds, user);
			}
			//汇总明细
			fbsBudgetDetailService.colloctDetail(nextFbsBudget.getId(), nextChildIds);
			beginDate = DateUtils.addMonth(beginDate,1);
		}
	}
	/**
	 * 汇总下期计划
	 * @param input
	 * @param childIds
	 * @param user
	 * @return
	 */
	public List<FbsBudget> collectNextFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
		//获取汇总单位计划
		FbsBudget budget = fbsBudgetService.get(input.getId(), true);
		//获取汇总单位计划明细
		List<FbsBudgetDtlAbsVO> listDtl = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
		HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
		List<FbsBudget> childBudgetList = new ArrayList<FbsBudget>();
		//汇总明细
		fbsBudgetDetailService.colloctDetail(input.getId(), childIds);
		//获取下级单位计划
		for (String childId : childIds) {
			//获取汇总单位计划
			FbsBudget childBudget = fbsBudgetService.get(childId, true);
			List<FbsBudgetDtlAbsVO> listChildDtl = fbsBudgetDtlAbsService.listByBudgetId(childBudget.getId(), childBudget.getTimeType());
			//循环设置汇总单位的计划
			for (FbsBudgetDtlAbsVO dtl : listDtl) {
				for (FbsBudgetDtlAbsVO childDtl : listChildDtl) {
					if (dtl.getItemCode().equals(childDtl.getItemCode())) {
						if(childDtl.getbMoney() != null) {
							dtl.setbMoney(dtl.getbMoney().add(childDtl.getbMoney().multiply(childBudget.getExchangeRate())));
						}
					}
				}
				dtlMap.put(dtl.getItemCode(), dtl);
			}
			childBudgetList.add(childBudget);
			fbsBudgetService.saveOrUpdate(childBudget);
			//生成日志
			fbsMessageService.saveMessage(childBudget.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, FbsMessageConstant.FBS_LOG_COLLECT_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		}
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsItem> fbsItemList = fbsItemService.getAllFbsItem();
		Map<String, FbsItem> fbsItemMap = new HashMap<>();
		for(FbsItem fbsItem : fbsItemList){
			fbsItemMap.put(fbsItem.getItemCode(),fbsItem);
		}
		for (FbsBudgetDtlAbsVO dtl : listDtl) {
			dtl.setItem(fbsItemService.getFbsItemVO(null, dtl.getItemCode()));
			fbsBudgetService.caluteRelation(dtl, dtlMap, BudgetConstant.B_MONEY,fbsItemMap);//科目公式计算
			FbsBudgetDtlAbs entity = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
			BeanUtils.copyNotNullProperties(dtl, entity);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsService.saveOrUpdate(entity);
		}
		budget.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(budget);
		//生成日志
		fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, FbsMessageConstant.FBS_LOG_COLLECT_SAVE_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		return childBudgetList;
	}
	/**
	 * 资金计划删除
	 *
	 * @param id
	 * @param timeType
	 */
	public void deleteFbsBudget(String id, String timeType, ICurrentUser user) {
		//删除本月
		FbsBudget fbsBudget = fbsBudgetService.get(id, true);
		fbsBudgetService.deleteFbsBudget(id, timeType, user);
        //月滚动模式
        int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
        //是否可以跨年
        boolean isSpanYear = false;
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
        int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
        //保存月份
        List<String> months = new ArrayList<String>();
        for (int i = 1; i <= mimodeRoll; i++) {
            if(!isSpanYear) {
                if (month <= 12) {
					months.add(year+"年"+month + "");
                    month++;
                }
            }else{
				if(month>12) {
					month = month - 12;
					year++;
				}
				months.add(year+"年"+month + "");
				month++;
			}
        }
        Date begindate = fbsBudget.getBeginDate();
        for(int i = 1;i< months.size();i++){
            //取下月预算信息 删除下个月 只能删除暂存状态的下个月预算
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(begindate, fbsBudget.getTimeType());
            FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
            if (null != nextFbsBudget && BudgetConstant.STATUE_TEMP.equals(nextFbsBudget.getStatus())) {
                fbsBudgetService.deleteFbsBudget(nextFbsBudget.getId(), timeType, user);
            }
            begindate =  DateUtils.addMonth(begindate,1);
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
	 * 初始化0预算
	 *
	 * @param fbsBudget
	 */
	public void initBudget(FbsBudgetVO fbsBudget, ICurrentUser user) {
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否可以跨年
		boolean isSpanYear = false;
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
		//保存月份
		List<String> months = new ArrayList<String>();
		for (int i = 1; i <= mimodeRoll; i++) {
			if(!isSpanYear) {
				if (month <= 12) {
					months.add(year+"年"+month + "");
					month++;
				}
			}else{
				if(month>12) {
					month = month - 12;
					year++;
				}
				months.add(year+"年"+month + "");
				month++;
			}
		}
		Date beginDate = fbsBudget.getBeginDate();
		for(int i = 1; i< months.size();i++){
			//取下月预算信息
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			if (null == nextFbsBudget) {
				fbsBudgetService.initBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
			}
			beginDate = DateUtils.addMonth(beginDate,1);
		}
	}
	/**
	 * 获取数据
	 * @param id
	 * @param user
	 * @return
	 * @author zuoy 2018-05-14
	 */
	public Map<String, Object> getRollMonthBudget(String id, ICurrentUser user) {
		Map<String, Object> map = new HashMap<>();
		FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否可以跨年
		boolean isSpanYear = false;
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		if (!EmptyUtils.isEmpty(fbsBudget) && null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpId(fbsBudget.getCorpId());
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存月份
			List<String> months = new ArrayList<String>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(year+"年"+month + "");
						month++;
					}
				}else{
					if(month>12) {
						month = month - 12;
						year++;
					}
					months.add(year+"年"+month + "");
					month++;
				}
			}
			map.put("months", months);
			List<FbsBudgetVO>  fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			if (!EmptyUtils.isEmptyList(listFbsItem)) {
				for(int i = 1; i < months.size(); i++){
					FbsBudgetVO fbsBudgetVO = new FbsBudgetVO();
					FbsBudget fbsBudgetTemp = new FbsBudget();
					FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
					fbsBudgetTemp = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
					if (null == fbsBudgetTemp) {
						fbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
						fbsBudgetTemp = new FbsBudget();
						BeanUtils.copyNotNullProperties(fbsBudgetVO, fbsBudgetTemp);
					} else {
						fbsBudgetVO = fbsBudgetService.getBudget(fbsBudgetTemp.getId());
					}
					beginDate = DateUtils.addMonth(beginDate,1);
					fbsBudgetVOList.add(fbsBudgetVO);
					fbsBudgetList.add(fbsBudgetTemp);
				}
				List<FbsBudgetRollMonthVO> list = new ArrayList<>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					FbsBudgetRollMonthVO vo = new FbsBudgetRollMonthVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					for(int j = 0; j < fbsBudgetVOList.size(); j++){
						FbsBudgetVO fbsBudgetVO = fbsBudgetVOList.get(j);
						if(!EmptyUtils.isEmpty(fbsBudgetVO.getDtlList())){
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = fbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
						}
					}
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					list.add(vo);
				}
				map.put("rollMonthList", list);
			} else {
				map.put("rollMonthList", new ArrayList<FbsBudgetRollMonthVO>());
			}
		} else {
			map.put("rollMonthList", new ArrayList<FbsBudgetRollMonthVO>());
		}
		return map;
	}
}
