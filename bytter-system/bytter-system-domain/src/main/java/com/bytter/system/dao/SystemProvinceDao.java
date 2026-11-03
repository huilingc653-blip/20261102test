package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.vo.SystemProvinceVo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SystemProvinceDao extends BaseDao<SystemProvince, String> {

    public DataTablesOutput queryByCodeNadName(DataTablesInput input, String sql){

       return getDynamicBySQL(input, sql, SystemProvinceVo.class, null, false, Maps.newHashMap());
    }

    public SystemProvinceVo getByUnion(String id){
        StringBuilder sql = new StringBuilder("select id, province_code as provinceCode, ");
        sql.append(" province_name as provinceName from sys_province where 1 = 1 ");
        sql.append(" and id = :ID");
        Map<String, String > params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, SystemProvinceVo.class);
    }

    public SystemProvince getById(String id){
        StringBuilder sql = new StringBuilder("select id, province_code as provinceCode, ");
        sql.append(" province_name as provinceName from sys_province where 1 = 1 ");
        sql.append(" and id = :ID");
        Map<String, String > params = Maps.newHashMap();
        params.put("ID", id);
        return getBySQLAliasToBean(sql.toString(), params, SystemProvince.class);
    }

    public List<SystemProvince> queryByIds(List<String> ids){
        if (ListUtils.isNullOrEmpty(ids)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from SystemProvince where 1 =1 and " + SqlUtil.getInSql(ids, "id", true);
        Query query = createQuery(hql, map);
        return query.list();
    }

    public List<SystemProvince> getSystemProvince(){
        Map<String, Object> map = new HashMap<>();
        String hql = "from SystemProvince where status = '1' ";
        Query query = createQuery(hql, map);
        return query.list();
    }

    public List<SystemProvince> queryByCodes(List<String> codes){
        if (ListUtils.isNullOrEmpty(codes)) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            String hql = "from SystemProvince where STATUS = '1' and " + SqlUtil.getInSql(codes, "provinceCode", true);
            Query query = createQuery(hql, map);
            return query.list();

    }

    public List<SystemProvince> queryByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        Map params = new HashMap();
        String sql = "select * from sys_province a where a.STATUS = '1' and a.PROVINCE_CODE = :code";
        params.put("code", code);
        return this.listBySQLAliasToBean(sql, params, SystemProvince.class);
    }

    public List<SystemProvince> queryList(List<SystemProvinceVo> areaVos){
        if (ListUtils.isNullOrEmpty(areaVos)) {
            return null;
        }
        List<String> codes = areaVos.stream().map(SystemProvinceVo::getProvinceCode).collect(Collectors.toList());
        List<String> names = areaVos.stream().map(SystemProvinceVo::getProvinceName).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        String hql = "from SystemProvince where STATUS = '1' and " + SqlUtil.getInSql(codes, "provinceCode", true) +" and "+ SqlUtil.getInSql(names, "provinceName", true);

        Query query = createQuery(hql, map);
        return query.list();

    }
}
