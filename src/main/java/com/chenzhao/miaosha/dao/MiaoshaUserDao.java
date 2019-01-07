package com.chenzhao.miaosha.dao;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName MiaoshaUserDao
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@RequestParam("id") long id);
}
