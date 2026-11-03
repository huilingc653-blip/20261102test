package com.bytter.workflow.domain.dao;

import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenlw
 * @since 2020-04-26
 */
@Repository
public class FlowCategoryApproveStatisticsDao extends BaseDao {

    /**
     * 按流程分类KEY汇总已审批总数目
     * Map<String,Integer>
     *
     * @param userId 用户ID
     * @return 已审批总数目 (Map<流程分类KEY,该流程分类审批记录总数> )
     */
    public List<Map> getHistoricApproveStatisticsListMap(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DEF.KEY_ AS FLOW_CATEGORY_KEY, count(1) AS DATA_COUNT FROM ");
        sb.append(" ( SELECT RES.BUSINESS_KEY_, min( RES.START_TIME_ ) AS START_TIME_ FROM ACT_HI_PROCINST RES GROUP BY RES.BUSINESS_KEY_ ) RES_MIN ");
        sb.append(" LEFT JOIN ( ");
        sb.append(" SELECT PROCINST.* FROM act_hi_procinst PROCINST,( SELECT RES.BUSINESS_KEY_, MAX( RES.START_TIME_ ) AS START_TIME_  ");
        sb.append(" FROM ACT_HI_PROCINST RES GROUP BY RES.BUSINESS_KEY_ ) ACT2 ");
        sb.append(" WHERE PROCINST.BUSINESS_KEY_ = ACT2.BUSINESS_KEY_ ");

        // 新增查询条件：查询用户已经审批了的工作流
        sb.append(" AND PROCINST.ID_ IN ( SELECT HI_TASK.ROOT_PROC_INST_ID_ FROM ACT_HI_TASKINST HI_TASK ");
        sb.append(" INNER JOIN ACT_HI_IDENTITYLINK LINK  ON LINK.TASK_ID_ = HI_TASK.ID_ ");
        sb.append(" AND LINK.TASK_ID_ IS NOT NULL ");
        sb.append(" AND HI_TASK.END_TIME_ IS NOT NULL ");
        sb.append(" AND LINK.USER_ID_ = :userId ) ");

        sb.append(" AND PROCINST.START_TIME_ = ACT2.START_TIME_ ) RES ");
        sb.append(" ON RES_MIN.BUSINESS_KEY_ = RES.BUSINESS_KEY_ ");
        sb.append(" LEFT OUTER JOIN ACT_RE_PROCDEF DEF ON RES.PROC_DEF_ID_ = DEF.ID_  ");

        sb.append(" WHERE ");
        sb.append(" EXISTS ( ");
        sb.append(" SELECT LINK.USER_ID_ FROM ACT_HI_IDENTITYLINK LINK ");
        sb.append(" WHERE USER_ID_ = :userId AND LINK.ROOT_PROC_INST_ID_ = RES.ID_ ) ");
        sb.append(" GROUP BY DEF.KEY_ ");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return listMapBySQL(sb.toString(), params);
    }


}
