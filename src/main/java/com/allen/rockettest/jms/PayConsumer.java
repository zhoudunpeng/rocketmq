package com.allen.rockettest.jms;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author sesshomaru
 * @date 2020/4/20 22:28
 */

@Component
public class PayConsumer {

    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public PayConsumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER_ADDR);

        //设置成广播模式，消费端重试机制将失效
        //广播模式 BROADCASTING
        //集群模式 CLUSTERING
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET)
        ;
        consumer.subscribe(JmsConfig.TOPIC, "*");
     // 方法一
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                int times = msg.getReconsumeTimes();
                System.out.println("重试次数: "+ times);
                try {

                    System.out.printf("%s Receive New Messages: %s %n",
                            Thread.currentThread().getName(), new String(list.get(0).getBody()));
                    String topic = msg.getTopic();
                    String body = new String(msg.getBody(), "utf-8");
                    String tags = msg.getTags();
                    String keys = msg.getKeys();

                    if(keys.equalsIgnoreCase("zdp666")){
                        throw new Exception();
                    }

                    System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {

                    //消费端重试次数超过2次，不再重试开始人工干预(这个重试次数可以通过实际业务上最多重试几次才完成消费来设定)
                    if(times > 2){
                        //todo 记录数据库，发短信通知开发人员或者运营人员
                        System.out.println("重试次数超过2次,记录数据库，发短信通知开发人员或者运营人员");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
        /*consumer.registerMessageListener((MessageListenerConcurrently)
                (msgs, context) -> {
                    try {
                        Message msg = msgs.get(0);
                        System.out.printf("%s Receive New Messages: %s %n",
                                Thread.currentThread().getName(), new String(msgs.get(0).getBody()));
                        String topic = msg.getTopic();
                        String body = new String(msg.getBody(), "utf-8");
                        String tags = msg.getTags();
                        String keys = msg.getKeys();
                        System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);

                        //如果消费成功告诉broker消费成功了，标记消息已经消费了可以删除
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        //消费失败，可以添加重试机制
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                });*/
        consumer.start();
        System.out.println("consumer start.....");
    }
}
