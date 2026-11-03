package com.bytter.workflow.bpm.context;

import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.aspect.IWorkFlowBusErrorLog;
import com.bytter.workflow.dto.WorkFlowResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * create by LuoYJ on 2026/3/12.
 */
@Slf4j
@Component
public class WorkFlowRetryHandler {

    @Resource
    private IWorkFlowBusErrorLog iWorkFlowBusErrorLog;

    public void retry(String id, ICurrentUser user) {
        Map<String, Object> map = iWorkFlowBusErrorLog.getById(id);

        if (ObjectUtils.isNotEmpty(map)){
            String cateKey = (String)map.get("cateKey");
            String businessKey = (String)map.get("businessKey");

//            IWorkFlowListener iWorkFlowListener = WorkFlowHelper.helper().get(cateKey);

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
                    WorkFlowResult workFlowResult = new WorkFlowResult(cateKey, businessKey, currentUser);
                    log.info("执行流程审批后【flowCompleted】方法，cateKey:{}, businessKey:{}", cateKey, businessKey);
                    iWorkFlowListener.flowCompleted(workFlowResult);
                    log.info("重试执行流程审批【flowCompleted】方法结束，cateKey:{}, businessKey:{}", cateKey, businessKey);
                } catch (Exception exception) {
                    log.error("重试执行流程审批后【flowCompleted】方法处理异常，id:{}, cateKey:{}, businessKey:{}",
                            id, cateKey, businessKey, exception);
                    throw exception;
                }
            } else {
                //TODO 重试失败，也会增加异常日志记录
                log.error("重试失败：未找到工作流监听器，cateKey: {}, id: {}, businessKey: {}", cateKey, id, businessKey);
                //throw new RuntimeException("未找到工作流监听器，cateKey: " + cateKey);
            }
        } else {
            log.warn("重试的记录不存在，id: {}", id);
        }

    }
}
