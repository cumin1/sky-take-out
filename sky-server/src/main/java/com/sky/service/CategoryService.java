package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);


    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);


    /**
     * 修改分类
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);


    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);


    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> selectByCategory(Integer type);
}
