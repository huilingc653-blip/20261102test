package com.bytter.workflow.domain.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.aspect.IWorkFlowBusErrorLog;
import com.bytter.workflow.domain.dao.WorkFlowBusErrorLogDao;
import com.bytter.workflow.domain.entity.WorkFlowBusErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

/**
 * create by LuoYJ on 2026/3/6.
 */
@Slf4j
@Service
public class WorkFlowBusErrorLogImpl implements IWorkFlowBusErrorLog {

    @Resource
    private WorkFlowBusErrorLogDao workFlowBusErrorLogDao;

    @Transactional
    @Override
    public void save(ICurrentUser user, String cateKey, String businessKey, Boolean isPass, String errorMsg) {
        WorkFlowBusErrorLog workFlowBusErrorLog = new WorkFlowBusErrorLog();
        workFlowBusErrorLog.setId(UUID.randomUUID().toString().replace("-", ""));
        workFlowBusErrorLog.setCateKey(cateKey);
        workFlowBusErrorLog.setBusinessKey(businessKey);
        workFlowBusErrorLog.setIsPass(isPass);
        workFlowBusErrorLog.setErrorMsg(errorMsg);
        workFlowBusErrorLog.setCreateBy(user.getId());
        workFlowBusErrorLog.setCreateDate(new Date());
        workFlowBusErrorLogDao.save(workFlowBusErrorLog);
    }

    @Override
    public Map<String, Object> getById(String id) {
        Map<String, Object> map = new HashMap<>();
        WorkFlowBusErrorLog workFlowBusErrorLog = workFlowBusErrorLogDao.get(id, true);
        if (ObjectUtils.isNotEmpty(workFlowBusErrorLog)){
            map.put("id", workFlowBusErrorLog.getId());
            map.put("cateKey", workFlowBusErrorLog.getCateKey());
            map.put("businessKey", workFlowBusErrorLog.getBusinessKey());
            map.put("isPass", workFlowBusErrorLog.getIsPass());
            map.put("errorMsg", workFlowBusErrorLog.getErrorMsg());
            map.put("createBy", workFlowBusErrorLog.getCreateBy());
        }
        return map;
    }


    /*public void retry(String id, ICurrentUser user) {
        WorkFlowBusErrorLog workFlowBusErrorLog = workFlowBusErrorLogDao.get(id,true);//TODO 数据增加状态，识别是否重试过
        if (ObjectUtils.isNotEmpty(workFlowBusErrorLog)){
            String cateKey = workFlowBusErrorLog.getCateKey();
            log.info("开始重试流程审批，id: {}, cateKey: {}, businessKey: {}", id, cateKey, workFlowBusErrorLog.getBusinessKey());
                
            //IWorkFlowListener iWorkFlowListener = WorkFlowHelper.helper().get(cateKey);
            ReferenceConfig<IWorkFlowListener> referenceConfig=new ReferenceConfig<>();
            referenceConfig.setGroup(cateKey);
            referenceConfig.setInterface(IWorkFlowListener.class);
            IWorkFlowListener iWorkFlowListener = referenceConfig.get();
                
            if (Objects.nonNull(iWorkFlowListener)) {
                log.info("成功获取工作流监听器：cateKey={}, listenerClass={}", cateKey, iWorkFlowListener.getClass().getName());
                try {
                    CurrentUser currentUser = new CurrentUser();
                    currentUser.setId(user.getId());
                    currentUser.setCorpId(user.getCorpId());
                    currentUser.setUsername(user.getUsername());
                    currentUser.setLoginName(user.getLoginName());
                    currentUser.setTenantId(user.getTenantId());
                    WorkFlowResult workFlowResult = new WorkFlowResult(cateKey, workFlowBusErrorLog.getBusinessKey(), currentUser);
                    log.info("执行流程审批后【flowCompleted】方法，cateKey:{}, businessKey:{}", cateKey, workFlowBusErrorLog.getBusinessKey());
                    iWorkFlowListener.flowCompleted(workFlowResult);
                    log.info("重试执行流程审批【flowCompleted】方法结束，cateKey:{}, businessKey:{}", cateKey, workFlowBusErrorLog.getBusinessKey());
                } catch (Exception exception) {
                    log.error("重试执行流程审批后【flowCompleted】方法处理异常，id:{}, cateKey:{}, businessKey:{}", 
                        id, cateKey, workFlowBusErrorLog.getBusinessKey(), exception);
                    throw exception;
                }
            } else {
                log.error("重试失败：未找到工作流监听器，cateKey: {}, id: {}, businessKey: {}", cateKey, id, workFlowBusErrorLog.getBusinessKey());
                throw new RuntimeException("未找到工作流监听器，cateKey: " + cateKey);
            }
        } else {
            log.warn("重试的记录不存在，id: {}", id);
        }
    
    }*/
}
