package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
/**
 * 单点登录--上海电气返回的信息
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@Getter
@Setter
public class ShdqAccount implements Serializable {
    private long id;
    private long userId;
    private String code;
    private String name;
    private boolean isOuter;
    private String sexCode;
    private String sexName;
    private String domainCode;
    private String domainName;
    private String email;
    private boolean isLeaved;
    private String inProcess;
    private boolean isLocked;
    private int failedCount;
}
