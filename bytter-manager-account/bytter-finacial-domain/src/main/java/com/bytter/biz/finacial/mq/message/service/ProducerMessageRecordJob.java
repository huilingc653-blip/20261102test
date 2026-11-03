package com.bytter.biz.finacial.mq.message.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bytter.biz.finacial.mq.message.entity.ProducerMessageRecord;
//import com.bytter.biz.finacial.mq.utils.RabbitProducerUtil;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.enums.SendStatus;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author YonJar
 * @date 2024/4/11.
 * @description 生产者消息记录表 定时扫描发送消息
 * 如何注入？-- 在common XxlJobConfig; 如何触发？-- admin操作页面编写cron表达式
 */
@Slf4j
@Component
public class ProducerMessageRecordJob implements ITaskService {

    @Autowired
    private ProducerMessageRecordService producerMessageRecordService;

    @Autowired
    private TenantAwareStreamBridge tenantAwareStreamBridge;

//    @Autowired
//    private StreamBridge streamBridge;

//    @Autowired
//    private RabbitProducerUtil rabbitProducerUtil;

    @XxlJob("ProducerMessageRecordJob")
    public void run(String arguments){
        log.info("Execute task: ProducerMessageRecordJob ");
        //扫描表数据
        LambdaQueryWrapper<ProducerMessageRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProducerMessageRecord::getSendStatus, SendStatus.N_SEND);
        List<ProducerMessageRecord> lists = producerMessageRecordService.list(queryWrapper);
        //lists 里面的数据应该是包含有 租户ID tenantId;

        //发送消息
        if (CollectionUtil.isNotEmpty(lists)){
            lists.forEach(a ->{
                // 数据入库前，已序列化; jsonMsg 反序列化为对象
                CommonMqMsg commonMqMsg = JSON.parseObject(a.getMessage(), CommonMqMsg.class);
//                rabbitProducerUtil.send(commonMqMsg);
                String jsonString = JSON.toJSONString(commonMqMsg);

                /*Message<String> message = MessageBuilder.withPayload(jsonString).setHeader("ftopkey", "ftopkey").build();
                MimeType mimeType = MimeType.valueOf("application/json");
                boolean send = streamBridge.send("finacial-to-payment-out-0", message, mimeType);*/

                HashMap<String, Object> headers = new HashMap<>();
                headers.put("ftopkey", "ftopkey"); // routingKey
                boolean send = tenantAwareStreamBridge.send("finacial-to-payment-out-0", jsonString, a.getTenantId(), headers);

                log.info("ProducerMessageRecordJob 发送消息:{}，status:{}",jsonString,send);
                if (send) {
                    //更新记录表状态和时间
                    boolean boo = producerMessageRecordService.updateById(ProducerMessageRecord.builder().msgId(commonMqMsg.getMsgId()).sendStatus(SendStatus.ACK).updateTime(LocalDateTime.now()).build());
                    log.info("msgId：{},更新结果：{}",commonMqMsg.getMsgId(),boo);
                } else {
                    //投递消息失败
                    log.warn("RabbitProducerUtil -- msgId：{}",commonMqMsg.getMsgId()) ;
                    //重试 或者 留给 定时任务去处理
                }
            });
        }



    }


}
