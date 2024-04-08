package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: OrderController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/8 下午5:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端-订单管理接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 管理端--订单搜索
     */
    @GetMapping("conditionSearch")
    @ApiOperation(value = "订单搜索", notes = "订单搜索")
    public Result<PageResult> conditionSearch(@ModelAttribute OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索：{}", ordersPageQueryDTO);

        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);

        log.info("订单搜索结果：total = {}", pageResult.getTotal());
        return Result.success(pageResult);
    }


}
