package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmScoringGrade;
import com.bytter.biz.creditRating.vo.CrmScoringGradeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
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
public class CrmScoringGradeDao extends BaseDao<CrmScoringGrade, String> {

    /**
     * 功能介绍 : 计算等级列表查询<br>
     */
    public DataTablesOutput getGradeDataTables(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * ");
        sql.append(" from SCORING_GRADE sg ");
        sql.append(" where status=95");
        return getDynamicBySQL(dataTablesInput, sql.toString(), CrmScoringGradeVO.class, null, false);
    }

    public List<CrmScoringGradeVO> getAllScoringGrade() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sg.*");
        sql.append(" from SCORING_GRADE sg ");
        sql.append(" where sg.status=95");
        sql.append(" and sg.valid_sign='1'");
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringGradeVO.class);
    }

    /**
     * 功能介绍 :根据模型id查询评分等级<br>
     */
    public List<CrmScoringGradeVO> getScoringGradeByModelId(String modelId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sg.*");
        sql.append(" from SCORING_GRADE sg ");
        sql.append(" where sg.valid_sign='1'");
        sql.append(" and sg.COMPUTATIONAL_MODEL_ID=:modelId");
        params.put("modelId", modelId);
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringGradeVO.class);
    }

    /**
     * 功能介绍 :根据机构类别获取评分标准,注意：可带着评分标准id查询，查询挂机构类别除了评分标准id外的评分标准<br>
     * @param institutionType 机构类别
     * @param id 评分标准id
     * @return
     */
    public List<CrmScoringGradeVO> getScoringGradeByType(String institutionType, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sg.*");
        sql.append(" from SCORING_GRADE sg ");
        sql.append(" where sg.valid_sign='1'");
        sql.append(" and sg.status=95");
        if (!EmptyUtils.isEmpty(institutionType)) {
            sql.append(" and sg.INSTITUTION_TYPE like :institutionType");
            params.put("institutionType", "%" + institutionType + "%");
        }
        if (!EmptyUtils.isEmpty(id)) {
            sql.append(" and sg.id<>:id");
            params.put("id", id);
        }
        return this.listBySQLAliasToBean(sql.toString(), params, CrmScoringGradeVO.class);
    }
}
