package com.bytter.biz.creditRating.vo;

import lombok.Data;

/**
 * @Title: VO
 * @Description: 指标评分对象
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class Evaluation<T> implements java.io.Serializable {

   private T data;

}
