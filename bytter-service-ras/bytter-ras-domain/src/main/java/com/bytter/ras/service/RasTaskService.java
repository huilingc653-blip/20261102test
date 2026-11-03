package com.bytter.ras.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.entity.RasAuditTask;
import com.bytter.ras.vo.RasAuditTaskVO;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.ITaskSwitchService;
import com.bytter.system.api.dto.SysTaskDTO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;

/**
 * 营收稽核定时任务操作服务类
 * @author zhengg
 *
 */
@Service("rasTaskService")
@Transactional
public class RasTaskService extends AbstractService implements ITaskService {
	
	@Autowired
	private RasAuditTaskService rasAuditTaskService;
	
	@Autowired(required=false)
	private ITaskSwitchService taskSwitchServiceImpl;
	
	@Override
	public void run(String arguments) {
		try{
			if(StringUtils.isNotEmpty(arguments)){
				RasAuditTaskVO info=rasAuditTaskService.getRasAuditTaskVO(arguments);
				rasAuditTaskService.executeStrategy(info);
			}
		}catch(Exception e){
			e.printStackTrace();
			this.logger.error(e.toString());
			ExceptionStackUtils.getStack(e);
			throw new RuntimeException(e);
		}
	}

	/**
     * @param id 定时任务ID
     * @param active 1启动0暂停 
     */
	public void opWarnTaskInfo(String id,String active){
		taskSwitchServiceImpl.operationTask(id, active);
	}
	
	/**
	 * 新增或者修改定时任务
	 * @param strategy 稽核任务
	 */
	public void saveOrUpdateWarnTaskInfo(RasAuditTask strategy,ICurrentUser currentUser){
		if(strategy!=null){
			SysTaskDTO dto=new SysTaskDTO();
			dto.setCurrentUser(currentUser);
			dto.setId(strategy.getTaskId());
			dto.setCron(strategy.getTaskCron());
			dto.setActive(Integer.parseInt(strategy.getValidSign()));
//			营收稽核-(任务代码)任务名称-缴款方式
			dto.setTaskname("营收稽核-"
					+"-("+strategy.getCode()+")"+strategy.getName()
					+"-"+strategy.getPayTypeName());
			dto.setCommand(RasConstant.RAS_TASKCOMMAND);
			dto.setArguments(strategy.getId());
			dto.setPeriod(1);
			if(strategy.getStartDate()==null){
				strategy.setStartDate(new Date());
			}
			if(StringUtils.isNotEmpty(strategy.getTaskId())){
				dto.setStartDate(strategy.getStartDate());
				dto.setEndDate(DateUtils.addYear(strategy.getStartDate(), 100));
				taskSwitchServiceImpl.updateTask(dto);
			}else{
				dto.setStartDate(strategy.getStartDate());
				dto.setEndDate(DateUtils.addYear(strategy.getStartDate(), 100));
				taskSwitchServiceImpl.addTask(dto);
			}
			strategy.setTaskId(dto.getId());
		}
	}
}
