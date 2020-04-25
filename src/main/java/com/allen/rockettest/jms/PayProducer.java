package com.allen.rockettest.jms;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * @author sesshomaru
 * @date 2020/4/20 21:28
 */
@Component
public class PayProducer {

    private DefaultMQProducer producer;

    private String producer_group = "pay_group";

    public PayProducer(){
        producer = new DefaultMQProducer(producer_group);
        //指定NameServerAddr，多个地址按 ; 分隔
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER_ADDR);

        //设置生产端重试次数 这里默认就是2 可以通过key来确定消息的唯一性(订单的id可以是key，key在发送消息的时候设定)
        producer.setRetryTimesWhenSendFailed(2);

        start();
    }

    public DefaultMQProducer getProducer(){
        return this.producer;
    }

    /**
     * 对象在使用之前必须调用一次，只能初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文，使用spring上下文监听器，进行关闭
     */
    public void shutdown(){
        this.producer.shutdown();
    }
}
