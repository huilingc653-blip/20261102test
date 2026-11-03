//package com.bytter.biz.finacial.mq.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.bytter.biz.finacial.mq.message.entity.ProducerMessageRecord;
//import com.bytter.biz.finacial.mq.message.service.ProducerMessageRecordService;
//import com.bytter.biz.payment.api.dto.TransactionMqMsg;
//import com.bytter.core.common.queue.CommonMqMsg;
//import com.bytter.core.enums.SendStatus;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageBuilder;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//
//
///**
// * @author luoyj
// * @date 2024/3/1.
// */
//@Slf4j
//@Component
//public class RabbitProducerUtil implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private ProducerMessageRecordService producerMessageRecordService;
//
//    public void send(String exchange, String routingKey, CommonMqMsg<TransactionMqMsg> commonMqMsg){
//        String jsonString = JSON.toJSONString(commonMqMsg);
//        Message message = MessageBuilder.withBody(jsonString.getBytes(StandardCharsets.UTF_8))
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(commonMqMsg.getMsgId()).build();
//
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//        log.info("RabbitProducerUtil messages sending：{}",message);
//        rabbitTemplate.convertAndSend(exchange,routingKey,message,new CorrelationData(commonMqMsg.getMsgId()));
//    }
//
//    /**
//     * 核算发送消息到结算
//     * @param commonMqMsg
//     */
//    public void send(CommonMqMsg<TransactionMqMsg> commonMqMsg){
//        String jsonString = JSON.toJSONString(commonMqMsg);
//        Message message = MessageBuilder.withBody(jsonString.getBytes(StandardCharsets.UTF_8))
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(commonMqMsg.getMsgId()).build();
//
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//        log.info("RabbitProducerUtil messages sending：{}",message);
//        // finacial-collection-exchange
//        rabbitTemplate.convertAndSend("finacial-collection-exchange","ftopkey",message,new CorrelationData(commonMqMsg.getMsgId()));
//    }
//
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (ack) {
//            //更新记录表状态和时间
//            boolean boo = producerMessageRecordService.updateById(ProducerMessageRecord.builder().msgId(correlationData.getId()).sendStatus(SendStatus.ACK).updateTime(LocalDateTime.now()).build());
//            log.info("msgId：{},更新结果：{}",correlationData.getId(),boo);
//        } else {
//            //投递消息失败
//            log.warn("RabbitProducerUtil -- msgId：{},Message was not acknowledged: {}",correlationData.getId(), cause) ;
//            //重试 或者 留给 定时任务去处理
//        }
//    }
//
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        log.warn("ProducerUtil -- Returned message: " + message + " with reply code: " + replyCode + " and reply text: " + replyText +
//                " sent to exchange: " + exchange + " with routing key: " + routingKey);
//    }
//}
