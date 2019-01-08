package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.service.GoodsService;
import com.chenzhao.miaosha.service.MiaoshaUserService;
import com.chenzhao.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;




/**
 * @ClassName LoginController
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Controller
@RequestMapping("/goods/")
@Slf4j
public class GoodsController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("to_list")
    public String toList(Model model,MiaoshaUser user
//            ,HttpServletResponse response
//                         @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false)String cookieToken,
//                         @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false)String paramToken)
                                                                                                                          ){
//下面这些代码包括被注释掉的参数已被配置进UserArgumentResolver里
//        if (StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        MiaoshaUser user=miaoshaUserService.getByToken(response,token);

        model.addAttribute("MiaoshaUser",user);

        //查询商品列表
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    @RequestMapping("to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId){
        //商品id很少采用自增的，这样很容易就被别人遍历,一般采用snowflake算法
        model.addAttribute("user",user);

        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        //.getTime()转化为毫秒
        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();

        int miaoshaStatus=0;
        int remainSeconds=0;

        if (now < startAt){
            //秒杀还未开始，设置倒计时
            miaoshaStatus=0;
            remainSeconds=(int)((startAt-now)/1000);//转化为秒
        }else if (now>endAt){
        //秒杀已经结束
            miaoshaStatus=2;
            remainSeconds=-1;
        }else {
            //秒杀正在进行
            miaoshaStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }


}
