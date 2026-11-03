package com.bytter.system.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.system.api.dto.CorpUserResourceDTO;
import com.bytter.system.api.dto.RoleResourceDTO;
import com.bytter.system.dao.CorpUserResourceDao;
import com.bytter.system.dao.RoleDao;
import com.bytter.system.entity.User;
import com.bytter.system.vo.UserPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/4
 */
@Slf4j
@Service
public class UserPermissionAsyncService {
    @Autowired
    private CorpUserResourceDao corpUserResourceDao;

    @Autowired
    private RoleDao roleDao;

    @Async("asyncThreadPoolExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<List<UserPermissionVO>> processUserPermissions(User user, String corpIds, String roleIds) {
        List<UserPermissionVO> result = new ArrayList<>();

        try {
            // 查询用户拥有的单位权限 //  为了查下效率这里会查出已删除单位的数据，下面会过滤掉
            List<CorpUserResourceDTO> corpUserResourceList = corpUserResourceDao.list(user.getId(), corpIds);
            if (ObjectUtil.isEmpty(corpUserResourceList)) {
                return CompletableFuture.completedFuture(result);
            }
            // 过滤corpUserResourceList中corpId为空的数据
            corpUserResourceList = corpUserResourceList.stream().filter(dto -> ObjectUtil.isNotEmpty(dto.getCorpId())).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(corpUserResourceList)) {
                return CompletableFuture.completedFuture(result);
            }
            // 查询用户拥有的角色权限
            List<RoleResourceDTO> userRoleList = roleDao.listUserRoleResource(user.getId(), roleIds);
            if (ObjectUtil.isEmpty(userRoleList)) {
                return CompletableFuture.completedFuture(result);
            }

            // 处理业务逻辑
            Map<String, Set<String>> roleResourceMap = userRoleList.stream()
                    .collect(Collectors.groupingBy(RoleResourceDTO::getRoleId,
                            Collectors.mapping(RoleResourceDTO::getResourceId,
                                    Collectors.toSet())));
            Map<String, String> roleNameMap = userRoleList.stream()
                    .collect(Collectors.toMap(RoleResourceDTO::getRoleId,
                            RoleResourceDTO::getRoleName,
                            (existing, replacement) -> existing));

            Map<String, List<CorpUserResourceDTO>> corpResourceMap = corpUserResourceList.stream()
                    .collect(Collectors.groupingBy(CorpUserResourceDTO::getCorpId));

            for (Map.Entry<String, List<CorpUserResourceDTO>> entry : corpResourceMap.entrySet()) {
                List<CorpUserResourceDTO> value = entry.getValue();
                Set<String> corpResourceIdSet = value.stream()
                        .filter(dto -> StrUtil.isNotEmpty(dto.getResourceId()))
                        .map(CorpUserResourceDTO::getResourceId)
                        .collect(Collectors.toSet());
                for (Map.Entry<String, Set<String>> roleEntry : roleResourceMap.entrySet()) {
                    Set<String> roleResourceIdSet =  roleEntry.getValue();
                    if (corpResourceIdSet.containsAll(roleResourceIdSet)) {
                        UserPermissionVO vo = new UserPermissionVO();
                        vo.setUserId(user.getId())
                                .setLoginName(user.getLoginName())
                                .setUsername(user.getUsername());
                        vo.setCorpId(entry.getKey())
                                .setCorpCode(value.get(0).getCorpCode())
                                .setCorpName(value.get(0).getCorpName());
                        vo.setRoleId(roleEntry.getKey())
                                .setRoleName(roleNameMap.get(roleEntry.getKey()));
                        vo.setAssignTime(value.get(0).getAssignTime());
                        result.add(vo);
                    }
                }
            }
        } catch (Exception e) {
            // 记录日志但不抛出异常，避免影响其他线程
            log.error("处理用户权限时发生错误，用户: {}", user.getLoginName(), e);
        }

        return CompletableFuture.completedFuture(result);
    }
}
