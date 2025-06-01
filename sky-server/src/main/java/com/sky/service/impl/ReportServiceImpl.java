package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;


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

    /**
     * 导出数据报表
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response) throws IOException {
        // 1.查询数据库 获取营业数据 -- 查询最近30天运营数据
        /**
         * 概览数据: 营业额、订单完成率、新增用户数、有效订单、平均客单价
         * 明细数据: 日期、营业额、有效订单、订单完成率、平均客单价、新增用户数
         */
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        LocalDateTime begin = LocalDateTime.of(dateBegin, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(dateEnd, LocalTime.MAX);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);

        // 2.将查询到的数据写入到excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        //基于模板文件创建一个新的Excel文件
        XSSFWorkbook excel = new XSSFWorkbook(in);

        //获取表格文件的Sheet页
        XSSFSheet sheet = excel.getSheet("Sheet1");
        //填充数据--时间
        sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

        //获得第4行
        XSSFRow row = sheet.getRow(3);
        row.getCell(2).setCellValue(businessDataVO.getTurnover());
        row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        row.getCell(6).setCellValue(businessDataVO.getNewUsers());

        //获得第5行
        row = sheet.getRow(4);
        row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

        //填充明细数据
        for (int i = 0; i < 30; i++) {
            LocalDate date = dateBegin.plusDays(i);
            //查询某一天的营业数据
            BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

            //获得某一行
            row = sheet.getRow(7 + i);
            row.getCell(1).setCellValue(date.toString());
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(3).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(5).setCellValue(businessData.getUnitPrice());
            row.getCell(6).setCellValue(businessData.getNewUsers());
        }

        //3. 通过输出流将Excel文件下载到客户端浏览器
        ServletOutputStream out = response.getOutputStream();
        excel.write(out);

        //关闭资源
        out.close();
        excel.close();
    }
}
