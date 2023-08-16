package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-07 10:56
 */

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("add dish, {}",dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * Paging Query
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO pageQueryDTO){
        log.info("page condition query, {}", pageQueryDTO);
        PageResult pageResult = dishService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Batch Delete Dish
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("batch delete dish {}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getInfo(@PathVariable Long id){
        log.info("Query based on id for page echo, {}", id);
        DishVO dishVO = dishService.getInfo(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("update dish, {}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * enable/disable dish
     *
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/status/{status}/{id}")
    @ApiOperation("enable/disable category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, @PathVariable Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }
}
