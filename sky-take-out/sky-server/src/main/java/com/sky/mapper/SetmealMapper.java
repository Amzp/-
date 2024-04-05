package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 保存新增的套餐信息
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT) // 公共字段填充
    void insertSetmeal(Setmeal setmeal); // 需要主键（id）回显，自定义sql语句

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     */
    Page<SetmealVO> pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐表setmeal中数据
     * @param ids
     */
    void deleteSetmealByIds(List<Long> ids);

    /**
     * 更新套餐信息
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE) // 公共字段填充
    void updateSetmeal(Setmeal setmeal);
}
