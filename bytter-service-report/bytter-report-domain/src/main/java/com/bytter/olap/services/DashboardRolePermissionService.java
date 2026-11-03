package com.bytter.olap.services;

import com.bytter.olap.dao.BoardRolePermissionDao;
import com.bytter.olap.pojo.DashboardRolePermission;
import com.bytter.olap.util.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenlw
 * @date 2019/11/05
 */
@Repository
public class DashboardRolePermissionService {

    @Autowired
    private BoardRolePermissionDao boardRolePermissionDao;


    public void updateBoardRolePermission(String boardId, List<String> roleIdList) {
        List<DashboardRolePermission> permissionList = boardRolePermissionDao.getPermissionListByBoardId(boardId);
        if (CollectionUtils.isEmpty(permissionList)) {
            // 无用户权限分配记录，则插入所有的用户ID
            saveBatch(boardId, roleIdList);
        } else {
            // 1、筛选未插入权限表的用户ID
            Set<String> hasPermissionRoleIdSet = new HashSet<>();
            List<String> noPermissionRoleIdList = new ArrayList<>();
            for (DashboardRolePermission permission : permissionList) {
                hasPermissionRoleIdSet.add(permission.getRoleId());
            }
            for (String roleId : roleIdList) {
                if (!hasPermissionRoleIdSet.contains(roleId)) {
                    noPermissionRoleIdList.add(roleId);
                }
            }
            if(!CollectionUtils.isEmpty(noPermissionRoleIdList)){
                // 将没有权限的用户ID插入权限表
                saveBatch(boardId, noPermissionRoleIdList);
            }


            // 2、删除要剔除权限的用户ID
            Set<String> saveRoleIdSet = new HashSet<>();
            List<String> deletePermissionIdList = new ArrayList<>();
            for (String userId : roleIdList) {
                saveRoleIdSet.add(userId);
            }
            for (DashboardRolePermission permission : permissionList) {
                if (!saveRoleIdSet.contains(permission.getRoleId())) {
                    deletePermissionIdList.add(permission.getId());
                }
            }
            if(!CollectionUtils.isEmpty(deletePermissionIdList)){
                // 删除剔除权限的记录
                boardRolePermissionDao.deleteByBatch(deletePermissionIdList);
            }
        }
    }

    /**
     * 删除看板的权限记录
     *
     * @param boardId 看板ID
     * @return 受影响行数
     */
    public int deleteAllRolePermissionByBoardId(String boardId) {
        return boardRolePermissionDao.deleteAllByBoardId(boardId);
    }

    public List<String> getRoleIdListByBoardId(String boardId) {
        List<DashboardRolePermission> permissionList = boardRolePermissionDao.getPermissionListByBoardId(boardId);
        List<String> roleIdList = new ArrayList<>();
        for (DashboardRolePermission permission : permissionList) {
            roleIdList.add(permission.getRoleId());
        }
        return roleIdList;
    }


    private void saveBatch(String boardId, List<String> roleIdList) {
        List<DashboardRolePermission> list = new ArrayList<>();
        DashboardRolePermission rolePermission;
        for (String roleId : roleIdList) {
            rolePermission = new DashboardRolePermission();
            rolePermission.setId(SqlUtils.getUuid());
            rolePermission.setBoardId(boardId);
            rolePermission.setRoleId(roleId);
            list.add(rolePermission);
        }
        if (!CollectionUtils.isEmpty(list)) {
            for (DashboardRolePermission permission : list) {
                boardRolePermissionDao.save(permission);
            }
        }
    }


}
