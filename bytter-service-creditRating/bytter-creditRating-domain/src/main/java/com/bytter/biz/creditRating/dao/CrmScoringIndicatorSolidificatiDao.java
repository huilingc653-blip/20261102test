package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmScoringIndicatorSolidificati;
import com.bytter.biz.creditRating.vo.CrmScoringIndicatorSolidificatiVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/16<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class CrmScoringIndicatorSolidificatiDao extends BaseDao<CrmScoringIndicatorSolidificati, String> {

    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorListByModelId(String modelId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sis.*");
        sql.append(" from SCORING_INDICATOR_SOLIDIFICATI sis ");
        sql.append(" where sis.COMPUTATIONAL_MODEL_ID=:modelId");
        params.put("modelId", modelId);
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringIndicatorSolidificatiVO.class);
    }

    /**
     * 根据原指标ids获取固化指标对应值
     *
     * @param ids
     * @return
     */
    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorForOldAndNew(String[] ids,String modelId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sis.id as id,sis.SCORING_INDICATOR_ID as scoringIndicatorId,sis.PARENT_ID as parentId,sis.SCORING_INDICATOR_CODE as scoringIndicatorCode,");
        sql.append(" sis.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,sis.SCORINGRUBRIC as scoringRubric,");
        sql.append(" sis.SCORING_INDICATOR_NAME as scoringIndicatorName,sis.SCORING_INDICATOR_COEFFICIENT as scoringIndicatorCoefficient,sis.LEVEL_ as level_");
        sql.append(" from SCORING_INDICATOR_SOLIDIFICATI sis");
        sql.append(" where").append(SqlUtil.getInSql(ids, "sis.SCORING_INDICATOR_ID", true));
        sql.append(" and sis.computational_model_id=:modelId");
        params.put("modelId",modelId);
        sql.append(" order by sis.create_date");
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringIndicatorSolidificatiVO.class);
    }

    public List<CrmScoringIndicatorSolidificatiVO> getIndicatorListByScoringId(String scoringId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sis.id as id,sis.SCORING_INDICATOR_ID as scoringIndicatorId,sis.PARENT_ID as parentId,sis.SCORING_INDICATOR_CODE as scoringIndicatorCode,");
        sql.append(" sis.SCORING_INDICATOR_NAME as scoringIndicatorName");
        sql.append(" from SCORING_INDICATOR_SOLIDIFICATI sis ");
        sql.append(" where sis.SCORING_INDICATOR_ID=:scoringId");
        params.put("scoringId", scoringId);
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringIndicatorSolidificatiVO.class);
    }
}
