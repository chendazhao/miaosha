package com.chenzhao.miaosha.service;

import com.chenzhao.miaosha.dao.GoodsDao;
import com.chenzhao.miaosha.domain.Goods;
import com.chenzhao.miaosha.domain.MiaoshaGoods;
import com.chenzhao.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GoodsService
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }


    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {

        MiaoshaGoods g=new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        g.setStockCount(goods.getStockCount());
        goodsDao.reduceStock(g);
    }
}
