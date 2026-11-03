package com.bytter.job.admin.core.model;

import lombok.Data;

import java.util.Date;

/**
 * 任务权限表。
 *
 * @author zhaohc
 * @date 2025/6/23 14:49
 */
@Data
public class XxlJobPermission {
    /**
     * 主键
     */
    private int id;
    /**
     * 任务ID
     */
    private int jobId;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 权限类型 0:不可见 1:只读 2:读写
     */
    private int permissionType;
    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 变更用户
     */
    private String updateBy;

    /**
     * 删除标志（0-存在 1-删除）
     */
    private String isDeleted;
}
