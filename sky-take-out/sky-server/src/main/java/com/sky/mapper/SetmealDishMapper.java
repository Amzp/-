package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: SetmealDishMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 16:44
 * @Version 1.0
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id获取对应的套餐id
     * @param dishIds
     * @return
     */
    // select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> getSetmealIdsbyDishId(List<Long> dishIds);
}
