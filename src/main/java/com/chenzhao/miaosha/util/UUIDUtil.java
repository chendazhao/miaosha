package com.chenzhao.miaosha.util;

import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
