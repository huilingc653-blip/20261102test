package com.bytter.biz.creditRating.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.entity.CrTaskCorpInfo;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 信用评估分析
 * @author chenqin
 * @date 2024-08-09
 *
 */
@Repository
public class EvaluationAnalysisDao extends BaseDao<CrTaskCorpInfo, String> {

    /**
     * 获取评估任务列表
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpIds, List<String> taskNo) {
        String corpAuthSql = SqlUtil.getInSql(corpIds, "ctci.corp_id", true);
        StringBuilder sql=new StringBuilder();
        sql.append(" select info.task_no as taskNo,info.task_name as taskName,info.evaluate_start_date as evaluateStartDate,");
        sql.append(" info.evaluate_end_date as evaluateEndDate,info.rmk as rmk,info.task_cycle as taskCycle,ctci.corp_id as corpId,");
        sql.append(" ctci.status as status,ctci.create_date as createDate,ctci.id as id,sc.name as corpName, ");
        sql.append(" ctci.evaluate_by as evaluateBy,info.org_type as orgType ");
        sql.append(" from cr_task_corp_info ctci ");
        sql.append(" left join cr_task_info info ");
        sql.append(" on ctci.task_no = info.task_no ");
        sql.append(" left join sys_corp sc ");
        sql.append(" on ctci.corp_id = sc.id ");
        sql.append(" where info.status =").append(CreditRatingConstant.APPROVED);
        sql.append(" and ");
        sql.append(corpAuthSql);
        if (!CollectionUtils.isEmpty(taskNo)) {
            sql.append(" and ").append(getSQLIn(taskNo, 900, "info.task_no"));
        }
        dataTablesInput.setOrderString(new OrderString("ctci.create_date","desc"));
        dataTablesInput.setOrderString(new OrderString("info.task_no","desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), CrTaskCorpInfoVo.class, null, false, null);
    }

    /**
     * 根据组织评价任务明细id获取评价任务信息
     * @param id
     * @return
     */
    public CrTaskInfoVO getCrTaskInfoById(String id){
        Map<String, Object> params = new HashMap<>();
        String sql = " select cti.task_no as taskNo,cti.task_name as taskName,cti.evaluate_start_date as evaluateStartDate," +
                " cti.evaluate_end_date as evaluateEndDate,cti.rmk as rmk,cti.task_cycle as taskCycle,ctci.corp_id as corpId," +
                " ctci.status as status,ctci.create_date as createDate,ctci.id as id,sc.name as corpName,cti.task_weight as weight," +
                " cti.template_name as templateName,cti.org_type as orgType, " +
                " cti.TASK_WEIGHT as taskWeight " +
                " from cr_task_corp_info ctci " +
                " left join cr_task_info cti " +
                " on ctci.task_no = cti.task_no " +
                " left join sys_corp sc " +
                " on ctci.corp_id = sc.id " +
                " where ctci.id =:id";
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, CrTaskInfoVO.class);
    }

    /**
     * 根据评价任务批次号获取组织评价任务明细
     * @param taskNo
     * @return
     */
    public List<CrTaskCorpInfoVo> getCrTaskCorpInfoByTaskNo(String taskNo){
        String sql = " select ctci.* from cr_task_corp_info ctci " +
                " where ctci.task_no =:taskNo ";
        Map<String, Object> params = new HashMap<>();
        params.put("taskNo", taskNo);

        return this.listBySQLAliasToBean(sql, params, CrTaskCorpInfoVo.class);
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql, List<String> taskNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.task_no as taskNo,info.task_name as taskName,info.evaluate_start_date as evaluateStartDate,");
        sql.append(" info.evaluate_end_date as evaluateEndDate,info.rmk as rmk,info.task_cycle as taskCycle,ctci.corp_id as corpId,");
        sql.append(" ctci.status as status,ctci.create_date as createDate, ");
        sql.append(" ctci.id as id,sc.name as corpName, ");
        sql.append(" ctci.evaluate_by as evaluator,info.org_type as orgType ");
        sql.append(" from cr_task_corp_info ctci ");
        sql.append(" left join cr_task_info info ");
        sql.append(" on ctci.task_no = info.task_no ");
        sql.append(" left join sys_corp sc ");
        sql.append(" on ctci.corp_id = sc.id ");
        sql.append(" where 1 = 1");
        if (!CollectionUtils.isEmpty(taskNo)) {
            sql.append(" and ").append(getSQLIn(taskNo, 900, "info.task_no"));
        }
        sql.append(" and ").append(businessSql);

        return getDynamicBySQL(dataTablesInput, sql.toString(), CrTaskInfoVO.class, "ctci", false);
    }

    /**
     * 根据机构名称模糊查询获取机构评价任务
     * @param orgName
     * @return
     */
    public List<CrTaskOrgInfoVo> getCrTaskOrgInfoVoByOrgName(String orgName){
        String sql = " select ctoi.* from cr_task_org_info ctoi " +
                " where ctoi.org_name like :orgName ";
        Map<String, Object> params = new HashMap<>();
        params.put("orgName", "%" + orgName + "%");

        return this.listBySQLAliasToBean(sql, params, CrTaskOrgInfoVo.class);
    }

    /**
     * 根据评价任务编号获取机构评价任务
     * @param taskNos
     * @return
     */
    public List<CrTaskOrgInfoVo> getCrTaskOrgInfoVoByTaskNo(List<String> taskNos){
        StringBuffer sql = new StringBuffer();
        sql.append(" select ctoi.* from cr_task_org_info ctoi ");
        sql.append(" where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (!CollectionUtils.isEmpty(taskNos)) {
            sql.append(" and ").append(getSQLIn(taskNos, 900, "ctoi.task_no"));
        }
        sql.append(" order by ctoi.ORG_ID");
        return this.listBySQLAliasToBean(sql.toString(), params, CrTaskOrgInfoVo.class);
    }

    /**
     * 根据评分任务、单位id、机构id集合获取评分指标值固化
     * @param taskNo
     * @param corpId
     * @param orgIds
     * @return
     */
    public List<ScoringMetricValFixReportVO> getScoringMetricValFixReport(String taskNo, String corpId, List<String> orgIds){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select smvfr.scoring_indicator_code as scoringIndicatorCode,smvfr.scoring_indicator_name as scoringIndicatorName,");
        sql.append(" smvfr.scoring_indicator_description as scoringIndicatorDescription,smvfr.indicator_value as indicatorValue,");
        sql.append(" smvfr.score as score, edfr.data_id as dataId,edfr.data_name as dataName,edfr.data_code as dataCode,");
        sql.append(" edfr.corp_id as corpId,smvfr.id as id ");
        sql.append(" from scoring_metric_val_fix_report smvfr ");
        sql.append(" left join evaluated_data_for_report edfr on ");
        sql.append(" smvfr.evaluated_data_id = edfr.id ");
        sql.append(" left join evaluation_report er on ");
        sql.append(" edfr.evaluation_report_id = er.id ");
        sql.append(" left join evaluation_batch eb on ");
        sql.append(" er.evaluation_batch_id  = eb.id ");
        sql.append(" where eb.task_no = :taskNo ");
        if(StringUtils.isNotBlank(corpId)){
            sql.append(" and edfr.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        if(EmptyUtils.isNotEmpty(orgIds)){
            sql.append(" and ").append(getSQLIn(orgIds, 900, "edfr.data_id"));
            sql.append(" order by edfr.data_id");
        }
        params.put("taskNo", taskNo);

        return this.listBySQLAliasToBean(sql.toString(), params, ScoringMetricValFixReportVO.class);
    }


    /**
     * 根据id修改评分指标值固化得分
     * @param  id
     * @param score
     */
    public void updateScoringMetricValFixReport(String id, BigDecimal score){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("id",id);
        params.put("score",score);
        sql.append(" update scoring_metric_val_fix_report set score = :score where id =:id");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 将前端输入数值写入后端存储
     * @param id
     * @param value
     */
    public void updateIndicatorValue(String id,BigDecimal value){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("id",id);
        params.put("value",value);
        sql.append(" update scoring_metric_val_fix_report set INDICATOR_VALUE = :value where id =:id");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 根据任务编号获取固化原数据
     * @return
     */
    public List<EvaluatedDataForReportVO> getEvaluatedDataForReport(String taskNo){
        Map<String, Object> params = new HashMap<>();
        String sql = "select edfr.* from evaluated_data_for_report edfr" +
                " left join evaluation_batch eb on " +
                " eb.evaluation_batch_number = edfr.evaluation_batch_number " +
                " where task_no = :taskNo";
        params.put("taskNo", taskNo);
        return this.listBySQLAliasToBean(sql, params, EvaluatedDataForReportVO.class);
    }

    /**
     * 根据任务编号获取计算模型选定的评分指标,权重
     * @return
     */
    public List<ScoringIndicatorFixReportVO> getScoringIndicatorFixReport(String evaluationBatchNumber){
        Map<String, Object> params = new HashMap<>();
        String sql = "select sifr.* from scoring_indicator_fix_report sifr " +
                " where sifr.evaluation_batch_number =:evaluationBatchNumber";
        params.put("evaluationBatchNumber",evaluationBatchNumber);
        return this.listBySQLAliasToBean(sql, params, ScoringIndicatorFixReportVO.class);
    }

    /**
     * 根据任务编号获取固化评分指标值,得分
     * @return
     */
    public List<ScoringMetricValFixReportVO> getScoringMetricValFixReport(List<String> ids){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append("select smvfr.* from scoring_metric_val_fix_report smvfr ");
        if(EmptyUtils.isNotEmpty(ids)){
            sql.append(" where ").append(getSQLIn(ids, 900, "smvfr.evaluated_data_id"));
        }
        return this.listBySQLAliasToBean(sql.toString(), params, ScoringMetricValFixReportVO.class);
    }

    /**
     * 根据任务编号获取固化评分指标值,得分
     * @return
     */
    public void updateCrTaskOrgInfo(Map<String, Object> map,String taskNo){
        Map<String, Object> params = new HashMap<>();
        String sql = "update cr_task_org_info set org_avg =:orgAvg,org_evaluate_level =:orgEvaluateLevel,status =:status " +
                " where task_no =:taskNo and org_name =:orgName";
        params.put("orgAvg",map.get("weightedTotalScore"));
        params.put("orgEvaluateLevel",map.get("level"));
        params.put("status",CreditRatingConstant.APPROVED);
        params.put("taskNo",taskNo);
        params.put("orgName",map.get("evaluationObject"));
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 :根据评分等级查询评分等级级别<br>
     */
    public List<ScoringGradeLeFixReportVO> getScoringGradeLeFixReport(String evaluationBatchNumber) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select sglfr.*" +
                " from scoring_grade_le_fix_report sglfr " +
                " left join  scoring_grade_fix_report sgfr " +
                " on sgfr.id = sglfr.scoring_grade_fix_report_id " +
                " where sgfr.evaluation_batch_number =:evaluationBatchNumber" +
                " order by sglfr.lower_score_limit_level DESC";
        params.put("evaluationBatchNumber",evaluationBatchNumber);
        return this.listBySQLAliasToBean(sql, params, ScoringGradeLeFixReportVO.class);
    }
}
