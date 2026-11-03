package com.bytter.third.dto;

import lombok.Data;

/**
 * 大参林接口鉴权Token。
 *
 * @author zhaohc
 * @since 2024/5/29 10:06
 */
@Data
public class DslToken {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;

}
