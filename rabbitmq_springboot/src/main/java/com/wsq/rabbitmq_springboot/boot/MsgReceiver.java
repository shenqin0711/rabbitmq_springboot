package com.wsq.rabbitmq_springboot.boot;

import com.wsq.rabbitmq_springboot.boot.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *用于监听队列A
 */
@Component
@RabbitListener(queues = RabbitConfig.QUEUE_A)
public class MsgReceiver {
    public void process(String content){
        System.out.println("接收处理队列A当中的消息： " + content);
    }
}
