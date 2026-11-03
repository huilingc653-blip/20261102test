//package com.bytter.bill.config;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
///**
// * RabbitMQ 消息队列消息确认机制配置，用于验证消息的消费应答，以及消息丢失的处理机制。
// *
// * @author zhaohc
// * @date 2023/3/18 14:04
// */
//@Configuration
//@Slf4j
//public class RabbitConfig {
//    @Bean
//    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConnectionFactory(connectionFactory);
//        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
//        rabbitTemplate.setMandatory(true);
//        //①消息推送到server，但是在server里找不到交换机，这种情况触发的是 ConfirmCallback 回调函数。
//        //②消息推送到server，找到交换机了，但是没找到队列，这种情况触发的是 ConfirmCallback和RetrunCallback两个回调函数。
//        //③消息推送到server，交换机和队列啥都没找到，这种情况触发的是 ConfirmCallback 回调函数。
//        //④消息推送成功，这种情况触发的是 ConfirmCallback 回调函数。
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (!ack) {
//                // 将发送失败的消息信息记录一下，或是发送邮件通知，不建议直接重新发送
//                log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
//                log.info("ConfirmCallback:     " + "确认情况：" + ack);
//                log.info("ConfirmCallback:     " + "原因：" + cause);
//            }
//        });
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            log.info("ReturnCallback:     " + "消息：" + message);
//            log.info("ReturnCallback:     " + "回应码：" + replyCode);
//            log.info("ReturnCallback:     " + "回应信息：" + replyText);
//            log.info("ReturnCallback:     " + "交换机：" + exchange);
//            log.info("ReturnCallback:     " + "路由键：" + routingKey);
//        });
//        return rabbitTemplate;
//    }
//}
