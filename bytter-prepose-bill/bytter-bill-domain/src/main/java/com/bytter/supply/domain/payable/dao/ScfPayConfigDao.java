package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.entity.ScfPayConfig;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScfPayConfigDao extends BaseDao<ScfPayConfig, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.* FROM scf_pay_config c WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayConfigVO.class, "c", false);
    }

    public String getMaxVersionNo(String templateId) {
        String sql = "select max(version_no) from scf_pay_config_version where id = ?";
        Object versionNoOb = createSQLQuery(sql, templateId).uniqueResult();
        String versionNo = null;
        if (null != versionNoOb) {
            versionNo = versionNoOb.toString();
        }
        return versionNo;
    }

    /**
     * 根据模板ID查询模板字段配置列表
     * @param templateId 模板ID
     * @return 字段配置VO列表（按fieldSort排序）
     */
    public List<ScfPayConfigVO> getTemplateConfig(String templateId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id as id,");
        sql.append(" c.TEMPLATE_VERSION_ID as templateVersionId,");
        sql.append(" c.TEMPLATE_ID as templateId,");
        sql.append(" c.FIELD_ID as fieldId,");
        sql.append(" c.FIELD_CODE as fieldCode,");
        sql.append(" c.FIELD_NAME as fieldName,");
        sql.append(" c.FIELD_TYPE as fieldType,");
        sql.append(" c.FIELD_SORT as fieldSort,");
        sql.append(" c.FIELD_LENGTH as fieldLength,");
        sql.append(" c.FIELD_LIMIT_VAL as fieldLimitVal,");
        sql.append(" c.FIELD_DEF_VAL as fieldDefVal,");
        sql.append(" c.FIELD_DICT_GROUP as fieldDictGroup,");
        sql.append(" c.FIELD_LIMIT_DICT as fieldLimitDict,");
        sql.append(" c.FIELD_AREA as fieldArea,");
        sql.append(" c.FIELD_CONTENT as fieldContent,");
        sql.append(" c.DATA_TYPE as dataType,");
        sql.append(" c.IS_SYS as isSys,");
        sql.append(" c.IS_VISIBLE as isVisible,");
        sql.append(" c.IS_REQUIRED as isRequired,");
        sql.append(" c.IS_LIMIT_LENGTH as isLimitLength,");
        sql.append(" c.IS_LIMIT_DICT as isLimitDict,");
        sql.append(" c.IS_DELETED as isDeleted,");
        sql.append(" c.STATUS as status,");
        sql.append(" c.CREATE_BY as createBy,");
        sql.append(" c.CREATE_DATE as createDate,");
        sql.append(" c.UPDATE_BY as updateBy,");
        sql.append(" c.UPDATE_DATE as updateDate ");
        sql.append(" FROM SCF_PAY_CONFIG c WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(templateId)) {
            sql.append(" AND c.TEMPLATE_ID = :templateId");
            params.put("templateId", templateId);
        }
        sql.append(" ORDER BY c.FIELD_SORT");
        return listBySQLAliasToBean(sql.toString(), params, ScfPayConfigVO.class);
    }

    /**
     * 根据模板ID删除所有字段配置
     * @param templateId 模板ID
     */
    public void deleteByTemplateId(String templateId) {
        String sql = "DELETE FROM SCF_PAY_CONFIG WHERE TEMPLATE_ID = :templateId";
        Map<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        createSQLQuery(sql, params).executeUpdate();
    }


}
