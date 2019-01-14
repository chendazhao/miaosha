package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.redis.MiaoshaUserKey;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.result.CodeMsg;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.service.MiaoshaUserService;
import com.chenzhao.miaosha.util.UUIDUtil;
import com.chenzhao.miaosha.util.ValidatorUtil;
import com.chenzhao.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @ClassName LoginController
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Controller
@RequestMapping("/login/")
@Slf4j
public class LoginController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;


    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response,@Valid LoginVo loginVo){
        log.info(loginVo.toString());
        //参数校验
//        String passInput=loginVo.getPassword();
//        String mobile=loginVo.getMobile();
//        if (StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录功能
        //原来返回的是boolean类型的值，现在为了测试方便，将返回类型改为string类型，也就是token
          //  miaoshaUserService.login(response,loginVo);
        String token=miaoshaUserService.login(response,loginVo);

        return Result.success(token);
    }
}
