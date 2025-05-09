package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id列表查询套餐id列表
     * @param ids
     * @return
     */
    // select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> selectIdsByDishIds(List<Long> ids);
}
