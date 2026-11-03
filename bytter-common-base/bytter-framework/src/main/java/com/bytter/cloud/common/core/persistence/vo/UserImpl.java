package com.bytter.cloud.common.core.persistence.vo;
import lombok.Data;
/**
 * 当前用户对象
 *
 * @author zhoul 2021-1-21
 * @since 1.0.0
 */
@Data
public class UserImpl implements IUser {
    private String currentUserId;
    private String corpId;
    private String tenantId;
    private String username;
    private String dataScope;
}
