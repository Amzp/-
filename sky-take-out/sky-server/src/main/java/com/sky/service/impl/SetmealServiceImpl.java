package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * ClassName: SetmealServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 20:19
 * @Version 1.0
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 保存新增的套餐信息
     *
     * @param setmealDTO
     */
    @Transactional // 开启事务，涉及到两个表（setmeal表和setmeal_dish表），需要事务保持一致性
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal); // 将setmealDTO中的属性值拷贝到setmeal对象中

        // 1. 向套餐表setmeal中插入1条数据
        setmealMapper.insertSetmeal(setmeal);

        // 2. 获取到刚插入的套餐的id
        Long setmealId = setmeal.getId();

        // 3. 向套餐菜品表setmeal_dish中插入多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 遍历setmealDishes集合，为每一个套餐菜品对象设置setmealId属性
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            // 批量插入套餐菜品表
            setmealDishMapper.insertSetmealDishes(setmealDishes);
        }
    }

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 使用PageHelper分页插件进行分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> result = setmealMapper.pageQuerySetmeal(setmealPageQueryDTO);

        return new PageResult(result.getTotal(), result.getResult());
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 1. 查询套餐表setmeal中数据
        Setmeal setmeal = setmealMapper.getById(id);
        // 2. 查询套餐菜品表setmeal_dish中数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        // 3. 将setmeal和setmealDishes封装到setmealDTO中
        SetmealVO setmealVO = new SetmealVO();
        if (setmeal == null){
            return null;
        }
        // 注意：在调用此方法之前，源对象(source)和目标对象(target)均不能为null。
        BeanUtils.copyProperties(setmeal, setmealVO); // 将setmeal中的属性值拷贝到setmealVO对象中
        setmealVO.setSetmealDishes(setmealDishes); // 将setmealDishes集合拷贝到setmealVO对象中

        return setmealVO;
    }

    /**
     * 根据id批量删除套餐信息
     *
     * @param ids
     */
    @Transactional // 开启事务，涉及到两个表（setmeal表和setmeal_dish表），需要事务保持一致性
    @Override
    public void deleteSetmealByIds(List<Long> ids) {
        // 判断当前套餐是否能够删除--是否存在起售中的套餐
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)){
                // 存在起售中的套餐，不能删除
                log.error("存在起售中的套餐，不能删除...");
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 1. 批量删除套餐表setmeal中数据
        setmealMapper.deleteSetmealByIds(ids);
        // 2. 批量删除套餐菜品表setmeal_dish中数据
        setmealDishMapper.deleteSetmealDishBySetmealIds(ids);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO
     */
    @Transactional // 开启事务，涉及到两个表（setmeal表和setmeal_dish表），需要事务保持一致性
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        // 1. 更新套餐表setmeal中数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal); // 将setmealDTO中的属性值拷贝到setmeal对象中
        setmealMapper.updateSetmeal(setmeal);

        // 2. 更新套餐菜品表setmeal_dish中数据
        // 2.1 先删除原有的套餐菜品信息
        setmealDishMapper.deleteSetmealDishBySetmealId(setmeal.getId());
        // 2.2 再插入新的套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 遍历setmealDishes集合，为每一个套餐菜品对象设置setmealId属性
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
            // 批量插入套餐菜品表
            setmealDishMapper.insertSetmealDishes(setmealDishes);
        }



    }
}
