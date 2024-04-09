package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: OrderTask
 * Package: com.sky.task
 * Description: 定时任务，定时处理订单状态
 *
 * @Author Rainbow
 * @Create 2024/4/9 上午10:37
 * @Version 1.0
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的方法，每分钟执行一次
     */
    /**
     * 0 * * * ? 表示的是每小时的整数分钟时刻执行一次任务。
     * 0 秒钟字段，表示每分钟的第0秒执行。
     * 第一个* 为分钟字段，表示每一分钟。
     * 第二个* 为小时字段，表示每小时。
     * 第三个* 为天字段，表示每天。
     * 第四个* 为月字段，表示每月。
     * 第五个*? 为星期字段，? 表示不指定具体的星期几。
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // select * from orders where sataus = ? and order_time < (当前时间-15分钟)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.CANCELLED)
                        .setCancelReason("订单超时，系统自动取消订单")
                        .setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
        log.info("定时处理超时订单结束：{}", LocalDateTime.now());
    }

    /**
     * 处理一直处于派送中状态的订单的方法，每天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("定时处理派送中订单：{}", LocalDateTime.now());

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(1));
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
        log.info("定时处理派送中订单结束：{}", LocalDateTime.now());
    }
}
