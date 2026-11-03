package com.bytter.workflow.domain.vo;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
/**
 * \* Created with IntelliJ IDEA.
 * \* FlowCategory: lifh
 * \* Date: 2018/11/22
 * \* Description:撤回结果vo
 * \
 */
@Data
public class RevokeResultVo implements Serializable {
    private String corpId;
    private String corpName;
    private int errorNum;
    private List<String> businessIdList;
    private List<WorkFlowMessageEnum> messageList;
}
