package com.bytter.biz.payment.api.dto;

import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author luoyj
 * @date 2024/3/5.
 */

@Data
public class TransactionMqMsg<T> implements Serializable {

//    private String msgId; //提到外层 CommonMqMsg.getMsgId
    private BusinessType businessType; //业务类型
    private String businessId; //业务ID
    /**
     * 发送消息体 需要实现序列化接口
     */
    private T message; //TODO 传什么消息，看具体业务需要
    private LocalDateTime sendTime;

    /**
     * 执行付款/收款的当前登录用户信息
     */
    private CurrentUser user;

    public static <T> TransactionMqMsg<T> buildMsg(BusinessType businessType, String businessId, T message, CurrentUser user){
        TransactionMqMsg<T> msg = new TransactionMqMsg<>();
        msg.businessType = businessType;
        msg.businessId = businessId;
        msg.setMessage(message);
        msg.setSendTime(LocalDateTime.now());
        msg.setUser(user);
        return msg;
    }

    public static <T> TransactionMqMsg<T> buildMsg(BusinessType businessType, String businessId, T message, CurrentUser user, LocalDateTime sendTime){
        TransactionMqMsg<T> msg = new TransactionMqMsg<>();
        msg.businessType = businessType;
        msg.businessId = businessId;
        msg.setMessage(message);
        msg.setSendTime(sendTime);
        msg.setUser(user);
        return msg;
    }
}
