package com.bytter.supply.domain.platform.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.platform.entity.ScfFunderInfo;
import com.bytter.supply.domain.platform.vo.ScfFunderInfoVo;
import org.springframework.stereotype.Repository;

/**
 * @Title: ScfFunderInfoDao
 * @Description: 资金方信息
 * @version 1.0
 */
@Repository
public class ScfFunderInfoDao extends BaseDao<ScfFunderInfo, String> {

    /**
     * 列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<ScfFunderInfoVo> listScfFunderInfo(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, a.code, a.type, a.bank_no as bankNo, a.name, a.sign, a.valid, a.rmk, ");
        sql.append("  a.create_by as createBy, a.create_date as createDate, ");
        sql.append("  a.update_by as updateBy, a.update_date as updateDate ");
        sql.append(" from scf_funder_info a where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfFunderInfoVo.class, "a", false);
    }

    /**
     * 获取最大编码流水号
     */
    public String getMaxCode() {
        String sql = " select MAX(CODE) as code from scf_funder_info where CODE like 'XJF%' ";
        Map<String, Object> params = new HashMap<String, Object>();
        List<ScfFunderInfo> list = listBySQLAliasToBean(sql, params, ScfFunderInfo.class);
        if (list != null && !list.isEmpty() && list.get(0).getCode() != null) {
            return list.get(0).getCode();
        }
        return null;
    }

    /**
     * 根据编码查询资金方（用于校验编码唯一性）
     */
    public List<ScfFunderInfo> getFunderInfoByCode(String code, String excludeId) {
        String hql = " from ScfFunderInfo a where a.code = :code ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        List<ScfFunderInfo> list = listByHQL(hql, params);
        if (excludeId != null && !excludeId.isEmpty()) {
            list.removeIf(item -> excludeId.equals(item.getId()));
        }
        return list;
    }

    /**
     * 查询资金方列表（有效）
     */
    public List<ScfFunderInfoVo> getFunderJson() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, a.code as code, a.name as name, a.type as type, a.bank_no as bankNo, a.sign as sign, a.valid as valid, a.rmk as rmk");
        sql.append(" from scf_funder_info a where a.valid = '1' ");
        return listBySQLAliasToBean(sql.toString(), null, ScfFunderInfoVo.class);
    }
}
