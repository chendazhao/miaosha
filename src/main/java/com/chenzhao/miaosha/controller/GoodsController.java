package com.chenzhao.miaosha.controller;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.domain.User;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.service.MiaoshaUserService;
import com.chenzhao.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.chenzhao.miaosha.service.MiaoshaUserService.COOKIE_NAME_TOKEN;


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
        return "goods_list";
    }


}
