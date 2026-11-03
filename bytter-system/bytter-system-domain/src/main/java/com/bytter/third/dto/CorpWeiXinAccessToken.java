package com.bytter.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信AccessToken封装类
 */
@Data
public class CorpWeiXinAccessToken implements Serializable {

    /**
     * 身份票据
     */
    @JsonProperty(value = "access_token")
    private String accessToken;
    /**
     * AccessToken过期时间，单位秒
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;
    /**
     * 获取AccessToken时间戳，用于判断AccessToken是否过期
     */
    @JsonProperty(value = "get_time")
    private Long getTime;

    /**
     * 返回码
     */
    @JsonProperty(value = "errcode")
    private Integer errCode;

    /**
     * 返回信息
     */
    @JsonProperty(value = "errmsg")
    private String errMsg;
}