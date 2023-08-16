package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dish (name, category_id, price, image, description, status," +
            "create_time, update_time, create_user, update_user) " +
            "VALUES (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, " +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * Batch Delete Dish
     * @param ids
     * @return
     */
    Long countEnableDishByIds(List<Long> ids);

    void deleteByIds(List<Long> ids);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);


    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);



    List<Dish> list(Dish dish);
}
