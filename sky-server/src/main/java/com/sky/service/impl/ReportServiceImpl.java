package com.sky.service.impl;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
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
}
