package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @Description 群聊机器人发送消息返回
* @author zhous
* @date 2024/8/21 15:10
*/
@Data
public class GroupChatBotMsgDTO implements Serializable {

    /**
     * 状态 0-成功 1-失败
     */
    private String status;
    /**
     * 请求报文
     */
    private String reqMsg;
    /**
     * 响应报文
     */
    private String respMsg;
}
