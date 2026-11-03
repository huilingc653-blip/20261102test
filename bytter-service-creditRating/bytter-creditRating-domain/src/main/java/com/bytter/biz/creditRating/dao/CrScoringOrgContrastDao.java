package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrScoringOrgContrast;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/**
 * 评分机构评分指标对照表dao
 * @author huangyl
 */
@Repository
public class CrScoringOrgContrastDao extends BaseDao<CrScoringOrgContrast, String> {

    public void deleteByTaskNo(String taskNo) {
        String sql = "delete from CR_SCORING_ORG_CONTRAST where task_no = :taskNo";
        Map<String, Object> map = new HashMap<>();
        map.put("taskNo", taskNo);
        createSQLQuery(sql, map).executeUpdate();
    }
}
