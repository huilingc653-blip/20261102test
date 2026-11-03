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
@Service("fbsBudgetRollQuarterService")
@Transactional
public class FbsBudgetRollQuarterService extends BaseService<FbsBudget, String> implements IWorkFlowStartedListener {
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
	/**
	 * 季滚动初始化
	 * @param fbsBudget
	 * @param model
	 * @param user
	 */
	public void initRollQuarterBudget(FbsBudgetVO fbsBudget, Model model, String fictitiousCorp, ICurrentUser user) {
		//是否是虚拟单位
		String corpId = fbsBudget.getCorpId();
		FbsCollectCorpVO fbsCollectCorpVO  = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
		if(EmptyUtils.isEmpty(fbsCollectCorpVO)){
			fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
		}
		if(!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)){
			corpId = fbsCollectCorpVO.getId();
		}
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = null;
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
			}else {
				listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			}
			int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			List<String> quartersHid = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow +=year+"年"+quarter+"季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
				quarters.add(quarterShow);
				quartersHid.add(quarter+"");
				quarter++;
				if(quarter>4){
					year++;
					quarter = 1;
				}
			}
			List<FbsBudgetVO>  fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			List<String> months = new ArrayList<>();
			//季下属月集合
			List<FbsBudgetVO> monthFbsBudgetsVOList = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			Date endDateRoll = fbsBudget.getEndDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for(int i = 0; i < quarters.size(); i++){
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if(i>0) {
						//取下季预算信息
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
						fbsBudgetVOTemp = nextFbsBudgetVO;
						beginDate = DateUtils.addMonth(beginDate, 3);
					}
					if(qRollSubperiod>i){
						//月下属周计划集合
						this.setMonthByQuarter(DateUtils.getYear(beginDate), DateUtils.getQuarterOfYear(beginDate),months);
						//获取
						List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						Date monthBeginDate = fbsBudgetVOTemp.getBeginDate();
						//是否有月计划
						String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
						List<FbsBudgetDetail> allDetails = null;
						if(CommonConstant.NO.equals(mcrl)) {
							String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
							//获取计划对应的明细
							allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
						}
						if (null != monthFbsBudgets && monthFbsBudgets.size() > 1) {
							Map<Long, Object> MonthMap = new HashMap<Long, Object>();
                            for (FbsBudget monthFbsBudget : monthFbsBudgets) {
                                FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.getBudgetNotItem(monthFbsBudget.getId());
                                MonthMap.put(monthFbsBudgetVO.getBeginDate().getTime(), monthFbsBudgetVO);
                            }
							for (int j = 0; j < 3; j++) {
								FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(monthBeginDate, BudgetConstant.TIME_TYPE_MONTH);
								if (MonthMap.containsKey(periodVO.getBeginDate().getTime())) {
									monthFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(periodVO.getBeginDate().getTime()));
								} else {
									FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, periodVO.getBeginDate(),
											periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
									//初始化明细
									if(allDetails!=null){
										for(FbsBudgetDtlAbsVO vo:monthFbsBudgetVO.getDtlList()){
											boolean flag = false;
											BigDecimal detailMoney = BigDecimal.ZERO;
											for(FbsBudgetDetail detail:allDetails) {
												//本期需要初始化明细计划
												if(monthFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
														&& monthFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
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
									monthFbsBudgetsVOList.add(monthFbsBudgetVO);
								}
								monthBeginDate = DateUtils.addMonth(monthBeginDate,1);
							}
						} else {//如果没有预算就初始化预算
							for (int j = 0; j < 3; j++) {
								FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(monthBeginDate, BudgetConstant.TIME_TYPE_MONTH);
									FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, periodVO.getBeginDate(),
											periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
									monthFbsBudgetsVOList.add(monthFbsBudgetVO);
								//初始化明细
								if(allDetails!=null){
									for(FbsBudgetDtlAbsVO vo:monthFbsBudgetVO.getDtlList()){
										boolean flag = false;
										BigDecimal detailMoney = BigDecimal.ZERO;
										for(FbsBudgetDetail detail:allDetails) {
											//本期需要初始化明细计划
											if(monthFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate())<=0
													&& monthFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate())>=0) {
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
								monthBeginDate = DateUtils.addMonth(monthBeginDate,1);
							}
						}
					}
				}
				model.addAttribute("endDateRoll", endDateRoll);
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollQuarter(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, monthFbsBudgetsVOList);
				List<FbsBudgetRollMonthWeekVO> list = new ArrayList<FbsBudgetRollMonthWeekVO>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					//本季度明细
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetRollMonthWeekVO vo = new FbsBudgetRollMonthWeekVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + 1);
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					//滚动季明细
					for(int j=0;j<fbsBudgetVOList.size();j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + (j+2)));
						}
					}
					//分解月明细
					List<List<FbsBudgetDtlAbsVO>> childMonthBudget = new ArrayList<>();
					int j=0;
					int size = 0;
					for(int k=0;k<months.size();k++){
						List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
						size = (k+1)*3;
						if(size > monthFbsBudgetsVOList.size()){
							size =  monthFbsBudgetsVOList.size();
						}
						for(;j<size;j++){
								FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = monthFbsBudgetsVOList.get(j).getDtlList().get(i);
								fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + (j + quarters.size() + 1)));
								absVOList.add(fbsBudgetDtlAbsVOWeek);
						}
						if(absVOList.size() >0) {
							childMonthBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childMonthBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					vo.setRmk(fbsBudgetDtlAbsVO.getRmk());
					list.add(vo);
				}
				model.addAttribute("rollMonthList", list);
				model.addAttribute("fbsBudgetList",fbsBudgetList);
			} else {
				model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthWeekVO>());
				model.addAttribute("fbsBudgetList",new ArrayList<FbsBudget>());
			}
			model.addAttribute("months", months);
			model.addAttribute("quarters",quarters);
			model.addAttribute("qRollSubperiod",qRollSubperiod);
			model.addAttribute("quartersHid",quartersHid);
		} else {
			model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthWeekVO>());
			model.addAttribute("fbsBudgetList",new ArrayList<FbsBudget>());
		}
	}

	public void initRollQuarterBudget(FbsBudgetVO fbsBudget, FbsBudgetInputContentVO model, String fictitiousCorp, ICurrentUser user) {
		//是否是虚拟单位
		String corpId = fbsBudget.getCorpId();
		FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
		if (EmptyUtils.isEmpty(fbsCollectCorpVO)) {
			fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
		}
		if (!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)) {
			corpId = fbsCollectCorpVO.getId();
		}
		//季计划滚动;
		int qimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		int qRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = null;
			if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
			} else {
				listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			}
			int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			List<String> quartersHid = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow += year + "年" + quarter + "季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
				quarters.add(quarterShow);
				quartersHid.add(quarter + "");
				quarter++;
				if (quarter > 4) {
					year++;
					quarter = 1;
				}
			}
			List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			List<String> months = new ArrayList<>();
			//季下属月集合
			List<FbsBudgetVO> monthFbsBudgetsVOList = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			Date endDateRoll = fbsBudget.getEndDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for (int i = 0; i < quarters.size(); i++) {
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if (i > 0) {
						//取下季预算信息
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
						fbsBudgetVOTemp = nextFbsBudgetVO;
						beginDate = DateUtils.addMonth(beginDate, 3);
					}
					if (qRollSubperiod > i) {
						//月下属周计划集合
						this.setMonthByQuarter(DateUtils.getYear(beginDate), DateUtils.getQuarterOfYear(beginDate), months);
						//获取
						List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(corpId, BudgetConstant.TIME_TYPE_MONTH, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						Date monthBeginDate = fbsBudgetVOTemp.getBeginDate();
						//是否有月计划
						String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
						List<FbsBudgetDetail> allDetails = null;
						if (CommonConstant.NO.equals(mcrl)) {
							String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
							//获取计划对应的明细
							allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
						}
						if (null != monthFbsBudgets && monthFbsBudgets.size() > 1) {
							Map<Long, Object> MonthMap = new HashMap<Long, Object>();
							for (int j = 0; j < monthFbsBudgets.size(); j++) {
								FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.getBudgetNotItem(monthFbsBudgets.get(j).getId());
								MonthMap.put(monthFbsBudgetVO.getBeginDate().getTime(), monthFbsBudgetVO);
							}
							for (int j = 0; j < 3; j++) {
								FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(monthBeginDate, BudgetConstant.TIME_TYPE_MONTH);
								if (MonthMap.containsKey(periodVO.getBeginDate().getTime())) {
									monthFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(periodVO.getBeginDate().getTime()));
								} else {
									FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, periodVO.getBeginDate(),
											periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
									//初始化明细
									if (allDetails != null) {
										for (FbsBudgetDtlAbsVO vo : monthFbsBudgetVO.getDtlList()) {
											boolean flag = false;
											BigDecimal detailMoney = BigDecimal.ZERO;
											for (FbsBudgetDetail detail : allDetails) {
												//本期需要初始化明细计划
												if (monthFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
														&& monthFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
													if (vo.getItemCode().equals(detail.getItemCode())) {
														if (detail.getNatualMoney() == null) {
															detail.setNatualMoney(detail.getBMoney() == null ? BigDecimal.ZERO : detail.getBMoney());
														}
														detailMoney = detailMoney.add(detail.getNatualMoney());
														flag = true;
													}
												}
											}
											if (flag) {
												vo.setbMoney(detailMoney);
												vo.setDetailMoney(detailMoney);
											}
										}
									}
									monthFbsBudgetsVOList.add(monthFbsBudgetVO);
								}
								monthBeginDate = DateUtils.addMonth(monthBeginDate, 1);
							}
						} else {//如果没有预算就初始化预算
							for (int j = 0; j < 3; j++) {
								FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(monthBeginDate, BudgetConstant.TIME_TYPE_MONTH);
								FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_MONTH, periodVO.getBeginDate(),
										periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
								monthFbsBudgetsVOList.add(monthFbsBudgetVO);
								//初始化明细
								if (allDetails != null) {
									for (FbsBudgetDtlAbsVO vo : monthFbsBudgetVO.getDtlList()) {
										boolean flag = false;
										BigDecimal detailMoney = BigDecimal.ZERO;
										for (FbsBudgetDetail detail : allDetails) {
											//本期需要初始化明细计划
											if (monthFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
													&& monthFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
												if (vo.getItemCode().equals(detail.getItemCode())) {
													if (detail.getNatualMoney() == null) {
														detail.setNatualMoney(detail.getBMoney() == null ? BigDecimal.ZERO : detail.getBMoney());
													}
													detailMoney = detailMoney.add(detail.getNatualMoney());
													flag = true;
												}
											}
										}
										if (flag) {
											vo.setbMoney(detailMoney);
											vo.setDetailMoney(detailMoney);
										}
									}
								}
								monthBeginDate = DateUtils.addMonth(monthBeginDate, 1);
							}
						}
					}
				}
				model.setEndDateRoll(endDateRoll);
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollQuarter(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, monthFbsBudgetsVOList);
				List<FbsBudgetRollMonthWeekVO> list = new ArrayList<FbsBudgetRollMonthWeekVO>();
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					//本季度明细
					FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO = fbsBudget.getDtlList().get(i);
					List<BigDecimal> detailMoneyList = new ArrayList<>();
					List<String> idList = new ArrayList<>();
					List<BigDecimal> bMoneyList = new ArrayList<>();
					FbsBudgetRollMonthWeekVO vo = new FbsBudgetRollMonthWeekVO();
					BeanUtils.copyNotNullProperties(fbsBudget.getDtlList().get(i), vo);
					BigDecimal detailMoney = map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + 1);
					detailMoneyList.add(detailMoney);
					idList.add(fbsBudgetDtlAbsVO.getId());
					bMoneyList.add(fbsBudgetDtlAbsVO.getbMoney());
					//滚动季明细
					for (int j = 0; j < fbsBudgetVOList.size(); j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + (j + 2)));
						}
					}
					//分解月明细
					List<List<FbsBudgetDtlAbsVO>> childMonthBudget = new ArrayList<>();
					int j = 0;
					int size = 0;
					for (int k = 0; k < months.size(); k++) {
						List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
						size = (k + 1) * 3;
						if (size > monthFbsBudgetsVOList.size()) {
							size = monthFbsBudgetsVOList.size();
						}
						for (; j < size; j++) {
							FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = monthFbsBudgetsVOList.get(j).getDtlList().get(i);
							fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + (j + quarters.size() + 1)));
							absVOList.add(fbsBudgetDtlAbsVOWeek);
						}
						if (absVOList.size() > 0) {
							childMonthBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childMonthBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					vo.setRmk(fbsBudgetDtlAbsVO.getRmk());
					list.add(vo);
				}
				model.setRollMonthList(list);
				model.setFbsBudgetList(fbsBudgetList);
			} else {
				model.setRollMonthList(new ArrayList<FbsBudgetRollMonthWeekVO>());
				model.setFbsBudgetList(new ArrayList<FbsBudget>());
			}
			model.setMonths(months);
			model.setQuarters(quarters);
			model.setQRollSubperiod(qRollSubperiod);
			model.setQuartersHid(quartersHid);
		} else {
			model.setRollMonthList(new ArrayList<FbsBudgetRollMonthWeekVO>());
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
		//季计划滚动，分解月
		int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
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
		//月总计集合
		List<FbsBudget> monthFbsBudgetsTotle = new ArrayList<>();
		//下属周计划保存 begin
		List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(entity.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, entity.getBeginDate(), entity.getEndDate());
		monthFbsBudgetsTotle.addAll(monthFbsBudgets);
		for (int i = 0; i < monthFbsBudgets.size(); i++) {
			FbsBudget t = monthFbsBudgets.get(i);
			//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额
			if (this.fbsCorpBudgetService.getCollectFlag(t.getCorpId())) {
				fbsBudgetService.subtractFbsBudget(t, isRoll);
			}
			t.setStatus(BudgetConstant.STATUE_TEMP);
			t.setCurId(input.getCurId());
			t.setExchangeRate(input.getExchangeRate());
			t.setUpdateBy(user.getLoginName());
			t.setUpdateDate(DateUtils.getDate());
		}
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
					if(qRollSubperiod-1>i){
						List<FbsBudget> nextMonthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(nextEntity.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, nextEntity.getBeginDate(), nextEntity.getEndDate());
						for (int k = 0; k < nextMonthFbsBudgets.size(); k++) {
							FbsBudget t = nextMonthFbsBudgets.get(k);
							//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额
							if (this.fbsCorpBudgetService.getCollectFlag(t.getCorpId())) {
								fbsBudgetService.subtractFbsBudget(t, isRoll);
							}
							t.setStatus(BudgetConstant.STATUE_TEMP);
							t.setCurId(input.getCurId());
							t.setExchangeRate(input.getExchangeRate());
							t.setUpdateBy(user.getLoginName());
							t.setUpdateDate(DateUtils.getDate());
						}
						monthFbsBudgetsTotle.addAll(nextMonthFbsBudgets);
					}
				}
			}
		}
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<BigDecimal> nextCheckMoneyList = new ArrayList<>();
		List<BigDecimal> monthCheckMoneyList = new ArrayList<BigDecimal>();
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
				for (int j = 0;j < fbsBudgetList.size();j++){
					//保存下月明细
					FbsBudget nextEntity = fbsBudgetList.get(j);
					BigDecimal nextCheckMoney = BigDecimal.ZERO;
					String[] nextBMoney = request.getParameterValues("bMoney"+(j+2));
					String[] nextDetailId = request.getParameterValues("detailId"+(j+2));
					if (null != nextEntity && !BudgetConstant.STATUE_CHECK.equals(nextEntity.getStatus())) {
						BigDecimal money = BigDecimal.ZERO;
						if (StringUtils.isNotEmpty(nextBMoney[i])) {
							money = new BigDecimal(Double.valueOf(nextBMoney[i]));
						}
						if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
							nextCheckMoney = money;
						}
						fbsBudgetDtlAbsService.updateByDtlId(nextDetailId[i],entity.getTimeType(),money);
					}
					nextCheckMoneyList.add(nextCheckMoney);
				}
				for(int k=1;k<=qRollSubperiod*3;k++) {
					//保存周明细monthDetailId
					BigDecimal monthCheckMoney = BigDecimal.ZERO;
					int nextIdLength = nextIds == null?0:nextIds.length;
					String[] monthDetailIds = request.getParameterValues("monthDetailId" + (k + nextIdLength + 1));
					String[] bMoneyMonths = request.getParameterValues("bMoneyMonth" + (k + nextIdLength + 1));
					if (!EmptyUtils.isEmpty(monthDetailIds)) {
						BigDecimal money = BigDecimal.ZERO;
						if (!EmptyUtils.isEmpty(bMoneyMonths[i])) {
							money = new BigDecimal(Double.parseDouble(bMoneyMonths[i]));
						}
						if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
							monthCheckMoney = money;
						}
						fbsBudgetDtlAbsService.updateByDtlId(monthDetailIds[i], BudgetConstant.TIME_TYPE_MONTH, money);
					}
					monthCheckMoneyList.add(monthCheckMoney);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		//月计划汇总
		for (int i = 0; i < monthFbsBudgetsTotle.size(); i++) {
			FbsBudget t = monthFbsBudgetsTotle.get(i);
			if (!EmptyUtils.isEmptyList(monthCheckMoneyList) && monthCheckMoneyList.size() >= i) {
				t.setCheckMoney(monthCheckMoneyList.get(i) == null ? BigDecimal.ZERO : monthCheckMoneyList.get(i));
			}
			fbsBudgetService.saveOrUpdate(t);
			//周计划汇总
			saveFbsBudgetCollectUnderCorp(t, user);
		}
		//end
		for (int i = 0;i < fbsBudgetList.size();i++){
			FbsBudget nextEntity = fbsBudgetList.get(i);
			if (null != nextEntity) {
				if(nextCheckMoneyList.size()>i) {
					nextEntity.setCheckMoney(nextCheckMoneyList.get(i));
				}else{
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
							//季计划模式控制
							if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
								//qRollSubperiod-季滚动模式 滚动分解子周期
								int qRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
								//单位分解月计划也更新为零计划
								if(qRollSubperiod >0){
									List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(budget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, budget.getBeginDate(), budget.getEndDate(), null);
									for(FbsBudget monthBudget :budgets){
										fbsBudgetDtlAbsService.clearInputMoney(monthBudget.getId(), monthBudget.getTimeType());
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
		FbsBudget budget = this.get(input.getId(), true);
		//清除汇总单位计划
		fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
		fbsBudgetService.collectFbsBudget(input, childIds, user);
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		//如果季滚动分解月，则也汇总季所属月计划
		if(qRollSubperiod>0) {
			// 当前季对应的月
			List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
					(budget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, budget.getBeginDate(), budget.getEndDate());
			for (FbsBudget monthBudget : monthFbsBudgets) {
				List<String> childWeekIds = new ArrayList<>();
				for (String childId : childIds) {
					// 汇总季对应月
					FbsBudget childBudget = this.get(childId, true);
					FbsBudget monthChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, monthBudget.getBeginDate(), monthBudget.getEndDate());
					if (!EmptyUtils.isEmpty(monthChildBudget)) {
						childWeekIds.add(monthChildBudget.getId());
					}
				}
				FbsBudgetInputVO monthInput = new FbsBudgetInputVO();
				monthInput.setId(monthBudget.getId());
				monthInput.setTimeType(monthBudget.getTimeType());
				//清除汇总单位计划
				fbsBudgetDtlAbsService.clearInputMoney(monthBudget.getId(), monthBudget.getTimeType());
				fbsBudgetService.collectFbsBudgetNotUpdateStatus(monthInput, childWeekIds, user);
			}
		}
		int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		//保存季度
		List<String> quarters = new ArrayList<>();
		for (int i = 1; i <= qimodeRoll; i++) {
			String quarterShow = "";
			quarterShow +=year+"年"+quarter+"季度</br>";
//			quarterShow +=this.getMonthByQuarter(year,quarter);
			quarters.add(quarterShow);
			quarter++;
			if(quarter>4){
				year++;
				quarter = 1;
			}
		}
		Date beginDate = budget.getBeginDate();
		for (int k= 1; k < quarters.size();k++) {
			//取下季预算信息
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			List<FbsBudgetInputPreVO> nextChildFbsBudget = fbsBudgetService.getChildCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
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
				FbsBudgetVO vo = fbsBudgetService.getBudgetNotItem(nextChildFbsBudget.get(i).getId());
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
			beginDate = DateUtils.addMonth(beginDate,3);
			if(k<qRollSubperiod){
				// 当前季对应的月
				List<FbsBudget> nextMonthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
						(nextFbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
				for (FbsBudget monthBudget : nextMonthFbsBudgets) {
					List<String> childWeekIds = new ArrayList<>();
					for (String childId : childIds) {
						// 汇总季对应月
						FbsBudget childBudget = this.get(childId, true);
						FbsBudget monthChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, monthBudget.getBeginDate(), monthBudget.getEndDate());
						if (!EmptyUtils.isEmpty(monthChildBudget)) {
							childWeekIds.add(monthChildBudget.getId());
						}
					}
					FbsBudgetInputVO monthInput = new FbsBudgetInputVO();
					monthInput.setId(monthBudget.getId());
					monthInput.setTimeType(monthBudget.getTimeType());
					//清除汇总单位计划
					fbsBudgetDtlAbsService.clearInputMoney(monthBudget.getId(), monthBudget.getTimeType());
					fbsBudgetService.collectFbsBudgetNotUpdateStatus(monthInput, childWeekIds, user);
				}
			}
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
		//清除汇总单位计划
		fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
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
			FbsItem fbsItem = fbsItemMap.get(dtl.getItemCode());
			FbsItemVO fbsItemVO = new FbsItemVO();
			BeanUtils.copyNotNullProperties(fbsItem,fbsItemVO);
			dtl.setItem(fbsItemVO);
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
		//删除本季
		FbsBudget fbsBudget = fbsBudgetService.get(id, true);
		fbsBudgetService.deleteFbsBudget(id, timeType, user);
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
		int year = DateUtils.getYear(fbsBudget.getBeginDate());
		//保存季度
		List<String> quarters = new ArrayList<>();
		for (int i = 1; i <= qimodeRoll; i++) {
			String quarterShow = "";
			quarterShow +=year+"年"+quarter+"季度</br>";
			quarterShow +=this.getMonthByQuarter(year,quarter);
			quarters.add(quarterShow);
			quarter++;
			if(quarter>4){
				year++;
				quarter = 1;
			}
		}
		//下属月计划信息
		List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, fbsBudget.getBeginDate(), fbsBudget.getEndDate());
		if (!EmptyUtils.isEmptyList(monthFbsBudgets)) {
			for (int i = 0; i < monthFbsBudgets.size(); i++) {
				fbsBudgetService.getChildCorpBudget(monthFbsBudgets.get(i).getCorpId(), monthFbsBudgets.get(i).getTimeType(),
						monthFbsBudgets.get(i).getBeginDate(), monthFbsBudgets.get(i).getEndDate());
				if (BudgetConstant.STATUE_TEMP.equals(monthFbsBudgets.get(i).getStatus())
						|| BudgetConstant.STATUE_BACK.equals(monthFbsBudgets.get(i).getStatus())) {
					fbsBudgetService.deleteFbsBudget(monthFbsBudgets.get(i).getId(), monthFbsBudgets.get(i).getTimeType(), user);
				}
			}
		}
		Date begindate = fbsBudget.getBeginDate();
		for(int i = 1;i< quarters.size();i++){
			//取下月预算信息 删除下个月 只能删除暂存状态的下个月预算
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(begindate, fbsBudget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			if (null != nextFbsBudget && BudgetConstant.STATUE_TEMP.equals(nextFbsBudget.getStatus())) {
				fbsBudgetService.deleteFbsBudget(nextFbsBudget.getId(), timeType, user);
				begindate =  DateUtils.addMonth(begindate,3);
				//月滚动下属月计划信息
				List<FbsBudget> nextmonthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
						(nextFbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
				if (!EmptyUtils.isEmptyList(nextmonthFbsBudgets)) {
					for (int j = 0; j < nextmonthFbsBudgets.size(); j++) {
						fbsBudgetService.getChildCorpBudget(nextmonthFbsBudgets.get(j).getCorpId(), nextmonthFbsBudgets.get(j).getTimeType(),
								nextmonthFbsBudgets.get(j).getBeginDate(), nextmonthFbsBudgets.get(j).getEndDate());
						if (BudgetConstant.STATUE_TEMP.equals(nextmonthFbsBudgets.get(j).getStatus())
								|| BudgetConstant.STATUE_BACK.equals(nextmonthFbsBudgets.get(j).getStatus())) {
							fbsBudgetService.deleteFbsBudget(nextmonthFbsBudgets.get(j).getId(), nextmonthFbsBudgets.get(j).getTimeType(), user);
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
			//下属月计划保存 begin
			List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
					(entity.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, entity.getBeginDate(), entity.getEndDate());
			for (int i = 0; i < monthFbsBudgets.size(); i++) {
				FbsBudget t = monthFbsBudgets.get(i);
				t.setStatus(entity.getStatus());
				t.setUpdateBy(user.getLoginName());
				t.setUpdateDate(DateUtils.getDate());
				fbsBudgetService.saveOrUpdate(t);
				//生成日志
				fbsMessageService.saveMessage(t.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_APP_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
				//月计划汇总
				saveFbsBudgetCollectUnderCorp(t, user);
			}
			//end
		}
	}
	/**
	 * 导出数据查询
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<FbsBudgetRollMonthWeekVO> listRollQuarterBudget(String id, ICurrentUser user) throws Exception {
		List<FbsBudgetRollMonthWeekVO> list = new ArrayList<>();
		FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(id);
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		if (null != fbsBudget.getBeginDate()) {
			//获取单位分配的科目
			List<FbsCorpItem> listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
			int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			List<String> quartersHid = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow +=year+"年"+quarter+"季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
				quarters.add(quarterShow);
				quartersHid.add(quarter+"");
				quarter++;
				if(quarter>4){
					year++;
					quarter = 1;
				}
			}
			List<FbsBudgetVO>  fbsBudgetVOList = new ArrayList<>();
			List<FbsBudget> fbsBudgetList = new ArrayList<>();
			List<String> months = new ArrayList<>();
			//季下属月集合
			List<FbsBudgetVO> monthFbsBudgetsVOList = new ArrayList<>();
			Date beginDate = fbsBudget.getBeginDate();
			if (!EmptyUtils.isEmptyList(listCorpItem)) {
				for(int i = 0; i < quarters.size(); i++){
					FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
					if(i>0) {
						//取下季预算信息
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
						fbsBudgetVOTemp = nextFbsBudgetVO;
						beginDate = DateUtils.addMonth(beginDate, 3);
					}
					if(qRollSubperiod>i){
						//月下属周计划集合
						this.setMonthByQuarter(DateUtils.getYear(beginDate), DateUtils.getQuarterOfYear(beginDate),months);
						//获取
						List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(fbsBudgetVOTemp.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
						Date monthBeginDate = fbsBudgetVOTemp.getBeginDate();
						if (null != monthFbsBudgets && monthFbsBudgets.size() > 1) {
							Map<Long, Object> MonthMap = new HashMap<Long, Object>();
							for (int j = 0; j < monthFbsBudgets.size(); j++) {
								FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.getBudgetNotItem(monthFbsBudgets.get(j).getId());
								MonthMap.put(monthFbsBudgetVO.getBeginDate().getTime(), monthFbsBudgetVO);
							}
							for (int j = 0; j < 3; j++) {
								FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(monthBeginDate, BudgetConstant.TIME_TYPE_MONTH);
								if (MonthMap.containsKey(vo.getBeginDate().getTime())) {
									monthFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(vo.getBeginDate().getTime()));
								} else {
									FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_MONTH, vo.getBeginDate(),
											vo.getEndDate(), BudgetConstant.STATUE_TEMP, user);
									monthFbsBudgetsVOList.add(monthFbsBudgetVO);
								}
								monthBeginDate = DateUtils.addMonth(monthBeginDate,1);
							}
						}
					}
				}
				Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollQuarter(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, monthFbsBudgetsVOList);
				for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
					//本季度明细
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
					//滚动季明细
					for(int j=0;j<fbsBudgetVOList.size();j++) {
						FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
						if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
							FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
							idList.add(nextFbsBudgetDtlAbsVO.getId());
							bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
							detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(nextFbsBudgetVO.getBeginDate())));
						}
					}
					//分解月明细
					List<List<FbsBudgetDtlAbsVO>> childMonthBudget = new ArrayList<>();
					int j=0;
					int size = 0;
					for(int k=0;k<months.size();k++){
						List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
						size = (k+1)*3;
						if(size > monthFbsBudgetsVOList.size()){
							size =  monthFbsBudgetsVOList.size();
						}
						for(;j<size;j++){
							FbsBudgetDtlAbsVO fbsBudgetDtlAbsVOWeek = monthFbsBudgetsVOList.get(j).getDtlList().get(i);
							fbsBudgetDtlAbsVOWeek.setDetailMoney(map.get(vo.getId() + "_" + vo.getItemCode() + "_" + (j + months.size() + 1)));
							absVOList.add(fbsBudgetDtlAbsVOWeek);
						}
						if(absVOList.size() >0) {
							childMonthBudget.add(absVOList);
						}
					}
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childMonthBudget);
					vo.setDetailMoney(detailMoneyList);
					vo.setId(idList);
					vo.setbMoney(bMoneyList);
					list.add(vo);
				}
			}
		}
		return list;
	}
	/**
	 * 根据季度年份得到显示时间
	 * @param year
	 * @param quarter
	 * @return
	 */
	public String getMonthByQuarter(int year,int quarter){
		String result = "";
		if(quarter == 1){
			result = year+".01-"+year+".03";
		}else if(quarter == 2){
			result = year+".04-"+year+".06";
		}else if(quarter == 3){
			result = year+".07-"+year+".09";
		}else if(quarter == 4){
			result = year+".10-"+year+".12";
		}
		return result;
	}
	/**
	 * 设置季度的分解月
	 * @param year
	 * @param quarter
	 * @param monthList
	 */
	public void setMonthByQuarter(int year,int quarter,List<String> monthList){
		String result = "";
		if(quarter == 1){
			result = year+"年"+1+"月";
			monthList.add(result);
			result = year+"年"+2+"月";
			monthList.add(result);
			result = year+"年"+3+"月";
			monthList.add(result);
			result = year+"年"+1+"季度";
			monthList.add(result);
		}else if(quarter == 2){
			result = year+"年"+4+"月";
			monthList.add(result);
			result = year+"年"+5+"月";
			monthList.add(result);
			result = year+"年"+6+"月";
			monthList.add(result);
			result = year+"年"+2+"季度";
			monthList.add(result);
		}else if(quarter == 3){
			result = year+"年"+7+"月";
			monthList.add(result);
			result = year+"年"+8+"月";
			monthList.add(result);
			result = year+"年"+9+"月";
			monthList.add(result);
			result = year+"年"+3+"季度";
			monthList.add(result);
		}else if(quarter == 4){
			result = year+"年"+10+"月";
			monthList.add(result);
			result = year+"年"+11+"月";
			monthList.add(result);
			result = year+"年"+12+"月";
			monthList.add(result);
			result = year+"年"+4+"季度";
			monthList.add(result);
		}
	}
}
