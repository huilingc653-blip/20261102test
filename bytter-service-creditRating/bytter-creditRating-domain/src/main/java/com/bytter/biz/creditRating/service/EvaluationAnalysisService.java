package com.bytter.biz.creditRating.service;

import cn.hutool.json.JSONUtil;
import com.bytter.biz.creditRating.api.constant.CreWorkFlowConstant;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.EvaluationAnalysisDao;
import com.bytter.biz.creditRating.entity.*;
import com.bytter.biz.creditRating.util.NormalizationModel;
import com.bytter.biz.creditRating.util.ScoringMethod;
import com.bytter.biz.creditRating.util.SegmentationScorer;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenqin
 * @Title: Service
 * @Description: 信用评估分析
 * @date 2024-08-09
 */
@Slf4j
@WorkFlow(CreWorkFlowConstant.CREDIT_ORG_EVALUATION)
@Service("evaluationAnalysisService")
@Transactional
public class EvaluationAnalysisService extends BaseService<CrTaskCorpInfo, String> implements IWorkFlowListener {

    @Autowired
    private EvaluationAnalysisDao evaluationAnalysisDao;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private EvaluationLogService evaluationLogService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired(required = false)
    private CrmComputationalModelService crmComputationalModelService;

    @Autowired(required = false)
    private CrmScoringGradeService crmScoringGradeService;

    @Autowired
    private CtrScoringCorpContrastService corpContrastService;

    @Autowired
    private CrmScoringIndicatorSolidService crmScoringIndicatorSolidService;

    @Autowired
    private CrdRawDataService crdRawDataService;

    @Autowired
    private CrTaskInfoService crTaskInfoService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Autowired
    private CrTaskCorpService crTaskCorpService;

    @Autowired
    private CrTaskOrgService crTaskOrgService;

    @Autowired
    private FinancingService financingService;

    @Autowired
    private LcGuaranteeBillBondService lcGuaranteeBillBondService;

    @Autowired
    private CreditLimitService creditLimitService;

    @Autowired
    private ChoDepositRateService choDepositRateService;

    //@Autowired
    //private IBankLoanService bankLoanService;

    //@DubboReference(check = false)
    //private BankAccountService bankAccountService;
    @Autowired
    private EvaluationAnalysisService evaluationAnalysisService;
    private final Map<String, Integer> indicatorMap = new ConcurrentHashMap<>();

    /**
     * 归一化
     */
    @Value("${url.nor.minmax:}")
    public  String urlNorMinmax;

    /**
     * 分段法
     */
    @Value("${url.nor.ss:}")
    public  String urlNorSs;

    /**
     * 均分与合计得分
     */
    @Value("${url.indicators.bat:}")
    public  String urlIndicatorsBat;


    @Override
    public BaseDao<CrTaskCorpInfo, String> getEntityDao() {
        return evaluationAnalysisDao;
    }

