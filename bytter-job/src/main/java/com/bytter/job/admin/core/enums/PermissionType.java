package com.bytter.job.admin.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限类型。
 *
 * @author zhaohc
 * @date 2025/6/23 15:00
 */
@Getter
@AllArgsConstructor
public enum PermissionType {
    NO_ACCESS(0),    // 不可见
    READ_ONLY(1),    // 只读
    READ_WRITE(2);   // 读写

    private final int level;

    public boolean canRead() {
        return level >= READ_ONLY.level;
    }

    public boolean canWrite() {
        return level >= READ_WRITE.level;
    }
}
