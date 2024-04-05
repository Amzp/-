package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * ClassName: SetmealService
 * Package: com.sky.service
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 20:18
 * @Version 1.0
 */
public interface SetmealService {

    /**
     * 保存新增的套餐信息
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     *  根据id批量删除套餐信息
     * @param ids
     */
    void deleteSetmealByIds(List<Long> ids);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void updateSetmealStatus(Integer status, Long id);
}
