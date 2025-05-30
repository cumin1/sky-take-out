package com.sky.service.impl;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
