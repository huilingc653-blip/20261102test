//package com.bytter.core.constant.rabbit;
///**
// * RabbitMQ 的消息主题，用于共享分发消息。
// *
// * @author zhaohc
// * @date 2023/3/18 9:59
// */
//public class RabbitConstant {
//    /**
//     * 当需要使用交换机，在此定义交换机名称，非必要时，请优先使用主题交换机，主题交换机也可以完成扇形交换机的业务广播； 系统中的交换机暂定只使用主题交换机，后期业务增多，再酌情添加新的交换机；
//     */
//    public static class Exchange {
//        // 全局广播消息交换机，用于数据同步
//        public static final String EXCHANGE_TOPIC_GLOBAL = "exchange.topic.global";
//        // 普通主题交换机
//        public static final String EXCHANGE_TOPIC_NORMAL = "exchange.topic.normal";
//        // 工作流主题交换机
//        public static final String EXCHANGE_TOPIC_WORKFLOW = "exchange.topic.workflow";
//    }
//    /**
//     * 针对某种业务定义队列名称，命名格式： 交换机类型.业务模块.队列名 ，例如：topic.bill.accbook； 系统中的队列建议每个模块各自使用各自的队列，以业务来划分； 主题消息订阅时，可以用通配符来匹配：*或是#， *
//     * (星号) 用来表示一个单词 (必须出现的) # (井号) 用来表示任意数量（零个或多个）单词 针对具体的业务来定义消息的routeKey，用来区分具体的业务方法，可以在各自的模块中定义。
//     */
//    public static class Queue {
//        // 定义【广播消息队列】==>topic.global.业务.模块
//        public static final String TOPIC_GLOBAL_BILL = "topic.global.bill";
//        public static final String TOPIC_GLOBAL_PAYMENT = "topic.global.payment";
//        public static final String TOPIC_GLOBAL_FINANCE = "topic.global.finance";
//        // 定义【普通消息队列】==>topic.normal.模块.队列名
//        public static final String TOPIC_NORMAL_BILL_BILLBANK = "topic.normal.bill.billbank";
//        public static final String TOPIC_NORMAL_BILL_BILLPOOL = "topic.normal.bill.billpool";
//        // 定义【工作流消息队列】==>topic.workflow.模块
//        public static final String TOPIC_WORKFLOW_BILL = "topic.workflow.bill";
//    }
//}
