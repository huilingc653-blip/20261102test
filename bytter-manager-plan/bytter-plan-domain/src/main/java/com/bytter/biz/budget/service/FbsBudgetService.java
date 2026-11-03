package com.bytter.biz.budget.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.dao.FbsBudgetDao;
import com.bytter.biz.budget.dao.FbsCollectCorpDao;
import com.bytter.biz.budget.entity.*;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.utils.BudgetUtils;
import com.bytter.biz.budget.vo.*;
import com.bytter.biz.dto.FbsBudgetSaveDTO;
import com.bytter.biz.dto.FbsBudgetSaveDetailDTO;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.vo.FbsBudgetProcessPageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowStartedListener;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.*;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 资金计划上报主表
 * @date 2017-07-14 14:51:48
 */
@Service("fbsBudgetService")
@Transactional
public class FbsBudgetService extends BaseService<FbsBudget, String> implements ICorpDeleteVerifyService, ICorpDisableVerifyService, ICorpParentChangVerifyService, DisposableBean, IWorkFlowStartedListener , DictInjectProvider {
	@Autowired
	private FbsBudgetDao fbsBudgetDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsCorpItemService fbsCorpItemService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsItemService fbsItemService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	@Autowired
	private FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	@Autowired
	private FbsBudgetDetailService fbsBudgetDetailService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@Autowired
	private FbsMessageService fbsMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Resource
    private IMultiLangService multiLangServiceI;
	@Autowired
	private FbsProcessRuleService fbsProcessRuleService;
	@Autowired
	private FbsCollectCorpDao fbsCollectCorpDao;

