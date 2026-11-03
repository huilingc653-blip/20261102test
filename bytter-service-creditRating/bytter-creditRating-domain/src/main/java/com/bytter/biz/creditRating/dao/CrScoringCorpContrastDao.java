package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CtrScoringCorpContrast;
import com.bytter.biz.creditRating.vo.CtrScoringCorpContrastVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 组织评分指标对照表dao
 *
 * @author huangyl
 */
@Repository
public class CrScoringCorpContrastDao extends BaseDao<CtrScoringCorpContrast, String> {

    public List<CtrScoringCorpContrastVo> getListByModelIdsAndTaskNo(String taskNo, String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ctr.*,s.name as corpName ");
        sql.append(" from CRT_SCORING_CORP_CONTRAST ctr left join sys_corp s on s.id = ctr.corp_id");
        sql.append(" where ctr.task_no = :taskNo ");
        params.put("taskNo", taskNo);
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and ctr.corp_id =:corpId ");
            params.put("corpId", corpId);
        }
        return this.listBySQLAliasToBean(sql.toString(), params, CtrScoringCorpContrastVo.class);
    }

    public void deleteByTaskNo(String taskNo) {
        String sql = "delete from CRT_SCORING_CORP_CONTRAST where task_no = :taskNo";
        Map<String, Object> map = new HashMap<>();
        map.put("taskNo", taskNo);
        createSQLQuery(sql, map).executeUpdate();
    }
}
