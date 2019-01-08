package com.chenzhao.miaosha.vo;

import com.chenzhao.miaosha.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName GoodsVo
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Data
public class GoodsVo extends Goods {

    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;


}
