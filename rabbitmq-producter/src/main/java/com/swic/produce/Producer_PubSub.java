package com.swic.produce;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer_PubSub {

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
        /**
         * exchangeDeclare(String var1, BuiltinExchangeType var2, boolean var3, boolean var4, boolean var5, Map<String, Object> var6) throws IOException;
         * 参数:
         * 1. exchange: 交换机名称
         * 2. type: 交换机类型
         *    DIRECT("direct"): 定向
         *    FANOUT("fanout"): 扇形(广播)，发送消息到每一个与之绑定队列
         *    TOPIC(“topic”): 通配符的方式
         *    HEADERS("headers"); 参数匹配
         * 3. durable: 是否持久化
         * 4. autoDelete: 自动删除
         * 5. internal: 内部使用
         * 6. arguments: 参数
         */

        String  exchangeName = "test_fanout";
        // 5.创建交换机
        channel.exchangeDeclare(exchangeName , BuiltinExchangeType.FANOUT , true , false , false , null);
        // 6.创建队列
        String queue1Name = "test_fanout_queue1";
        String queue2Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name , true , false , false , null);
        channel.queueDeclare(queue2Name , true , false , false , null);
        // 7. 绑定队列和交换机
        /**
         * queueBind(String var1, String var2, String var3)
         * 1. queue: 队列名称
         * 2. exchange: 交换机名称
         * 3. routingKey: 路由键，绑定规则
         *   如果交换机的类型为fanout，routingKey设置为""
         */
        channel.queueBind(queue1Name , exchangeName , "");
        channel.queueBind(queue2Name , exchangeName , "");

        String body = "日志信息: 别的";
        // 8.发送消息
        channel.basicPublish(exchangeName , "" , null , body.getBytes(StandardCharsets.UTF_8));

        channel.close();
        connection.close();
    }


}
