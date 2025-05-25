package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细表
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);


    /**
     * 根据订单id查询订单明细表 返回一个list 因为有多个
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> selectByOrderId(Long orderId);
}