    /**
     * 获取评估任务列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        //根据资源权限获取单位集合
        List<String> corpIds = corpService.getUserCorpIds(CreditRatingConstant.CREDIT_EVALUATION_ANALYSIS_APPLY, user.getId());
        List<String> taskNo = null;
        //查询条件机构名称特殊处理
        if (dataTablesInput.getFormdata().containsKey("likes_orgName")) {
            //机构名称
            String orgName = dataTablesInput.getFormdata().get("likes_orgName").toString();
            if (StringUtils.isNotEmpty(orgName)) {
                //根据机构名称模糊查询获取机构评价任务
                List<CrTaskOrgInfoVo> taskOrgInfoList = evaluationAnalysisDao.getCrTaskOrgInfoVoByOrgName(orgName);
                //获取机构评价任务编号
                taskNo = taskOrgInfoList.stream().map(CrTaskOrgInfoVo::getTaskNo).distinct().collect(Collectors.toList());
                dataTablesInput.getFormdata().remove("likes_orgName");
                if (EmptyUtils.isEmpty(taskNo)) {
                    DataTablesOutput newDate = new DataTablesOutput();
                    return newDate;
                }
            }
        }
        DataTablesOutput dataTablesOutput = evaluationAnalysisDao.getDataTablesUnion(dataTablesInput, corpIds, taskNo);
        List<CrTaskCorpInfoVo> list = dataTablesOutput.getData();
        if (EmptyUtils.isNotEmpty(list)) {
            //重新封装数据
            this.setData(list);
        }
        return dataTablesOutput;
    }

    /**
     * 组装数据
     *
     * @param list
     */
    public void setData(List<CrTaskCorpInfoVo> list) {
        Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
        //获取任务编号
        List<String> taskNo = list.stream().map(CrTaskCorpInfoVo::getTaskNo).distinct().collect(Collectors.toList());
        //根据评价任务编号获取机构评价任务
        List<CrTaskOrgInfoVo> taskOrgInfoList = evaluationAnalysisDao.getCrTaskOrgInfoVoByTaskNo(taskNo);
        if (EmptyUtils.isNotEmpty(taskOrgInfoList)) {
            //根据任务编号对数据分组
            Map<String, List<CrTaskOrgInfoVo>> taskOrgMap = taskOrgInfoList.stream().collect(Collectors.groupingBy(CrTaskOrgInfoVo::getTaskNo));
            for (CrTaskCorpInfoVo crTaskCorpInfoVo : list) {
                if (EmptyUtils.isNotEmpty(taskOrgInfoList)) {
                    if (taskOrgMap.containsKey(crTaskCorpInfoVo.getTaskNo())) {
                        crTaskCorpInfoVo.setOrgName(taskOrgMap.get(crTaskCorpInfoVo.getTaskNo()).stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
                    }
                }

                String orgTypeName = "";
                if(crTaskCorpInfoVo.getOrgType()!=null){
                    String[] split = crTaskCorpInfoVo.getOrgType().split(",");
                    for (int i = 0; i < split.length; i++) {
                        if (institutionaType.containsKey(split[i])) {
                            orgTypeName += institutionaType.get(split[i]).getName();
                        }
                        if (i < split.length - 1) {
                            orgTypeName += ",";
                        }
                    }
                    crTaskCorpInfoVo.setOrgType(orgTypeName);
                }
            }
        }
    }

    /**
     * 信用评估分析
     *
     * @param taskNo
     * @param model
     */
    public void evaluate(String taskNo, Model model, String taskId) {
        //获取评分任务基础信息
        CrTaskInfoVO crTaskInfoVO = crTaskInfoService.getTaskByNo(taskNo);
        //获取评分任务单位信息
        List<CrTaskCorpInfoVo> taskCorpInfoList = crTaskCorpService.getListTaskNoByTaskNo(taskNo);
        CrTaskInfoVO crTaskCorpInfo = evaluationAnalysisService.getCrTaskInfoById(taskId);
        List<CrTaskOrgInfoVo> taskOrgInfoVoList = crTaskOrgService.getListByTaskNo(taskNo);
        //将taskCorpInfoList,taskOrgInfoVoList的单位信息和机构信息拼接成字符串
        String orgIds = taskOrgInfoVoList.stream().map(CrTaskOrgInfoVo::getOrgId).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
        String orgNames = taskOrgInfoVoList.stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
//        String corpIds = taskCorpInfoList.stream().map(CrTaskCorpInfoVo::getCorpId).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
//        String corpName = taskCorpInfoList.stream().map(CrTaskCorpInfoVo::getCorpName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
        //给评级任务设置单位、机构信息
        if (crTaskInfoVO != null){
            crTaskInfoVO.setOrgIds(orgIds);
            crTaskInfoVO.setOrgName(orgNames);
        }
        //只展示当前评价的组织
        if (crTaskCorpInfo != null){
            crTaskInfoVO.setCorpId(crTaskCorpInfo.getCorpId());
            crTaskInfoVO.setCorpName(crTaskCorpInfo.getCorpName());
        }
        //重新封装数据
        Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
        String orgTypeName = "";
        if(crTaskInfoVO.getOrgType()!=null) {
            String[] split = crTaskInfoVO.getOrgType().split(",");
            for (int i = 0; i < split.length; i++) {
                if (institutionaType.containsKey(split[i])) {
                    orgTypeName += institutionaType.get(split[i]).getName();
                }
                if (i < split.length - 1) {
                    orgTypeName += ",";
                }
            }
        }
        crTaskInfoVO.setOrgTypeName(orgTypeName);
        model.addAttribute("crTaskInfo", crTaskInfoVO);
        model.addAttribute("taskNo", taskNo);


    }


    /**
     * 数据保存
     *
     * @param crmComputationalModelVO      计算模型
     * @param taskCorpInfoList             单位评价任务列表
     * @param scoringIndicatorSolidificati 计算模型评分指标
     * @param orgList                      机构信息集合
     * @param taskNo                       评价任务编号
     * @param scoringGrade                 评级标准
     * @param crmQuantitative              计算模型固化指标数据的定量表
     */
    public void save(CrmComputationalModelVO crmComputationalModelVO, List<CrTaskCorpInfoVo> taskCorpInfoList,
                     List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificati, List<InstitutionDetailsVo> orgList,
                     String taskNo, CrmScoringGradeVO scoringGrade, List<CrmQuantitativeMethodForSisVO> crmQuantitative) {
        //评估批次
        EvaluationBatch evaluationBatch = new EvaluationBatch();
        //评估批次号
        evaluationBatch.setEvaluationBatchNumber(SequenceSingleton.getSequence(SequenceEnum.EVALUATION_BATCH));
        //评价任务编号
        evaluationBatch.setTaskNo(taskNo);
        //创建人
        //evaluationBatch.setCreator(user.getUsername());
        //创建日期
        evaluationBatch.setCreateDate(DateUtils.getDate());
        evaluationAnalysisDao.save(evaluationBatch);

        //报告中计算模型固化
        ModelSolidificationReport modelSolidificationReport = new ModelSolidificationReport();
        //评估报告主键
        //modelSolidificationReport.setEvaluationReportId(evaluationReport.getId());
        //模型批次号
        modelSolidificationReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
        //模型公式编号
        modelSolidificationReport.setModelFormulaNumber(crmComputationalModelVO.getModelFormulaNumber());
        //模型公式名称
        modelSolidificationReport.setModelFormulaName(crmComputationalModelVO.getModelFormulaName());
        //模型公式说明
        modelSolidificationReport.setModelFormulaDescription(crmComputationalModelVO.getModelDescription());
        //评分等级
        //modelSolidificationReport.setRatingLevel("");
        //计算模型名称
        modelSolidificationReport.setModelName(crmComputationalModelVO.getModelName());
        //计算模型代码
        modelSolidificationReport.setModelCode(crmComputationalModelVO.getModelCode());
        //计算模型说明
        modelSolidificationReport.setModelDescription(crmComputationalModelVO.getModelDescription());
        //评估批次号
        modelSolidificationReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
        //创建人
        //modelSolidificationReport.setCreator(user.getUsername());
        //创建日期
        modelSolidificationReport.setCreateDate(DateUtils.getDate());
        evaluationAnalysisDao.save(modelSolidificationReport);

        List<ScoringIndicatorFixReport> scoringIndicatorFixReportList = new ArrayList<>();
        List<QMScoringIFixReport> qMScoringIFixReportList = new ArrayList<>();
        for (CrmScoringIndicatorSolidificatiVO crmScoringIndicatorSolidificatiVO : scoringIndicatorSolidificati) {
            //报告中评分指标固化
            ScoringIndicatorFixReport scoringIndicatorFixReport = new ScoringIndicatorFixReport();
            //报告中计算模型固化主键
            scoringIndicatorFixReport.setModelSolidificationId(modelSolidificationReport.getId());
            //评估报告主键
            //scoringIndicatorFixReport.setEvaluationReportId(evaluationReport.getId());
            //模型批次号
            scoringIndicatorFixReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
            //评估批次号
            scoringIndicatorFixReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
            //评分指标名称
            scoringIndicatorFixReport.setScoringIndicatorName(crmScoringIndicatorSolidificatiVO.getScoringIndicatorName());
            //评分指标代码
            scoringIndicatorFixReport.setScoringIndicatorCode(crmScoringIndicatorSolidificatiVO.getScoringIndicatorCode());
            //评分指标说明
            scoringIndicatorFixReport.setScoringIndicatorDescription(crmScoringIndicatorSolidificatiVO.getScoringIndicatorDescription());
            //评分指标系数
            scoringIndicatorFixReport.setScoringIndicatorCoefficient(crmScoringIndicatorSolidificatiVO.getScoringIndicatorCoefficient());
            //指标类别名称
            //scoringIndicatorFixReport.setCategoryName("");
            //指标类别代码
            //scoringIndicatorFixReport.setCategoryEncoding("");
            //报告中指标类别固化id
            //scoringIndicatorFixReport.setIcfrId("");
            //版本号
            scoringIndicatorFixReport.setVersionNumber(crmScoringIndicatorSolidificatiVO.getVersionNumber());
            //数据源id
            scoringIndicatorFixReport.setDataSourceId(crmScoringIndicatorSolidificatiVO.getDataSourceId());
            //数据源名称
            scoringIndicatorFixReport.setDataSourceName(crmScoringIndicatorSolidificatiVO.getDataSourceName());
            //数据源代码
            scoringIndicatorFixReport.setDataSourceCode(crmScoringIndicatorSolidificatiVO.getDataSourceCode());
            //数据源说明
            scoringIndicatorFixReport.setDataSourceOrigin(crmScoringIndicatorSolidificatiVO.getDataSourceOrigin());
            //指标层级
            scoringIndicatorFixReport.setLevel_(crmScoringIndicatorSolidificatiVO.getLevel());
            //评分指标父id
            scoringIndicatorFixReport.setParentId(crmScoringIndicatorSolidificatiVO.getParentId());
            //评分指标id
            scoringIndicatorFixReport.setScoringIndicatorId(crmScoringIndicatorSolidificatiVO.getScoringIndicatorId());
            //评分细则
            scoringIndicatorFixReport.setScoringRubric(crmScoringIndicatorSolidificatiVO.getScoringRubric());
            evaluationAnalysisDao.save(scoringIndicatorFixReport);
            for (CrmQuantitativeMethodForSisVO crmQuantitativeMethodForSisVO : crmQuantitative) {
                if (crmScoringIndicatorSolidificatiVO.getId().equals(crmQuantitativeMethodForSisVO.getScoringIndicatorSolId())) {
                    QMScoringIFixReport qMScoringIFixReport = new QMScoringIFixReport();
                    //模型批次号
                    qMScoringIFixReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
                    //评估批次号
                    qMScoringIFixReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
                    //评分指标名称
                    qMScoringIFixReport.setScoringIndicatorName(crmQuantitativeMethodForSisVO.getScoringIndicatorName());
                    //评分指标代码
                    qMScoringIFixReport.setScoringIndicatorCode(crmQuantitativeMethodForSisVO.getScoringIndicatorCode());
                    //评分指标说明
                    qMScoringIFixReport.setScoringIndicatorDescription(crmQuantitativeMethodForSisVO.getScoringIndicatorDescription());
                    //数值
                    qMScoringIFixReport.setValue(crmQuantitativeMethodForSisVO.getValue());
                    //数值名称
                    qMScoringIFixReport.setValueName(crmQuantitativeMethodForSisVO.getValueName());
                    //数值名称代码
                    qMScoringIFixReport.setValueNameCode(crmQuantitativeMethodForSisVO.getValueNameCode());
                    //报告中评分指标固化主键
                    qMScoringIFixReport.setSifrId(scoringIndicatorFixReport.getId());
                    //额外数值
                    qMScoringIFixReport.setOtherValue(crmQuantitativeMethodForSisVO.getOtherValue());
                    //版本号
                    qMScoringIFixReport.setVersionNumber(crmQuantitativeMethodForSisVO.getVersionNumber());
                    //数值对应得分细则符号
                    qMScoringIFixReport.setValueDirection(crmQuantitativeMethodForSisVO.getValueDirection());
                    //额外数值对应得分细则符号
                    qMScoringIFixReport.setOtherValueDirection(crmQuantitativeMethodForSisVO.getOtherValueDirection());
                    //得分
                    qMScoringIFixReport.setGrade(crmQuantitativeMethodForSisVO.getGrade());
                    qMScoringIFixReportList.add(qMScoringIFixReport);
                }
            }
            scoringIndicatorFixReportList.add(scoringIndicatorFixReport);
        }

        if (EmptyUtils.isNotEmpty(qMScoringIFixReportList)) {
            evaluationAnalysisDao.batchSave(qMScoringIFixReportList);
        }

        //报告中模型公式固化
        ModelFormulaFixReport modelFormulaFixReport = new ModelFormulaFixReport();
        //报告中计算模型固化主键
        modelFormulaFixReport.setModelSolidificationId(modelSolidificationReport.getId());
        //评估报告主键
        //modelFormulaFixReport.setEvaluationReportId(evaluationReport.getId());
        //模型批次号
        modelFormulaFixReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
        //评估批次号
        modelFormulaFixReport.setEvaluationNatchNumber(evaluationBatch.getEvaluationBatchNumber());
        //模型公式名称
        modelFormulaFixReport.setModelFormulaName(crmComputationalModelVO.getModelFormulaName());
        //模型公式编号
        modelFormulaFixReport.setModelFormulaNumber(crmComputationalModelVO.getModelFormulaNumber());
        //模型公式说明
        modelFormulaFixReport.setModelMormulaDescription(crmComputationalModelVO.getModelDescription());
        evaluationAnalysisDao.save(modelFormulaFixReport);


        //报告中评分等级固化
        ScoringGradeFixReport scoringGradeFixReport = new ScoringGradeFixReport();
        //scoringGradeFixReport.setEvaluationReportId();
        //模型批次号
        scoringGradeFixReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
        //计算模型固化id
        scoringGradeFixReport.setModelSolidificationId(modelSolidificationReport.getId());
        //计分制
        scoringGradeFixReport.setScoringSystem(scoringGrade.getScoringSystem());
        //评估批次号
        scoringGradeFixReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
        //评分等级名称
        scoringGradeFixReport.setScoringGradeName(scoringGrade.getScoringGradeName());
        //评分等级编码
        scoringGradeFixReport.setScoringGradeCode(scoringGrade.getScoringGradeCode());
        //机构类别
        scoringGradeFixReport.setInstitutionType(scoringGrade.getInstitutionType());
        evaluationAnalysisDao.save(scoringGradeFixReport);

        //获取评分等级明细
        List<CrmScoringGradeLevelVO> gradeLevels = scoringGrade.getGradeLevels();
        List<ScoringGradeLeFixReport> scoringGradeLeFixReportList = new ArrayList<>();
        for (CrmScoringGradeLevelVO crmScoringGradeLevelVO : gradeLevels) {
            ScoringGradeLeFixReport scoringGradeLeFixReport = new ScoringGradeLeFixReport();
            //模型批次号
            scoringGradeLeFixReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
            //评估批次号
            scoringGradeLeFixReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
            //报告中评分等级固化主键
            scoringGradeLeFixReport.setScoringGradeFixReportId(scoringGradeFixReport.getId());
            //计算模型固化主键
            scoringGradeLeFixReport.setModelSolidificationId(modelSolidificationReport.getId());
            //评分级别
            scoringGradeLeFixReport.setScoringLevel(crmScoringGradeLevelVO.getScoringLevel());
            //评分级别上限分
            scoringGradeLeFixReport.setUpperScoreLimitLevel(crmScoringGradeLevelVO.getUpperScoreLimitLevel());
            //评分级别下限分
            scoringGradeLeFixReport.setLowerScoreLimitLevel(crmScoringGradeLevelVO.getLowerScoreLimitLevel());
            //评分级别说明
            scoringGradeLeFixReport.setExplanationOfScoringLevel(crmScoringGradeLevelVO.getExplanationOfScoringLevel());
            //上限等级符号
            scoringGradeLeFixReport.setUpScoringSymbol(crmScoringGradeLevelVO.getUpScoringSymbol());
            //下限等级符号
            scoringGradeLeFixReport.setDownScoringSymbol(crmScoringGradeLevelVO.getDownScoringSymbol());

            scoringGradeLeFixReportList.add(scoringGradeLeFixReport);
        }
        if (EmptyUtils.isNotEmpty(scoringGradeLeFixReportList)) {
            evaluationAnalysisDao.batchSave(scoringGradeLeFixReportList);
        }

        //评估报告、评估数据、评分指标值入库
        for (CrTaskCorpInfoVo crTaskCorpInfoVo : taskCorpInfoList) {
            //遍历评估对象
            for (InstitutionDetailsVo institutionDetailsVo : orgList) {
                //评估报告
                EvaluationReport evaluationReport = new EvaluationReport();
                //评估批次主键
                evaluationReport.setEvaluationBatchId(evaluationBatch.getId());
                //评估批次号
                evaluationReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
                //模型批次号
                evaluationReport.setModelBatchNumber(crmComputationalModelVO.getBatchNumber());
                //报告中计算模型固化主键
                evaluationReport.setModelSolidificationId(crmComputationalModelVO.getId());
                //计算模型名称
                evaluationReport.setModelName(crmComputationalModelVO.getModelName());
                //计算模型代码
                evaluationReport.setModelCode(crmComputationalModelVO.getModelCode());
                //计算模型说明
                evaluationReport.setModelDescription(crmComputationalModelVO.getModelDescription());
                //已评数据主键
                evaluationReport.setEvaluatedDataId(institutionDetailsVo.getInsId());
                //数据名称
                evaluationReport.setDataName(institutionDetailsVo.getInstitutionName());
                //数据代码
                evaluationReport.setDataCode(institutionDetailsVo.getInstitutionCode());
                //数据来源类别名称
                evaluationReport.setDataSourceCategoryName(institutionDetailsVo.getDataSourceCategoryName());
                //数据来源类别代码
                evaluationReport.setDataSourceCategoryCode(institutionDetailsVo.getDataSourceCategoryCode());
                //创建人
                //evaluationReport.setCreator(user.getUsername());
                //创建日期
                evaluationReport.setCreateDate(DateUtils.getDate());
                evaluationAnalysisDao.save(evaluationReport);

                //报告中已评数据（原始数据固化）
                EvaluatedDataForReport evaluatedDataForReport = new EvaluatedDataForReport();
                //信用分
                //evaluatedDataForReport.setCreditScore(0);
                //信用等级
                //evaluatedDataForReport.setCreditGrade("");
                evaluatedDataForReport.setDataType(institutionDetailsVo.getInstitutionType());
                //数据名称
                evaluatedDataForReport.setDataName(institutionDetailsVo.getInstitutionName());
                //数据代码
                evaluatedDataForReport.setDataCode(institutionDetailsVo.getInstitutionCode());
                //数据来源业务表主建
                evaluatedDataForReport.setDataId(institutionDetailsVo.getInsId());
                //数据来源类别名称
                evaluatedDataForReport.setDataSourceCategoryName(institutionDetailsVo.getDataSourceCategoryName());
                //数据来源类别代码
                evaluatedDataForReport.setDataSourceCategoryCode(institutionDetailsVo.getDataSourceCategoryCode());
                //评估报告主键
                evaluatedDataForReport.setEvaluationReportId(evaluationReport.getId());
                //评估批次号
                evaluatedDataForReport.setEvaluationBatchNumber(evaluationBatch.getEvaluationBatchNumber());
                //创建人
                //evaluatedDataForReport.setCreator(user.getUsername());
                //创建日期
                evaluatedDataForReport.setCreateDate(DateUtils.getDate());
                evaluatedDataForReport.setCorpId(crTaskCorpInfoVo.getCorpId());
                if (StringUtils.isBlank(crTaskCorpInfoVo.getCorpName())) {
                    CorpDTO corp = corpService.get(crTaskCorpInfoVo.getCorpId());
                    evaluatedDataForReport.setCorpName(corp.getName());
                } else {
                    evaluatedDataForReport.setCorpName(crTaskCorpInfoVo.getCorpName());
                }

                evaluationAnalysisDao.save(evaluatedDataForReport);

                //评分指标固化对象集合
                List<ScoringMetricValFixReport> scoringMetricValFixReportList = new ArrayList<>();
                for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : crTaskCorpInfoVo.getScoringIndicatorList()) {
                    //报告中评分指标值固化
                    ScoringMetricValFixReport scoringMetricValFixReport = new ScoringMetricValFixReport();
                    //评分指标名称
                    scoringMetricValFixReport.setScoringIndicatorName(ctrScoringCorpContrastVo.getScoringIndicatorName());
                    //评分指标编码
                    scoringMetricValFixReport.setScoringIndicatorCode(ctrScoringCorpContrastVo.getScoringIndicatorCode());
                    //评分指标说明
                    scoringMetricValFixReport.setScoringIndicatorDescription(ctrScoringCorpContrastVo.getScoringIndicatorDescription());
                    //指标类别名称
                    //scoringMetricValFixReport.setCategoryName("");
                    //指标类别代码
                    //scoringMetricValFixReport.setCategoryEncoding("");
                    //外键（报告中已评数据主键）
                    scoringMetricValFixReport.setEvaluatedDataId(evaluatedDataForReport.getId());

                    List<OrgScoreVO> orgScoreList = ctrScoringCorpContrastVo.getOrgScoreList();
                    if (EmptyUtils.isNotEmpty(orgScoreList)) {
                        for (OrgScoreVO orgScoreVO : orgScoreList) {
                            if (institutionDetailsVo.getInsId().equals(orgScoreVO.getOrgId())) {
                                //指标值
                                scoringMetricValFixReport.setIndicatorValue(orgScoreVO.getDataSourceValue());
                                //指标得分
                                scoringMetricValFixReport.setScore(orgScoreVO.getScore());
                            }
                        }
                    }
                    scoringMetricValFixReportList.add(scoringMetricValFixReport);
                }
                if (EmptyUtils.isNotEmpty(scoringMetricValFixReportList)) {
                    evaluationAnalysisDao.batchSave(scoringMetricValFixReportList);
                }
            }
        }
    }

