package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;


    /**
     * 营业额统计接口
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 统计指定时间内 每天 已完成订单 金额之和

        // 计算日期列表
        List<LocalDate> localDateList = new ArrayList<>();
        while (begin.isBefore(end)) {
            localDateList.add(begin);
            begin = begin.plusDays(1);
        }
        localDateList.add(begin);
        String dateListString = StringUtil.join(",", localDateList).replace("[", "")
                .replace("]", "");

        // 根据日期列表的每个日期 查询当天总金额
        List<Double> amountList = new ArrayList<>();
        // 遍历 localDateList
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double amount = reportMapper.getAmountByDate(beginTime,endTime);
            amount = amount == null ? 0.0 : amount;
            amountList.add(amount);
        }
        String amountListString = StringUtil.join(",", amountList).replace("[", "")
                .replace("]", "");

        return TurnoverReportVO.builder().dateList(dateListString).turnoverList(amountListString).build();
    }


    /**
     * 用户统计接口
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        // 日期列表（保持不变）
        List<LocalDate> localDateList = new ArrayList<>();
        while (begin.isBefore(end)) {
            localDateList.add(begin);
            begin = begin.plusDays(1);
        }
        localDateList.add(begin);
        String dateListString = String.join(",", localDateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.toList()));

        // 每日总用户量（保持不变）
        List<Long> userTotalList = new ArrayList<>();
        LocalDate earlistDate = localDateList.get(0);
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(earlistDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Long userTotal = reportMapper.getUserTotalByDate(beginTime, endTime);
            userTotalList.add(userTotal != null ? userTotal : 0L);
        }
        String totalListString = String.join(",", userTotalList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));

        // 直接查询当日新增
        List<Long> newUserList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 直接查询当日新增用户数
            Long newUser = reportMapper.getNewUserCountByDate(beginTime, endTime);
            newUserList.add(newUser != null ? newUser : 0L);
        }
        String newUserListString = String.join(",", newUserList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));

        return UserReportVO.builder()
                .dateList(dateListString)
                .totalUserList(totalListString)
                .newUserList(newUserListString)
                .build();
    }


    /**
     * 订单统计接口
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        /**
         * dateList 日期列表，以逗号分隔
         * orderCompletionRate 订单完成率
         * orderCountList 订单数列表，以逗号分隔
         * totalOrderCount 订单总数
         * validOrderCount 有效订单数
         * validOrderCountList 有效订单数列表，以逗号分隔
         */
        // 日期列表
        List<LocalDate> localDateList = new ArrayList<>();
        while (begin.isBefore(end)) {
            localDateList.add(begin);
            begin = begin.plusDays(1);
        }
        localDateList.add(begin);
        String dateListString = String.join(",", localDateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.toList()));
        // 订单总数
        begin = localDateList.get(0);
        Long totalOrderCount = reportMapper.getOrderCount(begin,end);
        // 有效订单数
        Long validOrderCount = reportMapper.getVaildOrderCount(begin,end);
        // 订单完成率
        Double orderCompletionRate = (double) validOrderCount/(double) totalOrderCount;
        // 订单数列表，以逗号分隔
        List<Long> orderCountList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Long count = reportMapper.getOrderCountByDate(beginTime,endTime);
            count = count == null ? 0 : count;
            orderCountList.add(count);
        }
        String orderCountListString = String.join(",", orderCountList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        // 有效订单数列表，以逗号分隔
        List<Long> orderValidCountList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Long count = reportMapper.getValidOrderCountByDate(beginTime,endTime);
            count = count == null ? 0 : count;
            orderValidCountList.add(count);
        }
        String orderValidCountListString = String.join(",", orderValidCountList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));

        return OrderReportVO.builder()
                .dateList(dateListString)
                .orderCountList(orderCountListString)
                .validOrderCountList(orderValidCountListString)
                .orderCompletionRate(orderCompletionRate)
                .validOrderCount(Math.toIntExact(validOrderCount))
                .totalOrderCount(Math.toIntExact(totalOrderCount))
                .build();
    }


    /**
     * 查询销量排名top10接口
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> top10 = reportMapper.getTop10(beginTime, endTime);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (GoodsSalesDTO goods : top10) {
            nameList.add(goods.getName());
            numberList.add(goods.getNumber());
        }
        String nameListString = String.join(",", nameList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        String numberListString = String.join(",", numberList.stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        return SalesTop10ReportVO.builder().nameList(nameListString).numberList(numberListString).build();
    }
}
