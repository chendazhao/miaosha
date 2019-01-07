package com.chenzhao.miaosha.domain;

import lombok.Data;


import java.util.Date;

/**
 * @ClassName MiaoshaUser
 * @Description TODO
 * @Author chenzhao
 * @Date 2019/1/6 10:24
 * @Version 1.0
 **/
@Data
public class MiaoshaUser {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
