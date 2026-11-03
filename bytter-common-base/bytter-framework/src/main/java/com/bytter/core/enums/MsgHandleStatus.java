package com.bytter.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YonJar
 * @date 2024/3/4.
 * @description
 */
@Getter
@AllArgsConstructor
public enum MsgHandleStatus {

    N_HANDLE("未处理"),
    F_HANDLE("处理失败"),
    Y_HANDLE("处理成功");

    private final String name;
}
