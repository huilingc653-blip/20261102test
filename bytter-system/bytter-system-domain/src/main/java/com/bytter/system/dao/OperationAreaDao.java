package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.vo.OperationAreaVo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OperationAreaDao extends BaseDao<OperationArea, String> {

    public DataTablesOutput queryByCodeNadName(DataTablesInput input, String sql){

       return getDynamicBySQL(input, sql, OperationAreaVo.class, null, false, Maps.newHashMap());
    }

    public OperationAreaVo getByUnion(String id){
        StringBuilder sql = new StringBuilder("select id, operation_area_code as operationAreaCode, ");
        sql.append(" operation_area_name as operationAreaName,province_id provinceId from sys_operation_area where 1 = 1 ");
        sql.append(" and id = :ID");
        Map<String, String > params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, OperationAreaVo.class);
    }

    public OperationArea getById(String id){
        StringBuilder sql = new StringBuilder("select id, operation_area_code as operationAreaCode, ");
        sql.append(" operation_area_name as operationAreaName from sys_operation_area where 1 = 1 ");
        sql.append(" and id = :ID");
        Map<String, String > params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, OperationArea.class);
    }

    public List<OperationArea> queryByIds(List<String> ids){
        if (ListUtils.isNullOrEmpty(ids)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from OperationArea where 1 =1 and " + SqlUtil.getInSql(ids, "id", true);
        Query query = createQuery(hql, map);
        return query.list();
    }

    public List<OperationArea> getOperationArea(){
        Map<String, Object> map = new HashMap<>();
        String hql = "from OperationArea where status = '1' ";
        Query query = createQuery(hql, map);
        return query.list();
    }

    public List<OperationArea> queryByCodes(List<String> codes){
        if (ListUtils.isNullOrEmpty(codes)) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            String hql = "from OperationArea where STATUS = '1' and " + SqlUtil.getInSql(codes, "operationAreaCode", true);
            Query query = createQuery(hql, map);
            return query.list();

    }

    public List<OperationArea> queryByProvinceId(String provinces){
        if (StringUtils.isBlank(provinces)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from OperationArea where provinceId like '%" + provinces + "%'";
        Query query = createQuery(hql, map);
        return query.list();

    }

    public List<OperationArea> queryByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        Map params = new HashMap();
        String sql = "select * from SYS_OPERATION_AREA a where a.STATUS = '1'  and a.OPERATION_AREA_CODE = :code";
        params.put("code", code);
        return this.listBySQLAliasToBean(sql, params, OperationArea.class);
    }

    public List<OperationArea> queryList(List<OperationAreaVo> areaVos){
        if (ListUtils.isNullOrEmpty(areaVos)) {
            return null;
        }
        List<String> codes = areaVos.stream().map(OperationAreaVo::getOperationAreaCode).collect(Collectors.toList());
        List<String> names = areaVos.stream().map(OperationAreaVo::getOperationAreaName).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        String hql = "from OperationArea where STATUS = '1' and " + SqlUtil.getInSql(codes, "operationAreaCode", true) +" and "+ SqlUtil.getInSql(names, "operationAreaName", true);

        Query query = createQuery(hql, map);
        return query.list();

    }
}
