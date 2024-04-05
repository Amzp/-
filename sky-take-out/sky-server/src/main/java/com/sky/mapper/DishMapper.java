package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     */
    @AutoFill(value = OperationType.INSERT) // 公共字段填充
    void insertDish(Dish dish);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQueryDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据分类id查询菜品数据
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据主键id删除菜品
     * @param id
     */
    @Delete("delete from sky_take_out.dish where id = #{id}")
    void deleteDishById(Long id);

    /**
     * 根据id集合批量删除菜品
     * @param ids
     */
    void deleteDishByIds(List<Long> ids);

    /**
     * 更新菜品
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE) // 公共字段填充
    void updateDish(Dish dish);

    /**
     * 菜品起售、停售
     * @param dish
     */
    @Update("update sky_take_out.dish set " +
            "status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id}")
    @AutoFill(value = OperationType.UPDATE) // 公共字段填充（updateTime,updateUser）
    void updateSaleStatus(Dish dish);
}
