package com.bytter.biz.fundwarn.service;

import com.bytter.system.api.IXxlJobTemplate;
import com.bytter.system.api.vo.XxlJobInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.fundwarn.XxljobConstant;
import com.bytter.biz.fundwarn.entity.BtWarnStrategy;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 预警定时任务操作服务类
 *
 * @author zhengg
 */
@Service("fundWarnTaskService")
@Transactional
public class FundWarnTaskService extends AbstractService {
	
	@Autowired
    private BtWarnStrategyService btWarnStrategyService;
    @Autowired
    private FundWarnStrategyAnalysisService fundWarnStrategyAnalysisService;
    // @Autowired
    // private XxlJobTemplate xxlJobTemplate;
	@DubboReference(check = false)
	private IXxlJobTemplate xxlJobTemplate;
	@Value("${spring.application.name}")
	private String appName;
    
    @XxlJob("FundWarnTaskService")

	public void run() {
		try{
			String id = XxlJobHelper.getJobParam();
			if(StringUtils.isNotEmpty(id)){
				BtWarnStrategy strategy=btWarnStrategyService.get(id, true);
				if(strategy!=null){
					executeTaskStrategy(strategy);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			this.logger.error(e.toString());
			ExceptionStackUtils.getStack(e);
			throw new RuntimeException(e);
		}
	}
    /**
     * @param id     定时任务ID
     * @param active 1启动0暂停
     */
    public void opWarnTaskInfo(String id, String active) {
//		taskSwitchServiceImpl.operationTask(id, active);
    }
    /**
     * 时后 定时器调用预警功能处理
     *
     * @param strategy 预警策略对象
     */
    public void executeTaskStrategy(BtWarnStrategy strategy) {
        BtWarnStrategyVO entity = new BtWarnStrategyVO();
        BeanUtils.copyProperties(strategy, entity);
        fundWarnStrategyAnalysisService.executeStrategy(entity);
    }
    /**
     * 新增或者修改定时任务
     *
     * @param strategy 预警策略
     */
	public void saveOrUpdateWarnTaskInfo(BtWarnStrategy strategy, ICurrentUser currentUser) {
		XxlJobInfo xxlJobInfo = null;
		if (StringUtils.isBlank(strategy.getTaskId())) {
			xxlJobInfo = new XxlJobInfo();
			xxlJobInfo.setAuthor(currentUser.getUsername());
			xxlJobInfo.setScheduleConf(strategy.getTaskCron());
			xxlJobInfo.setScheduleType("CRON");
			xxlJobInfo.setExecutorHandler(XxljobConstant.beanName);
			xxlJobInfo.setTriggerStatus(strategy.getStatus());
			xxlJobInfo.setExecutorParam(strategy.getId());
			xxlJobInfo.setExecutorRouteStrategy("ROUND");
			// 资金预警-策略类型-策略代码策略名称
			xxlJobInfo.setJobDesc("资金预警-"
					+ "-" + BtFundWarnUtils.getTypeNameByCode(strategy.getTypeCode())
					+ "-" + BtFundWarnUtils.getDimensionNameByCode(strategy.getDimensionCode())
					+ "-" + strategy.getCode() + "-" + strategy.getName());
		} else {
			xxlJobInfo = xxlJobTemplate.getJobByExecutorHandler(appName, strategy.getTaskId());
			xxlJobInfo.setAuthor(currentUser.getUsername());
			xxlJobInfo.setScheduleConf(strategy.getTaskCron());
			xxlJobInfo.setScheduleType("CRON");
			xxlJobInfo.setExecutorHandler(XxljobConstant.beanName);
			xxlJobInfo.setTriggerStatus(strategy.getStatus());
			// 资金预警-策略类型-策略代码策略名称
			xxlJobInfo.setJobDesc("资金预警-"
					+ "-" + BtFundWarnUtils.getTypeNameByCode(strategy.getTypeCode())
					+ "-" + BtFundWarnUtils.getDimensionNameByCode(strategy.getDimensionCode())
					+ "-" + strategy.getCode() + "-" + strategy.getName());
		}
		Integer id = 0;
		if (StringUtils.isBlank(strategy.getTaskId())) {
			id = xxlJobTemplate.addJob(xxlJobInfo, appName);
			strategy.setTaskId(String.valueOf(id));
		} else {
			xxlJobTemplate.updateJob(xxlJobInfo);
		}

	}
    
}
