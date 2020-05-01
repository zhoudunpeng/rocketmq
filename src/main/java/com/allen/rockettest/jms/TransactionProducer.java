package com.allen.rockettest.jms;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author sesshomaru
 * @date 2020/5/1 19:18
 */
@Component
public class TransactionProducer {

    private String producerGroup = "transaction_producer_group";

    //设置自定义线程池
    private ExecutorService executorService = new ThreadPoolExecutor(2,
            5, 100,TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(producerGroup + "-check-thread");
            return thread;
        }
    });

    //事务监听器
    TransactionListener transactionListener = new TransactionListenerImpl();

    private TransactionMQProducer producer = null;

    //构造
    public TransactionProducer(){

        producer = new TransactionMQProducer(producerGroup);

        producer.setNamesrvAddr(JmsConfig.NAME_SERVER_ADDR);

        producer.setTransactionListener(transactionListener);

        producer.setExecutorService(executorService);

        start();

    }

    public TransactionMQProducer getProducer(){
        return this.producer;
    }


    class TransactionListenerImpl implements TransactionListener{

        @Override   // 执行本地事务   key可以是订单Id、关键主键等
        public LocalTransactionState executeLocalTransaction(Message message, Object o) {
            System.out.println("=============executeLocalTransaction==========");
            String body = new String(message.getBody());
            String key = message.getKeys();
            String transactionId = message.getTransactionId();
            System.out.println("transactionId="+transactionId+", key="+key+", body="+body);
            // 执行本地事务begin todo

            // 执行本地事务end todo
            int status = Integer.parseInt(o.toString());


            if(status == 1){
                //提交事务 将 "半消息" 至为可见
                //二次确定消息，然后消费者可以消费
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            if(status == 2){
                //回滚消息,将以在broker中的 "半消息" 删除
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }

            if(status == 3){
                //broker端会进行回查消息
                return LocalTransactionState.UNKNOW;
            }
            return null;
        }

        @Override  //回查消息，要么commit要么rollback，reconsumeTime不生效。也么成功要么失败
        //只会有两种情况
        public LocalTransactionState checkLocalTransaction(MessageExt message) {
            System.out.println("=============checkLocalTransaction==========");
            String body = new String(message.getBody());
            String key = message.getKeys();
            String transactionId = message.getTransactionId();
            System.out.println("transactionId="+transactionId+", key="+key+", body="+body);

            //要么commit，要么是rollback
            //这里可以用key再确认数据库操作是否成功  key就是数据库中查询的主键where条件等
            //todo 根据key检查本地事务是否成功 ok就commit 不ok就callback
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }

    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.producer.shutdown();
    }



}
