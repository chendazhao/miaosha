package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.redis.GoodsKey;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.service.GoodsService;
import com.chenzhao.miaosha.service.MiaoshaUserService;
import com.chenzhao.miaosha.vo.GoodsDetailVo;
import com.chenzhao.miaosha.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "to_list",produces = "text/html")
    @ResponseBody
    public String toList(Model model,MiaoshaUser user,HttpServletRequest request,HttpServletResponse response
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

        //从缓存中取出html页面，如果不为空，则直接返回，否则将手动用模板进行渲染
        String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //页面静态化，前后端分离，不直接返回模板渲染的页面（此为动态页面）
        //而是直接从缓存中返回html页面，然后html页面通过js/ajax与后台服务器进行数据交互(此为静态页面）
        //以上就是所谓的动静分离
      //  return "goods_list";


        //public class SpringWebContext
        //        extends WebContext 此类构造器中的final Map<String, ?> variables对应本方法中的model
        SpringWebContext ctx=new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        //为空，将手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);//如果页面不为空的话，将直接保存到缓存当中去
        }
        return html;

    }

    @RequestMapping(value = "to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(Model model, MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId,
                         HttpServletRequest request,HttpServletResponse response){
        //商品id很少采用自增的，这样很容易就被别人遍历,一般采用snowflake算法
        model.addAttribute("user",user);

        //以下称之为url缓存，因为不同的goodsId将对用不同的商品详情页
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染


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

       // return "goods_detail";
        SpringWebContext ctx=new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);

        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);//如果页面不为空的话，将直接保存到缓存当中去
        }
        return html;
    }

    @RequestMapping(value = "detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId,
                                        HttpServletRequest request, HttpServletResponse response){
        //商品id很少采用自增的，这样很容易就被别人遍历,一般采用snowflake算法
        model.addAttribute("user",user);
        //手动渲染

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

        GoodsDetailVo vo=new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);

        return Result.success(vo);
    }


}
