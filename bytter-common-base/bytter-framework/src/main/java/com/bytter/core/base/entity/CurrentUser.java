package com.bytter.core.base.entity;

import lombok.Data;

/**
 * 当前用户信息
 * @author zhoul
 * @since 2018/1/5.
 */
@Data
public class CurrentUser implements ICurrentUser {
    private String id;
    private String loginName;
    private String username;
    private String corpId;
    private boolean admin;

    private String tenantId;

}
