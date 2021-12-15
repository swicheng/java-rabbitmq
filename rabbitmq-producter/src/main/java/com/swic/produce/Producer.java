package com.swic.produce;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置参数
        factory.setHost("192.168.18.130");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //3.创建连接 Connection
        Connection connection = factory.newConnection();
        //4.创建Channel
        Channel channel = connection.createChannel();
        //5.创建队列
        /**
         * 参数
         * 1. queue队列名称
         * 2. durable: 是否持久化， 当mq重启后还在
         * 3. exclusive:
         *    1.是否独占。只能有一个消费者监听这队列
         *    2.当Connection关闭时，是否删除队列
         * 4. outDelete: 是否自动删除。当没有Consumer时，自动删除掉
         * 5. arguments: 参数
         */
        //如果没有一个名字叫swic的队列，则会创建该队列，如果有则不会创建
        channel.queueDeclare("swic",true,false,false,null);
        /**
         * 1.exchange: 交换机名称。简单模式下交换机会使用默认的""
         * 2.routingKey: 路由名称
         * 3.props: 配置信息
         * 4.body: 发送消息数据
         */
        //6.发送消息
        String  body = "hello rabbitmq...";
        channel.basicPublish("","swic",null ,body.getBytes(StandardCharsets.UTF_8));


    }


}
