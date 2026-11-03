package com.bytter.erp.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserDTO implements Serializable {
    private String username;
    private String password;
}
