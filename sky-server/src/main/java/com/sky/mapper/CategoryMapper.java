package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {


    /**
     * 分页查询分类表
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);
}
