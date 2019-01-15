package com.chenzhao.miaosha.redis;

/**
 * @ClassName AccessKey
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class AccessKey extends BasePrefix{

    private AccessKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

    public static AccessKey access=new AccessKey(5,"access");
}
