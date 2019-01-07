package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.domain.User;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.redis.UserKey;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static javafx.scene.input.KeyCode.T;


/**
 * @ClassName SampleController
 * @Description
 * @Author chenzhao
 * @Version 1.0
 **/
@Controller
@RequestMapping("/demo/")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    //thymeleaf模板的使用的演示
    @RequestMapping("thymeleaf")
    public String thymeleaf(Model model){
       model.addAttribute("name","陈昭");
       return "hello";
   }

   @RequestMapping("db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user=userService.getById(1);
        return Result.success(user);
   }

    @RequestMapping("db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("redis/get")
    @ResponseBody
    public Result<User> redisGet(){
       User user= redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }
    @RequestMapping("redis/set")
    @ResponseBody
    public Result<User> redisSet(){
        User user=new User();
        user.setId(1);
        user.setName("陈大昭");
        redisService.set(UserKey.getById,""+1,user);//UserKey:id1
        User user1=redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user1);
    }
}
