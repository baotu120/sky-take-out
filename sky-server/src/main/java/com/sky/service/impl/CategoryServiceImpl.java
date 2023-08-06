package com.sky.service.impl;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-05 21:15
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void save(CategoryDTO categoryDTO) {

    }

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(CategoryDTO categoryDTO) {

    }

    @Override
    public void startOrStop(Integer status, Long id) {

    }

    @Override
    public List<Category> list(Integer type) {
        return null;
    }
}
