package com.bytter.basic.dao;
import com.bytter.basic.entity.SysCountry;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 国家信息数据层
 *
 * @author qinl
 * @date 2019-07-18
 */
@Repository
public class SysCountryDao extends BaseDao<SysCountry, String> {
    /**
     * @return 通用sql查询语句
     */
    private StringBuilder sqlForSelectAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id,t.code,t.name_zh,t.name_en,t.is_system, t.continent ");
        sql.append(" ,t.create_by,t.create_date,t.update_by,t.update_date,t.ISO_TWO as isoTwo,t.ISO_THREE as isoThree ");
        sql.append(" FROM sys_country t");
        sql.append(" WHERE 1 = 1 ");
        return sql;
    }
    /**
     * <p>
     * 获取列表数据
     * </p>
     *
     * @return 列表(Entity)
     */
    public DataTablesOutput selectDataTable(DataTablesInput dataTablesInput) {
        return getDynamicBySQL(dataTablesInput, sqlForSelectAll().toString(), SysCountry.class, "t", false);
    }
    /**
     * <p>
     * 查询所有记录
     * </p>
     *
     * @return 集合(Entity)
     */
    public List<SysCountry> selectList() {
        return listBySQLAliasToBean(sqlForSelectAll().toString(), null, SysCountry.class);
    }
    /**
     * <p>
     * 查询一条记录
     * </p>
     *
     * @param sysCountry [条件集合]
     * @return (Entity)
     */
    public SysCountry selectOne(SysCountry sysCountry) {
        if (sysCountry == null) {
            return null;
        }
        StringBuilder sql = sqlForSelectAll();
        //条件拼接
        Map<String, String> params = new HashMap<>();
        boolean changeFlag = true;
        if (StringUtils.isNotEmpty(sysCountry.getCode())) {
            sql.append(" AND t.code = :code ");
            params.put("code", sysCountry.getCode());
            changeFlag = false;
        }
        if (StringUtils.isNotEmpty(sysCountry.getNameZh())) {
            sql.append(" AND t.name_zh = :name_zh ");
            params.put("name_zh", sysCountry.getNameZh());
            changeFlag = false;
        }
        if (StringUtils.isNotEmpty(sysCountry.getNameEn())) {
            sql.append(" AND t.name_en = :name_en ");
            params.put("name_en", sysCountry.getNameEn());
            changeFlag = false;
        }
        if (changeFlag) {
            return null;
        }
        return this.getBySQLAliasToBean(sql.toString(), params, SysCountry.class);
    }
    /**
     * <p>
     * 根据 <b>国家编号</b> 查询一条记录
     * </p>
     *
     * @param code [国家编号]
     * @return (Entity)
     */
    public SysCountry selectByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        StringBuilder sql = sqlForSelectAll();
        //条件拼接
        HashMap<String, String> params = new HashMap<>();
        sql.append(" AND t.code = :code ");
        params.put("code", code);
        return this.getBySQLAliasToBean(sql.toString(), params, SysCountry.class);
    }
    public List<SysCountry> listByContinent(String continent) {
        StringBuilder sql = sqlForSelectAll();
        //条件拼接
        HashMap<String, String> params = new HashMap<>();
        sql.append(" AND t.continent = :continent ");
        params.put("continent", continent);
        return this.listBySQLAliasToBean(sql.toString(), params, SysCountry.class);
    }
}
