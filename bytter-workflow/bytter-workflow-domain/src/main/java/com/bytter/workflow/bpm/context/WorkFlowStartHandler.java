package com.bytter.workflow.bpm.context;

import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.constant.MsgRecordStatus;
import com.bytter.workflow.dto.WorkFlowResult;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 业务与工作流对接统一处理，存在自动审批与启动工作流两种情况
 * 工作流模拟自动审批消息不可控 可能消息到 业务还未处理完成事务没提交 审批状态更新已经到达 查询不到业务数据造成更新失败
 * 所以采用本地服务方式调用来保证自动审批
 * 使用反射实现是因为启动逻辑和回调逻辑一直 放在common可以不用每个服务都增加一个处理类
 * 更好的方式？业务自己调用工作流判断是否需要挂接 自己判断是否启动或者直接审批 代码更直观
 * 目前改造点比较多 所以采取统一处理
 *
 */

@Slf4j
@Component
public class WorkFlowStartHandler {

    /**
     *
     * @param cateKey 流程分类
     * @param businessKey 业务ID
     * @param variables 流程变量
     * @param corpId 业务单位ID
     * @param userId 当前登陆用户ID 接口统一调整预留
     */
    @Transactional(rollbackFor = Exception.class)
    public void handler(String cateKey, String businessKey, Object variables, String corpId,String userId) {
        AssertHelper.isTrue(StringUtils.isNoneBlank(userId),"流程启动用户不能为空");
        //获取工作流服务
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        IWorkFlowService workFlowServiceI = ApplicationContextUtil.getContext().getBean(IWorkFlowService.class);
        try {
            Boolean hasModel = workFlowServiceI.hasApproveModel(cateKey, corpId);
            //有的通过dubbo传输可能获取不到用户信息
            if (currentUser == null) {
                currentUser = new CurrentUser();
                IUserService userServiceI = ApplicationContextUtil.getContext().getBean("userServiceI", IUserService.class);
                UserDTO userDto = userServiceI.getByUserId(userId);
                BeanUtils.copyNotNullProperties(userDto, currentUser);
            }
            if (hasModel) {
                String tenantId = UserContextHelper.helper().getTenantId();
                log.info("启动租户【{}】工作流【{}】,业务ID【{}】",tenantId, cateKey, businessKey);
                //调用启动工作流接口
                try {
                    UserContextHelper.helper().setTTLTenant(tenantId);
                    workFlowServiceI.start(cateKey, businessKey, variables, corpId, currentUser.getId());
                } finally {
                    UserContextHelper.helper().clearTTLTenant();
                }

            }else {
                WorkFlowResult workFlowResult = autoApproveModel(cateKey, businessKey, currentUser); //没有启动工作流审批 则直接调用
                //自动审批通过记录消息日志 非自动审批工作流服务统一记录
                try {
                    workFlowServiceI.saveMsgRecord(workFlowResult, MsgRecordStatus.AUTO);
                } catch (Exception exception) {
                    log.error("{}日志记录异常", businessKey, exception);
                }
            }
        } catch (Exception e) {
            log.error("流程启动或者自动审批回调业务未知异常", e);
            //业务校验失败直接抛出
            if (e instanceof ParameterException) {
                throw (ParameterException) e;
            }else if(e instanceof BusinessException){
                throw (BusinessException)e;
            }else if (e.getCause() instanceof ParameterException) {
                throw (ParameterException)e.getCause();
            } else if (e.getCause()instanceof BusinessException) {
                throw (BusinessException)e.getCause();
            }else {
                throw new BusinessException("未知异常，请联系开发人员");
            }
        }
    }


    /**
     * 构建自动审批数据 后调用审批通过接口
     *
     */
    private WorkFlowResult autoApproveModel(String processDefinitionKey, String businessKey, ICurrentUser iCurrentUser) {
        CurrentUser currentUser = BeanUtils.copyNewInstanceProperties(iCurrentUser, CurrentUser.class);
        WorkFlowResult workFlowResult = WorkFlowResult.buildResult(
                businessKey,
                processDefinitionKey,
                WorkFlowResult.ApprovalStatus.PASS, currentUser);
        IWorkFlowListener workFlowListener = WorkFlowHelper.helper().get(processDefinitionKey);
        workFlowListener.flowCompleted(workFlowResult);
        return workFlowResult;
    }
}
