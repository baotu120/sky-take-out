package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: Vic
 * @Create 2023-08-05 21:07
 */
@Mapper
public interface DishMapper {
    /**
     * according category id query dish quantity
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
