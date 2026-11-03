package com.bytter.workflow.vo;

import com.bytter.workflow.domain.vo.TaskDetailVo;
import lombok.Data;

import java.util.List;

@Data
public class WorkflowDetailPageVO {

    private List<TaskDetailVo> details;

    private String processInstanceId;

    private Boolean noFlow;
}
