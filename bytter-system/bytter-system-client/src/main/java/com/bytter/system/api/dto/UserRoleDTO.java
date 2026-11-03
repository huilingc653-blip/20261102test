package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleDTO implements Serializable {

    private String userId;

    private String roleId;

    private String roleName;
}
