package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: OrderController
 * Package: com.sky.controller.user
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午9:02
 * @Version 1.0
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation(value = "用户下单", notes = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        log.info("下单成功：{}", orderSubmitVO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     */
    @GetMapping("/historyOrders")
    @ApiOperation(value = "历史订单查询", notes = "历史订单查询")
    public Result<PageResult> historyOrders(@ModelAttribute OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询：page={}, pageSize={}, status={}",
                ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize(), ordersPageQueryDTO.getStatus());
        // 调用业务层方法，查询订单列表
        PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);
        log.info("历史订单查询结果：total={}", pageResult.getTotal());
    return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable("id") Long id) {
        log.info("查询订单详情：订单id={}", id);
        OrderVO orderVO = orderService.orderDetail(id);
        log.info("订单详情：{}", orderVO);
        return Result.success();
    }
}
