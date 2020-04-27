package com.allen.rockettest.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sesshomaru
 * @date 2020/4/27 23:30
 */
public class ProducerOrder implements Serializable {

    //订单id
    private long orderId;

    //操作(消息)类型  用于区分 是支付订单、还是下单完成 这里只是为了测试发送顺序消息使用
    private String type;


    //测试创建很多订单    消息顺序 创建订单 -----> 支付订单 ------> 完成订单
    public static List<ProducerOrder> getOrderList(){
        List<ProducerOrder> list = new ArrayList<>();
        list.add(new ProducerOrder(111L,"创建订单"));
        list.add(new ProducerOrder(222L,"创建订单"));
        list.add(new ProducerOrder(111L,"支付订单"));
        list.add(new ProducerOrder(222L,"支付订单"));
        list.add(new ProducerOrder(111L,"完成订单"));
        list.add(new ProducerOrder(333L,"创建订单"));
        list.add(new ProducerOrder(222L,"完成订单"));
        list.add(new ProducerOrder(333L,"支付订单"));
        list.add(new ProducerOrder(333L,"完成订单"));

        return list;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProducerOrder(long orderId, String type) {
        this.orderId = orderId;
        this.type = type;
    }

    public ProducerOrder() {
    }

    @Override
    public String toString() {
        return "ProducerOrder{" +
                "orderId=" + orderId +
                ", type='" + type + '\'' +
                '}';
    }
}
