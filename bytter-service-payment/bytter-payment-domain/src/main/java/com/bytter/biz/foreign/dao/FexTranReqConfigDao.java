package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranReqConfig;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FexTranReqConfigDao extends BaseDao<FexTranReqConfig, String> {

    private StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as id, c.template_id as templateId, c.is_sys as isSys, c.data_type as dataType,");
        sql.append("c.field_id as fieldId, c.field_code as fieldCode, c.field_name as fieldName, c.field_sort as fieldSort,");
        sql.append("c.is_use as isUse, c.is_visible as isVisible, c.is_required as isRequired, c.data_class as dataClass,");
        sql.append("c.is_length as isLength, c.is_limited_dict as isLimitedDict, c.field_length as fieldLength,");
        sql.append("c.field_limit_val as fieldLimitVal, c.field_def_val as fieldDefVal, c.field_dict_group as fieldDictGroup, c.field_limited_dict as fieldLimitedDict,");
        sql.append("c.status as status, c.is_exc as isExc, c.field_area as fieldArea, c.field_parent_code as fieldParentCode,v.version_no as versionNo,");
        sql.append("c.CREATE_BY as createBy, c.CREATE_DATE as createDate,c.UPDATE_BY as updateBy, c.UPDATE_DATE as updateDate ");
        sql.append(" from fex_tran_req_config_version v");
        sql.append(" left join fex_tran_req_config c on c.template_config_version_id = v.id");
        sql.append(" where 1 = 1");
        return sql;
    }


    public List<FexTranReqConfigVo> getTemplateConfig(String templateId, String versionNo) {
        StringBuilder sql = getCommonSql();
        Map<String, Object> params = new HashMap<>();
        sql.append(" and v.template_id = :templateId");
        params.put("templateId", templateId);

        if (StringUtils.isNotEmpty(versionNo)){
            sql.append(" and v.version_no = :versionNo");
            params.put("versionNo", versionNo);
        }
        sql.append(" order by c.field_sort");
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqConfigVo.class);
    }

    public void delByTemplateId(String templateId, String versionNo) {
        String sql = " delete from fex_tran_req_config where template_id = :templateId and version_no = :versionNo";
        HashMap<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        params.put("versionNo", versionNo);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }


    public List<FexTranReqConfigVo> listByFieldId(String fieldId) {
        StringBuilder sql = getCommonSql();
        Map<String, Object> params = new HashMap<>();
        sql.append(" and c.field_id = :fieldId");
        params.put("fieldId", fieldId);
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqConfigVo.class);
    }

    public void updatefieldName(String fieldId, String fieldName){
        String sql = "update fex_tran_req_config set field_name = :fieldName where field_id = :fieldId";
        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);
        params.put("fieldName", fieldName);
        createSQLQuery(sql, params).executeUpdate();
    }

    public List<FexTranReqConfigVo> listByReferenced(String fieldId) {
        StringBuilder sql = new StringBuilder();
        // 主查询
        sql.append(" SELECT t.template_name, t.template_no  ");
        sql.append(" FROM fex_tran_template_config t ");
        // 子查询：获取每个模板最大版本号
        sql.append(" LEFT JOIN ( ");
        sql.append("     SELECT template_id, MAX(version_no) AS max_version ");
        sql.append("     FROM fex_tran_req_config_version ");
        sql.append("     GROUP BY template_id ");
        sql.append(" ) max_v ON t.id = max_v.template_id ");
        // 关联版本表
        sql.append(" LEFT JOIN fex_tran_req_config_version v ");
        sql.append("     ON max_v.template_id = v.template_id ");
        sql.append("     AND max_v.max_version = v.version_no ");
        // 关联配置表
        sql.append(" LEFT JOIN fex_tran_req_config c ");
        sql.append("     ON c.template_config_version_id = v.id ");
        sql.append(" WHERE t.is_delete = '0' ");
        sql.append(" AND t.parent_id IS NULL ");
        sql.append(" AND v.version_no IS NOT NULL ");
        sql.append(" AND c.field_id = :fieldId ");
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqConfigVo.class);
    }
}
