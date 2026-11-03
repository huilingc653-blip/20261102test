package com.bytter.biz.creditRating.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XIRRCalculator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/M/d");
    private static final MathContext HIGH_PRECISION = new MathContext(50, RoundingMode.HALF_UP);
    private static final BigDecimal MAX_ITERATIONS = BigDecimal.valueOf(100);
    private static final BigDecimal NO_CONVERGENCE_VALUE = BigDecimal.valueOf(9999);
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    /**
     * Float精度版本 - 计算净现值（NPV）
     *
     * @param rate 折现率（IRR）
     * @param cashFlows 现金流列表（负值表示投资，正值表示回报）
     * @param firstDate 第一个现金流发生日期
     * @param dates 所有现金流发生日期列表
     * @param interestBase 一年中的天数（360或365）
     * @return 净现值
     */
    public static float calculateNPVFloat(float rate, List<Float> cashFlows,
                                          String firstDate, List<String> dates,
                                          int interestBase) {
        LocalDate baseDate = LocalDate.parse(firstDate, DATE_FORMATTER);
        float npv = 0.0f;

        for (int i = 0; i < cashFlows.size(); i++) {
            LocalDate currentDate = LocalDate.parse(dates.get(i), DATE_FORMATTER);
            long daysDiff = ChronoUnit.DAYS.between(baseDate, currentDate) + 1;
            float discountFactor = (float) Math.pow(1 + rate, daysDiff / (float) interestBase);
            npv += cashFlows.get(i) / discountFactor;
        }

        return npv;
    }

    /**
     * Float精度版本 - 使用二分法计算内部收益率
     *
     * @param cashFlows 现金流列表
     * @param firstDate 第一个现金流发生日期
     * @param dates 所有现金流发生日期列表
     * @param interestBase 一年中的天数
     * @param lowerBound 初始猜测下限
     * @param upperBound 初始猜测上限
     * @param epsilon 收敛精度
     * @return 内部收益率（IRR）
     */
    public static float calculateXIRRFloat(List<Float> cashFlows, String firstDate,
                                           List<String> dates, int interestBase,
                                           float lowerBound, float upperBound,
                                           float epsilon) {
        int iterations = 0;
        float a = lowerBound;
        float b = upperBound;

        while (true) {
            iterations++;
            if (iterations >= 100) {
                return 9999.0f; // 未收敛
            }

            float mid = (a + b) / 2.0f;
            float npvMid = calculateNPVFloat(mid, cashFlows, firstDate, dates, interestBase);

            if (Math.abs(a - b) < epsilon) {
                System.out.println("迭代次数: " + iterations);
                return mid; // 找到近似解
            }

            float npvA = calculateNPVFloat(a, cashFlows, firstDate, dates, interestBase);

            if (npvA * npvMid < 0) {
                b = mid;
            } else {
                a = mid;
            }
        }
    }

    /**
     * BigDecimal高精度版本 - 计算净现值（NPV）
     *
     * @param rate 折现率（IRR）
     * @param cashFlows 现金流列表
     * @param firstDate 第一个现金流发生日期
     * @param dates 所有现金流发生日期列表
     * @param interestBase 一年中的天数
     * @return 净现值
     */
    public static BigDecimal calculateNPVDecimal(BigDecimal rate, List<BigDecimal> cashFlows,
                                                 String firstDate, List<String> dates,
                                                 BigDecimal interestBase) {
        LocalDate baseDate = LocalDate.parse(firstDate, DATE_FORMATTER);
        BigDecimal npv = BigDecimal.ZERO;

        for (int i = 0; i < cashFlows.size(); i++) {
            LocalDate currentDate = LocalDate.parse(dates.get(i), DATE_FORMATTER);
            long daysDiff = ChronoUnit.DAYS.between(baseDate, currentDate) + 1;

            // 计算折现因子: (1 + rate) ^ (daysDiff / interestBase)
            BigDecimal exponent = BigDecimal.valueOf(daysDiff)
                    .divide(interestBase, HIGH_PRECISION);

            //BigDecimal discountFactor = ONE.add(rate)
                    //.pow(exponent, HIGH_PRECISION); // 注意：BigDecimal.pow() 不支持小数指数

            // 由于 BigDecimal.pow() 不支持小数指数，这里使用数学近似方法
            // 实际应用中建议使用更专业的数学库，如 Apache Commons Math
            BigDecimal discountFactor = BigDecimal.valueOf(
                    Math.pow(ONE.add(rate).doubleValue(),
                            exponent.doubleValue()));

            BigDecimal discountedCashFlow = cashFlows.get(i)
                    .divide(discountFactor, HIGH_PRECISION);

            npv = npv.add(discountedCashFlow, HIGH_PRECISION);
        }

        return npv;
    }

    /**
     * BigDecimal高精度版本 - 使用二分法计算内部收益率
     *
     * @param cashFlows 现金流列表
     * @param firstDate 第一个现金流发生日期
     * @param dates 所有现金流发生日期列表
     * @param interestBase 一年中的天数
     * @param lowerBound 初始猜测下限
     * @param upperBound 初始猜测上限
     * @param epsilon 收敛精度
     * @return 内部收益率（IRR）
     */
    public static BigDecimal calculateXIRRDecimal(List<BigDecimal> cashFlows, String firstDate,
                                                  List<String> dates, BigDecimal interestBase,
                                                  BigDecimal lowerBound, BigDecimal upperBound,
                                                  BigDecimal epsilon) {
        int iterations = 0;
        BigDecimal a = lowerBound;
        BigDecimal b = upperBound;

        while (true) {
            iterations++;
            if (iterations >= 100) {
                return NO_CONVERGENCE_VALUE;
            }

            BigDecimal mid = a.add(b).divide(TWO, HIGH_PRECISION);
            BigDecimal npvMid = calculateNPVDecimal(mid, cashFlows, firstDate, dates, interestBase);

            if (a.subtract(b).abs().compareTo(epsilon) < 0) {
                System.out.println("迭代次数: " + iterations);
                return mid;
            }

            BigDecimal npvA = calculateNPVDecimal(a, cashFlows, firstDate, dates, interestBase);

            if (npvA.multiply(npvMid).compareTo(ZERO) < 0) {
                b = mid;
            } else {
                a = mid;
            }
        }
    }

    /**
     * Float精度版本 - XIRR计算服务
     *
     * @param requestData 请求数据
     * @return 计算结果
     */
    public static Map<String, Object> xirrServiceFloat(Map<String, Object> requestData) {
        // 提取参数
        @SuppressWarnings("unchecked")
        List<Float> ncf = (List<Float>) requestData.get("ncf");
        String t1 = (String) requestData.get("t1");
        @SuppressWarnings("unchecked")
        List<String> ti = (List<String>) requestData.get("ti");
        int interestBase = ((Number) requestData.get("interestBase")).intValue();
        float ksi = ((Number) requestData.get("ksi")).floatValue();
        float a = ((Number) requestData.get("a")).floatValue();
        float b = ((Number) requestData.get("b")).floatValue();

        // 计算XIRR
        float xirr = calculateXIRRFloat(ncf, t1, ti, interestBase, a, b, ksi);

        // 构建结果
        requestData.put("xirr", xirr);
        Map<String, Object> result = new HashMap<>();
        result.put("data", requestData);

        return result;
    }

    /**
     * BigDecimal高精度版本 - XIRR计算服务
     *
     * @param requestData 请求数据
     * @return 计算结果
     */
    public static Map<String, Object> xirrServiceDecimal(Map<String, Object> requestData) {
        // 提取参数
        @SuppressWarnings("unchecked")
        List<Object> ncfRaw = (List<Object>) requestData.get("ncf");
        List<BigDecimal> ncf = new ArrayList<>();
        for (Object value : ncfRaw) {
            if (value instanceof Number) {
                ncf.add(new BigDecimal(value.toString()));
            } else {
                ncf.add(new BigDecimal(value.toString()));
            }
        }

        String t1 = (String) requestData.get("t1");

        @SuppressWarnings("unchecked")
        List<String> ti = (List<String>) requestData.get("ti");

        BigDecimal interestBase = new BigDecimal(requestData.get("interestBase").toString());
        BigDecimal ksi = new BigDecimal(requestData.get("ksi").toString());
        BigDecimal a = new BigDecimal(requestData.get("a").toString());
        BigDecimal b = new BigDecimal(requestData.get("b").toString());

        // 计算XIRR
        BigDecimal xirr = calculateXIRRDecimal(ncf, t1, ti, interestBase, a, b, ksi);

        // 构建结果
        requestData.put("xirr", xirr);
        Map<String, Object> result = new HashMap<>();
        result.put("data", requestData);

        return result;
    }

    /**
     * 将IRR转换为XIRR的示例方法（对应Python中的s_irr_to_xirr）
     */
    public static void irrToXIRRExample() {
        // 现金流和日期
        BigDecimal[] cashFlows = {
                new BigDecimal("19900"),
                new BigDecimal("-33.33"),
                new BigDecimal("-4010.56"),
                new BigDecimal("-68.89"),
                new BigDecimal("-68.89"),
                new BigDecimal("-66.67"),
                new BigDecimal("-4010.56"),
                new BigDecimal("-51.66"),
                new BigDecimal("-50"),
                new BigDecimal("-51.67"),
                new BigDecimal("-4010.56"),
                new BigDecimal("-34.44"),
                new BigDecimal("-31.11"),
                new BigDecimal("-34.44"),
                new BigDecimal("-4010"),
                new BigDecimal("-16.67"),
                new BigDecimal("-17.22"),
                new BigDecimal("-4010")
        };

        String[] dates = {
                "2024/7/9", "2024/7/21", "2024/8/9", "2024/8/21", "2024/9/21",
                "2024/10/21", "2024/11/9", "2024/11/21", "2024/12/21", "2025/1/21",
                "2025/2/9", "2025/2/21", "2025/3/21", "2025/4/21", "2025/5/9",
                "2025/5/21", "2025/6/21", "2025/7/9"
        };

        // 将数据转换为列表
        List<BigDecimal> cashFlowList = new ArrayList<>();
        for (BigDecimal cf : cashFlows) {
            cashFlowList.add(cf);
        }

        List<String> dateList = new ArrayList<>();
        for (String date : dates) {
            dateList.add(date);
        }

        // 设置参数
        String firstDate = dates[0];
        BigDecimal interestBase = new BigDecimal("360");
        BigDecimal ksi = new BigDecimal("0.0000001");
        BigDecimal lowerBound = new BigDecimal("-0.5");
        BigDecimal upperBound = new BigDecimal("0.5");

        // 计算XIRR
        BigDecimal xirr = calculateXIRRDecimal(cashFlowList, firstDate, dateList,
                interestBase, lowerBound, upperBound, ksi);

        System.out.println("The XIRR is approximately: " + xirr);

        // 如果XIRR计算失败，可以尝试使用IRR近似
        if (xirr.compareTo(NO_CONVERGENCE_VALUE) == 0) {
            System.out.println("XIRR计算未收敛，尝试使用等价定期现金流计算IRR近似值");

            LocalDate baseDate = LocalDate.parse(firstDate, DATE_FORMATTER);
            BigDecimal[] equivalentCashFlows = new BigDecimal[cashFlows.length];

            // 计算等价定期现金流
            for (int i = 0; i < cashFlows.length; i++) {
                LocalDate currentDate = LocalDate.parse(dates[i], DATE_FORMATTER);
                long daysDiff = ChronoUnit.DAYS.between(baseDate, currentDate) + 1;

                BigDecimal exponent = BigDecimal.valueOf(daysDiff)
                        .divide(interestBase, HIGH_PRECISION);

                //BigDecimal discountFactor = ONE.add(new BigDecimal("0.01"))
                        //.pow(exponent, HIGH_PRECISION);

                // 使用近似计算
                BigDecimal discountFactor = BigDecimal.valueOf(
                        Math.pow(1.01, exponent.doubleValue()));

                equivalentCashFlows[i] = cashFlows[i]
                        .divide(discountFactor, HIGH_PRECISION);
            }

            // 这里需要实现IRR计算函数
            // 由于Java没有内置IRR函数，可以使用Apache Commons Math或实现自己的IRR算法
            System.out.println("等价定期现金流已计算，需要实现IRR函数进一步计算");
        }
    }

    /**
     * 使用示例
     */
    public static void main(String[] args) {
        // 示例1：Float精度计算
        System.out.println("=== Float精度计算示例 ===");
        Map<String, Object> floatRequest = new HashMap<>();

        List<Float> cashFlows = new ArrayList<>();
        cashFlows.add(19900.0f);
        cashFlows.add(-33.33f);
        cashFlows.add(-4010.56f);
        // ... 添加更多现金流

        List<String> dates = new ArrayList<>();
        dates.add("2024/7/9");
        dates.add("2024/7/21");
        dates.add("2024/8/9");
        // ... 添加更多日期

        floatRequest.put("ncf", cashFlows);
        floatRequest.put("t1", "2024/7/9");
        floatRequest.put("ti", dates);
        floatRequest.put("interestBase", 360);
        floatRequest.put("ksi", 0.0000001f);
        floatRequest.put("a", -0.5f);
        floatRequest.put("b", 0.5f);

        Map<String, Object> floatResult = xirrServiceFloat(floatRequest);
        System.out.println("Float精度XIRR结果: " + floatResult.get("data"));

        // 示例2：BigDecimal高精度计算
        System.out.println("\n=== BigDecimal高精度计算示例 ===");
        Map<String, Object> decimalRequest = new HashMap<>();

        List<BigDecimal> decimalCashFlows = new ArrayList<>();
        decimalCashFlows.add(new BigDecimal("19900"));
        decimalCashFlows.add(new BigDecimal("-33.33"));
        decimalCashFlows.add(new BigDecimal("-4010.56"));

        decimalRequest.put("ncf", decimalCashFlows);
        decimalRequest.put("t1", "2024/7/9");
        decimalRequest.put("ti", dates);
        decimalRequest.put("interestBase", "360");
        decimalRequest.put("ksi", "0.0000001");
        decimalRequest.put("a", "-0.5");
        decimalRequest.put("b", "0.5");

        Map<String, Object> decimalResult = xirrServiceDecimal(decimalRequest);
        System.out.println("BigDecimal精度XIRR结果: " + decimalResult.get("data"));

        // 示例3：IRR转XIRR示例
        System.out.println("\n=== IRR转XIRR示例 ===");
        irrToXIRRExample();
    }
}
