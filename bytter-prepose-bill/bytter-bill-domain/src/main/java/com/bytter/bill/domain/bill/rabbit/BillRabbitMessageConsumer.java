//package com.bytter.bill.domain.bill.rabbit;
//import cn.hutool.core.util.StrUtil;
//import com.bytter.core.constant.rabbit.RabbitConstant;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.Map;
///**
// * Rabbit消费端案例。
// * RabbitMQ三种常用交换机模式：主题交换机（模糊匹配）、扇形交换机（一对多广播）、直连交换机（一对一消费），
// * 本系统都通过Topic主题交换机来模拟实现；
// *
// * @author zhaohc
// * @date 2023/3/18 16:52
// */
//@Component
//@Slf4j
//public class BillRabbitMessageConsumer {
//    /**
//     * 使用RabbitTemplate,这提供了接收/发送等等方法
//     */
//    @Resource
//    RabbitTemplate rabbitTemplate;
//    /**
//     * ####################################【Topic交换机，单服务业务消费】###############################################
//     * ### 消息发送，指定交换机，业务Key，消息体（监听方法中需要和消息体相同类型，才可以监听到）
//     * rabbitTemplate.convertAndSend("交换机","routingKey","消息体");
//     * <p>
//     * RabbitMQ 消息监听方法，消息队列一个模块不用建太多，一般3-5个就足够了，业务key可以根据业务创建即可。
//     * 同一个队列的业务方法可以通过 key = {"xxx.*","xxx.#"} 来配置
//     * * (星号) 用来表示一个单词 (必须出现的)；
//     * # (井号) 用来表示任意数量（零个或多个）单词；
//     * <p>
//     * 注意：同一个队列，只需要声明一个消费方法即可，方法中的业务根据routingKey来区分不同方法实现，同一队列如果实现多处方法，会随机消费。
//     * String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//     *
//     * @param testMessage 消息体，可以根据发送的消息对象来自定义接收对象。
//     * @param message     MQ参数。
//     * @param channel     MQ队列。
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange(value = RabbitConstant.Exchange.EXCHANGE_TOPIC_NORMAL, type = ExchangeTypes.TOPIC),
//            value = @Queue(value = RabbitConstant.Queue.TOPIC_NORMAL_BILL_BILLBANK),
//            key = RabbitConstant.Queue.TOPIC_NORMAL_BILL_BILLBANK + ".*"))
//    public void topicMessage(Map testMessage, Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            log.info("RabbitMessageReceiver topicMessage6消费者收到消息  : " + testMessage.toString());
//            log.info("message = " + message);
//            // do something……
//            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//            // 业务键，调用不同业务方法
//            if (StrUtil.equals("topic.normal.bill.billbank.xxx",routingKey)){
//                // do something……
//                log.info("message = " + message);
//            }
//            // 手动应答确认消息已被消费，避免消息丢失
//            channel.basicAck(deliveryTag, true);
//        } catch (IOException e) {
//            // 第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝，此处设为不再放回队列
//            // 可添加相关日志，或是邮件通知，标明业务方法未能成功消费
//            channel.basicReject(deliveryTag, false);
//            log.error("RabbitMQ消息消费异常：", e);
//        }
//    }
//    /**
//     * ####################################【模拟fanout模式，全局广播，用于多模块同步数据】#################################
//     * 扇形交换机使用，一个广播需要定义一个交换机，如果业务过多，会创建过多的交换机，可以通过Topic来实现。
//     * ### 消息发送，指定交换机，业务Key，消息体（监听方法中需要和消息体相同类型，才可以监听到）
//     * rabbitTemplate.convertAndSend("交换机","routingKey","消息体");
//     * 注意：直连交换机是一对一的消费方法，一个业务需要一个队列，不能模糊匹配。
//     *
//     * @param testMessage 消息体，可以根据发送的消息对象来自定义接收对象。
//     * @param message     MQ参数。
//     * @param channel     MQ队列。
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange(value = RabbitConstant.Exchange.EXCHANGE_TOPIC_GLOBAL,type = ExchangeTypes.TOPIC),
//            value = @Queue(value = RabbitConstant.Queue.TOPIC_GLOBAL_BILL),
//            key = RabbitConstant.Exchange.EXCHANGE_TOPIC_GLOBAL + ".*"))
//    public void fanoutMessage1(Map testMessage, Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            log.info("RabbitMessageReceiver topicMessage6消费者收到消息  : " + testMessage.toString());
//            log.info("message = " + message);
//            // do something……
//            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//            // 业务键，调用不同业务方法
//            if (StrUtil.equals("exchange.topic.global.xxx",routingKey)){
//                // do something……
//                log.info("message = " + message);
//            }
//            // 手动应答确认消息已被消费，避免消息丢失
//            channel.basicAck(deliveryTag, true);
//        } catch (IOException e) {
//            // 第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝，此处设为不再放回队列
//            // 可添加相关日志，或是邮件通知，标明业务方法未能成功消费
//            channel.basicReject(deliveryTag, false);
//            log.error("RabbitMQ消息消费异常：", e);
//        }
//    }
//    /**
//     * 模拟扇形交换机模式，在不同服务中实现消费方法，需要指定不同的队列，绑定相同的交换机。
//     *
//     * @param testMessage 消息体，可以根据发送的消息对象来自定义接收对象。
//     * @param message     MQ参数。
//     * @param channel     MQ队列。
//     * @throws IOException 异常。
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange(value = RabbitConstant.Exchange.EXCHANGE_TOPIC_GLOBAL,type = ExchangeTypes.TOPIC),
//            value = @Queue(value = RabbitConstant.Queue.TOPIC_GLOBAL_PAYMENT),
//            key = RabbitConstant.Exchange.EXCHANGE_TOPIC_GLOBAL + ".*"))
//    public void fanoutMessage2(Map testMessage, Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            log.info("RabbitMessageReceiver topicMessage6消费者收到消息  : " + testMessage.toString());
//            log.info("message = " + message);
//            // do something……
//            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//            // 业务键，调用不同业务方法
//            if (StrUtil.equals("exchange.topic.global.xxx",routingKey)){
//                // do something……
//                log.info("message = " + message);
//            }
//            // 手动应答确认消息已被消费，避免消息丢失
//            channel.basicAck(deliveryTag, true);
//        } catch (IOException e) {
//            // 第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝，此处设为不再放回队列
//            // 可添加相关日志，或是邮件通知，标明业务方法未能成功消费
//            channel.basicReject(deliveryTag, false);
//            log.error("RabbitMQ消息消费异常：", e);
//        }
//    }
//    // /**
//    //  * 直连交换机，一个业务需要定义一个队列，不建议使用，会导致RabbitMQ服务端创建过多的队列，不便于定位，优先使用Topic交换机。
//    //  *
//    //  * @param testMessage 消息体，可以根据发送的消息对象来自定义接收对象。
//    //  * @param message     MQ参数。
//    //  * @param channel     MQ队列。
//    //  * @throws IOException 异常。
//    //  */
//    // @RabbitListener(bindings = @QueueBinding(
//    //         exchange = @Exchange(value = RabbitConstant.Exchange.EXCHANGE_TOPIC_WORKFLOW),
//    //         value = @Queue(value = RabbitConstant.Queue.TOPIC_WORKFLOW_BILL),
//    //         key = RabbitConstant.Queue.TOPIC_WORKFLOW_BILL))
//    // public void directMessage(Map testMessage, Message message, Channel channel) throws IOException {
//    //
//    // }
//}
