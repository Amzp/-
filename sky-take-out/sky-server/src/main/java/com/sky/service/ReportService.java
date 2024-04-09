package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * ClassName: ReportService
 * Package: com.sky.service
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/9 下午3:52
 * @Version 1.0
 */
public interface ReportService {
    /**
     * 获取营业额统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return TurnoverReportVO
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
