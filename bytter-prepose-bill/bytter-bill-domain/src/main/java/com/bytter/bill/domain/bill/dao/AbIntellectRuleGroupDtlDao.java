package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleGroupDtl;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 智能选票规则组明细处理dao
 *
 * @author ouyj
 */
@Repository
public class AbIntellectRuleGroupDtlDao extends BaseDao<AbIntellectRuleGroupDtl, String> {
    /**
     * 获取选票规则组明细公共列sql
     *
     * @return 选票规则组明细公共列sql
     */
    public StringBuilder selectCommonColumnSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select r.id as id, r.APPLY_NO as applyNo, r.RULE_GROUP_CODE as ruleGroupCode, r.RULE_ID as ruleId, ");
        sql.append(" r.DTL_GROUP_CODE as dtlGroupCode, ");
        sql.append(" r.RELATION as relation, r.SERIALNO as serialNo ");
        return sql;
    }
    /**
     * <b>功能：</b>获取有效的选票规则组明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleGroupDtlVO> listAbIntellectRuleGroupDtl() {
        StringBuilder hql = new StringBuilder();
        hql.append(" FROM AbIntellectRuleGroupDtl WHERE validSign=:validSign  ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("validSign", "1");
        hql.append(" order by ruleName ");
        List<AbIntellectRuleGroupDtlVO> ruleGroupList = listByHQLAliasToBean(hql.toString(), params, AbIntellectRuleGroupDtlVO.class);
        return ruleGroupList;
    }
    /**
     * 查询选票规则组明细列表数据
     *
     * @param input 前端传参
     * @return 选票规则组明细列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" , rule.rmk as ruleRmk, rule.RULE_NAME as ruleName");
        sql.append(" from AB_INTELLECT_RULE_GROUP_DTL r ");
        sql.append(" left join AB_INTELLECT_RULE rule on r.RULE_ID = rule.id ");
        sql.append(" where 1 = 1 ");
        return getDynamicBySQL(input, sql.toString(), AbIntellectRuleGroupDtlVO.class, "r", false, null);
    }
    /**
     * 根据id查询选票规则组明细数据
     *
     * @param id 主键id
     * @return 选票规则组明细数据
     */
    public AbIntellectRuleGroupDtlVO getAbIntellectRuleGroupDtlVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_GROUP_DTL r ");
        sql.append(" where r.dtl_group_code = :dtlGroupCode");
        Map<String, Object> params = new HashMap<>(2);
        params.put("dtlGroupCode", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupDtlVO.class);
    }
    /**
     * 根据申请单号查询对应的选票规则组明细列表
     *
     * @param applyNo 申请单号
     * @return 选票规则组明细列表
     */
    public List<AbIntellectRuleGroupDtlVO> listRuleGroupDtlByApplyNo(String applyNo) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" , rule.rmk as ruleRmk, rule.RULE_NAME as ruleName");
        sql.append(" from AB_INTELLECT_RULE_GROUP_DTL r ");
        sql.append(" left join AB_INTELLECT_RULE rule on r.RULE_ID = rule.id ");
        sql.append(" where r.apply_no = :applyNo order by r.SERIALNO ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("applyNo", applyNo);
        return listBySQLAliasToBean(sql.toString(), params, AbIntellectRuleGroupDtlVO.class);
    }
    /**
     * 根据申请单号集合修改规则组编码
     *
     * @param applyNo       申请单号
     * @param ruleGroupCode 规则组编码
     */
    public void updateRuleGroupCodeByApplyNo(String applyNo, String ruleGroupCode) {
        StringBuilder sql = new StringBuilder(" update AB_INTELLECT_RULE_GROUP_DTL set rule_group_code = :ruleGroupCode where apply_no = :applyNo");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleGroupCode", ruleGroupCode);
        params.put("applyNo", applyNo);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据申请号删除无用的规则明细信息(删除规则编码为空的数据)
     *
     * @param applyNo 申请单号
     */
    public void delUselessByApplyNo(String applyNo) {
        StringBuilder sql = new StringBuilder("delete from AB_INTELLECT_RULE_GROUP_DTL where apply_no = :applyNo and rule_group_code is null");
        Map<String, Object> params = new HashMap<>(2);
        params.put("applyNo", applyNo);
        this.batchSQLExecute(sql.toString(), params);
    }
}
