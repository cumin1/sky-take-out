package com.sky.mapper;


import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Select("select count(*) from orders where order_time BETWEEN #{begin} AND #{end}")
    Long getOrderCount(LocalDate begin, LocalDate end);

    @Select("select count(*) from orders where order_time BETWEEN #{begin} AND #{end} and status = 5")
    Long getVaildOrderCount(LocalDate begin, LocalDate end);

    @Select("select count(*) from orders where order_time BETWEEN #{beginTime} AND #{endTime}")
    Long getOrderCountByDate(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("select count(*) from orders where order_time BETWEEN #{beginTime} AND #{endTime} and status = 5")
    Long getValidOrderCountByDate(LocalDateTime beginTime, LocalDateTime endTime);

    List<GoodsSalesDTO> getTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
