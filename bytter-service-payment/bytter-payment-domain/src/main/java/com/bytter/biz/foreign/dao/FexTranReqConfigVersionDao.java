package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranReqConfigVersion;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class FexTranReqConfigVersionDao extends BaseDao<FexTranReqConfigVersion, String> {

    public String getMaxVersionNo(String templateId) {
        String sql = "select max(version_no) from fex_tran_req_config_version  where template_id = ?";
        Object versionNoOb = createSQLQuery(sql, templateId).uniqueResult();
        String versionNo = null;
        if (null != versionNoOb) {
            versionNo = versionNoOb.toString();
        }
        return versionNo;
    }

    public List<String> listTmplateHisVersionNos(String templateId) {
        String sql = " select version_no from fex_tran_req_config_version where template_id = :templateId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }

    public void delByTemplateId(String templateId, String versionNo) {
        String sql = " delete from fex_tran_req_config_version where template_id = :templateId and version_no = :versionNo";
        HashMap<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        params.put("versionNo", versionNo);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }


    public FexTranReqConfigVersion getByTemplateId(String templateId, String versionNo) {
        String sql = " select * from fex_tran_req_config_version where template_id = :templateId and version_no = :versionNo";
        HashMap<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        params.put("versionNo", versionNo);
        return getBySQLAliasToBean(sql, params, FexTranReqConfigVersion.class);
    }
}
