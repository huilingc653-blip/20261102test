package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsBudgetDao;
import com.bytter.biz.budget.dao.FbsBudgetRollMonthWeekDao;
import com.bytter.biz.budget.entity.*;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.biz.vo.FbsBudgetInputContentVO;
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
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
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
@Service("fbsBudgetRollMonthWeekService")
@Transactional
public class FbsBudgetRollMonthWeekService extends BaseService<FbsBudget, String> implements IWorkFlowStartedListener {
	@Autowired
	private FbsBudgetDao fbsBudgetDao;
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
	private FbsBudgetRollMonthWeekDao fbsBudgetRollMonthWeekDao;
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
	@Resource
    private IMultiLangService multiLangServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudget entity) {
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
	public void save(FbsBudget entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudget entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudget entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudget, String> getEntityDao() {
		return fbsBudgetDao;
	}
	public void initRollMonthBudget(FbsBudgetVO fbsBudget, Model model, String fictitiousCorp, ICurrentUser user) {
		//是否是虚拟单位
		String corpId = fbsBudget.getCorpId();
		FbsCollectCorpVO fbsCollectCorpVO  = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
		if(EmptyUtils.isEmpty(fbsCollectCorpVO)){
			fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
		}
		if(!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)){
			corpId = fbsCollectCorpVO.getId();
		}
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//如果是双月周明细就固定分解2周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
			mRollSubperiod = 2;
		}
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		boolean isSpanYear = false;
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = null;
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
			}else {
				listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			}
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存月份
			List<FbsBudgetPaternityVO> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
			for (int i = 1; i <= mimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				if (!isSpanYear) {
					if (month <= 12) {
						fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
						months.add(fbsBudgetPaternityVO);
						monthsHid.add(month + "");
						month++;
					}
				} else {
					if (month > 12) {
						month = month - 12;
						year++;
					}
					fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
					months.add(fbsBudgetPaternityVO);
					monthsHid.add(month + "");
					month++;
				}
			}
			List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			//月下属周计划集合
			List<FbsBudgetVO> weekFbsBudgetsTotleVOList = new ArrayList<>();
			List<String> weeks = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			Date endDateRoll = fbsBudget.getEndDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for (int i = 0; i < months.size(); i++) {
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if (i > 0) {
						//取下月预算信息
						FbsBudgetVO nextFbsBudgetVO = null;
						FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
						endDateRoll = nextFbsBudgetPeriodVO.getEndDate();
						FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(corpId, fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
						if (null == nextFbsBudget) {
							nextFbsBudgetVO = fbsBudgetService.initBudget(corpId, fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
							nextFbsBudget = new FbsBudget();
							BeanUtils.copyNotNullProperties(nextFbsBudgetVO, nextFbsBudget);
						} else {
							nextFbsBudgetVO = fbsBudgetService.getBudgetNotItem(nextFbsBudget.getId());
						}
						fbsBudgetVOList.add(nextFbsBudgetVO);
						fbsBudgetList.add(nextFbsBudget);
						beginDate = DateUtils.addMonth(beginDate, 1);
						fbsBudgetVOTemp = nextFbsBudgetVO;
					}
					if (mRollSubperiod > i) {
						//获取月份的周数
						FbsBudgetPeriodVO weekFbsBudgetperiodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, BudgetConstant.TIME_TYPE_WEEK);
						//月下属周计划集合
						List<FbsBudgetVO> weekFbsBudgetsVOList = new ArrayList<FbsBudgetVO>();
						List<String> weeksTemp = new ArrayList<>();
						for (int j = 0; j < weekFbsBudgetperiodVO.getWeek(); j++) {
							weeks.add(j + 1 + "");
							weeksTemp.add(j + 1 + "");
						}
						List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						//是否有周计划
						String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
						List<FbsBudgetDetail> allDetails = null;
						if(CommonConstant.NO.equals(wcrl)) {
							String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
							//获取计划对应的明细
							allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
						}
						if (null != weekFbsBudgets && weekFbsBudgets.size() > 1) {
							Map<Long, Object> weekMap = new HashMap<Long, Object>();
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < weekFbsBudgets.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.getBudgetNotItem(weekFbsBudgets.get(j).getId());
								weekMap.put(weekFbsBudgetVO.getBeginDate().getTime(), weekFbsBudgetVO);
							}
							for (int j = 0; j < periodVOList.size(); j++) {
								if (weekMap.containsKey(periodVOList.get(j).getBeginDate().getTime())) {
									weekFbsBudgetsVOList.add((FbsBudgetVO) weekMap.get(periodVOList.get(j).getBeginDate().getTime()));
								} else {
									FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
											periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
									//初始化明细
									if(allDetails!=null){
										for(FbsBudgetDtlAbsVO vo:weekFbsBudgetVO.getDtlList()){
											boolean flag = false;
											BigDecimal detailMoney = BigDecimal.ZERO;
											for(FbsBudgetDetail detail:allDetails) {
												//本期需要初始化明细计划
												if(weekFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
														&& weekFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
													if (vo.getItemCode().equals(detail.getItemCode())) {
														if(detail.getNatualMoney()==null){
															detail.setNatualMoney(detail.getBMoney()==null?BigDecimal.ZERO:detail.getBMoney());
														}
														detailMoney = detailMoney.add(detail.getNatualMoney());
														flag = true;
													}
												}
											}
											if(flag) {
												vo.setbMoney(detailMoney);
												vo.setDetailMoney(detailMoney);
											}
										}
									}
									weekFbsBudgetsVOList.add(weekFbsBudgetVO);
								}
							}
						} else {//如果没有预算就初始化预算
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < periodVOList.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
										periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
								//初始化明细
								if(allDetails!=null){
									for(FbsBudgetDtlAbsVO vo:weekFbsBudgetVO.getDtlList()){
										boolean flag = false;
										BigDecimal detailMoney = BigDecimal.ZERO;
										for(FbsBudgetDetail detail:allDetails) {
											//本期需要初始化明细计划
											if(weekFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
													&& weekFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
												if (vo.getItemCode().equals(detail.getItemCode())) {
													if(detail.getNatualMoney()==null){
														detail.setNatualMoney(detail.getBMoney()==null?BigDecimal.ZERO:detail.getBMoney());
													}
													detailMoney = detailMoney.add(detail.getNatualMoney());
													flag = true;
												}
											}
										}
										if(flag) {
											vo.setbMoney(detailMoney);
											vo.setDetailMoney(detailMoney);
										}
									}
								}
								weekFbsBudgetsVOList.add(weekFbsBudgetVO);
							}
						}
						months.get(i).setChildList(weeksTemp);
						months.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
						months.get(i).setChildFbsBudgetVOList(weekFbsBudgetsVOList);
						weekFbsBudgetsTotleVOList.addAll(weekFbsBudgetsVOList);
					}
				}
				model.addAttribute("endDateRoll", endDateRoll);
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollMonthWeek(fbsBudget.getId(), fbsBudget.getTimeType(), BudgetConstant.BUDGET_TARGET_INPUT, weekFbsBudgetsTotleVOList);
				List<FbsBudgetRollMonthWeekVO> list = new ArrayList<FbsBudgetRollMonthWeekVO>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetRollMonthWeekVO vo = new FbsBudgetRollMonthWeekVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(fbsBudget.getBeginDate()));
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					for (int j = 0; j < fbsBudgetVOList.size(); j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(nextFbsBudgetVO.getBeginDate())));
						}
					}
					int size = 0;
					int j = 0;
					List<List<FbsBudgetDtlAbsVO>> childWeekBudget = new ArrayList<>();
					for (int k = 0; k < months.size(); k++) {
						if (!EmptyUtils.isEmptyList(months.get(k).getChildList()) && months.get(k).getChildList().size() > 0) {
							List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
							size += months.get(k).getChildList().size();
							if (weekFbsBudgetsTotleVOList.size() <= size) {
								size = weekFbsBudgetsTotleVOList.size();
							}
							for (; j < size; j++) {
								FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = weekFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
								fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(vo.getId() + "_" + vo.getItemCode() + "_" + (j + months.size() + 1)));
								absVOList.add(fbsBudgetDtlAbsVOWeek);
							}
							childWeekBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childWeekBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					vo.setRmk(fbsBudgetDtlAbsVO.getRmk());
					list.add(vo);
				}
				model.addAttribute("rollMonthList", list);
				model.addAttribute("fbsBudgetList", fbsBudgetList);
			} else {
				model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthWeekVO>());
				model.addAttribute("fbsBudgetList", new ArrayList<FbsBudget>());
			}
			model.addAttribute("months", months);
			model.addAttribute("monthsHid", monthsHid);
			model.addAttribute("weeks", weeks);
			model.addAttribute("mRollSubperiod", mRollSubperiod);
		} else {
			model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthWeekVO>());
			model.addAttribute("fbsBudgetList", new ArrayList<FbsBudget>());
		}
	}
	public void initRollMonthBudget(FbsBudgetVO fbsBudget, FbsBudgetInputContentVO model, String fictitiousCorp, ICurrentUser user) {
		//是否是虚拟单位
		String corpId = fbsBudget.getCorpId();
		FbsCollectCorpVO fbsCollectCorpVO  = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
		if(EmptyUtils.isEmpty(fbsCollectCorpVO)){
			fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
		}
		if(!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)){
			corpId = fbsCollectCorpVO.getId();
		}
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//如果是双月周明细就固定分解2周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
			mRollSubperiod = 2;
		}
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		boolean isSpanYear = false;
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = null;
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
			}else {
				listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			}
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存月份
			List<FbsBudgetPaternityVO> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
			for (int i = 1; i <= mimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				if (!isSpanYear) {
					if (month <= 12) {
						fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
						months.add(fbsBudgetPaternityVO);
						monthsHid.add(month + "");
						month++;
					}
				} else {
					if (month > 12) {
						month = month - 12;
						year++;
					}
					fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
					months.add(fbsBudgetPaternityVO);
					monthsHid.add(month + "");
					month++;
				}
			}
			List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			//月下属周计划集合
			List<FbsBudgetVO> weekFbsBudgetsTotleVOList = new ArrayList<>();
			List<String> weeks = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			Date endDateRoll = fbsBudget.getEndDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for (int i = 0; i < months.size(); i++) {
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if (i > 0) {
						//取下月预算信息
						FbsBudgetVO nextFbsBudgetVO = null;
						FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
						endDateRoll = nextFbsBudgetPeriodVO.getEndDate();
						FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(corpId, fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
						if (null == nextFbsBudget) {
							nextFbsBudgetVO = fbsBudgetService.initBudget(corpId, fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
							nextFbsBudget = new FbsBudget();
							BeanUtils.copyNotNullProperties(nextFbsBudgetVO, nextFbsBudget);
						} else {
							nextFbsBudgetVO = fbsBudgetService.getBudgetNotItem(nextFbsBudget.getId());
						}
						fbsBudgetVOList.add(nextFbsBudgetVO);
						fbsBudgetList.add(nextFbsBudget);
						beginDate = DateUtils.addMonth(beginDate, 1);
						fbsBudgetVOTemp = nextFbsBudgetVO;
					}
					if (mRollSubperiod > i) {
						//获取月份的周数
						FbsBudgetPeriodVO weekFbsBudgetperiodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, BudgetConstant.TIME_TYPE_WEEK);
						//月下属周计划集合
						List<FbsBudgetVO> weekFbsBudgetsVOList = new ArrayList<FbsBudgetVO>();
						List<String> weeksTemp = new ArrayList<>();
						for (int j = 0; j < weekFbsBudgetperiodVO.getWeek(); j++) {
							weeks.add(j + 1 + "");
							weeksTemp.add(j + 1 + "");
						}
						List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(corpId, BudgetConstant.TIME_TYPE_WEEK, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						//是否有周计划
						String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
						List<FbsBudgetDetail> allDetails = null;
						if(CommonConstant.NO.equals(wcrl)) {
							String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
							//获取计划对应的明细
							allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
						}
						if (null != weekFbsBudgets && weekFbsBudgets.size() > 1) {
							Map<Long, Object> weekMap = new HashMap<Long, Object>();
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < weekFbsBudgets.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.getBudgetNotItem(weekFbsBudgets.get(j).getId());
								weekMap.put(weekFbsBudgetVO.getBeginDate().getTime(), weekFbsBudgetVO);
							}
							for (int j = 0; j < periodVOList.size(); j++) {
								if (weekMap.containsKey(periodVOList.get(j).getBeginDate().getTime())) {
									weekFbsBudgetsVOList.add((FbsBudgetVO) weekMap.get(periodVOList.get(j).getBeginDate().getTime()));
								} else {
									FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
											periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
									//初始化明细
									if(allDetails!=null){
										for(FbsBudgetDtlAbsVO vo:weekFbsBudgetVO.getDtlList()){
											boolean flag = false;
											BigDecimal detailMoney = BigDecimal.ZERO;
											for(FbsBudgetDetail detail:allDetails) {
												//本期需要初始化明细计划
												if(weekFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
														&& weekFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
													if (vo.getItemCode().equals(detail.getItemCode())) {
														if(detail.getNatualMoney()==null){
															detail.setNatualMoney(detail.getBMoney()==null?BigDecimal.ZERO:detail.getBMoney());
														}
														detailMoney = detailMoney.add(detail.getNatualMoney());
														flag = true;
													}
												}
											}
											if(flag) {
												vo.setbMoney(detailMoney);
												vo.setDetailMoney(detailMoney);
											}
										}
									}
									weekFbsBudgetsVOList.add(weekFbsBudgetVO);
								}
							}
						} else {//如果没有预算就初始化预算
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < periodVOList.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
										periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
								//初始化明细
								if(allDetails!=null){
									for(FbsBudgetDtlAbsVO vo:weekFbsBudgetVO.getDtlList()){
										boolean flag = false;
										BigDecimal detailMoney = BigDecimal.ZERO;
										for(FbsBudgetDetail detail:allDetails) {
											//本期需要初始化明细计划
											if(weekFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
													&& weekFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
												if (vo.getItemCode().equals(detail.getItemCode())) {
													if(detail.getNatualMoney()==null){
														detail.setNatualMoney(detail.getBMoney()==null?BigDecimal.ZERO:detail.getBMoney());
													}
													detailMoney = detailMoney.add(detail.getNatualMoney());
													flag = true;
												}
											}
										}
										if(flag) {
											vo.setbMoney(detailMoney);
											vo.setDetailMoney(detailMoney);
										}
									}
								}
								weekFbsBudgetsVOList.add(weekFbsBudgetVO);
							}
						}
						months.get(i).setChildList(weeksTemp);
						months.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
						months.get(i).setChildFbsBudgetVOList(weekFbsBudgetsVOList);
						weekFbsBudgetsTotleVOList.addAll(weekFbsBudgetsVOList);
					}
				}
				model.setEndDateRoll( endDateRoll);
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollMonthWeek(fbsBudget.getId(), fbsBudget.getTimeType(), BudgetConstant.BUDGET_TARGET_INPUT, weekFbsBudgetsTotleVOList);
				List<FbsBudgetRollMonthWeekVO> list = new ArrayList<FbsBudgetRollMonthWeekVO>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetRollMonthWeekVO vo = new FbsBudgetRollMonthWeekVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(fbsBudget.getBeginDate()));
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					for (int j = 0; j < fbsBudgetVOList.size(); j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(nextFbsBudgetVO.getBeginDate())));
						}
					}
					int size = 0;
					int j = 0;
					List<List<FbsBudgetDtlAbsVO>> childWeekBudget = new ArrayList<>();
					for (int k = 0; k < months.size(); k++) {
						if (!EmptyUtils.isEmptyList(months.get(k).getChildList()) && months.get(k).getChildList().size() > 0) {
							List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
							size += months.get(k).getChildList().size();
							if (weekFbsBudgetsTotleVOList.size() <= size) {
								size = weekFbsBudgetsTotleVOList.size();
							}
							for (; j < size; j++) {
								FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = weekFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
								fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(vo.getId() + "_" + vo.getItemCode() + "_" + (j + months.size() + 1)));
								absVOList.add(fbsBudgetDtlAbsVOWeek);
							}
							childWeekBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childWeekBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					vo.setRmk(fbsBudgetDtlAbsVO.getRmk());
					list.add(vo);
				}
				model.setRollMonthList(  list);
				model.setFbsBudgetList( fbsBudgetList);
			} else {
				model.setRollMonthList(  new ArrayList<FbsBudgetRollMonthWeekVO>());
				model.setFbsBudgetList(  new ArrayList<FbsBudget>());
			}
			model.setMonths(   months);
			model.setMonthsHid(  monthsHid);
			model.setWeeks(  weeks);
			model.setMRollSubperiod(  mRollSubperiod);
		} else {
			model.setRollMonthList( new ArrayList<FbsBudgetRollMonthWeekVO>());
			model.setFbsBudgetList(new ArrayList<FbsBudget>());
		}
	}
	/**
	 * 保存
	 *
	 * @param input
	 */
	public void saveFbsBudget(FbsBudgetInputRollMonthWeekVO input, HttpServletRequest request, ICurrentUser user, boolean isApprove) {
		FbsBudget entity = fbsBudgetService.get(input.getId(), true);
		//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额，同时状态更新为待汇总状态
		FbsBudget preCorpBudget = fbsBudgetService.subtractFbsBudget(entity, false);
		boolean isRoll = false;
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//如果是双月周明细就固定分解2周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
			mRollSubperiod = 2;
		}
		String wcrl = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WCRL);
		String[] nextIds = request.getParameterValues("nextId");
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
			isRoll = true;
		} else {
			entity.setStatus(input.getStatus());
		}
		entity.setAttachmentId(input.getAttachmentId());
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		List<FbsBudget> fbsBudgetList = new ArrayList<>();
		//存在多少个周
		int weeks = 0;
		//周总计集合
		List<FbsBudget> weekFbsBudgetsTotle = new ArrayList<>();
		//下属周计划保存 begin
		List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(entity.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, entity.getBeginDate(), entity.getEndDate());
		weekFbsBudgetsTotle.addAll(weekFbsBudgets);
		for (int i = 0; i < weekFbsBudgets.size(); i++) {
			FbsBudget t = weekFbsBudgets.get(i);
			//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额
			if (this.fbsCorpBudgetService.getCollectFlag(t.getCorpId())) {
				fbsBudgetService.subtractFbsBudget(t, isRoll);
			}
			//如果是双月周明细参数并且没有周计划则直接周计划编制完成
			if(CommonConstant.NO.equalsIgnoreCase(wcrl) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
				t.setStatus(BudgetConstant.STATUE_CHECK);
			}else {
				t.setStatus(BudgetConstant.STATUE_TEMP);
			}
			t.setCurId(input.getCurId());
			t.setExchangeRate(input.getExchangeRate());
			t.setUpdateBy(user.getLoginName());
			t.setUpdateDate(DateUtils.getDate());
			weeks++;
		}
		if (!EmptyUtils.isEmpty(nextIds)) {
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
					if (mRollSubperiod - 1 > i) {
						List<FbsBudget> nextWeekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(nextEntity.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, nextEntity.getBeginDate(), nextEntity.getEndDate());
						for (int k = 0; k < nextWeekFbsBudgets.size(); k++) {
							FbsBudget t = nextWeekFbsBudgets.get(k);
							//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额
							if (this.fbsCorpBudgetService.getCollectFlag(t.getCorpId())) {
								fbsBudgetService.subtractFbsBudget(t, isRoll);
							}
							//如果是双月周明细参数并且没有周计划则直接周计划编制完成
							if(CommonConstant.NO.equalsIgnoreCase(wcrl) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
								t.setStatus(BudgetConstant.STATUE_CHECK);
							}else {
								t.setStatus(BudgetConstant.STATUE_TEMP);
							}
							t.setCurId(input.getCurId());
							t.setExchangeRate(input.getExchangeRate());
							t.setUpdateBy(user.getLoginName());
							t.setUpdateDate(DateUtils.getDate());
							weeks++;
						}
						weekFbsBudgetsTotle.addAll(nextWeekFbsBudgets);
					}
				}
			}
		}
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<BigDecimal> nextCheckMoneyList = new ArrayList<>();
		List<BigDecimal> weekCheckMoneyList = new ArrayList<BigDecimal>();
		String[] detailIds = request.getParameterValues("detailId");
		try {
			for (int i = 0; i < detailIds.length; i++) {
				//获取明细
				FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(detailIds[i], entity.getTimeType());
				if (null != dtl) {
					if (null != input.getbMoney1()[i]) {
						dtl.setbMoney(input.getbMoney1()[i]);
						dtl.setRmk(input.getRmk()[i]);
					} else {
						dtl.setbMoney(BigDecimal.ZERO);
					}
					if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
						checkMoney = dtl.getbMoney();
					}
					fbsBudgetDtlAbsService.saveOrUpdate(dtl);
				}
				for (int j = 0; j < fbsBudgetList.size(); j++) {
					//保存下月明细
					FbsBudget nextEntity = fbsBudgetList.get(j);
					BigDecimal nextCheckMoney = BigDecimal.ZERO;
					String[] nextBMoney = request.getParameterValues("bMoney" + (j + 2));
					String[] nextDetailId = request.getParameterValues("detailId" + (j + 2));
					if (null != nextEntity && !BudgetConstant.STATUE_CHECK.equals(nextEntity.getStatus())) {
						BigDecimal money = BigDecimal.ZERO;
						if (StringUtils.isNotEmpty(nextBMoney[i])) {
							money = new BigDecimal(Double.valueOf(nextBMoney[i]));
						}
						if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
							nextCheckMoney = money;
						}
						fbsBudgetDtlAbsService.updateByDtlId(nextDetailId[i], entity.getTimeType(), money);
					}
					nextCheckMoneyList.add(nextCheckMoney);
				}
				for (int k = 1; k <= weeks; k++) {
					//保存周明细weekDetailId
					BigDecimal weekCheckMoney = BigDecimal.ZERO;
					int nextIdLength = nextIds == null?0:nextIds.length;
					String[] weekDetailIds = request.getParameterValues("weekDetailId" + (k + nextIdLength + 1));
					String[] bMoneyWeeks = request.getParameterValues("bMoneyWeek" + (k + nextIdLength + 1));
					if (!EmptyUtils.isEmpty(weekDetailIds)) {
						BigDecimal money = BigDecimal.ZERO;
						if (!EmptyUtils.isEmpty(bMoneyWeeks[i])) {
							money = new BigDecimal(Double.parseDouble(bMoneyWeeks[i]));
						}
						if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
							weekCheckMoney = money;
						}
						fbsBudgetDtlAbsService.updateByDtlId(weekDetailIds[i], BudgetConstant.TIME_TYPE_WEEK, money);
					}
					weekCheckMoneyList.add(weekCheckMoney);
				}
