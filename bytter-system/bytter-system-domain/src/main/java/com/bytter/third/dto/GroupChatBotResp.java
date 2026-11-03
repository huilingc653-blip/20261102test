package com.bytter.third.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @Description 群聊机器人发送返回
* @author zhous
* @date 2024/8/22 16:08
*/
@Data
public class GroupChatBotResp implements Serializable {

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
