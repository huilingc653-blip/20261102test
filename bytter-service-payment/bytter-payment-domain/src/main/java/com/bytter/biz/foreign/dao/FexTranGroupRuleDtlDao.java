package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranGroupRuleDtl;
import com.bytter.biz.foreign.vo.FexTranGroupRuleDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易组规则
 * @date 2020-02-18
 */
@Repository
public class FexTranGroupRuleDtlDao extends BaseDao<FexTranGroupRuleDtl, String> {
    /**
     * <p>
     * 查询详细交易规则的数据集合
     * </p>
     *
     * @param ruleId 规则组id
     * @return List<FexTranGroupRuleDtlVO>
     */
    public List<FexTranGroupRuleDtlVO> selectListById(String ruleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id,a.rule_id,a.tran_type,a.pro_id,b.rule_name,b.valid_sign");
        sql.append(" FROM");
        sql.append(" fex_tran_group_rule_dtl a");
        sql.append(" LEFT JOIN fex_tran_group_rule b ON a.rule_id = b.id");
        sql.append(" WHERE 1=1");
        if (StringUtils.isNotEmpty(ruleId)) {
            sql.append(" AND a.rule_id = '").append(ruleId).append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null, FexTranGroupRuleDtlVO.class);
    }

    /**
     * <p>
     * 根据ID 查询详细
     * </p>
     *
     * @param id 规则详细id
     * @return FexTranGroupRuleDtlVO
     */
    public FexTranGroupRuleDtlVO getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id,a.rule_id,a.tran_type,a.pro_id,b.rule_name,b.valid_sign");
        sql.append(" FROM");
        sql.append(" fex_tran_group_rule_dtl a");
        sql.append(" LEFT JOIN fex_tran_group_rule b ON a.rule_id = b.id");
        sql.append(" WHERE 1=1");
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" AND a.id = '").append(id).append("'");
        }
        return getBySQLAliasToBean(sql.toString(), null, FexTranGroupRuleDtlVO.class);
    }

    /**
     * <p>
     * 查询规则组数据
     * </p>
     *
     * @param dataTablesInput [条件]
     * @return DataTablesOutput数据源
     */
    public DataTablesOutput selectDataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id,a.rule_id,a.tran_type,a.pro_id,b.rule_name,b.valid_sign,c.pro_name");
        sql.append(" FROM");
        sql.append(" fex_tran_group_rule_dtl a");
        sql.append(" LEFT JOIN fex_tran_group_rule b ON a.rule_id = b.id");
        sql.append(" LEFT JOIN fex_product c ON a.pro_id = c.id");
        sql.append(" WHERE 1 = 1");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranGroupRuleDtlVO.class, "a", false);
    }


    /**
     * <p>
     * 查询规则详细数据集合
     * </p>
     *
     * @param ruleId 规则组ID
     * @return List<FexTranGroupRuleDtlVO>
     */
    public List<FexTranGroupRuleDtlVO> selectListByRuleId(String ruleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" id,rule_id,tran_type,pro_id");
        sql.append(" FROM fex_tran_group_rule_dtl");
        sql.append(" WHERE 1=1");
        if (ruleId != null) {
            sql.append(" AND rule_id = '").append(ruleId).append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null, FexTranGroupRuleDtlVO.class);
    }
}
