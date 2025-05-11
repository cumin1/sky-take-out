package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味表
     * @param dishFlavors
     */
    public void insertBatch(List<DishFlavor> dishFlavors);

    /**
     * 根据菜品id删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id列表批量删除口味
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);


    /**
     * 根据dish_id查询口味表
     * @param dishId
     * @return
     */
    List<DishFlavor> selectByDishId(Long dishId);
}
