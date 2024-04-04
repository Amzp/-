package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

/**
 * ClassName: DishService
 * Package: com.sky.service
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 9:57
 * @Version 1.0
 */

public interface DishService {

    /**
     * 新增菜品及其口味
     * @param dishDTO
     */
    void addDishWithFlavors(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQueryDish(DishPageQueryDTO dishPageQueryDTO);
}
