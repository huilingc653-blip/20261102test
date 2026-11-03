package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfCostType;
import com.bytter.finance.domain.directFinance.vo.DfCostTypeVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:39
 * @Version 1.0
 */
@Repository
public class DfCostTypeDao extends BaseDao<DfCostType, String> {
    /**
     * 列表展示
     *
     * @param input
     * @return
     */
    public DataTablesOutput listCostType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" cost_code costCode, ");
        sql.append(" cost_name costName,");
        sql.append(" is_valid isValid,");
        sql.append(" is_init isInit");
        sql.append(" from df_cost_type t");
        return getDynamicBySQL(input, sql.toString(), DfCostTypeVO.class, "t", true, null);
    }
    /**
     * 计数
     * 
     * @param params
     * @return
     */
    public List<DfCostTypeVO> getCountByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" cost_code costCode ");
        sql.append(" from df_cost_type t");
        sql.append(" where 1 =1 ");
        sql.append(" and t.cost_code = :costCode");
        return listBySQLAliasToBean(sql.toString(), params, DfCostTypeVO.class);
    }
    /**
     * 返回有效数据
     * 
     * @param params
     * @return
     */
    public List<DfCostTypeVO> getValidDfCostType(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" cost_code costCode, ");
        sql.append(" cost_name costName,");
        sql.append(" is_valid isValid");
        sql.append(" from df_cost_type t");
        sql.append(" where 1 = 1 ");
        sql.append(" and t.is_valid = '" + CmsConstant.IS_TRUE + "' ");
        return listBySQLAliasToBean(sql.toString(), params, DfCostTypeVO.class);
    }
    /**
     * 根据费用id查询中间费用表数量
     * 
     * @param params
     * @return
     */
    public String getCountCostTypeById(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) as costTypes from DF_INTERMEDIATE_COST  where cost_type_id = :id");
        return createSQLQuery(sql.toString(), params).uniqueResult().toString();
    }
}
