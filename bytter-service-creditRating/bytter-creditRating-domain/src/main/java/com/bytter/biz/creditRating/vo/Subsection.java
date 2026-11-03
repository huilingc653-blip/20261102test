package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.util.List;

/**
 * @Title: VO
 * @Description: 评估操作日志
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class Subsection implements java.io.Serializable {

   /** 指标名称(标识)*/
   private List indicators;

   /** 评分分值*/
   private List values;

   /** 分段明细*/
   private List<SubsectionDetails> list;

}
