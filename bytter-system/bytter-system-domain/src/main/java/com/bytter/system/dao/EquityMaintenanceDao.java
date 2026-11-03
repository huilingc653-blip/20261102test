package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.EquityMaintenance;
import com.bytter.system.vo.EquityMaintenanceVo;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EquityMaintenanceDao extends BaseDao<EquityMaintenance, String> {

    public DataTablesOutput queryByCodeAndName(DataTablesInput input, String sql) {

        return getDynamicBySQL(input, sql, EquityMaintenance.class, null, false, Maps.newHashMap());
    }

    public EquityMaintenanceVo getByUnion(String id) {
        StringBuilder sql = new StringBuilder("select id, equity_code as equityCode, ");
        sql.append(" equity_name as equityName from sys_equity_maintenance where 1 = 1 and status = '1'");
        sql.append(" and id = :ID");
        Map<String, String> params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, EquityMaintenanceVo.class);
    }

    public EquityMaintenance getById(String id) {
        StringBuilder sql = new StringBuilder("select id, equity_code as equityCode, ");
        sql.append(" equity_name as equityName from sys_equity_maintenance where 1 = 1 ");
        sql.append(" and id = :ID and status = '1' ");
        Map<String, String> params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, EquityMaintenance.class);
    }

    public List<EquityMaintenance> queryByIds(List<String> ids) {

        if (ListUtils.isNullOrEmpty(ids)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from EquityMaintenance where " + SqlUtil.getInSql(ids, "id", true);
        Query query = createQuery(hql, map);
        return query.list();

    }

    public List<EquityMaintenance> queryByCodes(List<String> codes) {

        if (ListUtils.isNullOrEmpty(codes)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from EquityMaintenance where status = '1' and " + SqlUtil.getInSql(codes, "equityCode", true);
        Query query = createQuery(hql, map);
        return query.list();

    }

    public List<EquityMaintenance> json(){
        Map<String, Object> map = new HashMap<>();
        String hql = "from EquityMaintenance where status = '1' ";
        Query query = createQuery(hql, map);
        return query.list();
    }
}
