package com.chenzhao.miaosha.rabbitmq;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName MQConfig
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";//#占位符可以匹配到多个routing_key
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";
    public static final String HEADERS_QUEUE = "headers.queue";
    public static final String MIAOSHA_QUEUE = "miaosha.queue";

    @Bean
    public Queue miaoshaQueue() {
        return new Queue("miaosha.queue", true);
    }

    /**
     * Direct模式 交换机Exchange
     **/
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
    /**
     * Topic模式 交换机Exchangec 按照routingkey分发到指定队列
     **/
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }

    /**
	 * Fanout模式 交换机Exchange 将消息分发到所有的绑定队列，无routingkey的概念
	 * */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
	 * Header模式 交换机Exchange 通过添加属性key-value匹配
	 * */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headersQueue1(){
        return new Queue(HEADERS_QUEUE,true);
    }

    @Bean
    public Binding headersBinding(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("headers1","value1");
        map.put("headers2","value2");
        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();
    }







}






