    @DubboReference(check = false)
    private IDictService dictService;
	/**
	 * 付款办理批量处理线程池
	 */
	private static ExecutorService 	FBS_BUDGET_START_EXECUTOR_SERVICE = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(20));
	@Override
	public void destroy() throws Exception {
		FBS_BUDGET_START_EXECUTOR_SERVICE.shutdown();
	}
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
		//存储冗余字段
		this.setRedundantFields(entity);
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudget entity) {
		//存储冗余字段
		this.setRedundantFields(entity);
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudget entity) {
		//存储冗余字段
		this.setRedundantFields(entity);
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudget, String> getEntityDao() {
		return fbsBudgetDao;
	}
	/**
	 * 获取上报模式
	 *
	 * @param configName
	 * @return
	 */
	public String getInputMode(String configName) {
		return configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, configName).getValue();
	}
	/**
	 * 判断是否启用周期类型
	 *
	 * @param timeType
	 * @return
	 */
	public boolean isExistTimeType(String timeType) {
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
		String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
		String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
		String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
		if (BudgetConstant.TIME_TYPE_YEAR.equals(timeType) && !BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
			return true;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType) && !BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
			return true;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType) && !BudgetConstant.CONFIG_CRL_0.equals(mcrl)) {
			return true;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType) && !BudgetConstant.CONFIG_CRL_0.equals(wcrl)) {
			return true;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType) && !BudgetConstant.CONFIG_CRL_0.equals(dcrl)) {
			return true;
		}
		return false;
	}
	/**
	 * 获取最末级计划周期
	 *
	 * @return
	 */
	public String getEndTimeType() {
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
		String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
		String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
		String dcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_DCRL).getValue();
		if (!BudgetConstant.CONFIG_CRL_0.equals(dcrl)) {
			return BudgetConstant.TIME_TYPE_DAY;
		} else if (!BudgetConstant.CONFIG_CRL_0.equals(wcrl)) {
			return BudgetConstant.TIME_TYPE_WEEK;
		} else if (!BudgetConstant.CONFIG_CRL_0.equals(mcrl)) {
			return BudgetConstant.TIME_TYPE_MONTH;
		} else if (!BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
			return BudgetConstant.TIME_TYPE_QUARTER;
		} else if (!BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
			return BudgetConstant.TIME_TYPE_YEAR;
		}
		return "";
	}
	/**
	 * 获取父计划周期
	 *
	 * @param timeType
	 * @return
	 */
	public String getParentTimeType(String timeType) {
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
		String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
		String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
		if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
			//季计划
			if (BudgetConstant.CONFIG_CRL_1.equals(ycrl)) {
				return BudgetConstant.TIME_TYPE_YEAR;
			} else {
				return "";
			}
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
			//月计划
			if (BudgetConstant.CONFIG_CRL_1.equals(qcrl)) {
				return BudgetConstant.TIME_TYPE_QUARTER;
			} else {
				if (BudgetConstant.CONFIG_CRL_1.equals(ycrl)) {
					return BudgetConstant.TIME_TYPE_YEAR;
				} else {
					return "";
				}
			}
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
			//周计划
			if (BudgetConstant.CONFIG_CRL_1.equals(mcrl)) {
				return BudgetConstant.TIME_TYPE_MONTH;
			} else {
				if (BudgetConstant.CONFIG_CRL_1.equals(qcrl)) {
					return BudgetConstant.TIME_TYPE_QUARTER;
				} else {
					if (BudgetConstant.CONFIG_CRL_1.equals(ycrl)) {
						return BudgetConstant.TIME_TYPE_YEAR;
					} else {
						return "";
					}
				}
			}
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType)) {
			//天计划
			if (BudgetConstant.CONFIG_CRL_1.equals(wcrl)) {
				return BudgetConstant.TIME_TYPE_WEEK;
			} else {
				if (BudgetConstant.CONFIG_CRL_1.equals(mcrl)) {
					return BudgetConstant.TIME_TYPE_MONTH;
				} else {
					if (BudgetConstant.CONFIG_CRL_1.equals(qcrl)) {
						return BudgetConstant.TIME_TYPE_QUARTER;
					} else {
						if (BudgetConstant.CONFIG_CRL_1.equals(ycrl)) {
							return BudgetConstant.TIME_TYPE_YEAR;
						} else {
							return "";
						}
					}
				}
			}
		}
		return "";
	}
	/**
	 * 获取父计划周期(控制or不控制)
	 *
	 * @param timeType
	 * @return
	 */
	public String getParentTimeTypeAll(String timeType) {
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		String qcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_QCRL).getValue();
		String mcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MCRL).getValue();
		String wcrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_WCRL).getValue();
		if (BudgetConstant.TIME_TYPE_QUARTER.equals(timeType)) {
			//季计划
			if (!BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
				return BudgetConstant.TIME_TYPE_YEAR;
			} else {
				return "";
			}
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(timeType)) {
			//月计划
			if (!BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
				return BudgetConstant.TIME_TYPE_QUARTER;
			} else {
				if (BudgetConstant.CONFIG_CRL_1.equals(ycrl)) {
					return BudgetConstant.TIME_TYPE_YEAR;
				} else {
					return "";
				}
			}
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
			//周计划
			if (!BudgetConstant.CONFIG_CRL_0.equals(mcrl)) {
				return BudgetConstant.TIME_TYPE_MONTH;
			} else {
				if (!BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
					return BudgetConstant.TIME_TYPE_QUARTER;
				} else {
					if (!BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
						return BudgetConstant.TIME_TYPE_YEAR;
					} else {
						return "";
					}
				}
			}
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(timeType)) {
			//天计划
			if (!BudgetConstant.CONFIG_CRL_0.equals(wcrl)) {
				return BudgetConstant.TIME_TYPE_WEEK;
			} else {
				if (!BudgetConstant.CONFIG_CRL_0.equals(mcrl)) {
					return BudgetConstant.TIME_TYPE_MONTH;
				} else {
					if (!BudgetConstant.CONFIG_CRL_0.equals(qcrl)) {
						return BudgetConstant.TIME_TYPE_QUARTER;
					} else {
						if (!BudgetConstant.CONFIG_CRL_0.equals(ycrl)) {
							return BudgetConstant.TIME_TYPE_YEAR;
						} else {
							return "";
						}
					}
				}
			}
		}
		return "";
	}
	/**
	 * 判断单位是否上报过计划
	 *
	 * @param corpId
	 * @return
	 */
	public boolean isExistBudget(String corpId) {
		List<FbsBudget> list = fbsBudgetDao.listBy("corpId", corpId);
		if (EmptyUtils.isEmptyList(list)) {
			return false;
		}
		return true;
	}
    /**
     * 判断单位是否上报过计划
     *
     * @param corpIds
     * @return
     */
    public Map<String, Boolean> isExistBudget(List<String> corpIds) {
        List<FbsBudget> list = fbsBudgetDao.listByCorpids(corpIds);
        Map<String, List<FbsBudget>> listMap = list.stream().collect(Collectors.groupingBy(FbsBudget::getCorpId));
        Map<String, Boolean> flagMap = new HashMap<>();
        for (String corpId : corpIds) {
            if (listMap.containsKey(corpId)) {
                flagMap.put(corpId, Boolean.TRUE);
            } else {
                flagMap.put(corpId, Boolean.FALSE);
            }
        }
        return flagMap;
    }
    /**
	 * 获取有权限的单位
	 *
	 * @param showType
	 * @return
	 */
	public List<CorpDTO> listEffectiveCorp(String showType, ICurrentUser user) {
		String resource = ResourceConstant.FBS_INPUT_FBSBUDGET_APPLY;
		//showType为空时，表追加查询
		if (StringUtils.isEmpty(showType)) {
			resource = ResourceConstant.FBS_SUPER_FBSBUDGETSUPERADD_APPLY;
		}
		List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
		if (EmptyUtils.isEmptyList(corpIds)) {
			return null;
		}
		List<String> ctlCorpIds = this.fbsCorpBudgetService.listCorpIdsByReportBudget(corpIds);
		if (EmptyUtils.isEmptyList(ctlCorpIds)) {
			return null;
		}
//		List<CorpDTO> listCorp = this.corpService.listCorpDTOByCorpIds(ctlCorpIds);
//		if (EmptyUtils.isEmptyList(listCorp)) {
//			return null;
//		}
		return corpService.listTreeCorpDTOByCorpIds(ctlCorpIds);
	}
	/**
	 * 获取有权限的单位
	 *
	 * @param showType
	 * @return
	 */
	public List<CorpDTO> listEffectiveCorpV2(String showType, ICurrentUser user) {
		StopWatch stopWatch=new StopWatch("listEffectiveCorp");
		stopWatch.start("corpService.getUserCorpIds");
		String resource = ResourceConstant.FBS_INPUT_FBSBUDGET_APPLY;
		//showType为空时，表追加查询
		if (StringUtils.isEmpty(showType)) {
			resource = ResourceConstant.FBS_SUPER_FBSBUDGETSUPERADD_APPLY;
		}
//		List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
		List<String> corpIds = corpService.getCorpIdsByUserResource(resource, user.getId());
		if (EmptyUtils.isEmptyList(corpIds)) {
			return null;
		}
		stopWatch.stop();
		stopWatch.start("fbsCorpBudgetService.listCorpIdsByReportBudget");
		List<String> ctlCorpIds = this.fbsCorpBudgetService.listCorpIdsByReportBudget(corpIds);
		if (EmptyUtils.isEmptyList(ctlCorpIds)) {
			return null;
		}
//		List<CorpDTO> listCorp = this.corpService.listCorpDTOByCorpIds(ctlCorpIds);
//		if (EmptyUtils.isEmptyList(listCorp)) {
//			return null;
//		}
		stopWatch.stop();
		stopWatch.start("corpService.listTreeCorpDTOByCorpIds");
		List<CorpDTO> result = corpService.listTreeCorpDTOByCorpIdsV2(ctlCorpIds);
		stopWatch.stop();
		logger.debug(" listEffectiveCorp all spends :{}",stopWatch.prettyPrint());
		return result;
	}
	/**
	 * 根据单位ID集合和周期类型获取对应的预算上报信息
	 *
	 * @param corpIds
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Map<String, FbsBudget> listCorpBudget(List<String> corpIds, String timeType, Date beginDate, Date endDate) {
		List<FbsBudget> listBudget = this.fbsBudgetDao.listCorpBudget(corpIds, timeType, beginDate, endDate);
		Map<String, FbsBudget> budgetMap = new HashMap<>();
		if(EmptyUtils.isNotEmpty(listBudget)) {
			for (FbsBudget budget : listBudget) {
				budgetMap.put(budget.getCorpId(), budget);
			}
		}
		return budgetMap;
	}
	/**
	 * 计划上报树形展示
	 *
	 * @param timeType 周期类型
	 * @param showType 显示过滤
	 * @param flag     0-本期   1-下期
	 * @return
	 */
	public List<FbsBudgetInputPreVO> listTreeData(String timeType, String showType, int flag, String opType,List<CorpDTO> list) {
		List<FbsBudgetInputPreVO> listBudgetInput = new ArrayList<FbsBudgetInputPreVO>();
 		if (!EmptyUtils.isEmptyList(list)) {
			//获取周期
			FbsBudgetPeriodVO fbsBudgetPeriod = new FbsBudgetPeriodVO();
			if (flag == 0) {
				fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);//本期
			} else {
				fbsBudgetPeriod = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(DateUtils.getDate(), timeType);//下期
			}
			fbsBudgetPeriod.setShowEndDate(fbsBudgetPeriod.getEndDateStr());
			String mcrl = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_MCRL).getValue();
			String mimode = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
			// 选择月滚动
			if(BudgetConstant.TIME_TYPE_MONTH.equals(timeType) && !"0".equalsIgnoreCase(mcrl) && ("1".equalsIgnoreCase(mimode) || "2".equalsIgnoreCase(mimode)) &&"input".equalsIgnoreCase(opType)) {
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
				int month = DateUtils.getMonth(fbsBudgetPeriod.getBeginDate());
				List<String> months = new ArrayList<String>();
				for (int i = 1; i <= mimodeRoll; i++) {
					if (!isSpanYear) {
						if (month <= 12) {
							months.add(month + "");
							month++;
						}
					}else{
						months.add(month + "");
						month++;
					}
				}
				fbsBudgetPeriod.setShowEndDate(DateUtils.formatDate(DateUtils.getLastMonthDate(DateUtils.addMonth(fbsBudgetPeriod.getEndDate(), months.size()-1))));
			}
			// 批量获取单位计划
			List<String> corpIds = new ArrayList<>();
			for (CorpDTO corpDTO : list) {
				corpIds.add(corpDTO.getId());
			}
			//获取单位计划集合
			Map<String, FbsBudget> budgetMap = this.listCorpBudget(corpIds, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			//获取汇总单位集合
			Map<String, String> collectCorpMap = this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(corpIds);
			for (CorpDTO corpDTO : list) {
				FbsBudgetInputPreVO input = new FbsBudgetInputPreVO();
				input.setCorpId(corpDTO.getId());
				input.setCorpCode(corpDTO.getCode());
				input.setCorpName(corpDTO.getName());
				input.setParentCorpId(corpDTO.getParentId());
				input.setCorpType(corpDTO.getType());
				input.setTimeType(timeType);
				input.setShowType(showType);
				input.setCollectFlag(collectCorpMap.containsKey(corpDTO.getId()) ? 1 : 0);
				input.setId("");
				input.setStatus(BudgetConstant.STATUE_NULL);
				input.setBeginDate(fbsBudgetPeriod.getBeginDate());
				input.setEndDate(fbsBudgetPeriod.getEndDate());
				input.setShowEndDate(fbsBudgetPeriod.getShowEndDate());
				// 选择月滚动+双月周明细，无需上报周计划  zuoy 2018-11-28
				if(BudgetConstant.TIME_TYPE_WEEK.equals(timeType) && !"0".equalsIgnoreCase(mcrl) && "2".equalsIgnoreCase(mimode)){
					input.setView(true);
				}else{
					input.setView(false);
				}
				//获取单位周期计划
				FbsBudget budget = budgetMap.get(corpDTO.getId());
				if (budget != null) {
					input.setId(budget.getId());
					input.setEnrolDate(budget.getEnrolDate());
					input.setStatus(budget.getStatus());
					input.setCurId(budget.getCurId());
					input.setCurName(budget.getCurName());
					input.setExchangeRate(budget.getExchangeRate());
				}
				//showType为空时，表追加查询，过滤计划编制完成的单位
				if (StringUtils.isEmpty(showType)) {
					if (input.getStatus() < BudgetConstant.STATUE_CHECK) {
						continue;
					}
					if (StringUtils.isNotEmpty(budget.getId())) {
						FbsBudgetSuperadd budgetSuperadd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budget.getId());
						if (budgetSuperadd != null) {
							input.setSuperaddId(budgetSuperadd.getId());
						}
					}
					//获取待汇总子单位数
					if (collectCorpMap.containsKey(corpDTO.getId())) {
						input.setChildCorp(this.getChildCorp(budget));
					}
				} else if (BudgetConstant.SHOW_TYPE_2.equals(showType)) {
					//过滤录入状态的单位
					if (!BudgetConstant.STATUE_NULL.equals(input.getStatus()) && !BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
						continue;
					}
				}
				listBudgetInput.add(input);
			}
		}
		return listBudgetInput;
	}
	/**
	 * 获取汇总单位待汇总子单位数
	 *
	 * @param budget
	 * @return
	 */
	public Integer getChildCorp(FbsBudget budget) {
		Integer result = 0;
		//获取管理的单位
		List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(budget.getCorpId());
		if (!EmptyUtils.isEmptyList(list)) {
			List<String> corpIds = new ArrayList<>();
			for (FbsCorpBudgetDTO dto : list) {
				//过滤计划不控制单位
				if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
					continue;
				}
				corpIds.add(dto.getCorpId());
			}
			//批量获取子单位集合
			List<FbsBudget> childBudgetList = this.fbsBudgetDao.listCorpBudget(corpIds, budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
			if(!EmptyUtils.isEmptyList(childBudgetList)) {
				for (FbsBudget childBudget : childBudgetList) {
					//计划追加待汇总
					if (BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT.equals(childBudget.getStatus())) {
						result = result + 1;
					}
				}
			}
		}
		return result;
	}
	/**
	 * 周整月计划上报树形展示
	 *
	 * @param timeType 周期类型
	 * @param showType 1-本月周计划 2-下月周计划
	 * @return
	 */
	public LinkedHashMap<String, List<FbsBudgetInputPreVO>> listTreeDataWeek(String timeType, String showType, ICurrentUser user) {
		LinkedHashMap<String, List<FbsBudgetInputPreVO>> map = new LinkedHashMap<String, List<FbsBudgetInputPreVO>>();
		//获取周期
		List<FbsBudgetPeriodVO> listPeriod = null;
		if (BudgetConstant.SHOW_TYPE_WEEK_1.equals(showType)) {
			listPeriod = fbsBudgetPeriodService.autoAenerateByMonth(DateUtils.getDate());//本月周计划
		} else {
			listPeriod = fbsBudgetPeriodService.autoAenerateByMonth(DateUtils.addDay(DateUtils.getDate(), 30));//下月周计划
		}
		//获取管理的单位
		List<CorpDTO> list = this.listEffectiveCorp(showType, user);
		if (!EmptyUtils.isEmptyList(list)) {
			String mcrl = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_MCRL).getValue();
			String mimode = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
			for (int i = 0; i < listPeriod.size(); i++) {
				FbsBudgetPeriodVO fbsBudgetPeriod = listPeriod.get(i);
				List<FbsBudgetInputPreVO> listBudgetInput = new ArrayList<FbsBudgetInputPreVO>();
				// 批量获取单位计划
				List<String> corpIds = new ArrayList<>();
				for (CorpDTO corpDTO : list) {
					corpIds.add(corpDTO.getId());
				}
				//获取单位计划集合
				Map<String, FbsBudget> budgetMap = this.listCorpBudget(corpIds, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
				//获取汇总单位集合
				Map<String, String> collectCorpMap = this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(corpIds);
				for (CorpDTO corpDTO : list) {
					FbsBudgetInputPreVO input = new FbsBudgetInputPreVO();
					input.setCorpId(corpDTO.getId());
					input.setCorpCode(corpDTO.getCode());
					input.setCorpName(corpDTO.getName());
					input.setParentCorpId(corpDTO.getParentId());
					input.setCorpType(corpDTO.getType());
					input.setTimeType(timeType);
					input.setShowType(showType);
					input.setCollectFlag(collectCorpMap.containsKey(corpDTO.getId()) ? 1 : 0);
					input.setId("");
					input.setStatus(BudgetConstant.STATUE_NULL);
					input.setBeginDate(fbsBudgetPeriod.getBeginDate());
					input.setEndDate(fbsBudgetPeriod.getEndDate());
					input.setView(false);
					Date weekDate = BudgetUtils.isFirstWeek(DateUtils.getDate(), DateUtils.getFirstWeekDate(DateUtils.getDate()));
					if (fbsBudgetPeriod.getEndDate().compareTo(weekDate) == -1) {
						input.setView(true);
					}
					// 选择月滚动+双月周明细，无需上报周计划  zuoy 2018-11-28
					if(!"0".equalsIgnoreCase(mcrl) && "2".equalsIgnoreCase(mimode)){
						input.setView(true);
					}
					//获取单位周期计划
					FbsBudget budget = budgetMap.get(corpDTO.getId());
					if (budget != null) {
						input.setId(budget.getId());
						input.setEnrolDate(budget.getEnrolDate());
						input.setStatus(budget.getStatus());
						input.setCurId(budget.getCurId());
						input.setCurName(budget.getCurName());
						input.setExchangeRate(budget.getExchangeRate());
					}
					listBudgetInput.add(input);
				}
				map.put((i + 1) + "", listBudgetInput);
			}
		}
		return map;
	}
	/**
	 * 天计划连续上报树形展示
	 *
	 * @param timeType 周期类型
	 * @param showType 1-本月周计划 2-下月周计划
	 * @return
	 */
	public LinkedHashMap<String, List<FbsBudgetInputPreVO>> listTreeDataDay(String timeType, String showType, ICurrentUser user) {
		LinkedHashMap<String, List<FbsBudgetInputPreVO>> map = new LinkedHashMap<String, List<FbsBudgetInputPreVO>>();
		//天计划连续上报天数
		String dimodeOneTimeDays = this.getInputMode(BudgetConstant.FBS_CONFIG_DIMODEONETIMEDAYS);
		int days = 7;
		if(!StringUtils.isBlank(dimodeOneTimeDays)){
			days = Integer.parseInt(dimodeOneTimeDays);
		}
		//获取管理的单位
		List<CorpDTO> list = this.listEffectiveCorp(showType, user);
		if (!EmptyUtils.isEmptyList(list)) {
			for (int i = 0; i < days; i++) {
				Date curDate = DateUtils.addDay(DateUtils.getDateYMD(), i);
				List<FbsBudgetInputPreVO> listBudgetInput = new ArrayList<FbsBudgetInputPreVO>();
				// 批量获取单位计划
				List<String> corpIds = new ArrayList<>();
				for (CorpDTO corpDTO : list) {
					corpIds.add(corpDTO.getId());
				}
				//获取单位计划集合
				Map<String, FbsBudget> budgetMap = this.listCorpBudget(corpIds, timeType, curDate, curDate);
				//获取汇总单位集合
				Map<String, String> collectCorpMap = this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(corpIds);
				for (CorpDTO corpDTO : list) {
					FbsBudgetInputPreVO input = new FbsBudgetInputPreVO();
					input.setCorpId(corpDTO.getId());
					input.setCorpCode(corpDTO.getCode());
					input.setCorpName(corpDTO.getName());
					input.setParentCorpId(corpDTO.getParentId());
					input.setCorpType(corpDTO.getType());
					input.setTimeType(timeType);
					input.setShowType(showType);
					input.setCollectFlag(collectCorpMap.containsKey(corpDTO.getId()) ? 1 : 0);
					input.setId("");
					input.setStatus(BudgetConstant.STATUE_NULL);
					input.setBeginDate(curDate);
					input.setEndDate(curDate);
					input.setView(false);
					//获取单位周期计划
					FbsBudget budget = budgetMap.get(corpDTO.getId());;
					if (budget != null) {
						input.setId(budget.getId());
						input.setEnrolDate(budget.getEnrolDate());
						input.setStatus(budget.getStatus());
						input.setCurId(budget.getCurId());
						input.setCurName(budget.getCurName());
						input.setExchangeRate(budget.getExchangeRate());
					}
					if (BudgetConstant.SHOW_TYPE_2.equals(showType)) {
						//过滤录入状态的单位
						if (!BudgetConstant.STATUE_NULL.equals(input.getStatus()) && !BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
							continue;
						}
					}
					listBudgetInput.add(input);
				}
				map.put(DateUtils.getWeekdayDesc(curDate) + "("+ DateUtils.formatDate(curDate, DateUtils.PATTERN_CLASSICAL_SIMPLE)+")", listBudgetInput);
			}
		}
		return map;
	}
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudget getCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		return fbsBudgetDao.getCorpBudget(corpId, timeType, beginDate, endDate);
	}
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetVO getByUnion(String id) {
		return fbsBudgetDao.getByUnion(id);
	}
	/**
	 * 获取单位上报的资金计划
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @return
	 */
	public List<FbsBudget> getCorpBudget(String corpId, String timeType, Date beginDate) {
		return fbsBudgetDao.getCorpBudget(corpId, timeType, beginDate);
	}

	/**
	 * 初始化计划上报数据
	 *
	 * @param corpId    单位ID。
	 * @param timeType  周期类型。
	 * @param beginDate 周期开始时间。
	 * @param endDate   周期结束时间。
	 * @return 计划上报数据。
	 */
	public FbsBudgetVO initBudget(String corpId, String timeType, Date beginDate, Date endDate, Integer status, ICurrentUser user) {
		//是否是虚拟单位
		boolean fictitiousCorp = false;
		FbsCollectCorpVO fbsCollectCorpVO  = this.listCorpById(corpId);
		if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
			fictitiousCorp = true;
		}
		//获取单位周期计划
		FbsBudget corpBudget = null;
		if (fictitiousCorp) {
			corpBudget = this.getCorpBudget(fbsCollectCorpVO.getId(), timeType, beginDate, endDate);
		} else {
			corpBudget = this.getCorpBudget(corpId, timeType, beginDate, endDate);
		}
		if (corpBudget != null) {
			return this.getBudget(corpBudget.getId());
		}
		FbsBudgetVO entity = new FbsBudgetVO();
		//初始化计划主表
		FbsBudget budget = new FbsBudget();
		budget.setCorpId(corpId);
		budget.setEnrolDate(DateUtils.getDate());
		budget.setBeginDate(beginDate);
		budget.setEndDate(endDate);
		budget.setTimeType(timeType);
		budget.setCheckMoney(BigDecimal.ZERO);
		budget.setStatus(status);
		budget.setCreateBy(user.getLoginName());
		budget.setCreateDate(DateUtils.getDate());
		//判断单位是否是汇总单位，汇总单位币别固定本位币（顶级网点/单位的本位币）
		if (fbsCorpBudgetService.getChildCollectFlag(corpId)) {
			//获取顶级网点/单位
			CorpDTO topCorp = corpService.getTopCorpByCorpId(corpId);
			budget.setCurId(topCorp.getCurId());
			budget.setExchangeRate(BigDecimal.ONE);
		}else {
			//获取已上报的币别
			List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(corpId, null, DateUtils.getFirstYearDate(beginDate), DateUtils.getLastYearDate(beginDate), null);
			if (!EmptyUtils.isEmptyList(budgets)) {
				budget.setCurId(budgets.get(0).getCurId());
				budget.setExchangeRate(budgets.get(0).getExchangeRate());
				entity.setIsEditCur(CommonConstant.NO);
			} else {
				//获取单位本身的本位币
				CorpDTO corpDTO = corpService.get(corpId);
				//如果单位是空则取虚拟单位
				if(EmptyUtils.isEmpty(corpDTO)&&fictitiousCorp){
					corpDTO = corpService.get(fbsCollectCorpVO.getCorpId());
				}
				budget.setCurId(corpDTO.getCurId());
				//获取顶级单位/网点
				CorpDTO topCorp = corpService.getTopCorpByCorpId(corpId);
				BigDecimal exChangeRate = BigDecimal.ONE;
				if (topCorp != null && !corpDTO.getCurId().equals(topCorp.getCurId())) {
					exChangeRate = fcExcrateAdjustService.getExchangeRateBy(corpDTO.getCurId(), topCorp.getCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
				}
				budget.setExchangeRate(exChangeRate);
				entity.setIsEditCur(CommonConstant.YES);
			}
		}
		this.save(budget);
		BeanUtils.copyNotNullProperties(budget, entity);
		if (status.intValue() == BudgetConstant.STATUE_TEMP.intValue()) {
			//生成日志
			fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_INIT, FbsMessageConstant.FBS_LOG_INIT_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		}
		//获取单位分配的科目
		List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(corpId);
		if(fictitiousCorp){
			listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(fbsCollectCorpVO.getCorpId());
		}
		//初始化明细表
		List<FbsBudgetDtlAbsVO> dtlList = new ArrayList<FbsBudgetDtlAbsVO>();
		HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
//		CorpDTO parentCorp = corpService.getParentById(corpId);//获取上级单位
		if (!EmptyUtils.isEmptyList(listFbsItem)) {
			for (FbsItemVO item : listFbsItem) {
				FbsBudgetDtlAbs dtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(timeType);
				dtl.setBudgetId(budget.getId());
				dtl.setItemCode(item.getItemCode());
				dtl.setItemName(item.getItemName());
				dtl.setbMoney(BigDecimal.ZERO);
				dtl.setBsuperMoney(BigDecimal.ZERO);
				dtl.setBtempMoney(BigDecimal.ZERO);
				fbsBudgetDtlAbsService.save(dtl);
				FbsBudgetDtlAbsVO vo = new FbsBudgetDtlAbsVO();
				BeanUtils.copyNotNullProperties(dtl, vo);
				vo.setItem(item);
				dtlList.add(vo);
				dtlMap.put(vo.getItemCode(), vo);
			}
			entity.setDtlList(dtlList);
		}
		return entity;
	}
	/**
	 * 存储冗余字段信息
	 * @param budget
	 */
	public void setRedundantFields(FbsBudget budget) {
		//存储冗余字段
		if (StringUtil.isNotEmpty(budget.getCorpId())) {
			CorpDTO corp = corpService.get(budget.getCorpId());
			if (!Objects.isNull(corp)){
				budget.setCorpCode(corp.getCode());
				budget.setCorpName(corp.getName());
			}
		}
		if (StringUtil.isNotEmpty(budget.getCurId())) {
			BtCurrencyDTO cur = btCurrencyService.getObjectById(budget.getCurId());
			budget.setCurName(cur.getCurName());
			// 获取顶级单位/网点
			if (ObjectUtil.isNotEmpty(budget.getCorpId())){
				// 获取虚拟单位
				FbsCollectCorpVO collectCorp  = this.listCorpById(budget.getCorpId());
				CorpDTO corpDTO = corpService.get(budget.getCorpId());
				// 如果单位为空，则取虚拟单位
				if(EmptyUtils.isEmpty(corpDTO) && ObjectUtil.isNotEmpty(collectCorp)){
					corpDTO = corpService.get(collectCorp.getCorpId());
				}
				CorpDTO topCorp = corpService.getTopCorpByCorpId(corpDTO.getId());
				BigDecimal exChangeRate = BigDecimal.ONE;
				if (topCorp != null && !budget.getCurId().equals(topCorp.getCurId())) {
					exChangeRate = fcExcrateAdjustService.getExchangeRateBy(budget.getCurId(), topCorp.getCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
				}
				budget.setExchangeRate(exChangeRate);
			}
		}
	}
	/**
	 * 获取计划上报数据
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetVO getBudget(String id) {
		//获取单位周期计划
		FbsBudgetVO entity = this.getByUnion(id);
		List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
		if (!EmptyUtils.isEmptyList(dtlList)) {
			List<FbsItem> fbsItemList = fbsItemService.getAllFbsItem();
			Map<String, FbsItem> fbsItemMap = new HashMap<>();
			for(FbsItem fbsItem : fbsItemList){
				fbsItemMap.put(fbsItem.getItemCode(),fbsItem);
			}
			for (FbsBudgetDtlAbsVO vo : dtlList) {
				FbsItem fbsItem = fbsItemMap.get(vo.getItemCode());
                if(fbsItem!=null){
                    FbsItemVO fbsItemVO = new FbsItemVO();
                    BeanUtils.copyNotNullProperties(fbsItem,fbsItemVO);
                    vo.setItem(fbsItemVO);
                }
//				vo.setItem(fbsItemService.getFbsItemVO(null, vo.getItemCode()));
			}
		}
		entity.setDtlList(dtlList);
		return entity;
	}
	/**
	 * 获取计划上报数据并且不取科目信息
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetVO getBudgetNotItem(String id) {
		//获取单位周期计划
		FbsBudgetVO entity = this.getByUnion(id);
		List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
		entity.setDtlList(dtlList);
		return entity;
	}
	/**
	 * 获取计划上报数据，包含明细金额
	 *
	 * @param id
	 * @zuoy 2018-12-11
	 * @return
	 */
	public FbsBudgetVO getBudgetInfo(String id, String target) {
		//获取单位周期计划
		FbsBudgetVO entity = this.getByUnion(id);
		if(entity.getStatus().equals(BudgetConstant.STATUE_TEMP) || entity.getStatus().equals(BudgetConstant.STATUE_BACK)){
			entity.setIsEditCur(CommonConstant.YES);
		}else{
			entity.setIsEditCur(CommonConstant.NO);
		}
		//获取已上报的币别
		List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(entity.getCorpId(), null, DateUtils.getFirstYearDate(entity.getBeginDate()), DateUtils.getLastYearDate(entity.getBeginDate()), entity.getId());
		boolean isEditCur = true;
		for(FbsBudget fbsBudget:budgets){
			if(fbsBudget.getStatus().equals(BudgetConstant.STATUE_CHECK)){
				isEditCur = false;
				break;
			}
		}
		if (!isEditCur) {
			entity.setIsEditCur(CommonConstant.NO);
		}
		List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
		if (!EmptyUtils.isEmptyList(dtlList)) {
            List<FbsItem> fbsItemList = fbsItemService.getAllFbsItem();
            Map<String, FbsItem> fbsItemMap = new HashMap<>();
            for(FbsItem fbsItem : fbsItemList){
                fbsItemMap.put(fbsItem.getItemCode(),fbsItem);
            }
			Map<String, BigDecimal> detailMap = fbsBudgetDetailService.collectBudgetDetail(entity.getId(), entity.getTimeType(), target);
			for (FbsBudgetDtlAbsVO vo : dtlList) {
				FbsItem item = fbsItemMap.get(vo.getItemCode());
			if(item!=null){
                FbsItemVO itemVO = new FbsItemVO();
                BeanUtils.copyNotNullProperties(item,itemVO);
                vo.setItem(itemVO);
                BigDecimal detailMoney = detailMap.get(vo.getId() + "_" + vo.getItemCode());
                vo.setDetailMoney(detailMoney);
                if (detailMoney != null && detailMoney.compareTo(BigDecimal.ZERO) > 0) {
                    vo.setHasDetail(true);
                }
            }
			}
		}
		entity.setDtlList(dtlList);
		return entity;
	}
	/**
	 * 资金计划删除 - 上报
	 *
	 * @param id
	 * @param timeType
	 */
	public void deleteFbsBudget(String id, String timeType, ICurrentUser user) {
		FbsBudget entity = this.get(id, true);
		//判断是否是汇总单位删除资金计划，汇总单位需要更新汇总中的下级单位资金计划状态为待汇总
		if (this.fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(entity.getCorpId());
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO dto : list) {
					//过滤计划不控制单位
					if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
						continue;
					}
					FbsBudget budget = this.getCorpBudget(dto.getCorpId(), timeType, entity.getBeginDate(), entity.getEndDate());
					if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_WAIT_COLLECTED) {
						budget.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
						//生成日志
						fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_DELETE, FbsMessageConstant.FBS_LOG_COLLECT_DELETE, FbsMessageConstant.BUS_NAME_INPUT, user);
						this.saveOrUpdate(budget);
					}
				}
			}
		}
		//删除录入明细详情
		fbsBudgetDetailService.delColloctDetail(id, timeType);
		//删除计划明细
		fbsBudgetDtlAbsService.deleteByBudgetId(id, timeType);
		// 删除虚拟单位上报主表信息
		FbsCollectCorpVO collectCorp  = this.listCorpByCorpId(entity.getCorpId());
		if (collectCorp != null) {
			FbsBudget budget = this.getCorpBudget(collectCorp.getId(), timeType, entity.getBeginDate(), entity.getEndDate());
			if (budget != null) {
				this.delete(budget.getId());
			}
		}
		//删除计划上报主表信息
		this.delete(id);
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_DELETE, FbsMessageConstant.FBS_LOG_DELETE_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		//完成消息通知
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}
	/**
	 * 保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudget(FbsBudgetInputVO input, ICurrentUser user) {
		FbsBudget entity = this.get(input.getId(), true);
		//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额，同时状态更新为待汇总状态
		FbsBudget preCorpBudget = this.subtractFbsBudget(entity, false);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
		} else {
			entity.setStatus(input.getStatus());
		}
		entity.setCurId(input.getCurId());
		entity.setExchangeRate(input.getExchangeRate());
		entity.setAttachmentId(input.getAttachmentId());
		boolean isEdit = EmptyUtils.isBlank(input.getUpdateBy());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], entity.getTimeType());
			dtl.setbMoney(input.getbMoney()[i]);
			if(input.getRmk().length > 0) dtl.setRmk(input.getRmk()[i]);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
		}
		entity.setCheckMoney(checkMoney);
		this.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(entity, user);
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
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}


	public void saveFbsBudget(FbsBudgetSaveDTO saveDTO, ICurrentUser user) {
		FbsBudget entity = this.get(saveDTO.getId(), true);
		//如果是汇总单位编辑计划，需要扣减汇总单位的上报金额，同时状态更新为待汇总状态
		FbsBudget preCorpBudget = this.subtractFbsBudget(entity, false);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
		} else {
			entity.setStatus(saveDTO.getStatus());
		}
		entity.setCurId(saveDTO.getCurId());
		entity.setExchangeRate(saveDTO.getExchangeRate());
		entity.setAttachmentId(saveDTO.getAttachmentId());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();

		for (int i = 0; i < saveDTO.getDtlList().size(); i++) {
			FbsBudgetSaveDetailDTO detail = saveDTO.getDtlList().get(i);
			//获取明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(detail.getId(), entity.getTimeType());
			dtl.setbMoney(detail.getbMoney());
			if(StringUtils.isNotEmpty(detail.getRmk())) {
				dtl.setRmk(detail.getRmk());
			}
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
		}
		entity.setCheckMoney(checkMoney);
		this.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(entity, user);
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_TEMP;
		String msg = !EmptyUtils.isBlank(saveDTO.getMsg()) ? saveDTO.getMsg() : saveDTO.getEditFlag() ? FbsMessageConstant.FBS_LOG_ADD_TEMPSAVE_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_TEMPSAVE_SUCC;
		if (CommonConstant.YES.equalsIgnoreCase(saveDTO.getIsCollect())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_COLLECT_EDIT;
			msg = !EmptyUtils.isBlank(saveDTO.getMsg()) ? saveDTO.getMsg() : FbsMessageConstant.FBS_LOG_COLLECT_EDIT;
		}
		//提交时调用审批方法
		if (BudgetConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = FbsMessageConstant.MESSAGE_TYPE_SUBMIT;
			msg = !EmptyUtils.isBlank(saveDTO.getMsg()) ? saveDTO.getMsg() : saveDTO.getEditFlag() ? FbsMessageConstant.FBS_LOG_ADD_SUBMIT_SUCC : FbsMessageConstant.FBS_LOG_UPDATE_SUBMIT_SUCC;
			//调用审批后方法
			getStartHandler().handler(FbsWorkFlowConstant.FBS_BUDGET_INPUT, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, msg, FbsMessageConstant.BUS_NAME_INPUT, user);
		//完成消息通知
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}


	public void saveFbsBudgetSuperAdd(FbsBudgetSaveDTO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.get(input.getId(), true);
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
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(budget);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
		} else {
			entity.setStatus(input.getStatus());
		}
		entity.setAttachmentId(input.getAttachmentId());
		boolean isEdit = EmptyUtils.isBlank(entity.getUpdateBy());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		List<FbsBudgetSuperaddDetail> dtlList = new ArrayList<FbsBudgetSuperaddDetail>();
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDtlList().size(); i++) {
			FbsBudgetSaveDetailDTO detail = input.getDtlList().get(i);
			//更新计划上报明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(detail.getId(), budget.getTimeType());
			dtl.setBtempMoney(detail.getBtempMoney());//更新追加金额
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getBtempMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = fbsBudgetSuperaddDetailService.get(detail.getSuperaddDtlId(), true);
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(detail.getBtempMoney());
			superaddDtl.setRmk(detail.getRmk());
			fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
			dtlList.add(superaddDtl);
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
			superaddVO.setCorpName(budget.getCorpName());
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
	 * 审批时保存计划上报
	 *
	 * @param input
	 */
	public void saveFbsBudgetForApprove(FbsBudgetInputVO input, ICurrentUser user) {
		FbsBudget entity = this.get(input.getId(), true);
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
		}
		entity.setCheckMoney(checkMoney);
		this.saveOrUpdate(entity);
		this.fbsBudgetDtlAbsService.batchSaveOrUpdate(fbsBudgetDtlAbsList);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(entity, user);
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
		if (this.fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
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
			FbsCollectCorpVO vo = fbsCollectCorpDao.listCorpByCorpId(entity.getCorpId());
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
					FbsBudget budget = this.getCorpBudget(dto.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
					if (budget == null) {
						//初始化未编制资金计划的下级单位
						FbsBudgetVO fbsBudgetVO = this.initBudget(dto.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate(), status, user);
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
						}
						budget.setStatus(status);
						//上级单位编制完成，则更新下级单位的追加金额
						if (BudgetConstant.STATUE_CHECK.equals(entity.getStatus())) {
							//生成日志
							fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_COLLECT_APP_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
						}
						this.saveOrUpdate(budget);
					}
					//判断单位是否汇总下级单位
					if (this.fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId())) {
						//判断单位是否存在下级单位，如果存在下级单位，需要对下级单位做相同处理
						List<CorpDTO> listCorpTemp = corpService.listUnderCorpByParentCorpId(dto.getCorpId());
						if (!EmptyUtils.isEmptyList(listCorpTemp)) {
							this.saveFbsBudgetCollectUnderCorp(budget, user);
						}
					}
				}
			}
		}
	}
	/**
	 * 计划汇总-获取下级单位资金计划
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetInputPreVO> getChildCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		List<FbsBudgetInputPreVO> listBudgetInput = new ArrayList<>();
		//获取管理的单位
		List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId);
		//设置本单位虚拟单位
		setThisCorpBudgetInput(corpId,timeType,beginDate,endDate,listBudgetInput);
		if (!EmptyUtils.isEmptyList(list)) {
			for (FbsCorpBudgetDTO dto : list) {
				//过滤计划不控制单位
				if (dto.getReportBudget() == 0 || dto.getCollectFlag() == 0) {
					continue;
				}
				FbsBudgetInputPreVO input = new FbsBudgetInputPreVO();
				input.setCorpId(dto.getCorpId());
				input.setCorpCode(dto.getCorpCode());
				input.setCorpName(dto.getCorpName());
				input.setParentCorpId(dto.getCorpParentId());
				input.setCorpType(dto.getCorpType());
				input.setTimeType(timeType);
				input.setId("");
				input.setStatus(BudgetConstant.STATUE_NULL);
				input.setBeginDate(beginDate);
				input.setEndDate(endDate);
				//单位是否要汇总下级单位
				input.setCollectFlag(this.fbsCorpBudgetService.getChildCollectFlag(dto.getCorpId()) ? 1 : 0);
				//获取单位周期计划
				FbsBudget budget = this.getCorpBudget(dto.getCorpId(), timeType, beginDate, endDate);
				if (budget != null) {
					input.setId(budget.getId());
					input.setEnrolDate(budget.getEnrolDate());
					input.setStatus(budget.getStatus());
					input.setCurId(budget.getCurId());
					input.setCurName(budget.getCurName());
					input.setExchangeRate(budget.getExchangeRate());
					input.setCurName(btCurrencyService.getObjectById(budget.getCurId()).getCurName());
				}
				listBudgetInput.add(input);
			}
		}
		return listBudgetInput;
	}
	/**
	 * 设置本单位虚拟单位
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @param listBudgetInput
	 */
	public void setThisCorpBudgetInput(String corpId,String timeType, Date beginDate, Date endDate,List<FbsBudgetInputPreVO> listBudgetInput) {
		//获取管理的单位
		List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId);
		if (!EmptyUtils.isEmptyList(list)) {
			FbsCollectCorpVO vo = fbsCollectCorpDao.listCorpByCorpId(corpId);
			CorpDTO corpDTO = corpService.get(corpId);
			//如果不为空加上本单位虚拟单位
			if (!EmptyUtils.isEmpty(corpDTO)) {
				FbsBudgetInputPreVO input = new FbsBudgetInputPreVO();
				input.setCorpId(corpDTO.getId());
				input.setCorpCode(corpDTO.getCode());
				input.setCorpName(corpDTO.getName());
				input.setParentCorpId(corpDTO.getParentId());
				input.setCorpType(0);
				input.setTimeType(timeType);
				input.setId("");
				input.setStatus(BudgetConstant.STATUE_NULL);
				input.setBeginDate(beginDate);
				input.setEndDate(endDate);
				//单位是否要汇总下级单位
				input.setCollectFlag(1);
				input.setFictitiousCorp("1");
				//获取单位周期计划
				if (!EmptyUtils.isEmpty(vo)) {
					input.setCorpId(vo.getId());
					FbsBudget budget = this.getCorpBudget(vo.getId(), timeType, beginDate, endDate);
					if (budget != null) {
						input.setId(budget.getId());
						input.setEnrolDate(budget.getEnrolDate());
						input.setStatus(budget.getStatus());
						input.setCurId(budget.getCurId());
						input.setCurName(budget.getCurName());
						input.setExchangeRate(budget.getExchangeRate());
						if (ObjectUtil.isEmpty(budget.getCurName()) && ObjectUtil.isNotEmpty(budget.getCurId())) {
							input.setCurName(btCurrencyService.getObjectById(budget.getCurId()).getCurName());
						}
					}
				}else{
					FbsCollectCorp fbsCollectCorp = initCollectCorp(corpDTO);
					input.setCorpId(fbsCollectCorp.getId());
				}
				listBudgetInput.add(input);
			}
		}
	}

	/**
	 * 初始化虚拟单位。
	 *
	 * @param corpDTO 单位信息。
	 * @return FbsCollectCorp。
	 */
	private FbsCollectCorp initCollectCorp(CorpDTO corpDTO) {
		//创建虚拟单位
		FbsCollectCorp fbsCollectCorp = new FbsCollectCorp();
		fbsCollectCorp.setCorpId(corpDTO.getId());
		fbsCollectCorp.setCorpCode(corpDTO.getCode());
		fbsCollectCorp.setCorpName(corpDTO.getName());
		fbsCollectCorp.setCreateBy("auto");
		fbsCollectCorp.setTenantId(UserContextHelper.helper().getTenantId());
		fbsCollectCorp.setCreateDate(DateUtils.getDate());
		fbsCollectCorpDao.save(fbsCollectCorp);
		fbsCollectCorpDao.flush();
		return fbsCollectCorp;
	}

	/**
	 * 资金计划打回-上报
	 *
	 * @param entity
	 * @param backMsg
	 */
	public FbsBudget backFbsBudget(FbsBudget entity, String backMsg, ICurrentUser user) {
		//汇总单位计划扣减
		FbsBudget preCorpBudget = this.subtractFbsBudget(entity, false);
		entity.setStatus(BudgetConstant.STATUE_BACK);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT_BACK, backMsg, FbsMessageConstant.BUS_NAME_INPUT, user);
		//汇总打回消息写入
		String tag = "";
		if (BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_YEAR;
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_QUARTER;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_MONTH;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_WEEK;
		} else if (BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())) {
			tag = NotificationConstant.BUDGET.BUDGET_INPUT_DAY;
		}
		if (!StringUtils.isBlank(tag)) {
			final String tagTemp = tag;
			final FbsBudget fbsBudget = entity;
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			//使用线程池处理流程启动，并返回结果
			FBS_BUDGET_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
				@Override
				public String call() {
					try {
						notificationService.send(fbsBudget.getId(), fbsBudget.getCorpId(), tagTemp, fbsBudget);
					} catch (Exception e) {
						throw e;
					} finally {
						//线程监听器统计已经完成处理
						countDownLatch.countDown();
					}
					return "success";
				}
			});
		}
		return preCorpBudget;
	}
	/**
	 * 汇总打回/编辑扣减汇总单位计划追加数据
	 *
	 * @param entity
	 */
	public FbsBudget subtractFbsBudget(FbsBudget entity, boolean isRoll) {
		FbsBudget preCorpBudget = null;
		//汇总后打回，需扣减汇总单位上报金额
		if (BudgetConstant.STATUE_WAIT_COLLECT < entity.getStatus() || isRoll) {
			//获取计划明细
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
			HashMap<String, BigDecimal> dtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(dtlList)) {
				for (FbsBudgetDtlAbsVO dtl : dtlList) {
					dtlMap.put(dtl.getItemCode(), dtl.getbMoney().multiply(entity.getExchangeRate()));
				}
			}
			//获取上级单位
			CorpDTO preCorp = null;
			//如果是空则为虚拟单位
			FbsCollectCorpVO fbsCollectCorpVO = this.listCorpById(entity.getCorpId());
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
				preCorp = corpService.get(fbsCollectCorpVO.getCorpId());
			}else{
				preCorp = corpService.getParentById(entity.getCorpId());
			}
			//获取上级单位计划上报
			preCorpBudget = this.getCorpBudget(preCorp.getId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
			if(!EmptyUtils.isEmpty(preCorpBudget)) {
				List<FbsBudgetDtlAbsVO> preCorpDtlList = fbsBudgetDtlAbsService.listByBudgetId(preCorpBudget.getId(), preCorpBudget.getTimeType());
				if (!EmptyUtils.isEmptyList(preCorpDtlList)) {
					HashMap<String, FbsBudgetDtlAbsVO> dtlVoMap = new HashMap<String, FbsBudgetDtlAbsVO>();
					for (FbsBudgetDtlAbsVO preDtl : preCorpDtlList) {
						dtlVoMap.put(preDtl.getItemCode(), preDtl);
						if (dtlMap.containsKey(preDtl.getItemCode())) {
							preDtl.setbMoney(preDtl.getbMoney().subtract(dtlMap.get(preDtl.getItemCode())));
//						FbsBudgetDtlAbs budgetDtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(preCorpBudget.getTimeType());
//						BeanUtils.copyNotNullProperties(preDtl, budgetDtl);
//						fbsBudgetDtlAbsService.saveOrUpdate(budgetDtl);
//						fbsBudgetDtlAbsService.updateByDtlId(preDtl.getId(),entity.getTimeType(),preDtl.getbMoney());
						}
					}
					List<FbsItem> fbsItemList = fbsItemService.getAllFbsItem();
					Map<String, FbsItem> fbsItemMap = new HashMap<>();
					for (FbsItem fbsItem : fbsItemList) {
						fbsItemMap.put(fbsItem.getItemCode(), fbsItem);
					}
					//公式计算
					for (FbsBudgetDtlAbsVO preDtl : preCorpDtlList) {
//					preDtl.setItem(fbsItemService.getFbsItemVO(null, preDtl.getItemCode()));
						FbsItem item = fbsItemMap.get(preDtl.getItemCode());
						FbsItemVO itemVO = new FbsItemVO();
						BeanUtils.copyNotNullProperties(item, itemVO);
						preDtl.setItem(itemVO);
						this.caluteRelation(preDtl, dtlVoMap, BudgetConstant.B_MONEY, fbsItemMap);//科目公式计算
//					FbsBudgetDtlAbs budgetDtlAbs = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(entity.getTimeType());
//					BeanUtils.copyNotNullProperties(preDtl, budgetDtlAbs);
//					this.getSession().merge(budgetDtlAbs);
						fbsBudgetDtlAbsService.updateByDtlId(preDtl.getId(), entity.getTimeType(), preDtl.getbMoney());
					}
				}
			}
			//计划明细扣减
			fbsBudgetDetailService.repulseDeatil(entity);
		}
		return preCorpBudget;
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
		this.saveOrUpdate(entity);
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
			// 获取上级单位
			CorpDTO preCorp = null;
			// 如果是空则为虚拟单位
			FbsCollectCorpVO fbsCollectCorpVO = this.listCorpById(entity.getCorpId());
			if (ObjectUtil.isNotEmpty(fbsCollectCorpVO)) {
				preCorp = corpService.get(fbsCollectCorpVO.getCorpId());
			} else {
				preCorp = corpService.getParentById(entity.getCorpId());
			}
			//获取上级单位计划上报
			preCorpBudget = this.getCorpBudget(preCorp.getId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
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
	 * 计划执行组件取数
	 *
	 * @param corpId
	 * @param itemCode
	 * @param processDate
	 * @return
	 */
	public FbsBudgetProcessCommonVO getFbsBudgetProcessByItemCode(String corpId, String itemCode, Date processDate, String billNo, String billId, String curId) {
		//避免erp挂接不写计划执行日期，默认写入系统日期
		if(processDate == null){
			processDate = DateUtils.getDateYMD();
		}
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		//计划执行汇总单位科目余额控制
		String collectBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_COLLECTBALANCE).getValue();
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
		String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
		FbsBudgetProcessCommonVO process = new FbsBudgetProcessCommonVO();
		FbsBudget budget = null;
		//判断当前执行周期是否上报或者未编制完成
		FbsBudgetVO fbsBudgetVO = null;
		List<String> budgetIds = new ArrayList<>();
		for(int i=0;i<corpIds.length;i++) {
			//单位是否是汇总单位
			if(CommonConstant.NO.equals(collectBalance)
					&& fbsCorpBudgetService.getChildCollectFlag(corpIds[i])){
				//获取汇总单位本身的计划
				budget = this.getCollectCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			} else {
				//获取单位周期计划
				budget = fbsBudgetDao.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			}
			if (budget == null || budget.getStatus() < BudgetConstant.STATUE_CHECK) {
				//针对本期计划执行
				if (DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getBeginDate()) >= 0
						&& DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getEndDate()) <= 0) {
					fbsBudgetVO = this.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), true);
					if (fbsBudgetVO != null) {
						budget = new FbsBudget();
						BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
						//先执行后控制的时候需要判断
						process.setProcessRule(fbsBudgetVO.getProcessRule());
					}
				}
			}
			if(budget!=null) {
				budgetIds.add(budget.getId());
			}
		}
		//获取科目信息
		FbsItemVO item = fbsItemService.getFbsItemVO(null, itemCode);
		process.setItemId(item.getId());
		process.setItemCode(itemCode);
		process.setItemName(item.getItemName());
		if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
			if(StringUtils.isBlank(budget.getId())){
				process.setItemMoney(BigDecimal.ZERO);
				process.setSuperItemMoney(BigDecimal.ZERO);
				process.setTopItemMoney(BigDecimal.ZERO);
			}else {
				List<FbsBudgetDtlAbsVO> dtlList = null;
				if(corpIds.length > 1){
					dtlList = fbsBudgetDtlAbsService.listSumByBudgetIds(budgetIds, budget.getTimeType());
				} else {
					dtlList = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
				}
				for (FbsBudgetDtlAbsVO dtl : dtlList) {
					//增加科目过滤，提升性能
					if (!dtl.getItemCode().equals(itemCode)
							&& !dtl.getItemCode().equals(item.getSuperItemCode())
							&& !dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
						continue;
					}
					//获取本科目余额：科目余额=上报金额+追加金额+超计划比例金额-已执行金额（折合人民币）
					//超计划比例金额 = (上报金额+追加金额)*超计划比例
					BigDecimal processMoney = BigDecimal.ZERO;
					BigDecimal money = dtl.getbMoney().add(dtl.getBsuperMoney());//获取科目上报+追加金额总和
					if (fbsBudgetVO != null) {
						processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
						if (fbsBudgetVO.getProcessRatio() != null) {
							money = money.multiply(fbsBudgetVO.getProcessRatio());//上报金额*执行比例
						}
					} else {
						processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(budget.getBeginDate(), budget.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
					}
					//增加科目超计划比例的金额
					if (dtl.getOverPerSpend() != null) {
						money = money.add(money.multiply(dtl.getOverPerSpend()));//累加超计划比例金额
					}
					if (item.getSign() > 0) {
						money = money.add(processMoney);//加上计划执行金额
					} else {
						money = money.subtract(processMoney);//减去计划执行金额
					}
					if (dtl.getItemCode().equals(itemCode)) {
						process.setItemMoney(money);
					}
					//不存在上级科目
					if (StringUtils.isEmpty(item.getSuperItemCode())) {
						process.setSuperItemMoney(process.getItemMoney());
						process.setTopItemMoney(process.getItemMoney());
					} else {
						if (dtl.getItemCode().equals(item.getSuperItemCode())) {
							process.setSuperItemMoney(money);
						}
						//获取顶级科目
						if (dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
							process.setTopItemMoney(money);
						}
					}
				}
			}
		} else {
		    //存在单号表示编辑/查看页面，不弹框校验
			if(StringUtils.isBlank(billNo) && StringUtils.isBlank(billId) ) {
				//当前计划执行周期单位计划未上报或计划未编制完成！
				throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_PROCESS_MESSAGE_NOTINPUTBUDGET);
			}else{
				process.setItemMoney(BigDecimal.ZERO);
				process.setSuperItemMoney(BigDecimal.ZERO);
				process.setTopItemMoney(BigDecimal.ZERO);
			}
		}
		//获取汇率
		if(StringUtils.isBlank(curId)) {
			//默认本位币(单位本身本位币)
			curId = corpService.get(corpId).getCurId();
		}
		if (budget!=null) {
			BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, budget.getCurId(), DateUtils.setSpecifiedDateOfTime(processDate), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
			process.setExchangeRate(exchangeRate);
		}else{
			process.setExchangeRate(BigDecimal.ONE);
		}
	return process;
	}
	/**
	 * 汇总
	 *
	 * @param input
	 * @param childIds
	 */
	public List<FbsBudget> collectFbsBudget(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
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
				FbsBudget childBudget = this.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
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
		//获取汇总单位计划明细
		List<FbsBudgetDtlAbsVO> listDtl = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
		HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
		List<FbsBudget> childBudgetList = new ArrayList<FbsBudget>();
		//汇总明细
		fbsBudgetDetailService.colloctDetail(input.getId(), childIds);
		//获取下级单位计划
		for (String childId : childIds) {
			//获取汇总单位计划
			FbsBudget childBudget = this.get(childId, true);
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
			//更新单位状态为汇总中
			childBudget.setStatus(BudgetConstant.STATUE_WAIT_COLLECTED);
			this.saveOrUpdate(childBudget);
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
            if(fbsItem!=null){
                FbsItemVO fbsItemVO = new FbsItemVO();
                BeanUtils.copyNotNullProperties(fbsItem,fbsItemVO);
                dtl.setItem(fbsItemVO);
                this.caluteRelation(dtl, dtlMap, BudgetConstant.B_MONEY,fbsItemMap);//科目公式计算
                FbsBudgetDtlAbs entity = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
                BeanUtils.copyNotNullProperties(dtl, entity);
                if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
                    checkMoney = dtl.getbMoney();
                }
                fbsBudgetDtlAbsService.saveOrUpdate(entity);
            }
		}
		budget.setCheckMoney(checkMoney);
		this.saveOrUpdate(budget);
		//生成日志
		fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, FbsMessageConstant.FBS_LOG_COLLECT_SAVE_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		return childBudgetList;
	}
	/**
	 * 汇总并不更新状态
	 *
	 * @param input
	 * @param childIds
	 */
	public List<FbsBudget> collectFbsBudgetNotUpdateStatus(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user) {
		//获取汇总单位计划
		FbsBudget budget = this.get(input.getId(), true);
		//获取汇总单位计划明细
		List<FbsBudgetDtlAbsVO> listDtl = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
		HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
		List<FbsBudget> childBudgetList = new ArrayList<FbsBudget>();
		//汇总明细
		fbsBudgetDetailService.colloctDetail(input.getId(), childIds);
		//获取下级单位计划
		for (String childId : childIds) {
			//获取汇总单位计划
			FbsBudget childBudget = this.get(childId, true);
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
			this.saveOrUpdate(childBudget);
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
			this.caluteRelation(dtl, dtlMap, BudgetConstant.B_MONEY,fbsItemMap);//科目公式计算
			FbsBudgetDtlAbs entity = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
			BeanUtils.copyNotNullProperties(dtl, entity);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsService.saveOrUpdate(entity);
		}
		budget.setCheckMoney(checkMoney);
		this.saveOrUpdate(budget);
		//生成日志
		fbsMessageService.saveMessage(budget.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, FbsMessageConstant.FBS_LOG_COLLECT_SAVE_SUCC, FbsMessageConstant.BUS_NAME_INPUT, user);
		return childBudgetList;
	}
	/**
	 * 汇总 - 追加
	 *
	 * @param input
	 * @param childIds
	 */
	public List<FbsBudget> collectFbsBudgetSuperAdd(FbsBudgetInputVO input, List<String> childIds, ICurrentUser user,HashMap<String, FbsItemVO>  fbsItemVOHashMap) {
		//获取汇总单位计划上报
		FbsBudget budget = this.get(input.getId(), true);
		//获取汇总单位计划上报明细
		List<FbsBudgetDtlAbsVO> listDtl = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
		//获取汇总单位计划追加
		FbsBudgetSuperadd superAdd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budget.getId());
		//获取汇总单位计划追加明细
		List<FbsBudgetSuperaddDetailVO> listSuperAddDtl = fbsBudgetSuperaddDetailService.listVOBySuperaddId(superAdd.getId());
		//公式计算
		HashMap<String, FbsBudgetSuperaddDetailVO> dtlMap = new HashMap<String, FbsBudgetSuperaddDetailVO>();
		List<FbsBudget> childBudgetList = new ArrayList<FbsBudget>();
		//汇总明细
		fbsBudgetDetailService.colloctDetailSuperAdd(input.getId(), childIds);
		//获取下级单位计划
		for (String childId : childIds) {
			//获取子单位计划上报
			FbsBudget childBudget = this.get(childId, true);
			childBudget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECTED);//更新状态为计划追加汇总中
			this.saveOrUpdate(childBudget);
			//获取子单位计划追加
			FbsBudgetSuperadd childSuperAdd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(childBudget.getId());
			//获取子单位计划追加明细
			List<FbsBudgetSuperaddDetailVO> listChildDtl = fbsBudgetSuperaddDetailService.listVOBySuperaddId(childSuperAdd.getId());
			//循环设置汇总单位追加明细
			for (FbsBudgetSuperaddDetailVO dtl : listSuperAddDtl) {
				for (FbsBudgetSuperaddDetailVO childDtl : listChildDtl) {
					if (dtl.getItemCode().equals(childDtl.getItemCode())) {
						dtl.setBsuperMoney(dtl.getBsuperMoney().add(childDtl.getBsuperMoney().multiply(childBudget.getExchangeRate())));
						fbsBudgetSuperaddDetailService.updateSuperMoneyBySuperaddId(dtl);
					}
				}
				dtlMap.put(dtl.getItemCode(), dtl);
			}
			childBudgetList.add(childBudget);
			//更加汇总子单位汇总状态为汇总中
			childSuperAdd.setStatus(BudgetConstant.STATUE_WAIT_COLLECTED);
			fbsBudgetSuperaddService.saveOrUpdate(childSuperAdd);
			//生成日志
			fbsMessageService.saveMessage(childSuperAdd.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, "", FbsMessageConstant.BUS_NAME_SUPERADD, user);
		}
		//科目公式计算
		BigDecimal checkMoney = BigDecimal.ZERO;
		for (FbsBudgetSuperaddDetailVO dtl : listSuperAddDtl) {
//			FbsItemVO item = fbsItemService.getFbsItemVO(null, dtl.getItemCode());
			FbsItemVO item = fbsItemVOHashMap.get(dtl.getItemCode());
			fbsBudgetSuperaddService.caluteRelation(dtl, dtlMap, item);//科目公式计算
			fbsBudgetSuperaddDetailService.updateSuperMoneyBySuperaddId(dtl);
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getBsuperMoney();
			}
		}
		superAdd.setCheckMoney(checkMoney);
		fbsBudgetSuperaddService.saveOrUpdate(superAdd);
		//循环设置汇总单位计划上报明细
		for (FbsBudgetDtlAbsVO dtl : listDtl) {
			for (FbsBudgetSuperaddDetailVO superAddDtl : listSuperAddDtl) {
				if (dtl.getItemCode().equals(superAddDtl.getItemCode())) {
					dtl.setBtempMoney(superAddDtl.getBsuperMoney());
				}
			}
			FbsBudgetDtlAbs entity = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(budget.getTimeType());
			BeanUtils.copyNotNullProperties(dtl, entity);
			fbsBudgetDtlAbsService.saveOrUpdate(entity);
		}
		//生成日志
		fbsMessageService.saveMessage(superAdd.getId(), FbsMessageConstant.MESSAGE_TYPE_COLLECT, "", FbsMessageConstant.BUS_NAME_SUPERADD, user);
		return childBudgetList;
	}
	/**
	 * 计算预算科目(detail)的上报值
	 *
	 * @param dtl
	 * @param dtlMap
	 * @param moneyName 计算金额
	 * @return
	 */
	public void caluteRelation(FbsBudgetDtlAbsVO dtl, HashMap<String, FbsBudgetDtlAbsVO> dtlMap, String moneyName, Map<String, FbsItem> fbsItemMap) {
		//获取科目计算公式
		String itemRelation = dtl.getItem().getItemRelation();
		if (StringUtils.isNotEmpty(itemRelation)) { //当存在自定义科目关系时，计算科目科目金额
			String itemRelation1 = itemRelation.replace("+", ";");
			itemRelation1 = itemRelation1.replace("-", ";");
			itemRelation1 = itemRelation1.replace("*", ";");
			itemRelation1 = itemRelation1.replace("/", ";");
			String[] itemCodes = itemRelation1.split(";");
			String itemRelation2 = dtl.getItem().getItemRelation();
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
							if (BudgetConstant.CAN_BMONEY.equals(moneyName)) {
								bMoneySuper = dtlMap.get(itemCode).getCanBMoney().multiply(new BigDecimal(fbsItemMap.get(itemCode).getSign()));
							} else if (BudgetConstant.REMAIN_BMONEY.equals(moneyName)) {
								bMoneySuper = dtlMap.get(itemCode).getRemainBMoney().multiply(new BigDecimal(fbsItemMap.get(itemCode).getSign()));
							} else {
								bMoneySuper = dtlMap.get(itemCode).getbMoney().multiply(new BigDecimal(fbsItemMap.get(itemCode).getSign()));
							}
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
						if (BudgetConstant.CAN_BMONEY.equals(moneyName)) {
							bMoneySuperNext = dtlMap.get(itemCodeNext).getCanBMoney().multiply(new BigDecimal(fbsItemMap.get(itemCodeNext).getSign()));
						} else if (BudgetConstant.REMAIN_BMONEY.equals(moneyName)) {
							bMoneySuperNext = dtlMap.get(itemCodeNext).getRemainBMoney().multiply(new BigDecimal(fbsItemMap.get(itemCodeNext).getSign()));
						} else {
							bMoneySuperNext = dtlMap.get(itemCodeNext).getbMoney().multiply(new BigDecimal(fbsItemMap.get(itemCodeNext).getSign()));
						}
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
			if (BudgetConstant.CAN_BMONEY.equals(moneyName)) {
				dtl.setCanBMoney(bMoneyTotal);
			} else if (BudgetConstant.REMAIN_BMONEY.equals(moneyName)) {
				dtl.setRemainBMoney(bMoneyTotal);
			} else {
				dtl.setbMoney(bMoneyTotal);
			}
		}
	}
	/**
	 * 初始化计划追加
	 *
	 * @param budget
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public FbsBudgetVO initBudgetSuperadd(FbsBudgetVO budget, ICurrentUser user) {
		// 获取计划追加信息
		FbsBudgetSuperadd budgetSuperadd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budget.getId());
		if (!EmptyUtils.isEmpty(budgetSuperadd)) {
			budget.setSuperaddId(budgetSuperadd.getId());
		}
		// 获取管理的单位
		List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(budget.getCorpId());
		if (ObjectUtil.isNotEmpty(list)) {
			// 获取虚拟单位信息
			FbsCollectCorpVO collectCorp = fbsCollectCorpDao.listCorpByCorpId(budget.getCorpId());
			CorpDTO corpDTO = corpService.get(budget.getCorpId());
			if (ObjectUtil.isNotEmpty(corpDTO)){
				String collectCorpId;
				if (ObjectUtil.isEmpty(collectCorp)) {
					// 初始化虚拟单位
					FbsCollectCorp initCorp = initCollectCorp(corpDTO);
					collectCorpId = initCorp.getId();
				} else {
					collectCorpId = collectCorp.getId();
				}
				// 初始化虚拟单位数据上报
				FbsBudgetVO fbsBudget = initBudget(collectCorpId, budget.getTimeType(), budget.getBeginDate(), budget.getEndDate(), budget.getStatus(), user);
				logger.info("初始化虚拟单位【{}】数据上报【{}】", collectCorpId, fbsBudget.getId());
			}
		}
		if (ObjectUtil.isNotEmpty(budget.getSuperaddId())) {
			budgetSuperadd = fbsBudgetSuperaddService.get(budget.getSuperaddId(), true);
			budget.setAttachmentId(budgetSuperadd.getAttachmentId());//追加附件
			//获取计划追加明细
			List<FbsBudgetSuperaddDetail> dtlList = fbsBudgetSuperaddDetailService.listBySuperaddId(budgetSuperadd.getId());
			HashMap<String, FbsBudgetSuperaddDetail> dtlMap = new HashMap<String, FbsBudgetSuperaddDetail>();
			if (!EmptyUtils.isEmptyList(dtlList)) {
				for (FbsBudgetSuperaddDetail dtl : dtlList) {
					dtlMap.put(dtl.getItemCode(), dtl);
				}
				//设置明细ID
				for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
					dtl.setSuperaddDtlId(dtlMap.get(dtl.getItemCode()).getId());
					dtl.setRmk(dtlMap.get(dtl.getItemCode()).getRmk());
				}
			}
		} else {
			//初始化追加
			budgetSuperadd = new FbsBudgetSuperadd();
			budgetSuperadd.setCorpId(budget.getCorpId());
			budgetSuperadd.setBudgetId(budget.getId());
			budgetSuperadd.setCheckMoney(BigDecimal.ZERO);
			budgetSuperadd.setStatus(BudgetConstant.STATUE_TEMP);
			budgetSuperadd.setCreateBy(user.getLoginName());
			budgetSuperadd.setCreateDate(DateUtils.getDate());
			fbsBudgetSuperaddService.saveOrUpdate(budgetSuperadd);
			//设置追加ID
			budget.setSuperaddId(budgetSuperadd.getId());
			//生成日志
			fbsMessageService.saveMessage(budgetSuperadd.getId(), FbsMessageConstant.MESSAGE_TYPE_INIT, FbsMessageConstant.FBS_LOG_INIT_SUCC, FbsMessageConstant.BUS_NAME_SUPERADD, user);
			//初始化追加明细
			if (!EmptyUtils.isEmptyList(budget.getDtlList())) {
				for (FbsBudgetDtlAbsVO dtl : budget.getDtlList()) {
					FbsBudgetSuperaddDetail superaddDtl = new FbsBudgetSuperaddDetail();
					superaddDtl.setSuperaddId(budgetSuperadd.getId());
					superaddDtl.setItemCode(dtl.getItemCode());
					superaddDtl.setItemName(dtl.getItemName());
					superaddDtl.setBsuperMoney(BigDecimal.ZERO);
					fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
					//设置明细ID
					dtl.setSuperaddDtlId(superaddDtl.getId());
					dtl.setRmk("");
				}
			}
			//更新计划上报状态
			FbsBudget entity = this.get(budget.getId(), true);
			entity.setStatus(BudgetConstant.STATUE_SUPERADD_TEMP);
			this.saveOrUpdate(entity);
		}
		return budget;
	}
	/**
	 * 资金计划追加保存
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAdd(FbsBudgetInputVO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.get(input.getId(), true);
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
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(budget);
		if (!EmptyUtils.isEmpty(preCorpBudget)) {
			entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);
		} else {
			entity.setStatus(input.getStatus());
		}
		entity.setAttachmentId(input.getAttachmentId());
		boolean isEdit = EmptyUtils.isBlank(entity.getUpdateBy());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		List<FbsBudgetSuperaddDetail> dtlList = new ArrayList<FbsBudgetSuperaddDetail>();
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//更新计划上报明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			dtl.setBtempMoney(input.getBtempMoney()[i]);//更新追加金额
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getBtempMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
			//新增或更新追加明细表
			FbsBudgetSuperaddDetail superaddDtl = fbsBudgetSuperaddDetailService.get(input.getSuperaddDtlId()[i], true);
			superaddDtl.setSuperaddId(entity.getId());
			superaddDtl.setItemCode(dtl.getItemCode());
			superaddDtl.setItemName(dtl.getItemName());
			superaddDtl.setBsuperMoney(input.getBtempMoney()[i]);
			superaddDtl.setRmk(input.getRmk()[i]);
			fbsBudgetSuperaddDetailService.saveOrUpdate(superaddDtl);
			dtlList.add(superaddDtl);
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
			superaddVO.setCorpName(budget.getCorpName());
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
	 * 审批时资金计划追加保存
	 *
	 * @param input
	 */
	public void saveFbsBudgetSuperAddForApprove(FbsBudgetInputVO input, ICurrentUser user) {
		//更新上报状态为计划追加
		FbsBudget budget = this.get(input.getId(), true);
		if (BudgetConstant.STATUE_TEMP.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_TEMP);//计划追加录入
		} else if (BudgetConstant.STATUE_WAIT_CHECK.equals(input.getStatus())) {
			budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_CHECK);//计划追加审批中
		}
		budget.setUpdateBy(user.getLoginName());
		budget.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(budget);
		//新增或更新追加表
		FbsBudgetSuperadd entity = fbsBudgetSuperaddService.get(input.getSuperaddId());
		entity.setStatus(input.getStatus());
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		fbsBudgetSuperaddService.saveOrUpdate(entity);
		List<FbsBudgetSuperaddDetail> dtlList = new ArrayList<FbsBudgetSuperaddDetail>();
		BigDecimal checkMoney = BigDecimal.ZERO;
		List<FbsBudgetDtlAbs> fbsBudgetDtlAbsList = new ArrayList<>();
		for (int i = 0; i < input.getDetailId().length; i++) {
			//更新计划上报明细
			FbsBudgetDtlAbs dtl = fbsBudgetDtlAbsService.getById(input.getDetailId()[i], budget.getTimeType());
			dtl.setBtempMoney(input.getBtempMoney()[i]);//更新追加金额
			if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
				checkMoney = dtl.getbMoney();
			}
			fbsBudgetDtlAbsList.add(dtl);
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
			} else {
				return;
			}
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(entity.getCorpId());
			//是否存在虚拟单位
			FbsCollectCorpVO vo = fbsCollectCorpDao.listCorpByCorpId(entity.getCorpId());
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
					FbsBudget budgetTemp = this.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
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
									//汇总中状态的下级单位状态更新追加金额
									fbsBudgetDtlAbsService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
									// 更新明细录入的追加金额 zuoy 2018-11-20
									fbsBudgetDetailService.setSuperaddMoney(budgetTemp.getId(), budgetTemp.getTimeType());
									//生成日志
									fbsMessageService.saveMessage(budgetTemp.getId(), FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS, FbsMessageConstant.FBS_LOG_COLLECT_APP_SUCC, FbsMessageConstant.BUS_NAME_SUPERADD, user);
								}
								//更新计划上报状态
								budgetTemp.setStatus(status);
								this.saveOrUpdate(budgetTemp);
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
		this.saveOrUpdate(budget);
		fbsBudgetDtlAbsService.clearSuperaddTempMoney(budget.getId(), budget.getTimeType());
		//获取资金计划追加信息
		FbsBudgetSuperadd superAdd = fbsBudgetSuperaddService.get(superaddId, true);
		CorpDTO corp = corpService.get(budget.getCorpId());
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
					FbsBudget budgetTemp = this.getCorpBudget(dto.getCorpId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
					if (budgetTemp != null && budgetTemp.getStatus() >= BudgetConstant.STATUE_SUPERADD_WAIT_COLLECTED) {
						budgetTemp.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT);
						this.saveOrUpdate(budgetTemp);
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

		// 删除汇总单位需要删除本身虚拟单位
		FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(budget.getCorpId());
		if (ObjectUtil.isNotEmpty(fbsCollectCorpVO)) {
			FbsBudget budgetTemp = this.getCorpBudget(fbsCollectCorpVO.getId(), budget.getTimeType(), budget.getBeginDate(), budget.getEndDate());
			if (ObjectUtil.isNotEmpty(budgetTemp)) {
				FbsBudgetSuperadd budgetSuperadd = fbsBudgetSuperaddService.getLastSuperaddByBudgetId(budgetTemp.getId());
				if (ObjectUtil.isNotEmpty(budgetSuperadd)) {
					logger.info("删除汇总单位【{}】需要删除本身虚拟单位",fbsCollectCorpVO.getCorpId());
					deleteFbsBudgetSuperAdd(budgetTemp, budgetSuperadd.getId(), user);
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
			if (this.fbsCorpBudgetService.getCollectFlag(entity.getCorpId())) {
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
			} else {
				entity.setStatus(BudgetConstant.STATUE_CHECK);//计划编制完成
			}
			//如果是汇总单位虚拟单位需要汇总
			FbsCollectCorpVO fbsCollectCorpVO = this.listCorpById(entity.getCorpId());
			if(!EmptyUtils.isEmpty(fbsCollectCorpVO)){
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
			}
		} else {
			entity.setStatus(BudgetConstant.STATUE_BACK);//打回
			messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		}
		this.saveOrUpdate(entity);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(entity, user);
		String message = result ? FbsMessageConstant.FBS_LOG_APP_SUCC : FbsMessageConstant.FBS_LOG_APP_BACK;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, message, FbsMessageConstant.BUS_NAME_INPUT, user);
	}
	/**
	 * 计划上报-审批通过
	 *
	 * @param entity
	 */
	public void approvePassEndSuper(FbsBudgetSuperadd entity, boolean result, ICurrentUser user) {
		//获取对应的资金计划上报信息
		FbsBudget budget = this.get(entity.getBudgetId(), true);
		Integer messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS;
		if (result) {
			//如果是汇总单位虚拟单位需要汇总
			FbsCollectCorpVO fbsCollectCorpVO = this.listCorpById(entity.getCorpId());
			//判断单位是否需要汇总
			if (this.fbsCorpBudgetService.getCollectFlag(entity.getCorpId()) || !EmptyUtils.isEmpty(fbsCollectCorpVO)) {
				entity.setStatus(BudgetConstant.STATUE_WAIT_COLLECT);//待汇总
				budget.setStatus(BudgetConstant.STATUE_SUPERADD_WAIT_COLLECT);
			} else {
				entity.setStatus(BudgetConstant.STATUE_CHECK);//计划编制完成
				budget.setStatus(BudgetConstant.STATUE_CHECK);
				//更新上报明细的追加金额
				fbsBudgetDtlAbsService.setSuperaddMoney(budget.getId(), budget.getTimeType());
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
		this.saveOrUpdate(budget);
		//下级单位计划更新
		this.saveFbsBudgetCollectUnderCorp(budget, entity, user);
		String message = result ? FbsMessageConstant.FBS_LOG_APP_SUCC : FbsMessageConstant.FBS_LOG_APP_BACK;
		//生成日志
		fbsMessageService.saveMessage(entity.getId(), messageType, message, FbsMessageConstant.BUS_NAME_SUPERADD, user);
	}
	/**
	 * 根据单位ID和周期类型获取往期上报完成的计划
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudget> getPreCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		return fbsBudgetDao.getPreCorpBudget(corpId, timeType, beginDate, endDate);
	}
	/**
	 * 可上报/可追加金额计算
	 *
	 * @param fbsBudget
	 * @param target    input-上报  super-追加
	 */
	public void calculateCanBMoney(FbsBudgetVO fbsBudget, String target) {
		//父执行金额
		Map<String, BigDecimal> parentProcessMap = new HashMap<>();
		//本期执行金额
		Map<String, BigDecimal> curProcessMap = new HashMap<>();
		//追加时，获取留存金额
		if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				//获取科目执行金额
				BigDecimal processMoney = fbsBudgetProcessService.collectProcessMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
				curProcessMap.put( dtl.getItemCode(),processMoney);
				dtl.setRemainBMoney(dtl.getbMoney().add(dtl.getBsuperMoney()).subtract(processMoney));//留存金额 = 上报金额+追加金额-已执行金额
			}
		}
		//不存在父计划，不计算可上报/可追加金额计算
		if (StringUtils.isEmpty(fbsBudget.getParentTimeType())) {
			return;
		}
		//获取父计划周期开始
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudget.getBeginDate(), fbsBudget.getParentTimeType());
		//获取父计划
		FbsBudget parentBudget = this.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getParentTimeType(), fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		if (parentBudget == null || parentBudget.getStatus() < BudgetConstant.STATUE_CHECK) {
			return;
		}
		//获取父计划明细
		List<FbsBudgetDtlAbsVO> parentDtlList = fbsBudgetDtlAbsService.listByBudgetId(parentBudget.getId(), parentBudget.getTimeType());
		HashMap<String, BigDecimal> parentDtlMap = null;
		//上期计划余额下期是否可用：0-否 1-是
		//选择是时：可上报金额=父上报-父已执行（只适合本期上报） ；可上报金额=父上报-父已执行（不含本期）-本期上报；选择否时：可上报金额=父上报-子已上报
		String useBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_USEBALANCE).getValue();
		if ("0".equals(useBalance)) {
			//设置父计划上报金额
			parentDtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(parentDtlList)) {
				for (FbsBudgetDtlAbsVO dtl : parentDtlList) {
					parentDtlMap.put(dtl.getItemCode(), dtl.getbMoney().add(dtl.getBsuperMoney()));//上报总金额 = 上报金额+追加金额（已通过）
				}
			}
			//获取往期上报计划
			List<FbsBudget> preBudgetList = fbsBudgetDao.getPreCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), parentBudget.getBeginDate(), parentBudget.getEndDate());
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
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				BigDecimal parentBMoney = parentDtlMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : parentDtlMap.get(dtl.getItemCode());
				BigDecimal preBMoney = preDtlMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : preDtlMap.get(dtl.getItemCode());
				dtl.setCanBMoney(parentBMoney.subtract(preBMoney));
			}
		} else {
			//设置父计划执行金额
			parentDtlMap = new HashMap<String, BigDecimal>();
			if (!EmptyUtils.isEmptyList(parentDtlList)) {
				for (FbsBudgetDtlAbsVO dtl : parentDtlList) {
					//获取科目执行金额
					BigDecimal processMoney = fbsBudgetProcessService.collectProcessMoney(parentBudget.getBeginDate(), parentBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
					parentProcessMap.put(dtl.getItemCode(),processMoney);
					parentDtlMap.put(dtl.getItemCode(), dtl.getbMoney().add(dtl.getBsuperMoney()).subtract(processMoney));//可上报金额=父上报-父已执行（只适合本期上报）
					//追加时，可上报金额=父上报-父已执行（不包含本期执行）-本期已上报
					if ("super".equals(target)) {
						//获取本期计划周期
//						FbsBudgetPeriodVO curBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), fbsBudget.getTimeType());
						//获取本期已执行
//						BigDecimal curProcessMoney = fbsBudgetProcessService.collectProcessMoney(curBudgetPeriod.getBeginDate(), curBudgetPeriod.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
						BigDecimal curProcessMoney = curProcessMap.get(dtl.getItemCode());
						parentDtlMap.put(dtl.getItemCode(), parentDtlMap.get(dtl.getItemCode()).add(curProcessMoney));
					}
				}
			}
			//判断是否是本期报下期或下下期（如：9月份报10月计划或周整月上报这种情况）
			if (fbsBudget.getBeginDate().compareTo(DateUtils.getDate()) > 0) {
				//获取本期计划周期
				FbsBudgetPeriodVO curBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), fbsBudget.getTimeType());
				//获取本期及上报周期之间上报的计划
				List<FbsBudget> preBudgetList = null;
				//如果本期不包含在父周期里面（周整月上报时，本期月报下期月里面的周计划）
				if (curBudgetPeriod.getEndDate().compareTo(parentBudget.getBeginDate()) < 0) {
					preBudgetList = fbsBudgetDao.getPreCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), parentBudget.getBeginDate(), parentBudget.getEndDate());
				} else {
					preBudgetList = fbsBudgetDao.getPreCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), curBudgetPeriod.getBeginDate(), parentBudget.getEndDate());
				}
				List<String> preBudgetIds = new ArrayList<String>();
				//获取下级单位计划ID集合
				if (!EmptyUtils.isEmptyList(preBudgetList)) {
					for (FbsBudget budget : preBudgetList) {
						preBudgetIds.add(budget.getId());
					}
				}
				String budgetAuthSql = SqlUtil.getInSql(preBudgetIds, "b.budget_id", true);
				List<FbsBudgetDtlAbsVO> preDtlist = fbsBudgetDtlAbsService.collectByBudgetIdsNormal(fbsBudget.getTimeType(), budgetAuthSql);
				if (!EmptyUtils.isEmptyList(preDtlist)) {
					for (FbsBudgetDtlAbsVO dtl : preDtlist) {
						//获取科目执行金额
						//如果本期不包含在父周期里面（周整月上报时，本期月报下期月里面的周计划）
						BigDecimal processMoney;
						if (curBudgetPeriod.getEndDate().compareTo(parentBudget.getBeginDate()) < 0) {
//							processMoney = fbsBudgetProcessService.collectProcessMoney(parentBudget.getBeginDate(), parentBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
							processMoney = parentProcessMap.get(dtl.getItemCode());
						} else {
//							processMoney = fbsBudgetProcessService.collectProcessMoney(curBudgetPeriod.getBeginDate(), parentBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId());
							processMoney = curProcessMap.get(dtl.getItemCode());
						}
						if (processMoney == null) {
							processMoney = BigDecimal.ZERO;
						}
						BigDecimal bMoney = dtl.getbMoney().add(dtl.getBsuperMoney());
						BigDecimal parentBMoney = EmptyUtils.isEmpty(parentDtlMap.get(dtl.getItemCode())) ? BigDecimal.ZERO : parentDtlMap.get(dtl.getItemCode());
						parentDtlMap.put(dtl.getItemCode(), parentBMoney.add(processMoney).subtract(bMoney));//可上报金额=父上报-父已执行（不含本期）-本期上报
					}
				}
			}
			HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				BigDecimal parentBMoney = parentDtlMap.get(dtl.getItemCode()) == null ? BigDecimal.ZERO : parentDtlMap.get(dtl.getItemCode());
				dtl.setCanBMoney(parentBMoney);
				//追加时，可上报金额=父上报-父已执行（不含本期）-本期上报
				if ("super".equals(target)) {
					dtl.setCanBMoney(parentBMoney.subtract(dtl.getbMoney()).subtract(dtl.getBsuperMoney()));//减去本期已上报
				}
				dtlMap.put(dtl.getItemCode(), dtl);
			}
			List<FbsItem> fbsItemList = fbsItemService.getAllFbsItem();
			Map<String, FbsItem> fbsItemMap = new HashMap<>();
			for(FbsItem fbsItem : fbsItemList){
				fbsItemMap.put(fbsItem.getItemCode(),fbsItem);
			}
			Map<String, FbsItemVO> fbsItemVOMap = fbsItemService.getAllItemMap();
			//公式计算
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				dtl.setItem(fbsItemVOMap.get(dtl.getItemCode()));
				this.caluteRelation(dtl, dtlMap, BudgetConstant.CAN_BMONEY,fbsItemMap);//科目公式计算
				if ("super".equals(target)) {
					this.caluteRelation(dtl, dtlMap, BudgetConstant.REMAIN_BMONEY,fbsItemMap);//科目公式计算
				}
			}
		}
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		String sign = FbsWorkFlowConstant.FBS_BUDGET_INPUT;
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "fbs.id", true);
		DataTablesOutput dp = fbsBudgetDao.dataTableApprove(dataTablesInput, businessSql);
		for (int i = 0; i < dp.getData().size(); i++) {
			FbsBudgetVO vo = (FbsBudgetVO) dp.getData().get(i);
			String timeType = vo.getTimeType();
			if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
				timeType = BudgetConstant.TIME_TYPE_MONTH;
			}
			FbsBudgetPeriodVO curvo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			FbsBudgetPeriodVO nextvo = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			if (vo.getBeginDate().getTime() >= curvo.getBeginDate().getTime()
					&& vo.getEndDate().getTime() <= curvo.getEndDate().getTime()) {
				vo.setPreiodNameTemp(BudgetConstant.TIME_TYPE_PREIODNAME_CUR);
			} else if (vo.getBeginDate().getTime() >= nextvo.getBeginDate().getTime()
					&& vo.getEndDate().getTime() <= nextvo.getEndDate().getTime()) {
				vo.setPreiodNameTemp(BudgetConstant.TIME_TYPE_PREIODNAME_NEXT);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 加载追加审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableSuperAddApprove(DataTablesInput dataTablesInput) {
		String sign = FbsWorkFlowConstant.FBS_BUDGET_SUPERADD;
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "super.id", true);
		DataTablesOutput dp = fbsBudgetDao.dataTableSuperAddApprove(dataTablesInput, businessSql);
		for (int i = 0; i < dp.getData().size(); i++) {
			FbsBudgetSuperAddApproveVO vo = (FbsBudgetSuperAddApproveVO) dp.getData().get(i);
			String timeType = vo.getTimeType();
			if (BudgetConstant.TIME_TYPE_WEEK.equals(timeType)) {
				timeType = BudgetConstant.TIME_TYPE_MONTH;
			}
			FbsBudgetPeriodVO curvo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			FbsBudgetPeriodVO nextvo = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			if (vo.getBeginDate().getTime() >= curvo.getBeginDate().getTime()
					&& vo.getEndDate().getTime() <= curvo.getEndDate().getTime()) {
				vo.setPreiodNameTemp(BudgetConstant.TIME_TYPE_PREIODNAME_CUR);
			} else if (vo.getBeginDate().getTime() >= nextvo.getBeginDate().getTime()
					&& vo.getEndDate().getTime() <= nextvo.getEndDate().getTime()) {
				vo.setPreiodNameTemp(BudgetConstant.TIME_TYPE_PREIODNAME_NEXT);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 资金计划汇总提交，检查下级单位存在待审批状态的计划单
	 *
	 * @param id
	 */
	public boolean checkChildCollectCorpStatus(String id) {
		FbsBudget entity = this.get(id);
		boolean flag = false;
		//判断是否是汇总单位提交资金计划
		if (this.fbsCorpBudgetService.getChildCollectFlag(entity.getCorpId())) {
			//获取下级单位
			List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(entity.getCorpId());
			if (!EmptyUtils.isEmptyList(list)) {
				for (FbsCorpBudgetDTO corpBudgetDTO : list) {
					FbsBudget fbsBudget = this.getCorpBudget(corpBudgetDTO.getCorpId(), entity.getTimeType(), entity.getBeginDate(), entity.getEndDate());
					if (!EmptyUtils.isEmpty(fbsBudget)) {
						if (fbsBudget.getStatus().intValue() >= BudgetConstant.STATUE_WAIT_CHECK && fbsBudget.getStatus().intValue() < BudgetConstant.STATUE_WAIT_COLLECT) {
							flag = true;
							break;
						}
						if (fbsBudget.getStatus().intValue() == BudgetConstant.STATUE_SUPERADD_WAIT_CHECK) {
							flag = true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginStartDate
	 * @param beginEndDate
	 * @return
	 */
	public List<FbsBudget> listBudgetByBeginDate(String corpId, String timeType, Date beginStartDate, Date beginEndDate) {
		return fbsBudgetDao.listBudgetByBeginDate(corpId, timeType, beginStartDate, beginEndDate, null);
	}
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginStartDate
	 * @param beginEndDate
	 * @return
	 */
	public List<FbsBudget> listBudgetByBeginDate(String corpId, String timeType, Date beginStartDate, Date beginEndDate, String budgetId) {
		return fbsBudgetDao.listBudgetByBeginDate(corpId, timeType, beginStartDate, beginEndDate, budgetId);
	}
	/**
	 * app审批挂接计划执行数据
	 *
	 * @param billNo
	 * @param billId
	 * @param businessName
	 * @param processSysname
	 * @param corpId
	 * @param processDate
	 * @return
	 */
	public List<FbsBudgetProcessCommonVO> listFbsBudgetProcessByBusiness(String billNo, String billId, String businessName, String processSysname, String corpId, Date processDate) {
		List<FbsBudgetProcessCommonVO> voList = null;
		try {
			List<FbsBudgetProcess> list = null;
			if (!StringUtils.isBlank(billId)) {
				list = this.fbsBudgetProcessService.getBudgetProcessByBillId(businessName, processSysname, billId);
			} else {
				list = this.fbsBudgetProcessService.getBudgetProcessByBillNo(businessName, processSysname, billNo);
			}
			if (!EmptyUtils.isEmpty(list)) {
				voList = new ArrayList<>();
				for (FbsBudgetProcess fbsBudgetProcess : list) {
					FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = this.getFbsBudgetProcessByItemCode(corpId, fbsBudgetProcess.getItemCode(), DateUtils.parseDate(processDate), null, null, null);
					fbsBudgetProcessCommonVO.setItemName(fbsBudgetProcess.getItemName());
					fbsBudgetProcessCommonVO.setProcessMoney(fbsBudgetProcess.getProcessMoney());
					voList.add(fbsBudgetProcessCommonVO);
				}
			}
			return voList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voList;
	}
	/**
	 * 汇总
	 *
	 * @param fbsBudgetAnalyse
	 * @param childIds
	 */
	public void saveFact(FbsBudgetAnalyseVO fbsBudgetAnalyse, List<String> childIds, List<String> diffReasons, List<String> factMoneys) {
		for (int i = 0; i < childIds.size(); i++) {
			FbsBudgetDtlAbs dtl = this.fbsBudgetDtlAbsService.getById(childIds.get(i), fbsBudgetAnalyse.getTimeType());
			dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_WAIT_CHECK);
			dtl.setDiffReason(diffReasons.get(i));
			dtl.setFactMoney(new BigDecimal(factMoneys.get(i)));
			fbsBudgetDtlAbsService.saveOrUpdate(dtl);
		}
	}
	/**
	 * 分析复核
	 *
	 * @param fbsBudgetAnalyse
	 * @param childIds
	 */
	public void saveCheckFact(FbsBudgetAnalyseVO fbsBudgetAnalyse, List<String> childIds, String passFlag) {
		for(int i=0; i<childIds.size(); i++){
			FbsBudgetDtlAbs dtl = this.fbsBudgetDtlAbsService.getById(childIds.get(i), fbsBudgetAnalyse.getTimeType());
			if("0".equals(passFlag)) {
				dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_CHECK);//审核通过
			}else{
				dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_BACK);//审核打回
			}
			fbsBudgetDtlAbsService.saveOrUpdate(dtl);
		}
	}
	/**
	 * 获取资金计划参数 明细项控制模式参数
	 * 从参数读到需要控制的列
	 *
	 * @return
	 * @author zuoy 2018-11-14
	 */
	public String getColumnsByDtlColSel() {
		String dtlColSel = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOLSEL).getValue();
		dtlColSel = dtlColSel.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		return dtlColSel;
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @return
	 */
	public List<FbsBudgetProcessCommonVO> listFbsBudgetProcessCommonVOInfo(FbsBudgetProjectDetailVO projectDetailVO, Model model) {
		String businessName = projectDetailVO.getBusinessName();
		String processSysname = projectDetailVO.getProcessSysname();
		String corpId = projectDetailVO.getCorpId();
		String itemCode = projectDetailVO.getItemCode();
		Date processDate = projectDetailVO.getProcessDate();
		String billNo = projectDetailVO.getBillNo();
		String billId = projectDetailVO.getBillId();
		String id = projectDetailVO.getId();
		String curId = projectDetailVO.getCurId();
		List<FbsBudgetProcess> processList = this.fbsBudgetProcessService.getBudgetProcesInfo(businessName, processSysname, billNo, billId, id);
		if (EmptyUtils.isEmptyList(processList)) {
			if(EmptyUtils.isEmpty(projectDetailVO.getDefItemCode())){
				return new ArrayList<>();
			}
			List<FbsBudgetProcessCommonVO> list = new ArrayList<>();
			list.add(getFbsBudgetProcessByItemCode(corpId, projectDetailVO.getDefItemCode(), processDate, billNo, billId, curId));
			model.addAttribute("status", BudgetConstant.STATUE_TEMP);
			return list;
		}
		model.addAttribute("status", processList.get(0).getStatus());
		List<FbsBudgetProcessCommonVO> list = BeanUtils.copyListProperties(processList, FbsBudgetProcessCommonVO.class);
		for(FbsBudgetProcessCommonVO vo : list){
			FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = this.getFbsBudgetProcessByItemCode(corpId, vo.getItemCode(), processDate, billNo, billId, curId);
			vo.setItemMoney(fbsBudgetProcessCommonVO.getItemMoney());
			vo.setSuperItemMoney(fbsBudgetProcessCommonVO.getSuperItemMoney());
			vo.setTopItemMoney(fbsBudgetProcessCommonVO.getTopItemMoney());
			vo.setExchangeRate(fbsBudgetProcessCommonVO.getExchangeRate());
			vo.setItemId(fbsBudgetProcessCommonVO.getItemId());
			vo.setProcessRule(fbsBudgetProcessCommonVO.getProcessRule());
		}
		return list;
	}

	public List<FbsBudgetProcessCommonVO> listFbsBudgetProcessCommonVOInfo(FbsBudgetProjectDetailVO projectDetailVO, FbsBudgetProcessPageVO pageVO) {
		String businessName = projectDetailVO.getBusinessName();
		String processSysname = projectDetailVO.getProcessSysname();
		String corpId = projectDetailVO.getCorpId();
		String itemCode = projectDetailVO.getItemCode();
		Date processDate = projectDetailVO.getProcessDate();
		String billNo = projectDetailVO.getBillNo();
		String billId = projectDetailVO.getBillId();
		String id = projectDetailVO.getId();
		String curId = projectDetailVO.getCurId();
		List<FbsBudgetProcess> processList = this.fbsBudgetProcessService.getBudgetProcesInfo(businessName, processSysname, billNo, billId, id);
		if (EmptyUtils.isEmptyList(processList)) {
			if(EmptyUtils.isEmpty(projectDetailVO.getDefItemCode())){
				return new ArrayList<>();
			}
			List<FbsBudgetProcessCommonVO> list = new ArrayList<>();
			list.add(getFbsBudgetProcessByItemCode(corpId, projectDetailVO.getDefItemCode(), processDate, billNo, billId, curId));
			pageVO.setStatus(BudgetConstant.STATUE_TEMP);
			return list;
		}
		pageVO.setStatus(processList.get(0).getStatus());
		List<FbsBudgetProcessCommonVO> list = BeanUtils.copyListProperties(processList, FbsBudgetProcessCommonVO.class);
		Map<String,String> statusMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			FbsBudgetProcessCommonVO vo = list.get(i);
			FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = this.getFbsBudgetProcessByItemCode(corpId, vo.getItemCode(), processDate, billNo, billId, curId);
			vo.setItemMoney(fbsBudgetProcessCommonVO.getItemMoney());
			vo.setSuperItemMoney(fbsBudgetProcessCommonVO.getSuperItemMoney());
			vo.setTopItemMoney(fbsBudgetProcessCommonVO.getTopItemMoney());
			vo.setExchangeRate(fbsBudgetProcessCommonVO.getExchangeRate());
			vo.setItemId(fbsBudgetProcessCommonVO.getItemId());
			vo.setProcessRule(fbsBudgetProcessCommonVO.getProcessRule());
			if(StringUtils.isEmpty(vo.getStatus()) && processList.get(i).getStatus() != null){
				vo.setStatus(processList.get(i).getStatus().toString());
				if(statusMap.get(vo.getStatus()) == null){
					String statusStr = dictService.get("fbsBudgetProcess.status", vo.getStatus());
					statusMap.put(vo.getStatus(), statusStr);
				}
				vo.setStatusName(statusMap.get(vo.getStatus()));
			}
		}
		return list;
	}


	@Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有上报资金计划，不允许删除！
            int num = this.fbsBudgetDao.getBudgetCountByCorpId(corpId,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FBS,
                        SysConfigConstant.BusType.FBS_INPUT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DEL_INPUT_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有资金计划上报的在途数据，不允许禁用！
			int num = this.fbsBudgetDao.getBudgetCountByCorpId(corpId,"disable");
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_INPUT,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DISABLE_INPUT_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有资金计划上报的在途数据，不允许变更！
			int num = this.fbsBudgetDao.getBudgetCountByCorpId(corpId,"change");
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_INPUT,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_CHANGE_INPUT_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 手机端上报情况表
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetVO> getBudgetInputMoblieList(String corpId, String timeType, String beginDate, String endDate , String joinSql) {
		return this.fbsBudgetDao.getBudgetInputMoblieList(corpId,timeType,beginDate,endDate,joinSql);
	}
	/**
	 * 构建初始化计划上报数据
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudgetVO buildBudget(String corpId, String timeType, Date beginDate, Date endDate, boolean ruleFlag) {
		//初始化计划主表
		FbsBudgetVO fbsBudgetVO = null;
		if(ruleFlag){
			//获取单位周期计划
			FbsBudget budget = fbsBudgetDao.getCorpBudget(corpId, timeType, beginDate, endDate);
			//判断是否有启用的计划执行规则
			List<FbsProcessRuleVO> processRuleVOList = fbsProcessRuleService.listValidProcessRule();
			if (EmptyUtils.isNotEmpty(processRuleVOList)) {
				for(FbsProcessRuleVO processRule:processRuleVOList){
					if(BudgetConstant.PROCESS_RULE_1.equals(processRule.getProcessRule())){
						//1-临时资金计划
						//暂未规划，跳过
						continue;
					}else if(BudgetConstant.PROCESS_RULE_2.equals(processRule.getProcessRule())){
						//2-未编制完成计划
						if(budget == null){
							continue;
						}else{
							fbsBudgetVO = new FbsBudgetVO();
							BeanUtils.copyNotNullProperties(budget, fbsBudgetVO);
							fbsBudgetVO.setStatus(BudgetConstant.STATUE_CHECK);
							fbsBudgetVO.setProcessRule(BudgetConstant.PROCESS_RULE_2);
							fbsBudgetVO.setProcessRatio(processRule.getProcessRatio());
							break;
						}
					}else if(BudgetConstant.PROCESS_RULE_3.equals(processRule.getProcessRule())){
						//3-父周期计划
						//根据执行日期获取计划周期
						FbsBudgetPeriodVO fbsBudgetPeriodParent = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, this.getParentTimeTypeAll(timeType));
						//获取单位父周期计划
						FbsBudget budgetParent = fbsBudgetDao.getCorpBudget(corpId, this.getParentTimeTypeAll(timeType), fbsBudgetPeriodParent.getBeginDate(), fbsBudgetPeriodParent.getEndDate());
						if(budgetParent == null || budgetParent.getStatus()< BudgetConstant.STATUE_CHECK){
							continue;
						}else{
							fbsBudgetVO = new FbsBudgetVO();
							BeanUtils.copyNotNullProperties(budgetParent, fbsBudgetVO);
							fbsBudgetVO.setProcessRule(BudgetConstant.PROCESS_RULE_3);
							fbsBudgetVO.setProcessRatio(processRule.getProcessRatio());
							break;
						}
					}else if(BudgetConstant.PROCESS_RULE_4.equals(processRule.getProcessRule())){
						//4-上期资金计划比例
						//根据执行日期获取计划周期
						FbsBudgetPeriodVO fbsBudgetPeriodPre = fbsBudgetPeriodService.getSuperBudgetPeriodByTimeType(beginDate, timeType);
						//获取单位父周期计划
						FbsBudget budgetPre = fbsBudgetDao.getCorpBudget(corpId, timeType, fbsBudgetPeriodPre.getBeginDate(), fbsBudgetPeriodPre.getEndDate());
						if(budgetPre == null || budgetPre.getStatus()< BudgetConstant.STATUE_CHECK){
							continue;
						}else{
							fbsBudgetVO = new FbsBudgetVO();
							BeanUtils.copyNotNullProperties(budgetPre, fbsBudgetVO);
							fbsBudgetVO.setProcessRule(BudgetConstant.PROCESS_RULE_4);
							fbsBudgetVO.setProcessRatio(processRule.getProcessRatio());
							break;
						}
					}else if(BudgetConstant.PROCESS_RULE_5.equals(processRule.getProcessRule())){
						//5-先执行不控制
						fbsBudgetVO = this.buildBudget(corpId, timeType, beginDate, endDate, false);
						fbsBudgetVO.setProcessRule(BudgetConstant.PROCESS_RULE_5);
						break;
					}
				}
			}
		}else {
			fbsBudgetVO = new FbsBudgetVO();
			fbsBudgetVO.setCorpId(corpId);
			fbsBudgetVO.setEnrolDate(DateUtils.getDate());
			fbsBudgetVO.setBeginDate(beginDate);
			fbsBudgetVO.setEndDate(endDate);
			fbsBudgetVO.setTimeType(timeType);
			fbsBudgetVO.setCheckMoney(BigDecimal.ZERO);
			fbsBudgetVO.setStatus(BudgetConstant.STATUE_CHECK);
			//判断单位是否是汇总单位，汇总单位币别固定本位币（顶级网点/单位的本位币）
			if (fbsCorpBudgetService.getChildCollectFlag(corpId)) {
				//获取顶级网点/单位
				CorpDTO topCorp = corpService.getTopCorpByCorpId(corpId);
				fbsBudgetVO.setCurId(topCorp.getCurId());
				fbsBudgetVO.setExchangeRate(BigDecimal.ONE);
			} else {
				//获取已上报的币别
				List<FbsBudget> budgets = fbsBudgetDao.listBudgetByBeginDate(corpId, null, DateUtils.getFirstYearDate(beginDate), DateUtils.getLastYearDate(beginDate), null);
				if (!EmptyUtils.isEmptyList(budgets)) {
					fbsBudgetVO.setCurId(budgets.get(0).getCurId());
					fbsBudgetVO.setExchangeRate(budgets.get(0).getExchangeRate());
				} else {
					//获取单位本身的本位币
					CorpDTO corpDTO = corpService.get(corpId);
					fbsBudgetVO.setCurId(corpDTO.getCurId());
					//获取顶级单位/网点
					CorpDTO topCorp = corpService.getTopCorpByCorpId(corpId);
					BigDecimal exChangeRate = BigDecimal.ONE;
					if (topCorp != null && !corpDTO.getCurId().equals(topCorp.getCurId())) {
						exChangeRate = fcExcrateAdjustService.getExchangeRateBy(corpDTO.getCurId(), topCorp.getCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
					}
					fbsBudgetVO.setExchangeRate(exChangeRate);
				}
			}
			//获取单位分配的科目
			List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(corpId);
			//初始化明细表
			List<FbsBudgetDtlAbsVO> dtlList = new ArrayList<FbsBudgetDtlAbsVO>();
			HashMap<String, FbsBudgetDtlAbsVO> dtlMap = new HashMap<String, FbsBudgetDtlAbsVO>();
			if (!EmptyUtils.isEmptyList(listFbsItem)) {
				for (FbsItemVO item : listFbsItem) {
					FbsBudgetDtlAbs dtl = FbsBudgetDtlAbs.getFbsBudgetDtlByTimeType(timeType);
					dtl.setBudgetId(fbsBudgetVO.getId());
					dtl.setItemCode(item.getItemCode());
					dtl.setItemName(item.getItemName());
					dtl.setbMoney(BigDecimal.ZERO);
					dtl.setBsuperMoney(BigDecimal.ZERO);
					dtl.setBtempMoney(BigDecimal.ZERO);
				}
			}
		}
		return fbsBudgetVO;
	}
	/**
	 * 根据id获取虚拟单位
	 * @param id
	 * @return
	 */
	public FbsCollectCorpVO listCorpById(String id) {
		return this.fbsCollectCorpDao.listCorpById(id);
	}
	/**
	 * 根据corpID获取虚拟单位
	 * @param id
	 * @return
	 */
	public FbsCollectCorpVO listCorpByCorpId(String id) {
		return this.fbsCollectCorpDao.listCorpByCorpId(id);
	}
	/**
	 * 获取汇总单位本单位计划
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @return
	 */
	public FbsBudget getCollectCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		FbsBudget budget = null;
		FbsCollectCorpVO fbsCollectCorpVO = fbsCollectCorpDao.listCorpByCorpId(corpId);
		if(!EmptyUtils.isEmpty(fbsCollectCorpVO)) {
			budget = fbsBudgetDao.getCorpBudget(fbsCollectCorpVO.getId(), timeType, beginDate, endDate);
		}
		return budget;
	}

	/**
	 * 计划执行组件取数
	 *
	 * @param corpId
	 * @param itemCode
	 * @param processDate
	 * @return
	 */
	public FbsBudgetProcessCommonVO getFbsBudgetProcessByItemCodeList(String corpId, String itemCode, Date processDate, String billNo, String billId, String curId) {
		//避免erp挂接不写计划执行日期，默认写入系统日期
		if(processDate == null){
			processDate = DateUtils.getDateYMD();
		}
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		//计划执行汇总单位科目余额控制
		String collectBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_COLLECTBALANCE).getValue();
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
		String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
		FbsBudgetProcessCommonVO process = new FbsBudgetProcessCommonVO();
		FbsBudget budget = null;
		//判断当前执行周期是否上报或者未编制完成
		FbsBudgetVO fbsBudgetVO = null;
		List<String> budgetIds = new ArrayList<>();
		for(int i=0;i<corpIds.length;i++) {
			//单位是否是汇总单位
			if(CommonConstant.NO.equals(collectBalance)
					&& fbsCorpBudgetService.getChildCollectFlag(corpIds[i])){
				//获取汇总单位本身的计划
				budget = this.getCollectCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			} else {
				//获取单位周期计划
				budget = fbsBudgetDao.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			}
			if (budget == null || budget.getStatus() < BudgetConstant.STATUE_CHECK) {
				//针对本期计划执行
				if (DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getBeginDate()) >= 0
						&& DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getEndDate()) <= 0) {
					fbsBudgetVO = this.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), true);
					if (fbsBudgetVO != null) {
						budget = new FbsBudget();
						BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
						//先执行后控制的时候需要判断
						process.setProcessRule(fbsBudgetVO.getProcessRule());
					}
				}
			}
			if(budget!=null) {
				budgetIds.add(budget.getId());
			}
		}
		//获取科目信息
		FbsItemVO item = fbsItemService.getFbsItemVO(null, itemCode);
		process.setItemId(item.getId());
		process.setItemCode(itemCode);
		process.setItemName(item.getItemName());
		if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
			if(StringUtils.isBlank(budget.getId())){
				process.setItemMoney(BigDecimal.ZERO);
				process.setSuperItemMoney(BigDecimal.ZERO);
				process.setTopItemMoney(BigDecimal.ZERO);
			}else {
				List<FbsBudgetDtlAbsVO> dtlList = null;
				if(corpIds.length > 1){
					dtlList = fbsBudgetDtlAbsService.listSumByBudgetIds(budgetIds, budget.getTimeType());
				} else {
					dtlList = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
				}
				for (FbsBudgetDtlAbsVO dtl : dtlList) {
					//增加科目过滤，提升性能
					if (!dtl.getItemCode().equals(itemCode)
							&& !dtl.getItemCode().equals(item.getSuperItemCode())
							&& !dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
						continue;
					}
					//获取本科目余额：科目余额=上报金额+追加金额+超计划比例金额-已执行金额（折合人民币）
					//超计划比例金额 = (上报金额+追加金额)*超计划比例
					BigDecimal processMoney = BigDecimal.ZERO;
					BigDecimal money = dtl.getbMoney().add(dtl.getBsuperMoney());//获取科目上报+追加金额总和
					if (fbsBudgetVO != null) {
						processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
						if (fbsBudgetVO.getProcessRatio() != null) {
							money = money.multiply(fbsBudgetVO.getProcessRatio());//上报金额*执行比例
						}
					} else {
						processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(budget.getBeginDate(), budget.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
					}
					//增加科目超计划比例的金额
					if (dtl.getOverPerSpend() != null) {
						money = money.add(money.multiply(dtl.getOverPerSpend()));//累加超计划比例金额
					}
					if (item.getSign() > 0) {
						money = money.add(processMoney);//加上计划执行金额
					} else {
						money = money.subtract(processMoney);//减去计划执行金额
					}
					if (dtl.getItemCode().equals(itemCode)) {
						process.setItemMoney(money);
					}
					//不存在上级科目
					if (StringUtils.isEmpty(item.getSuperItemCode())) {
						process.setSuperItemMoney(process.getItemMoney());
						process.setTopItemMoney(process.getItemMoney());
					} else {
						if (dtl.getItemCode().equals(item.getSuperItemCode())) {
							process.setSuperItemMoney(money);
						}
						//获取顶级科目
						if (dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
							process.setTopItemMoney(money);
						}
					}
				}
			}
		} else {
		    //存在单号表示编辑/查看页面，不弹框校验
			if(StringUtils.isBlank(billNo) && StringUtils.isBlank(billId) ) {
				//当前计划执行周期单位计划未上报或计划未编制完成！
				throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_PROCESS_MESSAGE_NOTINPUTBUDGET);
			}else{
				process.setItemMoney(BigDecimal.ZERO);
				process.setSuperItemMoney(BigDecimal.ZERO);
				process.setTopItemMoney(BigDecimal.ZERO);
			}
		}
		//获取汇率
		if(StringUtils.isBlank(curId)) {
			//默认本位币(单位本身本位币)
			curId = corpService.get(corpId).getCurId();
		}
		if (budget!=null) {
			BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, budget.getCurId(), DateUtils.setSpecifiedDateOfTime(processDate), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
			process.setExchangeRate(exchangeRate);
		}else{
			process.setExchangeRate(BigDecimal.ONE);
		}
	return process;
	}

	/**
	 * 计划执行组件取数
	 *
	 * @return
	 */
		public List<FbsBudgetProcessCommonVO> getFbsBudgetProcessByItemCodeForList(List<FbsBudgetProcessDTO> list) {

			String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
			//计划执行汇总单位科目余额控制
			String collectBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_COLLECTBALANCE).getValue();
			List<FbsBudgetProcessCommonVO> processList = new ArrayList<FbsBudgetProcessCommonVO>();
			////// 优化 start
			for(FbsBudgetProcessDTO processDTO : list) {
				Date processDate = processDTO.getProcessDate();
				String corpId = processDTO.getCorpId();
				String itemCode = processDTO.getItemCode();
				String billNo = "";
				String billId = "";
				String curId = "";

			//避免erp挂接不写计划执行日期，默认写入系统日期
			if(processDate == null){
				processDate = DateUtils.getDateYMD();
			}

			//根据执行日期获取计划周期
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
			String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
			FbsBudgetProcessCommonVO process = new FbsBudgetProcessCommonVO();
			FbsBudget budget = null;
			//判断当前执行周期是否上报或者未编制完成
			FbsBudgetVO fbsBudgetVO = null;
			List<String> budgetIds = new ArrayList<>();
			for(int i=0;i<corpIds.length;i++) {
				//单位是否是汇总单位
				if(CommonConstant.NO.equals(collectBalance) && fbsCorpBudgetService.getChildCollectFlag(corpIds[i])){
					//获取汇总单位本身的计划
					budget = this.getCollectCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
				} else {
					//获取单位周期计划
					budget = fbsBudgetDao.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
				}
				if (budget == null || budget.getStatus() < BudgetConstant.STATUE_CHECK) {
					//针对本期计划执行
					if (DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getBeginDate()) >= 0
							&& DateUtils.dayDiffs(processDate, fbsBudgetPeriod.getEndDate()) <= 0) {
						fbsBudgetVO = this.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), true);
						if (fbsBudgetVO != null) {
							budget = new FbsBudget();
							BeanUtils.copyNotNullProperties(fbsBudgetVO, budget);
							//先执行后控制的时候需要判断
							process.setProcessRule(fbsBudgetVO.getProcessRule());
						}
					}
				}
				if(budget!=null) {
					budgetIds.add(budget.getId());
				}
			}
			//获取科目信息
			FbsItemVO item = fbsItemService.getFbsItemVO(null, itemCode);
			process.setItemId(item.getId());
			process.setItemCode(itemCode);
			process.setItemName(item.getItemName());
			if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
				if(StringUtils.isBlank(budget.getId())){
					process.setItemMoney(BigDecimal.ZERO);
					process.setSuperItemMoney(BigDecimal.ZERO);
					process.setTopItemMoney(BigDecimal.ZERO);
				}else {
					List<FbsBudgetDtlAbsVO> dtlList = null;
					if(corpIds.length > 1){
						dtlList = fbsBudgetDtlAbsService.listSumByBudgetIds(budgetIds, budget.getTimeType());
					} else {
						dtlList = fbsBudgetDtlAbsService.listByBudgetId(budget.getId(), budget.getTimeType());
					}
					for (FbsBudgetDtlAbsVO dtl : dtlList) {
						//增加科目过滤，提升性能
						if (!dtl.getItemCode().equals(itemCode)
								&& !dtl.getItemCode().equals(item.getSuperItemCode())
								&& !dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
							continue;
						}
						//获取本科目余额：科目余额=上报金额+追加金额+超计划比例金额-已执行金额（折合人民币）
						//超计划比例金额 = (上报金额+追加金额)*超计划比例
						BigDecimal processMoney = BigDecimal.ZERO;
						BigDecimal money = dtl.getbMoney().add(dtl.getBsuperMoney());//获取科目上报+追加金额总和
						if (fbsBudgetVO != null) {
							processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
							if (fbsBudgetVO.getProcessRatio() != null) {
								money = money.multiply(fbsBudgetVO.getProcessRatio());//上报金额*执行比例
							}
						} else {
							processMoney = fbsBudgetProcessService.collectProcessCurMoneyByNotBillNo(budget.getBeginDate(), budget.getEndDate(), dtl.getItemCode(), corpId, billNo, billId);
						}
						//增加科目超计划比例的金额
						if (dtl.getOverPerSpend() != null) {
							money = money.add(money.multiply(dtl.getOverPerSpend()));//累加超计划比例金额
						}
						if (item.getSign() > 0) {
							money = money.add(processMoney);//加上计划执行金额
						} else {
							money = money.subtract(processMoney);//减去计划执行金额
						}
						if (dtl.getItemCode().equals(itemCode)) {
							process.setItemMoney(money);
						}
						//不存在上级科目
						if (StringUtils.isEmpty(item.getSuperItemCode())) {
							process.setSuperItemMoney(process.getItemMoney());
							process.setTopItemMoney(process.getItemMoney());
						} else {
							if (dtl.getItemCode().equals(item.getSuperItemCode())) {
								process.setSuperItemMoney(money);
							}
							//获取顶级科目
							if (dtl.getItemCode().equals(item.getItemCode().substring(0, 3))) {
								process.setTopItemMoney(money);
							}
						}
					}
				}
			} else {
			    //存在单号表示编辑/查看页面，不弹框校验
				if(StringUtils.isBlank(billNo) && StringUtils.isBlank(billId) ) {
					//当前计划执行周期单位计划未上报或计划未编制完成！
					throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_PROCESS_MESSAGE_NOTINPUTBUDGET);
				}else{
					process.setItemMoney(BigDecimal.ZERO);
					process.setSuperItemMoney(BigDecimal.ZERO);
					process.setTopItemMoney(BigDecimal.ZERO);
				}
			}
			//获取汇率
			if(StringUtils.isBlank(curId)) {
				//默认本位币(单位本身本位币)
				curId = corpService.get(corpId).getCurId();
			}
			if (budget!=null) {
				BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, budget.getCurId(), DateUtils.setSpecifiedDateOfTime(processDate), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
				process.setExchangeRate(exchangeRate);
			}else{
				process.setExchangeRate(BigDecimal.ONE);
			}
			processList.add(process);
		}
		return processList;
		}

	public List<FbsBudgetProcessCommonVO> getMatchedBudgetItem(FcAccDetailBudgetMatchVO matchVO) {
		List<FbsBudgetProcessCommonVO> resultList = new ArrayList<>();
		String corpId = matchVO.getCorpId();
		String itemInfoText = matchVO.getItemInfoText();
		Date processDate = null;
		try {
			processDate = StringUtils.isNotEmpty(matchVO.getProcessDate()) ? DateUtils.parseDate(matchVO.getProcessDate(), "yyyy-MM-dd") : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		String billNo = matchVO.getVoucherNo();
		String curId = matchVO.getCurId();
		List<String> itemCodeList = StringUtils.isNotEmpty(itemInfoText)
				? Arrays.stream(itemInfoText.split(","))
					.filter(item -> item.contains("(") && item.contains(")"))
					.map(item -> item.substring(item.indexOf("(") + 1, item.indexOf(")")))
					.collect(Collectors.toList())
				: new ArrayList<>();
		for (String itemCode : itemCodeList) {
			FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = this.getFbsBudgetProcessByItemCode(corpId, itemCode, processDate, billNo, "", curId);
			resultList.add(fbsBudgetProcessCommonVO);
		}
		return resultList;
	}

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
