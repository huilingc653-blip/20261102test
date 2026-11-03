package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrTaskInfoDao;
import com.bytter.biz.creditRating.dao.CrmComputationalModelDao;
import com.bytter.biz.creditRating.dao.CrmModelBatchDao;
import com.bytter.biz.creditRating.dao.CrmModelFormulaDao;
import com.bytter.biz.creditRating.entity.CrmComputationalModel;
import com.bytter.biz.creditRating.entity.CrmModelBatch;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/12<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("crmComputationalModelService")
@Transactional
public class CrmComputationalModelService extends BaseService<CrmComputationalModel,String> {

    @Autowired
    private CrmComputationalModelDao crmComputationalModelDao;

    @Autowired
    private CrmModelFormulaDao crmModelFormulaDao;

    @Autowired
    private CrmModelBatchDao crmModelBatchDao;

    @Autowired
    private CrmScoringIndicatorSolidService crmScoringIndicatorSolidService;

    @Autowired
    private CrScoringIndicatorService crScoringIndicatorService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Autowired
    private CrTaskInfoDao taskInfoDao;
    /**
     * 功能介绍 : 根据ID查询CrmComputationalModel对象<br>
     */
    @Override
    public BaseDao<CrmComputationalModel, String> getEntityDao() {
        return crmComputationalModelDao;
    }

    /**
     * 功能介绍 : 计算模型列表查询<br>
     */
    public DataTablesOutput getModelDataTables(DataTablesInput dataTablesInput){
        return crmComputationalModelDao.getModelDataTables(dataTablesInput);
    }

    /**
     * 功能介绍 : 根据ID查询CrmComputationalModelVO对象<br>
     */
    public CrmComputationalModelVO getComputationalModelById(String id){
        return crmComputationalModelDao.getComputationalModelById(id);
    }

    /**
     * 功能介绍 :查询所有CrmComputationalModelVO对象<br>
     */
    public List<CrmComputationalModelVO> getAllComputationalModel(){
        return crmComputationalModelDao.getAllComputationalModel();
    }

    /**
     * 功能介绍 :查询所有CrmModelFormulaVO对象<br>
     */
    public List<CrmModelFormulaVO> getCrmModelFormulaVOs(){
        return crmModelFormulaDao.getCrmModelFormulaVOs();
    }

    /**
     * <b>功能介绍：</b>生成模型批次号<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getCrmModelBatchNumber() {
        return SequenceSingleton.getSequence(SequenceEnum.CRM_MODEL_BATCH_NUMBER, "M");
    }

    /**
     * <b>功能介绍：</b>生成模型编号<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String makeCrmModelCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CRM_COMPUTATION_MODEL_CODE, "CM");
    }
    /**
     * 计算模型保存
     */
    public void submit(CrmComputationalModelVO crmComputationalModelVO, ICurrentUser user){
            //计算模型保存
        CrmComputationalModel crmComputationalModel;
        if(EmptyUtils.isEmpty(crmComputationalModelVO.getId())){
            //新增
            //创建批次
            String modelBatchNumber=this.getCrmModelBatchNumber();
            CrmModelBatch crmModelBatch=new CrmModelBatch();
            crmModelBatch.setModelBatchNumber(modelBatchNumber);
            crmModelBatch.setCreator(user.getLoginName());
            crmModelBatch.setCreateDate(new Date());
            crmModelBatchDao.save(crmModelBatch);

            crmComputationalModel=new CrmComputationalModel();
            BeanUtils.copyNotNullProperties(crmComputationalModelVO,crmComputationalModel);
            crmComputationalModel.setModelCode(this.makeCrmModelCode());
            crmComputationalModel.setModelBatchId(crmModelBatch.getId());
            crmComputationalModel.setBatchNumber(crmModelBatch.getModelBatchNumber());
            crmComputationalModel.setStatus(CreditRatingConstant.APPROVED);
            crmComputationalModel.setCreator(user.getLoginName());
            crmComputationalModel.setCreateDate(new Date());
            this.saveEntity(crmComputationalModel);
        }else {
            //变更
            crmComputationalModel=this.get(crmComputationalModelVO.getId());
            if (taskInfoDao.isExistByModelId(crmComputationalModelVO.getId())) {
                //已被评级任务引用的模板不允许修改，但没有做任何修改时不需要报错，直接忽略本次提交
                if (isSameAsStored(crmComputationalModelVO, crmComputationalModel)) {
                    return;
                }
                throw new BusinessException(CreditRatingEnum.CREDIT_TASK_EXIST_COMPUTATIONAL_MODEL);
            }
            BeanUtils.copyNotNullProperties(crmComputationalModelVO,crmComputationalModel);
            this.updateEntity(crmComputationalModel);
        }

        //保存关联指标信息
        crmScoringIndicatorSolidService.saveScoringIndicatorForModel(crmComputationalModelVO,crmComputationalModel);
    }

