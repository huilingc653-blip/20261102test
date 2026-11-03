package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.util.List;

/**
 * @Title: VO
 * @Description: 分段明细
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class SubsectionDetails implements java.io.Serializable {

   /** 分段上下限(评分分值左右限) */
   private List segments;

   /** 分段对应的得分*/
   private List scores;

   /** 分段符号*/
   private List operator;

}