    /**
     * 根据组织评价任务明细id获取评价任务信息
     *
     * @param id
     * @return
     */
    public CrTaskInfoVO getCrTaskInfoById(String id) {
        //根据评价任务id获取任务详细信息
        CrTaskInfoVO crTaskInfoVO = evaluationAnalysisDao.getCrTaskInfoById(id);
        Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
        List<String> taskNos = new ArrayList<>();
        taskNos.add(crTaskInfoVO.getTaskNo());
        //根绝任务编号获取评价机构
        List<CrTaskOrgInfoVo> taskOrgInfoList = evaluationAnalysisDao.getCrTaskOrgInfoVoByTaskNo(taskNos);
        if (EmptyUtils.isNotEmpty(taskOrgInfoList)) {
            crTaskInfoVO.setOrgName(taskOrgInfoList.stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
        }
        String orgType = crTaskInfoVO.getOrgType();
        String orgTypeName = "";
        String[] split = orgType.split(",");
        for (int i = 0; i < split.length; i++) {
            if (institutionaType.containsKey(split[i])) {
                orgTypeName += institutionaType.get(split[i]).getName();
            }
            if (i < split.length - 1) {
                orgTypeName += ",";
            }
        }
        crTaskInfoVO.setOrgTypeName(orgTypeName);

        return crTaskInfoVO;
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CreWorkFlowConstant.CREDIT_ORG_EVALUATION, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "ctci.id", true);


        List<String> taskNo = null;
        //查询条件机构名称特殊处理
        if (dataTablesInput.getFormdata().containsKey("likes_orgName")) {
            //机构名称
            String orgName = dataTablesInput.getFormdata().get("likes_orgName").toString();
            if (StringUtils.isNotEmpty(orgName)) {
                //根据机构名称模糊查询获取机构评价任务
                List<CrTaskOrgInfoVo> taskOrgInfoList = evaluationAnalysisDao.getCrTaskOrgInfoVoByOrgName(orgName);
                //获取机构评价任务编号
                taskNo = taskOrgInfoList.stream().map(CrTaskOrgInfoVo::getTaskNo).distinct().collect(Collectors.toList());
                dataTablesInput.getFormdata().remove("likes_orgName");
                if (EmptyUtils.isEmpty(taskNo)) {
                    DataTablesOutput newDate = new DataTablesOutput();
                    return newDate;
                }
            }
        }

        DataTablesOutput dp = evaluationAnalysisDao.dataTableApprove(dataTablesInput, businessSql,taskNo);
        List<CrTaskInfoVO> list = dp.getData();
        if (EmptyUtils.isNotEmpty(list)) {
            //重新封装数据
            Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
            for  (CrTaskInfoVO crTaskInfoVO :list) {
                String orgTypeName = "";
                String orgName="";
                if(crTaskInfoVO.getOrgType()!=null) {
                    String[] split = crTaskInfoVO.getOrgType().split(",");
                    for (int i = 0; i < split.length; i++) {
                        if (institutionaType.containsKey(split[i])) {
                            orgTypeName += institutionaType.get(split[i]).getName();
                        }
                        if (i < split.length - 1) {
                            orgTypeName += ",";
                        }
                    }
                }
                List<CrTaskOrgInfoVo> orgInfoVoList=crTaskOrgService.getListByTaskNo(crTaskInfoVO.getTaskNo());
                for (int i = 0; i < orgInfoVoList.size(); i++) {
                    CrTaskOrgInfoVo vo=orgInfoVoList.get(i);
                    orgName += vo.getOrgName();
                    if (i < orgInfoVoList.size() - 1) {
                        orgName += ",";
                    }
                }
                crTaskInfoVO.setOrgType(orgTypeName);
                crTaskInfoVO.setOrgName(orgName);
            }
        }
        return workFlowBusiness.merge(dp);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();

        CrTaskCorpInfo crTaskCorpInfo = this.get(workFlowResult.getBusinessKey(), true);
        String opType = "";
        String msg = "";
        //审批通过
        if (workFlowResult.isPass()) {
            crTaskCorpInfo.setStatus(CreditRatingConstant.APPROVED);
            opType = CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_CODE;
//            msg = CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_DESCRIPTION;
            msg = CreditRatingConstant.EVALUATION_LOG_LANG_APPROVAL;
        } else {
            //审批打回
            crTaskCorpInfo.setStatus(CreditRatingConstant.APPROVAL_REJECTED);
            opType = CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE;
//            msg = CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_DESCRIPTION;
            msg = CreditRatingConstant.EVALUATION_LOG_LANG_APPROVALBACK;
        }
        //更新评价组织任务表状态
        this.updateEntity(crTaskCorpInfo);

        if(workFlowResult.isPass()){
            if( !indicatorMap.containsKey(crTaskCorpInfo.getTaskNo())){
                //根据评价任务批次号获取组织评价任务明细
                List<CrTaskCorpInfoVo> taskList = evaluationAnalysisDao.getCrTaskCorpInfoByTaskNo(crTaskCorpInfo.getTaskNo());
                if (!EmptyUtils.isEmpty(taskList)) {
                    //评价组织合计数量
                    Integer totalNum = taskList.size();
                    //评价组织审批通过数量
                    Long longPassNum = taskList.stream().filter(item -> item.getStatus() == 95).count();
                    Integer passNum = longPassNum.intValue();
                    indicatorMap.put(crTaskCorpInfo.getTaskNo(),totalNum-passNum);
                }
            }
                Integer num = indicatorMap.get(crTaskCorpInfo.getTaskNo());
                if(num-1==0){
                    //更新评价机构信息
                    this.finalScoreLevel(crTaskCorpInfo.getTaskNo());
                }else {
                    indicatorMap.put(crTaskCorpInfo.getTaskNo(),num-1);
                }

        }

        //添加操作日志
        this.saveMessage(crTaskCorpInfo.getId(), opType, msg, user);
    }

