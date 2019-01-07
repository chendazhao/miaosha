package com.chenzhao.miaosha.redis;



import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Component
@ConfigurationProperties(prefix = "redis")
@Getter
@Setter
public class RedisConfig {

    private String host;
    private int port;
    private int timeout;//秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;//秒


}
