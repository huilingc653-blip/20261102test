package com.bytter.cloud.auth.persistence;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
/**
 * 配置DTO
 *
 * @author wangce 2021-03-04
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ConfigDto implements Serializable {
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 服务
     */
    private String service;
    /**
     * 配置类型
     */
    private String type;
    /**
     * 配置名称
     */
    private String name;
    /**
     * 配置值
     */
    private String value;
}
