package com.bytter.system.dao;
import com.bytter.system.entity.SupplierType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class SupplierTypeDao extends BaseDao<SupplierType, String> {
    public DataTablesOutput datatables(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select st.id as id, st.supplier_code as code, st.supplier_name as name, st.rmk as rmk, ");
        sql.append(" st.valid_sign as validSign from SYS_SUPPLIER_TYPE st where 1 = 1 and st.status <> -2 ");
        return dealDynamicBySQL(input, sql.toString(), SupplierType.class, "", false, null);
    }
    /**
     * 获取有效的供应商类别
     *
     * @return
     */
    public List<SupplierType> getValidSign() {
        StringBuilder hql = new StringBuilder();
        hql.append(" from SupplierType where validSign = 1 and status <> -2 ");
        return listByHQL(hql.toString());
    }
    /**
     * 根据供应商编号查询实体,用于检查是否存在相同的供应商编号
     *
     * @param code
     * @return
     */
    public SupplierType getEntityByCode(String code) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from sys_supplier_type st where st.valid_sign = 1 ");
        sql.append(" and st.status <> -2 and st.supplier_code = :code ");
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        return getBySQLAliasToBean(sql.toString(), params, SupplierType.class);
    }
    /**
     * 根据供应商名称查询实体,用于检查是否存在相同的供应商名称
     *
     * @param name
     * @return
     */
    public SupplierType getEntityByName(String name) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from SupplierType where validSign = 1 and status <> -2 and name = :name");
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return getUniqueCache(hql.toString(), params);
    }
}
