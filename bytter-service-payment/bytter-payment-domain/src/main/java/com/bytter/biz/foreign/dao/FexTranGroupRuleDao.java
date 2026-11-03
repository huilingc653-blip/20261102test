package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranGroupRule;
import com.bytter.core.base.BaseDao;
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
public class FexTranGroupRuleDao extends BaseDao<FexTranGroupRule, String> {
    /**
     * <p>
     * 查询规则组数据集合
     * </p>
     *
     * @param validSign 是否有效
     * @return List<FexTranGroupRule>
     */
    public List<FexTranGroupRule> selectListByValidSign(Boolean validSign) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" id as id, rule_name as ruleName, rule_desc as ruleDesc, rmk as rmk, valid_sign as validSign");
        sql.append(" FROM fex_tran_group_rule");
        sql.append(" WHERE 1=1");
        if (validSign != null) {
            sql.append(" AND valid_sign = ").append(validSign ? "1" : "0");
        }
        return listBySQLAliasToBean(sql.toString(), null, FexTranGroupRule.class);
    }

    /**
     * <p>
     * 根据名称查询规则组
     * </p>
     *
     * @param ruleName 名称
     * @return FexTranGroupRule
     */
    public FexTranGroupRule getByRuleName(String ruleName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" id as id, rule_name as ruleName, rule_desc as ruleDesc, rmk as rmk, valid_sign as validSign");
        sql.append(" FROM fex_tran_group_rule");
        sql.append(" WHERE 1=1");
        if (StringUtils.isNotBlank(ruleName)) {
            sql.append(" AND rule_name = '").append(ruleName).append("'");
        }
        return getBySQLAliasToBean(sql.toString(), null, FexTranGroupRule.class);
    }
}
