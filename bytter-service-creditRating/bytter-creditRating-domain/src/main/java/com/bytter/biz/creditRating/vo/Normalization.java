package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.util.List;

/**
 * @Title: VO
 * @Description: 归一法
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class Normalization implements java.io.Serializable {

   /** 指标名称(标识)*/
   private List indicatorName;

   /** 指标最大值*/
   private List maxValue;

   /** 指标最小值*/
   private List minValue;

   /** 设定分值*/
   private List indicatorValue;

   /** 实际值*/
   private List rawValue;

   /** 指标方向*/
   private List direction;

}
