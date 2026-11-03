package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrQuaMethodForScoringInd;
import com.bytter.biz.creditRating.vo.CrQuaMethodForScoringIndVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CrQuaMethodForScoringIndDao extends BaseDao<CrQuaMethodForScoringInd, String> {
    public List<CrQuaMethodForScoringIndVo> batchSelectBySids(List<String> sidList) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> param = new HashMap<>();
        sql.append(" select qmf.ID as id,qmf.SCORING_INDICATOR_ID as scoringIndicatorId,qmf.SCORING_INDICATOR_CODE as scoringIndicatorCode,qmf.SCORING_INDICATOR_NAME as scoringIndicatorName,");
        sql.append(" qmf.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,qmf.CATEGORY_ENCODING as categoryEnCoding,qmf.CATEGORY_NAME as categoryName,qmf.VALUE as value,");
        sql.append(" qmf.OTHERVALUE as otherValue,qmf.VALUE_NAME as valueName,qmf.VALUE_NAME_CODE as valueNameCode,qmf.VALUE_DIRECTION AS valueDirection,qmf.OTHERVALUE_DIRECTION as otherValueDirection,qmf.GRADE as grade,");
        sql.append(" qmf.VERSION_NUMBER as versionNumber,qmf.CREATOR as creator,qmf.CREATE_DATE as createDate from CR_QUA_METHOD_FOR_SCORING_IND qmf where 1=1 ");
        sql.append(" and "+getSQLIn(sidList,900,"qmf.SCORING_INDICATOR_ID"));
        return listBySQLAliasToBean(sql.toString(),param, CrQuaMethodForScoringIndVo.class);
    }
    public List<CrQuaMethodForScoringIndVo> getBySid(String sid) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> param = new HashMap<>();
        sql.append(" select qmf.ID as id,qmf.SCORING_INDICATOR_ID as scoringIndicatorId,qmf.SCORING_INDICATOR_CODE as scoringIndicatorCode" +
                ",qmf.SCORING_INDICATOR_NAME as scoringIndicatorName,");
        sql.append(" qmf.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,qmf.CATEGORY_ENCODING as categoryEnCoding" +
                ",qmf.CATEGORY_NAME as categoryName,qmf.VALUE as value,");
        sql.append(" qmf.OTHERVALUE as otherValue,qmf.VALUE_NAME as valueName,qmf.VALUE_NAME_CODE as valueNameCode" +
                ",qmf.VALUE_DIRECTION AS valueDirection,qmf.OTHERVALUE_DIRECTION as otherValueDirection,qmf.GRADE as grade,");
        sql.append(" qmf.VERSION_NUMBER as versionNumber,qmf.CREATOR as creator,qmf.CREATE_DATE as createDate " +
                "from CR_QUA_METHOD_FOR_SCORING_IND qmf where 1=1 ");
        sql.append(" and qmf.SCORING_INDICATOR_ID = :sid");
        param.put("sid",sid);
        return listBySQLAliasToBean(sql.toString(),param, CrQuaMethodForScoringIndVo.class);
    }
}
