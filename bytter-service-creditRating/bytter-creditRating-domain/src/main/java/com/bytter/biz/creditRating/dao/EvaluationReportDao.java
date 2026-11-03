package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.api.dto.CrTaskOrgInfoDTO;
import com.bytter.biz.creditRating.entity.EvaluationReport;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 信用评估报告
 * @author chenqin
 * @date 2024-08-09
 *
 */
@Repository
public class EvaluationReportDao extends BaseDao<EvaluationReport,String> {

	/**
	 * 获取评估报告数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		//String corpAuthSql = SqlUtil.getInSql(corpIds, "ctci.corp_id", true);
		String sql = " select info.task_no as taskNo,info.task_name as taskName,info.evaluate_start_date as evaluateStartDate," +
				" info.evaluate_end_date as evaluateEndDate,info.rmk as rmk,info.task_cycle as taskCycle," +
				" ctoi.org_name as orgName,ctoi.org_avg as orgAvg,info.org_type as orgType,ctoi.org_evaluate_level as orgEvaluateLevel, " +
				" ctoi.id as id,ctoi.org_id as orgId ,ird.Institution_Code as orgCode " +
				" from cr_task_org_info ctoi " +
				" left join cr_task_info info " +
				" on ctoi.task_no = info.task_no " +
				" left join institutional_raw_Data ird on ird.id= ctoi.org_id " +
				" where info.status = " + CreditRatingConstant.APPROVED +
				" and NOT EXISTS ( " +
				"SELECT 1  FROM cr_task_corp_info ctci   WHERE   ctci.task_no = ctoi.task_no  AND (ctci.status <> 95 ) " +
				" ) " ;

		dataTablesInput.setOrderString(new OrderString("ctoi.create_date","desc"));
		return getDynamicBySQL(dataTablesInput, sql, CrTaskOrgInfoVo.class, null, false, null);
	}

	/**
	 * 获取综合评估报告数据列表
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getAnalyzeDataTables(DataTablesInput dataTablesInput){
		String sql = " select info.task_no as taskNo,ird.Institution_Code as orgCode,ctoi.org_name as orgName,info.org_type as orgType,ctoi.ORG_EVALUATE_LEVEL as grade," +
				" ctoi.org_id as orgId,ctoi.org_avg as orgAvg,info.evaluate_start_date as evaluateStartDate,info.evaluate_end_date as evaluateEndDate," +
				" ird.BANK_TYPE_ID as bankTypeId,ird.BANK_CODE as bankCode" +
				" from cr_task_org_info ctoi" +
				" left join cr_task_info info " +
				" on ctoi.task_no = info.task_no " +
				" left join institutional_raw_Data ird on ird.id= ctoi.org_id " +
				" where info.status = " + CreditRatingConstant.APPROVED +
				" and ctoi.org_avg is not null and ctoi.ORG_EVALUATE_LEVEL is not null";//评级和评分不为空，完成了所有评分的
		return getDynamicBySQL(dataTablesInput, sql, CreditAnalyzeReportVO.class, null, false, null);
	}
	/**
	 * 获取评估报告数据列表
	 * @param id
	 * @return
	 */
	public CrTaskOrgInfoVo getCrTaskInfoById(String id) {
		String sql = " select cti.task_no as taskNo,cti.task_name as taskName,cti.evaluate_start_date as evaluateStartDate," +
				" cti.evaluate_end_date as evaluateEndDate,cti.rmk as rmk,cti.task_cycle as taskCycle," +
				" ctoi.org_name as orgName,ctoi.org_avg as orgAvg,ctoi.org_evaluate_level as orgEvaluateLevel, " +
				" ctoi.id as id,cti.template_name as templateName " +
				" from cr_task_org_info ctoi " +
				" left join cr_task_info cti " +
				" on ctoi.task_no = cti.task_no " +
				" where ctoi.id =:id";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return getBySQLAliasToBean(sql, params, CrTaskOrgInfoVo.class);
	}

