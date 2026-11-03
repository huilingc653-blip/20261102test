package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrmQuantitativeMethodForSisDao;
import com.bytter.biz.creditRating.dao.CrmScoringIndicatorSolidificatiDao;
import com.bytter.biz.creditRating.entity.CrmComputationalModel;
import com.bytter.biz.creditRating.entity.CrmQuantitativeMethodForSis;
import com.bytter.biz.creditRating.entity.CrmScoringIndicatorSolidificati;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/9/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("crmScoringIndicatorSolidService")
@Transactional
public class CrmScoringIndicatorSolidService extends BaseService<CrmScoringIndicatorSolidificati, String> {

    @Autowired
    private CrmScoringIndicatorSolidificatiDao crmScoringIndicatorSolidificatiDao;

    @Autowired
    private CrmQuantitativeMethodForSisDao crmQuantitativeMethodForSisDao;

    @Autowired
    private CrScoringIndicatorService crScoringIndicatorService;

    @Autowired
    private CrQuaMethodForScoringIndService crQuaMethodForScoringIndService;

    @Autowired
    private CrmComputationalModelService crmComputationalModelService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<CrmScoringIndicatorSolidificati, String> getEntityDao() {
        return crmScoringIndicatorSolidificatiDao;
    }

    public void saveScoringIndicatorForModel(CrmComputationalModelVO crmComputationalModelVO, CrmComputationalModel crmComputationalModel) {
        String newScoringIds = crmComputationalModelVO.getNewScoringIds();
        String oldScoringIds = crmComputationalModelVO.getOldScoringIds();
        String copyScoringIds = null;
        if (!EmptyUtils.isEmpty(crmComputationalModelVO.getCopyModelId())) {
            //复制功能 则将指标数据赋值给复制指标ids
            copyScoringIds = oldScoringIds;
            oldScoringIds = null;
        }
        Map<String, BigDecimal> scCoefficientMap = crmComputationalModelVO.getScCoefficientMap();
        if (StringUtils.isNotEmpty(copyScoringIds)) {
            //复制的指标新增
            //从固化表获取数据
            List<CrmScoringIndicatorSolidificatiVO> copyScoringIndicators = this.getIndicatorListByModelId(crmComputationalModelVO.getCopyModelId());
            List<CrmScoringIndicatorSolidificati> copyAddScoringList = new ArrayList<>();//固化表需新增数据
            List<CrmQuantitativeMethodForSis> copyQuaMethodForScoringIndList = new ArrayList<>();//固化指标定量表增加数据
            Map<String,String> scoringIndicatorIdMap = new HashMap<>();//记录复制的指标id
            for (CrmScoringIndicatorSolidificatiVO vo : copyScoringIndicators) {
                CrmScoringIndicatorSolidificati scoringIndicatorSolidificati = new CrmScoringIndicatorSolidificati();
                BeanUtils.copyNotNullProperties(vo, scoringIndicatorSolidificati);
                scoringIndicatorSolidificati.setModelBatchId(crmComputationalModel.getModelBatchId());
                scoringIndicatorSolidificati.setBatchNumber(crmComputationalModel.getBatchNumber());
                scoringIndicatorSolidificati.setComputatationalModelId(crmComputationalModel.getId());
                scoringIndicatorIdMap.put(scoringIndicatorSolidificati.getScoringIndicatorId(),vo.getId());
                scoringIndicatorSolidificati.setId(null);
                if (!EmptyUtils.isEmpty(scCoefficientMap.get(vo.getScoringIndicatorId()))) {
                    scoringIndicatorSolidificati.setScoringIndicatorCoefficient(scCoefficientMap.get(vo.getScoringIndicatorId()));
                }
                copyAddScoringList.add(scoringIndicatorSolidificati);
            }
            this.batchSave(copyAddScoringList);

            for (CrmScoringIndicatorSolidificati solidificati : copyAddScoringList) {
                //指标定量表数据
                List<CrmQuantitativeMethodForSisVO> quaMethodForScoringIndVoList = crmQuantitativeMethodForSisDao.getCrmQuantitativeBySid(scoringIndicatorIdMap.get(solidificati.getScoringIndicatorId()));
                for (CrmQuantitativeMethodForSisVO qVo : quaMethodForScoringIndVoList) {
                    CrmQuantitativeMethodForSis crmQuantitativeMethodForSis = new CrmQuantitativeMethodForSis();
                    BeanUtils.copyNotNullProperties(qVo, crmQuantitativeMethodForSis);
                    crmQuantitativeMethodForSis.setId(null);
                    crmQuantitativeMethodForSis.setScoringIndicatorSolId(solidificati.getId());

                    copyQuaMethodForScoringIndList.add(crmQuantitativeMethodForSis);
                }
            }
            crmQuantitativeMethodForSisDao.batchSave(copyQuaMethodForScoringIndList);
        }else {
            if (StringUtils.isNotEmpty(oldScoringIds)) {
                //从固化表获取数据
                List<CrmScoringIndicatorSolidificatiVO> oldScoringIndicators = this.getIndicatorListByModelId(crmComputationalModel.getId());
                List<CrmScoringIndicatorSolidificati> oldUpdateScoringList = new ArrayList<>();//固化表需变更数据
                List<String> oldDeleteIds = new ArrayList<>();//固化表需删除数据
                //部分保留之前的固化数据

                for (CrmScoringIndicatorSolidificatiVO vo : oldScoringIndicators) {
                    //如果新指标被覆盖则删除旧的指标
                    List<String> oldScoringIdList = Arrays.asList(oldScoringIds.split(","));
                    if (!oldScoringIdList.contains(vo.getScoringIndicatorId())) {
                        oldDeleteIds.add(vo.getId());
                    } else {
                        CrmScoringIndicatorSolidificati scoringIndicatorSolidificati = this.get(vo.getId());
                        scoringIndicatorSolidificati.setScoringIndicatorCoefficient(scCoefficientMap.get(vo.getScoringIndicatorId()));
                        oldUpdateScoringList.add(scoringIndicatorSolidificati);
                    }
                }
                //固化指标定量表数据
                List<CrmQuantitativeMethodForSisVO> deleQuantitativeVoList = crmQuantitativeMethodForSisDao.batchSelectBySids(oldDeleteIds);
                List<String> deleteQuanIds = deleQuantitativeVoList.stream().map(CrmQuantitativeMethodForSisVO::getId).distinct().collect(Collectors.toList());
                this.batchUpdate(oldUpdateScoringList);
                this.delete(oldDeleteIds);
                crmQuantitativeMethodForSisDao.delete(deleteQuanIds);
            } else {
                //删除所有旧的数据
                List<CrmScoringIndicatorSolidificatiVO> oldScoringIndicators = this.getIndicatorListByModelId(crmComputationalModel.getId());
                if (!EmptyUtils.isEmpty(oldScoringIndicators)) {
                    List<String> oldDeleteIds = oldScoringIndicators.stream().map(CrmScoringIndicatorSolidificatiVO::getId).distinct().collect(Collectors.toList());
                    this.delete(oldDeleteIds);
                    //删除指标定量表数据
                    List<CrmQuantitativeMethodForSisVO> deleQuantitativeVoList = crmQuantitativeMethodForSisDao.batchSelectBySids(oldDeleteIds);
                    List<String> deleteQuanIds = deleQuantitativeVoList.stream().map(CrmQuantitativeMethodForSisVO::getId).distinct().collect(Collectors.toList());
                    crmQuantitativeMethodForSisDao.delete(deleteQuanIds);
                }
            }
        }
        if (StringUtils.isNotEmpty(newScoringIds)) {
            //新需要从指标表获取数据
            List<CrScoringIndicatorVo> newScoringIndicators = crScoringIndicatorService.getByIds(Arrays.asList(newScoringIds.split(",")));
            List<CrmScoringIndicatorSolidificati> newAddScoringList = new ArrayList<>();//固化表需新增数据
            List<CrmQuantitativeMethodForSis> newQuaMethodForScoringIndList = new ArrayList<>();//固化指标定量表增加数据
            for (CrScoringIndicatorVo vo : newScoringIndicators) {
                CrmScoringIndicatorSolidificati scoringIndicatorSolidificati = new CrmScoringIndicatorSolidificati();
                BeanUtils.copyNotNullProperties(vo, scoringIndicatorSolidificati);
                scoringIndicatorSolidificati.setModelBatchId(crmComputationalModel.getModelBatchId());
                scoringIndicatorSolidificati.setBatchNumber(crmComputationalModel.getBatchNumber());
                scoringIndicatorSolidificati.setComputatationalModelId(crmComputationalModel.getId());
                scoringIndicatorSolidificati.setScoringIndicatorId(vo.getId());
                scoringIndicatorSolidificati.setId(null);
                if (!EmptyUtils.isEmpty(scCoefficientMap.get(vo.getId()))) {
                    scoringIndicatorSolidificati.setScoringIndicatorCoefficient(scCoefficientMap.get(vo.getId()));
                }
                newAddScoringList.add(scoringIndicatorSolidificati);
            }
            this.batchSave(newAddScoringList);

            for (CrmScoringIndicatorSolidificati solidificati : newAddScoringList) {
                List<CrQuaMethodForScoringIndVo> quaMethodForScoringIndVoList = crQuaMethodForScoringIndService.getBySid(solidificati.getScoringIndicatorId());
                for (CrQuaMethodForScoringIndVo qVo : quaMethodForScoringIndVoList) {
                    CrmQuantitativeMethodForSis crmQuantitativeMethodForSis = new CrmQuantitativeMethodForSis();
                    BeanUtils.copyNotNullProperties(qVo, crmQuantitativeMethodForSis);
                    crmQuantitativeMethodForSis.setId(null);
                    crmQuantitativeMethodForSis.setQuantitativeMethodForSiId(qVo.getId());
                    crmQuantitativeMethodForSis.setScoringIndicatorSolId(solidificati.getId());

                    newQuaMethodForScoringIndList.add(crmQuantitativeMethodForSis);
                }
            }
            crmQuantitativeMethodForSisDao.batchSave(newQuaMethodForScoringIndList);
        }
    }

