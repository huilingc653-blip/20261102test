package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrmScoringGradeDao;
import com.bytter.biz.creditRating.dao.CrmScoringGradeLevelDao;
import com.bytter.biz.creditRating.entity.CrmScoringGrade;
import com.bytter.biz.creditRating.entity.CrmScoringGradeLevel;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.vo.CrmScoringGradeLevelVO;
import com.bytter.biz.creditRating.vo.CrmScoringGradeVO;
import com.bytter.biz.creditRating.vo.CrmSectionDataVO;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/28<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("crmScoringGradeService")
@Transactional
public class CrmScoringGradeService extends BaseService<CrmScoringGrade,String> {

    @Autowired
    private CrmScoringGradeDao crmScoringGradeDao;

    @Autowired
    private CrmScoringGradeLevelDao crmScoringGradeLevelDao;

    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<CrmScoringGrade, String> getEntityDao() {
        return crmScoringGradeDao;
    }

    /**
     * 功能介绍 : 计算等级列表查询<br>
     */
    public DataTablesOutput getGradeDataTables(DataTablesInput dataTablesInput){
        DataTablesOutput output= crmScoringGradeDao.getGradeDataTables(dataTablesInput);
        makeInstitutionTypeName(output);
        return output;
    }

    /**
     * 功能介绍 :根据评分等级查询评分等级级别<br>
     */
    public List<CrmScoringGradeLevelVO> getScoringGradeLexelByGradeId(String gradeId){
        return crmScoringGradeLevelDao.getScoringGradeLexelByGradeId(gradeId);
    }


    /**
     * 根据机构类别获取对应有效的评分标准（包括评级明细）
     * @param institutionType
     * @return
     */
    public CrmScoringGradeVO getScoringGradeByItype(String institutionType) {
        String[] institutionTypes = institutionType.split(",");
        List<CrmScoringGradeVO> gradeVOS = crmScoringGradeDao.getScoringGradeByType(institutionTypes[0], null);
//        if (gradeVOS==null || gradeVOS.size()==0){
//            throw new ParameterException(CreditRatingEnum.NOT_RATING_STANDARD);
//        }
        CrmScoringGradeVO crmScoringGradeVO=gradeVOS.get(0);
        List<CrmScoringGradeLevelVO> levelVOList=getScoringGradeLexelByGradeId(crmScoringGradeVO.getId());
        crmScoringGradeVO.setGradeLevels(levelVOList);
        return  crmScoringGradeVO;
    }

    /**
     * <b>功能介绍：</b>生成评级标准编号<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String makeCrmGradeCode() {
        return SequenceSingleton.getSequence(SequenceEnum.SCORING_GRADE_CODE, "SG");
    }

    /**
     * 功能介绍 : 前端单向符号与数值赋值<br>
     */
    public void  makeSymbolAndValue(List<CrmScoringGradeLevelVO> scoringGradeLevelVOS){
        for(CrmScoringGradeLevelVO levelVO:scoringGradeLevelVOS){
            if(!EmptyUtils.isEmpty(levelVO.getLowerScoreLimitLevel())){
                //下限分不为空则为正向
                if("(".equals(levelVO.getDownScoringSymbol())){
                    levelVO.setScoringSymbol("10");
                }else if("[".equals(levelVO.getDownScoringSymbol())){
                    levelVO.setScoringSymbol("11");
                }
                    levelVO.setScoreLimitLevel(levelVO.getLowerScoreLimitLevel());
            }else {
                //反向符号
                if(")".equals(levelVO.getUpScoringSymbol())){
                    levelVO.setScoringSymbol("20");
                }else if("]".equals(levelVO.getUpScoringSymbol())){
                    levelVO.setScoringSymbol("21");
                }
                levelVO.setScoreLimitLevel(levelVO.getUpperScoreLimitLevel());
            }
        }
    }

    /**
     * 功能介绍 : 机构名称赋值<br>
     */
    public void  makeInstitutionTypeName (DataTablesOutput output){
        List<CrmScoringGradeVO> gradeVOS=output.getData();
        if(gradeVOS.size()>0){
            for(CrmScoringGradeVO vo:gradeVOS){
                String[] institutionTypes=vo.getInstitutionType().split(",");
                String institutionTypeName="";
                for(String institutionType:institutionTypes){
                   String typeName= dictService.get("INSTITUTIONA_TYPE",institutionType);
                   if(institutionTypeName.equals("")){
                       institutionTypeName=typeName;
                   }else {
                        institutionTypeName=institutionTypeName+","+typeName;
                   }
                }
                vo.setInstitutionTypeName(institutionTypeName);
            }
        }
    }

