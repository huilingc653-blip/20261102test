package com.bytter.system.dao;
import com.bytter.system.entity.OrgantypeCorp;
import com.bytter.system.vo.OrgantypeCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Dao
 * @Description: 组织架构单位层级关联表数据库操作
 * @date 2020-05-14 16:01:41
 */
@Repository
public class OrgantypeCorpDao extends BaseDao<OrgantypeCorp, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select oc.id as id,oc.corp_id as corpId,sc.name as corpName,oc.parent_id as parentId,oc.orgt_id as orgtId,");
        sql.append(" sc.sort as sort,sc.code as corpCode,sc.status as status,sc.type as type,sc.unit_attribute as unitAttribute,sc.unit_cm as unitCm, ");
        sql.append(" cm.cell_name as unitCmName");
        sql.append(" from SYS_ORGANTYPE_CORP oc ");
        sql.append(" inner join sys_corp sc on oc.corp_id=sc.id ");
        sql.append(" left join SYS_CELL_MANAGE cm on cm.id=sc.unit_Cm");
        return sql;
    }
    /**
     * 组织架构单位层级关联表 列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" from sys_organtype_corp where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), OrgantypeCorpVO.class, "", false, params);
    }
    /**
     * 根据参数查询组织架构单位层级关联信息
     *
     * @param params 查询参数
     * @return
     */
    public List<OrgantypeCorpVO> getOrgtCorpByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1  ");
        if (params.containsKey("orgtId")) {
            sql.append(" and oc.orgt_id=:orgtId ");
        }
        if (params.containsKey("id")) {
            sql.append(" and oc.id in (:id) ");
        }
        if (params.containsKey("corpName")) {
            sql.append(" and sc.name like :corpName ");
        }
        if (params.containsKey("parentId")) {
            sql.append(" and oc.parent_id=:parentId ");
        }
        sql.append(" order by sc.sort,sc.code");
        List<OrgantypeCorpVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, OrgantypeCorpVO.class);
        return listInfo;
    }
    /**
     * 根据组织架构Id获取所有父类Id
     *
     * @return 单位id
     * @author
     */
    public List<String> getAllOrgtCorpParentIds(String orgtId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.PARENT_ID as parentId from SYS_ORGANTYPE_CORP c  ");
        sql.append(" where c.orgt_Id=:orgtId ");
        params.put("orgtId", orgtId);
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 获取所有组织架构单位Id
     *
     * @return 单位corpIds
     * @author
     */
    public List<String> listOrgtCorpIdsByOrgtId(String orgtId, String corpAuthSql) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.corp_Id as corpId from SYS_ORGANTYPE_CORP c");
        sql.append(" where c.orgt_Id=:orgtId ");
        params.put("orgtId", orgtId);
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql);
        }
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 根据单位id和组织架构获取单位层级信息
     *
     * @param corpId 单位id
     * @param orgtId 组织架构id
     * @return 单位层级信息
     */
    public OrgantypeCorp getOrgtCorpByCorpIdAndRgatId(String corpId, String orgtId) {
        if (StringUtils.isEmpty(corpId) && StringUtils.isEmpty(orgtId)) {
            return null;
        }
        StringBuffer hql = new StringBuffer("from OrgantypeCorp where corpId=:corpId and orgtId=:orgtId  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("orgtId", orgtId);
        hql.append(" order by createTime ");
        List<OrgantypeCorp> list = this.listByHQL(hql.toString(), params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