    /**
     * 模型id获取模型固化指标数据
     *
     * @param modelId
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorListByModelId(String modelId) {
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS = crmScoringIndicatorSolidificatiDao.getIndicatorListByModelId(modelId);
        this.makeQuaMethodForScoringIndProperties(scoringIndicatorSolidificatiVOS);
        return scoringIndicatorSolidificatiVOS;
    }

    /**
     * 模型id获取模型固化指标的权重（原指标id -> 权重），用于比较模板内容是否发生变更
     *
     * @param modelId
     * @return
     */
    public Map<String, BigDecimal> getIndicatorCoefficientMapForModel(String modelId) {
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS = crmScoringIndicatorSolidificatiDao.getIndicatorListByModelId(modelId);
        Map<String, BigDecimal> coefficientMap = new HashMap<>();
        for (CrmScoringIndicatorSolidificatiVO vo : scoringIndicatorSolidificatiVOS) {
            coefficientMap.put(vo.getScoringIndicatorId(), vo.getScoringIndicatorCoefficient());
        }
        return coefficientMap;
    }

    /**
     * 根据计算模型获取所有对应的固化指标数据的定量表数据
     * @param modelId
     * @return
     */
    public List<CrmQuantitativeMethodForSisVO> getCrmQuantitativeByModelId(String modelId) {
        return crmQuantitativeMethodForSisDao.getCrmQuantitativeByModelId(modelId);
    }

