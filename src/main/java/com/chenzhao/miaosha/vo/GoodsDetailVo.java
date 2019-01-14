package com.chenzhao.miaosha.vo;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @ClassName GoodsDetailVo
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Data
public class GoodsDetailVo {

        private int miaoshaStatus = 0;
        private int remainSeconds = 0;
        private GoodsVo goods ;
        private MiaoshaUser user;
}
