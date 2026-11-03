package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.math.BigDecimal;
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
public class EvaluationObject implements java.io.Serializable {

   /** 评估机构 */
   private String evaluationObject;

   /** 指标信息*/
   private List<Object> indicators;

}
