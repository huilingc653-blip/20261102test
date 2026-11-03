package com.bytter.workflow.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.workflow.constant.MsgRecordStatus;
import com.bytter.workflow.dto.*;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/7/20.
 */
public interface IWorkFlowService {
    /**
     * 待办流程的业务数据
     */
    String TODO = "todo";
    /**
     * 获取（用户发起、经办的数据）
     */
    String TRACK = "track";
    /**
     * 审批通过
     */
    String  PASS= "pass";
    List<CommentDTO> listComments(String processInstanceId);
    /**
     * 获取审批数据
     * @param queryDTO
     * @return
     */
    WorkFlowBusiness getWorkFlowBusiness(BusinessTaskQueryDTO queryDTO);
    /**
     * 建议后续废弃
     * 获取审批数据(兼容旧接口)
     *
     * @param processDefinitionKey
     * @return
     */
    WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, IParameterInput pagePara);
    /**
     * 根据流程类型的KEY，获取业务ID集合
     *
     * @param processDefinitionKey
     * @param userId
     * @return
     */
    WorkFlowBusiness  getWorkFlowBusiness(String processDefinitionKey, String userId);
    List<String> getTaskIdsByBusinessKeysAndCateKey(List<String> businessKeys);
    /**
     * 获取用户在指定流程分类下已审批的业务ID集合，口径与 /bpm/my/finishCount 角标一致
     * （同一审批人、同一流程分类、同一时间窗）
     *
     * @param processDefinitionKey 流程分类KEY
     * @param cycleTime            周期，取值见 WorkFlowConstant，为空表示不限制时间范围
     * @param userId               审批人
     * @return 业务ID集合，没有则为空集合
     */
    List<String> listFinishedBusinessKeys(String processDefinitionKey, String cycleTime, String userId);
    /**
     * 启动流程
     *
     * @param piStartDTO 流程启动key
     * @return
     */
    Boolean start(PIStartDTO piStartDTO,Map<String,Object> variable);


    /**
     * 启动流程
     *
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     * @return
     */
    Boolean start(String processDefinitionKey, String businessKey, Object variables, String corpId, String startUserId);
    /**
     * 定时任务启动流程（使用默认的用户scheduler作为流程发起人）
     *
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     * @param corpId
     * @return
     */
    ProcessInstanceDTO startByScheduler(String processDefinitionKey, String businessKey, Object variables, String corpId);
    /**
     * 完成审批
     *
     * @param input
     * @param currentUser
     */
    void complete(WorkFlowInput input, ICurrentUser currentUser);
    /**
     * 批量撤回流程，当流程未有人审批时，才可以执行该造成
     *
     * @param ids    业务ID
     * @param userId 操作人ID
     */
    Map<String, Map<String, WorkFlowMessageEnum>> batchRevoke(String[] ids, String userId);
    /**
     * 检查指定流程的单位是否必须挂接审批流
     *
     * @param processDefinitionKey 流程类型KEY
     * @param corpIds              单位ID集合
     * @return 必须挂接流程，但未创建工作流的单位
     */
    List<String> checkWorkFlowMustApprovedByCorpIds(String processDefinitionKey, List<String> corpIds);
    /**
     * 通过流程分类与业务ID删除运行中的流程
     * @param category 流程分类
     * @param businessId 业务ID
     */
    void deleteRuntimeTaskByCategoryAndBusinessId(String category, String businessId);
    /**
     * 根据业务ID获取流程记录
     * @param businessId 业务ID
     * @return
     */
    List<TaskResponseDTO> listCommentsByBusinessId(String businessId);
    /**
     * 根据业务ID获取流程记录（包含历史流程）
     * @param businessId 业务ID
     * @return
     */
    List<TaskResponseDTO> listAllCommentsByBusinessId(String businessId);
    /**
     * 根据业务ID获取流程操作记录（不包含没有操作的）（包含历史流程）
     * @param businessId 业务ID
     * @return
     */
    List<TaskResponseDTO> listAllOperatedCommentsByBusinessId(String businessId);
    /**
     * 根据用户获取待审批
     * @param userId
     * @return
     */
    Integer getTodoByUserId(String userId);
    /**
     * 判断流程中是否设置有用户
     * @param userId
     * @return
     */
     Boolean flowIsHasUserId(String userId);
    /**
     * 是否是审批通过
     * @param taskResponseDTO
     * @return
     */
    Boolean isPastTask(TaskResponseDTO taskResponseDTO);

    /**
     * 更新发送消息记录表
     * @param status 状态 查看枚举 FlowStatus
     * @param exMsg 错误信息
     * @return
     */
    Boolean updateMsgRecord(String businessKey,Integer status, String exMsg);

    /**
     * 自动审批记录消息
     */
    Boolean saveMsgRecord(WorkFlowResult workFlowResult, MsgRecordStatus status);

    /**
     * 返回单位下业务是否有审批流模型 同时校验是否开启必须审批
     *
     */
    Boolean hasApproveModel(String cateKey, String corpId);

    List<BusinessTaskDTO> listBusinessTask(String userId,String cateKey);
}
