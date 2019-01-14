package com.chenzhao.miaosha.service;

import com.chenzhao.miaosha.dao.MiaoshaUserDao;
import com.chenzhao.miaosha.domain.MiaoshaUser;
import com.chenzhao.miaosha.exception.GlobalException;
import com.chenzhao.miaosha.redis.MiaoshaUserKey;
import com.chenzhao.miaosha.redis.RedisService;
import com.chenzhao.miaosha.result.CodeMsg;
import com.chenzhao.miaosha.result.Result;
import com.chenzhao.miaosha.util.MD5Util;
import com.chenzhao.miaosha.util.UUIDUtil;
import com.chenzhao.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MiaoshaUserService
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN="token";

     @Autowired
     MiaoshaUserDao miaoshaUserDao;

     @Autowired
     private RedisService redisService;

    public MiaoshaUser getById(long id){

        //取缓存
        MiaoshaUser user=redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user!=null){
            return user;
        }
        //取数据库
        user=miaoshaUserDao.getById(id);
        if (user!=null){
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return user;
    }

    public boolean updatePassword(String token,long id,String formPass){
        //取user
        MiaoshaUser user=getById(id);
        if (user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate=new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //如果更新数据库成功则需要更新/处理缓存
        redisService.delete(MiaoshaUserKey.getById,""+id);
        //token不能直接删除，否则需要重新登录，所以应该在此处做更新处理
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }

    public String login(HttpServletResponse response,LoginVo loginVo) {
        if (loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
       String mobile= loginVo.getMobile();
        String formPass=loginVo.getPassword();
        //判断手机号是否存在
      MiaoshaUser miaoshaUser=miaoshaUserDao.getById(Long.parseLong(mobile));
      if (miaoshaUser==null){
          throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
      }
      //验证密码
     String dbPass=miaoshaUser.getPassword();
      String saltDB=miaoshaUser.getSalt();
      String calcPass= MD5Util.formPassToDBPass(formPass,saltDB);
      if (!calcPass.equals(dbPass)){
          throw new GlobalException(CodeMsg.PASSWORD_ERROR);
      }
        //生成cookie
        String token=UUIDUtil.uuid();
        addCookie(response,token,miaoshaUser);
     // return true;
        return token;
    }


    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser= redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //延长有效期,重新获取token（即重新“登录”时，自动重新设置cookie，这是cookie的有效期重新开始计算
        if (miaoshaUser!=null) {
            addCookie(response,token, miaoshaUser);
        }
        return  miaoshaUser;
    }

    private void addCookie(HttpServletResponse response,String token,MiaoshaUser miaoshaUser){
        //String token= UUIDUtil.uuid();  无需每次都生成新的token，只需要更新即可
        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
        Cookie cookie=new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
