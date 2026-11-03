package com.bytter.job.admin.dao;

import com.bytter.job.admin.core.model.XxlJobPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 定时任务权限。
 *
 * @author zhaohc
 * @date 2025/6/24 16:24
 */
@Mapper
public interface XxlJobPermissionDao {

    int save(XxlJobPermission xxlJobPermission);

    int update(XxlJobPermission xxlJobPermission);

    int delete(@Param("id") long id);

    List<XxlJobPermission> findByUserId(int userId);

    int deleteByUserId(int userId);

    XxlJobPermission findByUserAndJob(@Param("userId") int userId, @Param("jobId") int jobId);

    int deleteByUserAndJob(@Param("userId") int userId, @Param("jobId") int jobId);

    int deleteByJobId(int jobId);

    int batchSavePermissions(@Param("userId") int userId, @Param("permissionType") int permissionType,
                             @Param("userName") String userName, @Param("jobGroup") int jobGroup,
                             @Param("jobDesc") String jobDesc);
}
