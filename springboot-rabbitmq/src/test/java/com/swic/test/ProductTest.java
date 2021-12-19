package com.swic.test;


import com.swic.RabbitApplication;
import com.swic.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductTest {

    //1.注入RabbitTemplate
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认模式:
     *  步骤:
     *   1. 确认模式开启: ConnectionFactory中开启publisher-confirms = "true"
     *   2. 在rabbitTemplate定义ConfirmCallBack回调函数
     */

    @Test
    public void testSend(){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"boot.haha" , "boot.hello");
    }

    @Test
    public void testConfirm(){

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关配置信息
             * @param ack  exchange交换机 是否成功收到了消息。true 成功， false代表失败
             * @param cause 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("方法被执行了....");
                if (ack){ // 接收成功
                    System.out.println("接收成功...."+cause);
                }else {
                    // 接收失败
                    System.out.println("接收失败...."+cause);
                }
            }
        });
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME+"78","boot.haa" , "boot.hello");

    }


    /**
     *   回退模式: 当消息发送给Exchange后，Exchange路由到Queue失败时 才会执行ReturnCallback
     *   步骤:
     *   1.开启回退模式 publisher-returns="true"
     *   2.设置ReturnCallback
     *   3.设置Exchange处理消息的模式:
     *     1.如果消息没有路由到Queue，则丢弃消息(默认)
     *     2.如果消息没有路由到Queue，返回给消息发送方法ReturnCallBack
     *
     */
    @Test
    public void testReturn(){

        //1.设置交换处理失败消息的模式
//        rabbitTemplate.setMandatory(true);

        /**
         * message 消息对象
         * replyCode 请求码
         * replyText错误信息
         * exchange 交换机
         * routingKey 路由Key
         *
         */
        //2.设置ReturnCallback
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey)-> {

            System.out.println("message："+message);
            System.out.println("replyText:"+ replyText);
            System.out.println("replyCode："+ replyCode);
            System.out.println("exchange: "+ exchange);
            System.out.println("routingKey: "+ routingKey);

            System.out.println("return 执行...");
        });

    }


    /**
     * 过期时间
     * 1. 队列统一过期
     * 2. 消息过期
     */
    @Test
    public void testTTl(){

        for (int i = 0; i < 10; i++) {

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"boot.haa" , "boot.hello"+i);

        }
    }




    @Test
    public void testDxl(){

        for (int i = 0; i < 10; i++) {

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"boot.haa" , "boot.hello"+i);
            try {
                Thread.sleep(1000);
                System.out.println("发了一条");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
