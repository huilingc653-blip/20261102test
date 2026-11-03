package com.bytter.supply.domain.platform.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.supply.domain.platform.entity.ScfBankOpen;
import com.bytter.supply.domain.platform.vo.ScfBankOpenVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScfBankOpenDao
 * @Description 接口开通情况 DAO
 */
@Repository
public class ScfBankOpenDao extends BaseDao<ScfBankOpen, String> {

    /**
     * 根据平台ID查询接口开通列表
     */
    public List<ScfBankOpenVo> getListByPlatformId(String scfPlatformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.id as id, b.type as type, b.api_name_cn as apiNameCn, ");
        sql.append(" b.api_name as apiName, b.scf_platform_id as scfPlatformId, ");
        sql.append(" b.create_by as createBy, b.create_date as createDate, ");
        sql.append(" b.update_by as updateBy, b.update_date as updateDate ");
        sql.append(" FROM scf_bank_open b WHERE b.scf_platform_id = :scfPlatformId ");
        Map<String, Object> params = new HashMap<>();
        params.put("scfPlatformId", scfPlatformId);
        return listBySQLAliasToBean(sql.toString(), params, ScfBankOpenVo.class);
    }
}
