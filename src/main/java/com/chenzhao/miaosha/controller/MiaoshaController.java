package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.domain.MiaoshaOrder;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.domain.OrderInfo;
import com.chenzhao.miaosha.result.CodeMsg;
import com.chenzhao.miaosha.service.GoodsService;
import com.chenzhao.miaosha.service.MiaoshaService;
import com.chenzhao.miaosha.service.OrderService;
import com.chenzhao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName MiaoshaController
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Controller
@RequestMapping("/miaosha/")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser user,
                       @RequestParam("goodsId")long goodsId){

        model.addAttribute("user",user);
        if (user==null){
            return "login";
        }
        //判断库存
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goods.getGoodsStock();
        if (stock<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了,防止重复秒杀
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //1.减库存，2.下订单3.写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(user,goods);

        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";
    }
}
