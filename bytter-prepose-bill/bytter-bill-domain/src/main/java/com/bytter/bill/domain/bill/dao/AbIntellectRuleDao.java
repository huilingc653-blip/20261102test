package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbIntellectRule;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 智能选票规则处理dao
 */
@Repository
public class AbIntellectRuleDao extends BaseDao<AbIntellectRule, String> {
    /**
     * 获取选票规则公共列sql
     *
     * @return 选票规则公共列sql
     */
    public StringBuilder selectCommonColumnSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, RULE_CODE as ruleCode, RULE_NAME as ruleName, VALID_SIGN as validSign, ");
        sql.append(" SCREEN_NUM as screenNum, RMK as rmk, apply_no as applyNo, create_by as createBy ");
        return sql;
    }
    /**
     * <b>功能：</b>获取有效的选票规则<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleVO> listAbIntellectRule() {
        StringBuilder hql = new StringBuilder();
        hql.append(" FROM AbIntellectRule WHERE validSign=:validSign  ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("validSign", "1");
        hql.append(" order by ruleName ");
        List<AbIntellectRuleVO> ruleGroupList = listByHQLAliasToBean(hql.toString(), params, AbIntellectRuleVO.class);
        return ruleGroupList;
    }
    /**
     * 查询选票规则列表数据
     *
     * @param input 前端传参
     * @return 选票规则列表数据
     */
    public DataTablesOutput<AbIntellectRuleVO> datatable(DataTablesInput input) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE r ");
        return getDynamicBySQL(input, sql.toString(), AbIntellectRuleVO.class, "r", true, null);
    }
    /**
     * 根据id查询有效的选票规则数据
     *
     * @param id 主键id
     * @return 选票规则数据
     */
    public AbIntellectRuleVO getAbIntellectRuleVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE r ");
        sql.append(" where valid_sign = :validSign and id = :id");
        Map<String, Object> params = new HashMap<>(2);
        params.put("validSign", "1");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleVO.class);
    }
    /**
     * 根据id查询有效的选票规则数据
     *
     * @param id 主键id
     * @return 选票规则数据
     */
    public AbIntellectRuleVO getAllAbIntellectRuleVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE r ");
        sql.append(" where id = :id");
        Map<String, Object> params = new HashMap<>(2);
        //params.put("validSign", "1");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleVO.class);
    }
    /**
     * 根据规则编码查询选票规则信息
     *
     * @param id       主键
     * @param ruleCode 规则编码
     * @return 选票规则数据
     */
    public AbIntellectRuleVO queryByRuleCode(String id, String ruleCode) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE r ");
        sql.append(" where rule_code = :ruleCode");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleCode", ruleCode);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleVO.class);
    }
    /**
     * 根据规则名称查询选票规则信息
     *
     * @param id       主键
     * @param ruleName 规则名称
     * @return 选票规则数据
     */
    public AbIntellectRuleVO queryByRuleName(String id, String ruleName) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE r ");
        sql.append(" where rule_name = :ruleName");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleName", ruleName);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleVO.class);
    }
    /**
     * 判断该选票规则是否被引用
     *
     * @param id 选票规则id
     * @return 是否被引用 true被引用
     */
    public boolean isQuote(String id) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder("select d.id from AB_INTELLECT_RULE r ");
        sql.append(" left join AB_INTELLECT_RULE_GROUP_Dtl d on d.rule_id = r.id where r.id = :id and d.rule_group_code is not null ");
        params.put("id", id);
        List<AbIntellectRuleVO> list = listBySQLAliasToBean(sql.toString(), params, AbIntellectRuleVO.class);
        return !CollectionUtils.isEmpty(list);
    }
}