    public void finalScoreLevel(String taskNo){
        //获取固化原数据
        List<EvaluatedDataForReportVO> evaluatedDataForReportList = evaluationAnalysisDao.getEvaluatedDataForReport(taskNo);

        //获取评估批次号
        String evaluationBatchNumber = evaluatedDataForReportList.get(0).getEvaluationBatchNumber();
        List<String> dataIds = evaluatedDataForReportList.stream().map(o -> o.getId()).collect(Collectors.toList());

        //评级标准模板、评分等级固化集合
        List<CrmSectionDataVO> crmSectionDataVOS = this.getscoringGradeLeFixReport(evaluationBatchNumber);

        //获取固化评分指标值,得分
        List<ScoringMetricValFixReportVO> scoringMetricValFixReportList = evaluationAnalysisDao.getScoringMetricValFixReport(dataIds);
        //获取计算模型选定的评分指标,权重
        List<ScoringIndicatorFixReportVO> scoringIndicatorFixReportList = evaluationAnalysisDao.getScoringIndicatorFixReport(evaluationBatchNumber);

        //将指标按固化数据id分组
        Map<String, List<ScoringMetricValFixReportVO>> scoringMetricValFixReportMap = scoringMetricValFixReportList.stream().collect(Collectors.groupingBy(ScoringMetricValFixReportVO::getEvaluatedDataId));
        for (EvaluatedDataForReportVO evaluatedDataForReportVO : evaluatedDataForReportList) {
            for (String key : scoringMetricValFixReportMap.keySet()) {
                if(evaluatedDataForReportVO.getId().equals(key)){
                    evaluatedDataForReportVO.setScoringMetricValFixReportList(scoringMetricValFixReportMap.get(key));
                }
            }

        }
        this.calculateScore(crmSectionDataVOS,evaluatedDataForReportList,scoringIndicatorFixReportList, taskNo);
    }


    /**
     * 根据评价批次号获取评级标准模板、评分等级固化
     * @return
     */
    public List<CrmSectionDataVO> getscoringGradeLeFixReport(String evaluationBatchNumber){
        List<ScoringGradeLeFixReportVO> scoringGradeLeFixReportList = evaluationAnalysisDao.getScoringGradeLeFixReport(evaluationBatchNumber);
        return this.makeGradeSectionList(scoringGradeLeFixReportList);

    }

    public List<CrmSectionDataVO> makeGradeSectionList(List<ScoringGradeLeFixReportVO> ScoringGradeLeFixReportVOS){
        List<CrmSectionDataVO> sectionDataVOList=new ArrayList<>();
        for(ScoringGradeLeFixReportVO levelVO:ScoringGradeLeFixReportVOS){
            CrmSectionDataVO crmSectionDataVO=new CrmSectionDataVO();
            crmSectionDataVO.setLeftValue(levelVO.getLowerScoreLimitLevel());//左值
            crmSectionDataVO.setRightValue(levelVO.getUpperScoreLimitLevel());//右值
            //左边符号
            if("(".equals(levelVO.getDownScoringSymbol())){//小于
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_SYMBOL);
            }else if("[".equals(levelVO.getDownScoringSymbol())){//小于等于
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
            //最高级别（开放上界，upperScoreLimitLevel 为空）下限取等：满分=下限时被开区间"("排除会导致评价等级为-9999
            if(levelVO.getUpperScoreLimitLevel() == null){
                log.info("评价等级-9999处理");
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
            //右边符号
            if(")".equals(levelVO.getUpScoringSymbol())){//小于
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_SYMBOL);
            }else if("]".equals(levelVO.getUpScoringSymbol())){//小于等于
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
            crmSectionDataVO.setScoringLevel(levelVO.getScoringLevel());
            sectionDataVOList.add(crmSectionDataVO);
        }
        return sectionDataVOList;
    }



    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    /**
     * 根据任务编号创建评估报告
     *
     * @param crTaskInfo
     */
    public void createEvaluationReport(CrTaskInfo crTaskInfo) {
        //评价任务编号
        String taskNo = crTaskInfo.getTaskNo();
        //根据评价任务获取评价单位集合
        List<CrTaskCorpInfoVo> taskCorpInfoList = evaluationAnalysisDao.getCrTaskCorpInfoByTaskNo(crTaskInfo.getTaskNo());
        //根据评分任务编号获取任务评分指标
        List<CtrScoringCorpContrastVo> scoringCorpList = corpContrastService.getTaskScoring(taskNo);
        //根据计算模型获取评分指标信息集合
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificati = crmScoringIndicatorSolidService.getIndicatorListByModelId(crTaskInfo.getComputationalModelId());
        //按评分指标id分组
        Map<String, CrmScoringIndicatorSolidificatiVO> indicatorMap = scoringIndicatorSolidificati.stream().collect(Collectors.toMap(CrmScoringIndicatorSolidificatiVO::getScoringIndicatorId, Function.identity()));
        //遍历任务评分指标，根据评分指标id关联评分指标基础信息
        for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : scoringCorpList) {
            if (indicatorMap.containsKey(ctrScoringCorpContrastVo.getScoringIndicatorSolidificati())) {
                ctrScoringCorpContrastVo.setIndicatorInfo(indicatorMap.get(ctrScoringCorpContrastVo.getScoringIndicatorSolidificati()));
            }
        }
        Map<String, List<CtrScoringCorpContrastVo>> corpIndicatorMap =ctrScoringCorpContrastVoorderbycorpId(scoringCorpList);
        List<CrTaskOrgInfoVo> taskOrgInfoList = evaluationAnalysisDao.getCrTaskOrgInfoVoByTaskNo(new ArrayList<>(Collections.singletonList(taskNo)));
        //遍历评分机构列表获取机构id列表
        List<String> orgIds = taskOrgInfoList.stream().map(CrTaskOrgInfoVo::getOrgId).distinct().collect(Collectors.toList());
        //遍历单位获取单位自动评分指标，指标数据源值，计算指标得分
        for (CrTaskCorpInfoVo crTaskCorpInfoVo : taskCorpInfoList) {
            String corpId = crTaskCorpInfoVo.getCorpId();
            //单位id获取单位评分指标集合
            if (corpIndicatorMap.containsKey(corpId)) {
                List<CtrScoringCorpContrastVo> scoringIndicatorList = corpIndicatorMap.get(corpId);
                //根据单位id，机构id,评估开始时间,评价结束时间,评分指标获取评分指标数据源
                this.getIndicatorDataSourceValue(corpId, orgIds, scoringIndicatorList,crTaskInfo.getEvaluateStartDate(),crTaskInfo.getEvaluateEndDate());
                //计算评分指标得分
                this.calculateIndicatorScore(scoringIndicatorList);
                crTaskCorpInfoVo.setScoringIndicatorList(scoringIndicatorList);
            }
        }
        //根据计算模型id获取计算模型信息
        CrmComputationalModelVO crmComputationalModelVO = crmComputationalModelService.getComputationalModelById(crTaskInfo.getComputationalModelId());
        //根据机构id获取机构信息集合
        List<InstitutionDetailsVo> orgList = crdRawDataService.findInstitutionByIds(orgIds);
        //根据机构类别获取对应有效的评分标准（包括评级明细）
        CrmScoringGradeVO scoringGrade = crmScoringGradeService.getScoringGradeByItype(crTaskInfo.getOrgType());
        //根据计算模型id获取所有对应的固化指标数据的定量表数据
        List<CrmQuantitativeMethodForSisVO> crmQuantitative = crmScoringIndicatorSolidService.getCrmQuantitativeByModelId(crmComputationalModelVO.getId());

        //保存数据
        this.save(crmComputationalModelVO, taskCorpInfoList, scoringIndicatorSolidificati, orgList, taskNo, scoringGrade, crmQuantitative);
    }

