package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "套餐相关接口")
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result addMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐: {}", setmealDTO);
        setmealService.insert(setmealDTO);
        return Result.success();
    }


    /**
     * 分页查询套餐表
     * @param queryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐表")
    public Result<PageResult> selectMealByPage(SetmealPageQueryDTO queryDTO)
    {
        log.info("分页查询套餐表: {}",queryDTO);
        PageResult pageResult = setmealService.selectByPage(queryDTO);
        return Result.success(pageResult);
    }

}
