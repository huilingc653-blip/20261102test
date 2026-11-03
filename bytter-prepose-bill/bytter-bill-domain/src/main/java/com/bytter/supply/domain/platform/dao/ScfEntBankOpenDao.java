package com.bytter.supply.domain.platform.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.platform.entity.ScfEntBankOpen;
import com.bytter.supply.domain.platform.vo.ScfBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntBankOpenVo;
import com.bytter.supply.domain.platform.vo.ScfEntPlatformListVo;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScfEntBankOpenDao
 * @Description 平台接口开通配置 DAO
 * @version 1.0
 */
@Repository
public class ScfEntBankOpenDao extends BaseDao<ScfEntBankOpen, String> {

    /**
     * 平台配置列表（仅展示已在 scf_ent_bank_open 配置过接口的平台）
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listPlatform(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT p.id, p.platform_code as entCode, p.platform_name as entName ");
        sql.append(" FROM scf_ent_bank_open ebo ");
        sql.append(" INNER JOIN scf_platform p ON ebo.ent_platform_id = p.id ");
        sql.append(" WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfEntPlatformListVo.class, "", false);
    }

    /**
     * 获取接口配置字典项（从 sys_dict_group + sys_dict 查询3个接口字典组：额度管理、应付债权凭证、应收债权凭证）
     */
    public List<ScfBankOpenVo> getBankOpenConfigList() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id, ");
        sql.append("        dg.TYPE_GROUP_NAME as type, ");
        sql.append("        d.TYPE_NAME as apiNameCn, ");
        sql.append("        d.TYPE_CODE as apiName, ");
        sql.append("        d.sort as sort ");
        sql.append(" FROM sys_dict d ");
        sql.append(" INNER JOIN sys_dict_group dg ON d.TYPE_GROUP_ID = dg.id ");
        sql.append(" WHERE dg.TYPE_GROUP_CODE IN ('scf.interface.credit', 'scf.interface.payable', 'scf.interface.receivable') ");
        sql.append("   AND (d.TYPE_ID IS NULL OR d.TYPE_ID = '') ");
        sql.append(" ORDER BY dg.id, d.sort ");
        return listBySQLAliasToBean(sql.toString(), null, ScfBankOpenVo.class);
    }
    /**
     * 根据平台ID查询接口开通配置列表
     */
    public List<ScfEntBankOpenVo> getListByEntPlatformId(String entPlatformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.id as id, b.ent_platform_id as entPlatformId, ");
        sql.append("        b.type as type, b.api_name_cn as apiNameCn, ");
        sql.append("        b.api_name as apiName, ");
        sql.append("        b.has_direct as hasDirect, ");
        sql.append("        b.direct as direct, ");
        sql.append("        b.create_by as createBy, b.create_date as createDate, ");
        sql.append("        b.update_by as updateBy, b.update_date as updateDate ");
        sql.append(" FROM scf_ent_bank_open b WHERE b.ent_platform_id = :entPlatformId ");
        Map<String, Object> params = new HashMap<>();
        params.put("entPlatformId", entPlatformId);
        return listBySQLAliasToBean(sql.toString(), params, ScfEntBankOpenVo.class);
    }

    /**
     * 根据平台ID判断是否已存在直联配置
     */
    public boolean existsByEntPlatformId(String entPlatformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from scf_ent_bank_open t where t.ent_platform_id = :entPlatformId ");
        Map<String, Object> params = new HashMap<>();
        params.put("entPlatformId", entPlatformId);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return count != null && count.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 根据平台ID删除接口开通配置
     */
    public void deleteByEntPlatformId(String entPlatformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM scf_ent_bank_open WHERE ent_platform_id = :entPlatformId ");
        Map<String, Object> params = new HashMap<>();
        params.put("entPlatformId", entPlatformId);
        batchSQLExecute(sql.toString(), params);
    }

    public boolean judgeInstructionOpen(String platformId,String apiCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from scf_ent_bank_open t where t.ent_platform_id = :platformId and t.api_name = :apiName ");
        sql.append(" and t.has_direct = '1' ");
        Map<String, Object> params = new HashMap<>();
        params.put("platformId", platformId);
        params.put("apiName", apiCode);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return count != null && count.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean judgeInstructionOpenAllPlatform(String apiName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from scf_ent_bank_open t where t.api_name = :apiName ");
        sql.append(" and t.has_direct = '1' ");
        Map<String, Object> params = new HashMap<>();
        params.put("apiName", apiName);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return count != null && count.compareTo(BigDecimal.ZERO) > 0;
    }
}
