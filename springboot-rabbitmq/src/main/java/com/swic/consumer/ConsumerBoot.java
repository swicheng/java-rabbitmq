package com.swic.consumer;

import com.swic.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBoot {

    @RabbitListener(queues = {RabbitConfig.QUEUE_NAME})
    public void consume(Message message){
        System.out.println(message);
    }

}
