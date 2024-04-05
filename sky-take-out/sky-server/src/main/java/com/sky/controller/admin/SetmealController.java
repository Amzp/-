package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
