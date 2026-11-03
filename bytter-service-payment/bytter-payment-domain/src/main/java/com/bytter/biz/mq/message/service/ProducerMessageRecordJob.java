package com.bytter.biz.mq.message.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bytter.biz.mq.message.entity.ProducerMessageRecord;
//import com.bytter.biz.mq.utils.RabbitProducerUtil;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.enums.SendStatus;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author YonJar
 * @date 2024/3/11.
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
//
//    @Autowired
//    private RabbitProducerUtil rabbitProducerUtil;

    @XxlJob("ProducerMessageRecordJob")
    public void run(String arguments){
        log.info("Execute task: ProducerMessageRecordJob ");
        //扫描表数据
        LambdaQueryWrapper<ProducerMessageRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProducerMessageRecord::getSendStatus, SendStatus.N_SEND);
        List<ProducerMessageRecord> lists = producerMessageRecordService.list(queryWrapper);
        //发送消息
        if (CollectionUtil.isNotEmpty(lists)){
            lists.forEach(a ->{
                sendMessage(a.getExchangeName(), a.getRoutingKey(), a);
            });
        }

    }

    //这里会有多个不同的 exchange消息发送
    private void sendMessage(String exchangeName, String routingKey, ProducerMessageRecord record){
        // 数据入库前，已序列化; jsonMsg 反序列化为对象
        CommonMqMsg commonMqMsg = JSON.parseObject(record.getMessage(), CommonMqMsg.class);
        String jsonString = JSON.toJSONString(commonMqMsg);

        String bindingName = "";
        if ("payment-finacial-exchange".equalsIgnoreCase(exchangeName)){
            bindingName = "record-booking-out-0";
        }else if ("payment-bill-exchange".equalsIgnoreCase(exchangeName)) {
            bindingName = "payment-to-bill-out-0";
        }
        /*Message<String> message = MessageBuilder.withPayload(jsonString).setHeader(routingKey, routingKey).build();
        MimeType mimeType = MimeType.valueOf("application/json");
        boolean send = streamBridge.send( bindingName, message, mimeType);*/

        HashMap<String, Object> headers = new HashMap<>();
        headers.put(routingKey, routingKey); // routingKey
        boolean send = tenantAwareStreamBridge.send(bindingName, jsonString, record.getTenantId(), headers);

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
    }


}
