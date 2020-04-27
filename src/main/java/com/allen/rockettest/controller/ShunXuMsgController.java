package com.allen.rockettest.controller;

import com.allen.rockettest.domain.ProducerOrder;
import com.allen.rockettest.jms.PayProducer;
import com.sun.org.apache.xpath.internal.Arg;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author sesshomaru
 * @date 2020/4/27 23:39
 *
 * 顺序消息
 *
 * 同一个订单id的消息会进入同一个队列里面去
 *
 *
 *
 */
@RestController
public class ShunXuMsgController {

    @Autowired
    private PayProducer payProducer;

    private static final String topic = "zdp_pay_test_topic2";

    @RequestMapping("/api/v2/pay_cb")
    public Object callback() throws Exception{
        List<ProducerOrder> orderList = ProducerOrder.getOrderList();
        for(int i = 0 ; i < orderList.size() ; i++){
            ProducerOrder order = orderList.get(i);
            Message message = new Message(topic, "", order.getClass() + "", order.toString().getBytes());
            SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
                // list 就是队列   message 消息  o 附带参数
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    //o 就是 order.getOrderId() 获得订单号将将 消息中有着相同订单号的消息放到同一个队列下
                    Long id = (Long) o;
                    long index = id % list.size();
                    System.out.println("去往queue" + index);
                    //这里控制去发送到哪个队列
                    return list.get((int) index);
                }
                //同一个订单id的消息会进入同一个队列里面去
            }, order.getOrderId());

            System.out.printf("发送结果=%s,msg=%s,orderId=%s,type=%s\n",sendResult.getSendStatus(),sendResult.toString(),order.getOrderId(),order.getType());
        }

        return new HashMap<>();
    }

    /**
     * 结果
     *
     * 去往queue3
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB280000, offsetMsgId=C0A800E900002A9F0000000000090250, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=3], queueOffset=3],orderId=111,type=创建订单
     * 去往queue2
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB2D0001, offsetMsgId=C0A800E900002A9F0000000000090356, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=2], queueOffset=3],orderId=222,type=创建订单
     * 去往queue3
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB300002, offsetMsgId=C0A800E900002A9F000000000009045C, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=3], queueOffset=4],orderId=111,type=支付订单
     * 去往queue2
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB330003, offsetMsgId=C0A800E900002A9F0000000000090562, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=2], queueOffset=4],orderId=222,type=支付订单
     * 去往queue3
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB360004, offsetMsgId=C0A800E900002A9F0000000000090668, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=3], queueOffset=5],orderId=111,type=完成订单
     * 去往queue1
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB380005, offsetMsgId=C0A800E900002A9F000000000009076E, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=1], queueOffset=3],orderId=333,type=创建订单
     * 去往queue2
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB3A0006, offsetMsgId=C0A800E900002A9F0000000000090874, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=2], queueOffset=5],orderId=222,type=完成订单
     * 去往queue1
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB3C0007, offsetMsgId=C0A800E900002A9F000000000009097A, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=1], queueOffset=4],orderId=333,type=支付订单
     * 去往queue1
     * 发送结果=SEND_OK,msg=SendResult [sendStatus=SEND_OK, msgId=C0A8009F3BFC18B4AAC28B11FB3F0008, offsetMsgId=C0A800E900002A9F0000000000090A80, messageQueue=MessageQueue [topic=zdp_pay_test_topic2, brokerName=broker-a, queueId=1], queueOffset=5],orderId=333,type=完成订单
     */
}
