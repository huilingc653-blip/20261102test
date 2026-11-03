package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.SysTenantManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 租户DAO
 *
 * @author wuxc
 * @date 2026-01-29
 */
@Repository
public class SysTenantManagerDao extends BaseDao<SysTenantManager, String> {

    /**
     * 修改银行流水截至时间
     *
     * @param tenantCode
     * @author zuoy 2018-04-04
     */
    public void updateByTenantId(String tenantCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("tenantCode", tenantCode);
        param.put("updateDate", new Date());
        this.batchExecute(" update SysTenantManager set updateTime = :updateDate where id = :tenantCode", param);
    }

    public void saveScheduledTaskAssignment(String tenantId, Integer jobId, String userId, Date now) {
        String sql = "insert into SYS_TENANT_SCHEDULED_TASK "
                + "(ID, TENANT_ID, JOB_ID, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME) "
                + "values (:id, :tenantId, :jobId, :createBy, :createTime, :updateBy, :updateTime)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", StringUtils.getUUId());
        params.put("tenantId", tenantId);
        params.put("jobId", jobId);
        params.put("createBy", userId);
        params.put("createTime", now);
        params.put("updateBy", userId);
        params.put("updateTime", now);
        this.batchSQLExecute(sql, params);
    }

}
