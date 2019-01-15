package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.access.AccessLimit;
import com.chenzhao.miaosha.domain.MiaoshaOrder;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.domain.OrderInfo;
import com.chenzhao.miaosha.rabbitmq.MQSender;
import com.chenzhao.miaosha.rabbitmq.MiaoShaMessage;
import com.chenzhao.miaosha.redis.AccessKey;
import com.chenzhao.miaosha.redis.GoodsKey;
import com.chenzhao.miaosha.redis.MiaoshaKey;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.result.CodeMsg;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.service.GoodsService;
import com.chenzhao.miaosha.service.MiaoshaService;
import com.chenzhao.miaosha.service.OrderService;
import com.chenzhao.miaosha.util.MD5Util;
import com.chenzhao.miaosha.util.UUIDUtil;
import com.chenzhao.miaosha.vo.GoodsVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MiaoshaController
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Controller
@RequestMapping("/miaosha/")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap=new HashMap<Long,Boolean>();

    /**
     *秒杀接口优化
     * 思路：减少数据库的访问
     * 1、系统初始化，把商品库存数量加载到redis中
     * 2、收到请求后，redis预减库存，库存不足，直接返回，否则进入3
     * 3、入队缓冲，直接返回，并不是返回成功，而是返回排队中，客户端不能直接提示秒杀成功，而是启动定时器，
     * 过一段时间再去查是否成功
     * 4、出队，修改库存，修改结束标志
     **/
    @AccessLimit(seconds = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path")String path){

        model.addAttribute("user",user);
        if (user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check=miaoshaService.checkPath(user,goodsId,path);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over=localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        long stock=redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if (stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了,防止重复秒杀
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoShaMessage mm=new MiaoShaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);

        sender.sendMiaoShaMessage(mm);


        return Result.success(0);//表示排队中
    }

    //成功则直接返回orderId，否则秒杀失败，即库存不足，则返回-1，返回0则表示还在排队当中
    @RequestMapping(value = "result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId")long goodsId) {

        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result=miaoshaService.getMiaoShaResult(user.getId(),goodsId);
        return Result.success(result);
    }

    /**
     * 系统初始化
     **/
    @Override
    public void afterPropertiesSet() throws Exception {
       List<GoodsVo> goodsList= goodsService.listGoodsVo();
       if (goodsList==null){
           return;
       }
       for (GoodsVo goods:goodsList){
           //系统启动是便已将商品(库存)加载到了缓存当中
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
       }
    }

    @AccessLimit(seconds = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request,MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value = "verifyCode",defaultValue = "0")int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
//        //查询访问的次数
//        String uri=request.getRequestURI();
//        String key=uri+"_"+user.getId();
//        Integer count=redisService.get(AccessKey.access,key,Integer.class);
//        if (count==null){
//            redisService.set(AccessKey.access,key,1);
//        }else if (count<5){
//            redisService.incr(AccessKey.access,key);
//        }else {
//            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//        }

        boolean check=miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        String path=miaoshaService.createMiaoshaPath(user,goodsId);

        return Result.success(path);
    }


    @RequestMapping(value = "verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> verifyCode( MiaoshaUser user,HttpServletResponse response,
                                         @RequestParam("goodsId")long goodsId) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        try {
            BufferedImage image=miaoshaService.createVerifyCode(user,goodsId);
            OutputStream out=response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

    }



    //    @RequestMapping(value = "do_miaosha",method = RequestMethod.POST)
//    @ResponseBody
//    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user,
//                                       @RequestParam("goodsId")long goodsId){
//
//        model.addAttribute("user",user);
//        if (user==null){
//            return Result.error(CodeMsg.SESSION_ERROR);
//        }
//        //判断库存
//        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock=goods.getGoodsStock();
//        if (stock<=0){
////            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
////            return "miaosha_fail";
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//        //判断是否已经秒杀到了,防止重复秒杀
//        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
//        if (order!=null){
////            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
////            return "miaosha_fail";
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//        //1.减库存，2.下订单3.写入秒杀订单
//        OrderInfo orderInfo=miaoshaService.miaosha(user,goods);
//
////        model.addAttribute("orderInfo",orderInfo);
////        model.addAttribute("goods",goods);
//      //  return "order_detail";
//        return Result.success(orderInfo);
//    }
}
