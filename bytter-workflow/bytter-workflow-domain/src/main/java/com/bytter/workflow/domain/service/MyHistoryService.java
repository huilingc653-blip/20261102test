package com.bytter.workflow.domain.service;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.vo.HistoricActivityInstanceVO;
import com.bytter.workflow.domain.vo.HistoricProcessInstanceVO;
import com.bytter.workflow.domain.vo.HistoricTaskInstanceVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class MyHistoryService {
    private final HistoryService historyService;
    private final FlowCategoryService flowCategoryService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IUserService userService;
    public MyHistoryService(HistoryService historyService, FlowCategoryService flowCategoryService) {
        this.historyService = historyService;
        this.flowCategoryService = flowCategoryService;
    }
    public TableGridResponse<HistoricProcessInstanceVO> historicProcessInstanceDataTable(TableGridInput tableGridInput) {
        IParameter parameter = tableGridInput.getParameter();
        String categoryId = parameter.getPara("categoryId");
        Long total = 0L;
        List<HistoricProcessInstanceVO> list = new ArrayList<>();
        if(StringUtils.isBlank(categoryId)){
            return new TableGridResponse<>(tableGridInput, total, list);
        }
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        FlowCategory flowCategory = flowCategoryService.getById(categoryId);
        historicProcessInstanceQuery.processDefinitionKey(flowCategory.getCateKey()).finished();
        total = historicProcessInstanceQuery.count();
        if (total > 0) {
            boolean flag = true;
            switch (tableGridInput.getOrder()) {
                case "startTime":
                    historicProcessInstanceQuery.orderByProcessInstanceStartTime();
                    break;
                case "endTime":
                    historicProcessInstanceQuery.orderByProcessInstanceEndTime();
                    break;
                default:
                    historicProcessInstanceQuery.orderByProcessInstanceEndTime().desc();
                    flag = false;
                    break;
            }
            if (flag) {
                if (tableGridInput.getSort().equals("desc")) {
                    historicProcessInstanceQuery.desc();
                } else {
                    historicProcessInstanceQuery.asc();
                }
            }
            List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery.listPage(tableGridInput.getStart(), tableGridInput.getRows());
            if (CollectionUtils.isEmpty(historicProcessInstanceList)) {
                return new TableGridResponse<>(tableGridInput, total, list);
            }
            //租户ID=corpID 目前没有多租户
            List<String> corpIds = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
                corpIds.add(historicProcessInstance.getTenantId());
                userIds.add(historicProcessInstance.getStartUserId());
            }
            Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIds);
            Map<String, String> nameMap = userService.getUserNameByIds(userIds);
            list = historicProcessInstanceList.stream().map(historicProcessInstance -> {
                HistoricProcessInstanceVO instanceVO = new HistoricProcessInstanceVO();
                BeanUtils.copyProperties(historicProcessInstance, instanceVO);
                instanceVO.setFlowCategoryName(flowCategory.getName());
                CorpDTO corpDTO = corpDTOMap.get(historicProcessInstance.getTenantId());
                if (Objects.nonNull(corpDTO)) {
                    instanceVO.setCorpCode(corpDTO.getCode());
                    instanceVO.setCorpName(corpDTO.getName());
                }
                instanceVO.setUsername(nameMap.get(historicProcessInstance.getStartUserId()));
                return instanceVO;
            }).collect(Collectors.toList());
        }
        return new TableGridResponse<>(tableGridInput, total, list);
    }
    public DataTablesOutput<HistoricTaskInstanceVO> historicTaskInstanceDataTable(DataTablesInput dataTablesInput) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        long total = historicTaskInstanceQuery.count();
        List<HistoricTaskInstance> historicProcessInstanceList = historicTaskInstanceQuery.finished().desc().listPage(dataTablesInput.getStart(), dataTablesInput.getLength());
        List<HistoricTaskInstanceVO> list = BeanUtils.copyListProperties(historicProcessInstanceList, HistoricTaskInstanceVO.class);
        return new DataTablesOutput<>(dataTablesInput.getDraw(), list, total, total);
    }
    /**
     * 流程实例节点
     * @param processInstanceId 流程实例ID
     * @return
     */
    public List<HistoricActivityInstanceVO> activityInstanceVo(String processInstanceId) {
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceStartTime().asc().processInstanceId(processInstanceId).list();
        return BeanUtils.copyListProperties(historicActivityInstanceList, HistoricActivityInstanceVO.class);
    }
    /**
     * 流程实例变量
     * @param processInstanceId
     * @return
     */
    public List<HistoricVariableInstance> activityInstanceVar(String processInstanceId) {
        //流程变量
        return historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
    }
    /**
     * 删除历史流程实例
     * @param ids
     * @return
     */
    public Boolean batchDelete(List<String> ids) {
        for(String processInstanceId:ids){
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
        return Boolean.TRUE;
    }
}
