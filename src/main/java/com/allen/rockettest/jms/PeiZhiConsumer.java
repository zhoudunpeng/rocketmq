package com.allen.rockettest.jms;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sesshomaru
 * @date 2020/4/20 22:28
 */

@Component
public class PeiZhiConsumer {

    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public PeiZhiConsumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER_ADDR);

        //负载策略算法，即消费者分配到的queue的算法，默认是取模平均算法

        //设置成广播模式，消费端重试机制将失效
        //广播模式 BROADCASTING  默认的offset(消费进度)是保存consumer端也就是程序本地 LocalFileOffsetStore
        //集群模式 CLUSTERING    默认的offset是在broker上也就是rocketMq保存  RemoteBrokerOffsetStore
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET)
        ;
        consumer.subscribe(JmsConfig.TOPIC, "*");
     // 方法一
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                try {

                  /*  System.out.printf("%s Receive New Messages: %s %n",
                            Thread.currentThread().getName(), new String(list.get(0).getBody()));*/
                    String topic = msg.getTopic();
                    String body = new String(msg.getBody(), "utf-8");
                    String tags = msg.getTags();
                    String keys = msg.getKeys();
                    System.out.println("消费成功 topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        consumer.start();
        System.out.println("consumer start.....");
    }
}
