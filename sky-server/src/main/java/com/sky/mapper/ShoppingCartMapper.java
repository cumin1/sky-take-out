package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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


    /**
     * 根据用户id查找购物车
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectByUserId(Long userId);


    /**
     * 根据userId清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);


    /**
     * 删除购物车中的一个商品
     * @param id
     * @return
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
