package com.allen.rockettest.controller;

import com.allen.rockettest.jms.JmsConfig;
import com.allen.rockettest.jms.TransactionProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author sesshomaru
 * @date 2020/5/1 19:37
 *
 * 事务消息测试
 */
@RestController
public class TxController {

    @Autowired
    private TransactionProducer transactionProducer;

    @RequestMapping("/api/v1/tx")
    public Object tx(String tag, String otherParam) throws MQClientException {
        Message message = new Message(JmsConfig.TOPIC,tag,tag + "_key",tag.getBytes());
        //otherParam 其他自定义参数
        TransactionSendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, otherParam);

        System.out.printf("发送结果 %s, msg=%s", sendResult.getSendStatus(), sendResult.toString()+"\n");
        System.out.println(1123);
        return new HashMap<>();
    }
}
