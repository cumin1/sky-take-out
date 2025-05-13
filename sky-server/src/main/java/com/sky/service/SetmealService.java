package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void insert(SetmealDTO setmealDTO);


    /**
     * 套餐分页查询
     * @param queryDTO
     * @return
     */
    PageResult selectByPage(SetmealPageQueryDTO queryDTO);


    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);


    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);
}
