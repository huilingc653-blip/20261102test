package com.bytter.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息提醒关联用户对象。
 *
 * @author zhaohc
 * @since 2024/6/27 15:13
 */
@Data
public class NotificationUserVO implements Serializable {
    private String id;
    private String categoryId;
    private String userId;
    private String userName;
}
