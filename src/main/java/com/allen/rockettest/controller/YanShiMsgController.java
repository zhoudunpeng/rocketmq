//package com.allen.rockettest.controller;
//
//import com.allen.rockettest.jms.JmsConfig;
//import com.allen.rockettest.jms.PayProducer;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//
///**
// * @author sesshomaru
// * @date 2020/4/20 21:38
// *
// * 延时消息就是
// *              Producer将消息发送到broker上，而不希望马上被消息，所有等待设定的时间后才会被consumer消费
// *
// *  使用场景:
// *              1.通过消息触发一些定时任务，比如在某一固定时刻点向用户发送提醒消息
// *              2.消息生产和消费有时间窗口，比如在电商交易中超时未支付关闭订单的场景，在订单创建时会发送
// *              一条延迟消息。这条消息将会在30分钟以后投递给消费者，消费者收到此消息后需要判断对应的订单
// *              是否支付。如果没有支付则关闭订单，释放库存。如果完成支付则忽略。
// */
//@RestController
//public class YanShiMsgController {
//
//    @Autowired
//    private PayProducer payProducer;
//
//    @RequestMapping("/api/v1/pay_cb")
//    public Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//
//        Message message = new Message(JmsConfig.TOPIC,"tagA","zdp666",("Hello RocketMQ!" + text).getBytes());
//
//        //1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
//        message.setDelayTimeLevel(2);
//        /**
//         *使用 message.setDelayTimeLevel(xxx) //xxx是级别，1表示配置里面的1级别 1s ，2就表示2级别 5s
//         *
//         * 定时消息：目前rocketmq开源版本不支持，商业版本则有，两者使用场景类式
//         */
//
//        SendResult sendResult = payProducer.getProducer().send(message);
//        System.out.println(sendResult);
//
//        return new HashMap<>();
//    }
//}