//				for (int j = 1; j <= 6; j++) {
//					BigDecimal weekCheckMoney = BigDecimal.ZERO;
//					Method m = input.getClass().getMethod("getWeekDetailId" + j, new Class[0]);
//					Method m1 = input.getClass().getMethod("getbMoneyWeek" + j, new Class[0]);
//					if (null == m.invoke(input)) {
//						break;
//					}
//					BigDecimal money = BigDecimal.ZERO;
//					if (StringUtils.isNotEmpty(((BigDecimal[]) m1.invoke(input))[i])) {
//						money = ((BigDecimal[]) m1.invoke(input))[i];
//					}
//					if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
//						weekCheckMoney = money;
//					}
//					fbsBudgetDtlAbsService.updateByDtlId(((String[]) m.invoke(input))[i], BudgetConstant.TIME_TYPE_WEEK, money);
//					weekCheckMoneyList.add(weekCheckMoney);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		//周计划汇总
		for (int i = 0; i < weekFbsBudgetsTotle.size(); i++) {
			FbsBudget t = weekFbsBudgetsTotle.get(i);
			if (!EmptyUtils.isEmptyList(weekCheckMoneyList) && weekCheckMoneyList.size() >= i) {
				t.setCheckMoney(weekCheckMoneyList.get(i) == null ? BigDecimal.ZERO : weekCheckMoneyList.get(i));
			}
			fbsBudgetService.saveOrUpdate(t);
			//周计划汇总
			saveFbsBudgetCollectUnderCorp(t, user);
		}
		//end
		for (int i = 0; i < fbsBudgetList.size(); i++) {
			FbsBudget nextEntity = fbsBudgetList.get(i);
			if (null != nextEntity) {
				if (nextCheckMoneyList.size() > i) {
					nextEntity.setCheckMoney(nextCheckMoneyList.get(i));
				} else {
					nextEntity.setCheckMoney(BigDecimal.ZERO);
				}
				fbsBudgetService.saveOrUpdate(nextEntity);
				//资金计划汇总提交，下级单位计划处理
				saveFbsBudgetCollectUnderCorp(nextEntity, user);
			}
		}
		if (isApprove) {
			String msg = !EmptyUtils.isBlank(input.getMsg()) ? input.getMsg() : FbsMessageConstant.FBS_LOG_APP_SAVE_SUCC;
			//生成日志
			fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_EDIT, msg, FbsMessageConstant.BUS_NAME_INPUT, user);
		} else {
			boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
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
		CorpDTO corp = corpService.get(entity.getCorpId());
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
						//生成日志
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
							String timeType = budget.getTimeType();
							//月计划模式控制
							if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
								//mRollSubperiod-月滚动模式 滚动分解子周期
								int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
								//单位分解周计划也更新为零计划
								if(mRollSubperiod >0){
									List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(budget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, budget.getBeginDate(), budget.getEndDate(), null);
									for(FbsBudget weekBudget :budgets){
										fbsBudgetDtlAbsService.clearInputMoney(weekBudget.getId(), weekBudget.getTimeType());
									}
								}
							}
						}
						budget.setStatus(status);
						fbsBudgetService.saveOrUpdate(budget);
					}
					//判断单位是否汇总下级单位
					if (fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId())) {
						List<CorpDTO> listCorpTemp = corpService.listUnderCorpByParentCorpId(corp.getId());
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
		//获取汇总单位计划
		FbsBudget budget = this.get(input.getId(), true);
		//是否清除汇总计划
		Boolean isClear = true;
		List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(budget.getCorpId());
		if (!EmptyUtils.isEmptyList(list)) {
			for (FbsCorpBudgetDTO dto : list) {
				//过滤计划不控制单位
				if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
					continue;
				}
				//获取下级单位已上报计划，未上报计划，则初始化计划上报数据
				FbsBudget childBudget = fbsBudgetService.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
				if(childBudget != null){
					if(childBudget.getStatus()> BudgetConstant.STATUE_WAIT_COLLECT){
						isClear = false;
					}
				}
			}
		}
		if(isClear) {
			//清除汇总单位计划
			fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
			//先删除汇总明细
			fbsBudgetDetailService.delColloctDetail(budget.getId(), budget.getTimeType());
		}
		fbsBudgetService.collectFbsBudget(input, childIds, user);
		// 当前月对应周
		List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(budget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, budget.getBeginDate(), budget.getEndDate());
		for (FbsBudget weekBudget : weekFbsBudgets) {
			List<String> childWeekIds = new ArrayList<>();
			for (String childId : childIds) {
				// 汇总月对应周
				FbsBudget childBudget = this.get(childId, true);
				FbsBudget weekChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, weekBudget.getBeginDate(), weekBudget.getEndDate());
				if (!EmptyUtils.isEmpty(weekChildBudget)) {
					childWeekIds.add(weekChildBudget.getId());
				}
			}
			FbsBudgetInputVO weekInput = new FbsBudgetInputVO();
			weekInput.setId(weekBudget.getId());
			weekInput.setTimeType(weekBudget.getTimeType());
			if(isClear) {
				//清除汇总单位计划
				fbsBudgetDtlAbsService.clearInputMoney(weekInput.getId(), weekBudget.getTimeType());
			}
			fbsBudgetService.collectFbsBudgetNotUpdateStatus(weekInput, childWeekIds, user);
		}
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//如果是双月周明细就固定分解2周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		if(BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
			mRollSubperiod = 2;
		}
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		boolean isSpanYear = false;
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		List<String> months = new ArrayList<>();
		List<String> monthsHid = new ArrayList<>();
        String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
		for (int i = 1; i <= mimodeRoll; i++) {
			if (!isSpanYear) {
				if (month <= 12) {
					months.add(year + yearLang + month + "");
					monthsHid.add(month + "");
					month++;
				}
			} else {
				if (month > 12) {
					month = month - 12;
					year++;
				}
				months.add(year + yearLang + month + "");
				monthsHid.add(month + "");
				month++;
			}
		}
		Date beginDate = budget.getBeginDate();
		for (int k = 1; k < months.size(); k++) {
			//取下月预算信息
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			List<FbsBudgetInputPreVO> nextChildFbsBudget = fbsBudgetService.getChildCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			List<String> nextChildIds = new ArrayList<>();
			List<FbsBudgetInputPreVO> nextChildFbsBudgetTemp = new ArrayList<>();
			for (String id : childIds) {
				FbsBudget t = fbsBudgetService.get(id);
				for (FbsBudgetInputPreVO vo : nextChildFbsBudget) {
					if (vo.getCorpId().equals(t.getCorpId())) {
						nextChildFbsBudgetTemp.add(vo);
					}
				}
			}
			for (int i = 0; i < nextChildFbsBudgetTemp.size(); i++) {
				if (StringUtils.isEmpty(nextChildFbsBudgetTemp.get(i).getId())) {
					continue;
				}
				FbsBudgetVO vo = fbsBudgetService.getBudgetNotItem(nextChildFbsBudgetTemp.get(i).getId());
				if (null == vo || null == vo.getDtlList()) {
					continue;
				}
				nextChildIds.add(nextChildFbsBudgetTemp.get(i).getId());
			}
			if (null == nextFbsBudget || (null != nextFbsBudget && nextFbsBudget.getStatus().equals(BudgetConstant.STATUE_TEMP))) {
				FbsBudgetInputVO nextInput = new FbsBudgetInputVO();
				nextInput.setId(nextFbsBudget.getId());
				this.collectNextFbsBudget(nextInput, nextChildIds, user,isClear);
			}
			beginDate = DateUtils.addMonth(beginDate, 1);
			if (k < mRollSubperiod) {
				// 当前月对应周
				List<FbsBudget> nextweekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
						(nextFbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
				for (FbsBudget weekBudget : nextweekFbsBudgets) {
					List<String> childWeekIds = new ArrayList<>();
					for (String childId : childIds) {
						// 汇总月对应周
						FbsBudget childBudget = this.get(childId, true);
						FbsBudget weekChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, weekBudget.getBeginDate(), weekBudget.getEndDate());
						if (!EmptyUtils.isEmpty(weekChildBudget)) {
							childWeekIds.add(weekChildBudget.getId());
						}
					}
					FbsBudgetInputVO weekInput = new FbsBudgetInputVO();
					weekInput.setId(weekBudget.getId());
					weekInput.setTimeType(weekBudget.getTimeType());
					if(isClear) {
						//清除汇总单位计划
						fbsBudgetDtlAbsService.clearInputMoney(weekInput.getId(), weekBudget.getTimeType());
					}
					fbsBudgetService.collectFbsBudgetNotUpdateStatus(weekInput, childWeekIds, user);
				}
			}
		}
	}
	/**
	 * 汇总下期计划
	 *
	 * @param input
	 * @param childIds
	 * @param user
	 * @return
	 */
	public List<FbsBudget> collectNextFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user, Boolean isClear) {
		//获取汇总单位计划
		FbsBudget budget = fbsBudgetService.get(input.getId(), true);
		if(isClear) {
			//清除汇总单位计划
			fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
		}
		//获取汇总单位计划明细
		List<FbsBudgetDtlAbsVO> listDtl = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
		HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
		List<FbsBudget> childBudgetList = new ArrayList<FbsBudget>();
		//汇总明细
		fbsBudgetDetailService.colloctDetail(input.getId(), childIds,isClear);
		//获取下级单位计划
		for (String childId : childIds) {
			//获取汇总单位计划
			FbsBudget childBudget = fbsBudgetService.get(childId, true);
			List<FbsBudgetDtlAbsVO> listChildDtl = fbsBudgetDtlAbsService.listByBudgetId(childBudget.getId(), childBudget.getTimeType());
			//循环设置汇总单位的计划
			for (FbsBudgetDtlAbsVO dtl : listDtl) {
				for (FbsBudgetDtlAbsVO childDtl : listChildDtl) {
					if (dtl.getItemCode().equals(childDtl.getItemCode())) {
						if (childDtl.getbMoney() != null) {
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
		for (FbsItem fbsItem : fbsItemList) {
			fbsItemMap.put(fbsItem.getItemCode(), fbsItem);
		}
		for (FbsBudgetDtlAbsVO dtl : listDtl) {
			FbsItem fbsItem = fbsItemMap.get(dtl.getItemCode());
			FbsItemVO fbsItemVO = new FbsItemVO();
			BeanUtils.copyNotNullProperties(fbsItem, fbsItemVO);
			dtl.setItem(fbsItemVO);
			fbsBudgetService.caluteRelation(dtl, dtlMap, BudgetConstant.B_MONEY, fbsItemMap);//科目公式计算
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
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年
		boolean isSpanYear = false;
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
		List<String> months = new ArrayList<>();
		List<String> monthsHid = new ArrayList<>();
		String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
		for (int i = 1; i <= mimodeRoll; i++) {
			if (!isSpanYear) {
				if (month <= 12) {
					months.add(year + yearLang + month + "");
					monthsHid.add(month + "");
					month++;
				}
			} else {
				if (month > 12) {
					month = month - 12;
					year++;
				}
				months.add(year + yearLang + month + "");
				monthsHid.add(month + "");
				month++;
			}
		}
		//下属周计划信息
		List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, fbsBudget.getBeginDate(), fbsBudget.getEndDate());
		if (!EmptyUtils.isEmptyList(weekFbsBudgets)) {
			for (int i = 0; i < weekFbsBudgets.size(); i++) {
				fbsBudgetService.getChildCorpBudget(weekFbsBudgets.get(i).getCorpId(), weekFbsBudgets.get(i).getTimeType(),
						weekFbsBudgets.get(i).getBeginDate(), weekFbsBudgets.get(i).getEndDate());
				if (BudgetConstant.STATUE_TEMP.equals(weekFbsBudgets.get(i).getStatus())
						|| BudgetConstant.STATUE_BACK.equals(weekFbsBudgets.get(i).getStatus())) {
					fbsBudgetService.deleteFbsBudget(weekFbsBudgets.get(i).getId(), weekFbsBudgets.get(i).getTimeType(), user);
				}
			}
		}
		Date begindate = fbsBudget.getBeginDate();
		for (int i = 1; i < months.size(); i++) {
			//取下月预算信息 删除下个月 只能删除暂存状态的下个月预算
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(begindate, fbsBudget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			if (null != nextFbsBudget && BudgetConstant.STATUE_TEMP.equals(nextFbsBudget.getStatus())) {
				fbsBudgetService.deleteFbsBudget(nextFbsBudget.getId(), timeType, user);
				begindate = DateUtils.addMonth(begindate, 1);
				//下属周计划信息
				List<FbsBudget> nextWeekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
						(nextFbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
				if (!EmptyUtils.isEmptyList(nextWeekFbsBudgets)) {
					for (int j = 0; j < nextWeekFbsBudgets.size(); j++) {
						fbsBudgetService.getChildCorpBudget(nextWeekFbsBudgets.get(j).getCorpId(), nextWeekFbsBudgets.get(j).getTimeType(),
								nextWeekFbsBudgets.get(j).getBeginDate(), nextWeekFbsBudgets.get(j).getEndDate());
						if (BudgetConstant.STATUE_TEMP.equals(nextWeekFbsBudgets.get(j).getStatus())
								|| BudgetConstant.STATUE_BACK.equals(nextWeekFbsBudgets.get(j).getStatus())) {
							fbsBudgetService.deleteFbsBudget(nextWeekFbsBudgets.get(j).getId(), nextWeekFbsBudgets.get(j).getTimeType(), user);
						}
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
		if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
			//下属周计划保存 begin
			List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
					(entity.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, entity.getBeginDate(), entity.getEndDate());
			for (int i = 0; i < weekFbsBudgets.size(); i++) {
				FbsBudget t = weekFbsBudgets.get(i);
				t.setStatus(entity.getStatus());
				t.setUpdateBy(user.getLoginName());
				t.setUpdateDate(DateUtils.getDate());
				fbsBudgetService.saveOrUpdate(t);
				//生成日志
				fbsMessageService.saveMessage(t.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_APP_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
				//周计划汇总
				saveFbsBudgetCollectUnderCorp(t, user);
			}
			//end
		}
	}
	/**
	 * 初始化0预算
	 *
	 * @param fbsBudget
	 */
	public void initBudget(FbsBudgetVO fbsBudget, ICurrentUser user) {
		List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, fbsBudget.getBeginDate(), fbsBudget.getEndDate());
		//初始化周预算
		if (EmptyUtils.isEmptyList(weekFbsBudgets)) {
			List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudget.getBeginDate());
			for (int i = 0; i < periodVOList.size(); i++) {
				fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(i).getBeginDate(),
						periodVOList.get(i).getEndDate(), BudgetConstant.STATUE_TEMP, user);
			}
		}
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年
		boolean isSpanYear = false;
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
		List<String> months = new ArrayList<>();
		List<String> monthsHid = new ArrayList<>();
		String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
		for (int i = 1; i <= mimodeRoll; i++) {
			if (!isSpanYear) {
				if (month <= 12) {
					months.add(year + yearLang + month + "");
					monthsHid.add(month + "");
					month++;
				}
			} else {
				if (month > 12) {
					month = month - 12;
					year++;
				}
				months.add(year + yearLang + month + "");
				monthsHid.add(month + "");
				month++;
			}
		}
		Date beginDate = fbsBudget.getBeginDate();
		for (int i = 1; i < months.size(); i++) {
			//取下月预算信息
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			if (null == nextFbsBudget) {
				fbsBudgetService.initBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
			}
			beginDate = DateUtils.addMonth(beginDate, 1);
		}
	}
	public List<FbsBudgetRollMonthWeekVO> listRollMonthBudget(String id, ICurrentUser user) throws Exception {
		FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(id);
		List<FbsBudgetRollMonthWeekVO> list = new ArrayList<FbsBudgetRollMonthWeekVO>();
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//是否可以跨年
		boolean isSpanYear = false;
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存月份
			List<FbsBudgetPaternityVO> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			String yearLang = multiLangServiceI.getLang("table.fbsBudgetReport.year");
			for (int i = 1; i <= mimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				if (!isSpanYear) {
					if (month <= 12) {
						fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
						months.add(fbsBudgetPaternityVO);
						monthsHid.add(month + "");
						month++;
					}
				} else {
					if (month > 12) {
						month = month - 12;
						year++;
					}
					fbsBudgetPaternityVO.setPaternal(year + yearLang + month + "");
					months.add(fbsBudgetPaternityVO);
					monthsHid.add(month + "");
					month++;
				}
			}
			List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			//月下属周计划集合
			List<FbsBudgetVO> weekFbsBudgetsTotleVOList = new ArrayList<>();
			List<String> weeks = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for (int i = 0; i < months.size(); i++) {
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if (i > 0) {
						//取下月预算信息
						FbsBudgetVO nextFbsBudgetVO = null;
						FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, fbsBudget.getTimeType());
						FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
						if (null == nextFbsBudget) {
							nextFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
							nextFbsBudget = new FbsBudget();
							BeanUtils.copyNotNullProperties(nextFbsBudgetVO, nextFbsBudget);
						} else {
							nextFbsBudgetVO = fbsBudgetService.getBudgetNotItem(nextFbsBudget.getId());
						}
						fbsBudgetVOList.add(nextFbsBudgetVO);
						fbsBudgetList.add(nextFbsBudget);
						beginDate = DateUtils.addMonth(beginDate, 1);
						fbsBudgetVOTemp = nextFbsBudgetVO;
					}
					if (mRollSubperiod > i) {
						//获取月份的周数
						FbsBudgetPeriodVO weekFbsBudgetperiodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, BudgetConstant.TIME_TYPE_WEEK);
						//月下属周计划集合
						List<FbsBudgetVO> weekFbsBudgetsVOList = new ArrayList<FbsBudgetVO>();
						List<String> weeksTemp = new ArrayList<>();
						for (int j = 0; j < weekFbsBudgetperiodVO.getWeek(); j++) {
							weeks.add(j + 1 + "");
							weeksTemp.add(j + 1 + "");
						}
						List<FbsBudget> weekFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(fbsBudgetVOTemp.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						if (null != weekFbsBudgets && weekFbsBudgets.size() > 1) {
							Map<Long, Object> weekMap = new HashMap<Long, Object>();
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < weekFbsBudgets.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.getBudgetNotItem(weekFbsBudgets.get(j).getId());
								weekMap.put(weekFbsBudgetVO.getBeginDate().getTime(), weekFbsBudgetVO);
							}
							for (int j = 0; j < periodVOList.size(); j++) {
								if (weekMap.containsKey(periodVOList.get(j).getBeginDate().getTime())) {
									weekFbsBudgetsVOList.add((FbsBudgetVO) weekMap.get(periodVOList.get(j).getBeginDate().getTime()));
								} else {
									FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
											periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
									weekFbsBudgetsVOList.add(weekFbsBudgetVO);
								}
							}
						} else {//如果没有预算就初始化预算
							List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudgetVOTemp.getBeginDate());
							for (int j = 0; j < periodVOList.size(); j++) {
								FbsBudgetVO weekFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_WEEK, periodVOList.get(j).getBeginDate(),
										periodVOList.get(j).getEndDate(), BudgetConstant.STATUE_TEMP, user);
								weekFbsBudgetsVOList.add(weekFbsBudgetVO);
							}
						}
						months.get(i).setChildList(weeksTemp);
						months.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
						months.get(i).setChildFbsBudgetVOList(weekFbsBudgetsVOList);
						weekFbsBudgetsTotleVOList.addAll(weekFbsBudgetsVOList);
					}
				}
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollMonthWeek(fbsBudget.getId(), fbsBudget.getTimeType(), BudgetConstant.BUDGET_TARGET_INPUT, weekFbsBudgetsTotleVOList);
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetRollMonthWeekVO vo = new FbsBudgetRollMonthWeekVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(fbsBudget.getBeginDate()));
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					for (int j = 0; j < fbsBudgetVOList.size(); j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(nextFbsBudgetVO.getBeginDate())));
						}
					}
					int size = 0;
					int j = 0;
					List<List<FbsBudgetDtlAbsVO>> childWeekBudget = new ArrayList<>();
					for (int k = 0; k < months.size(); k++) {
						if (!EmptyUtils.isEmptyList(months.get(k).getChildList()) && months.get(k).getChildList().size() > 0) {
							List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
							size += months.get(k).getChildList().size();
							if (weekFbsBudgetsTotleVOList.size() <= size) {
								size = weekFbsBudgetsTotleVOList.size();
							}
							for (; j < size; j++) {
								FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = weekFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
								fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(vo.getId() + "_" + vo.getItemCode() + "_" + (j + months.size() + 1)));
								absVOList.add(fbsBudgetDtlAbsVOWeek);
							}
							childWeekBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childWeekBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					list.add(vo);
				}
			}
		}
		return list;
	}
}
