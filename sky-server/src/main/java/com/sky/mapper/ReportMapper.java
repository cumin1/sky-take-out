package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface ReportMapper {

    @Select("select sum(amount) from orders where order_time BETWEEN #{beginTime} AND #{endTime} and status = 5")
    Double getAmountByDate(LocalDateTime beginTime, LocalDateTime endTime);
}
