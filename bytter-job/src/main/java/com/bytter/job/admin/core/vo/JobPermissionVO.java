package com.bytter.job.admin.core.vo;

import lombok.Data;

/**
 * 权限信息。
 *
 * @author zhaohc
 * @date 2025/6/25 9:34
 */
@Data
public class JobPermissionVO {
    private int id;
    private String jobDesc;
    private String jobGroupTitle;
    private int permissionType;
}
