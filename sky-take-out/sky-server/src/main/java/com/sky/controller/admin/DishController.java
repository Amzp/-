package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * ClassName: DishController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/4 9:02
 * @Version 1.0
 */
/*@RestController是Spring框架中一个用于构建RESTful Web服务的注解。它是@Controller和@ResponseBody注解的结合体，意味着它不仅接收HTTP请求，
同时方法的响应会自动转换为JSON或XML格式返回给客户端。这样的设计简化了开发者在创建RESTful服务时的工作量，无需在每个方法上分别使用@ResponseBody注解。*/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "后台菜品管理接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品", notes = "新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO
            /*@RequestBody DishDTO dishDTO参数表示这个方法期望从请求的body中获取菜品数据，并且Spring将自动把JSON数据映射成DishDTO对象
             * 使用@RequestBody注解时，Spring会将HTTP请求的内容区中的JSON或XML数据反序列化为Java对象，因此请求的Content-Type必须为application/json或application/xml等相应的媒体类型*/) {
        log.info("新增菜品：{}", dishDTO);

        // 调用业务逻辑层方法，新增菜品及其口味
        dishService.addDishWithFlavors(dishDTO);
        // 清除redis缓存数据
        log.info("清除redis缓存数据...");
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);

        // 返回成功结果
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品", notes = "分页查询菜品")
    public Result<PageResult> pageDish(@ModelAttribute DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品：{}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageQueryDish(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 根据ids批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除菜品", notes = "删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids) { // Spring MVC 会自动将这个以逗号分隔的字符串转换为 Long 类型的列表
        log.info("批量删除菜品：{}", ids);

        // 调用业务逻辑层方法，批量删除菜品及其口味
        dishService.deleteDishBatch(ids);

        // 将所有的菜品缓存数据清理掉，所有以dish_开头的redis缓存数据都会被清理掉
        clearRedisCache("dish_*");

        return Result.success();
    }


    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品", notes = "根据id查询菜品")
    public Result<DishVO> queryDishById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        // 调用业务逻辑层方法，根据ID查询菜品及其口味
        DishVO dishVO = dishService.queryDishByIdWithFlavors(id);

        if (dishVO == null) {
            return Result.error("指定id的菜品不存在");
        }
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品", notes = "根据分类id查询菜品")
    public Result<List<Dish>> queryDishByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        // 调用业务逻辑层方法，根据分类ID查询菜品
        List<Dish> dishList = dishService.queryDishByCategoryId(categoryId);

        return Result.success(dishList);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改菜品", notes = "修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        // 调用业务逻辑层方法，修改菜品及其口味
        dishService.updateDishWithFlavors(dishDTO);

        // 清除redis缓存数据
        clearRedisCache("dish_*");
        // 返回成功结果
        return Result.success();
    }

    /**
     * 菜品起售、停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "菜品起售、停售", notes = "菜品起售、停售")
    public Result saleStatusOfDish(@PathVariable Integer status,
                                   @RequestParam Long id) {
        log.info("菜品起售、停售：status={}, id={}", status, id);
        // 调用业务逻辑层方法，修改菜品售卖状态
        dishService.updateSaleStatus(status, id);

        // 清除redis缓存数据
        clearRedisCache("dish_*");

        // 返回成功结果
        return Result.success();
    }

    /**
     * 清除redis缓存数据
     * @param pattern
     */
    private void clearRedisCache(String pattern) {
        log.info("清除所有菜品redis缓存数据...");
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
