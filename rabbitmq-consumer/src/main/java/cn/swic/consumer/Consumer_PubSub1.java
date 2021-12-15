package cn.swic.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_PubSub1 {

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
        String queue1Name = "test_fanout_queue1";
        channel.queueDeclare(queue1Name,true,false,false,null);
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
                System.out.println("body: "+new String(body));
                System.out.println("将日志信息打印出来....");
            }
        };
        //接收消息
        channel.basicConsume(queue1Name,consumer);
    }

}
