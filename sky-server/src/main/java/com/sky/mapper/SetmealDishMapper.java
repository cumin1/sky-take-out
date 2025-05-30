package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
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


    /**
     * 批量插入套餐菜品关系表
     * @param setmealDishList
     */
    void insertBatch(List<SetmealDish> setmealDishList);


    /**
     * 删除套餐关系表中数据
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);


    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    List<SetmealDish> selectByDishId(Long id);
}
