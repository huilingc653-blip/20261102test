package com.bytter.workflow.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.AbstractService;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserAndCorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.constant.OptionConstant;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.entity.FlowOption;
import com.bytter.workflow.domain.entity.Option;
import com.bytter.workflow.domain.mapper.FlowTaskMapper;
import com.bytter.workflow.domain.vo.ApproveExtendParamVo;
import com.bytter.workflow.domain.vo.ApproveVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/9/27.
 *
 * @history 1. 2019-12-30  chenlw  移除单据配置查询、单据配置数据处理的相关方法。
 * 2. 2020-01-14  chenlw  新增APP-H5的接口方法：查询已办、办结流程的接口方法。
 */
@Slf4j
@Service("myTaskService")
@Transactional
public class MyTaskService extends AbstractService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;


    @Resource
    private FlowCategoryService flowCategoryService;


    @Autowired
    private IConfigService configServiceI;

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Autowired
    private FlowOptionService optionService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IUserService userService;

    @Autowired
    private FlowTaskMapper flowTaskMapper;

    /**
             * 获取APP默认显示数据条数
     *
     * @param pagePara
     * @return
     */
    private Integer getPageSizeConfig(RestInput pagePara) {
        Integer pageSize = pagePara.getPageSize();

        //加载APP默认显示数据条数
        ConfigDTO configDTO = configServiceI.getConfig(ConfigConstant.SYS, "appPageSize");
        if (configDTO != null && !StringUtils.isEmpty(configDTO.getValue())) {
            Integer temp = Integer.parseInt(configDTO.getValue());
            if (temp > 0) {
                pageSize = temp;
                //把服务器的APP默认显示数据条数传递给APP端，以便于APP端计算页码的递增
                pagePara.setPageSize(temp);
            }
        }
        return pageSize;
    }
    /**
     * 获取待办流程
     *
     * @param pagePara
     * @return
     */
    public PageResult todo(RestInput pagePara) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        TaskQuery taskQuery = taskService.createTaskQuery();
        IParameter parameter = pagePara.getParameter();
        Map<String, Object> map = parameter.get("time");
        if (map != null) {
            Integer type = (Integer) map.get("type");
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            switch (type) {
                case 0:
                    calendar.add(Calendar.DATE, -7);
                    taskQuery.taskCreatedBefore(now).taskCreatedAfter(calendar.getTime());
                    break;
                case 1:
                    calendar.add(Calendar.MONTH, -1);
                    taskQuery.taskCreatedBefore(now).taskCreatedAfter(calendar.getTime());
                    break;
                case 2:
                    calendar.add(Calendar.MONTH, -3);
                    taskQuery.taskCreatedBefore(now).taskCreatedAfter(calendar.getTime());
                    break;
                case 3:
                    Long startDate = (Long) map.get("startDate");
                    Long endDate = (Long) map.get("endDate");
                    taskQuery.taskCreatedBefore(new Date(endDate)).taskCreatedAfter(new Date(startDate));
                    break;
                default:
                    break;
            }
        }


        List<String> categories = parameter.getList("category");
        if (categories != null && !categories.isEmpty()) {
            //删除非移动端审批内容
            List<String> isNotMobileApprovalKeyList = flowCategoryService.getIsNotMobileApprovalKey(categories);
            categories.removeAll(isNotMobileApprovalKeyList);
            taskQuery.processDefinitionKeyIn(categories.toArray(new String[0]));
        } else {
            List<String> isMobileApprovalKeyList = flowCategoryService.getIsMobileApprovalKey();
            if (isMobileApprovalKeyList != null && isMobileApprovalKeyList.size() > 0) {
                // 过滤非移动端审批的流程
                taskQuery.processDefinitionKeyIn(isMobileApprovalKeyList.toArray(new String[0]));
            }
        }
        int order = parameter.getParaToInt("order",0);
        if(order==1){    //order参数为1，则倒序
            taskQuery.active().taskAssignee(pagePara.getCurrentUserId()).orderByTaskCreateTime().desc();
        }else{//默认正序排序
            taskQuery.active().taskAssignee(pagePara.getCurrentUserId()).orderByTaskCreateTime().asc();
        }
        Long count = taskQuery.count();
        int pageSize = getPageSizeConfig(pagePara);
        List<ApproveVO> list = new ArrayList<>();
        if (count > 0) {
            Integer start = (pagePara.getPageNumber() - 1) * pageSize;
            List<Task> taskList = taskQuery.listPage(start, pageSize);
            String[] proIds = taskList.stream().map(Task::getProcessInstanceId).toArray(String[]::new);
            List<VariableInstance> variableInstances = runtimeService.createVariableInstanceQuery()
                    .processInstanceIdIn(proIds).variableName(WorkFlowConstant.BUSINESS_JSON).list();
            Map<String, Object> variableInstanceMap = variableInstances.stream().collect(Collectors.toMap(VariableInstance::getProcessInstanceId, VariableInstance::getValue));
            for (Task task : taskList) {
                //流程定义key
                String definitionId = task.getProcessDefinitionId();
                String cateKey = definitionId.split(CommonConstant.COLON_SPLIT)[0];
                ProcessInstance processInstance = processInstanceQuery
                        .processInstanceId(task.getProcessInstanceId())
                        .singleResult();
                if  (processInstance == null){
                    continue;
                }
                // 查询历史流程实例
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstance.getProcessInstanceId())
                        .singleResult();
                if (Objects.isNull(historicProcessInstance)) {
                    log.warn("查询历史流程实例为空, processInstanceId={}", processInstance.getProcessInstanceId());
                    continue;
                }
                ApproveVO approveVO = new ApproveVO();
                approveVO.setId(task.getId());
//                UserDTO userDTO =userService.getByUserId(task.getAssignee());
                UserDTO userDTO =userService.getByUserId(historicProcessInstance.getStartUserId());
                approveVO.setUserId(userDTO.getId());
                approveVO.setUsername(userDTO.getUsername());//申请人名称
                //申请人单位不一定是流程启动的单位
                String corpId = (String) variableInstanceMap.getOrDefault("corpId", processInstance.getTenantId());
                if (StringUtils.isNoneBlank(corpId)) {
                    CorpDTO corpDTO = corpService.get(corpId);
                    approveVO.setCorpName(corpDTO.getName());
                    approveVO.setCorpId(corpId);
                }
                approveVO.setCategoryId(cateKey);
                approveVO.setCategoryName(flowCategoryService.getFullCategoryNameByKey(cateKey," > "));
                approveVO.setStatus(parameter.getParaToInt("type"));
                approveVO.setBusinessId(processInstance.getBusinessKey());
                approveVO.setCreateTime(task.getCreateTime().getTime());
                //app与H5定制显示字段
                List<FlowOption> options = optionService.getExtendParamsListByCategoryKey(cateKey);
                List<ApproveExtendParamVo> extendParamsList = new ArrayList<>();
                String value = String.valueOf(variableInstanceMap.getOrDefault(processInstance.getProcessInstanceId(), ""));
                setAppShow(list, approveVO, options, extendParamsList, value);
            }
        }
        return new PageResult<>(pagePara, count.intValue(), list, 1);
    }



    /**
     * 获取已办流程
     *
     * @param pagePara
     * @return
     */
    public PageResult historic(RestInput pagePara) {
        IParameter parameter = pagePara.getParameter();
        String userId = pagePara.getCurrentUserId();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        //获取分类条件
        List<String> categories = parameter.getList("category");
        //删除非移动端审批内容
        List<String> isNotMobileApprovalKeyList = flowCategoryService.getIsNotMobileApprovalKey();
        if(CollUtil.isNotEmpty(categories)){
            categories.removeAll(isNotMobileApprovalKeyList);
            historicProcessInstanceQuery.processDefinitionKeyIn(categories.toArray(new String[categories.size()]));
        }
        //组装时间条件
        buildTime(parameter, historicProcessInstanceQuery);
        //任务完成流程不一定完成
        List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery.list();
        List<String> bussKeys = historicProcessInstanceList.stream().map(HistoricProcessInstance::getBusinessKey).collect(Collectors.toList());
        int pageSize = getPageSizeConfig(pagePara);
        Integer start = (pagePara.getPageNumber() - 1) * pageSize;
        Long count = historyService.createHistoricTaskInstanceQuery()
        .processInstanceBusinessKeyIn(bussKeys.stream().toArray(String[]::new))
        .taskAssignee(userId).finished().orderByHistoricActivityInstanceStartTime().desc()
        .count();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKeyIn(bussKeys.stream().toArray(String[]::new))
                .taskAssignee(userId).finished().orderByHistoricActivityInstanceStartTime().desc()
                .listPage(start, pageSize);
        String[] prdIds = historicTaskInstances.stream().map(HistoricTaskInstance::getProcessInstanceId).toArray(String[]::new);
        List<ApproveVO> list = new ArrayList<>();
        buildTask(prdIds, historicProcessInstanceList, parameter, list);

        List<String> userIds = historicProcessInstanceList.stream().map(HistoricProcessInstance::getStartUserId).collect(Collectors.toList());
        List<UserAndCorpDTO> userDTOList = userService.getUserNameAndCorpNameByUserIds(userIds);
        Map<String, UserAndCorpDTO> userMap = userDTOList.stream().collect(Collectors.toMap(UserAndCorpDTO::getId, Function.identity(), (v1, v2) -> v1));

        //申请单位 是制单的申请单位，不是申请人的单位
        List<String> corpIds = list.stream().map(ApproveVO::getCorpId).collect(Collectors.toList());
        Map<String, String> corpMap = corpService.getNameByIds(corpIds);

        list.forEach(a ->{
            UserAndCorpDTO dto = userMap.get(a.getUserId());
            if (ObjectUtil.isNotEmpty(dto)){
                a.setUsername(dto.getUsername());
                a.setCorpName(corpMap.get(a.getCorpId()));
            }
        });

        return new PageResult<>(pagePara, count.intValue(), list, 1);
    }

    private void buildTask(String[] prdIds, List<HistoricProcessInstance> historicProcessInstanceList, IParameter parameter, List<ApproveVO> list) {
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceIdIn(prdIds).variableName(WorkFlowConstant.BUSINESS_JSON).list();
        Map<String, Object> variableInstanceMap = variableInstances.stream().collect(Collectors.toMap(HistoricVariableInstance::getProcessInstanceId, HistoricVariableInstance::getValue));
        List<String> prdIdLists = Arrays.stream(prdIds).collect(Collectors.toList());
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
            if (prdIdLists.contains(historicProcessInstance.getId())){
                //流程定义key
                String definitionId = historicProcessInstance.getProcessDefinitionId();
                String cateKey = definitionId.split(CommonConstant.COLON_SPLIT)[0];
                ApproveVO approveVO = new ApproveVO();
                approveVO.setId(historicProcessInstance.getId());
                approveVO.setCorpId(historicProcessInstance.getTenantId());
                approveVO.setUserId(historicProcessInstance.getStartUserId());
            /*UserDTO userDTO = null;
            if (ObjectUtil.isNotEmpty(historicProcessInstance.getStartUserId())) {
                userDTO = userService.getByUserId(historicProcessInstance.getStartUserId());
            }
            if (ObjectUtil.isNotEmpty(userDTO)){
                approveVO.setUserId(userDTO.getId());
                approveVO.setUsername(userDTO.getUsername());
                CorpDTO corpDTO =corpService.get(userDTO.getCorpId());
                approveVO.setCorpName(corpDTO.getName());
            }*/
                approveVO.setCreateTime(historicProcessInstance.getStartTime().getTime());
                approveVO.setStatus(parameter.getParaToInt("type"));
                approveVO.setCategoryId(cateKey);
                approveVO.setCategoryName(flowCategoryService.getFullCategoryNameByKey(cateKey, " > "));
                approveVO.setBusinessId(historicProcessInstance.getBusinessKey());
                //app与H5定制显示字段
                List<FlowOption> options = optionService.getExtendParamsListByCategoryKey(cateKey);
                List<ApproveExtendParamVo> extendParamsList = new ArrayList<>();
                String value = String.valueOf(variableInstanceMap.getOrDefault(historicProcessInstance.getId(), ""));
                setAppShow(list, approveVO, options, extendParamsList, value);
            }
        }
    }

    private static void buildTime(IParameter parameter, HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        Map<String, Object> map = parameter.get("time");
        if (map != null) {
            Integer type = (Integer) map.get("type");
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            switch (type) {
                case 0:
                    calendar.add(Calendar.DATE, -7);
                    historicProcessInstanceQuery.startedBefore(now).startedAfter(calendar.getTime());
                    break;
                case 1:
                    calendar.add(Calendar.MONTH, -1);
                    historicProcessInstanceQuery.startedBefore(now).startedAfter(calendar.getTime());
                    break;
                case 2:
                    calendar.add(Calendar.MONTH, -3);
                    historicProcessInstanceQuery.startedBefore(now).startedAfter(calendar.getTime());
                    break;
                case 3:
                    Long startDate = (Long) map.get("startDate");
                    Long endDate = (Long) map.get("endDate");
                    historicProcessInstanceQuery.startedBefore(new Date(endDate)).startedAfter(new Date(startDate));
                    break;
                default:
                    break;
            }
        }
    }

    private void setAppShow(List<ApproveVO> list, ApproveVO approveVO, List<FlowOption> options, List<ApproveExtendParamVo> extendParamsList, String value) {
        if (CollUtil.isNotEmpty(options)&& StringUtils.isNotEmpty(value)) {
            //业务数据快照
            Map<String,Object> busMap = JsonUtils.parseObject(value, Map.class);
            for (FlowOption option : options) {
                if (OptionConstant.EXTEND_PARAM_BUSINESSID_KEY.equals(option.getFieldName())) {
                    extendParamsList.add(new ApproveExtendParamVo(option.getName(), approveVO.getBusinessId(), option.getSort()));
                }else {
                    extendParamsList.add(new ApproveExtendParamVo(option.getName(), busMap.get(option.getFieldName()), option.getSort()));
                }
            }
        }
        approveVO.setExtendParams(extendParamsList);
        list.add(approveVO);
    }


    /**
     * Created by chenlw on 2019/03/07.
     * 配置已办流程的定制显示字段，APP根据这个显示字段列表来显示流程的相关数据
     *
     * @param extendParamsOptionListMap 定制显示字段列表map，用于存储已经从数据库查询出来的字段列表，减少数据库查询次数
     * @param approveVO                 审批对象
     * @param historicProcessInstance   用于流程变量表map
     * @param invoiceOption             单据配置
     */
    private void setFinishApproveExtendParams(Map<String, List<Option>> extendParamsOptionListMap,
                                              ApproveVO approveVO,
                                              HistoricProcessInstance historicProcessInstance,
                                              Option invoiceOption) {
        List<ApproveExtendParamVo> extendParamsList = new ArrayList<>();

      /*  // 流程配置的定制显示字段
        // approveVO.getCategoryId()   FlowCategory.key
        List<Option> extendParamsOptionList = extendParamsOptionListMap.get(approveVO.getCategoryId());
        if (extendParamsOptionList == null) {
            extendParamsOptionList = optionService.getExtendParamsListByCategoryKey(approveVO.getCategoryId());

            if (extendParamsOptionList.size() == 0) {
                // 定制显示字段列表个数为0时，说明该流程未初始化定制显示字段。
                // 程序根据其单据配置附加固定显示字段到列表，如果没有单据配置，则使用默认列表。
                if (StringUtils.isNotBlank(approveVO.getCategoryId())) {
                    List<Option> stableExtendParams = optionService.getAppendStaleExtendParamsByCategoryKey(approveVO.getCategoryId(), invoiceOption);
                    extendParamsOptionList.addAll(stableExtendParams);
                }
            } else if (extendParamsOptionList.size() == 1) {
                // 定制显示字段列表个数为1时，说明该显示字段是用SQL脚本初始化的，另外三个固定字段（申请单位、申请人、申请时间）未初始化。
                // 因此需要附加另外三个固定字段到定制显示字段列表当中。
                List<Option> stableExtendParams = optionService.getAppendStaleExtendParamsExceptInvoice();
                extendParamsOptionList.addAll(stableExtendParams);
            }

            // 把流程的定制显示字段列表暂时存起来，避免多次查询数据库
            extendParamsOptionListMap.put(approveVO.getCategoryId(), extendParamsOptionList);
        }
        if (extendParamsOptionList != null && extendParamsOptionList.size() > 0) {
            // 查询已审批数据的Variable数据（流程变量表数据）
            HistoricVariableInstance historicVariableInstance;
            HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcessInstance.getId());

            for (Option option : extendParamsOptionList) {

                if (option.getVisible() != null && option.getVisible().equals(OptionConstant.EXTEND_PARAMS_VISIBLE)) {

                    // 固定显示字段: 1、单据号; 2、申请单位;3、申请人;4、申请时间
                    if (!StringUtils.isEmpty(approveVO.getInvoiceId())
                            && approveVO.getInvoiceId().equals(option.getFieldName())) {
                        // 有单据配置
                        extendParamsList.add(new ApproveExtendParamVo(approveVO.getInvoiceName(), approveVO.getInvoiceId(), option.getSort()));
                    } else if (OptionConstant.EXTEND_PARAM_BUSINESSID_KEY.equals(option.getFieldName())) {
                        extendParamsList.add(new ApproveExtendParamVo(option.getName(), approveVO.getBusinessId(), option.getSort()));
                    } else if (OptionConstant.EXTEND_PARAM_CORP_NAME_KEY.equals(option.getFieldName())) {
                        extendParamsList.add(new ApproveExtendParamVo(option.getName(), approveVO.getCorpName(), option.getSort()));
                    } else if (OptionConstant.EXTEND_PARAM_USERNAME_KEY.equals(option.getFieldName())) {
                        extendParamsList.add(new ApproveExtendParamVo(option.getName(), approveVO.getUsername(), option.getSort()));
                    } else if (OptionConstant.EXTEND_PARAM_CREATE_TIME_KEY.equals(option.getFieldName())) {
                        extendParamsList.add(new ApproveExtendParamVo(option.getName(), approveVO.getCreateTime(), option.getSort()));
                    } else {
                        // 自定义添加的显示字段
                        historicVariableInstance = historicVariableInstanceQuery.variableName(option.getFieldName()).singleResult();
                        if (historicVariableInstance != null) {
                            Object variablesValue = historicVariableInstance.getValue();
                            addVariablesValue(extendParamsList, option, variablesValue);
                        }

                    }

                }

            }
        }*/

        approveVO.setExtendParams(extendParamsList);
    }

    /**
     * 对流程变量表的字段值进行转换处理（金钱要转化成#,###.00的格式）
     *
     * @param extendParamsList   定制显示字段列表
     * @param extendParamsOption 显示字段对象
     * @param variablesValue     字段值
     */
    private void addVariablesValue(List<ApproveExtendParamVo> extendParamsList, Option extendParamsOption, Object variablesValue) {
        if (!StringUtils.isEmpty(extendParamsOption.getName())
                && !StringUtils.isEmpty(extendParamsOption.getFieldName())
                && extendParamsOption.getSort() != null) {
            // 添加定制显示字段
            if (variablesValue != null) {
                if (variablesValue instanceof BigDecimal || variablesValue instanceof Double) {
                    // 金钱格式化，保留2位小数
                    // NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA); //建立货币格式化引用
                    // DecimalFormat df = new DecimalFormat("#.00");
                    // NumberFormat numberFormat = new DecimalFormat("#,###.00");
                    // #,##0.00，解决金额小于1时金额显示格式不正确的问题；比如0.5元，#,###.00格式就会显示成.50，这显然不正确。
                    NumberFormat numberFormat = new DecimalFormat("#,##0.00");
                    extendParamsList.add(new ApproveExtendParamVo(extendParamsOption.getName(), numberFormat.format(variablesValue), extendParamsOption.getSort()));
                } else {
                    extendParamsList.add(new ApproveExtendParamVo(extendParamsOption.getName(), variablesValue, extendParamsOption.getSort()));
                }
            }
        }
    }

    /**
     * 根据用户ID，获取username
     * 在controller层不能直接调用其它模块
     * @param assigneeLists
     * @return
     */
    public Map<String, String> getLoginNameByIds(List<String> assigneeLists) {
        Map<String, String> loginNameMap = userService.getLoginNameByIds(assigneeLists);
        return loginNameMap;
    }

    public Map<String, String> getUserNameByIds(List<String> assigneeLists) {
        Map<String, String> userNameMap = userService.getUserNameByIds(assigneeLists);
        return userNameMap;
    }
}