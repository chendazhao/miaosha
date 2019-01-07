package com.chenzhao.miaosha.redis;

/**
 * @ClassName MiaoshaUserKey
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class MiaoshaUserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE=3600*24*2;
    private String prefix ;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super( expireSeconds,prefix);
        this.prefix=prefix;
    }

    public static MiaoshaUserKey token=new MiaoshaUserKey(TOKEN_EXPIRE,"token");
    public static MiaoshaUserKey getByName=new MiaoshaUserKey(TOKEN_EXPIRE,"name");
    public MiaoshaUserKey withExpire(int seconds) {
        return new MiaoshaUserKey(seconds, prefix);
    }
}
