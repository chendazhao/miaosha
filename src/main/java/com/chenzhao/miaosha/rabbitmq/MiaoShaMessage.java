package com.chenzhao.miaosha.rabbitmq;

import com.chenzhao.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @ClassName MiaoShaMessage
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Data
public class MiaoShaMessage {

    private MiaoshaUser user;

    private long goodsId;
}
