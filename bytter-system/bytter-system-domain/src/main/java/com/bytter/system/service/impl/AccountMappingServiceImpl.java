package com.bytter.system.service.impl;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountMappingService;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.api.enums.UserMessageEnum;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.entity.AccountMapping;
import com.bytter.system.service.AccountMappingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author luxy
 * @Title: AccountMappingServiceImpl
 * @ProjectName T3.4
 * @Description: TODO
 * @date 2020/8/26 16:48
 */
@Slf4j
@Service("accountMappingServiceI")
@DubboService
public class AccountMappingServiceImpl implements IAccountMappingService {
    @Autowired
    private AccountMappingService mappingService;
    @Override
    public AccountMappingDTO queryAccountMapping(String relationId, String sysUserId, String clientType) {
        return mappingService.queryAccountMapping(relationId, sysUserId, clientType);
    }
    @Override
    public void saveMapRelation(AccountMappingDTO mappingDTO) {
        AccountMapping mapping = new AccountMapping();
        BeanUtils.copyNotNullProperties(mappingDTO, mapping);
        mappingService.saveEntity(mapping);
    }
    @Override
    public void unbindAccount(String relationUserId, String clientType) {
        if (!StringUtils.isBlank(relationUserId) && (!StringUtils.isBlank(clientType))) {
            mappingService.unbindAccount(relationUserId, clientType);
        }
    }
    @Override
    public void saveAccountMap(String relationUserId, String sysUserId, String clientType) {
        if (EmptyUtils.isNotEmpty(relationUserId)) {
            AccountMappingDTO mappingDTO = queryAccountMapping(relationUserId, null, clientType);
            if (null != mappingDTO) {
                // 先查询输入的第三方用户id是否已存在映射关系用户
                if (AppH5Constants.ClientType.DING_TALK.equals(clientType)) {
                    // throw new BusinessException(UserMessageEnum.SYS_ACCOUNT_MAPPING_DINGTALK_EXIST);
                    log.error("钉钉账号已存在映射关系，请勿重复绑定！");
                } else if (AppH5Constants.ClientType.CORP_WEI_XIN.equals(clientType)) {
                    // throw new BusinessException(UserMessageEnum.SYS_ACCOUNT_MAPPING_WEIXIN_EXIST);
                    log.error("企业微信账号已存在映射关系，请勿重复绑定！");
                }
            } else {
                // 不存在则新增
                mappingService.saveAccountMap(relationUserId, sysUserId, clientType);
            }
        }
    }
    @Override
    public void updateAccountMap(String relationUserId, String sysUserId, String clientType) {
        if (EmptyUtils.isNotEmpty(relationUserId)) {
            AccountMappingDTO mapDTO = queryAccountMapping(relationUserId, null, clientType);
            // 先查询输入的第三方用户id是否已存在映射关系用户
            if (null != mapDTO) {
                String originSysUserId = mapDTO.getSysUserId();
                if (originSysUserId.equals(sysUserId)) {
                    // 更新sysUserId的第三方id，更新账号映射关系
                    AccountMapping accountMapping = mappingService.get(mapDTO.getId());
                    accountMapping.setRelationUserId(relationUserId);
                    mappingService.saveOrUpdate(accountMapping);
                } else {
                    if (AppH5Constants.ClientType.DING_TALK.equals(clientType)) {
                        // throw new BusinessException(UserMessageEnum.SYS_ACCOUNT_MAPPING_DINGTALK_EXIST);
                        log.error("钉钉账号已存在映射关系，请勿重复绑定！");
                    } else if (AppH5Constants.ClientType.CORP_WEI_XIN.equals(clientType)) {
                        // throw new BusinessException(UserMessageEnum.SYS_ACCOUNT_MAPPING_WEIXIN_EXIST);
                        log.error("企业微信账号已存在映射关系，请勿重复绑定！");
                    }
                }
            } else {
                AccountMappingDTO mappingDTO = queryAccountMapping(null, sysUserId, clientType);
                if (null != mappingDTO) {
                    // 更新账号映射关系
                    AccountMapping accountMapping = mappingService.get(mappingDTO.getId());
                    accountMapping.setRelationUserId(relationUserId);
                    mappingService.saveOrUpdate(accountMapping);
                } else {
                    // 之前未维护，新增账号映射关系
                    mappingService.saveAccountMap(relationUserId, sysUserId, clientType);
                }
            }
        } else {
            // 第三方用户账号为空，则删除该账号映射关系
            AccountMappingDTO mappingDTO = queryAccountMapping(null, sysUserId, clientType);
            if (null != mappingDTO) {
                mappingService.delete(mappingDTO.getId());
            }
        }
    }
}
