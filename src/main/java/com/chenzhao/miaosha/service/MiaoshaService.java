package com.chenzhao.miaosha.service;

import com.chenzhao.miaosha.dao.GoodsDao;
import com.chenzhao.miaosha.domain.Goods;
import com.chenzhao.miaosha.domain.MiaoshaOrder;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.domain.OrderInfo;
import com.chenzhao.miaosha.redis.MiaoshaKey;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName MiaoshaService
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
public class MiaoshaService {

    //不提倡下面这种做法，不该在XXService中引入YYDao，而应该引入YYService
//    @Autowired
//    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //1.减库存，2.下订单3.写入秒杀订单
        boolean success=goodsService.reduceStock(goods);
        if (success){
            //order_info  miaosha_order
            return orderService.createOrder(user,goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }



    public long getMiaoShaResult(Long userId, long goodsId) {
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        if (order!=null){
            //秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver=getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }
}
