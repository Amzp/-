package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: SetmealController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 20:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "管理端-套餐管理-接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐管理-新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增套餐", notes = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result setmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        // 调用service层方法保存套餐
        setmealService.save(setmealDTO);

        return Result.success();
    }

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询套餐", notes = "分页查询套餐")
    public Result<PageResult> pageSetmeat(@ModelAttribute SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐：{}", setmealPageQueryDTO);
        // 调用service层方法查询套餐
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐", notes = "根据id查询套餐")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        log.info("根据id查询套餐：{}", id);
        // 调用service层方法查询套餐
        SetmealVO setmealVO = setmealService.getById(id);

        if (setmealVO == null) {
            return Result.error("指定id的套餐不存在");
        }
        return Result.success(setmealVO);
    }


    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除套餐", notes = "批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteSetmeal(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}", ids);
        // 调用service层方法删除套餐
        setmealService.deleteSetmealByIds(ids);

        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "修改套餐", notes = "修改套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}", setmealDTO);
        // 调用service层方法修改套餐
        setmealService.updateSetmeal(setmealDTO);

        return Result.success();
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "套餐起售、停售", notes = "套餐起售、停售")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateSetmealStatus(@PathVariable Integer status,
                                      @RequestParam Long id){
        log.info("套餐起售、停售：status={}, id={}", status, id);
        // 调用service层方法修改套餐状态
        setmealService.updateSetmealStatus(status, id);

        return Result.success();
    }
}
