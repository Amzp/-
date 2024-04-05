package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result setmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        // 调用service层方法保存套餐
        setmealService.save(setmealDTO);

        return Result.success();
    }
}
