package cn.swic.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_WorkQueues1 {

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
        channel.queueDeclare("work_queues",true,false,false,null);
        /**
         * 1.queue: 队列名称
         * 2.autoAck：是否自动确认
         * 3.callback: 回调对象
         */
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            // 回调方法, 收到消息后，会自动执行该方法

            /**
             *回调方法, 收到消息后，会自动执行该方法
             * 1.consumerTag: 标识
             * 2.envelope：获取一些消息，交换机，路由key
             * 3.properties: 配置信息
             * 4.body:数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("5.body: "+new String(body));

            }
        };
        //接收消息
        channel.basicConsume("work_queues",consumer);



    }

}
