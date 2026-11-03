package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 工作流接口服务类
 * 
 * @author zhenghj
 *
 */
@Service("conWorkFlowService")
@Transactional
public class ConWorkFlowService extends BaseService<String, ConInfoRecord> {
    @Autowired
    private IWorkFlowService workFlowService;
    @Override
    public BaseDao<String, ConInfoRecord> getEntityDao() {
        return null;
    }
    public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, IParameterInput pagePara) {
        return workFlowService.getWorkFlowBusiness(processDefinitionKey, pagePara);
    }
    /**
     * 根据流程类型的KEY，获取业务ID集合
     * 
     * @param processDefinitionKey
     * @param userId
     * @return
     */
    public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, String userId) {
        return workFlowService.getWorkFlowBusiness(processDefinitionKey, userId);
    }

}
