package com.chenzhao.miaosha.config;

import com.chenzhao.miaosha.access.UserContext;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserArgumentResolver
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    MiaoshaUserService userService;

    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz==MiaoshaUser.class;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getUser();
    }

//    @Autowired
//    private MiaoshaUserService miaoshaUserService;
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        Class<?> clazz=parameter.getParameterType();
//        return clazz== MiaoshaUser.class;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request=webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response=webRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramToken=request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
//        String cookieToken=getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return null;
//        }
//
//        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        return miaoshaUserService.getByToken(response,token);
//    }
//
//    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
//        Cookie[] cookies=request.getCookies();
//        if(cookies==null||cookies.length<=0){
//            return null;
//        }
//        for (Cookie cookie:cookies){
//            if (cookie.getName().equals(cookieNameToken));
//            return cookie.getValue();
//        }
//        return null;
//    }
}
