package com.bytter.biz.finacial.mq.message.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.enums.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author luoyj
 * @date 2024/4/10.
 * 用于记录消息 发送到结算
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("FINACIAL_PRODUCER_RECORD")
public class ProducerMessageRecord implements Serializable {

    @TableId
    private String msgId;
    private String tenantId;
    private BusinessType businessType; //业务类型
    private String businessId; //业务唯一ID，如 voucherNo 等
    private String exchangeName;
    private String routingKey;
    private String message;
    private SendStatus sendStatus;
    private LocalDateTime createTime;
    private String createBy;
    private LocalDateTime updateTime;
    private String updateBy;

}
