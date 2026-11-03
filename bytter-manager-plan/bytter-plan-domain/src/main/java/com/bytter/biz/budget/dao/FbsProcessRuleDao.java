package com.bytter.biz.budget.dao;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsProcessRule;
import com.bytter.biz.budget.vo.FbsProcessRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 资金计划执行规则表
 * @date 2020-08-31 14:51:48
 */
@Repository
public class FbsProcessRuleDao extends BaseDao<FbsProcessRule, String> {
    /**
     * 外汇产品表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.process_Rule as processRule,f.sort as sort,f.process_Ratio as processRatio,f.valid_Sign as validSign,");
        sql.append(" f.create_By as createBy, f.create_Date as createDate, f.update_By as updateBy, f.update_Date as updateDate");
        sql.append(" from fbs_process_rule f ");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 根据执行规则取规则信息
     *
     * @param processRule
     * @param id
     * @return
     */
    public List<FbsProcessRuleVO> isExistProcessRule(String processRule, String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.process_Rule=:processRule");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("processRule", processRule);
        if (StringUtils.isNotBlank(id)) {
            sql.append("  and f.id<>:id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, FbsProcessRuleVO.class);
    }
    /**
     * 根据优先级取规则信息
     *
     * @param sort
     * @param id
     * @return
     */
    public List<FbsProcessRuleVO> isExistSort(Integer sort, String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.sort=:sort");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("sort", sort);
        if (StringUtils.isNotBlank(id)) {
            sql.append("  and f.id<>:id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, FbsProcessRuleVO.class);
    }
    /**
     * 获取有效的执行规则
     *
     * @return
     */
    public List<FbsProcessRuleVO> listValidProcessRule() {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.valid_Sign=:validSign");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", CommonConstant.YES);
        sql.append(" order by sort");
        return listBySQLAliasToBean(sql.toString(), params, FbsProcessRuleVO.class);
    }
}
