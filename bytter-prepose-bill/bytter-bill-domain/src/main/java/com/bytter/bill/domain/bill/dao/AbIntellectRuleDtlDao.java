package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleDtl;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 智能选票规则明细处理dao
 *
 * @author ouyj
 */
@Repository
public class AbIntellectRuleDtlDao extends BaseDao<AbIntellectRuleDtl, String> {
    /**
     * 获取选票规则明细公共列sql
     *
     * @return 选票规则明细公共列sql
     */
    public StringBuilder selectCommonColumnSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select r.id as id, r.RULE_CODE as ruleCode, r.ELEMENT_ID as elementId, r.ELEMENT_NAME as elementName, ");
        sql.append(" r.PARENT_ID as parentId, r.PARENT_NAME as parentName, r.SCREEN_TYPE as screenType, ");
        sql.append(" r.SORT_ORDER as sortOrder, r.SCOPE_MODE as scopeMode, r.VALUE as value, r.START_VALUE as startValue, ");
        sql.append(" r.END_VALUE as endValue, r.START_RELATION as startRelation, r.END_RELATION as endRelation, ");
        sql.append(" r.RELATION as relation, r.SERIALNO as serialNo, r.RMK as rmk, r.apply_no as applyNo, r.VALUE_IS_ELEMENT as valueIsElement ");
        return sql;
    }
    /**
     * <b>功能：</b>获取有效的选票规则明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleDtlVO> listAbIntellectRuleDtl() {
        StringBuilder hql = new StringBuilder();
        hql.append(" FROM AbIntellectRuleDtl WHERE validSign=:validSign  ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("validSign", "1");
        hql.append(" order by ruleName ");
        List<AbIntellectRuleDtlVO> ruleGroupList = listByHQLAliasToBean(hql.toString(), params, AbIntellectRuleDtlVO.class);
        return ruleGroupList;
    }
    /**
     * 查询选票规则明细列表数据
     *
     * @param input 前端传参
     * @return 选票规则明细列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_DTL r ");
        sql.append(" where 1 = 1 ");
        return getDynamicBySQL(input, sql.toString(), AbIntellectRuleDtlVO.class, "r", false, null);
    }
    /**
     * 根据id查询选票规则明细数据
     *
     * @param id 主键id
     * @return 选票规则明细数据
     */
    public AbIntellectRuleDtlVO getAbIntellectRuleDtlVOById(String id) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" , e.data_type as dataType, e.source_type as sourceType, e.dict_group as dictGroup ");
        sql.append(" from AB_INTELLECT_RULE_DTL r ");
        sql.append(" left join AB_INTELLECT_ELEMENT e on r.ELEMENT_ID = e.id ");
        sql.append(" where r.id = :id");
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectRuleDtlVO.class);
    }
    /**
     * 根据申请单号查询对应的选票规则明细列表
     *
     * @param applyNo 申请单号
     * @return 选票规则明细列表
     */
    public List<AbIntellectRuleDtlVO> listRuleDtlByApplyNo(String applyNo) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_RULE_DTL r ");
        sql.append(" where apply_no = :applyNo order by SERIALNO ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("applyNo", applyNo);
        return listBySQLAliasToBean(sql.toString(), params, AbIntellectRuleDtlVO.class);
    }
    /**
     * 根据申请单号集合修改规则编码
     *
     * @param applyNo  申请单号
     * @param ruleCode 规则编码
     */
    public void updateRuleCodeByApplyNo(String applyNo, String ruleCode) {
        StringBuilder sql = new StringBuilder(" update AB_INTELLECT_RULE_DTL set rule_code = :ruleCode where apply_no = :applyNo");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleCode", ruleCode);
        params.put("applyNo", applyNo);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据申请号删除无用的规则明细信息(删除规则编码为空的数据)
     *
     * @param applyNo 申请单号
     */
    public void delUselessByApplyNo(String applyNo) {
        StringBuilder sql = new StringBuilder("delete from AB_INTELLECT_RULE_DTL where apply_no = :applyNo and rule_code is null");
        Map<String, Object> params = new HashMap<>(2);
        params.put("applyNo", applyNo);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据规则编码删除规则明细数据
     *
     * @param ruleCode 规则编码
     */
    public void delDtlByRuleCode(String ruleCode) {
        StringBuilder sql = new StringBuilder("delete from AB_INTELLECT_RULE_DTL where rule_code = :ruleCode");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ruleCode", ruleCode);
        this.batchSQLExecute(sql.toString(), params);
    }
}
