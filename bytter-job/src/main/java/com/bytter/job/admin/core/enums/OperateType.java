package com.bytter.job.admin.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举。
 *
 * @author zhaohc
 * @date 2025/6/23 17:46
 */
@Getter
@AllArgsConstructor
public enum OperateType {
    ADD(1, "添加"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    START(4, "启动"),
    STOP(5, "停止"),
    TRIGGER(6, "执行"),
    OTHER(7, "其它"),
    ;

    private final int code;
    private final String value;
}
