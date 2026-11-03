package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.SysCorp;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpSysCorpDao extends BaseDao<SysCorp, String> {

    /***
     * 根据租户id，单位代码获取单位信息
     *
     * @param tenantId
     * @param corpCode
     * @return 单位信息
     */
    public SysCorp getOneCorp(String tenantId, String corpCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, name, code, tenant_id as tenantId ");
        sql.append(" from SYS_CORP ");
        sql.append(" where status = :status and code = :code and tenant_id = :tenantId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("status", CommonConstant.VALID_SIGN_YES);
        params.put("code", corpCode);
        params.put("tenantId", tenantId);

        List<SysCorp> corpList = this.listBySQLAliasToBean(sql.toString(), params, SysCorp.class);

        if (EmptyUtils.isEmpty(corpList)) {
            return null;
        } else if (corpList.size() > 1) {
            logger.error("同一租户下存在重复的单位，请联系管理员删除重复数据！，租户id：{}，单位代码：{}", tenantId, corpCode);
        }
        return corpList.stream().findFirst().get();
    }

    public SysCorp getByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, name, code, tenant_id as tenantId ");
        sql.append(" from SYS_CORP ");
        sql.append(" where id = :corpId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("corpId", corpId);

        return this.getBySQLAliasToBean(sql.toString(), params, SysCorp.class);
    }
}
