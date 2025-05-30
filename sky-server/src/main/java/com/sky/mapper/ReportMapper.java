package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface ReportMapper {

    @Select("select sum(amount) from orders where order_time BETWEEN #{beginTime} AND #{endTime} and status = 5")
    Double getAmountByDate(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("select count(*) from user where create_time BETWEEN #{beginTime} AND #{endTime}")
    Long getUserTotalByDate(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("SELECT COUNT(*) FROM user WHERE create_time >= #{beginTime} AND create_time <= #{endTime}")
    Long getNewUserCountByDate(
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime
    );
}
