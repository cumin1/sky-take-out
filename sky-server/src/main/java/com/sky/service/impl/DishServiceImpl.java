package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        // 向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 向口味表插入多条数据
        Long dishId = dish.getId();  // 获取到生成的主键值
        List<DishFlavor> flavors = dishDTO.getFlavors();  // 取出口味集合
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });  // 遍历数组 将每个dishFlavor对象的id进行赋值
            // 批量插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectByPage(dishPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否能够删除 -- 起售中的菜品不可删除
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品是否能够删除 -- 如果菜品关联了套餐不可删除
        List<Long> setmealDishIds = setmealDishMapper.selectIdsByDishIds(ids);
        if (!setmealDishIds.isEmpty() && setmealDishIds.size() > 0) { // 说明删除的菜品中有一个或多个菜品关联了套餐
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品数据和菜品对应的口味数据
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查找菜品
     * @param id
     * @return
     */
    public DishVO selectById(Long id) {
        // 根据id查询dish表
        Dish dish = dishMapper.selectById(id);
        // 根据查到的category_id查询category表 取出name
        Category category = categoryMapper.selectById(dish.getCategoryId());
        String name = category.getName();
        // 根据id查询dish_flavor表
        List<DishFlavor> dishFlavorList = dishFlavorMapper.selectByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setCategoryName(name);
        dishVO.setFlavors(dishFlavorList);
        return dishVO;
    }
}
