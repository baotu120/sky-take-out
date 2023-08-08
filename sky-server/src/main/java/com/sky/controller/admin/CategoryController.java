package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-05 20:54
 */

@RestController
@RequestMapping("/admin/category")
//@Api(tags = "Category-related api")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * add category
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("add category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("add category：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * category page query
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("category page query")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("category page query：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * del category
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("del category")
    public Result<String> deleteById(Long id) {
        log.info("del category：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * modify category
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("modify category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * enable/disable category
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}/{id}")
    @ApiOperation("enable/disable category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, @PathVariable Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * according type query category
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("according type query category")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }


}