	/**
	 * 根据id获取评估报告详细信息
	 * @param id
	 * @return
	 */
	public EvaluationReportVO getByIdUnion(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id",id);
		String sql = " SELECT er.id AS id,er.evaluation_batch_id AS evaluationBatchId,er.evaluation_batch_number AS evaluationBatchNumber," +
				" er.model_solidification_id AS modelSolidificationId,er.model_name AS modelName,er.model_code AS modelCode," +
				" er.model_description AS modelDescription,er.evaluated_data_id AS evaluatedDataId,er.data_name AS dataName," +
				" er.data_code AS dataCode,er.data_source_category_name AS dataSourceCategoryName,er.model_batch_number AS modelBatchNumber," +
				" er.data_source_category_code AS dataSourceCategoryCode,er.creator AS creator,er.create_date AS createDate," +
				" edfr.credit_score AS creditScore,edfr.credit_grade AS creditGrade,rd.establishment_date AS establishmentDate," +
				" rd.registered_capital AS registeredCapital,rd.legal_representative AS legalRepresentative," +
				" rd.business_scope AS businessScope " +
				" FROM evaluation_report er " +
				" LEFT JOIN evaluated_data_for_report edfr ON er.id = edfr.evaluation_report_id " +
				" LEFT JOIN raw_data rd ON rd.id = edfr.data_id " +
				" WHERE er.id = :id";
		return getBySQLAliasToBean(sql, params, EvaluationReportVO.class);
	}

	/**
	 * 根据评估报告id获取评报告中指标类别固化
	 * @param id
	 * @return
	 */
	public List<IndicatorCategoryFixReportVO> getIndicatorCategoryFixReport(String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id",id);
		String sql = " SELECT icfr.id AS id,icfr.model_solidification_id AS modelSolidificationId," +
				" icfr.evaluation_report_id AS evaluationReportId,icfr.model_batch_number AS modelBatchNumber," +
				" icfr.rvaluation_batch_number AS rvaluationBatchNumber,icfr.category_name AS categoryName," +
				" icfr.category_rncoding AS categoryRncoding,icfr.category_description AS categoryDescription" +
				" FROM indicator_category_fix_report icfr " +
				" WHERE icfr.evaluation_report_id = :id";
		return listBySQLAliasToBean(sql, params, IndicatorCategoryFixReportVO.class);
	}

	/**
	 * 根据任务编号获取评价组织信息
	 * @param taskNo
	 * @return
	 */
	public List<CrTaskCorpInfoVo> getCrTaskCorpInfoByTaskNo(String taskNo) {
		String sql = " select ctoi.* from cr_task_corp_info ctoi where ctoi.task_no =:taskNo";
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo",taskNo);
		return listBySQLAliasToBean(sql, params, CrTaskCorpInfoVo.class);
	}

	/**
	 * 根据固化指标id获取指标定量方法
	 * @param ids
	 * @return
	 */
	public List<QMScoringIFixReportVO> getQMScoringIFixReport(List<String> ids){

		String sql = "select qmsifr.id as id,qmsifr.model_batch_number as modelBatchNumber," +
				" qmsifr.evaluation_batch_number as evaluationBatchNumber," +
				" qmsifr.scoring_indicator_name as scoringIndicatorName," +
				" qmsifr.scoring_indicator_code as scoringIndicatorCode," +
				" qmsifr.scoring_indicator_description as scoringIndicatorDescription," +
				" qmsifr.value as value,qmsifr.value_name as valueName,qmsifr.value_name_code as valueNameCode," +
				" qmsifr.s_i_f_r_id as sifrId,qmsifr.othervalue as otherValue,qmsifr.value_direction as valueDirection," +
				" qmsifr.othervalue_direction as otherValueDirection" +
				" from q_m_scoring_i_fix_report qmsifr " +
				" where " + getSQLIn(ids, 900, "qmsifr.s_i_f_r_id");
		return listBySQLAliasToBean(sql, null, QMScoringIFixReportVO.class);
	}

