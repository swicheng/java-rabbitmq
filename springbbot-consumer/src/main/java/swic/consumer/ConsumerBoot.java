package swic.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBoot {

    @RabbitListener(queues = {"del_test_queue"} )
    public void consume(Message message){
        System.out.println(message);
    }

}
