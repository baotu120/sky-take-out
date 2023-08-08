package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-07 10:58
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Transactional
    @Override
    public void save(DishDTO dishDTO) {

        //1. save dish basic info
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insert(dish);

        //2. save flavours info
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dish.getId());
        });
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * Paging Query
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO pageQueryDTO) {
        //1. set properties(page number && page size)
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());

        //2. execute query
        Page<DishVO> dishVOList = dishMapper.pageQuery(pageQueryDTO);

        //3. get total && result, then package
        long total = dishVOList.getTotal();
        List<DishVO> result = dishVOList.getResult();
        return new PageResult(total, result);
    }

    /**
     * Batch Delete Dish
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //1. judge the status of dish, can't delete when the dish on sale
        Long count = dishMapper.countEnableDishByIds(ids);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //2. delete dish and delete flavour
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getInfo(Long id) {
        DishVO dishVO = new DishVO();
        //1. query basic dish info base on id
        Dish dish = dishMapper.getById(id);

        //2.query flavour list
        List<DishFlavor> flavorList = dishFlavorMapper.getByDishId(id);

        //3. combine data
        BeanUtils.copyProperties(dish, dishVO);
        if (dishVO != null) dishVO.setFlavors(flavorList);

        return dishVO;
    }

    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        //1. update dish basic info base on id
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //2. update dish flavor info base on id (delete then add)
        //2.1 delete dish flavor info
        dishFlavorMapper.deleteByDishIds(Collections.singletonList(dishDTO.getId()));

        //2.2 add
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dish.getId());
        });
        dishFlavorMapper.insertBatch(flavors);

    }
}
