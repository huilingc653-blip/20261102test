package com.bytter.system.api;
import com.bytter.system.api.dto.AccountMappingDTO;
/**
 * @author luxy
 * @Title: IAccountMappingService
 * @ProjectName T3.4
 * @Description: 用户账号映射接口
 * @date 2020/8/26 16:41
 */
public interface IAccountMappingService {
    /**
     * 根据其他平台用户id查询是否存在映射关系
     *
     * @param relationId
     * @param sysUserId
     * @param clientType
     * @return
     */
    AccountMappingDTO queryAccountMapping(String relationId, String sysUserId, String clientType);
    /**
     * 保存映射关系
     *
     * @param mappingDTO
     */
    void saveMapRelation(AccountMappingDTO mappingDTO);
    /**
     * 解除账号绑定
     *
     * @param relationUserId
     * @param clientType
     */
    void unbindAccount(String relationUserId, String clientType);
    /**
     * 管理员维护时传入钉钉或微信用户id保存映射关系
     */
    void saveAccountMap(String relationUserId, String sysUserId, String clientType);
    /**
     * 管理员维护时传入钉钉或微信用户id更新映射关系
     */
    void updateAccountMap(String relationUserId, String sysUserId, String clientType);
}
