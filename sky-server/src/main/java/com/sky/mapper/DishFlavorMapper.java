package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味表
     * @param dishFlavors
     */
    public void insertBatch(List<DishFlavor> dishFlavors);
}
