package com.bytter.system.dao;
import com.bytter.system.entity.OrgantypeRelease;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhengg
 * @version 1.0
 * @Title: Dao
 * @Description: 组织架构发版表数据库操作
 * @date 2020-05-14 16:01:42
 */
@Repository
public class OrgantypeReleaseDao extends BaseDao<OrgantypeRelease, String> {

    private String getQuerySql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select sor.id as id, sor.orgt_id as orgtId, sor.orgt_name as orgtName, sor.orgt_version as orgtVersion, sor.release_date as releaseDate,");
        sql.append("sor.effective_date as effectiveDate, sor.rmk as rmk from sys_organtype_release sor");
        return sql.toString();
    }

    public List<OrgantypeRelease> getByOrgId(Map<String, String> params) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1  ");
        if (params.containsKey("orgtId")) {
            sql.append(" and sor.ORGT_ID=:orgtId ");
        }
        sql.append(" order by sor.orgt_version desc ");
        return this.listBySQLAliasToBean(sql.toString(), params, OrgantypeRelease.class);
    }
}
