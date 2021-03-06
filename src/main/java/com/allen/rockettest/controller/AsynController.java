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
// * 异步发送消息
// *
// *  使用场景大量消息需要快速处理但是并不在乎发送结果的情况
// *  对RT时间敏感度，可以支持更高的并发，回调成功触发相对应的业务，比如注册成功后通知积分系统发放优惠券
// *
// *  例如 用户注册完成后 需要发优惠券，那么就可以通过异步发送添加优惠券的消息，不需要等发送优惠券的结果就可以可以提示完成注册
// *
// */
//@RestController
//public class AsynController {
//
//    @Autowired
//    private PayProducer payProducer;
//
//    @RequestMapping("/api/v1/pay_cb")
//    public Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//
//        Message message = new Message(JmsConfig.TOPIC,"tagA","zdp666",("Hello RocketMQ!" + text).getBytes());
//
//        payProducer.getProducer().send(message, new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.printf("发送结果 %s, msg=%s", sendResult.getSendStatus(), sendResult.toString());
//            }
//
//            @Override
//            public void onException(Throwable e) {
//                e.printStackTrace();
//                //补偿机制，根据业务情况进行使用，看是否进行重试, 就是异常后重新发送消息
//
//            }
//        });
//        return new HashMap<>();
//    }
//}
