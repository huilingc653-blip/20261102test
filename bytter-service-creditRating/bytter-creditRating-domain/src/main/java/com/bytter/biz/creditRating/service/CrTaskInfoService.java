package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreWorkFlowConstant;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrTaskInfoDao;
import com.bytter.biz.creditRating.dao.CrmScoringGradeDao;
import com.bytter.biz.creditRating.entity.*;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
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
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 评价任务主表service
 *
 * @author huangyl
 */
@WorkFlow(CreWorkFlowConstant.CR_TASK_INFO_FLOW)
@Service(value = "crTaskInfoService")
@Transactional
public class CrTaskInfoService extends BaseService<CrTaskInfo, String> implements IWorkFlowListener {

    @Autowired
    private CrTaskInfoDao taskInfoDao;

    @Autowired
    private CrTaskCorpService taskCorpService;

    @Autowired
    private CrTaskOrgService taskOrgService;

    @Autowired
    private CrmScoringIndicatorSolidService solidService;

    @Autowired
    private CrScoringOrgContrastService orgContrastService;

    @Autowired
    private CtrScoringCorpContrastService corpContrastService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private CrmComputationalModelService crmComputationalModelService;

    @Autowired
    private CrdRawDataService crdRawDataService;

    @Autowired
    EvaluationLogService evaluationLogService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Autowired
    EvaluationAnalysisService evaluationAnalysisService;

    @Autowired
    private CrmScoringGradeDao crmScoringGradeDao;

    @Override
    public BaseDao<CrTaskInfo, String> getEntityDao() {
        return taskInfoDao;
    }

    @Transactional(readOnly = true)
    public DataTablesOutput dataCollection(ICurrentUser user) {
        return new DataTablesOutput();
    }

    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(String ids) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<CrdRawDataVo> queryByIds(List<String> ids) {
        return null;
    }

    public void modify(CrTaskInfoVO taskInfoVo, ICurrentUser user) {
        clearData(taskInfoVo);
        taskInfoVo.setUpdateBy(user.getLoginName());
        taskInfoVo.setUpdateDate(DateUtils.getDate());
        submit(taskInfoVo, user, CommonConstant.NO);
    }

    /**
     * 删除中间表数据
     *
     * @param taskInfoVo 任务实体
     */
    private void clearData(CrTaskInfoVO taskInfoVo) {
        orgContrastService.deleteByTaskNo(taskInfoVo.getTaskNo());
        corpContrastService.deleteByTaskNo(taskInfoVo.getTaskNo());
        taskCorpService.deleteByTaskNo(taskInfoVo.getTaskNo());
        taskOrgService.deleteByTaskNo(taskInfoVo.getTaskNo());
    }


