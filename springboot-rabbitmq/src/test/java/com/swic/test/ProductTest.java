package com.swic.test;


import com.swic.RabbitApplication;
import com.swic.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
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


}
