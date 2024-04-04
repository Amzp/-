package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: DishFlavorMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 11:29
 * @Version 1.0
 */
@Mapper  // 该注解用于将该接口定义为mybatis的mapper接口
public interface DishFlavorMapper {
    /**
     *  批量插入菜品口味信息
     * @param flavors
     */
    void insertBatchDishFlavor(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除菜品口味信息
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteDishFlavorByDishId(Long dishId);
}
