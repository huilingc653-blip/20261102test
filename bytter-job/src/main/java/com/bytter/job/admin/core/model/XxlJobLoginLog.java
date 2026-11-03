package com.bytter.job.admin.core.model;

import lombok.Data;

import java.util.Date;

/**
 * 登陆日志。
 *
 * @author zhaohc
 * @date 2025/6/24 15:42
 */
@Data
public class XxlJobLoginLog {
    /**
     * 主键
     */
    private long id;
    /**
     * 登陆时间
     */
    private Date loginTime;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登陆IP
     */
    private String ip;
    /**
     * 状态：1成功 2失败
     */
    private int status;
    /**
     * 失败原因
     */
    private String reason;
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
