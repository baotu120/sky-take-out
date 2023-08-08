package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-05 20:57
 */
public interface CategoryService {
    /**
     * add category
     * @param categoryDTO
     * @return
     */
    void save(CategoryDTO categoryDTO);

    /**
     * category page query
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * del category
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * modify category
     * @param categoryDTO
     * @return
     */
    void update(CategoryDTO categoryDTO);

    /**
     * enable/disable category
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);

    /**
     * according type query category
     * @param type
     * @return
     */
    List<Category> list(Integer type);

    Category getById(Long id);
}
