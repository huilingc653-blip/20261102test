package com.bytter.biz.creditRating.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.EvaluationAnalysisDao;
import com.bytter.biz.creditRating.dao.EvaluationReportDao;

import com.bytter.biz.creditRating.entity.EvaluationReport;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;

import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 信用评估报告
 * @author chenqin
 * @date 2024-08-09
 */
@Service("evaluationReportService")
@Transactional
public class EvaluationReportService extends BaseService<EvaluationReport, String> {

	@Autowired
	private EvaluationReportDao evaluationReportDao;

    @DubboReference(check = false)
	private ICorpService corpService;

    @DubboReference(check = false)
	private IDictService dictService;

    @Autowired
    private CtrScoringCorpContrastService corpContrastService;

    @Autowired
    private EvaluationAnalysisDao evaluationAnalysisDao;

    @Autowired
    private CreditLimitService creditLimitService;
    @Autowired
    private FinancingService financingService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private ChoDepositRateService choDepositRateService;

	@Override
    public BaseDao<EvaluationReport, String> getEntityDao() {
        return evaluationReportDao;
    }

    /**
     * 获取信用评估报告数据列表
     *
     *
     * @param dataTablesInput
     * @return
     */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, CorpDTO> corpInfoMap = corpService.getCorpMap();
        Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
        DataTablesOutput dataTablesOutput = evaluationReportDao.getDataTablesUnion(dataTablesInput);

        List<CrTaskOrgInfoVo> crTaskOrgInfoList = dataTablesOutput.getData();

