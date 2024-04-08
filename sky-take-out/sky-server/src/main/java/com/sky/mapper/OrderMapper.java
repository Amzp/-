package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: OrderMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午9:21
 * @Version 1.0
 */
@Mapper
public interface OrderMapper {
    /**
     * 插入订单信息
     * @param orders
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from sky_take_out.orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from sky_take_out.orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 统计订单数量
     *
     * @return
     */
    @Select("""
            select sum(IF(status = 2, 1, 0)) as toBeConfirmed,
                   sum(IF(status = 3, 1, 0)) as confirmed,
                   sum(if(status = 4, 1, 0)) as deliveryInProgress
            from sky_take_out.orders
            where status in (2, 3, 4)
            """)
    OrderStatisticsVO statistics();
}
