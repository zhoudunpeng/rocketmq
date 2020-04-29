//package com.allen.rockettest.jms;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.*;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author sesshomaru
// * @date 2020/4/20 22:28
// *
// * 顺序消费
// *
// * MessageListenerOrderly  单线程消费(集群模式下)
// * 并不是简单禁止并发处理，而是指每一个consumer queue加个锁，消费每个消息前，需要
// * 获得这个消息所在queue的锁，这样同一个时间，同一个queue的消息不被并发消费，但是
// * 不同的queue的消息可以并发处理
// */
//
//@Component
//public class ShunXuConsumer {
//
//    private DefaultMQPushConsumer consumer;
//
//    private String consumerGroup = "pay_shunxu_consumer_group";
//
//    public ShunXuConsumer() throws MQClientException {
//        consumer = new DefaultMQPushConsumer(consumerGroup);
//        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER_ADDR);
//
//        //设置成广播模式，消费端重试机制将失效
//        //广播模式 BROADCASTING
//        //集群模式 CLUSTERING
//        consumer.setMessageModel(MessageModel.CLUSTERING);
//
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET)
//        ;
//        consumer.subscribe("zdp_pay_test_topic2", "*");
//     // 方法一
//        consumer.registerMessageListener(new MessageListenerOrderly() {
//            @Override
//            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
//                MessageExt msg = list.get(0);
//                try {
//                    //System.out.printf("%s Receive New Messages: %s %n",
//                      //      Thread.currentThread().getName(), new String(list.get(0).getBody()));
//                    String topic = msg.getTopic();
//                    String body = new String(msg.getBody(), "utf-8");
//                    String tags = msg.getTags();
//                    String keys = msg.getKeys();
//                    System.out.println("消费结果 topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
//                    //todo 这里可以做业务 什么物流处理 修改订单状态等
//                    return ConsumeOrderlyStatus.SUCCESS;
//                }catch (Exception e) {
//                    //todo 异常时的补偿机制
//                    e.printStackTrace();
//                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
//                }
//            }
//        });
//        consumer.start();
//        System.out.println("shun xu consumer start.....");
//    }
//}
