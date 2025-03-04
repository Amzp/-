package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: ShoppingCartController
 * Package: com.sky.controller.user
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午3:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api("C端-购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车", notes = "添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，商品信息：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查看购物车", notes = "查看购物车")
    public Result<List<ShoppingCart>> list() {
        log.info("查看购物车");
        // 调用业务逻辑层方法，获取购物车列表
        List<ShoppingCart> shoppingCartList = shoppingCartService.showShoppingCart();
        return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车", notes = "清空购物车")
    public Result clean() {
        log.info("清空购物车...");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation(value = "删除购物车中一个商品", notes = "减少购物车商品数量")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中一个商品，商品信息：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