    public Map<String, List<CtrScoringCorpContrastVo>> ctrScoringCorpContrastVoorderbycorpId ( List<CtrScoringCorpContrastVo> scoringCorpList){
        //需要深拷贝，后面会修改集合
        List<CtrScoringCorpContrastVo> deepCopiedList = scoringCorpList.stream()
                .map(sc -> SerializationUtils.clone(sc))
                .collect(Collectors.toList());
        //评分指标按单位分组
        Map<String, List<CtrScoringCorpContrastVo>> corpIndicatorMap =deepCopiedList.stream().collect(Collectors.groupingBy(scoringCorp -> scoringCorp.getCorpId()));
        return corpIndicatorMap;

    }

    /**
     * 根据单位id，机构id,评分指标获取评分指标数据源
     *
     * @param corpId
     * @param orgIds
     * @param scoringCorpContrastList
     * @param evaluateStartDate
     * @param evaluateEndDate
     */
    public void getIndicatorDataSourceValue(String corpId, List<String> orgIds, List<CtrScoringCorpContrastVo> scoringCorpContrastList,Date evaluateStartDate, Date evaluateEndDate) {
        Map<String, BigDecimal> dataSourceMap = new HashMap<>();
        //遍历评分指标，获取指标数据源值
        for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : scoringCorpContrastList) {
           //防止从表中取出的自动评分数据是BigDecimal类型，转成字符串常量
            if (StringUtils.isNotBlank(ctrScoringCorpContrastVo.getIsAutoScoring())){
                ctrScoringCorpContrastVo.setIsAutoScoring(ctrScoringCorpContrastVo.getIsAutoScoring().replaceAll("(\\.0+)$", ""));
            }
            //校验评分指标是否自动评分
            if (CommonConstant.YES.equals(ctrScoringCorpContrastVo.getIsAutoScoring())) {
                CrmScoringIndicatorSolidificatiVO indicatorInfo = ctrScoringCorpContrastVo.getIndicatorInfo();
                //根据指标代码获取指标数据源值
                switch (indicatorInfo.getDataSourceCode()) {
                    case CreditRatingConstant.CREDIT_DATASOURCE_CREDIT_EXPOSURE_LIMIT_CODE:
                        //授信敞口额度
                        dataSourceMap = creditLimitService.getCreditExposureLimit(corpId,orgIds, DateUtils.format(evaluateStartDate,"yyyy-MM-dd"), DateUtils.format(evaluateEndDate,"yyyy-MM-dd"));
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_CREDIT_LIMIT_CHANGE_CODE:
                        //授信的增减变化
                        dataSourceMap = creditLimitService.getCreditLimitChange(corpId,orgIds, DateUtils.format(evaluateStartDate,"yyyy-MM-dd"), DateUtils.format(evaluateEndDate,"yyyy-MM-dd"));
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_FINANCE_DOLLAR_CODE:
                        //融资成本(美元)
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_FINANCE_RMB_CODE:
                        //融资成本(人民币)
                        dataSourceMap = financingService.getFinancingdata(corpId,orgIds,evaluateStartDate,evaluateEndDate,"3");
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_BANK_LOAN_BALANCE_ODE:
                        //贷款余额
                        dataSourceMap=financingService.getFinancingdata(corpId,orgIds,evaluateStartDate,evaluateEndDate,"1");
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_BANK_ADD_LOAN_CODE:
                        //新增的贷款
                        dataSourceMap=financingService.getFinancingdata(corpId,orgIds,evaluateStartDate,evaluateEndDate,"2");
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_BANK_LOAN_RATE_CODE:
                        //贷款利率（人民币）
                        dataSourceMap=financingService.getFinancingdata(corpId,orgIds,evaluateStartDate,evaluateEndDate,"3");
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_LC_GUARANTEE_BILL_BOND_CODE:
                        //承兑、信用证业务保证金比例
                        dataSourceMap = lcGuaranteeBillBondService.getLcGuaranteeBillBond(corpId,orgIds, DateUtils.format(evaluateStartDate, DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.format(evaluateEndDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
                        break;
                    case CreditRatingConstant.CREDIT_DATASOURCE_CHO_DEPOSIT_RATE_CODE:
                        //存款利率（人民币）
                        dataSourceMap = choDepositRateService.getDepositRate(corpId,orgIds, DateUtils.format(evaluateStartDate, DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.format(evaluateEndDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
                        break;
                    case "10":
                        //理财利率平均值
                        dataSourceMap = choDepositRateService.getDepositRate(corpId,orgIds, DateUtils.format(evaluateStartDate), DateUtils.format(evaluateEndDate));
                        break;
                    default:

                }
                Map<String, BigDecimal> rankedMap =null;
                //排名法
                if (ctrScoringCorpContrastVo.getScoringMethod().compareTo(new BigDecimal(CreditRatingConstant.SCORING_METHOD_DESC))==0
                ||ctrScoringCorpContrastVo.getScoringMethod().compareTo(new BigDecimal(CreditRatingConstant.SCORING_METHOD_ASC))==0){
                    //如果没有需要给他默认给个0
                    for (String orgId : orgIds) {
                        if (!dataSourceMap.containsKey(orgId)) {
                            dataSourceMap.put(orgId,new BigDecimal("0.00"));
                        }
                    }
                    //默认降序
                    List<Map.Entry<String, BigDecimal>> sortedEntries = new ArrayList<>(dataSourceMap.entrySet());
                    Collections.sort(sortedEntries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
                    //如果是升序
                    if (ctrScoringCorpContrastVo.getScoringMethod().compareTo(new BigDecimal(CreditRatingConstant.SCORING_METHOD_ASC))==0){
                        sortedEntries = new ArrayList<>(dataSourceMap.entrySet());
                        Collections.sort(sortedEntries, (o1, o2) -> o1.getValue().compareTo(o2.getValue())); // 升序
                    }
                    rankedMap=new HashMap<>();
                    int currentRank = 1;
                    BigDecimal previousValue = null;
                    int position = 1; // 当前位置计数器
                    for (Map.Entry<String, BigDecimal> entry : sortedEntries) {
                        if (previousValue == null || entry.getValue().compareTo(previousValue) != 0) {
                            currentRank = position; // 不同值时更新排名
                        }
                        rankedMap.put(entry.getKey(), new BigDecimal(currentRank));
                        previousValue = entry.getValue();
                        position++;
                    }
                }

                // 初始化最大值和最小值
                BigDecimal maxValue = BigDecimal.ZERO;
                BigDecimal minValue = BigDecimal.ZERO;

                for (BigDecimal value : dataSourceMap.values()) {
                    if(null != value){
                        if (value.compareTo(maxValue) > 0) {
                            maxValue = value; // 更新最大值
                        }
                        if (value.compareTo(minValue) < 0) {
                            minValue = value; // 更新最小值
                        }
                    }
                }
                List<OrgScoreVO> orgScoreList = new ArrayList<>();
                for (String orgId : orgIds) {
                    OrgScoreVO orgScoreVO = new OrgScoreVO();
                    orgScoreVO.setOrgId(orgId);
                    orgScoreVO.setMaxValue(maxValue);
                    orgScoreVO.setMinValue(minValue);
                    if (dataSourceMap.containsKey(orgId)) {
                        if(null != dataSourceMap.get(orgId)){
                            orgScoreVO.setDataSourceValue(dataSourceMap.get(orgId));
                            if (rankedMap!=null){
                                orgScoreVO.setDataSourceValueOrder(rankedMap.get(orgId));
                            }
                        }else{
                            orgScoreVO.setDataSourceValue(new BigDecimal("0.00"));
                        }
                    }else {
                        orgScoreVO.setDataSourceValue(new BigDecimal("0.00"));
                    }
                    orgScoreList.add(orgScoreVO);
                }

                ctrScoringCorpContrastVo.setOrgScoreList(orgScoreList);
            }else{
                //防止后续报空指针
                ctrScoringCorpContrastVo.setOrgScoreList(new ArrayList<>());
            }
        }
    }

    /**
     * 计算指标得分
     *
     * @param ctrScoringCorpContrastList
     */
    public void calculateIndicatorScore(List<CtrScoringCorpContrastVo> ctrScoringCorpContrastList) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        //评分方法-归一法
        List<CtrScoringCorpContrastVo> normalizationList = ctrScoringCorpContrastList.stream().filter(item -> item.getScoringMethod() != null && CreditRatingConstant.SCORING_METHOD_GUIYI == item.getScoringMethod().intValue()).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(normalizationList)) {
            this.send(CreditRatingConstant.SCORING_METHOD_GUIYI, normalizationList, resultMap);
        }
        //分段法、排序法
        List<CtrScoringCorpContrastVo> subsectionList = ctrScoringCorpContrastList.stream().filter(item -> item.getScoringMethod() != null && CreditRatingConstant.SCORING_METHOD_FENDUAN == item.getScoringMethod().intValue()).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(subsectionList)) {
            this.send(CreditRatingConstant.SCORING_METHOD_FENDUAN, subsectionList, resultMap);
        }
        //排名法(降序)
        List<CtrScoringCorpContrastVo> descList = ctrScoringCorpContrastList.stream().filter(item -> item.getScoringMethod() != null && CreditRatingConstant.SCORING_METHOD_DESC == item.getScoringMethod().intValue()).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(descList)) {
            this.send(CreditRatingConstant.SCORING_METHOD_FENDUAN, descList, resultMap);
        }
        //排名法(升序序)
        List<CtrScoringCorpContrastVo> ascList = ctrScoringCorpContrastList.stream().filter(item -> item.getScoringMethod() != null && CreditRatingConstant.SCORING_METHOD_ASC == item.getScoringMethod().intValue()).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(ascList)) {
            this.send(CreditRatingConstant.SCORING_METHOD_FENDUAN, ascList, resultMap);
        }
        //插值法
        List<CtrScoringCorpContrastVo> interpolationList = ctrScoringCorpContrastList.stream().filter(item -> item.getScoringMethod() != null && CreditRatingConstant.SCORING_METHOD_INTERPOLATION == item.getScoringMethod().intValue()).collect(Collectors.toList());
        if (EmptyUtils.isNotEmpty(interpolationList)) {
            this.send(CreditRatingConstant.SCORING_METHOD_INTERPOLATION, interpolationList, resultMap);
        }

        for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : ctrScoringCorpContrastList) {
            List<OrgScoreVO> orgScoreList = ctrScoringCorpContrastVo.getOrgScoreList();
            if (EmptyUtils.isNotEmpty(orgScoreList)) {
                for (OrgScoreVO orgScoreVO : orgScoreList) {
                    String key = ctrScoringCorpContrastVo.getScoringIndicatorSolidificati() + "," + orgScoreVO.getOrgId();
                    if (resultMap.containsKey(key)) {
                        if(resultMap.get(key).compareTo(new BigDecimal(-9999)) == 0){
                            orgScoreVO.setScore(BigDecimal.ZERO);
                        }else{
                            orgScoreVO.setScore(resultMap.get(key));
                        }
                    }else{
                        orgScoreVO.setScore(BigDecimal.ZERO);
                    }
                }
            }
        }
    }


