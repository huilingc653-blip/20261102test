package com.bytter.olap.services;

import com.bytter.olap.dao.BoardUserPermissionDao;
import com.bytter.olap.pojo.DashboardUserPermission;
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
public class DashboardUserPermissionService {

    @Autowired
    private BoardUserPermissionDao boardUserPermissionDao;

    /**
     * 更新用户对看板的权限
     *
     * @param boardId    看板ID
     * @param userIdList 用户ID列表
     */
    public void updateBoardUserPermission(String boardId, List<String> userIdList) {
        List<DashboardUserPermission> permissionList = boardUserPermissionDao.getPermissionListByBoardId(boardId);
        if (CollectionUtils.isEmpty(permissionList)) {
            // 无用户权限分配记录，则插入所有的用户ID
            saveBatch(boardId, userIdList);
        } else {
            // 1、筛选未插入权限表的用户ID
            Set<String> hasPermissionUserIdSet = new HashSet<>();
            List<String> noPermissionUserIdList = new ArrayList<>();
            for (DashboardUserPermission permission : permissionList) {
                hasPermissionUserIdSet.add(permission.getUserId());
            }
            for (String userId : userIdList) {
                if (!hasPermissionUserIdSet.contains(userId)) {
                    noPermissionUserIdList.add(userId);
                }
            }
            if (!CollectionUtils.isEmpty(noPermissionUserIdList)) {
                // 将没有权限的用户ID插入权限表
                saveBatch(boardId, noPermissionUserIdList);
            }


            // 2、删除要剔除权限的用户ID
            Set<String> saveUserIdSet = new HashSet<>();
            List<String> deletePermissionIdList = new ArrayList<>();
            for (String userId : userIdList) {
                saveUserIdSet.add(userId);
            }
            for (DashboardUserPermission permission : permissionList) {
                if (!saveUserIdSet.contains(permission.getUserId())) {
                    deletePermissionIdList.add(permission.getId());
                }
            }
            if (!CollectionUtils.isEmpty(deletePermissionIdList)) {
                // 删除剔除权限的记录
                boardUserPermissionDao.deleteByBatch(deletePermissionIdList);
            }
        }
    }

    /**
     * 删除看板的权限记录
     *
     * @param boardId 看板ID
     * @return 受影响行数
     */
    public int deleteAllUserPermissionByBoardId(String boardId) {
        return boardUserPermissionDao.deleteAllByBoardId(boardId);
    }

    public List<String> getUserIdListByBoardId(String boardId) {
        List<DashboardUserPermission> permissionList = boardUserPermissionDao.getPermissionListByBoardId(boardId);
        List<String> userIdList = new ArrayList<>();
        for (DashboardUserPermission permission : permissionList) {
            userIdList.add(permission.getUserId());
        }
        return userIdList;
    }


    private void saveBatch(String boardId, List<String> userIdList) {
        List<DashboardUserPermission> list = new ArrayList<>();
        DashboardUserPermission userPermission;
        for (String userId : userIdList) {
            userPermission = new DashboardUserPermission();
            userPermission.setId(SqlUtils.getUuid());
            userPermission.setBoardId(boardId);
            userPermission.setUserId(userId);
            list.add(userPermission);
        }
        if (!CollectionUtils.isEmpty(list)) {
            for (DashboardUserPermission permission : list) {
                boardUserPermissionDao.save(permission);
            }
        }
    }

}
