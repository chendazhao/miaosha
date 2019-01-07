package com.chenzhao.miaosha.redis;

/**
 * @ClassName UserKey
 * @Description 设置用户模块的prefix
 * @Author chenzhao
 * @Version 1.0
 **/
public class UserKey extends BasePrefix{

    private UserKey(String prefix) {
        super( prefix);
    }

    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");




}
