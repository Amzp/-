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
}
