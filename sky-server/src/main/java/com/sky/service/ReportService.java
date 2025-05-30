package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

    /**
     * 营业额统计接口
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);
}