    /**
     * 得到模型关联的固化指标对应原指标id的字符串
     * @param modelId
     * @return
     */
    public String getIndicatorListIdsForModel(String modelId) {
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS = crmScoringIndicatorSolidificatiDao.getIndicatorListByModelId(modelId);
        List<String> idList = scoringIndicatorSolidificatiVOS.stream().map(CrmScoringIndicatorSolidificatiVO::getScoringIndicatorId).distinct().collect(Collectors.toList());
        String ids = "";
        for (String id : idList) {
            if ("".equals(ids)) {
                ids = id;
            } else {
                ids = ids + "," + id;
            }
        }
        return ids;
    }

    /**
     * 根据固化指标父id查询所有的子集数据
     *
     * @param computationalModelId 模板id
     * @return 集合
     */
    public Map<String, List<CrmScoringIndicatorSolidificatiVO>> getScoringList(String computationalModelId) {
        List<CrmScoringIndicatorSolidificatiVO> solidificatiVOS = crmComputationalModelService.getModelScoringJson(computationalModelId);
        Map<String, List<CrmScoringIndicatorSolidificatiVO>> solidificatiVOMap = new HashMap<>();
        for (CrmScoringIndicatorSolidificatiVO solidificatiVO : solidificatiVOS) {
            List<CrmScoringIndicatorSolidificatiVO> list = new ArrayList<>();
            list.add(solidificatiVO);
            if (null != solidificatiVO.getChildrenScoringList() && !solidificatiVO.getChildrenScoringList().isEmpty()) {
                addChildrenScoringList(solidificatiVO, list);
            }
            solidificatiVOMap.put(solidificatiVO.getScoringIndicatorId(), list);
        }
        return solidificatiVOMap;
    }

