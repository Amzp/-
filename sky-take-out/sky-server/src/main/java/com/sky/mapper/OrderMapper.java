package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}
