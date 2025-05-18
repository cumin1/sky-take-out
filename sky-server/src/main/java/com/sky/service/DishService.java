package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据id查找菜品
     * @param id
     * @return
     */
    DishVO selectById(Long id);


    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);


    /**
     * 启用/停售菜品
     * @param id
     * @param status
     */
    void setStatus(Long id, Integer status);


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> selectByCategoryId(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
