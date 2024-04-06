package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: shopController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/6 下午12:14
 * @Version 1.0
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺操作接口")
@Slf4j
public class shopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public final static String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 设置营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation(value = "设置营业状态", notes = "设置营业状态，1-营业，0-打烊")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业" : "打烊");
        redisTemplate.opsForValue().set(SHOP_STATUS, status);

        return Result.success();
    }

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "获取营业状态", notes = "获取店铺营业状态，1-营业，0-打烊")
    public Result<Integer> getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);

        log.info("获取店铺营业状态：{}", shopStatus == 1 ? "营业" : "打烊");

        return Result.success(shopStatus);
    }
}
