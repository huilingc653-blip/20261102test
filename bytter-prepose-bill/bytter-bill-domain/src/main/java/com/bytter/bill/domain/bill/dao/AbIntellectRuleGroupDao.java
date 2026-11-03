package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleGroup;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 智能选票规则组组处理dao
 *
 * @author ouyj
 */
@Repository
public class AbIntellectRuleGroupDao extends BaseDao<AbIntellectRuleGroup, String> {
    /**
     * 获取选票规则组公共列sql
     *
     * @return 选票规则组公共列sql
     */
    public StringBuilder selectCommonColumnSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select r.id, r.RULE_GROUP_CODE as ruleGroupCode, r.RULE_GROUP_NAME as ruleGroupName, r.VALID_SIGN as validSign, ");
        sql.append(" r.RELATION as relation, r.SCREEN_NUM as screenNum, r.RMK as rmk, r.apply_no as applyNo ");
        return sql;
    }
    /**
     * 查询选票规则组列表数据
     *
     * @param input 前端传参
     * @return 选票规则组列表数据
     */
    public DataTablesOutput<AbIntellectRuleGroupVO> datatable(DataTablesInput input) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP r ");
        return getDynamicBySQL(input, sql.toString(), AbIntellectRuleGroupVO.class, "r", true, null);
    }
    /**
     * 根据id查询有效的选票规则组数据
     *
     * @param id 主键id
     * @return 选票规则组数据
     */
    public AbIntellectRuleGroupVO getAbIntellectRuleGroupVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP r ");
        sql.append(" where valid_sign = :validSign and id = :id");
        Map<String, Object> params = new HashMap<>(2);
        params.put("validSign", "1");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupVO.class);
    }
    /**
     * 根据id查询选票规则组数据
     *
     * @param id 主键id
     * @return 选票规则组数据
     */
    public AbIntellectRuleGroupVO getAllAbIntellectRuleGroupVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP r ");
        sql.append(" where id = :id");
        Map<String, Object> params = new HashMap<>(2);
        //params.put("validSign", "1");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupVO.class);
    }
    /**
     * 根据规则编码查询选票规则组信息
     *
     * @param id            主键
     * @param ruleGroupCode 规则编码
     * @return 选票规则组数据
     */
    public AbIntellectRuleGroupVO queryByRuleGroupCode(String id, String ruleGroupCode) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP r ");
        sql.append(" where rule_group_code = :ruleGroupCode");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleGroupCode", ruleGroupCode);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupVO.class);
    }
    /**
     * 根据规则名称查询选票规则组信息
     *
     * @param id            主键
     * @param ruleGroupName 规则名称
     * @return 选票规则组数据
     */
    public AbIntellectRuleGroupVO queryByRuleGroupName(String id, String ruleGroupName) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP r ");
        sql.append(" where rule_group_name = :ruleGroupName");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleGroupName", ruleGroupName);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupVO.class);
    }
    /**
     * 根据规则组编码删除对应的规则组明细数据
     *
     * @param ruleGroupCode 规则组编码
     */
    public void delDtlByRuleGroupCode(String ruleGroupCode) {
        StringBuilder sql = new StringBuilder("delete from AB_INTELLECT_RULE_GROUP_DTL where rule_group_code = :ruleGroupCode");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleGroupCode", ruleGroupCode);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * <b>功能：</b>获取有效的选票规则组组<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleGroupVO> listAbIntellectRuleGroup() {
        StringBuilder hql = new StringBuilder();
        hql.append(" FROM AbIntellectRuleGroup WHERE validSign=:validSign  ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", "1");
        hql.append(" order by ruleGroupName ");
        List<AbIntellectRuleGroupVO> ruleGroupList = listByHQLAliasToBean(hql.toString(), params, AbIntellectRuleGroupVO.class);
        return ruleGroupList;
    }
}
