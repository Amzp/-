package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: OrderServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午9:10
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 校验参数，处理各种业务异常（地址簿为空、购物车数据为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            // 地址簿为空
            log.info("地址簿为空");
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 2. 查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            // 购物车数据为空
            log.info("购物车数据为空");
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 3. 向订单表插入1条数据，并返回该条数据的主键id
        Orders orders = new Orders()
                .setOrderTime(LocalDateTime.now())  // 订单创建时间
                .setPayStatus(Orders.UN_PAID)     // 订单支付状态，默认为未支付
                .setStatus(Orders.PENDING_PAYMENT)  // 订单状态，默认为待支付
                .setNumber(String.valueOf(System.currentTimeMillis()))  // 订单编号，采用当前时间戳生成
                .setPhone(addressBook.getPhone())    // 收货人手机号
                .setConsignee(addressBook.getConsignee())    // 收货人姓名
                .setUserId(userId);    // 用户id
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orderMapper.insert(orders);

        // 4. 向订单明细表插入多条数据，并返回插入的条数
        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail()  // 订单明细对象
                    .setOrderId(orders.getId());  // 设置当前订单明细关联的订单id
            BeanUtils.copyProperties(cart, orderDetail);  // 将购物车对象属性复制到订单明细对象
            orderDetailArrayList.add(orderDetail);  // 将订单明细对象添加到集合中
        }
        // 批量插入订单明细数据
        orderDetailMapper.insertBatch(orderDetailArrayList);

        // 清空当前用户的购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        // 返回订单提交成功的VO对象
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount()).build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        /*JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && "ORDERPAID".equals(jsonObject.getString("code"))) {
            throw new OrderBusinessException("该订单已支付");
        }*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        // 替代微信支付成功后的数据库订单状态更新，直接在这里更新了
        // 根据订单号查询当前用户的该订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(ordersPaymentDTO.getOrderNumber(), userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
//        Orders orders=new Orders();
//        orders.setId(ordersDB.getId());
//        orders.setStatus(Orders.TO_BE_CONFIRMED);
//        orders.setPayStatus(Orders.PAID);
//        orders.setCheckoutTime(LocalDateTime.now());
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED) // 订单状态，待接单
                .payStatus(Orders.PAID) // 支付状态，已支付
                .checkoutTime(LocalDateTime.now()) // 更新支付时间
                .build();
        orderMapper.update(orders);
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

//        // 通过websocker向客户端推送支付成功消息
//        Map map = new HashMap();
//        map.put("type",1); // 1. 来单提醒  2. 客户催单
//        map.put("orderId",orders.getId());
//        map.put("content","订单号：" +outTradeNo);
//
//        String jsonString = JSON.toJSONString(map);
//        webSocketServer.sendMsg(jsonString);
    }

    /**
     * 历史订单查询
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 1. 在查询之前调用PageHelper.startPage方法，传入当前页码和每页显示条数
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO orderDetail(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 取消订单
     *
     * @param id
     */
    @Override
    public void cancel(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            log.info("订单不存在");
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            log.info("订单状态错误");
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            /*weChatPayUtil.refund(
                    ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额*/
            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }
        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED)
                .setCancelReason("用户取消")
                .setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    public void repetition(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream()
                .map(x -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    // 将原订单详情里面的菜品信息重新复制到购物车对象中
                    BeanUtils.copyProperties(x, shoppingCart, "id"); // 排除id属性
                    // 设置购物车的userId和创建时间
                    shoppingCart.setUserId(userId).setCreateTime(LocalDateTime.now());
                    return shoppingCart;
                })
                .collect(Collectors.toList()); // 将流中的内容收集到一个新的列表中

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }
}
