package com.bytter.biz.budget.service;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 资金计划工作流
* @author 黄茜
* @date 2017-08-05 10:13:44
* @version 1.0
*
*/
@WorkFlow({FbsWorkFlowConstant.FBS_BUDGET_INPUT,
	FbsWorkFlowConstant.FBS_BUDGET_SUPERADD})
@Service("fbsBudgetWorkFlowService")
@Transactional
public class FbsBudgetWorkFlowService implements IWorkFlowListener {
	
	/**
	 * 固定模式
	 */
	@Autowired
	private FbsBudgetService fbsBudgetService;
	
	/**
	 * 年分解模式
	 */
	@Autowired
	private FbsBudgetResolveYearService fbsBudgetResolveYearService;
	
	/**
	 * 月分解模式
	 */
	@Autowired
	private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
	
	/**
	 * 月滚动
	 */
	@Autowired
	private FbsBudgetRollMonthService fbsBudgetRollMonthService;
	
	/**
	 * 月滚动+双月周明细
	 */
	@Autowired
	private FbsBudgetRollMonthWeekService fbsBudgetRollMonthWeekService;
	
	/**
	 * 追加
	 */
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	
	@DubboReference(check = false)
	private INotificationService notificationService;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 启动完成
	 * @param workFlowResult 工作流结果
	 */
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 流程结束
	 * @param workFlowResult 工作流结果
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String tag = "";
		ICurrentUser user = workFlowResult.getUser();
		logger.error("资金计划审批人："+user.getLoginName()+"操作时间：" + DateUtils.formatDateTime());
		//年计划上报模式:0,年固定;1,年分解
		String yimode = fbsBudgetService.getInputMode("yimode");
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode("mimode");
		if(FbsWorkFlowConstant.FBS_BUDGET_INPUT.equals(workFlowResult.getProcessDefinitionKey())){
			//计划上报审批通过
			FbsBudget entity = fbsBudgetService.get(workFlowResult.getBusinessKey(), true);
			if(BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)){
				//年计划上报+年分解
				fbsBudgetResolveYearService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}else if(BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)){
				//月计划上报+月分解周
				fbsBudgetResolveMonthService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}else if(BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)){
				//月计划上报+月滚动
				fbsBudgetRollMonthService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}else if(BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
				//月计划上报+月滚动+双月周明细
				fbsBudgetRollMonthWeekService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}else{
				//固定模式
				fbsBudgetService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}
			
			//审批打回消息写入
			if(!workFlowResult.isPass()){
				if(BudgetConstant.TIME_TYPE_YEAR.equals(entity.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_INPUT_YEAR;
				}else if(BudgetConstant.TIME_TYPE_QUARTER.equals(entity.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_INPUT_QUARTER;
				}else if(BudgetConstant.TIME_TYPE_MONTH.equals(entity.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_INPUT_MONTH;
				}else if(BudgetConstant.TIME_TYPE_WEEK.equals(entity.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_INPUT_WEEK;
				}else if(BudgetConstant.TIME_TYPE_DAY.equals(entity.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_INPUT_DAY;
				}
				if(!StringUtils.isBlank(tag)){
					notificationService.send(entity.getId(), entity.getCorpId(), tag, entity);
				}
			}
		}else if(FbsWorkFlowConstant.FBS_BUDGET_SUPERADD.equals(workFlowResult.getProcessDefinitionKey())){
			//计划追加审批通过
			FbsBudgetSuperadd entity = fbsBudgetSuperaddService.get(workFlowResult.getBusinessKey(), true);
			FbsBudgetVO budget = fbsBudgetService.getBudget(entity.getBudgetId());
			if(BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode)){
				//年计划上报+年分解
				fbsBudgetResolveYearService.approvePassEndSuper(entity, workFlowResult.isPass(), user);
			}else if(BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)){
				//月计划上报+月分解周
				fbsBudgetResolveMonthService.approvePassEndInput(entity, workFlowResult.isPass(), user);
			}/*else if(BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)){
				//月计划上报+月滚动
				fbsBudgetRollMonthService.approvePassEndInput(budget, workFlowResult.isPass(), user);
			}else if(BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType()) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)){
				//月计划上报+月滚动+双月周明细
				fbsBudgetRollMonthWeekService.approvePassEndInput(budget, workFlowResult.isPass(), user);
			}*/else{
				//固定模式
				fbsBudgetService.approvePassEndSuper(entity, workFlowResult.isPass(), user);
			}
			//审批打回消息写入
			if(!workFlowResult.isPass()){
				if(BudgetConstant.TIME_TYPE_YEAR.equals(budget.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_YEAR;
				}else if(BudgetConstant.TIME_TYPE_QUARTER.equals(budget.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_QUARTER;
				}else if(BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_MONTH;
				}else if(BudgetConstant.TIME_TYPE_WEEK.equals(budget.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_WEEK;
				}else if(BudgetConstant.TIME_TYPE_DAY.equals(budget.getTimeType())){
					tag = NotificationConstant.BUDGET.BUDGET_SUPERADD_DAY;
				}
				if(!StringUtils.isBlank(tag)){
					notificationService.send(entity.getId(), entity.getCorpId(), tag, entity);
				}
			}
		}
	}
}
