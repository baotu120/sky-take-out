package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @Author: Vic
 * @Create 2023-08-07 10:55
 */
public interface DishService {
    void save(DishDTO dishDTO);

    /**
     * Paging Query
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * Batch Delete Dish
     * @param ids
     * @return
     */
    void delete(List<Long> ids);

    DishVO getInfo(Long id);

    void update(DishDTO dishDTO);
}
