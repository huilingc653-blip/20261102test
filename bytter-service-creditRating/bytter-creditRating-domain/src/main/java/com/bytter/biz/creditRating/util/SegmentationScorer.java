package com.bytter.biz.creditRating.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.biz.creditRating.vo.Evaluation;
import com.bytter.biz.creditRating.vo.CreditLevels;
import com.bytter.biz.creditRating.vo.SubsectionDetails;
import com.bytter.biz.creditRating.vo.EvaluationObject;

public class SegmentationScorer {

    /**
     * 计算得分
     * @param value 评分分值
     * @param segments 分段上下限
     * @param scores 分段对应的得分
     * @param operatorList 分段符号列表
     * @return 返回等级对应的分数。没有对应分数返回-9999
     */
    public BigDecimal calculateScore(BigDecimal value, List<List<Object>> segments,
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

    /**
     * 计算得分对应的等级
     * @param value 评分分值
     * @param segments 分段上下限
     * @param scores 分段对应的得分
     * @param operatorList 分段符号列表
     * @return 返回等级对应的等级。没有对应等级返回-9999
     */
    public static String calculateLevel(BigDecimal value, List<List<Object>> segments,
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
                return (String)scores.get(i);
            }
        }

        return "-9999";
    }

    /**
     * 计算每个指标的平均分，并按照指定格式返回结果。
     * @param data 包含指标数据的字典
     * @return 按照指定格式返回的字典，包含评分对象、总分和各个指标的数据
     */
    public Map<String, Object> calculateScoresAandT(Map<String, Object> data) {
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);

        String evaluationObject = (String) data.get("evaluationObject");
        @SuppressWarnings("unchecked")
        List<List<Object>> indicators = (List<List<Object>>) data.get("indicators");

        List<List<Object>> formattedIndicators = new ArrayList<>();
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal weightedTotalScore = BigDecimal.ZERO;

        for (List<Object> indicator : indicators) {
            String name = (String) indicator.get(0);
            BigDecimal total = toBigDecimal(indicator.get(1));
            BigDecimal count = toBigDecimal(indicator.get(2));
            BigDecimal weighted = toBigDecimal(indicator.get(3));

            BigDecimal average = BigDecimal.ZERO;
            BigDecimal weightedAverage = BigDecimal.ZERO;

            if (count.compareTo(BigDecimal.ZERO) != 0) {
                average = total.divide(count, mc);
                weightedAverage = average.multiply(weighted);
            }

            List<Object> formattedIndicator = new ArrayList<>();
            formattedIndicator.add(name);
            formattedIndicator.add(total);
            formattedIndicator.add(count);
            formattedIndicator.add(weighted);
            formattedIndicator.add(average);
            formattedIndicator.add(weightedAverage);

            formattedIndicators.add(formattedIndicator);

            totalScore = totalScore.add(average);
            weightedTotalScore = weightedTotalScore.add(weightedAverage);
        }

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> dataResult = new HashMap<>();

        dataResult.put("evaluationObject", evaluationObject);
        dataResult.put("totalScore", totalScore);
        dataResult.put("weightedTotalScore", weightedTotalScore);
        dataResult.put("indicators", formattedIndicators);

        result.put("data", dataResult);
        return result;
    }

    /**
     * 计算多个评估对象的指标平均分、指标加权平均分和总分、加权总分。
     * @param data 评估数据的字典
     * @return 包含更新后的评估数据字典，每个评估对象的指标列表中添加了平均分，并计算了总分
     */
    public Map<String, Object> calculateScoresAandTBatch(Map<String, Object> data) {
        MathContext mc = new MathContext(6, RoundingMode.HALF_UP);

        @SuppressWarnings("unchecked")
        Map<String, Object> creditLevels = (Map<String, Object>) data.get("creditLevels");
        @SuppressWarnings("unchecked")
        List<List<Object>> segments = (List<List<Object>>) creditLevels.get("segments");
        @SuppressWarnings("unchecked")
        List<Object> scores = (List<Object>) creditLevels.get("scores");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> operator = (List<Map<String, String>>) creditLevels.get("operator");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");

        for (Map<String, Object> item : list) {
            @SuppressWarnings("unchecked")
            List<List<Object>> indicators = (List<List<Object>>) item.get("indicators");

            List<List<Object>> updatedIndicators = new ArrayList<>();
            BigDecimal totalScore = BigDecimal.ZERO;
            BigDecimal weightedTotalScore = BigDecimal.ZERO;

            for (List<Object> indicator : indicators) {
                String name = (String) indicator.get(0);
                BigDecimal score = toBigDecimal(indicator.get(1));
                BigDecimal count = toBigDecimal(indicator.get(2));
                BigDecimal weighted = toBigDecimal(indicator.get(3));

                BigDecimal average = BigDecimal.ZERO;
                BigDecimal weightedAverage = BigDecimal.ZERO;

                if (count.compareTo(BigDecimal.ZERO) != 0) {
                    average = score.divide(count, mc);
                    weightedAverage = average.multiply(weighted);
                }

                List<Object> updatedIndicator = new ArrayList<>();
                updatedIndicator.add(name);
                updatedIndicator.add(score);
                updatedIndicator.add(count);
                updatedIndicator.add(weighted);
                updatedIndicator.add(average);
                updatedIndicator.add(weightedAverage);

                updatedIndicators.add(updatedIndicator);

                totalScore = totalScore.add(average);
                weightedTotalScore = weightedTotalScore.add(weightedAverage);
            }

            item.put("indicators", updatedIndicators);
            item.put("totalScore", totalScore);
            item.put("weightedTotalScore", weightedTotalScore);

            String level = calculateLevel(weightedTotalScore, segments, scores, operator);
            item.put("level", level);
        }

        return data;
    }

    /**
     * 新方法：使用Evaluation对象作为参数
     * 计算多个评估对象的指标平均分、指标加权平均分和总分、加权总分。
     * @param evaluation 评估对象
     * @return 返回更新后的Evaluation对象
     */
    public static List<Map<String, Object>> calculateScoresAandTBatch(Evaluation<CreditLevels> evaluation) {
        
        // 使用2位小数定点数而非MathContext(6, HALF_UP)（6位有效数字），
        // 避免对 8999999 这类7位整数分数做除法时被四舍五入到 9000000，
        // 与评分入库时使用的 setScale(2, RoundingMode.HALF_UP) 保持一致。
        if (evaluation == null || evaluation.getData() == null) {
            throw new IllegalArgumentException("Evaluation对象不能为空");
        }

        CreditLevels creditLevels = evaluation.getData();
        SubsectionDetails subsectionDetails = creditLevels.getCreditLevels();
        List<EvaluationObject> evaluationObjects = creditLevels.getList();

        if (subsectionDetails == null || evaluationObjects == null) {
            return null;
        }

        // 获取分段配置信息
        List<List<Object>> segments = subsectionDetails.getSegments();
        List<Object> scores = subsectionDetails.getScores();
        List<Map<String, String>> operator = subsectionDetails.getOperator();

        List<Map<String, Object>> result = new ArrayList<>();

        // 为每个评估对象计算指标
        for (EvaluationObject evaluationObject : evaluationObjects) {
            Map<String, Object> evaluationObjectMap = new HashMap<>();
            List<Object> indicators = evaluationObject.getIndicators();

            if (indicators == null || indicators.isEmpty()) {
                continue;
            }

            List<Object> updatedIndicators = new ArrayList<>();
            BigDecimal totalScore = BigDecimal.ZERO;
            BigDecimal weightedTotalScore = BigDecimal.ZERO;

            // 处理每个指标
            for (Object indicatorObj : indicators) {
                if (!(indicatorObj instanceof List)) {
                    continue;
                }

                List<Object> indicator = (List<Object>) indicatorObj;
                if (indicator.size() < 4) {
                    continue;
                }

                String name = (String) indicator.get(0);
                BigDecimal score = toBigDecimal(indicator.get(1));
                BigDecimal count = toBigDecimal(indicator.get(2));
                BigDecimal weighted = toBigDecimal(indicator.get(3));

                BigDecimal average = BigDecimal.ZERO;
                BigDecimal weightedAverage = BigDecimal.ZERO;

                if (count != null && count.compareTo(BigDecimal.ZERO) != 0) {
                    average = score.divide(count, 2, RoundingMode.HALF_UP);
                    weightedAverage = average.multiply(weighted);
                }

                weightedTotalScore = weightedTotalScore.add(weightedAverage);
            }

            // 更新评估对象的指标数据
            //evaluationObject.setIndicators(updatedIndicators);

            evaluationObjectMap.put("evaluationObject",evaluationObject.getEvaluationObject());
            evaluationObjectMap.put("weightedTotalScore",weightedTotalScore);

            // 计算并设置等级
            if (segments != null && scores != null && operator != null) {
                String level = calculateLevel(weightedTotalScore, segments, scores, operator);
                evaluationObjectMap.put("level",level);
            }
            result.add(evaluationObjectMap);

        }
        return result;
    }

    /**
     * 将Map转换为Evaluation对象
     */
    private Evaluation<CreditLevels> convertMapToEvaluation(Map<String, Object> data) {
        Evaluation<CreditLevels> evaluation = new Evaluation<>();
        CreditLevels creditLevels = new CreditLevels();
        SubsectionDetails subsectionDetails = new SubsectionDetails();

        // 转换信用等级配置
        @SuppressWarnings("unchecked")
        Map<String, Object> creditLevelsMap = (Map<String, Object>) data.get("creditLevels");

        if (creditLevelsMap != null) {
            @SuppressWarnings("unchecked")
            List<List<Object>> segments = (List<List<Object>>) creditLevelsMap.get("segments");
            @SuppressWarnings("unchecked")
            List<Object> scores = (List<Object>) creditLevelsMap.get("scores");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> operator = (List<Map<String, String>>) creditLevelsMap.get("operator");

            subsectionDetails.setSegments(segments);
            subsectionDetails.setScores(scores);
            subsectionDetails.setOperator(operator);
        }

        creditLevels.setCreditLevels(subsectionDetails);

        // 转换评估对象列表
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) data.get("list");
        List<EvaluationObject> evaluationObjects = new ArrayList<>();

        if (listMap != null) {
            for (Map<String, Object> itemMap : listMap) {
                EvaluationObject evaluationObject = new EvaluationObject();
                evaluationObject.setEvaluationObject((String) itemMap.get("evaluationObject"));

                @SuppressWarnings("unchecked")
                List<List<Object>> indicators = (List<List<Object>>) itemMap.get("indicators");
                List<Object> indicatorList = new ArrayList<>();

                if (indicators != null) {
                    indicatorList.addAll(indicators);
                }

                evaluationObject.setIndicators(indicatorList);
                evaluationObjects.add(evaluationObject);
            }
        }

        creditLevels.setList(evaluationObjects);
        evaluation.setData(creditLevels);

        return evaluation;
    }

    /**
     * 将Evaluation对象转换为Map
     */
    private Map<String, Object> convertEvaluationToMap(Evaluation<CreditLevels> evaluation) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();

        CreditLevels creditLevels = evaluation.getData();
        SubsectionDetails subsectionDetails = creditLevels.getCreditLevels();

        // 转换信用等级配置
        Map<String, Object> creditLevelsMap = new HashMap<>();
        creditLevelsMap.put("segments", subsectionDetails.getSegments());
        creditLevelsMap.put("scores", subsectionDetails.getScores());
        creditLevelsMap.put("operator", subsectionDetails.getOperator());

        dataMap.put("creditLevels", creditLevelsMap);

        // 转换评估对象列表
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<EvaluationObject> evaluationObjects = creditLevels.getList();

        for (EvaluationObject evaluationObject : evaluationObjects) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("evaluationObject", evaluationObject.getEvaluationObject());
            itemMap.put("indicators", evaluationObject.getIndicators());

            // 获取额外属性（通过反射）
            try {
                BigDecimal totalScore = (BigDecimal) evaluationObject.getClass()
                        .getMethod("getTotalScore")
                        .invoke(evaluationObject);
                BigDecimal weightedTotalScore = (BigDecimal) evaluationObject.getClass()
                        .getMethod("getWeightedTotalScore")
                        .invoke(evaluationObject);
                BigDecimal level = (BigDecimal) evaluationObject.getClass()
                        .getMethod("getLevel")
                        .invoke(evaluationObject);

                itemMap.put("totalScore", totalScore);
                itemMap.put("weightedTotalScore", weightedTotalScore);
                itemMap.put("level", level);
            } catch (Exception e) {
                // 如果没有这些方法，跳过
            }

            listMap.add(itemMap);
        }

        dataMap.put("list", listMap);
        result.put("data", dataMap);

        return result;
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
}