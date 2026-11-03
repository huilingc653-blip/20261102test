package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrTaskCorpInfo;
import com.bytter.biz.creditRating.vo.CrTaskCorpInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务组织表dao
 *
 * @author huangyl
 */
@Repository
public class CrTaskCorpDao extends BaseDao<CrTaskCorpInfo, String> {

    public void deleteByTaskNo(String taskNo) {
        String sql = "delete from CR_TASK_CORP_INFO where task_no = :taskNo";
        Map<String, Object> map = new HashMap<>();
        map.put("taskNo", taskNo);
        createSQLQuery(sql, map).executeUpdate();
    }

    public List<CrTaskCorpInfoVo> getListTaskNoByCorpId(String corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select taskCorp.task_no as task_no,taskCorp.corp_id as corpId,corp.name as corpName ");
        sql.append(" from CR_TASK_CORP_INFO taskCorp left join sys_corp corp on corp.id = taskCorp.corp_id ");
        List<String> corpIdList = Arrays.asList(corpIds.split(CommonConstant.COMMA_SPLIT));
        sql.append(" where ").append(SqlUtil.getInSql(corpIdList, "taskCorp.corp_id", true));
        return this.listBySQLAliasToBean(sql.toString(), null, CrTaskCorpInfoVo.class);
    }

    public List<CrTaskCorpInfoVo> getListByTaskNoList(List<String> taskNoList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select taskCorp.task_no as task_no,taskCorp.corp_id as corpId,corp.name as corpName ");
        sql.append(" from CR_TASK_CORP_INFO taskCorp left join sys_corp corp on corp.id = taskCorp.corp_id ");
        sql.append(" where ").append(SqlUtil.getInSql(taskNoList, "taskCorp.task_No", true));
        return this.listBySQLAliasToBean(sql.toString(), null, CrTaskCorpInfoVo.class);
    }

    public List<CrTaskCorpInfoVo> getListTaskNoByTaskNo(String taskNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select taskCorp.task_no as task_no,taskCorp.corp_id as corpId,corp.name as corpName ");
        sql.append(" from CR_TASK_CORP_INFO taskCorp left join sys_corp corp on corp.id = taskCorp.corp_id ");
        sql.append(" where taskCorp.task_no = :taskNo");
        Map<String, Object> map = new HashMap<>();
        map.put("taskNo", taskNo);
        return this.listBySQLAliasToBean(sql.toString(), map, CrTaskCorpInfoVo.class);
    }
}
