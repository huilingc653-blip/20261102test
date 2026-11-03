package com.bytter.cloud.auth.authorization.oauth2.sso.dsl;

import lombok.Data;

import java.io.Serializable;

/**
 * 单点登录--大参林返回的信息
 *
 * @author zhaohc
 * @date 2024/5/16 15:19
 */
@Data
public class DslAccount implements Serializable {

    private Long id;
    private String username;
    private String nickname;
    private String mobile;
    private String mail;
    private String workWeixinMail;
    private String workweixinUserId;
    private String employeeId;
    private String deptIdMain;
    private boolean remind;
    private boolean firstLogin;
    private boolean checkType;
    private Long groupId;

}
