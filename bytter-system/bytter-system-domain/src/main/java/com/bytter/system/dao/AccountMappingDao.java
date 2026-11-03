package com.bytter.system.dao;
import com.bytter.system.entity.AccountMapping;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @author luxy
 * @Title: UserMappingDao
 * @ProjectName T3.4
 * @Description: 用户账号映射数据访问层
 * @date 2020/8/26 16:50
 */
@Repository
public class AccountMappingDao extends BaseDao<AccountMapping, String> {
    /**
     * 查询账号关系映射信息
     *
     * @return
     */
    public AccountMappingDTO queryAccountMapping(String relationId, String sysUserId, String clientType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT * FROM SYS_ACCOUNT_MAPPING um where  1=1");
        if (!StringUtils.isBlank(relationId)) {
            sql.append(" and um.relation_user_id=:relationId ");
            params.put("relationId", relationId);
        }
        if (!StringUtils.isBlank(sysUserId)) {
            sql.append(" and um.SYS_USER_ID=:sysUserId ");
            params.put("sysUserId", sysUserId);
        }
        if (!StringUtils.isBlank(clientType)) {
            sql.append(" and um.CLIENT_TYPE=:clientType ");
            params.put("clientType", clientType);
        }
        return getBySQLAliasToBean(sql.toString(), params, AccountMappingDTO.class);
    }
    public void deleteMapInfo(String relationUserId, String clientType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from SYS_ACCOUNT_MAPPING  where 1=1 ");
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isBlank(relationUserId)) {
            sql.append(" and relation_user_id=:relationUserId ");
            params.put("relationUserId", relationUserId);
        }
        if (!StringUtils.isBlank(clientType)) {
            sql.append(" and CLIENT_TYPE=:clientType ");
            params.put("clientType", clientType);
        }
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public void deleteMapInfoByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from SYS_ACCOUNT_MAPPING ");
        Map<String, String> params = new HashMap<>();
        sql.append(" where SYS_USER_ID=:userId ");
        params.put("userId", userId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
