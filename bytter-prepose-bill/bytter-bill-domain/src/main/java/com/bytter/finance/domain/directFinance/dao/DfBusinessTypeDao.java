package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfBusinessType;
import com.bytter.finance.domain.directFinance.vo.DfBusinessTypeVO;
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
public class DfBusinessTypeDao extends BaseDao<DfBusinessType, String> {
    /**
     * 列表展示
     *
     * @param input
     * @return
     */
    public DataTablesOutput listBusinessType(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" business_code businessCode, ");
        sql.append(" business_name businessName,");
        sql.append(" is_valid isValid");
        sql.append(" from df_business_type t");
        return getDynamicBySQL(input, sql.toString(), DfBusinessTypeVO.class, "t", true, null);
    }
    /**
     * 计数
     * 
     * @param params
     * @return
     */
    public List<DfBusinessTypeVO> getCountByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" business_code businessCode ");
        sql.append(" from df_business_type t");
        sql.append(" where 1 =1 ");
        sql.append(" and t.business_code = :businessCode");
        return listBySQLAliasToBean(sql.toString(), params, DfBusinessTypeVO.class);
    }
    /**
     * 查询有效数据
     *
     * @return
     */
    public List<DfBusinessTypeVO> listBusinessTypeByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id, ");
        sql.append(" business_code businessCode, ");
        sql.append(" business_name businessName,");
        sql.append(" is_valid isValid");
        sql.append(" from df_business_type t");
        sql.append(" where 1 = 1");
        sql.append(" and t.is_valid = '" + CmsConstant.IS_TRUE + "'");
        return listBySQLAliasToBean(sql.toString(), params, DfBusinessTypeVO.class);
    }
    /**
     * 根据费用id查询中间费用表数量
     * 
     * @param params
     * @return
     */
    public String getCountBybusiTypeId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) as busis from DF_DIRECT_FINANCE_INFO  where DF_BUSINESS_TYPE_ID = :id");
        return createSQLQuery(sql.toString(), params).uniqueResult().toString();
    }
}
