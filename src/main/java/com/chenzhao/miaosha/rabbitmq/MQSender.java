package com.chenzhao.miaosha.rabbitmq;

import com.chenzhao.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MQSender
 * @Description 消息发送者
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoShaMessage(MiaoShaMessage mm) {

        String msg= RedisService.beanToString(mm);
        log.info("sender message={}",mm);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);

    }

    public void send(Object message){
        String msg= RedisService.beanToString(message);
        log.info("sender message={}",message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg= RedisService.beanToString(message);
        log.info("sender topic  message={}",message);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,msg+"2");
    }

    public void sendFanout(Object message){
        String msg= RedisService.beanToString(message);
        log.info("sender fanout  message={}",message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }

    public void sendHeaders(Object message){
        String msg= RedisService.beanToString(message);
        log.info("sender headers  message={}",message);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("headers1","value1");
        properties.setHeader("headers2","value2");
        Message obj=new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);
    }



}
