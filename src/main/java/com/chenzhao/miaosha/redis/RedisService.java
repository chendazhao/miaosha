package com.chenzhao.miaosha.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisService
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
@Slf4j
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个对象
     * */
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            //1.生成真正的key
            String realKey=keyPrefix.getPrefix()+key;
            String str=jedis.get(realKey);
            //log.info("str取出的值是,str={}",str);
            T t=stringToBean(str,clazz);
            return t;

        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * */
    public <T> Boolean set(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String str=beanToString(value);
            if (str==null||str.length()<=0){
                return false;
            }
            String realKey=keyPrefix.getPrefix()+key;
            int seconds =  keyPrefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = keyPrefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     * */
    public boolean delete(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String keyWillBeDeleted=keyPrefix.getPrefix()+key;
            long ret=jedis.del(keyWillBeDeleted);
            return ret>0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = keyPrefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = keyPrefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }



        private <T> String beanToString(T value) {
        if (value==null){
            return null;
        }
        Class<?> clazz=value.getClass();
        if (clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if (clazz==Long.class||clazz==long.class){
            return ""+value;
        }else if (clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String str,Class<T> clazz) {
        if (str==null||str.length()<=0||clazz==null){
            return null;
        }
        if (clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if (clazz==Long.class||clazz==long.class){
            return (T)Long.valueOf(str);
        }else if (clazz==String.class){
            return (T)str;
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }

    }

    private void returnToPool(Jedis jedis) {
        if (jedis!=null){
            jedis.close();
        }
    }

}