    public void submit(CrmScoringGradeVO crmScoringGradeVO, ICurrentUser user){
        verifyInstitutionType(crmScoringGradeVO);
        if (crmScoringGradeVO.getScoringGradeDescription() != null &&
                crmScoringGradeVO.getScoringGradeDescription().length() > 200) {
            throw new ParameterException(CreditRatingEnum.CREDIT_RATING_DESCRIPTION_TOO_LONG);
        }
        CrmScoringGrade crmScoringGrade;
        if(EmptyUtils.isEmpty(crmScoringGradeVO.getId())){
            //新增
            crmScoringGrade=new CrmScoringGrade();
            BeanUtils.copyNotNullProperties(crmScoringGradeVO,crmScoringGrade);
            crmScoringGrade.setScoringGradeCode(this.makeCrmGradeCode());
            crmScoringGrade.setStatus(CreditRatingConstant.APPROVED);
            crmScoringGrade.setCreator(user.getLoginName());
            crmScoringGrade.setCreateDate(new Date());
            this.saveEntity(crmScoringGrade);
        } else{
            crmScoringGrade=this.get(crmScoringGradeVO.getId());
            BeanUtils.copyNotNullProperties(crmScoringGradeVO,crmScoringGrade);
            this.updateEntity(crmScoringGrade);
        }
        //保存评级标准明细
        List<CrmScoringGradeLevelVO> gradeLevelVOs=crmScoringGradeVO.getGradeLevels();
        if(!EmptyUtils.isEmpty(gradeLevelVOs) && gradeLevelVOs.size()>0) {
            List<CrmScoringGradeLevel> gradeLevels = new ArrayList<>();
            //先删除之前的评级标准明细
            crmScoringGradeLevelDao.deleteScoringGradeLexelByGradeId(crmScoringGrade.getId());
            for (CrmScoringGradeLevelVO levelVO : gradeLevelVOs) {
                CrmScoringGradeLevel gradeLevel=new CrmScoringGradeLevel();
                BeanUtils.copyNotNullProperties(levelVO,gradeLevel);
                gradeLevel.setScoringGradeId(crmScoringGrade.getId());
                gradeLevel.setCreator(user.getLoginName());
                gradeLevel.setCreateDate(new Date());
                gradeLevels.add(gradeLevel);
            }
            crmScoringGradeLevelDao.batchSave(gradeLevels);
        }
    }

    //机构是否维护评级标准校验
    public void verifyInstitutionType(CrmScoringGradeVO crmScoringGradeVO){
        String institutionTypeName="";
        String[] institutionTypes=crmScoringGradeVO.getInstitutionType().split(",");
        for(String institutionType:institutionTypes){
            List<CrmScoringGradeVO> gradeVOS= crmScoringGradeDao.getScoringGradeByType(institutionType,crmScoringGradeVO.getId());
            if(gradeVOS.size()>0){
                String typeName= dictService.get("INSTITUTIONA_TYPE",institutionType);
                if(institutionTypeName.equals("")){
                    institutionTypeName=typeName;
                }else {
                    institutionTypeName=institutionTypeName+","+typeName;
                }
            }
        }
        if(!institutionTypeName.equals("")) {
            throw new ParameterException(CreditRatingEnum.CREDIT_RATING_INSTITUTION_ONE, institutionTypeName);
        }
    }

    /**
     * 获取有效标准的机构类别code 字符串
     * @return
     */
    public String  getInstitutionTypeCodes(){
        List<CrmScoringGradeVO> gradeVOList=crmScoringGradeDao.getAllScoringGrade();
        return gradeVOList.stream().map(CrmScoringGradeVO::getInstitutionType).distinct().collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
    }

    /**
     * 获取机构类别对应的评分级别区间
     * @return
     */
    public Map<String,List<CrmSectionDataVO>> getGradeSectionData(){
        Map<String,List<CrmSectionDataVO>> gradeSectionDataMap=new HashMap<>();
        List<CrmScoringGradeVO> gradeVOList=crmScoringGradeDao.getAllScoringGrade();
        for(CrmScoringGradeVO gradeVO:gradeVOList){
            if(!EmptyUtils.isEmpty(gradeVO.getInstitutionType())){
                List<CrmScoringGradeLevelVO> scoringGradeLevelVOS= crmScoringGradeLevelDao.getScoringGradeLexelByGradeId(gradeVO.getId());
                List<CrmSectionDataVO> sectionDataVOList=this.makeGradeSectionList(scoringGradeLevelVOS);
                String[] institutionTypes= gradeVO.getInstitutionType().split(",");
                for(String institutionType:institutionTypes){
                    gradeSectionDataMap.put(institutionType,sectionDataVOList);
                }
            }
        }
        return gradeSectionDataMap;
    }

    public List<CrmSectionDataVO> makeGradeSectionList(List<CrmScoringGradeLevelVO> scoringGradeLevelVOS){
        List<CrmSectionDataVO> sectionDataVOList=new ArrayList<>();
        for(CrmScoringGradeLevelVO levelVO:scoringGradeLevelVOS){
            CrmSectionDataVO crmSectionDataVO=new CrmSectionDataVO();
            crmSectionDataVO.setLeftValue(levelVO.getLowerScoreLimitLevel());//左值
            crmSectionDataVO.setRightValue(levelVO.getUpperScoreLimitLevel());//右值
            //左边符号
            if("(".equals(levelVO.getDownScoringSymbol())){//小于
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_SYMBOL);
            }else if("[".equals(levelVO.getDownScoringSymbol())){//小于等于
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
           //右边符号
            if(")".equals(levelVO.getUpScoringSymbol())){//小于
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_SYMBOL);
            }else if("]".equals(levelVO.getUpScoringSymbol())){//小于等于
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
            //最高级别（开放上界，upperScoreLimitLevel 为空）下限取等：满分=下限时被开区间"("排除会导致评价等级为-9999
            if(levelVO.getUpperScoreLimitLevel() == null){
                crmSectionDataVO.setLeftSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
                crmSectionDataVO.setRightSymbol(CreditRatingConstant.DOWN_EQ_SYMBOL);
            }
            crmSectionDataVO.setScoringLevel(levelVO.getScoringLevel());
            sectionDataVOList.add(crmSectionDataVO);
        }
        return sectionDataVOList;
    }
}
