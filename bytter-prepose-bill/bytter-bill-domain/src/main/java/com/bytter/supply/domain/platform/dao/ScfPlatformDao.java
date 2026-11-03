package com.bytter.supply.domain.platform.dao;

import com.bytter.supply.domain.platform.entity.ScfPlatform;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName ScfPlatformManagerDao
 * @Description 供应链金融平台DAO
 */
@Repository
public class ScfPlatformDao extends BaseDao<ScfPlatform, String> {

    /**
     * 平台列表数据查询
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.platform_code as platformCode, p.platform_name as platformName, p.valid as valid, p.rmk as rmk ");
        sql.append(" FROM scf_platform p WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPlatformVo.class, "p", false);
    }

    /**
     * 根据平台名称查询
     */
    public ScfPlatformVo getPlatformByName(String name, String excludeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.platform_code as platformCode, p.platform_name as platformName, p.valid as valid, p.rmk as rmk ");
        sql.append(" FROM scf_platform p WHERE p.platform_name = :platformName ");
        if (excludeId != null && !excludeId.isEmpty()) {
            sql.append(" AND p.id <> :excludeId ");
        }
        java.util.HashMap<String, Object> params = new java.util.HashMap<>();
        params.put("platformName", name);
        if (excludeId != null && !excludeId.isEmpty()) {
            params.put("excludeId", excludeId);
        }
        List<ScfPlatformVo> list = listBySQLAliasToBean(sql.toString(), params, ScfPlatformVo.class);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 获取最大流水号
     */
    public String getMaxCode() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT MAX(p.platform_code) FROM scf_platform p WHERE p.platform_code LIKE 'SCF%' ");
        List<Object> list = createSQLQuery(sql.toString()).list();
        if (list != null && !list.isEmpty() && list.get(0) != null) {
            return (String) list.get(0);
        }
        return null;
    }

    public List<ScfPlatformVo> getPlatformJson() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.platform_code as platformCode, p.platform_name as platformName, p.valid as valid, p.rmk as rmk ");
        sql.append(" FROM scf_platform p WHERE 1=1 and p.valid = '1' ");
        return listBySQLAliasToBean(sql.toString(), null, ScfPlatformVo.class);
    }

    /**
     * 根据ID查询平台
     */
    public ScfPlatformVo getPlatformById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.platform_code as platformCode, p.platform_name as platformName, p.valid as valid, p.rmk as rmk, ");
        sql.append(" p.create_by as createBy, p.create_date as createDate, ");
        sql.append(" p.update_by as updateBy, p.update_date as updateDate ");
        sql.append(" FROM scf_platform p WHERE p.id = :id ");
        java.util.HashMap<String, Object> params = new java.util.HashMap<>();
        params.put("id", id);
        List<ScfPlatformVo> list = listBySQLAliasToBean(sql.toString(), params, ScfPlatformVo.class);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
}
