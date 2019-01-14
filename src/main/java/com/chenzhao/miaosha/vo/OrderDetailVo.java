package com.chenzhao.miaosha.vo;

import com.chenzhao.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * @ClassName OrderDetailVo
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Data
public class OrderDetailVo {

    private GoodsVo goods;
    private OrderInfo order;
}
