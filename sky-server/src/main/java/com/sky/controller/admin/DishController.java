package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}",dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        clearCache(key);
        return Result.success();
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> selectByPage(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询: {}",dishPageQueryDTO);
        PageResult pageResult = dishService.selectByPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}",ids);
        dishService.deleteBatch(ids);
        // 将所有菜品缓存数据清理掉
        clearCache("dish_*");
        return Result.success();
    }

    /**
     * 根据id查找菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查找菜品")
    public Result<DishVO> selectById(@PathVariable Long id){
        log.info("根据id查找菜品: {}",id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }


    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}",dishDTO);
        dishService.updateDish(dishDTO);
        // 将所有菜品缓存数据清理掉
        clearCache("dish_*");
        return Result.success();
    }


    /**
     * 启用/停售菜品
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/停售菜品")
    public Result setStatus(@PathVariable Integer status, Long id){
        log.info("启用/停售菜品: {} {}",id,status);
        dishService.setStatus(id,status);
        // 将所有菜品缓存数据清理掉
        clearCache("dish_*");
        return Result.success();
    }


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> selectByCategoryId(Long categoryId)
    {
        log.info("根据分类id查询菜品: {}",categoryId);
        List<Dish> dishList = dishService.selectByCategoryId(categoryId);
        return Result.success(dishList);
    }

    /**
     * 清理缓存的方法
     * @param patten
     */
    private void clearCache(String patten)
    {
        Set keys = redisTemplate.keys(patten);
        redisTemplate.delete(keys);
    }
}
