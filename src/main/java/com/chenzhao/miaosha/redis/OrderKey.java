package com.chenzhao.miaosha.redis;



/**
 * @ClassName OrderKey
 * @Description TODO
 * @Author chenzhao
 * @Date 2019/1/5 16:20
 * @Version 1.0
 **/
public class OrderKey extends BasePrefix{

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
