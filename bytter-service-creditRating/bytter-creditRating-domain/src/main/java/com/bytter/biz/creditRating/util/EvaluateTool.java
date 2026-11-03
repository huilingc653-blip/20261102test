package com.bytter.biz.creditRating.util;

import com.bytter.core.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class EvaluateTool {

    public EvaluateTool() {
    }

    /**
     * 计算等级
     * @param a 第一个数
     * @param b 第二个数
     * @param c 第三个数
     * @param operatorA 第一个符号
     * @param operatorB 第二个符号
     * @return 布尔结果
     */
    public boolean evaluateExpression(Number a, Number b, Number c, String operatorA, String operatorB) {
        Map<String, BiPredicate<Double, Double>> operators = new HashMap<>();

        operators.put(">=", (x, y) -> x >= y);
        operators.put(">", (x, y) -> x > y);
        operators.put("<=", (x, y) -> x <= y);
        operators.put("<", (x, y) -> x < y);
        operators.put("==", (x, y) -> Math.abs(x - y) < 1e-10); // 浮点数相等比较
        operators.put("!=", (x, y) -> Math.abs(x - y) >= 1e-10);

        // 转换为double类型
        double aVal = a.doubleValue();
        double bVal = b.doubleValue();
        double cVal = c.doubleValue();

        // 应用第一个运算符
        boolean resultA = operators.get(operatorA).test(aVal, bVal);

        // 应用第二个运算符
        boolean resultB = operators.get(operatorB).test(bVal, cVal);

        return resultA && resultB;
    }

    /**
     * BigDecimal版本的计算等级方法
     * @param a 第一个数
     * @param b 第二个数
     * @param c 第三个数
     * @param operatorA 第一个符号
     * @param operatorB 第二个符号
     * @return 布尔结果
     */
    public boolean evaluateExpressionBigDecimal(java.math.BigDecimal a, java.math.BigDecimal b,
                                                java.math.BigDecimal c, String operatorA, String operatorB) {
        Map<String, BiPredicate<java.math.BigDecimal, java.math.BigDecimal>> operators = new HashMap<>();

        operators.put(">=", (x, y) -> x.compareTo(y) >= 0);
        operators.put(">", (x, y) -> x.compareTo(y) > 0);
        operators.put("<=", (x, y) -> x.compareTo(y) <= 0);
        operators.put("<", (x, y) -> x.compareTo(y) < 0);
        operators.put("==", (x, y) -> x.compareTo(y) == 0);
        operators.put("!=", (x, y) -> x.compareTo(y) != 0);

        if (StringUtils.isEmpty(operatorA) || StringUtils.isEmpty(operatorB)) {
            return false;
        }

        // 应用第一个运算符
        boolean resultA = operators.get(operatorA).test(a, b);

        // 应用第二个运算符
        boolean resultB = operators.get(operatorB).test(b, c);

        return resultA && resultB;
    }

}