    /**
     * 递归设置子层级
     * @param solidificatiVO 父对象
     * @param list 集合
     */
    private void addChildrenScoringList(CrmScoringIndicatorSolidificatiVO solidificatiVO, List<CrmScoringIndicatorSolidificatiVO> list) {
        List<CrmScoringIndicatorSolidificatiVO> childrenScoringList = solidificatiVO.getChildrenScoringList();
        for (CrmScoringIndicatorSolidificatiVO item : childrenScoringList) {
            list.add(item);
            if (null != item.getChildrenScoringList() && !item.getChildrenScoringList().isEmpty()) {
                addChildrenScoringList(item, list);
            }
        }
    }

    /**
     * 根据原指标ids获取固化指标对应值
     *
     * @param ids
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorForOldAndNew(String[] ids, String modelId) {
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS = crmScoringIndicatorSolidificatiDao.getIndicatorForOldAndNew(ids, modelId);
        this.makeQuaMethodForScoringIndProperties(scoringIndicatorSolidificatiVOS);
        return scoringIndicatorSolidificatiVOS;
    }

    /**
     * 给固化指标批量赋值定量值
     *
     * @param list
     */
    public void makeQuaMethodForScoringIndProperties(List<CrmScoringIndicatorSolidificatiVO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            List<String> sidList = new ArrayList<>();
            for (CrmScoringIndicatorSolidificatiVO scoringIndicatorSolidificatiVO : list) {
                sidList.add(scoringIndicatorSolidificatiVO.getId());
            }
            List<CrmQuantitativeMethodForSisVO> crmQuantitativeMethodForSisVOS = crmQuantitativeMethodForSisDao.batchSelectBySids(sidList);
            //根据评分指标id分组
            Map<String, List<CrmQuantitativeMethodForSisVO>> quaListGroupBySid = crmQuantitativeMethodForSisVOS.stream().collect(Collectors.groupingBy(CrmQuantitativeMethodForSisVO::getScoringIndicatorSolId));
            for (CrmScoringIndicatorSolidificatiVO scoringIndicatorSolidificatiVO : list) {
                if (quaListGroupBySid.containsKey(scoringIndicatorSolidificatiVO.getId())) {
                    List<CrmSectionDataVO> crmSectionDataVOList = new ArrayList<>();//指标区间
                    List<CrmQuantitativeMethodForSisVO> quantitativeMethodForSisVOS = quaListGroupBySid.get(scoringIndicatorSolidificatiVO.getId());
                    for (CrmQuantitativeMethodForSisVO sisVO : quantitativeMethodForSisVOS) {
                        //定量基础信息
                        setQuaMethodForScoringIndProperties(sisVO, scoringIndicatorSolidificatiVO);
                    }
                    for (CrmQuantitativeMethodForSisVO sisVO1 : quantitativeMethodForSisVOS) {
                        //设置评分细则
                        if (sisVO1.getValueNameCode() != null && scoringIndicatorSolidificatiVO.getScoringMethod() != null) {
                            if (sisVO1.getValueNameCode().equals(String.valueOf(scoringIndicatorSolidificatiVO.getScoringMethod().intValue()))) {
                                crmSectionDataVOList.add(setModelSolidSectionData(sisVO1));
                            }
                        }
                    }
                    scoringIndicatorSolidificatiVO.setSectionDataVOList(crmSectionDataVOList);
                }
            }

        }

    }

    public CrmSectionDataVO setModelSolidSectionData(CrmQuantitativeMethodForSisVO sisVO) {
        CrmSectionDataVO crmSectionDataVO = new CrmSectionDataVO();
        //左边符号
        if (CreditRatingConstant.CREDIT_SCORING_GT.equals(sisVO.getValueDirection())) {//大于
            crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_GTE.equals(sisVO.getValueDirection())) {//大于等于
            crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_LT.equals(sisVO.getValueDirection())) {//小于
            crmSectionDataVO.setLeftSymbol(CreditRatingConstant.UP_SYMBOL);//小于
        } else if (CreditRatingConstant.CREDIT_SCORING_LTE.equals(sisVO.getValueDirection())) {//小于等于
            crmSectionDataVO.setLeftSymbol(CreditRatingConstant.UP_EQ_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_EQ.equals(sisVO.getValueDirection())) {//等于
            crmSectionDataVO.setLeftSymbol(CreditRatingConstant.EQ_SYMBOL);
        }
        //右边符号
        if (CreditRatingConstant.CREDIT_SCORING_GT.equals(sisVO.getOtherValueDirection())) {//大于
            crmSectionDataVO.setRightSymbol(CreditRatingConstant.UP_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_GTE.equals(sisVO.getOtherValueDirection())) {//大于等于
            crmSectionDataVO.setRightSymbol(CreditRatingConstant.UP_EQ_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_LT.equals(sisVO.getOtherValueDirection())) {//小于
            crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_LTE.equals(sisVO.getOtherValueDirection())) {//小于等于
            crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
        } else if (CreditRatingConstant.CREDIT_SCORING_EQ.equals(sisVO.getOtherValueDirection())) {//等于
            crmSectionDataVO.setRightSymbol(CreditRatingConstant.EQ_SYMBOL);
        }
        if (!EmptyUtils.isEmpty(sisVO.getValue())) {
            crmSectionDataVO.setLeftValue(sisVO.getValue());
        } else {
            if (CreditRatingConstant.DOWN_SYMBOL.equals(crmSectionDataVO.getLeftSymbol()) || CreditRatingConstant.DOWN_EQ_SYMBOL.equals(crmSectionDataVO.getLeftSymbol())) {
                crmSectionDataVO.setLeftValue(CreditRatingConstant.minLimit);
            } else if (CreditRatingConstant.UP_SYMBOL.equals(crmSectionDataVO.getLeftSymbol()) || CreditRatingConstant.UP_EQ_SYMBOL.equals(crmSectionDataVO.getLeftSymbol())) {
                crmSectionDataVO.setLeftValue(CreditRatingConstant.maxLimit);
            }
        }
        if (!EmptyUtils.isEmpty(sisVO.getOtherValue())) {
            crmSectionDataVO.setRightValue(sisVO.getOtherValue());
        } else {
            if (CreditRatingConstant.DOWN_SYMBOL.equals(crmSectionDataVO.getRightSymbol()) || CreditRatingConstant.DOWN_EQ_SYMBOL.equals(crmSectionDataVO.getRightSymbol())) {
                crmSectionDataVO.setRightValue(CreditRatingConstant.maxLimit);
            } else if (CreditRatingConstant.UP_SYMBOL.equals(crmSectionDataVO.getRightSymbol()) || CreditRatingConstant.UP_EQ_SYMBOL.equals(crmSectionDataVO.getRightSymbol())) {
                crmSectionDataVO.setRightValue(CreditRatingConstant.minLimit);
            }
        }
        crmSectionDataVO.setGrade(sisVO.getGrade());
        return crmSectionDataVO;
    }

    /**
     * 将定量方法的数据转换称vo中对应的字段
     *
     * @param crmQuantitativeMethodForSisVO
     * @param crmScoringIndicatorSolidificatiVO
     */
    private void setQuaMethodForScoringIndProperties(CrmQuantitativeMethodForSisVO crmQuantitativeMethodForSisVO, CrmScoringIndicatorSolidificatiVO crmScoringIndicatorSolidificatiVO) {
        switch (crmQuantitativeMethodForSisVO.getValueNameCode()) {
            case CreditRatingConstant.INDICATOR_DIRECTION_CODE://指标方向
                crmScoringIndicatorSolidificatiVO.setIndicatorDirectionValue(crmQuantitativeMethodForSisVO.getValue());
                break;
            case CreditRatingConstant.MAX_POSSIBLE_VALUE_CODE://最大可能值
                crmScoringIndicatorSolidificatiVO.setMaxPossibleValue(crmQuantitativeMethodForSisVO.getValue());
                break;
            case CreditRatingConstant.MIN_POSSIBLE_VALUE_CODE://最小可能值
                crmScoringIndicatorSolidificatiVO.setMinPossibleValue(crmQuantitativeMethodForSisVO.getValue());
                break;
            case CreditRatingConstant.IS_AUTO_SCORING_CODE://是否自动评分
                crmScoringIndicatorSolidificatiVO.setIsAutoScoring(crmQuantitativeMethodForSisVO.getValue());
                break;
            case CreditRatingConstant.SCORING_METHOD_CODE://评分方法
                if (crmQuantitativeMethodForSisVO.getValue()!= null) {
                    crmScoringIndicatorSolidificatiVO.setScoringMethod(crmQuantitativeMethodForSisVO.getValue());
                    crmScoringIndicatorSolidificatiVO.setScoringMethodName(dictService.get(CreditRatingConstant.SCORING_METHOD_GROUPCODE, crmScoringIndicatorSolidificatiVO.getScoringMethod().intValue()));
                }
                break;
            case CreditRatingConstant.IS_ADD_OR_SUB_GRADE_CODE://是否额外加/减分
                if (crmQuantitativeMethodForSisVO.getValue()!= null) {
                    crmScoringIndicatorSolidificatiVO.setIsAddOrSubGrade(crmQuantitativeMethodForSisVO.getValue());
                    String typeName = dictService.get("sf_yn", crmQuantitativeMethodForSisVO.getValue().intValue());
                    crmScoringIndicatorSolidificatiVO.setIsAddOrSubGradeName(typeName);
                }
                break;
            case CreditRatingConstant.INDEX_PROPERTY_CODE://指标性质
                crmScoringIndicatorSolidificatiVO.setIndexProperty(crmQuantitativeMethodForSisVO.getValue());
                break;
        }
    }

    /**
     * 指标id获取模型数据
     *
     * @param scoringId
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorListByScoringId(String scoringId) {
        List<CrmScoringIndicatorSolidificatiVO> scoringIndicatorSolidificatiVOS = crmScoringIndicatorSolidificatiDao.getIndicatorListByScoringId(scoringId);
        return scoringIndicatorSolidificatiVOS;
    }

    /**
     * 根据计算模型id得到指标map(包含得分区间)
     * @param modelId
     * @return
     */
    public Map<String, CrmScoringIndicatorSolidificatiVO> getModelSectionDta(String modelId) {
        Map<String, CrmScoringIndicatorSolidificatiVO> crmSectionDataMap = new HashMap<>();
        //得到模型所有指标以及指标的的定量属性
        List<CrmScoringIndicatorSolidificatiVO> solidificatiVOS = this.getIndicatorListByModelId(modelId);
        for (CrmScoringIndicatorSolidificatiVO solidificatiVO : solidificatiVOS) {
            crmSectionDataMap.put(solidificatiVO.getId(), solidificatiVO);
        }
        return crmSectionDataMap;
    }
}
