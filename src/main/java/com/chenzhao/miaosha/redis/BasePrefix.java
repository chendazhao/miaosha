package com.chenzhao.miaosha.redis;

/**
 * @ClassName BasePrefix
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix){
        //expireSeconds默认0，代表永不过期
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds=expireSeconds;
        this.prefix=prefix;
    }

    @Override
    public int expireSeconds() {
        //默认0，代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className=getClass().getSimpleName();
        return className+":"+prefix;
    }
}