    public boolean deleteById(String id, ICurrentUser user) {
        CrTaskInfo taskInfo = this.get(id);
        taskInfo.setStatus(CommonConstant.STATUE_DELETE);
        taskInfo.setUpdateBy(user.getLoginName());
        taskInfo.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(taskInfo);
//        saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_DEL_DESCRIPTION, "任务删除", user);
        saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_DEL_DESCRIPTION, CreditRatingConstant.EVALUATION_LOG_LANG_TASKDEL, user);
        return true;
    }

    public DataTablesOutput<CrTaskInfoVO> getDataTablesUnion(DataTablesInput input, ICurrentUser currentUser, String isApprove) {
        String corpIds = (String) input.getFormdata().get("ins_corpId");
        String orgName = (String) input.getFormdata().get("likes_orgName");
        input.getFormdata().remove("ins_corpId");
        input.getFormdata().remove("likes_orgName");
        List<String> taskNoList = new ArrayList<>();
        Map<String, List<CrTaskCorpInfoVo>> taskCorpMap = new HashMap<>();
        Map<String, List<CrTaskOrgInfoVo>> taskOrgMap = new HashMap<>();
        if (StringUtils.isNotEmpty(corpIds)) {
            //单位
            List<CrTaskCorpInfoVo> taskCorpList = taskCorpService.getListTaskNoByCorpId(corpIds);
            if(EmptyUtils.isEmpty(taskCorpList) || taskCorpList.size() == 0){
                return new DataTablesOutput<>();
            }
            taskNoList = taskCorpList.stream().map(CrTaskCorpInfoVo::getTaskNo).distinct().collect(Collectors.toList());
            taskCorpMap = taskCorpList.stream().collect(Collectors.groupingBy(CrTaskCorpInfoVo::getTaskNo));
        }
        if (StringUtils.isNotEmpty(orgName)) {
            //机构
            List<CrTaskOrgInfoVo> orgList = taskOrgService.getListTaskNoByOrgName(orgName);
            if(EmptyUtils.isEmpty(orgList) || orgList.size() == 0){
                return new DataTablesOutput<>();
            }
            taskOrgMap = orgList.stream().collect(Collectors.groupingBy(CrTaskOrgInfoVo::getTaskNo));
            if (!taskNoList.isEmpty()) {
                taskNoList.addAll(orgList.stream().map(CrTaskOrgInfoVo::getTaskNo).distinct().collect(Collectors.toList()));
                taskNoList = taskNoList.stream().distinct().collect(Collectors.toList());
            } else {
                taskNoList = orgList.stream().map(CrTaskOrgInfoVo::getTaskNo).distinct().collect(Collectors.toList());
            }
        }
        List<String> businessList = Collections.emptyList();
        WorkFlowBusiness workFlowBusiness = null;
        //是否审批流
        if (isApprove.equals(CommonConstant.YES)) {
            workFlowBusiness = workFlowService.getWorkFlowBusiness(CreWorkFlowConstant.CR_TASK_INFO_FLOW, input);
            businessList = workFlowBusiness.getBusinessIdList();
        }
        DataTablesOutput dataTablesUnion = taskInfoDao.getDataTablesUnion(input, currentUser, taskNoList, businessList);
//        if (taskNoList.isEmpty()) {
//            setReturnData(dataTablesUnion.getData());
//        } else {
//            setResultData(dataTablesUnion.getData(), taskCorpMap, taskOrgMap);
//        }
        setReturnData(dataTablesUnion.getData());
        if (isApprove.equals(CommonConstant.YES)) {
            dataTablesUnion = workFlowBusiness.merge(dataTablesUnion);
        }
        return dataTablesUnion;
    }

    /**
     * 设置单位和机构名称
     *
     * @param data 分页数据
     */
    private void setReturnData(List<CrTaskInfoVO> data) {
        List<String> taskNoList = data.stream().map(CrTaskInfoVO::getTaskNo).collect(Collectors.toList());
        Map<String, List<CrTaskCorpInfoVo>> taskCorpMap = taskCorpService.getListByTaskNoList(taskNoList);
        Map<String, List<CrTaskOrgInfoVo>> taskOrgMap = taskOrgService.getListByTaskNoList(taskNoList);
        Map<String, DictDTO> institutionaType = dictService.listMap("INSTITUTIONA_TYPE");
        setResultData(data, taskCorpMap, taskOrgMap,institutionaType);
    }

    /**
     * 设置单位和机构名称
     *
     * @param data        分页数据
     * @param taskCorpMap 单位名称Map
     * @param taskOrgMap  机构名称Map
     */
    private void setResultData(List<CrTaskInfoVO> data, Map<String, List<CrTaskCorpInfoVo>> taskCorpMap, Map<String, List<CrTaskOrgInfoVo>> taskOrgMap,Map<String, DictDTO> institutionaType) {
        for (CrTaskInfoVO entity : data) {
            if (taskCorpMap.containsKey(entity.getTaskNo())) {
                entity.setCorpName(taskCorpMap.get(entity.getTaskNo()).stream().map(CrTaskCorpInfoVo::getCorpName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
            }
            if (taskOrgMap.containsKey(entity.getTaskNo())) {
                entity.setOrgName(taskOrgMap.get(entity.getTaskNo()).stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
            }

            String orgType = entity.getOrgType();
            String orgTypeName = "";
            if (StringUtils.isNotEmpty(orgType)){
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
            entity.setOrgType(orgTypeName);
        }
    }

    /**
     * 编辑、查询获取对象数据
     *
     * @param id   任务主表id
     * @param type 编辑或者新增
     * @return 封装对象
     */
    public CrTaskInfoVO getTaskInfoVo(String id, String type) {
        CrTaskInfoVO taskInfoVo = new CrTaskInfoVO();
        CrTaskInfo taskInfo = taskInfoDao.get(id);
        BeanUtils.copyProperties(taskInfo, taskInfoVo);
        //模板指标
        List<CrmScoringIndicatorSolidificatiVO> modelScoringJson = crmComputationalModelService.getModelScoringJson(taskInfoVo.getComputationalModelId());
        //已维护任务的指标,这里会多出
        List<CtrScoringCorpContrastVo> contrastList = corpContrastService.getListByModelIdsAndTaskNo(taskInfoVo.getTaskNo());
        Map<String, List<CtrScoringCorpContrastVo>> corpContrastMap = getCorpContrastMap(contrastList);
        for (CrmScoringIndicatorSolidificatiVO model : modelScoringJson) {
            if (corpContrastMap.containsKey(model.getScoringIndicatorId())) {
                List<CtrScoringCorpContrastVo> corpContrastVoList = corpContrastMap.get(model.getScoringIndicatorId());
                if (CommonConstant.YES.equals(type)) {
                    String corpNames = corpContrastVoList.stream().map(CtrScoringCorpContrastVo::getCorpName).distinct().collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
                    model.setCorpIds(corpNames);
                } else {
                    String corpIds = corpContrastVoList.stream().map(CtrScoringCorpContrastVo::getCorpId).distinct().collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
                    model.setCorpIds(corpIds);
                }
            }
        }
        taskInfoVo.setScoringList(modelScoringJson);
        List<CrTaskOrgInfoVo> orgInfoVoList = taskOrgService.getListByTaskNo(taskInfoVo.getTaskNo());
        if (CommonConstant.YES.equals(type)) {
            taskInfoVo.setOrgName(orgInfoVoList.stream().map(CrTaskOrgInfoVo::getOrgName).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
            Map<String, DictDTO> dictMap = dictService.listMap("INSTITUTIONA_TYPE");
            String[] orgTypes = taskInfo.getOrgType().split(CommonConstant.COMMA_SPLIT);
            StringBuilder orgTypeName = new StringBuilder();
            for (int i = 0; i < orgTypes.length; i++) {
                DictDTO dictDTO = dictMap.get(orgTypes[i]);
                if (i == orgTypes.length - 1) {
                    orgTypeName.append(dictDTO.getName());
                } else {
                    orgTypeName.append(dictDTO.getName()).append(CommonConstant.COMMA_SPLIT);
                }
            }
            taskInfoVo.setOrgType(orgTypeName.toString());
        } else {
            taskInfoVo.setOrgIds(orgInfoVoList.stream().map(CrTaskOrgInfoVo::getOrgId).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
        }
        return taskInfoVo;
    }

    private Map<String, List<CtrScoringCorpContrastVo>> getCorpContrastMap(List<CtrScoringCorpContrastVo> contrastList) {
        Map<String, List<CtrScoringCorpContrastVo>> corpContrastMap = new HashMap<>();
        for (CtrScoringCorpContrastVo corpContrastVo : contrastList) {
            List<CtrScoringCorpContrastVo> vos = new ArrayList<>();
            //如果map中不包含该指标id并且固化指标父id为空，则新建一个list，并将当前对象添加到list中
            if (!corpContrastMap.containsKey(corpContrastVo.getScoringIndicatorSolidificati()) && StringUtils.isEmpty(corpContrastVo.getParentId())) {
                vos.add(corpContrastVo);
                corpContrastMap.put(corpContrastVo.getScoringIndicatorSolidificati(), vos);
            //如果map包含指标id，将对象加入该key对应的list当中
            } else if (corpContrastMap.containsKey(corpContrastVo.getScoringIndicatorSolidificati())) {
                corpContrastMap.get(corpContrastVo.getScoringIndicatorSolidificati()).add(corpContrastVo);
            }else if (corpContrastMap.containsKey(corpContrastVo.getParentId())) {
                vos.add(corpContrastVo);
                corpContrastMap.put(corpContrastVo.getScoringIndicatorSolidificati(),vos);
                corpContrastMap.get(corpContrastVo.getParentId()).add(corpContrastVo);
            } else {
                vos.add(corpContrastVo);
                corpContrastMap.put(corpContrastVo.getParentId(), vos);
            }
        }
        return corpContrastMap;
    }

    public void submit(CrTaskInfoVO taskInfoVo, ICurrentUser user, String saveType) {
        if (saveType.equals(CommonConstant.YES)) {
            taskInfoVo.setTaskNo(SequenceSingleton.getSequence(SequenceEnum.CR_TASK_NO));
        }
        if (null != taskInfoVo.getRmk() && taskInfoVo.getRmk().length() > 800) {
            throw new ParameterException(CreditRatingEnum.CREDIT_TASK_RMK_TOO_LONG);
        }
        if(!EmptyUtils.isEmpty(taskInfoVo.getOrgType())) {
            String[] institutionTypes = taskInfoVo.getOrgType().split(",");
            //用的同一个评级类型
            String iType = null;
            for (String institutionType : institutionTypes) {
                List<CrmScoringGradeVO> gradeVOS = crmScoringGradeDao.getScoringGradeByType(institutionType, null);
                if(gradeVOS==null || gradeVOS.size()==0){
                    //机构无标准
                    throw new ParameterException(CreditRatingEnum.NOT_RATING_STANDARD);
                }else {
                    if(null!=iType && !iType.equals(gradeVOS.get(0).getId())){
                   //标准不一致
                    throw new ParameterException(CreditRatingEnum.NOT_RATING_STANDARD);
                    }
                    iType=gradeVOS.get(0).getId();
                }
            }
        }
        //新增单位数据
        if (null != taskInfoVo.getScoringList() && !taskInfoVo.getScoringList().isEmpty()) {
            List<CrmScoringIndicatorSolidificatiVO> scoringList = taskInfoVo.getScoringList();
            List<String> corpIds = new ArrayList<>();
            for (CrmScoringIndicatorSolidificatiVO item : scoringList) {
                corpIds.addAll(Arrays.asList(item.getCorpIds().split(CommonConstant.COMMA_SPLIT)));
            }
            List<String> distinctCorpList = corpIds.stream().distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            if (distinctCorpList.size() <= 0) {
                throw new BusinessException(CreditRatingEnum.EVALUATION_CORP_IS_NOT_NULL);
            }
            taskInfoVo.setEvaluateCnt(distinctCorpList.size());
            //新增任务单位表数据
            saveCrTaskCorpAndOrg(distinctCorpList, taskInfoVo, user);
            //新增指标跟单位表数据
            saveScoringCorpContrastAndOrg(scoringList, taskInfoVo, user);
        }
        if (StringUtils.isEmpty(taskInfoVo.getId())) {
            taskInfoVo.setCreator(user.getLoginName());
            taskInfoVo.setCreateDate(DateUtils.getDate());
        }
        CrTaskInfo taskInfo = new CrTaskInfo();
        BeanUtils.copyNotNullProperties(taskInfoVo, taskInfo);
        CrmComputationalModel crmComputationalModel = crmComputationalModelService.get(taskInfoVo.getComputationalModelId());
        taskInfo.setTemplateName(crmComputationalModel.getModelName());
        taskInfo.setEvaluateCnt(taskInfoVo.getEvaluateCnt());
        //提交
        if (taskInfoVo.getOperationType().equals(CommonConstant.YES)) {
            taskInfo.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            taskInfoDao.saveOrUpdate(taskInfo);
            if (saveType.equals(CommonConstant.YES)) {
//                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION, "新增提交", user);
                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION, CreditRatingConstant.EVALUATION_LOG_LANG_ADDSUBMIT, user);
            } else {
//                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION, "编辑提交", user);
                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION, CreditRatingConstant.EVALUATION_LOG_LANG_EDITSUBMIT, user);
            }
            //挂接审批
            getStartHandler().handler(CreWorkFlowConstant.CR_TASK_INFO_FLOW, taskInfo.getId(), taskInfo, user.getCorpId(), user.getId());
        } else {
            if (saveType.equals(CommonConstant.YES)) {
//                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION, "新增暂存", user);
                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION, CreditRatingConstant.EVALUATION_LOG_LANG_ADDTEMP, user);
            } else {
//                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION, "编辑暂存", user);
                saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION, CreditRatingConstant.EVALUATION_LOG_LANG_EDITTEMP, user);
            }
            //暂存
            taskInfo.setStatus(CommonConstant.STATUE_TEMP);
            taskInfoDao.saveOrUpdate(taskInfo);
        }
    }

    /**
     * 新增消息
     *
     * @param id            单据id
     * @param operationType 日志类型
     * @param msg           日志消息
     * @param user          用户
     */
    private void saveMessage(String id, String operationType, String msg, ICurrentUser user) {
        EvaluationLog log = new EvaluationLog();
        log.setQuoteId(id);
        log.setLogType(operationType);
        log.setMsg(msg);
        log.setCreateName(user.getUsername());
        log.setCreateBy(user.getLoginName());
        log.setCreateDate(DateUtils.getDate());
        evaluationLogService.saveEntity(log);
    }

    /**
     * 新增指标和任务表数据和机构数据
     *
     * @param scoringList 指标和单位数据
     * @param taskInfoVo  任务数据
     * @param user        用户
     */
    private void saveScoringCorpContrastAndOrg(List<CrmScoringIndicatorSolidificatiVO> scoringList, CrTaskInfoVO taskInfoVo, ICurrentUser user) {
        //父指标id集合
        List<String> scoringParentIdList = scoringList.stream().map(CrmScoringIndicatorSolidificatiVO::getId).collect(Collectors.toList());
        //指标集合
        Map<String, List<CrmScoringIndicatorSolidificatiVO>> indicatorMap = solidService.getScoringList(taskInfoVo.getComputationalModelId());
        //key-->指标父id，value-->需要评价的单位
        Map<String, String> corpIdsMap = scoringList.stream().collect(Collectors.toMap(CrmScoringIndicatorSolidificatiVO::getId, CrmScoringIndicatorSolidificatiVO::getCorpIds));
        //机构id集合数据
        String[] orgIdList = taskInfoVo.getOrgIds().split(CommonConstant.COMMA_SPLIT);
        List<CrScoringOrgContrast> orgContrastList = new ArrayList<>();
        List<CtrScoringCorpContrast> corpContrastList = new ArrayList<>();
        CrScoringOrgContrast orgContrast;
        CtrScoringCorpContrast corpContrast;
        for (String indicatorId : scoringParentIdList) {
            if (indicatorMap.containsKey(indicatorId)) {
                List<CrmScoringIndicatorSolidificatiVO> solidificatiList = indicatorMap.get(indicatorId);
                //一个指标对应多个单位
                for (CrmScoringIndicatorSolidificatiVO entity : solidificatiList) {
                    String corpIds = corpIdsMap.get(indicatorId);
                    if (StringUtils.isNotEmpty(corpIds)) {
                        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
                            corpContrast = new CtrScoringCorpContrast();
                            corpContrast.setTaskNo(taskInfoVo.getTaskNo());
                            corpContrast.setComputationalModelId(taskInfoVo.getComputationalModelId());
                            corpContrast.setCorpId(corpId);
                            corpContrast.setScoringIndicatorSolidificati(entity.getScoringIndicatorId());
                            corpContrast.setParentId(entity.getParentId());
                            corpContrast.setScoringIndicatorName(entity.getScoringIndicatorName());
                            corpContrast.setScoringIndicatorCode(entity.getScoringIndicatorCode());
                            corpContrast.setScoringIndicatorDescription(entity.getScoringIndicatorDescription());
                            //指标系数
                            corpContrast.setScoringIndicatorCoefficient(entity.getScoringIndicatorCoefficient());
                            corpContrast.setIsAutoScoring(entity.getIsAutoScoring());
                            corpContrast.setIndexProperty(entity.getIndexProperty());
                            corpContrast.setScoringMethod(entity.getScoringMethod());
                            corpContrast.setLevel_(entity.getLevel());
                            corpContrast.setScoringRubric(entity.getScoringRubric());
                            corpContrast.setIsAddOrSubGradeName(entity.getIsAddOrSubGradeName());
                            corpContrastList.add(corpContrast);
                        }
                    }
                    //一个指标对应多个机构
                    for (String orgId : orgIdList) {
                        orgContrast = new CrScoringOrgContrast();
                        orgContrast.setTaskNo(taskInfoVo.getTaskNo());
                        orgContrast.setComputationalModelId(taskInfoVo.getComputationalModelId());
                        orgContrast.setOrgId(orgId);
                        orgContrast.setScoringIndicatorSolidificati(entity.getScoringIndicatorId());
                        orgContrast.setParentId(entity.getParentId());
                        orgContrast.setScoringIndicatorName(entity.getScoringIndicatorName());
                        orgContrast.setScoringIndicatorCode(entity.getScoringIndicatorCode());
                        orgContrast.setScoringIndicatorDescription(entity.getScoringIndicatorDescription());
                        orgContrast.setScoringIndicatorCoefficient(entity.getScoringIndicatorCoefficient());
                        orgContrastList.add(orgContrast);
                    }
                }
            }
        }
        orgContrastService.batchSave(orgContrastList);
        corpContrastService.batchSave(corpContrastList);
    }

    /**
     * 新增单位任务表数据
     *
     * @param taskInfoVo 任务
     * @param user       用户信息
     */
    private void saveCrTaskCorpAndOrg(List<String> distinctCorpList, CrTaskInfoVO taskInfoVo, ICurrentUser user) {
        CrTaskCorpInfo taskCorpInfo;
        CrTaskOrgInfo orgInfo;
        List<CrTaskCorpInfo> corpInfoList = new ArrayList<>();
        List<CrTaskOrgInfo> orgInfoList = new ArrayList<>();
        for (String corpId : distinctCorpList) {
            if (StringUtils.isNotEmpty(corpId)) {
                taskCorpInfo = new CrTaskCorpInfo();
                taskCorpInfo.setCorpId(corpId);
                taskCorpInfo.setTaskNo(taskInfoVo.getTaskNo());
                taskCorpInfo.setCreator(user.getId());
                taskCorpInfo.setCreateDate(DateUtils.getDate());
                corpInfoList.add(taskCorpInfo);
            }
        }
        List<String> orgIdList = Arrays.asList(taskInfoVo.getOrgIds().split(CommonConstant.COMMA_SPLIT));
        List<InstitutionDetailsVo> institutionDetailsVoList = crdRawDataService.findInstitutionByIds(orgIdList);
        Map<String, String> orgMap = institutionDetailsVoList.stream().collect(Collectors.toMap(InstitutionDetailsVo::getInsId, InstitutionDetailsVo::getInstitutionName));
        for (String orgId : orgIdList) {
            orgInfo = new CrTaskOrgInfo();
            orgInfo.setOrgId(orgId);
            orgInfo.setOrgName(orgMap.get(orgId));
            orgInfo.setTaskNo(taskInfoVo.getTaskNo());
            orgInfo.setCreator(user.getId());
            orgInfo.setCreateDate(DateUtils.getDate());
            orgInfoList.add(orgInfo);
        }
        taskCorpService.batchSave(corpInfoList);
        taskOrgService.batchSave(orgInfoList);
    }

    public CrTaskInfoVO getTaskByNo(String taskNo) {
        return taskInfoDao.getTaskByNo(taskNo);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        CrTaskInfo taskInfo = this.get(workFlowResult.getBusinessKey(), true);
        //不同意
        if (!workFlowResult.isPass()) {
            taskInfo.setStatus(CommonConstant.STATUE_BACK);
//            saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE, "任务审批打回", workFlowResult.getUser());
            saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE, CreditRatingConstant.EVALUATION_LOG_LANG_TASKBACK, workFlowResult.getUser());
        } else {
            taskInfo.setStatus(CommonConstant.STATUE_CHECK);
//            saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_CODE, "任务审批通过", workFlowResult.getUser());
            saveMessage(taskInfo.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_CODE, CreditRatingConstant.EVALUATION_LOG_LANG_TASKAPPROVAL, workFlowResult.getUser());
            //计算模型、评分指标固化,评估报表生成
            evaluationAnalysisService.createEvaluationReport(taskInfo);
        }
        this.updateEntity(taskInfo);

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }
}
