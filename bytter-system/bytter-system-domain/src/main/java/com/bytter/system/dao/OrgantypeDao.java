package com.bytter.system.dao;
import com.bytter.system.entity.Organtype;
import com.bytter.system.api.constant.SysOrgantypeConstant;
import com.bytter.system.vo.OrgantypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Dao
 * @Description: 组织架构表数据库操作
 * @date 2020-05-14 16:01:40
 */
@Repository
public class OrgantypeDao extends BaseDao<Organtype, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select o.id as id,o.orgt_name as orgtName,o.is_init as isInit,o.orgt_version as orgtVersion,o.release_date as releaseDate,");
        sql.append("o.effective_date as effectiveDate,o.valid_sign as validSign,o.rmk as rmk,o.create_time as createTime,o.IS_CHANG_CORP,");
        sql.append("o.create_by as createBy ");
        sql.append(" from SYS_ORGANTYPE o");
        return sql;
    }
    /**
     * 组织架构数据列表加载
     *
     * @param dataTablesInput
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1  and o.VALID_SIGN in (:validSign) ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[]{SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_0, SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_1});
        return getDynamicBySQL(dataTablesInput, sql.toString(), OrgantypeVO.class, "", false, params);
    }
    /**
     * 根据参数查询组织架构数据
     *
     * @param params 查询参数
     * @return
     */
    public List<OrgantypeVO> getOrgantypeByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1  ");
        if (params.containsKey("validSign")) {
            sql.append(" and o.VALID_SIGN in (:validSign) ");
        }
        if (params.containsKey("orgtName")) {
            sql.append(" and o.orgt_Name=:orgtName ");
        }
        if (params.containsKey("id")) {
            sql.append(" and o.id=:id ");
        }
        List<OrgantypeVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, OrgantypeVO.class);
        return listInfo;
    }
    /**
     * 根据参数查询组织架构数据
     *
     * @param params 查询参数
     * @return
     */
    public List<OrgantypeVO> getOrgantypeIsRelease(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE o.id in (select sor.orgt_id from  SYS_ORGANTYPE_RELEASE  sor  group by sor.orgt_id)  and  o.valid_sign=:validSign");
        if (params.containsKey("id")) {
            sql.append(" and o.id=:id ");
        }
        params.put("validSign", SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_1);
        List<OrgantypeVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, OrgantypeVO.class);
        return listInfo;
    }
    /**
     * 更新组织架构
     *
     * @param params
     * @return
     */
    public int updateOrgantype(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update Organtype "
                + " set updateTime = :updateTime ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("isChangCorp")) {
            hql.append(",isChangCorp = :isChangCorp ");
        }
        hql.append(" where id = :id ");
        return this.batchExecute(hql.toString(), params);
    }
}
