package com.chenzhao.miaosha.rabbitmq;


import com.chenzhao.miaosha.domain.MiaoshaOrder;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.service.GoodsService;
import com.chenzhao.miaosha.service.MiaoshaService;
import com.chenzhao.miaosha.service.OrderService;
import com.chenzhao.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MQReceiver
 * @Description 消息接受者
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void miaoshaReceive(String message){
        log.info("receive message={}",message);
        MiaoShaMessage mm=RedisService.stringToBean(message,MiaoShaMessage.class);
        MiaoshaUser user=mm.getUser();
        long goodsId=mm.getGoodsId();

        //判断库存
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goods.getGoodsStock();
        if (stock<=0){
            return;
        }
        //判断是否已经秒杀到了,防止重复秒杀
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){
            return;
        }
        //1.减库存，2.下订单3.写入秒杀订单
        miaoshaService.miaosha(user,goods);

    }


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
        log.info("receive message={}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message){
        log.info("receive topic queue1 message={}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message){
        log.info("receive topic queue2 message={}",message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeaders(byte[] message){
        log.info("receive headers queue message:"+new String(message));
    }



}