        for (CrTaskOrgInfoVo crTaskOrgInfoVo : crTaskOrgInfoList) {
            List<CrTaskCorpInfoVo> crTaskCorpInfo = evaluationReportDao.getCrTaskCorpInfoByTaskNo(crTaskOrgInfoVo.getTaskNo());
            String corpName = "";
            for(int i = 0 ; i < crTaskCorpInfo.size(); i++){
                CorpDTO corpDTO = corpInfoMap.get(crTaskCorpInfo.get(i).getCorpId());
                corpName += corpDTO.getName();
                if(i < crTaskCorpInfo.size() - 1){
                    corpName += ",";
                }
            }
            String orgType = crTaskOrgInfoVo.getOrgType();
            String orgTypeName = "";
            if(orgType!=null){
                String[] split = orgType.split(",");
                for(int i = 0; i < split.length; i++){
                    if(institutionaType.containsKey(split[i])){
                        orgTypeName += institutionaType.get(split[i]).getName();
                    }
                    if(i < split.length - 1){
                        orgTypeName += ",";
                    }
                }
            }
            crTaskOrgInfoVo.setCorpName(corpName);
            crTaskOrgInfoVo.setOrgType(orgTypeName);
        }
        return dataTablesOutput;
    }

    /**
     * 获取综合报表数据信息
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getAnalyzeDataTables(DataTablesInput dataTablesInput){
        DataTablesOutput output= evaluationReportDao.getAnalyzeDataTables(dataTablesInput);
        // 写入任务机构的其他数据
        makeAnalyzeData(output);
        return output;
    }

    /**
     * 写入其他数据信息
     * @param output
     */
    public void makeAnalyzeData(DataTablesOutput output){
        List<CreditAnalyzeReportVO> reportVOList=output.getData();
        if(!EmptyUtils.isEmpty(reportVOList)){
            for(CreditAnalyzeReportVO creditAnalyzeReportVO:reportVOList){
                List<CrTaskCorpInfoVo> crTaskCorpInfo = evaluationReportDao.getCrTaskCorpInfoByTaskNo(creditAnalyzeReportVO.getTaskNo());
                List<String> corpIds=crTaskCorpInfo.stream().collect(Collectors.mapping(CrTaskCorpInfoVo::getCorpId,Collectors.toList()));
                //写入授信总额
                BigDecimal creditBal=creditLimitService.getSumCreditBalForReportDate(creditAnalyzeReportVO.getOrgId(),corpIds,creditAnalyzeReportVO.getEvaluateEndDate());
                creditAnalyzeReportVO.setCreditTotal(creditBal);
                //写入借款余额
                BigDecimal provideBal= financingService.getSumProvideBalForReportDate(creditAnalyzeReportVO.getOrgId(),corpIds,creditAnalyzeReportVO.getEvaluateEndDate());
                creditAnalyzeReportVO.setLoanBalance(provideBal);
                //写入结算总量
                BigDecimal accDtl=bankAccountService.getSumBankAccDtlForReportDate(creditAnalyzeReportVO.getOrgId(),corpIds,creditAnalyzeReportVO.getEvaluateStartDate(),creditAnalyzeReportVO.getEvaluateEndDate());
                creditAnalyzeReportVO.setSettlementTotal(accDtl);
                //写入账户余额
                BigDecimal accBal= bankAccountService.getSumBankAccBalForReportDate(creditAnalyzeReportVO.getOrgId(),corpIds,creditAnalyzeReportVO.getEvaluateEndDate());
                creditAnalyzeReportVO.setAccountBalance(accBal);
                //写入理财余额
                BigDecimal choBal= choDepositRateService.getSumChoDepositForReportDate(creditAnalyzeReportVO.getOrgId(),corpIds,creditAnalyzeReportVO.getEvaluateEndDate());
                creditAnalyzeReportVO.setFinanceBalance(choBal);
            }
        }
    }

    /**
     * 根据机构评价任务明细id获取评价任务信息
     * @param id
     * @return
     */
    public CrTaskOrgInfoVo getCrTaskInfoById(String id) {
        return evaluationReportDao.getCrTaskInfoById(id);
    }

    /**
     * 根据任务编号获取固化评分指标
     * @param taskNo 任务编号
     * @param orgId 机构id
     */
    public Map<String, Object> getScoringIndicatorFixReport(String taskNo, String orgId){
        //根据任务编号获取固化评分指标
        List<ScoringIndicatorFixReportVO> scoringIndicatorFixReportVOList = evaluationReportDao.getScoringIndicatorFixReport(taskNo);
        //获取固化指标id
        List<String> ids = scoringIndicatorFixReportVOList.stream().map(ScoringIndicatorFixReportVO::getId).collect(Collectors.toList());
        //根据固化指标id获取指标定量方法
        List<QMScoringIFixReportVO> qmScoringIFixReportVOList = evaluationReportDao.getQMScoringIFixReport(ids);
        //根据指标id分组
        Map<String, List<QMScoringIFixReportVO>> qMScoringIFixReportVOMap = qmScoringIFixReportVOList.stream().collect(Collectors.groupingBy(QMScoringIFixReportVO::getSifrId));
        //根据任务编号、机构id获取机构指标加权平均得分
        List<CrScoringOrgContrastVo> crScoringOrgContrastList = evaluationReportDao.getCrScoringOrgContrast(taskNo, orgId);
        Map<String, CrScoringOrgContrastVo> crScoringOrgContrastMap = crScoringOrgContrastList.stream().collect(Collectors.toMap(CrScoringOrgContrastVo::getScoringIndicatorCode, Function.identity()));

        //根据评价任务编号、机构id 获取评价单位指标得分
        List<ScoringMetricValFixReportVO> scoringMetricValFixReportList = evaluationReportDao.getScoringMetricValFixReport(taskNo, orgId);
        //根据单位名称排序
        List<ScoringMetricValFixReportVO> sortedScoringMetricValFixReportList = scoringMetricValFixReportList.stream().sorted(Comparator.comparing(ScoringMetricValFixReportVO::getCorpName)).collect(Collectors.toList());
        //根据指标类型对固化指标分组
        Map<String, List<ScoringMetricValFixReportVO>> codeMap = sortedScoringMetricValFixReportList.stream().collect(Collectors.groupingBy(ScoringMetricValFixReportVO::getScoringIndicatorCode));

        //获取评价单位名称
        List<String> corpNames = sortedScoringMetricValFixReportList.stream().map(ScoringMetricValFixReportVO ::getCorpName).distinct().collect(Collectors.toList());
        List<String> corpIds = sortedScoringMetricValFixReportList.stream().map(ScoringMetricValFixReportVO ::getCorpId).distinct().collect(Collectors.toList());
        Map<String,BigDecimal> corpNumber=new HashMap<>();
        for (String key:codeMap.keySet()){
            //参与指标算分的组织数量
            corpNumber.put(key,new BigDecimal(codeMap.get(key).size()));
            List<ScoringMetricValFixReportVO> voList=new ArrayList<>();
            for (String corpId:corpIds){
                Boolean flag=false;
                List<ScoringMetricValFixReportVO> value=  codeMap.get(key);
                for(ScoringMetricValFixReportVO vo:value){
                    if (corpId.equals(vo.getCorpId())){
                        flag=true;
                        voList.add(vo);
                    }
                }

                if (!flag){
                    ScoringMetricValFixReportVO  newVO=new ScoringMetricValFixReportVO();
                    newVO.setScore(BigDecimal.ZERO);
                    newVO.setCorpId(corpId);
                    voList.add(newVO);
                }
            }
            codeMap.put(key,voList);
        }

        //封装评分指标信息
        for (ScoringIndicatorFixReportVO scoringIndicatorFixReportVO : scoringIndicatorFixReportVOList) {
            //设置固化指标定量方法值
            if(qMScoringIFixReportVOMap.containsKey(scoringIndicatorFixReportVO.getId())){
                List<QMScoringIFixReportVO> qmScoringIFixReportVOS = qMScoringIFixReportVOMap.get(scoringIndicatorFixReportVO.getId());
                for (QMScoringIFixReportVO qmScoringIFixReportVO : qmScoringIFixReportVOS) {
                    this.setScoringIndicatorFixReport(scoringIndicatorFixReportVO, qmScoringIFixReportVO);
                }
            }
            //设置评分指标加权平均得分
            if(crScoringOrgContrastMap.containsKey(scoringIndicatorFixReportVO.getScoringIndicatorCode())){
                CrScoringOrgContrastVo crScoringOrgContrastVo = crScoringOrgContrastMap.get(scoringIndicatorFixReportVO.getScoringIndicatorCode());
                if(null != crScoringOrgContrastVo.getScoringOrgAvg()){
                    scoringIndicatorFixReportVO.setIndicatorWeightingAvgScore(crScoringOrgContrastVo.getScoringOrgAvg());
                }
            }
            //设置评分指标单位评价得分
            if(codeMap.containsKey(scoringIndicatorFixReportVO.getScoringIndicatorCode())){
                scoringIndicatorFixReportVO.setScoringMetricValFixReportList(codeMap.get(scoringIndicatorFixReportVO.getScoringIndicatorCode()));
            }
            //得出指标得分平均分以及总分值
            if(scoringIndicatorFixReportVO.getScoringMetricValFixReportList() != null && scoringIndicatorFixReportVO.getScoringMetricValFixReportList().size() > 0){
                //得到指标不为空的得分合计
                BigDecimal totalScore=BigDecimal.ZERO;
                for(ScoringMetricValFixReportVO mvo:scoringIndicatorFixReportVO.getScoringMetricValFixReportList()){
                    if(!EmptyUtils.isEmpty(mvo.getScore())){
                        totalScore=totalScore.add(mvo.getScore());
                    }
                }
                //得到指标得分乘以指标系数后平均分
                BigDecimal avgScore = totalScore.multiply(scoringIndicatorFixReportVO.getScoringIndicatorCoefficient()).divide(corpNumber.get(scoringIndicatorFixReportVO.getScoringIndicatorCode()), 2, RoundingMode.HALF_UP);
                scoringIndicatorFixReportVO.setIndicatorWeightingAvgScore(avgScore);
            }
        }
        //指标层级排序
        List<ScoringIndicatorFixReportVO> levelList = scoringIndicatorFixReportVOList.stream().sorted(Comparator.comparing(ScoringIndicatorFixReportVO::getLevel).reversed()).collect(Collectors.toList());
        //封装固化指标树形
        List<ScoringIndicatorFixReportVO> scoringIndicatorFixReportTree = this.setTree(scoringIndicatorFixReportVOList);
//        //得到指标得分合计
//        BigDecimal totalScore = scoringIndicatorFixReportVOList.stream().map(ScoringIndicatorFixReportVO::getIndicatorWeightingAvgScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        //得到封装树形指标根指标行数合计
//        Integer totalRowSpan = scoringIndicatorFixReportTree.stream()
//                .mapToInt(ScoringIndicatorFixReportVO::getRowSpan)
//                .sum();

        Map<String, Object> resultMap = new HashMap<>();
        //评分任务指标集合
        resultMap.put("taskScoringList", scoringIndicatorFixReportTree);
        //指标最大层级
        resultMap.put("maxLevel", levelList.get(0).getLevel());
        //评价单位
        resultMap.put("corpName", corpNames);

        return resultMap;
    }

    /**
     * 将指标固化集合转换成树形结构
     * @param list
     * @return
     */
    public List<ScoringIndicatorFixReportVO> setTree(List<ScoringIndicatorFixReportVO> list){
        List<ScoringIndicatorFixReportVO> scoringList = list.stream().sorted(Comparator.comparing(ScoringIndicatorFixReportVO::getLevel).reversed()).collect(Collectors.toList());
        List<ScoringIndicatorFixReportVO> treeList = new ArrayList<>();
        for (int i = 0; i < scoringList.size(); i++) {
            ScoringIndicatorFixReportVO scoringIndicatorFixReportVO = scoringList.get(i);
            int count = 0;//所占行数
            int maxLevel = scoringList.get(0).getLevel();
            BigDecimal total = BigDecimal.ZERO;
            for (int j = 0; j < scoringList.size(); j++) {
                ScoringIndicatorFixReportVO soVO = scoringList.get(j);
                if (scoringIndicatorFixReportVO.getScoringIndicatorId().equals(soVO.getParentId())) {
                    List<ScoringIndicatorFixReportVO> childrenScoringList = new ArrayList<>();
                    if (!EmptyUtils.isEmpty(scoringIndicatorFixReportVO.getChildrenScoringList())) {
                        childrenScoringList = scoringIndicatorFixReportVO.getChildrenScoringList();
                    }
                    childrenScoringList.add(soVO);
                    if(null != soVO.getIndicatorWeightingAvgScore()){
                        total = total.add(soVO.getIndicatorWeightingAvgScore());
                    }
                    scoringIndicatorFixReportVO.setChildrenScoringList(childrenScoringList);
                    if (!EmptyUtils.isEmpty(soVO.getRowSpan())) {
                        count += soVO.getRowSpan();
                    }
                }
            }
            //设置行
            if (count != 0) {
                scoringIndicatorFixReportVO.setRowSpan(count);
            } else {
                scoringIndicatorFixReportVO.setRowSpan(1);
            }
            //设置列
            if (EmptyUtils.isEmpty(scoringIndicatorFixReportVO.getChildrenScoringList())) {
                int colNum = maxLevel - scoringIndicatorFixReportVO.getLevel() + 1;
                scoringIndicatorFixReportVO.setColSpan(colNum);
            }
            if(!total.equals(BigDecimal.ZERO)){
                scoringIndicatorFixReportVO.setTotal(total);
            }
            if (EmptyUtils.isEmpty(scoringIndicatorFixReportVO.getParentId())) {
                treeList.add(scoringIndicatorFixReportVO);
            }
        }
        for (ScoringIndicatorFixReportVO reportVO : treeList) {
            if (CollectionUtil.isEmpty(reportVO.getChildrenScoringList())) {
                reportVO.setIndicatorWeightingAvgScore(BigDecimal.ZERO);
            } else {
                reportVO.setColSpan(reportVO.getChildrenScoringList().stream().mapToInt(ScoringIndicatorFixReportVO::getRowSpan).sum());
                reportVO.setIndicatorWeightingAvgScore(reportVO.getChildrenScoringList().stream().map(ScoringIndicatorFixReportVO::getIndicatorWeightingAvgScore).reduce(BigDecimal.ZERO, BigDecimal::add));
            }

        }
        return treeList;
    }

    /**
     * 设置固化指标定量属性值
     * @param scoringIndicatorFixReportVO
     * @param qMScoringIFixReportVO
     */
    public void setScoringIndicatorFixReport(ScoringIndicatorFixReportVO scoringIndicatorFixReportVO,QMScoringIFixReportVO qMScoringIFixReportVO){
        switch (qMScoringIFixReportVO.getValueNameCode()){
            case CreditRatingConstant.INDICATOR_DIRECTION_CODE://指标方向
                scoringIndicatorFixReportVO.setIndicatorDirectionValue(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.MAX_POSSIBLE_VALUE_CODE://最大可能值
                scoringIndicatorFixReportVO.setMaxPossibleValue(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.MIN_POSSIBLE_VALUE_CODE://最小可能值
                scoringIndicatorFixReportVO.setMinPossibleValue(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.IS_AUTO_SCORING_CODE://是否自动评分
                scoringIndicatorFixReportVO.setIsAutoScoring(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.SCORING_METHOD_CODE://评分方法
                scoringIndicatorFixReportVO.setScoringMethod(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.IS_ADD_OR_SUB_GRADE_CODE://是否额外加/减分
                scoringIndicatorFixReportVO.setIsAddOrSubGrade(qMScoringIFixReportVO.getValue());break;
            case CreditRatingConstant.INDEX_PROPERTY_CODE://指标性质
                scoringIndicatorFixReportVO.setIndexProperty(qMScoringIFixReportVO.getValue());break;
        }
    }

}
