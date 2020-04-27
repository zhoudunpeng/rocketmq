//package com.allen.rockettest.controller;
//
//import com.allen.rockettest.jms.JmsConfig;
//import com.allen.rockettest.jms.PayProducer;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.MessageQueueSelector;
//import org.apache.rocketmq.client.producer.SendCallback;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageQueue;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author sesshomaru
// * @date 2020/4/20 21:38
// *
// * 指定队列发送消息
// *    注意点 1.投递的队列序号必须存在
// *
// */
//@RestController
//public class ZhiDingDuiLieXFMsgController {
//
//    @Autowired
//    private PayProducer payProducer;
//
//    @RequestMapping("/api/v1/pay_cb")
//    public Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//
//        Message message = new Message(JmsConfig.TOPIC,"tagA","zdp666",("Hello RocketMQ!" + text).getBytes());
//
//   /*
//     同步 指定队列发送消息
//     SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
//            @Override
//            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
//                int queueNum = Integer.parseInt(o.toString());
//                return list.get(queueNum);
//            }
//            // 0就是queueId=0 1就是queueId=1 ....... (不超过实际的queue数量)
//        }, 0);*/
//
//   //异步 指定队列发送消息
//        payProducer.getProducer().send(message, (mqs, msg, arg) -> {
//            int queueNum = Integer.parseInt(arg.toString());
//            return mqs.get(queueNum);
//            //arg可以通过入参控制，可以随机指定
//        }, 1, new SendCallback() {
//
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.printf("发送结果 %s, msg=%s", sendResult.getSendStatus(), sendResult.toString());
//            }
//
//            @Override
//            public void onException(Throwable e) {
//            e.printStackTrace();
//            }
//        });
//
//
//
//        return new HashMap<>();
//    }
//}
