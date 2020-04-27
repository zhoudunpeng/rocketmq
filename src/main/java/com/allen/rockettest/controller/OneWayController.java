//package com.allen.rockettest.controller;
//
//import com.allen.rockettest.jms.JmsConfig;
//import com.allen.rockettest.jms.PayProducer;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.SendCallback;
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
// * 单向发送
// *
// * sendOneWay 发送消息
// * 使用场景: 主要是日志收集，适用于某些耗时非常短，但对可靠性要求不高的场景，也就是logserver，
// * 只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答
// *
// */
//@RestController
//public class OneWayController {
//
//    @Autowired
//    private PayProducer payProducer;
//
//    @RequestMapping("/api/v1/pay_cb")
//    public Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//
//        Message message = new Message(JmsConfig.TOPIC,"tagA","zdp666",("Hello RocketMQ!" + text).getBytes());
//        payProducer.getProducer().sendOneway(message);
//
//        return new HashMap<>();
//    }
//}
