package com.chenzhao.miaosha.redis;

/**
 * @ClassName GoodsKey
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class GoodsKey extends BasePrefix{

    //做页面缓存时，有效期一般比较短
    //做页面缓存是防止服务器短时间内访问量过大
    //如果该缓存的有效期过长，则即时性将不能满足要求,比如某数据已发生变化，将容易出现与缓存中页面中的数据不一致的情况

    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList=new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail=new GoodsKey(60,"gd");

}
