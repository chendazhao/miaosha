package com.chenzhao.miaosha.access;

import com.chenzhao.miaosha.domain.MiaoshaUser;

/**
 * @ClassName UserContext
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder=new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        userHolder.set(user);
    }

    public static MiaoshaUser getUser(){
        return userHolder.get();
    }
}
