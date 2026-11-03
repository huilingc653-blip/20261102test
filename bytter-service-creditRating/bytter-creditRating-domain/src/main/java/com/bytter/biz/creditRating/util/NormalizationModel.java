package com.bytter.biz.creditRating.util;

import com.bytter.biz.creditRating.vo.Evaluation;
import com.bytter.biz.creditRating.vo.Normalization;
import com.bytter.biz.creditRating.vo.Subsection;
import com.bytter.biz.creditRating.vo.SubsectionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalizationModel {

    private static final Logger logger = LoggerFactory.getLogger(NormalizationModel.class);

    public NormalizationModel() {
    }

    /**
     * 最小-最大归一化。计算每个评估对象的指标得分。
     * @param evaluation 评估数据对象
     * @return 指标得分映射
     */
    public static Map<String, BigDecimal> minMaxScaler(Evaluation<Normalization> evaluation) {
        // 设置精度和舍入模式
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);

        // 从Evaluation对象中获取Normalization数据
        Normalization normalization = evaluation.getData();

        // 获取各个列表
        List<String> indicators = normalization.getIndicatorName();
        List<Object> maxValues = normalization.getMaxValue();
        List<Object> minValues = normalization.getMinValue();
        List<Object> setScores = normalization.getIndicatorValue();
        List<Object> actualValues = normalization.getRawValue();
        List<Object> directionValues = normalization.getDirection();

        Map<String, BigDecimal> scores = new HashMap<>();

        // 验证数据长度一致性
        if (indicators == null || indicators.isEmpty()) {
            logger.warn("指标列表为空，无法进行计算");
            return scores;
        }

        int size = indicators.size();

        for (int i = 0; i < size; i++) {
            String indicator = indicators.get(i);
            BigDecimal maxValue = toBigDecimal(maxValues.get(i));
            BigDecimal minValue = toBigDecimal(minValues.get(i));
            BigDecimal setScore = toBigDecimal(setScores.get(i));
            BigDecimal actualValue = toBigDecimal(actualValues.get(i));
            int direction = toInt(directionValues.get(i));

            if (direction == 1) {
                // 正向指标
                BigDecimal numerator = actualValue.subtract(minValue);
                BigDecimal denominator = maxValue.subtract(minValue);

                if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal score = numerator.divide(denominator, mc)
                            .multiply(setScore);
                    scores.put(indicator, score);
                } else {
                    scores.put(indicator, BigDecimal.ZERO);
                }
            } else if (direction == -1) {
                // 负向指标
                BigDecimal numerator = maxValue.subtract(actualValue);
                BigDecimal denominator = maxValue.subtract(minValue);

                if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal score = numerator.divide(denominator, mc)
                            .multiply(setScore);
                    scores.put(indicator, score);
                } else {
                    scores.put(indicator, BigDecimal.ZERO);
                }
            } else {
                logger.warn("指标 {} 的方向值 {} 无效，使用正向指标处理", indicator, direction);
                // 默认按正向指标处理
                BigDecimal numerator = actualValue.subtract(minValue);
                BigDecimal denominator = maxValue.subtract(minValue);

                if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal score = numerator.divide(denominator, mc)
                            .multiply(setScore);
                    scores.put(indicator, score);
                } else {
                    scores.put(indicator, BigDecimal.ZERO);
                }
            }
        }

        logger.info("计算得到的指标得分: {}", scores);
        return scores;
    }

    /**
     * 分段（排名）计分法。计算每个评估对象的指标得分。
     * @param evaluation
     * @return 指标得分结果
     */
    public static Map<String, Object> sectionalScoring(Evaluation<Subsection> evaluation) {
        Map<String, Object> resultMap = new HashMap<>();
        if (evaluation == null || evaluation.getData() == null) {
            logger.error("Evaluation对象或数据为空");
            return resultMap;
        }

        Subsection subsection = evaluation.getData();
        if (subsection == null) {
            logger.error("Subsection数据为空");
            return resultMap;
        }

        List<String> indicators = subsection.getIndicators();
        List<Object> values = subsection.getValues();
        List<SubsectionDetails> list = subsection.getList();

        if (indicators == null || values == null || list == null) {
            logger.error("分段计分数据不完整");
            return resultMap;
        }

        if (indicators.size() != values.size() || indicators.size() != list.size()) {
            logger.error("数据长度不一致: indicators={}, values={}, list={}",
                    indicators.size(), values.size(), list.size());
            return resultMap;
        }


        List<String> resultIndicators = new ArrayList<>();
        List<BigDecimal> resultDf = new ArrayList<>();

        for (int i = 0; i < indicators.size(); i++) {
            String indicator = indicators.get(i);
            BigDecimal value = toBigDecimal(values.get(i));
            SubsectionDetails subsectionDetails = list.get(i);

            if (subsectionDetails == null) {
                logger.warn("指标 {} 的分段详情为空，跳过", indicator);
                resultIndicators.add(indicator);
                resultDf.add(new BigDecimal("-9999"));
                continue;
            }

            // 获取分段配置
            List<List<Object>> segments = subsectionDetails.getSegments();
            List<Object> scores = subsectionDetails.getScores();
            List<Map<String, String>> operatorList = subsectionDetails.getOperator();

            if (segments == null || scores == null || operatorList == null) {
                logger.warn("指标 {} 的分段配置不完整，跳过", indicator);
                resultIndicators.add(indicator);
                resultDf.add(new BigDecimal("-9999"));
                continue;
            }

            BigDecimal rdf = calculateScore(value, segments, scores, operatorList);

            resultIndicators.add(indicator);
            resultDf.add(rdf);

            logger.debug("指标 {} 计算完成: 值={}, 得分={}", indicator, value, rdf);
        }

        resultMap.put("indicators", resultIndicators);
        resultMap.put("df", resultDf);


        logger.info("分段计分完成，共处理 {} 个指标", indicators.size());
        return resultMap;
    }

    /**
     * 计算得分
     * @param value 评分分值
     * @param segments 分段上下限
     * @param scores 分段对应的得分
     * @param operatorList 分段符号列表
     * @return 返回等级对应的分数。没有对应分数返回-9999
     */
    public static BigDecimal calculateScore(BigDecimal value, List<List<Object>> segments,
                                     List<Object> scores, List<Map<String, String>> operatorList) {
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);
        EvaluateTool eva = new EvaluateTool();

        for (int i = 0; i < segments.size(); i++) {
            List<Object> segment = segments.get(i);
            BigDecimal low = toBigDecimal(segment.get(0));
            BigDecimal high = toBigDecimal(segment.get(1));

            Map<String, String> operator = operatorList.get(i);
            String lowOp = operator.get("low");
            String highOp = operator.get("high");

            if (eva.evaluateExpressionBigDecimal(low, value, high, lowOp, highOp)) {
                return toBigDecimal(scores.get(i));
            }
        }

        return new BigDecimal("-9999");
    }

    // 辅助方法：转换为BigDecimal
    private static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return new BigDecimal(obj.toString());
        }
        return new BigDecimal(obj.toString());
    }

    // 辅助方法：转换为int
    private static int toInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return Integer.parseInt(obj.toString());
    }
}
