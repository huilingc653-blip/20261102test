package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.util.List;

/**
 * @Title: VO
 * @Description: 平均分、合计
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class CreditLevels implements java.io.Serializable {

   /** 指标名称(标识) */
   private SubsectionDetails creditLevels;

   /** 评估对象*/
   private List<EvaluationObject> list;

}