    /**
     * 判断本次提交的内容是否与已保存的模板一致，即没有做任何修改
     * <p>只比较页面上可以修改的内容：模板基础信息、指标增删、指标权重</p>
     *
     * @param crmComputationalModelVO 页面提交的数据
     * @param crmComputationalModel   数据库中的模板
     * @return true：没有做任何修改
     */
    private boolean isSameAsStored(CrmComputationalModelVO crmComputationalModelVO, CrmComputationalModel crmComputationalModel) {
        //模板基础信息
        if (!isSameText(crmComputationalModel.getModelName(), crmComputationalModelVO.getModelName())
                || !isSameText(crmComputationalModel.getModelDescription(), crmComputationalModelVO.getModelDescription())
                || !isSameText(crmComputationalModel.getValidSign(), crmComputationalModelVO.getValidSign())) {
            return false;
        }
        //勾选了新的指标
        if (StringUtils.isNotEmpty(crmComputationalModelVO.getNewScoringIds())) {
            return false;
        }
        Map<String, BigDecimal> storedCoefficientMap = crmScoringIndicatorSolidService.getIndicatorCoefficientMapForModel(crmComputationalModel.getId());
        //指标增删：页面保留的固化指标id必须与库中完全一致
        Set<String> submittedIds = new HashSet<>();
        for (String scoringId : StringUtils.split(StringUtils.defaultString(crmComputationalModelVO.getOldScoringIds()), ",")) {
            if (StringUtils.isNotBlank(scoringId)) {
                submittedIds.add(scoringId.trim());
            }
        }
        if (!storedCoefficientMap.keySet().equals(submittedIds)) {
            return false;
        }
        //指标权重
        Map<String, BigDecimal> submittedCoefficientMap = crmComputationalModelVO.getScCoefficientMap();
        if (submittedCoefficientMap == null || submittedCoefficientMap.isEmpty()) {
            return storedCoefficientMap.isEmpty();
        }
        for (Map.Entry<String, BigDecimal> entry : storedCoefficientMap.entrySet()) {
            BigDecimal submittedCoefficient = submittedCoefficientMap.get(entry.getKey());
            BigDecimal storedCoefficient = entry.getValue() == null ? BigDecimal.ZERO : entry.getValue();
            if (submittedCoefficient == null || submittedCoefficient.compareTo(storedCoefficient) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个文本是否一致，null与空串视为相同
     */
    private boolean isSameText(String stored, String submitted) {
        return StringUtils.equals(StringUtils.defaultString(stored), StringUtils.defaultString(submitted));
    }


    /**
     * 制作层型结构数据
     * @param scoringList
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO> setTableDateScoring(List<CrmScoringIndicatorSolidificatiVO> scoringList){
        List<CrmScoringIndicatorSolidificatiVO>  thisScoringList = scoringList.stream().sorted(Comparator.comparing(CrmScoringIndicatorSolidificatiVO::getLevel).reversed()).collect(Collectors.toList());
        List<CrmScoringIndicatorSolidificatiVO> returnScoringList=new ArrayList<>();
        for(int i=0;i<thisScoringList.size();i++){
             CrmScoringIndicatorSolidificatiVO solidificatiVO=thisScoringList.get(i);
             int count=0;//所占行数
             int maxLevel=thisScoringList.get(0).getLevel();
            if(StringUtils.isNotEmpty(solidificatiVO.getScoringRubric())) {
                solidificatiVO.setScoringRubric(solidificatiVO.getScoringRubric().replace("\n", "<br>"));
            }
             for(int j=0;j<thisScoringList.size();j++){
                 CrmScoringIndicatorSolidificatiVO soVO=thisScoringList.get(j);
                 if(solidificatiVO.getScoringIndicatorId().equals(soVO.getParentId())){
                     List<CrmScoringIndicatorSolidificatiVO> childrenScoringList=new ArrayList<>();
                     if(!EmptyUtils.isEmpty(solidificatiVO.getChildrenScoringList())) {
                         childrenScoringList = solidificatiVO.getChildrenScoringList();
                     }
                     childrenScoringList.add(soVO);
                     solidificatiVO.setChildrenScoringList(childrenScoringList);
                     if(!EmptyUtils.isEmpty(soVO.getRowSpan())){
                         count+=soVO.getRowSpan();
                     }
                 }
             }
             //设置行
             if(count!=0){
                 solidificatiVO.setRowSpan(count);
             }else {
                 solidificatiVO.setRowSpan(1);
             }
             //设置列
            if(EmptyUtils.isEmpty(solidificatiVO.getChildrenScoringList())){
                int colNum=maxLevel-solidificatiVO.getLevel()+1;
                solidificatiVO.setColSpan(colNum);
            }else {
                solidificatiVO.setColSpan(1);
            }
             if(EmptyUtils.isEmpty(solidificatiVO.getParentId())){
                 returnScoringList.add(solidificatiVO);
             }
         }
        return returnScoringList;
    }

    /**
     * 功能介绍 :查询所有有效CrmComputationalModelVO对象<br>
     */
    public List<CrmScoringIndicatorSolidificatiVO> getModelScoringJson(String modelId){
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorList= crmScoringIndicatorSolidService.getIndicatorListByModelId(modelId);
        List<CrmScoringIndicatorSolidificatiVO> returnScoringList=this.setTableDateScoring(scoringIndicatorList);
        return returnScoringList;
    }

    /**
     * 将新获取与固化获取合并数据
     * @param newScoringIds
     * @param oldScoringIds
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO>  getScoringForShow(String newScoringIds, String oldScoringIds, String modelId, List<ScCoefficients> scCoefficientsList){
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorList=new ArrayList<>();
        //新需要从指标表获取数据
        List<CrScoringIndicatorVo> newScoringIndicators =crScoringIndicatorService.getByIds(Arrays.asList(newScoringIds.split(",")));

       //从固化表获取数据
        List<CrmScoringIndicatorSolidificatiVO> oldScoringIndicators= crmScoringIndicatorSolidService.getIndicatorForOldAndNew(oldScoringIds.split(","),modelId);
        for(CrScoringIndicatorVo crScoringIndicatorVo:newScoringIndicators){
            CrmScoringIndicatorSolidificatiVO solidificatiVO=new CrmScoringIndicatorSolidificatiVO();
            BeanUtils.copyNotNullProperties(crScoringIndicatorVo,solidificatiVO);
            solidificatiVO.setScoringIndicatorId(solidificatiVO.getId());
            if(!EmptyUtils.isEmpty(solidificatiVO.getIsAddOrSubGrade())) {
                String typeName = dictService.get("sf_yn", solidificatiVO.getIsAddOrSubGrade().intValue());
                solidificatiVO.setIsAddOrSubGradeName(typeName);
            }
            if(StringUtils.isNotEmpty(solidificatiVO.getScoringRubric())) {
                solidificatiVO.setScoringRubric(solidificatiVO.getScoringRubric().replace("\n", "<br>"));
            }
            scoringIndicatorList.add(solidificatiVO);
        }
        for(CrmScoringIndicatorSolidificatiVO crmScoringIndicatorSolidificatiVO:oldScoringIndicators){
            if(StringUtils.isNotEmpty(crmScoringIndicatorSolidificatiVO.getScoringRubric())) {
                crmScoringIndicatorSolidificatiVO.setScoringRubric(crmScoringIndicatorSolidificatiVO.getScoringRubric().replace("\n", "<br>"));
            }
            scoringIndicatorList.add(crmScoringIndicatorSolidificatiVO);
        }
        List<CrmScoringIndicatorSolidificatiVO> returnScoringList=this.setTableDateScoring(scoringIndicatorList);

        // 对每个 CrmScoringIndicatorSolidificatiVO 进行排序
        this.sortScoringIndicatorsRecursively(returnScoringList);
        //判断之前是否选择了该指标并且填写了权重，如果填写了，则保存该权重
        if (scCoefficientsList!=null && !scCoefficientsList.isEmpty()) {
            this.saveOldScoringIndicatorCoefficient(returnScoringList,scCoefficientsList);
        }
        return returnScoringList;
    }

    /**
     * 判断之前是否选择了该指标并且填写了权重，如果填写了，则保存该权重
     * @param returnScoringList
     * @param scCoefficientsList
     */
    public void saveOldScoringIndicatorCoefficient(List<CrmScoringIndicatorSolidificatiVO> returnScoringList, List<ScCoefficients> scCoefficientsList){
        for (CrmScoringIndicatorSolidificatiVO vo : returnScoringList) {
            if (vo.getChildrenScoringList() != null && !vo.getChildrenScoringList().isEmpty()) {
                saveOldScoringIndicatorCoefficient(vo.getChildrenScoringList(), scCoefficientsList);
            }else{
                for (ScCoefficients scCoefficients : scCoefficientsList)
                    //使用递归，找到该对象的最底层的childrenScoringList
                    if (vo.getScoringIndicatorId().equals(scCoefficients.getId())) {
                         //这里权重需要进行换算除以100,前端有利率的换算
                         if (scCoefficients.getValue() != null){
                             vo.setScoringIndicatorCoefficient(scCoefficients.getValue().divide(new BigDecimal(100)));
                         }
                    }
                 }
            }
    }
    public void sortScoringIndicatorsRecursively(List<CrmScoringIndicatorSolidificatiVO> scoringList) {
        if (scoringList != null && !scoringList.isEmpty()) {
            scoringList.sort(Comparator.comparing(CrmScoringIndicatorSolidificatiVO::getScoringIndicatorCode));
            for (CrmScoringIndicatorSolidificatiVO vo : scoringList) {
                sortScoringIndicatorsRecursively(vo.getChildrenScoringList());
            }
        }
    }

    /**
     * 删除模型
     * @param modelId
     */
    public void deleteModel(String modelId){
        Boolean isExistByModel = taskInfoDao.isExistByModelId(modelId);
        if (isExistByModel) {
           throw new BusinessException(CreditRatingEnum.CREDIT_TASK_EXIST_COMPUTATIONAL_MODEL);
        }
        CrmComputationalModel crmComputationalModel = crmComputationalModelDao.get(modelId);
        crmComputationalModel.setStatus(CreditRatingConstant.DELETE);
        crmComputationalModelDao.update(crmComputationalModel);
    }

}
