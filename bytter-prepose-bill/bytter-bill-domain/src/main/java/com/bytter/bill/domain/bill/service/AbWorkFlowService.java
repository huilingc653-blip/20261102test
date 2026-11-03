package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.ProcessInstanceDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 工作流接口服务类
 * @author zhenghj
 *
 */
@Service("abWorkFlowService")
@Transactional
public class AbWorkFlowService extends BaseService<String, AbAccrecords>{
	
	@Autowired
	private IWorkFlowService workFlowService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, IParameterInput pagePara){
		return workFlowService.getWorkFlowBusiness(processDefinitionKey, pagePara);
	}
	/**
	 * 根据流程类型的KEY，获取业务ID集合
	 * @param processDefinitionKey
	 * @param userId
	 * @return
	 */
	public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, String userId){
		return workFlowService.getWorkFlowBusiness(processDefinitionKey, userId);
	}


	/**
	 * 定时任务启动流程（使用默认的用户scheduler作为流程发起人）
	 * @param processDefinitionKey
	 * @param businessKey
	 * @param variables
	 * @param corpId
	 * @return
	 */
	public ProcessInstanceDTO startByScheduler(String processDefinitionKey, String businessKey, Object variables, String corpId){
		return workFlowService.startByScheduler(processDefinitionKey, businessKey, variables, corpId);
	}
}
