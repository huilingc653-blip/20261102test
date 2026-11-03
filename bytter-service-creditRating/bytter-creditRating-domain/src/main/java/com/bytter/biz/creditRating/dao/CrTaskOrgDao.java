package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrTaskOrgInfo;
import com.bytter.biz.creditRating.vo.CrTaskOrgInfoVo;
import com.bytter.biz.creditRating.vo.CrorgEvaluationInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务机构表dao
 *
 * @author huangyl
 */
@Repository
public class CrTaskOrgDao extends BaseDao<CrTaskOrgInfo, String> {

    public void deleteByTaskNo(String taskNo) {
        String sql = "delete from CR_TASK_ORG_INFO where task_no = :taskNo";
        Map<String, Object> map = new HashMap<>();
        map.put("taskNo", taskNo);
        createSQLQuery(sql, map).executeUpdate();
    }

    public List<CrTaskOrgInfoVo> getListTaskNoByOrgName(String orgName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select org.org_name as orgName,org.org_id as orgId,org.task_no as taskNo");
        sql.append(" from CR_TASK_ORG_INFO org ");
        sql.append(" where org_name like :orgName ");
        params.put("orgName", "%" + orgName + "%");
        return this.listBySQLAliasToBean(sql.toString(), params, CrTaskOrgInfoVo.class);
    }

    public List<CrTaskOrgInfoVo> getListByTaskNoList(List<String> taskNoList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select org.org_name as orgName,org.org_id as orgId,org.task_no as taskNo ");
        sql.append(" from CR_TASK_ORG_INFO org ");
        sql.append(" where ").append(SqlUtil.getInSql(taskNoList, "org.task_no", true));
        return this.listBySQLAliasToBean(sql.toString(), null, CrTaskOrgInfoVo.class);
    }

    public List<CrTaskOrgInfoVo> getListByTaskNo(String taskNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select org.org_name as orgName,org.org_id as orgId,org.task_no as taskNo ");
        sql.append(" from CR_TASK_ORG_INFO org ");
        sql.append(" where org.task_no = :taskNo ");
        params.put("taskNo", taskNo);
        return this.listBySQLAliasToBean(sql.toString(), params, CrTaskOrgInfoVo.class);
    }

    public DataTablesOutput queryByOrgName(String orgName){
        DataTablesInput input = new DataTablesInput();

        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySql = new StringBuilder();
        querySql.append(" select toi.id, toi.org_avg as agrAvg, ti.EVALUATE_START_DATE as evaluateStartDate, ti.EVALUATE_END_DATE as evaluateEndDate  ");
        querySql.append(" , toi.task_no as taskNo, toi.org_id as orgId,toi.org_evaluate_level as evaluationLevel,toi.org_avg as orgAvg ");
        querySql.append(" from CR_TASK_ORG_INFO toi left join cr_task_info ti on toi.task_no = ti.task_no ");
        querySql.append(" where toi.status = '95' and ti.status = '95' ");
        if (StringUtils.isNotBlank(orgName)){
            querySql.append("  and  toi.org_name =:orgName ");
            params.put("orgName",orgName);
        }
        return getDynamicBySQL(input, querySql.toString(), CrorgEvaluationInfoVo.class,"", false, params);


    }
}
