package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 保存新增的套餐信息
     *
     * @param setmeal
     */
    // 公共字段填充
    @AutoFill(value = OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal); // 需要主键（id）回显，自定义sql语句

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     */
    Page<SetmealVO> pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐表setmeal中数据
     *
     * @param ids
     */
    void deleteSetmealByIds(List<Long> ids);

    /**
     * 更新套餐信息
     *
     * @param setmeal
     */
    // 公共字段填充
    @AutoFill(value = OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);

    /**
     * 查看套餐中是否有未启用的菜品
     *
     * @param id
     * @return 停售的菜品数
     */
    @Select("select count(0)" +
            "from sky_take_out.setmeal as s " +
            "         left join sky_take_out.setmeal_dish as sd on s.id = sd.setmeal_id " +
            "         left join sky_take_out.dish as d on sd.dish_id = d.id " +
            "where s.id = #{id} " +
            "  and d.status = 0 ")
    Integer countZeroRow(Long id);

    /**
     * 套餐起售、停售
     *
     * @param setmeal
     */
    @Update("update sky_take_out.setmeal set " +
            "status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id}")
    // 公共字段填充
    @AutoFill(value = OperationType.UPDATE)
    void updateSetmealStatus(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from sky_take_out.setmeal_dish sd left join sky_take_out.dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据条件统计套餐数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
