package com.chenzhao.miaosha.redis;

/**
 * @ClassName KeyPrefix
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