	/**
	 * 根据任务编号获取固化评分指标
	 * @param taskNo
	 * @return
	 */
	public List<ScoringIndicatorFixReportVO> getScoringIndicatorFixReport(String taskNo){
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo",taskNo);
		String sql = "select sifr.id as id,sifr.scoring_indicator_name as scoringIndicatorName," +
				" sifr.scoring_indicator_code as scoringIndicatorCode," +
				" sifr.scoring_indicator_coefficient as scoringIndicatorCoefficient," +
				" sifr.level_ as \"level\",sifr.parent_id as parentId," +
				" sifr.scoring_indicator_id as scoringIndicatorId," +
				" sifr.scoring_Indicator_Description as scoringIndicatorDescription, " +
				" sifr.scoring_rubric as scoringRubric " +
				" from scoring_indicator_fix_report sifr " +
				" left join model_solidification_report msr on " +
				" sifr.model_solidification_id = msr.id " +
				" left join evaluation_batch eb on " +
				" msr.evaluation_batch_number = eb.evaluation_batch_number" +
				" where eb.task_no =:taskNo";
		return listBySQLAliasToBean(sql, params, ScoringIndicatorFixReportVO.class);
	}

	/**
	 * 根据任务编号、机构id获取固化评分指标分数
	 * @param taskNo
	 * @return
	 */
	public List<ScoringMetricValFixReportVO> getScoringMetricValFixReport(String taskNo,String orgId){
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo", taskNo);
		params.put("dataId", orgId);
		String sql = " select smvfr.scoring_indicator_name as scoringIndicatorName," +
				" smvfr.scoring_indicator_code as scoringIndicatorCode," +
				" smvfr.scoring_indicator_description as scoringIndicatorDescription,smvfr.score as score," +
				" edfr.data_name as dataName,edfr.data_code as dataCode,edfr.data_id as dataId,edfr.corp_id as corpId," +
				" edfr.corp_name as corpName " +
				" from scoring_metric_val_fix_report smvfr  " +
				" left join evaluated_data_for_report edfr on smvfr.evaluated_data_id = edfr.id " +
				" left join evaluation_report er on edfr.evaluation_report_id = er.id " +
				" left join evaluation_batch eb on er.evaluation_batch_id = eb.id " +
				" where eb.task_no =:taskNo" +
				" and edfr.data_id =:dataId";
		return listBySQLAliasToBean(sql, params, ScoringMetricValFixReportVO.class);
	}

	/**
	 * 根据任务编号、机构id获取机构指标加权平均得分
	 * @param taskNo 任务编号
	 * @param orgId 机构id
	 * @return
	 */
	public List<CrScoringOrgContrastVo> getCrScoringOrgContrast(String taskNo, String orgId){
		Map<String, Object> params = new HashMap<>();
		params.put("taskNo", taskNo);
		params.put("orgId", orgId);

		String sql = " select csoc.id as id,csoc.task_no as taskNo,csoc.org_id as orgId," +
				" csoc.scoring_indicator_code as scoringIndicatorCode,csoc.scoring_org_avg as scoringOrgAvg " +
				" from cr_scoring_org_contrast csoc" +
				" where csoc.task_no =:taskNo  and csoc.org_id =:orgId ";
		return listBySQLAliasToBean(sql, params, CrScoringOrgContrastVo.class);
	}

	public CrTaskOrgInfoDTO getLatestOrgScoreByOrgName(String orgType, String orgName) {
		String sql =
				" select info.task_no as taskNo,info.task_name as taskName,info.evaluate_start_date as evaluateStartDate," +
				" info.evaluate_end_date as evaluateEndDate,info.rmk as rmk,info.task_cycle as taskCycle," +
				" ctoi.org_name as orgName,ctoi.org_avg as orgAvg,info.org_type as orgType,ctoi.org_evaluate_level as orgEvaluateLevel, " +
				" ctoi.id as id,ctoi.org_id as orgId ,ird.Institution_Code as orgCode " +
				" from cr_task_org_info ctoi " +
				" left join cr_task_info info " +
				" on ctoi.task_no = info.task_no " +
				" left join institutional_raw_Data ird on ird.id= ctoi.org_id " +
				" where info.status = " + CreditRatingConstant.APPROVED +
				" and NOT EXISTS ( " +
				"			SELECT 1  FROM cr_task_corp_info ctci   WHERE   ctci.task_no = ctoi.task_no  AND (ctci.status <> 95 ) " +
				" 		)	 " +
				" and info.org_type = " + orgType +
				" and ctoi.org_name = " + orgName + " ORDER BY ctoi.create_date DESC " +
				" limit 1" ;
		return getBySQLAliasToBean(sql, null, CrTaskOrgInfoDTO.class);
	}
}
