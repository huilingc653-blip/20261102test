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
 * 周滚动服务类
 */
@Service("fbsBudgetRollWeekService")
@Transactional
public class FbsBudgetRollWeekService extends BaseService<FbsBudget, String> implements IWorkFlowStartedListener {
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
     * 周滚动初始化
     *
     * @param fbsBudget
     * @param model
     * @param user
     */
    public void initRollWeekBudget(FbsBudgetVO fbsBudget, Model model, String fictitiousCorp, ICurrentUser user) {
        //是否是虚拟单位
        String corpId = fbsBudget.getCorpId();
        FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
        if (EmptyUtils.isEmpty(fbsCollectCorpVO)) {
            fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
        }
        if (!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)) {
            corpId = fbsCollectCorpVO.getId();
        }
        //周计划滚动;
        int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        if (null != fbsBudget.getBeginDate()) {
            //获取单位分配的科目
            List<FbsCorpItem> listCorpItem = null;
            if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
                listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
            } else {
                listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
            }
            Date weekBeginDate = fbsBudget.getBeginDate();
            Date endDateRoll = fbsBudget.getEndDate();
            List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
            List<String> weeksHid = new ArrayList<>();
            FbsBudgetPeriodVO curVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate, fbsBudget.getTimeType());
            for (int i = 1; i <= wimodeRoll; i++) {
                FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
                int week = fbsBudgetPeriodService.getWeekNoByTime(curVO.getBeginDate());
                int month = DateUtils.getMonth(curVO.getBeginDate());
                //保存周
                String weekShow = "";
                weekShow += month + "月第" + week + "周";
                fbsBudgetPaternityVO.setPaternal(weekShow);
                weeks.add(fbsBudgetPaternityVO);
                weeksHid.add(week + "");
                curVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(curVO.getBeginDate(), fbsBudget.getTimeType());
            }
            List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
            List<FbsBudget> fbsBudgetList = new ArrayList<>();
            List<String> days = new ArrayList<>();
            //周下属天集合
            List<FbsBudgetVO> dayFbsBudgetsTotleVOList = new ArrayList<>();
            curVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate, fbsBudget.getTimeType());
            if (!EmptyUtils.isEmptyList(listCorpItem)) {
                for (int i = 0; i < weeks.size(); i++) {
                    FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
                    if (i > 0) {
                        //取下周预算信息
                        FbsBudgetVO nextFbsBudgetVO = null;
                        FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(curVO.getBeginDate(), fbsBudget.getTimeType());
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
                        curVO = nextFbsBudgetPeriodVO;
                    }
                    if (wRollSubperiod > i) {
                        List<String> weeksTemp = new ArrayList<>();
                        //周下属天计划集合
                        this.setDayByWeek(fbsBudgetVOTemp, days, weeksTemp);
                        //月下属周计划集合
                        List<FbsBudgetVO> dayFbsBudgetsVOList = new ArrayList<>();
                        //一周有多少天
                        int count = DateUtils.dayDiffs(fbsBudgetVOTemp.getEndDate(), fbsBudgetVOTemp.getBeginDate()) + 1;
                        //获取
                        List<FbsBudget> dayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                                (corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
                        Date dayBeginDate = fbsBudgetVOTemp.getBeginDate();
                        //是否有天计划
                        String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
                        List<FbsBudgetDetail> allDetails = null;
                        if (CommonConstant.NO.equals(dcrl)) {
                            String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
                            //获取计划对应的明细
                            allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
                        }
                        if (null != dayFbsBudgets && dayFbsBudgets.size() > 1) {
                            Map<Long, Object> MonthMap = new HashMap<Long, Object>();
                            for (int j = 0; j < dayFbsBudgets.size(); j++) {
                                FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.getBudgetNotItem(dayFbsBudgets.get(j).getId());
                                MonthMap.put(dayFbsBudgetVO.getBeginDate().getTime(), dayFbsBudgetVO);
                            }
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                if (MonthMap.containsKey(periodVO.getBeginDate().getTime())) {
                                    dayFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(periodVO.getBeginDate().getTime()));
                                } else {
                                    FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_DAY, periodVO.getBeginDate(),
                                            periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                    //初始化明细
                                    if (allDetails != null) {
                                        for (FbsBudgetDtlAbsVO vo : dayFbsBudgetVO.getDtlList()) {
                                            boolean flag = false;
                                            BigDecimal detailMoney = BigDecimal.ZERO;
                                            for (FbsBudgetDetail detail : allDetails) {
                                                //本期需要初始化明细计划
                                                if (dayFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
                                                        && dayFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
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
                                    dayFbsBudgetsVOList.add(dayFbsBudgetVO);
                                }
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        } else {//如果没有预算就初始化预算
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_DAY, periodVO.getBeginDate(),
                                        periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                //初始化明细
                                if (allDetails != null) {
                                    for (FbsBudgetDtlAbsVO vo : dayFbsBudgetVO.getDtlList()) {
                                        boolean flag = false;
                                        BigDecimal detailMoney = BigDecimal.ZERO;
                                        for (FbsBudgetDetail detail : allDetails) {
                                            //本期需要初始化明细计划
                                            if (dayFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
                                                    && dayFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
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
                                dayFbsBudgetsVOList.add(dayFbsBudgetVO);
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        }
                        weeks.get(i).setChildList(weeksTemp);
                        weeks.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
                        weeks.get(i).setChildFbsBudgetVOList(dayFbsBudgetsVOList);
                        dayFbsBudgetsTotleVOList.addAll(dayFbsBudgetsVOList);
                    }
                }
                model.addAttribute("endDateRoll", endDateRoll);
                Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollWeek(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, dayFbsBudgetsTotleVOList);
                List<FbsBudgetRollMonthWeekVO> list = new ArrayList<>();
                for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
                    //本周明细
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
                    //滚动周明细
                    for (int j = 0; j < fbsBudgetVOList.size(); j++) {
                        FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
                        if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
                            FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
                            idList.add(nextFbsBudgetDtlAbsVO.getId());
                            bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
                            detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + (j + 2)));
                        }
                    }
                    //分解天明细
                    List<List<FbsBudgetDtlAbsVO>> childDayBudget = new ArrayList<>();
                    int j = 0;
                    int size = 0;
                    for (int k = 0; k < weeks.size(); k++) {
                        if (EmptyUtils.isNotEmpty(weeks.get(k).getChildList())) {
                            size += weeks.get(k).getChildList().size();
                        }
                        List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
                        for (; j < size; j++) {
                            FbsBudgetDtlAbsVO fbsBudgetDtlAbsVODay = dayFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
                            fbsBudgetDtlAbsVODay.setDetailMoney(map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + (j + weeks.size() + 1)));
                            absVOList.add(fbsBudgetDtlAbsVODay);
                        }
                        if (absVOList.size() > 0) {
                            childDayBudget.add(absVOList);
                        }
                    }
					vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
					vo.setChildWeekBudget(childDayBudget);
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
			model.addAttribute("days", days);
			model.addAttribute("weeks",weeks);
			model.addAttribute("wRollSubperiod",wRollSubperiod);
			model.addAttribute("weeksHid",weeksHid);
		} else {
			model.addAttribute("rollMonthList", new ArrayList<FbsBudgetRollMonthWeekVO>());
			model.addAttribute("fbsBudgetList",new ArrayList<FbsBudget>());
		}
	}
    public void initRollWeekBudget(FbsBudgetVO fbsBudget, FbsBudgetInputContentVO model, String fictitiousCorp, ICurrentUser user) {
        //是否是虚拟单位
        String corpId = fbsBudget.getCorpId();
        FbsCollectCorpVO fbsCollectCorpVO = fbsBudgetService.listCorpByCorpId(fbsBudget.getCorpId());
        if (EmptyUtils.isEmpty(fbsCollectCorpVO)) {
            fbsCollectCorpVO = fbsBudgetService.listCorpById(fbsBudget.getCorpId());
        }
        if (!EmptyUtils.isEmpty(fbsCollectCorpVO) && "1".equals(fictitiousCorp)) {
            corpId = fbsCollectCorpVO.getId();
        }
        //周计划滚动;
        int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        if (null != fbsBudget.getBeginDate()) {
            //获取单位分配的科目
            List<FbsCorpItem> listCorpItem = null;
            if (!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
                listCorpItem = fbsCorpItemService.getItemCode(fbsCollectCorpVO.getCorpId());
            } else {
                listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
            }
            Date weekBeginDate = fbsBudget.getBeginDate();
            Date endDateRoll = fbsBudget.getEndDate();
            List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
            List<String> weeksHid = new ArrayList<>();
            FbsBudgetPeriodVO curVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate, fbsBudget.getTimeType());
            for (int i = 1; i <= wimodeRoll; i++) {
                FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
                int week = fbsBudgetPeriodService.getWeekNoByTime(curVO.getBeginDate());
                int month = DateUtils.getMonth(curVO.getBeginDate());
                //保存周
                String weekShow = "";
                weekShow += month + "月第" + week + "周";
                fbsBudgetPaternityVO.setPaternal(weekShow);
                weeks.add(fbsBudgetPaternityVO);
                weeksHid.add(week + "");
                curVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(curVO.getBeginDate(), fbsBudget.getTimeType());
            }
            List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
            List<FbsBudget> fbsBudgetList = new ArrayList<>();
            List<String> days = new ArrayList<>();
            //周下属天集合
            List<FbsBudgetVO> dayFbsBudgetsTotleVOList = new ArrayList<>();
            curVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate, fbsBudget.getTimeType());
            if (!EmptyUtils.isEmptyList(listCorpItem)) {
                for (int i = 0; i < weeks.size(); i++) {
                    FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
                    if (i > 0) {
                        //取下周预算信息
                        FbsBudgetVO nextFbsBudgetVO = null;
                        FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(curVO.getBeginDate(), fbsBudget.getTimeType());
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
                        curVO = nextFbsBudgetPeriodVO;
                    }
                    if (wRollSubperiod > i) {
                        List<String> weeksTemp = new ArrayList<>();
                        //周下属天计划集合
                        this.setDayByWeek(fbsBudgetVOTemp, days, weeksTemp);
                        //月下属周计划集合
                        List<FbsBudgetVO> dayFbsBudgetsVOList = new ArrayList<>();
                        //一周有多少天
                        int count = DateUtils.dayDiffs(fbsBudgetVOTemp.getEndDate(), fbsBudgetVOTemp.getBeginDate()) + 1;
                        //获取
                        List<FbsBudget> dayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                                (corpId, BudgetConstant.TIME_TYPE_DAY, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
                        Date dayBeginDate = fbsBudgetVOTemp.getBeginDate();
                        //是否有天计划
                        String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
                        List<FbsBudgetDetail> allDetails = null;
                        if (CommonConstant.NO.equals(dcrl)) {
                            String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
                            //获取计划对应的明细
                            allDetails = fbsBudgetDetailService.getDetailListByBudgetId(fbsBudgetVOTemp.getId(), entity);
                        }
                        if (null != dayFbsBudgets && dayFbsBudgets.size() > 1) {
                            Map<Long, Object> MonthMap = new HashMap<Long, Object>();
                            for (int j = 0; j < dayFbsBudgets.size(); j++) {
                                FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.getBudgetNotItem(dayFbsBudgets.get(j).getId());
                                MonthMap.put(dayFbsBudgetVO.getBeginDate().getTime(), dayFbsBudgetVO);
                            }
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                if (MonthMap.containsKey(periodVO.getBeginDate().getTime())) {
                                    dayFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(periodVO.getBeginDate().getTime()));
                                } else {
                                    FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_DAY, periodVO.getBeginDate(),
                                            periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                    //初始化明细
                                    if (allDetails != null) {
                                        for (FbsBudgetDtlAbsVO vo : dayFbsBudgetVO.getDtlList()) {
                                            boolean flag = false;
                                            BigDecimal detailMoney = BigDecimal.ZERO;
                                            for (FbsBudgetDetail detail : allDetails) {
                                                //本期需要初始化明细计划
                                                if (dayFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
                                                        && dayFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
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
                                    dayFbsBudgetsVOList.add(dayFbsBudgetVO);
                                }
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        } else {//如果没有预算就初始化预算
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO periodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.initBudget(corpId, BudgetConstant.TIME_TYPE_DAY, periodVO.getBeginDate(),
                                        periodVO.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                //初始化明细
                                if (allDetails != null) {
                                    for (FbsBudgetDtlAbsVO vo : dayFbsBudgetVO.getDtlList()) {
                                        boolean flag = false;
                                        BigDecimal detailMoney = BigDecimal.ZERO;
                                        for (FbsBudgetDetail detail : allDetails) {
                                            //本期需要初始化明细计划
                                            if (dayFbsBudgetVO.getBeginDate().compareTo(detail.getVestingDate()) <= 0
                                                    && dayFbsBudgetVO.getEndDate().compareTo(detail.getVestingDate()) >= 0) {
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
                                dayFbsBudgetsVOList.add(dayFbsBudgetVO);
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        }
                        weeks.get(i).setChildList(weeksTemp);
                        weeks.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
                        weeks.get(i).setChildFbsBudgetVOList(dayFbsBudgetsVOList);
                        dayFbsBudgetsTotleVOList.addAll(dayFbsBudgetsVOList);
                    }
                }
                model.setEndDateRoll(endDateRoll);
                Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollWeek(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, dayFbsBudgetsTotleVOList);
                List<FbsBudgetRollMonthWeekVO> list = new ArrayList<>();
                for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
                    //本周明细
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
                    //滚动周明细
                    for (int j = 0; j < fbsBudgetVOList.size(); j++) {
                        FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
                        if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
                            FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
                            idList.add(nextFbsBudgetDtlAbsVO.getId());
                            bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
                            detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + (j + 2)));
                        }
                    }
                    //分解天明细
                    List<List<FbsBudgetDtlAbsVO>> childDayBudget = new ArrayList<>();
                    int j = 0;
                    int size = 0;
                    for (int k = 0; k < weeks.size(); k++) {
                        if (EmptyUtils.isNotEmpty(weeks.get(k).getChildList())) {
                            size += weeks.get(k).getChildList().size();
                        }
                        List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
                        for (; j < size; j++) {
                            FbsBudgetDtlAbsVO fbsBudgetDtlAbsVODay = dayFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
                            fbsBudgetDtlAbsVODay.setDetailMoney(map.get(fbsBudgetDtlAbsVO.getId() + "_" + fbsBudgetDtlAbsVO.getItemCode() + "_" + (j + weeks.size() + 1)));
                            absVOList.add(fbsBudgetDtlAbsVODay);
                        }
                        if (absVOList.size() > 0) {
                            childDayBudget.add(absVOList);
                        }
                    }
                    vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
                    vo.setChildWeekBudget(childDayBudget);
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
            model.setDays(days);
            model.setWeeks(weeks);
            model.setWRollSubperiod(wRollSubperiod);
            model.setWeeksHid(weeksHid);
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
		//周计划滚动，分解天
		int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
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
		//天总计集合
		List<FbsBudget> dayFbsBudgetsTotle = new ArrayList<>();
		//下属周天计划保存 begin
		List<FbsBudget> dayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
				(entity.getCorpId(), BudgetConstant.TIME_TYPE_DAY, entity.getBeginDate(), entity.getEndDate());
		dayFbsBudgetsTotle.addAll(dayFbsBudgets);
		for (int i = 0; i < dayFbsBudgets.size(); i++) {
			FbsBudget t = dayFbsBudgets.get(i);
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
					if(wRollSubperiod-1>i){
						List<FbsBudget> nextDayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
								(nextEntity.getCorpId(), BudgetConstant.TIME_TYPE_DAY, nextEntity.getBeginDate(), nextEntity.getEndDate());
						for (int k = 0; k < nextDayFbsBudgets.size(); k++) {
							FbsBudget t = nextDayFbsBudgets.get(k);
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
						dayFbsBudgetsTotle.addAll(nextDayFbsBudgets);
					}
				}
			}
		}
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<BigDecimal> nextCheckMoneyList = new ArrayList<>();
		List<BigDecimal> dayCheckMoneyList = new ArrayList<BigDecimal>();
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
					//保存下周明细
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
				for(int k=1;k<=dayFbsBudgetsTotle.size();k++) {
					//保存天明细dayDetailId
					BigDecimal dayCheckMoney = BigDecimal.ZERO;
					int nextIdLength = nextIds == null?0:nextIds.length;
					String[] dayDetailIds = request.getParameterValues("dayDetailId" + (k + nextIdLength + 1));
					String[] bMoneydays = request.getParameterValues("bMoneyday" + (k + nextIdLength + 1));
					if (!EmptyUtils.isEmpty(dayDetailIds)) {
						BigDecimal money = BigDecimal.ZERO;
						if (!EmptyUtils.isEmpty(bMoneydays[i])) {
							money = new BigDecimal(Double.parseDouble(bMoneydays[i]));
						}
						if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
							dayCheckMoney = money;
						}
						fbsBudgetDtlAbsService.updateByDtlId(dayDetailIds[i], BudgetConstant.TIME_TYPE_DAY, money);
					}
					dayCheckMoneyList.add(dayCheckMoney);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setCheckMoney(checkMoney);
		fbsBudgetService.saveOrUpdate(entity);
		//资金计划汇总提交，下级单位计划处理
		saveFbsBudgetCollectUnderCorp(entity, user);
		//天计划汇总
		for (int i = 0; i < dayFbsBudgetsTotle.size(); i++) {
			FbsBudget t = dayFbsBudgetsTotle.get(i);
			if (!EmptyUtils.isEmptyList(dayCheckMoneyList) && dayCheckMoneyList.size() >= i) {
				t.setCheckMoney(dayCheckMoneyList.get(i) == null ? BigDecimal.ZERO : dayCheckMoneyList.get(i));
			}
			fbsBudgetService.saveOrUpdate(t);
			//天计划汇总
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
							//周计划模式控制
							if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
								//wRollSubperiod-周滚动模式 滚动分解子周期
								int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
								//单位分解天计划也更新为零计划
								if(wRollSubperiod >0){
									List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(budget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, budget.getBeginDate(), budget.getEndDate(), null);
									for(FbsBudget dayBudget :budgets){
										fbsBudgetDtlAbsService.clearInputMoney(dayBudget.getId(), dayBudget.getTimeType());
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
	 * @param input
	 * @param childIds
	 */
	public void collectFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
		FbsBudget budget = this.get(input.getId(), true);
		//清除汇总单位计划
		fbsBudgetDtlAbsService.clearInputMoney(budget.getId(), budget.getTimeType());
		fbsBudgetService.collectFbsBudget(input, childIds, user);
		//周计划滚动;
		int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
		//周计划滚动，分解天
		int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
		//如果周滚动分解天，则也汇总季所属天计划
		if(wRollSubperiod>0) {
			// 当前季对应的月
			List<FbsBudget> monthFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
					(budget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, budget.getBeginDate(), budget.getEndDate());
			for (FbsBudget monthBudget : monthFbsBudgets) {
				List<String> childWeekIds = new ArrayList<>();
				for (String childId : childIds) {
					// 汇总季对应月
					FbsBudget childBudget = this.get(childId, true);
					FbsBudget monthChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, monthBudget.getBeginDate(), monthBudget.getEndDate());
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
		Date weekBeginDate = budget.getBeginDate();
		List<String> weeks = new ArrayList<>();
		List<String> weeksHid = new ArrayList<>();
		for (int i = 1; i <= wimodeRoll; i++) {
			int week = fbsBudgetPeriodService.getWeekNoByTime(weekBeginDate);
			int month = DateUtils.getMonth(weekBeginDate);
			//保存周
			String weekShow = "";
			weekShow +=month+"月第"+week+"周";
			weeks.add(weekShow);
			weeksHid.add(week+"");
			weekBeginDate = DateUtils.addDay(weekBeginDate,7);
		}
		Date beginDate = budget.getBeginDate();
		for (int k= 1; k < weeks.size();k++) {
			//取下周预算信息
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
			beginDate = nextFbsBudgetPeriodVO.getBeginDate();
//			beginDate = DateUtils.addDay(beginDate,7);
            if (k < wRollSubperiod) {
                // 当前周对应的天
                List<FbsBudget> nextDayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                        (nextFbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
                for (FbsBudget monthBudget : nextDayFbsBudgets) {
                    List<String> childWeekIds = new ArrayList<>();
                    for (String childId : childIds) {
                        // 汇总季对应月
                        FbsBudget childBudget = this.get(childId, true);
                        FbsBudget dayChildBudget = fbsBudgetRollMonthWeekDao.getCorpBudget(childBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, monthBudget.getBeginDate(), monthBudget.getEndDate());
                        if (!EmptyUtils.isEmpty(dayChildBudget)) {
                            childWeekIds.add(dayChildBudget.getId());
                        }
                    }
                    FbsBudgetInputVO dayInput = new FbsBudgetInputVO();
                    dayInput.setId(monthBudget.getId());
                    dayInput.setTimeType(monthBudget.getTimeType());
                    //清除汇总单位计划
                    fbsBudgetDtlAbsService.clearInputMoney(monthBudget.getId(), monthBudget.getTimeType());
                    fbsBudgetService.collectFbsBudgetNotUpdateStatus(dayInput, childWeekIds, user);
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
        //删除本周
        FbsBudget fbsBudget = fbsBudgetService.get(id, true);
        fbsBudgetService.deleteFbsBudget(id, timeType, user);
        //周计划滚动;
        int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        Date weekBeginDate = fbsBudget.getBeginDate();
        List<String> weeks = new ArrayList<>();
        List<String> weeksHid = new ArrayList<>();
        for (int i = 1; i <= wimodeRoll; i++) {
            int week = fbsBudgetPeriodService.getWeekNoByTime(weekBeginDate);
            int month = DateUtils.getMonth(weekBeginDate);
            //保存周
            String weekShow = "";
            weekShow += month + "月第" + week + "周";
            weeks.add(weekShow);
            weeksHid.add(week + "");
            weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
        }
        //本周下属天计划信息
        List<FbsBudget> dayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                (fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, fbsBudget.getBeginDate(), fbsBudget.getEndDate());
        if (!EmptyUtils.isEmptyList(dayFbsBudgets)) {
            for (int i = 0; i < dayFbsBudgets.size(); i++) {
                fbsBudgetService.getChildCorpBudget(dayFbsBudgets.get(i).getCorpId(), dayFbsBudgets.get(i).getTimeType(),
                        dayFbsBudgets.get(i).getBeginDate(), dayFbsBudgets.get(i).getEndDate());
                if (BudgetConstant.STATUE_TEMP.equals(dayFbsBudgets.get(i).getStatus())
                        || BudgetConstant.STATUE_BACK.equals(dayFbsBudgets.get(i).getStatus())) {
                    fbsBudgetService.deleteFbsBudget(dayFbsBudgets.get(i).getId(), dayFbsBudgets.get(i).getTimeType(), user);
                }
            }
        }
        Date begindate = fbsBudget.getBeginDate();
        for (int i = 1; i < weeks.size(); i++) {
            //取下周预算信息 删除下周 只能删除暂存状态的下周预算
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(begindate, fbsBudget.getTimeType());
            FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
            if (null != nextFbsBudget && BudgetConstant.STATUE_TEMP.equals(nextFbsBudget.getStatus())) {
                fbsBudgetService.deleteFbsBudget(nextFbsBudget.getId(), timeType, user);
                //滚动下属天计划信息
                List<FbsBudget> nextDayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                        (fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, nextFbsBudget.getBeginDate(), nextFbsBudget.getEndDate());
                if (!EmptyUtils.isEmptyList(nextDayFbsBudgets)) {
                    for (int j = 0; j < nextDayFbsBudgets.size(); j++) {
                        fbsBudgetService.getChildCorpBudget(nextDayFbsBudgets.get(j).getCorpId(), nextDayFbsBudgets.get(j).getTimeType(),
                                nextDayFbsBudgets.get(j).getBeginDate(), nextDayFbsBudgets.get(j).getEndDate());
                        if (BudgetConstant.STATUE_TEMP.equals(nextDayFbsBudgets.get(j).getStatus())
                                || BudgetConstant.STATUE_BACK.equals(nextDayFbsBudgets.get(j).getStatus())) {
                            fbsBudgetService.deleteFbsBudget(nextDayFbsBudgets.get(j).getId(), nextDayFbsBudgets.get(j).getTimeType(), user);
                        }
                    }
                }
            }
            begindate = DateUtils.addWeek(begindate, 1);
        }
    }

    /**
     * 导出数据查询
     *
     * @param id
     * @param user
     * @return
     * @throws Exception
     */
    public List<FbsBudgetRollMonthWeekVO> listRollQuarterBudget(String id, ICurrentUser user) throws Exception {
        List<FbsBudgetRollMonthWeekVO> list = new ArrayList<>();
        //周计划滚动;
        int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(id);
        if (null != fbsBudget.getBeginDate()) {
            //获取单位分配的科目
            List<FbsCorpItem> listCorpItem = fbsCorpItemService.getItemCode(fbsBudget.getCorpId());
            Date weekBeginDate = fbsBudget.getBeginDate();
            List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
            List<String> weeksHid = new ArrayList<>();
            int thismonth = DateUtils.getMonth(weekBeginDate);
            for (int i = 1; i <= wimodeRoll; i++) {
                FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
                int week = fbsBudgetPeriodService.getWeekNoByTime(weekBeginDate);
                int month = DateUtils.getMonth(weekBeginDate);
                if (month > thismonth) {
                    week = week - 1;
                }
                //保存周
                String weekShow = "";
                weekShow += month + "月第" + week + "周";
                fbsBudgetPaternityVO.setPaternal(weekShow);
                weeks.add(fbsBudgetPaternityVO);
                weeksHid.add(week + "");
                weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
            }
            List<FbsBudgetVO> fbsBudgetVOList = new ArrayList<>();
            List<FbsBudget> fbsBudgetList = new ArrayList<>();
            List<String> days = new ArrayList<>();
            //周下属天集合
            List<FbsBudgetVO> dayFbsBudgetsTotleVOList = new ArrayList<>();
            Date beginDate = fbsBudget.getBeginDate();
            if (!EmptyUtils.isEmptyList(listCorpItem)) {
                for (int i = 0; i < weeks.size(); i++) {
                    FbsBudgetVO fbsBudgetVOTemp = fbsBudget;
                    if (i > 0) {
                        //取下周预算信息
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
                        beginDate = DateUtils.addWeek(beginDate, 1);
                    }
                    if (wRollSubperiod > i) {
                        List<String> weeksTemp = new ArrayList<>();
                        //周下属天计划集合
                        this.setDayByWeek(fbsBudgetVOTemp, days, weeksTemp);
                        //月下属周计划集合
                        List<FbsBudgetVO> dayFbsBudgetsVOList = new ArrayList<>();
                        //一周有多少天
                        int count = DateUtils.dayDiffs(fbsBudgetVOTemp.getEndDate(), fbsBudgetVOTemp.getBeginDate()) + 1;
                        //获取
                        List<FbsBudget> dayFbsBudgets = fbsBudgetRollMonthWeekDao.listCorpBudget
                                (fbsBudgetVOTemp.getCorpId(), BudgetConstant.TIME_TYPE_DAY, fbsBudgetVOTemp.getBeginDate(), fbsBudgetVOTemp.getEndDate());
                        Date dayBeginDate = fbsBudgetVOTemp.getBeginDate();
                        if (null != dayFbsBudgets && dayFbsBudgets.size() > 1) {
                            Map<Long, Object> MonthMap = new HashMap<Long, Object>();
                            for (int j = 0; j < dayFbsBudgets.size(); j++) {
                                FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.getBudgetNotItem(dayFbsBudgets.get(j).getId());
                                MonthMap.put(monthFbsBudgetVO.getBeginDate().getTime(), monthFbsBudgetVO);
                            }
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                if (MonthMap.containsKey(vo.getBeginDate().getTime())) {
                                    dayFbsBudgetsVOList.add((FbsBudgetVO) MonthMap.get(vo.getBeginDate().getTime()));
                                } else {
                                    FbsBudgetVO monthFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, vo.getBeginDate(),
                                            vo.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                    dayFbsBudgetsVOList.add(monthFbsBudgetVO);
                                }
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        } else {//如果没有预算就初始化预算
                            for (int j = 0; j < count; j++) {
                                FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(dayBeginDate, BudgetConstant.TIME_TYPE_DAY);
                                FbsBudgetVO dayFbsBudgetVO = fbsBudgetService.initBudget(fbsBudget.getCorpId(), BudgetConstant.TIME_TYPE_DAY, vo.getBeginDate(),
                                        vo.getEndDate(), BudgetConstant.STATUE_TEMP, user);
                                dayFbsBudgetsVOList.add(dayFbsBudgetVO);
                                dayBeginDate = DateUtils.addDay(dayBeginDate, 1);
                            }
                        }
                        weeks.get(i).setChildList(weeksTemp);
                        weeks.get(i).setPaternalFbsBudgetVO(fbsBudgetVOTemp);
                        weeks.get(i).setChildFbsBudgetVOList(dayFbsBudgetsVOList);
                        dayFbsBudgetsTotleVOList.addAll(dayFbsBudgetsVOList);
                    }
                }
                Map<String, BigDecimal> map = this.fbsBudgetDetailService.collectBudgetDetailRollWeek(fbsBudget.getId(), BudgetConstant.BUDGET_TARGET_INPUT, dayFbsBudgetsTotleVOList);
                for (int i = 0; i < fbsBudget.getDtlList().size(); i++) {
                    //本周明细
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
                    //滚动周明细
                    for (int j = 0; j < fbsBudgetVOList.size(); j++) {
                        FbsBudgetVO nextFbsBudgetVO = fbsBudgetVOList.get(j);
                        if (!EmptyUtils.isEmpty(nextFbsBudgetVO)) {
                            FbsBudgetDtlAbsVO nextFbsBudgetDtlAbsVO = nextFbsBudgetVO.getDtlList().get(i);
                            idList.add(nextFbsBudgetDtlAbsVO.getId());
                            bMoneyList.add(nextFbsBudgetDtlAbsVO.getbMoney());
                            detailMoneyList.add(map.get(nextFbsBudgetDtlAbsVO.getId() + "_" + nextFbsBudgetDtlAbsVO.getItemCode() + "_" + DateUtils.getMonth(nextFbsBudgetVO.getBeginDate())));
                        }
                    }
                    //分解天明细
                    List<List<FbsBudgetDtlAbsVO>> childDayBudget = new ArrayList<>();
                    int j = 0;
                    int size = 0;
                    for (int k = 0; k < days.size(); k++) {
                        List<FbsBudgetDtlAbsVO> absVOList = new ArrayList<>();
                        size = (k + 1) * 7;
                        if (size > dayFbsBudgetsTotleVOList.size()) {
                            size = dayFbsBudgetsTotleVOList.size();
                        }
                        for (; j < size; j++) {
                            FbsBudgetDtlAbsVO fbsBudgetDtlAbsVODay = dayFbsBudgetsTotleVOList.get(j).getDtlList().get(i);
                            fbsBudgetDtlAbsVODay.setDetailMoney(map.get(vo.getId() + "_" + vo.getItemCode() + "_" + (j + days.size() + 1)));
                            absVOList.add(fbsBudgetDtlAbsVODay);
                        }
                        if (absVOList.size() > 0) {
                            childDayBudget.add(absVOList);
                        }
                    }
                    vo.setCanBMoney(fbsBudgetDtlAbsVO.getCanBMoney());
                    vo.setChildWeekBudget(childDayBudget);
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
     * 设置周的分解天
     *
     * @param fbsBudgetVO
     * @param dayList
     */
    public void setDayByWeek(FbsBudgetVO fbsBudgetVO, List<String> dayList, List<String> weeks) {
        String result = "";
        int month = DateUtils.getMonth(fbsBudgetVO.getBeginDate());
        int week = fbsBudgetPeriodService.getWeekNoByTime(fbsBudgetVO.getBeginDate());
        //一周有多少天
        int count = DateUtils.dayDiffs(fbsBudgetVO.getEndDate(), fbsBudgetVO.getBeginDate()) + 1;
        for (int i = 1; i <= count; i++) {
            result = "第" + i + "天";
            dayList.add(result);
            weeks.add(i + "");
        }
        result = month + "月第" + week + "周";
        dayList.add(result);
    }
}
