package com.bytter.biz.foreign.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 计算交割期限（Delivery Deadline）：交易日期到结算日期的间隔天数，扣除周六周日
 */
public class DeliveryDeadlineCalculatorUtil {

    // 日期格式化器（与 JS 代码的 'yyyy-MM-dd' 格式一致）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 核心计算方法：对应 JS 的 changeDeliveryDeadline 函数
     * @param tranDate 交易日期（格式：yyyy-MM-dd）
     * @param settleDate 结算日期（格式：yyyy-MM-dd）
     * @return 交割期限（自然间隔天数 - 周末天数）
     */
    public static int calculateDeliveryDeadline(String tranDate, String settleDate) {
        // 1. 参数校验（避免空指针和非法日期）
        validateParams(tranDate, settleDate);

        // 2. 解析日期字符串为 LocalDate（Java 8 日期类，线程安全）
        LocalDate beginDate = LocalDate.parse(tranDate, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(settleDate, DATE_FORMATTER);

        // 3. 校验：交易日期不能晚于结算日期（与 JS 逻辑一致，避免负数结果）
        if (beginDate.isAfter(endDate)) {
            throw new IllegalArgumentException("交易日期（" + tranDate + "）不能晚于结算日期（" + settleDate + "）");
        }

        // 4. 计算自然间隔天数（含首尾日期，对应 JS 的 getBetweenDay）
        long naturalDays = ChronoUnit.DAYS.between(beginDate, endDate);

        // 5. 计算间隔内的周末天数（周六 + 周日）
        long weekendDays = calculateWeekendDays(beginDate, endDate);

        // 6. 最终交割期限 = 自然天数 - 周末天数
        return (int) (naturalDays - weekendDays);
    }

    /**
     * 计算两个日期之间的周末天数（周六 + 周日）
     * @param beginDate 开始日期（含）
     * @param endDate 结束日期（含）
     * @return 周末天数
     */
    private static long calculateWeekendDays(LocalDate beginDate, LocalDate endDate) {
        long weekendDays = 0;
        LocalDate current = beginDate;

        // 循环遍历从开始日期到结束日期的每一天（与 JS 的 while 循环逻辑一致）
        while (!current.isAfter(endDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            // 周六（DayOfWeek.SATURDAY）或周日（DayOfWeek.SUNDAY）计入周末
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendDays++;
            }
            // 日期加1（对应 JS 的 dateFun.addDay(begin,1)）
            current = current.plusDays(1);
        }
        return weekendDays;
    }

    /**
     * 参数校验：非空 + 日期格式合法
     */
    private static void validateParams(String tranDate, String settleDate) {
        // 校验非空
        if (Objects.isNull(tranDate) || tranDate.trim().isEmpty()) {
            throw new IllegalArgumentException("交易日期（tranDate）不能为空");
        }
        if (Objects.isNull(settleDate) || settleDate.trim().isEmpty()) {
            throw new IllegalArgumentException("结算日期（settleDate）不能为空");
        }

        // 校验日期格式（yyyy-MM-dd）
        try {
            LocalDate.parse(tranDate, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("交易日期（" + tranDate + "）格式非法，需满足 yyyy-MM-dd");
        }
        try {
            LocalDate.parse(settleDate, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("结算日期（" + settleDate + "）格式非法，需满足 yyyy-MM-dd");
        }
    }

    // ------------------------------ 测试示例 ------------------------------
    public static void main(String[] args) {
        // 测试用例1：无周末（交易日期2025-11-24，结算日期2025-11-26）
        String tranDate1 = "2025-11-24"; // 周一
        String settleDate1 = "2025-12-26"; // 周三
        int result1 = calculateDeliveryDeadline(tranDate1, settleDate1);
        System.out.println("测试用例1：" + result1); // 输出：3（3天间隔，无周末）

        // 测试用例2：含1个周末（交易日期2025-11-22，结算日期2025-11-24）
        String tranDate2 = "2025-11-22"; // 周六
        String settleDate2 = "2025-11-24"; // 周一
        int result2 = calculateDeliveryDeadline(tranDate2, settleDate2);
        System.out.println("测试用例2：" + result2); // 输出：1（3天间隔 - 2天周末 = 1）

        // 测试用例3：含完整周末（交易日期2025-11-21，结算日期2025-11-24）
        String tranDate3 = "2025-11-21"; // 周五
        String settleDate3 = "2025-11-24"; // 周一
        int result3 = calculateDeliveryDeadline(tranDate3, settleDate3);
        System.out.println("测试用例3：" + result3); // 输出：2（4天间隔 - 2天周末 = 2）

        // 测试用例4：交易日期 = 结算日期
        String tranDate4 = "2025-11-24";
        String settleDate4 = "2025-11-24";
        int result4 = calculateDeliveryDeadline(tranDate4, settleDate4);
        System.out.println("测试用例4：" + result4); // 输出：1（当天，无周末）
    }
}
