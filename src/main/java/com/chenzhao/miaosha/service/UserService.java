package com.chenzhao.miaosha.service;

import com.chenzhao.miaosha.dao.UserDao;
import com.chenzhao.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName UserService
 * @Description
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    //@Transactional
    public Boolean tx(){
        User user1=new User();
        User user2=new User();

        user1.setId(2);
        user1.setName("孙悟空");

        user2.setId(1);
        user2.setName("陈大昭");

        userDao.insert(user1);
        userDao.insert(user2);

        return true;
    }
}
