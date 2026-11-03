package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmScoringGradeLevel;
import com.bytter.biz.creditRating.vo.CrmScoringGradeLevelVO;
import com.bytter.core.base.BaseDao;
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
public class CrmScoringGradeLevelDao extends BaseDao<CrmScoringGradeLevel,String> {

    /**
     * 功能介绍 :根据评分等级查询评分等级级别<br>
     */
    public List<CrmScoringGradeLevelVO> getScoringGradeLexelByGradeId(String gradeId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sgl.*");
        sql.append(" from SCORING_GRADE_LEVEL sgl ");
        sql.append(" where sgl.SCORING_GRADE_ID=:gradeId");
        sql.append(" ORDER BY CASE WHEN LOWER_SCORE_LIMIT_LEVEL IS NULL THEN 1 ELSE 0 END,LOWER_SCORE_LIMIT_LEVEL DESC ");
        params.put("gradeId",gradeId);
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringGradeLevelVO.class);
    }

    /**
     * 根据评分等级删除评分等级级别
     * @param gradeId
     */
    public void deleteScoringGradeLexelByGradeId(String gradeId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" delete from SCORING_GRADE_LEVEL where SCORING_GRADE_ID=:gradeId");
        params.put("gradeId",gradeId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