    /**
     * 调用计算方法API
     *
     * @param scoreMethod
     * @param autoIndicatorList
     * @return
     */
    public void send(int scoreMethod, List<CtrScoringCorpContrastVo> autoIndicatorList,Map<String, BigDecimal> resultMap) {
        //String url = "";
        Evaluation evaluation = new Evaluation();
        //评分指标集合
        List indicatorList = new ArrayList<>();
        switch (scoreMethod) {
            case 1:
                //url = urlNorMinmax;
                //归一化算法
                Normalization normalization = new Normalization();
                //指标最大值集合
                List maxList = new ArrayList<>();
                //指标最小值集合
                List minList = new ArrayList<>();
                //指标设定值集合
                List indicatorValueList = new ArrayList<>();
                //指标数据值集合
                List rawValueList = new ArrayList<>();
                //指标方向
                List directionList = new ArrayList<>();
                for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : autoIndicatorList) {
                    //机构评分指标集合
                    List<OrgScoreVO> orgScoreList = ctrScoringCorpContrastVo.getOrgScoreList();
                    //指标基础信息
                    CrmScoringIndicatorSolidificatiVO indicatorInfo = ctrScoringCorpContrastVo.getIndicatorInfo();
                    //指标设置值
                    List<CrmSectionDataVO> sectionDataVOList = indicatorInfo.getSectionDataVOList();
                    CrmSectionDataVO crmSectionDataVO = sectionDataVOList.get(0);
                    for (OrgScoreVO orgScoreVO : orgScoreList) {
                        //指标标识 = 指标id + 机构id
                        indicatorList.add(ctrScoringCorpContrastVo.getScoringIndicatorSolidificati() + "," + orgScoreVO.getOrgId());
                        //指标最大值
                        maxList.add(orgScoreVO.getMaxValue());
                        //指标最小值
                        minList.add(orgScoreVO.getMinValue());
                        indicatorValueList.add(crmSectionDataVO.getGrade());
                        //指标实际值
                        rawValueList.add(orgScoreVO.getDataSourceValue());
                        //指标方向
                        directionList.add(1);
                    }
                }
                normalization.setIndicatorName(indicatorList);
                normalization.setMaxValue(maxList);
                normalization.setMinValue(minList);
                normalization.setIndicatorValue(indicatorValueList);
                normalization.setRawValue(rawValueList);
                normalization.setDirection(directionList);
                evaluation.setData(normalization);

                Map<String, BigDecimal> stringBigDecimalMap = NormalizationModel.minMaxScaler(evaluation);

                stringBigDecimalMap.forEach(resultMap::put);
                break;
            case 2:

                //url = urlNorSs;
                //分段法
                Subsection subsection = new Subsection();
                //评分指标数据源值集合
                List valuesList = new ArrayList();
                //评分指标操作明细集合
                List list = new ArrayList();

                for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : autoIndicatorList) {
                    //机构评分指标集合
                    List<OrgScoreVO> orgScoreList = ctrScoringCorpContrastVo.getOrgScoreList();
                    //指标基础信息
                    CrmScoringIndicatorSolidificatiVO indicatorInfo = ctrScoringCorpContrastVo.getIndicatorInfo();
                    //指标设置值
                    List<CrmSectionDataVO> sectionDataVOList = indicatorInfo.getSectionDataVOList();

                    for (OrgScoreVO orgScoreVO : orgScoreList) {
                        //指标标识 = 指标id + 机构id
                        indicatorList.add(ctrScoringCorpContrastVo.getScoringIndicatorSolidificati() + "," + orgScoreVO.getOrgId());

                        //排名法
                        if (ctrScoringCorpContrastVo.getScoringMethod().compareTo(new BigDecimal(CreditRatingConstant.SCORING_METHOD_DESC))==0
                                ||ctrScoringCorpContrastVo.getScoringMethod().compareTo(new BigDecimal(CreditRatingConstant.SCORING_METHOD_ASC))==0){
                            valuesList.add(orgScoreVO.getDataSourceValueOrder());
                        }else {
                            //指标数据源值
                            valuesList.add(orgScoreVO.getDataSourceValue());
                        }
                        SubsectionDetails subsectionDetails = new SubsectionDetails();
                        //值区间
                        List segments = new ArrayList();
                        //评分指标得分集合
                        List scores = new ArrayList();
                        //评分指标操作明细
                        List operator = new ArrayList();
                        for (CrmSectionDataVO crmSectionDataVO : sectionDataVOList) {
                            //区间值
                            List section = new ArrayList();
                            //区间值-左值
                            section.add(crmSectionDataVO.getLeftValue());
                            //区间值-右值
                            section.add(crmSectionDataVO.getRightValue());
                            segments.add(section);
                            //区间分数
                            scores.add(crmSectionDataVO.getGrade());
                            //计算符号
                            Map<String, String> symbolMap = new HashMap<>();
                            //左符号
                            symbolMap.put("low", crmSectionDataVO.getLeftSymbol());
                            //又符号
                            symbolMap.put("high", crmSectionDataVO.getRightSymbol());
                            operator.add(symbolMap);
                        }
                        subsectionDetails.setSegments(segments);
                        subsectionDetails.setScores(scores);
                        subsectionDetails.setOperator(operator);
                        list.add(subsectionDetails);
                    }
                }
                //评分指标集合
                subsection.setIndicators(indicatorList);
                //评分指标数据源值集合
                subsection.setValues(valuesList);
                //评分指标明细
                subsection.setList(list);
                evaluation.setData(subsection);

                Map<String, Object> map = NormalizationModel.sectionalScoring(evaluation);

                //指标得分
                List<BigDecimal> scoreList = (List<BigDecimal>) map.get("df");
                //指标编码
                List<String> indicatorsList = (List<String>) map.get("indicators");

