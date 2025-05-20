package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态查询shopping_cart表
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> select(ShoppingCart shoppingCart);


    /**
     * 更新shopping_cart表
     * @param cart
     */
    void update(ShoppingCart cart);


    /**
     * 插入数据
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);
}
