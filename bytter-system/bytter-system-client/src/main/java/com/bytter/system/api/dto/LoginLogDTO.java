package com.bytter.system.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class LoginLogDTO  implements Serializable {

	private String id;

    private String loginName;
    /**
     * 登录方式
     */
    private Integer type;
    /**
     * 登录IP
     */
    private String host;
    /**
     * 浏览器
     */
    private String userAgent;
    /**
     * 登录时间
     */
    private Date loginDate;
    /**
     * 退出时间
     */
    private Date logoutDate;
    /**
     * 会话Id
     */
    private String sessionId;
    private Integer status;
    private String reason;
    /**
     * 租户Id
     */
    private String tenantId;

}
