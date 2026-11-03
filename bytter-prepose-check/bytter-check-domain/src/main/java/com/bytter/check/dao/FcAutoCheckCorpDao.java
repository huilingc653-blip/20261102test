package com.bytter.check.dao;
import com.bytter.check.entity.FcAutoCheckCorp;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcAutoCheckCorpDao extends BaseDao<FcAutoCheckCorp, String> {
    /**
     * 功能介绍 :根据对账类型获和单位取对账策略与单位的连接
     */
    public FcAutoCheckCorp getCheckCorp(String checkType, String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select *");
        sql.append(" from  FC_AUTO_CHECK_CORP facc ");
        sql.append(" where 1=1");
        if (!EmptyUtils.isEmpty(checkType)) {
            sql.append(" and facc.CHECK_TYPE=:checkType");
            params.put("checkType", checkType);
        }
        if (!EmptyUtils.isEmpty(corpId)) {
            sql.append(" and  facc.CORP_ID=:corpId");
            params.put("corpId", corpId);
        }
        return getBySQLAliasToBean(sql.toString(), params, FcAutoCheckCorp.class);
    }
    public void deleteByCode(String autoCheckCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("delete from FC_AUTO_CHECK_CORP where AUTO_CHECK_CODE=:autoCheckCode");
        params.put("autoCheckCode", autoCheckCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public void updateByCode(String autoCheckCode, String oldCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("update FC_AUTO_CHECK_CORP set AUTO_CHECK_CODE=:autoCheckCode where AUTO_CHECK_CODE=:oldCode");
        params.put("autoCheckCode", autoCheckCode);
        params.put("oldCode", oldCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public List<FcAutoCheckCorp> getByCode(String autoCheckCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select *");
        sql.append(" from  FC_AUTO_CHECK_CORP facc ");
        sql.append(" where 1=1");
        sql.append(" and facc.AUTO_CHECK_CODE=:autoCheckCode");
        params.put("autoCheckCode", autoCheckCode);
        return listBySQLAliasToBean(sql.toString(), params, FcAutoCheckCorp.class);
    }
}
