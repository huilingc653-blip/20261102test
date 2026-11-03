package com.bytter.biz.mq.message.entity;

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
 * @date 2024/3/1.
 * 用于记录消息 发送到核算
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("PAYMENT_PRODUCER_RECORD")
public class ProducerMessageRecord implements Serializable {

    @TableId
    private String msgId;
    private String tenantId;
    private BusinessType businessType; //业务类型
    /**
     * -- 消息记录表里面的 business_id = 付款指令编号
     * -- 付款指令表 bis_exc a
     * --                      和 资金调拨制单表 nis_allocation_req b 关联是 on b.id = a.CBS_BILL_NO 付款单号
     * --                      和 付款通知表 nis_billhead b 关联是 on b.id = a.CBS_BILL_NO 付款单号
     */
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
