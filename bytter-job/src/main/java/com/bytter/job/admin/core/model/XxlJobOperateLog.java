package com.bytter.job.admin.core.model;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志。
 *
 * @author zhaohc
 * @date 2025/6/24 15:41
 */
@Data
public class XxlJobOperateLog {
    /**
     * 主键
     */
    private long id;
    /**
     * 操作时间
     */
    private Date operateTime;
    /**
     * 操作用户
     */
    private int userId;
    /**
     * 操用用户名
     */
    private String username;
    /**
     * 操作任务
     */
    private int jobId;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 操作描述
     */
    private String description;
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
