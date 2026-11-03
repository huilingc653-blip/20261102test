package com.bytter.olap.dao;

import com.bytter.olap.pojo.DashboardRolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenlw on 2019/11/05.
 */
@Repository
public interface BoardRolePermissionDao {

    List<DashboardRolePermission> getPermissionListByBoardId(@Param("boardId") String boardId);


    int save(DashboardRolePermission dashboardUserPermission);

    /**
     * 批量删除
     *
     * @param ids 要删除的ID列表
     * @return 受影响数目
     */
    int deleteByBatch(@Param("ids") List<String> ids);

    int deleteAllByBoardId(@Param("boardId") String boardId);


}
