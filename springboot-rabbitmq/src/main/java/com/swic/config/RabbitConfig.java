package com.swic.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String QUEUE_NAME = "boot_queue";

    //1.交换机
    @Bean
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    //2.Queue 队列
    @Bean
    public Queue bootQueue(){
        Map<String , Object> map = new HashMap<>();
        map.put("x-message-ttl",new Long(10000));
        map.put("x-dead-letter-exchange" , "del_text_exchange");
        map.put("x-dead-letter-routing-key","del_routing");


        return QueueBuilder.durable(QUEUE_NAME).withArguments(map).build();
    }

    //3.队列和交换机绑定关系 Binding
    /**
     * 1. 知道那个队列
     * 2. 知道那个交换机
     * 3. routing key
     */
    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue ,@Qualifier("bootExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

    //配置死信队列
    @Bean
    public Queue dxlQueue(){
        return QueueBuilder.durable("del_test_queue").build();
    }

    // 配置死信交换机
    @Bean
    public Exchange dxlExchange(){
        return ExchangeBuilder.topicExchange("del_text_exchange").build();
    }

    // 死信队列与交换机绑定
    @Bean
    public Binding binDxlQueueExchange(@Qualifier("dxlQueue") Queue queue ,@Qualifier("dxlExchange") Exchange exchange){
        return  BindingBuilder.bind(queue).to(exchange).with("del_routing").noargs();
    }

}
