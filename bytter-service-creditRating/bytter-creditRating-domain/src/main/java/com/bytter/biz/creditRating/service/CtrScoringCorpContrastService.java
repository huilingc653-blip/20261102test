package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrScoringCorpContrastDao;
import com.bytter.biz.creditRating.entity.CtrScoringCorpContrast;
import com.bytter.biz.creditRating.vo.CtrScoringCorpContrastVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 组织指标对照表service
 *
 * @author huangyl
 */
@Service(value = "ctrScoringCorpContrastService")
@Transactional
public class CtrScoringCorpContrastService extends BaseService<CtrScoringCorpContrast, String> {

    @Autowired
    private CrScoringCorpContrastDao corpContrastDao;

    @Override
    public BaseDao<CtrScoringCorpContrast, String> getEntityDao() {
        return corpContrastDao;
    }

    /**
     * 获取组装树形任务集合
     *
     * @param taskNo 任务编号
     * @return 树形集合
     */
    public List<CtrScoringCorpContrastVo> getTaskScoringJson(String taskNo, String corpId) {
        List<CtrScoringCorpContrastVo> corpContrastVoList = corpContrastDao.getListByModelIdsAndTaskNo(taskNo, corpId);
        return this.setCorpScoring(corpContrastVoList);
    }

    /**
     * 获取单位评分指标集合
     *
     *
     * @param taskNo 任务编号
     * @return
     */
    public List<CtrScoringCorpContrastVo> getTaskScoring(String taskNo) {
        return corpContrastDao.getListByModelIdsAndTaskNo(taskNo,"");
    }

    /**
     * 组装树形
     *,
     * @param corpContrastVoList 任务指标数据集合
     * @return 树形集合
     */
    private List<CtrScoringCorpContrastVo> setCorpScoring(List<CtrScoringCorpContrastVo> corpContrastVoList) {
        List<CtrScoringCorpContrastVo> thisScoringList = corpContrastVoList.stream().sorted(Comparator.comparing(CtrScoringCorpContrastVo::getLevel_).reversed()).collect(Collectors.toList());
        List<CtrScoringCorpContrastVo> returnScoringList = new ArrayList<>();
        for (int i = 0; i < thisScoringList.size(); i++) {
            CtrScoringCorpContrastVo solidificatiVO = thisScoringList.get(i);
            int count = 0;//所占行数
            int maxLevel = thisScoringList.get(0).getLevel_();
            for (int j = 0; j < thisScoringList.size(); j++) {
                CtrScoringCorpContrastVo soVO = thisScoringList.get(j);
                if (solidificatiVO.getScoringIndicatorSolidificati().equals(soVO.getParentId())) {
                    List<CtrScoringCorpContrastVo> childrenScoringList = new ArrayList<>();
                    if (!EmptyUtils.isEmpty(solidificatiVO.getChildrenScoringList())) {
                        childrenScoringList = solidificatiVO.getChildrenScoringList();
                    }
                    childrenScoringList.add(soVO);
                    solidificatiVO.setChildrenScoringList(childrenScoringList);
                    if (!EmptyUtils.isEmpty(soVO.getRowSpan())) {
                        count += soVO.getRowSpan();
                    }
                }
            }
            //设置行
            if (count != 0) {
                solidificatiVO.setRowSpan(count);
            } else {
                solidificatiVO.setRowSpan(1);
            }
            //设置列
            if (EmptyUtils.isEmpty(solidificatiVO.getChildrenScoringList())) {
                int colNum = maxLevel - solidificatiVO.getLevel_() + 1;
                solidificatiVO.setColSpan(colNum);
            }
            if (EmptyUtils.isEmpty(solidificatiVO.getParentId())) {
                returnScoringList.add(solidificatiVO);
            }
        }
        return returnScoringList;
    }

    public List<CtrScoringCorpContrastVo> getListByModelIdsAndTaskNo(String taskNo) {
        return corpContrastDao.getListByModelIdsAndTaskNo(taskNo, null);
    }

    public void deleteByTaskNo(String taskNo) {
        corpContrastDao.deleteByTaskNo(taskNo);
    }
}
