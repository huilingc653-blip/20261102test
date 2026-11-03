package com.bytter.workflow.dto;
import cn.hutool.core.lang.UUID;
import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;
import java.util.Objects;
/**
 * 工作流审批结果
 *
 * @author zhoul
 * @since 2018/1/5.
 */
public class WorkFlowResult implements IWorkFlowResult {
    private String msgId;
    private String processDefinitionKey;
    private String businessKey;
    private Boolean isPass;
    private CurrentUser currentUser;
    private WorkFlowResult.ApprovalStatus approvalStatus;
    public WorkFlowResult() {
    }
    public WorkFlowResult(String processDefinitionKey, String businessKey, CurrentUser currentUser) {
        this.processDefinitionKey = processDefinitionKey;
        this.businessKey = businessKey;
        this.currentUser = currentUser;
        this.msgId = UUID.fastUUID().toString();
    }
    public WorkFlowResult(String processDefinitionKey, String businessKey, CurrentUser currentUser, ApprovalStatus approvalStatus) {
        this(processDefinitionKey, businessKey, currentUser);
        this.approvalStatus = approvalStatus;
        this.isPass = approvalStatus == ApprovalStatus.PASS;
        this.msgId = UUID.fastUUID().toString();
    }
    @Override
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
    @Override
    public String getBusinessKey() {
        return businessKey;
    }
    @Override
    public Boolean getIsPass() {
        return isPass;
    }
    @Override
    public ICurrentUser getCurrentUser() {
        return currentUser;
    }
    @Override
    public Boolean isPass() {
        return getIsPass();
    }
    @Override
    public ICurrentUser getUser() {
        return getCurrentUser();
    }
    public ApprovalStatus getApprovalStatus() {
        return this.approvalStatus;
    }

    public String getMsgId() {
        return msgId;
    }

    public static WorkFlowResult buildResult(String businessKey, String processDefinitionKey, WorkFlowResult.ApprovalStatus approvalStatus, CurrentUser currentUser) {
        return new WorkFlowResult(processDefinitionKey, businessKey, currentUser, approvalStatus);
    }

    @AllArgsConstructor
    @Getter
    public enum ApprovalStatus {
        PASS("1", "同意"),
        RK("2", "撤回"),
        RJ("3", "打回上一节点"),
        RJS("4", "打回制单处"),
        UKN("5", "未知状态"),
        ;
        private final String code;
        private final String desc;
        public static ApprovalStatus of(Object code) {
            if (Objects.isNull(code)) {
                return UKN;
            }
            return Arrays.stream(ApprovalStatus.values())
                    .filter(item -> item.code.equalsIgnoreCase(String.valueOf(code)))
                    .findFirst().orElse(UKN);
        }
    }
}
