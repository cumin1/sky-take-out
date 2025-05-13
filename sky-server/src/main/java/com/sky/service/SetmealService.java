package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

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
}