                for (int i = 0; i < indicatorList.size(); i++) {
                    for (int j = 0; j < scoreList.size(); j++) {
                        if (i == j) {
                            String key = indicatorsList.get(i);
                            BigDecimal score = scoreList.get(j);
                            resultMap.put(key, score.setScale(2, RoundingMode.HALF_UP));
                        }
                    }
                }
                break;
            case 3:
                //插值法
                for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : autoIndicatorList) {
                    //机构评分指标集合
                    List<OrgScoreVO> orgScoreList = ctrScoringCorpContrastVo.getOrgScoreList();
                    //指标基础信息
                    CrmScoringIndicatorSolidificatiVO indicatorInfo = ctrScoringCorpContrastVo.getIndicatorInfo();
                    //指标设置值
                    List<CrmSectionDataVO> sectionDataVOList = indicatorInfo.getSectionDataVOList();
                    for (OrgScoreVO orgScoreVO : orgScoreList) {
                        BigDecimal value=orgScoreVO.getDataSourceValue();
                        BigDecimal score= ScoringMethod.interpolation(value,sectionDataVOList);
                        String key= ctrScoringCorpContrastVo.getScoringIndicatorSolidificati() + "," + orgScoreVO.getOrgId();
                        resultMap.put(key,score);
                    }
                }
                return;
            default:
        }
        /*String jsonUrl = JSONUtil.toJsonStr(evaluation);
        String response = HttpUtils.postJson(url, evaluation);
        Map jsonMap = JsonUtils.json2Map(response);
        if (null != jsonMap) {
            switch (scoreMethod) {
                case 1:
                    //指标得分
                    Map<String, String> indicatorScoreMap = (Map<String, String>) jsonMap.get("creditScoring");
                    for (String key : indicatorScoreMap.keySet()) {
                        BigDecimal score = new BigDecimal(indicatorScoreMap.get(key));
                        resultMap.put(key, score.setScale(2, RoundingMode.HALF_UP));
                    }
                    break;
                case 2:
                    //指标得分
                    List<String> scoreList = (List<String>) jsonMap.get("df");
                    //指标编码
                    List<String> indicatorsList = (List<String>) jsonMap.get("indicators");

                    for (int i = 0; i < indicatorList.size(); i++) {
                        for (int j = 0; j < scoreList.size(); j++) {
                            if (i == j) {
                                String key = indicatorsList.get(i);
                                BigDecimal score = new BigDecimal(scoreList.get(j));
                                resultMap.put(key, score.setScale(2, RoundingMode.HALF_UP));
                            }
                        }
                    }
                    break;
                case 3:
                    break;
                default:
            }
        }*/
    }

    /**
     * 调用计算方法API
     *
     * @param crmSectionDataVOS 评级模板集合
     * @param evaluatedDataForReportList 单位机构评价指标集合
     * @param scoringIndicatorFixReportList 评价模板指标固化
     * @return
     */
    public void calculateScore(List<CrmSectionDataVO> crmSectionDataVOS,List<EvaluatedDataForReportVO> evaluatedDataForReportList,
                                        List<ScoringIndicatorFixReportVO> scoringIndicatorFixReportList, String taskNo) {
        //根据机构类型分组
        Map<String, List<EvaluatedDataForReportVO>> dataTypeMap = evaluatedDataForReportList.stream().collect(Collectors.groupingBy(EvaluatedDataForReportVO::getDataType));
        for(String key : dataTypeMap.keySet()){
            Evaluation evaluation = new Evaluation();
            //得分区间
            List segments = new ArrayList();
            //得分
            List scores = new ArrayList();
            //得分条件
            List operator = new ArrayList();
            CreditLevels creditLevels = new CreditLevels();
            SubsectionDetails subsectionDetails = new SubsectionDetails();
            for (CrmSectionDataVO crmSectionDataVO : crmSectionDataVOS) {
                //区间值
                List section = new ArrayList();
                //区间值-左值
                if(null == crmSectionDataVO.getLeftValue()){
                    section.add(new BigDecimal("-99999999999999.00"));
                }else {
                    section.add(crmSectionDataVO.getLeftValue());
                }
                //区间值-右值
                if(null == crmSectionDataVO.getRightValue()){
                    section.add(new BigDecimal("99999999999999.00"));
                }else{
                    section.add(crmSectionDataVO.getRightValue());
                }
                segments.add(section);
                //区间等级
                scores.add(crmSectionDataVO.getScoringLevel());
                //计算符号
                Map<String, String> symbolMap = new HashMap<>();
                //左符号
                symbolMap.put("low", crmSectionDataVO.getLeftSymbol());
                //又符号
                symbolMap.put("high", crmSectionDataVO.getRightSymbol());
                operator.add(symbolMap);
            }
            //评级标准分段上下限
            subsectionDetails.setSegments(segments);
            //评级标准等级
            subsectionDetails.setScores(scores);
            //评级标准分段符号
            subsectionDetails.setOperator(operator);
            creditLevels.setCreditLevels(subsectionDetails);
            List<EvaluatedDataForReportVO> evaluatedDataForReportVOS = dataTypeMap.get(key);
            //根据机构名称分组
            Map<String, List<EvaluatedDataForReportVO>> evaluatedDataForReportMap = evaluatedDataForReportVOS.stream().collect(Collectors.groupingBy(EvaluatedDataForReportVO::getDataName));
            List<EvaluationObject> evaluationObjectList = new ArrayList<>();
            for(String evaluatedDataKey:evaluatedDataForReportMap.keySet()){
                EvaluationObject evaluationObject = new EvaluationObject();
                List indicatorList = new ArrayList<>();
                BigDecimal scoreSum = BigDecimal.ZERO;
                BigDecimal evaluateNumber = BigDecimal.ZERO;
                BigDecimal coefficient = BigDecimal.ZERO;
                evaluationObject.setEvaluationObject(evaluatedDataKey);
                List<EvaluatedDataForReportVO> evaluatedDataForReportVOS1 = evaluatedDataForReportMap.get(evaluatedDataKey);
                List<ScoringMetricValFixReportVO> scoringMetricValFixReportLists = new ArrayList<>();
                for (EvaluatedDataForReportVO evaluatedDataForReportVO : evaluatedDataForReportVOS1) {
                    List<ScoringMetricValFixReportVO> scoringMetricValFixReportList = evaluatedDataForReportVO.getScoringMetricValFixReportList();
                    for (ScoringMetricValFixReportVO scoringMetricValFixReportVO : scoringMetricValFixReportList) {
                        scoringMetricValFixReportLists.add(scoringMetricValFixReportVO);
                    }
                }
                for (ScoringIndicatorFixReportVO scoringIndicatorFixReportVO : scoringIndicatorFixReportList) {
                    if(null != scoringIndicatorFixReportVO.getScoringIndicatorCoefficient()
                            && scoringIndicatorFixReportVO.getScoringIndicatorCoefficient().compareTo(BigDecimal.ZERO) >0){
                        List<Object> indicators = new ArrayList<>();
                        for (ScoringMetricValFixReportVO scoringMetricValFixReportVO : scoringMetricValFixReportLists) {

                            if (scoringIndicatorFixReportVO.getScoringIndicatorCode().equals(scoringMetricValFixReportVO.getScoringIndicatorCode())) {
                                //指标分数为空，表示没有评价
                                if(null != scoringMetricValFixReportVO.getScore()){
                                    scoreSum = scoreSum.add(scoringMetricValFixReportVO.getScore());
                                    evaluateNumber = evaluateNumber.add(BigDecimal.ONE);
                                }
                            }
                        }
                        coefficient = scoringIndicatorFixReportVO.getScoringIndicatorCoefficient();

                        indicators.add(scoringIndicatorFixReportVO.getScoringIndicatorName());
                        indicators.add(scoreSum);
                        indicators.add(evaluateNumber);
                        indicators.add(coefficient);
                        indicatorList.add(indicators);
                        scoreSum = BigDecimal.ZERO;
                        evaluateNumber = BigDecimal.ZERO;
                        coefficient = BigDecimal.ZERO;

                    }

                }

                evaluationObject.setIndicators(indicatorList);
                evaluationObjectList.add(evaluationObject);
                creditLevels.setList(evaluationObjectList);
            }

            evaluation.setData(creditLevels);
            List<Map<String,Object>> result = this.calculationLevel(evaluation);
            for (Map<String, Object> stringObjectMap : result) {
                evaluationAnalysisDao.updateCrTaskOrgInfo(stringObjectMap,taskNo);
            }

        }
    }

    /**
     * 保存操作日志
     *
     * @param quoteId
     * @param logType
     * @param msg
     * @param user
     */
    public void saveMessage(String quoteId, String logType, String msg, ICurrentUser user) {
        //保存操作日志
        EvaluationLog evaluationLog = new EvaluationLog();
        evaluationLog.setQuoteId(quoteId);
        evaluationLog.setLogType(logType);
        evaluationLog.setMsg(msg);
        evaluationLog.setCreateBy(user.getLoginName());
        evaluationLog.setCreateName(user.getUsername());
        evaluationLog.setCreateDate(DateUtils.getDate());
        evaluationLogService.saveEntity(evaluationLog);
    }

    /**
     * 根据任务编号获、单位id取评分指标
     *
     * @param taskNo
     */
    public Map<String, Object> getCorpScoringJson(String taskNo, String corpId) {
        //根据任务编号获取评分指标
        List<CtrScoringCorpContrastVo> taskScoringList = corpContrastService.getTaskScoringJson(taskNo, corpId);
        List<Integer> levelList = new ArrayList<>();
        getIndicatorsLevel(taskScoringList, levelList);
        Optional<Integer> max = levelList.stream().max(Comparator.naturalOrder());

        List<String> taskNos = Collections.singletonList(taskNo);
        List<CrTaskOrgInfoVo> crTaskOrgInfoVo = evaluationAnalysisDao.getCrTaskOrgInfoVoByTaskNo(taskNos);
        List<String> orgIds = crTaskOrgInfoVo.stream().map(CrTaskOrgInfoVo::getOrgId).collect(Collectors.toList());
        List<String> orgName = crTaskOrgInfoVo.stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.toList());

        //根据评分任务、单位id、机构id集合获取评分指标值固化
        List<ScoringMetricValFixReportVO> scoringMetricValFixReportList = evaluationAnalysisDao.getScoringMetricValFixReport(taskNo, corpId, orgIds);
        //Map<String, List<ScoringMetricValFixReportVO>> orgIndicatorsMap = scoringMetricValFixReportList.stream().collect(Collectors.groupingBy(ScoringMetricValFixReportVO::getDataCode));
        Map<String, List<ScoringMetricValFixReportVO>> codeMap = scoringMetricValFixReportList.stream().collect(Collectors.groupingBy(ScoringMetricValFixReportVO::getScoringIndicatorCode));

        this.setScoringMetricValFixReportVO(taskScoringList, codeMap);

        Map<String, Object> resultMap = new HashMap<>();
        //评分任务指标集合
        resultMap.put("taskScoringList", taskScoringList);
        //指标最大层级
        resultMap.put("maxLevel", max.get());
        //机构评分指标值固化
        resultMap.put("orgName", orgName);

        return resultMap;
    }

    /**
     * 递归遍历单位评分指标树,获取指标层级
     *
     *
     * @param scoringCorpList
     */
    public void getIndicatorsLevel(List<CtrScoringCorpContrastVo> scoringCorpList, List<Integer> levelList) {
        for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : scoringCorpList) {
            if (EmptyUtils.isEmpty(ctrScoringCorpContrastVo.getChildrenScoringList())) {
                levelList.add(ctrScoringCorpContrastVo.getLevel_());
            } else {
                getIndicatorsLevel(ctrScoringCorpContrastVo.getChildrenScoringList(), levelList);
            }
        }
    }

    /**
     * 递归遍历单位评分指标树,设置评分指标值固化数据
     *
     * @param scoringCorpList
     */
    public void setScoringMetricValFixReportVO(List<CtrScoringCorpContrastVo> scoringCorpList, Map<String, List<ScoringMetricValFixReportVO>> map) {
        for (CtrScoringCorpContrastVo ctrScoringCorpContrastVo : scoringCorpList) {
            if (EmptyUtils.isEmpty(ctrScoringCorpContrastVo.getChildrenScoringList())) {
                if (map.containsKey(ctrScoringCorpContrastVo.getScoringIndicatorCode())) {
                    ctrScoringCorpContrastVo.setScoringMetricValFixReportList(map.get(ctrScoringCorpContrastVo.getScoringIndicatorCode()));
                }
            } else {
                setScoringMetricValFixReportVO(ctrScoringCorpContrastVo.getChildrenScoringList(), map);
            }
        }
    }

    public void update(CrTaskCorpInfoVo crTaskCorpInfoVo, ICurrentUser user) {
        CrTaskCorpInfo crTaskCorpInfo = evaluationAnalysisDao.get(crTaskCorpInfoVo.getId(), true);
        String indicatorScore = crTaskCorpInfoVo.getIndicatorScore();
        if(StringUtils.isNotBlank(indicatorScore)){
            String[] split = indicatorScore.split(",");
            for (String s : split) {
                String[] score = s.split("_");
                if (!"null".equals(score[1])) {
                    //根据id修改评分指标值固化得分
                    evaluationAnalysisDao.updateScoringMetricValFixReport(score[0], new BigDecimal(score[1]));
                }
            }
        }
        String valueScore= crTaskCorpInfoVo.getValueScore();
        if(StringUtils.isNotBlank(valueScore)){
            String[] split = valueScore.split(",");
            for (String s : split) {
                String[] score = s.split("_");
                if (!"null".equals(score[1])) {
                    //根据id修改评分指标值固化得分
                    evaluationAnalysisDao.updateIndicatorValue(score[0], new BigDecimal(score[1]));
                }
            }
        }

        String opType = "";
        String msg = "";
        if ("1".equals(crTaskCorpInfoVo.getOpType())) {
            crTaskCorpInfo.setStatus(CreditRatingConstant.APPROVAL_PROCESS_ADD);
            getStartHandler().handler(CreWorkFlowConstant.CREDIT_ORG_EVALUATION, crTaskCorpInfo.getId(), crTaskCorpInfo, crTaskCorpInfo.getCorpId(), user.getId());
            opType = CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_CODE;
//            msg = "评价提交";
            msg = CreditRatingConstant.EVALUATION_LOG_LANG_EVALUATIONSUBMIT;
        } else {
            crTaskCorpInfo.setStatus(0);
            opType = CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_CODE;
//            msg = "评价暂存";
            msg = CreditRatingConstant.EVALUATION_LOG_LANG_EVALUATIONTEMP;
        }
        crTaskCorpInfo.setEvaluateBy(user.getLoginName());
        this.updateEntity(crTaskCorpInfo);
        this.saveMessage(crTaskCorpInfo.getId(), opType, msg, user);
    }

    /**
     * 调用计算方法API
     *
     * @param evaluation
     * @param evaluation
     * @return
     */
    public List<Map<String,Object>> send(Evaluation evaluation) {
        String url = urlIndicatorsBat;
        String jsonUrl = JSONUtil.toJsonStr(evaluation);
        String response = HttpUtils.postJson(url, evaluation);
        Map jsonMap = JsonUtils.json2Map(response);
        if (null != jsonMap) {
            return (List<Map<String, Object>>)jsonMap.get("list");
        }
        return null;
    }

    /**
     * 调用计算方法API
     *
     * @param evaluation
     * @param evaluation
     * @return
     */
    public List<Map<String,Object>> calculationLevel(Evaluation evaluation) {
        return SegmentationScorer.calculateScoresAandTBatch(evaluation);
    }

    /**
     * 定量根据数值自动计算得分
     * @param inId  指标id
     * @param value  数值
     * @return
     */
    public BigDecimal getScoreByValue(String inId, String value,String modelId){
        //根据指标id获取指标信息
        String[] ids = {inId};
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS= crmScoringIndicatorSolidService.getIndicatorForOldAndNew(ids,modelId);
        CrmScoringIndicatorSolidificatiVO solidificatiVO=scoringIndicatorSolidificatiVOS.get(0);
        //评分细则区间
        List<CrmSectionDataVO> sectionDataVOList = solidificatiVO.getSectionDataVOList();
        if(solidificatiVO.getScoringMethod()!= null && CreditRatingConstant.SCORING_METHOD_FENDUAN == solidificatiVO.getScoringMethod().intValue()){
            //String url = urlNorSs;
            Evaluation evaluation = new Evaluation();
            //分段法
            Subsection subsection = new Subsection();
            //评分指标集合
            List indicatorList = new ArrayList<>();
            indicatorList.add(inId);
            //评分指标数据源值集合
            List valuesList = new ArrayList();
            valuesList.add(value);
            //评分指标操作明细集合
            List list = new ArrayList();

            SubsectionDetails subsectionDetails = new SubsectionDetails();
            //值区间
            List segments = new ArrayList();
            //评分指标得分集合
            List scores = new ArrayList();
            //评分指标操作明细
            List operator = new ArrayList();
            for (CrmSectionDataVO crmSectionDataVO : sectionDataVOList) {
                //区间值
                List section = new ArrayList();
                //区间值-左值
                section.add(crmSectionDataVO.getLeftValue());
                //区间值-右值
                section.add(crmSectionDataVO.getRightValue());
                segments.add(section);
                //区间分数
                scores.add(crmSectionDataVO.getGrade());
                //计算符号
                Map<String, String> symbolMap = new HashMap<>();
                //左符号
                symbolMap.put("low", crmSectionDataVO.getLeftSymbol());
                //又符号
                symbolMap.put("high", crmSectionDataVO.getRightSymbol());
                operator.add(symbolMap);
            }
            subsectionDetails.setSegments(segments);
            subsectionDetails.setScores(scores);
            subsectionDetails.setOperator(operator);
            list.add(subsectionDetails);
            //评分指标集合
            subsection.setIndicators(indicatorList);
            //评分指标数据源值集合
            subsection.setValues(valuesList);
            //评分指标明细
            subsection.setList(list);
            evaluation.setData(subsection);
           /* String jsonUrl = JSONUtil.toJsonStr(evaluation);
            String response = HttpUtils.postJson(url, evaluation);

            Map jsonMap = JsonUtils.json2Map(response);
            //指标得分
            List<String> scoreList = (List<String>) jsonMap.get("df");
            if (scoreList != null && scoreList.size() > 0) {
                return new BigDecimal(scoreList.get(0));
            }*/

            Map<String, Object> map = NormalizationModel.sectionalScoring(evaluation);

            //指标得分
            List<BigDecimal> scoreList = (List<BigDecimal>) map.get("df");
            //指标编码
            List<String> indicatorsList = (List<String>) map.get("indicators");
            if (scoreList != null && scoreList.size() > 0) {
                return scoreList.get(0);
            }
        }else if(solidificatiVO.getScoringMethod()!= null && CreditRatingConstant.SCORING_METHOD_INTERPOLATION == solidificatiVO.getScoringMethod().intValue()){
            BigDecimal score=ScoringMethod.interpolation(new BigDecimal(value),sectionDataVOList);
            return score;
        }
        return BigDecimal.ZERO;
    }
}