package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 批量插入套餐菜品关联数据
     * @param setmealDishes
     */
    void insertSetmealDishes(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id批量删除套餐菜品关联数据（setmeal_id -- id)
     * @param setmealIds（setmeal_dish表的setmeal_id，即setmeal表的id）
     */
    void deleteSetmealDishBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐id删除单个套餐菜品关联数据（setmeal_id -- id)
     * @param setmealId
     */
    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id = #{setmealId}")
    void deleteSetmealDishBySetmealId(Long setmealId);

    /**
     * 查询套餐菜品表setmeal_dish中数据
     * @param setmealId
     * @return
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
