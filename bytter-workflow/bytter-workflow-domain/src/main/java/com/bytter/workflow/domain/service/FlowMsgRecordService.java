package com.bytter.workflow.domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.workflow.constant.MsgRecordStatus;
import com.bytter.workflow.dto.WorkFlowResult;
import com.bytter.core.utils.JsonUtils;
import com.bytter.workflow.domain.entity.FlowMsgRecord;
import com.bytter.workflow.domain.mapper.FlowMsgRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FlowMsgRecordService  extends ServiceImpl<FlowMsgRecordMapper, FlowMsgRecord> {

    public Boolean saveMsgRecord(WorkFlowResult workFlowResult, MsgRecordStatus status) {
        FlowMsgRecord flowMsgRecord = new FlowMsgRecord();
        flowMsgRecord.setStatus(status.getCode());
        flowMsgRecord.setCateKey(workFlowResult.getProcessDefinitionKey());
        flowMsgRecord.setBusinessKey(workFlowResult.getBusinessKey());
        flowMsgRecord.setMsg(JsonUtils.toJSONString(workFlowResult));
        return save(flowMsgRecord);
    }
}
