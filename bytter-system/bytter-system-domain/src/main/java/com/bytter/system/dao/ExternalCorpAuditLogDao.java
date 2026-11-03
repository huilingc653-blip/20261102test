package com.bytter.system.dao;
import com.bytter.system.entity.ExternalCorpAuditLog;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author lifh
 * @description 客商资料管理操作日志
 * @createDate 2019/3/11
 */
@Repository
public class ExternalCorpAuditLogDao extends BaseDao<ExternalCorpAuditLog, String> implements Serializable {
    /**
     * 根据客商id查询日志
     *
     * @param externalCorpId
     * @return
     */
    public List<ExternalCorpAuditLog> listAuditLogByExternalCorpId(String externalCorpId) {
        String hql = "from ExternalCorpAuditLog log where log.externalCorpId=?0 order by createTime desc";
        return createQuery(hql, externalCorpId).list();
    }
    /**
     * 根据客商id查找审核日志
     *
     * @param id
     * @return
     */
    public List<ExternalCorpAuditLog> getLogListByCorpId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ID as id               ,");
        sql.append(" AUDIT_MESSAGE     as auditMessage   ,");
        sql.append(" AUDIT_TYPE        as auditType      ,");
        sql.append(" CREATE_BY         as createBy       ,");
        sql.append(" CREATE_LOGIN_NAME as createLoginName,");
        sql.append(" CREATE_TIME       as createTime     ,");
        sql.append(" CREATE_USERNAME   as createUsername ,");
        sql.append(" EXTERNAL_CORP_ID  as  externalCorpId ");
        sql.append(" from SYS_EXTERNAL_CORP_AUDIT_LOG ");
        sql.append(" where external_Corp_Id = :externalCorpId order by create_Time desc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("externalCorpId", id);
        List<ExternalCorpAuditLog> list = listBySQLAliasToBean(sql.toString(), params, ExternalCorpAuditLog.class);
        if (null != list && list.size() > 0) {
            return list;
        }
        return new ArrayList<>();
    }
    public void deleteByCorpId(String id) {
        createSQLQuery("delete from SYS_EXTERNAL_CORP_AUDIT_LOG where external_Corp_Id=?", id).executeUpdate();
    }
}
