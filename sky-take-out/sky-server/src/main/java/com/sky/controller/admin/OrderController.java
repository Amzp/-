package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        log.info("管理员端--订单搜索：{}", ordersPageQueryDTO);

        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);

        log.info("管理员端--订单搜索结果：total = {}", pageResult.getTotal());
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "各个状态的订单数量统计", notes = "各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        log.info("管理员端--各个状态的订单数量统计...");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        log.info("管理员端--各个状态的订单数量统计结果：{}", orderStatisticsVO);
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation(value = "管理员端--查询订单详情", notes = "查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("管理员端--查询订单详情：id = {}", id);
        OrderVO orderVO = orderService.orderDetail(id);

        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @ApiOperation(value = "管理员端--接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("管理员端--接单：id = {}", ordersConfirmDTO.getId());
        orderService.confirm(ordersConfirmDTO.getId());
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation(value = "管理员端--拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("管理员端--拒单：id = {}, reason = {}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason());

        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 管理员端--取消订单
     *
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("管理员端--取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("管理员端--取消订单：id = {}", ordersCancelDTO.getId());
        orderService.adminCancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 管理员端--派送订单
     *
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable("id") Long id) {
        log.info("管理员端--派送订单：id = {}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 管理员端--完成订单
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        log.info("管理员端--完成订单：id = {}", id);
        orderService.complete(id);
        return Result.success();
    }


}
