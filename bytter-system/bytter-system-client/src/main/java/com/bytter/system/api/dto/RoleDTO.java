package com.bytter.system.api.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色对象
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class RoleDTO implements Serializable {
    /**
     * 角色编号
     */
    private String id;
    /**
     * 角色代码
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
}
