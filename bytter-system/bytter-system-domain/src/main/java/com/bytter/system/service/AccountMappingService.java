package com.bytter.system.service;
import com.bytter.system.dao.AccountMappingDao;
import com.bytter.system.entity.AccountMapping;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
/**
 * @author luxy
 * @Title: AccountMappingService
 * @ProjectName T3.4
 * @Description: TODO
 * @date 2020/8/26 16:49
 */
@Service("accountMappingService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountMappingService extends BaseService<AccountMapping, String> {
    @Autowired
    private AccountMappingDao mappingDao;
    @Override
    public BaseDao<AccountMapping, String> getEntityDao() {
        return mappingDao;
    }
    /**
     * 查询映射关系是否存在
     *
     * @return
     */
    public AccountMappingDTO queryAccountMapping(String relationId, String sysUserId, String clientType) {
        AccountMappingDTO accountMappingDTO = mappingDao.queryAccountMapping(relationId, sysUserId, clientType);
        return accountMappingDTO;
    }
    /**
     * 解除账号绑定关系
     *
     * @param relationUserId 第三方账户ID
     * @param clientType     客户端类型
     */
    public void unbindAccount(String relationUserId, String clientType) {
        mappingDao.deleteMapInfo(relationUserId, clientType);
    }
    /**
     * 解除所有第三方平台账号绑定关系
     *
     * @param userId 用户ID
     */
    public void unbindAllAccount(String userId) {
        mappingDao.deleteMapInfoByUserId(userId);
    }
    /**
     * 保存账号绑定关系
     */
    public void saveAccountMap(String relationUserId, String sysUserId, String clientType) {
        AccountMapping mapping = new AccountMapping();
        mapping.setRelationUserId(relationUserId);
        mapping.setSysUserId(sysUserId);
        mapping.setClientType(clientType);
        this.saveOrUpdate(mapping);
    }
}
