package com.bytter.cloud.common.core.persistence.dto;
import lombok.Data;
import java.io.Serializable;
/**
 * 租户传输对象
 *
 * @author zhoul
 * @since 1.0.0
 */
@Data
public class TenantDto implements Serializable {
    /**
     * 租戶编码
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 地址
     */
    private String address;
    /**
     * 角色编码
     */
    private String roleId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 添加用户
     */
    private String createdBy;
}
