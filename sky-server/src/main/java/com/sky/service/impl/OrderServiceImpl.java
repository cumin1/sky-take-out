package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 处理各种异常情况 (地址簿为空、购物车为空)
        AddressBook addressBookData = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBookData == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserId(BaseContext.getCurrentId());
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);   // 设置支付状态为：未支付
        orders.setStatus(Orders.PENDING_PAYMENT);  // 设置订单状态为：待付款
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBookData.getPhone());
        orders.setConsignee(addressBookData.getConsignee());
        orders.setUserId(BaseContext.getCurrentId());
        orderMapper.insert(orders);
        // 向订单明细表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        // 如果用户下单成功后 清空这个用户的购物车
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        // 封装VO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }
}
