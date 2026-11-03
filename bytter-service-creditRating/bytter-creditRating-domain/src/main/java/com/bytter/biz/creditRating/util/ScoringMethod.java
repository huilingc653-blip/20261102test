package com.bytter.biz.creditRating.util;

import com.bytter.biz.creditRating.vo.CrmSectionDataVO;
import org.apache.commons.lang3.SerializationUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平分方法算法
 * @author pzc  2025/03/04
 * @since 1.0.0
 */
public class ScoringMethod {

//    public static void main(String[] args) {
        //验证
//        BigDecimal values=new BigDecimal("1100");

        //页面如果是   》=1000
        //页面如果是   ==800
        //页面如果是   《= 500
        //转换结果
//        List<CrmSectionDataVO> segments=new ArrayList<>();
//        CrmSectionDataVO vo1=new CrmSectionDataVO();
//        vo1.setLeftValue(new BigDecimal(1000));
//        vo1.setRightValue(new BigDecimal(999999999));
//        vo1.setLeftSymbol("<=");
//        vo1.setRightSymbol("<");
//        vo1.setGrade(new BigDecimal(10));
//
//        CrmSectionDataVO vo2=new CrmSectionDataVO();
//        vo2.setLeftValue(new BigDecimal(800));
//        vo2.setRightValue(new BigDecimal(800));
//        vo2.setLeftSymbol("==");
//        vo2.setRightSymbol("==");
//        vo2.setGrade(new BigDecimal(8));
//
//        CrmSectionDataVO vo3=new CrmSectionDataVO();
//        vo3.setLeftValue(new BigDecimal(500));
//        vo3.setRightValue(new BigDecimal(-999));
//        vo3.setLeftSymbol(">=");
//        vo3.setRightSymbol(">");
//        vo3.setGrade(new BigDecimal(5));

//        segments.add(vo1);
//        segments.add(vo2);
//        segments.add(vo3);
//        BigDecimal reg=interpolation(values,segments);
//        System.out.println(reg);
//    }

    /**
     * 插值法
     * @param values    需要计算的值,注意事项，此处默认scores是有顺序的，传入的时候注意排序
     * @param segments  指标区间："segments": [[100, 80], [80, 60], [60, 40], [40, 20], [20, 0]]
     * @param segments    指标区间得分："scores" : [50, 40, 30, 20, 10]
     * @param segments  区间符号：[{"low": ">=", "high": ">"},{"low": ">=", "high": ">"},{"low": ">=", "high": ">"},{"low": ">=", "high": ">"},{"low": ">=", "high": ">="}]
     * @return
     */
    public  static BigDecimal interpolation(BigDecimal values, List<CrmSectionDataVO> segments){
        BigDecimal score=BigDecimal.ZERO;

        //第一次循环，看看有没有命中区间，如果命中直接返回分值
        for (int i = 0; i < segments.size(); i++) {
            CrmSectionDataVO seg = segments.get(i);
            //区间值——左值
            BigDecimal leftValue = seg.getLeftValue();
            //区间值——右值
            BigDecimal rightValue = seg.getRightValue();

            //左右符号
            String leftSymbol=seg.getLeftSymbol();
            String rightSymbol=seg.getRightSymbol();

            //满足左边同时满足右边直接返回分数
            if (compareTo(leftValue,leftSymbol,values) && compareTo(values,rightSymbol,rightValue)){
                return seg.getGrade();
            }
        }

        //如果只有一条,并且前面没有命中区间，直接给0
        if (segments.size()==1){
            return BigDecimal.ZERO;
        }

        //需要深拷贝，后面会修改集合
        List<CrmSectionDataVO> segments2 = segments.stream()
                .map(sc -> SerializationUtils.clone(sc))
                .collect(Collectors.toList());

        //如果没有命中区间，把value假设一个对象放进去，看看他的位置
        CrmSectionDataVO vo=new CrmSectionDataVO();
        vo.setLeftValue(values);
        segments2.add(vo);
        //根据左边值排个序
        segments2.sort(Comparator.comparing(CrmSectionDataVO::getLeftValue).reversed());

        for (int i = 0; i < segments2.size(); i++) {
            CrmSectionDataVO seg = segments2.get(i);
            //如果命中了。上下位置，开始计算
            if (compareTo(seg.getLeftValue(),"==",values)){
                //额度区间
                BigDecimal maxvalue=BigDecimal.ZERO;
                BigDecimal minvalue=BigDecimal.ZERO;

                //分数区间
                BigDecimal maxScore=BigDecimal.ZERO;
                BigDecimal minScore=BigDecimal.ZERO;
                if (i==0){
                    maxvalue=segments2.get(i+1).getLeftValue();
                    minvalue=segments2.get(i+2).getLeftValue();
                    maxScore=segments2.get(i+1).getGrade();
                    minScore=segments2.get(i+2).getGrade();
                }else if (i==segments2.size()-1){
                    maxvalue=segments2.get(i-2).getLeftValue();
                    minvalue=segments2.get(i-1).getLeftValue();
                    maxScore=segments2.get(i-2).getGrade();
                    minScore=segments2.get(i-1).getGrade();
                }else {
                    maxvalue=segments2.get(i-1).getLeftValue();
                    minvalue=segments2.get(i+1).getLeftValue();
                    maxScore=segments2.get(i-1).getGrade();
                    minScore=segments2.get(i+1).getGrade();
                }


                //得分=下限分数+(额度−区间下限)× (分数差/额度差)
                //区间下限
                //额度−区间下限
                BigDecimal value3=values.subtract(minvalue);
                //分数差
                BigDecimal value4= maxScore.subtract(minScore);
                //额度差
                BigDecimal value5= maxvalue.subtract(minvalue);
                //(分数差/额度差)
                BigDecimal value6= value4.divide(value5,20, RoundingMode.HALF_UP);
                //(额度−区间下限)× (分数差/额度差)
                BigDecimal value7=value3.multiply(value6);
                score=minScore.add(value7);

            }
        }
        return score;
    }


    /**
     * 如果  values1 >= value2   返回true  否则返回false
     * @param values1   第一个值
     * @param symbol    比较符号
     * @param value2    第二个值
     * @return
     */
    public static Boolean compareTo(BigDecimal values1,String symbol,BigDecimal value2){
        switch (symbol) {
            case "==":
                return values1.compareTo(value2)==0;
            case ">=":
                return values1.compareTo(value2)>=0 ;
            case ">":
                return values1.compareTo(value2)>0 ;
            case "<":
                return values1.compareTo(value2)<0 ;
            case "<=":
                return values1.compareTo(value2)<=0 ;
        }
        return false;
    }

}
