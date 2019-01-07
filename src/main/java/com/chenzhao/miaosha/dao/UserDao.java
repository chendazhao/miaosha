package com.chenzhao.miaosha.dao;

import com.chenzhao.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName UserDao
 * @Description
 * @Author chenzhao
 * @Version 1.0
 **/
@Mapper
public interface UserDao {

    @Select("select * from user where id=#{id}")
    public User getById(@RequestParam("id") Integer id);

    @Insert("insert into user(id,name) values(#{id},#{name})")
    public int insert(User user);
}
