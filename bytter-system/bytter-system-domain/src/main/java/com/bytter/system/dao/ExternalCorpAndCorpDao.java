package com.bytter.system.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.ExternalCorpAndCorp;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description: 客商单位关联表
 * @date 2018/6/15 11:06
 */
@Repository
public class ExternalCorpAndCorpDao extends BaseDao<ExternalCorpAndCorp, String> {
    /**
     * 根据客商Id查找对应的单位
     *
     * @param externalId
     * @return
     */
    public List<ExternalCorpAndCorp> getByExternalCorpId(String externalId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select e.externalCorp_id, e.corp_id from sys_externalCorp_corp e where e.externalCorp_id = :externalId ");
        HashMap<String, String> params = new HashMap<>();
        params.put("externalId", externalId);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpAndCorp.class);
    }
    /**
     * 根据客商Id查找对应的单位ID
     *
     * @param externalId
     * @return
     */
    public List<String> getCorpIdsByExternalCorpId(String externalId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select e.corp_id from sys_externalCorp_corp e where e.externalCorp_id = :externalId ");
        HashMap<String, String> params = new HashMap<>();
        params.put("externalId", externalId);
        return this.createSQLQuery(sql.toString(), params).list();
    }

    /**
     * 根据客商ID集合批量查询客商与单位关联关系。
     *
     * @param externalIds 客商ID集合
     * @return 客商与单位关联关系集合
     */
    public List<ExternalCorpAndCorp> listByExternalCorpIds(List<String> externalIds) {
        if (EmptyUtils.isEmptyList(externalIds)) {
            return Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select e.externalCorp_id as externalCorpId, e.corp_id as corpId ");
        sql.append(" from sys_externalCorp_corp e where ");
        sql.append(SqlUtil.getInSqlByParams(externalIds, "e.externalCorp_id", params));
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpAndCorp.class);
    }

    /**
     * 删除指定客商对应的单位关联关系
     *
     * @param externalId
     */
    public void deleteExternalCorpAndCorp(String externalId) {
        String sql = " delete from sys_externalCorp_corp where externalCorp_id = :externalId ";
        Map<String, Object> paramas = new HashMap<>();
        paramas.put("externalId", externalId);
        createSQLQuery(sql.toString(), paramas).executeUpdate();
    }
    /**
     * 根据单位id查询绑定的客商
     *
     * @param corpId
     * @return
     */
    public List<ExternalCorpAndCorp> listExternalCorpByCorpIds(String corpId) {
        String sql = " select e.corp_id from sys_externalCorp_corp e where e.corp_id = :corpId ";
        HashMap<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.listBySQLAliasToBean(sql.toString(), params, ExternalCorpAndCorp.class);
    }
    /**
     * 根据单位id查询绑定的客商Id
     *
     * @param corpId
     * @return
     */
    public List<String> listExternalCorpIdsByCorpIds(String corpId) {
        String sql = " select e.externalCorp_id from sys_externalCorp_corp e where e.corp_id = :corpId ";
        HashMap<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.createSQLQuery(sql.toString(), params).list();
    }


    /**
     * 根据客商id获取所有关联单位
     * @param externalId
     * @return
     */
    public List<String> getCorpNameByExternalId(String externalId){
        String sql = " select distinct corp.name from sys_externalCorp_corp e ";
        sql+=" left join sys_corp corp on corp.id=e.corp_Id where e.externalCorp_id = :externalId and corp.name is not null";
        HashMap<String, String> params = new HashMap<>();
        params.put("externalId",externalId);
        return this.createSQLQuery(sql.toString(),params).list();
    }
}
