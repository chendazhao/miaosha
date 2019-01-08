package com.chenzhao.miaosha.service;

import com.chenzhao.miaosha.dao.GoodsDao;
import com.chenzhao.miaosha.domain.Goods;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.domain.OrderInfo;
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

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //1.减库存，2.下订单3.写入秒杀订单
        goodsService.reduceStock(goods);

        //order_info  miaosha_order
        return orderService.createOrder(user,goods);


    }
}
