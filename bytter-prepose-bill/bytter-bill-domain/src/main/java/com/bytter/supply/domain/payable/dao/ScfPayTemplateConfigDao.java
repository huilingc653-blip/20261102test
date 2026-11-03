package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.entity.ScfPayConfigVersion;
import com.bytter.supply.domain.payable.entity.ScfPayTemplateConfig;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVersionVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScfPayTemplateConfigDao extends BaseDao<ScfPayTemplateConfig, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.* FROM scf_pay_template_config t WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayConfigVersionVO.class, "v", false);
    }


    public List<ScfPayConfigVersion> listByCondition(ScfPayConfigVersion vo) {
        StringBuilder joinSql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        if (EmptyUtils.isNotEmpty(vo.getTemplateCode())) {
            joinSql.append(" AND template_code = :templateCode");
            param.put("templateCode", vo.getTemplateCode());
        }
        if (EmptyUtils.isNotEmpty(vo.getTemplateName())) {
            joinSql.append(" AND template_name = :templateName");
            param.put("templateName", vo.getTemplateName());
        }
        StringBuilder sql = new StringBuilder("select  id, template_code," +
                " template_name, template_desc, " +
                " remark, valid_sign," +
                " create_by, create_date, update_by, update_date " +
                " from SCF_PAY_CONFIG_VERSION  where  1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" order by create_date desc");
        return listBySQLAliasToBean(sql.toString(), param, ScfPayConfigVersion.class);
    }

    /**
     * 根据平台ID查询有效的模板配置（关联的模板版本需为有效状态）
     *
     * @param platformId 平台ID
     * @return 有效的模板配置，无则返回null
     */
    public ScfPayTemplateConfig getValidBaseInfoByPlatformId(String platformId) {
        String sql = "select t.* from scf_pay_template_config t" +
                " inner join scf_pay_config_version v on v.id = t.version_id" +
                " where t.platform_id = :platformId and t.is_delete = 0 and v.valid_sign = '1'";
        Map<String, Object> params = new HashMap<>();
        params.put("platformId", platformId);
        List<ScfPayTemplateConfig> list = listBySQLAliasToBean(sql, params, ScfPayTemplateConfig.class);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public ScfPayConfigVersion getVersionById(String id) {
        String sql = "select id, template_code, template_name, template_desc, remark, valid_sign," +
                " create_by, create_date, update_by, update_date from scf_pay_config_version where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, ScfPayConfigVersion.class);
    }

    public void deleteConfigByVersionId(String versionId) {
        String sql = "delete from scf_pay_template_config where version_id = :versionId";
        Map<String, Object> params = new HashMap<>();
        params.put("versionId", versionId);
        createSQLQuery(sql, params).executeUpdate();
    }

    public Boolean isUsingProductTemplate(List<String> productIdList) {
        String sql = "select * from scf_pay_template_config where product_id in (:productIdList) and is_delete = 0";
        Map<String, Object> params = new HashMap<>();
        params.put("productIdList", productIdList);
        return getBySQLAliasToBean(sql, params, ScfPayTemplateConfig.class) != null;
    }

    public Boolean isUsingCustFieldTemplate(List<String> custFieldIdList) {
        String sql = "select * from scf_pay_config where field_id in (:custFieldIdList) and is_delete = 0";
        Map<String, Object> params = new HashMap<>();
        params.put("custFieldIdList", custFieldIdList);
        return getBySQLAliasToBean(sql, params, ScfPayTemplateConfig.class) != null;
    }
}
