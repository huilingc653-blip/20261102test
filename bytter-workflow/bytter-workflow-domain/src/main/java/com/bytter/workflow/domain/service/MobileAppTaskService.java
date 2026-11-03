package com.bytter.workflow.domain.service;

import com.bytter.core.common.rest.RestInput;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.domain.dao.FlowCategoryApproveStatisticsDao;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.NativeHistoricProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端审批service
 * <p>
 * - MyTaskService类写了很多方法，有点臃肿，因此把移动端新开发的接口方法写到这里来。
 *
 * @author chenlw
 * @since 2020-04-01
 */
@Service("mobileAppTaskService")
@Transactional
public class MobileAppTaskService {

    @Resource
    private TaskService taskService;

    @Resource
    private FlowCategoryService flowCategoryService;

    @Resource
    private HistoryService historyService;
    
    @Resource
    private FlowCategoryApproveStatisticsDao flowCategoryApproveStatisticsDao;
    /**
     * 获取对应流程分类的待审批数目
     * <p>
     * - 比如查询结算管理，就把结算管理的子分类列表KEY传进来。
     *
     * @param userId                  用户ID
     * @param findFlowCategoryKeyList 流程分类列表
     * @return 待审批数目
     */
    private long getFlowCategoryTodoCount(String userId, List<String> findFlowCategoryKeyList) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (findFlowCategoryKeyList != null && !findFlowCategoryKeyList.isEmpty()) {
        	String[] array = findFlowCategoryKeyList.stream().toArray(String[]::new);
        	taskQuery.processDefinitionKeyIn(array);
        }
        taskQuery.active().taskAssignee(userId).orderByTaskCreateTime().asc();
        Long count = taskQuery.count();
        return count;
    }

    /**
     * 待审批-按[流程分类]展示待审批数量接口开发
     *
     * @param userId 用户ID
     * @return List Map:  id:流程分类key；name:流程分类名称; data：该流程分类的所有子流程列表。  todoCount: 一级流程分类待审批数目
     */
    private List<Map<String, Object>> getTodoFlowCategoryList(String userId) {
        // Map:  id:流程分类key；name:流程分类名称; data：该流程分类的所有子流程列表。;todoCount：该流程分类待办事项数目;
        // 子流程列表的元素也是Map: id:流程分类key；name:流程分类名称;
        List<Map<String, Object>> flowCategoryList = flowCategoryService.makeSecondLevelCategory(userId);
        List<Map<String, Object>> subFlowCategoryMapList;
        for (Map<String, Object> flowCategoryMap : flowCategoryList) {
            if (flowCategoryMap.get("data") != null) {
                subFlowCategoryMapList = (List<Map<String, Object>>) flowCategoryMap.get("data");
                List<String> subFlowCategoryKeyList = new ArrayList<>();
                for (Map<String, Object> subFlowCategoryMap : subFlowCategoryMapList) {
                    if (subFlowCategoryMap.get("id") != null) {
                        subFlowCategoryKeyList.add(subFlowCategoryMap.get("id").toString());
                    }
                }
                long todoCount = getFlowCategoryTodoCount(userId, subFlowCategoryKeyList);
                flowCategoryMap.put("todoCount", todoCount);
                if (todoCount > 0) {
                    Task latestTask = getLatestTodoFlowCategoryTask(userId, subFlowCategoryKeyList);
                    if (latestTask != null) {
                        long latestDataTime = latestTask.getCreateTime().getTime();
                        flowCategoryMap.put("latestDataTime", latestDataTime);
                    }
                }
            }
        }
        return flowCategoryList;
    }

    /**
     * 待审批-按[流程分类]展示待审批数量接口开发
     *
     * @param pagePara
     * @return
     */
    public List<Map<String, Object>> getTodoFlowCategoryList(RestInput pagePara) {
        String userId = pagePara.getCurrentUserId();
        return getTodoFlowCategoryList(userId);
    }

    /**
     * 获取最近一条 指定流程分类的待审批任务
     *
     * @param userId                  用户ID
     * @param findFlowCategoryKeyList 流程分类列表
     * @return 流程分类的待审批任务
     */
    private Task getLatestTodoFlowCategoryTask(String userId, List<String> findFlowCategoryKeyList) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (findFlowCategoryKeyList != null && !findFlowCategoryKeyList.isEmpty()) {
        	String[] array = findFlowCategoryKeyList.stream().toArray(String[]::new);
        	taskQuery.processDefinitionKeyIn(array);
        }
        taskQuery.active().taskAssignee(userId).orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.listPage(0, 1);
        if (taskList.size() > 0) {
            return taskList.get(0);
        } else {
            return null;
        }
    }

    /***********************************  已审批-按[流程分类]展示待审批数量接口开发  *********************************************************/

    /**
     * 已审批-按[流程分类]展示待审批数量接口开发
     *
     * @param pagePara
     * @return
     */
    public List<Map<String, Object>> getHistoryFlowCategoryList(RestInput pagePara) {
        String userId = pagePara.getCurrentUserId();
        return getHistoryFlowCategoryList(userId);
    }

    private List<Map<String, Object>> getHistoryFlowCategoryList(String userId) {
        // Map:  id:流程分类key；name:流程分类名称; data：该流程分类的所有子流程列表。;todoCount：该流程分类待办事项数目;
        // 子流程列表的元素也是Map: id:流程分类key；name:流程分类名称;
        List<Map<String, Object>> flowCategoryList = flowCategoryService.makeSecondLevelCategory(userId);
        List<Map<String, Object>> subFlowCategoryMapList;

        // Map<String, String> allSubFlowCategoryKey2ParentKeyMap：Map<子流程分类key, 所属父级流程分类key>
        Map<String, String> allSubFlowCategoryKey2ParentKeyMap = new HashMap<>();
        for (Map<String, Object> flowCategoryMap : flowCategoryList) {
            if (flowCategoryMap.get("data") != null) {
                subFlowCategoryMapList = (List<Map<String, Object>>) flowCategoryMap.get("data");
                String parentFlowCategoryKey = (String) flowCategoryMap.get("id");
                if (StringUtils.isEmpty(parentFlowCategoryKey)) {
                    continue;
                }
                allSubFlowCategoryKey2ParentKeyMap.put(parentFlowCategoryKey, parentFlowCategoryKey);
                for (Map<String, Object> subFlowCategoryMap : subFlowCategoryMapList) {
                    if (subFlowCategoryMap.get("id") != null) {
                        allSubFlowCategoryKey2ParentKeyMap.put(subFlowCategoryMap.get("id").toString(), parentFlowCategoryKey);
                    }
                }
            }
        }
        // 统计父级流程分类审批信息
        //  Map<String, Integer> approveStatisticsMap： Map<父级流程分类, 该流程分类以及子流程分类的审批数据统计数目>
        Map<String, Integer> approveStatisticsMap = new HashMap<>();
        // List中的Map结构：Map<流程分类KEY,该流程分类审批记录总数>
        List<Map> historicApproveStatisticsListMap = flowCategoryApproveStatisticsDao.getHistoricApproveStatisticsListMap(userId);
        if (historicApproveStatisticsListMap != null) {
            for (Map statisticsMap : historicApproveStatisticsListMap) {
                String flowCategoryKey = (String) statisticsMap.get("flow_category_key");
                Object dataCountTemp = statisticsMap.get("data_count");
                if (StringUtils.isEmpty(flowCategoryKey) || dataCountTemp == null) {
                    continue;
                }
                Integer dataCount = Integer.parseInt(String.valueOf(statisticsMap.get("data_count")));
                if (allSubFlowCategoryKey2ParentKeyMap.get(flowCategoryKey) != null) {
                    String parentFlowCategoryKey = allSubFlowCategoryKey2ParentKeyMap.get(flowCategoryKey);
                    if (approveStatisticsMap.get(parentFlowCategoryKey) != null) {
                        approveStatisticsMap.put(parentFlowCategoryKey, approveStatisticsMap.get(parentFlowCategoryKey) + dataCount);
                    } else {
                        approveStatisticsMap.put(parentFlowCategoryKey, dataCount);
                    }
                }
            }
        }


        for (Map<String, Object> flowCategoryMap : flowCategoryList) {
            String parentFlowCategoryKey = (String) flowCategoryMap.get("id");
            Integer completedTotalCount = approveStatisticsMap.get(parentFlowCategoryKey);
            if (completedTotalCount == null) {
                flowCategoryMap.put("completedTotalCount", 0);
                continue;
            }
            flowCategoryMap.put("completedTotalCount", approveStatisticsMap.get(parentFlowCategoryKey));

            // 获取父级分类下的所有子流程分类
            subFlowCategoryMapList = (List<Map<String, Object>>) flowCategoryMap.get("data");
            List<String> subFlowCategoryKeyList = new ArrayList<>();
            for (Map<String, Object> subFlowCategoryMap : subFlowCategoryMapList) {
                if (subFlowCategoryMap.get("id") != null) {
                    subFlowCategoryKeyList.add(subFlowCategoryMap.get("id").toString());
                }
            }
            if (completedTotalCount > 0) {
                HistoricProcessInstance historicProcessInstance = getLatestCompletedFlowCategoryLatest(userId, subFlowCategoryKeyList);
                if (historicProcessInstance != null) {
                    long latestDataTime = historicProcessInstance.getStartTime().getTime();
                    flowCategoryMap.put("latestDataTime", latestDataTime);
                }
            }
        }
        return flowCategoryList;
    }


    private StringBuilder getQueryHistoryApproveSubSql(NativeHistoricProcessInstanceQuery nativeHistoricProcessInstanceQuery,
                                                       List<String> findFlowCategoryKeyList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ( SELECT RES.BUSINESS_KEY_, min( RES.START_TIME_ ) AS START_TIME_ FROM ACT_HI_PROCINST RES GROUP BY RES.BUSINESS_KEY_ ) RES_MIN ");
        sb.append(" LEFT JOIN ( ");
        sb.append(" SELECT PROCINST.* FROM act_hi_procinst PROCINST,( SELECT RES.BUSINESS_KEY_, MAX( RES.START_TIME_ ) AS START_TIME_  ");
        sb.append(" FROM ACT_HI_PROCINST RES GROUP BY RES.BUSINESS_KEY_ ) ACT2 ");
        sb.append(" WHERE PROCINST.BUSINESS_KEY_ = ACT2.BUSINESS_KEY_ AND PROCINST.START_TIME_ = ACT2.START_TIME_ ) RES ");
        sb.append(" ON RES_MIN.BUSINESS_KEY_ = RES.BUSINESS_KEY_ ");
        sb.append(" LEFT OUTER JOIN ACT_RE_PROCDEF DEF ON RES.ROOT_PROC_INST_ID_ = DEF.ID_  ");
        sb.append(" WHERE ");
        sb.append(" EXISTS ( SELECT LINK.USER_ID_ FROM ACT_HI_IDENTITYLINK LINK WHERE USER_ID_ = #{userId} AND LINK.ROOT_PROC_INST_ID_ = RES.ID_ ) ");

        // 新增查询条件：查询用户已经审批了的工作流
        sb.append(" AND ");
        sb.append(" RES.ID_ IN ");
        sb.append(" ( SELECT HI_TASK.ROOT_PROC_INST_ID_ FROM ACT_HI_TASKINST HI_TASK INNER JOIN ACT_HI_IDENTITYLINK LINK  ON LINK.TASK_ID_ = HI_TASK.ID_ ");
        sb.append(" AND LINK.USER_ID_ = #{userId} AND HI_TASK.END_TIME_ IS NOT NULL )");

        if (findFlowCategoryKeyList != null && findFlowCategoryKeyList.size() > 0) {
            sb.append(" AND DEF.KEY_ IN (");
            for (int i = 0, len = findFlowCategoryKeyList.size(); i < len; i++) {
                sb.append("#{flowCategoryKey").append(i).append("}");
                String isNotMobileApprovalKey = findFlowCategoryKeyList.get(i);
                nativeHistoricProcessInstanceQuery.parameter("flowCategoryKey" + i, isNotMobileApprovalKey);
                if (i != len - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
        }
        return sb;
    }

    /**
     * 获取最近一条 指定流程分类的  已审批记录
     *
     * @param userId                  用户ID
     * @param findFlowCategoryKeyList 流程分类列表
     * @return 已审批记录
     */
    private HistoricProcessInstance getLatestCompletedFlowCategoryLatest(String userId, List<String> findFlowCategoryKeyList) {
        NativeHistoricProcessInstanceQuery nativeHistoricProcessInstanceQuery = historyService.createNativeHistoricProcessInstanceQuery();

        // 组装查询子SQL
        StringBuilder sb = getQueryHistoryApproveSubSql(nativeHistoricProcessInstanceQuery, findFlowCategoryKeyList);
        nativeHistoricProcessInstanceQuery.parameter("userId", userId);

        StringBuilder sql = new StringBuilder("SELECT max(RES_MIN.START_TIME_) AS START_TIME_ FROM ");
        sql.append(sb.toString());

        HistoricProcessInstance historicProcessInstance = nativeHistoricProcessInstanceQuery
                .sql(sql.toString())
                .singleResult();
        return historicProcessInstance;
    }


    /***********************************  已审批-按[流程分类]展示待审批数量接口开发  *********************************************************/


}
