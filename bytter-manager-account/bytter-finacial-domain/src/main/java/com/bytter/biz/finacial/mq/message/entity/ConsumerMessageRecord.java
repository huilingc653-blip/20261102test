package com.bytter.biz.finacial.mq.message.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.enums.MsgHandleStatus;
import com.bytter.biz.payment.api.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author YonJar
 * @date 2024/3/4.
 * @description 主要是记录 payment-to-finacial 的消息
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("FINACIAL_CONSUMER_RECORD")
public class ConsumerMessageRecord implements Serializable {

    @TableId
    private String msgId;
    private String tenantId;
    private BusinessType businessType; //业务类型
    private String businessId; //业务唯一ID，如 voucherNo 等
    private String message;
    private MsgHandleStatus msgHandleStatus;
    private String exceptionMsg; //异常时 记录
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
