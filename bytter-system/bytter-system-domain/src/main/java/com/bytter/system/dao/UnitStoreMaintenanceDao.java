package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.UnitStoreMaintenance;
import com.bytter.system.vo.UnitStoreMaintenanceVo;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UnitStoreMaintenanceDao extends BaseDao<UnitStoreMaintenance, String> {

    public DataTablesOutput queryByCodeNadName(DataTablesInput input, String sql) {

        return getDynamicBySQL(input, sql, UnitStoreMaintenance.class, null, false, Maps.newHashMap());
    }

    public UnitStoreMaintenanceVo getByUnion(String id) {
        StringBuilder sql = new StringBuilder("select id as id, store_code as storeCode, ");
        sql.append(" store_name as storeName, corp_id as corpId, corp_code as corpCode, corp_name as corpName   ");
        sql.append(" from sys_unit_store_maintenance where 1 = 1  ");
        sql.append(" and id = :ID");
        Map<String, String> params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, UnitStoreMaintenanceVo.class);
    }

    public UnitStoreMaintenance getById(String id) {
        StringBuilder sql = new StringBuilder("select id as id, store_code as storeCode, ");
        sql.append(" store_name as storName, corp_id as corpId, corp_code as corpCode, corp_name as corpName   ");
        sql.append(" from sys_unit_store_maintenance where 1 = 1  ");
        sql.append(" and id = :ID");
        Map<String, String> params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, UnitStoreMaintenance.class);
    }

    public List<UnitStoreMaintenance> queryByIds(List<String> ids) {

        if (ListUtils.isNullOrEmpty(ids)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from UnitStoreMaintenance where " + SqlUtil.getInSql(ids, "id", true);
        Query query = createQuery(hql, map);
        return query.list();

    }

    public UnitStoreMaintenance queryByStroeCodOrName(String storeCode, String storeName){
        StringBuilder sql = new StringBuilder("select id as id, store_code as storeCode, ");
        sql.append(" store_name as storName, corp_id as corpId, corp_code as corpCode, corp_name as corpName   ");
        sql.append(" from sys_unit_store_maintenance where 1 = 1 and STATUS ='1' ");
        Map<String, String> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(storeCode)){
            sql.append(" and store_code = :storeCode");
            params.put("storeCode", storeCode);
        }
        if (StringUtils.isNotBlank(storeName)){
            sql.append(" and store_name = :storeName");
            params.put("storeName", storeName);
        }
        return getBySQLAliasToBean(sql.toString(), params, UnitStoreMaintenance.class);
    }

    public List<UnitStoreMaintenance> json(){
        Map<String, Object> map = new HashMap<>();
        String hql = "from UnitStoreMaintenance where status = '1' ";
        Query query = createQuery(hql, map);
        return query.list();

    }

    public List<UnitStoreMaintenance> queryByCodes(List<String> codes) {

        if (ListUtils.isNullOrEmpty(codes)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from UnitStoreMaintenance where  status = '1' and  " + SqlUtil.getInSql(codes, "storeCode", true);
        Query query = createQuery(hql, map);
        return query.list();

    }
}
